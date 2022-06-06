package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageService imageService;
    @Autowired
    private EmailService emailService;

    @Override
    public Optional<User> getUserById(long userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Transactional
    @Override
    public User create(String username, String password, String email) {
        Optional<User> user = getUserByEmail(email);
        if (user.isPresent())
            return user.get().updateLegacyUser(username, passwordEncoder.encode(password));
        return userDao.create(username, passwordEncoder.encode(password), email);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Transactional
    @Override
    public User updateImage(String username, byte[] data) {
        User user = getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot update image for user {} because it does not exist", username);
            return new UserNotFoundException();
        });

        Image image = null;
        if (user.getImage() != null)
            image = user.getImage();

        user.updateImage(data);

        if (image != null) imageService.deleteImage(image);

        return user;
    }

    @Override
    public void requestModerator(String username, String reason) {
        emailService.sendEmailSelf("New user moderator request for " + username, "reason for request: " + reason);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        User user = getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot delete User {} because it does not exist", username);
            return new UserNotFoundException();
        });
        user.removeUser();
    }
}