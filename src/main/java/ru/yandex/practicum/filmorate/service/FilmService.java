package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service

public class FilmService extends ru.yandex.practicum.filmorate.service.Service {


    public Film addFilm(Film film) {
        if (validateFilm(film)) {
            filmStorage.addFilm(film);
            return film;
        } else {
            throw new ValidationException("Wrong film info");
        }
    }

    public Map<Integer, Film> getFilms() {
        return filmStorage.getFilms();
    }


    public Film updateFilm(Film film) {
        for (Film film1 :
                filmStorage.getFilms().values()) {
            if (film1.getId() == film.getId()) {
                if (validateFilm(film)) {
                    filmStorage.updateFilm(film, film.getId());
                    return film;
                } else {
                    throw new ValidationException("Wrong film info");
                }
            }
        }
        throw new NotFoundException("film does not exist");
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilms().get(filmId);
        if (film == null) {
            throw new NotFoundException("Film not found", filmId);
        } else {
            Set<Integer> filmLikes = film.getLikes();
            if (userStorage.getUsers().containsKey(userId)) {
                if (filmLikes.contains(userId)) {
                    throw new ValidationException("User already liked that film");
                } else {
                    film.addLike(userId);
                    return film;
                }
            } else {
                throw new NotFoundException("User not found", userId);
            }
        }
    }

    public Film removeLike(int filmId, int userId) {
        if(!filmStorage.getFilms().containsKey(filmId)){
            throw new NotFoundException("Film not found");
        } else {
            Film film = filmStorage.getFilms().get(filmId);
            Set<Integer> filmLikes = film.getLikes();
            if (userStorage.getUsers().containsKey(userId)) {
                if (filmLikes.contains(userId)) {
                    filmLikes.remove(userId);
                    film.setLikes(filmLikes);
                    return film;
                } else {
                    throw new ValidationException("User didn't like that film");
                }
            } else {
                throw new NotFoundException("User not found", userId);
            }
        }
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getMostPopulars(int count) {
        return ((InMemoryFilmStorage) filmStorage).getMostPopular(count).reversed();
    }

    public boolean validateFilm(Film film) {
        return !film.getName().isBlank() &&
                film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 27)) &&
                film.getDuration() >= 0;
    }
}
