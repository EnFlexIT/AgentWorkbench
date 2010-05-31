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

import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.util.leap.List;

import java.util.HashMap;

import javax.swing.JDesktopPane;

import contmas.behaviours.requestOntologyRepresentation;
import contmas.behaviours.subscribeToDF;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.OntRepRequester;
import contmas.main.AgentView;
import contmas.main.MonitorGUI;
import contmas.ontology.ContainerHolder;

/**
 * @author Hanno - Felix Wagner
 * 
 */
public class MonitorAgent extends GuiAgent implements OntRepRequester,	DFSubscriber {

	public static final Integer REFRESH = 0;
	private MonitorGUI myGui;
	private JDesktopPane canvas;
	private AID harbourMaster;

	/**
	 * @param canvas
	 * 
	 */
	public MonitorAgent(JDesktopPane canvas) {
		this.canvas = canvas;
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		this.myGui = new MonitorGUI(this, canvas);
		this.myGui.setVisible(true);
		ContainerAgent.enableForCommunication(this);
		this.addBehaviour(new subscribeToDF(this, "container-handling"));
		this.harbourMaster=ContainerAgent.getFirstAIDFromDF("harbor-managing",this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jade.gui.GuiAgent#onGuiEvent(jade.gui.GuiEvent)
	 */
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		if (ev.getType() == REFRESH) {
			AID subject=(AID) ev.getParameter(0);
//			System.out.println("Refreshing view of "+subject.getLocalName());
			this.addBehaviour(new requestOntologyRepresentation(this,subject,this.harbourMaster));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * contmas.interfaces.OntRepRequester#processOntologyRepresentation(contmas
	 * .ontology.ContainerHolder, jade.core.AID)
	 */
	@Override
	public void processOntologyRepresentation(ContainerHolder recieved,	AID agent) {
		HashMap<AID, AgentView> monitoredAgents = myGui.getMonitoredAgents();
		if(monitoredAgents.containsKey(agent)){
			AgentView view = monitoredAgents.get(agent);
			view.updateOntRep(recieved);
		} else{
			myGui.monitorAgent(agent,recieved);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * contmas.interfaces.DFSubscriber#processSubscriptionUpdate(jade.util.leap
	 * .List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents, Boolean remove) {
		this.updateAgentTree(updatedAgents, remove);
	}

	public void updateAgentTree(List newAgents, Boolean remove) {
		this.myGui.updateAgentTree(newAgents, remove);
	}
}
