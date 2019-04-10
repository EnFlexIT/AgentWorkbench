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
package org.awb.env.networkModel.controller;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.awb.env.networkModel.networkModel.NetworkModel;

import agentgui.simulationService.environment.AbstractEnvironmentModel;
import agentgui.simulationService.environment.EnvironmentModel;

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
	protected GraphEnvironmentController graphController;
	/** The file extension used for filtering in JFileChooser selecting the file to import */
	protected String fileTypeExtension;
    /** The file type description for the JFileChooser for selecting the file to import */
	protected String fileTypeDescription;
    /** The file filter. */
	protected FileFilter fileFilter;

	
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
	 * This method has to import a {@link NetworkModel} from the file. 
	 * @param graphFile The file containing the external network model definition.
	 * @return the NetworkModel to be used within Agent.Workbnech.
	 */
	public abstract NetworkModel importNetworkModelFromFile(File graphFile);
	
	/**
	 * May return an abstract environment model for the {@link EnvironmentModel}.
	 * <b>Overwrite</b> this method, if your importer provides an abstract environment model after a successful import.
	 * @return the abstract environment model that comes with the imported {@link NetworkModel}
	 * @see EnvironmentModel
	 * @see EnvironmentModel#setAbstractEnvironment(agentgui.simulationService.environment.AbstractEnvironmentModel)
	 */
	public AbstractEnvironmentModel getAbstractEnvironmentModel() {
		return null;
	}
	/**
	 * Since the instance of the importer will remain after import, this method should reset
	 * all local variables of the importer to reduce the RAM usage.
	 */
	public abstract void cleanupImporter();
	
	
	
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
			        	if (path.endsWith(fileTypeExtension) || path.endsWith(fileTypeExtension.toLowerCase()) || path.endsWith(fileTypeExtension.toUpperCase())) {
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
