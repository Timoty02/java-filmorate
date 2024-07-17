package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    protected int id;
    protected String name;
    protected String description;
    protected LocalDate releaseDate;
    protected int duration;
    protected Set<Integer> likes = new HashSet<>();

    public void addLike(int userId) {
        likes.add(userId);
    }
}
