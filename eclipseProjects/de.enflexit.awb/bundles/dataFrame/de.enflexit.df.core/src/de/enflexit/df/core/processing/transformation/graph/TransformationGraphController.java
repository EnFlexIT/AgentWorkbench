package de.enflexit.df.core.processing.transformation.graph;


import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import de.enflexit.common.ServiceFinder;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.ProcessingDataSourceIntegration;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformation.graph.persistence.PersistentTransformationNode;
import de.enflexit.df.core.processing.transformation.graph.persistence.PersistentTransformationEdge;
import de.enflexit.df.core.processing.transformation.graph.persistence.PersistentTransformationGraph;
import de.enflexit.df.core.processing.transformation.graph.persistence.PersistentTransformationNode.NodeType;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import tech.tablesaw.api.Table;

/**
 * This class manages the transformation graph.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TransformationGraphController implements PropertyChangeListener {

	private ObservableGraph<DataTableNode, DataTransformationEdge> transformationGraph;
	private StaticLayout<DataTableNode, DataTransformationEdge> graphLayout;
	
	private DataController dataController;
	
	private ReentrantLock lockWaitForDTNOs;
	private Condition conditionDtnoAvailable;
	
	private boolean isDebug = false;
	
	/**
	 * Instantiates a new transformation graph controller.
	 * @param dataController the data controller
	 */
	public TransformationGraphController(DataController dataController) {
		this.dataController = dataController;
		this.dataController.addPropertyChangeListener(this);
	}

	/**
	 * Gets the transformation graph.
	 * @return the transformation graph
	 */
	public ObservableGraph<DataTableNode, DataTransformationEdge> getTransformationGraph() {
		if (transformationGraph==null) {
			transformationGraph = new ObservableGraph<DataTableNode, DataTransformationEdge>(new DirectedSparseGraph<DataTableNode, DataTransformationEdge>());
		}
		return transformationGraph;
	}
	
	/**
	 * Gets the graph layout.
	 * @return the graph layout
	 */
	public StaticLayout<DataTableNode, DataTransformationEdge> getGraphLayout() {
		if (graphLayout==null) {
			graphLayout = new StaticLayout<DataTableNode, DataTransformationEdge>(this.getTransformationGraph());
		}
		return graphLayout;
	}
	
	
	/**
	 * Sets the transformation graph.
	 * @param transformationGraph the transformation graph
	 */
	public void setTransformationGraph(Graph<DataTableNode, DataTransformationEdge> transformationGraph) {
		if (transformationGraph instanceof ObservableGraph<DataTableNode, DataTransformationEdge> og) {
			this.transformationGraph = og;
		} else {
			ObservableGraph<DataTableNode, DataTransformationEdge> obsGraph = new ObservableGraph<DataTableNode, DataTransformationEdge>(transformationGraph);
			this.transformationGraph = obsGraph;
		}
		this.resetGraphLayout(this.transformationGraph);
	}
	
	/**
	 * Resets the graph layout for a new graph.
	 * @param transformationGraph the transformation graph
	 */
	private void resetGraphLayout(Graph<DataTableNode, DataTransformationEdge> transformationGraph) {
		this.graphLayout = new StaticLayout<DataTableNode, DataTransformationEdge>(transformationGraph);
	}
	
	/**
	 * Creates a {@link DataTableNode} for the provided {@link AbstractDataSourceDTNO} and adds it to the graph.
	 * @param dataSourceDTNO the data source DTNO
	 * @param position the position
	 */
	public void addDataTableNode(AbstractDataSourceDTNO<?> dataSourceDTNO, Point2D position) {
		DataTableNodeDataSource dtNode = new DataTableNodeDataSource();
		dtNode.setDataSourceDTNO(dataSourceDTNO);
		dtNode.setPosition(position);
		this.addDataTableNode(dtNode);
	}
	
	/**
	 * Adds the provided {@link DataTableNode} to the graph.
	 * @param dataTableNode the node
	 * @param position the position
	 */
	public void addDataTableNode(DataTableNode dataTableNode) {
		this.getTransformationGraph().addVertex(dataTableNode);
		this.getGraphLayout().setLocation(dataTableNode, dataTableNode.getPosition().getX(), dataTableNode.getPosition().getY());
	}
	
	/**
	 * Adds a transformation edge between the provided data nodes.
	 * @param edge the edge
	 * @param nodeFrom the node from
	 * @param nodeTo the node to
	 */
	public void addTransformationEdge(DataTransformationEdge edge, DataTableNode nodeFrom, DataTableNode nodeTo) {
		this.getTransformationGraph().addEdge(edge, nodeFrom, nodeTo);
	}
	
	/**
	 * Removes the provided data table node from the graph. Might fail if the node
	 * is part of a transformation, then the transformation must be removed first.
	 * @param nodeToRemove the node to remove
	 * @return true, if successful
	 */
	public boolean removeDataTableNode(DataTableNode nodeToRemove) {
		if (this.getTransformationGraph().getOutEdges(nodeToRemove).size()>0) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The node is part of existing transformations, can't be removed!");
			return false;
		} else {
			this.getTransformationGraph().removeVertex(nodeToRemove);
			return true;
		}
	}
	
	/**
	 * Removes the node for the provided data source from the graph. Might fail if the
	 * node is part of a transformation, then the transformation must be removed first. 
	 * @param dataSourceDTNO the data source DTNO
	 * @return true, if successful
	 */
	public boolean removeDataTableNode(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		DataTableNode nodeToRemove = this.findNodeForDataSource(dataSourceDTNO);
		if (nodeToRemove!=null) {
			return this.removeDataTableNode(nodeToRemove);
		}
		return false;
	}
	
	/**
	 * Removes the data transformation with the provided output node.
	 * @param resultNode the result node
	 * @return true, if successful
	 */
	public boolean removeDataTransformation(DataTableNodeTransformationResult resultNode) {
		if (this.getTransformationGraph().getOutEdges(resultNode).size()>0) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The transformation is an input for subsequent transformations, can't be removed!");
			return false;
		} else {
			
			// --- Collect all involved edges first (can't remove directly -> CuncurrentModificationException)
			ArrayList<DataTransformationEdge> edgesToRemove = new ArrayList<DataTransformationEdge>();
			for (DataTransformationEdge edge : this.getTransformationGraph().getInEdges(resultNode)) {
				edgesToRemove.add(edge);
			}
			
			// --- Remove all involved elements from the graph
			for (DataTransformationEdge edge : edgesToRemove) {
				this.getTransformationGraph().removeEdge(edge);
			}
			this.getTransformationGraph().removeVertex(resultNode);
			
			return true;
		}
	}
	
	/**
	 * Finds the graph node representing the provided data source.
	 * @param dataSourceDTNO the data source DTNO
	 * @return the data table node, null if not found
	 */
	public DataTableNode findNodeForDataSource(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		for (DataTableNode node : this.getTransformationGraph().getVertices()) {
			if (node instanceof DataTableNodeDataSource && ((DataTableNodeDataSource)node).getDataSourceDTNO()==dataSourceDTNO) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * Notifies the transformation graph controller about a modified transformation.
	 * @param transformation the transformation
	 */
	public void transformationModified(AbstractDataTransformation transformation) {
		
		// --- Find all transformations that might be affected by the change ------- ----
		ArrayList<AbstractDataTransformation> dependentTransformations = new ArrayList<AbstractDataTransformation>();
		dependentTransformations.addAll(this.findDependentTransformations(transformation));
		
		// --- Reset the transformation results, since they might be outdated ----------- 
		for (AbstractDataTransformation resetTransformation : dependentTransformations) {
			resetTransformation.resetResult();
		}
	}
	
	/**
	 * Finds all subsequent transformations that depend on the result of the provided transformations, directly or indirectly.
	 * @param transformation the transformation
	 * @return the list of dependent transformations
	 */
	private ArrayList<AbstractDataTransformation> findDependentTransformations(AbstractDataTransformation transformation){
		
		HashSet<AbstractDataTransformation> dependentTransformaitons = new HashSet<AbstractDataTransformation>();
		
		// --- Check all defined transformations ------------------------------
		for (DataTransformationEdge edge : this.getTransformationGraph().getEdges()) {
			AbstractDataTransformation edgeTransformation = edge.getDataTransformation();
			
			// --- Direct dependency to the transformation in question --------
			if (edgeTransformation.getInputNodes().contains(transformation.getOutputNode())) {
				dependentTransformaitons.add(edgeTransformation);
				// --- Also find dependencies for this transformation, since they are indirect dependencies
				dependentTransformaitons.addAll(this.findDependentTransformations(edgeTransformation));
			}
		}
		
		return new ArrayList<AbstractDataTransformation>(dependentTransformaitons);
	}
	
	/**
	 * Gets the data table by UUID.
	 * @param uuid the uuid
	 * @return the data table by UUID
	 */
	public Table getDataTableByUUID(String uuid) {
		DataTableNode tableNode = this.getTableNodeByUUID(uuid);
		if (tableNode!=null) {
			return tableNode.getDataTable();
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the table node by UUID.
	 * @param uuid the uuid
	 * @return the table node by UUID
	 */
	public DataTableNode getTableNodeByUUID(String uuid) {
		for (DataTableNode tableNode : this.getTransformationGraph().getVertices()) {
			if (tableNode.getDataSourceUUID().equals(uuid)) {
				return tableNode;
			}
		}
		return null;
	}
	
	/**
	 * Gets the transformation service for the provided transformation name.
	 * @param transformationName the transformation name
	 * @return the transformation service
	 */
	private DataTransformationService getTransformationService(String transformationName) {
		List<DataTransformationService> services = ServiceFinder.findServices(DataTransformationService.class);
		for (DataTransformationService service : services) {
			if (service.getName().equals(transformationName)) {
				return service;
			}
		}
		return null;
	}
	
	/**
	 * Gets the data transformation that leads to the provided data table node.
	 * @param tableNode the table node
	 * @return the transformation for node
	 */
	public AbstractDataTransformation getTransformationForNode(DataTableNode tableNode) {
		if (tableNode instanceof DataTableNodeTransformationResult resultNode) {
			// --- Get the first incoming edge (the transformation is the same instance for all edges) 
			DataTransformationEdge inEdge = this.getTransformationGraph().getInEdges(resultNode).iterator().next();
			return inEdge.getDataTransformation();
		} else {
			// --- No transformation result node -> no transformation ---------
			return null;
		}
	}
	
	/**
	 * Determines the UUIDs of all data sources that are required for this transformation graph.
	 * @param persTransGraph the transformation graph definition.
	 * @return the UUIDs of the required data sources
	 */
	private ArrayList<String> determineRequiredDataSourceUUIDs(PersistentTransformationGraph persTransGraph){
		ArrayList<String> requiredDataSourceUUIDs = new ArrayList<String>();
		for (PersistentTransformationNode dataNode : persTransGraph.getTableNodes()) {
			if (dataNode.getNodeType()==NodeType.DATA_SOURCE) {
				requiredDataSourceUUIDs.add(dataNode.getDataSourceUUID());
			}
		}
		return requiredDataSourceUUIDs;
	};
	
	/**
	 * Checks if the  DTNOs with the specified UUIDs are available.
	 * @param uuidsLists the UUIDs to check
	 * @return true, if successful
	 */
	private boolean checkDTNOsAvailable(ArrayList<String> uuidsLists) {
		
		for (String uuid : uuidsLists) {
			AbstractDataSourceDTNO<?> dtno = dataController.getDataTreeModel().getDataSourceDtnoByExpression("sUUID('" + uuid + "')");
			if (dtno==null) {
				if (this.isDebug==true) System.out.println("[" + this.getClass().getSimpleName() + "] DTNO not available for " + uuid);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Creates an actual transformation graph from the provided {@link PersistentTransformationGraph}.
	 * @param persistentGraph the persistent graph
	 */
	public void loadPersistentGraph(PersistentTransformationGraph persistentGraph, ProcessingDataSourceIntegration dsIntegration) {
		this.loadGraphInThread(persistentGraph, dsIntegration);
	}
	
	/**
	 * Starts a thread to load a persisted transformation graph.
	 * @param persistentGraph the persistent graph
	 */
	private void loadGraphInThread(PersistentTransformationGraph persistentGraph, ProcessingDataSourceIntegration dsIntegration) {
		Thread loadGraphThread = new Thread(new Runnable() {
			@Override
			public void run() {
				TransformationGraphController.this.loadGraph(persistentGraph, dsIntegration);
			}
		}, "PersistenceGraphLoader");
		loadGraphThread.start();
	}
	
	/**
	 * Performs the actual graph loading.
	 * @param persistentGraph the persistent graph
	 */
	private void loadGraph(PersistentTransformationGraph persistentGraph, ProcessingDataSourceIntegration dsIntegration) {
		
		// --- Wait until all required DTNOs are available --------------------
		ArrayList<String> requiredDataSourceUUIDs = this.determineRequiredDataSourceUUIDs(persistentGraph);
		while (this.checkDTNOsAvailable(requiredDataSourceUUIDs)==false) {
			try {
				if (this.isDebug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Missing DTNOs, waiting...");
				this.getLockWaitForDTNOs().lock();
				this.getConditionDtnoAvailable().await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				this.getLockWaitForDTNOs().unlock();
			}
		}
		
		if (this.isDebug==true) System.out.println("[" + this.getClass().getSimpleName() + "] All required DTNOs available, creating the graph");
		
		// --- Create the nodes first, organized by their UUIDs ---------------
		HashMap<String, DataTableNode> nodesByUUID = new HashMap<String, DataTableNode>();
		for (PersistentTransformationNode persistentNode : persistentGraph.getTableNodes()) {
			DataTableNode tableNode = null;
			if (persistentNode.getNodeType()==NodeType.DATA_SOURCE) {
				tableNode = new DataTableNodeDataSource();
				((DataTableNodeDataSource)tableNode).setDataSourceDTNO(dataController.getDataTreeModel().getDataSourceDtnoByExpression("sUUID('" + persistentNode.getDataSourceUUID() + "')"));
			} else if (persistentNode.getNodeType()==NodeType.TRANSFORMATION_RESULT) {
				tableNode = new DataTableNodeTransformationResult();
				((DataTableNodeTransformationResult)tableNode).setDataSourceUUID(persistentNode.getDataSourceUUID());
			}
			
			if (tableNode!=null) {
				tableNode.setPosition(new Point2D.Double(persistentNode.getPosX(), persistentNode.getPosY()));
				this.addDataTableNode(tableNode);
				
				nodesByUUID.put(tableNode.getDataSourceUUID(), tableNode);
			}
		}
		
		// --- Create the edges for the transformations -------------
		for (PersistentTransformationEdge persistentEdge : persistentGraph.getTransformationEdges()) {
			
			DataTransformationService transformationService = this.getTransformationService(persistentEdge.getTransformationName());
			if (transformationService!=null) {
				AbstractDataTransformation  transformation = transformationService.getNewInstance();
				transformation.setTransformationParameters(persistentEdge.getTransformationParameters());
				transformation.setOutputTableName(persistentEdge.getResultTableName());
				DataTableNodeTransformationResult outputNode = (DataTableNodeTransformationResult)nodesByUUID.get(persistentEdge.getOutputNodeUUID());
				transformation.setOutputNode(outputNode);
				for (String inputNodeUUID : persistentEdge.getInputNodeUUIDs()) {
					DataTableNode inputNode = nodesByUUID.get(inputNodeUUID);
					DataTransformationEdge transformationEdge = new DataTransformationEdge();
					transformationEdge.setDataTransformation(transformation);
					this.addTransformationEdge(transformationEdge, inputNode, outputNode);
					transformation.getInputNodes().add(inputNode);
				}
				outputNode.setDataTransformation(transformation);
			}
		}
		
		dsIntegration.addDataTreeSubNodes();
		
	}
	
	/**
	 * Gets the lock that is required for waiting for new DTNOs.
	 * @return the lock wait for DTNOs
	 */
	private synchronized ReentrantLock getLockWaitForDTNOs() {
		if (lockWaitForDTNOs==null) {
			lockWaitForDTNOs = new ReentrantLock();
		}
		return lockWaitForDTNOs;
	}
	
	/**
	 * Gets the condition that will be triggered when a new data source was loaded. 
	 * @return the condition dtno available
	 */
	private synchronized Condition getConditionDtnoAvailable() {
		if (conditionDtnoAvailable==null) {
			conditionDtnoAvailable = this.getLockWaitForDTNOs().newCondition();
		}
		return conditionDtnoAvailable;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(DataController.DC_DATA_LOADED)) {
			AffectedDataObjects ados = (AffectedDataObjects) pce.getNewValue();
			if (this.isDebug==true) System.out.println("[" + this.getClass().getSimpleName() + "] Received data source loaded event for " + ados.getDataTreeNodeObject().getShortUUID() + "(" + ados.getDataTreeNodeObject().getCaption() + ")");
			
			this.getLockWaitForDTNOs().lock();
			this.getConditionDtnoAvailable().signal();
			this.getLockWaitForDTNOs().unlock();
		}
	}
	
}
