package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
//@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM \"users\" WHERE \"id\" = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> new User(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("login"), resultSet.getString("name"), resultSet.getDate("birthday")));
        if (user != null) {
            String sql1 = "SELECT * FROM \"friends\" f JOIN \"users\" u ON f.\"receiving_user_id\" = u.\"id\" WHERE \"requested_user_id\" = ?";
            Set<Integer> friends = jdbcTemplate.query(sql1, new Object[]{id, id},
                            (resultSet, i) -> new User(resultSet.getInt("receiving_user_id"),
                                    resultSet.getString("email"), resultSet.getString("login"),
                                    resultSet.getString("name"), resultSet.getDate("birthday")))
                    .stream().map(User::getId).collect(Collectors.toSet());
            user.setFriends(friends);
            return user;
        } else {
            return null;
        }
    }

    public Map<Integer, User> getUsers() {
        String sql = "SELECT * FROM \"users\"";
        Map<Integer, User> result = jdbcTemplate.query(sql, new UserRowMapper()).stream().collect(Collectors.toMap(User::getId, Function.identity(), (existing, replacement) -> existing, TreeMap<Integer, User>::new));
        return result;
    }

    public User addUser(User user) {
        String sql = "INSERT INTO \"users\" (\"email\", \"login\", \"name\", \"birthday\") VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
        String sql1 = "SELECT * FROM \"users\" ORDER BY \"id\" DESC LIMIT 1;";
        user = jdbcTemplate.queryForObject(sql1, (resultSet, i) -> new User(resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("login"), resultSet.getString("name"), resultSet.getDate("birthday")));
        return user;
    }

    public User updateUser(User user, int id) {
        String sql = "UPDATE \"users\" SET \"email\" = ?, \"login\" = ?, \"name\" = ?, \"birthday\" = ? WHERE \"id\" = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), id);
        return user;
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM \"users\" WHERE \"id\" = ?";
        jdbcTemplate.update(sql, id);
    }

    public void addFriend(int requestedUserId, int receivingUserId) {
        try {
            String sql = "INSERT INTO \"friends\" (\"requested_user_id\", \"receiving_user_id\") VALUES (?, ?)";
            jdbcTemplate.update(sql, requestedUserId, receivingUserId);
        } catch (DataAccessException e) {
            throw new NotFoundException("User not found");
        }

    }

    public List<Integer> getFriends(int userId) {
        try {
            String sql = "SELECT \"receiving_user_id\" FROM \"friends\" WHERE \"requested_user_id\" = ?";
            return jdbcTemplate.queryForList(sql, new Object[]{userId}, Integer.class);
        } catch (DataAccessException e) {
            throw new NotFoundException("User not found");
        }
    }

    public void removeFriend(int requestedUserId, int receivingUserId) {
        try {
            String sql = "DELETE FROM \"friends\" WHERE \"requested_user_id\" = ? AND \"receiving_user_id\" = ?";
            jdbcTemplate.update(sql, requestedUserId, receivingUserId);
        } catch (DataAccessException e) {
            throw new NotFoundException("User not found");
        }

    }

    public List<Integer> getCommonFriends(int userId1, int userId2) {
        String sql = """
                SELECT DISTINCT f1."receiving_user_id" AS common_friend_id
                FROM "friends" f1
                JOIN "friends" f2 ON f1."receiving_user_id" = f2."receiving_user_id"
                WHERE f1."requested_user_id" = ?
                AND f2."requested_user_id" = ?;
                """;
        return jdbcTemplate.queryForList(sql, new Object[]{userId1, userId2}, Integer.class);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setLogin(resultSet.getString("login"));
            user.setName(resultSet.getString("name"));
            user.setBirthday(resultSet.getObject("birthday", LocalDate.class));
            return user;
        }
    }
}

