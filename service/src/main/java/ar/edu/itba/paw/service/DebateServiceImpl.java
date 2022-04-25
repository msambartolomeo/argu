package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DebateDao;
import ar.edu.itba.paw.interfaces.services.DebateService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.model.Debate;
import ar.edu.itba.paw.model.DebateCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DebateServiceImpl implements DebateService {

    @Autowired
    private DebateDao debateDao;
    @Autowired
    private ImageService imageService;

    @Override
    public Optional<Debate> getDebateById(long id) {
        return debateDao.getDebateById(id);
    }

    @Override
    public Debate create(String name, String description, DebateCategory category) {
        return debateDao.create(name, description, null, category);
    }

    @Override
    public Debate create(String name, String description, byte[] image, DebateCategory category) {
        long imageId = imageService.createImage(image);
        return debateDao.create(name, description, imageId, category);
    }

    @Override
    public List<Debate> getSubscribedDebatesByUsername(long userid, int page) {
        return debateDao.getSubscribedDebatesByUsername(userid, page);
    }

    @Override
    public List<Debate> get(int page, String search) {
        if (search != null)
            return debateDao.getQuery(page, search);
         else
             return debateDao.getAll(page);

    }
}
