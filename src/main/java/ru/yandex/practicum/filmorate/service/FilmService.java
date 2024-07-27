package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service

public class FilmService extends ru.yandex.practicum.filmorate.service.Service {

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        super(filmStorage, userStorage);
    }

    public Film addFilm(Film film) {
        if (validateFilm(film)) {

            return filmStorage.addFilm(film);
        } else {
            throw new ValidationException("Wrong film info");
        }
    }

    public List<Film> getFilms() {
        return ((FilmDbStorage) filmStorage).getFilms();
    }


    public Film updateFilm(Film film) {
        try {
            return filmStorage.updateFilm(film, film.getId());
        } catch (NotFoundException e) {
            throw new NotFoundException("film does not exist");
        }

    }

    public void addLike(int filmId, int userId) {
        ((FilmDbStorage) filmStorage).addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        ((FilmDbStorage) filmStorage).removeLike(filmId, userId);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getMostPopulars(int count) {
        return ((FilmDbStorage) filmStorage).getMostPopular(count).reversed();
    }

    public boolean validateFilm(Film film) {
        return !film.getName().isBlank() &&
                film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 27)) &&
                film.getDuration() >= 0;
    }
}
