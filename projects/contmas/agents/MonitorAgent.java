/**
 * @author Hanno - Felix Wagner, 27.05.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.agents;

import javax.swing.JDesktopPane;

import contmas.behaviours.subscribeToDF;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.OntRepRequester;
import contmas.main.ControlGUI;
import contmas.main.MonitorGUI;
import contmas.ontology.ContainerHolder;
import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.util.leap.List;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class MonitorAgent extends GuiAgent implements OntRepRequester,DFSubscriber{

	private MonitorGUI myGui;
	private JDesktopPane canvas;

	/**
	 * @param canvas 
	 * 
	 */
	public MonitorAgent(JDesktopPane canvas){
		this.canvas=canvas;
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup(){
		super.setup();
		this.myGui=new MonitorGUI(this,canvas);
		this.myGui.setVisible(true);
		this.addBehaviour(new subscribeToDF(this,"container-handling"));

	}

	/* (non-Javadoc)
	 * @see jade.gui.GuiAgent#onGuiEvent(jade.gui.GuiEvent)
	 */
	@Override
	protected void onGuiEvent(GuiEvent ev){
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.OntRepRequester#processOntologyRepresentation(contmas.ontology.ContainerHolder, jade.core.AID)
	 */
	@Override
	public void processOntologyRepresentation(ContainerHolder recieved,AID agent){
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		this.updateAgentTree(updatedAgents,remove);
	}
	
	public void updateAgentTree(List newAgents,Boolean remove){
		this.myGui.updateAgentTree(newAgents,remove);
	}

}
