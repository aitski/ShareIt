package ru.yandex.practicum.ShareIt.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommentDto {

    long id;
    String text;
    String authorName;
    LocalDateTime created;

}
