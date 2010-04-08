package mas.display;

import java.util.HashMap;
import java.util.Iterator;

import mas.environment.DisplayConstants;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.w3c.dom.Element;

import sma.ontology.Movement;
import sma.ontology.Position;

import application.Project;

/**
 * GUI for DisplayAgent
 * @author nils
 *
 */
public class DisplayAgentGUI extends BasicSVGGUI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	// Current Agent-Project of AgentGUI
	Project currentProject = null;
	
	// SVG Namespace
	public static final String svgNs=SVGDOMImplementation.SVG_NAMESPACE_URI;
	/**
	 * The DisplayAgent controlling this GUI
	 */
	private DisplayAgent myAgent = null;
	/**
	 * Movements to be processed
	 */
	private HashMap<String, Iterator<Position>> movements;
	
	public DisplayAgentGUI(){
		super();
		this.getCanvas().setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		this.getCanvas().addGVTTreeRendererListener(new GVTTreeRendererAdapter(){
			public void gvtRenderingCompleted(GVTTreeRendererEvent re){
				
				Window window = getCanvas().getUpdateManager().getScriptingEnvironment().createWindow();
				window.setInterval(new Animation(), DisplayConstants.PERIOD);
			}
		});
		this.movements = new HashMap<String, Iterator<Position>>();
		
	}
	
	public void setAgent(DisplayAgent agent){
		this.myAgent = agent;
	}
	/**
	 * Adds all steps of a new movement to this.movements
	 * @param id The id of the moving agent
	 * @param mv The Movement instance
	 */
	@SuppressWarnings("unchecked")
	public void addMovement(String id, Movement mv){
		this.movements.put(id, mv.getAllSteps());
		System.out.println(id+" movement started");
	}
	
	/**
	 * Responsible for updating the SVG element positions
	 * @author Nils
	 *
	 */
	private class Animation implements Runnable{

		@Override
		public void run() {
						
			Iterator<String> ids= movements.keySet().iterator();
			while(ids.hasNext()){
				String id = ids.next();
				Iterator<Position> steps = movements.get(id);
				if(steps.hasNext()){
					changePos(id, steps.next());					
				}else{
					movements.remove(id);
					System.out.println(id+" movement finished");
				}
			}		
		}
		
		private void changePos(String id, Position pos){
			float posX, posY;
			
			posX = pos.getX();
			posY = pos.getY();
			
			Element element = DisplayAgentGUI.this.getCanvas().getSVGDocument().getElementById(id);
			
			switch(SvgTypes.getType(element)){
				case CIRCLE:
				case ELLIPSE:
					element.setAttributeNS(null, "cx", ""+posX);
					element.setAttributeNS(null, "cy", ""+posY);
				break;
				
				case RECT:
					// RECTs use top left coordinates
					float width = Float.parseFloat(element.getAttributeNS(null, "width"));
					float height = Float.parseFloat(element.getAttributeNS(null, "height"));
					posX -= width/2;
					posY -= height/2;
					element.setAttributeNS(null, "x", ""+posX);
					element.setAttributeNS(null, "y", ""+posY);
				break;
			}
		}
		
	}
			
}
