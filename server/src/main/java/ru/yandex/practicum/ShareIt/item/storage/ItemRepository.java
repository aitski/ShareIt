package ru.yandex.practicum.ShareIt.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ShareIt.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByRequestId(long requestId, Sort sort);

    Page<Item> findAll (Pageable pageable);

}
