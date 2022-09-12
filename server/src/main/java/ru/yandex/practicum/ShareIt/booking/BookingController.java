package ru.yandex.practicum.ShareIt.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.BookingDto;
import ru.yandex.practicum.ShareIt.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor

public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public Booking create(@RequestBody BookingDto bookingDTO,
                          @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingService.create(bookingMapper.convertFromDto(bookingDTO, userId));
    }

    @PatchMapping("{bookingId}")
    public Booking updateStatus(@RequestParam boolean approved,
                                @RequestHeader("X-Sharer-User-Id") long userId,
                                @PathVariable long bookingId) {

        return bookingService.updateStatus(bookingId, userId, approved);
    }

    @GetMapping("{bookingId}")
    public Booking getById(@PathVariable long bookingId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping()
    public List<Booking> getAll(@RequestParam String state,
                                @RequestParam int from,
                                @RequestParam int size,
                                @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingService.getAll(state, userId, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwner(@RequestParam String state,
                                       @RequestParam int from,
                                       @RequestParam int size,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingService.getAllByOwner(state, userId, from, size);
    }
}
