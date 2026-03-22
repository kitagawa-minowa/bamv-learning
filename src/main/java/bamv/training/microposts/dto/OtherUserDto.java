package bamv.training.microposts.dto;

public class OtherUserDto extends UserDto{
    private boolean isFollowing;

    public OtherUserDto(String userId, String name) {
        super(userId, name);
        this.isFollowing = false;
    }

    public OtherUserDto(String userId, String name, boolean isFollowing){
        super(userId, name);
        this.isFollowing = isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isFollowing() {
        return isFollowing;
    }
}
