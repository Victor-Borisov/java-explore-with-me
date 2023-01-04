package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.AdminUpdateEventRequest;
import ru.practicum.event.dto.FullEventDto;
import ru.practicum.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin/events")
@Validated
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<FullEventDto> getAll(
            @RequestParam(required = false) Long[] users,
            @RequestParam(required = false) String[] states,
            @RequestParam(required = false) Long[] categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Called add");

        return eventService.getAllByAdmin(users, states, categories, rangeStart,
                rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public FullEventDto update(@PathVariable long eventId, @RequestBody AdminUpdateEventRequest eventDto) {
        log.info("Called update");

        return eventService.updateByAdmin(eventId, eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public FullEventDto publish(@PathVariable long eventId) {
        log.info("Called publish");

        return eventService.publishEventAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public FullEventDto reject(@PathVariable long eventId) {
        log.info("Called reject");

        return eventService.rejectEventAdmin(eventId);
    }
}
