package ru.yandex.practicum.ShareIt.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public List<ItemDto> items(@RequestHeader("X-Sharer-User-Id") long ownerId) {

        return itemService.getAll(ownerId)
                .stream()
                .map(itemMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{itemId}")
    public ItemDto getById(@PathVariable long itemId) {
        return itemMapper.convertToDto(itemService.getById(itemId));
    }

    @PostMapping
    public Item create(@Valid @RequestBody Item item,
                       @RequestHeader("X-Sharer-User-Id") long ownerId) {

        return itemService.create(item, ownerId);
    }

    @PatchMapping("{itemId}")
    public Item update(@Valid @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") long ownerId,
                       @PathVariable long itemId) {

        Item item = itemService.getById(itemId);
        itemService.validateOwnership(item.getOwner().getId(), ownerId);

        return itemService.update(itemMapper.convertFromDto(item, itemDto));
    }

    @GetMapping("search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text).stream().map(itemMapper::convertToDto)
                .collect(Collectors.toList());
    }

}
