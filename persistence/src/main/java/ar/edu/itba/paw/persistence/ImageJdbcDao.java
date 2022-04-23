package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImageJdbcDao implements ImageDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Image> IMAGE_ROW_MAPPER = (rs, rowNum) ->
            new Image(rs.getInt("imageid"),
                    rs.getBytes("data"));

    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("images")
                .usingGeneratedKeyColumns("imageid");
    }

    @Override
    public Optional<Image> getImage(long id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE imageid = ?", new Object[]{id}, IMAGE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public long createImage(byte[] data) {
        final Map<String, Object> sqlData = new HashMap<>();
        sqlData.put("data", data);

        return jdbcInsert.executeAndReturnKey(sqlData).longValue();
    }

    @Override
    public void deleteImage(long id) {
        jdbcTemplate.update("DELETE FROM images WHERE imageid = ?", id);
    }
}
