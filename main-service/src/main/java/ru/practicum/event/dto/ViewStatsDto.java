package ru.practicum.event.dto;

import lombok.*;

@Data
@Builder
public class ViewStatsDto {
    private String app;
    private String uri;
    private int hits;
}