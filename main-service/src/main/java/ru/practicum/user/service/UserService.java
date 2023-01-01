package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    UserDto add(UserDto userDto);

    List<UserDto> getAll(List<Long> ids, int from, int size);

    void delete(long userId);

    User getUserById(long userId);

}
