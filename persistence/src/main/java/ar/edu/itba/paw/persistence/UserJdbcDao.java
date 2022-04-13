package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static final RowMapper<User> ROW_MAPPER =
            (rs, rowNum) -> new User(
                    rs.getLong("userid"),
                    rs.getString("email"));

    @Autowired
    public UserJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("users")
                .usingGeneratedKeyColumns("userid");
    }

    @Override
    public Optional<User> getUserById(long id) {
        List<User> query = jdbcTemplate.query("SELECT * FROM users WHERE userid = ?",
                new Object[]{id},
                ROW_MAPPER);

        return query.stream().findFirst();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        List<User> query = jdbcTemplate.query("SELECT * FROM users WHERE email = ?",
                new Object[]{email},
                ROW_MAPPER);

        return query.stream().findFirst();
    }

    @Override
    public User create(String email) {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);

        final Number userId = jdbcInsert.executeAndReturnKey(userData);

        return new User(userId.longValue(), email);
    }

    @Override
    public List<User> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM users LIMIT 10 OFFSET ?", new Object[] { page * 10 }, ROW_MAPPER);
    }

    @Override
    public List<User> getAllUsersByDebate(long debateId) {
        return jdbcTemplate.query("SELECT DISTINCT userid, email FROM users NATURAL JOIN posts WHERE debateid = ?", new Object[] { debateId }, ROW_MAPPER);
    }
}
