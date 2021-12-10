package main;

import com.alibaba.fastjson.JSON;
import frame.Client_chatFrame;
import frame.Client_enterFrame;
import frame.Client_singleFrame;
import message.LoginMes;
import message.Message;
import utils.MessageUtils;
import utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Client extends Thread{

	public Socket c_socket ;
	private Client_chatFrame c_chatFrame;
	private Client_enterFrame c_enterFrame;
	private Client_singleFrame c_singleFrame;
	private MessageUtils mesus;
	private Message mess;
	public Utils utils;

	private boolean flag_exit = false;
	
	private int threadID;
	
	public Map<String, Client_singleFrame> c_singleFrames;
	public  List<String> username_online;//list数组存储在线客户端用户名
	public  List<Integer> clientuserid;//用户ID
	public String username = null;
	public String chat_re;
	
	//getter, setter方法
	public Client_chatFrame getC_chatFrame() {
		return c_chatFrame;
	}
	public Client_singleFrame getC_singlFrame() {
		return c_singleFrame;
	}
	public void setC_singleFrame(Client_singleFrame c_singlFrame) {
		this.c_singleFrame = c_singlFrame;
	}
	public void setC_chatFrame(Client_chatFrame c_chatFrame) {
		this.c_chatFrame = c_chatFrame;
	}
	public Client_enterFrame getC_enterFrame() {
		return c_enterFrame;
	}
	public void setC_enterFrame(Client_enterFrame c_enterFrame) {
		this.c_enterFrame = c_enterFrame;
	}
	public Utils getUtils() {
		return utils;
	}
	public void setUtils(Utils utils) {
		this.utils = utils;
	}

	public int getThreadID() {
		return threadID;
	}
	public void setThreadID(int threadID) {
		this.threadID = threadID;
	}
	//添加无参构造方法，如没有构造方法，无参的构造方法系统也会自动添加
	public Client(){
		c_singleFrames = new HashMap<String, Client_singleFrame>();
		username_online = new ArrayList<String>();
		clientuserid = new ArrayList<Integer>();
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		Client_enterFrame c_enterFrame = new Client_enterFrame(client);
		client.setC_enterFrame(c_enterFrame);//注入到本类中
		//使窗口可见，Client_enterFrame继承的JFrame自带方法
		c_enterFrame.setVisible(true);
	}
	
	//Client_enterFrame类登录启动客户端时调用
	public String login(String username, String hostIp, String hostPort) {
		this.username = username;
		String login_mess = null;
		mesus = new MessageUtils();
		try {
			c_socket = new Socket(hostIp, Integer.parseInt(hostPort));
		} catch (NumberFormatException e) {
			login_mess = "接口号应为：1024<port<65535";
			return login_mess;
		} catch (UnknownHostException e) {
			login_mess = "未知服务器端异常";
			return login_mess;
		} catch (IOException e) {
			login_mess = "IO异常";
			return login_mess;
		}
		// 序列化登录信息

		utils = new Utils(c_socket);//初始化数据输入和数据输出流
		threadID = 2;//这里的线程ID设为手动假设，可以再优化时由服务器分发。
		try{
			utils.WritePkg(mesus.LogInMess(threadID, "2", username));
		}catch (InterruptedException e){
			System.out.println("登录时发生错误:" + e);
		}
		return "true";
	}
	//参数由Client_enterFream登录成功传来
	//打开本类中的线程
	public void showChatFrame(String username) {
		c_chatFrame = new Client_chatFrame(this,username);//this代表本类对象
		// 启动聊天窗口，并将客户端线程启动。
		c_chatFrame.setVisible(true);
		flag_exit = true;
		this.start();
	}

	@Override
	public void run() {
		while(flag_exit){
			try {
				chat_re = utils.ReadPkg();// 从输入流中读取信息。
			} catch (IOException e) {
				flag_exit = false;
				System.out.println("Client线程接收信息时错误:" + e);
			}
			System.out.println(chat_re);
			mess = JSON.parseObject(chat_re, Message.class);//将接收的字符串反序列化为Message对象。
			// 从输入流中读到的数据不为空。
			if(!mess.getType().isEmpty()){
				//信息类型，传送到聊天窗口展示。
				if(mess.getType().equals("SmsMes")){
					System.out.println("匹配到SmsMes");
					c_chatFrame.setDisMess(mess.getData());//传递消息以便展示
				}else{
					// 登录后服务器返回的在线用户列表
					if(mess.getType().equals("LoginResMes")){
						c_chatFrame.setDisUsers(mess.getData());
					}else{
						if(mess.getType().equals("NotifyUserStatusMes")){//如果是某一人离线的消息通知
							c_chatFrame.setUpDownUsers(mess.getData());//进行聊天用户的更新。
						}else{
							//以下暂未开放。
							System.out.println("进入到暂未开放区域！");
							if(chat_re.contains("@serverexit")){
								c_chatFrame.closeClient();
							}else{
								if(chat_re.contains("@single")){
									c_chatFrame.setSingleFrame(chat_re);
								}
							}
						}
					}
				}//外层else
			}//if结束
		}//while循环结束。
	}
	//客户端传递群聊消息的方法
	//Client_chatFrame中的按钮事件actionPerformed调用
	//IO流的方式通过socket传递给ClientThread
	public void transMess(String text) {
		try {
			utils.WritePkg(mesus.SmsMess(text, threadID));
		} catch (InterruptedException e) {
			System.out.println("transMess()发送信息时错误:" + e);
		}
	}
	//Client_chatFrame中的按钮事件actionPerformed调用
	//IO流的方式通过socket传递给ClientThread
	public void exitChat() {
		//try {
			//dos.writeUTF(username + "@exit" + getThreadID() + "@exit");
			flag_exit = false;
			System.exit(0);
		//} catch (IOException e) {
			//e.printStackTrace();
		//}
	}
	public void exitLogin() {
		System.exit(0);
	}
	public void exitClient() {
		flag_exit = false;
		System.exit(0);
	}
}
