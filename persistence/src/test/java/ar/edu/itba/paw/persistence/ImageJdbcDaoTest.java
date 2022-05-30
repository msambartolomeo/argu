//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.model.Image;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class ImageJdbcDaoTest {
//
//    private ImageJdbcDao imageJdbcDao;
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsert;
//
//    private final static String IMAGE_TABLE = "images";
//    private final static String IMAGE_TABLE_ID = "imageid";
//    private final static long IMAGE_ID = 1;
//    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
//
//    @Autowired
//    DataSource ds;
//
//    @Before
//    public void setUp() {
//        imageJdbcDao = new ImageJdbcDao(ds);
//        jdbcTemplate = new JdbcTemplate(ds);
//        jdbcInsert = new SimpleJdbcInsert(ds)
//                .withTableName(IMAGE_TABLE)
//                .usingGeneratedKeyColumns(IMAGE_TABLE_ID);
//    }
//    @After
//    public void tearDown() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);
//    }
//
//    @Test
//    public void testCreateImage() {
//        Image image = imageJdbcDao.createImage(IMAGE_DATA);
//
//        assertNotNull(image);
//        assertEquals(IMAGE_DATA, image.getData());
//    }
//
//    @Test
//    public void testGetImageEmpty() {
//        Optional<Image> image = imageJdbcDao.getImage(IMAGE_ID);
//
//        assertFalse(image.isPresent());
//    }
//
//    @Test
//    public void testGetImage() {
//        final Map<String, Object> data = new HashMap<>();
//        data.put("data", IMAGE_DATA);
//        long key = jdbcInsert.executeAndReturnKey(data).longValue();
//
//        Optional<Image> image = imageJdbcDao.getImage(key);
//
//        assertTrue(image.isPresent());
//        assertArrayEquals(IMAGE_DATA, image.get().getData());
//    }
//
//    @Test
//    public void testDeleteImage() {
//        final Map<String, Object> data = new HashMap<>();
//        data.put("data", IMAGE_DATA);
//        Number key = jdbcInsert.executeAndReturnKey(data);
//
//        imageJdbcDao.deleteImage(key.longValue());
//
//        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
//    }
//}
