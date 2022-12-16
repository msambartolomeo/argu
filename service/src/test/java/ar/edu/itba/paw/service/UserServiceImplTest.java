package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.UserConflictException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static byte[] IMAGE_DATA = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ImageService imageService;

    @Test
    public void testCreateUserNew() {
        User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        when(userDao.create(anyString(), anyString(), anyString(), any(Locale.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(USER_PASSWORD);

        User u = userService.create(USER_USERNAME, USER_PASSWORD, USER_EMAIL, Locale.ENGLISH);

        assertEquals(user.getUsername(), u.getUsername());
        assertEquals(user.getPassword(), u.getPassword());
        assertEquals(user.getEmail(), u.getEmail());
        assertEquals(user.getRole(), u.getRole());
    }

    @Test
    public void testCreateUserUpdateLegacy() {
        User user = new User(USER_EMAIL, null, null, Locale.ENGLISH);
        when(userDao.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn(USER_PASSWORD);

        User u = userService.create(USER_USERNAME, USER_PASSWORD, USER_EMAIL, Locale.ENGLISH);

        assertEquals(user.getUsername(), u.getUsername());
        assertEquals(user.getPassword(), u.getPassword());
        assertEquals(user.getEmail(), u.getEmail());
        assertEquals(user.getRole(), u.getRole());
    }

    @Test(expected = UserConflictException.class)
    public void testCreateUserConflictingUsername() {
        User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        when(userDao.getUserByEmail(anyString())).thenReturn(Optional.of(user));

        userService.create(USER_USERNAME, USER_PASSWORD, USER_EMAIL, Locale.ENGLISH);
    }

    @Test(expected = UserConflictException.class)
    public void testCreateUserConflictingEmail() {
        User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        when(userDao.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        userService.create(USER_USERNAME, USER_PASSWORD, USER_EMAIL, Locale.ENGLISH);
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        when(userDao.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> u = userService.getUserByUsername(USER_USERNAME);

        assertTrue(u.isPresent());
        assertEquals(user.getUsername(), u.get().getUsername());
        assertEquals(user.getPassword(), u.get().getPassword());
        assertEquals(user.getEmail(), u.get().getEmail());
        assertEquals(user.getRole(), u.get().getRole());
    }

    @Test
    public void testGetUserByUsernameEmpty() {
        when(userDao.getUserByUsername(anyString())).thenReturn(Optional.empty());

        Optional<User> u = userService.getUserByUsername(USER_USERNAME);

        assertFalse(u.isPresent());
    }

    @Test
    public void testGetUserByEmail() {
        User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        when(userDao.getUserByEmail(anyString())).thenReturn(Optional.of(user));

        Optional<User> u = userService.getUserByEmail(USER_EMAIL);

        assertTrue(u.isPresent());
        assertEquals(user.getUsername(), u.get().getUsername());
        assertEquals(user.getPassword(), u.get().getPassword());
        assertEquals(user.getEmail(), u.get().getEmail());
        assertEquals(user.getRole(), u.get().getRole());
    }

    @Test
    public void testGetUserByEmailEmpty() {
        when(userDao.getUserByEmail(anyString())).thenReturn(Optional.empty());

        Optional<User> u = userService.getUserByEmail(USER_EMAIL);

        assertFalse(u.isPresent());
    }

    @Test
    public void testUpdateImage() {
        User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        user.updateImage(IMAGE_DATA);
        when(userDao.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        User u = userService.updateImage(USER_USERNAME, IMAGE_DATA);

        assertEquals(IMAGE_DATA, u.getImage().getData());
        verify(imageService).deleteImage(any(Image.class));
    }

    @Test
    public void testUpdateImageNoPreviousImage() {
        User user = new User(USER_EMAIL, USER_USERNAME, USER_PASSWORD, Locale.ENGLISH);
        when(userDao.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        User u = userService.updateImage(USER_USERNAME, IMAGE_DATA);

        assertEquals(IMAGE_DATA, u.getImage().getData());
        verify(imageService, times(0)).deleteImage(any(Image.class));
    }

    @Test(expected = UserNotFoundException.class)
    public void testUpdateImageNoValidUser() {
        when(userDao.getUserByUsername(anyString())).thenReturn(Optional.empty());

        userService.updateImage(USER_USERNAME, IMAGE_DATA);
    }

    @Test(expected = UserNotFoundException.class)
    public void testDeleteUserNoValidUser() {
        when(userDao.getUserByUsername(anyString())).thenReturn(Optional.empty());

        userService.deleteUser(USER_USERNAME);
    }
}
