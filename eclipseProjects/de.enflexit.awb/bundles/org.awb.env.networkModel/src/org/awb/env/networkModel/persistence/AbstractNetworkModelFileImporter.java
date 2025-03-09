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
package org.awb.env.networkModel.persistence;

import java.io.File;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

import de.enflexit.awb.simulation.environment.AbstractEnvironmentModel;


/**
 * Classes that should work as import components for the 
 * GraphEnvironmentController must extend this class. 
 * 
 * @see GraphEnvironmentController
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class AbstractNetworkModelFileImporter {
	
	/** The current settings for the graph environment */
	protected GraphEnvironmentController graphController;
	
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
	 * Gets a file filter accepting the specified file type or directories.
	 * @param fileTypeExtension the file type extension
	 * @param fileTypeDescription the file type description
	 * @return the file filter
	 */
	protected FileFilter createFileFilter(String fileTypeExtension, String fileTypeDescription) {
		return GraphGlobals.createFileFilter(fileTypeExtension, fileTypeDescription);
	}
	
	/**
	 * Sets the graph environment controller.
	 * @param graphController the new graph environment controller
	 */
	public void setGraphController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	
	/**
	 * Gets the file filters for this network importer
	 * @return the file filters
	 */
	public abstract List<FileFilter> getFileFilters();
	
}
