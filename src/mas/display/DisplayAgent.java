package mas.display;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

/**
 * Offering display service for other agents. Takes care of displaying, collision management etc.
 *   
 * @author nils
 *
 */
public class DisplayAgent extends Agent {
	
	// SVG Namespace
	public static final String svgNs="http://www.w3.org/2000/svg";
	
	private JFrame frame;	// Later replaced by christian's GUI
	private JPanel panel;
	private JSVGCanvas canvas;
	
	private Document svgDoc;
	private Element svgRoot;	// Root element of the SVG Document
	private Window window;
	private Map <String, AnimAgent> animAgents;  // Hashmap storing animated agents, key=localName
	
	public void setup(){
		frame = new JFrame();		// Later replaced by christian's GUI
		
		// GUI and SVG Initialization
		panel = new JPanel();
		canvas = new JSVGCanvas();
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);	// Dynamic document
		canvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter(){
			// Method called when the document is completely loaded
			public void gvtRenderingCompleted(GVTTreeRendererEvent re) {
				// Animation initialization
				window = canvas.getUpdateManager().getScriptingEnvironment().createWindow();
				window.setInterval(new Animation(), 50);
			}
		});
		
		// SVG document creation
		svgDoc = SVGDOMImplementation.getDOMImplementation().createDocument(svgNs, "svg", null);
		svgRoot = svgDoc.getDocumentElement();

		
		canvas.setDocument(svgDoc);
		panel.add(canvas);		
		
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
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
		frame.dispose();
	}
	
	/**
	 * Controlling animation
	 * @author nils
	 *
	 */
	private class Animation implements Runnable{

		@Override
		public void run() {
			if(animAgents!=null){
				Iterator<String> keys=animAgents.keySet().iterator();
				while(keys.hasNext()){
					AnimAgent agent=animAgents.get(keys.next());
					agent.getElem().getAttributeNodeNS(null, "x").setValue(""+agent.getXPos());
					agent.getElem().getAttributeNodeNS(null, "y").setValue(""+agent.getYPos());					
				}
			}
			
		}
		
	}
	
	/**
	 * Helper class storing and agent's position and representing SVG Element 
	 * @author nils
	 */
	private class AnimAgent{
		
		String id;
		Element elem;	// Element visualizing the agent
		int xPos;		// The agents x coordinate
		int yPos;		// The agents y coordinate
		int width;
		int height;		
		
		private AnimAgent(String id, Element elem, int xPos, int yPos, int width, int height) {
			this.id=id;
			this.elem = elem;
			this.xPos = xPos;
			this.yPos = yPos;
			this.width=width;
			this.height=height;			
		}		
		
		private String getId(){
			return id;
		}
		
		private Element getElem() {
			return elem;
		}		
		private int getXPos() {
			return xPos;
		}
		private void setXPos(int pos) {
			xPos = pos;
		}
		private int getYPos() {
			return yPos;
		}
		private void setYPos(int pos) {
			yPos = pos;
		}
		private int getWidth(){
			return width;
		}
		private int getHeight(){
			return height;
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
			Element agentSvg=svgDoc.createElementNS(svgNs, "rect");
			agentSvg.setAttributeNS(null, "id", localName);
			agentSvg.setAttributeNS(null, "width", width);
			agentSvg.setAttributeNS(null, "height", height);
			agentSvg.setAttributeNS(null, "x", xPos);
			agentSvg.setAttributeNS(null, "y", yPos);
			agentSvg.setAttributeNS(null, "style", "fill:"+color);
			svgRoot.appendChild(agentSvg);
			if(animAgents==null)
				animAgents=new HashMap<String, AnimAgent>();
			animAgents.put(localName, new AnimAgent(localName, agentSvg,Integer.parseInt(xPos),Integer.parseInt(yPos),
					Integer.parseInt(width),Integer.parseInt(height)));
			System.out.println("Agent "+localName+" sucessfully registered");
		}
		
		private void deregisterAgent(String name){
			AnimAgent aa=animAgents.remove(name);
			svgRoot.removeChild(aa.getElem());
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
			if(newPosX<0||newPosX+aa.getWidth()>canvas.getWidth())
				coll[0]=true;
			if(newPosY<0||newPosY+aa.getHeight()>canvas.getHeight())
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
