package message;

import com.alibaba.fastjson.annotation.JSONField;

public class SmsMes {
    @JSONField(name = "content", ordinal = 1)
    private String Content;
    @JSONField(name = "userId", ordinal = 2)
    private int UserId;
    @JSONField(name = "UserPwd", ordinal = 3)
    private String UserPwd;
    @JSONField(name = "UserName", ordinal = 4)
    private String UserName;
    @JSONField(name = "UserStatus", ordinal = 5)
    private int UserStatus;
    @JSONField(name = "Sex", ordinal = 6)
    private String Sex;

    public SmsMes(String content, int userId, String userPwd, String userName, int userStatus, String sex) {
        Content = content;
        UserId = userId;
        UserPwd = userPwd;
        UserName = userName;
        UserStatus = userStatus;
        Sex = sex;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
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

