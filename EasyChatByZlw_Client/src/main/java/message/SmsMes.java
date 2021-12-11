package message;

import com.alibaba.fastjson.annotation.JSONField;

public class SmsMes {
    @JSONField(name = "content", ordinal = 1)
    private String Content;
    @JSONField(name = "user", ordinal = 2)
    private User User;


    public SmsMes(String content, User user) {
        Content = content;
        User = user;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public message.User getUser() {
        return User;
    }

    public void setUser(message.User user) {
        User = user;
    }
}

