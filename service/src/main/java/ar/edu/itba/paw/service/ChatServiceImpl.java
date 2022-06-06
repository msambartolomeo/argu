package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ChatDao;
import ar.edu.itba.paw.interfaces.services.ChatService;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.model.Chat;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.DebateStatus;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.ForbiddenChatException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatServiceImpl.class);
    private static final int PAGE_SIZE = 15;

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

        // TODO: check condition with extended voting in debate
        if (debate.getStatus() == DebateStatus.CLOSED || debate.getStatus() == DebateStatus.DELETED || debate.getCreator().getUsername().equals(username) || debate.getOpponent().getUsername().equals(username)) {
            LOGGER.error("Cannot create new Chat on Debate {} because it is closed or because the requesting user {} is the creator or the opponent", debateId, username);
            throw new ForbiddenChatException();
        }

        return chatDao.create(user, debate, message);
    }

    @Override
    public List<Chat> getDebateChat(long debateId, int page) {
        if (page < 0) {
            return Collections.emptyList();
        }
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot get Chat on Debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });

        return chatDao.getDebateChat(debate, page);
    }

    @Override
    public int getDebateChatPageCount(long debateId) {
        return (int) Math.ceil(chatDao.getDebateChatsCount(debateId) / (double) PAGE_SIZE);
    }
}
