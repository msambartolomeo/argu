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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteDao voteDao;
    @Autowired
    DebateService debateService;
    @Autowired
    UserService userService;


    @Override
    public Vote addVote(long debateId, String username, DebateVote vote) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        Optional<Vote> voteOptional = voteDao.getVote(user, debate);
        if (voteOptional.isPresent()) {
            throw new UserAlreadyVotedException();
        }
        return voteDao.addVote(user, debate, vote);
    }

    @Override
    public Optional<Vote> getVote(long debateId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        return voteDao.getVote(user, debate);
    }


    @Override
    public void removeVote(long debateId, String username) {
        User user = userService.getUserByUsername(username).orElseThrow(UserNotFoundException::new);
        Debate debate = debateService.getDebateById(debateId).orElseThrow(DebateNotFoundException::new);
        voteDao.getVote(user, debate).ifPresent(vote -> voteDao.delete(vote));
    }
}
