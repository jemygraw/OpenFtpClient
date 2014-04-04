package com.xinxin.openftp.config;

import java.io.File;
import java.io.IOException;

import ch.ubique.inieditor.IniEditor;

import com.xinxin.openftp.ftpclient.FTPClientImpl.DATATYPE;
import com.xinxin.openftp.ftpclient.FTPClientImpl.TRANSFERTYPE;
import javax.swing.JOptionPane;
public class FTPClientConfig {
	private TRANSFERTYPE transferType;
	private DATATYPE dataType;
	private String localWorkDir;
	private String lookAndFeel;
	private String showLocal;
	private String showRemote;
	private String showCommonTool;
	private String showLoginTool;
	
	private String fileShowStyle;
	
	private String showHiddenFiles;
	
	private IniEditor editor;
	
	public FTPClientConfig(){
		editor=new IniEditor(true);
	}
	//加载配置文件
	public boolean loadConfigFile(){
		try {
			editor.load(new File("config.ini"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "加载配置文件错误!","错误信息",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	public void loadConfigInfo(){
		//读取传输模式
		String trans=editor.get("TransferType", "transferType");
		if(trans.equalsIgnoreCase("PORT")){
			transferType=TRANSFERTYPE.PORT;
		}else if(trans.equalsIgnoreCase("PASV")){
			transferType=TRANSFERTYPE.PASV;
		}
		//读取数据模式
		String data=editor.get("DataType", "dataType");
		if(data.equalsIgnoreCase("ASCII")){
			dataType=DATATYPE.ASCII;
		}else if(data.equalsIgnoreCase("BINARY")){
			dataType=DATATYPE.BINARY;
		}
		//读取本地初始工作目录
		localWorkDir=editor.get("LocalWorkDir","localWorkDir");
		//读取软件的初始界面
		lookAndFeel=editor.get("LookAndFeel", "lookAndFeel");
		//读取界面组件显示信息
		showLocal=editor.get("ShowComponents", "local");
		showRemote=editor.get("ShowComponents", "remote");
		showCommonTool=editor.get("ShowComponents", "commonTool");
		showLoginTool=editor.get("ShowComponents", "loginTool");
		//读取文件和目录显示方式
		fileShowStyle=editor.get("FileShowStyle", "showStyle");
		//读取隐藏文件和目录的显示方式
		showHiddenFiles=editor.get("ShowHiddenFiles", "showHiddenFiles");
	}
	public TRANSFERTYPE getTransferType() {
		return transferType;
	}
	public DATATYPE getDataType() {
		return dataType;
	}
	public String getLocalWorkDir() {
		return localWorkDir;
	}
	public String getLookAndFeel() {
		return lookAndFeel;
	}
	public String getShowLocal() {
		return showLocal;
	}
	public String getShowRemote() {
		return showRemote;
	}
	public String getShowCommonTool() {
		return showCommonTool;
	}
	public String getShowLoginTool() {
		return showLoginTool;
	}
	public String getFileShowStyle() {
		return fileShowStyle;
	}
	public String getShowHiddenFiles() {
		return showHiddenFiles;
	}
	//更新配置文件
	public void updateTransferType(TRANSFERTYPE transferType) {
		if(transferType==TRANSFERTYPE.PORT){
			editor.set("TransferType", "transferType","PORT");
		}else if(transferType==TRANSFERTYPE.PASV){
			editor.set("TransferType", "transferType","PASV");
		}
	}
	public void updateDataType(DATATYPE dataType) {
		if(dataType==DATATYPE.ASCII){
			editor.set("DataType", "dataType", "ASCII");
		}else if(dataType==DATATYPE.BINARY){
			editor.set("DataType", "dataType", "BINARY");
		}
	}
	public void updateLocalWorkDir(String localWorkDir) {
		editor.set("LocalWorkDir", "localWorkDir", localWorkDir);
	}
	public void updateLookAndFeel(String lookAndFeel) {
		editor.set("LookAndFeel", "lookAndFeel", lookAndFeel);
	}
	public void updateShowLocal(String showLocal) {
		editor.set("ShowComponents", "local",showLocal);
	}
	public void updateShowRemote(String showRemote) {
		editor.set("ShowComponents", "remote",showRemote);
	}
	public void updateShowCommonTool(String showCommonTool) {
		editor.set("ShowComponents", "commonTool",showCommonTool);
	}
	public void updateShowLoginTool(String showLoginTool) {
		editor.set("ShowComponents", "loginTool",showLoginTool);
	}
	public void updateFileShowStyle(String fileShowStyle) {
		editor.set("FileShowStyle", "showStyle",fileShowStyle);
	}
	public void updateShowHiddenFiles(String showHiddenFiles) {
		editor.set("ShowHiddenFiles", "showHiddenFiles",showHiddenFiles);
	}
	public void saveConfig(){
		try {
			editor.save("config.ini");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "保存配置出错","错误提示",JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
