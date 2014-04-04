package com.xinxin.openftp.gui;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
public class CmdMenu {
	private JMenu cmdMenu; // 命令

	private JMenuItem putFile;
	private JMenuItem getFile;
	
	private JMenuItem renameFile;
	private JMenuItem deleteFile;
	
	private JMenuItem renameDir;
	private JMenuItem makeDir;
	private JMenuItem removeDir;
	
	private JMenuItem mputFile;
	private JMenuItem mgetFile;
	private JMenuItem mdeleteFile;
	private JMenuItem openShell;
	public JMenu initCmdMenu(){
		cmdMenu=new JMenu("命令(C)");
		cmdMenu.setMnemonic('C');
		
		putFile=new JMenuItem("上传本地文件(U)",new ImageIcon("img/putFile.png"));
		putFile.setMnemonic('U');
		putFile.setActionCommand("putFile");
		getFile=new JMenuItem("下载远程文件(G)",new ImageIcon("img/getFile.png"));
		getFile.setMnemonic('G');
		getFile.setActionCommand("getFile");
		renameFile=new JMenuItem("重命名远程文件(R)",new ImageIcon("img/renameFile.png"));
		renameFile.setMnemonic('R');
		renameFile.setActionCommand("renameFile");
		deleteFile=new JMenuItem("删除远程文件(D)",new ImageIcon("img/deleteFile.png"));
		deleteFile.setMnemonic('D');
		deleteFile.setActionCommand("deleteFile");
		renameDir=new JMenuItem("更改远程目录名称(C)",new ImageIcon("img/renameDir.png"));
		renameDir.setMnemonic('C');
		renameDir.setActionCommand("renameDir");
		makeDir=new JMenuItem("新建远程目录(N)",new ImageIcon("img/makeDir.png"));
		makeDir.setMnemonic('N');
		makeDir.setActionCommand("makeDir");
		removeDir=new JMenuItem("删除远程目录(E)",new ImageIcon("img/removeDir.png"));
		removeDir.setMnemonic('E');
		removeDir.setActionCommand("removeDir");
		mputFile=new JMenuItem("批量上传本地文件(W)");
		mputFile.setMnemonic('W');
		mputFile.setActionCommand("mputFile");
		mgetFile=new JMenuItem("批量下载远程文件(M)");
		mgetFile.setMnemonic('M');
		mgetFile.setActionCommand("mgetFile");
		mdeleteFile=new JMenuItem("批量删除远程文件(X)");
		mdeleteFile.setMnemonic('X');
		mdeleteFile.setActionCommand("mdeleteFile");
		openShell=new JMenuItem("FTP客户端命令行工具(S)",new ImageIcon("img/openShell.png"));
		openShell.setMnemonic('S');
		openShell.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK));
		openShell.setActionCommand("openShell");
	
		cmdMenu.add(putFile);
		cmdMenu.add(getFile);
		cmdMenu.addSeparator();
		cmdMenu.add(renameFile);
		cmdMenu.add(deleteFile);
		cmdMenu.addSeparator();
		cmdMenu.add(renameDir);
		cmdMenu.add(makeDir);
		cmdMenu.add(removeDir);
		cmdMenu.addSeparator();
		cmdMenu.add(mputFile);
		cmdMenu.add(mgetFile);
		cmdMenu.add(mdeleteFile);
		cmdMenu.addSeparator();
		cmdMenu.add(openShell);
		return cmdMenu;
	}
	public JMenuItem getPutFile() {
		return putFile;
	}
	public JMenuItem getGetFile() {
		return getFile;
	}
	public JMenuItem getRenameFile() {
		return renameFile;
	}
	public JMenuItem getDeleteFile() {
		return deleteFile;
	}
	public JMenuItem getRenameDir() {
		return renameDir;
	}
	public JMenuItem getMakeDir() {
		return makeDir;
	}
	public JMenuItem getRemoveDir() {
		return removeDir;
	}
	public JMenuItem getMputFile() {
		return mputFile;
	}
	public JMenuItem getMgetFile() {
		return mgetFile;
	}
	public JMenuItem getMdeleteFile() {
		return mdeleteFile;
	}

	public JMenuItem getOpenShell() {
		return openShell;
	}
	
}
