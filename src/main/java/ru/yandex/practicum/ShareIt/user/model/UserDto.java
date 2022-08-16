package ru.yandex.practicum.ShareIt.user.model;

import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDto {

    long id;
    String name;
    String email;

}
