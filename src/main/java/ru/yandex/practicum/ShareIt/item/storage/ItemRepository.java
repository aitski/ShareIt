package ru.yandex.practicum.ShareIt.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ShareIt.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
