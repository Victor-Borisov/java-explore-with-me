package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EventService {

    List<FullEventDto> getAllByAdmin(Long[] users, String[] states, Long[] categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    FullEventDto updateByAdmin(long eventId, AdminUpdateEventRequest eventDto);

    FullEventDto publishEventAdmin(long eventId);

    FullEventDto rejectEventAdmin(long eventId);

    List<ShortEventDto> getAllPrivate(long userId, int from, int size);

    FullEventDto updatePrivate(long userId, UpdateEventRequest eventDto);

    FullEventDto addPrivate(long userId, NewEventDto eventDto);

    FullEventDto getByIdPrivate(long userId, long eventId);

    FullEventDto cancelPrivate(long userId, long eventId);

    List<ParticipationRequestDto> getEventRequestsPrivate(long userId, long eventId);

    ParticipationRequestDto confirmRequestPrivate(long userId, long eventId, long requestId);

    ParticipationRequestDto rejectRequestPrivate(long userId, long eventId, long requestId);

    Event getById(long eventId);

    List<ShortEventDto> getAllPublic(String text,
                                     Long[] categories,
                                     Boolean paid,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Boolean onlyAvailable,
                                     String sort,
                                     Integer from,
                                     Integer size,
                                     HttpServletRequest request);

    FullEventDto getByIdPublic(long eventId);

    Set<Event> getAllByEvents(Set<Long> events);

    Map<Long, Integer> getHitCounts(Collection<Event> events);
}
