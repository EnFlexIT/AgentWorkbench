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

import org.w3c.dom.Element;

/**
 * The types of SVG elements supported 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 *
 */
public enum SvgTypes {
	SVG, RECT, CIRCLE, ELLIPSE, IMAGE;
	/**
	 * Returns the type constant for a given SVG element
	 * @param element The SVG element
	 * @return The type constant, or null if the type is not supported
	 */
	
	public static SvgTypes getType(Element element){
		SvgTypes type = null;
		
		if(element.getTagName().equals("svg")){
			type = SVG;
		}else if(element.getTagName().equals("rect")){
			type = RECT;
		}else if(element.getTagName().equals("circle")){
			type = CIRCLE;
		}else if(element.getTagName().equals("ellipse")){
			type = ELLIPSE;
		}else if(element.getTagName().equals("image")){
			type = IMAGE;
		}
		
		return type;
	}
}
