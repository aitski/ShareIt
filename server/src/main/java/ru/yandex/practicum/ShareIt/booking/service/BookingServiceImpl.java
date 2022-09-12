package ru.yandex.practicum.ShareIt.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ShareIt.booking.BookingRepository;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.State;
import ru.yandex.practicum.ShareIt.booking.model.Status;
import ru.yandex.practicum.ShareIt.exception.exceptions.BookingOwnedItemException;
import ru.yandex.practicum.ShareIt.exception.exceptions.NotFoundException;
import ru.yandex.practicum.ShareIt.exception.exceptions.OwnershipException;
import ru.yandex.practicum.ShareIt.exception.exceptions.ValidationException;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.user.model.User;
import ru.yandex.practicum.ShareIt.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;

    @Override
    public List<Booking> getAll(String state, long userId, int from, int size) {
        //validate userId
        userService.getById(userId);
        List<Booking> list = new ArrayList<>();
        State stateEnum = State.valueOf(state);
        Pageable pageRequest = PageRequest.of(from/size, size, Sort.by(Sort.Direction.DESC, "end"));

        switch (stateEnum) {

            case ALL:
                list = bookingRepository.findByBooker_Id(userId, pageRequest).getContent();
                break;
            case PAST:
                list = bookingRepository.findByBooker_IdAndEndIsBefore(userId, LocalDateTime.now().withNano(0), pageRequest).getContent();
                break;
            case CURRENT:
                list = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0), pageRequest).getContent();
                break;
            case FUTURE:
                list = bookingRepository.findByBooker_IdAndStartIsAfter(userId, LocalDateTime.now().withNano(0), pageRequest).getContent();
                break;
            case WAITING:
                list = bookingRepository.findByBooker_IdAndStatus(userId, Status.WAITING, pageRequest).getContent();
                break;
            case REJECTED:
                list = bookingRepository.findByBooker_IdAndStatus(userId, Status.REJECTED, pageRequest).getContent();
                break;
        }

        log.debug("list of bookings with state {} from {} size {} returned: {}", state, from, size, list);
        return list;
    }

    public List<Booking> getAllByOwner(String state, long userId, int from, int size) {

        //validate userId
        userService.getById(userId);

        List<Booking> list = new ArrayList<>();
        State stateEnum = State.valueOf(state);
        Pageable pageRequest = PageRequest.of(from/size, size, Sort.by(Sort.Direction.DESC, "end"));

        switch (stateEnum) {

            case ALL:
                list = bookingRepository.findByOwner(userId, pageRequest).getContent();
                break;
            case PAST:
                list = bookingRepository.findByOwnerPast(userId, pageRequest).getContent();
                break;
            case CURRENT:
                list = bookingRepository.findByOwnerCurrent(userId, pageRequest).getContent();
                break;
            case FUTURE:
                list = bookingRepository.findByOwnerFuture(userId, pageRequest).getContent();
                break;
            case WAITING:
                list = bookingRepository.findByOwnerWaiting(userId, pageRequest).getContent();
                break;
            case REJECTED:
                list = bookingRepository.findByOwnerRejected(userId, pageRequest).getContent();
                break;
        }
        log.debug("list of bookings with state {}  from {} size {} for the owner {} returned: {}", state, from, size, userId, list);
        return list;

    }

    @Override
    public Booking getById(long bookingId, long userId) {

        //validate userId
        userService.getById(userId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow
                (() -> {
                    log.error("Booking with id {} not found", bookingId);
                    return new NotFoundException();
                });

        //A booking can be returned only to the booker or the item owner
        User booker = booking.getBooker();
        User owner = booking.getItem().getOwner();

        if (!owner.getId().equals(userId)
                && !booker.getId().equals(userId)) {
            OwnershipException e = new OwnershipException("Item", userId);
            log.error(e.getMessage());
            throw e;
        }

        log.debug("Booking returned {}", booking);
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

        Item item = booking.getItem();
        User booker = booking.getBooker();

        if (item.getOwner().getId().equals(booker.getId())) {
            BookingOwnedItemException e = new BookingOwnedItemException
                    ("Ownership exception");
            log.error("User {} cannot book his own item {}",booker.getId(),  item.getId());
            throw e;
        }

        Booking newBooking = bookingRepository.save(booking);
        log.debug("new booking created: {}", newBooking);
        return newBooking;
    }

    @Override
    public Booking updateStatus(long bookingId, long userId, boolean approved) {

        userService.getById(userId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow
                (() -> {
                    log.error("Booking with id {} not found", bookingId);
                    return new NotFoundException();
                });

        Item item = booking.getItem();

        if (item.getOwner().getId() != userId) {
            OwnershipException e = new OwnershipException("Item", userId);
            log.error(e.getMessage());
            throw e;
        }

        if (approved && booking.getStatus().equals(Status.APPROVED)
                || !approved && booking.getStatus().equals(Status.REJECTED)
        ) {
            ValidationException e = new ValidationException
                    ("Validation error");
            log.error("Status update not required");
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

