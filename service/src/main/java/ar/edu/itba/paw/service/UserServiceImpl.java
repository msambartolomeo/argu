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

    @Override
    public User create(String username, String password, String email) {
        Optional<User> userByUsername = userDao.getUserByUsername(username);
        Optional<User> userByEmail = userDao.getUserByEmail(email);

        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException(userByEmail.isPresent(), true);
        }

        if (userByEmail.isPresent()) {
            if (userByEmail.get().getUsername() == null) {
                return userDao.updateLegacyUser(userByEmail.get().getUserId(), username, passwordEncoder.encode(password), email);
            } else {
                throw new UserAlreadyExistsException(true, false);
            }
        } else {
            return userDao.create(username, passwordEncoder.encode(password), email);
        }
    }

    @Override
    public void updateImage(long id, byte[] image) {
        getUserById(id).ifPresent(user -> {
            if (user.getImageId() != null) imageService.deleteImage(user.getImageId());
        });
        long imageId = imageService.createImage(image);
        userDao.updateImage(id, imageId);
    }

    @Override
    public void requestModerator(String username, String reason) {
        emailService.sendEmailSelf("New user moderator request for " + username, "reason for request: " + reason);
    }
}