package ru.yandex.practicum.ShareIt.item;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;

@Service
public class ItemMapper {

    public ItemDto convertToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public Item convertFromDto(Item item, ItemDto itemDto) {

        if (itemDto.getName()!=null){
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription()!=null){
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable()!=null){
            item.setAvailable(itemDto.getAvailable());
        }

        return item;
    }
}
