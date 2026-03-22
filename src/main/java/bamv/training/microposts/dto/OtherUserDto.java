package bamv.training.microposts.dto;

public class OtherUserDto extends UserDto{
    boolean isFollowing;

    public OtherUserDto(String userId, String name) {
        super(userId, name);
        this.isFollowing = false;
    }

    public OtherUserDto(String userId, String name, boolean isFollowing){
        super(userId, name);
        this.isFollowing = isFollowing;
     }
}
