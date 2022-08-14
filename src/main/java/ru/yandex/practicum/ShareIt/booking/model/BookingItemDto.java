package ru.yandex.practicum.ShareIt.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BookingItemDto {

    Long id;
    Long bookerId;

}
