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

import application.Language;

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
 * Steuert die Darstellung 
 * 
 * @author Nils
 *
 */
public class DisplayAgent extends Agent {

	
	private static final long serialVersionUID = 1L;
	
	public static final String svgNs=SVGDOMImplementation.SVG_NAMESPACE_URI;
	/**
	 * Container, der die GUI dieses Agenten enthält
	 */
	private Container parent = null;
	/**
	 * GUI, die von diesem Agenten kontrolliert wird
	 */
	private BasicSvgGUI myGUI = null;
	/**
	 * Umgebung, die von diesem Agenten dargestellt wird 
	 */
	private Playground environment = null;
	/**
	 * Dargestelltes SVG Dokument
	 */
	private Document svgDoc = null;
	/**
	 * SVG-Wurzelelement
	 */
	private Element svgRoot = null;
	/**
	 * Bekannte Agenten, key = localName des Agenten = id des SVG-Elementes
	 */
	private HashMap<String, AgentObject> agents = null;
	
	public void setup(){
		/*
		 * Übergebene Argumente auswerten
		 * args[0] = Playground-Objekt der darzustellenden Umgebung
		 * args[1] = Zugehöriges SVG-Dokument
		 * args[2] = Container; der die GUI aufnimmt 
		 */
		Object[] args = getArguments();
		this.environment = (Playground) args[0];
		this.svgDoc = (Document) args[1];
		if(args[2] != null){
			// Container übergeben
			this.parent = (Container) args[2];
		}else{
			// Kein Container übergeben -> erzeuge JFrame
			JFrame frame = new JFrame("DisplayAgent - Stand Alone Mode");
			parent = frame;			
			frame.pack();
			frame.setVisible(true);
		}
		// GUI-Objekt erzeugen und zum Parent-Container hinzufügen
		myGUI = new BasicSvgGUI();
		myGUI.setSize(parent.getSize());
		parent.add(myGUI);
		
		/*
		 * Agenten aus der Umgebungsdefinition lesen und in HashMap speichern
		 * (=> über id/localName direkt adressierbar)
		 */
		Iterator<AgentObject> agents = environment.getAgents().iterator();
		this.agents = new HashMap<String, AgentObject>();
		while(agents.hasNext()){
			AgentObject newAgent = agents.next();
			this.agents.put(newAgent.getId(), newAgent);
		}
		
		
		myGUI.getCanvas().setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);	// Dynamisches SVG-Dokument
		// Starte die Animation wenn das Dokument vollständig geladen wurde
		myGUI.getCanvas().addGVTTreeRendererListener(new GVTTreeRendererAdapter(){
			public void gvtRenderingCompleted(GVTTreeRendererEvent re) {
				// Animation initialisieren
				Window window = myGUI.getCanvas().getUpdateManager().getScriptingEnvironment().createWindow();
				window.setInterval(new Animation(), 50);				
			}
		});
		
		// Erzeuge Rahmen um das Dokument
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
		
		// Fordere TopicManagementHelper an und registriere für Topic "position"
		try {
			TopicManagementHelper tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			AID positionTopic = tmh.createTopic("position");
			tmh.register(positionTopic);
			addBehaviour(new PosUpdater(MessageTemplate.MatchTopic(positionTopic)));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		System.out.println("DisplayAgent "+getLocalName()+" "+Language.translate("bereit"));		
	}
	
	/**
	 * Aktualisiert in regelmäßigen Abständen die Position aller bekannten Agents im SVG Dokument 
	 * @author Nils
	 *
	 */
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
			// Empfange Nachricht des TopicManagementHelpers
			ACLMessage posUpdate = receive(positionTemplate);
			if(posUpdate != null){
				String sender = posUpdate.getSender().getLocalName();
				String content = posUpdate.getContent();
				if(content.equals("bye")){
					// Agent wurde beendet -> entferne aus HashMap
					agents.remove(sender);
				}else{
					// Aktualisiere Positionsdaten des Absenders in der HashMap
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
	
	/**
	 * Aktualisiert die Positionen aller SVG-Elemente, die Agenten repräsentieren
	 * @author Nils
	 *
	 */
	class Animation implements Runnable{

		@Override
		public void run() {
			Iterator<AgentObject> iter = agents.values().iterator(); 
			while(iter.hasNext()){
				setAgentPos(iter.next());
			}
		}
		
	}
	
	/**
	 * Aktualisiert die Position der Agenten im SVG
	 * @param agent
	 */
	private void setAgentPos(AgentObject agent){
		Element agentSvg;
		switch(agent.getSvgType()){
			case rect:
				agentSvg = svgDoc.getElementById(agent.getId());
				agentSvg.setAttributeNS(null, "x", ""+agent.getPosX());
				agentSvg.setAttributeNS(null, "y", ""+agent.getPosY());
			break;
			
			case circle:				
			case ellipse:
				agentSvg = svgDoc.getElementById(agent.getId());
				int cx = agent.getPosX()+agent.getWidth()/2;
				int cy = agent.getPosY()+agent.getHeight()/2;
				agentSvg.setAttributeNS(null, "cx", ""+cx);
				agentSvg.setAttributeNS(null, "cy", ""+cy);
			break;
		}
	}
	

}
