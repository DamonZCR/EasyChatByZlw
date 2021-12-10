package message;

import com.alibaba.fastjson.annotation.JSONField;

public class Message {
    @JSONField(name = "type", ordinal = 1)
    private String Type;
    @JSONField(name = "data", ordinal = 2)
    private String Data;

    public Message(String Type, String Data){
        super();
        this.Type = Type;
        this.Data = Data;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
