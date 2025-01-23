package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JToolBar;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.Insets;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import agentgui.core.application.Application;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProviderConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.dataModel.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.dataModel.AbstractDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.dataModel.CsvDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.dataModel.CsvSourceConfiguration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JSplitPane;

/**
 * GUI panel for the configuration of the weather data provider
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataProviderConfigurationPanel extends JPanel implements ActionListener, TreeSelectionListener, PropertyChangeListener {

	private static final long serialVersionUID = -7240796684633097667L;
	
	private static final Dimension buttonSize = new Dimension(26, 26);
	private static final String ICON_PATH_LOAD_FILE = "/icons/LoadFromFile.png";
	private static final String ICON_PATH_LOAD_DB = "/icons/LoadFromDB.png";
	private static final String ICON_PATH_LOAD_WEB = "/icons/LoadFromWeb.png";
	private static final String ICON_PATH_REMOVE = "/icons/Delete.png";
	private static final String ICON_PATH_ADD_SERIES = "/icons/ListPlus.png";
	private static final String ICON_PATH_REMOVE_SERIES = "/icons/ListMinus.png";
	
	private JToolBar toolBar;
	private JButton jButtonAddCsvDataSource;
	private JButton jButtonAddDbDataSource;
	private JButton jButtonAddWebDataSource;
	private JScrollPane scrollPaneDataSourceTree;
	private JTree dataSourceTree;
	private JButton jButtonDeleteDataSource;
	
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private CsvDataSourceConfigurationPanel dataSourceConfigPanel;
	private JSeparator separator;
	private JButton jButtonAddSeries;
	private JButton jButtonRemoveSeries;
	
	private DefaultMutableTreeNode selectedNode;
	private AbstractDataSourceConfiguration selectedDataSource;
	private JSplitPane jSplitPaneMainPanel;
	
	/**
	 * Instantiates a new weather data provider configuration panel.
	 */
	public TimeSeriesDataProviderConfigurationPanel() {
		initialize();
		TimeSeriesDataProvider.getInstance().addPropertyChangeListener(this);
	}
	
	/**
	 * Initialize the GUI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[]{28, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.anchor = GridBagConstraints.NORTH;
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		add(getToolBar(), gbc_toolBar);
		GridBagConstraints gbc_jSplitPaneMainPanel = new GridBagConstraints();
		gbc_jSplitPaneMainPanel.fill = GridBagConstraints.BOTH;
		gbc_jSplitPaneMainPanel.gridx = 0;
		gbc_jSplitPaneMainPanel.gridy = 1;
		add(getJSplitPaneMainPanel(), gbc_jSplitPaneMainPanel);
	}
	
	// ------------------------------------------------------------------------
	// --- From here, private getter methods for the GUI components -----------

	private JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.add(getJButtonAddCsvDataSource());
			toolBar.add(getJButtonAddDbDataSource());
			toolBar.add(getJButtonAddWebDataSource());
			toolBar.add(getJButtonDeleteDataSource());
			toolBar.add(getSeparator());
			toolBar.add(getJButtonAddSeries());
			toolBar.add(getJButtonRemoveSeries());
			
		}
		return toolBar;
	}
	private JButton getJButtonAddCsvDataSource() {
		if (jButtonAddCsvDataSource == null) {
			jButtonAddCsvDataSource = new JButton();
			jButtonAddCsvDataSource.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_LOAD_FILE)));
			jButtonAddCsvDataSource.setToolTipText("Load Weather Data from CSV ile");
			jButtonAddCsvDataSource.setPreferredSize(buttonSize);
			jButtonAddCsvDataSource.addActionListener(this);
		}
		return jButtonAddCsvDataSource;
	}
	private JButton getJButtonAddDbDataSource() {
		if (jButtonAddDbDataSource == null) {
			jButtonAddDbDataSource = new JButton();
			jButtonAddDbDataSource.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_LOAD_DB)));
			jButtonAddDbDataSource.setToolTipText("Load Weather Data from Database");
			jButtonAddDbDataSource.setPreferredSize(buttonSize);
			jButtonAddDbDataSource.setEnabled(false);
			jButtonAddDbDataSource.addActionListener(this);
		}
		return jButtonAddDbDataSource;
	}
	private JButton getJButtonAddWebDataSource() {
		if (jButtonAddWebDataSource == null) {
			jButtonAddWebDataSource = new JButton();
			jButtonAddWebDataSource.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_LOAD_WEB)));
			jButtonAddWebDataSource.setToolTipText("Load Weather Data from Web Service");
			jButtonAddWebDataSource.setPreferredSize(buttonSize);
			jButtonAddWebDataSource.setEnabled(false);
			jButtonAddWebDataSource.addActionListener(this);
		}
		return jButtonAddWebDataSource;
	}
	private JButton getJButtonDeleteDataSource() {
		if (jButtonDeleteDataSource == null) {
			jButtonDeleteDataSource = new JButton();
			jButtonDeleteDataSource.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_REMOVE)));
			jButtonDeleteDataSource.setToolTipText("Remove the selected data source");
			jButtonDeleteDataSource.setPreferredSize(buttonSize);
			jButtonDeleteDataSource.addActionListener(this);
		}
		return jButtonDeleteDataSource;
	}

	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
		}
		return separator;
	}

	private JButton getJButtonAddSeries() {
		if (jButtonAddSeries == null) {
			jButtonAddSeries = new JButton();
			jButtonAddSeries.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_ADD_SERIES)));
			jButtonAddSeries.setToolTipText("Add a data series to this source");
			jButtonAddSeries.setPreferredSize(buttonSize);
			jButtonAddSeries.setEnabled(false);
			jButtonAddSeries.addActionListener(this);
		}
		return jButtonAddSeries;
	}

	private JButton getJButtonRemoveSeries() {
		if (jButtonRemoveSeries == null) {
			jButtonRemoveSeries = new JButton();
			jButtonRemoveSeries.setIcon(new ImageIcon(this.getClass().getResource(ICON_PATH_REMOVE_SERIES)));
			jButtonRemoveSeries.setToolTipText("Remove the selected data series");
			jButtonRemoveSeries.setPreferredSize(buttonSize);
			jButtonRemoveSeries.setEnabled(false);
			jButtonRemoveSeries.addActionListener(this);
		}
		return jButtonRemoveSeries;
	}
	private JScrollPane getScrollPaneDataSourceTree() {
		if (scrollPaneDataSourceTree == null) {
			scrollPaneDataSourceTree = new JScrollPane();
			scrollPaneDataSourceTree.setViewportView(getDataSourceTree());
		}
		return scrollPaneDataSourceTree;
	}
	private JTree getDataSourceTree() {
		if (dataSourceTree == null) {
			dataSourceTree = new JTree();
			dataSourceTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			dataSourceTree.setModel(this.getTreeModel());
//			this.expandAllTreeNodes();
			dataSourceTree.addTreeSelectionListener(this);
		}
		return dataSourceTree;
	}
	
	private DefaultMutableTreeNode getRootNode() {
		if (rootNode==null) {
			rootNode= new DefaultMutableTreeNode("Data Sources");
		}
		return rootNode;
	}
	
	/**
	 * Gets the tree model, initializes it if necessary.
	 * @return the tree model
	 */
	private DefaultTreeModel getTreeModel() {
		if (treeModel==null) {
			
			TimeSeriesDataProviderConfiguration configuration = TimeSeriesDataProvider.getInstance().getConfiguration();
			
			// --- Create nodes for the configured data sources -----
			for (int i=0; i<configuration.getDataSourceConfigurations().size(); i++) {
				AbstractDataSourceConfiguration sourceConfig = configuration.getDataSourceConfigurations().get(i);
				DefaultMutableTreeNode sourceNode = new DefaultMutableTreeNode(sourceConfig);
				this.getRootNode().add(sourceNode);
				
				// --- Create nodes for the configured data series --
				for (int j=0; j<sourceConfig.getDataSeriesConfigurations().size(); j++) {
					AbstractDataSeriesConfiguration seriesConfig = sourceConfig.getDataSeriesConfigurations().get(j);
					DefaultMutableTreeNode seriesNode = new DefaultMutableTreeNode(seriesConfig);
					sourceNode.add(seriesNode);
				}
			}
			treeModel = new DefaultTreeModel(this.getRootNode());
		}
		return treeModel;
	}

	private CsvDataSourceConfigurationPanel getDataSourceConfigPanel() {
		if (dataSourceConfigPanel == null) {
			dataSourceConfigPanel = new CsvDataSourceConfigurationPanel();
		}
		return dataSourceConfigPanel;
	}

	
	// ------------------------------------------------------------------------
	// --- From here, methods to handle CSV data sources and series -----------
	
	/**
	 * Adds a new CSV data source to the configuration
	 */
	private void addNewCsvDataSource() {
		JFileChooser jFileChooserImportCSV = new JFileChooser();
		jFileChooserImportCSV.setCurrentDirectory(Application.getGlobalInfo().getLastSelectedFolder());
		jFileChooserImportCSV.setFileFilter(new FileNameExtensionFilter("CSV-File", "csv"));
		jFileChooserImportCSV.setDialogTitle("Choose CSV-File");
		if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File csvFile = jFileChooserImportCSV.getSelectedFile();
			Application.getGlobalInfo().setLastSelectedFolder(csvFile.getParent());
			if (this.isInProjectFolder(csvFile)==false) {
				JOptionPane.showMessageDialog(this, "Please choose a file that is located inside the project folder!", "Invalid location", JOptionPane.WARNING_MESSAGE);
			} else {
				if (csvFile!=null && csvFile.exists()) {
					// --- Create an initial CSV data source configuration --------
					CsvSourceConfiguration sourceConfig = new CsvSourceConfiguration();
					sourceConfig.setName("New CSV data source");
					sourceConfig.setCsvFilePath(csvFile.toPath());
					sourceConfig.setHeadline(true);
					sourceConfig.setColumnSeparator(";");
					
					// --- Add it to the data model -------------------------------
					TimeSeriesDataProvider.getInstance().addDataSourceConfiguration(sourceConfig);
					
					DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(sourceConfig);
					this.getRootNode().add(newTreeNode);
					this.getTreeModel().reload();
					Object[] seletionPath = {this.getRootNode(), newTreeNode};
					this.getDataSourceTree().setSelectionPath(new TreePath(seletionPath));
				}
			}
		}
	}
	
	/**
	 * Checks if the specified file is inside the project folder (including subfolders).
	 * @param file the file
	 * @return true, if the file is inside the project folder
	 */
	private boolean isInProjectFolder(File file) {
		Path filePath = file.toPath();
		Path projectFolderPath = new File(Application.getProjectFocused().getProjectFolderFullPath()).toPath();
		return filePath.startsWith(projectFolderPath);
	}

	/**
	 * Adds a new csv data series.
	 */
	private void addNewCsvDataSeries() {
		// --- Prepare an initial series configuration --------------
		CsvDataSeriesConfiguration newSeries = new CsvDataSeriesConfiguration();
		newSeries.setName("New CSV data series");
		newSeries.setDataColumn(0);
		
		// --- Create and select the corresponding tree node --------
		DefaultMutableTreeNode seriesNode = new DefaultMutableTreeNode(newSeries); 
		this.addSeriesNode(seriesNode);
		
		
		// --- Add the new series to the source's series list -------
		this.selectedDataSource.addDataSeriesConfiguration(newSeries, false);
	}
	
	// ------------------------------------------------------------------------
	// --- From here, methods to add and remove sources and series to/from the tree
	
	/**
	 * Adds a series node to the tree, making sure added to a data source node.
	 * @param seriesNode the series node
	 */
	private void addSeriesNode(DefaultMutableTreeNode seriesNode) {
		TreePath selectionPath;
		if (this.selectedNode.getParent()==this.getRootNode()) {
			// --- The currently selected node is a source node, add directly
			this.selectedNode.add(seriesNode);
			selectionPath = this.getDataSourceTree().getSelectionPath().pathByAddingChild(seriesNode);
		} else {
			// --- The currently selected node is a series node, add to the parent source node instead
			((DefaultMutableTreeNode)this.selectedNode.getParent()).add(seriesNode);
			selectionPath = this.getDataSourceTree().getSelectionPath().getParentPath().pathByAddingChild(seriesNode);
		}
		this.getTreeModel().reload(seriesNode.getParent());
		this.getDataSourceTree().setSelectionPath(selectionPath);
	}
	
	/**
	 * Deletes a node from the tree, and the corresponding data source or series from the model.
	 * @param treeNode the tree node to delete
	 */
	private void deleteNode(DefaultMutableTreeNode treeNode) {
		int selectionIndex = this.getDataSourceTree().getMinSelectionRow();
		if (treeNode.getUserObject() instanceof AbstractDataSeriesConfiguration) {
			this.deleteDataSeriesNode(treeNode);
		} else if (treeNode.getUserObject() instanceof AbstractDataSourceConfiguration) {
			this.deleteDataSourceNode(treeNode);
		}
		this.getTreeModel().reload();
//		this.expandAllTreeNodes();
		
		if (selectionIndex==this.getDataSourceTree().getRowCount()) {
			selectionIndex--;
		}
		this.getDataSourceTree().setSelectionRow(selectionIndex);
	}
	
	/**
	 * Deletes a data series node from the tree, and the corresponding series from the source.
	 * @param dataSeriesNode the data series node
	 */
	private void deleteDataSeriesNode(DefaultMutableTreeNode dataSeriesNode) {
		AbstractDataSeriesConfiguration seriesToDelete = (AbstractDataSeriesConfiguration) dataSeriesNode.getUserObject();
		DefaultMutableTreeNode parentSourceNode = (DefaultMutableTreeNode) dataSeriesNode.getParent();
		AbstractDataSourceConfiguration parentSource = (AbstractDataSourceConfiguration) parentSourceNode.getUserObject();
		
		parentSourceNode.remove(dataSeriesNode);
		parentSource.removeDataSeriesConfiguration(seriesToDelete);
	}
	
	/**
	 * Deletes data source node from the tree, and the corresponding data source from the data provider's list of sources
	 * @param dataSourceNode the data source node
	 */
	private void deleteDataSourceNode(DefaultMutableTreeNode dataSourceNode) {
		AbstractDataSourceConfiguration dataSourceToDelete = (AbstractDataSourceConfiguration) dataSourceNode.getUserObject();
		
		// --- If the selected source has configured series, ask for confirmation
		boolean doDelete = true;
		if (dataSourceToDelete.getDataSeriesConfigurations().size()>0) {
			int userChoice = JOptionPane.showConfirmDialog(this, "The selected data source contains data series! Are you sure you want to delete it?", "Data series found!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (userChoice==JOptionPane.NO_OPTION) {
				doDelete = false;
			}
		}
		
		// --- Remove the node and the corresponding data source --------------
		if (doDelete==true) {
			this.getRootNode().remove(dataSourceNode);
			TimeSeriesDataProvider.getInstance().getConfiguration().remove(dataSourceToDelete);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonAddCsvDataSource()) {
			this.addNewCsvDataSource();
		} else if (ae.getSource()==this.getJButtonAddDbDataSource()) {
			//TODO implement loading from database
		} else if (ae.getSource()==this.getJButtonAddWebDataSource()) {
			// TODO implement loading from web service
		} else if (ae.getSource()==this.getJButtonDeleteDataSource()) {
			this.deleteNode(selectedNode);
		} else if (ae.getSource()==this.getJButtonRemoveSeries()) {
			this.deleteNode(selectedNode);
		}
		
		else if (ae.getSource()==this.getJButtonAddSeries()) {
			if (this.getDataSourceConfigPanel().getCurrentDataSourceConfiguration() instanceof CsvSourceConfiguration) {
				this.addNewCsvDataSeries();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent tse) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
		this.selectedNode = selectedNode;
		if (selectedNode==this.getRootNode()) {
			this.getJButtonDeleteDataSource().setEnabled(false);
			this.getJButtonAddSeries().setEnabled(false);
			this.getJButtonRemoveSeries().setEnabled(false);
			this.getDataSourceConfigPanel().setCurrentDataSourceConfiguration(null);
		} else if (selectedNode.getUserObject() instanceof AbstractDataSourceConfiguration) {
			AbstractDataSourceConfiguration dataSourceConfiguration = (AbstractDataSourceConfiguration) selectedNode.getUserObject();
			this.setSelectedDataSource(dataSourceConfiguration);
			this.getJButtonRemoveSeries().setEnabled(true);
			this.getJButtonAddSeries().setEnabled(true);
		} else if (selectedNode.getUserObject() instanceof AbstractDataSeriesConfiguration) {
			AbstractDataSeriesConfiguration dataSeriesConfiguration = (AbstractDataSeriesConfiguration) selectedNode.getUserObject();
			if (dataSeriesConfiguration instanceof CsvDataSeriesConfiguration) {
				
				// --- If the selected series is from a different source, set that as selected  
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
				if (parentNode!=null && parentNode.getUserObject()!=this.selectedDataSource) {
					AbstractDataSourceConfiguration dataSourceConfiguration = (AbstractDataSourceConfiguration) parentNode.getUserObject();
					this.selectedDataSource = dataSourceConfiguration;
					this.getDataSourceConfigPanel().setCurrentDataSourceConfiguration((CsvSourceConfiguration) dataSourceConfiguration);
				}
				
				// --- Set the series data to the config panel ------
				this.getDataSourceConfigPanel().setDataSeriesConfiguration((CsvDataSeriesConfiguration) dataSeriesConfiguration);
			}
			
			this.getJButtonRemoveSeries().setEnabled(true);
			this.getJButtonAddSeries().setEnabled(true);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(AbstractDataSourceConfiguration.DATA_SOURCE_RENAMED) || evt.getPropertyName().equals(AbstractDataSeriesConfiguration.DATA_SERIES_RENAMED)) {
			TreePath selection = this.getDataSourceTree().getSelectionPath();
			this.getTreeModel().reload();
			this.getDataSourceTree().setSelectionPath(selection);
		} else if (evt.getPropertyName().equals(AbstractDataSourceConfiguration.DATA_SERIES_ADDED)) {
			AbstractDataSeriesConfiguration newSeries = (AbstractDataSeriesConfiguration) evt.getNewValue();
			AbstractDataSourceConfiguration parentSource = (AbstractDataSourceConfiguration) evt.getOldValue(); 
			DefaultMutableTreeNode parentNode = this.findDataSourceNode(parentSource);
			if (parentNode!=null) {
				DefaultMutableTreeNode seriesNode = new DefaultMutableTreeNode(newSeries);
				parentNode.add(seriesNode);
			}
			this.getTreeModel().reload(parentNode);
		}
	}
	
	/**
	 * Sets the selected data source.
	 * @param dataSourceConfiguration the new selected data source
	 */
	private void setSelectedDataSource(AbstractDataSourceConfiguration dataSourceConfiguration) {
		this.selectedDataSource = dataSourceConfiguration;
		this.getDataSourceConfigPanel().setCurrentDataSourceConfiguration((CsvSourceConfiguration) dataSourceConfiguration);
	}
	
//	/**
//	 * Expands all tree nodes.
//	 */
//	private void expandAllTreeNodes() {
//		// --- Expand all tree nodes ----------------------------
//		for (int i=0; i<this.getDataSourceTree().getRowCount(); i++) {
//			this.getDataSourceTree().expandRow(i);
//		}
//	}
	
	private DefaultMutableTreeNode findDataSourceNode(AbstractDataSourceConfiguration sourceConfiguration) {
		for (int i=0; i<this.getRootNode().getChildCount(); i++) {
			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) this.getRootNode().getChildAt(i);
			if (childNode.getUserObject()==sourceConfiguration) {
				return childNode;
			}
		}
		return null;
	}

	private JSplitPane getJSplitPaneMainPanel() {
		if (jSplitPaneMainPanel == null) {
			jSplitPaneMainPanel = new JSplitPane();
			jSplitPaneMainPanel.setLeftComponent(this.getScrollPaneDataSourceTree());
			jSplitPaneMainPanel.setRightComponent(this.getDataSourceConfigPanel());
			jSplitPaneMainPanel.setDividerLocation(0.2);
		}
		return jSplitPaneMainPanel;
	}
}
