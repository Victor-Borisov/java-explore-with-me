package ru.practicum.event.dto;

import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.utils.DateFormatterCustom;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class EventMapper {
    private static DateFormatterCustom formatter;

    public EventMapper(DateFormatterCustom formatter) {
        this.formatter = formatter;
    }

    public static FullEventDto toFullDto(Event event, Long confirmedRequests, Integer views) {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(event.getCategory().getId())
                .name(event.getCategory().getName())
                .build();
        UserShortDto userShortDto = UserShortDto.builder()
                .id(event.getInitiator().getId())
                .name(event.getInitiator().getName())
                .build();
        return FullEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(confirmedRequests)
                .createdOn(formatter.dateToString(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(formatter.dateToString(event.getEventDate()))
                .initiator(userShortDto)
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(formatter.dateToString(event.getPublishedOn()))
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static ShortEventDto toShortDto(Event event, Long confirmedRequests, Integer views) {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(event.getCategory().getId())
                .name(event.getCategory().getName())
                .build();
        UserShortDto userShortDto = UserShortDto.builder()
                .id(event.getInitiator().getId())
                .name(event.getInitiator().getName())
                .build();

        return ShortEventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(confirmedRequests)
                .eventDate(formatter.dateToString(event.getEventDate()))
                .initiator(userShortDto)
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static Event fromNewDto(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(Category.builder().id(newEventDto.getCategory()).build())
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public static Event fromUpdateEventRequest(UpdateEventRequest newEventDto) {
        return Event.builder()
                .id(newEventDto.getEventId())
                .annotation(newEventDto.getAnnotation())
                .category(Category.builder().id(newEventDto.getCategory()).build())
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public static Event fromAdminUpdateEventRequest(AdminUpdateEventRequest newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(Category.builder().id(newEventDto.getCategory()).build())
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public static Long toId(Event event) {
        return event.getId();
    }

}