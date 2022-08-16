package ru.yandex.practicum.ShareIt.booking.service;

import ru.yandex.practicum.ShareIt.booking.model.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> getAll(String state, long id);

    List<Booking> getAllByOwner(String state, long userId);

    Booking getById(long bookingId, long userId);

    Booking create(Booking booking);

    Booking updateStatus(long bookingId, long userId, boolean available);
}
