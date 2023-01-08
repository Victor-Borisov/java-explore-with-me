package ru.practicum.comments.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private Long userId;
    private Long eventId;
    private LocalDateTime createdOn;
}