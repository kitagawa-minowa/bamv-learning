package bamv.training.microposts.service.impl;

import bamv.training.microposts.dao.TFollowDao;
import bamv.training.microposts.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private TFollowDao tFollowDao;

    @Override
    public int findFollowNumber(String userId) {
        return tFollowDao.countFollowingNumber(userId);
    }

    @Override
    public int findFollowerNumber(String userId) {
        return tFollowDao.countFollowerNumber(userId);
    }

    @Override
    public int addFollow(String followingId, String followedId) {
        return tFollowDao.addFollow(followingId, followedId);
    }

    @Override
    public int deleteFollow(String followingId, String followedId) {
        return tFollowDao.deleteFollow(followingId, followedId);
    }
}