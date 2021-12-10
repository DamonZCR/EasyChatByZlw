package message;

import com.alibaba.fastjson.annotation.JSONField;

public class LoginMes {
    @JSONField(name = "userId", ordinal = 1)
    private int UserId;
    @JSONField(name = "userPwd", ordinal = 2)
    private String UserPwd;
    @JSONField(name = "userName", ordinal = 3)
    private String UserName;

    public LoginMes(int userId, String userPwd, String userName) {
        UserId = userId;
        UserPwd = userPwd;
        UserName = userName;
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
}
