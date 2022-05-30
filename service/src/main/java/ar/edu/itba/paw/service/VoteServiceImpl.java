package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.VoteService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Vote;
import ar.edu.itba.paw.model.enums.DebateVote;
import ar.edu.itba.paw.model.exceptions.DebateNotFoundException;
import ar.edu.itba.paw.model.exceptions.UserAlreadyVotedException;
import ar.edu.itba.paw.model.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteServiceImpl.class);
    @Autowired
    VoteDao voteDao;
    @Autowired
    DebateService debateService;
    @Autowired
    UserService userService;


    @Override
    @Transactional
    public Vote addVote(long debateId, String username, DebateVote vote) {
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot vote winner in debate {} because user {} does not exist", debateId, username);
            return new UserNotFoundException();
        });
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot vote winner in debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });
        Optional<Vote> voteOptional = voteDao.getVote(user, debate);
        if (voteOptional.isPresent()) {
            LOGGER.error("Cannot vote winner in debate {} because user {} already voted", debateId, username);
            throw new UserAlreadyVotedException();
        }
        return voteDao.addVote(user, debate, vote);
    }

    @Override
    public Optional<Vote> getVote(long debateId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot get vote in debate {} because user {} does not exist", debateId, username);
            return new UserNotFoundException();
        });
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot get vote in debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });
        return voteDao.getVote(user, debate);
    }


    @Override
    @Transactional
    public void removeVote(long debateId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(() -> {
            LOGGER.error("Cannot remove vote in debate {} because user {} does not exist", debateId, username);
            return new UserNotFoundException();
        });
        Debate debate = debateService.getDebateById(debateId).orElseThrow(() -> {
            LOGGER.error("Cannot remove vote in debate {} because it does not exist", debateId);
            return new DebateNotFoundException();
        });
        voteDao.getVote(user, debate).ifPresent(vote -> voteDao.delete(vote)); // TODO: why get before delete?
    }
}
