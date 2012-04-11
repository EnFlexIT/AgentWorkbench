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

import gasmas.transfer.zib.cdf.CombinedDecisions;
import gasmas.transfer.zib.cs.CompressorStationsType;
import gasmas.transfer.zib.net.CompressorStationType;
import gasmas.transfer.zib.net.ControlValveType;
import gasmas.transfer.zib.net.GasConnectionType;
import gasmas.transfer.zib.net.GasNetwork;
import gasmas.transfer.zib.net.GasNodeType;
import gasmas.transfer.zib.net.PipeType;
import gasmas.transfer.zib.net.ResistorType;
import gasmas.transfer.zib.net.ShortPipeType;
import gasmas.transfer.zib.net.ValveType;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import agentgui.core.application.Application;
import agentgui.envModel.graph.controller.AddComponentDialog;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.NetworkModelFileImporter;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.GraphNodePairs;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;

public class OGE_Importer extends NetworkModelFileImporter {

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
		
		NetworkModel networkModel = new NetworkModel();
		networkModel.setGeneralGraphSettings4MAS(this.graphController.getNetworkModel().getGeneralGraphSettings4MAS());
		
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
						translateGasNetwork2NetworkModel();
					} else {
						return;
					}
				} else {
					return;
				}
				
				// ------------------------------------------------------------
				// --- Import the *.cs file -----------------------------------
				CompressorStationsType compressorStations = importCsFile(csFile);
				
				// ------------------------------------------------------------
				// --- Import the *.cdf file ----------------------------------
				CombinedDecisions combinedDecisions = importCdfFile(cdfFile);
				
			}
		});
		// ----------------------------------------------------------------
		return networkModel;
	}

	
	/**
	 * Translate GasNetwork to NetworkNodel.
	 * @param gasNetwork the gas network
	 */
	private void translateGasNetwork2NetworkModel() {
		
		AddComponentDialog componentFactory = new AddComponentDialog(this.graphController);
		
		// --------------------------------------------------------------------
		// --- Run through the nodes of the network ---------------------------
		for (GasNodeType gasNodeType : this.GNW_Nodes.values()) {

			String className = gasNodeType.getClass().getSimpleName();
			TypeDescription typeDescription = this.GNW_Types4Mapping.get(className);
			String mapNode2Component = typeDescription.getMapToComponent();
			if (mapNode2Component!=null) {
				
				// --- Create the component for this single node --------------
				NetworkModel compNetModel = componentFactory.getNetworkModel4Component(mapNode2Component);
				// --- Get the new NetworkComponent ---------------------------
				NetworkComponent netComp = compNetModel.getNetworkComponents().values().iterator().next();
				// --- Get the new GraphNode ----------------------------------
				GraphNode graphNode = compNetModel.getGraph().getVertices().iterator().next();
				
				// ------------------------------------------------------------
				// --- Do renaming of NetworkComponent and GraphNode ----------
				compNetModel.renameNetworkComponent(netComp.getId(), gasNodeType.getId());
				compNetModel.renameGraphNode(graphNode.getId(), gasNodeType.getId());
				
				// --- Define the right position ------------------------------
				double newX = gasNodeType.getX().doubleValue();
				double newY = gasNodeType.getY().doubleValue();
				Point2D pos = new Point2D.Double(newX, newY);
				graphNode.setPosition(pos);
				
				// --- Assign to the import NetworkModel ----------------------
				this.graphController.getNetworkModel().mergeNetworkModel(compNetModel, null, false);
				this.graphController.addAgent(netComp);
			}
			
		}
		
		// --------------------------------------------------------------------
		// --- Run through the connections of the network ---------------------
		for (GasConnectionType connection : this.GNW_Connections.values()) {
			
			String className = connection.getClass().getSimpleName();
			TypeDescription typeDescription = this.GNW_Types4Mapping.get(className);
			String mapConnection2Component = typeDescription.getMapToComponent();
			if (mapConnection2Component!=null) {

				String connectionID = connection.getId();
				String connectionNodeFrom = connection.getFrom();
				String connectionNodeTo = connection.getTo();
				GasNodeType gasNodeFrom = this.GNW_Nodes.get(connectionNodeFrom );
				GasNodeType gasNodeTo = this.GNW_Nodes.get(connectionNodeTo );
				
				// --- Create the component for this single connection --------
				NetworkModel compNetModel = componentFactory.getNetworkModel4Component(mapConnection2Component);
				// --- Get the new NetworkComponent ---------------------------
				NetworkComponent netComp = compNetModel.getNetworkComponents().values().iterator().next();
				// --- Get the new GraphNode ----------------------------------
				Iterator<GraphNode> nodeIt = compNetModel.getGraph().getVertices().iterator();
				GraphNode compNetGraphNodeFrom = nodeIt.next();
				GraphNode compNetGraphNodeTo = nodeIt.next();
				
				// ------------------------------------------------------------
				// --- Do renaming of NetworkComponent and GraphNode ----------
				compNetModel.renameNetworkComponent(netComp.getId(), connectionID);
				compNetModel.renameGraphNode(compNetGraphNodeFrom.getId(), gasNodeFrom.getId());
				compNetModel.renameGraphNode(compNetGraphNodeTo.getId(), gasNodeTo.getId());
				
				// --- Define the right position ------------------------------
				double newX = gasNodeFrom.getX().doubleValue();
				double newY = gasNodeFrom.getY().doubleValue();
				Point2D pos = new Point2D.Double(newX, newY);
				compNetGraphNodeFrom.setPosition(pos);
				
				// --- Define the right position ------------------------------
				newX = gasNodeTo.getX().doubleValue();
				newY = gasNodeTo.getY().doubleValue();
				pos = new Point2D.Double(newX, newY);
				compNetGraphNodeTo.setPosition(pos);
				
				// --- Is one oft the GraphNodes already there ? --------------
				GraphNode nmGraphNodeFrom = (GraphNode) this.graphController.getNetworkModel().getGraphElement(compNetGraphNodeFrom.getId());
				GraphNodePairs pairFrom = null;
				if (nmGraphNodeFrom!=null) {
					compNetModel.renameGraphNode(compNetGraphNodeFrom.getId(), compNetGraphNodeFrom.getId() + "_1");
					HashSet<GraphNode> nodeHash = new HashSet<GraphNode>();
					nodeHash.add(compNetGraphNodeFrom);
					pairFrom = new GraphNodePairs(nmGraphNodeFrom, nodeHash);
				}

				GraphNode nmGraphNodeTo = (GraphNode) this.graphController.getNetworkModel().getGraphElement(compNetGraphNodeTo.getId());				
				GraphNodePairs pairTo = null;
				if (nmGraphNodeTo!=null) {
					compNetModel.renameGraphNode(compNetGraphNodeTo.getId(), compNetGraphNodeTo.getId() + "_1");
					HashSet<GraphNode> nodeHash = new HashSet<GraphNode>();
					nodeHash.add(compNetGraphNodeTo);
					pairTo = new GraphNodePairs(nmGraphNodeFrom, nodeHash);
				}
				
				
				// --- Assign to the import NetworkModel ----------------------
				this.graphController.getNetworkModel().mergeNetworkModel(compNetModel, null, false);
				this.graphController.addAgent(netComp);
				
				if (pairFrom!=null) {
					this.graphController.getNetworkModel().mergeNodes(pairFrom);	
				}
				if (pairTo!=null) {
					this.graphController.getNetworkModel().mergeNodes(pairTo);	
				}
				
				
				// --- 
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
		this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Reload));
		
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
	
	/**
	 * Import cs file.
	 *
	 * @param file the graph file
	 * @return the gas network
	 */
	@SuppressWarnings("unchecked")
	private CompressorStationsType importCsFile(File file) {

		// --- Import *.net file ------------------------------------
		JAXBElement<CompressorStationsType> jaxbElement = null;
		CompressorStationsType compressorStations=null;
		try {
			JAXBContext context = JAXBContext.newInstance("gasmas.transfer.zib.cs");
			Unmarshaller unmarshaller = context.createUnmarshaller(); 
			FileInputStream fileInputStream = new FileInputStream(file);
			jaxbElement = (JAXBElement<CompressorStationsType>) unmarshaller.unmarshal(fileInputStream);
			compressorStations = (CompressorStationsType) jaxbElement.getValue();
			fileInputStream.close();
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return compressorStations;
	}
	
	/**
	 * Import net file.
	 *
	 * @param graphFile the graph file
	 * @return the gas network
	 */
	private CombinedDecisions importCdfFile(File file) {

		// --- Import *.net file ------------------------------------
		CombinedDecisions combinedDecisions=null;
		try {
			JAXBContext context = JAXBContext.newInstance("gasmas.transfer.zib.cdf");
			Unmarshaller unmarshaller = context.createUnmarshaller(); 
			FileInputStream fileInputStream = new FileInputStream(file);
			combinedDecisions = (CombinedDecisions) unmarshaller.unmarshal(fileInputStream);
			fileInputStream.close();
			
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return combinedDecisions;
	}
	
	
}
