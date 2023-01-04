package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventForSpecific {
    private String text;
    private Long[] categories;
    private Long[] users;
    private String[] states;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private boolean onlyAvailable = false;
    private String sort;
    private Integer from = 0;
    private Integer size = 10;
}
