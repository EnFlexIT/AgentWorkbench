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
	public static final String svgNs="http://www.w3.org/2000/svg";
	
	private DisplayAgentGUI myGUI = null;
	
	private Document svgDoc;
	private Element svgRoot;	// Root element of the SVG Document
	public Map <String, AnimAgent> animAgents = null;  // Hashmap storing animated agents, key=localName
	
	public void setup(){
		
		myGUI = new DisplayAgentGUI(this);
		
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
		
		
	}
	
	public void takeDown(){
		// Deregister Service
		try{
			DFService.deregister(this);
		}catch(FIPAException fe){
			fe.printStackTrace();
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
			String name, width, height, xPos, yPos, color;
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
					String[] props=request.getContent().split(",");
					name=request.getSender().getLocalName();
					width=props[0];
					height=props[1];
					xPos=props[2];
					yPos=props[3];
					color=props[4];
					registerAgent(name, width, height,xPos,yPos,color);
					

				}else{
					System.out.println("Received deregistration request from "+request.getSender().getLocalName());
					deregisterAgent(request.getSender().getLocalName());
				}
			}else{
				block();
			}
			
		}
		
		private void registerAgent(String localName, String width, String height, String xPos, String yPos, String color){
			
			Element agentSvg=myGUI.svgDoc.createElementNS(svgNs, "rect");
			agentSvg.setAttributeNS(null, "id", localName);
			agentSvg.setAttributeNS(null, "width", width);
			agentSvg.setAttributeNS(null, "height", height);
			agentSvg.setAttributeNS(null, "x", xPos);
			agentSvg.setAttributeNS(null, "y", yPos);
			agentSvg.setAttributeNS(null, "style", "fill:"+color);
			myGUI.addAgent(agentSvg);
			if(animAgents==null)
				animAgents=new HashMap<String, AnimAgent>();
			animAgents.put(localName, new AnimAgent(localName, agentSvg,Integer.parseInt(xPos),Integer.parseInt(yPos),
					Integer.parseInt(width),Integer.parseInt(height)));
			System.out.println("Agent "+localName+" sucessfully registered");
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
				String[] newPos=moveRequest.getContent().split(",");
				int newPosX=Integer.parseInt(newPos[0]);
				int newPosY=Integer.parseInt(newPos[1]);
				
				
				// Collision detection
				// Borders
				boolean[] colls=detectBorderCollision(aa,newPosX,newPosY);
				if(colls[0]||colls[1]){
					System.out.println("Border collision detected");
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
}
