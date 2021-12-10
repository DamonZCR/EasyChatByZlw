package frame;

import main.Client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JTextField;
import javax.swing.JButton;

public class Client_enterFrame extends JFrame implements ActionListener, KeyListener{
	public Client_enterFrame(Client client) {
		this.client = client;//将传来的client赋值给本类成员变量
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
		setSize(296, 249);
		WinCenter.center(this);
		//设置是否允许调整窗口大小
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jbt_exit.doClick();
			}
		});
		//设置标签：‘用户名’
		JLabel lblNewLabel = new JLabel("\u7528\u6237\u540D");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel.setBounds(23, 30, 81, 34);
		getContentPane().add(lblNewLabel);
		//用户名输入框
		jtf_username = new JTextField();
		jtf_username.addKeyListener(this);
		jtf_username.setBounds(114, 30, 143, 34);
		getContentPane().add(jtf_username);
		jtf_username.setColumns(10);
		
		//设置标签：‘服务器地址’
		JLabel lblNewLabel_1 = new JLabel("\u670D\u52A1\u5668\u5730\u5740");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(23, 74, 81, 34);
		getContentPane().add(lblNewLabel_1);
		//设置IP地址栏
		jtf_hostIp = new JTextField();
		jtf_hostIp.setBounds(114, 74, 143, 34);
		jtf_hostIp.addKeyListener(this);
		getContentPane().add(jtf_hostIp);
		try {
			//获取本地主机IP
			//getLocalHost()返回：本地主机的 IP 地址。getHostAddress();返回字符串格式的原始 IP 地址。
			String ip = (String)Inet4Address.getLocalHost().getHostAddress();
			jtf_hostIp.setText(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		jtf_hostIp.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("\u7AEF\u53E3\u53F7");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(23, 118, 81, 34);
		getContentPane().add(lblNewLabel_2);
		
		// 端口号
		jtf_hostPort = new JTextField();
		jtf_hostPort.addKeyListener(this);
		jtf_hostPort.setBounds(114, 118, 143, 34);
		getContentPane().add(jtf_hostPort);
		jtf_hostPort.setText("8889");
		jtf_hostPort.setColumns(10);
		//进入聊天室
		jbt_enter = new JButton("\u8FDB\u5165\u804A\u5929\u5BA4");
		jbt_enter.addActionListener(this);
		jbt_enter.addKeyListener(this);
		jbt_enter.setFont(new Font("宋体", Font.PLAIN, 14));
		jbt_enter.setBounds(23, 162, 108, 39);
		getContentPane().add(jbt_enter);
		//退出聊天室
		jbt_exit = new JButton("\u9000\u51FA\u804A\u5929\u5BA4");
		jbt_exit.setFont(new Font("宋体", Font.PLAIN, 14));
		jbt_exit.setBounds(144, 162, 113, 39);
		jbt_exit.addActionListener(this);
		getContentPane().add(jbt_exit);
	}

	/**
	 * 添加事件监听。
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jtf_username;
	private JTextField jtf_hostIp;
	private JTextField jtf_hostPort;
	private JButton jbt_enter;
	private JButton jbt_exit;
	private Client client;

	//按钮动作监听
	@Override
	public void actionPerformed(ActionEvent e) {
		//如果点击的是退出聊天
		if(e.getSource() == jbt_exit){
			setVisible(false);
			client.exitLogin();
		}
		//点击登录聊天
		if(e.getSource() == jbt_enter){
			//获取输入的用户名、IP和端口号
			String username = jtf_username.getText();
			username.trim();
			String hostIp = jtf_hostIp.getText();
			hostIp.trim();
			String hostPort = jtf_hostPort.getText();
			hostPort.trim();
			if(!username.equals("")){
				if(!hostIp.equals("")){
					if(!hostPort.equals("")){
						//执行登录。
						String login_mess = client.login(username, hostIp, hostPort);
						if(login_mess.equals("true")){//socket注册成功
							this.setVisible(false);//登录窗口消失
							client.showChatFrame(username);//用户名到聊天窗口
						}else{
							JOptionPane.showMessageDialog(this, login_mess);
						}
					}else{
						JOptionPane.showMessageDialog(this, "服务器连接端口号不能为空！");
					}
				}else{
					JOptionPane.showMessageDialog(this, "服务器地址不能为空！");
				}
			}else{
				JOptionPane.showMessageDialog(this, "用户名不能为空！");
			}
		}
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
