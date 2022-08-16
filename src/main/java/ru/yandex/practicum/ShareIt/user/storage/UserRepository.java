package ru.yandex.practicum.ShareIt.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ShareIt.user.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
