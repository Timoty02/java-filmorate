package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) {
        try {
            User addedUser = userService.addUser(user);
            log.info("Added new user - " + user);
            return addedUser;
        } catch (ValidationException e) {
            log.warn("Unable to add user - " + user + " due to validation error");
            throw new ValidationException("Wrong user info");
        }

    }

    @GetMapping
    public List<User> getUsers() {
        log.info("List of users sent");
        return userService.getUsers().values().stream().toList();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        try {
            User user = userService.getUserById(id);
            log.info("User sent");
            return user;
        } catch (NotFoundException e) {
            log.warn("Unable to sent user - " + id + " does not exist");
            throw new NotFoundException("User does not exist");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user);
            log.info("user " + user + " updated");
            return updatedUser;
        } catch (ValidationException e) {
            log.warn("Unable to update user - " + user + " due to validation error");
            throw new ValidationException("Wrong user info");
        } catch (NotFoundException e) {
            log.warn("Unable to update user - " + user + " does not exist");
            throw new NotFoundException("User does not exist");
        }

    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        try {
            userService.addFriend(id, friendId);
            log.info("Friend added");
        } catch (NotFoundException e) {
            log.warn("Unable to add friend user - " + e.getId() + " does not exist");
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            log.warn("Unable to add friend users are already friends");
            throw new ValidationException("Users are already friends");
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        try {
            userService.removeFriend(id, friendId);
            log.info("Friend removed");
        } catch (NotFoundException e) {
            log.warn("Unable to remove friend user - " + e.getId() + " does not exist");
            throw new NotFoundException(e.getMessage());
        } catch (ValidationException e) {
            log.warn("Unable to remove friend users aren't friends");
            //throw new ValidationException("Users aren't friends");
        }
    }

    @GetMapping("/{id}/friends")
    public List<Integer> getFriends(@PathVariable int id) {
        try {
            return userService.getFriends(id);
        } catch (NotFoundException e) {
            log.warn("Unable to get friends user - " + e.getId() + " does not exist");
            throw new NotFoundException(e.getMessage(), e.getId());
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        try {
            return userService.getCommonFriends(id, otherId);
        } catch (NotFoundException e) {
            log.warn("Unable to get friends user - " + e.getId() + " does not exist");
            throw new NotFoundException(e.getMessage(), e.getId());
        }
    }


}

