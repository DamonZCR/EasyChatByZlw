package process

import (
	"EasyChatByZlw_Server/com.damon/message"
	"EasyChatByZlw_Server/com.damon/model"
	"EasyChatByZlw_Server/com.damon/utils"
	"encoding/json"
	"fmt"
	"net"
)

type UserProcess struct {
	//字段
	Conn net.Conn
	//增加一个字段，表示该Conn是哪个用户
	UserId int
}

//当接收到某一个用户离线的消息时，通知其他在线用户有人离线
//用户发送的离线消息类型时LeaveMess,其中只包含用户的id.
func (this *UserProcess) NotifyOtherOffLine(mes *message.Message) (err error) {
	var offMes message.LeaveMes
	err = json.Unmarshal([]byte(mes.Data), &offMes)
	if err != nil {
		fmt.Println("json.Unmarshal fail NotifyOtherOffLine err=", err)
		return
	}
	userMgr.DelOnlineUser(offMes.UserId) //在服务器端的在线列表删除这个用户
	this.Conn.Close()                    //关闭
	//遍历 onlineUsers, 然后一个一个的发送 NotifyUserStatusMes
	for id, up := range userMgr.onlineUsers {
		//过滤到自己
		if id == this.UserId {
			continue
		}
		//开始通知【单独的写一个方法】,1代表上线，0代表下线
		up.NotifyMeOtherline(offMes.UserId, message.UserOffline)
	}
	return
}

//这里我们编写通知所有在线的用户的方法
//userId 要通知其它的在线用户，我上线
func (this *UserProcess) NotifyOthersOnlineUser() {

	//遍历 onlineUsers, 然后一个一个的发送 NotifyUserStatusMes
	for id, up := range userMgr.onlineUsers {
		//过滤到自己
		if id == this.UserId {
			continue
		}
		//开始通知【单独的写一个方法】,1代表上线，0代表下线
		up.NotifyMeOtherline(this.UserId, message.UserOnline)
	}
}

//负责提醒别人有用户上线，或者上线，当在NotifyOthersOnlineUser()方法调用，那么status就为1，提醒所有用户有用户上线
//当在NotifyOtherOffLine()方法中调用，status就为0，提醒所有用户有用户下线。
func (this *UserProcess) NotifyMeOtherline(userId int, status int) {

	//组装我们的NotifyUserStatusMes
	var mes message.Message
	mes.Type = message.NotifyUserStatusMesType

	var notifyUserStatusMes message.NotifyUserStatusMes
	notifyUserStatusMes.UserId = userId
	notifyUserStatusMes.Status = status

	//将notifyUserStatusMes序列化
	data, err := json.Marshal(notifyUserStatusMes)
	if err != nil {
		fmt.Println("json.Marshal err=", err)
		return
	}
	//将序列化后的notifyUserStatusMes赋值给 mes.Data
	mes.Data = string(data)

	//对mes再次序列化，准备发送.
	data, err = json.Marshal(mes)
	if err != nil {
		fmt.Println("json.Marshal err=", err)
		return
	}
	//发送,创建我们Transfer实例，发送
	tf := &utils.Transfer{
		Conn: this.Conn,
	}

	err = tf.WritePkg(data)
	if err != nil {
		fmt.Println("NotifyMeOnline err=", err)
		return
	}
}

func (this *UserProcess) ServerProcessRegister(mes *message.Message) (err error) {

	//1.先从mes 中取出 mes.Data ，并直接反序列化成RegisterMes
	var registerMes message.RegisterMes
	err = json.Unmarshal([]byte(mes.Data), &registerMes)
	if err != nil {
		fmt.Println("json.Unmarshal fail err=", err)
		return
	}

	//1先声明一个 resMes
	var resMes message.Message
	resMes.Type = message.RegisterResMesType
	var registerResMes message.RegisterResMes

	//我们需要到redis数据库去完成注册.
	//1.使用model.MyUserDao 到redis去验证
	err = model.MyUserDao.Register(&registerMes.User)

	if err != nil {
		if err == model.ERROR_USER_EXISTS {
			registerResMes.Code = 505
			registerResMes.Error = model.ERROR_USER_EXISTS.Error()
		} else {
			registerResMes.Code = 506
			registerResMes.Error = "注册发生未知错误..."
		}
	} else {
		registerResMes.Code = 200
	}

	data, err := json.Marshal(registerResMes)
	if err != nil {
		fmt.Println("json.Marshal fail", err)
		return
	}

	//4. 将data 赋值给 resMes
	resMes.Data = string(data)

	//5. 对resMes 进行序列化，准备发送
	data, err = json.Marshal(resMes)
	if err != nil {
		fmt.Println("json.Marshal fail", err)
		return
	}
	//6. 发送data, 我们将其封装到writePkg函数
	//因为使用分层模式(mvc), 我们先创建一个Transfer 实例，然后读取
	tf := &utils.Transfer{
		Conn: this.Conn,
	}
	err = tf.WritePkg(data)
	return
}

//编写一个函数serverProcessLogin函数， 专门处理登录请求
func (this *UserProcess) ServerProcessLogin(mes *message.Message) (err error) {
	//核心代码...
	//1. 先从mes 中取出 mes.Data ，并直接反序列化成LoginMes
	var loginMes message.LoginMes
	err = json.Unmarshal([]byte(mes.Data), &loginMes)
	if err != nil {
		fmt.Println("json.Unmarshal fail err=", err)
		return
	}
	//1先声明一个 resMes
	var resMes message.Message
	resMes.Type = message.LoginResMesType
	//2在声明一个 LoginResMes，并完成赋值
	var loginResMes message.LoginResMes

	//我们需要到redis数据库去完成验证.
	//1.使用model.MyUserDao 到redis去验证
	user, err := model.MyUserDao.Login(loginMes.UserId, loginMes.UserPwd)

	if err != nil {

		if err == model.ERROR_USER_NOTEXISTS {
			loginResMes.Code = 500
			loginResMes.Error = err.Error()
		} else if err == model.ERROR_USER_PWD {
			loginResMes.Code = 403
			loginResMes.Error = err.Error()
		} else {
			loginResMes.Code = 505
			loginResMes.Error = "服务器内部错误..."
		}
	} else {
		loginResMes.Code = 200
		//这里，因为用户登录成功，我们就把该登录成功的用放入到userMgr中
		//将登录成功的用户的userId 赋给 this，this指的就是uerProcess的对象，因为
		// uerProcess.go有一个结构体属性是UserId。
		this.UserId = loginMes.UserId
		userMgr.AddOnlineUser(this)
		//1、通知其它的在线用户， 我上线了
		this.NotifyOthersOnlineUser()
		//2、将当前在线用户的id 放入到loginResMes.UsersId，用于通知新登录的人所有在线人的id
		//遍历 userMgr.onlineUsers，他是一个map,里面存储了所有在线的用户id.
		for id, _ := range userMgr.onlineUsers {
			loginResMes.UsersId = append(loginResMes.UsersId, id)
		}
		loginResMes.UserName = loginMes.UserName
		fmt.Println(user, "登录成功")
	}

	//3将 loginResMes 序列化
	data, err := json.Marshal(loginResMes)
	if err != nil {
		fmt.Println("json.Marshal fail", err)
		return
	}

	//4. 将data 赋值给 resMes
	resMes.Data = string(data)

	//5. 对resMes 进行序列化，准备发送
	data, err = json.Marshal(resMes)
	if err != nil {
		fmt.Println("json.Marshal fail", err)
		return
	}
	//6. 发送data, 我们将其封装到writePkg函数
	//因为使用分层模式(mvc), 我们先创建一个Transfer 实例，然后读取
	tf := &utils.Transfer{
		Conn: this.Conn,
	}
	err = tf.WritePkg(data)
	return
}
