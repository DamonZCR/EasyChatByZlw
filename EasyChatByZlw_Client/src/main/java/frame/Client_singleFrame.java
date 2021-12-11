package frame;

import main.Client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Client_singleFrame extends JFrame implements ActionListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JTextArea jta_disMess;
	private JTextField jtf_inputMess;
	private JButton jbt_trans;
	public int userThreadID = 0;
	private Client client;
	private int toUserId;

	public Client_singleFrame(Client client, String title, int toUserId) {
		this.client = client;
		init(title);
		this.toUserId= toUserId;
	}

	private void init(String title) {
		try {
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
		WinCenter.center(this);
		setTitle(title);
		setSize(400, 400);
		setContentPane(createContentPanel());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeSingleFrame();
			}
		});
	}

	private Container createContentPanel() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder("聊天消息"));
		jp.setLayout(new BorderLayout());
		jta_disMess = new JTextArea();
		jta_disMess.setEditable(false);
		jp.add(BorderLayout.CENTER, new JScrollPane(jta_disMess));
		jp.add(BorderLayout.SOUTH, createInput());
		return jp;
	}

	private Component createInput() {
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder("发送消息"));
		jp.setLayout(new BorderLayout());
		jtf_inputMess = new JTextField();
		jtf_inputMess.addKeyListener(this);
		jbt_trans = new JButton("发送");
		jbt_trans.addActionListener(this);
		jp.add(jtf_inputMess, BorderLayout.CENTER);
		jp.add(jbt_trans, BorderLayout.EAST);
		return jp;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			if(arg0.getSource() == jtf_inputMess){
				jbt_trans.doClick();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbt_trans){
			String str = jtf_inputMess.getText();
			str.trim();
			jtf_inputMess.setText("");
			if(str.equals("")){
				JOptionPane.showMessageDialog(this, "信息不能为空");
			}else{
				SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
				String date = form.format(new Date());
				String mess = client.username + "  " + date + "\n" + str;
				// 客户端窗口自己输出这个消息。
				jta_disMess.append(mess + "\n");
				jta_disMess.setCaretPosition(jta_disMess.getText().length());

				try {
					client.utils.WritePkg(client.mesus.SingleChatMess(toUserId, client.getUserId(), mess));
				} catch (InterruptedException e1) {
					System.out.println("单独聊天信息发送错误:" + e1);
				}
			}
		}
	}

	public void setDisMess(String chat_re) {
		jta_disMess.append(chat_re + "\n");
		jta_disMess.setCaretPosition(jta_disMess.getText().length());
	}

	public void closeSingleFrame(){
		setExitNotify();
		client.c_singleFrames.remove(this.getTitle(), client.c_singleFrames);//本地移除这个私聊窗口记录。
		setVisible(false);
	}

	public void setExitNotify() {
		jta_disMess.append(this.getTitle() + "已下线.....");
		jbt_trans.setEnabled(false);
	}
}
