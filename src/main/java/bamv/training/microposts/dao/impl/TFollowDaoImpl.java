package bamv.training.microposts.dao.impl;

import bamv.training.microposts.dao.TFollowDao;
import bamv.training.microposts.dto.UserDto;
import bamv.training.microposts.entity.MUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TFollowDaoImpl implements TFollowDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    public List<String> getFollowingUsers(String userId) {
        RowMapper<String> rowMapper = new SingleColumnRowMapper<>(String.class);
        String query = "SELECT followed_user_id FROM t_follow WHERE following_user_id = ?";
        return jdbcTemplate.query(query, rowMapper, userId);
    }

    @Override
    public void followUser(String userId, String followedUserId) {
        String followId = generateFollowId();
        String query = "INSERT INTO t_follow (follow_id, following_user_id, followed_user_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, followId, userId, followedUserId);
    }

    @Override
    public void unfollowUser(String userId, String followedUserId) {
        String query = "DELETE FROM t_follow WHERE following_user_id = ? AND followed_user_id = ?";
        jdbcTemplate.update(query, userId, followedUserId);
    }

    public String generateFollowId() {
        String query = "SELECT MAX(follow_id) FROM t_follow";
        String MaxId = jdbcTemplate.queryForObject(query, String.class);

        if (MaxId == null) {
            return "FL00000001";
        }

        int nextIdNum = Integer.parseInt(MaxId.substring(2)) + 1;

        return String.format("FL%08d", nextIdNum);
    }
}