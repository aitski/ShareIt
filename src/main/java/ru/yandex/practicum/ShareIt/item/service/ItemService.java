package ru.yandex.practicum.ShareIt.item.service;

import ru.yandex.practicum.ShareIt.item.model.Item;
import java.util.List;

public interface ItemService {

    List<Item> getAll(long id);
    Item getById(long id);
    Item create(Item item);
    Item update(Item item);
    List<Item> search(String text);
}
