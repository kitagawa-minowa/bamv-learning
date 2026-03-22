package bamv.training.microposts.service;

import bamv.training.microposts.dto.OtherUserDto;
import bamv.training.microposts.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto findUser(String userId);

    int createNewUser(String userId, String name, String password);

    List<OtherUserDto> findAllUsers(String userId);
}
