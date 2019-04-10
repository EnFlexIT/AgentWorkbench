/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
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
