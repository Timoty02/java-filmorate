package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class Service {
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;
}
