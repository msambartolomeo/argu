package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.DebateCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DebateJdbcDao implements DebateDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static final RowMapper<Debate> ROW_MAPPER = (rs, rowNum) ->
            new Debate(rs.getLong("debateid"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getObject("created_date", LocalDateTime.class),
                    rs.getLong("imageid"),
                    DebateCategory.getFromInt(rs.getInt("category")));

    @Autowired
    public DebateJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("debates")
                .usingGeneratedKeyColumns("debateid");
    }

    @Override
    public Optional<Debate> getDebateById(long id) {
        return jdbcTemplate.query("SELECT * FROM debates WHERE debateId = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public List<Debate> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM debates LIMIT 10 OFFSET ?", new Object[]{ page * 10 }, ROW_MAPPER);
    }

    @Override
    public int getAllcount() {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates", new Object[]{}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public List<Debate> getQuery(int page, String query) {
        return jdbcTemplate.query("SELECT * FROM debates WHERE name ILIKE ? LIMIT 10 OFFSET ?", new Object[]{ "%" + query + "%", page * 10}, ROW_MAPPER);
    }

    @Override
    public int getQueryCount(String query) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates WHERE name ILIKE ?", new Object[]{ "%" + query + "%" }, (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public Debate create(String name, String description, Long imageId, DebateCategory category) {
        final Map<String, Object> data = new HashMap<>();
        LocalDateTime created = LocalDateTime.now();
        data.put("name", name);
        data.put("description", description);
        data.put("created_date", created);
        data.put("imageid", imageId);
        data.put("category", DebateCategory.getFromCategory(category));

        final Number debateId = jdbcInsert.executeAndReturnKey(data);

        return new Debate(debateId.longValue(), name, description, created, imageId, category);
    }
    
    @Override
    public List<Debate> getSubscribedDebatesByUsername(long userid, int page) {
        return jdbcTemplate.query("SELECT * FROM debates WHERE debateid IN (SELECT debateid FROM suscribed WHERE userid = ?) LIMIT 10 OFFSET ?", new Object[]{userid, page * 10}, ROW_MAPPER);
    }

    @Override
    public int getSubscribedDebatesByUsernameCount(long userid) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates WHERE debateid IN (SELECT debateid FROM suscribed WHERE userid = ?)", new Object[]{userid}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public List<Debate> getMostSubscribed() {
        return jdbcTemplate.query("SELECT debateid, name, description, created_date, imageid, category FROM debates NATURAL JOIN suscribed GROUP BY debateid ORDER BY COUNT(userid) DESC LIMIT 3;", ROW_MAPPER);
    }

    @Override
    public List<Debate> getAllFromCategory(DebateCategory category, int page) {
        return jdbcTemplate.query("SELECT * FROM debates WHERE category = ? LIMIT 10 OFFSET ?",
                new Object[]{DebateCategory.getFromCategory(category), page * 10},ROW_MAPPER);
    }

    @Override
    public int getAllFromCategoryCount(DebateCategory category) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates WHERE category = ?", new Object[]{DebateCategory.getFromCategory(category)}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }
}
