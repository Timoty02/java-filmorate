package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
    public Map<Integer, Mpa> getAllMpa() {
        String sql = "SELECT * FROM \"rating\"";
        List<Mpa> mpas = jdbcTemplate.query(sql, new MpaStorage.MpaRowMapper());
        return mpas.stream()
                .collect(TreeMap::new, (map, mpa) -> map.put(mpa.getId(), mpa), Map::putAll);
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
