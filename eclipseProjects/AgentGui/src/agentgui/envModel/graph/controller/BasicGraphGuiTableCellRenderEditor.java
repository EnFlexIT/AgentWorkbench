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
package agentgui.envModel.graph.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import agentgui.envModel.graph.GraphGlobals;

/**
 * The Class BasicGraphGuiTableCellRenderEditor is used in the table of 
 * NetworkComponent's in order to display or edit the name of single NetworkComponent.
 * 
 * @see BasicGraphGuiRootJSplitPane
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BasicGraphGuiTableCellRenderEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

	private static final long serialVersionUID = 1154095803299095301L;

	private JTextField jTextFieldEdit = null; 
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel jLabelDisplay = new JLabel("");
		if (value!=null) {
			jLabelDisplay.setText(value.toString());
		}
		jLabelDisplay.setFont(new Font("Dialog", Font.PLAIN, 12));
		GraphGlobals.Colors.setTableCellRendererColors(jLabelDisplay, row, isSelected);
		return jLabelDisplay;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return this.getJTextFieldEditor().getText();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.getJTextFieldEditor().setText(value.toString());
		GraphGlobals.Colors.setTableCellRendererColors(this.getJTextFieldEditor(), row, isSelected);
		return this.getJTextFieldEditor();
	}

	/**
	 * Gets the JTextField of the editor.
	 * @return the editor JTextField  
	 */
	private JTextField getJTextFieldEditor() {
		if (jTextFieldEdit==null) {
			jTextFieldEdit = new JTextField();
			jTextFieldEdit.setOpaque(true);
			jTextFieldEdit.setBorder(BorderFactory.createEmptyBorder());
			jTextFieldEdit.setBackground(new Color(0,0,0,0));
		}
		return jTextFieldEdit;
	}
	
}
