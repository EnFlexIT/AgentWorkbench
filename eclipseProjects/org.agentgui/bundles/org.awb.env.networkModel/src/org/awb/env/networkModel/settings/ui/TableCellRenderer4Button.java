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

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.enflexit.common.swing.TableCellColorHelper;

/**
 * Is used in the {@link ComponentTypeDialog} for rendering JButton.
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class TableCellRenderer4Button implements TableCellRenderer {
	
	private JButton button;

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.getJButton().setText("");
		if (value!=null) {
			this.getJButton().setText(value.toString());
		}
		TableCellColorHelper.setTableCellRendererColors(this.getJButton(), row, isSelected);
		return this.getJButton();
	}
	
	 /**
     * Returns the JBbutton.
     * @return the JButton
     */
    private JButton getJButton() {
    	if (button==null) {
    		button = new JButton();
    		//button.setUI(new BasicButtonUI());
    		//button.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    	}
    	return button;
    }
}
