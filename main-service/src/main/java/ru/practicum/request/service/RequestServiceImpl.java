package ru.practicum.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.service.EventService;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.Status;
import ru.practicum.request.storage.RequestRepository;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor(onConstructor_ = {@Lazy})
@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    private final EventService eventService;
    private final UserService userService;
    private final RequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> getAllByUserRequests(Long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("Retrieved request list of user with id: {}", userId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        Event event = eventService.getById(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Participation request for own event prohibited");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Participation in unpublished event prohibited");
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ValidationException("Participation request limit reached");
        }
        Status status;
        if (event.getRequestModeration()) {
            status = Status.PENDING;
        } else {
            status = Status.CONFIRMED;
            eventService.increaseConfirmedRequestsPrivate(event);
        }
        Request request = Request.builder()
                .status(status)
                .event(event)
                .requester(userService.getUserById(userId))
                .created(LocalDateTime.now())
                .build();
        log.info("Added participation request for event {} from user {}", eventId, userId);

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = getByRequestId(requestId);
        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidationException("Cancellation of someone else's request prohibited");
        }
        if (request.getStatus().equals(Status.REJECTED) || request.getStatus().equals(Status.CANCELED)) {
            throw new ValidationException("Request already canceled/rejected");
        }
        if (request.getStatus().equals(Status.CONFIRMED)) {
            eventService.decreaseConfirmedRequestsPrivate(eventService.getById(request.getEvent().getId()));
        }
        request.setStatus(Status.CANCELED);
        log.info("Request on event {} canceled", requestId);

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getAllByEventRequests(Long eventId) {
        log.info("List of request on event {} retrieved", eventId);

        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Request getByRequestId(Long requestId) {
        log.info("Request {} retrieved", requestId);

        return requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request {} not found", requestId));
    }

    @Override
    public void saveRequest(Request request) {
        log.info("Request {} saved", request);
        requestRepository.save(request);
    }

    @Override
    public void rejectAllRequests(Long eventId) {
        log.info("Reject of all requests on event {} requested", eventId);
        requestRepository.rejectAll(eventId);
    }
}
