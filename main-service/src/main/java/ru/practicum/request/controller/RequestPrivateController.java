package ru.practicum.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
@AllArgsConstructor
public class RequestPrivateController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getAll(@PathVariable Long userId) {
        log.info("Called getAll");

        return requestService.getAllByUserRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto add(@PathVariable Long userId, @RequestParam(value = "eventId") Long eventId) {
        log.info("Called add");

        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Called cancel");

        return requestService.cancelRequest(userId, requestId);
    }
}
