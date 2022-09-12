package ru.yandex.practicum.ShareIt.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.storage.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        List<User> list = userRepository.findAll();
        log.debug("list of users returned: {}", list);
        return list;
    }

    @Override
    public User getById(long id) {

        User user = userRepository.findById(id).orElseThrow
                (() -> {
                    log.error("User with id {} not found", id);
                    return new NotFoundException();
                });
        log.debug("User returned {}",user);
        return user;
    }

    @Override
    public User create(User user) {

        User userNew = userRepository.save(user);
        log.debug("user created: {}", userNew);
        return userNew;
    }

    @Override
    public User update(User user) {

        User userNew = userRepository.save(user);
        log.debug("user updated: {}", userNew);
        return userNew;
    }

    @Override
    public void delete(long id) {
        userRepository.delete(getById(id));
        log.debug("user with id {} deleted", id);
    }
}
