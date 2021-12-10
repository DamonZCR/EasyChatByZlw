package message;

import com.alibaba.fastjson.annotation.JSONField;

public class LeaveMes {
    @JSONField(name = "userId", ordinal = 1)
    private int UserId;

    public LeaveMes(int userId) {
        UserId = userId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }
}
