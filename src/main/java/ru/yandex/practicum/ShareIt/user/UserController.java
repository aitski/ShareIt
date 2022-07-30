package ru.yandex.practicum.ShareIt.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.model.UserDto;
import ru.yandex.practicum.ShareIt.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;
    boolean needUpdate = false;

    @GetMapping
    public List<UserDto> users() {
        return userServiceImpl.getAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable long id) {
        return convertToDto(userServiceImpl.getById(id));
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userServiceImpl.create(user);
    }

    @PatchMapping("{userId}")
    public User update(@Valid @RequestBody UserDto userDto,
                       @PathVariable long userId) {

        User user = convertFromDto(userDto, userId);
        if (needUpdate) {
            return userServiceImpl.update(user);
        }
        return userServiceImpl.getById(userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userServiceImpl.delete(id);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    private User convertFromDto(UserDto userDto, long userId) {

        userServiceImpl.validateEmail(userDto.getEmail());
        User user = userServiceImpl.getById(userId);
        if (userDto.getName()!=null){
            user.setName(userDto.getName());
            needUpdate=true;
        }
        if (userDto.getEmail()!=null){
            user.setEmail(userDto.getEmail());
            needUpdate=true;
        }
        return user;
    }
}
