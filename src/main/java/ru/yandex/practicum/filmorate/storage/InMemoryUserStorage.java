package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

//@Component
//@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    @Getter
    Map<Integer, User> users = new HashMap<>();
    int id = 1;

    public User addUser(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }


    public User updateUser(User user, int userId) {
        users.put(userId, user);
        return user;
    }

    public User getUserById(int userId) {
        return users.get(userId);
    }

}
