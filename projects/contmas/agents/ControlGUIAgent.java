package contmas.agents;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.Random;

import javax.swing.JDesktopPane;

import contmas.ontology.*;

import application.Application;
import application.Project;


public class ControlGUIAgent extends GuiAgent{
    private ControlGUI myGui = null;

	protected void setup(){ 
		// Instanciate the gui
		myGui = new ControlGUI(this);
		myGui.setVisible(true);
		// ------------------------------------------------
		// --- Hier gab es eine Änderung von Christian ----
		Project CurrPro = Application.Projects.get("contmas");
		JDesktopPane desktop = CurrPro.ProjectDesktop;
		// --- Hier gab es eine Änderung von Christian ----
		// ------------------------------------------------
		desktop.add(myGui);
		
        AgentContainer c = getContainerController();
        try {
            AgentController a = c.createNewAgent( "RandomGenerator", "contmas.agents.RandomGeneratorAgent", null );
            a.start();
            a = c.createNewAgent( "HarborMaster", "contmas.agents.HarborMasterAgent", null );
            a.start();
        }
        catch (Exception e){
        	
        }
	}
	
	protected void onGuiEvent(GuiEvent ev) {
		int command = ev.getType();
		if (command == 1) {
	        AgentContainer c = getContainerController();
	        try {
	        	String name=ev.getParameter(0).toString();
//	            AgentController a = c.createNewAgent(name , "contmas.agents.ShipAgent", null );
		        Ship ontologyRepresentation=new Ship();
				Domain habitat = new Sea();
		        ontologyRepresentation.setLives_in(habitat);
		        if(ev.getParameter(1).toString()!="" && ev.getParameter(1).toString()!="" && ev.getParameter(1).toString()!=""){
			        BayMap loadBay=new BayMap();
			        loadBay.setX_dimension(Integer.parseInt(ev.getParameter(1).toString()));
			        loadBay.setY_dimension(Integer.parseInt(ev.getParameter(2).toString()));
			        loadBay.setZ_dimension(Integer.parseInt(ev.getParameter(3).toString()));
			        ontologyRepresentation.setContains(loadBay);
		        }
//				habitat.setLies_in(terminalArea);
	            AgentController a=c.acceptNewAgent(name, new ShipAgent(ontologyRepresentation));

	            a.start();
	        }
	        catch (Exception e){}
		} else if (command == -1) {
	         doDelete();
	         //System.exit(0);
		}
		
	}
	
	protected void takeDown () {
		myGui.dispose();
	}
	 
}