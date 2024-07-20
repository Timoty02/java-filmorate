package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Autowired
    FilmService filmService;

    protected final String pathLike = "/{id}/like/{user-id}";

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
    public List<Film> getFilms() {
        log.info("List of films sent");
        return filmService.getFilms().values().stream().toList();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        try {
            Film film = filmService.getFilmById(id);
            log.info("Film sent");
            return film;
        } catch (NotFoundException e) {
            log.warn("Unable to sent film - " + id + " does not exist");
            throw new NotFoundException("Film does not exist");
        }
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

    @PutMapping(pathLike)
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        try {
            Film film = filmService.addLike(id, userId);
            log.info("User " + userId + " liked film " + id);
            return film;
        } catch (ValidationException e) {
            log.warn("User " + userId + " already liked that film");
            throw new ValidationException("User already liked that film");
        } catch (NotFoundException e) {
            log.warn("User " + userId + " does not exist");
            throw new NotFoundException("User does not exist");
        }
    }

    @DeleteMapping(pathLike)
    public void removeLike(@PathVariable int id, @PathVariable("user-id") int userId) {
        try {
            filmService.removeLike(id, userId);
        } catch (ValidationException e) {
            log.warn("User " + userId + " didn't like that film");
            throw new ValidationException(e.getMessage());
        } catch (NotFoundException e) {
            log.warn("User " + userId + " does not exist");
            throw new NotFoundException("User does not exist");
        }
        log.info("User " + userId + " unliked film " + id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopular(@RequestParam(defaultValue = "10") int count) {
        try {
            List<Film> films = filmService.getMostPopulars(count);
            log.info("List sent");
            return films;
        } catch (RuntimeException e) {
            log.warn("Unknown error");
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}
