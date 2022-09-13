package ru.yandex.practicum.ShareIt.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ItemDto {

    long id;
    String name;
    String description;
    Boolean available;
    Booking lastBooking;
    Booking nextBooking;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Booking {
        long id;
        long bookerId;
    }
    List<CommentDto> comments;
    long requestId;
}
