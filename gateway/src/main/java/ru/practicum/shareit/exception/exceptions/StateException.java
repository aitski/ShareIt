package ru.practicum.shareit.exception.exceptions;

public class StateException extends RuntimeException{

    private final String name;
    private final String value;

    public StateException(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public String getValue() {return value;}


}
