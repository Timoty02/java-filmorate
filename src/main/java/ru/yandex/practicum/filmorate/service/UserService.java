package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends ru.yandex.practicum.filmorate.service.Service {
    @Autowired
    public UserService(FilmStorage filmStorage, UserStorage userStorage) {
        super(filmStorage, userStorage);
    }

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
        if (userStorage.getUsers().containsKey(user.getId())) {
            if (validateUser(user)) {
                User user1 = userStorage.updateUser(user, user.getId());
                return user1;
            } else {
                throw new ValidationException("Wrong user info");
            }
        } else {
            throw new NotFoundException("User doesn't exist");
        }
    }

    public void addFriend(int firstId, int secondId) {
        ((UserDbStorage) userStorage).addFriend(firstId, secondId);
    }

    public void removeFriend(int firstId, int secondId) {
        ((UserDbStorage) userStorage).removeFriend(firstId, secondId);
    }

    public List<User> getFriends(int id) {
        return ((UserDbStorage) userStorage).getFriends(id);

    }

    public List<User> getCommonFriends(int firstId, int secondId) {
        return ((UserDbStorage) userStorage).getCommonFriends(firstId, secondId);

    }

    public User getUserById(int id) {
        if (userStorage.getUsers().containsKey(id)) {
            return userStorage.getUserById(id);
        } else {
            throw new NotFoundException("User doesn't exist");
        }
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
