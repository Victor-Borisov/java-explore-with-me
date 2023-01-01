package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.client.EventClient;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.storage.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.Status;
import ru.practicum.request.service.RequestService;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.service.UserService;
import ru.practicum.utils.PageableRequest;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final CategoryService categoryService;
    private final EventRepository repository;
    private final UserService userService;
    private final RequestService requestService;
    private final EventClient eventClient;

    private static final String DATE_TIME_STRING = "yyyy-MM-dd HH:mm:ss";

    @Override
    public List<FullEventDto> getAllByAdmin(Long[] users, String[] states, Long[] categories,
                                            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        if (users != null) {
            for (Long userId : users) {
                userService.getUserById(userId);
            }
        }
        List<State> stateList = new ArrayList<>();
        if (states != null) {
            for (String state : states) {
                State stateCorrect = State.from(state)
                        .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
                stateList.add(stateCorrect);
            }
        }
        if (categories != null) {
            for (Long categoryId : categories) {
                categoryService.getById(categoryId);
            }
        }
        List<LocalDateTime> ranges = eventDatePreparation(rangeStart, rangeEnd);

        Sort sort = Sort.sort(Event.class).by(Event::getEventDate).descending();

        List<Event> events = repository.findAllByUsersAndStatesAndCategories(users, stateList, categories,
                ranges.get(0), ranges.get(1), getPageable(from, size, sort));
        List<Long> eventIds = events.stream().map(EventMapper::toId).collect(Collectors.toList());
        Map<Long, Long> confirmedRequests = requestService.getCountConfirmedByEventIdList(eventIds);
        log.info("List of events {} is retrieved by administrator", events);
        return events.stream()
                .map((Event event) -> EventMapper.toFullDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullEventDto updateByAdmin(long eventId, AdminUpdateEventRequest eventDto) {
        Event event = getByIdAndThrow(eventId);
        eventUpdatePreparation(EventMapper.fromAdminUpdateEventRequest(eventDto), event);
        Optional.ofNullable(eventDto.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(eventDto.getRequestModeration()).ifPresent(event::setRequestModeration);
        repository.save(event);
        log.info("Event {} updated by admin", eventId);

        return EventMapper.toFullDto(event, requestService.getConfirmedRequests(eventId));
    }

    @Override
    @Transactional
    public FullEventDto publishEventAdmin(long eventId) {
        Event event = getByIdAndThrow(eventId);
        if (event.getEventDate().plusHours(1).isAfter(LocalDateTime.now()) &&
                event.getState() == State.PENDING) {
            event.setPublishedOn(LocalDateTime.now());
            event.setState(State.PUBLISHED);
            repository.save(event);
            log.info("Event {} published by admin", eventId);
        }

        return EventMapper.toFullDto(event, requestService.getConfirmedRequests(eventId));
    }

    @Override
    @Transactional
    public FullEventDto rejectEventAdmin(long eventId) {
        Event event = getByIdAndThrow(eventId);
        if (event.getState() == State.PENDING) {
            event.setState(State.CANCELED);
            repository.save(event);
            log.info("Event {} rejected by admin", eventId);
        }

        return EventMapper.toFullDto(event, requestService.getConfirmedRequests(eventId));
    }

    @Override
    public List<ShortEventDto> getAllPrivate(long userId, int from, int size) {
        List<Event> events = repository.findAllByInitiatorId(userId, getPageable(from, size, Sort.unsorted()));
        List<Long> eventIds = events.stream().map(EventMapper::toId).collect(Collectors.toList());
        Map<Long, Long> confirmedRequests = requestService.getCountConfirmedByEventIdList(eventIds);
        log.info("List of events {} retrieved", events);
        return events.stream()
                .map((Event event) -> EventMapper.toShortDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FullEventDto updatePrivate(long userId, UpdateEventRequest eventDto) {
        eventDateValidation(eventDto.getEventDate());
        Event event = getByIdAndThrow(eventDto.getEventId());
        eventUpdatePreparation(EventMapper.fromUpdateEventRequest(eventDto), event);
        if (userId != event.getInitiator().getId()) {
            throw new ValidationException("Event can be edited only by initiator");
        }
        if (event.getState() == State.PUBLISHED) {
            throw new ValidationException("Only rejected or moderation waiting events may be edited");
        }
        if (event.getState() == State.CANCELED) {
            event.setState(State.PENDING);
        }
        repository.save(event);
        log.info("Event {} updated", event);

        return EventMapper.toFullDto(event, requestService.getConfirmedRequests(event.getId()));
    }

    @Override
    @Transactional
    public FullEventDto addPrivate(long userId, NewEventDto newEventDto) {
        eventDateValidation(newEventDto.getEventDate());
        Event event = EventMapper.fromNewDto(newEventDto);
        event.setCategory(CategoryMapper.fromCategoryDto(categoryService.getById(newEventDto.getCategory())));
        event.setInitiator(UserMapper.fromUserDto(userService.getAll(List.of(userId), 0, 1).get(0)));
        repository.save(event);
        log.info("Event {} added", event);

        return EventMapper.toFullDto(event, requestService.getConfirmedRequests(event.getId()));
    }

    @Override
    public FullEventDto getByIdPrivate(long userId, long eventId) {
        Event event = repository.findByInitiatorIdAndId(userId, eventId);
        if (event == null) {
            throw new NotFoundException("Event {} not found", eventId);
        }
        log.info("Event {} retrieved", eventId);

        return EventMapper.toFullDto(event, requestService.getConfirmedRequests(eventId));
    }

    @Override
    @Transactional
    public FullEventDto cancelPrivate(long userId, long eventId) {
        Event event = getByIdAndThrow(eventId);
        if (userId != event.getInitiator().getId()) {
            throw new ValidationException("Event can be canceled only by initiator");
        }
        if (event.getState().equals(State.CANCELED) || event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Only moderation waiting event may be canceled");
        }
        event.setState(State.CANCELED);
        repository.save(event);
        log.info("Event {} canceled", eventId);

        return EventMapper.toFullDto(event, requestService.getConfirmedRequests(eventId));
    }

    @Override
    public List<ParticipationRequestDto> getEventRequestsPrivate(long userId, long eventId) {
        Event event = getByIdAndThrow(eventId);
        if (userId != event.getInitiator().getId()) {
            throw new ValidationException("Only initiator can retrieve requests on the event");
        }
        List<ParticipationRequestDto> requests = requestService.getAllByEventRequests(eventId);
        log.info("List of requests {} retrieved", requests);

        return requests;
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequestPrivate(long userId, long eventId, long requestId) {
        Event event = getByIdAndThrow(eventId);
        Request request = requestService.getByRequestId(requestId);
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return RequestMapper.toRequestDto(request);
        }
        Long confirmedRequests = requestService.getConfirmedRequests(eventId);
        if (confirmedRequests.equals((long) event.getParticipantLimit())) {
            log.info("Limit of requests on participation in {} reached", eventId);
            throw new ValidationException("Limit of requests on participation reached");
        }
        request.setStatus(Status.CONFIRMED);
        if (confirmedRequests.equals((long) event.getParticipantLimit())) {
            requestService.rejectAllRequests(eventId);
        }
        requestService.saveRequest(request);
        log.info("Request {} confirmed", requestId);

        return RequestMapper.toRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequestPrivate(long userId, long eventId, long requestId) {
        getByIdAndThrow(eventId);
        Request request = requestService.getByRequestId(requestId);
        if (request.getStatus() == Status.REJECTED || request.getStatus() == Status.CANCELED) {
            throw new ValidationException("Request already canceled or rejected");
        }
        request.setStatus(Status.REJECTED);
        requestService.saveRequest(request);
        log.info("Request {} rejected", requestId);

        return RequestMapper.toRequestDto(request);
    }

    @Override
    public Event getById(long eventId) {
        return getByIdAndThrow(eventId);
    }

    @Override
    public List<ShortEventDto> getAllPublic(String text,
                                            Long[] categories,
                                            Boolean paid,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Boolean onlyAvailable,
                                            String sortType,
                                            Integer from,
                                            Integer size,
                                            HttpServletRequest request) {
        if (categories != null) {
            for (Long categoryId : categories) {
                categoryService.getById(categoryId);
            }
        }
        Sort sort = "EVENT_DATE".equals(sortType) ?
                Sort.sort(Event.class).by(Event::getEventDate).ascending() :
                Sort.sort(Event.class).by(Event::getViews).descending();
        if (onlyAvailable == null) {
            onlyAvailable = true;
        }
        if (paid == null) {
            paid = true;
        }
        List<LocalDateTime> ranges = eventDatePreparation(rangeStart, rangeEnd);

        List<Event> events = repository.findAllByParam(
                text,
                categories,
                paid,
                ranges.get(0),
                ranges.get(1),
                onlyAvailable,
                getPageable(from / size, size, sort));
        setViews(events);
        List<Long> eventIds = events.stream().map(EventMapper::toId).collect(Collectors.toList());
        Map<Long, Long> confirmedRequests = requestService.getCountConfirmedByEventIdList(eventIds);
        log.info("List of events {} retrieved", events);

        return events.stream()
                .map((Event event) -> EventMapper.toShortDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public FullEventDto getByIdPublic(long eventId) {
        Event event = repository.findByIdAndStateLike(eventId, State.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Event {} not found", eventId);
        }
        setViews(List.of(event));
        log.info("Event {} retrieved", eventId);
        return EventMapper.toFullDto(event, requestService.getConfirmedRequests(eventId));
    }

    @Override
    public Set<Event> getAllByEvents(Set<Long> events) {
        return repository.findAllByEvents(events);
    }

    private void setViews(List<Event> events) {
        events.forEach(event -> {
            List<ViewStatsDto> views = eventClient.getHits(event.getCreatedOn(), LocalDateTime.now(),
                            new String[]{"/events/" + event.getId()}, false)
                    .getBody();
            if (views != null && views.size() > 0) {
                event.setViews(views.get(0).getHits());
            }
        });
    }

    private Event getByIdAndThrow(long eventId) {
        return repository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event {} not found", eventId));
    }

    private void eventDateValidation(String eventDate) {
        String[] lines = eventDate.split(" ");
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(lines[0]), LocalTime.parse(lines[1]));
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Event start time can not be earlier than now plus 2 hours");
        }
    }

    private Pageable getPageable(int from, int size, Sort sort) {
        return new PageableRequest(from, size, sort);
    }

    private void eventUpdatePreparation(Event eventDto, Event event) {
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(eventDto.getCategory());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
    }

    private List<LocalDateTime> eventDatePreparation(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.parse("5000-01-01 00:00:00",
                DateTimeFormatter.ofPattern(DATE_TIME_STRING));
        if (rangeStart != null) {
            startDate = rangeStart;
        }
        if (rangeEnd != null) {
            endDate = rangeEnd;
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("rangeStart must not be after rangeEnd");
        }

        return List.of(startDate, endDate);
    }
}
