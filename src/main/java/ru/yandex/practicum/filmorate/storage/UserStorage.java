package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    Map<Integer, User> getUsers();

    User updateUser(User user, int id);

    User getUserById(int userId);
}
