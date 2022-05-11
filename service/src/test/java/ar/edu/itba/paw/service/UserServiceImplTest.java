package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private final static long USER_ID = 1;
    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static String MODERATOR_REASON = "reason";
    private final static LocalDate USER_DATE = LocalDate.parse("2022-01-01");
    private final static UserRole USER_ROLE = UserRole.USER;
    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    private final static long IMAGE_ID = 1;

    private final static long DEBATE_ID = 1;

    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private ImageService imageService;

    @Test
    public void testCreateUserNew() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        Mockito.when(userDao.create(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(user);
        Mockito.when(userDao.getUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(USER_PASSWORD);

        User u = userService.create(USER_USERNAME, USER_PASSWORD, USER_EMAIL);

        assertEquals(user.getUsername(), u.getUsername());
        assertEquals(user.getPassword(), u.getPassword());
        assertEquals(user.getEmail(), u.getEmail());
        assertEquals(user.getRole(), u.getRole());
    }

    @Test
    public void testCreateUserUpdateLegacy() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        Mockito.when(userDao.updateLegacyUser(Mockito.anyLong(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(user);
        Mockito.when(userDao.getUserByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(USER_PASSWORD);

        User u = userService.create(USER_USERNAME, USER_PASSWORD, USER_EMAIL);

        assertEquals(user.getUsername(), u.getUsername());
        assertEquals(user.getPassword(), u.getPassword());
        assertEquals(user.getEmail(), u.getEmail());
        assertEquals(user.getRole(), u.getRole());
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        Mockito.when(userDao.getUserByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        Optional<User> u = userService.getUserByUsername(USER_USERNAME);

        assertTrue(u.isPresent());
        assertEquals(user.getUsername(), u.get().getUsername());
        assertEquals(user.getPassword(), u.get().getPassword());
        assertEquals(user.getEmail(), u.get().getEmail());
        assertEquals(user.getRole(), u.get().getRole());
    }

    @Test
    public void testGetUserByUsernameEmpty() {
        Mockito.when(userDao.getUserByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        Optional<User> u = userService.getUserByUsername(USER_USERNAME);

        assertFalse(u.isPresent());
    }

    @Test
    public void testGetUserByEmail() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        Mockito.when(userDao.getUserByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        Optional<User> u = userService.getUserByEmail(USER_EMAIL);

        assertTrue(u.isPresent());
        assertEquals(user.getUsername(), u.get().getUsername());
        assertEquals(user.getPassword(), u.get().getPassword());
        assertEquals(user.getEmail(), u.get().getEmail());
        assertEquals(user.getRole(), u.get().getRole());
    }

    @Test
    public void testGetUserByEmailEmpty() {
        Mockito.when(userDao.getUserByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Optional<User> u = userService.getUserByEmail(USER_EMAIL);

        assertFalse(u.isPresent());
    }

    @Test
    public void testRequestModerator() {
        userService.requestModerator(USER_USERNAME, MODERATOR_REASON);

        Mockito.verify(emailService).sendEmailSelf(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateImage() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, IMAGE_ID, USER_ROLE);
        Image image = new Image(IMAGE_ID, IMAGE_DATA);
        Mockito.when(userDao.getUserByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(imageService.createImage(Mockito.any())).thenReturn(image);

        userService.updateImage(USER_USERNAME, IMAGE_DATA);

        Mockito.verify(userDao).updateImage(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(imageService).deleteImage(Mockito.anyLong());
    }

    @Test
    public void testUpdateImageNoPreviousImage() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        Image image = new Image(IMAGE_ID, IMAGE_DATA);
        Mockito.when(userDao.getUserByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(imageService.createImage(Mockito.any())).thenReturn(image);

        userService.updateImage(USER_USERNAME, IMAGE_DATA);

        Mockito.verify(userDao).updateImage(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(imageService, Mockito.times(0)).deleteImage(Mockito.anyLong());
    }

    @Test
    public void testGetSubscribedUsersByDebate() {
        User user = new User(USER_ID, USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_DATE, USER_ROLE);
        List<User> users = new ArrayList<>();
        users.add(user);

        Mockito.when(userDao.getSubscribedUsersByDebate(Mockito.anyLong())).thenReturn(users);

        List<User> u = userService.getSubscribedUsersByDebate(DEBATE_ID);

        assertFalse(u.isEmpty());
        assertEquals(user.getUserId(), u.get(0).getUserId());
        assertEquals(user.getUsername(), u.get(0).getUsername());
        assertEquals(user.getPassword(), u.get(0).getPassword());
        assertEquals(user.getEmail(), u.get(0).getEmail());
        assertEquals(user.getRole(), u.get(0).getRole());
        assertEquals(user.getCreatedDate(), u.get(0).getCreatedDate());
    }

    @Test
    public void testGetSubscribedUsersByDebateEmpty() {
        Mockito.when(userDao.getSubscribedUsersByDebate(Mockito.anyLong())).thenReturn(new ArrayList<>());

        List<User> u = userService.getSubscribedUsersByDebate(DEBATE_ID);

        assertTrue(u.isEmpty());
    }
}
