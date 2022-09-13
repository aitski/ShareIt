package ru.yandex.practicum.ShareIt.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.ShareIt.item.model.Item;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    private long nextId = 0;

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item create(Item item) {
        long id = getNextId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public Optional<Item> findById(long id) {
        if (items.containsKey(id)) {
            return Optional.of(items.get(id));
        }
        return Optional.empty();
    }

    private long getNextId() {
        return ++nextId;
    }
}
