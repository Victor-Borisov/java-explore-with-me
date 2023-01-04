package ru.practicum.compilation.dto;

import org.springframework.stereotype.Component;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.model.Event;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {
    private final EventMapper eventMapper;

    public CompilationMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public CompilationDto toDto(Compilation compilation,
                                Map<Long, Long> confirmedRequests,
                                Map<Long, Integer>  hitCounts) {
        return CompilationDto.builder()
                .events(compilation.getEvents().stream()
                        .map((Event event) -> eventMapper.toShortDto(event,
                                confirmedRequests.get(event.getId()),
                                hitCounts.get(event.getId())
                                )
                        )
                        .collect(Collectors.toList()))
                .id(compilation.getId())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}
