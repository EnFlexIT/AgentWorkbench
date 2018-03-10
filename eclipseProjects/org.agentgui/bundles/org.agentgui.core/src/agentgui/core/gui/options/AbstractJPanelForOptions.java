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

import javax.swing.JPanel;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.ExecutionMode;

/**
 * The Class AbstractJPanelForOptions is used as super class for
 * the configuration of specific global information, as for example for
 * the server.master configuration, database settings and other.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractJPanelForOptions extends JPanel {

	private static final long serialVersionUID = -4522581044585166846L;

	/** The current instance of the {@link OptionDialog}. */
	protected OptionDialog optionDialog;
	/** The current parent panel of the {@link StartOptions} */
	protected StartOptions startOptions;
	
	private GlobalInfo globalInfo;
	
	/**
	 * Required constructor for the usage of this abstract class.
	 *
	 * @param optionDialog the option dialog
	 * @param startOptions the start options
	 */
	public AbstractJPanelForOptions(OptionDialog optionDialog, StartOptions startOptions) {
		this.optionDialog = optionDialog;
		this.optionDialog.registerOptionPanel(this);
		this.startOptions = startOptions;
	}
	
	/**
	 * Returns the global info.
	 * @return the global info
	 */
	protected GlobalInfo getGlobalInfo() {
		if (globalInfo==null) {
			globalInfo = Application.getGlobalInfo();
		}
		return globalInfo;
	}
	
	/**
	 * This method has to set the data from the global area to the current Form.
	 */
	public abstract void setGlobalData2Form();
	
	/**
	 * This method writes the data back from the form to the global area.
	 */
	public abstract void setFormData2Global();
	
	/**
	 * This method doe's the Error-Handling for this Dialog.
	 * @return true or false
	 */
	public abstract boolean errorFound();

	/**
	 * Should refresh the view according to the current settings.
	 */
	public abstract void refreshView();
	
	/**
	 * Will be invoked, if the dialog is closing. Overwrite this 
	 * method, if you have to react on this event.
	 */
	public void doDialogCloseAction() { }
	
	/**
	 * Gets the selected execution mode.
	 * @return the selected execution mode
	 */
	protected ExecutionMode getSelectedExecutionMode() {
		return this.startOptions.getSelectedExecutionMode();
	}
	
}
