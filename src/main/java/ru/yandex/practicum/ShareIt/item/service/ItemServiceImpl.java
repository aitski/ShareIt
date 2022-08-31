package ru.yandex.practicum.ShareIt.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.storage.CommentRepository;
import ru.yandex.practicum.ShareIt.item.storage.ItemRepository;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    private final UserService userService;

    @Override
    public List<Item> getAll(long ownerId, int from, int size) {

        userService.getById(ownerId);

        if (from < 0 || size <= 0) {
            ValidationException e = new ValidationException(
                    "from or size param is incorrect"
            );
            log.error(e.getMessage());
            throw e;
        }

        Pageable pageRequest = PageRequest.of(from, size);

        return itemRepository.findAll(pageRequest).getContent()
                .stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(long id) {

        Item item = itemRepository.findById(id).orElseThrow
                (() -> {
                    log.error("Item with id {} not found", id);
                    return new NotFoundException();
                });
        log.debug("Item returned {}", item);
        return item;
    }

    @Override
    public Item create(Item item, long ownerId) {
        User owner = userService.getById(ownerId);
        item.setOwner(owner);
        Item newItem = itemRepository.save(item);
        log.debug("new item created: {}", newItem);
        return newItem;
    }

    public Comment createComment(Comment comment) {

        Comment newComment = commentRepository.save(comment);
        log.debug("new comment created: {}", newComment);
        return newComment;
    }

    @Override
    public Item update(Item item) {

        itemRepository.save(item);
        log.debug("item updated: {}", item);
        return item;
    }

    @Override
    public List<Item> search(String text, int from, int size) {

        if (from < 0 || size <= 0) {
            ValidationException e = new ValidationException(
                    "from or size param is incorrect"
            );
            log.error(e.getMessage());
            throw e;
        }

        Pageable pageRequest = PageRequest.of(from, size);

        if (!text.isBlank()) {

            return itemRepository.findAll(pageRequest)
                    .getContent()
                    .stream()
                    .filter(item -> item.getAvailable() &&
                            item.getDescription().toLowerCase().contains(text.toLowerCase())
                            || item.getName().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}


