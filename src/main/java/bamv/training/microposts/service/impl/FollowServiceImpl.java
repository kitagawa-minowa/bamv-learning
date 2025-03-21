package bamv.training.microposts.service.impl;

import bamv.training.microposts.dao.TFollowDao;
import bamv.training.microposts.dto.UserDto;
import bamv.training.microposts.entity.MUser;
import bamv.training.microposts.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<String> findFollows(String userId) {
        return tFollowDao.getFollowingUsers(userId);
    }

    @Override
    public void followUser(String userId, String followedUserId) {
        tFollowDao.followUser(userId, followedUserId);
    }

    @Override
    public void unfollowUser(String userId, String followedUserId) {
        tFollowDao.unfollowUser(userId, followedUserId);
    }
}