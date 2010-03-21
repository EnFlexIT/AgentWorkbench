/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.agents;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import javax.swing.JDesktopPane;

import contmas.behaviours.prepareSubscribeToDF;
import contmas.main.AgentDesktop;
import contmas.main.ControlGUI;
import contmas.ontology.BayMap;
import contmas.ontology.Domain;
import contmas.ontology.Sea;
import contmas.ontology.Ship;

public class ControlGUIAgent extends GuiAgent{

	class listenForLogMessage extends CyclicBehaviour{
		/**
		 * 
		 */
		private static final long serialVersionUID=3580181910527477281L;
		MessageTemplate loggingTemplate=MessageTemplate.MatchTopic(((ControlGUIAgent) this.myAgent).loggingTopic);

		/**
		 * @param controlGUIAgent
		 */
		public listenForLogMessage(ControlGUIAgent myCAgent){
			super(myCAgent);
		}

		/**
		 * 
		 */

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action(){
			ACLMessage logMsg=ControlGUIAgent.this.receive(this.loggingTemplate);
			if(logMsg != null){
				String content=logMsg.getContent();
				((ControlGUIAgent) this.myAgent).writeLogMsg(content);
			}else{
				this.block();
			}

		}
	}

	/*
	class refreshAgentTree extends CyclicBehaviour{


		@Override
		public void action(){
			ACLMessage logMsg=ControlGUIAgent.this.receive(this.loggingTemplate);
			if(logMsg != null){
				String content=logMsg.getContent();
				((ControlGUIAgent) this.myAgent).writeLogMsg(content);
			}else{
				this.block();
			}

		}
	}
	*/
	private static final long serialVersionUID= -7176620366025244274L;
	private JDesktopPane canvas=null;
	private ControlGUI myGui=null;
	private TopicManagementHelper tmh;
	private AID loggingTopic=null;

	@Override
	protected void onGuiEvent(GuiEvent ev){
		int command=ev.getType();
		if(command == 1){
			AgentContainer c=this.getContainerController();
			try{
				String name=ev.getParameter(0).toString();
				//	            AgentController a = c.createNewAgent(name , "contmas.agents.ShipAgent", null );
				Ship ontologyRepresentation=new Ship();
				Domain habitat=new Sea();
				ontologyRepresentation.setLives_in(habitat);
				if((ev.getParameter(1).toString() != "") && (ev.getParameter(1).toString() != "") && (ev.getParameter(1).toString() != "")){
					BayMap loadBay=new BayMap();
					loadBay.setX_dimension(Integer.parseInt(ev.getParameter(1).toString()));
					loadBay.setY_dimension(Integer.parseInt(ev.getParameter(2).toString()));
					loadBay.setZ_dimension(Integer.parseInt(ev.getParameter(3).toString()));
					ontologyRepresentation.setContains(loadBay);
					ontologyRepresentation.setLength(Float.parseFloat(ev.getParameter(3).toString()));
				}
				//				habitat.setLies_in(terminalArea);
				AgentController a=c.acceptNewAgent(name,new ShipAgent(ontologyRepresentation));

				a.start();
			}catch(Exception e){
			}
		}else if(command == -1){
			this.doDelete();
			//System.exit(0);
		}

	}

	@Override
	protected void setup(){
		// Instanciate the gui

		/*
		Object[] args=this.getArguments();
		if((args != null) && (args[0] instanceof JDesktopPane)){
			this.canvas=(JDesktopPane) args[0];
		}
		if(this.canvas == null){
			ContMASContainer CMC=new ContMASContainer();
			CMC.setVisible(true);
			this.canvas=CMC.getJDesktopPane();
		}*/

		this.myGui=new ControlGUI(this);

		AgentDesktop ad=new AgentDesktop("ContMAS");
		this.canvas=ad.getDesktopPane();
		this.myGui.displayOn(this.canvas);
//		this.canvas.add(this.myGui);
		this.myGui.setVisible(true);
		if(ad.getStandaloneMode() == AgentDesktop.AGENTDESKTOP_STANDALONE){
			this.myGui.getCanvas().getParent().getParent().getParent().getParent().setSize(this.myGui.getWidth() + 10,this.myGui.getHeight() + 30);
/*
			try{
				this.myGui.setMaximum(true);
			}catch(PropertyVetoException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
*/
		}

		try{
			this.tmh=(TopicManagementHelper) this.getHelper(TopicManagementHelper.SERVICE_NAME);
			this.loggingTopic=this.tmh.createTopic("container-harbour-logging");
			this.tmh.register(this.loggingTopic);
		}catch(ServiceException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.addBehaviour(new listenForLogMessage(this));

		DFAgentDescription dfd=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType("container-handling");
//		sd.setType("short-time-storage");
		dfd.addServices(sd);

		Behaviour DFsubscribePreparer=null;
		try{
			DFsubscribePreparer=new prepareSubscribeToDF(this,this.getClass().getMethod("updateAgentTree",List.class),dfd);
		}catch(SecurityException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(NoSuchMethodException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		DFsubscribePreparer=new prepareSubscribeToDF(this,allAvailableAgents,dfd);
		this.addBehaviour(DFsubscribePreparer);

//		this.addBehaviour(new prepareSubscribeToDF(this, allAvailableAgents, "container-handling"));

		AgentContainer c=this.getContainerController();
		try{
			AgentController a=c.createNewAgent("RandomGenerator","contmas.agents.RandomGeneratorAgent",null);
			a.start();
			a=c.createNewAgent("HarborMaster","contmas.agents.HarborMasterAgent",null);
			a.start();
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void takeDown(){
		this.myGui.dispose();
		try{
			this.tmh.deregister(this.loggingTopic); //does it work?
		}catch(ServiceException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateAgentTree(List newAgents){
		this.myGui.updateAgentTree(newAgents);
	}

	/**
	 * @param content
	 */
	public void writeLogMsg(String content){
		this.myGui.writeLogMsg(content);
	}

}