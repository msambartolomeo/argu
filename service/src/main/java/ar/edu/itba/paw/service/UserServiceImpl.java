package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public User getRealUserByUsername(String username) {
        return getUserByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public User create(String username, String password, String email) {
        Optional<User> user = getUserByEmail(email);
        if (user.isPresent())
            return userDao.updateLegacyUser(user.get().getUserId(), username, passwordEncoder.encode(password), email);
        return userDao.create(username, passwordEncoder.encode(password), email);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Transactional
    @Override
    public void updateImage(String username, byte[] image) {
        User user = getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        long imageId = imageService.createImage(image);
        userDao.updateImage(user.getUserId(), imageId);
        if (user.getImageId() != null ) imageService.deleteImage(user.getImageId());
    }

    @Override
    public void requestModerator(String username, String reason) {
        emailService.sendEmailSelf("New user moderator request for " + username, "reason for request: " + reason);
    }

    @Override
    public List<User> getSubscribedUsersByDebate(long debateId) {
        return userDao.getSubscribedUsersByDebate(debateId);
    }
}