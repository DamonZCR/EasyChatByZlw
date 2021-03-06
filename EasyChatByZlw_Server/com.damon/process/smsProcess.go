package process

import (
	"EasyChatByZlw_Server/com.damon/message"
	"EasyChatByZlw_Server/com.damon/utils"
	"fmt"
	"net"

	"encoding/json"
)

//写方法实现群发消息

type SmsProcess struct {
	//..[暂时不需字段]
}

func (this *SmsProcess) SendGroupMes(mes *message.Message) {

	//遍历服务器端的onlineUsers map[int]*UserProcess,
	//将消息转发取出.
	//取出mes的内容 SmsMes
	var smsMes message.SmsMes
	err := json.Unmarshal([]byte(mes.Data), &smsMes)
	if err != nil {
		fmt.Println("json.Unmarshal SendGroupMes err=", err)
		return
	}

	data, err := json.Marshal(mes)
	if err != nil {
		fmt.Println("json.Marshal SendGroupMes err=", err)
		return
	}

	for id, up := range userMgr.onlineUsers {
		//这里，还需要过滤到自己,即不要再发给自己
		if id == smsMes.User.UserId {
			//continue
		}
		this.SendMesToEachOnlineUser(data, up.Conn)
	}
}
func (this *SmsProcess) SendSingleMes(mes *message.Message) {
	var singleMes message.SingleChatMes
	err := json.Unmarshal([]byte(mes.Data), &singleMes)
	if err != nil {
		fmt.Println("json.Unmarshal SendSingleMes err=", err)
		return
	}

	toUser := userMgr.onlineUsers[singleMes.ToUserId]
	data, err := json.Marshal(mes)
	if err != nil {
		fmt.Println("json.Marshal SendSingleMes err=", err)
		return
	}

	tf := &utils.Transfer{
		Conn: toUser.Conn, //
	}
	err = tf.WritePkg(data)
	if err != nil {
		fmt.Println("转发私聊消息时失败 err=", err)
	}
}

func (this *SmsProcess) SendMesToEachOnlineUser(data []byte, conn net.Conn) {

	//创建一个Transfer 实例，发送data
	tf := &utils.Transfer{
		Conn: conn, //
	}
	err := tf.WritePkg(data)
	if err != nil {
		fmt.Println("转发消息失败 err=", err)
	}
}
