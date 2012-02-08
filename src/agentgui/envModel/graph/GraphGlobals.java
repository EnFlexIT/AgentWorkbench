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
package agentgui.envModel.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * The Class Globals for global constant values of the Graph or Network model.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public final class GraphGlobals {

	/** The path images. */
	private static String pathImages = "/agentgui/envModel/graph/img/";
	private static boolean debug = false;
	private static boolean colorPropertiesPrinted = false;
	
	/**
	 * Gets the path for images.
	 * @return the pathImages
	 */
	public static String getPathImages() {
		return pathImages;
	}
	
	/**
	 * The Class Colors.
	 */
	public final static class Colors {
		
		/** The Constant TB_BACKGROUND. */
		public final static Color TB_BACKGROUND = UIManager.getColor("Table.background");
		/** The Constant TB_ALTERNATEROWCOLOR. */
		public final static Color TB_ALTERNATEROWCOLOR = Color.WHITE;
		/** The Constant TB_HIGHLIGHT. */
		public final static Color TB_HIGHLIGHT = UIManager.getColor("Table[Enabled+Selected].textBackground");

		/** The Constant TB_TEXTFOREGROUND. */
		public final static Color TB_TEXTFOREGROUND = UIManager.getColor("Table.textForeground");
		/** The Constant TB_TEXTFOREGROUND_SELECTED. */
		public final static Color TB_TEXTFOREGROUND_SELECTED = UIManager.getColor("Table[Enabled+Selected].textForeground");
		
		/**
		 * Sets the color for a component, which is located in a JTable cell.
		 *
		 * @param comp the JComponent
		 * @param row the row
		 * @param isSelected the is selected
		 */
		public static void setTableCellRendererColors(JComponent comp, int row, boolean isSelected) {
			
			// --- do the settings --------------
			comp.setOpaque(true);
			if (isSelected==true) {
				comp.setForeground(GraphGlobals.Colors.TB_TEXTFOREGROUND_SELECTED);
				comp.setBackground(GraphGlobals.Colors.TB_HIGHLIGHT);
				
			} else {
				comp.setForeground(GraphGlobals.Colors.TB_TEXTFOREGROUND);
				if(row % 2 == 0){
					comp.setBackground(GraphGlobals.Colors.TB_BACKGROUND);
				} else {
					comp.setBackground(GraphGlobals.Colors.TB_ALTERNATEROWCOLOR);					
				}
				
			}
			
			// --- In case of debugging ---------
			if (debug==false) {
				return;
				
			} else {
				if (colorPropertiesPrinted==false) {
					List<String> colors = new ArrayList<String>(); 
					for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
						if (entry.getValue() instanceof Color) {         
							colors.add((String) entry.getKey()); // all the keys are strings     
						}
					}
					Collections.sort(colors); 
					for (String name : colors)     
						System.out.println(name);

					colorPropertiesPrinted = true;
				}
			}

		}
		
		
	} // end color sub class
	
} // end class
