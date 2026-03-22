package bamv.training.microposts.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface TFollowDao {
    int countFollowingNumber(String userId);

    int countFollowerNumber(String userId);

    int addFollow(String followingUserId, String followedUserId);

    int deleteFollow(String followingUserId, String followedUserId);

    boolean isFollowing(String followingUserId, String followedUserId);
}
