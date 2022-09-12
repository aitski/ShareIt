package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDtoGateway {
    @NotBlank
    @Size(max = 255)
    String name;
    @NotBlank
    @Size(max = 3000)
    String description;
    @NotNull
    Boolean available;
    long requestId;
}
