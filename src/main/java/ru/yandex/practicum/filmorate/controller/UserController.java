package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    List<User> users = new ArrayList<>();

    @PostMapping
    public User addFilm(@RequestBody User user) {
        if (validateUser(user)) {
            users.add(user);
            log.info("Added new user - " + user);
            return user;
        } else {
            log.warn("Unable to add user - " + user + " due to validation error");
            throw new ValidationException("Wrong user info");
        }
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("List of users sent");
        return users;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("user " + user + " updated");
        return user;
    }

    protected boolean validateUser(User user) {
        return !user.getEmail().isBlank() &&
                user.getEmail().contains("@") &&
                !user.getLogin().isBlank() &&
                !user.getLogin().contains(" ") &&
                user.getBirthday().isBefore(LocalDate.now());
    }
}
