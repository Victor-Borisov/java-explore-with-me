package ru.practicum.comments.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortDto {
    private String text;
}