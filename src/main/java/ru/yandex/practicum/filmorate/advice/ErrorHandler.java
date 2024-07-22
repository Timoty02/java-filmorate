package ru.yandex.practicum.filmorate.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of(
                "error", "Validation error",
                "errorMessage", e.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        Map<String, String> returnMap;
        if (e.getId() != null) {
            returnMap = Map.of(
                    "error", "NotFound error",
                    "errorMessage", e.getMessage(),
                    "id", e.getId().toString()
            );
        } else {
            returnMap = Map.of(
                    "error", "NotFound error",
                    "errorMessage", e.getMessage()
            );
        }
        return returnMap;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleExceptions(final RuntimeException e) {
        return Map.of(
                "error", "Unknown error",
                "errorMessage", e.getMessage()
        );
    }
}
