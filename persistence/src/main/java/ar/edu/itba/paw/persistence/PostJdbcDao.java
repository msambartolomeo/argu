package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PostDao;
import ar.edu.itba.paw.model.Post;
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
public class PostJdbcDao implements PostDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static final RowMapper<Post> ROW_MAPPER = (rs, rowNum) ->
            new Post(rs.getLong("postId"),
                    rs.getLong("userId"),
                    rs.getLong("debateId"),
                    rs.getString("content"));

    @Autowired
    public PostJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("posts")
                .usingGeneratedKeyColumns("postId");
    }

    @Override
    public Optional<Post> getPostById(long postid) {
        return jdbcTemplate.query("SELECT * FROM posts WHERE postId = ?",
                        new Object[]{postid},
                        ROW_MAPPER)
                .stream().findFirst();
    }

    @Override
    public List<Post> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM posts LIMIT 10 OFFSET ?", new Object[]{(page-1) * 10}, ROW_MAPPER);
    }

    @Override
    public Post create(long userId, long debateId, String content) {
        final Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("debateId", debateId);
        data.put("content", content);

        final Number postId = jdbcInsert.executeAndReturnKey(data);

        return new Post(postId.longValue(), userId, debateId, content);
    }
}
