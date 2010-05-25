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

import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.SniffOn;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;

import contmas.behaviours.*;
import contmas.interfaces.DFSubscriber;
import contmas.interfaces.HarbourLayoutRequester;
import contmas.interfaces.Logger;
import contmas.interfaces.OntRepRequester;
import contmas.main.*;
import contmas.ontology.*;

public class ControlGUIAgent extends GuiAgent implements OntRepRequester,DFSubscriber,HarbourLayoutRequester,Logger{

	private Domain harbourMap;
	
	@Override
	public void processLogMsg(String logMsg){
		writeLogMsg(logMsg);
	}

	private String workingDir="";

	public String getWorkingDir(){
		return workingDir;
	}

	public void setWorkingDir(String workingDir){
		this.workingDir=workingDir;
	}

	@Override
	public void processOntologyRepresentation(ContainerHolder ontRep,AID agent){
		if((ontRep == null) || (ontRep.getContains() == null)){
			return;
		}
		XMLCodec xmlCodec=new XMLCodec();
		String s;
		try{
			s=xmlCodec.encodeObject(ContainerTerminalOntology.getInstance(),ontRep,true);
			this.myGui.printOntRep(s);
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.myGui.setOntRepAgent(agent.getLocalName());
		DefaultListModel containerList=new DefaultListModel();
		Iterator allContStates=ontRep.getAllContainer_states();
		while(allContStates.hasNext()){
			TOCHasState contState=(TOCHasState) allContStates.next();
			String administeredContBic=contState.getSubjected_toc().getTransports().getBic_code();
			String dispString=administeredContBic;
			dispString+=" - " + contState.getState().getClass().getSimpleName();
			if(contState.getState() instanceof Holding){
				BlockAddress curCont=((Holding)contState.getState()).getAt_address();
				dispString+=" ("+Const.blockAddressToString(curCont)+")";
			} else{
				dispString+=" [NotHeld]";
			}
			containerList.addElement(dispString);
		}
		this.myGui.setContainerList(containerList);
	}

	private static final long serialVersionUID= -7176620366025244274L;
	private JDesktopPane canvas=null;
	public ControlGUI myGui=null;
	private AID harbourMaster=null;

	private AID sniffer=null;

	@Override
	protected void onGuiEvent(GuiEvent ev){
		int command=ev.getType();
		if(command == 1){ //create new ship
			this.getContainerController();
			try{
				String name=ev.getParameter(0).toString();
				//	            AgentController a = c.createNewAgent(name , "contmas.agents.ShipAgent", null );
				Class<?> selectedContainerHolderType=(Class<?>) ev.getParameter(7);
				ContainerHolder ontologyRepresentation=(ContainerHolder) selectedContainerHolderType.newInstance();
				if(ev.getParameter(8) instanceof BayMap){ //Loaded BayMap present
					ontologyRepresentation.setContains((BayMap) ev.getParameter(8));
				}else{
					if((ev.getParameter(1).toString() != "") && (ev.getParameter(2).toString() != "") && (ev.getParameter(3).toString() != "")){
						BayMap loadBay=new BayMap();
						loadBay.setX_dimension(Integer.parseInt(ev.getParameter(1).toString()));
						loadBay.setY_dimension(Integer.parseInt(ev.getParameter(2).toString()));
						loadBay.setZ_dimension(Integer.parseInt(ev.getParameter(3).toString()));
						ontologyRepresentation.setContains(loadBay);
					}
				}
				if(ev.getParameter(9) instanceof List){ //Loaded Containers present
					ontologyRepresentation.setContainer_states((List) ev.getParameter(9));
				}
				if(ontologyRepresentation instanceof Ship){
					((Ship) ontologyRepresentation).setShip_length(Float.parseFloat(ev.getParameter(6).toString()));
				}
				DomainOntologyElement habitat=((DomainOntologyElement) ev.getParameter(10));
				ontologyRepresentation.setLives_in(habitat.getDomain());
				if(ontologyRepresentation instanceof ActiveContainerHolder){
					Object[] capabilities=((Object[]) ev.getParameter(11));
					for(int i=0;i < capabilities.length;i++){
						DomainOntologyElement domainOntologyElement=(DomainOntologyElement) capabilities[i];
						((ActiveContainerHolder) ontologyRepresentation).addCapable_of(domainOntologyElement.getDomain());
					}
				}
				
				HarbourSetup.inflateCH(ontologyRepresentation,harbourMap);
				
				StartNewContainerHolder act=new StartNewContainerHolder();
				act.setName(name);
				act.setTo_be_added(ontologyRepresentation);
				act.setRandomize(Boolean.parseBoolean(ev.getParameter(4).toString()));
				if(ev.getParameter(9) instanceof List){ //Loaded Containers present
					act.setPopulate(false);
				}else{
					act.setPopulate(Boolean.parseBoolean(ev.getParameter(5).toString()));
				}
				this.addBehaviour(new requestStartAgent(this,this.harbourMaster,act));


			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(command == 2){ // get ontologyRepresentation
			AID inQuestion=new AID();
			inQuestion.setLocalName(ev.getParameter(0).toString());

			this.addBehaviour(new requestOntologyRepresentation(this,inQuestion,this.harbourMaster));
		}else if(command == -1){ // GUI closed
			this.doDelete();
		}

	}
	
	protected void addToSniffer(AID agentToSniff){
		AID address=new AID();
		address.setName(agentToSniff.getName());
		SniffOn agact=new SniffOn();
		agact.addSniffedAgents(address);
		agact.setSniffer(sniffer);
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setLanguage(new SLCodec().getName());
		msg.setOntology(JADEManagementOntology.getInstance().getName());
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(JADEManagementOntology.getInstance());
		Action act=new Action(sniffer,agact);
		try{
			getContentManager().fillContent(msg,act);
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg.addReceiver(sniffer);
		send(msg);
	}

	@Override
	protected void setup(){
		// Instanciate the gui

		this.myGui=new ControlGUI(this);

		AgentDesktop ad=new AgentDesktop("ContMAS");
		this.canvas=ad.getDesktopPane();
		this.myGui.displayOn(this.canvas);
		this.myGui.setVisible(true);
		if(ad.getStandaloneMode() == AgentDesktop.AGENTDESKTOP_STANDALONE){
			this.myGui.getCanvas().getParent().getParent().getParent().getParent().setSize(this.myGui.getWidth() + 10,this.myGui.getHeight() + 30);
		}else{
			this.setWorkingDir("projects\\contmas\\");
		}

		this.addBehaviour(new listenForLogMessage(this));

		this.addBehaviour(new subscribeToDF(this,"involved-in-container-port"));

		AgentContainer c=this.getContainerController();

		AgentController a;
		try{
			a=c.createNewAgent("Sniffer","jade.tools.sniffer.Sniffer",null);//new Object[] {"Yard;StraddleCarrier;Apron;Crane-#2;Crane-#1"});
			a.start();
			this.sniffer=new AID();
			this.sniffer.setName(a.getName());
			
			String[] args=new String[1]; //Because of different working directories 
			args[0]=getWorkingDir();
			
			a=c.createNewAgent("RandomGenerator","contmas.agents.RandomGeneratorAgent",null);
			a.start();
			
			a=c.createNewAgent("HarborMaster","contmas.agents.HarborMasterAgent",args);
			a.start();
			
			this.harbourMaster=new AID();
			this.harbourMaster.setName(a.getName());

			a=c.createNewAgent("Optimizer","contmas.agents.BayMapOptimisationAgent",null);
			a.start();
			
//			a=c.createNewAgent("VisualisationProxy","contmas.agents.AgentGUIVisualisationProxyAgent",null);
//			a.start();
			
//			a=c.createNewAgent("SimulationController","contmas.agents.SimulationControlAgent",null);
//			a.start();
			
//			a=c.createNewAgent("Visualiser","contmas.agents.VisualisationAgent",null);
//			a.start();
		}catch(StaleProxyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.addBehaviour(new requestHarbourSetup(this,this.harbourMaster));

	}

	@Override
	protected void takeDown(){
		this.myGui.dispose();
	}

	public void updateAgentTree(List newAgents,Boolean remove){
		this.myGui.updateAgentTree(newAgents,remove);
	}

	/**
	 * @param content
	 */
	public void writeLogMsg(String content){
		this.myGui.writeLogMsg(content);
	}
	
	

	/**
	 * @param current_harbour_layout
	 */
	public void processHarbourLayout(Domain current_harbour_layout){
		harbourMap=current_harbour_layout;
		XMLCodec xmlCodec=new XMLCodec();
		String s;
		try{
			s=xmlCodec.encodeObject(ContainerTerminalOntology.getInstance(),current_harbour_layout,true);
			this.myGui.printOntRep(s);
		}catch(CodecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(OntologyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DomainOntologyElement root=ControlGUIAgent.renderDomain(current_harbour_layout);

		this.myGui.displayHarbourLayout(root);

//		this.myGui.displayHarbourLayout(current_harbour_layout);
	}

	private static DomainOntologyElement renderDomain(Domain dohmein){
		Iterator agentIter=dohmein.getAllHas_subdomains();
		DomainOntologyElement root=new DomainOntologyElement(dohmein);//dohmein.getClass().getSimpleName()+" - "+dohmein.getId());
		DomainOntologyElement domNode=null;

		while(agentIter.hasNext()){
			Domain curDom=(Domain) agentIter.next();
			domNode=renderDomain(curDom);
			root.add(domNode);
		}
		return root;
	}

	/* (non-Javadoc)
	 * @see contmas.behaviours.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		this.updateAgentTree(updatedAgents,remove);
		if(!remove){
			Iterator allAgents=updatedAgents.iterator();
			while(allAgents.hasNext()){
				AID curAgent=(AID) allAgents.next();
				addToSniffer(curAgent);
			}
		}
	}
	
	public void alterAgentName(){
		myGui.alterAgentName();
	}


}