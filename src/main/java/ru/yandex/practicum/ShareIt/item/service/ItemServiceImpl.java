package ru.yandex.practicum.ShareIt.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.exception.NotFoundException;
import ru.yandex.practicum.ShareIt.item.model.Comment;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.storage.CommentRepository;
import ru.yandex.practicum.ShareIt.item.storage.ItemRepository;
import ru.yandex.practicum.ShareIt.item.storage.ItemStorage;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.storage.UserRepository;
import ru.yandex.practicum.ShareIt.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<Item> getAll(long ownerId) {
        validateUser(ownerId);

        return itemRepository.findAll()
                .stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NotFoundException
                ("Item with id=" + id + " not found"));
    }

    @Override
    public Item create(Item item, long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException
                        ("User with id=" + ownerId + " not found"));
        item.setOwner(owner);
        itemRepository.save(item);
        log.debug("new item created: {}", item);
        return item;
    }

    public Comment createComment (Comment comment){

        commentRepository.save(comment);
        log.debug("new comment created: {}", comment);
        return comment;
    }

    @Override
    public Item update(Item item) {

        validateUser(item.getOwner().getId());
        itemRepository.save(item);
        log.debug("item updated: {}", item);
        return item;
    }

    @Override
    public List<Item> search(String text) {

        if (!text.isBlank()) {

            return itemRepository.findAll()
                    .stream()
                    .filter(item -> item.getAvailable() &&
                            item.getDescription().toLowerCase().contains(text.toLowerCase())
                            || item.getName().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    private void validateUser(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException
                        ("User with id=" + userId + " not found"));
    }
}


