package bamv.training.microposts.dao.impl;

import bamv.training.microposts.dao.TFollowDao;
import bamv.training.microposts.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TFollowDaoImpl implements TFollowDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SequenceService sequenceService;

    @Override
    public int countFollowingNumber(String userId) {
        String query = "SELECT COUNT(*) as count FROM t_follow WHERE following_user_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, userId);
    }

    @Override
    public int countFollowerNumber(String userId) {
        String query = "SELECT COUNT(*) as count FROM t_follow WHERE followed_user_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, userId);
    }

    @Override
    public int addFollow(String followingUserId, String followedUserId) {
        String followId = sequenceService.issueSequence("follow_id");
        String query = "INSERT INTO t_follow VALUES(?, ?, ?)";
        return jdbcTemplate.update(query, Integer.class, followId, followingUserId, followedUserId);
    }
}
