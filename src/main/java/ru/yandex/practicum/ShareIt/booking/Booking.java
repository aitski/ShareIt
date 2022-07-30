package ru.yandex.practicum.ShareIt.booking;

import lombok.Value;
import ru.yandex.practicum.ShareIt.item.model.Item;
import ru.yandex.practicum.ShareIt.user.model.User;

import java.time.LocalDate;

@Value
public class Booking {

    long id;
    LocalDate start;
    LocalDate end;
    Item item;
    User booker;
    String status;
}


