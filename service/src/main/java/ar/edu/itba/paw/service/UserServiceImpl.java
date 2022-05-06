package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageService imageService;
    @Autowired
    private EmailService emailService;

    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Transactional
    @Override
    public User create(String username, String password, String email) {
        Optional<User> userByUsername = userDao.getUserByUsername(username);
        Optional<User> userByEmail = userDao.getUserByEmail(email);

        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException(userByEmail.isPresent(), true, username, email, password);
        }

        if (userByEmail.isPresent()) {
            if (userByEmail.get().getUsername() == null) {
                return userDao.updateLegacyUser(userByEmail.get().getUserId(), username, passwordEncoder.encode(password), email);
            } else {
                throw new UserAlreadyExistsException(true, false, username, email, password);
            }
        } else {
            return userDao.create(username, passwordEncoder.encode(password), email);
        }
    }

    @Transactional
    @Override
    public void updateImage(long id, byte[] image) {
        Optional<User> user = getUserById(id);
        long imageId = imageService.createImage(image);
        userDao.updateImage(id, imageId);
        user.ifPresent(u -> {
            if (u.getImageId() != null ) imageService.deleteImage(u.getImageId());
        });
    }

    @Override
    public void requestModerator(String username, String reason) {
        emailService.sendEmailSelf("New user moderator request for " + username, "reason for request: " + reason);
    }
}