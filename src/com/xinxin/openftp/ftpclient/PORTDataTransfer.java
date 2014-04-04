package com.xinxin.openftp.ftpclient;

import java.net.*;
import java.io.*;
/**
 * 本类实现PORT(主动模式)的数据传输
 * 在发送PORT请求之后，客户端开启数据端口监听，等待远程服务器
 * 的连接。在接收到连接之后，停止监听，进行数据接收。
 * */
public class PORTDataTransfer {
	private ServerSocket localDataHandleSocket;			//本地监听远程主机主动连接的套接字
	private Socket dataSocket;							//本地进行数据通信的套接字

	private int localDataPort;							//本地数据监听端口
	private InetAddress localIPAddress;					//本地IP地址

	private InputStream inputStream;					//网络输入流
	private OutputStream outputStream;					//网络输出流
	private DataInputStream input;
	private DataOutputStream output;

	public PORTDataTransfer(InetAddress ipAddress, int port) {
		this.localIPAddress = ipAddress;
		this.localDataPort = port;
		this.listeningPort();
	}
	private void listeningPort() {
		try {
			localDataHandleSocket = new ServerSocket(localDataPort,0,localIPAddress);
			try {
				dataSocket = localDataHandleSocket.accept();
			} catch (IOException e) {
				System.out.println("PORT模式:监听数据端口失败!");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public DataInputStream getInputDataStream() {
		try{
			this.inputStream=this.dataSocket.getInputStream();
		}catch(IOException e){
			System.out.println("PORT模式:初始化数据输入流失败!");
			return null;
		}
		this.input=new DataInputStream(this.inputStream);
		return this.input;
	}
	public DataOutputStream getOutputDataStream() {
		try{
			this.outputStream=dataSocket.getOutputStream();
		}catch(IOException e){
			System.out.println("PORT模式:初始化数据输出流失败!");
			return null;
		}
		return output;
	}
	public void closeConnection() {
		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
