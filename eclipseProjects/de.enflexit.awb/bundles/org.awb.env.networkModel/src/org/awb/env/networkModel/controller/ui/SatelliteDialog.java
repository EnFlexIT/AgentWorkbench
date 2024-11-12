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
package org.awb.env.networkModel.controller.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.Timer;

import org.awb.env.networkModel.controller.GraphEnvironmentController;

import de.enflexit.language.Language;
import agentgui.core.config.GlobalInfo;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;


/**
 * The SatelliteDialog for the graph and network environment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SatelliteDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -4309439744074827584L;

	private GraphEnvironmentController graphController;
	
	private Timer resizeWaitTimer;
	
	/**
	 * Instantiates a new satellite view.
	 *
	 * @param ownerFrame the owner frame
	 * @param graphEnvController GraphEnvironmentController
	 * @param basicGraphGui the BasicGraphGui
	 */
	public SatelliteDialog(Frame ownerFrame, GraphEnvironmentController graphEnvController, BasicGraphGui basicGraphGui) {
		super(ownerFrame);
		this.initialize(graphEnvController, basicGraphGui);
	}
	/**
	 * Instantiates a new satellite view.
	 *
	 * @param ownerDialog the owner dialog
	 * @param graphEnvController GraphEnvironmentController
	 * @param basicGraphGui the BasicGraphGui
	 */
	public SatelliteDialog(Dialog ownerDialog, GraphEnvironmentController graphEnvController, BasicGraphGui basicGraphGui) {
		super(ownerDialog);
		this.initialize(graphEnvController, basicGraphGui);
	}
	/**
	 * Initializes this dialog.
	 *
	 * @param graphEnvController the current GraphEnvironmentController
	 * @param basicGraphGui the current BasicGraphGui
	 */
	private void initialize(GraphEnvironmentController graphEnvController, BasicGraphGui basicGraphGui) {
		
		this.graphController = graphEnvController;
		
		this.setTitle(Language.translate("Übersicht"));
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());

		this.add(basicGraphGui.getSatelliteVisualizationViewer());
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				graphController.getNetworkModelUndoManager().setSatelliteView(false);
			}
		});
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent ce) {
				if (getResizeWaitTimer().isRunning()==true) {
					SatelliteDialog.this.getResizeWaitTimer().restart();
				} else {
					SatelliteDialog.this.getResizeWaitTimer().start();
				}
			}
		});
		
		this.setSize(200, 150);
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ScreenTopLeft);
		
	}
	
	/**
	 * Returns the resize wait timer.
	 * @return the resize wait timer
	 */
	private Timer getResizeWaitTimer() {
		if (resizeWaitTimer==null) {
			resizeWaitTimer = new Timer(250, this);
			resizeWaitTimer.setRepeats(false);
		}
		return resizeWaitTimer;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getResizeWaitTimer()) {
			graphController.getNetworkModelUndoManager().setSatelliteView(true);
		}
	}
	
} 
	
