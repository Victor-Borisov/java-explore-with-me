package ru.practicum.dto;

import org.springframework.stereotype.Component;
import ru.practicum.model.EndpointHit;
import ru.practicum.utils.DateFormatterCustom;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EndpointHitMapper {

    private final DateFormatterCustom formatter = new DateFormatterCustom();

    public ViewStatsDto toDto(EndpointHit endpointHit) {
        return new ViewStatsDto(endpointHit.getApp(), endpointHit.getUri(), 0);
    }

    public EndpointHit fromDto(EndpointHitDto endpointHitDto) {
        return new EndpointHit(null,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                formatter.stringToDate(endpointHitDto.getTimestamp()));
    }

    public List<ViewStatsDto> toDto(List<EndpointHit> endpointHits) {
        return endpointHits
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
