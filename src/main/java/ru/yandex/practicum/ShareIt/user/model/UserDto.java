package ru.yandex.practicum.ShareIt.user.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Value
public class UserDto {

    @NonFinal
    @Setter
    long id;

    @NonFinal
    @Setter
    @Size(max = 50)
    String name;

    @Email
    String email;

}
