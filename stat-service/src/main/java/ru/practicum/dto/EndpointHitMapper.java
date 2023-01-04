package ru.practicum.dto;

import org.springframework.stereotype.Component;
import ru.practicum.model.EndpointHit;

@Component
public class EndpointHitMapper {
    public ViewStatsDto toDto(EndpointHit endpointHit, Integer hits) {
        return ViewStatsDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .hits(hits)
                .build();
    }

    public EndpointHit fromDto(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .id(null)
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }

    public String toUri(EndpointHit endpointHit) {
        return endpointHit.getUri();
    }
}
