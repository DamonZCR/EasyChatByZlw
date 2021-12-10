package message;

import com.alibaba.fastjson.annotation.JSONField;

public class User {
    @JSONField(name = "userId", ordinal = 1)
    private int UserId;
    @JSONField(name = "userPwd", ordinal = 2)
    private String UserPwd;
    @JSONField(name = "userName", ordinal = 3)
    private String UserName;
    @JSONField(name = "userStatus", ordinal = 4)
    private int UserStatus ;
    @JSONField(name = "sex", ordinal = 5)
    private String Sex;

    public User(int userId, String userPwd, String userName, int userStatus, String sex) {
        UserId = userId;
        UserPwd = userPwd;
        UserName = userName;
        UserStatus = userStatus;
        Sex = sex;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserPwd() {
        return UserPwd;
    }

    public void setUserPwd(String userPwd) {
        UserPwd = userPwd;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(int userStatus) {
        UserStatus = userStatus;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }
}
