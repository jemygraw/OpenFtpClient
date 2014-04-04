package com.xinxin.openftp.table;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel{
	private static final long serialVersionUID = 6369503573052186824L;

	public   boolean   isCellEditable(int   row,int   column){   
		if(column==1||column==2||column==3||column==4){
			return false;
		}else{
			return true;
		}
	}   
	
}
