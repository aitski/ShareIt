package ru.yandex.practicum.ShareIt.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;
    boolean needUpdate = false;

    @GetMapping
    public List<ItemDto> items(@RequestHeader ("X-Sharer-User-Id") long ownerId) {

        return itemServiceImpl.getAll(ownerId)
                .stream()
                .filter(item -> item.getOwnerId() == ownerId)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{itemId}")
    public ItemDto getById(@PathVariable long itemId) {
        return convertToDto(itemServiceImpl.getById(itemId));
    }

    @PostMapping
    public Item create(@Valid @RequestBody Item item,
                       @RequestHeader ("X-Sharer-User-Id") long ownerId) {

        item.setOwnerId(ownerId);
        return itemServiceImpl.create(item);
    }

    @PatchMapping("{itemId}")
    public Item update(@Valid @RequestBody ItemDto itemDto,
                       @RequestHeader ("X-Sharer-User-Id") long ownerId,
                       @PathVariable long itemId) {

        Item item = convertFromDto(itemDto,itemId,ownerId);

        if (needUpdate) {
            return itemServiceImpl.update(item);
        }
        return itemServiceImpl.getById(itemId);
    }

    @GetMapping("search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemServiceImpl.search(text).stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ItemDto convertToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public Item convertFromDto(ItemDto itemDto, long itemId, long ownerId) {

        itemServiceImpl.validateUser(ownerId);
        itemServiceImpl.validateOwnership(itemServiceImpl.getById(itemId),ownerId);

        Item item = itemServiceImpl.getById(itemId);

        if (itemDto.getName()!=null){
            item.setName(itemDto.getName());
            needUpdate=true;
        }
        if (itemDto.getDescription()!=null){
            item.setDescription(itemDto.getDescription());
            needUpdate=true;
        }
        if (itemDto.getAvailable()!=null){
            item.setAvailable(itemDto.getAvailable());
            needUpdate=true;
        }

        return item;
    }

}
