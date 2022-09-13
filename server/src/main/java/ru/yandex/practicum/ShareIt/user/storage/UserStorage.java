package ru.yandex.practicum.ShareIt.user.storage;

import ru.yandex.practicum.ShareIt.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {


    List<User> getAll();

    User create(User user);

    User update(User user);

    Optional<User> findById(long id);

    void delete(long userId);
}
