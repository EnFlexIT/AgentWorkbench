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
package agentgui.envModel.graph.networkModel;

import jade.util.leap.Serializable;

/**
 * The Class LayoutSettings describes how a NetworkModel should be displayed and handled.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class LayoutSettings implements Serializable, Cloneable{

	private static final long serialVersionUID = 7769992668898387964L;

	
	
	/**
	 * Instantiates new layout settings.
	 */
	public LayoutSettings() { }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null) return false;
		if (! (compareObject instanceof LayoutSettings)) return false;
		
		LayoutSettings ls2Compare = (LayoutSettings) compareObject;
		// TODO 
		boolean isEqual = true;
//		if (isEqual==true) isEqual = (this.isShowLabel()==ds2Compare.isShowLabel());
//		if (isEqual==true) isEqual = GeneralGraphSettings4MAS.isEqualString(this.getClusterShape(), ds2Compare.getClusterShape());
		return isEqual;
	}

	/**
	 * Returns a copy of the current instance.
	 * @return the copy
	 */
	public LayoutSettings getCopy() {
		LayoutSettings copy = new LayoutSettings();
		// TODO 		
		
		return copy;
	}
	
}
