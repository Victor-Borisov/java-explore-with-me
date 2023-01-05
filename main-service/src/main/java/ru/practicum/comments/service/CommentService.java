package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentService {

    @Transactional
    Comment addComment(CommentShortDto comment, long userId, long eventId);

    CommentDto getComment(long commentId);

    @Transactional
    List<Comment> getAllCommentsByUser(long userId);

    @Transactional
    List<Comment> getAllCommentsByEvent(long eventId);

    @Transactional
    void deleteComment(long commentId, long userId);

    @Transactional
    CommentDto updateComment(long userId, long commentId, CommentShortDto commentDto);
}