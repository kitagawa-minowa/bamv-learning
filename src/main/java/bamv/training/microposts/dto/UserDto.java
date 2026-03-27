package bamv.training.microposts.dto;

public class UserDto {
    private String userId;

    private String name;

    private  boolean isFollowing;

    /* フォロー関係を考慮しない場合のコンストラクタ */
    public UserDto(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.isFollowing = false;
    }

    public UserDto(String userId, String name, boolean isFollowing) {
        this.userId = userId;
        this.name = name;
        this.isFollowing = isFollowing;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}