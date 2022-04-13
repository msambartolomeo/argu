package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private final long USER_ID = 1;
    private final String USER_EMAIL = "email@test.com";

    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();
    @Mock
    private UserDao userDao;

    @Test
    public void testCreateUser() {
        User user = new User(USER_ID, USER_EMAIL);
        Mockito.when(userDao.create(Mockito.anyString())).thenReturn(user);


        User u = userService.create(USER_EMAIL);

        assertEquals(user, u);
    }

    @Test
    public void testGetUserById() {
        User user = new User(USER_ID, USER_EMAIL);
        Mockito.when(userDao.getUserById(USER_ID)).thenReturn(Optional.of(user));

        Optional<User> u = userService.getUserById(USER_ID);

        assertTrue(u.isPresent());
        assertEquals(user, u.get());
    }

    @Test
    public void testGetUserByIdDoesntExist() {
        Mockito.when(userDao.getUserById(USER_ID)).thenReturn(Optional.empty());

        Optional<User> u = userService.getUserById(USER_ID);

        assertFalse(u.isPresent());
    }
}
