package ru.yandex.practicum.ShareIt.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingOwnedItemException extends RuntimeException {
    public BookingOwnedItemException(String message) {
        super(message);
    }
}
