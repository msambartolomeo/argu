package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public User create(String username, String password, String email) {
        Optional<User> user = userDao.getUserByUsername(username);
        if (user.isPresent())
            //TODO: verificar excepciones correctas (quizás dos excepciones distintas para cada caso)
            throw new UserAlreadyExistsException();
        user = userDao.getUserByEmail(email);
        if (user.isPresent()) {
            if (user.get().getUsername() == null) {
                return userDao.updateLegacyUser(user.get().getId(), username, passwordEncoder.encode(password), email);
            }
            else  {
                // TODO: verificar excepciones correctas
                throw new UserAlreadyExistsException();
            }
        }
        else
            return userDao.create(username, passwordEncoder.encode(password), email);
    }
}