/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.p2Dsvg.display;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PlaygroundObject;
import agentgui.envModel.p2Dsvg.ontology.Position;
import agentgui.envModel.p2Dsvg.ontology.Scale;

public class DisplayAgentGUI extends BasicSVGGUI {
	
	/**
	 * Updating of the screen, elements. Initialization of elements.
	 * Nils Loose - DAWIS - ICB - University of Duisburg - Essen
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
			
			try
			{
						
			um.getUpdateRunnableQueue().invokeLater(new posUpdater(movingObjects));
			}
			catch(Exception e)
			{
				this.updatePositions(movingObjects);
			}
		}
		
	}
	
	
	
	/**
	 * This class updates the positions of the SVG elements representing moving objects
	 * @author Nils
	 *
	 */
	private class posUpdater implements Runnable{
		
		private HashSet <Physical2DObject> movingObjects;
		
		
		public posUpdater(HashSet<Physical2DObject> movingObjects){
			this.movingObjects = movingObjects;
		}

		@Override
		public void run() {
			
			
		try
		{	
			
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
		}
		catch(Exception e)
		{
			
		}
		} // --- end run() ---
		
	}
}
