package bamv.training.microposts.service.impl;

import bamv.training.microposts.dao.MUserDao;
import bamv.training.microposts.dao.TFollowDao;
import bamv.training.microposts.dto.OtherUserDto;
import bamv.training.microposts.dto.UserDto;
import bamv.training.microposts.entity.MUser;
import bamv.training.microposts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MUserDao mUserDao;

    @Autowired
    private TFollowDao tFollowDao;

    @Override
    public UserDto findUser(String userId) {
        MUser mUser = mUserDao.findUser(userId);
        return new UserDto(
                mUser.getUserId(),
                mUser.getName()
        );
    }

    @Override
    @Transactional
    public int createNewUser(String userId, String name, String password) {
        return mUserDao.addNewUser(userId, name, password);
    }

    @Override
    public List<OtherUserDto> findAllUsers(String userId, int page) {
        List<MUser> mUsers = mUserDao.findAllUsers(userId, page);
        return mUsers.stream().map(user ->
                        new OtherUserDto(user.getUserId(),
                                    user.getName(),
                                    tFollowDao.isFollowing(userId, user.getUserId()))
                        ).toList();
    }
}
