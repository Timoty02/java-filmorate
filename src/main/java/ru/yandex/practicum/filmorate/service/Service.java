package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class Service {
    @Autowired
    //@Qualifier("filmDbStorage")
            @Qualifier("inMemoryFilmStorage")
    FilmStorage filmStorage;
    @Autowired
    //@Qualifier("userDbStorage")
            @Qualifier("inMemoryUserStorage")
    UserStorage userStorage;
}
