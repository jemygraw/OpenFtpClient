package com.xinxin.openftp.gui;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
public class ViewMenu {
	private JMenu viewMenu; // 查看
	private JCheckBoxMenuItem localWorkSpace;
	private JCheckBoxMenuItem remoteWorkSpace;
	private JMenu toolBarView;
	private JCheckBoxMenuItem commonToolBar;
	private JCheckBoxMenuItem loginToolBar;
	private JRadioButtonMenuItem dirsOnly;
	private JRadioButtonMenuItem filesOnly;
	private JRadioButtonMenuItem filesAndDirs;
	private JCheckBoxMenuItem showHiddenFiles;
	private JMenuItem refreshLocalFiles;
	public JMenu initViewMenu(){
		viewMenu=new JMenu("查看(V)");
		viewMenu.setMnemonic('V');

		localWorkSpace=new JCheckBoxMenuItem("本地工作目录(L)");
		localWorkSpace.setActionCommand("localWorkSpace");
		localWorkSpace.setMnemonic('L');
		viewMenu.add(localWorkSpace);
		remoteWorkSpace=new JCheckBoxMenuItem("远程工作目录(S)");
		remoteWorkSpace.setActionCommand("remoteWorkSpace");
		remoteWorkSpace.setMnemonic('S');
		viewMenu.add(remoteWorkSpace);
		viewMenu.addSeparator();
		
		commonToolBar=new JCheckBoxMenuItem("常规工具栏(C)");
		commonToolBar.setMnemonic('C');
		commonToolBar.setActionCommand("commonToolBar");
		loginToolBar=new JCheckBoxMenuItem("登陆工具栏(I)");
		loginToolBar.setMnemonic('I');
		loginToolBar.setActionCommand("loginToolBar");
		toolBarView=new JMenu("工具栏(T)");
		toolBarView.setMnemonic('T');
		toolBarView.add(commonToolBar);
		toolBarView.add(loginToolBar);
		viewMenu.add(toolBarView);
		viewMenu.addSeparator();
		
		dirsOnly=new JRadioButtonMenuItem("只显示目录(D)");
		dirsOnly.setMnemonic('D');
		dirsOnly.setActionCommand("dirsOnly");
		filesOnly=new JRadioButtonMenuItem("只显示文件(F)");
		filesOnly.setMnemonic('F');
		filesOnly.setActionCommand("filesOnly");
		filesAndDirs=new JRadioButtonMenuItem("显示目录和文件(B)");
		filesAndDirs.setMnemonic('B');
		filesAndDirs.setActionCommand("filesAndDirs");
		ButtonGroup bg=new ButtonGroup();
		bg.add(dirsOnly);
		bg.add(filesOnly);
		bg.add(filesAndDirs);
		viewMenu.add(dirsOnly);
		viewMenu.add(filesOnly);
		viewMenu.add(filesAndDirs);
		viewMenu.addSeparator();
		showHiddenFiles=new JCheckBoxMenuItem("显示隐藏文件(H)");
		showHiddenFiles.setMnemonic('H');
		showHiddenFiles.setActionCommand("showHiddenFiles");
		viewMenu.add(showHiddenFiles);
		viewMenu.addSeparator();
		refreshLocalFiles=new JMenuItem("刷新(R)",new ImageIcon("img/refresh.png"));
		refreshLocalFiles.setMnemonic('R');
		refreshLocalFiles.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,InputEvent.CTRL_MASK));
		refreshLocalFiles.setActionCommand("refreshLocalFiles");
		viewMenu.add(refreshLocalFiles);
		return viewMenu;
	}
	 
	public JCheckBoxMenuItem getLocalWorkSpace() {
		return localWorkSpace;
	}
	public JCheckBoxMenuItem getRemoteWorkSpace() {
		return remoteWorkSpace;
	}
	public JMenu getToolBarView() {
		return toolBarView;
	}
	public JCheckBoxMenuItem getCommonToolBar() {
		return commonToolBar;
	}
	public JCheckBoxMenuItem getLoginToolBar() {
		return loginToolBar;
	}
	public JRadioButtonMenuItem getDirsOnly() {
		return dirsOnly;
	}
	public JRadioButtonMenuItem getFilesOnly() {
		return filesOnly;
	}
	public JRadioButtonMenuItem getFilesAndDirs() {
		return filesAndDirs;
	}
	public JCheckBoxMenuItem getShowHiddenFiles() {
		return showHiddenFiles;
	}
	public JMenuItem getRefreshLocalFiles() {
		return refreshLocalFiles;
	}
	
}
