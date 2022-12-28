package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    UserDto add(UserDto userDto);

    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    void delete(Long userId);

    User getUserById(Long userId);

}
