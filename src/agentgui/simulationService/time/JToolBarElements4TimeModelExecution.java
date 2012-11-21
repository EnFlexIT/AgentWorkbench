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
package agentgui.simulationService.time;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JToolBar;

/**
 * The Class JToolBarElements4TimeModelExecution has to be extended in order to
 * provide a specific display for a TimeModel during the execution of an agency.
 * 
 * @see TimeModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public abstract class JToolBarElements4TimeModelExecution implements ActionListener {

	private static final long serialVersionUID = 4966720402773236025L;

	/** This is the JToolBar to which your elements can be added !*/
	protected JToolBar jToolBar4Elements = null;
	private Vector<JToolBar.Separator> separatorVector=null;
	
	/**
	 * Sets the TimeModel.
	 * @param timeModel the new TimeModel
	 */
	public abstract void setTimeModel(TimeModel timeModel);
	/**
	 * Returns the TimeModel.
	 * @return the TimeModell
	 */
	public abstract TimeModel getTimeModel();
	
	
	/**
	 * Adds the custom toolbar elements.
	 * @param jToolBar2AddElements 
	 */
	public void addToolbarElements(JToolBar jToolBar2AddElements) {
		this.jToolBar4Elements = jToolBar2AddElements;
		this.addToolbarElements();
	}
	/**
	 * Adds the custom toolbar elements.
	 */
	public abstract void addToolbarElements();
	
	/**
	 * Removes the custom toolbar elements.
	 */
	public abstract void removeToolbarElements();
	

	/**
	 * Returns a reminded separator, so that separators can be removed later on.
	 * @param numberOfSeperator the number of separator
	 * @return the separator
	 */
	protected JToolBar.Separator getSeparator(int numberOfSeperator) {
		if (separatorVector==null) {
			separatorVector = new Vector<JToolBar.Separator>();
		}
		if (separatorVector.size()<numberOfSeperator) {
			for (int i = 0; i < numberOfSeperator; i++) {
				separatorVector.add(new JToolBar.Separator());
			}
		}
		return this.separatorVector.get(numberOfSeperator-1);
	}
	
	/**
	 * Removes all separator.
	 */
	protected  void removeAllSeparator() {
		if (this.separatorVector!=null) {
			for (int i = 0; i < this.separatorVector.size(); i++) {
				this.jToolBar4Elements.remove(this.separatorVector.get(i));
			}
			this.separatorVector=null;
		}
	}
}
