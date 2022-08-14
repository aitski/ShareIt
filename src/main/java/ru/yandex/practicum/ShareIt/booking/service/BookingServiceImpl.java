package ru.yandex.practicum.ShareIt.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.State;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.exception.NotFoundException;

import ru.yandex.practicum.ShareIt.exception.ValidationException;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.item.storage.ItemRepository;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<Booking> getAll(String state, long userId) {

        if (Arrays.stream(State.values()).noneMatch(e -> e.name().equals(state))) {
            ValidationException e =  new ValidationException
                    ("Unknown state: "+state);
            log.error(e.getMessage());
            throw e;
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException
                        ("Item with id=" + userId + " not found"));

        List<Booking> list = new ArrayList<>();
        State stateEnum = State.valueOf(state);
        Sort sort = Sort.by(Sort.Direction.DESC, "end");

        switch (stateEnum) {

            case ALL:
                list = bookingRepository.findByBooker_Id(userId, sort);
                break;
            case PAST:
                list = bookingRepository.findByBooker_IdAndEndIsBefore(userId, LocalDateTime.now(), sort);
                break;
            case CURRENT:
                list = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), sort);
                break;
            case FUTURE:
                list = bookingRepository.findByBooker_IdAndStartIsAfter(userId, LocalDateTime.now(), sort);
                break;
            case WAITING:
                list = bookingRepository.findByBooker_IdAndStatus(userId, Status.WAITING, sort);
                break;
            case REJECTED:
                list = bookingRepository.findByBooker_IdAndStatus(userId, Status.REJECTED, sort);
                break;
        }
        log.debug("list of bookings with state {} returned: {}", state, list);
        return list;
    }

    public List<Booking> getAllByOwner(String state, long userId) {

        if (Arrays.stream(State.values()).noneMatch(e -> e.name().equals(state))) {

            ValidationException e = new ValidationException("Unknown state: " + state);
            log.error(e.getMessage());
            throw e;
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException
                        ("Item with id=" + userId + " not found"));

        List<Booking> list = new ArrayList<>();
        State stateEnum = State.valueOf(state);

        switch (stateEnum) {

            case ALL:
                list = bookingRepository.findByOwner(userId);
                break;
            case PAST:
                list = bookingRepository.findByOwnerPast(userId);
                break;
            case CURRENT:
                list = bookingRepository.findByOwnerCurrent(userId);
                break;
            case FUTURE:
                list = bookingRepository.findByOwnerFuture(userId);
                break;
            case WAITING:
                list = bookingRepository.findByOwnerWaiting(userId);
                break;
            case REJECTED:
                list = bookingRepository.findByOwnerRejected(userId);
                break;
        }
        log.debug("list of bookings with state {} for the owner {} returned: {}", state, userId, list);
        return list;

    }


    @Override
    public Booking getById(long bookingId, long userId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException
                        ("Item with id=" + bookingId + " not found"));

        User booker = userRepository.findById(booking.getBooker().getId()).get();
        Item item = itemRepository.findById(booking.getItem().getId()).get();

        if (!(item.getOwner().getId() == userId
                || booker.getId() == userId)) {
            NotFoundException e = new NotFoundException
                    ("Item does not belong to user or not booked by user");
            log.error(e.getMessage());
            throw e;
        }

        return booking;
    }

    @Override
    public Booking create(Booking booking) {

        if (!booking.getItem().getAvailable()) {
            ValidationException e = new ValidationException
                    ("Item is unavailable");
            log.error(e.getMessage());
            throw e;
        }

        if (itemRepository.findAll()
                .stream().anyMatch
                        (i -> i.getId().equals(booking.getItem().getId())
                                && i.getOwner().getId().equals(booking.getBooker().getId())
                        )) {
            NotFoundException e = new NotFoundException
                    ("Owner " + booking.getBooker().getId() + " and item " + booking.getItem().getId() + " already exist in booking");
            log.error(e.getMessage());
            throw e;
        }

        Booking newBooking = bookingRepository.save(booking);
        log.debug("new booking created: {}", newBooking);
        return newBooking;
    }

    @Override
    public Booking updateStatus(long bookingId, long userId, boolean approved) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException
                        ("Item with id=" + bookingId + " not found"));

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException
                        ("Item with id=" + userId + " not found"));

        Item item = itemRepository.findById(booking.getItem().getId()).get();

        if (item.getOwner().getId() != userId) {
            NotFoundException e = new NotFoundException
                    ("Item does not belong to user");
            log.error(e.getMessage());
            throw e;
        }

        if (approved && booking.getStatus().equals(Status.APPROVED)
                || !approved && booking.getStatus().equals(Status.REJECTED)
        ) {
            ValidationException e = new ValidationException
                    ("Status update not required");
            log.error(e.getMessage());
            throw e;
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        log.debug("booking updated: {}", booking);
        return bookingRepository.save(booking);
    }

}

