package ru.yandex.practicum.ShareIt.BookingTests.JUnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.exception.exceptions.BookingOwnedItemException;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.service.BookingServiceImpl;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.exception.exceptions.OwnershipException;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.service.ItemServiceImpl;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceJUnitTest {

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    UserService userService;

    @InjectMocks
    BookingServiceImpl bookingService;

    User user = new User();

    @Test
    public void whenGetById_thenReturnBooking() {

        when(userService.getById(1))
                .thenReturn(user);

        user.setId(1L);

        Item item = new Item();
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);

        when(mockBookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        Assertions.assertEquals(booking, bookingService.getById(1, 1));
    }

    @Test
    public void whenGetById_thenReturnNotFoundException() {

        when(userService.getById(1))
                .thenReturn(user);

        when(mockBookingRepository.findById(1L))
                .thenThrow(new NotFoundException());

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getById(1, 1));
    }

    @Test
    public void whenGetById_thenReturnOwnershipException() {

        when(userService.getById(1))
                .thenReturn(user);

        user.setId(1L);

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(owner);
        booking.setItem(item);

        when(mockBookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        Assertions.assertThrows(OwnershipException.class,
                () -> bookingService.getById(1, 1));
    }

    @Test
    public void whenCreateNewBooking_thenReturnNewBookingWithId() {

        user.setId(1L);
        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);

        Booking bookingDAO = new Booking();
        bookingDAO.setItem(item);
        bookingDAO.setBooker(user);
        bookingDAO.setId(1L);

        when(mockBookingRepository.save(booking))
                .thenReturn(bookingDAO);

        Assertions.assertEquals(bookingDAO, bookingService.create(booking));
    }

    @Test
    public void whenCreateNewBookingNotAvailable_thenReturnException() {

        Item item = new Item();
        item.setAvailable(false);

        Booking booking = new Booking();
        booking.setItem(item);

        Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.create(booking));
    }

    @Test
    public void whenCreateNewBookingOwnItem_thenReturnException() {

        user.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);

        Assertions.assertThrows(
                BookingOwnedItemException.class,
                () -> bookingService.create(booking));
    }

    @Test
    public void whenUpdateBooking_thenReturnUpdatedBookingApproved() {

        user.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        when(userService.getById(1))
                .thenReturn(user);

        when(mockBookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        bookingService.updateStatus(1, 1, true);
        Assertions.assertEquals(Status.APPROVED, booking.getStatus());
    }

    @Test
    public void whenUpdateBooking_thenReturnUpdatedBookingRejected() {

        user.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        when(userService.getById(1))
                .thenReturn(user);

        when(mockBookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        bookingService.updateStatus(1, 1, false);
        Assertions.assertEquals(Status.REJECTED, booking.getStatus());
    }

    @Test
    public void whenUpdateBooking_thenReturnOwnershipException() {

        user.setId(1L);

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        when(userService.getById(1))
                .thenReturn(user);

        when(mockBookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        Assertions.assertThrows(
                OwnershipException.class,
                () -> bookingService.updateStatus(1, 1, true));
    }

    @Test
    public void whenUpdateBookingAlreadyApproved_thenReturnValidationException() {

        user.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);

        when(userService.getById(1))
                .thenReturn(user);

        when(mockBookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.updateStatus(1, 1, true));
    }

    @Test
    public void whenUpdateBookingAlreadyRejected_thenReturnValidationException() {

        user.setId(1L);

        Item item = new Item();
        item.setAvailable(true);
        item.setOwner(user);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(Status.REJECTED);

        when(userService.getById(1))
                .thenReturn(user);

        when(mockBookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        Assertions.assertThrows(
                ValidationException.class,
                () -> bookingService.updateStatus(1, 1, false));
    }
}
