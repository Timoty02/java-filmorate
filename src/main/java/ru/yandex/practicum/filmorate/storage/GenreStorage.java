package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
@Component
@Qualifier("genreStorage")
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Integer, Genre> getAllGenres() {
        String sql = "SELECT * FROM \"genres\"";
        List<Genre> genres = jdbcTemplate.query(sql, new GenreRowMapper());
        return genres.stream()
                .collect(TreeMap::new, (map, genre) -> map.put(genre.getId(), genre), Map::putAll);
    }

    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM \"genres\" WHERE \"id\" = ?";
        return jdbcTemplate.queryForObject(sql, new GenreRowMapper(), id);
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }
    }
}
