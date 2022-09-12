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
import ru.yandex.practicum.ShareIt.booking.service.BookingServiceImpl;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceGetAllOwnerJUnitTest {

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    UserService userService;

    @InjectMocks
    BookingServiceImpl bookingService;

    User user = new User();

    @Test
    public void whenBookingGetAllOwnerStateALL_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByOwner(1L, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAllByOwner("ALL", 1, 0, 10));
    }

    @Test
    public void whenBookingGetAllOwnerStatePAST_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByOwnerPast(1L, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAllByOwner("PAST", 1, 0, 10));
    }

    @Test
    public void whenBookingGetAllOwnerStateCURRENT_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByOwnerCurrent(1L, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAllByOwner("CURRENT", 1, 0, 10));
    }

    @Test
    public void whenBookingGetAllOwnerStateFUTURE_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByOwnerFuture(1L, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAllByOwner("FUTURE", 1, 0, 10));
    }

    @Test
    public void whenBookingGetAllOwnerStateWAITING_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByOwnerWaiting(1L, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAllByOwner("WAITING", 1, 0, 10));
    }

    @Test
    public void whenBookingGetAllOwnerStateREJECTED_thenReturnListOfBookings() {

        when(userService.getById(1))
                .thenReturn(user);

        List<Booking> list = new ArrayList<>();

        Pageable pageBooking = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "end"));

        Page<Booking> page = new PageImpl<>(list, pageBooking, 0);

        when(mockBookingRepository.findByOwnerRejected(1L, pageBooking))
                .thenReturn(page);

        Assertions.assertEquals(list, bookingService.getAllByOwner("REJECTED", 1, 0, 10));
    }
}
