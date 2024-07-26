package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage{
    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public Map<Integer, User> getUsers() {
        return null;
    }

    @Override
    public User updateUser(User user, int id) {
        return null;
    }

    @Override
    public User getUserById(int userId) {
        return null;
    }
}
