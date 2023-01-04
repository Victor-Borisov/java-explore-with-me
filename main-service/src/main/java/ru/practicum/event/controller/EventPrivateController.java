package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.FullEventDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.ShortEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping
    public List<ShortEventDto> getAll(@PathVariable long userId,
                                      @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                      @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Called getAll");

        return eventService.getAllPrivate(userId, from, size);
    }

    @PostMapping
    public FullEventDto add(@PathVariable long userId, @Valid @RequestBody NewEventDto eventDto) {
        log.info("Called add");

        return eventService.addPrivate(userId, eventDto);
    }

    @PatchMapping
    public FullEventDto update(@PathVariable long userId, @Valid @RequestBody UpdateEventRequest eventDto) {
        log.info("Called update");

        return eventService.updatePrivate(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public FullEventDto getById(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Called getById");

        return eventService.getByIdPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public FullEventDto cancelById(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Called cancelById");

        return eventService.cancelPrivate(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Called getEventRequests");

        return eventService.getEventRequestsPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable long userId,
                                                  @PathVariable long eventId,
                                                  @PathVariable long reqId) {
        log.info("Called confirmRequest");

        return eventService.confirmRequestPrivate(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable long userId,
                                                 @PathVariable long eventId,
                                                 @PathVariable long reqId) {
        log.info("Called rejectRequest");

        return eventService.rejectRequestPrivate(userId, eventId, reqId);
    }

}

