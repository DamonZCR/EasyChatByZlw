package frame;

import main.Client;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class Client_registerFrame extends JFrame implements ActionListener, KeyListener{
	public static void main(String[] args) {
		Client client = new Client();
		Client_registerFrame crg = new Client_registerFrame(client);
		crg.setVisible(true);
	}
	public Client_registerFrame(Client client) {
		this.client = client;
		try {
			//这是把外观设置成你所使用的平台的外观,程序在哪个平台运行,显示的窗口,对话框外观将是哪个平台的外观
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		setIconImage(Toolkit.getDefaultToolkit().getImage(ResourcesUtil.resourcesPath + "/socket.jpg"));
		setTitle("\u804A\u5929\u5BA4");//使用的是Unicode编码转汉字
		getContentPane().setLayout(null);
		setSize(320, 287);
		WinCenter.center(this);
		//设置是否允许调整窗口大小
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jbt_exit.doClick();
			}
		});
		//以下两个组件之间的间距y坐标是44
		//设置标签：‘用户ID’
		JLabel lblNewLabel = new JLabel("\u7528\u6237\u0049\u0044");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel.setBounds(33, 30, 81, 34);
		getContentPane().add(lblNewLabel);
		//用户ID输入框
		jtf_userId = new JTextField();
		jtf_userId.addKeyListener(this);
		jtf_userId.setBounds(124, 30, 143, 34);
		getContentPane().add(jtf_userId);
		jtf_userId.setColumns(10);

		//设置标签：密码
		JLabel pwlNewLabel = new JLabel("\u5BC6\u7801");
		pwlNewLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		pwlNewLabel.setBounds(33, 74, 81, 34);
		getContentPane().add(pwlNewLabel);
		//密码输入框
		jtf_pwd = new JPasswordField("123");
		jtf_pwd.setEchoChar('*');
		jtf_pwd.addKeyListener(this);
		jtf_pwd.setBounds(124, 74, 143, 34);
		getContentPane().add(jtf_pwd);
		jtf_pwd.setColumns(10);

		//设置标签：‘用户名’
		JLabel lblNewLabel_1 = new JLabel("\u7528\u6237\u540D");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(33, 118, 81, 34);
		getContentPane().add(lblNewLabel_1);
		//设置用户名栏，默认为：用户
		jtf_username = new JTextField();
		jtf_username.setBounds(124, 118, 143, 34);
		jtf_username.addKeyListener(this);
		getContentPane().add(jtf_username);
		jtf_username.setText("用户");
		jtf_username.setColumns(10);
		// 性别
		JLabel lblNewLabel_2 = new JLabel("\u6027\u522B");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(33, 162, 81, 34);
		getContentPane().add(lblNewLabel_2);
		// 端口号输入面板
		jtf_sex = new JTextField();
		jtf_sex.addKeyListener(this);
		jtf_sex.setBounds(124, 162, 143, 34);
		getContentPane().add(jtf_sex);
		jtf_sex.setText("未知");
		jtf_sex.setColumns(10);
		//注册
		jbt_enter = new JButton("\u6CE8\u518C");
		jbt_enter.addActionListener(this);
		jbt_enter.addKeyListener(this);
		jbt_enter.setFont(new Font("宋体", Font.PLAIN, 14));
		jbt_enter.setBounds(33, 206, 108, 39);
		getContentPane().add(jbt_enter);
		//退出
		jbt_exit = new JButton("\u9000\u51FA");
		jbt_exit.setFont(new Font("宋体", Font.PLAIN, 14));
		jbt_exit.setBounds(154, 206, 113, 39);
		jbt_exit.addActionListener(this);
		getContentPane().add(jbt_exit);
	}

	/**
	 * 添加事件监听。
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jtf_userId;
	private JPasswordField jtf_pwd;
	private JTextField jtf_username;
	private JTextField jtf_sex;
	private JButton jbt_enter;
	private JButton jbt_exit;
	private Client client;
	private Client_enterFrame c_enterFrame;

	public Client_enterFrame getC_enterFrame() {
		return c_enterFrame;
	}
	public void setC_enterFrame(Client_enterFrame c_enterFrame) {
		this.c_enterFrame = c_enterFrame;
	}

	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}

	//按钮动作监听
	@Override
	public void actionPerformed(ActionEvent e) {
		//如果点击的是退出聊天
		if(e.getSource() == jbt_exit){
			setVisible(false);
			client.exitClient();
		}
		//点击登录聊天
		if(e.getSource() == jbt_enter){
			//获取输入的用户名、IP和端口号
			String userStr = jtf_userId.getText();
			userStr.trim();
			String pwd = new String(jtf_pwd.getPassword());
			String username = jtf_username.getText();
			username.trim();
			String sex = jtf_sex.getText();
			sex.trim();
			if(isInteger(userStr)){
				int userId = Integer.parseInt(userStr);
				if (!pwd.equals("")){
					if(!username.equals("")){
						if(!sex.equals("")){
							//执行注册。
							boolean login_mess = false;
							try {
								login_mess = client.register(userId, pwd, username, sex);
							} catch (IOException | InterruptedException interruptedException) {
								interruptedException.printStackTrace();
							}

							if(login_mess){//注册成功
								this.setVisible(false);//窗口消失
								c_enterFrame = new Client_enterFrame(client);
								client.setC_enterFrame(c_enterFrame);//注入到本类中
								//使窗口可见，Client_enterFrame继承的JFrame自带方法
								c_enterFrame.setVisible(true);
							}else{
								JOptionPane.showMessageDialog(this, "服务器连接错误！");
							}
						}else{
							JOptionPane.showMessageDialog(this, "性别不能为空！");
						}
					}else{
						JOptionPane.showMessageDialog(this, "用户名不能为空！");
					}
				}else {
					JOptionPane.showMessageDialog(this, "密码不能为空！");
				}
			}else{
				JOptionPane.showMessageDialog(this, "ID不能为空或者ID不为数字！");
			}
		}
	}

	//判断一个字符串是不是整数
	public static boolean isInteger(String str) {
		if (StringUtils.isBlank(str)){
			return false;
		}
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			jbt_enter.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
