package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.ForbiddenUserException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

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
    public Optional<User> getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Transactional
    @Override
    public User create(String username, String password, String email, Locale locale) {
        Optional<User> optionalUser = getUserByEmail(email);
        if (optionalUser.isPresent())
            return optionalUser.get().updateLegacyUser(username, passwordEncoder.encode(password));

        User user = userDao.create(username, passwordEncoder.encode(password), email, locale);

        final Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);

        return user;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Transactional
    @Override
    public User updateImage(String username, byte[] data) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenUserException();
        }

        final User user = getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot update image for user {} because it does not exist", username);
            return new UserNotFoundException();
        });

        final Image image = user.getImage();

        user.updateImage(data);

        if (image != null) imageService.deleteImage(image);

        return user;
    }

    @Override
    public boolean deleteImage(String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenUserException();
        }

        final User user = getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot update image for user {} because it does not exist", username);
            return new UserNotFoundException();
        });

        final Image image = user.getImage();

        if (image != null) {
            user.deleteImage();
            imageService.deleteImage(image);
            return true;
        }
        return false;
    }

    @Override
    public void requestModerator(String username, String reason) {
        emailService.sendEmailSelf("New user moderator request for " + username, "reason for request: " + reason);
    }

    @Override
    @Async
    @Transactional
    public void deleteUser(String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!username.equals(auth.getName())) {
            throw new ForbiddenUserException();
        }

        User user = getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot delete User {} because it does not exist", username);
            return new UserNotFoundException();
        });
        user.removeUser();
    }
}