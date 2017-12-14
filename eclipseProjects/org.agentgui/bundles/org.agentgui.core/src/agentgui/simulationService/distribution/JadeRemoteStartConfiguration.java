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
package agentgui.simulationService.distribution;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import agentgui.core.application.Application;
import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.simulationService.ontology.RemoteContainerConfig;
import de.enflexit.common.classLoadService.ObjectInputStreamForClassLoadService;

/**
 * The class JadeRemoteStartConfiguration serves as data model for the
 * a remote container start in case that the current installation
 * requires the installation of a feature.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeRemoteStartConfiguration implements Serializable {

	private static final long serialVersionUID = -8262061775133630087L;
	
	public static String DEFAULT_CONFIGURATION_FILE_NAME = "reStaCo.bin";
	
	private File projectPath;
	private RemoteContainerConfig reCoCo;
	
	
	/**
	 * Instantiates a new remote start configuration.
	 */
	public JadeRemoteStartConfiguration() { }
	
	/**
	 * Instantiates a new remote start configuration.
	 *
	 * @param projectPath the project path
	 * @param reCoCo the re co co
	 */
	public JadeRemoteStartConfiguration(File projectPath, RemoteContainerConfig reCoCo) {
		this.setProjectPath(projectPath);
		this.setRemoteContainerConfig(reCoCo);
	}

	/**
	 * Gets the project path.
	 * @return the project path
	 */
	public File getProjectPath() {
		return projectPath;
	}
	/**
	 * Sets the project path.
	 * @param projectPath the new project path
	 */
	public void setProjectPath(File projectPath) {
		this.projectPath = projectPath;
	}

	/**
	 * Gets the RemoteContainerConfig .
	 * @return the RemoteContainerConfig 
	 */
	public RemoteContainerConfig getRemoteContainerConfig() {
		return reCoCo;
	}
	/**
	 * Sets the RemoteContainerConfig .
	 * @param reCoCo the new RemoteContainerConfig 
	 */
	public void setRemoteContainerConfig(RemoteContainerConfig reCoCo) {
		this.reCoCo = reCoCo;
	}
	
	// ------------------------------------------------------------------------
	// --- From here, static help methods for the file handling can be found --
	// ------------------------------------------------------------------------	
	/**
	 * Returns the default configuration file name.
	 * @return the default configuration file name
	 */
	public static File getDefaultConfigurationFile() {
		return new File(Application.getGlobalInfo().getPathProjects() + DEFAULT_CONFIGURATION_FILE_NAME);
	}
	/**
	 * Deletes the default remote start configuration file.
	 */
	public static boolean deleteRemoteStartConfiguration() {
		return deleteRemoteStartConfiguration(getDefaultConfigurationFile());
	}
	/**
	 * Deletes the specified remote start configuration file.
	 * @param configurationFile the configuration file
	 */
	public static boolean deleteRemoteStartConfiguration(File configurationFile) {
		if (configurationFile.exists()==true) {
			return configurationFile.delete();
		}
		return false;
	}
	/**
	 * Saves the specified jade remote start configuration to the default location.
	 *
	 * @param reStaCo the JadeRemoteStartConfiguration to save
	 * @return true, if successful
	 * @see #getDefaultConfigurationFile()
	 */
	public static boolean saveRemoteStartConfiguration(JadeRemoteStartConfiguration reStaCo) {
		return saveRemoteStartConfiguration(getDefaultConfigurationFile(), reStaCo);
	}
	/**
	 * Saves the specified jade remote start configuration.
	 *
	 * @param configurationFile the configuration file
	 * @param reStaCo the JadeRemoteStartConfiguration to save
	 * @return true, if successful
	 */
	public static boolean saveRemoteStartConfiguration(File configurationFile, JadeRemoteStartConfiguration reStaCo) {
		
		boolean successful = false;
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(configurationFile);
			out = new ObjectOutputStream(fos);
			out.writeObject(reStaCo);
			successful = true;
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
	    	if (out!=null) {
	    		try {
	    			out.close();
	    		} catch (IOException ioEx) {
	    			ioEx.printStackTrace();
	    		}
	    	}
	    	if (fos!=null) {
	    		try {
	    			fos.close();
	    		} catch (IOException ioEx) {
	    			ioEx.printStackTrace();
	    		}
	    	}
		}
		return successful;
	}
	/**
	 * Load the file with the jade remote start configuration from the default location.
	 *
	 * @return the jade remote start configuration
	 * @see #getDefaultConfigurationFile()
	 */
	public static JadeRemoteStartConfiguration loadRemoteStartConfiguration() {
		return loadRemoteStartConfiguration(getDefaultConfigurationFile());
	}
	/**
	 * Load the specified file with the jade remote start configuration.
	 *
	 * @param configurationFile the configuration file
	 * @return the remote start configuration
	 */
	public static JadeRemoteStartConfiguration loadRemoteStartConfiguration(File configurationFile) {
		
		JadeRemoteStartConfiguration reCoCo = null;
		if (configurationFile.exists()) {

			FileInputStream fis = null;
			ObjectInputStreamForClassLoadService inStream = null;
			try {
				fis = new FileInputStream(configurationFile);
				inStream = new ObjectInputStreamForClassLoadService(fis, ClassLoadServiceUtility.class);
				reCoCo = (JadeRemoteStartConfiguration) inStream.readObject();

			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} finally {
				try {
					if (inStream!=null) inStream.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				try {
					if (fis!=null) fis.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return reCoCo;
	}
	
}
