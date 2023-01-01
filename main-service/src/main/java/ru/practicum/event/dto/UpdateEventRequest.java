package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    @Size(max = 2000)
    private String annotation;

    @Positive
    private Long category;

    private String description;
    private String eventDate;
    private Location location;

    @NotNull
    private Long eventId;

    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;

    @Size(max = 120)
    private String title;

}
