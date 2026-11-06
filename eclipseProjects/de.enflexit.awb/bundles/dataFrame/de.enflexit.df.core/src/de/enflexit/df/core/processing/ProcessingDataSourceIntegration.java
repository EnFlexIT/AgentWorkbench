package de.enflexit.df.core.processing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.transformation.DataTransformationProcessor;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTransformationEdge;
import de.enflexit.df.core.processing.transformationGraph.PersistentTransformationGraph;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;
import de.enflexit.df.core.processing.ui.TransformationGraphEditorPanel;
import de.enflexit.df.core.workbook.DataWorkbook;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class ProcessingDataSourceIntegration.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ProcessingDataSourceIntegration extends AbstractDataSourceIntegration<ProcessingDataSource>{
	
	private ProcessingDataSourceDTNO dtno;
	private JPanelDataSourceConfigurationProcessing configPanel;
	private TransformationGraphEditorPanel detailsViewPanel;
	
	private TransformationGraphController graphController;
	
	private DataTransformationProcessor transformationProcessor;

	/**
	 * Instantiates a new processing data source integration.
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 */
	public ProcessingDataSourceIntegration(DataController dataController, DataWorkbook dataWorkbook, ProcessingDataSource dataSource) {
		super(dataController, dataWorkbook, dataSource);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.DataSourceIntegration#getDTNO()
	 */
	@Override
	public AbstractDataSourceDTNO<ProcessingDataSource> getDTNO() {
		if (dtno==null) {
			dtno = new ProcessingDataSourceDTNO(this);
		}
		return dtno;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		if (configPanel==null) {
			configPanel = new JPanelDataSourceConfigurationProcessing(this);
		}
		return configPanel;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetConfigurationPanel()
	 */
	@Override
	public void resetConfigurationPanel() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#getDetailViewPanel()
	 */
	@Override
	public JComponent getDetailViewPanel() {
		if (detailsViewPanel==null) {
			
			detailsViewPanel = new TransformationGraphEditorPanel(this.getDataController());
			PersistentTransformationGraph persTransGraph = this.getTransformationGraph();
			if (persTransGraph!=null) {
				this.getGraphController().loadPersistentGraph(persTransGraph);
			}
		}
		return detailsViewPanel;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.DataSourceConfigurationPanel#resetDetailViewPanel()
	 */
	@Override
	public void resetDetailViewPanel() {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration#addDataTreeSubNodes()
	 */
	@Override
	public void addDataTreeSubNodes() {
		ArrayList<DataTableNode> endNodes = this.findResultNodes(this.getGraphController().getTransformationGraph());
		for (DataTableNode endNode : endNodes) {
			this.addDataTreeSubNode(endNode);
		}
	}
	
	/**
	 * Adds a sub node for the provided transformation result table node.
	 * @param resultNode the result node
	 */
	private void addDataTreeSubNode(DataTableNode resultNode) {
		ProcessingResultDataSource dataSource = new ProcessingResultDataSource(resultNode, this.getDataSource());
		ProcessingResultDataSourceIntegration dsIntegration = new ProcessingResultDataSourceIntegration(this.getDataController(), this.getDataWorkbook(), dataSource);
		DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(dsIntegration.getDTNO());
		
		this.getDataTreeNode().add(subNode);
		this.getDataController().getDataTreeModel().informTreeNodeAdded(this.getDataTreeNode(), subNode);
		
		if (resultNode.getDataTable()==null) {
			this.getTransformationProcessor().performTransformation(this.getGraphController().getTransformationForNode(resultNode));
		}
	}
	
	/**
	 * Find the nodes that represent a final transformation result.
	 * @param transformationGraph the transformation graph
	 * @return the array list
	 */
	private ArrayList<DataTableNode> findResultNodes(Graph<DataTableNode, DataTransformationEdge> transformationGraph){
		ArrayList<DataTableNode> resultNodes = new ArrayList<DataTableNode>();
		for (DataTableNode node : transformationGraph.getVertices()) {
			if (transformationGraph.getOutEdges(node).size()==0) {
				resultNodes.add(node);
			}
		}
		return resultNodes;
	}
	
	/**
	 * Gets the transformation processor.
	 * @return the transformation processor
	 */
	private DataTransformationProcessor getTransformationProcessor() {
		if (transformationProcessor==null) {
			transformationProcessor = new DataTransformationProcessor(this.getGraphController());
		}
		return transformationProcessor;
	}
	
	/**
	 * Gets the graph controller.
	 * @return the graph controller
	 */
	public TransformationGraphController getGraphController() {
		if (graphController==null) {
			graphController = ((TransformationGraphEditorPanel)this.getDetailViewPanel()).getGraphController();
		}
		return graphController;
	}
	
	/**
	 * Sets the transformation graph for this data source.
	 * @param transformationGraph the new transformation graph
	 */
	public void setTransformationGraph(PersistentTransformationGraph transformationGraph) {
		transformationGraph.setDataSourceUUID(this.getDataSource().getShortUUID());
		this.getDataWorkbook().addTransformationGraph(transformationGraph);
		this.addDataTreeSubNodes();
	}
	
	private PersistentTransformationGraph getTransformationGraph() {
		return this.getDataWorkbook().getTransformationGraphForDataSource(this.getDataSource().getShortUUID());
	}


}
