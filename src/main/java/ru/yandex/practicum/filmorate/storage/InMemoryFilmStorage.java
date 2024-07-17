package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component

public class InMemoryFilmStorage implements FilmStorage {
    @Getter
    Map<Integer, Film> films = new HashMap<>();
    int id = 1;

    public Film addFilm(Film film) {
        film.setId(id);
        id++;
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film, int filmId) {
        films.put(filmId, film);
        return film;
    }

    public List<Film> getMostPopular() {
        return films.values().stream().
                sorted(Comparator.comparingInt((Film film) -> film.getLikes().size())).
                limit(10).
                collect(Collectors.toList());
    }

    public Film getFilmById(int filmId) {
        return films.get(filmId);
    }
}
