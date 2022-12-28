package ru.practicum.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.service.UserService;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> get(@RequestParam(value = "ids", required = false) List<Long> ids,
                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                             @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return userService.getAll(ids, from, size);
    }

    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @DeleteMapping(path = "/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
