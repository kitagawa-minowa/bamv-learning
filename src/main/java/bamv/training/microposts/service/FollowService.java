package bamv.training.microposts.service;

import bamv.training.microposts.dto.UserDto;

import java.util.List;

public interface FollowService {
    int findFollowNumber(String userId);

    int findFollowerNumber(String userId);

    List<String> findFollows(String userId);

    void followUser(String userId, String followedUserId);

    void unfollowUser(String userId, String followedUserId);
}
