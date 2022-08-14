package ru.yandex.practicum.ShareIt.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ShareIt.user.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
