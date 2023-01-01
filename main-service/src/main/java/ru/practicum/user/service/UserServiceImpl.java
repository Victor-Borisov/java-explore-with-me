package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto add(UserDto userDto) {
        checkIfExistsWithSameName(userDto.getName());
        User user = userRepository.save(UserMapper.fromUserDto(userDto));
        log.info("User added {}", user);

        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        List<User> users = userRepository.findAllById(ids);
        log.info("User list retrieved {}", users);

        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
        log.info("User with id {} deleted", userId);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id {} not found", userId));
    }

    void checkIfExistsWithSameName(String name) {
        Optional<User> userWithSameName = userRepository.findByName(name);
        if (userWithSameName.isPresent()) {
            throw new ConflictException("User with the same name already exists");
        }
    }
}
