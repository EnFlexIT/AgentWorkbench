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
package agentgui.core.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * The Class JPanelForActions is a normal JPanel that is able
 * to fire ActionEvents, so you can add ActionListener to it.
 */
public class JPanelForActions extends JPanel implements MouseListener {
		
	private static final long serialVersionUID = -4592535323022211528L;
	
	private ArrayList<ActionListener> listener = null;
	private boolean pauseFireUpdates = false;
	
	/**
	 * Instantiates a new JPanel that can fire ActionEvents.
	 */
	public JPanelForActions() {
		listener = new ArrayList<ActionListener>();
		addMouseListener(this);
	}
	
	/**
	 * Fire update.
	 * @param evt the ActionEvent
	 */
	protected void fireUpdate(ActionEvent evt) {
		if (isPauseFireUpdates()==false) {
			for (ActionListener al : listener) {
				al.actionPerformed(evt);
			}	
		}
	}
	/**
	 * @return the pauseFireUpdates
	 */
	protected boolean isPauseFireUpdates() {
		return pauseFireUpdates;
	}
	/**
	 * @param pauseFireUpdates the pauseFireUpdates to set
	 */
	protected void setPauseFireUpdates(boolean pauseFireUpdates) {
		this.pauseFireUpdates = pauseFireUpdates;
	}

	/**
	 * Adds the action listener.
	 * @param al the ActionListener
	 */
	public void addActionListener(ActionListener al) {
		listener.add(al);
	}
	/**
	 * Removes the action listener.
	 * @param al the ActionListener
	 */
	public void removeActionListener(ActionListener al) {
		listener.remove(al);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent evt) {
		fireUpdate(new ActionEvent(this, 0, "command"));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent evt) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent evt) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent evt) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent evt) {}
		
}
