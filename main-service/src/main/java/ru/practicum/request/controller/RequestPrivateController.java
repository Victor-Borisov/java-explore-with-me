package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class RequestPrivateController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getAll(@PathVariable long userId) {
        log.info("Called getAll");

        return requestService.getAllByUserRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto add(@PathVariable long userId, @RequestParam(value = "eventId") long eventId) {
        log.info("Called add");

        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@Positive @PathVariable Long userId, @Positive @PathVariable Long requestId) {
        log.info("Called cancel");

        return requestService.cancelRequest(userId, requestId);
    }
}
