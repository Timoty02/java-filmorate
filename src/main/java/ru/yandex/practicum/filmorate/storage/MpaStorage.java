package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier("mpaStorage")
public class MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM \"rating\" WHERE \"id\" = ?";
        return jdbcTemplate.queryForObject(sql, new MpaRowMapper(), id);
    }

    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM \"rating\"";
        List<Mpa> mpas = jdbcTemplate.query(sql, new MpaStorage.MpaRowMapper());
        return mpas;
    }

    static class MpaRowMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("id"));
            mpa.setName(rs.getString("name"));
            return mpa;
        }
    }

}
