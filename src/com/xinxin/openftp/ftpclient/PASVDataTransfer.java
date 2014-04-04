package com.xinxin.openftp.ftpclient;

import java.net.*;
import java.io.*;
/**
 * 本类实现FTP的PASV(被动模式)的数据传输
 * */
public class PASVDataTransfer {
	private InetAddress remoteIPAddress;			//远程FTP主机IP地址
	private int remoteDataPort;						//远程FTP主机的数据端口
	
	private InputStream inputStream;				//网络输入流
	private OutputStream outputStream;				//网络输出流
	private DataInputStream input;
	private DataOutputStream output;
	
	private Socket dataSocket;						//数据套接字
	
	public PASVDataTransfer(InetAddress ipAddress,int port){
		this.remoteIPAddress=ipAddress;
		this.remoteDataPort=port;
	}
	public boolean connectDataServer(){
		try{
			this.dataSocket=new Socket(this.remoteIPAddress,this.remoteDataPort);
			return true;
		}catch(IOException e){
			System.out.println("PASV模式:连接FTP数据端口失败!");
			return false;
		}
	}
    public boolean multiConnectServer(){
    	int times=1;
		while(times<=10){
			System.out.println("进行第"+times+"次连接!");
			if(this.connectDataServer()){
				return true;
			}
			times++;
		}
		return false;
    }
	public DataInputStream getInputDataStream(){
		try {
			this.inputStream=this.dataSocket.getInputStream();
		} catch (IOException e) {
			System.out.println("PASV模式:初始化数据输入流失败!");
			return null;
		}
		this.input=new DataInputStream(this.inputStream);
		return this.input;
	}
	public DataOutputStream getOutputDataStream(){
		try {
			this.outputStream=this.dataSocket.getOutputStream();
		} catch (IOException e) {
			System.out.println("PASV模式:初始化数据输出流失败!");
			return null;
		} 
		this.output=new DataOutputStream(this.outputStream);
		return this.output;
	}
	public void closeConnection(){
		try{
			this.dataSocket.close();
			if(this.input!=null){
				this.input.close();
			}
			if(this.output!=null){
				this.output.close();
			}
			if(this.inputStream!=null){
				this.inputStream.close();
			}
			if(this.outputStream!=null){
				this.outputStream.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}