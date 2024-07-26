package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> getAllFilms() {
        String sql = "SELECT f.*, r.name AS rating_name, COUNT(l.user_id) AS likes_count, GROUP_CONCAT(g.name) AS genres " +
                "FROM films f " +
                "LEFT JOIN likes l ON f.id = l.film_id " +
                "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "LEFT JOIN rating r ON f.rating_id = r.id " +
                "GROUP BY f.id";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    public Film getFilmById(int id) {
        String sql = "SELECT f.*, r.name AS rating_name, COUNT(l.user_id) AS likes_count, GROUP_CONCAT(g.name) AS genres " +
                "FROM films f " +
                "LEFT JOIN likes l ON f.id = l.film_id " +
                "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "LEFT JOIN rating r ON f.rating_id = r.id " +
                "WHERE f.id = ? " +
                "GROUP BY f.id";
        return jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
    }

    public void addFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId());
    }

    public void updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId());
    }

    public void deleteFilm(int id) {
        String sql = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class FilmRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));

            // Set Rating
            Mpa mpa = new Mpa();
            mpa.setName(rs.getString("rating_name"));
            film.setMpa(mpa);

            // Set Genres
            String genresString = rs.getString("genres");
            if (genresString != null) {
                String[] genreNames = genresString.split(",");
                for (String genreName : genreNames) {
                    Genre genre = new Genre();
                    genre.setName(genreName.trim());
                    film.addGenre(genre);
                }
            }
            return film;
        }
    }
}
