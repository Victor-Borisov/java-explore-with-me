package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllByUserRequests(Long userId);

    List<ParticipationRequestDto> getAllByEventRequests(Long eventId);

    Request getByRequestId(Long requestId);

    void saveRequest(Request request);

    void rejectAllRequests(Long eventId);
}
