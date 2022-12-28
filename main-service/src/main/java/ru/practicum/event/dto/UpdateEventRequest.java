package ru.practicum.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    @Length(min = 20, max = 2000)
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

    @Length(min = 3, max = 120)
    private String title;

}
