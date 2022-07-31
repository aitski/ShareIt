package ru.yandex.practicum.ShareIt.item.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.exception.NotFoundException;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.storage.ItemStorage;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Item> getAll(long ownerId) {
        validateUser(ownerId);
        return itemStorage.getAll()
                .stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(long id) {
        return itemStorage.findById(id).orElseThrow(() -> new NotFoundException
                ("Item with id=" + id + " not found"));
    }

    @Override
    public Item create(Item item, long ownerId) {
        User owner = userStorage.findById(ownerId)
                .orElseThrow(() -> new NotFoundException
                        ("User with id=" + ownerId + " not found"));
        item.setOwner(owner);
        return itemStorage.create(item);
    }

    @Override
    public Item update(Item item) {

        validateUser(item.getOwner().getId());
        return itemStorage.update(item);
    }

    @Override
    public List<Item> search(String text) {

        if (!text.isBlank()) {

            return itemStorage.getAll()
                    .stream()
                    .filter(item -> item.getAvailable() &&
                            item.getDescription().toLowerCase().contains(text.toLowerCase())
                            || item.getName().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void validateUser(long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException
                        ("User with id=" + userId + " not found"));
    }

    @Override
    public void validateOwnership(long itemOwnerId, long userId) {

        if (itemOwnerId != userId) {
            throw new NotFoundException
                    ("Item does not belong to user");
        }

    }

}


