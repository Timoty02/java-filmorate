package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    Integer id;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }
}