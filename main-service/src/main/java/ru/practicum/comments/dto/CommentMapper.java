package ru.practicum.comments.dto;

import org.springframework.stereotype.Component;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .build();
    }

    public Comment toComment(CommentShortDto commentShortDto, User user, Event event) {
        return Comment.builder()
                .text(commentShortDto.getText())
                .user(user)
                .event(event)
                .build();
    }

}