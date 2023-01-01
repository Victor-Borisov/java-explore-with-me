package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(long compilationId);

    CompilationDto add(NewCompilationDto newCompilationDto);

    void delete(long compilationId);

    void deleteEvent(long compilationId, long eventId);

    void addEvent(long compilationId, long eventId);

    void pin(long compilationId);

    void unpin(long compilationId);
}
