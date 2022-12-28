package ru.practicum.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.request.model.Request;
import ru.practicum.utils.DateFormatterCustom;

@UtilityClass
public class RequestMapper {
    private final DateFormatterCustom formatter = new DateFormatterCustom();

    public ParticipationRequestDto toRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(formatter.dateToString(request.getCreated()))
                .status(request.getStatus().toString())
                .build();
    }
}
