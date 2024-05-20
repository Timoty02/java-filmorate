package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();
    int id = 1;

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (validateUser(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(id);
            id++;
            users.put(user.getId(), user);
            log.info("Added new user - " + user);
            return user;
        } else {

            log.warn("Unable to add user - " + user + " due to validation error");
            throw new ValidationException("Wrong user info");
        }
    }

    @GetMapping
    public Map<Integer, User> getUsers() {
        log.info("List of users sent");
        return users;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (validateUser(user)) {
                if (user.getName().isBlank()) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                log.info("user " + user + " updated");
                return user;
            } else {
                log.warn("Unable to update user - " + user + " due to validation error");
                throw new ValidationException("Wrong user info");
            }
        } else {
            log.warn("Unable to update user - " + user + " does not exist");
            throw new ValidationException("User does not exist");
        }
    }

    protected boolean validateUser(User user) {
        return !user.getEmail().isBlank() &&
                user.getEmail().contains("@") &&
                !user.getLogin().isBlank() &&
                !user.getLogin().contains(" ") &&
                user.getBirthday().isBefore(LocalDate.now());
    }
}
