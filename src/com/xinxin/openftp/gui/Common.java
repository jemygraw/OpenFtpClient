package com.xinxin.openftp.gui;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Enumeration;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
public class Common{
	private static Toolkit toolkit=Toolkit.getDefaultToolkit();
	public static int posX;
	public static int posY;
	private static int screenWidth=toolkit.getScreenSize().width;
	private static int screenHeight=toolkit.getScreenSize().height;
	public static String java="javax.swing.plaf.metal.MetalLookAndFeel";
	public static String liquid="com.birosoft.liquid.LiquidLookAndFeel";
	public static String solaris="com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	public static String windows="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	public static String mac="ch.randelshofer.quaqua.QuaquaLookAndFeel";
	public static void getPos(int winWidth,int winHeight){
		posX=(screenWidth-winWidth)/2;
		posY=(screenHeight-winHeight)/2;
	}
	public static void initGlobalFontSetting(Font font) {
		FontUIResource fontRes = new javax.swing.plaf.FontUIResource(font);
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, fontRes);
		}
	}
	public static void initGlobalFontSetting() {
		Font font=new Font("SansSerif",Font.TRUETYPE_FONT,12);
		FontUIResource fontRes = new javax.swing.plaf.FontUIResource(font);
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, fontRes);
		}
	}
	//设置程序的观感
	public static void setSystemLookAndFeel(Component comp,String lookAndFeelStr){
		try{
			UIManager.setLookAndFeel(lookAndFeelStr);
			SwingUtilities.updateComponentTreeUI(comp);
		}catch(Exception e){
			return;
		}
	}
}
