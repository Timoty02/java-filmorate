package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    protected int id;
    protected String email;
    protected String login;
    protected String name = "";
    protected LocalDate birthday;
    protected Set<Integer> friends = new HashSet<>();

    public User(String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    public User() {

    }

    public void addFriend(int userId) {
        friends.add(userId);
    }

    public void removeFriend(int userId) {
        friends.remove(userId);
    }
}
