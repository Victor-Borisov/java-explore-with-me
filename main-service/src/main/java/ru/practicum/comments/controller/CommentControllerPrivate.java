package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentShortDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CommentControllerPrivate {
    private final CommentService commentService;

    @PostMapping("/users/{userId}/comments/events/{eventId}")
    public Comment addComment(@PathVariable(value = "userId") @Positive long userId,
                              @PathVariable(value = "eventId") @Positive long eventId,
                              @RequestBody @Valid CommentShortDto comment) {
        log.info("Called addComment");

        return commentService.addComment(comment, userId, eventId);
    }

    @GetMapping("comments/{commentId}")
    public CommentDto getComment(@PathVariable long commentId) {
        log.info("Called getComment");

        return commentService.getComment(commentId);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public void deleteComment(@PathVariable long commentId, @PathVariable long userId) {
        log.info("Called deleteComment");

        commentService.deleteComment(commentId, userId);
    }
}