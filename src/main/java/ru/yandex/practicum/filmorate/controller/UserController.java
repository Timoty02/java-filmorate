package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) {
        try {
            userService.addUser(user);
        } catch (ValidationException e) {
            log.warn("Unable to add user - " + user + " due to validation error");
            throw new ValidationException("Wrong user info");
        }
        log.info("Added new user - " + user);
        return user;
    }

    @GetMapping
    public Map<Integer, User> getUsers() {
        log.info("List of users sent");
        return userService.getUsers();
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
        } catch (ValidationException e) {
            log.warn("Unable to update user - " + user + " due to validation error");
            throw new ValidationException("Wrong user info");
        } catch (NotFoundException e) {
            log.warn("Unable to update user - " + user + " does not exist");
            throw new NotFoundException("User does not exist");
        }
        log.info("user " + user + " updated");
        return user;
    }
}

