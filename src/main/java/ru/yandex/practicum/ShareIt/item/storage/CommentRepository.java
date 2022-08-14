package ru.yandex.practicum.ShareIt.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ShareIt.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItem_Id (long itemId);
}
