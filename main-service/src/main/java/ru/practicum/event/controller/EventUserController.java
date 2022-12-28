package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.client.EventClient;
import ru.practicum.event.dto.FullEventDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.ShortEventDto;
import ru.practicum.event.dto.UpdateEventRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EventUserController {
    private final EventService eventService;
    private final EventClient eventClient;

    @PostMapping("/users/{userId}/events")
    public FullEventDto add(@PathVariable Long userId, @Valid @RequestBody NewEventDto eventDto) {
        return eventService.addPrivate(userId, eventDto);
    }

    @PatchMapping("/users/{userId}/events")
    public FullEventDto update(@PathVariable Long userId, @Valid @RequestBody UpdateEventRequest eventDto) {
        return eventService.updatePrivate(userId, eventDto);
    }

    @GetMapping("/users/{userId}/events")
    public List<ShortEventDto> getAllByUser(@PathVariable Long userId,
                                            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                            Integer from,
                                            @Positive @RequestParam(value = "size", defaultValue = "10")
                                            Integer size) {
        return eventService.getAllPrivate(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public FullEventDto getById(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getByIdPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public FullEventDto cancelById(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.cancelPrivate(userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventRequestsPrivate(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        return eventService.confirmRequestPrivate(userId, eventId, reqId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {
        return eventService.rejectRequestPrivate(userId, eventId, reqId);
    }

    @GetMapping("/events")
    public List<ShortEventDto> getAll(
            @RequestParam(name = "text", defaultValue = "") String text,
            @RequestParam(name = "categories", required = false) Long[] categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE", required = false) String sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request) {
        eventClient.addHit(request);
        return eventService.getAllPublic(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request);
    }

    @GetMapping("/events/{id}")
    public FullEventDto getById(@PathVariable(name = "id") Long eventId, HttpServletRequest request) {
        eventClient.addHit(request);
        return eventService.getByIdPublic(eventId);
    }
}

