package com.xinxin.openftp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;
public class HelpMenu implements ActionListener{
	private JMenu helpMenu;						//帮助
	//定义帮助菜单下面的子菜单
	private JMenuItem helpContent;				//帮助文档
	private JMenuItem officialSite;				//官方主页
	private JMenuItem techSupport;				//技术支持
	private JMenuItem aboutSoftware;			//关于本软件
	public JMenu initHelpMenu(){
		helpMenu=new JMenu("帮助(H)");
		helpMenu.setMnemonic('H');
		helpContent=new JMenuItem("内容(C)",new ImageIcon("img/help.png"));
		helpContent.setAccelerator(KeyStroke.getKeyStroke("F1"));
		helpContent.setMnemonic('C');
		helpContent.addActionListener(this);
		helpContent.setActionCommand("helpContent");
		helpMenu.add(helpContent);
		helpMenu.addSeparator();
		officialSite=new JMenuItem("官方网站(P)",new ImageIcon("img/official.png"));
		officialSite.setMnemonic('P');
		officialSite.addActionListener(this);
		officialSite.setActionCommand("officialSite");
		helpMenu.add(officialSite);

		techSupport=new JMenuItem("技术支持(S)",new ImageIcon("img/support.png"));
		techSupport.setMnemonic('S');
		techSupport.addActionListener(this);
		techSupport.setActionCommand("techSupport");
		helpMenu.add(techSupport);
		helpMenu.addSeparator();
		aboutSoftware=new JMenuItem("关于(A)",new ImageIcon("img/about.png"));
		aboutSoftware.setMnemonic('A');
		aboutSoftware.addActionListener(this);
		aboutSoftware.setActionCommand("aboutSoftware");
		helpMenu.add(aboutSoftware);
		return helpMenu;
	}

	@Override
	public void actionPerformed(ActionEvent event) {		
		if(event.getActionCommand().equalsIgnoreCase("helpContent") || event.getActionCommand().equalsIgnoreCase("helpToolButton")){
			String  helpfileurl="help.chm";   
			 try {
				Runtime.getRuntime().exec("hh"+" "+helpfileurl);
			}catch (IOException e) {
				return;
			}   
		}else if(event.getActionCommand().equalsIgnoreCase("officialSite")){
			String uriStr="http://hi.csdn.net/space-3820011.html";
			if(!Desktop.isDesktopSupported()){
				JOptionPane.showMessageDialog(null, "不支持打开桌面浏览器,请直接访问http://hi.csdn.net/space-3820011.html");
				return;
			}
			Desktop desktop=Desktop.getDesktop();
			try{
				URI uri=new URI(uriStr);
				desktop.browse(uri);
			}catch(URISyntaxException e){
				return;
			}catch(IOException e){
				return;
			}
		}else if(event.getActionCommand().equalsIgnoreCase("techSupport")){
			String uriStr="mailto://xinxinli1234@hotmail.com";
			if(!Desktop.isDesktopSupported()){
				JOptionPane.showMessageDialog(null, "不支持打开默认邮件客户端,请发邮件到:xinxinli1234@hotmail.com");
				return;
			}
			Desktop desktop=Desktop.getDesktop();
			try{
				URI uri=new URI(uriStr);
				desktop.browse(uri);
			}catch(URISyntaxException e){
				return;
			}catch(IOException e){
				return;
			}
		}else if(event.getActionCommand().equalsIgnoreCase("aboutSoftware")){
			new About();
		}
	}
}
class About extends JDialog implements ActionListener{
	private static final long serialVersionUID = 4745639077829122095L;
	private JTabbedPane tabbedPane;
	private JPanel softPane;
	private JPanel infoPane;
	private JPanel rightPane;
	private JPanel yescancelPanel;
	private JButton yesButton;
	private JButton cancelButton;
	private JLabel softInfoLabel;
	private JTextArea rightArea;
	private JTextArea infoArea;
	private JScrollPane infoScrollPane;
	private String copyright;
	private JScrollPane rightScrollPane;
	public void initPanel(){
		softPane=new JPanel();
		softPane.setLayout(new BorderLayout());
		ImageIcon logo=new ImageIcon("img/logo.png");
		softInfoLabel=new JLabel(logo);	 
		softPane.add(softInfoLabel,BorderLayout.CENTER);

		infoPane=new JPanel();
		infoPane.setLayout(new BorderLayout());
		infoArea=new JTextArea();
		infoArea.setEditable(false);
		infoScrollPane=new JScrollPane(infoArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		infoArea.append("用户信息:\n"+
				"用户主目录:"+System.getProperty("user.home")+"\n"+
				"用户名:"	+System.getProperty("user.name")+"\n\n"+
				"Java虚拟机信息:\n"+
				"Java安装目录:"+System.getProperty("java.home")+"\n"+
				"Java版本号:"+System.getProperty("java.version")+"\n"+
				"Java虚拟机名称:"+System.getProperty("java.vm.name")+"\n\n"+
				"系统信息:\n"+
				"系统体系结构:"+System.getProperty("os.arch")+"\n"+
				"系统名称:"+System.getProperty("os.name")+"\n"+
				"系统版本号:"+System.getProperty("os.version")
		);
		infoPane.add(infoScrollPane,BorderLayout.CENTER);
		rightPane=new JPanel();
		rightPane.setLayout(new BorderLayout());
		rightArea=new JTextArea();
		rightArea.setAutoscrolls(true);
		rightArea.setEditable(false);
		rightScrollPane=new JScrollPane(rightArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		rightArea.setText(copyright);
		rightPane.add(rightScrollPane,BorderLayout.CENTER);
		yescancelPanel=new JPanel();
	}
	public About(){
		this.setTitle("关于 OpenFTP");
		this.setLayout(new BorderLayout());
		copyright="许可协议:\r\n本软件是自由软件，遵循GPL授权，详情请参阅\r\n安装目录下面的Licence.txt文件。\r\n\r\n" +
		"版权声明:\r\n本软件遵循 GPL 协议发布。任何的复制，修改，\r\n传播都遵循GPL协议。详情请参阅安装目录下面\r\n的Licence.txt文件。\r\n\r\n" +
		"商标注册:\r\nOpenFTP为金鑫鑫注册的商标,保留所有权利。"
		;
		initPanel();
		yesButton=new JButton("确定(O)");
		yesButton.setMnemonic('O');
		yesButton.addActionListener(this);
		cancelButton=new JButton("取消(N)");
		cancelButton.setMnemonic('N');
		cancelButton.addActionListener(this);
		yescancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		yescancelPanel.add(yesButton);
		yescancelPanel.add(cancelButton);

		tabbedPane=new JTabbedPane();
		tabbedPane.addTab("程序", softPane);
		tabbedPane.addTab("信息", infoPane);
		tabbedPane.addTab("版权", rightPane);
		this.getContentPane().add(tabbedPane,BorderLayout.CENTER);
		this.getContentPane().add(yescancelPanel,BorderLayout.SOUTH);
		this.setIconImage(new ImageIcon("img/about.png").getImage());
		this.setModal(true);
		this.setSize(320,400);
		Common.getPos(320,400);
		this.setLocation(Common.posX,Common.posY);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.dispose();
	}
}