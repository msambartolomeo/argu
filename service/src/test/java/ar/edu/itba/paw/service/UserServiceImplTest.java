package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private final static long USER_ID = 1;
    private final static String USER_USERNAME = "username";
    private final static String USER_PASSWORD = "password";
    private final static String USER_EMAIL = "test@test.com";
    private final static LocalDate USER_DATE = LocalDate.parse("2022-01-01");
    private final static UserRole USER_ROLE = UserRole.USER;
    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;

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
}
