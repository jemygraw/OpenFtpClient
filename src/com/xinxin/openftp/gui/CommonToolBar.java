package com.xinxin.openftp.gui;
 
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.border.MatteBorder;
 
public class CommonToolBar {
	private JToolBar toolsToolBar; // 快捷工具工具条
	private JButton addTabToolButton;
	private JButton lcdToolButton;
	private JButton copyFileToolButton;
	private JButton pasteFileToolButton;
	private JButton openShellToolButton;
	private JButton showClientInfoToolButton;
	private JButton showServerInfoToolButton;
	private JButton helpToolButton;
	private JButton snaker;
	public JToolBar initCommonToolBar(){
		// 队列工具条
		toolsToolBar = new JToolBar("常规工具栏");
		toolsToolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolsToolBar.setOrientation(JToolBar.HORIZONTAL);
		addTabToolButton = new JButton(new ImageIcon("img/addtab.png"));
		 
		lcdToolButton=new JButton(new ImageIcon("img/curdir.png"));
		helpToolButton = new JButton(new ImageIcon("img/help.png"));
		
		addTabToolButton.setToolTipText("添加新标签");
		addTabToolButton.setActionCommand("addTabToolButton");
		addTabToolButton.setActionCommand("addTabToolButton");
	 
		lcdToolButton.setToolTipText("设置为当前路径");
		lcdToolButton.setActionCommand("lcdToolButton");
		copyFileToolButton=new JButton(new ImageIcon("img/copy.png"));
		copyFileToolButton.setActionCommand("copyFileToolButton");
		copyFileToolButton.setToolTipText("复制文件");
		pasteFileToolButton=new JButton(new ImageIcon("img/paste.png"));
		pasteFileToolButton.setActionCommand("pasteFileToolButton");
		pasteFileToolButton.setToolTipText("粘贴文件");
		openShellToolButton=new JButton(new ImageIcon("img/openShell.png"));
		openShellToolButton.setToolTipText("FTP客户端命令行工具");
		openShellToolButton.setActionCommand("openShellToolButton");
		showClientInfoToolButton=new JButton(new ImageIcon("img/client.png"));
		showClientInfoToolButton.setToolTipText("显示客户端信息");
		showClientInfoToolButton.setActionCommand("showClientInfoToolButton");
		showServerInfoToolButton=new JButton(new ImageIcon("img/server.png"));
		showServerInfoToolButton.setToolTipText("显示服务器信息");
		showServerInfoToolButton.setActionCommand("showServerInfoToolButton");
		helpToolButton.setToolTipText("帮助");
		helpToolButton.setActionCommand("helpToolButton");
		toolsToolBar.add(addTabToolButton);
		toolsToolBar.add(lcdToolButton);
		toolsToolBar.add(copyFileToolButton);
		toolsToolBar.add(pasteFileToolButton);
		toolsToolBar.add(openShellToolButton);
		toolsToolBar.add(showClientInfoToolButton);
		toolsToolBar.add(showServerInfoToolButton);
		toolsToolBar.add(helpToolButton);
		snaker=new JButton(new ImageIcon("img/snaker.png"));
		snaker.setToolTipText("让我们一起悼念...一个伟大的太阳落下了...");
		snaker.addMouseListener(new Funny());
		toolsToolBar.add(snaker);
		return toolsToolBar;
	}
	
	class Funny extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			 new Snake();
		}
	}
	class Snake {
		private JFrame f;
		private JLabel label;
		public Snake(){
			MatteBorder b=new MatteBorder(60,200,60,200,new ImageIcon("img/bg.png"));
			label=new JLabel(new ImageIcon("img/sun.png"));
			label.setBorder(b);
			f=new JFrame();
			f.getContentPane().add(label);
			f.addMouseListener(new go());
			f.setExtendedState(JFrame.MAXIMIZED_BOTH);
			f.setUndecorated(true);
			f.setAlwaysOnTop(true);
			f.setVisible(true);
		}
		class go extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				f.dispose();
			}
		}
	}
	public JButton getCopyFileToolButton() {
		return copyFileToolButton;
	}
	public JButton getPasteFileToolButton() {
		return pasteFileToolButton;
	}
	public JButton getOpenShellToolButton() {
		return openShellToolButton;
	}
	public JButton getShowClientInfoToolButton() {
		return showClientInfoToolButton;
	}
	public JButton getShowServerInfoToolButton() {
		return showServerInfoToolButton;
	}
	public JToolBar getToolsToolBar() {
		return toolsToolBar;
	}
	public JButton getAddTabToolButton() {
		return addTabToolButton;
	}
	public JButton getLcdToolButton() {
		return lcdToolButton;
	}
	public JButton getHelpToolButton() {
		return helpToolButton;
	}
	
}
