package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
//@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Film> getFilms() {
        String sql = "SELECT f.*, r.\"name\" AS rating_name, GROUP_CONCAT(l.\"user_id\") AS likes_count,GROUP_CONCAT(g.\"id\") AS genres_ids, GROUP_CONCAT(g.\"name\") AS genres " +
                "FROM \"films\" f " +
                "LEFT JOIN \"likes\" l ON f.\"id\" = l.\"film_id\" " +
                "LEFT JOIN \"film_genres\" fg ON f.\"id\" = fg.\"film_id\" " +
                "LEFT JOIN \"genres\" g ON fg.\"genre_id\" = g.\"id\" " +
                "LEFT JOIN \"rating\" r ON f.\"rating_id\" = r.\"id\" " +
                "GROUP BY f.\"id\"";
        return jdbcTemplate.query(sql, new FilmRowMapper());//.stream().collect(java.util.stream.Collectors.toMap(Film::getId, Function.identity(), (existing, replacement) -> existing, TreeMap<Integer, Film>::new));
        /*String sql = "SELECT \"id\" FROM \"films\"";
        List<Integer> count = jdbcTemplate.queryForList(sql, Integer.class);
        Map<Integer, Film> films = new TreeMap<>();
        for (Integer i : count) {
            films.put(i, getFilmById(i));
        }
        return films;*/
    }

    public Film getFilmById(int id) {
        String sql = "SELECT f.*, r.\"name\" AS rating_name, GROUP_CONCAT(l.\"user_id\") AS likes_count,GROUP_CONCAT(g.\"id\") AS genres_ids, GROUP_CONCAT(g.\"name\") AS genres " +
                "FROM \"films\" f " +
                "LEFT JOIN \"likes\" l ON f.\"id\" = l.\"film_id\" " +
                "LEFT JOIN \"film_genres\" fg ON f.\"id\" = fg.\"film_id\" " +
                "LEFT JOIN \"genres\" g ON fg.\"genre_id\" = g.\"id\" " +
                "LEFT JOIN \"rating\" r ON f.\"rating_id\" = r.\"id\" " +
                "WHERE f.\"id\" = ? " +
                "GROUP BY f.\"id\"";
        Film film = jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
        return film;
    }

    public Film addFilm(Film film) {
        try {
            String sql = "INSERT INTO \"films\" (\"name\", \"description\", \"release_date\", \"duration\", \"rating_id\") VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    film.getMpa().getId());
            String sql2 = "SELECT \"id\" FROM \"films\" ORDER BY \"id\" DESC limit 1";
            Integer film_id = jdbcTemplate.queryForObject(sql2, Integer.class);
            for (Genre genre : film.getGenres()) {
                int genreId = genre.getId();
                String sql3 = "INSERT INTO \"film_genres\" (\"film_id\", \"genre_id\") VALUES (?, ?)";
                jdbcTemplate.update(sql3, film_id, genreId);
            }
            return getFilmById(film_id);
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO \"likes\" (\"film_id\", \"user_id\") VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM \"likes\" WHERE \"film_id\" = ? AND \"user_id\" = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public Film updateFilm(Film film, int id) {
        String sql = "UPDATE \"films\" SET \"name\" = ?, \"description\" = ?, \"release_date\" = ?, \"duration\" = ?, \"rating_id\" = ? WHERE \"id\" = ?";
        int i = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), id);
        if (i == 0) {
            throw new NotFoundException("Film not found");
        }
        return film;
    }

    public void deleteFilm(int id) {
        String sql = "DELETE FROM \"films\" WHERE \"id\" = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Film> getMostPopular(int count) {
        String sql = "SELECT f.*, r.\"name\" AS rating_name, GROUP_CONCAT(l.\"user_id\") AS likes_count,GROUP_CONCAT(g.\"id\") AS genres_ids, GROUP_CONCAT(g.\"name\") AS genres " +
                "FROM \"films\" f " +
                "LEFT JOIN \"likes\" l ON f.\"id\" = l.\"film_id\" " +
                "LEFT JOIN \"film_genres\" fg ON f.\"id\" = fg.\"film_id\" " +
                "LEFT JOIN \"genres\" g ON fg.\"genre_id\" = g.\"id\" " +
                "LEFT JOIN \"rating\" r ON f.\"rating_id\" = r.\"id\" " +
                "GROUP BY f.\"id\"" +
                "ORDER BY count(l.\"user_id\") " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, new FilmRowMapper(), count);
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
            mpa.setId(rs.getInt("rating_id"));
            film.setMpa(mpa);
            // Set Genres
            String genresString = rs.getString("genres");
            String genresIdsString = rs.getString("genres_ids");
            if (genresIdsString != null) {
                String[] genreIds = genresIdsString.split(",");
                String[] genreNames = genresString.split(",");
                for (int i = 0; i < genreIds.length; i++) {
                    Genre genre = new Genre();
                    genre.setId(Integer.parseInt(genreIds[i]));
                    genre.setName(genreNames[i].trim());
                    film.addGenre(genre);
                }
            }
            // Set Likes
            String likesCountString = rs.getString("likes_count");
            if (likesCountString != null) {
                String[] likesCount = likesCountString.split(",");
                for (int i = 0; i < likesCount.length; i++) {
                    film.addLike(Integer.parseInt(likesCount[i]));
                }
            }
            return film;
        }
    }
}
