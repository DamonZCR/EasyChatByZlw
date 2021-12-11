package utils;

import com.alibaba.fastjson.JSON;
import message.*;

public class MessageUtils {
    public String s2 = "ceshi2";
    // 封装注册消息:RegisterMesType
    public String RegisMess(int userId, String userPwd, String userName, int userStatus, String sex){
        String user = JSON.toJSONString(new RegisterMes(new User(userId, userPwd, userName, userStatus, sex)));
        String res = JSON.toJSONString(new Message("RegisterMes", user));
        return res;
    }

    // 封装登录消息
    public String LogInMess(int userId, String userPwd, String userName){
        String logM = JSON.toJSONString(new LoginMes(userId, userPwd, userName));
        String res = JSON.toJSONString(new Message("LoginMes", logM));
        return res;
    }

    // 封装信息消息
    public String SmsMess(String cont, int userId){
        String sms = JSON.toJSONString(new SmsMes(cont, new User(userId, "","",1,"")));
        String res = JSON.toJSONString(new Message("SmsMes", sms));
        return res;
    }
    // 封装私聊信息消息
    public String SingleChatMes(int toUserId, int userId, String content){
        String singleMes = JSON.toJSONString(new SingleChatMes(toUserId,
                new SmsMes(content, new User(userId, "","",1,""))));
        String res = JSON.toJSONString(new Message("SingleChatMes", singleMes));
        return res;
    }

    // 封装离线消息
    public String LeaveMess(int userId){
        String ls = JSON.toJSONString(new LeaveMes(userId));
        String res = JSON.toJSONString(new Message("LeaveMes", ls));
        return res;
    }


}
