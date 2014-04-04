package com.xinxin.openftp.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import com.xinxin.openftp.config.FTPClientConfig;
import com.xinxin.openftp.ftpclient.FTPClientImpl;
import com.xinxin.openftp.ftpclient.FTPClientHelp;
public class OpenFTPShell extends JFrame{
	private static final long serialVersionUID = -919440165341340417L;
	private JTextArea outputArea;
	private JTextField inputArea;
	private JScrollPane scroll;
	private FTPClientImpl ftpClient;
	private FTPClientConfig config;
	private JPopupMenu popMenu;
	private int x;
	private int y;
	private Vector<String> cmdHist;
	private JList cmdList;
	private TextAreaAction textAreaAction;
	public void init(){
		textAreaAction=new TextAreaAction();
		cmdHist=new Vector<String>();
		cmdList=new JList();
		Font font=new Font("SansSerif",Font.PLAIN,18);
		outputArea=new JTextArea();
		outputArea.setEditable(false);
		outputArea.setBackground(Color.black);
		outputArea.setForeground(Color.GREEN);
		outputArea.setFont(font);
		inputArea=new JTextField();
		inputArea.setToolTipText("输入FTP命令,如果不熟悉FTP命令,可以输入help或者help help以查看使用方法 (*^__^*) ");
		inputArea.setFont(font);
		inputArea.setCaretColor(Color.YELLOW);
		inputArea.setForeground(Color.red);
		inputArea.setBackground(Color.darkGray);
		inputArea.requestFocus(true);
		inputArea.addKeyListener(new inputAreaKeyEvent());
		scroll=new JScrollPane(outputArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
				,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		popMenu=new JPopupMenu();
		JMenuItem copyText=new JMenuItem("复制(C)",new ImageIcon("img/copy.png"));
		copyText.setMnemonic('C');
		copyText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_DOWN_MASK));
		copyText.setActionCommand("copyText");
		copyText.addActionListener(textAreaAction);
		JMenuItem pasteText=new JMenuItem("粘贴(P)",new ImageIcon("img/paste.png"));
		pasteText.setMnemonic('P');
		pasteText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_DOWN_MASK));
		pasteText.setActionCommand("pasteText");
		pasteText.addActionListener(textAreaAction);
		JMenuItem clearText=new JMenuItem("清空(E)",new ImageIcon("img/clearText.png"));
		clearText.setMnemonic('E');
		clearText.setActionCommand("clearText");
		clearText.addActionListener(textAreaAction);
		JMenuItem history=new JMenuItem("历史(H)",new ImageIcon("img/history.png"));
		history.setMnemonic('H');
		history.setActionCommand("history");
		history.addActionListener(textAreaAction);
		popMenu.add(copyText);
		popMenu.add(pasteText);
		popMenu.add(clearText);
		popMenu.add(history);
		outputArea.add(popMenu);
		outputArea.addMouseListener(new TextAreaMouseEvent());

		ftpClient=new FTPClientImpl();
		ftpClient.setShowInfoTextArea(outputArea);
		ftpClient.setCmdLine(true);
		FTPClientHelp.setT(outputArea);
		config=new FTPClientConfig();
		config.loadConfigFile();
		config.loadConfigInfo();
		ftpClient.setDataTransferType(config.getDataType());
		ftpClient.setTransferType(config.getTransferType());
		ftpClient.setLocalWorkDir(config.getLocalWorkDir());
	}
	public OpenFTPShell(){
		this.init();
		this.getContentPane().add(inputArea,BorderLayout.SOUTH);
		this.getContentPane().add(scroll,BorderLayout.CENTER);
		this.setTitle("OpenFTP命令行工具");
		this.setIconImage(new ImageIcon("img/openShell.png").getImage());
		this.setSize(800, 500);	
		Common.getPos(800, 500);
		this.setLocation(Common.posX, Common.posY);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);

	}
	class TextAreaMouseEvent extends MouseAdapter{
		public void mousePressed( MouseEvent event ) {  //点击鼠标
			triggerEvent(event);  //调用triggerEvent方法处理事件
		}
		public void mouseReleased( MouseEvent event ) { //释放鼠标
			triggerEvent(event); 
		}
		private void triggerEvent(MouseEvent event) { //处理事件
			x=event.getXOnScreen();y=event.getYOnScreen();
			if (event.isPopupTrigger()) //如果是弹出菜单事件(根据平台不同可能不同)
				popMenu.show(event.getComponent(),event.getX(),event.getY());  //显示菜单
		}
	}
	class TextAreaAction implements ActionListener{
		private History histObj;
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand()=="copyText"){
				outputArea.copy();
			}else if(e.getActionCommand()=="pasteText"){
				outputArea.paste();
			}else if(e.getActionCommand()=="clearText"){
				outputArea.setText("");
			}else if(e.getActionCommand()=="history"){
				if(histObj!=null && histObj.getD()!=null){
					histObj.getD().dispose();
				}
				histObj=new History();
			}
		}

	}
	class History implements ActionListener{
		private static final long serialVersionUID = -6880467535109330138L;
		private JDialog d;
		private JScrollPane listScroll;
		private JButton closeHist;
		private JButton clearHist;
		private JToolBar toolBar;
		
		public JDialog getD() {
			return d;
		}
		public boolean init(){
			if(cmdHist.size()==0){
				JOptionPane.showMessageDialog(null, "历史记录为空!","提示",JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			d=new JDialog();
			closeHist=new JButton(new ImageIcon("img/exitHist.png"));
			closeHist.setToolTipText("关闭历史小面板");
			closeHist.addActionListener(this);
			clearHist=new JButton(new ImageIcon("img/clearHist.png"));
			clearHist.setToolTipText("清空历史小面板");
			clearHist.addActionListener(this);
			toolBar=new JToolBar();
			toolBar.setFloatable(false);
			toolBar.addMouseMotionListener(new MoveHistPos());
			toolBar.add(clearHist);toolBar.add(closeHist);
			cmdList.addMouseListener(new CmdListAction());
			listScroll=new JScrollPane(cmdList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			cmdList.setListData(cmdHist);
			return true;
		}
		public History(){
			if(this.init()){
				d.getContentPane().add(toolBar,BorderLayout.NORTH);
				d.getContentPane().add(listScroll,BorderLayout.CENTER);
				d.setIconImage(new ImageIcon("img/history.png").getImage());
				d.setUndecorated(true);
				d.setAlwaysOnTop(true);
				d.setSize(200, 200);
				d.setLocation(x, y);
				d.setVisible(true);
			}
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			 if(e.getSource()==closeHist){
				 d.dispose();
			 }else if(e.getSource()==clearHist){
				 cmdHist.clear();
				 d.repaint();
			 }
		}
		class MoveHistPos extends MouseMotionAdapter{
			public void mouseDragged(MouseEvent me) {
				 d.setLocation(me.getXOnScreen(),me.getYOnScreen());
			}
		}
	}
	class CmdListAction extends MouseAdapter{
		public void mouseClicked(MouseEvent me){
			if(me.getClickCount()==2){
				inputArea.setText((String)cmdList.getSelectedValue());
				inputArea.requestFocus(true);
			}
		}
	}
	class inputAreaKeyEvent extends KeyAdapter{
		class exec extends Thread{
			public void run(){
				
			}
		}
		public void keyTyped(java.awt.event.KeyEvent e) {
			if(e.getKeyChar()==java.awt.event.KeyEvent.VK_ENTER){
				String inputStr=inputArea.getText();
				inputArea.setText("");
				outputArea.append(inputStr+"\r\n");
				cmdHist.add(inputStr);
				cmdList.setListData(cmdHist);
				cmdList.repaint();
				execFTPCmd(inputStr);
			}
		}

		public void ambiCmd(){
			outputArea.append("二义性命令!\r\n");
		}
		public void wrongCmd(){
			outputArea.append("未知命令!\r\n");
		}
		public void notConnected(){
			outputArea.append("未连接!\r\n");
		}
		public boolean MatchesCmd(String cmd,String cmdPattern){
			Pattern pattern;
			Matcher matcher;
			pattern=Pattern.compile(cmdPattern);
			matcher=pattern.matcher(cmd);
			return matcher.matches();
		}
		public void execFTPCmd(String cmdStr){
			String cmdParts[]=cmdStr.trim().split(" ");
			int len=cmdParts.length;
			String cmd=cmdParts[0].toLowerCase();
			//APPEND ASCII
			if(cmd.startsWith("a")){
				if(this.MatchesCmd(cmd, "a")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "ap|app|appe|appen|append")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.append(cmdParts[1],cmdParts[1]);
						}else if(len==3){
							ftpClient.append(cmdParts[1],cmdParts[2]);
						}else{
							FTPClientHelp.appendUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "as|asc|asci|ascii")){
					if(ftpClient.isConnected()){
						ftpClient.ascii(); 
					}else{
						this.notConnected();
					}
				}else{
					this.wrongCmd();
				}
			}
			//BELL BINARY BYE
			else if(cmd.startsWith("b")){
				if(this.MatchesCmd(cmd, "b")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "be|bel|bell")){
					ftpClient.bell();
				}else if(this.MatchesCmd(cmd, "bi|bin|bina|binar|binary")){
					if(ftpClient.isConnected()){
						ftpClient.binary();
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "by|bye")){
					ftpClient.bye();
				}else{
					this.wrongCmd();
				}
			}
			//CD CLOSE CDUP
			else if(cmd.startsWith("c")){
				if(this.MatchesCmd(cmd, "c")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd,"cd")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.cd(cmdParts[1]);
						}else{
							FTPClientHelp.cdUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "cdu|cdup")){
					if(ftpClient.isConnected()){
						ftpClient.cdup();
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "cl|clo|clos|close")){
					if(ftpClient.isConnected()){
						ftpClient.close();
					}else{
						this.notConnected();
					}
				}else{
					this.wrongCmd();
				}
			}
			//DELETE DISCONNECT
			else if(cmd.startsWith("d")){
				if(this.MatchesCmd(cmd, "d|de|di")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "del|dele|delet|delete")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.delete(cmdParts[1]);
						}else{
							FTPClientHelp.deleteUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "dis|disc|disco|discon|disconn|disconne|disconnec|disconnect")){
					if(ftpClient.isConnected()){
						ftpClient.disconnect();
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "dir")){
					if(ftpClient.isConnected()){
						if(len==1){
							ftpClient.dir();
						}else if(len==2){
							ftpClient.dir(cmdParts[1]);
						}else if(len==3){
							ftpClient.dir(cmdParts[1], cmdParts[2]);
						}else{
							FTPClientHelp.dirUse();
						}
					}else{
						this.notConnected();
					}
				}
				else{
					this.wrongCmd();
				}
			}
			//GET
			else if(cmd.startsWith("g")){
				if(this.MatchesCmd(cmd, "g")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "get")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.get(cmdParts[1],cmdParts[1]);
						}else if(len==3){
							ftpClient.get(cmdParts[1],cmdParts[2]);
						}else{
							FTPClientHelp.getUse();
						}
					}else{
						this.notConnected();
					}
				}else{
					this.wrongCmd();
				}
			}
			//HELP
			else if(cmd.startsWith("h")){
				if(this.MatchesCmd(cmd, "h")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "he|hel|help")){
					if(len==1){
						FTPClientHelp.helpList();
					}else{
						String helpCmds[]=new String[len-1];
						System.arraycopy(cmdParts,1,helpCmds,0,helpCmds.length);
						FTPClientHelp.help(helpCmds);
					}
				}else{
					this.wrongCmd();
				}
			}
			//LCD LS
			else if(cmd.startsWith("l")){
				if(this.MatchesCmd(cmd, "l")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "lc|lcd")){
					if(len==1){
						ftpClient.lcd();
					}else if(len==2){
						ftpClient.lcd(cmdParts[1]);
					}else{
						FTPClientHelp.lcdUse();
					}
				}else if(this.MatchesCmd(cmd, "ls")){
					if(ftpClient.isConnected()){
						if(len==1){
							ftpClient.ls();
						}else if(len==2){
							ftpClient.ls(cmdParts[1]);
						}else if(len==3){
							ftpClient.ls(cmdParts[1],cmdParts[2]);
						}else{
							FTPClientHelp.lsUse();
						}
					}else{
						this.notConnected();
					}
				}else{
					this.wrongCmd();
				}
			}
			//MKDIR
			else if(cmd.startsWith("m")){
				if(this.MatchesCmd(cmd, "m|md|mo|mod")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "mdelete|mde|mdel|mdele|mdelet")){
					if(ftpClient.isConnected()){
						String remoteFiles[];
						if(len>=2){
							remoteFiles=new String[len-1];
							System.arraycopy(cmdParts,1,remoteFiles,0,len-1);
							ftpClient.mdelete(remoteFiles);
						}else{
							FTPClientHelp.mdeleteUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "mdi|mdir")){
					if(ftpClient.isConnected()){
						String mdirParams[]=null; 
						if(len>=3){
							mdirParams=new String[cmdParts.length-2];
							System.arraycopy(cmdParts,1,mdirParams,0,cmdParts.length-2);
							ftpClient.mdir(mdirParams,cmdParts[len-1]);
						}else{
							FTPClientHelp.mdirUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "mget|mg|mge")){
					if(ftpClient.isConnected()){
						String [] remoteFiles;
						if(len>=2){
							remoteFiles=new String[len-1];
							System.arraycopy(cmdParts,1,remoteFiles,0,len-1);
							ftpClient.mget(remoteFiles);
						}else{
							FTPClientHelp.mgetUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "mls|ml")){
					if(ftpClient.isConnected()){
						String mlsParams[]=null;
						if(len>=3){
							mlsParams=new String[cmdParts.length-2];
							System.arraycopy(cmdParts,1,mlsParams,0,cmdParts.length-2);
							ftpClient.mls(mlsParams,cmdParts[len-1]); 
						}
					}else{
						this.notConnected();
					}
				}
				else if(this.MatchesCmd(cmd, "mput|mp|mpu")){
					if(ftpClient.isConnected()){
						String localFiles[];
						if(len>=2){
							localFiles=new String[len-1];
							System.arraycopy(cmdParts, 1, localFiles, 0, len-1);
							ftpClient.mput(localFiles);
						}else{
							FTPClientHelp.mputUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "mk|mkd|mkdi|mkdir")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.mkdir(cmdParts[1]);
						}else{
							FTPClientHelp.mkdirUse();
						}
					}else{
						this.notConnected();
					}
				}else{
					this.wrongCmd();
				}
			}
			//OPEN
			else if(cmd.startsWith("o")){
				if(this.MatchesCmd(cmd, "o|op|ope|open")){
					String remoteHost=null;
					if(!ftpClient.isConnected()){
						if(len==3){//提供了主机名和端口
							remoteHost=cmdParts[1];
							int port=Integer.parseInt(cmdParts[2]);
							ftpClient.open(remoteHost,port);
						}else if(len==2){//仅提供了主机名
							remoteHost=cmdParts[1];
							ftpClient.open(cmdParts[1],21);
						}else{
							FTPClientHelp.openUse();
						}
					}else{//如果网络连接不为空
						outputArea.append("已经连接到了 "+ftpClient.getRemoteHostName()+"，请首先使用断开连接。\r\n");
					}
				}else{
					this.wrongCmd();
				}
			}
			//PWD
			else if(cmd.startsWith("p")){
				if(this.MatchesCmd(cmd, "p")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "pw|pwd")){
					if(ftpClient.isConnected()){
						ftpClient.pwd();
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "pu|put")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.put(cmdParts[1],cmdParts[1]);
						}else if(len==3){
							ftpClient.put(cmdParts[1],cmdParts[2]);
						}else{
							FTPClientHelp.putUse();
						}
					}else{
						this.notConnected();
					}
				}
				else{
					this.wrongCmd();
				}
			}
			//REMOTEHELP RENAME RMDIR
			else if(cmd.startsWith("r")){
				if(this.MatchesCmd(cmd, "r|re|res")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "rec|recv")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.recv(cmdParts[1],cmdParts[1]);
						}else if(len==3){
							ftpClient.recv(cmdParts[1],cmdParts[2]);
						}else{
							FTPClientHelp.recvUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "rem|remo|remot|remote|remoteh|remotehe|remotehel|remotehelp")){
					if(ftpClient.isConnected()){
						if(len==1){
							ftpClient.remotehelp();
						}else if(len==2){
							ftpClient.remotehelp(cmdParts[1]);
						}else{
							FTPClientHelp.remotehelpUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "ren|rena|renam|rename")){
					if(ftpClient.isConnected()){
						if(len==3){
							ftpClient.rename(cmdParts[1],cmdParts[2]);
						}else{
							FTPClientHelp.renameUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "rm|rmd|rmdi|rmdir")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.rmdir(cmdParts[1]);
						}else{
							FTPClientHelp.rmdirUse();
						}
					}else{
						this.notConnected();
					}
				}else{
					this.wrongCmd();
				}
			}
			//STATUS
			else if(cmd.startsWith("s")){
				if(this.MatchesCmd(cmd, "s|si|st")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "se|sen|send")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.send(cmdParts[1],cmdParts[1]);
						}else if(len==3){
							ftpClient.send(cmdParts[1],cmdParts[2]);
						}else{
							FTPClientHelp.sendUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "sta|stat|statu|status")){
					ftpClient.status();
				}else if(this.MatchesCmd(cmd, "siz|size")){
					if(ftpClient.isConnected()){
						if(len==2){
							ftpClient.size(cmdParts[1]);
						}else{
							FTPClientHelp.sizeUse();
						}
					}else{
						this.notConnected();
					}
				}else if(this.MatchesCmd(cmd, "sy|sys|syst|syste|system")){
					if(ftpClient.isConnected()){
						ftpClient.system();
					}else{
						this.notConnected();
					}
				}else{
					this.wrongCmd();
				}
			}
			//TYPE
			else if(cmd.startsWith("t")){
				if(this.MatchesCmd(cmd, "t")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "ty|typ|type")){
					if(ftpClient.isConnected()){
						if(len==1){
							ftpClient.type();
						}else if(len==2){
							if(cmdParts[1].equalsIgnoreCase("ASCII")){
								ftpClient.type("ASCII");
							}else if(cmdParts[1].equalsIgnoreCase("BINARY")){
								ftpClient.type("BINARY");
							}else{
								System.out.println(cmdParts[1]+" :未知模式");
							}
						}
					}else{
						this.notConnected();
					}
				}else{
					this.wrongCmd();
				}
			}
			else if(cmd.startsWith("u")){
				if(this.MatchesCmd(cmd, "u")){
					this.ambiCmd();
				}else if(this.MatchesCmd(cmd, "us|use|user")){
					if(ftpClient.isConnected()){
						if(len==3){
							ftpClient.user(cmdParts[1]);
							ftpClient.pass(cmdParts[2]);
						}else{
							FTPClientHelp.userUse();
						}
					}else{
						this.notConnected();
					}
				}
			}else if(cmd.equals("?")){
				if(len==1){
					FTPClientHelp.helpList();
				}else{
					String helpCmds[]=new String[len-1];
					System.arraycopy(cmdParts,1,helpCmds,0,helpCmds.length);
					FTPClientHelp.help(helpCmds);
				}
			}
			else if(cmdStr.isEmpty()){
				return;
			}
			else{
				this.wrongCmd();
			}
		} 
	}
}
