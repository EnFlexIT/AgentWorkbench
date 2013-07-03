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

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;

/**
 * The Class SatelliteView.
 */
public class SatelliteView extends JDialog {

	private static final long serialVersionUID = -4309439744074827584L;

	private final String pathImage = GraphGlobals.getPathImages();
	private final ImageIcon iconAgentGUI = new ImageIcon(this.getClass().getResource(pathImage + "AgentGUI.png"));
	private final Image imageAgentGUI = iconAgentGUI.getImage();
	
	private GraphEnvironmentController graphController = null;
	private BasicGraphGui basicGraphGui = null;
	
	/**
	 * Instantiates a new satellite view.
	 *
	 * @param graphEnvController GraphEnvironmentController
	 * @param basicGraphGui the BasicGraphGui
	 */
	public SatelliteView(GraphEnvironmentController graphEnvController, BasicGraphGui basicGraphGui) {
		
		super();
		this.basicGraphGui = basicGraphGui;
		this.graphController = graphEnvController;
		
		Dimension dim = (Dimension) this.basicGraphGui.getVisViewSatelliteDimension().clone();
		dim.setSize(dim.getWidth(), dim.getHeight()+50);
		this.setSize(dim);
		this.setIconImage(imageAgentGUI);
		this.setAlwaysOnTop(true);
		
		this.add(this.basicGraphGui.getVisViewSatellite());
		this.setTitle(Language.translate("Übersicht"));
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				graphController.getNetworkModelAdapter().setSatelliteView(false);
			}
		});
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
	
