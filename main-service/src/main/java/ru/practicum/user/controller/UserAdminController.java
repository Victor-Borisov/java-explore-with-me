package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.service.UserService;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin/users")
@Validated
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll(@RequestParam(value = "ids", required = false) List<Long> ids,
                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                             @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Called getAll");

        return userService.getAll(ids, from, size);
    }

    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        log.info("Called add");

        return userService.add(userDto);
    }

    @DeleteMapping(path = "/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("Called delete");

        userService.delete(userId);
    }
}
