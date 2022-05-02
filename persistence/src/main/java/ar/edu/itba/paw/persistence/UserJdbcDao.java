package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.*;

@Repository
public class UserJdbcDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static final RowMapper<User> ROW_MAPPER =
            (rs, rowNum) -> new User(
                    rs.getLong("userid"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getObject("created_date", LocalDate.class),
                    rs.getLong("imageid"),
                    UserRole.getRole(rs.getInt("role")));

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
    public Optional<User> getUserByUsername(String username) {
        List<User> query = jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                new Object[]{username},
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
    public User create(String username, String password, String email) {
        final Map<String, Object> userData = new HashMap<>();
        LocalDate created = LocalDate.now();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("email", email);
        userData.put("created_date", created);
        userData.put("role", UserRole.getValue(UserRole.USER));

        final Number userId = jdbcInsert.executeAndReturnKey(userData);

        return new User(userId.longValue(), username, password, email, created, UserRole.USER);
    }

    @Override
    public User updateLegacyUser(long userId, String username, String password, String email) {
        LocalDate created = LocalDate.now();
        jdbcTemplate.update("UPDATE users SET username = ?, password = ?, created_date = ?, role = ? WHERE email = ?", username, password, created, UserRole.USER.ordinal(), email);
        return new User(userId, username, password, email, created, UserRole.USER);
    }

    @Override
    public List<User> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM users LIMIT 10 OFFSET ?", new Object[] { page * 10 }, ROW_MAPPER);
    }

    @Override
    public List<User> getSubscribedUsersByDebate(long debateId) {
        return jdbcTemplate.query("SELECT DISTINCT userid, username, password, email, created_date, imageid, role FROM users NATURAL JOIN subscribed WHERE debateid = ?", new Object[] { debateId }, ROW_MAPPER);

    }

    @Override
    public void updateImage(long userId, long imageId) {
        jdbcTemplate.update("UPDATE users SET imageid = ? WHERE userid = ?", imageId, userId);
    }
}
