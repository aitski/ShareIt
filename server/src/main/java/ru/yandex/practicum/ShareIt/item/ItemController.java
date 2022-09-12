package ru.yandex.practicum.ShareIt.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.CommentDto;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.service.ItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @GetMapping
    public List<ItemDto> items(@RequestHeader("X-Sharer-User-Id") long userId,
                               @RequestParam int from,
                               @RequestParam int size) {

        List<ItemDto> list = itemService.getAll(userId, from, size)
                .stream()
                .map(item -> itemMapper.convertToDto(item, userId))
                .collect(Collectors.toList());

        log.debug("list of items returned: {}", list);
        return list;
    }

    @GetMapping("{itemId}")
    public ItemDto getById(@PathVariable long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {

        Item item = itemService.getById(itemId);
        return itemMapper.convertToDto(item, userId);
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") long ownerId) {

        Item item = itemService.create(itemMapper.convertFromDtoCreate(itemDto), ownerId);
        return itemMapper.convertToDto(item, ownerId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto create(@RequestBody CommentDto commentDto,
                             @RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long itemId) {

        Comment comment = itemService.createComment
                (commentMapper.convertFromDto(commentDto, userId, itemId));
        return commentMapper.convertToDto(comment);
    }


    @PatchMapping("{itemId}")
    public Item update(@RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") long ownerId,
                       @PathVariable long itemId) {

        Item item = itemService.getById(itemId);
        return itemService.update(itemMapper.convertFromDtoPatch(item, itemDto, ownerId));
    }

    @GetMapping("search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestParam int from,
                                @RequestParam int size) {
        return itemService.search(text, from, size).stream().map(
                        item -> itemMapper.convertToDto(item, userId))
                .collect(Collectors.toList());
    }
}
