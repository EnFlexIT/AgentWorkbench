package de.enflexit.df.descriptionService.ui;

import javax.swing.JPanel;

import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceIntegration;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.DTNO_Base;
import de.enflexit.df.core.model.treeNode.DTNO_DataWorkbook;
import de.enflexit.df.core.ui.DataTreeCellRenderer;
import de.enflexit.df.core.workbook.DataWorkbook;
import de.enflexit.df.descriptionService.DescriptionsController;
import de.enflexit.df.descriptionService.db.DataColumnDescription;

import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * The main UI panel for the data column description editor.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ColumnDescriptionEditorMainPanel extends JPanel implements TreeSelectionListener{

	private static final long serialVersionUID = 1011843753073573266L;
	
	private DataWorkbook dataWorkbook;
	
	private DescriptionsController descriptionsController;
	
	private JSplitPane jSplitPaneMain;
	private JPanel jPanelDataSourceTree;
	private JScrollPane jScrollPaneDataSourceTree;
	private ColumnDescriptionEditorColumnSelectionPanel jPanelColumnDescriptionEditor;
	private JTree jTreeDataSources;
	
	private DefaultTreeModel dataSourcesTreeModel;
	
	private HashMap<String, DataColumnDescription> dataColumnDescriptions;
	
	/**
	 * Instantiates a new data column description editor main panel.
	 */
	public ColumnDescriptionEditorMainPanel() {
		initialize();
	}
	
	/**
	 * Instantiates a new data column description editor main panel.
	 * @param dataController the data controller
	 */
	public ColumnDescriptionEditorMainPanel(DescriptionsController descriptionsController) {
		this.descriptionsController = descriptionsController;
		this.dataWorkbook = descriptionsController.getDataController().getSelectionModel().getSelectedDataWorkbook();
		
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJSplitPaneMain(), BorderLayout.CENTER);
		
		this.setCurrentlySelectedDataSource();
	}
	

	/**
	 * Sets the data workbook.
	 * @param dataWorkbook the new data workbook
	 */
	public void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
	}
	
	/**
	 * Gets the j split pane main.
	 * @return the j split pane main
	 */
	private JSplitPane getJSplitPaneMain() {
		if (jSplitPaneMain == null) {
			jSplitPaneMain = new JSplitPane();
			jSplitPaneMain.setOneTouchExpandable(true);
			jSplitPaneMain.setDividerSize(5);
			jSplitPaneMain.setResizeWeight(0.25);
			jSplitPaneMain.setLeftComponent(this.getjPanelDataSourceTree());
			jSplitPaneMain.setRightComponent(this.getJPanelColumnDescriptionEditor());
		}
		return jSplitPaneMain;
	}
	
	/**
	 * Gets the j panel data source tree.
	 * @return the j panel data source tree
	 */
	private JPanel getjPanelDataSourceTree() {
		if (jPanelDataSourceTree==null) {
			jPanelDataSourceTree = new JPanel();
			
			GridBagLayout gbl_jPanelTree = new GridBagLayout();
			gbl_jPanelTree.columnWidths = new int[]{0, 0};
			gbl_jPanelTree.rowHeights = new int[]{0, 0};
			gbl_jPanelTree.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelTree.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			jPanelDataSourceTree.setLayout(gbl_jPanelTree);
			
			GridBagConstraints gbc_jTree = new GridBagConstraints();
			gbc_jTree.fill = GridBagConstraints.BOTH;
			gbc_jTree.insets = new Insets(0, 5, 0, 0);
			gbc_jTree.gridx = 0;
			gbc_jTree.gridy = 0;
			jPanelDataSourceTree.add(this.getJScrollPaneDataSourceTree(), gbc_jTree);
		}
		return jPanelDataSourceTree;
	}
	
	/**
	 * Gets the j scroll pane data source tree.
	 * @return the j scroll pane data source tree
	 */
	private JScrollPane getJScrollPaneDataSourceTree() {
		if (jScrollPaneDataSourceTree == null) {
			jScrollPaneDataSourceTree = new JScrollPane();
			jScrollPaneDataSourceTree.setViewportView(getJTreeCurrentDataWorkbook());
			this.expandFirstLevelNodesWithChildNodes();
		}
		return jScrollPaneDataSourceTree;
	}
	
	/**
	 * Gets the j tree current data workbook.
	 * @return the j tree current data workbook
	 */
	private JTree getJTreeCurrentDataWorkbook() {
		if (jTreeDataSources == null) {
			jTreeDataSources = new JTree();
			jTreeDataSources.setModel(this.getDataSourcesTreeModel());
			jTreeDataSources.setRootVisible(false);
			jTreeDataSources.setCellRenderer(new DataTreeCellRenderer());
			jTreeDataSources.addTreeSelectionListener(this);
		}
		return jTreeDataSources;
	}
	
	/**
	 * Gets the data sources tree model.
	 * @return the data sources tree model
	 */
	private DefaultTreeModel getDataSourcesTreeModel() {
		if (dataSourcesTreeModel==null) {
			DataController dataController = this.descriptionsController.getDataController(); 
			if (dataWorkbook==null || dataController==null) {
				throw new IllegalArgumentException("No data workbook selected or data controller not available!");
			}
			DTNO_DataWorkbook rootDTNO = new DTNO_DataWorkbook(dataController, this.dataWorkbook);
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDTNO);
			for (DefaultDataSource dataSource : dataWorkbook.getDataSources()) {
				AbstractDataSourceIntegration<?> dsIntegration = dataSource.getDataSourceIntegration(dataController, dataWorkbook);
				AbstractDataSourceDTNO<?> dataSourceDTNO = dsIntegration.getDTNO();
				DefaultMutableTreeNode dataSourceNode = new DefaultMutableTreeNode(dataSourceDTNO);
				for(int i=0; i<dsIntegration.getDataTreeNode().getChildCount(); i++) {
					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) dsIntegration.getDataTreeNode().getChildAt(i);
					AbstractDataSourceDTNO<?> childDTNO = (AbstractDataSourceDTNO<?>) childNode.getUserObject();
					dataSourceNode.add(new DefaultMutableTreeNode(childDTNO));
				}
				rootNode.add(dataSourceNode);
			}
			dataSourcesTreeModel = new DefaultTreeModel(rootNode);
		}
		return dataSourcesTreeModel;
	}
	
	
	/**
	 * Gets the j panel col desc editor.
	 * @return the j panel col desc editor
	 */
	private ColumnDescriptionEditorColumnSelectionPanel getJPanelColumnDescriptionEditor() {
		if (jPanelColumnDescriptionEditor == null) {
			jPanelColumnDescriptionEditor = new ColumnDescriptionEditorColumnSelectionPanel(this.descriptionsController);
		}
		return jPanelColumnDescriptionEditor;
	}
	
	/**
	 * Sets the data source that is currently selected in the {@link DataController}'s 
	 * selection model as active data source for the column editor.
	 */
	private void setCurrentlySelectedDataSource() {
		AbstractDataSourceDTNO<?> dataSourceDTNO = this.descriptionsController.getDataController().getSelectionModel().getSelectedDataTreeNodeDataSource();
		DefaultMutableTreeNode dataSourceNode = this.findTreeNodeByDTNO(dataSourceDTNO);
		if (dataSourceNode!=null) {
			TreePath dataSourceTreePath = new TreePath(dataSourceNode.getPath());
			this.getJTreeCurrentDataWorkbook().setSelectionPath(dataSourceTreePath);
		}
	}
	
	/**
	 * Expands all first level nodes with child nodes.
	 */
	private void expandFirstLevelNodesWithChildNodes() {
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this.getDataSourcesTreeModel().getRoot();
		for (int i=0; i<rootNode.getChildCount(); i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) rootNode.getChildAt(i);
			if (childNode.isLeaf()==false) {
				TreePath treePath = new TreePath(childNode.getPath());
				this.getJTreeCurrentDataWorkbook().expandPath(treePath);
			}
		}
	}
	
	/**
	 * Finds the tree node with the specified DTNO.
	 * @param dtno the dtno
	 * @return the tree node, null if not found
	 */
	private DefaultMutableTreeNode findTreeNodeByDTNO(DTNO_Base dtno) {
		if (dtno!=null) {
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this.getJTreeCurrentDataWorkbook().getModel().getRoot();
			Enumeration<TreeNode> allNodes = rootNode.depthFirstEnumeration();
			while (allNodes.hasMoreElements()) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) allNodes.nextElement();
				if (treeNode.getUserObject()==dtno) {
					return treeNode;
				}
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent tse) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tse.getNewLeadSelectionPath().getLastPathComponent();
		DTNO_Base selectedDTNO = (DTNO_Base) selectedNode.getUserObject();
		if (selectedDTNO instanceof AbstractDataSourceDTNO<?>) {
			AbstractDataSourceDTNO<?> dsDTNO = (AbstractDataSourceDTNO<?>) selectedDTNO;
			this.getJPanelColumnDescriptionEditor().setDataSourceDTNO(dsDTNO);
		}
	}

	/**
	 * Gets the data column descriptions.
	 * @return the data column descriptions
	 */
	public HashMap<String, DataColumnDescription> getDataColumnDescriptions() {
		return dataColumnDescriptions;
	}

	/**
	 * Sets the data column descriptions.
	 * @param dataColumnDescriptions the data column descriptions
	 */
	public void setDataColumnDescriptions(HashMap<String, DataColumnDescription> dataColumnDescriptions) {
		this.dataColumnDescriptions = dataColumnDescriptions;
	}
	
	/**
	 * Checks for unsaved changes before leaving the current selection.
	 *  If there are any, the user will be asked how to proceed.
	 * @return true, if successful
	 */
	public boolean allowLeaveSelection() {
		return this.getJPanelColumnDescriptionEditor().checkAllowLeaveSelection();
	}
	
	/**
	 * Sets the column to edit.
	 * @param columnName the name of the column to edit
	 */
	public void setColumnToEdit(String columnName) {
		this.getJPanelColumnDescriptionEditor().setColumnToEdit(columnName);
	}
	
}
