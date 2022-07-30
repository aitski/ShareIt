package ru.yandex.practicum.ShareIt.item.model;


import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.yandex.practicum.ShareIt.request.ItemRequest;
import ru.yandex.practicum.ShareIt.user.model.User;

import javax.validation.constraints.Size;

@Value
public class ItemDto {
    @NonFinal
    @Setter
    long id;
    @NonFinal
    @Setter
    @Size(max = 50)
    String name;
    @NonFinal
    @Setter
    @Size(max = 3000)
    String description;
    @NonFinal
    @Setter
    Boolean available;
}
