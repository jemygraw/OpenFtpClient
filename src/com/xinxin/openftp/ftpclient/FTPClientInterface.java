package com.xinxin.openftp.ftpclient;
/**
 * 本接口定义了FTP客户端所有的命令原型
 * */
public interface FTPClientInterface{
	/**
	 * Windows系列操作系统支持的FTP客户端命令
	 * */
	public void append(String localFile,String remoteFile);
	 
	public void ascii();
	 
	public void bell();
	 
	public void binary();
	 
	public void bye();
	 
	public void cd(String remoteDir);
	
	public void cdup();
	
	public void close();
	 
	public void delete(String remoteFile);
	 
	public void debug(int debugValue);
	public void dir();
	public void dir(String remoteDir);
	public void dir(String remoteDir,String localFile);
	 
	public void disconnect();
	 
	public void exit();
	 
	public void get(String remoteFile,String localFile);
	
	public void glob();
	
	public void hash();
	
	public void help();
	
	public void help(String cmds[]);
	
	public void lcd(String dir);
	
	public void literal();
	public void ls();
	public void ls(String remoteDir);
	public void ls(String remoteDir,String localFile);
	
	public void mdelete(String remoteFiles[]);
	
	public void mdir(String remoteFiles[],String localFile);
	
	public void mget(String remoteFiles[]);
	
	public void mkdir(String dirName);
	
	public void mls(String remoteFiles[],String localFile);
	
	public void mput(String localFiles[]);
	
	public boolean open(String remoteHostName,int remoteHostPort);
	
	public boolean pass(String userPassword);
	
	public void prompt();
	
	public void put(String localFile,String remoteFile);
	
	public void pwd();
	
	public void quit();
	
	public void quote(String args[]);
	
	public void recv(String remoteFile,String localFile);
	
	public void remotehelp();
	
	public void remotehelp(String cmd);
	
	public void rename(String fromFileName,String toFileName);
	
	public void rmdir(String remoteDir);
	
	public void send(String localFile,String remoteFile);
	
	public void size(String remoteFile);
	
	public void status();
	
	public void system();
	public void trace();
	
	public void type();
	
	public void type(String typeName);
	
	public boolean user(String userName);
	
	public void user(String userName,String userPassword,String userAccount);
	
	public void verbose();
}
