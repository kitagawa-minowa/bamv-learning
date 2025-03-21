package bamv.training.microposts.dao;

import bamv.training.microposts.dto.UserDto;
import bamv.training.microposts.entity.MUser;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TFollowDao {
    int countFollowingNumber(String userId);

    int countFollowerNumber(String userId);

    List<String> getFollowingUsers(String userId);

    void followUser(String userId, String followedUserId);

    void unfollowUser(String userId, String followedUserId);
}
