package com.xinxin.openftp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.xinxin.openftp.config.FTPClientConfig;
import com.xinxin.openftp.ftpclient.FTPClientImpl.DATATYPE;
import com.xinxin.openftp.ftpclient.FTPClientImpl.TRANSFERTYPE;

public class ConfigMenuItem extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1021779399252757121L;
	private JTabbedPane tabbedPane;
	private JButton confirmButton;
	private JButton cancelButton;
	private JTextField localWorkDir;
	private JComboBox transType;
	private JComboBox dataType;
	private JRadioButton idleState;
	private JRadioButton closeState;
	private JRadioButton exitState;
	private JRadioButton lockState;
	private JRadioButton logoutState;
	private JRadioButton restartState;
	private JRadioButton shutdownState;
	private JRadioButton javaSkin;
	private JRadioButton liquidSkin;
	private JRadioButton solarisSkin;
	private JRadioButton windowsSkin;
	private JRadioButton quaquaMacSkin;
	private JFileChooser fileChooser;
	private JCheckBox showLocal;
	private JCheckBox showRemote;
	private JCheckBox showCommonTool;
	private JCheckBox showLoginTool;
	private JRadioButton showDirs;
	private JRadioButton showFiles;
	private JRadioButton showBoth;
	private JCheckBox showHiddenFiles;
	public void init(){
		tabbedPane=new JTabbedPane();

		JPanel connSetting=new JPanel();

		TitledBorder tb1=new TitledBorder("传输模式设置");
		JPanel transTypePanel=new JPanel();
		transTypePanel.setBorder(tb1);
		JLabel transTypeLabel=new JLabel("传输模式");
		transType=new JComboBox();
		transType.addItem("PORT (主动模式)");
		transType.addItem("PASV (被动模式)");
		transTypePanel.add(transTypeLabel);
		transTypePanel.add(transType);

		TitledBorder tb2=new TitledBorder("数据模式设置");
		JPanel dataTypePanel=new JPanel();
		dataTypePanel.setBorder(tb2);
		JLabel dataTypeLabel=new JLabel("数据模式");
		dataType=new JComboBox();
		dataType.addItem("ASCII (文本模式)");
		dataType.addItem("BINARY (二进制模式)");
		dataTypePanel.add(dataTypeLabel);
		dataTypePanel.add(dataType);
		connSetting.add(transTypePanel);
		connSetting.add(dataTypePanel);

		JPanel workSpace=new JPanel();
		JLabel label=new JLabel("请选择本地目录:");
		localWorkDir=new JTextField(20);
		JButton selectDir=new JButton("...");
		selectDir.addActionListener(this);
		selectDir.setActionCommand("selectDir");
		workSpace.add(label);
		workSpace.add(localWorkDir);
		workSpace.add(selectDir);

		JPanel feelSetting=new JPanel(new GridLayout(4,1));

		JPanel softSkinPanel=new JPanel(new GridLayout(2,3));
		TitledBorder tb4=new TitledBorder("软件外观选择");
		softSkinPanel.setBorder(tb4);
		javaSkin=new JRadioButton("Java外观");
		liquidSkin=new JRadioButton("Liquid外观");
		solarisSkin=new JRadioButton("Solaris外观");
		windowsSkin=new JRadioButton("Windows外观");
		quaquaMacSkin=new JRadioButton("QuaQua Mac外观");
		ButtonGroup bg2=new ButtonGroup();
		bg2.add(javaSkin);bg2.add(liquidSkin);
		bg2.add(solarisSkin);bg2.add(windowsSkin);
		bg2.add(quaquaMacSkin);
		softSkinPanel.add(javaSkin);softSkinPanel.add(liquidSkin);
		softSkinPanel.add(solarisSkin);softSkinPanel.add(windowsSkin);
		softSkinPanel.add(quaquaMacSkin);
		feelSetting.add(softSkinPanel);

		JPanel setVisiblePanel=new JPanel(new GridLayout(2,3));
		TitledBorder tb5=new TitledBorder("设置显示组件");
		setVisiblePanel.setBorder(tb5);
		 
		showLocal=new JCheckBox("显示本地目录");
		showRemote=new JCheckBox("显示远程目录");
		showCommonTool=new JCheckBox("显示常规工具栏");
		showLoginTool=new JCheckBox("显示登陆工具栏");
	 
		setVisiblePanel.add(showLocal);
		setVisiblePanel.add(showRemote);
		setVisiblePanel.add(showCommonTool);
		setVisiblePanel.add(showLoginTool);
		feelSetting.add(setVisiblePanel);

		JPanel showStylePanel=new JPanel(new GridLayout(1,3));
		TitledBorder tb6=new TitledBorder("设置本地文件的显示方式");
		showStylePanel.setBorder(tb6);
		showDirs=new JRadioButton("只显示目录");
		showFiles=new JRadioButton("只显示文件");
		showBoth=new JRadioButton("显示目录和文件");
		ButtonGroup bg3=new ButtonGroup();
		bg3.add(showDirs);bg3.add(showFiles);bg3.add(showBoth);
		showStylePanel.add(showDirs);showStylePanel.add(showFiles);showStylePanel.add(showBoth);
		feelSetting.add(showStylePanel);

		JPanel showHiddenPanel=new JPanel(new GridLayout(1,1));
		TitledBorder tb7=new TitledBorder("显示隐藏文件或目录设置");
		showHiddenPanel.setBorder(tb7);
		showHiddenFiles=new JCheckBox("显示隐藏文件或目录");
		showHiddenPanel.add(showHiddenFiles);
		feelSetting.add(showHiddenPanel);

		tabbedPane.addTab("连接", connSetting);
		tabbedPane.addTab("工作区", workSpace);
		tabbedPane.addTab("界面", feelSetting);

		confirmButton=new JButton("确定(O)");
		confirmButton.setMnemonic('O');
		cancelButton=new JButton("取消(C)");
		cancelButton.setMnemonic('C');
		confirmButton.addActionListener(new ConfigChangeAction(this));
		cancelButton.addActionListener(this);
		this.getContentPane().add(tabbedPane,BorderLayout.CENTER);
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		this.getContentPane().add(buttonPanel,BorderLayout.SOUTH);
	}
	public ConfigMenuItem(){
		this.init();
		this.setTitle("参数设置");
		this.setIconImage(new ImageIcon("img/config.png").getImage());
		this.setModal(true);
		this.setSize(500, 500);
		Common.getPos(500, 500);
		this.setLocation(Common.posX, Common.posY);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="paramsConfig"){
			this.setVisible(true);
		}else if(e.getSource()==cancelButton){
			this.dispose();
		}else if(e.getActionCommand()=="selectDir"){
			fileChooser=new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setApproveButtonText("确定(O)");
			fileChooser.setApproveButtonMnemonic('O');
			fileChooser.addActionListener(new FileChooserAction());
			fileChooser.showOpenDialog(this);
		}
	}
	class FileChooserAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			localWorkDir.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}
	class ConfigChangeAction implements ActionListener{
		private ConfigMenuItem d;
		public ConfigChangeAction(ConfigMenuItem d){
			this.d=d;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			 FTPClientConfig config=new FTPClientConfig();
			 config.loadConfigFile();
			 if(transType.getSelectedIndex()==0){
				 config.updateTransferType(TRANSFERTYPE.PORT);
			 }else if(transType.getSelectedIndex()==1){
				 config.updateTransferType(TRANSFERTYPE.PASV);
			 }
			 if(dataType.getSelectedIndex()==0){
				 config.updateDataType(DATATYPE.ASCII);
			 }else if(dataType.getSelectedIndex()==1){
				 config.updateDataType(DATATYPE.BINARY);
			 }
			 if(localWorkDir.getText()!=""){
				 config.updateLocalWorkDir(localWorkDir.getText());
			 }
			 
			 
			 if(javaSkin.isSelected()){
				 config.updateLookAndFeel("java");
			 }else if(liquidSkin.isSelected()){
				 config.updateLookAndFeel("liquid");
			 }else if(solarisSkin.isSelected()){
				 config.updateLookAndFeel("solaris");
			 }else if(windowsSkin.isSelected()){
				 config.updateLookAndFeel("windows");
			 }else if(quaquaMacSkin.isSelected()){
				 config.updateLookAndFeel("quaquamac");
			 }
			 
			 if(showLocal.isSelected()){
				 config.updateShowLocal("true");
			 }else{
				 config.updateShowLocal("false");
			 }
			 if(showRemote.isSelected()){
				 config.updateShowRemote("true");
			 }else{
				 config.updateShowRemote("false");
			 }
			 if(showCommonTool.isSelected()){
				 config.updateShowCommonTool("true");
			 }else{
				 config.updateShowCommonTool("false");
			 }
			 if(showLoginTool.isSelected()){
				 config.updateShowLoginTool("true");
			 }else{
				 config.updateShowLoginTool("false");
			 }
			 
			 if(showFiles.isSelected()){
				 config.updateFileShowStyle("file");
			 }else if(showDirs.isSelected()){
				 config.updateFileShowStyle("dir");
			 }else if(showBoth.isSelected()){
				 config.updateFileShowStyle("both");
			 }
			 
			 if(showHiddenFiles.isSelected()){
				 config.updateShowHiddenFiles("true");
			 }else{
				 config.updateShowHiddenFiles("false");
			 }
			 config.saveConfig();
			 d.dispose();
		}
		
	}
	public JComboBox getTransType() {
		return transType;
	}
	public JComboBox getDataType() {
		return dataType;
	}
	public JTextField getLocalWorkDir() {
		return localWorkDir;
	}
	public JRadioButton getIdleState() {
		return idleState;
	}
	public JRadioButton getExitState() {
		return exitState;
	}
	public JRadioButton getLockState() {
		return lockState;
	}
	public JRadioButton getLogoutState() {
		return logoutState;
	}
	public JRadioButton getRestartState() {
		return restartState;
	}
	public JRadioButton getShutdownState() {
		return shutdownState;
	}
	public JRadioButton getCloseState() {
		return closeState;
	}
	public JRadioButton getJavaSkin() {
		return javaSkin;
	}
	public JRadioButton getLiquidSkin() {
		return liquidSkin;
	}
	public JRadioButton getSolarisSkin() {
		return solarisSkin;
	}
	public JRadioButton getWindowsSkin() {
		return windowsSkin;
	}
	public JRadioButton getQuaquaMacSkin() {
		return quaquaMacSkin;
	}
	public JCheckBox getShowLocal() {
		return showLocal;
	}
	public JCheckBox getShowRemote() {
		return showRemote;
	}
	public JCheckBox getShowCommonTool() {
		return showCommonTool;
	}
	public JCheckBox getShowLoginTool() {
		return showLoginTool;
	}
	public JRadioButton getShowDirs() {
		return showDirs;
	}
	public JRadioButton getShowFiles() {
		return showFiles;
	}
	public JRadioButton getShowBoth() {
		return showBoth;
	}
	public JCheckBox getShowHiddenFiles() {
		return showHiddenFiles;
	}
	
}
