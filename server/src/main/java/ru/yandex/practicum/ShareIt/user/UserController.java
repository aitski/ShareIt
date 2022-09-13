package ru.yandex.practicum.ShareIt.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.model.UserDto;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable long id) {
        return userMapper.convertToDto(userService.getById(id));
    }

    @PostMapping
    public User create(@RequestBody User user) {

        return userService.create(user);
    }

    @PatchMapping("{userId}")
    public User update(@RequestBody UserDto userDto,
                       @PathVariable long userId) {

        User user = userService.getById(userId);
        return userService.update(userMapper.convertFromDto(user,userDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }

}
