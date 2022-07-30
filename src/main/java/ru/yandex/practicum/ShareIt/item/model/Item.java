package ru.yandex.practicum.ShareIt.item.model;


import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class Item {
    @NonFinal
    @Setter
    long id;
    @NonFinal
    @NotBlank
    @Setter
    @Size(max = 50)
    String name;
    @NonFinal
    @NotBlank
    @Setter
    @Size(max = 3000)
    String description;
    @NotNull
    @NonFinal
    @Setter
    Boolean available;
    @NonFinal
    @Setter
    long ownerId;
    @NonFinal
    @Setter
    long requestId;

}