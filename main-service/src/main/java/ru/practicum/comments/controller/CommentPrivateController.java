package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class CommentPrivateController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAll(@PathVariable long userId,
                                      @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                      @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Called getAll");

        return commentService.getAllPrivate(userId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getById(@PathVariable long userId, @PathVariable long commentId) {
        log.info("Called getComment");

        return commentService.getByIdPrivate(userId, commentId);
    }

    @PostMapping
    public CommentDto add(@PathVariable(value = "userId") @Positive long userId,
                              @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Called addComment");

        return commentService.add(newCommentDto, userId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable long userId, @PathVariable long commentId, @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Called update");

        return commentService.updatePrivate(userId, commentId, updateCommentDto);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable long userId, @PathVariable long commentId) {
        log.info("Called deleteComment");

        commentService.delete(userId, commentId);
    }
}