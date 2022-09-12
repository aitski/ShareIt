package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestDtoGateway {

    @NotBlank
    @Size(max = 3000)
    private String description;
}
