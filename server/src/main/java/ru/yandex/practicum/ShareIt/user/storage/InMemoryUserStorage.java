package ru.yandex.practicum.ShareIt.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.ShareIt.user.model.User;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private long nextId = 0;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        long id = getNextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
    }

    private long getNextId() {
        return ++nextId;
    }
}
