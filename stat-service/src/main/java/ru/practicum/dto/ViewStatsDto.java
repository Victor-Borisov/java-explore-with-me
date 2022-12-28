package ru.practicum.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {

    private String app;
    private String uri;
    private int hits;

}
