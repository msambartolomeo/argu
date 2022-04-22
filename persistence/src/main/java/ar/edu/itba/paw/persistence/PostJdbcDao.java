package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PostJdbcDao implements PostDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertLikes;

    private static final RowMapper<Post> ROW_MAPPER = (rs, rowNum) ->
            new Post(rs.getLong("postid"),
                    rs.getLong("userid"),
                    rs.getLong("debateid"),
                    rs.getString("content"),
                    rs.getObject("creationdate", LocalDateTime.class));

    private static final RowMapper<PublicPost> PUBLIC_POST_ROW_MAPPER = (rs, rowNum) ->
            new PublicPost(rs.getLong("postid"),
                    rs.getString("email"),
                    rs.getLong("debateid"),
                    rs.getString("content"),
                    rs.getInt("likes"),
                    rs.getObject("creationdate", LocalDateTime.class));

    @Autowired
    public PostJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("posts")
                .usingGeneratedKeyColumns("postid");
        jdbcInsertLikes = new SimpleJdbcInsert(ds)
                .withTableName("likes");
    }

    @Override
    public Optional<Post> getPostById(long postid) {
        return jdbcTemplate.query("SELECT * FROM posts WHERE postId = ?",
                        new Object[]{postid},
                        ROW_MAPPER)
                .stream().findFirst();
    }

    @Override
    public List<Post> getPostsByDebate(long debateId, int page) {
        return jdbcTemplate.query("SELECT * FROM posts WHERE debateId = ? LIMIT 30 OFFSET ?", new Object[]{debateId, page * 10}, ROW_MAPPER);
    }

    @Override
    public Optional<PublicPost> getPublicPostById(long id) {
        return jdbcTemplate.query("SELECT postid, email, debateid, content FROM posts NATURAL JOIN users WHERE postId = ?",
                new Object[]{id},
                PUBLIC_POST_ROW_MAPPER)
                .stream().findFirst();
    }

    @Override
    public List<PublicPost> getPublicPostsByDebate(long debateId, int page) {
        return jdbcTemplate.query("SELECT postid, email, debateid, content, likes, created_date FROM posts_with_likes NATURAL JOIN users WHERE debateid = ? ORDER BY postid LIMIT 30 OFFSET ?",
                new Object[]{debateId, page * 10},
                PUBLIC_POST_ROW_MAPPER);
    }

    @Override
    public Post create(long userId, long debateId, String content) {
        final Map<String, Object> data = new HashMap<>();
        LocalDateTime created = LocalDateTime.now();
        data.put("userId", userId);
        data.put("debateId", debateId);
        data.put("content", content);
        data.put("creationDate", created);

        final Number postId = jdbcInsert.executeAndReturnKey(data);

        return new Post(postId.longValue(), userId, debateId, content, created);
    }

    @Override
    public void likePost(long postId, long userId) {
        final Map<String, Object> data = new HashMap<>();
        data.put("postId", postId);
        data.put("userId", userId);

        jdbcInsertLikes.execute(data);
    }

    @Override
    public void unlikePost(long postId, long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE postId = ? AND userId = ?", postId, userId);
    }
}
