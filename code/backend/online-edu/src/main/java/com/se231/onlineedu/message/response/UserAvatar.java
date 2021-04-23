package com.se231.onlineedu.message.response;

/**
 * User Avatar Class
 *
 * used to return username and avatar
 *
 * @author Zhe Li
 * @date 2019/07/29
 */
public class UserAvatar {
    private String username;

    private String avatar;

    public UserAvatar() {
    }

    public UserAvatar(String username, String avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
