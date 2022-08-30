package ru.yandex.practicum.ShareIt.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ShareIt.booking.model.Booking;
import ru.yandex.practicum.ShareIt.booking.model.BookingDto;
import ru.yandex.practicum.ShareIt.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public Booking create(@Valid @RequestBody BookingDto bookingDTO,
                          @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingService.create(bookingMapper.convertFromDto(bookingDTO, userId));
    }

    @PatchMapping("{bookingId}")
    public Booking updateStatus(@Valid @RequestParam boolean approved,
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
    public List<Booking> getAll(@RequestParam Optional<String> state,
                                @RequestParam(defaultValue = "1", required = false) int from,
                                @RequestParam(defaultValue = "10", required = false) int size,
                                @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingService.getAll(state.orElse("ALL"), userId, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwner(@RequestParam Optional<String> state,
                                       @RequestParam(defaultValue = "0", required = false) int from,
                                       @RequestParam(defaultValue = "10", required = false) int size,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {

        return bookingService.getAllByOwner(state.orElse("ALL"), userId, from, size);
    }
}
