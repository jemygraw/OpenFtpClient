package com.xinxin.openftp.gui;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class LoginInfoToolBar implements ItemListener {
	private JToolBar loginInfoToolBar; // 登录信息工具条
	// 登录信息工具条的子项目
	private JTextField name = new JTextField(15); // 用户名
	private JPasswordField pass = new JPasswordField(15); // 密码
	private JComboBox addressList = new JComboBox(); // 服务器地址
	private JTextField port = new JTextField("21", 4); // 服务器端口
	private JCheckBox anony = new JCheckBox(); // 匿名登录
	private JButton connectButton;
	public JToolBar initLoginInfoToolBar(){
		// 登录信息工具条
		JLabel username = new JLabel("用户名:");
		JLabel password = new JLabel("密码:");
		JLabel remoteport = new JLabel("端口:");
		JLabel anonyLogin = new JLabel("匿名登录");
		JLabel addressLabel = new JLabel("地址:");
		
		anony.addItemListener(this);
		pass.setEchoChar('*');
		connectButton = new JButton(new ImageIcon("img/connect.png"));
		connectButton.setActionCommand("connectButton");
		connectButton.setToolTipText("连接服务器");

		addressList.setEditable(true);
		addressList.configureEditor(addressList.getEditor(), "");
		addressList.addItem(new String("jinxinxin-pc"));
		addressList.addItem(new String("192.168.254.130"));
		addressList.addItem(new String("ftp.sjtu.edu.cn"));
		loginInfoToolBar = new JToolBar("用户登录信息工具栏");
		loginInfoToolBar.setOrientation(JToolBar.HORIZONTAL);
		loginInfoToolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		loginInfoToolBar.add(username);
		loginInfoToolBar.add(name);
		loginInfoToolBar.add(password);
		loginInfoToolBar.add(pass);
		loginInfoToolBar.add(remoteport);
		loginInfoToolBar.add(port);
		loginInfoToolBar.add(anony);
		loginInfoToolBar.add(anonyLogin);
		loginInfoToolBar.addSeparator();
		loginInfoToolBar.add(addressLabel);
		loginInfoToolBar.add(addressList);
		loginInfoToolBar.add(connectButton);
		return loginInfoToolBar;
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == anony) {
			if (anony.isSelected()) {
				this.name.setEnabled(false);
				this.pass.setEnabled(false);
			} else if (!anony.isSelected()) {
				this.name.setEnabled(true);
				this.pass.setEnabled(true);
			}
		} 
	}
	public JButton getConnectButton() {
		return connectButton;
	}
	public JTextField getName() {
		return name;
	}
	public JPasswordField getPass() {
		return pass;
	}
	public JComboBox getAddressList() {
		return addressList;
	}
	public JTextField getPort() {
		return port;
	}
	public JCheckBox getAnony() {
		return anony;
	}
	
}
