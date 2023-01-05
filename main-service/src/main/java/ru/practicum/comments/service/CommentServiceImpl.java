package ru.practicum.comments.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.storage.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventService eventService;
    private final UserService userService;

    @Override
    @Transactional
    public Comment addComment(CommentShortDto comment, long userId, long eventId) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventByIdPrivate(eventId);

        return commentRepository.save(commentMapper.toComment(comment, user, event));
    }

    @Override
    public CommentDto getComment(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment {} not found", commentId));
        log.info("Retrieved comment of user: {}", commentId);

        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public List<Comment> getAllCommentsByUser(long userId) {
        log.info("Retrieved comments of user: {}", userId);

        return commentRepository.getAllByUserId(userId);
    }

    @Override
    @Transactional
    public List<Comment> getAllCommentsByEvent(long eventId) {
        log.info("Retrieved comments of event: {}", eventId);

        return commentRepository.getAllByEventId(eventId);
    }

    @Override
    @Transactional
    public void deleteComment(long commentId, long userId) {
        Optional<Comment> comment = commentRepository.findByIdAndUserId(userId, commentId);
        if (comment.isEmpty()) {
            throw new ValidationException("Comment with specified commentId and userId not found");
        }
        commentRepository.deleteById(commentId);
        log.info("Comment {} deleted", commentId);
    }

    @Override
    @Transactional
    public CommentDto updateComment(long userId, long commentId, CommentShortDto commentShortDto) {
        Optional<Comment> comment = commentRepository.findByIdAndUserId(userId, commentId);
        if (comment.isEmpty()) {
            throw new ValidationException("Comment with specified commentId and userId not found");
        }
        comment.get().setText(commentShortDto.getText());
        log.info("Comment {} updated", commentId);

        return commentMapper.toDto(commentRepository.save(comment.get()));
    }
}
