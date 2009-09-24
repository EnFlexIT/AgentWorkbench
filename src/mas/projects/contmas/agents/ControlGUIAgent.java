package mas.projects.contmas.agents;
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

import application.Application;

import mas.projects.contmas.ontology.*;

public class ControlGUIAgent extends GuiAgent{
    AID[] randomGenerators=null;
    private ControlGUI myGui = null;
    
    
	protected void setup(){ 
		// Instanciate the gui
		myGui = new ControlGUI(this);
		myGui.setVisible(true);
		JDesktopPane desktop=Application.MainWindow.ProjectDesktop;
		desktop.add(myGui);
        AgentContainer c = getContainerController();
        try {
            AgentController a = c.createNewAgent( "RandomGenerator", "mas.projects.contmas.agents.RandomGeneratorAgent", null );
            a.start();
            a = c.createNewAgent( "HarborMaster", "mas.projects.contmas.agents.HarborMasterAgent", null );
            a.start();

            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd  = new ServiceDescription();
            sd.setType( "random-generation" );
            dfd.addServices(sd);
            try {
				DFAgentDescription[] result = DFService.search(this, dfd);
				randomGenerators= new AID[result.length];
				for (int i = 0; i < result.length; i++) {
					randomGenerators[i]=result[i].getName();
				}
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        catch (Exception e){}
	}
	
	protected void onGuiEvent(GuiEvent ev) {
		// TODO Auto-generated method stub
		int command = ev.getType();
		if (command == 1) {
	        AgentContainer c = getContainerController();
	        try {
	        	String name=ev.getParameter(0).toString();
	            AgentController a = c.createNewAgent(name , "mas.projects.contmas.agents.ShipAgent", null );
	            a.start();
				ACLMessage sndMsg = new ACLMessage(ACLMessage.REQUEST);
				sndMsg.addUserDefinedParameter("article","bayMap");
				sndMsg.addUserDefinedParameter("dimensionX", ev.getParameter(1).toString());
				sndMsg.addUserDefinedParameter("dimensionY", ev.getParameter(2).toString());
				sndMsg.addUserDefinedParameter("dimensionZ", ev.getParameter(3).toString());
				sndMsg.addReceiver(randomGenerators[0]);
				send(sndMsg);
	        }
	        catch (Exception e){}
		} else if (command == -1) {
	         doDelete();
	         System.exit(0);
		}
		
	}
	
	protected void takeDown () {
		myGui.dispose();
	}
	 
}