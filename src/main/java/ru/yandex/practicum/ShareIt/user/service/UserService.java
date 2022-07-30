package ru.yandex.practicum.ShareIt.user.service;

import ru.yandex.practicum.ShareIt.user.model.User;
import java.util.List;

public interface UserService {

    List<User> getAll();
    User getById(long id);
    User create(User user);
    User update(User user);
    void delete (long id);

}
