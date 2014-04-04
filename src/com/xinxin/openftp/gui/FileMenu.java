package com.xinxin.openftp.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class FileMenu {
	private JMenu fileMenu; // 文件
	// 定义文件菜单下面的子菜单
	private JMenuItem newTab; // 新建标签(T) CTRL+T
	private JMenuItem connect; // 连接(C)
	private JMenuItem disconnect; // 断开连接(D) CTRL+D
	private JMenuItem close; // 关闭(L)

	/* 初始化菜单项目 */
	public JMenu initFileMenu() {
		fileMenu = new JMenu("文件(F)");
		fileMenu.setMnemonic('F');
		newTab = new JMenuItem("新建标签(T)", new ImageIcon("img/addtab.png"));
		newTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_DOWN_MASK));
		newTab.setMnemonic('T');
		newTab.setActionCommand("addNewTab");
		fileMenu.add(newTab);
		fileMenu.addSeparator();

		connect = new JMenuItem("连接(C)", new ImageIcon("img/connect.png"));
		connect.setMnemonic('C');
		connect.setActionCommand("connect");
		fileMenu.add(connect);
 

		disconnect = new JMenuItem("断开连接(D)", new ImageIcon("img/discon.png"));
		disconnect.setAccelerator(KeyStroke.getKeyStroke('D',InputEvent.CTRL_DOWN_MASK));
		disconnect.setMnemonic('D');
		disconnect.setActionCommand("disconnect");
		fileMenu.add(disconnect);
		fileMenu.addSeparator();

		close = new JMenuItem("关闭(Z)", new ImageIcon("img/exit.png"));
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		close.setMnemonic('Z');
		close.setActionCommand("close");
		fileMenu.add(close);
		return fileMenu;
	}

	public JMenuItem getNewTab() {
		return newTab;
	}

	public JMenuItem getConnect() {
		return connect;
	}
 
	public JMenuItem getDisconnect() {
		return disconnect;
	}

	public JMenuItem getClose() {
		return close;
	}
	
}
