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
package gasmas.agents.manager;

import gasmas.clustering.EdgeBetweenessBehaviour;
import gasmas.clustering.ShortestPathBlackboard;
import jade.core.ServiceException;

import java.util.ArrayList;

import agentgui.core.application.Application;
import agentgui.core.project.Project;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModelDiscrete;

/**
 * The Class NetworManagerAgent.
 */
public class NetworkManagerAgent extends SimulationManagerAgent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1823164338744218569L;

    /** The curr project. */
    private Project currProject = null;

    /** The my network model. */
    private NetworkModel myNetworkModel = null;

    /** The active network component agent class prefix. */
    private final String activeNetworkComponentAgentClassPrefix = "gasmas.agents.components.";

    /** The active network component agent classes. */
    private final String[] activeNetworkComponentAgentClasses = new String[] {
	    "CompressorAgent", "EntryAgent", "ExitAgent", "StorageAgent" };

    /** The active network components. */
    private ArrayList<NetworkComponent> activeNetworkComponents = new ArrayList<NetworkComponent>();

    private ShortestPathBlackboard shortestPathBlackboard = new ShortestPathBlackboard();

    /*
     * (non-Javadoc)
     * 
     * @see agentgui.simulationService.agents.SimulationManagerAgent#setup()
     */
    @Override
    protected void setup() {
	super.setup();

	// --- Get the connection to the current project ------------
	currProject = Application.ProjectCurr;
	if (currProject == null) {
	    takeDown();
	}

	// --- Get the initial environment model --------------------
	this.envModel = this.getInitialEnvironmentModel();
	// --- Remind the current network model ---------------------
	this.myNetworkModel = (NetworkModel) this.getDisplayEnvironment();

	// ++++++++++++++ Just to see if this feature can work +++++++++++
	this.testAlternativeModels();
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// --- Put the environment model into the SimulationService -
	// --- in order to make it accessible for the whole agency --
	try {
	    simHelper.setEnvironmentModel(this.envModel, true);
	} catch (ServiceException e) {
	    e.printStackTrace();
	}

	// --- Start working ----------------------------------------
	identifyActiveComponents();
	behaviours();

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++ Some temporary test cases here +++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /**
     * Test alternative models.
     */
    private void testAlternativeModels() {

	NetworkModel nmCopy1 = this.myNetworkModel.getCopy();
	NetworkModel nmCopy2 = this.myNetworkModel.getCopy();
	nmCopy2.getAlternativeNetworkModel().put("Sub Alternative of Alternative 2", nmCopy1.getCopy());

	this.myNetworkModel.getAlternativeNetworkModel().put("Alternative 1", nmCopy1);
	this.myNetworkModel.getAlternativeNetworkModel().put("Alternative 2", nmCopy2);
	this.setDisplayEnvironment(this.myNetworkModel);

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /*
     * (non-Javadoc)
     * 
     * @see agentgui.simulationService.agents.SimulationManagerInterface#
     * getInitialEnvironmentModel()
     */
    @Override
    public EnvironmentModel getInitialEnvironmentModel() {

	int currentlyDoing = 0;
	final int GET_EnvCont = 1;
	final int GET_NetworkModel = 2;

	EnvironmentModel envModel = new EnvironmentModel();
	TimeModelDiscrete myTimeModel = new TimeModelDiscrete(new Long(
		1000 * 60));

	GraphEnvironmentController envController = null;
	NetworkModel networkModel = null;

	try {
	    currentlyDoing = GET_EnvCont;
	    envController = (GraphEnvironmentController) currProject
		    .getEnvironmentController();

	    currentlyDoing = GET_NetworkModel;
	    networkModel = (NetworkModel) envController
		    .getEnvironmentModelCopy();

	    envModel.setTimeModel(myTimeModel);
	    envModel.setDisplayEnvironment(networkModel);

	} catch (Exception ex) {

	    String msg = null;
	    switch (currentlyDoing) {
	    case GET_EnvCont:
		msg = ": Could not get GraphEnvironmentController!";
		break;
	    case GET_NetworkModel:
		msg = ": Could not get NetworkModel!";
		break;
	    }

	    if (msg == null) {
		ex.printStackTrace();
	    } else {
		System.err.println(this.getLocalName() + ": " + msg);
	    }
	    return null;

	}
	return envModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see agentgui.simulationService.agents.SimulationManagerInterface#
     * doSingleSimulationSequennce()
     */
    @Override
    public void doSingleSimulationSequennce() {

    }

    /**
     * Adds Behaviours.
     */
    private void behaviours() {
	this.addBehaviour(new EdgeBetweenessBehaviour(envModel));
	// Shortest Path Analysis
	for (NetworkComponent networkComponent : activeNetworkComponents) {
	    // TODO: adding the ShortestPathBehaviour to activeCompnent
	}
    }

    /**
     * Identify active components.
     */
    private void identifyActiveComponents() {
	
    	for (NetworkComponent networkComponent : myNetworkModel.getNetworkComponents().values()) {
		    String agentClassName = networkComponent.getAgentClassName();
		    for (String activeAgentClassType : activeNetworkComponentAgentClasses) {
				if (agentClassName.equals(activeNetworkComponentAgentClassPrefix + activeAgentClassType)) {
				    activeNetworkComponents.add(networkComponent);
				}
		    }
    	}

    }

    /**
     * Gets the active network components.
     * 
     * @return the active network components
     */
    public ArrayList<NetworkComponent> getActiveNetworkComponents() {
	return activeNetworkComponents;
    }

    public ShortestPathBlackboard getShortestPathBlackboard() {
	return shortestPathBlackboard;
    }
}
