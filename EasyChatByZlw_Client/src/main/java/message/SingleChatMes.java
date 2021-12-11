package message;

import com.alibaba.fastjson.annotation.JSONField;

public class SingleChatMes {
    @JSONField(name = "toUserId", ordinal = 1)
    private int ToUserId;
    @JSONField(name = "sms", ordinal = 2)
    private SmsMes Sms;

    public SingleChatMes(int toUserId, SmsMes sms) {
        ToUserId = toUserId;
        Sms = sms;
    }

    public int getToUserId() {
        return ToUserId;
    }

    public void setToUserId(int toUserId) {
        ToUserId = toUserId;
    }

    public SmsMes getSms() {
        return Sms;
    }

    public void setSms(SmsMes sms) {
        Sms = sms;
    }
}
