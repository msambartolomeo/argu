package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
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
    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(rs.getLong("userId"), rs.getString("username"), rs.getString("password"));

    @Autowired
    public UserJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("users")
                .usingGeneratedKeyColumns("userid");
    }

    @Override
    public Optional<User> getUserById(long id) {
        List<User> query = jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", new Object[]{id}, ROW_MAPPER);

        return query.stream().findFirst();
    }

    @Override
    public User create(String username, String password) {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);

        final Number userId = jdbcInsert.executeAndReturnKey(userData);

        return new User(userId.longValue(), username, password);
    }

    @Override
    public List<User> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM users LIMIT 10 OFFSET ?", new Object[] { (page - 1) * 10}, ROW_MAPPER);
    }
}
