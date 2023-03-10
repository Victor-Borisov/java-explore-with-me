package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    @Transactional
    CommentDto add(NewCommentDto comment, long userId);

    CommentDto getById(long commentId);

    CommentDto getByIdPrivate(long userId, long commentId);

    List<CommentDto> getAllAdmin(Long[] users, Long[] events,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<CommentDto> getAllPrivate(long userId, int from, int size);

    List<CommentDto> getAllByUserId(long userId);

    List<CommentDto> getAllByEventId(long eventId);

    void delete(long userId, long commentId);

    CommentDto updatePrivate(long userId, long commentId, UpdateCommentDto updateCommentDto);

    CommentDto updateAdmin(long commentId, UpdateCommentDto updateCommentDto);
}