package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ChatDao;
import ar.edu.itba.paw.interfaces.services.ChatService;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Chat;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateClosedException;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    private ChatDao chatDao;
    @Autowired
    private UserService userService;
    @Autowired
    private DebateService debateService;

    @Transactional
    @Override
    public Chat create(String username, long debateId, String message) {
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot create new Chat because Debate {} does not exist", debateId);
            return new DebateNotFoundException();
        });
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot create new Chat on Debate {} because User {} does not exist", debateId, username);
            return new UserNotFoundException();
        });
        if (debate.getStatus() == DebateStatus.CLOSED || debate.getStatus() == DebateStatus.DELETED) {
            LOGGER.error("Cannot create new Chat on Debate {} because it is closed", debateId);
            throw new DebateClosedException();
        }

        return chatDao.create(user, debate, message);
    }

    @Override
    @Transactional
    public List<Chat> getDebateChat(long debateId, int page, int size) {
        if (page < 0) {
            return Collections.emptyList();
        }
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot get Chat on Debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });

        return chatDao.getDebateChat(debate, page, size);
    }

    @Override
    public int getDebateChatPageCount(long debateId, int size) {
        return (int) Math.ceil(chatDao.getDebateChatsCount(debateId) / (double) size);
    }

    @Override
    public Optional<Chat> getChatById(long id) {
        return chatDao.getChatById(id);
    }
}
