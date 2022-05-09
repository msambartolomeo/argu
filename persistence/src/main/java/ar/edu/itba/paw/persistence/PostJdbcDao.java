package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PostDao;
import ar.edu.itba.paw.model.Post;
import ar.edu.itba.paw.model.PublicPost;
import ar.edu.itba.paw.model.PublicPostWithUserLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostJdbcDao implements PostDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertLikes;

    private static final RowMapper<PublicPost> PUBLIC_POST_ROW_MAPPER = (rs, rowNum) ->
            new PublicPost(rs.getLong("postid"),
                    rs.getString("username"),
                    rs.getLong("debateid"),
                    rs.getString("content"),
                    rs.getInt("likes"),
                    rs.getObject("created_date", Timestamp.class).toLocalDateTime(),
                    rs.getLong("imageid"));

    private static final RowMapper<PublicPostWithUserLike> PUBLIC_POST_WITH_LIKES_ROW_MAPPER = (rs, rowNum) ->
            new PublicPostWithUserLike(rs.getLong("postid"),
                    rs.getString("username"),
                    rs.getLong("debateid"),
                    rs.getString("content"),
                    rs.getInt("likes"),
                    rs.getObject("created_date", Timestamp.class).toLocalDateTime(),
                    rs.getLong("imageid"),
                    rs.getInt("isliked") != 0);

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
    public int getPostsByDebateCount(long debateId) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM posts WHERE debateId = ?", new Object[]{debateId}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public List<PublicPost> getPublicPostsByDebate(long debateId, int page) {
        return jdbcTemplate.query("SELECT * FROM public_posts WHERE debateid = ? ORDER BY created_date LIMIT 5 OFFSET ?",
                new Object[]{debateId, page * 5},
                PUBLIC_POST_ROW_MAPPER);
    }

    @Override
    public List<PublicPostWithUserLike> getPublicPostsByDebateWithIsLiked(long debateId, long userId, int page) {
        return jdbcTemplate.query("SELECT postid, username, debateid, content, likes, created_date, imageid, " +
                "(SELECT COUNT(*) FROM likes WHERE userid = ? AND postid = public_posts.postid) as isliked " +
                "FROM public_posts WHERE debateid = ? ORDER BY created_date LIMIT 5 OFFSET ?", new Object[]{userId, debateId, page * 5}, PUBLIC_POST_WITH_LIKES_ROW_MAPPER);
    }

    @Override
    public Post create(long userId, long debateId, String content, Long imageId) {
        final Map<String, Object> data = new HashMap<>();
        LocalDateTime created = LocalDateTime.now();
        data.put("userId", userId);
        data.put("debateId", debateId);
        data.put("content", content);
        data.put("created_date", created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        data.put("imageid", imageId);

        final Number postId = jdbcInsert.executeAndReturnKey(data);

        return new Post(postId.longValue(), userId, debateId, content, created, imageId);
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

    @Override
    public boolean hasLiked(long postId, long userId) {
        return jdbcTemplate.query("SELECT * FROM likes WHERE postId = ? AND userId = ?", new Object[]{postId, userId}, (rs, rowNum) -> rs.getLong("postId")).size() > 0;
    }
}