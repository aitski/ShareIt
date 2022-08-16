package ru.yandex.practicum.ShareIt.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
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

        Optional<Booking> last = bookingRepository.findFirstByItem_IdAndEndIsBefore(item.getId(), LocalDateTime.now());
        Optional<Booking> next = bookingRepository.findFirstByItem_IdAndStartIsAfter(item.getId(), LocalDateTime.now());

        ItemDto.Booking lastBooking;
        ItemDto.Booking nextBooking;

        if (item.getOwner().getId() == userId && last.isPresent() ) {
            lastBooking = new ItemDto.Booking(last.get().getId(), last.get().getBooker().getId());
        } else {
            lastBooking = null;
        }

        if (item.getOwner().getId() == userId && next.isPresent() ) {
            nextBooking = new ItemDto.Booking(next.get().getId(), next.get().getBooker().getId());
        } else {
            nextBooking = null;
        }

        List<CommentDto> list = commentRepository.findAllByItem_Id(item.getId())
                .stream().map(commentMapper::convertToDto)
                .collect(Collectors.toList());

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
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
