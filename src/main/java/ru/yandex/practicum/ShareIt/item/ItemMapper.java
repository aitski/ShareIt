package ru.yandex.practicum.ShareIt.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.exception.exceptions.OwnershipException;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.model.CommentDto;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.storage.CommentRepository;
import ru.yandex.practicum.ShareIt.request.RequestRepository;
import ru.yandex.practicum.ShareIt.request.model.Request;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemMapper {

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private final RequestRepository requestRepository;

    private final UserService userService;

    public ItemDto convertToDto(Item item, long userId) {

        //validation of userId
        userService.getById(userId);

        Optional<Booking> last = bookingRepository.findFirstByItem_IdAndEndIsBefore(item.getId(), LocalDateTime.now());
        Optional<Booking> next = bookingRepository.findFirstByItem_IdAndStartIsAfter(item.getId(), LocalDateTime.now());

        ItemDto.Booking lastBooking;
        ItemDto.Booking nextBooking;

        if (item.getOwner().getId() == userId && last.isPresent()) {
            lastBooking = new ItemDto.Booking(last.get().getId(), last.get().getBooker().getId());
        } else {
            lastBooking = null;
        }

        if (item.getOwner().getId() == userId && next.isPresent()) {
            nextBooking = new ItemDto.Booking(next.get().getId(), next.get().getBooker().getId());
        } else {
            nextBooking = null;
        }

        List<CommentDto> list = commentRepository.findAllByItem_Id(item.getId())
                .stream().map
                        (commentMapper::convertToDto)
                .collect(Collectors.toList());

        long requestId = 0;

        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                list,
                requestId
        );
    }

    public Item convertFromDtoPatch(Item item, ItemDto itemDto, long userId) {

        //validation of userId
        userService.getById(userId);

        if (item.getOwner().getId() != userId) {
            OwnershipException e = new OwnershipException("Item", userId);
            log.error(e.getText());
            throw e;
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return item;
    }

    public Item convertFromDtoCreate(ItemDto itemDto) {

        Item item = new Item();

        if (itemDto.getName() != null
                && !(itemDto.getName().isBlank())) {
            item.setName(itemDto.getName());
        } else {
            ValidationException e = new ValidationException("Blank name");
            log.error(e.getMessage());
            throw e;
        }

        if (itemDto.getDescription() != null
                && !(itemDto.getDescription().isBlank())) {
            item.setDescription(itemDto.getDescription()
            );
        } else {
            ValidationException e = new ValidationException("Blank description");
            log.error(e.getMessage());
            throw e;
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        } else {
            ValidationException e = new ValidationException("Blank available");
            log.error(e.getMessage());
            throw e;
        }

        if (itemDto.getRequestId() != 0) {
            Request request = requestRepository.findById(itemDto.getRequestId()).orElseThrow(
                    (() -> {
                        log.error("Request with id {} not found", itemDto.getRequestId());
                        return new NotFoundException();
                    }));
            item.setRequest(request);
        }
        return item;
    }
}
