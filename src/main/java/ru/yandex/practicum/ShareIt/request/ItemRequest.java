package ru.yandex.practicum.ShareIt.request;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.ShareIt.user.model.User;

import java.time.LocalDate;
@Value
public class ItemRequest {

    long id;
    String description;
    User requestor;
    LocalDate created;
}
