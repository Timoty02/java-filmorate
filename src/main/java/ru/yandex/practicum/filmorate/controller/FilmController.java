package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Autowired
    FilmService filmService;


    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        try {
            filmService.addFilm(film);
        } catch (ValidationException e) {
            log.warn("Unable to add film - " + film + " due to validation error");
            throw new ValidationException("Wrong film info");
        }
        log.info("Added new film - " + film);
        return film;
    }

    @GetMapping
    public Map<Integer, Film> getFilms() {
        log.info("List of films sent");
        return filmService.getFilms();
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        try {
            filmService.updateFilm(film);
        } catch (ValidationException e) {
            log.warn("Unable to update film - " + film + " due to validation error");
            throw new ValidationException("Wrong film info");
        } catch (NotFoundException e) {
            log.warn("Unable to update film - " + film + " does not exist");
            throw new NotFoundException("Film does not exist");
        }
        log.info("film " + film + " updated");
        return film;
    }

}
