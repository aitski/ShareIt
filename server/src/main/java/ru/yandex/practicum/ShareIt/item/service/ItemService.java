package ru.yandex.practicum.ShareIt.item.service;

import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> getAll(long id, int from, int size);

    Item getById(long id);

    Item create(Item item, long ownerId);

    Comment createComment(Comment comment);

    Item update(Item item);

    List<Item> search(String text, int from, int size);

}
