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
package org.awb.env.networkModel;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URL;
import java.util.Collection;

import javax.swing.ImageIcon;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.BackingStoreException;

import agentgui.core.application.Application;
import agentgui.core.project.Project;
import de.enflexit.common.PathHandling;
import de.enflexit.geography.coordinates.AbstractCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class Globals for global constant values of the Graph or Network model.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public final class GraphGlobals {

	public static final String PREF_SHOW_LAYOUT_TOOLBAR = "ShowLayoutToolBar";
	
	private static String pathImages = "/org/awb/env/networkModel/img/";

	private static Bundle localBundle;
	private static IEclipsePreferences eclipsePreferences;
	
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
	    
 		if (path2Image==null) return null;

 		String path = null;
 		File imageFile = null;
 		ImageIcon imageIcon = null;
 		
 		
 		// ----------------------------------------------------------
		// --- 1. Try current project locations ---------------------
 		// ----------------------------------------------------------
		Project project = Application.getProjectFocused();
		if (project!=null) {
			// --- 1a. Try in projects sub directory ----------------
			path = project.getProjectFolderFullPath() + path2Image;
			imageFile = new File(PathHandling.getPathName4LocalOS(path));
			if (imageFile.exists()) {
				imageIcon = new ImageIcon(imageFile.getAbsolutePath());
			} 

			// --- 1b. Starts with the projects directory? ----------
			if (imageIcon==null && path2Image.startsWith("/" + project.getProjectFolder())==true) {
				path = Application.getGlobalInfo().getPathProjects() + path2Image;
				imageFile = new File(PathHandling.getPathName4LocalOS(path));
				if (imageFile.exists()) {
					imageIcon = new ImageIcon(imageFile.getAbsolutePath());
				} 
			}
		}
 		
 		// ----------------------------------------------------------
		// --- 2. Search in the loaded packages ---------------------
 		// ----------------------------------------------------------
		// --- 2a. Try the direct way -------------------------------
		if (imageIcon==null) {
			try {
				URL url = GraphGlobals.class.getResource(path2Image);
				imageIcon = new ImageIcon(url);
				
			} catch (Exception ex) {
				imageIcon = null;
			}
		}

		// --- 2b. Try the image package of the GraphEnvironement ---
		if (imageIcon==null) {
			try {
				URL url = GraphGlobals.class.getResource(getPathImages() + path2Image);
				imageIcon = new ImageIcon(url);
				
			} catch (Exception ex) {
				imageIcon = null;
			}
		}
		
 		// ----------------------------------------------------------
		// --- 3. Try folder locations ------------------------------
 		// ----------------------------------------------------------
		if (imageIcon==null) {
			// --- 3a. Try direct folder location -------------------
	 		path = path2Image;
	 		imageFile = new File(PathHandling.getPathName4LocalOS(path));
			if (imageFile.exists()) {
				imageIcon = new ImageIcon(imageFile.getAbsolutePath());
			} 
		}
		if (imageIcon==null) {
			// --- 3b. Try absolute sub folder 'project' ------------
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
		if (graph==null) {
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

		Rectangle2D rect = new Rectangle2D.Double();
		boolean firstNodeAdded = false;
		GraphNode[] nodes = graphNodes.toArray(new GraphNode[graphNodes.size()]);
		for (int i = 0; i < nodes.length; i++) {
			
			try {

				// --- Check if the coordinate needs to be converted ---------- 
				AbstractCoordinate coordToUse = null;
				AbstractCoordinate coord = nodes[i].getCoordinate();
				if (coord instanceof WGS84LatLngCoordinate) {
					coordToUse = ((WGS84LatLngCoordinate) coord).getUTMCoordinate();
				} else {
					coordToUse = coord;
				}
				
				// --- Get x and y to be integrated in the result ------------- 
				double x = coordToUse.getX();
				double y = coordToUse.getY();
				if (firstNodeAdded==false) {
					rect.setRect(x, y, 0, 0);
					firstNodeAdded = true;
				}
				rect.add(x, y);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
		return rect;
	}

	// --------------------------------------------------------------
	// --- Provider methods to access preferences for this bundle ---
	// --------------------------------------------------------------
	/**
	 * Gets the local bundle.
	 * @return the local bundle
	 */
	public static Bundle getLocalBundle() {
		if (localBundle==null) {
			localBundle = FrameworkUtil.getBundle(GraphGlobals.class);
		}
		return localBundle;
	}
	/**
	 * Returns the eclipse preferences.
	 * @return the eclipse preferences
	 */
	public static IEclipsePreferences getEclipsePreferences() {
		if (eclipsePreferences==null) {
			IScopeContext iScopeContext = ConfigurationScope.INSTANCE;
			eclipsePreferences = iScopeContext.getNode(getLocalBundle().getSymbolicName());
		}
		return eclipsePreferences;
	}
	/**
	 * Saves the current preferences.
	 */
	public static void saveEclipsePreferences() {
		try {
			getEclipsePreferences().flush();
		} catch (BackingStoreException bsEx) {
			bsEx.printStackTrace();
		}
	}
	
} 
