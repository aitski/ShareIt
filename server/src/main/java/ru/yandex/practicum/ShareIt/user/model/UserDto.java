package ru.yandex.practicum.ShareIt.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDto {

    long id;
    String name;
    String email;

}
