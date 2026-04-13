package de.enflexit.df.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import de.enflexit.df.core.model.treeNode.DataTreeNodeObjectBase;

/**
 * The Class JPanelTree.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelTree extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 4514690381659493450L;
	
	private DataController dataController;
	
	private JSplitPane jSplitPaneTreeConfiguration;
	private boolean isConfigurationVisible;
	private Integer dividerLocationReminder;
	
	private JPanel jPanelDataTree;
	private JTreeData jTreeData;
	private JScrollPane jScrollPaneTree;

	private JPanelConfigurationWrapper jPanelConfigurationWrapper;
	
	private JPanel jPanelSearch;
	private JLabel jLabelSearch;
	private JTextField jTextFieldSearch;
	private JButton jButtonResetSearch;
	
	/**
	 * Instantiates a new JPanelTree.
	 */
	@SuppressWarnings("unused")
	private JPanelTree() {
		this(null);
	}
	/**
	 * Instantiates a new JPanelTree.
	 * @param jPanelData the current JPanelData instance
	 */
	public JPanelTree(DataController dataController) {
		this.setDataController(dataController);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(this.getJPanelDataTree(), BorderLayout.CENTER);
		this.add(this.getPanelSearch(), BorderLayout.SOUTH);
	}
	
	/**
	 * Sets the DataController.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
		if (this.dataController!=null) {
			this.dataController.addPropertyChangeListener(this);
		}
	}
	public DataController getDataController() {
		return dataController;
	}
	
	private JSplitPane getJSplitPaneTreeConfiguration() {
		if (jSplitPaneTreeConfiguration==null) {
			jSplitPaneTreeConfiguration = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneTreeConfiguration.setDividerLocation(0.55);
			jSplitPaneTreeConfiguration.setResizeWeight(0.55);
			jSplitPaneTreeConfiguration.setDividerSize(5);
			jSplitPaneTreeConfiguration.setTopComponent(this.getJPanelDataTree());
			jSplitPaneTreeConfiguration.setBottomComponent(this.getJPanelConfigurationWrapper());
		}
		return jSplitPaneTreeConfiguration;
	}
	
	private JPanel getJPanelDataTree() {
		if (jPanelDataTree==null) {
			jPanelDataTree = new JPanel();
			
			GridBagLayout gbl_jPanelTree = new GridBagLayout();
			gbl_jPanelTree.columnWidths = new int[]{0, 0};
			gbl_jPanelTree.rowHeights = new int[]{0, 0};
			gbl_jPanelTree.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelTree.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			jPanelDataTree.setLayout(gbl_jPanelTree);
			
			GridBagConstraints gbc_jTree = new GridBagConstraints();
			gbc_jTree.fill = GridBagConstraints.BOTH;
			gbc_jTree.insets = new Insets(0, 5, 0, 0);
			gbc_jTree.gridx = 0;
			gbc_jTree.gridy = 0;
			jPanelDataTree.add(this.getJScrollPaneTree(), gbc_jTree);
		}
		return jPanelDataTree;
	}
	private JScrollPane getJScrollPaneTree() {
		if (jScrollPaneTree==null) {
			jScrollPaneTree = new JScrollPane();
			jScrollPaneTree.setViewportView(this.getJTreeData());
		}
		return jScrollPaneTree;
	}
	private JTreeData getJTreeData() {
		if (jTreeData == null) {
			jTreeData = new JTreeData(this.getDataController());
		}
		return jTreeData;
	}

	
	private JPanelConfigurationWrapper getJPanelConfigurationWrapper() {
		if (jPanelConfigurationWrapper==null) {
			jPanelConfigurationWrapper = new JPanelConfigurationWrapper();
			jPanelConfigurationWrapper.addCloseActionListener(this);
		}
		return jPanelConfigurationWrapper;
	}
	
	
	private JPanel getPanelSearch() {
		if (jPanelSearch == null) {
			jPanelSearch = new JPanel();
			GridBagLayout gbl_jPanelSearch = new GridBagLayout();
			gbl_jPanelSearch.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelSearch.rowHeights = new int[]{0, 0};
			gbl_jPanelSearch.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelSearch.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelSearch.setLayout(gbl_jPanelSearch);
			GridBagConstraints gbc_jLabelSearch = new GridBagConstraints();
			gbc_jLabelSearch.anchor = GridBagConstraints.EAST;
			gbc_jLabelSearch.insets = new Insets(5, 5, 5, 0);
			gbc_jLabelSearch.gridx = 0;
			gbc_jLabelSearch.gridy = 0;
			jPanelSearch.add(getJLabelSearch(), gbc_jLabelSearch);
			GridBagConstraints gbc_jTextFieldSearch = new GridBagConstraints();
			gbc_jTextFieldSearch.insets = new Insets(5, 5, 5, 0);
			gbc_jTextFieldSearch.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldSearch.gridx = 1;
			gbc_jTextFieldSearch.gridy = 0;
			jPanelSearch.add(getJTextFieldSearch(), gbc_jTextFieldSearch);
			GridBagConstraints gbc_jButtonResetSearch = new GridBagConstraints();
			gbc_jButtonResetSearch.insets = new Insets(5, 5, 5, 5);
			gbc_jButtonResetSearch.gridx = 2;
			gbc_jButtonResetSearch.gridy = 0;
			jPanelSearch.add(getJButtonResetSearch(), gbc_jButtonResetSearch);
		}
		return jPanelSearch;
	}
	private JLabel getJLabelSearch() {
		if (jLabelSearch == null) {
			jLabelSearch = new JLabel("Search:");
			jLabelSearch.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelSearch;
	}
	private JTextField getJTextFieldSearch() {
		if (jTextFieldSearch == null) {
			jTextFieldSearch = new JTextField();
			jTextFieldSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldSearch.setColumns(10);
		}
		return jTextFieldSearch;
	}
	private JButton getJButtonResetSearch() {
		if (jButtonResetSearch == null) {
			jButtonResetSearch = new JButton("");
			jButtonResetSearch.setPreferredSize(new Dimension(26, 26));
		}
		return jButtonResetSearch;
	}
	
	/**
	 * Checks if is configuration visible.
	 * @return true, if is configuration visible
	 */
	private boolean isConfigurationVisible() {
		return this.isConfigurationVisible;
	}
	/**
	 * Sets the configuration.
	 * @param isShowConfiguration the new configuration
	 */
	private void setConfiguration(boolean isShowConfiguration) {
		
		if (isShowConfiguration==false) {
			this.dividerLocationReminder = this.getJSplitPaneTreeConfiguration().getDividerLocation();
			this.remove(this.getJSplitPaneTreeConfiguration());
			this.add(this.getJPanelDataTree(), BorderLayout.CENTER);
			
		} else {
			this.remove(this.getJPanelDataTree());
			this.getJSplitPaneTreeConfiguration().setTopComponent(this.getJPanelDataTree());
			this.getJSplitPaneTreeConfiguration().setBottomComponent(this.getJPanelConfigurationWrapper());
			this.add(this.getJSplitPaneTreeConfiguration(), BorderLayout.CENTER);
			if (this.dividerLocationReminder!=null) {
				this.getJSplitPaneTreeConfiguration().setDividerLocation(this.dividerLocationReminder);
			}
		}
		this.updateConfigurationView();
		
		this.validate();
		this.repaint();
		
		this.isConfigurationVisible = isShowConfiguration;
	}
	/**
	 * Updates the configuration.
	 */
	private void updateConfigurationView() {
		
		// --- React on newly selected tree path ----------------
		ConfigurationPanel configPanel = this.getDataController().getSelectionModel().getSelectedFirstConfigurationPanel();
		this.getJPanelConfigurationWrapper().setConfigurationPanel(configPanel);
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (evt.getPropertyName().equals(DataController.DC_ADDED_DATA_WORKBOOK)==true) {
			this.setConfiguration(true);
		
		} else if (evt.getPropertyName().equals(DataController.DC_ADDED_DATA_SOURCE)==true) {
			this.setConfiguration(true);
			
		} else if (evt.getPropertyName().equals(DataController.DC_DATA_SOURCE_CONFIGURATION_SHOW)==true) {
			boolean isShowConfig = (boolean) evt.getNewValue();
			this.setConfiguration(isShowConfig);
			
		} else if (evt.getPropertyName().equals(DataController.DC_DATA_SOURCE_CONFIGURATION_CHANGED)==true) {
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) this.getJTreeData().getSelectionPath().getLastPathComponent();
			if (treeNode!=null) {
				((DefaultTreeModel) this.getJTreeData().getModel()).nodeChanged(treeNode);
			}
			
		} else if (evt.getPropertyName().equals(DataController.DC_NEW_TREE_PATH_SELECTED)==true) {
			if (this.isConfigurationVisible()==true) {
				this.updateConfigurationView();
			}
			
		} else if (evt.getPropertyName().equals(DataController.DC_DATA_LOADED)) {
			DataTreeNodeObjectBase dtno = this.getDataController().getSelectionModel().getSelectedDataTreeNodeObjectBase();
			if (dtno!=null) {
				this.getJPanelConfigurationWrapper().setError(dtno.getErrorMessage());
			}
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJPanelConfigurationWrapper()) {
			// --- React on configuration close event --------------
			this.getDataController().firePropertyChange(DataController.DC_DATA_SOURCE_CONFIGURATION_SHOW, true, false);
			
		} else if (ae.getSource()==this.getJButtonResetSearch()) {
			// --- Reset search -------------------------------------
			
			
		}
		
	}
	
	
}
