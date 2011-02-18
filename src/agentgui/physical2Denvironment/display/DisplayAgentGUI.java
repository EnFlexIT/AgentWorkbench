package agentgui.physical2Denvironment.display;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.PlaygroundObject;
import agentgui.physical2Denvironment.ontology.Position;
import agentgui.physical2Denvironment.ontology.Scale;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;

public class DisplayAgentGUI extends BasicSVGGUI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6509863549022282766L;
	/**
	 * The scale of the SVG document
	 */
	private Scale scale;


	/**
	 * Constructor
	 * @param svgDoc The SVG document to display
	 * @param env The Physical2DEnvironment represented by the SVG
	 */
	public DisplayAgentGUI(Document svgDoc, Physical2DEnvironment env) {
		super();
		this.getCanvas().setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		this.scale = env.getScale();
		initPositions(svgDoc, env.getRootPlayground());
		this.setSVGDoc(svgDoc);
		
				
	}
	
	/**
	 * This method synchronizes the SVG document with the environment model before displaying it 
	 * @param svgDoc The SVG document
	 * @param pg The environment model's root playground
	 */
	@SuppressWarnings("unchecked")
	private void initPositions(Document svgDoc, PlaygroundObject pg){
		Iterator<Physical2DObject> objects = pg.getAllChildObjects();
		while(objects.hasNext()){
			Physical2DObject object = objects.next();
			Element element = svgDoc.getElementById(object.getId());
			if(element != null){
				setElementPosition(element, object.getPosition());
			}
			
			if(object instanceof PlaygroundObject){
				initPositions(svgDoc, (PlaygroundObject) object);
			}
		}
	}
	
	/**
	 * Sets the position of a SVG element according to a mas.environment.ontology.Position
	 * @param elem The SVG element
	 * @param pos The Position instance
	 */
	private void setElementPosition(Element elem, Position pos){
		// Convert to pixel values
		float xPos = pos.getXPos() / scale.getRealWorldUnitValue() * scale.getPixelValue();
		float yPos = pos.getYPos() / scale.getRealWorldUnitValue() * scale.getPixelValue();
		
		String elementType = elem.getTagName();	// Determine element type
		if(elementType.equals("rect")){
			// Convert to top left coordinates
			float width = Float.parseFloat(elem.getAttributeNS(null, "width"));
			float height = Float.parseFloat(elem.getAttributeNS(null, "height"));
			xPos -= width/2;
			yPos -= height/2;
			
			elem.setAttributeNS(null, "x", ""+xPos);
			elem.setAttributeNS(null, "y", ""+yPos);
		}else if(elementType.equals("circle") || elementType.equals("ellipse")){
			// No conversion necessary, circles and ellipses use center coordinates
			elem.setAttributeNS(null, "cx", ""+xPos);
			elem.setAttributeNS(null, "cy", ""+yPos);
		}
	}
	
	/**
	 * This method starts the posUpdater updating the SVG element's positions
	 * @param movingObjects
	 */
	void updatePositions(HashSet<Physical2DObject> movingObjects){
		UpdateManager um = getCanvas().getUpdateManager();
		if(um != null){
			
			um.getUpdateRunnableQueue().invokeLater(new posUpdater(movingObjects));
		}
		
	}
	
	
	
	/**
	 * This class updates the positions of the SVG elements representing moving objects
	 * @author Nils
	 *
	 */
	private class posUpdater implements Runnable{
		
		private HashSet<Physical2DObject> movingObjects;
		
		
		public posUpdater(HashSet<Physical2DObject> movingObjects){
			this.movingObjects = movingObjects;
		}

		@Override
		public void run() {
			
			
		
			
			
			// --- CD: Bug wg. concurrent Exception behogen ----
			HashSet<Physical2DObject> movingObjectsCopy = null;
			synchronized (movingObjects) {
				movingObjectsCopy = new HashSet<Physical2DObject>(movingObjects);
			}
			Iterator<Physical2DObject> objects = movingObjectsCopy.iterator();
			while(objects.hasNext()){
				Physical2DObject object = objects.next();
				Element element = getSVGDoc().getElementById(object.getId());
				if(element != null){
					setElementPosition(element, object.getPosition());
				}
			}				
		} // --- end run() ---
		
	}
}
