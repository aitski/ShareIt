package ru.yandex.practicum.ShareIt.exception.exceptions;

public class OwnershipException extends RuntimeException{

    private final long id;
    private final String text;

    public OwnershipException(String text, long id) {
        this.id = id;
        this.text = text;
    }
    public long getID() {
        return id;
    }

    public String getText() {
        return text;
    }

}
