package ru.yandex.practicum.ShareIt.BookingTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.booking.service.BookingService;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.service.ItemService;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceIntegrTest {

    @Autowired
    BookingService bookingService;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Test
    public void whenBookingGetAll_thenReturnBookingsList(){

        User owner = new User();
        owner.setName("user");
        owner.setEmail("user@mail.ru");

        User user1 = new User();
        user1.setName("booker");
        user1.setEmail("booker@mail.ru");

        userService.create(owner);
        User booker = userService.create(user1);

        Item item = new Item();
        item.setName("item");
        item.setDescription("desc");
        item.setAvailable(true);

        Item newItem = itemService.create(item,owner.getId());

        Booking bookingCur = new Booking();
        bookingCur.setBooker(booker);
        bookingCur.setItem(newItem);
        bookingCur.setStatus(Status.WAITING);
        bookingCur.setStart(LocalDateTime.of(2020,12,1,10,55));
        bookingCur.setEnd(LocalDateTime.of(2024,12,1,10,55));

        Booking bookingRej = new Booking();
        bookingRej.setBooker(booker);
        bookingRej.setItem(item);
        bookingRej.setStatus(Status.REJECTED);
        bookingRej.setStart(LocalDateTime.of(2020,12,1,10,55));
        bookingRej.setEnd(LocalDateTime.of(2024,12,1,10,55));

        Booking bookingPast = new Booking();
        bookingPast.setBooker(booker);
        bookingPast.setItem(item);
        bookingPast.setStatus(Status.APPROVED);
        bookingPast.setStart(LocalDateTime.of(2020,12,1,10,55));
        bookingPast.setEnd(LocalDateTime.of(2021,12,1,10,55));

        Booking bookingFut = new Booking();
        bookingFut.setBooker(booker);
        bookingFut.setItem(item);
        bookingFut.setStatus(Status.APPROVED);
        bookingFut.setStart(LocalDateTime.of(2023,12,1,10,55));
        bookingFut.setEnd(LocalDateTime.of(2025,12,1,10,55));

        bookingService.create(bookingCur);
        bookingService.create(bookingRej);
        bookingService.create(bookingPast);
        bookingService.create(bookingFut);

        Assertions.assertEquals(List.of(bookingCur,bookingRej),bookingService.getAll("CURRENT", booker.getId(), 1, 10));
        Assertions.assertEquals(List.of(bookingCur),bookingService.getAll("WAITING", booker.getId(), 1, 10));
        Assertions.assertEquals(List.of(bookingRej),bookingService.getAll("REJECTED", booker.getId(), 1, 10));
        Assertions.assertEquals(List.of(bookingPast),bookingService.getAll("PAST", booker.getId(), 1, 10));
        Assertions.assertEquals(List.of(bookingFut),bookingService.getAll("FUTURE", booker.getId(), 1, 10));
        Assertions.assertEquals(List.of(bookingFut,bookingCur,bookingRej,bookingPast),
                bookingService.getAll("ALL", booker.getId(), 1, 10));
    }
}
