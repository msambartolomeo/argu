package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.PublicDebate;
import ar.edu.itba.paw.model.enums.DebateCategory;
import ar.edu.itba.paw.model.enums.DebateOrder;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.enums.DebateVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class DebateJdbcDao implements DebateDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertSubscribed;
    private final SimpleJdbcInsert jdbcInsertVotes;
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
                    DebateStatus.getFromInt(rs.getInt("status")),
                    rs.getInt("forcount"),
                    rs.getInt("againstcount"));

    @Autowired
    public DebateJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("debates")
                .usingGeneratedKeyColumns("debateid");
        this.jdbcInsertSubscribed = new SimpleJdbcInsert(ds)
                .withTableName("subscribed");
        this.jdbcInsertVotes = new SimpleJdbcInsert(ds)
                .withTableName("votes");
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
        return jdbcTemplate.query("SELECT * FROM public_debates ORDER BY created_date DESC LIMIT 5 OFFSET ?", new Object[]{ page * 5 }, PUBLIC_ROW_MAPPER);
    }
    
    @Override
    public int getAllcount() {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates", new Object[]{}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }
    

    @Override
    public List<PublicDebate> getQuery(int page, String query) {
        return jdbcTemplate.query("SELECT * FROM public_debates WHERE name ILIKE ? LIMIT 5 OFFSET ?", new Object[]{ "%" + query + "%", page * 5 }, PUBLIC_ROW_MAPPER);
    }

    @Override
    public int getQueryCount(String query) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates WHERE name ILIKE ?", new Object[]{ "%" + query + "%" }, (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public List<PublicDebate> getSubscribedDebatesByUsername(long userid, int page) {
        return jdbcTemplate.query("SELECT * FROM public_debates WHERE debateid IN (SELECT debateid FROM subscribed WHERE userid = ?) ORDER BY created_date DESC LIMIT 5 OFFSET ?", new Object[]{userid, page * 5}, PUBLIC_ROW_MAPPER);
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
        return jdbcTemplate.query("SELECT * FROM public_debates WHERE category = ? ORDER BY created_date DESC LIMIT 5 OFFSET ?",
                new Object[]{DebateCategory.getFromCategory(category), page * 5},
                PUBLIC_ROW_MAPPER);
    }

    @Override
    public int getAllFromCategoryCount(DebateCategory category) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM debates WHERE category = ?", new Object[]{DebateCategory.getFromCategory(category)}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }
    @Override
    public void subscribeToDebate(long userid, long debateid) {
        final Map<String, Object> data = new HashMap<>();
        data.put("userid", userid);
        data.put("debateid", debateid);
        jdbcInsertSubscribed.execute(data);
    }
    @Override
    public void unsubscribeToDebate(long userid, long debateid) {
        jdbcTemplate.update("DELETE FROM subscribed WHERE userid = ? AND debateid = ?", userid, debateid);
    }
    @Override
    public boolean isUserSubscribed(long userid, long debateid) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM subscribed WHERE userid = ? AND debateid = ?",
                new Object[]{userid, debateid},
                (rs, rowNum) -> rs.getInt(1)).get(0) > 0;
    }

    @Override
    public List<PublicDebate> getPublicDebatesGeneral(int page, int pageSize, String searchQuery, String category, String order, String status, String date) {
        StringBuilder queryString = new StringBuilder("SELECT * FROM public_debates WHERE TRUE");
        List<Object> params = setUpQuery(searchQuery, category, queryString, status, date);

        queryString.append(" ORDER BY");
        DebateOrder orderBy;
        if (order == null)
            orderBy = DebateOrder.DATE_DESC;
        else
            orderBy = DebateOrder.valueOf(order.toUpperCase());

        switch(orderBy) {
            case DATE_ASC:
                queryString.append(" created_date ASC");
                break;
            case DATE_DESC:
                queryString.append(" created_date DESC");
                break;
            case ALPHA_ASC:
                queryString.append(" name ASC");
                break;
            case ALPHA_DESC:
                queryString.append(" name DESC");
                break;
            case SUBS_ASC:
                queryString.append(" subscribedcount ASC");
                break;
            case SUBS_DESC:
                queryString.append(" subscribedcount DESC");
                break;
        }

        queryString.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(page * pageSize);

        System.out.println(queryString);
        return jdbcTemplate.query(queryString.toString(), params.toArray(), PUBLIC_ROW_MAPPER);
    }

    @Override
    public int getPublicDebatesCount(String searchQuery, String category, String status, String date) {
        StringBuilder queryString = new StringBuilder("SELECT COUNT(*) FROM public_debates WHERE TRUE");
        List<Object> params = setUpQuery(searchQuery, category, queryString, status, date);
        return jdbcTemplate.query(queryString.toString(), params.toArray(), (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    private List<Object> setUpQuery(String searchQuery, String category, StringBuilder queryString, String status, String date) {
        List<Object> params = new ArrayList<>();

        if(searchQuery != null) {
            queryString.append(" AND name ILIKE ?");
            params.add("%" + searchQuery + "%");
        }
        if(category != null) {
            queryString.append(" AND category = ?");
            params.add(DebateCategory.getFromCategory(DebateCategory.valueOf(category.toUpperCase())));
        }
        if (status != null) {
            queryString.append(" AND status = ?");
            params.add(DebateStatus.getFromStatus(DebateStatus.valueOf(status.toUpperCase())));
        }
        if (date != null) {
            LocalDateTime dateTime = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay();
            queryString.append(" AND created_date >= ?");
            params.add(dateTime);
            queryString.append(" AND created_date <= ?");
            params.add(dateTime.plusDays(1));
        }
        return params;
    }

    @Override
    public List<PublicDebate> getMyDebates(long userid, int page) {
        return jdbcTemplate.query("SELECT DISTINCT debateid, name, description, category, public_debates.created_date, public_debates.imageid, creatorusername, opponentusername, subscribedcount, status FROM public_debates JOIN users ON username = creatorusername OR username = opponentusername WHERE userid = ? ORDER BY public_debates.created_date DESC LIMIT 5 OFFSET ?",
                new Object[] {userid, page * 5},
                PUBLIC_ROW_MAPPER);
    }

    @Override
    public int getMyDebatesCount(long userid) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM public_debates JOIN users ON username = creatorusername OR username = opponentusername WHERE userid = ?", new Object[] {userid}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public void addVote(long debateId, long userId, DebateVote vote) {
        final Map<String, Object> params = new HashMap<>();
        params.put("debateid", debateId);
        params.put("userid", userId);
        params.put("vote", vote.ordinal());
        jdbcInsertVotes.execute(params);
    }

    @Override
    public void removeVote(long debateId, long userId) {
        jdbcTemplate.update("DELETE FROM votes WHERE debateid = ? AND userid = ?", debateId, userId);
    }

    @Override
    public Boolean hasUserVoted(long debateId, long userId) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM votes WHERE debateid = ? AND userid = ?", new Object[] {debateId, userId},
                (rs, rowNum) -> rs.getInt(1) > 0).get(0);
    }
}
