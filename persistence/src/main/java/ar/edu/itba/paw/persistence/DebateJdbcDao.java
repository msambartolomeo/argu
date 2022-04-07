package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.DebateDao;
import ar.edu.itba.paw.model.Debate;
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
public class DebateJdbcDao implements DebateDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static final RowMapper<Debate> ROW_MAPPER = (rs, rowNum) ->
            new Debate(rs.getLong("debateId"),
                    rs.getString("name"),
                    rs.getString("description"));

    @Autowired
    public DebateJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("debates")
                .usingGeneratedKeyColumns("debateId");
    }

    @Override
    public Optional<Debate> getPostById(long id) {
        return jdbcTemplate.query("SELECT * FROM debates WHERE debateId = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public List<Debate> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM debates LIMIT 10 OFFSET ?", new Object[]{ (page-1) * 10 }, ROW_MAPPER);
    }

    @Override
    public Debate create(String name, String description) {
        final Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("description", description);

        final Number debateId = jdbcInsert.executeAndReturnKey(data);

        return new Debate(debateId.longValue(), name, description);
    }
}
