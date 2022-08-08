package ru.yandex.practicum.ShareIt.user.model;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class User {

    @NonFinal
    @Setter
    long id;

    @NonFinal
    @NotBlank
    @Setter
    @Size(max = 50)
    String name;

    @Email
    @NonFinal
    @NotBlank
    @Setter
    String email;

}
