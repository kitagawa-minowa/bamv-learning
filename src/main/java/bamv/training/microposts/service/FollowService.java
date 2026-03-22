package bamv.training.microposts.service;

public interface FollowService {
    int findFollowNumber(String userId);

    int findFollowerNumber(String userId);

    int addFollow(String followingId, String followedId);

    int deleteFollow(String followingId, String followedId);
}
