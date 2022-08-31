package ru.yandex.practicum.ShareIt.BookingTests.JUnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.booking.service.BookingServiceImpl;
import ru.yandex.practicum.ShareIt.exception.exceptions.StateException;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceGetAllJUnitTest {

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    UserService userService;

    @InjectMocks
    BookingServiceImpl bookingService;

    User user = new User();

    @Test
    public void whenBookingGetAllStateALL_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByBooker_Id(1L, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAll("ALL", 1, 1, 10));
    }

    @Test
    public void whenBookingGetAllStatePAST_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByBooker_IdAndEndIsBefore(any(), any(), any()))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAll("PAST", 1, 1, 10));
    }

    @Test
    public void whenBookingGetAllStateCURRENT_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAll("CURRENT", 1, 1, 10));
    }

    @Test
    public void whenBookingGetAllStateFUTURE_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByBooker_IdAndStartIsAfter(
                1L,
                LocalDateTime.now(),
                pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAll("FUTURE", 1, 1, 10));
    }

    @Test
    public void whenBookingGetAllStateWAITING_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByBooker_IdAndStatus(1L, Status.WAITING, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAll("WAITING", 1, 1, 10));
    }

    @Test
    public void whenBookingGetAllStateREJECTED_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByBooker_IdAndStatus(1L, Status.REJECTED, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAll("REJECTED", 1, 1, 10));
    }

    @Test
    public void whenBookingGetAllStateUnknown_thenReturnException() {

        when(userService.getById(1))
                .thenReturn(user);

        Assertions.assertThrows(
                StateException.class,
                () -> bookingService.getAll("unknown", 1, 1, 10));
    }
}
