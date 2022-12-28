package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto add(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Called add");

        return compilationService.add(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void delete(@PathVariable Long compId) {
        log.info("Called delete");

        compilationService.delete(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Called deleteEvent");

        compilationService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEvent(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Called addEvent");

        compilationService.addEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Called pinCompilation");

        compilationService.pinCompilation(true, compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Called unpinCompilation");

        compilationService.pinCompilation(false, compId);
    }
}