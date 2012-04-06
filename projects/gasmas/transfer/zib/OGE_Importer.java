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
package gasmas.transfer.zib;

import gasmas.transfer.zib.net.CompressorStationType;
import gasmas.transfer.zib.net.ControlValveType;
import gasmas.transfer.zib.net.GasConnectionType;
import gasmas.transfer.zib.net.GasNetwork;
import gasmas.transfer.zib.net.GasNodeType;
import gasmas.transfer.zib.net.PipeType;
import gasmas.transfer.zib.net.ResistorType;
import gasmas.transfer.zib.net.ShortPipeType;
import gasmas.transfer.zib.net.ValveType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import agentgui.core.application.Application;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.NetworkModelFileImporter;
import agentgui.envModel.graph.networkModel.NetworkModel;

public class OGE_Importer extends NetworkModelFileImporter {

	private NetworkModel networkModel = null;
	
	private HashMap<String, TypeDescription> GNW_Types4Mapping = null;
	private HashMap<String, GasConnectionType> GNW_Connections = null;
	private HashMap<String, GasNodeType> GNW_Nodes = null;
	
	/**
	 * Instantiates a new OGE / ZIB GraphFileImporter.
	 *
	 * @param graphController the graph controller
	 * @param fileTypeExtension the file type extension
	 * @param fileTypeDescription the file type description
	 */
	public OGE_Importer(GraphEnvironmentController graphController, String fileTypeExtension, String fileTypeDescription) {
		super(graphController, fileTypeExtension, fileTypeDescription);
	}

	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.controller.GraphFileImporter#importGraphFromFile(java.io.File)
	 */
	@Override
	public NetworkModel importGraphFromFile(File graphFile) {
		
		this.networkModel = new NetworkModel();
		this.networkModel.setGeneralGraphSettings4MAS(this.graphController.getNetworkModel().getGeneralGraphSettings4MAS());
		
		String fileName = graphFile.getAbsolutePath();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		
		final File netFile = new File(fileName + ".net");
		final File csFile = new File(fileName + ".cs");
		final File cdfFile = new File(fileName + ".cdf");
		
		// --------------------------------------------------------------------
		// --- Build the new NetworkModel (in an own thread) ------------------
		// --------------------------------------------------------------------
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				// ------------------------------------------------------------
				// --- Import the *.net file ----------------------------------
				GasNetwork gasNetwork = importNetFile(netFile);
				if (gasNetwork!=null) {
					// --- Fill local HashMaps --------------------------------
					fillLocalHashMaps(gasNetwork);
					// --- Get the user mapping for external/internal mapping -
					if (getUserMapping4Components()==true) {
						translateGasNetwork2NetworkModel(gasNetwork);
					} else {
						return;
					}
				} else {
					return;
				}
				
				// ------------------------------------------------------------
				// --- Import the *.cs file -----------------------------------

				
				// ------------------------------------------------------------
				// --- Import the *.cdf file ----------------------------------
				
				
			}
		});
		// ----------------------------------------------------------------
		return this.networkModel;
	}

	
	/**
	 * Translate GasNetwork to NetworkNodel.
	 *
	 * @param gasNetwork the gas network
	 */
	private void translateGasNetwork2NetworkModel(GasNetwork gasNetwork) {
		
		// --- Run through the connections of the network ---------------------
		for (GasConnectionType connection : this.GNW_Connections.values()) {
			
			HashMap<String, GasNodeType> nodes = getNodes(connection);
			
			if (connection instanceof PipeType) {
				
			} else if (connection instanceof ShortPipeType) {
			
			} else if (connection instanceof ResistorType) {
				
			} else if (connection instanceof ValveType) {
				
			} else if (connection instanceof ControlValveType) {
			
			} else if (connection instanceof CompressorStationType) {
				
			} else {
				System.out.println("Unknown import Type: " + connection.getClass().getName() );	
			}
			
		}
		
	}
	
	/**
	 * Gets the nodes for the specified GasConnectionType.
	 *
	 * @param gasConnectionType the GasConnectionType
	 * @return the nodes
	 */
	private HashMap<String, GasNodeType> getNodes(GasConnectionType gasConnectionType){
		HashMap<String, GasNodeType> nodesFound = new HashMap<String, GasNodeType>();
		nodesFound.put(gasConnectionType.getFrom(), this.GNW_Nodes.get(gasConnectionType.getFrom()));
		nodesFound.put(gasConnectionType.getTo(), this.GNW_Nodes.get(gasConnectionType.getTo()));
		return nodesFound;
	}
	

	/**
	 * Gets the user mapping for the incoming components.
	 * @return the user mapping4 components
	 */
	private boolean getUserMapping4Components() {
		
		//System.out.println(this.GNW_Types4Mapping.toString());
		UserMapping userMapping = new UserMapping(Application.MainWindow, this.GNW_Types4Mapping, this.graphController.getGeneralGraphSettings4MAS());
		userMapping.setVisible(true);
		// --- Wait -------------------
		if (userMapping.isCancelled()==true) {
			return false;
		}
		
		this.GNW_Types4Mapping = userMapping.getExternalTypes();
		return true;
	}
	
	/**
	 * Transfer the nodes and connections into local HashMaps, in order to access them faster.
	 *
	 * @param gasNetwork the gas network
	 */
	private void fillLocalHashMaps(GasNetwork gasNetwork) {
		
		GNW_Types4Mapping = new HashMap<String, TypeDescription>(); 
		GNW_Connections = new HashMap<String, GasConnectionType>();
		GNW_Nodes = new HashMap<String, GasNodeType>();
		
		List<?> connectionList = gasNetwork.getValue().getConnections().getConnection();
		for (int i = 0; i < connectionList.size(); i++) {
			JAXBElement<?> element = (JAXBElement<?>) connectionList.get(i);
			GasConnectionType conType = (GasConnectionType) element.getValue();
			GNW_Connections.put(conType.getId(), conType);
			this.put2TypeMap(conType.getClass());
		}
		
		List<?> nodeList = gasNetwork.getValue().getNodes().getNode();
		for (int i = 0; i < nodeList.size(); i++) {
			JAXBElement<?> element = (JAXBElement<?>) nodeList.get(i);
			GasNodeType nodeType = (GasNodeType) element.getValue();
			GNW_Nodes.put(nodeType.getId(), nodeType);
			this.put2TypeMap(nodeType.getClass());
		}
	}
	
	/**
	 * Put an external type to the local TypeMap, if the type is not already there.
	 *
	 * @param className the class name
	 */
	private void put2TypeMap(Class<?> clazz) {

		String className = clazz.getName();
		String classNameShort = clazz.getSimpleName();
		TypeDescription typeDesc = this.GNW_Types4Mapping.get(classNameShort);
		if (typeDesc==null) {
			typeDesc = new TypeDescription(className);
			this.GNW_Types4Mapping.put(classNameShort, typeDesc);
		} else {
			typeDesc.stepClassOccurrence();
		}
	}
	
	/**
	 * Import net file.
	 *
	 * @param graphFile the graph file
	 * @return the gas network
	 */
	private GasNetwork importNetFile(File graphFile) {

		// --- Import *.net file ------------------------------------
		GasNetwork gasNetwork=null;
		try {
			JAXBContext context = JAXBContext.newInstance("gasmas.transfer.zib.net");
			Unmarshaller unmarshaller = context.createUnmarshaller(); 
			FileInputStream fileInputStream = new FileInputStream(graphFile);
			gasNetwork = (GasNetwork)unmarshaller.unmarshal(fileInputStream);
			fileInputStream.close();
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return gasNetwork;
	}
	
	
}
