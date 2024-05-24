package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    List<Film> films = new ArrayList<>();
    int id = 1;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (validateFilm(film)) {
            film.setId(id);
            id++;
            films.add(film);
            log.info("Added new film - " + film);
            return film;
        } else {
            log.warn("Unable to add film - " + film + " due to validation error");
            throw new ValidationException("Wrong film info");
        }
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("List of films sent");
        return films;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        for (Film film1 :
                films) {
            if (film1.getId() == film.getId()) {
                if (validateFilm(film)) {
                    films.add(film);
                    log.info("film " + film + " updated");
                    return film;
                } else {
                    log.warn("Unable to update film - " + film + " due to validation error");
                    throw new ValidationException("Wrong film info");
                }
            }
        }

        log.warn("Unable to update film - " + film + " does not exist");
        throw new ValidationException("film does not exist");

    }

    public boolean validateFilm(Film film) {
        return !film.getName().isBlank() &&
                film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 27)) &&
                film.getDuration() >= 0;
    }


}
