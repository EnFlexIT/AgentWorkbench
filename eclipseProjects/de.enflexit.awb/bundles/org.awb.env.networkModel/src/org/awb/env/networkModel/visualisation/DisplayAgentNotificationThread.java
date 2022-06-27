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
package org.awb.env.networkModel.visualisation;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.messaging.GraphUIMessage;
import org.awb.env.networkModel.visualisation.notifications.DataModelNotification;
import org.awb.env.networkModel.visualisation.notifications.DisplayAgentNotificationGraph;
import org.awb.env.networkModel.visualisation.notifications.UIMessage;
import org.awb.env.networkModel.visualisation.notifications.UpdateDataSeries;

/**
 * The Class DisplayAgentNotificationThread.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DisplayAgentNotificationThread extends Thread {

	private DisplayAgentNotificationHandler displayNotificationHandler = null;
	private GraphEnvironmentController graphController = null;
	private boolean dispose = false;
	
	/**
	 * Instantiates a new display agent notification thread.
	 *
	 * @param displayNotificationHandler the display notification handler
	 * @param graphController the graph controller
	 */
	public DisplayAgentNotificationThread(DisplayAgentNotificationHandler displayNotificationHandler, GraphEnvironmentController graphController) {
		this.setName("DisplayAgentNotificationThread");
		this.displayNotificationHandler = displayNotificationHandler;
		this.graphController = graphController;
	}
	
	/**
	 * Marks the thread to be disposed.
	 */
	public void dispose() {
		this.dispose=true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
	
		while(true) {
			
			while (this.displayNotificationHandler.getDisplayNotificationStack().size()>0) {

				final DisplayAgentNotificationGraph displayNotification = this.displayNotificationHandler.getDisplayNotificationStack().remove(0);
				boolean proceed = true;

				// ------------------------------------------------------------				
				// --- Case DataModelNotification -----------------------------
				// --- Is the next notification for the same GraphElement? ----
				// ------------------------------------------------------------
				if (displayNotification instanceof DataModelNotification && this.displayNotificationHandler.getDisplayNotificationStack().size()>0) {
					
					DisplayAgentNotificationGraph displayNotificationNext = this.displayNotificationHandler.getDisplayNotificationStack().get(0);
					if (displayNotificationNext instanceof DataModelNotification) {
						// --- Compare if the two DataModelNotification ------- 
						// --- working on the same component			-------
						DataModelNotification firstDmNote = (DataModelNotification) displayNotification;
						DataModelNotification seconDmNote = (DataModelNotification) displayNotificationNext;
						if (firstDmNote.isNetworkComponentConfiguration()==true && seconDmNote.isNetworkComponentConfiguration()==true) {
							if (seconDmNote.isForNetworkComponent(firstDmNote.getNetworkComponent())==true){
								proceed = false;
							}
						} else if (firstDmNote.isGraphNodeConfiguration()==true && seconDmNote.isGraphNodeConfiguration()==true) {
							if (seconDmNote.isForGraphNode(firstDmNote.getGraphNode())==true) {
								proceed = false;
							}
						}
					}
				}
				// ------------------------------------------------------------
				
				// ------------------------------------------------------------
				if (proceed==true) {
					
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							@Override
							public void run() {
								if (displayNotification instanceof DataModelNotification) {
									sendDataModelUpdate(displayNotification);
								} else if (displayNotification instanceof UpdateDataSeries) {
									sendDataModelUpdate(displayNotification);
								} else if (displayNotification instanceof UIMessage) {
									sendUIMessage(displayNotification);
								} else {
									sendNetworkModelNotification(displayNotification);
								}
							}
						});
						
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					} catch (InvocationTargetException ite) {
						ite.printStackTrace();
					}
					
				} // end proceed==true
				// ------------------------------------------------------------
				
			} // end while for jobs

			// --- Exit while-loop ------
			if (this.dispose==true) break;
			
			synchronized (this.displayNotificationHandler.getDisplayNotificationStack()) {
				if (this.displayNotificationHandler.getDisplayNotificationStack().size()==0) {
					try {
						this.displayNotificationHandler.getDisplayNotificationStack().wait();
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
			}
			
			if (this.dispose==true) break;
						
		} // end endless while
		
	}

	/**
	 * Sends a NetworkModelNnotification in order to update the visualisation.
	 *
	 * @param graphController the graph controller
	 * @param displayNotification the display notification
	 */
	private void sendNetworkModelNotification(DisplayAgentNotificationGraph displayNotification) {
		NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Repaint);
		if (displayNotification!=null) {
			nmn.setInfoObject(displayNotification);
		}
		this.graphController.notifyObservers(nmn);
	}
	
	/**
	 * Sends a data model update.
	 *
	 * @param displayNotification the display notification
	 */
	private void sendDataModelUpdate(DisplayAgentNotificationGraph displayNotification) {
		
		// --- Avoid exceptions -----------------------------------------------
		if (displayNotification==null) return;
		if (this.graphController==null) return;
		if (this.graphController.getGraphEnvironmentControllerGUI()==null) return;
		
		// --- Place notification ---------------------------------------------
		if (displayNotification instanceof DataModelNotification) {
			// --- Put DataModelNotification to the display -------------------
			DataModelNotification dataModelNotification = (DataModelNotification) displayNotification; 
			this.graphController.getGraphEnvironmentControllerGUI().getBasicGraphGuiJDesktopPane().setDataModelNotification(dataModelNotification);
			
		} else if (displayNotification instanceof UpdateDataSeries) {
			// --- Put the Update of the DataSeries to the display ------------
			UpdateDataSeries uds = (UpdateDataSeries) displayNotification;
			this.graphController.getGraphEnvironmentControllerGUI().getBasicGraphGuiJDesktopPane().setUpdateDataSeries(uds);
		}
	}

	/**
	 * Sends an {@link GraphUIMessage} to the visualization.
	 * @param displayNotification the display notification
	 */
	private void sendUIMessage(DisplayAgentNotificationGraph displayNotification) {
		UIMessage uiMessage = (UIMessage) displayNotification;
		if (uiMessage.getGraphUIMessage()!=null) {
			this.graphController.getUiMessagingController().addMessage(uiMessage.getGraphUIMessage());
		}
	}
	
}
