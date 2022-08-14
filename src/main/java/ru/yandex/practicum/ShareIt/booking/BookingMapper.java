package ru.yandex.practicum.ShareIt.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.BookingDTO;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.exception.NotFoundException;
import ru.yandex.practicum.ShareIt.exception.ValidationException;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.storage.ItemRepository;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.storage.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMapper {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Booking convertFromDto(BookingDTO bookingDTO, Long userId) {

        Item item = itemRepository.findById(bookingDTO.getItemId())
                .orElseThrow(() -> new NotFoundException
                        ("Item with id=" + bookingDTO.getItemId() + " not found"));
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException
                        ("Item with id=" + userId + " not found"));

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);

        if (bookingDTO.getStart().isBefore(LocalDateTime.now())
                || bookingDTO.getEnd().isBefore(LocalDateTime.now())) {
            ValidationException e = new ValidationException
                    ("Start or End is in the past");
            log.error(e.getMessage());
            throw e;
        }

        booking.setStart(bookingDTO.getStart());
        booking.setEnd(bookingDTO.getEnd());
        booking.setStatus(Status.WAITING);
        return booking;
    }
}
