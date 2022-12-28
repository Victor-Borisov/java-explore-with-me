package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.storage.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.utils.PageableRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final EventService eventService;
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getAll(boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepository.findAllByPinnedIs(pinned,
                getPageable(from, size, Sort.unsorted()));
        log.info("List of compilations {} retrieved", compilations);

        return compilations.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compilationId) {
        Compilation compilation = getByIdAndThrow(compilationId);
        log.info("Compilation {} retrieved", compilation);

        return CompilationMapper.toDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            checkIsEventsExist(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.fromNewCompilationDto(newCompilationDto);
        List<Event> eventList = Collections.emptyList();
        if (newCompilationDto.getEvents() != null || !newCompilationDto.getEvents().isEmpty()) {
            eventList = newCompilationDto.getEvents()
                    .stream().map(eventService::getById)
                    .collect(Collectors.toList());
        }
        compilation.setEvents(eventList);
        compilation = compilationRepository.save(compilation);
        log.info("Compilation {} added", compilation);

        return CompilationMapper.toDto(compilation);
    }

    @Override
    public void delete(Long compilationId) {
        getByIdAndThrow(compilationId);
        compilationRepository.deleteById(compilationId);
        log.info("Удалена подборка c id {}", compilationId);
    }

    @Override
    @Transactional
    public void deleteEvent(Long compilationId, Long eventId) {
        Compilation compilation = getByIdAndThrow(compilationId);
        eventService.getById(eventId);
        compilationRepository.deleteEvent(compilationId, eventId);
        log.info("From compilation {} deleted event {}", compilation, eventId);
    }

    @Override
    @Transactional
    public void addEvent(Long compilationId, Long eventId) {
        Compilation compilation = getByIdAndThrow(compilationId);
        eventService.getById(eventId);
        compilationRepository.addEvent(compilationId, eventId);
        log.info("To compilation {} added event {}", compilation, eventId);
    }

    @Override
    @Transactional
    public void pinCompilation(boolean pin, Long compilationId) {
        compilationRepository.pinningCompilation(pin, compilationId);
        Compilation compilation = getByIdAndThrow(compilationId);
        if (pin) {
            log.info("Compilation {} pinned", compilation);
        } else {
            log.info("Compilation {} pinned out", compilation);
        }
    }

    private Compilation getByIdAndThrow(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException("Compilation {} not found", compilationId));
    }

    private Pageable getPageable(Integer from, Integer size, Sort sort) {
        return new PageableRequest(from, size, sort);
    }

    public void checkIsEventsExist(List<Long> eventIdList) {
        for (Long eventId : eventIdList) {
            eventService.getById(eventId);
        }
    }
}
