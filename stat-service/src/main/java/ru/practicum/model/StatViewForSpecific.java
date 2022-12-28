package ru.practicum.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatViewForSpecific {

    private String start;
    private String end;
    private String[] uris;
    private boolean unique;

}
