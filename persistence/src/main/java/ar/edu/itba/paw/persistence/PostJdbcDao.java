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
import java.time.LocalDateTime;
import java.util.*;

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
                    rs.getObject("created_date", LocalDateTime.class),
                    rs.getLong("imageid"));

    private static final RowMapper<PublicPost> PUBLIC_POST_ROW_MAPPER = (rs, rowNum) ->
            new PublicPost(rs.getLong("postid"),
                    rs.getString("username"),
                    rs.getLong("debateid"),
                    rs.getString("content"),
                    rs.getInt("likes"),
                    rs.getObject("created_date", LocalDateTime.class),
                    rs.getLong("imageid"));

    private static final RowMapper<PublicPostWithUserLike> PUBLIC_POST_WITH_LIKES_ROW_MAPPER = (rs, rowNum) ->
            new PublicPostWithUserLike(rs.getLong("postid"),
                    rs.getString("username"),
                    rs.getLong("debateid"),
                    rs.getString("content"),
                    rs.getInt("likes"),
                    rs.getObject("created_date", LocalDateTime.class),
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
    public Optional<Post> getPostById(long postid) {
        return jdbcTemplate.query("SELECT * FROM posts WHERE postId = ?",
                        new Object[]{postid},
                        ROW_MAPPER)
                .stream().findFirst();
    }

    @Override
    public List<Post> getPostsByDebate(long debateId, int page) {
        if (page < 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.query("SELECT * FROM posts WHERE debateId = ? ORDER BY created_date LIMIT 5 OFFSET ?", new Object[]{debateId, page * 5}, ROW_MAPPER);
    }

    @Override
    public int getPostsByDebateCount(long debateId) {
        return jdbcTemplate.query("SELECT COUNT(*) FROM posts WHERE debateId = ?", new Object[]{debateId}, (rs, rowNum) -> rs.getInt(1)).get(0);
    }

    @Override
    public Optional<PublicPost> getPublicPostById(long id) {
        return jdbcTemplate.query("SELECT * FROM public_posts WHERE postId = ?",
                new Object[]{id},
                PUBLIC_POST_ROW_MAPPER)
                .stream().findFirst();
    }

    @Override
    public List<PublicPost> getPublicPostsByDebate(long debateId, int page) {
        if (page < 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.query("SELECT * FROM public_posts WHERE debateid = ? ORDER BY created_date LIMIT 5 OFFSET ?",
                new Object[]{debateId, page * 5},
                PUBLIC_POST_ROW_MAPPER);
    }

    @Override
    public List<PublicPostWithUserLike> getPublicPostsByDebateWithIsLiked(long debateId, long userId, int page) {
        if (page < 0) {
            return new ArrayList<>();
        }
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
        data.put("created_date", created);
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