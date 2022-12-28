package ru.practicum.event.model;

import lombok.*;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {
    private Float lat;
    private Float lon;
}
