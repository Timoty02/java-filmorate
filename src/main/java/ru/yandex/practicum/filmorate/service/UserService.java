package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    public User addUser(User user) {
        if (validateUser(user)) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return userStorage.addUser(user);
        } else {
            throw new ValidationException("Wrong user info");
        }
    }


    public User updateUser(User user) {
        if (userStorage.getUsers().containsValue(user)) {
            if (validateUser(user)) {
                userStorage.updateUser(user, user.getId());
                return user;
            } else {
                throw new ValidationException("Wrong user info");
            }
        } else {
            throw new NotFoundException("User doesn't exist");
        }
    }

    public void addFriend(int firstId, int secondId) {
        Map<Integer, User> users = userStorage.getUsers();
        if (users.keySet().containsAll(List.of(firstId, secondId))) {
            User user1 = users.get(firstId);
            User user2 = users.get(secondId);
            if (!user1.getFriends().contains(secondId) & !user2.getFriends().contains(firstId)) {
                user1.addFriend(secondId);
                user2.addFriend(firstId);
            } else {
                throw new ValidationException("Users are already friends");
            }
        } else {
            throw new NotFoundException("One or both users don't exist");
        }
    }

    public void removeFriend(int firstId, int secondId) {
        Map<Integer, User> users = userStorage.getUsers();
        if (users.keySet().containsAll(List.of(firstId, secondId))) {
            User user1 = users.get(firstId);
            User user2 = users.get(secondId);
            if (user1.getFriends().contains(secondId) & user2.getFriends().contains(firstId)) {
                user1.removeFriend(secondId);
                user2.removeFriend(firstId);
            } else {
                throw new ValidationException("Users aren't friends");
            }
        } else {
            throw new NotFoundException("One or both users don't exist");
        }
    }

    public Set<Integer> getMutualFriends(int firstId, int secondId) {
        Map<Integer, User> users = userStorage.getUsers();
        if (users.keySet().containsAll(List.of(firstId, secondId))) {
            User user1 = users.get(firstId);
            User user2 = users.get(secondId);
            Set<Integer> mutualFriends = user1.getFriends();
            mutualFriends.retainAll(user2.getFriends());
            if (!mutualFriends.isEmpty()) {
                return mutualFriends;
            } else {
                throw new NotFoundException("Mutual friends not found");
            }
        } else {
            throw new NotFoundException("One or both users don't exist");
        }
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }

    protected boolean validateUser(User user) {
        return !user.getEmail().isBlank() &&
                user.getEmail().contains("@") &&
                !user.getLogin().isBlank() &&
                !user.getLogin().contains(" ") &&
                user.getBirthday().isBefore(LocalDate.now());
    }
}
