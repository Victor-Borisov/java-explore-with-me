package ru.practicum.comments.dto;

import org.springframework.stereotype.Component;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class CommentMapper {
    public CommentDto fromComment(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .build();
    }

    public Comment fromNewCommentDto(NewCommentDto newCommentDto, User user, Event event) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .user(user)
                .event(event)
                .createdOn(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .build();
    }

}