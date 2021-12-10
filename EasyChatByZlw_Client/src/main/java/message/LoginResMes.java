package message;

import com.alibaba.fastjson.annotation.JSONField;

public class LoginResMes {
    @JSONField(name = "code", ordinal = 1)
    private int Code;
    @JSONField(name = "UsersId", ordinal = 2)
    private int[] UsersId;
    @JSONField(name = "userName", ordinal = 3)
    private String UserName;
    @JSONField(name = "error", ordinal = 4)
    private String Error;

    public LoginResMes() {
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public int[] getUsersId() {
        return UsersId;
    }

    public void setUsersId(int[] usersId) {
        UsersId = usersId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }
}
