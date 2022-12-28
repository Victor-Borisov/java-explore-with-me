package ru.practicum.request.dto;

import lombok.*;

@Data
@Builder
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private String created;
    private String status;
}
