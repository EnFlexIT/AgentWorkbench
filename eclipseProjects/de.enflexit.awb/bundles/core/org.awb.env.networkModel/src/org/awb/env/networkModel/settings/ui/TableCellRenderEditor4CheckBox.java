package org.awb.env.networkModel.settings.ui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.enflexit.common.swing.TableCellColorHelper;

/**
 * The Class TableCellRenderEditor4CheckBox.
 */
public class TableCellRenderEditor4CheckBox extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	private static final long serialVersionUID = -1544056145162025328L;

	private JCheckBox checkBox;
	
	public TableCellRenderEditor4CheckBox() { }
	
	public JCheckBox getCheckBox() {
		if (checkBox==null) {
			checkBox = new JCheckBox();
			checkBox.setHorizontalAlignment(JCheckBox.CENTER);
			checkBox.setOpaque(true);
		}
		return checkBox;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.getCheckBox().setSelected((boolean)value);
		TableCellColorHelper.setTableCellRendererColors(this.getCheckBox(), row, isSelected);
		return this.getCheckBox();
	}
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.getCheckBox().setSelected((boolean)value);
		return this.getCheckBox();
	}
	@Override
	public Object getCellEditorValue() {
		return this.getCheckBox().isSelected();
	}
	
}
