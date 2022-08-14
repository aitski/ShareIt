package ru.yandex.practicum.ShareIt.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.BookingItemDto;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.exception.NotFoundException;
import ru.yandex.practicum.ShareIt.item.model.CommentDto;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;
import ru.yandex.practicum.ShareIt.item.storage.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemMapper {

    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    public ItemDto convertToDto(Item item, long userId) {

        Optional<Booking> lastBooking = bookingRepository.findFirstByItem_IdAndEndIsBefore(item.getId(), LocalDateTime.now());
        Optional<Booking> nextBooking = bookingRepository.findFirstByItem_IdAndStartIsAfter(item.getId(), LocalDateTime.now());
        BookingItemDto last;
        BookingItemDto next;

        if (item.getOwner().getId() == userId && lastBooking.isPresent()) {
            last = new BookingItemDto(lastBooking.get().getId(), lastBooking.get().getBooker().getId());
        } else {
            last = null;
        }

        if (item.getOwner().getId() == userId && nextBooking.isPresent()) {
            next = new BookingItemDto(nextBooking.get().getId(), nextBooking.get().getBooker().getId());
        } else {
            next = null;
        }

        List<CommentDto> list = commentRepository.findAllByItem_Id(item.getId())
                .stream().map(commentMapper::convertToDto)
                .collect(Collectors.toList());

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                last,
                next,
                list
        );
    }

    public Item convertFromDto(Item item, ItemDto itemDto, long userId) {

        if (item.getOwner().getId() != userId) {
            throw new NotFoundException
                    ("Item does not belong to user");
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
}
