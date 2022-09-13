package ru.yandex.practicum.ShareIt.booking.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BookingDto {

    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;

}
