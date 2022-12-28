package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAll(boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compilationId);

    CompilationDto add(NewCompilationDto newCompilationDto);

    void delete(Long compilationId);

    void deleteEvent(Long compilationId, Long eventId);

    void addEvent(Long compilationId, Long eventId);

    void pinCompilation(boolean pin, Long compilationId);
}
