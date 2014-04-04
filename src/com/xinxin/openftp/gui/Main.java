package com.xinxin.openftp.gui;

import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
public class Main {
	//闪屏
	public static void splash(){
		int splashWidth=300;
		int splashHeight=200;
		JFrame f=new JFrame();
		f.setLayout(new GridLayout(1,1));
		JLabel l=new JLabel(new ImageIcon("img/splash.png"));
		f.add(l);
		f.setUndecorated(true);
		f.setSize(splashWidth,splashHeight);
		Common.getPos(splashWidth,splashHeight);
		f.setLocation(Common.posX,Common.posY);
		f.setVisible(true);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		f.dispose();
	}
	public static void main(String args[]){
		Main.splash();

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				//全局化软件字体
				Common.initGlobalFontSetting();
				new OpenFTPClient();
			}
		});

	}
}
