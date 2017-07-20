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

/**
 * The Class NetworkModelNotification.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkModelNotification {

	/** Possible reasons. */
	public static final int NETWORK_MODEL_ComponentTypeSettingsChanged = 1;
	
	public static final int NETWORK_MODEL_Reload = 2;
	public static final int NETWORK_MODEL_Repaint = 3;
	
	public static final int NETWORK_MODEL_Satellite_View = 4;
	
	public static final int NETWORK_MODEL_Zoom_In = 5;
	public static final int NETWORK_MODEL_Zoom_Out = 6;
	public static final int NETWORK_MODEL_Zoom_Fit2Window = 7;
	public static final int NETWORK_MODEL_Zoom_One2One = 8;
	public static final int NETWORK_MODEL_Zoom_Component = 9;
	
	public static final int NETWORK_MODEL_Merged_With_Supplement_NetworkModel = 10;
	
	public static final int NETWORK_MODEL_Component_Added = 11;
	public static final int NETWORK_MODEL_Component_Removed = 12;
	public static final int NETWORK_MODEL_Component_Renamed = 13;
	public static final int NETWORK_MODEL_Component_Select = 14;
	
	public static final int NETWORK_MODEL_Nodes_Moved = 15;
	public static final int NETWORK_MODEL_Nodes_Merged = 16;
	public static final int NETWORK_MODEL_Nodes_Splited = 17;
	
	public static final int NETWORK_MODEL_GraphMouse_Picking = 20;
	public static final int NETWORK_MODEL_GraphMouse_Transforming = 21;
	public static final int NETWORK_MODEL_ShowPopUpMenue = 22;
	
	public static final int NETWORK_MODEL_Paste_Action_Do = 23;
	public static final int NETWORK_MODEL_Paste_Action_Stop = 24;
	
	public static final int NETWORK_MODEL_EditComponentSettings = 25;
	
	public static final int NETWORK_MODEL_ExportGraphAsImage = 30;
	
	public static final int NETWORK_MODEL_AddedCustomToolbarComponentDescription = 40;
	
	/** The reason. */
	private int reason = 0;
	
	/** The info object, which come with the notification. */
	private Object infoObject = null;
	
	
	/**
	 * Instantiates a new network model notification.
	 * @param reason the reason
	 */
	public NetworkModelNotification(int reason) {
		this.setReason(reason);
	}

	/**
	 * Sets the reason for the notification.
	 * @param reason the new reason
	 */
	public void setReason(int reason) {
		this.reason = reason;
	}
	/**
	 * Returns the reason of the notification.
	 * @return the reason
	 */
	public int getReason() {
		return reason;
	}

	/**
	 * Sets the info object, which comes with the notification.
	 * @param infoObject the new info object
	 */
	public void setInfoObject(Object infoObject) {
		this.infoObject = infoObject;
	}
	/**
	 * Returns the info object, which comes with the notification.
	 * @return the info object
	 */
	public Object getInfoObject() {
		return infoObject;
	}
	
}
