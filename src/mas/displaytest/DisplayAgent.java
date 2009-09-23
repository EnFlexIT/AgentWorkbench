package mas.displaytest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.script.Window;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class DisplayAgent extends Agent {
	
	public static final String svgNs="http://www.w3.org/2000/svg";
	
	
	JFrame frame;
	DOMImplementation impl;
	SVGDocument doc;
	Element svgRoot;
	JSVGCanvas canvas;
	Window window;
	Map <String, AnimAgent> animAgents;  // Hashmap storing animated agents, key=localName
	
	
	public void setup(){
		
		// Initializing SVG document
		impl=SVGDOMImplementation.getDOMImplementation();
		doc=(SVGDocument)impl.createDocument(svgNs, "svg", null);
		svgRoot=doc.getDocumentElement();
		svgRoot.setAttributeNS(null, "width", "800");
		svgRoot.setAttributeNS(null, "height", "600");
		
		// Displaying SVG document
		frame=new JFrame("Display agent 1st try");
		canvas=new JSVGCanvas();
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);	// Dynamic SVG document
		canvas.addGVTTreeRendererListener(new GVTTreeRendererListener(){

			@Override
			public void gvtRenderingCancelled(GVTTreeRendererEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void gvtRenderingCompleted(GVTTreeRendererEvent arg0) {
				window=canvas.getUpdateManager().getScriptingEnvironment().createWindow();
				window.setInterval(new Animation(), 50);
			}

			@Override
			public void gvtRenderingFailed(GVTTreeRendererEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void gvtRenderingPrepare(GVTTreeRendererEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void gvtRenderingStarted(GVTTreeRendererEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		canvas.setSVGDocument(doc);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);		
		System.out.println("DisplayAgent "+getLocalName()+" ready");
		
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
		
		addBehaviour(new RegistrationServer());
		addBehaviour(new MoveServer());
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
	
//	public void newAgent(String id, String width, String height, String startPosX, String startPosY, String color){
//		Element agent=doc.createElementNS(svgNs, "rect");
//		agent.setAttributeNS(null, "id", id);
//		agent.setAttributeNS(null, "width", width);
//		agent.setAttributeNS(null, "height", height);
//		agent.setAttributeNS(null, "x", startPosX);
//		agent.setAttributeNS(null, "y", startPosY);
//		agent.setAttributeNS(null, "style", "fill:"+color);
//	}
	
	
	
	/**
	 * Recieves and processes registration requests
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
			Element agentSvg=doc.createElementNS(svgNs, "rect");
			agentSvg.setAttributeNS(null, "id", localName);
			agentSvg.setAttributeNS(null, "width", width);
			agentSvg.setAttributeNS(null, "height", height);
			agentSvg.setAttributeNS(null, "x", xPos);
			agentSvg.setAttributeNS(null, "y", yPos);
			agentSvg.setAttributeNS(null, "style", "fill:"+color);
			svgRoot.appendChild(agentSvg);
			if(animAgents==null)
				animAgents=new HashMap<String, AnimAgent>();
			animAgents.put(localName, new AnimAgent(agentSvg,Integer.parseInt(xPos),Integer.parseInt(yPos),
					Integer.parseInt(width),Integer.parseInt(height)));
			System.out.println("Agent "+localName+" sucessfully registered");
		}
		
		private void deregisterAgent(String name){
			AnimAgent aa=animAgents.remove(name);
			svgRoot.removeChild(aa.getElem());
		}
		
	}
	
	/**
	 * Receives and processes movement requests 
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
				boolean collideX, collideY;
				AnimAgent aa=animAgents.get(moveRequest.getSender().getLocalName());
				String[] newPos=moveRequest.getContent().split(",");
				int newPosX=Integer.parseInt(newPos[0]);
				int newPosY=Integer.parseInt(newPos[1]);
				aa.setXPos(newPosX);
				aa.setYPos(newPosY);
				int col=detectCollisions(newPosX, newPosY, aa.getWidth(), aa.getHeight());
				if(col!=0){
					System.out.println("Collision detected");
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
		
		private int detectCollisions(int x, int y, int width, int height){
			int result=0;  // 1==horizontal collision, 2==vertical collision, 3==both	
			boolean colX=false, colY=false;
			if(x<=0||(x+width)>canvas.getWidth())
				colX=true;
			if(y<=0||(y+height)>canvas.getHeight())
				colY=true;
			if(colX)
				result+=1;
			if(colY)
				result+=2;
			return result;
		}
		
	}
	
	/**
	 * Actualizing agent positions
	 *  
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
	 * Container class storing the data needed to animate an agent
	 * @author nils
	 *
	 */
	private class AnimAgent{
		
		Element elem;	// Element visualizing the agent
		int xPos;		// The agents x coordinate
		int yPos;		// The agents y coordinate
		int width;
		int height;
		
		private AnimAgent(Element elem, int xPos, int yPos, int width, int height) {
			this.elem = elem;
			this.xPos = xPos;
			this.yPos = yPos;
			this.width=width;
			this.height=height;			
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
	

}
