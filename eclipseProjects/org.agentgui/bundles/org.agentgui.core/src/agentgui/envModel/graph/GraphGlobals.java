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
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import edu.uci.ics.jung.graph.Graph;

import agentgui.core.application.Application;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import de.enflexit.common.PathHandling;

/**
 * The Class Globals for global constant values of the Graph or Network model.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public final class GraphGlobals {

	private static String pathImages = "/agentgui/envModel/graph/img/";
	private static boolean debug = false;
	private static boolean colorPropertiesAlreadyPrinted = false;

	/**
	 * Gets the path for images.
	 * @return the pathImages
	 */
	public static String getPathImages() {
		return pathImages;
	}

	 /**
 	 * Returns an ImageIcon as specified in path2Image. Therefore the methods
 	 * tries to find the images at different places. This is, first, in the
 	 * available packages or second at different folder location on the disc.
 	 *
 	 * @param path2Image the path to the image
 	 * @return the image icon
 	 */
 	public static ImageIcon getImageIcon(String path2Image) {
	    
 		String path = null;
 		File imageFile = null;
 		ImageIcon imageIcon = null;
 		
 		// ----------------------------------------------------------
 		// --- 0. Get the source folder of the execution instance ---
 		// ----------------------------------------------------------
 		// --- Removed because of a authority bug in IOS-systems ---- 
// 		File currRootFile;
//		try {
//			currRootFile = new File(GraphGlobals.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//			if (currRootFile.exists()) {
//				pathRoot = currRootFile.getParentFile().getAbsolutePath();
//	    	}
//		} catch (URISyntaxException e) {
//			//e.printStackTrace();
//		}
 		// --- New approach to the above mentioned problem ----------
 		
 		// ----------------------------------------------------------
		// --- 1. Search in the loaded packages ---------------------
 		// ----------------------------------------------------------
		// --- 1a. Try the direct way -------------------------------
		try {
			URL url = GraphGlobals.class.getResource(path2Image);
			imageIcon = new ImageIcon(url);
			
		} catch (Exception ex) {
			//System.err.println("Could not find image for '" + description + "' in packages");
			imageIcon = null;
		}

		// --- 1b. Try the image package of the GraphEnvironement ---
		if (imageIcon==null) {
			try {
				URL url = GraphGlobals.class.getResource(getPathImages() + path2Image);
				imageIcon = new ImageIcon(url);
				
			} catch (Exception ex) {
				//System.err.println("Could not find image for '" + description + "' in packages");
				imageIcon = null;
			}
		}
		
 		// ----------------------------------------------------------
		// --- 2. Try folder locations ------------------------------
 		// ----------------------------------------------------------
		if (imageIcon==null) {
			// --- 2a. Try direct folder location -------------------
	 		path = path2Image;
	 		imageFile = new File(PathHandling.getPathName4LocalOS(path));
			if (imageFile.exists()) {
				imageIcon = new ImageIcon(imageFile.getAbsolutePath());
			} 
		}
		if (imageIcon==null) {
			// --- 2b. Try sub folder 'project' ---------------------
			path = "/projects" + path2Image;
			imageFile = new File(PathHandling.getPathName4LocalOS(path));
			if (imageFile.exists()) {
				imageIcon = new ImageIcon(imageFile.getAbsolutePath());
			} 
		}
		if (imageIcon==null) {
			// --- 2c. Try absolute sub folder 'project' ---------------------
			path = Application.getGlobalInfo().getPathProjects() + path2Image;
			imageFile = new File(PathHandling.getPathName4LocalOS(path));
			if (imageFile.exists()) {
				imageIcon = new ImageIcon(imageFile.getAbsolutePath());
			} 
		}
		if (imageIcon!=null) {
			imageIcon.setDescription(path2Image);
		}
		return imageIcon;
    }
	
	
 	/**
	 * Gets the graph spread as Rectangle.
	 * 
	 * @param graph the graph
	 * @return the graph spread
	 */
	public static Rectangle2D getGraphSpreadDimension(Graph<GraphNode, GraphEdge> graph) {
		if (graph == null) {
			return new Rectangle2D.Double(0, 0, 0, 0);
		}
		return getGraphSpreadDimension(graph.getVertices());
	}
	/**
	 * Gets the vertices spread dimension.
	 *
	 * @param graphNodes the graph nodes
	 * @return the vertices spread dimension
	 */
	public static Rectangle2D getGraphSpreadDimension(Collection<GraphNode> graphNodes) {

		int count = 0;
		double x_min = 0;
		double x_max = 0;
		double y_min = 0;
		double y_max = 0;

		GraphNode[] nodes = graphNodes.toArray(new GraphNode[graphNodes.size()]);
		for (GraphNode node : nodes) {
			double x = node.getPosition().getX();
			double y = node.getPosition().getY();

			if (count == 0) {
				x_min = x;
				x_max = x;
				y_min = y;
				y_max = y;
			}

			if (x < x_min) x_min = x;
			if (x > x_max) x_max = x;
			if (y < y_min) y_min = y;
			if (y > y_max) y_max = y;
			count++;
		}
		return new Rectangle2D.Double(x_min, y_min, x_max - x_min, y_max - y_min);
	}
 	
	/**
	 * The Class Colors.
	 */
	public final static class Colors {

		/** The Constant TB_BACKGROUND. */
		public final static Color TB_BACKGROUND = Color.WHITE;
		/** The Constant TB_ALTERNATEROWCOLOR. */
		public final static Color TB_ALTERNATEROWCOLOR = new Color(242, 242, 242);
		/** The Constant TB_HIGHLIGHT. */
		public final static Color TB_HIGHLIGHT = new Color(57, 105, 138);

		/** The Constant TB_TEXTFOREGROUND. */
		public final static Color TB_TEXTFOREGROUND = new Color(35, 35, 36);
		/** The Constant TB_TEXTFOREGROUND_SELECTED. */
		public final static Color TB_TEXTFOREGROUND_SELECTED = Color.WHITE;

		/**
		 * Sets the color for a component that is located in a cell of a JTable.
		 *
		 * @param comp the JComponent
		 * @param row the row
		 * @param isSelected the is selected
		 */
		public static void setTableCellRendererColors(JComponent comp, int row, boolean isSelected) {

			// --- do the settings --------------
			comp.setOpaque(true);
			if (isSelected == true) {
				comp.setForeground(GraphGlobals.Colors.TB_TEXTFOREGROUND_SELECTED);
				comp.setBackground(GraphGlobals.Colors.TB_HIGHLIGHT);

			} else {
				comp.setForeground(GraphGlobals.Colors.TB_TEXTFOREGROUND);
				if (row%2==0) {
					comp.setBackground(GraphGlobals.Colors.TB_BACKGROUND);
				} else {
					comp.setBackground(GraphGlobals.Colors.TB_ALTERNATEROWCOLOR);			
				}
			}

			// --- In case of debugging ---------
			if (debug==false) {
				return;

			}
			if (colorPropertiesAlreadyPrinted==false) {
				List<String> colors = new ArrayList<String>();
				for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
					if (entry.getValue() instanceof Color) {
						colors.add((String) entry.getKey()); // all the keys are strings
					}
				}
				Collections.sort(colors);
				for (String name : colors) {
					System.out.println(name);
				}
				
				// --- Print the current color setting ----
				printColorSetting();

				colorPropertiesAlreadyPrinted = true;
			}

		}
		
		/**
		 * Prints the color settings for table cells.
		 */
		public static void printColorSetting() {
			
			Color color = TB_BACKGROUND;
			System.out.println("TB_BACKGROUND => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");

			color = TB_ALTERNATEROWCOLOR;
			System.out.println("TB_ALTERNATEROWCOLOR => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");
			
			color = TB_HIGHLIGHT;
			System.out.println("TB_HIGHLIGHT => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");

			color = TB_TEXTFOREGROUND;
			System.out.println("TB_TEXTFOREGROUND => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");
			
			color = TB_TEXTFOREGROUND_SELECTED;
			System.out.println("TB_TEXTFOREGROUND_SELECTED => " + " new Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ");");
			
		}
		

	} // end color sub class

	
} // end class
