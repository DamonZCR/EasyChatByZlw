package frame;

import com.alibaba.fastjson.JSON;
import main.Client;
import message.LoginResMes;
import message.NotifyUserStatusMes;
import message.SmsMes;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Client_chatFrame extends JFrame implements ActionListener,
		KeyListener, ListSelectionListener {
	public Client_chatFrame(Client client, String title) {
		this.client = client;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setIconImage(Toolkit.getDefaultToolkit().getImage(ResourcesUtil.resourcesPath + "/socket.jpg"));
		setTitle("\u804A\u5929\u5BA4" + "  " + title);
		setSize(450, 325);
		WinCenter.center(this);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				jbt_exit.doClick();
			}
		});
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(BorderFactory.createTitledBorder("聊天消息"));
		scrollPane.setBounds(10, 10, 283, 167);
		scrollPane.setWheelScrollingEnabled(true);
		getContentPane().add(scrollPane);
		//聊天信息展示面板
		jta_disMess = new JTextArea();
		jta_disMess.setEditable(false);
		scrollPane.setViewportView(jta_disMess);

		jtf_inputMess = new JTextField();
		jtf_inputMess.addKeyListener(this);
		jtf_inputMess.setBounds(10, 242, 192, 32);
		getContentPane().add(jtf_inputMess);
		jtf_inputMess.setColumns(10);

		jbt_trans = new JButton("\u53D1  \u9001");
		jbt_trans.setFont(new Font("宋体", Font.PLAIN, 14));
		jbt_trans.setBounds(212, 241, 93, 32);
		jbt_trans.addActionListener(this);
		getContentPane().add(jbt_trans);
		
		//清除聊天记录
		jbt_clear = new JButton("\u6E05\u9664\u804A\u5929\u8BB0\u5F55");
		jbt_clear.setFont(new Font("宋体", Font.PLAIN, 14));
		jbt_clear.setBounds(158, 187, 135, 37);
		jbt_clear.addActionListener(this);
		getContentPane().add(jbt_clear);

		//退出聊天室
		jbt_exit = new JButton("\u9000\u51FA\u804A\u5929\u5BA4");
		jbt_exit.setFont(new Font("宋体", Font.PLAIN, 14));
		jbt_exit.setBounds(20, 189, 128, 37);
		jbt_exit.addActionListener(this);
		getContentPane().add(jbt_exit);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(BorderFactory.createTitledBorder("在线用户"));
		scrollPane_1.setBounds(303, 10, 128, 214);
		getContentPane().add(scrollPane_1);

		jlt_disUsers = new JList();
		jlt_disUsers.setVisibleRowCount(4);
		jlt_disUsers.setSelectedIndex(0);
		jlt_disUsers
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jlt_disUsers.addListSelectionListener(this);
		scrollPane_1.setViewportView(jlt_disUsers);

		//单人聊天
		jbt_singlChat = new JButton("\u5355\u4EBA\u804A\u5929");
		jbt_singlChat.setBounds(315, 241, 116, 32);
		jbt_singlChat.addActionListener(this);
		getContentPane().add(jbt_singlChat);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jtf_inputMess;
	private JTextArea jta_disMess;
	private JButton jbt_trans;
	private JButton jbt_clear;
	private JButton jbt_exit;
	private JList jlt_disUsers;
	private JButton jbt_singlChat;
	private JScrollPane scrollPane_1;
	private Client client;

	//发送消息
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbt_clear) {
			jta_disMess.setText("");
		}
		if (e.getSource() == jbt_trans) {
			String mess = jtf_inputMess.getText();
			mess.trim();
			jtf_inputMess.setText("");
			if (mess.equals("")) {
				JOptionPane.showMessageDialog(this, "不能发送空消息");
				jtf_inputMess.setText("");
			} else {
				client.transMess(mess);//传送消息
			}
		}
		if (e.getSource() == jbt_exit) {
			if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this,
					"是否确定要退出聊天室？", "提示", JOptionPane.OK_CANCEL_OPTION)) {
				this.setVisible(false);
				client.exitChat();
				System.exit(0);
			}
		}
		if (e.getSource() == jbt_singlChat) {
			String user_names = (String) jlt_disUsers.getSelectedValue();
			if (user_names == null) {
				JOptionPane.showMessageDialog(this, "您未选择聊天对象\n请选择要单独聊天的对象");
			} else {
				if (!client.c_singleFrames.containsKey(user_names)) {
					createSingleChatFrame(user_names);
				} else {
					client.c_singleFrames.get(user_names)
							.setFocusableWindowState(true);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			if (arg0.getSource() == jtf_inputMess) {
				jbt_trans.doClick();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	
	//客户端聊天文本框展示信息
	//展示消息格式为：用户ID + 服务器端接收到的时间 + “\n(下一行)” + 显示的消息
	public void setDisMess(String text) {
		SmsMes smsMes = JSON.parseObject(text, SmsMes.class);
		StringBuilder sb = new StringBuilder(smsMes.getUser().getUserId());
		sb.append("(Time)\n");
		sb.append(smsMes.getContent());
		jta_disMess.append(sb + "\n");
		jta_disMess.setCaretPosition(jta_disMess.getText().length());
	}

	public void setUpDownUsers(String chat_re) {
		NotifyUserStatusMes nus = JSON.parseObject(chat_re, NotifyUserStatusMes.class);
		System.out.println("需要序列化的是：" + chat_re);
		System.out.println("在setUpDownUsers函数中输出通知的状态：" + nus.getStatus());

		if (nus.getStatus() == 1){//状态等于1，说明是上线
			//如果新上线的用户不在用户列表就添加，如果新上线的用户在本地存在但是用户名以变化就什么也不干。
			if (!client.clientuserid.contains(nus.getUserId())){
				client.username_online.add(0, "用户" + nus.getUserId());
				client.clientuserid.add(0, nus.getUserId());
				String[] infos = new String[client.username_online.size()];
				for (int i = 0; i < client.username_online.size(); i++) {
					infos[i] = client.username_online.get(i);
				}
				jlt_disUsers.removeAll();
				jlt_disUsers.setListData(infos);
			}
		}else {//说明是有人下线的通知信息
			if (client.clientuserid.contains(nus.getUserId())){//本地用户列表存在此用户的记录。
				//本地用户列表只剩下一个人，说明这条离线信息很可能就是用户列表中最后一个在线的用户的离线消息。
				if (client.username_online.size() == 1){
					String[] s = new String[] {};//窗口的用户列表可以设为空了。
					if (!client.c_singleFrames.isEmpty()) {
						ListModel list = jlt_disUsers.getModel();
						for (int i = 0; i < list.getSize(); i++) {
							if (client.c_singleFrames.get(list.getElementAt(i)) != null) {
								client.c_singleFrames.get(list.getElementAt(i))
										.setExitNotify();
							}
						}
					}
					client.clientuserid.clear();
					client.username_online.clear();
					jlt_disUsers.removeAll();
					jlt_disUsers.setListData(s);
				}else {//在现有的用户列表中删除这个用户。
					int i1 = client.clientuserid.indexOf(nus.getUserId());
					String name = client.username_online.remove(i1);
					try {
						client.clientuserid.remove(i1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 关闭在本地和它有关的单聊窗口
					if (client.c_singleFrames.containsKey(name)) {
						client.c_singleFrames.get(name).closeSingleFrame();
						client.c_singleFrames.remove(name);
					}
				}
			}else {//收到的用户离线信息，但是本地用户列表并没有该用户的记录。
				//尝试重新从服务器获取用户列表。
				System.out.println("用户缺少！用户列表需要更新！");
			}
		}
	}
	// 这里接收只是所有客户端的ID，而非用户名，用户名后期加。
	public void setDisUsers(String chat_re) {
		LoginResMes lrm = JSON.parseObject(chat_re, LoginResMes.class);
		if (lrm.getCode() != 200){
			JOptionPane.showMessageDialog(this,
					"登录失败！原因: " + lrm.getError());
			System.exit(0);
		}

		int[] users = lrm.getUsersId();
		// 用户列表以 用户 + 用户的ID来区分呈现，因为项目服务器端并没有优化传过来用户的用户名。
		String[] infos = new String[users.length - 1];

		//当前只存在自己一个用户,初始登录，发现只有自己一个用户时，只需展示空白的用户列表。
		if (users.length == 1) {
			System.out.println("进入if，是因为只有我一个用户！");
			String[] s = new String[] {};
			jlt_disUsers.removeAll();
			jlt_disUsers.setListData(s);
		} else {//用户列表存在除自己以外的其他用户。
			System.out.println("我真的没有进入if，进入了else");
			// 本地用户列表为空，最新的用户列表除了本客户端的其他全部客户端加入用户名列表和线程id列表
			System.out.println("本地用户为空我正在全部加入新的用户列表！");
			for (int i = 0; i < users.length; i++) {
				if (users[i] != client.getUserId()) {
					client.username_online.add(0, "用户" + users[i]);
					client.clientuserid.add(0, users[i]);
				}
			}
			try {
				for (int i = 0; i < users.length - 1; i++) {
					infos[i] = client.username_online.get(i);
				}
			} catch (Exception e) {
			}
			jlt_disUsers.removeAll();
			jlt_disUsers.setListData(infos);
		}
	}

	public void closeClient() {
		JOptionPane.showMessageDialog(this, "服务器已关闭", "提示",
				JOptionPane.OK_OPTION);
		client.exitClient();
		setVisible(false);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == jlt_disUsers) {
		}
	}

	public void createSingleChatFrame(String name) {
		Client_singleFrame c_singlFrame = new Client_singleFrame(client, name);
		client.c_singleFrames.put(name, c_singlFrame);
		try {
			c_singlFrame.userThreadID = client.clientuserid
					.get(client.username_online.indexOf(name));
		} catch (Exception e) {
		}

		c_singlFrame.setVisible(true);
	}

	public void setSingleFrame(String chat_re) {
		String[] infos = chat_re.split("@single");
		try {
			if (client.c_singleFrames.containsKey(infos[0])) {
				client.c_singleFrames.get(infos[0]).setDisMess(infos[3]);
			} else {
				createSingleChatFrame(infos[0]);
				client.c_singleFrames.get(infos[0]).setDisMess(infos[3]);
			}
		} catch (Exception e) {
		}
	}
}
