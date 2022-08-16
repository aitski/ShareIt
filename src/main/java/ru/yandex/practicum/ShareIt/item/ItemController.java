package ru.yandex.practicum.ShareIt.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.CommentDto;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.service.ItemService;

import javax.validation.Valid;
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
    public List<ItemDto> items(@RequestHeader("X-Sharer-User-Id") long userId) {

        List<ItemDto> list = itemService.getAll(userId)
                .stream()
                .map(item -> itemMapper.convertToDto(item, userId))
                .collect(Collectors.toList());

        log.debug("list of users returned: {}", list);
        return list;
    }

    @GetMapping("{itemId}")
    public ItemDto getById(@PathVariable long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemDto itemDto = itemMapper.convertToDto(itemService.getById(itemId), userId);
        log.debug("Item returned: {}", itemDto);
        return itemDto;

    }

    @PostMapping
    public Item create(@Valid @RequestBody Item item,
                       @RequestHeader("X-Sharer-User-Id") long ownerId) {

        return itemService.create(item, ownerId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto create(@Valid @RequestBody CommentDto commentDto,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId){

        Comment comment = itemService.createComment(commentMapper.convertFromDto(commentDto, userId, itemId));
        CommentDto newCommentDto = commentMapper.convertToDto(comment);
        log.debug("Comment returned: {}", newCommentDto);
        return newCommentDto;
    }


    @PatchMapping("{itemId}")
    public Item update(@Valid @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") long ownerId,
                       @PathVariable long itemId) {

        Item item = itemService.getById(itemId);
        return itemService.update(itemMapper.convertFromDto(item, itemDto,ownerId));
    }

    @GetMapping("search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.search(text).stream().map(item -> itemMapper.convertToDto(item, userId))
                .collect(Collectors.toList());
    }

}
