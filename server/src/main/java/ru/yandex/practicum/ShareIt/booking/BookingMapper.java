package ru.yandex.practicum.ShareIt.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.BookingDto;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.service.ItemService;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingMapper {

    private final ItemService itemService;
    private final UserService userService;

    public Booking convertFromDto(BookingDto bookingDTO, Long userId) {

        User booker = userService.getById(userId);
        Item item = itemService.getById(bookingDTO.getItemId());

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);

        booking.setStart(bookingDTO.getStart());
        booking.setEnd(bookingDTO.getEnd());
        booking.setStatus(Status.WAITING);
        return booking;
    }
}
