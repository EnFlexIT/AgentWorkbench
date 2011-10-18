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
package agentgui.envModel.p2Dsvg.utils;

import org.w3c.dom.Element;

import agentgui.envModel.p2Dsvg.ontology.Position;
import agentgui.envModel.p2Dsvg.ontology.Scale;
import agentgui.envModel.p2Dsvg.ontology.Size;


/**
 * Some useful static helper methods for SVG handling
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public class EnvironmentHelper {
	/**
	 * This method calculates the size of a SVG element, considering the defined scale
	 * @param elem The element
	 * @param scale The environments scale
	 * @return The size
	 */
	public static Size getSizeFromElement(Element elem, Scale scale){
		float width = -1, height = -1;
		
		if(elem.getTagName().equals("rect")){
			width = Float.parseFloat(elem.getAttributeNS(null, "width"));
			height = Float.parseFloat(elem.getAttributeNS(null, "height"));
		}else if(elem.getTagName().equals("circle")){
			float r = Float.parseFloat(elem.getAttributeNS(null, "r"));
			width = height = 2*r;
		}else if(elem.getTagName().equals("ellipse")){
			width = (Float.parseFloat(elem.getAttributeNS(null, "rx")))*2;
			height = (Float.parseFloat(elem.getAttributeNS(null, "ry")))*2;
		}
		
		// Calculate Scale
		width /= scale.getPixelValue();
		width *= scale.getRealWorldUnitValue();
		height /= scale.getPixelValue();
		height *= scale.getRealWorldUnitValue();
		
		Size size = new Size();
		size.setWidth(width);
		size.setHeight(height);
		return size;
	}
	/**
	 * This method calculates the center position of a SVG element, considering the defined scale
	 * @param elem The element
	 * @param scale The environments scale
	 * @return The position
	 */
	public static Position getPosFromElement(Element elem, Scale scale){
		float x=-1, y=-1;
		if(elem.getTagName().equals("rect")){
			x = (Float.parseFloat(elem.getAttributeNS(null, "x")) + Float.parseFloat(elem.getAttributeNS(null, "width"))/2);
			y = (Float.parseFloat(elem.getAttributeNS(null, "y")) + Float.parseFloat(elem.getAttributeNS(null, "height"))/2);
		}else if(elem.getTagName().equals("circle") || elem.getTagName().equals("ellipse")){
			x = Float.parseFloat(elem.getAttributeNS(null, "cx"));
			y = Float.parseFloat(elem.getAttributeNS(null, "cy"));
		}
		
		x /= scale.getPixelValue();
		x *= scale.getRealWorldUnitValue();
		y /= scale.getPixelValue();
		y *= scale.getRealWorldUnitValue();
		Position pos = new Position();
		pos.setXPos(x);
		pos.setYPos(y);
		return pos;
	}
	
	public static void setSizeFromStrings(Element elem, Scale scale, String width, String height){
		float w = Float.parseFloat(width);
		w /= scale.getRealWorldUnitValue();
		w *= scale.getPixelValue();
		float h = Float.parseFloat(height);
		h /= scale.getRealWorldUnitValue();
		h *= scale.getPixelValue();
		
		if(elem.getTagName().equals("rect")){
			elem.setAttributeNS(null, "width", ""+w);
			elem.setAttributeNS(null, "height", ""+h);
		}else if(elem.getTagName().equals("circle")){
			elem.setAttributeNS(null, "r", ""+(w/2));
		}else if(elem.getTagName().equals("ellipse")){
			elem.setAttributeNS(null, "rx", ""+(w/2));
			elem.setAttributeNS(null, "ry", ""+(h/2));
		}
	}
	
	public static void setPosFromStrings(Element elem, Scale scale, String xPos, String yPos, String width, String height){
		float x = Float.parseFloat(xPos);
		x /= scale.getRealWorldUnitValue();
		x *= scale.getPixelValue();
		float y = Float.parseFloat(yPos);
		y /= scale.getRealWorldUnitValue();
		y *= scale.getPixelValue();
		
		if(elem.getTagName().equals("rect")){
			float w = Float.parseFloat(width);
			w /= scale.getRealWorldUnitValue();
			w *= scale.getPixelValue();
			float h = Float.parseFloat(height);
			h /= scale.getRealWorldUnitValue();
			h *= scale.getPixelValue();
			
			elem.setAttributeNS(null, "x", ""+(x-(w/2)));
			elem.setAttributeNS(null, "y", ""+(y-(h/2)));
		}else if(elem.getTagName().equals("circle") || elem.getTagName().equals("ellipse")){
			elem.setAttributeNS(null, "cx", ""+x);
			elem.setAttributeNS(null, "cy", ""+y);
		}
		
		
	}
}
