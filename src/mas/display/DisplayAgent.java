package mas.display;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Offering display service for other agents. Takes care of displaying, collision management etc.
 *   
 * @author nils
 *
 */
public class DisplayAgent extends Agent {
	
	// SVG Namespace
	public static final String svgNs=SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	private DisplayAgentGUI myGUI = null;
	private DisplayAgent myAgent = this;
	
	public Map <String, AnimAgent> animAgents = null;  // Hashmap storing animated agents, key=localName
	
	public void setup(){
		
		myGUI = new DisplayAgentGUI(this);
		animAgents=new HashMap<String, AnimAgent>();
		
		// Add Behaviours
		this.addBehaviour(new RegistrationServer());
		this.addBehaviour(new MoveServer());
		
		// Registering service
		DFAgentDescription dfd=new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd=new ServiceDescription();
		sd.setType("Display Service");
		sd.setName(getLocalName()+" Display Service");
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
		System.out.println("Display agent "+getLocalName()+"ready.");
		this.addBehaviour(new DisplayableSearchBehaviour(this, 10000));
		
		
	}
	
	public void takeDown(){
		// Deregister Service
		try{
			DFService.deregister(this);
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
		if(myGUI!=null){
			myGUI.setVisible(false);
		}
		
	}
	
	/**
	 * Behaviour for registration and deregistration of agents to be displayed  
	 * @author nils
	 *
	 */
	class RegistrationServer extends CyclicBehaviour{

		@Override
		public void action() {
			String name, type, xPos, yPos;
			MessageTemplate mt=MessageTemplate.and(
					MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
					MessageTemplate.or(
							MessageTemplate.MatchConversationId("register"), 
							MessageTemplate.MatchConversationId("deregister")
					)
			);
			ACLMessage request=receive(mt);
			if(request!=null){
				if(request.getConversationId()=="register"){
					System.out.println("Received registration request from "+request.getSender().getLocalName());
					String[] props = request.getContent().split(",");
					name = request.getSender().getLocalName();
					type = props[0];
					xPos = props[1];
					yPos = props[2];
					registerAgent(name, type, xPos, yPos);
					

				}else{
					System.out.println("Received deregistration request from "+request.getSender().getLocalName());
					deregisterAgent(request.getSender().getLocalName());
				}
			}else{
				block();
			}
			
		}
		
		/**
		 * Getting the agents SVG representation and adds it to the SVG document
		 * @param localName
		 * @param type
		 * @param xPos
		 * @param yPos
		 */
		private void registerAgent(String name, String type, String xPos, String yPos){
			
			// Try to obtain SVG representation
			Element agentSVG = AgentSVGDefinitions.getSVG(type, xPos, yPos, myGUI.svgDoc);
			if(agentSVG != null){
				// Set id attribute and append to the SVG document
				agentSVG.setAttributeNS(null, "id", name);
				myGUI.addAgent(agentSVG);
				
				// Get position and size
				int x, y, width, height;
				String tagName = agentSVG.getTagName();
				if(tagName == "circle"){
					int r = Integer.parseInt(agentSVG.getAttribute("r"));
					x = Integer.parseInt(agentSVG.getAttribute("cx"));
					y = Integer.parseInt(agentSVG.getAttribute("cy"));
					width = r;
					height = width;
				}else{
					x = Integer.parseInt(agentSVG.getAttribute("x"));
					y = Integer.parseInt(agentSVG.getAttribute("y"));
					width = Integer.parseInt(agentSVG.getAttribute("width"));
					height = Integer.parseInt(agentSVG.getAttribute("height"));
				}					
				animAgents.put(name, new AnimAgent(name, agentSVG, x, y, width, height));
				System.out.println("Agent "+name+" sucessfully registered, pos"+x+","+y);
			}else{
				System.err.println("Could not get SVG representation for agent type "+type);
			}
			
			
		}
		
		private void deregisterAgent(String name){
			AnimAgent aa=animAgents.remove(name);
			myGUI.removeAgent(name);
			System.out.println("Agent "+name+" sucessfully deregistered");
		}
		
	}

	/**
	 * Behaviour processing the agents position updates 
	 * @author nils
	 *
	 */
	class MoveServer extends CyclicBehaviour{

		@Override
		public void action() {
			MessageTemplate mt=MessageTemplate.and(
					MessageTemplate.MatchPerformative(ACLMessage.REQUEST), 
					MessageTemplate.MatchConversationId("move"));
			ACLMessage moveRequest=receive(mt);
			if(moveRequest!=null){
				boolean collideX=false, collideY=false;
				
				AnimAgent aa=animAgents.get(moveRequest.getSender().getLocalName());
				if(aa!=null){
					String[] newPos=moveRequest.getContent().split(",");
					int newPosX=Integer.parseInt(newPos[0]);
					int newPosY=Integer.parseInt(newPos[1]);
				
					// Collision detection
					// Borders
					boolean[] colls=detectBorderCollision(aa,newPosX,newPosY);
					if(colls[0]||colls[1]){
						System.out.println("Border collision detected, pos "+newPosX+","+newPosY);
						ACLMessage reply=moveRequest.createReply();
						reply.setPerformative(ACLMessage.INFORM);
						reply.setConversationId("collision");
						reply.setContent(Arrays.toString(colls));
						send(reply);
					}
					// Other agents
					Iterator<String> keys=animAgents.keySet().iterator();
					while(keys.hasNext()){
						AnimAgent oa=animAgents.get(keys.next());
						if(!oa.equals(aa)){		// If not the same agent
							colls=detectAgentCollision(aa,newPosX,newPosY,oa);
							if(colls[0]||colls[1]){
								System.out.println("Agent collision detected");
								ACLMessage reply=moveRequest.createReply();
								reply.addReceiver(new AID(oa.getId(),AID.ISLOCALNAME));
								reply.setPerformative(ACLMessage.INFORM);
								reply.setConversationId("collision");
								reply.setContent(Arrays.toString(colls));
								send(reply);
							}							
						}
					}
				
					aa.setXPos(newPosX);
					aa.setYPos(newPosY);
					int col=0;
					if(collideX)col+=1;
					if(collideY)col+=2;
				
					if(col!=0){
						System.out.println("Border collision detected");
						ACLMessage reply=moveRequest.createReply();
						reply.setPerformative(ACLMessage.INFORM);
						reply.setConversationId("collision");
						reply.setContent(""+col);
						send(reply);
					}
				}
			}else{
				block();
			}			
		}
		
		private boolean[] detectBorderCollision(AnimAgent aa, int newPosX, int newPosY){
			boolean coll[]={false, false};
			if(newPosX<0||newPosX+aa.getWidth()>myGUI.canvas.getWidth())
				coll[0]=true;
			if(newPosY<0||newPosY+aa.getHeight()>myGUI.canvas.getHeight())
				coll[1]=true;
			return coll;
		}
		
		private boolean[] detectAgentCollision(AnimAgent aa, int newPosX, int newPosY, AnimAgent oa){
			boolean coll[]={false, false};
			// X direction
			if(aa.getYPos()+aa.getHeight()>oa.getYPos()&&aa.getYPos()<oa.getYPos()+oa.getHeight()){
				if(newPosX+aa.getWidth()>oa.getXPos()&&newPosX<oa.getXPos()+oa.getWidth()
						&&!(aa.getXPos()+aa.getWidth()>oa.getXPos()&&aa.getXPos()<oa.getXPos()+oa.getWidth()))
					coll[0]=true;
			}
			// Y direction
			if(aa.getXPos()+aa.getWidth()>oa.getXPos()&&aa.getXPos()<oa.getXPos()+oa.getWidth()){
				if(newPosY+aa.getHeight()>oa.getYPos()&&newPosY<oa.getYPos()+oa.getHeight()
						&&!(aa.getYPos()+aa.getHeight()>oa.getYPos()&&aa.getYPos()<oa.getYPos()+oa.getHeight()))
					coll[1]=true;
			}
			return coll;
		}		
	}
	
	class DisplayableSearchBehaviour extends TickerBehaviour{

		public DisplayableSearchBehaviour(Agent a, long period) {
			super(a, period);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onTick() {
			DFAgentDescription template= new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("displayable");
			template.addServices(sd);
			try{
				DFAgentDescription[] results = DFService.search(myAgent, template);
				for (int i=0; i<results.length; i++){
					jade.util.leap.Iterator services = results[i].getAllServices();
					while(services.hasNext()){
						ServiceDescription service = (ServiceDescription) services.next();
						if(service.getType().equals("displayable")){
							String name = service.getName();
							int splitPos = name.indexOf('-');
							String type = name.substring(splitPos+1);
							System.out.println("Found displayable agent "+results[i].getName().getLocalName()+" of type"+type);
						}
					}
				}
			}catch (FIPAException fe){
				fe.printStackTrace();
			}			
					
		}
		
//		private void searchDisplayables(){
//			DFAgentDescription template= new DFAgentDescription();
//			ServiceDescription sd = new ServiceDescription();
//			sd.setType("displayable");
//			template.addServices(sd);
//			try{
//				DFAgentDescription[] results = DFService.search(myAgent, template);
//				for (int i=0; i<results.length; i++){
//					jade.util.leap.Iterator services = results[i].getAllServices();
//					while(services.hasNext()){
//						ServiceDescription service = (ServiceDescription) services.next();
//						if(service.getType().equals("displayable")){
//							String name = service.getName();
//							int splitPos = name.indexOf('-');
//							String type = name.substring(splitPos+1);
//							System.out.println("Found displayable agent "+results[i].getName().getLocalName()+" of type"+type);
//						}
//					}
//				}
//			}catch (FIPAException fe){
//				fe.printStackTrace();
//			}			
//		}

		
		
	}
}
