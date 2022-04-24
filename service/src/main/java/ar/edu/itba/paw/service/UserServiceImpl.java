package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserAlreadyExistsException;
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
        Optional<User> user = userDao.getUserByUsername(username);
        if (user.isPresent())
            //TODO: verificar excepciones correctas (quizás dos excepciones distintas para cada caso)
            throw new UserAlreadyExistsException();
        user = userDao.getUserByEmail(email);
        if (user.isPresent()) {
            if (user.get().getUsername() == null) {
                return userDao.updateLegacyUser(user.get().getUserId(), username, passwordEncoder.encode(password), email);
            }
            else  {
                // TODO: verificar excepciones correctas
                throw new UserAlreadyExistsException();
            }
        }
        else
            return userDao.create(username, passwordEncoder.encode(password), email);
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