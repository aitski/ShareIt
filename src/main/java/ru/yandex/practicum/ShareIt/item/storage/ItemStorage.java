package ru.yandex.practicum.ShareIt.item.storage;

import ru.yandex.practicum.ShareIt.item.model.Item;

import java.util.List;
import java.util.Optional;

 public interface ItemStorage {

     List<Item> getAll();

     Item create(Item item);
    
     Item update(Item item);
    
     Optional<Item> findById(long id);

}
