package message;

import com.alibaba.fastjson.annotation.JSONField;

public class NotifyUserStatusMes {
    @JSONField(name = "userId", ordinal = 1)
    private int UserId;
    @JSONField(name = "status", ordinal = 2)
    private int Status;

    public NotifyUserStatusMes() {
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
