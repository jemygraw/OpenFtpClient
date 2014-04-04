package com.xinxin.openftp.table;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class LabelEditor extends DefaultCellEditor{
	private static final long serialVersionUID = -1345485282231448713L;
	private JLabel valueLabel;
	public LabelEditor(JTextField checkBox) {
		super(checkBox);
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		valueLabel=(JLabel)value;
		return valueLabel;
	}

	public Object getCellEditorValue() {
		return valueLabel;
	}
	public boolean stopCellEditing() {
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}
