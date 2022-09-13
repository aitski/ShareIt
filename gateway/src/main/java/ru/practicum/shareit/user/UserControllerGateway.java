package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoGateway;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserControllerGateway {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get all users");
        return userClient.getAll();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDtoGateway user) {
        log.info("Creating user {}", user);
        return userClient.create(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        log.info("Get user {}", userId);
        return userClient.getById(userId);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> patch(@RequestBody UserDtoGateway user,
                                        @PathVariable long userId) {
        log.info("Patch user {}", userId);
        return userClient.patch(userId, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        log.info("Delete user {}", id);
        return userClient.delete(id);
    }

}
