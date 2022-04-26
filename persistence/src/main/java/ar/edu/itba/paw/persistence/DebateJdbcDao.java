package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateStatus;
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
                    rs.getLong("creatorid"),
                    rs.getLong("opponentid"),
                    rs.getObject("created_date", LocalDateTime.class),
                    rs.getLong("imageid"),
                    DebateCategory.getFromInt(rs.getInt("category")),
                    DebateStatus.getFromInt(rs.getInt("status")));

    private static final RowMapper<PublicDebate> PUBLIC_ROW_MAPPER = (rs, rowNum) ->
            new PublicDebate(
                    rs.getLong("debateid"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("creatorusername"),
                    rs.getString("opponentusername"),
                    rs.getLong("imageid"),
                    rs.getObject("created_date", LocalDateTime.class),
                    DebateCategory.getFromInt(rs.getInt("category")),
                    rs.getInt("subscribedcount"),
                    DebateStatus.getFromInt(rs.getInt("status")));

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
    public Optional<PublicDebate> getPublicDebateById(long id) {
        return jdbcTemplate.query("SELECT * FROM public_debates WHERE debateId = ?", PUBLIC_ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public List<PublicDebate> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM public_debates LIMIT 10 OFFSET ?", new Object[]{ page * 10 }, PUBLIC_ROW_MAPPER);
    }
    
    @Override
    public int getAllcount() {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates", new Object[]{}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }
    

    @Override
    public List<PublicDebate> getQuery(int page, String query) {
        return jdbcTemplate.query("SELECT * FROM public_debates WHERE name ILIKE ? LIMIT 10 OFFSET ?", new Object[]{ "%" + query + "%", page * 10 }, PUBLIC_ROW_MAPPER);
    }

    @Override
    public int getQueryCount(String query) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates WHERE name ILIKE ?", new Object[]{ "%" + query + "%" }, (rs, rowNum) -> rs.getInt(1)).get(0);
    }
    
    @Override
    public Debate create(String name, String description, Long creatorId, Long opponentId, Long imageId, DebateCategory category) {
        final Map<String, Object> data = new HashMap<>();
        LocalDateTime created = LocalDateTime.now();
        data.put("name", name);
        data.put("description", description);
        data.put("creatorid", creatorId);
        data.put("opponentid", opponentId);
        data.put("created_date", created);
        data.put("imageid", imageId);
        data.put("category", DebateCategory.getFromCategory(category));
        data.put("status", DebateStatus.getFromStatus(DebateStatus.OPEN));

        final Number debateId = jdbcInsert.executeAndReturnKey(data);

        return new Debate(debateId.longValue(), name, description, creatorId, opponentId, created, imageId, category, DebateStatus.OPEN);
    }
    
    @Override
    public List<PublicDebate> getSubscribedDebatesByUsername(long userid, int page) {
        return jdbcTemplate.query("SELECT * FROM public_debates WHERE debateid IN (SELECT debateid FROM subscribed WHERE userid = ?) LIMIT 5 OFFSET ?", new Object[]{userid, page * 5}, PUBLIC_ROW_MAPPER);
    }

    @Override
    public int getSubscribedDebatesByUsernameCount(long userid) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates WHERE debateid IN (SELECT debateid FROM subscribed WHERE userid = ?)", new Object[]{userid}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public List<PublicDebate> getMostSubscribed() {
        return jdbcTemplate.query("SELECT * FROM public_debates WHERE status = ? ORDER BY subscribedcount DESC LIMIT 3", new Object[]{DebateStatus.OPEN.ordinal()}, PUBLIC_ROW_MAPPER);
    }

    @Override
    public List<PublicDebate> getAllFromCategory(DebateCategory category, int page) {
        return jdbcTemplate.query("SELECT * FROM public_debates WHERE category = ? LIMIT 10 OFFSET ?",
                new Object[]{DebateCategory.getFromCategory(category), page * 10},
                PUBLIC_ROW_MAPPER);
    }

    @Override
    public int getAllFromCategoryCount(DebateCategory category) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates WHERE category = ?", new Object[]{DebateCategory.getFromCategory(category)}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }
}
