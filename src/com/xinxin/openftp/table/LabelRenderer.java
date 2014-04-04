package com.xinxin.openftp.table;
import java.awt.Component;
 
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
public class LabelRenderer extends JLabel implements TableCellRenderer{
	private static final long serialVersionUID = -3566134720288564271L;

	public LabelRenderer() {
	    setOpaque(true);
	  }
	  
	  public Component getTableCellRendererComponent(JTable table, Object value,
	                   boolean isSelected, boolean hasFocus, int row, int column) {
		if(value instanceof JLabel){
			 this.setIcon(((JLabel)value).getIcon());
		}
	    return this;
	  }
}
