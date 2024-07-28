package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public abstract class Service {
    @Autowired
    @Qualifier("filmDbStorage")
    protected final FilmStorage filmStorage;
    @Autowired
    @Qualifier("userDbStorage")
    protected final UserStorage userStorage;

    protected Service(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }
}
