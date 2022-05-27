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

import java.util.ArrayList;
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
    public void updateImage(String username, byte[] image) {
        User user = getUserByUsername(username).orElseThrow(UserNotFoundException::new);

        Long imageId = null;
        if (user.getImage() != null)
            imageId = user.getImage().getId();

        user.updateImage(image);

        if (imageId != null) imageService.deleteImage(imageId);
    }

    @Override
    public void requestModerator(String username, String reason) {
        emailService.sendEmailSelf("New user moderator request for " + username, "reason for request: " + reason);
    }

    // TODO: moved to Debate model (not implemented yet) remove for migration merge
    @Override
    public List<User> getSubscribedUsersByDebate(long debateId) {
        return new ArrayList<>();
//        return userDao.getSubscribedUsersByDebate(debateId);
    }
}