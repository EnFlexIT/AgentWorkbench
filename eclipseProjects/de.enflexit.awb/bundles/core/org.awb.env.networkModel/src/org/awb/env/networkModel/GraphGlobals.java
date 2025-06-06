package org.awb.env.networkModel;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.BackingStoreException;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;
import de.enflexit.common.PathHandling;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class Globals for global constant values of the Graph or Network model.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public final class GraphGlobals {

	public static final String PREF_SHOW_LAYOUT_TOOLBAR = "ShowLayoutToolBar";
	public static final String MISSING_ICON = "MissingIcon";
	
	private static String pathImages = "/icons/";

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
	 * Searches and returns the image url for the specified image reference.
	 *
	 * @param imageRef the image reference
	 * @return the URL of the image 
	 */
	public static URL getImageURL(String imageRef){
		
		// --- Abort URL generation ---------------------------------
		if (imageRef.equals(MISSING_ICON)==true) return null;

		// --- Resource by the class loader, as configured ----------
		URL url = GraphGlobals.class.getClass().getResource(imageRef);
		if (url!=null && isUrlPointToImage(url)) return url;
		
		// --- Prepare folder for projects and project name ---------
		String projectsFolder = Application.getGlobalInfo().getPathProjects();
		projectsFolder = projectsFolder.replace("\\", "/");
		String projectName = Application.getProjectFocused().getProjectFolder();
				
		// --- Default: by file, in projects, relative --------------
		String extImageRef = (projectsFolder + "/" + projectName + imageRef).replace("//", "/");
		url = getURLfromPath(extImageRef);
		if (url!=null && isUrlPointToImage(url)) return url;

		// --- Alternative: by file, in projects, absolute ----------
		extImageRef = (projectsFolder + imageRef).replace("//", "/");
		url = getURLfromPath(extImageRef);
		if (url!=null && isUrlPointToImage(url)) return url;
		
		// --- Nothing found ----------------------------------------
		return null;
	}
	/**
	 * Gets the URL from path.
	 *
	 * @param path the path
	 * @return the URl from provided path - can be null
	 */
	public static URL getURLfromPath(String path) {
		URL url = null;
		try {
			url = new URL("file", null, -1, path);
		} catch (MalformedURLException urlEx) {
			//urlEx.printStackTrace();
		}
		return url;
	}
	/**
	 * Checks if the provided URL points to an image file.
	 *
	 * @param url the url
	 * @return true, if the provided url point to an image
	 */
	public static boolean isUrlPointToImage(URL url) {
		try {
			return ImageIO.read(url)!=null;
		} catch (IOException e) {
			//e.printStackTrace();
			//nothing to do, as expected to fail
		}
		return false;
	}
 	
	// ------------------------------------------------------------------------
 	// --- Some help method to create a simple FileFilter ---------------------
 	// ------------------------------------------------------------------------
	/**
	 * Creates a FileFilter accepting the specified file type or directories.
	 * @param fileTypeExtension the file type extension
	 * @param fileTypeDescription the file type description
	 * @return the file filter
	 */
	public static FileFilter createFileFilter(String fileTypeExtension, String fileTypeDescription) {
			
		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory()==true) return true;
		        String path = file.getAbsolutePath();
		        if (path!=null && path.isBlank()==false) {
		        	if (path.toLowerCase().endsWith(fileTypeExtension.toLowerCase())) {
		        		return true;
		            } else {
		                return false;
		            }
		        }		
				return false;
			}
			@Override
			public String getDescription() {
				return fileTypeDescription;
			}
		};
		return fileFilter;
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
