package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RequestService {

    ParticipationRequestDto addRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllByUserRequests(long userId);

    List<ParticipationRequestDto> getAllByEventRequests(Long eventId);

    Request getByRequestId(Long requestId);

    Long getConfirmedRequests(long eventId);

    Map<Long, Long> getCountConfirmedByEventIdSet(Set<Long> eventIds);

    void saveRequest(Request request);

    void rejectAllRequests(Long eventId);
}
