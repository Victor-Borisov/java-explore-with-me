package ru.practicum.comments.dto;

import lombok.*;

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
}