package ru.practicum.comments.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private long id;
    private String text;
    private long userId;
    private long eventId;
    private LocalDateTime createdOn;
}