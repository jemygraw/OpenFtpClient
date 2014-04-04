package com.xinxin.openftp.ftpclient;

import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextArea;

public class FTPClientImpl implements FTPClientInterface{
	//可配置成员变量
	public enum TRANSFERTYPE{PORT,PASV};
	private	TRANSFERTYPE transferType;		//传输模式 PORT PASV
	public enum DATATYPE {ASCII,BINARY};		
	private DATATYPE dataTransferType;		//数据传输模式 ASCII BINARY
	private String homeDir;					//本地原来工作目录

	//定义成员变量 
	private int remoteServerPort; 				//服务器命令端口号 通常为21
	private InetAddress remoteServerIPAddress; 	//服务器IP地址(合法IP地址) 一般为公网IP地址

	private InputStream inputStream; 		//网络输入流 与21命令端口通信的输入流
	private OutputStream outputStream; 		//网络输出流 与21命令端口通信的输出流
	private DataOutputStream output;		//与21命令端口通信的包装输出流
	private DataInputStream input;			//与21命令端口通信的包装输入流 

	private Socket clientSocket; 			//客户端套接字 连接21端口的客户端套接字
	private int localConnectionPort;		//本地初始连接21号端口的端口

	private String localWorkDir;			//本地当前工作目录 
	//PORT模式参数
	private InetAddress portLocalIPAddress;	//本地IP地址
	private int portLocalDataPort;			//本地PORT模式数据端口
	private PORTDataTransfer portHandle;	//PORT模式的数据接收句柄
	//PASV模式参数
	private InetAddress pasvRemoteIPAddress;//远程IP地址
	private int pasvRemoteDataPort;			//远程PASV模式数据端口
	private PASVDataTransfer pasvHandle;	//PASV模式的数据接收句柄
	//设置客户端模式
	private boolean bellModeOn;				//响铃模式
	//其余参数 
	private String remoteServerName;			//远程FTP主机名称
	private String localHostName;			//本地主机名称
	private String serverReturnInfo;		//服务器返回信息 
	private JTextArea showInfoTextArea;
	private boolean isLoggedIn;
	private String cmdResultInfo;
	private final int BUFFER_SIZE=4096;
	private boolean isCmdLine;
	private int tabIndex;
	private JTable dirTable;
	
	private String remoteFile;
	private Vector <String> remoteFiles;
	//初始化客户端
	public void init(){
		this.homeDir=System.getProperty("user.home");
		this.localWorkDir=this.homeDir;
		this.isLoggedIn=false;
		this.cmdResultInfo="";
		this.isCmdLine=false;
	}
	public FTPClientImpl(){
		this.init();
	}
	public FTPClientImpl(String remoteServerName,int remoteServerPort){
		this.init();
		this.remoteServerName=remoteServerName;
		this.remoteServerPort=remoteServerPort;
	}
	public void setShowInfoTextArea(JTextArea showInfoTextArea) {
		this.showInfoTextArea = showInfoTextArea;
	}
	public void setTransferType(TRANSFERTYPE transferType) {
		this.transferType = transferType;
	}
	public void setDataTransferType(DATATYPE dataTransferType) {
		this.dataTransferType = dataTransferType;
	}
	public void setCmdLine(boolean isCmdLine) {
		this.isCmdLine = isCmdLine;
	}
	public int getTabIndex() {
		return tabIndex;
	}
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}
	public void setDirTableMouseListener(MouseListener ml){
		this.dirTable.addMouseListener(ml);
	}
	public JTable getDirTable() {
		return dirTable;
	}
	public void setDirTable(JTable dirTable) {
		this.dirTable = dirTable;
	}
	public String getRemoteFile() {
		return remoteFile;
	}
	public void setRemoteFile(String remoteFile) {
		this.remoteFile = remoteFile;
	}
	
	public void setRemoteFiles(Vector<String> remoteFiles) {
		this.remoteFiles = remoteFiles;
	}
	public Vector<String> getRemoteFiles() {
		return remoteFiles;
	}
	//访问器
	public String getRemoteHostName() {
		return remoteServerName;
	}
	public String getLocalHostName() {
		return localHostName;
	}
	public String getServerReturnInfo() {
		return serverReturnInfo;
	}
	public void setLocalWorkDir(String localWorkDir) {
		this.localWorkDir = localWorkDir;
	}
	public String getLocalWorkDir() {
		return localWorkDir;
	}
	public DATATYPE getDataTransferType() {
		return dataTransferType;
	}
	public TRANSFERTYPE getTransferType() {
		return transferType;
	}
	public String getCmdResultInfo() {
		return cmdResultInfo;
	}
	private void appInfoToShowArea(String info){
		Calendar c=Calendar.getInstance();
		String time="["+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"]";
		String text=time+"  "+info+"\r\n";
		this.showInfoTextArea.append(text);
		this.showInfoTextArea.setCaretPosition(this.showInfoTextArea.getDocument().getLength());
	}
	//本类私有方法
	private boolean acceptFileData(DataInputStream netDataInput,String localFileName,boolean isAppend){
		ByteArrayOutputStream swapStream=new ByteArrayOutputStream();
		DataOutputStream storeOutput=null;
		byte recvDataInfo[]=new byte[BUFFER_SIZE];
		int result=0;
		try {
			storeOutput = new DataOutputStream(new FileOutputStream(new File(this.localWorkDir,localFileName),isAppend));
			while((result=netDataInput.read(recvDataInfo))>0){
				swapStream.write(recvDataInfo,0,result);
				storeOutput.write(swapStream.toByteArray());
				swapStream.reset();
			}
		}catch (FileNotFoundException e) {
			System.out.println("创建本地数据文件失败!");
			return false;
		}catch (IOException e){
			remoteServerClosed();
			return false;
		}finally{
			try {
				netDataInput.close();
				swapStream.close();
				storeOutput.close();
			} catch (IOException e) {
				System.out.println("关闭文件输出流失败!");
			}
		}
		return true;
	}
	private boolean showCmdData(DataInputStream dataInput){
		byte recvDataInfo[]=new byte[BUFFER_SIZE];
		this.cmdResultInfo="";
		try{
			while(dataInput.read(recvDataInfo)>0){
				this.cmdResultInfo+=new String(recvDataInfo,"utf8");
			}
		}catch(IOException e){
			remoteServerClosed();
			return false;
		}
		try {
			dataInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    Set <String> strSet=new HashSet <String>();
	    String dirParts[]=this.cmdResultInfo.trim().split("\r\n");
	    this.cmdResultInfo="";
	    for(String dir:dirParts){
	    	strSet.add(dir);
	    }
	    String str[]=new String[strSet.size()];
	    strSet.toArray(str);
	    for(int i=0;i<str.length-1;i++){
	    	this.cmdResultInfo+=str[i]+"\r\n";
	    }
	    this.cmdResultInfo+=str[str.length-1];
	    if(this.isCmdLine){
			this.appInfoToShowArea("\r\n"+this.cmdResultInfo);
	    }
		return true;
	}
	private boolean storeCmdData(DataInputStream netDataInput,String localFileName,boolean isAppend){
		return this.acceptFileData(netDataInput,localFileName,isAppend);
	}
	//连接远程服务器
	private boolean buildCommandConnection() {
		try {
			//连接FTP服务器
			this.clientSocket = new Socket(this.remoteServerIPAddress, this.remoteServerPort);
			this.appInfoToShowArea("连接到:"+this.remoteServerName);
			//建立数据流连接
			this.inputStream=this.clientSocket.getInputStream();
			this.outputStream=this.clientSocket.getOutputStream();
			//封装数据流
			this.input=new DataInputStream(this.inputStream);
			this.output=new DataOutputStream(this.outputStream);
			//设置一些连接参数
			this.localHostName=this.clientSocket.getLocalAddress().getHostName();
			this.localConnectionPort=this.clientSocket.getLocalPort();
			this.portLocalDataPort=this.localConnectionPort+1;
			this.portLocalIPAddress=this.clientSocket.getLocalAddress();
			//接受服务器返回的欢迎信息
			this.recvServerReturnInfo();
			return true;
		} catch (IOException e) {
			this.appInfoToShowArea("连接FTP服务器失败!");
			return false;
		}
	}
	private boolean buildDataConnection(){
		if(this.transferType==TRANSFERTYPE.PORT){
			portHandle=new PORTDataTransfer(this.portLocalIPAddress,this.portLocalDataPort);
			return true;
		}else if(this.transferType==TRANSFERTYPE.PASV){
			pasvHandle=new PASVDataTransfer(this.pasvRemoteIPAddress,this.pasvRemoteDataPort);
			if(pasvHandle.multiConnectServer()){
				return true;
			}
		}
		return false;
	}
	private void closeConnection() {
		try {
			this.clientSocket.close();
			this.clientSocket=null;
		} catch (IOException e) {
			this.appInfoToShowArea("关闭连接失败!");
		}
	}
	private void commandRequest(String cmdName){
		byte cmd[]=null; 
		cmd=new String(cmdName+"\r\n").getBytes();
		this.appInfoToShowArea(cmdName);
		this.sendInfoToServer(cmd);
	}
	private void commandRequest(String cmdName,String cmdParam){
		byte cmd[]=null;
		cmd=new String(cmdName+" "+cmdParam+"\r\n").getBytes();
		this.appInfoToShowArea(cmdName+" "+cmdParam);
		this.sendInfoToServer(cmd);
	}
	private void recvServerReturnInfo(){
		byte buf[] = new byte[BUFFER_SIZE];
		try {
			input.read(buf);
		} catch (IOException e) {
			this.appInfoToShowArea("读取FTP服务器返回信息失败!");
			return;
		}
		String info = new String(buf).trim();
		this.serverReturnInfo=info;
		this.appInfoToShowArea(info);
	}
	private void remoteServerClosed(){
		this.appInfoToShowArea("远程主机关闭连接!");
		if(this.isConnected()){
			this.closeConnection();
		}
	}
	private void sendInfoToServer(byte info[]){
		//发送命令请求
		try{
			this.output.write(info);
		}catch(IOException e){
			this.remoteServerClosed();
		}
	}
	private boolean sendFileData(DataOutputStream dataOutput,String fileName){
		byte localInfo[]=new byte[BUFFER_SIZE];
		ByteArrayOutputStream swapStream=new ByteArrayOutputStream();
		DataInputStream inputData=null;
		int result=0;
		try {
			inputData=new DataInputStream(new FileInputStream(fileName));
			while((result=inputData.read(localInfo))>0){
				swapStream.write(localInfo, 0, result);
				dataOutput.write(swapStream.toByteArray());
				swapStream.reset();
			}
		} catch (FileNotFoundException e) {
			this.appInfoToShowArea("获取输入流失败!");
			return false;
		} catch(IOException e){
			remoteServerClosed();
			return false;
		}

		try {
			dataOutput.close();	
			inputData.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	private void setBellMode(){
		if(bellModeOn){
			bellModeOn=false;
			this.appInfoToShowArea("铃声模式:关!");
		}else{
			bellModeOn=true;
			this.appInfoToShowArea("铃声模式:开!");
		}
	}
	private String setPORTCmd(InetAddress ipAddress,int dataPort){
		String portCmd=null;
		String strIP=ipAddress.getHostAddress();
		strIP=strIP.replace('.', ',');
		//数据端口格式转换
		String strPort=Integer.toHexString(dataPort);
		int len=strPort.length();
		String lowPart=strPort.substring(len-2);
		String highPart=strPort.substring(0,len-2);
		//把两部分转换为十进制的数，进行发送
		int low=Integer.decode("0x"+lowPart);
		int high=Integer.decode("0x"+highPart);
		portCmd=new String("PORT"+" "+strIP+","+high+","+low);
		return portCmd;
	}
	private boolean sendPASVRequest(){
		this.commandRequest("PASV");
		this.recvServerReturnInfo();
		if(this.getServerReturnInfo().startsWith("530")){
			return false;
		}
		this.getPASVInfo(this.getServerReturnInfo());//获取PASV响应信息 包括远程主机IP地址和数据端口
		return true;
	} 
	private boolean sendPORTRequest(){
		String portCmd=null;
		portCmd=this.setPORTCmd(portLocalIPAddress, portLocalDataPort);
		this.commandRequest(portCmd);
		this.recvServerReturnInfo();
		//530 Not logged in.
		if(this.serverReturnInfo.startsWith("530")){
			return false;
		}
		return true;
	}
	private void endPORTTransfer(){
		this.portHandle.closeConnection();
		this.recvServerReturnInfo();
		this.portLocalDataPort+=1;
		if(bellModeOn){
			Toolkit.getDefaultToolkit().beep();
		}
	}
	private void endPASVTransfer(){ 
		this.pasvHandle.closeConnection();
		this.recvServerReturnInfo();
		if(bellModeOn){
			Toolkit.getDefaultToolkit().beep();
		}
	}
	private void getPASVInfo(String pasvInfo){
		int begin=pasvInfo.indexOf("(");
		int end=pasvInfo.indexOf(")");
		String ipPortStr[]=pasvInfo.substring(begin+1,end).split(",");
		byte ipByte[]=new byte[4];
		ipByte[0]=(byte)Integer.parseInt(ipPortStr[0]);
		ipByte[1]=(byte)Integer.parseInt(ipPortStr[1]);
		ipByte[2]=(byte)Integer.parseInt(ipPortStr[2]);
		ipByte[3]=(byte)Integer.parseInt(ipPortStr[3]);
		String portHigh=Integer.toHexString(Integer.parseInt(ipPortStr[4]));
		String portLow=Integer.toHexString(Integer.parseInt(ipPortStr[5]));
		this.pasvRemoteDataPort=Integer.decode(("0x"+portHigh+portLow));
		try {
			this.pasvRemoteIPAddress=InetAddress.getByAddress(ipByte);
		} catch (UnknownHostException e) {
			this.appInfoToShowArea("获取远程主机IP地址失败!");
		}
	}
	private boolean isRemoteFileExists(){
		if(this.getServerReturnInfo().startsWith("550")){
			return false;
		}
		return true;
	}
	private boolean isLocalFileExists(String fileName){
		File file=new File(fileName);
		if(file.exists() && file.isFile()){
			return true;
		}else{
			return false;
		}
	}
	public String getCmdReplyInfo(){
		int strStart=this.getServerReturnInfo().indexOf('\"');
		int strEnd=this.getServerReturnInfo().indexOf('\"', strStart+1);
		String str=this.getServerReturnInfo().substring(strStart+1, strEnd);
		return str;
	}
	public boolean resolveAddress(String remoteServerName){
		try {
			this.remoteServerIPAddress=InetAddress.getByName(remoteServerName);
			this.appInfoToShowArea("解析地址 "+remoteServerName+" 为:"+this.remoteServerIPAddress.getHostAddress());
			this.remoteServerName=remoteServerName;
		} catch (UnknownHostException e) {
			this.appInfoToShowArea("未知主机地址");
			return false;
		}
		return true;
	}
	public boolean isConnected(){
		if(clientSocket==null){
			return false;
		}
		return true;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	@Override
	public void append(String localFile, String remoteFile) {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.isLocalFileExists(localFile)){
				if(this.sendPORTRequest()){
					this.commandRequest("APPE", remoteFile);
					this.buildDataConnection();
					this.recvServerReturnInfo();
					this.sendFileData(portHandle.getOutputDataStream(), localFile);
					this.endPORTTransfer();
				}
			}else{
				this.appInfoToShowArea(localFile+" :本地文件不存在!");
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.isLocalFileExists(localFile)){
				if(this.sendPASVRequest()){
					this.commandRequest("APPE", remoteFile);
					this.buildDataConnection();
					this.recvServerReturnInfo();
					this.sendFileData(pasvHandle.getOutputDataStream(), localFile);
					this.endPASVTransfer();
				}
			}else{
				this.appInfoToShowArea(localFile+" :本地文件不存在!");
			}
		}
	}

	@Override
	public void ascii() {
		this.dataTransferType=DATATYPE.ASCII;
		this.commandRequest("TYPE","A");
		this.recvServerReturnInfo();
	}

	@Override
	public void bell() {
		this.setBellMode();
	}

	@Override
	public void binary() {
		this.dataTransferType=DATATYPE.BINARY;
		this.commandRequest("TYPE","I");
		this.recvServerReturnInfo();
	}

	@Override
	public void bye() {
		this.quit();
	}

	@Override
	public void cd(String remoteDir) {
		this.commandRequest("CWD",remoteDir);
		this.recvServerReturnInfo();
	}

	@Override
	public void cdup(){
		this.commandRequest("CDUP");
		this.recvServerReturnInfo();
	}

	@Override
	public void close() {
		this.disconnect();
	}

	@Override
	public void debug(int debugValue) {


	}

	@Override
	public void delete(String remoteFile) {
		this.commandRequest("DELE",remoteFile);
		this.recvServerReturnInfo();
	}

	@Override
	public void dir() {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.sendPORTRequest()){
				this.commandRequest("LIST");
				if(this.buildDataConnection()){
					this.recvServerReturnInfo();
					this.showCmdData(portHandle.getInputDataStream());
					this.endPORTTransfer();
				}
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.sendPASVRequest()){
				this.commandRequest("LIST");
				if(this.buildDataConnection()){
					this.recvServerReturnInfo();
					this.showCmdData(pasvHandle.getInputDataStream());
					this.endPASVTransfer();
				}
			}
		}
	}
	@Override
	public void dir(String remoteDir) {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.sendPORTRequest()){
				this.commandRequest("LIST", remoteDir);
				if(this.buildDataConnection()){
					this.recvServerReturnInfo();
					this.showCmdData(portHandle.getInputDataStream());
					this.endPORTTransfer();
				}
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.sendPASVRequest()){
				this.commandRequest("LIST",remoteDir);
				if(this.buildDataConnection()){
					this.recvServerReturnInfo();
					this.showCmdData(pasvHandle.getInputDataStream());
					this.endPASVTransfer();
				}
			}
		}
	}
	@Override
	public void dir(String remoteDir, String localFile) {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.sendPORTRequest()){
				this.commandRequest("LIST", remoteDir);
				if(this.buildDataConnection()){
					this.recvServerReturnInfo();
					this.storeCmdData(portHandle.getInputDataStream(), localFile, false);
					this.endPORTTransfer();
				}
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.sendPASVRequest()){
				this.commandRequest("LIST",remoteDir);
				if(this.buildDataConnection()){
					this.recvServerReturnInfo();
					this.storeCmdData(pasvHandle.getInputDataStream(), localFile,false);
					this.endPASVTransfer();
				}
			}
		}
	}

	@Override
	public void disconnect() {
		this.commandRequest("QUIT");
		this.recvServerReturnInfo();
		if(isConnected()){
			this.closeConnection();
			this.clientSocket=null;
		}
	}

	@Override
	public void exit(){
		this.quit();
	}

	@Override
	public void get(String remoteFile, String localFile) {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.sendPORTRequest()){
				this.commandRequest("RETR", remoteFile);
				this.buildDataConnection();
				this.recvServerReturnInfo();
				if(this.isRemoteFileExists()){
					this.acceptFileData(portHandle.getInputDataStream(), localFile, false);
					this.endPORTTransfer();
				}
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.sendPASVRequest()){
				this.commandRequest("RETR",remoteFile);
				this.buildDataConnection();
				this.recvServerReturnInfo();
				if(this.isRemoteFileExists()){
					this.acceptFileData(pasvHandle.getInputDataStream(), localFile,false);
					this.endPASVTransfer();
				}
			}
		}
	}

	@Override
	public void glob() {


	}

	@Override
	public void hash() {


	}
	@Override
	public void help(){
		FTPClientHelp.helpList();
	}
	@Override
	public void help(String cmds[]) {
		FTPClientHelp.help(cmds);

	}
	public void lcd(){
		this.appInfoToShowArea("目前的本地目录为:"+this.getLocalWorkDir());
	}
	@Override
	public void lcd(String dir) {
		File f=new File(dir);
		if(f.isDirectory()){
			this.setLocalWorkDir(dir);
			this.appInfoToShowArea("目前的本地目录为:"+this.getLocalWorkDir());
		}else{
			this.appInfoToShowArea("非法目录!");
		}
	}

	@Override
	public void literal() {


	}

	@Override
	public void ls() {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.sendPORTRequest()){
				this.commandRequest("NLST");
				this.buildDataConnection();
				this.recvServerReturnInfo();
				this.showCmdData(portHandle.getInputDataStream());
				this.endPORTTransfer();
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.sendPASVRequest()){
				this.commandRequest("NLST");
				this.buildDataConnection();
				this.recvServerReturnInfo();
				this.showCmdData(pasvHandle.getInputDataStream());
				this.endPASVTransfer();
			}
		}

	}
	@Override
	public void ls(String remoteDir) {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.sendPORTRequest()){
				this.commandRequest("NLST",remoteDir);
				this.buildDataConnection();
				this.recvServerReturnInfo();
				if(this.isRemoteFileExists()){
					this.showCmdData(portHandle.getInputDataStream());
					this.endPORTTransfer();
				}
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.sendPASVRequest()){
				this.commandRequest("NLST",remoteDir);
				this.buildDataConnection();
				this.recvServerReturnInfo();
				if(this.isRemoteFileExists()){
					this.showCmdData(pasvHandle.getInputDataStream());
					this.endPASVTransfer();
				}
			}
		}

	}
	@Override
	public void ls(String remoteDir, String localFile) {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.sendPORTRequest()){
				this.commandRequest("NLST",remoteDir);
				this.buildDataConnection();
				this.recvServerReturnInfo();
				if(this.isRemoteFileExists()){
					this.storeCmdData(portHandle.getInputDataStream(), localFile, false);
					this.endPORTTransfer();
				}
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.sendPASVRequest()){
				this.commandRequest("NLST",remoteDir);
				this.buildDataConnection();
				this.recvServerReturnInfo();
				if(this.isRemoteFileExists()){	
					this.storeCmdData(pasvHandle.getInputDataStream(), localFile,false);
					this.endPASVTransfer();
				}
			}
		}
	}

	@Override
	public void mdelete(String[] remoteFiles) {
		if(this.transferType==TRANSFERTYPE.PORT){
			for(String file:remoteFiles){
				this.delete(file);
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			for(String file:remoteFiles){
				this.delete(file);
			}
		}
	}

	@Override
	public void mdir(String[] remoteFiles, String localFile) {
		if(this.transferType==TRANSFERTYPE.PORT){
			int times=1;
			for(String d:remoteFiles){
				if(times>1){
					if(this.sendPORTRequest()){
						this.commandRequest("LIST",d);
						this.buildDataConnection();
						this.recvServerReturnInfo();
						this.storeCmdData(portHandle.getInputDataStream(),localFile,true);
						this.endPORTTransfer();
					}
				}else{
					this.dir(d,localFile);
				}
				times++;
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			int times=1;
			for(String d:remoteFiles){
				if(times>1){
					if(this.sendPASVRequest()){
						this.commandRequest("LIST",d);
						this.buildDataConnection();
						this.recvServerReturnInfo();
						this.storeCmdData(pasvHandle.getInputDataStream(),localFile,true);
						this.endPASVTransfer();
					}
				}else{
					this.dir(d, localFile);
				}
				times++;
			}
		}
	}

	@Override
	public void mget(String[] remoteFiles) {
		if(this.transferType==TRANSFERTYPE.PORT){
			for(String file:remoteFiles){
				this.get(file, file);
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			for(String file:remoteFiles){
				this.get(file, file);
			}
		}
	}

	@Override
	public void mkdir(String dirName) {
		this.commandRequest("MKD",dirName);
		this.recvServerReturnInfo();
	}

	@Override
	public void mls(String[] remoteFiles, String localFile) {
		if(this.transferType==TRANSFERTYPE.PORT){
			int times=1;
			for(String d:remoteFiles){
				if(times>1){
					if(this.sendPORTRequest()){
						this.commandRequest("NLST",d);
						this.buildDataConnection();
						this.recvServerReturnInfo();
						this.storeCmdData(portHandle.getInputDataStream(),localFile,true);
						this.endPORTTransfer();
					}
				}else{
					this.ls(d, localFile);
				}
				times++;
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			int times=1;
			for(String d:remoteFiles){
				if(times>1){
					if(this.sendPASVRequest()){
						this.commandRequest("NLST",d);
						this.buildDataConnection();
						this.recvServerReturnInfo();
						this.storeCmdData(pasvHandle.getInputDataStream(),localFile,true);
						this.endPASVTransfer();
					}
				}else{
					this.ls(d, localFile);
				}
				times++;
			}
		}
	}


	@Override
	public void mput(String[] localFiles) {
		if(this.transferType==TRANSFERTYPE.PORT){
			for(String file:localFiles){
				this.put(file, file);
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			for(String file:localFiles){
				this.put(file, file);
			}
		}
	}

	@Override
	public boolean open(String remoteServerName, int remoteHostPort) {
		if(this.resolveAddress(remoteServerName)){
			this.remoteServerPort=remoteHostPort;
			if(this.buildCommandConnection()){//建立命令连接
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean pass(String userPassword) {
		this.commandRequest("PASS", userPassword);
		this.recvServerReturnInfo();
		if(this.getServerReturnInfo().startsWith("230")){
			this.isLoggedIn=true;
			return true;
		}
		return false; 
	}

	@Override
	public void prompt() {


	}

	@Override
	public void put(String localFile, String remoteFile) {
		if(this.transferType==TRANSFERTYPE.PORT){
			if(this.isLocalFileExists(localFile)){
				if(this.sendPORTRequest()){
					this.commandRequest("STOR", remoteFile);
					this.recvServerReturnInfo();
					this.buildDataConnection();
					this.sendFileData(portHandle.getOutputDataStream(), localFile);
					this.endPORTTransfer();
				}
			}else{
				this.appInfoToShowArea(localFile+" :本地文件不存在!");
			}
		}else if(this.transferType==TRANSFERTYPE.PASV){
			if(this.isLocalFileExists(localFile)){
				if(this.sendPASVRequest()){
					this.commandRequest("STOR", remoteFile);
					this.recvServerReturnInfo();
					this.buildDataConnection();
					this.sendFileData(pasvHandle.getOutputDataStream(), localFile);
					this.endPASVTransfer();
				}
			}else{
				this.appInfoToShowArea(localFile+" :本地文件不存在!");
			}
		}
	}

	@Override
	public void pwd() {
		this.commandRequest("PWD");
		this.recvServerReturnInfo();
	}

	@Override
	public void quit() {
		if(this.isConnected()){
			this.disconnect();
		}
		System.exit(0);
	}

	@Override
	public void quote(String[] args) {


	}

	@Override
	public void recv(String remoteFile, String localFile) {
		this.get(remoteFile, localFile);
	}
	@Override
	public void remotehelp(){
		this.commandRequest("HELP");
		this.recvServerReturnInfo();
	}
	@Override
	public void remotehelp(String cmd) {
		this.commandRequest("HELP",cmd);
		this.recvServerReturnInfo();
	}

	@Override
	public void rename(String fromFileName, String toFileName) {
		this.commandRequest("RNFR",fromFileName);
		this.recvServerReturnInfo();
		if(this.getServerReturnInfo().startsWith("530") || this.getServerReturnInfo().startsWith("550")){
			return;
		}
		this.commandRequest("RNTO",toFileName);
		this.recvServerReturnInfo();
	}

	@Override
	public void rmdir(String remoteDir) {
		this.commandRequest("RMD",remoteDir);
		this.recvServerReturnInfo();
	}

	@Override
	public void send(String localFile, String remoteFile) {
		this.put(localFile, remoteFile);
	}

	@Override
	public void size(String remoteFile) {
		this.commandRequest("SIZE",remoteFile);
		this.recvServerReturnInfo();
	}
	@Override
	public void status() {
		if(!this.isConnected()){
			System.out.print("未连接!");
		}else{
			System.out.print("连接到 "+this.getRemoteHostName());
		}
		if(this.dataTransferType==DATATYPE.ASCII){
			System.out.print("类型:ASCII;");
		}else if(this.dataTransferType==DATATYPE.BINARY){
			System.out.print("类型:BINARY;");
		}
		if(this.bellModeOn){
			System.out.print("铃声:开;");
		}else{
			System.out.print("铃声:关;");
		}
	}

	@Override
	public void system() {
		this.commandRequest("SYST");
		this.recvServerReturnInfo();
	}
	@Override
	public void trace() {


	}
	@Override 
	public void type(){
		if(this.dataTransferType==DATATYPE.ASCII){
			this.appInfoToShowArea("使用 ASCII 模式传送文件");
		}else if(this.dataTransferType==DATATYPE.BINARY){
			this.appInfoToShowArea("使用 BINARY 模式传送文件");
		}
	}
	@Override
	public void type(String typeName) {
		DATATYPE t=null;
		if(typeName.equalsIgnoreCase("ASCII")){
			t=DATATYPE.ASCII;
		}else  if(typeName.equalsIgnoreCase("BINARY")){
			t=DATATYPE.BINARY;
		}
		if(t==DATATYPE.ASCII ){
			this.ascii();
		}else if(t==DATATYPE.BINARY){
			this.binary();
		}else{
			this.appInfoToShowArea(t.toString()+":未知模式");
		}
	}

	@Override
	public boolean user(String userName) {
		this.commandRequest("USER", userName);
		this.recvServerReturnInfo();
		if(this.getServerReturnInfo().startsWith("530")){
			return false;
		}
		return true;
	}

	@Override
	public void user(String userName, String userPassword, String userAccount) {


	}

	@Override
	public void verbose() {


	}

}
