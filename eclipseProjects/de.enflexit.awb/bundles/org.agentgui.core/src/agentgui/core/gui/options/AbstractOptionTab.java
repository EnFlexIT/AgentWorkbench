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
package agentgui.core.gui.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * The Class AbstractOptionTab.
 */
public abstract class AbstractOptionTab extends JPanel implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 210514550187553667L;
	protected OptionDialog optionDialog;

	
	/**
	 * Instantiates a new abstract option tab.
	 */
	public AbstractOptionTab() {
		super();
	}
	/**
	 * Instantiates a new abstract option tab.
	 * @param optionDialog the option dialog
	 */
	public AbstractOptionTab(OptionDialog optionDialog) {
		super();
		this.optionDialog=optionDialog;

	}
	
	/**
	 * Returns the title addition for the {@link OptionDialog}.
	 * @return the title addition
	 */
	public abstract String getTitle();
	
	/**
	 * Returns the tool tip text.
	 * @return the tool tip text
	 */
	public abstract String getTabToolTipText();
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
