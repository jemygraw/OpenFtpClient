package com.xinxin.openftp.gui;
import java.awt.event.InputEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

public class ToolMenu{
	private JMenu toolMenu; // 工具
	private JMenu transferType;					//传输模式
	private JRadioButton pasvType;
	private JRadioButton portType;
	private JMenu dataTransferType;				//数据模式
	private JRadioButton asciiDataType;			//ASCII数据模式
	private JRadioButton binaryDataType;		//BINARY数据模式
	
	private JMenu lookAndFeel;					//软件的外观 
	private JRadioButton metal;
	private JRadioButton apple;
	private JRadioButton windows;
	private JRadioButton motif;
	private JRadioButton liquid;
	private JMenuItem showClientInfo;
	private JMenuItem showServerInfo;
 
	public JMenu initToolMenu(){
		toolMenu=new JMenu("工具(T)");
		toolMenu.setMnemonic('T');	
		transferType=new JMenu("传输模式(M)");
		pasvType=new JRadioButton("PASV 被动模式");
		pasvType.setActionCommand("pasvType");
		portType=new JRadioButton("PORT 主动模式");
		portType.setActionCommand("portType");
		ButtonGroup mbg=new ButtonGroup();
		mbg.add(pasvType);
		mbg.add(portType);
		transferType.add(pasvType);
		transferType.add(portType);
		toolMenu.add(transferType);
		dataTransferType=new JMenu("数据模式(D)");
		dataTransferType.setMnemonic('D');
	 
		asciiDataType=new JRadioButton("文本模式");
		asciiDataType.setActionCommand("asciiDataType");
		binaryDataType=new JRadioButton("二进制模式");
		binaryDataType.setActionCommand("binaryDataType");
		ButtonGroup dataBg=new ButtonGroup();
	  
		dataBg.add(asciiDataType);
		dataBg.add(binaryDataType);
		 
		dataTransferType.add(asciiDataType);
		dataTransferType.add(binaryDataType);
		toolMenu.add(dataTransferType);

		toolMenu.addSeparator();
		metal=new JRadioButton("Java 外观");
		windows=new JRadioButton("Windows 外观");
		motif=new JRadioButton("Solaris 外观");
		liquid=new JRadioButton("Liquid 外观");
		apple=new JRadioButton("QuaQua Mac 外观");
		ButtonGroup bg=new ButtonGroup();
		bg.add(metal);
		bg.add(windows);
		bg.add(motif);
		bg.add(liquid);
		bg.add(apple);
		metal.setActionCommand("metal");
		liquid.setActionCommand("liquid");
		motif.setActionCommand("motif");
		windows.setActionCommand("windows");
		apple.setActionCommand("apple");
		lookAndFeel=new JMenu("软件皮肤(S)");
		lookAndFeel.setMnemonic('S');
		lookAndFeel.add(metal);
		lookAndFeel.add(liquid);
		lookAndFeel.add(motif);
		lookAndFeel.add(windows);
		lookAndFeel.add(apple);
		toolMenu.add(lookAndFeel);
		toolMenu.addSeparator();
		
		showClientInfo=new JMenuItem("显示客户端信息(C)",new ImageIcon("img/client.png"));
		showClientInfo.setMnemonic('C');
		showClientInfo.setActionCommand("showClientInfo");
		toolMenu.add(showClientInfo);
		showServerInfo=new JMenuItem("显示服务器信息(I)",new ImageIcon("img/server.png"));
		showServerInfo.setMnemonic('I');
		showServerInfo.setAccelerator(KeyStroke.getKeyStroke('I',InputEvent.CTRL_MASK));
		showServerInfo.setActionCommand("showServerInfo");
		toolMenu.add(showServerInfo);
		return toolMenu;
	}

	public JRadioButton getPasvType() {
		return pasvType;
	}

	public JRadioButton getPortType() {
		return portType;
	}

	public JRadioButton getAsciiDataType() {
		return asciiDataType;
	}

	public JRadioButton getBinaryDataType() {
		return binaryDataType;
	}

	public JRadioButton getMetal() {
		return metal;
	}

	public JRadioButton getApple() {
		return apple;
	}

	public JRadioButton getWindows() {
		return windows;
	}

	public JRadioButton getMotif() {
		return motif;
	}

	public JRadioButton getLiquid() {
		return liquid;
	}

	public JMenuItem getShowClientInfo() {
		return showClientInfo;
	}

	public JMenuItem getShowServerInfo() {
		return showServerInfo;
	}
}
