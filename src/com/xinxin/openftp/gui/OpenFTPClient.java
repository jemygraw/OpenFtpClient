package com.xinxin.openftp.gui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.Vector;

import javax.swing.*;

import com.xinxin.openftp.config.FTPClientConfig;
import com.xinxin.openftp.ftpclient.FTPClientImpl.DATATYPE;
import com.xinxin.openftp.ftpclient.FTPClientImpl.TRANSFERTYPE;

/**
 * 				OpenFTP User Interface Demo
 * ______________________________________________________
 * |OpenFtp2.0 ---Jinxinxin								|
 * |----------------------------------------------------|
 * |File-Edit-View-Cmd-Site-Tool-Dir-Help				|
 * |----------------------------------------------------|
 * |Common ToolBar										|
 * |----------------------------------------------------|
 * |Login Information ToolBar							|
 * |____________________________________________________|
 * |						|							|
 * |						|							|
 * |						|							|	
 * |						|							|
 * |	LocalWorkSpace	  <-|->		RemoteWorkSpace		|
 * |						|___________________________|
 * |						|		Operation Show		|
 * |________________________|___________________________|
 * |				Monitor WorkSpace					|
 * |____________________________________________________|
 * */
public class OpenFTPClient extends JFrame{
	private static final long serialVersionUID = -6977611708171334182L;
	//定义菜单组件
	private JMenu 	 fileMenu; // 文件
	private JMenu 	 editMenu; // 编辑
	private JMenu 	 viewMenu; // 查看
	private JMenu 	 cmdMenu; //  命令
	private JMenu 	 toolMenu; // 工具
	private JMenu 	 helpMenu; // 帮助
	private JToolBar commonToolBar;
	private JToolBar loginInfoToolBar;
	private JPanel toolBarPanel;
	private JPanel localWorkSpace;
	private JPanel remoteWorkSpace;
	private JPanel remoteDirPane;
	private JTabbedPane remoteDirTabbedPane;
	//定义菜单处理对象
	private FileMenu 		 file;
	private EditMenu 		 edit;
	private ViewMenu 		 view;
	private CmdMenu 		 cmd;
	private ToolMenu 		 tool;
	private HelpMenu 		 help;
	private CommonToolBar 	 common;
	private LoginInfoToolBar loginInfo;
	private LocalWorkSpace localHandle;
	//窗格对象
	private Container container;
	private OpenFTPHandle handle;
	private FTPClientConfig config;
	private Vector<RemoteWorkSpaceTab> tabbedPanelVector;
	public void initSystem(){
		this.container=this.getContentPane();
		//设置事件处理类
		this.config=new FTPClientConfig();
		this.config.loadConfigFile();
		this.config.loadConfigInfo();
		this.handle=new OpenFTPHandle(this);
		tabbedPanelVector=new Vector<RemoteWorkSpaceTab>();
		//初始化菜单
		file=new FileMenu();
		edit=new EditMenu();
		view=new ViewMenu();
		cmd=new CmdMenu();
		tool=new ToolMenu();
		help=new HelpMenu();
		common=new CommonToolBar();
		loginInfo=new LoginInfoToolBar();
		localHandle=new LocalWorkSpace();
	}
	public void initMenu(){
		fileMenu=file.initFileMenu();
		editMenu=edit.initEditMenu();
		viewMenu=view.initViewMenu();
		cmdMenu=cmd.initCmdMenu();
		toolMenu=tool.initToolMenu();
		helpMenu=help.initHelpMenu();
	}
	//添加菜单事件监听器
	public void addEventListener(){
		//FileMenu
		file.getNewTab().addActionListener(handle);
		file.getConnect().addActionListener(handle);
		file.getDisconnect().addActionListener(handle);
		file.getClose().addActionListener(handle);
		//EditMenu
		edit.getCopyFile().addActionListener(handle);
		edit.getPasteFile().addActionListener(handle);
		edit.getSearchFile().addActionListener(handle);
		edit.getSelectAll().addActionListener(handle);
		edit.getReverseSelect().addActionListener(handle);
		//ViewMenu
		view.getLocalWorkSpace().addActionListener(handle);
		view.getRemoteWorkSpace().addActionListener(handle);
		view.getCommonToolBar().addActionListener(handle);
		view.getLoginToolBar().addActionListener(handle);
		view.getDirsOnly().addActionListener(handle);
		view.getFilesOnly().addActionListener(handle);
		view.getFilesAndDirs().addActionListener(handle);
		view.getShowHiddenFiles().addActionListener(handle);
		view.getRefreshLocalFiles().addActionListener(handle);
		//CmdMenu
		cmd.getPutFile().addActionListener(handle);
		cmd.getGetFile().addActionListener(handle);
		cmd.getRenameFile().addActionListener(handle);
		cmd.getDeleteFile().addActionListener(handle);
		cmd.getRenameDir().addActionListener(handle);
		cmd.getMakeDir().addActionListener(handle);
		cmd.getRemoveDir().addActionListener(handle);
		cmd.getMputFile().addActionListener(handle);
		cmd.getMgetFile().addActionListener(handle);
		cmd.getMdeleteFile().addActionListener(handle);
		cmd.getOpenShell().addActionListener(handle);
		//SiteMenu
		//ToolMenu
		tool.getAsciiDataType().addActionListener(handle);
		tool.getBinaryDataType().addActionListener(handle);
		tool.getPasvType().addActionListener(handle);
		tool.getPortType().addActionListener(handle);
		tool.getMetal().addActionListener(handle);
		tool.getMotif().addActionListener(handle);
		tool.getApple().addActionListener(handle);
		tool.getLiquid().addActionListener(handle);
		tool.getWindows().addActionListener(handle);
		tool.getShowClientInfo().addActionListener(handle);
		tool.getShowServerInfo().addActionListener(handle);
		//CommonToolBar
		common.getAddTabToolButton().addActionListener(handle);
		common.getLcdToolButton().addActionListener(handle);
		common.getCopyFileToolButton().addActionListener(handle);
		common.getPasteFileToolButton().addActionListener(handle);
		common.getOpenShellToolButton().addActionListener(handle);
		common.getShowClientInfoToolButton().addActionListener(handle);
		common.getShowServerInfoToolButton().addActionListener(handle);
		common.getHelpToolButton().addActionListener(handle);
		//LoginToolBar
		loginInfo.getConnectButton().addActionListener(handle);

	}
	public void initToolBar(){
		commonToolBar=common.initCommonToolBar();
		loginInfoToolBar=loginInfo.initLoginInfoToolBar();
		commonToolBar.setVisible(false);
		loginInfoToolBar.setVisible(false);
		toolBarPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		toolBarPanel.add(commonToolBar);
		toolBarPanel.add(loginInfoToolBar);
	}
	public void initLocalWorkSpace(){
		this.localWorkSpace=localHandle.initLocalDirPane();
		this.localWorkSpace.setVisible(false);
	}
	public JPanel initRemoteDirPane() {
		remoteDirPane = new JPanel();
		remoteDirPane.setLayout(new BorderLayout());
		RemoteWorkSpaceTab tab = new RemoteWorkSpaceTab();
		remoteDirTabbedPane = new JTabbedPane();
		remoteDirTabbedPane.addTab("远程地址", tab);
		tabbedPanelVector.add(tab);
		tab.getConnButton().addActionListener(handle);
		tab.getCloseButton().addActionListener(handle);
		tab.getRemoteLastDir().addActionListener(handle);
		tab.getRemoteRefresh().addActionListener(handle);
		this.remoteDirTabbedPane.setName("remote");
		remoteDirPane.add(remoteDirTabbedPane, BorderLayout.CENTER);
		return remoteDirPane;
	}
	public void initRemoteWorkSpace(){
		this.remoteWorkSpace=this.initRemoteDirPane();
		this.remoteWorkSpace.setVisible(false);
	}
	 
	public void layoutComponents(){
		//设置菜单
		JMenuBar menuBar=new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		menuBar.add(cmdMenu);
		menuBar.add(toolMenu);
		menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);
		//设置本地和远程工作区域已经监视工作区
		JSplitPane hSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		hSplitPane.setOneTouchExpandable(true);
		hSplitPane.add(localWorkSpace);
		hSplitPane.add(remoteWorkSpace);
		// 窗口整体布局
		container.add(toolBarPanel,BorderLayout.NORTH);
		container.add(hSplitPane, BorderLayout.CENTER);
	}

	public void configSystem(){
		//设置传输模式
		if(config.getTransferType()==TRANSFERTYPE.PORT){
			edit.getConfigMenuItem().getTransType().setSelectedIndex(0);
			tool.getPortType().setSelected(true);
		}else if(config.getTransferType()==TRANSFERTYPE.PASV){
			edit.getConfigMenuItem().getTransType().setSelectedIndex(1);
			tool.getPasvType().setSelected(true);
		}
		//设置数据模式
		if(config.getDataType()==DATATYPE.ASCII){
			edit.getConfigMenuItem().getDataType().setSelectedIndex(0);
			tool.getAsciiDataType().setSelected(true);
		}else if(config.getDataType()==DATATYPE.BINARY){
			edit.getConfigMenuItem().getDataType().setSelectedIndex(1);
			tool.getBinaryDataType().setSelected(true);
		}
		//设置本地工作目录
		String local=config.getLocalWorkDir();
		if(local!=""){
			File f=new File(local);
			if(f.isDirectory()){
				edit.getConfigMenuItem().getLocalWorkDir().setText(config.getLocalWorkDir());
				localHandle.getFileChooser().setCurrentDirectory(f);
			}
		}
		//界面设置
		String softSkin=config.getLookAndFeel();
		if(softSkin.equals("java")){
			Common.setSystemLookAndFeel(this,Common.java);
			Common.setSystemLookAndFeel(edit.getConfigMenuItem(),Common.java);
			edit.getConfigMenuItem().getJavaSkin().setSelected(true);
			tool.getMetal().setSelected(true);
		}else if(softSkin.equals("liquid")){
			Common.setSystemLookAndFeel(this, Common.liquid);
			Common.setSystemLookAndFeel(edit.getConfigMenuItem(), Common.liquid);
			edit.getConfigMenuItem().getLiquidSkin().setSelected(true);
			tool.getLiquid().setSelected(true);
		}else if(softSkin.equals("solaris")){
			Common.setSystemLookAndFeel(this, Common.solaris);
			Common.setSystemLookAndFeel(edit.getConfigMenuItem(), Common.solaris);
			edit.getConfigMenuItem().getSolarisSkin().setSelected(true);
			tool.getMotif().setSelected(true);
		}else if(softSkin.equals("windows")){
			Common.setSystemLookAndFeel(this, Common.windows);
			Common.setSystemLookAndFeel(edit.getConfigMenuItem(), Common.windows);
			edit.getConfigMenuItem().getWindowsSkin().setSelected(true);
			tool.getWindows().setSelected(true);
		}else if(softSkin.equals("quaquamac")){
			Common.setSystemLookAndFeel(this, Common.mac);
			Common.setSystemLookAndFeel(edit.getConfigMenuItem(), Common.mac);
			edit.getConfigMenuItem().getQuaquaMacSkin().setSelected(true);
			tool.getApple().setSelected(true);
		}
		//面板显示
		if(config.getShowLocal().equals("true")){
			this.localWorkSpace.setVisible(true);
			view.getLocalWorkSpace().setSelected(true);
			edit.getConfigMenuItem().getShowLocal().setSelected(true);
		}else if(config.getShowLocal().equals("false")){
			this.localWorkSpace.setVisible(false);
			view.getLocalWorkSpace().setSelected(false);
			edit.getConfigMenuItem().getShowLocal().setSelected(false);
		}
		if(config.getShowRemote().equals("true")){
			this.remoteWorkSpace.setVisible(true);
			view.getRemoteWorkSpace().setSelected(true);
			edit.getConfigMenuItem().getShowRemote().setSelected(true);
		}else if(config.getShowRemote().equals("false")){
			this.remoteWorkSpace.setVisible(false);
			view.getRemoteWorkSpace().setSelected(false);
			edit.getConfigMenuItem().getShowRemote().setSelected(false);
		}
		//工具栏显示 
		if(config.getShowCommonTool().equals("true")){
			this.commonToolBar.setVisible(true);
			if(view.getLoginToolBar().isSelected()){
				this.toolBarPanel.setLayout(new GridLayout(2,1));
			}
			view.getCommonToolBar().setSelected(true);
			edit.getConfigMenuItem().getShowCommonTool().setSelected(true);
		}else if(config.getShowCommonTool().equals("false")){
			this.commonToolBar.setVisible(false);
			view.getCommonToolBar().setSelected(false);
			edit.getConfigMenuItem().getShowCommonTool().setSelected(false);
		}
		if(config.getShowLoginTool().equals("true")){
			this.loginInfoToolBar.setVisible(true);
			if(view.getCommonToolBar().isSelected()){
				this.toolBarPanel.setLayout(new GridLayout(2,1));
			}
			view.getLoginToolBar().setSelected(true);
			edit.getConfigMenuItem().getShowLoginTool().setSelected(true);
		}else if(config.getShowLoginTool().equals("false")){
			this.loginInfoToolBar.setVisible(false);
			view.getLoginToolBar().setSelected(false);
			edit.getConfigMenuItem().getShowLoginTool().setSelected(false);
		}
		//显示文件和目录显示方式
		if(config.getFileShowStyle().equals("file")){
			view.getFilesOnly().setSelected(true);
			edit.getConfigMenuItem().getShowFiles().setSelected(true);
			localHandle.getFileChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);
		}else if(config.getFileShowStyle().equals("dir")){
			view.getDirsOnly().setSelected(true);
			edit.getConfigMenuItem().getShowDirs().setSelected(true);
			localHandle.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}else if(config.getFileShowStyle().equals("both")){
			view.getFilesAndDirs().setSelected(true);
			edit.getConfigMenuItem().getShowBoth().setSelected(true);
			localHandle.getFileChooser().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}
		//设置隐藏文件的显示方式
		if(config.getShowHiddenFiles().equals("true")){
			view.getShowHiddenFiles().setSelected(true);
			edit.getConfigMenuItem().getShowHiddenFiles().setSelected(true);
			localHandle.getFileChooser().setFileHidingEnabled(false);
		}else if(config.getShowHiddenFiles().equals("false")){
			view.getShowHiddenFiles().setSelected(false);
			edit.getConfigMenuItem().getShowHiddenFiles().setSelected(false);
			localHandle.getFileChooser().setFileHidingEnabled(true);
		}
	}

	public void startSoftware(){
		this.setTitle(ZiString.S_Title);
		this.setIconImage(new ImageIcon("img/snake.png").getImage());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(1000,600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public OpenFTPClient(){
		//初始化成员变量
		this.initSystem();
		//初始化菜单，工具栏，工作区域
		this.initMenu();
		this.initToolBar();
		this.initLocalWorkSpace();
		this.initRemoteWorkSpace();
		this.addEventListener();
		//布局各个组件
		this.layoutComponents();
		//加载配置
		this.configSystem();
		//启动程序
		this.startSoftware();
	}

	public LocalWorkSpace getLocalHandle() {
		return localHandle;
	}
	public JTabbedPane getRemoteDirTabbedPane() {
		return remoteDirTabbedPane;
	}
	public LoginInfoToolBar getLoginInfo() {
		return loginInfo;
	}
	public void setLoginToolBar(boolean isVisible){
		this.loginInfoToolBar.setVisible(isVisible);
		this.toolBarPanel.validate();
		this.validate();
	}
	public void setCommonToolBar(boolean isVisible){
		this.commonToolBar.setVisible(isVisible);
		this.toolBarPanel.validate();
		this.validate();
	}
	public void setLocalWorkSpace(boolean isVisible){
		this.localWorkSpace.setVisible(isVisible);
		this.validate();
	}
	public void setRemoteWorkSpace(boolean isVisible){
		this.remoteWorkSpace.setVisible(isVisible);
		this.validate();
	}
	public JPanel getToolBarPanel() {
		return toolBarPanel;
	}
	public ViewMenu getView() {
		return view;
	}
	public Vector<RemoteWorkSpaceTab> getTabbedPanelVector() {
		return tabbedPanelVector;
	}
	public JPanel getRemoteWorkSpace() {
		return remoteWorkSpace;
	}
	public EditMenu getEdit() {
		return edit;
	}
	public ToolMenu getTool() {
		return tool;
	}
	public FTPClientConfig getConfig() {
		return config;
	}
}	