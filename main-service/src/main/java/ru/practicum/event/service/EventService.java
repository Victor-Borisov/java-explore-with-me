package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<FullEventDto> getAllByAdmin(Long[] users, String[] states, Long[] categories,
                                     String rangeStart, String rangeEnd, int from, int size);

    FullEventDto updateByAdmin(Long eventId, AdminUpdateEventRequest eventDto);

    FullEventDto publishEventAdmin(Long eventId);

    FullEventDto rejectEventAdmin(Long eventId);

    List<ShortEventDto> getAllPrivate(Long userId, Integer from, Integer size);

    FullEventDto updatePrivate(Long userId, UpdateEventRequest eventDto);

    FullEventDto addPrivate(Long userId, NewEventDto eventDto);

    FullEventDto getByIdPrivate(Long userId, Long eventId);

    FullEventDto cancelPrivate(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequestsPrivate(Long userId, Long eventId);

    ParticipationRequestDto confirmRequestPrivate(Long userId, Long eventId, Long requestId);

    ParticipationRequestDto rejectRequestPrivate(Long userId, Long eventId, Long requestId);

    void increaseConfirmedRequestsPrivate(Event event);

    void decreaseConfirmedRequestsPrivate(Event event);

    Event getById(Long eventId);

    List<ShortEventDto> getAllPublic(String text,
                                     Long[] categories,
                                     Boolean paid,
                                     String rangeStart,
                                     String rangeEnd,
                                     Boolean onlyAvailable,
                                     String sort,
                                     Integer from,
                                     Integer size,
                                     HttpServletRequest request);

    FullEventDto getByIdPublic(Long eventId);
}
