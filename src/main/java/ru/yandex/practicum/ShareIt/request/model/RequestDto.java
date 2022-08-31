package ru.yandex.practicum.ShareIt.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.ShareIt.item.model.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RequestDto {

    long id;
    String description;
    LocalDateTime created;
    List<ItemDto> items;

}
