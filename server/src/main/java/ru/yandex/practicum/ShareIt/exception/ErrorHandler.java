package ru.yandex.practicum.ShareIt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.ShareIt.exception.exceptions.OwnershipException;
import ru.yandex.practicum.ShareIt.exception.exceptions.StateException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleStateException(final StateException ex) {
        return new ErrorResponse(String.format("Unknown %s: %s", ex.getName(), ex.getValue()));
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOwnershipException(final OwnershipException ex) {
        return new ErrorResponse(String.format("%s does not belong to user id %s", ex.getText(),ex.getID()));
    }

}
