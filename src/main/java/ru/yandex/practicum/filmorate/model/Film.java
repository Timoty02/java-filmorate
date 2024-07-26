package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    protected int id;
    protected String name;
    protected String description;
    protected Mpa mpa;
    protected Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
    protected LocalDate releaseDate;
    protected int duration;
    protected Set<Integer> likes = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

}
