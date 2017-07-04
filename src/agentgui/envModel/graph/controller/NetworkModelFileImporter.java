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
package agentgui.envModel.graph.controller;

import java.io.File;
import java.util.Iterator;

import javax.swing.filechooser.FileFilter;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkModel;
import edu.uci.ics.jung.algorithms.layout.Layout;

/**
 * Classes that should work as import components for the 
 * GraphEnvironmentController must extend this class. 
 * 
 * @see GraphEnvironmentController
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class NetworkModelFileImporter {
	
	/** The current settings for the graph environment */
	protected GraphEnvironmentController graphController = null;
	/** The file extension used for filtering in JFileChooser selecting the file to import */
	protected String fileTypeExtension = null;
    /** The file type description for the JFileChooser for selecting the file to import */
	protected String fileTypeDescription = null;
    /** The file filter. */
	protected FileFilter fileFilter = null;

	
	/**
	 * Constructor.
	 *
	 * @param graphController the {@link GraphEnvironmentController}
	 * @param fileTypeExtension the file type extension
	 * @param fileTypeDescription the file type description
	 */
	public NetworkModelFileImporter(GraphEnvironmentController graphController, String fileTypeExtension, String fileTypeDescription){
		this.graphController = graphController;
	    this.fileTypeExtension = fileTypeExtension;
	    this.fileTypeDescription = fileTypeDescription;
	}
	
	/**
	 * Initialize the node positions according to a specified layout
	 * @param network The NetworkModel containing the graph
	 * @param layout The initial layout
	 */
	public void initPosition(NetworkModel network, Layout<GraphNode, GraphEdge> layout){
		Iterator<GraphNode> nodes = network.getGraph().getVertices().iterator();
		while(nodes.hasNext()){
			GraphNode node = nodes.next();
			node.setPosition(layout.transform(node));
		}
		
	}
	
	/**
	 * This method loads the graph from the file and translates it into a JUNG graph. 
	 * @param graphFile The file containing the graph definition.
	 * @return The JUNG graph.
	 */
	public abstract NetworkModel importGraphFromFile(File graphFile);
	
	/**
	 * Returns the extension of the file type the GraphFileLoader can handle
	 * @return The file extension
	 */
	public String getFileTypeExtension() {
		return fileTypeExtension;
	}
	/**
	 * Returns a type string used for GraphFileLoader selection
	 * @return The type String
	 */
	public String getFileTypeDescription() {
		return fileTypeDescription;
	}
	
	/**
	 * Returns the file filter for this type of import.
	 * @return the file filter
	 */
	public FileFilter getFileFilter() {
		if (this.fileFilter==null) {
			this.fileFilter = new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (file.isDirectory()) {
			            return true;
			        }
			        String path = file.getAbsolutePath();
			        if (path != null) {
			        	if (path.endsWith(fileTypeExtension)) {
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
		}
		return this.fileFilter;
	}
	
}
