/**
 * 
 */
package mas.display;

import java.awt.Container;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mas.environment.AgentObject;
import mas.environment.Playground;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Neuimplementierung,   
 * 
 * @author Nils
 *
 */
public class DisplayAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String svgNs=SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	private Container parent = null;
	private BasicSvgGUI myGUI = null;
	private Playground environment = null;
	private Document svgDoc = null;
	private Element svgRoot = null;
	private HashMap<String, AgentObject> agents = null;
	
	public void setup(){
					
		Object[] args = getArguments();
		this.environment = (Playground) args[0];
		this.svgDoc = (Document) args[1];
		if(args[2] != null){
			// Setze GUI auf übergebenen Container
			this.parent = (Container) args[2];
		}else{
			// Kein Container übergeben -> erzeuge JFrame
			JFrame frame = new JFrame("DisplayAgent2 Test");
			parent = frame;			
			frame.pack();
			frame.setVisible(true);
		}
		myGUI = new BasicSvgGUI();
		myGUI.setSize(parent.getSize());
		parent.add(myGUI);
		
		Iterator<AgentObject> agents = environment.getAgents().iterator();
		this.agents = new HashMap<String, AgentObject>();
		while(agents.hasNext()){
			AgentObject newAgent = agents.next();
			this.agents.put(newAgent.getId(), newAgent);
		}
		
		
		myGUI.getCanvas().setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		
		myGUI.getCanvas().addGVTTreeRendererListener(new GVTTreeRendererAdapter(){
			public void gvtRenderingCompleted(GVTTreeRendererEvent re) {
				// Animation initialization
				Window window = myGUI.getCanvas().getUpdateManager().getScriptingEnvironment().createWindow();
				window.setInterval(new Animation(), 50);				
			}
		});
		
		myGUI.getCanvas().setDocument(svgDoc);
		svgRoot = svgDoc.getDocumentElement();
		Element border = svgDoc.createElementNS(svgNs, "rect");
		border.setAttributeNS(null, "x", "1");
		border.setAttributeNS(null, "y", "1");
		border.setAttributeNS(null, "width", ""+(environment.getWidth()-2));
		border.setAttributeNS(null, "height", ""+(environment.getHeight()-2));
		border.setAttributeNS(null, "stroke", "black");
		border.setAttributeNS(null, "stroke-width", "2");
		border.setAttributeNS(null, "fill", "none");
		svgRoot.appendChild(border);
		
		try {
			TopicManagementHelper tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			AID positionTopic = tmh.createTopic("position");
			tmh.register(positionTopic);
			addBehaviour(new PosUpdater(MessageTemplate.MatchTopic(positionTopic)));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Display agent "+getLocalName()+"ready.");		
	}
	
	class PosUpdater extends CyclicBehaviour{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		MessageTemplate positionTemplate;
		DisplayAgent da;
		
		public PosUpdater(MessageTemplate mt){
			this.positionTemplate = mt;			
		}

		@Override
		public void action() {
			ACLMessage posUpdate = receive(positionTemplate);
			if(posUpdate != null){
				String sender = posUpdate.getSender().getLocalName();
				String content = posUpdate.getContent();
				if(content.equals("bye")){
					agents.remove(sender);
				}else{
					String[] pos = content.split(",");
					AgentObject agent = agents.get(sender);
					agent.setPosX(Integer.parseInt(pos[0]));
					agent.setPosY(Integer.parseInt(pos[1]));
				}
				
			}else{
				block();
			}
			
		}
		
	}
	
	class Animation implements Runnable{

		@Override
		public void run() {
			Iterator<AgentObject> iter = agents.values().iterator(); 
			while(iter.hasNext()){
				AgentObject agentObject = iter.next();
				Element agentSvg = svgDoc.getElementById(agentObject.getId());
				agentSvg.setAttributeNS(null, "x", ""+agentObject.getPosX());
				agentSvg.setAttributeNS(null, "y", ""+agentObject.getPosY());
			}
		}
		
	}
	

}
