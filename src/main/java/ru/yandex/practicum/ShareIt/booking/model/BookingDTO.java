package ru.yandex.practicum.ShareIt.booking.model;

import lombok.*;
import ru.yandex.practicum.ShareIt.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BookingDTO {

    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;

}
