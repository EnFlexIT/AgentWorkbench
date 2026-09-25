package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.DTNO_Base;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;

/**
 * This panel provides a list of data sources from the current workbook, and allows to
 * select and deselect them to specify the starting nodes of a transformation graph.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JPanelSourceTableSelection extends JPanel implements CheckBoxListSelectionListener<AbstractDataSourceDTNO<?>> {
	
	private static final long serialVersionUID = -2860160566604926918L;
	
	private JLabel jScrollPaneSourceTableList;
	private JScrollPane scrollPane;
	private CheckBoxList<AbstractDataSourceDTNO<?>> jListSourceTables;
	private DefaultListModel<AbstractDataSourceDTNO<?>> dataSourcesListModel;
	
	private DataController dataController;
	private TransformationGraphController graphController;
	
	/**
	 * Instantiates a new j panel source table selection.
	 */
	public JPanelSourceTableSelection(DataController dataController, TransformationGraphController graphController) {
		this.dataController = dataController;
		this.graphController = graphController;
		initialize();
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jScrollPaneSourceTableList = new GridBagConstraints();
		gbc_jScrollPaneSourceTableList.anchor = GridBagConstraints.WEST;
		gbc_jScrollPaneSourceTableList.insets = new Insets(5, 5, 5, 0);
		gbc_jScrollPaneSourceTableList.gridx = 0;
		gbc_jScrollPaneSourceTableList.gridy = 0;
		add(getJScrollPaneSourceTableList(), gbc_jScrollPaneSourceTableList);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(getScrollPane(), gbc_scrollPane);
	}

	private JLabel getJScrollPaneSourceTableList() {
		if (jScrollPaneSourceTableList == null) {
			jScrollPaneSourceTableList = new JLabel("Data source tables");
			jScrollPaneSourceTableList.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jScrollPaneSourceTableList;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getJListSourceTables());
		}
		return scrollPane;
	}
	private CheckBoxList<AbstractDataSourceDTNO<?>> getJListSourceTables() {
		if (jListSourceTables == null) {
			jListSourceTables = new CheckBoxList<AbstractDataSourceDTNO<?>>(this.getDataSourcesListModel());
			jListSourceTables.addCheckBoxListSelectionListener(this);
		}
		return jListSourceTables;
	}
	
	/**
	 * Gets the data sources list model.
	 * @return the data sources list model
	 */
	private DefaultListModel<AbstractDataSourceDTNO<?>> getDataSourcesListModel() {
		if (dataSourcesListModel==null) {
			
			dataSourcesListModel = new DefaultListModel<AbstractDataSourceDTNO<?>>();
			dataSourcesListModel.addAll(this.getRelevantDataSources());
			
		}
		return dataSourcesListModel;
	}
	
	/**
	 * Gets the relevant data sources, i.e. those that actually provide a table. 
	 * @return the relevant data sources
	 */
	private ArrayList<AbstractDataSourceDTNO<?>> getRelevantDataSources(){
		DefaultMutableTreeNode workbookNode = this.dataController.getSelectionModel().getSelectedDataWorkbookTreeNode();
		ArrayList<AbstractDataSourceDTNO<?>> relevantDataSources = new ArrayList<AbstractDataSourceDTNO<?>>();
		this.recursivelyCollectRelevantDataSources(workbookNode, relevantDataSources);
		return relevantDataSources;
	}
	
	/**
	 * Recursively searches for relevant data sources, starting from the provided start node.
	 * @param startNode the start node
	 * @param sourcesList the sources list
	 */
	private void recursivelyCollectRelevantDataSources(DefaultMutableTreeNode startNode, ArrayList<AbstractDataSourceDTNO<?>> sourcesList) {
		
		// --- Check if the start node is relevant ------------------
		DTNO_Base dtno = (DTNO_Base) startNode.getUserObject();
		if (dtno instanceof AbstractDataSourceDTNO<?>) {
			DefaultDataSource dataSource = ((AbstractDataSourceDTNO<?>) dtno).getDataSource();
			if (dataSource.requiresSubConfiguration()==false) {
				sourcesList.add((AbstractDataSourceDTNO<?>) dtno);
			}
		}
		
		// --- Check all child nodes recursively --------------------
		for (int i=0; i<startNode.getChildCount(); i++) {
			this.recursivelyCollectRelevantDataSources((DefaultMutableTreeNode) startNode.getChildAt(i), sourcesList);
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.ui.CheckBoxListSelectionListener#selectionChanged(de.enflexit.df.core.processing.ui.CheckBoxListSelectionEvent)
	 */
	@Override
	public void selectionChanged(CheckBoxListSelectionEvent<AbstractDataSourceDTNO<?>> cblse) {
		if (cblse.getSource()==this.getJListSourceTables()) {
			AbstractDataSourceDTNO<?> affectedDataSourceDTNO = cblse.getItem();
			
			if (cblse.isSelected()==true) {
				this.graphController.addDataTableNode(affectedDataSourceDTNO);
			} else {
				this.graphController.removeDataTableNode(affectedDataSourceDTNO);
			}
		}
	}

	
		
}
