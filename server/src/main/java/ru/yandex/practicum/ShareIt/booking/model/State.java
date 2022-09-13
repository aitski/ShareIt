package ru.yandex.practicum.ShareIt.booking.model;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    @Override
    public String toString() {
        switch (this){
            case ALL: return "ALL";
            case CURRENT: return "CURRENT";
            case PAST: return "PAST";
            case FUTURE: return "FUTURE";
            case WAITING: return "WAITING";
            case REJECTED: return "REJECTED";
            default: throw new IllegalArgumentException();
        }
    }
}
