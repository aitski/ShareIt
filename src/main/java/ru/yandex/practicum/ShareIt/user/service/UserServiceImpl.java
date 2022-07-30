package ru.yandex.practicum.ShareIt.user.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.exception.ConflictException;
import ru.yandex.practicum.ShareIt.exception.NotFoundException;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.storage.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getById(long id) {
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException
                ("User with id=" + id + " not found"));
    }

    @Override
    public User create(User user) {
        validateEmail(user.getEmail());
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        getById(user.getId());
        return userStorage.update(user);
    }

    @Override
    public void delete(long id) {
        userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException
                        ("User with id=" + id + " not found"));
        userStorage.delete(id);
    }

    public void validateEmail(String email) {
        boolean emailAlreadyExists = userStorage.getAll()
                .stream()
                .map(User::getEmail).anyMatch(s -> s.equals(email));

        if (emailAlreadyExists) {
            throw new ConflictException
                    ("User with email =" + email + " already exists");
        }
    }

}
