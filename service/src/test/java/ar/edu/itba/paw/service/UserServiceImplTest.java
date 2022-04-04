package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService = new UserServiceImpl();
    @Mock
    private UserDao userDao;

    @Test
    public void testCreateUser() {
        User user = new User(1, "juan", "1234");
        Mockito.when(userDao.create(Mockito.anyString(), Mockito.anyString())).thenReturn(user);


        User u = userService.create("juan", "1234");

        assertEquals(user, u);
    }
}
