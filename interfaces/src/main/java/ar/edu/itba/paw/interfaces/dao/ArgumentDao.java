package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.ArgumentStatus;

import java.util.List;
import java.util.Optional;

public interface ArgumentDao {
    Optional<Argument> getArgumentById(long argumentId);
    int getArgumentsByDebateCount(long debateId);
    Argument create(User user, Debate debate, String content, Image image, ArgumentStatus status);
    List<Argument> getArgumentsByDebate(Debate debate, int page);
    Optional<Argument> getLastArgument(Debate debate);
}
