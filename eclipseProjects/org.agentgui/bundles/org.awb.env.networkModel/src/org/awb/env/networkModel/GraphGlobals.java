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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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
	
 	// ------------------------------------------------------------------------
 	// --- Some help method for buffered images -------------------------------
 	// ------------------------------------------------------------------------
	/**
	 * Converts an {@link Image} to a {@link BufferedImage}.
	 * @param image The image
	 * @return The buffered image
	 */
	public static BufferedImage convertToBufferedImage(Image image) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return bufferedImage;
	}

	/**
	 * Replaces a specified color with another one in an image.
	 * @param image    The image
	 * @param oldColor The color that will be replaced
	 * @param newColor The new color
	 * @return The image
	 */
	public static BufferedImage exchangeColor(BufferedImage image, Color oldColor, Color newColor) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				Color currentColor = new Color(image.getRGB(x, y));
				if (currentColor.equals(oldColor)) {
					image.setRGB(x, y, newColor.getRGB());
				}
			}
		}
		return image;
	}
	/**
	 * Scales the buffered image by its height and it width with the specified scale multiplier.
	 * @param scrImage        the buffered image
	 * @param scaleMultiplier the scale multiplier
	 * @return the buffered image
	 */
	public static BufferedImage scaleBufferedImage(BufferedImage scrImage, int scaleMultiplier) {
		
		if (scrImage==null || scaleMultiplier==1) return scrImage;
		
	    BufferedImage scaledImage = null;
	    try {
	    	// --- Resize the source image ----------------
	    	int newWidth  = scrImage.getWidth() * scaleMultiplier;
	    	int newHeight = scrImage.getHeight() * scaleMultiplier;

	    	scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TRANSLUCENT);
	    	Graphics2D g2d = scaledImage.createGraphics();
	    	g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    	g2d.drawImage(scrImage, 0, 0, newWidth, newHeight, null);
	    	g2d.dispose();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			// -- Return source image as backup -----------
			scaledImage = scrImage;
		}
	    return scaledImage;
	} 	
 	
	
 	// ------------------------------------------------------------------------
 	// --- Some global help method for a Graph --------------------------------
 	// ------------------------------------------------------------------------
 	/**
	 * Gets the graph spread as Rectangle.
	 * 
	 * @param graph the graph
	 * @return the graph spread
	 */
	public static GraphRectangle2D getGraphSpreadDimension(Graph<GraphNode, GraphEdge> graph) {
		return new GraphRectangle2D(graph);
	}
	/**
	 * Gets the vertices spread dimension.
	 *
	 * @param graphNodes the graph nodes
	 * @return the vertices spread dimension
	 */
	public static GraphRectangle2D getGraphSpreadDimension(Collection<GraphNode> graphNodes) {
		return new GraphRectangle2D(graphNodes);
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
