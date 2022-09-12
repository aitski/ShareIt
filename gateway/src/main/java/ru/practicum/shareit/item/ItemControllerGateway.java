package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoGateway;
import ru.practicum.shareit.item.dto.ItemDtoGateway;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemControllerGateway {
    private final ItemClient itemClient;

    @GetMapping()
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get all items user {}, from {}, size {}", userId, from, size);
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId) {
        log.info("Get item {}, user {}", itemId, userId);
        return itemClient.getById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDtoGateway item,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Creating item {}, user {}", item, userId);
        return itemClient.create(userId, item);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDtoGateway comment,
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long itemId) {
        log.info("Creating comment {}, user {}, item {}", comment, userId, itemId);
        return itemClient.createComment(userId, itemId, comment);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> create(@RequestBody ItemDtoGateway item,
                                         @RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long itemId) {
        log.info("Patching item {}, user {}", itemId, userId);
        return itemClient.patch(userId, itemId, item);
    }

    @GetMapping("search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestHeader("X-Sharer-User-Id") long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Search items text {}, user {}, from {}, size {}", text, userId, from, size);
        return itemClient.search(text, userId, from, size);
    }


}
