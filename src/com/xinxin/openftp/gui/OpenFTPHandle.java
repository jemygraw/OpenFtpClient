package com.xinxin.openftp.gui;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.*;
import com.xinxin.openftp.ftpclient.*;
import com.xinxin.openftp.ftpclient.FTPClientImpl.TRANSFERTYPE;
import com.xinxin.openftp.table.LabelEditor;
import com.xinxin.openftp.table.LabelRenderer;
import com.xinxin.openftp.table.MyTableModel;
public class OpenFTPHandle implements ActionListener{
	private OpenFTPClient client;
	private File files[];
	private Vector<String> filesVector;
	public OpenFTPHandle(OpenFTPClient client){
		this.client=client;
		filesVector=new Vector<String>();
	}
	public void initFTPClient(FTPClientImpl ftpClient){
		ftpClient.setTransferType(client.getConfig().getTransferType());
		ftpClient.setDataTransferType(client.getConfig().getDataType());
		ftpClient.setLocalWorkDir(client.getConfig().getLocalWorkDir());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="addNewTab" || e.getActionCommand()=="addTabToolButton"){
			this.addNewTab();
		}else if(e.getActionCommand()=="connect" || e.getActionCommand()=="connectButton" || e.getActionCommand()=="connButton"){
			new Thread(new FTPConnect()).start();
		}else if(e.getActionCommand()=="disconnect" || e.getActionCommand()=="closeButton"){
			new Thread(new FTPClose()).start();
		}else if(e.getActionCommand()=="remoteRefresh"){
			new Thread(new FTPDirRefresh()).start();
		}else if(e.getActionCommand()=="lastDir"){
			new Thread(new FTPDirBack()).start();
		}
		else if(e.getActionCommand()=="close"){
			//保存队列
			System.exit(0);
		}
		//EditMenu
		else if(e.getActionCommand()=="copyFile" || e.getActionCommand()=="copyFileToolButton"){
			this.copyFiles(e);

		}else if(e.getActionCommand()=="pasteFile" || e.getActionCommand()=="pasteFileToolButton"){
			new Thread(new CopyFiles()).start();
		}else if(e.getActionCommand()=="searchFile"){
			new SearchFile();
		}else if(e.getActionCommand()=="selectAll"){
			this.selectAllFiles();
		}else if(e.getActionCommand()=="reverseSelect"){
			this.reverseSelectFiles();
		}
		//ViewMenu
		else if(e.getActionCommand()=="localWorkSpace"){
			this.setLocalWorkSpaceVisible(e);
		}else if(e.getActionCommand()=="remoteWorkSpace"){
			this.setRemoteWorkSpaceVisible(e);
		}else if(e.getActionCommand()=="commonToolBar" || e.getActionCommand()=="loginToolBar"){
			this.setToolBarVisible(e);
		}else if(e.getActionCommand()=="dirsOnly" || e.getActionCommand()=="filesOnly" || e.getActionCommand()=="filesAndDirs"){
			this.setFileSelectionMode(e);
		}else if(e.getActionCommand()=="showHiddenFiles"){
			this.showHiddenFiles(e);
		}else if(e.getActionCommand()=="refreshLocalFiles"){
			this.refreshLocalFiles();
		}
		//CmdMenu
		else if(e.getActionCommand()=="openShell" || e.getActionCommand()=="openShellToolButton"){
			new Thread(new OpenShell()).start();
		}else if(e.getActionCommand()=="getFile"){
			new Thread(new GetFile()).start();
		}else if(e.getActionCommand()=="putFile"){
			new Thread(new UploadFile()).start();
		}else if(e.getActionCommand()=="renameFile"){
			String oldName=JOptionPane.showInputDialog("请输入远程文件名称:");
			String newName=JOptionPane.showInputDialog("请输入重命名后的远程文件名称:");
			new Thread(new RenameFileDir(oldName,newName)).start();
		}else if(e.getActionCommand()=="deleteFile"){
			String fileName=JOptionPane.showInputDialog("请输入要删除的远程文件名称:");
			new Thread(new RemoveRemoteFile(fileName)).start();
		}else if(e.getActionCommand()=="renameDir"){
			String oldName=JOptionPane.showInputDialog("请输入远程目录名称:");
			String newName=JOptionPane.showInputDialog("请输入重命名后的远程目录名称:");
			new Thread(new RenameFileDir(oldName,newName)).start();
		}else if(e.getActionCommand()=="makeDir"){
			String dirName=JOptionPane.showInputDialog("请输入新目录名称:");
			new Thread(new MakeDir(dirName)).start();
		}else if(e.getActionCommand()=="removeDir"){
			String dirName=JOptionPane.showInputDialog("请输入远程目录名称:");
			new Thread(new RemoveRemoteDir(dirName)).start();
		}else if(e.getActionCommand()=="mputFile"){
			new Thread(new MultiUploadFiles()).start();
		}else if(e.getActionCommand()=="mgetFile"){
			new Thread(new MultiGetFiles()).start();
		}else if(e.getActionCommand()=="mdeleteFile"){
			new Thread(new MultiDeleteFiles()).start();
		}
		//ToolMenu
		else if(e.getActionCommand()=="asciiDataType"){
			this.setAsciiType(this.getCurrentFTPClient());
		}else if(e.getActionCommand()=="binaryDataType"){
			this.setBinaryType(this.getCurrentFTPClient());
		}else if(e.getActionCommand()=="pasvType"){
			this.setPasvType(this.getCurrentFTPClient());
		}else if(e.getActionCommand()=="portType"){
			this.setPortType(this.getCurrentFTPClient());
		}
		else if(e.getActionCommand()=="metal"){
			if(client.getTool().getMetal().isSelected()){
				Common.setSystemLookAndFeel(client, Common.java);
				Common.setSystemLookAndFeel(client.getEdit().getConfigMenuItem(),Common.java);
			}
		}else if(e.getActionCommand()=="liquid"){
			if(client.getTool().getLiquid().isSelected()){
				Common.setSystemLookAndFeel(client, Common.liquid);
				Common.setSystemLookAndFeel(client.getEdit().getConfigMenuItem(),Common.liquid);
			}
		}else if(e.getActionCommand()=="motif"){
			if(client.getTool().getMotif().isSelected()){
				Common.setSystemLookAndFeel(client, Common.solaris);
				Common.setSystemLookAndFeel(client.getEdit().getConfigMenuItem(),Common.solaris);
			}
		}else if(e.getActionCommand()=="windows"){
			if(client.getTool().getWindows().isSelected()){
				Common.setSystemLookAndFeel(client, Common.windows);
				Common.setSystemLookAndFeel(client.getEdit().getConfigMenuItem(),Common.windows);
			}
		}else if(e.getActionCommand()=="apple"){
			if(client.getTool().getApple().isSelected()){
				Common.setSystemLookAndFeel(client, Common.mac);
				Common.setSystemLookAndFeel(client.getEdit().getConfigMenuItem(),Common.mac);
			}
		}else if(e.getActionCommand()=="showClientInfo" || e.getActionCommand()=="showClientInfoToolButton"){
			if(!this.getCurrentFTPClient().isConnected()){
				JOptionPane.showMessageDialog(null, "当前没有活动的客户端连接!","提示",JOptionPane.INFORMATION_MESSAGE);
			}else{
				new ShowClientInfo(this.getCurrentFTPClient());
			}
		}else if(e.getActionCommand()=="showServerInfo" || e.getActionCommand()=="showServerInfoToolButton"){
			if(!this.getCurrentFTPClient().isConnected()){
				JOptionPane.showMessageDialog(null, "未连接任何服务器!","提示",JOptionPane.INFORMATION_MESSAGE);
			}else if(!this.getCurrentFTPClient().isLoggedIn()){
				JOptionPane.showMessageDialog(null, "用户未登陆,不能查看服务器信息。请登录!","提示",JOptionPane.INFORMATION_MESSAGE);
			}else{
				new ShowServerInfo(this.getCurrentFTPClient());
			}
		}
		//CommonToolBar
		else if(e.getActionCommand()=="helpToolButton" || e.getActionCommand()=="helpToolButton"){
			String  helpfileurl="help.chm";   
			 try {
				Runtime.getRuntime().exec("hh"+" "+helpfileurl);
			}catch (IOException re) {
				return;
			}   
		}else if(e.getActionCommand()=="lcdToolButton"){
			this.setLocalWorkDir(this.getCurrentFTPClient());
		}
	}
//	//-------------------------------------方法实现-----------------------------------
	public void setLocalWorkDir(FTPClientImpl ftp){
		ftp.lcd(client.getLocalHandle().getFileChooser().getCurrentDirectory().getAbsolutePath());
	}
	public void setAsciiType(FTPClientImpl ftp){
		ftp.ascii();
	}
	public void setBinaryType(FTPClientImpl ftp){
		ftp.binary();
	}
	public void setPasvType(FTPClientImpl ftp){
		if(client.getTool().getPasvType().isSelected()){
			ftp.setTransferType(TRANSFERTYPE.PASV);
		}
	}
	public void setPortType(FTPClientImpl ftp){
		if(client.getTool().getPortType().isSelected()){
			ftp.setTransferType(TRANSFERTYPE.PORT);
		}
	}
	private void addNewTab(){
		RemoteWorkSpaceTab newTab=new RemoteWorkSpaceTab();
		client.getRemoteDirTabbedPane().addTab("远程地址",newTab);
		client.getTabbedPanelVector().add(newTab);
		newTab.getConnButton().addActionListener(this);
		newTab.getCloseButton().addActionListener(this);
		newTab.getRemoteLastDir().addActionListener(this);
		newTab.getRemoteRefresh().addActionListener(this);
		client.getRemoteDirTabbedPane().setSelectedIndex(client.getRemoteDirTabbedPane().getComponentCount()-1);
	}
	private FTPClientImpl getCurrentFTPClient(){
		int tabIndex=client.getRemoteDirTabbedPane().getSelectedIndex();
		RemoteWorkSpaceTab tabPanel=client.getTabbedPanelVector().get(tabIndex);
		return tabPanel.getFtpClient();
	}
	class FTPConnect implements Runnable{
		@Override
		public void run() {
			openFTPConnection(getCurrentFTPClient());
		}
	}
	class FTPClose implements Runnable{
		@Override
		public void run() {
			FTPClientImpl ftp=getCurrentFTPClient();
			closeFTPConnection(ftp);
			client.getRemoteDirTabbedPane().setTitleAt(client.getRemoteDirTabbedPane().getSelectedIndex(),"远程主机");
			client.getTabbedPanelVector().get(client.getRemoteDirTabbedPane().getSelectedIndex()).getRemoteDirSelect().setSelectedIndex(0);
			updateRemoteDirTable(ftp,"",ftp.getTabIndex());
		}
	}
	class FTPDir implements Runnable{
		private String dirName;
		public FTPDir(String dirName){
			this.dirName=dirName;
		}
		@Override
		public void run() {
			changeFTPDir(getCurrentFTPClient(),dirName);
		}
	}
	class FTPDirRefresh implements Runnable{
		@Override
		public void run() {
			refreshFTPDir(getCurrentFTPClient());
		}
	}
	class FTPDirBack implements Runnable{
		@Override
		public void run() {
			backFTPDir(getCurrentFTPClient());
		}
	}
	class MakeDir implements Runnable{
		private String dirName;
		public MakeDir(String dirName){
			this.dirName=dirName;
		}
		@Override
		public void run() {
			makeDir(getCurrentFTPClient(),dirName);
		}
	}
	class RenameFileDir implements Runnable{
		private String oldName;
		private String newName;
		public RenameFileDir(String oldName,String newName){
			this.oldName=oldName;
			this.newName=newName;
		}
		@Override
		public void run() {
			renameFileDir(getCurrentFTPClient(),oldName,newName);
		}
	}
	class RemoveRemoteDir implements Runnable{
		private String dirName;
		public RemoveRemoteDir(String dirName){
			this.dirName=dirName;
		}
		@Override
		public void run() {
			removeRemoteDir(getCurrentFTPClient(),dirName);
		}

	}
	class RemoveRemoteFile implements Runnable{
		private String fileName;
		public RemoveRemoteFile(String fileName){
			this.fileName=fileName;
		}
		@Override
		public void run(){
			deleteRemoteFile(getCurrentFTPClient(),fileName);
		}
	}
	class UploadFile implements Runnable{
		@Override
		public void run() {
			uploadFile(getCurrentFTPClient());
		}	
	}
	class MultiUploadFiles implements Runnable{
		@Override
		public void run() {
			multiUploadFiles(getCurrentFTPClient());
		}
	}
	class MultiGetFiles implements Runnable{
		@Override
		public void run() {
			multiGetFiles(getCurrentFTPClient());
		}
	}
	class MultiDeleteFiles implements Runnable{
		@Override
		public void run() {
			 multiDeleteFiles(getCurrentFTPClient());
		}
		
	}
	class GetFile implements Runnable{
		@Override
		public void run() {
			getFile(getCurrentFTPClient());
		}
	}
	class ShowServerInfo extends Thread{
		private FTPClientImpl ftp;
		public void run(){
			ftp.system();
			ftp.remotehelp();
		}
		public ShowServerInfo(FTPClientImpl ftp){
			this.ftp=ftp;
			this.start();
		}
	}
	class ShowClientInfo{
		private static final long serialVersionUID = 8345288244163506023L;
		private JDialog d;
		private JTextArea infoTextArea;
		private JButton button;
		private FTPClientImpl ftp;
		public void loadClientInfo(){
			infoTextArea.append("传输模式:"+ftp.getTransferType().toString()+"\r\n");
			infoTextArea.append("数据模式:"+ftp.getDataTransferType().toString()+"\r\n");
			infoTextArea.append("本地目录:"+ftp.getLocalWorkDir()+"\r\n");
		}
		public void init(){
			d=new JDialog();
			d.setTitle("客户端信息");
			infoTextArea=new JTextArea();
			infoTextArea.setEditable(false);
			button=new JButton("确定(O)");
			button.setMnemonic('O');
			button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					d.dispose();
				}

			});
			JScrollPane s=new JScrollPane(infoTextArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			JPanel p=new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
			p.add(button);
			d.getContentPane().add(s,java.awt.BorderLayout.CENTER);
			d.getContentPane().add(p,java.awt.BorderLayout.SOUTH);
			d.setModal(true);
			d.setSize(400, 400);
			Common.getPos(400, 400);
			d.setLocation(Common.posX,Common.posY);
			d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.loadClientInfo();
			d.setVisible(true);
		}
		public ShowClientInfo(FTPClientImpl ftp){
			this.ftp=ftp;
			this.init();
		}
	}
	private void refreshFTPDir(FTPClientImpl ftp){
		if(ftp.isConnected()){
			ftp.dir();	
			this.updateRemoteDirTable(ftp,ftp.getCmdResultInfo(),ftp.getTabIndex());
		}
	}
	private void backFTPDir(FTPClientImpl ftp){
		if(ftp.isConnected()){
			ftp.cdup();
			ftp.pwd();
			JComboBox b=client.getTabbedPanelVector().get(ftp.getTabIndex()).getRemoteDirSelect();
			b.addItem(ftp.getCmdReplyInfo());
			b.setSelectedIndex(b.getItemCount()-1);
			this.refreshFTPDir(ftp);
		}
	}
	private void makeDir(FTPClientImpl ftp,String dirName){
		if(ftp.isConnected()){
			if(dirName==null || dirName.trim().equals("")){
				return;
			}
			ftp.mkdir(dirName);
			this.refreshFTPDir(ftp);
		}
	}
	private void renameFileDir(FTPClientImpl ftp,String oldName,String newName){
		if(ftp.isConnected()){
			if(oldName==null || oldName.trim().equals("")){
				return;
			}
			ftp.rename(oldName, newName);
			this.refreshFTPDir(ftp);
		}
	}
	private void removeRemoteDir(FTPClientImpl ftp,String dirName){
		if(ftp.isConnected()){
			if(dirName==null || dirName.trim().equals("")){
				return;
			}
			ftp.rmdir(dirName);
			this.refreshFTPDir(ftp);
		}
	}
	private void deleteRemoteFile(FTPClientImpl ftp,String fileName){
		if(ftp.isConnected()){
			if(fileName==null || fileName.trim().equals("")){
				return;
			}
			ftp.delete(fileName);
			this.refreshFTPDir(ftp);
		}
	}
	private void changeFTPDir(FTPClientImpl ftp,String dirName){
		if(ftp.isConnected()){
			ftp.cd(dirName);
			ftp.pwd();
			JComboBox b=client.getTabbedPanelVector().get(ftp.getTabIndex()).getRemoteDirSelect();
			b.addItem(ftp.getCmdReplyInfo());
			b.setSelectedIndex(b.getItemCount()-1);
			this.refreshFTPDir(ftp);
		}
	}
	private void uploadFile(FTPClientImpl ftp){
		JFileChooser fc=new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.showOpenDialog(null);
		fc.addActionListener(new PutFileChooser(fc));
		fc.approveSelection();
	}
	private void getFile(FTPClientImpl ftp){
		if(ftp.isConnected()){
			if(ftp.getRemoteFile()!=null){
				ftp.get(ftp.getRemoteFile(), ftp.getRemoteFile());
			}
			ftp.getDirTable().clearSelection();
		}
	}
	
	class PutFileChooser implements ActionListener{
		private JFileChooser fc;
		public PutFileChooser(JFileChooser fc){
			this.fc=fc;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			File f=fc.getSelectedFile();
			FTPClientImpl ftp=getCurrentFTPClient();
			ftp.put(f.getAbsolutePath(),f.getName());
			refreshFTPDir(ftp);
		}
	}
	private void multiUploadFiles(FTPClientImpl ftp){
		JFileChooser fc=new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		fc.showOpenDialog(null);
		fc.addActionListener(new MultiPutFileChooser(fc));
		fc.approveSelection();
	}
	class MultiPutFileChooser implements ActionListener{
		private JFileChooser fc;
		public MultiPutFileChooser(JFileChooser fc){
			this.fc=fc;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			File f[]=fc.getSelectedFiles();
			FTPClientImpl ftp=getCurrentFTPClient();
			for(File file:f){
				ftp.put(file.getAbsolutePath(), file.getName());
			}
			refreshFTPDir(ftp);
		}
	}
	private void multiGetFiles(FTPClientImpl ftp){
		if(ftp.isConnected()){
			if(filesVector.size()!=0){
				for(int i=0;i<filesVector.size();i++){
					ftp.get(filesVector.get(i).toString(), filesVector.get(i).toString());
				}
				ftp.getDirTable().clearSelection();
				filesVector.clear();
			}
		}
	}
	private void multiDeleteFiles(FTPClientImpl ftp){
		if(ftp.isConnected()){
			if(filesVector.size()!=0){
				for(int i=0;i<filesVector.size();i++){
					ftp.delete(filesVector.get(i).toString());
				}
				ftp.getDirTable().clearSelection();
				this.refreshFTPDir(ftp);
				filesVector.clear();
			}
		}
	}
	private void openFTPConnection(FTPClientImpl ftp){
		//连接服务器
		int tabIndex=client.getRemoteDirTabbedPane().getSelectedIndex();
		ftp.setTabIndex(tabIndex);
		ftp.setDirTable(client.getTabbedPanelVector().get(tabIndex).getRemoteFileTable());
		ftp.setDirTableMouseListener(new SelectRowEvent(ftp));
		RemoteWorkSpaceTab tabPanel=client.getTabbedPanelVector().get(tabIndex);
		this.initFTPClient(ftp);//初始化客户端的设置，这是必须的步骤
		ftp.setShowInfoTextArea(tabPanel.getReturnInfoArea());
		String name=client.getLoginInfo().getName().getText();
		String pass=new String(client.getLoginInfo().getPass().getPassword());
		String address=(String)client.getLoginInfo().getAddressList().getSelectedItem();
		String portAddr=client.getLoginInfo().getPort().getText();
		if(address==null || portAddr==""){
			return;
		}
		if(client.getLoginInfo().getAnony().isSelected()){
			name="ftp";
			pass="ftp";
		}
		if(ftp.isConnected()){
			this.closeFTPConnection(ftp);
		}
		if(ftp.open(address, Integer.parseInt(portAddr))){
			client.getRemoteDirTabbedPane().setTitleAt(tabIndex,address);
			if(ftp.user(name)){
				if(ftp.pass(pass)){
					ftp.pwd();
					JComboBox b=client.getTabbedPanelVector().get(tabIndex).getRemoteDirSelect();
					b.addItem(ftp.getCmdReplyInfo());
					b.setSelectedIndex(b.getItemCount()-1);
					ftp.dir();
					this.updateRemoteDirTable(ftp,ftp.getCmdResultInfo(),tabIndex);
				}
			}
		}
	}
	private void closeFTPConnection(FTPClientImpl ftp){
		if(ftp.isConnected()){
			ftp.close();
		}
	}
	//选择全部文件
	private void selectAllFiles(){
		File strCur=client.getLocalHandle().getFileChooser().getCurrentDirectory();
		File files[]=null;
		if(strCur.isDirectory()){
			files=strCur.listFiles();
		}
		client.getLocalHandle().getFileChooser().setSelectedFiles(files);
	}
	//反向选择
	private void reverseSelectFiles(){
		File files[]=client.getLocalHandle().getFileChooser().getCurrentDirectory().listFiles();
		Vector <File> revFiles=new Vector<File>();
		for(File f:files){
			if(!isSelected(f)){
				revFiles.add(f);
			}
		}
		File rev[]=new File[revFiles.size()];
		revFiles.copyInto(rev);
		client.getLocalHandle().getFileChooser().setSelectedFiles(rev);
	}	
	private boolean isSelected(File f){
		File files[]=client.getLocalHandle().getFileChooser().getSelectedFiles();
		for(File file:files){
			if(f.getName().equals(file.getName())){
				return true;
			}
		}
		return false;
	}
	//复制文件
	private void copyFiles(ActionEvent e){
		files=client.getLocalHandle().getFileChooser().getSelectedFiles();
	}
	//粘贴文件
	private void pasteFiles(){
		File curDir=client.getLocalHandle().getFileChooser().getCurrentDirectory();
		BufferedInputStream in=null;
		BufferedOutputStream out=null;
		if(curDir!=null && curDir.canWrite() && files!=null){
			for(File file:files){
				try {
					in=new BufferedInputStream(new FileInputStream(file));
					out=new BufferedOutputStream(new FileOutputStream(new File(curDir,file.getName())));
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null,"粘贴文件"+file.getName()+"失败!","出错信息",JOptionPane.ERROR_MESSAGE);
					System.out.println("粘贴失败!");
					return;
				}
				int b=0;
				try {
					while((b=in.read())!=-1){
						out.write(b);
					}
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,"粘贴文件"+file.getName()+"失败!","出错信息",JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					out.flush();
					in.close();
					out.close();
				} catch (IOException e1) {
					return;
				}
			}
		}
		this.refreshLocalFiles();
	}
	private void setLocalWorkSpaceVisible(ActionEvent e){
		if(((JCheckBoxMenuItem)e.getSource()).isSelected()){
			client.setLocalWorkSpace(true);
		}else{
			client.setLocalWorkSpace(false);
		}
	}
	private void setRemoteWorkSpaceVisible(ActionEvent e){
		if(((JCheckBoxMenuItem)e.getSource()).isSelected()){
			client.setRemoteWorkSpace(true);
		}else{
			client.setRemoteWorkSpace(false);
		}
	}
	private void setToolBarVisible(ActionEvent e){
		if(e.getActionCommand()=="commonToolBar"){
			if(((JCheckBoxMenuItem)e.getSource()).isSelected()){
				client.setCommonToolBar(true);
				if(client.getView().getLoginToolBar().isSelected()){
					client.getToolBarPanel().setLayout(new GridLayout(2,1));
				}
			}else{
				client.setCommonToolBar(false);
				client.getToolBarPanel().setLayout(new FlowLayout(FlowLayout.LEFT));
			}
		}else{
			if(((JCheckBoxMenuItem)e.getSource()).isSelected()){
				client.setLoginToolBar(true);
				if(client.getView().getCommonToolBar().isSelected()){
					client.getToolBarPanel().setLayout(new GridLayout(2,1));
				}
			}else{
				client.setLoginToolBar(false);
				client.getToolBarPanel().setLayout(new FlowLayout(FlowLayout.LEFT));
			}
		}
	}
	private void setFileSelectionMode(ActionEvent e){
		if(((JRadioButtonMenuItem)e.getSource()).isSelected()){
			if(e.getActionCommand()=="dirsOnly"){
				client.getLocalHandle().getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			}else if(e.getActionCommand()=="filesOnly"){
				client.getLocalHandle().getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
			}else{
				client.getLocalHandle().getFileChooser().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			}
		}
	}
	private void showHiddenFiles(ActionEvent e){
		if(((JCheckBoxMenuItem)e.getSource()).isSelected()){
			client.getLocalHandle().getFileChooser().setFileHidingEnabled(false);
		}else{
			client.getLocalHandle().getFileChooser().setFileHidingEnabled(true);
		}
	}
	private void refreshLocalFiles(){
		client.getLocalHandle().getFileChooser().rescanCurrentDirectory();
	}
	class CopyFiles implements Runnable{
		@Override
		public void run() {
			pasteFiles();
		}
	}
	class SearchFile extends JDialog implements ActionListener{
		private static final long serialVersionUID = -5831566887780943200L;
		private JLabel label;
		private JTextField textField;
		private JButton search;
		private JButton exit;
		public JPanel panel;
		public void initPanel(){
			panel=new JPanel(new FlowLayout(FlowLayout.LEFT));
			label=new JLabel("文件名:");
			textField=new JTextField(20);
			search=new JButton("查找(F)");
			search.setMnemonic('F');
			search.setDefaultCapable(true);
			search.addActionListener(this);
			exit=new JButton("结束(E)");
			exit.setMnemonic('E');
			exit.addActionListener(this);
			panel.add(label);
			panel.add(textField);
			panel.add(search);
			panel.add(exit);
		}
		public SearchFile(){
			this.initPanel();
			this.getContentPane().add(panel);
			this.setAlwaysOnTop(true);
			this.setTitle("查找文件");
			this.setIconImage(new ImageIcon("img/find.png").getImage());
			this.setSize(450,70);
			Common.getPos(450,70);
			this.setLocation(Common.posX,Common.posY);
			this.setVisible(true);
			this.setResizable(false);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==exit){
				this.dispose();
			}else if(e.getSource()==search){
				String fileName=textField.getText().trim();
				JFileChooser fc=client.getLocalHandle().getFileChooser();
				File f=new File(fc.getCurrentDirectory(),fileName);
				if(f.isFile()){
					fc.setSelectedFile(f);
					this.dispose();
				}else{
					this.dispose();
					JOptionPane.showMessageDialog(null, "不存在文件"+fileName+"!","查找提示",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
	class OpenShell implements Runnable{
		@Override
		public void run() {
			openShell();
		}
	}
	private void openShell(){
		new OpenFTPShell();
	}

	//每次执行dir命令之后都要这样
	public void updateRemoteDirTable(FTPClientImpl ftpClient,String dirStr,int tabIndex){
		JTable table=ftpClient.getDirTable();
		MyTableModel md = new MyTableModel();
		table.setModel(md);
		md.addColumn("");
		md.addColumn("名称");md.addColumn("修改日期");
		md.addColumn("类型");md.addColumn("权限");
		if(dirStr==""){
			return;
		}
		String [] dirInfoParts=dirStr.split("\r\n");
		Set<Info> info= new HashSet<Info>();
		//解析每一个字符序列
		//drw-rw-rw-   1 user     group           0 Feb 25 19:27 d
		for(String s:dirInfoParts){
			String eachRow[]=s.trim().split(" +");
			String priv=eachRow[0];
			String date=eachRow[5]+" "+eachRow[6]+"  "+eachRow[7];
			String name="";
			JLabel iconLabel=null;
			for(int i=8;i<eachRow.length;i++){
				name+=" "+eachRow[i];   
			}
			String type="";
			if(priv.startsWith("d")){
				type="文件夹";
				iconLabel=new JLabel(new ImageIcon("img/folder.png"));
			}else if(priv.startsWith("-")){
				type="文件";
				iconLabel=new JLabel(new ImageIcon("img/file.png"));
			}
			info.add(new Info(iconLabel,name,date,type,priv));
		}
		//将信息添加到表格中
		Info infoArray[]=new Info[info.size()];
		info.toArray(infoArray);

		table.getColumn("").setCellRenderer(new LabelRenderer());
		table.getColumn("").setCellEditor(new LabelEditor(new JTextField()));
		for(int i=0;i<info.size();i++){
			md.addRow(new Object[]{infoArray[i].getIconLabel(),infoArray[i].getName(),
					infoArray[i].getDate(),infoArray[i].getType(),infoArray[i].getPriv()});
		}
		client.getRemoteWorkSpace().repaint();
	}
	class SelectRowEvent extends MouseAdapter{
		private FTPClientImpl ftp;
		public SelectRowEvent(FTPClientImpl ftp){
			this.ftp=ftp;
			this.ftp.setRemoteFiles(filesVector);
		}
		public void mouseClicked(MouseEvent me){
			if(me.isControlDown() && me.getClickCount()==1){
				int row=ftp.getDirTable().rowAtPoint(me.getPoint());
				int column=1;
				Object value=ftp.getDirTable().getValueAt(row,column);   
				ftp.getRemoteFiles().add(((String)value).trim());
			}else if(me.getClickCount()==1){
				int row=ftp.getDirTable().rowAtPoint(me.getPoint());   
				int column=1;   
				Object value=ftp.getDirTable().getValueAt(row,column);   
				ftp.setRemoteFile(((String)value).trim());
			}
			if(me.getClickCount()==2){
				int row=ftp.getDirTable().rowAtPoint(me.getPoint());   
				int column=1;   
				Object value=ftp.getDirTable().getValueAt(row,column);   
				String dirName=((String)value).trim();
				new Thread(new FTPDir(dirName)).start();
			}
		}
	}
	class Info{
		private JLabel iconLabel;
		private String name;
		private String date;
		private String type;
		private String priv;
		public Info(JLabel iconLabel,String name,String date,String type,String priv){
			this.iconLabel=iconLabel;
			this.name=name;
			this.date=date;
			this.type=type;
			this.priv=priv;
		}

		public JLabel getIconLabel() {
			return iconLabel;
		}
		public String getName() {
			return name;
		}
		public String getDate() {
			return date;
		}
		public String getType() {
			return type;
		}
		public String getPriv() {
			return priv;
		}

	}
} 
