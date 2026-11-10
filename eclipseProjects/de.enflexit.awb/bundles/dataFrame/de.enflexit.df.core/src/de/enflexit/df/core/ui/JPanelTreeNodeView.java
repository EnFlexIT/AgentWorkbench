package de.enflexit.df.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.common.swing.KeyAdapter4Numbers;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.PropertiesConfigurationHelper;
import de.enflexit.df.core.dataSources.DataSource;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;
import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.core.extension.ColumnDescriptionPanel;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.TablesawTableModel;
import de.enflexit.df.core.workbook.DataWorkbook;
import tech.tablesaw.api.Table;

/**
 * The Class JPanelTreeNodeView.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelTreeNodeView extends JPanel implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = -2503356793902058897L;

	private static final String TAB_COLUMN_DESCRIPTION = "Column Description";
	
	public enum DataViewConfiguration {
		No_PropertiesConfiguration,
		PropertiesConfiguration_Top,
		PropertiesConfiguration_Right,
		PropertiesConfiguration_Left,
		PropertiesConfiguration_Bottom
	}
	
	
	private DataController dataController;
	private DataSource lastDataSource;
	
	private Font baseFont = new Font("Dialog", Font.PLAIN, 12);
	private Dimension textFieldDimension = new Dimension(60, 24);
	
	
	private GridBagConstraints gbc_dataViewConfiguration;
	private JComponent jComponentDataViewConfiguration;
	
	private JPanel jPanelData;
	private JTabbedPane jTabbedPaneData;
	private JScrollPane jScrollPaneData;
	private JTable jTableData;
	private JTableHeaderWithToolTips jTableHeaderWithToolTips;
	
	private JPanel jPanelDataless;
	
	private JToolBar jToolBarDatasetNavigation;
		private JButton jButtonDatasetFirst;
		private JButton jButtonDatasetPrevious;
		private JTextField jTextFieldDatasetNo;
		private JButton jButtonDatasetNext;
		private JButton jButtonDatasetLast;
	
		private JToggleButton jToggleButtonEnabledPagination;
		private JLabel jLabelRowsPerPage;
		private JTextField jTextFieldRowsPerPage;
		private JLabel jLabelPageLoaded;
		private JTextField jTextFieldPageLoaded;
	
		private JLabel jLabelColumnDescription;
		private JToggleButton jToggleButtonOrientationBottom;
		private JToggleButton jToggleButtonOrientationRight;
		private JToggleButton jToggleButtonOrientationTop;
		private JToggleButton jToggleButtonOrientationLeft;
		
		private JToggleButton jToggleButtonOrientationClose;
		
	private ColumnDescriptionPanel columnDescriptionPanel;
	private JTabbedPane jTabbedPanePropertiesContainer;
	
	private DataViewConfiguration currentDataViewConfiguration;
	private DataViewConfiguration lastDataViewConfiguration;
	
		
	/**
	 * Instantiates a new JPanelTreeNodeView.
	 * @param dataController the data controller
	 */
	public JPanelTreeNodeView(DataController dataController) {
		this.setDataController(dataController);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		this.setCurrentDataViewConfiguration(DataViewConfiguration.No_PropertiesConfiguration);
		this.getJToggleButtonOrientationClose().setSelected(true);
		
		GridBagConstraints gbc_jToolBarDatasetNavigation = new GridBagConstraints();
		gbc_jToolBarDatasetNavigation.fill = GridBagConstraints.HORIZONTAL;
		gbc_jToolBarDatasetNavigation.insets = new Insets(5, 5, 5, 5);
		gbc_jToolBarDatasetNavigation.gridx = 0;
		gbc_jToolBarDatasetNavigation.gridy = 1;
		this.add(this.getJToolBarDatasetNavigation(), gbc_jToolBarDatasetNavigation);
		
		this.setJToolDatasetNavigationEnabled();
	}
	
	/**
	 * Returns the data controller.
	 * @return the data controller
	 */
	public DataController getDataController() {
		return dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
		if (this.dataController!=null) {
			this.dataController.addPropertyChangeListener(this);
		}
	}
	
	/**
	 * Returns the selected data tree node.
	 * @return the selected data tree node
	 */
	private DefaultMutableTreeNode getSelectedDataTreeNode() {
		return this.getDataController().getSelectionModel().getSelectedDataTreeNode();
	}
	/**
	 * Returns the currently selected data tree node data source.
	 * @return the selected data tree node data source
	 */
	private AbstractDataSourceDTNO<?> getSelectedDataTreeNodeDataSource() {
		return this.getDataController().getSelectionModel().getSelectedDataTreeNodeDataSource();
	}
	/**
	 * Returns the current AbstractPaginationDataLoader or <code>null</code>, if no data tree node can be found.
	 * @return the pagination data loader
	 */
	private AbstractPaginationDataLoader<?> getPaginationDataLoader() {
		AbstractDataSourceDTNO<?> dtnoDS = getSelectedDataTreeNodeDataSource();
		return (dtnoDS==null ? null : dtnoDS.getPaginationDataLoader());
	}
	/**
	 * Sets the pagination data loader activated.
	 * @param activate the new pagination data loader activated
	 */
	private void setPaginationDataLoaderActivated(boolean activate) {
		if (this.getPaginationDataLoader()!=null) {
			this.getPaginationDataLoader().setPaginationActivated(activate);
		}
	}
	
	/**
	 * Sets the current data view configuration.
	 * @param currentDataViewConfiguration the new current data view configuration
	 */
	public void setCurrentDataViewConfiguration(DataViewConfiguration currentDataViewConfiguration) {
		if (currentDataViewConfiguration!=this.currentDataViewConfiguration) {
			this.currentDataViewConfiguration = currentDataViewConfiguration;
			this.setDataViewConfiguration(currentDataViewConfiguration);
		}
	}
	/**
	 * Sets the data view configuration.
	 * @param dataViewConfig the new data view configuration
	 */
	private void setDataViewConfiguration(DataViewConfiguration dataViewConfig) {
		
		if (dataViewConfig==null) return;
		
		if (this.jComponentDataViewConfiguration!=null) {
			this.remove(this.jComponentDataViewConfiguration);
		}
		
		int dividerLocationAbs = 0;
		double dividerLocation = 0.25;
		double resizeWeight = 0.95;
		
		switch (dataViewConfig) {
		case No_PropertiesConfiguration:
			this.jComponentDataViewConfiguration = this.getJPanelData();
			break;
		case PropertiesConfiguration_Top:
			resizeWeight = (1-resizeWeight);
			dividerLocationAbs = (int) (this.getSize().getHeight() * dividerLocation);
			this.jComponentDataViewConfiguration = this.createJSplitPaneData(JSplitPane.VERTICAL_SPLIT, dividerLocation, resizeWeight, this.getJTabbedPanePropertiesContainer(), this.getJPanelData());
			break;
		case PropertiesConfiguration_Bottom:
			dividerLocation = (1 - dividerLocation);
			dividerLocationAbs = (int) (this.getSize().getHeight() * dividerLocation);
			this.jComponentDataViewConfiguration = this.createJSplitPaneData(JSplitPane.VERTICAL_SPLIT, dividerLocation, resizeWeight, this.getJPanelData(), this.getJTabbedPanePropertiesContainer());
			break;
		case PropertiesConfiguration_Left:
			resizeWeight = (1-resizeWeight);
			dividerLocationAbs = (int) (this.getSize().getWidth() * dividerLocation);
			this.jComponentDataViewConfiguration = this.createJSplitPaneData(JSplitPane.HORIZONTAL_SPLIT, dividerLocation, resizeWeight, this.getJTabbedPanePropertiesContainer(), this.getJPanelData());
			break;
		case PropertiesConfiguration_Right:
			dividerLocation = (1 - dividerLocation);
			dividerLocationAbs = (int) (this.getSize().getWidth() * dividerLocation);
			this.jComponentDataViewConfiguration = this.createJSplitPaneData(JSplitPane.HORIZONTAL_SPLIT, dividerLocation, resizeWeight, this.getJPanelData(), this.getJTabbedPanePropertiesContainer());
			break;
		}
		
		if (dataViewConfig!=DataViewConfiguration.No_PropertiesConfiguration) {
			this.lastDataViewConfiguration = dataViewConfig;
			this.updatePropertiesConfigurationPanel(this.getSelectedDataTreeNode(), this.getSelectedDataTreeNodeDataSource());
		}
		
		this.add(this.jComponentDataViewConfiguration, this.getGridBagConstraintsDataViewConfiguration());
		this.validate();
		this.repaint();
		
		if (this.jComponentDataViewConfiguration instanceof JSplitPane jspDV) {
			jspDV.setDividerLocation(dividerLocationAbs);
		}
		
	}
	
	/**
	 * Returns the container JPanel for detail view property configurations.
	 * @return the JPanel detail view properties container
	 */
	private JTabbedPane getJTabbedPanePropertiesContainer() {
		if (jTabbedPanePropertiesContainer==null) {
			jTabbedPanePropertiesContainer = new JTabbedPane();
			jTabbedPanePropertiesContainer.setTabPlacement(JTabbedPane.BOTTOM);
			this.addUpdateOrRemovePropertiesTab(TAB_COLUMN_DESCRIPTION, this.getJPanelColumnDescription());
		}
		return jTabbedPanePropertiesContainer;
	}
	/**
	 * Returns the component by title.
	 *
	 * @param tabbedPane the JTabbedPane pane
	 * @param title the title
	 * @return the component by title
	 */
	private JComponent getPropertiesTabComponentByTitle(String title) {
		for (int i = 0; i < this.getJTabbedPanePropertiesContainer().getTabCount(); i++) {
			if (title.equals(this.getJTabbedPanePropertiesContainer().getTitleAt(i))) {
				return (JComponent) this.getJTabbedPanePropertiesContainer().getComponentAt(i);
			}
		}
		return null;
	}
	/**
	 * Returns the list of properties configuration titles.
	 * @return the properties configuration tab title list
	 */
	private List<String> getPropertiesConfigurationTabTitleList() {
		List<String> tabTitleList = new ArrayList<>();
		for (int i = 0; i < this.getJTabbedPanePropertiesContainer().getTabCount(); i++) {
			tabTitleList.add(this.getJTabbedPanePropertiesContainer().getTitleAt(i));
		}
		return tabTitleList;
	}
	/**
	 * Adds, replaces or removes a properties tab.
	 *
	 * @param title the title of the tab
	 * @param componentToUse the component to use. Set this to <code>null</code> if you want to remove a component
	 */
	private void addUpdateOrRemovePropertiesTab(String title, JComponent componentToUse) {
		
		if (title==null) return;
		
		// --- Check for previous components ------------------------
		JComponent oldComp = this.getPropertiesTabComponentByTitle(title);
		if (oldComp!=null) {
			// --- Remove old component? ----------------------------
			if (componentToUse==null || componentToUse!=oldComp) {
				// --- Just remove component ------------------------
				this.getJTabbedPanePropertiesContainer().remove(oldComp);
			}
		}
		
		// --- Add the component to use -----------------------------
		if (componentToUse!=null) {
			this.getJTabbedPanePropertiesContainer().addTab(title, componentToUse);
		}
		// --- Refresh view -----------------------------------------
		this.getJTabbedPanePropertiesContainer().validate();
		this.getJTabbedPanePropertiesContainer().repaint();
		
	}
	
	/**
	 * Returns the column description panel.
	 * @return the column description panel
	 */
	public ColumnDescriptionPanel getColumnDescriptionPanel() {
		// --- Default Case -----------------------------------------
		if (columnDescriptionPanel==null) {
			columnDescriptionPanel = new JPanelColumnDescription();
		}
		// --- Has the workbook selection changed? ------------------
		DataWorkbook selDW = this.getDataController().getSelectionModel().getSelectedDataWorkbook();
		ColumnDescriptionPanel cdPanel = selDW!=null ? selDW.getExtensionCache().getColumnDescriptionPanel() : null;
		if (cdPanel!=null) {
			return cdPanel;
		}
		return columnDescriptionPanel;
	}
	/**
	 * Returns the column description panel.
	 * @return the column description panel
	 */
	private JPanel getJPanelColumnDescription() {
		return this.getColumnDescriptionPanel().getActualColumnDescriptionPanel();
	}
	
	/**
	 * Depending on the extension configured, sets the column description panel.
	 * @param dtnoDS the current data source to be used
	 */
	private void setColumnDescriptionPanel(AbstractDataSourceDTNO<?> dtnoDS) {
		
		ColumnDescriptionPanel colDescPanel = this.getColumnDescriptionPanel();
		if (dtnoDS==null ||  dtnoDS.getTable()==null || dtnoDS.getPaginationDataLoader()==null) {
			// --- Nothing to show with data -> remove column description -----  
			colDescPanel.setColumnDescription(null);
			this.addUpdateOrRemovePropertiesTab(TAB_COLUMN_DESCRIPTION, null);
			
		} else {
			// --- Data are to be shown ---------------------------------------
			JPanel currPanelColDesc = (JPanel) this.getPropertiesTabComponentByTitle(TAB_COLUMN_DESCRIPTION);
			JPanel newPanelColDesc  = colDescPanel.getActualColumnDescriptionPanel();
			if (newPanelColDesc != currPanelColDesc) {
				// --- Exchange ColumnDescriptionPanel ------------------------
				colDescPanel.setColumnDescription(null);
				this.addUpdateOrRemovePropertiesTab(TAB_COLUMN_DESCRIPTION, newPanelColDesc);
				
			} else {
				// --- Changed data source? -----------------------------------
				DataSource currDataSource = dtnoDS.getDataSource();
				if (currDataSource!=this.lastDataSource) {
					colDescPanel.setColumnDescription(null);
				}
				this.lastDataSource = currDataSource;
			}
		}
	}
	
	
	/**
	 * Returns the grid bag constraints data view configuration.
	 * @return the grid bag constraints data view configuration
	 */
	public GridBagConstraints getGridBagConstraintsDataViewConfiguration() {
		if (gbc_dataViewConfiguration==null) {
			gbc_dataViewConfiguration = new GridBagConstraints();
			gbc_dataViewConfiguration.fill = GridBagConstraints.BOTH;
			gbc_dataViewConfiguration.gridx = 0;
			gbc_dataViewConfiguration.gridy = 0;
		}
		return gbc_dataViewConfiguration;
	}
	/**
	 * Creates a JSplitPane for the data and the column description.
	 *
	 * @param orientation the orientation
	 * @param dividerLocation the divider location
	 * @param resizeWeight the resize weight
	 * @param topLeftComponent the top left component
	 * @param bottomRightComponent the bottom right component
	 * @return the JSplitPane for the data / property combination
	 */
	private JSplitPane createJSplitPaneData(int orientation, double dividerLocation, double resizeWeight, JComponent topLeftComponent, JComponent bottomRightComponent) {
		JSplitPane jSplitPaneData = new JSplitPane();
		jSplitPaneData.setDividerSize(5);
		jSplitPaneData.setOrientation(orientation);
		jSplitPaneData.setDividerLocation(dividerLocation);
		jSplitPaneData.setResizeWeight(resizeWeight);
		if (orientation==JSplitPane.HORIZONTAL_SPLIT) {
			jSplitPaneData.setLeftComponent(topLeftComponent);
			jSplitPaneData.setRightComponent(bottomRightComponent);
		} else {
			jSplitPaneData.setTopComponent(topLeftComponent);
			jSplitPaneData.setBottomComponent(bottomRightComponent);
		}
		return jSplitPaneData;
	}
	
	private JPanel getJPanelData() {
		if (jPanelData==null) {
			jPanelData = new JPanel();
			jPanelData.setLayout(new BorderLayout());
			
			// TODO if no further detail tabs ... 
			int noOfTabs = 1;
			if (noOfTabs>1) {
				jPanelData.add(this.getJTabbedPaneData(), BorderLayout.CENTER);
			} else {
				jPanelData.add(this.getJScrollPaneData(), BorderLayout.CENTER);
			}
		}
		return jPanelData;
	}
	
	private JTabbedPane getJTabbedPaneData() {
		if (jTabbedPaneData==null) {
			jTabbedPaneData = new JTabbedPane();
			jTabbedPaneData.setTabPlacement(JTabbedPane.TOP);
			jTabbedPaneData.addTab("Data", this.getJScrollPaneData());
		}
		return jTabbedPaneData;
	}
	private JScrollPane getJScrollPaneData() {
		if (jScrollPaneData == null) {
			jScrollPaneData = new JScrollPane();
			jScrollPaneData.setViewportView(this.getJPanelDataless());
		}
		return jScrollPaneData;
	}
	private JTable getJTableData() {
		if (jTableData == null) {
			jTableData = new JTable();
			jTableData.setFillsViewportHeight(true);
			jTableData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			jTableData.setDefaultRenderer(Object.class, new DateTimeTableCellRenderer("dd.MM.yyyy HH:mm:ss"));
			jTableData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent lse) {
					if (lse.getValueIsAdjusting()==true) return;
					JPanelTreeNodeView.this.setDatasetSelection(lse.getFirstIndex(), lse.getLastIndex(), false);
				}
			});
			
			jTableData.setTableHeader(this.getJTableHeaderWithTooltips());
			jTableData.getTableHeader().setReorderingAllowed(false);
		}
		return jTableData;
	}
	/**
	 * Returns the j table header with tool tips.
	 * @return the j table header with tool tips
	 */
	private JTableHeaderWithToolTips  getJTableHeaderWithTooltips() {
		if (jTableHeaderWithToolTips==null) {
			jTableHeaderWithToolTips = new JTableHeaderWithToolTips(this.getJTableData().getColumnModel());
			
			// --- Show a column description on the side panel when clicking on the header.
			jTableHeaderWithToolTips.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					int viewColumn = jTableHeaderWithToolTips.columnAtPoint(me.getPoint());

			        if (viewColumn < 0) {
			            return;
			        }

			        int columnIndex = JPanelTreeNodeView.this.getJTableData().convertColumnIndexToModel(viewColumn);
			        String columnName = JPanelTreeNodeView.this.getJTableData().getColumnName(columnIndex);
			        ColumnDescription colDesc = JPanelTreeNodeView.this.getColumnDescription(columnName);
			        JPanelTreeNodeView.this.getColumnDescriptionPanel().setColumnDescription(colDesc);
				}
			});
		}
		return jTableHeaderWithToolTips;
	}
	
	private ColumnDescription getColumnDescription(String columnName) {
		AbstractDataSourceDTNO<?> dsDTNO = this.getDataController().getSelectionModel().getSelectedDataTreeNodeDataSource();
        for (ColumnDescription colDesc : dsDTNO.getColumnDescriptionList()) {
        	if(colDesc.getColumnName().equals(columnName)){
        		return colDesc;
        	}
        }
        return null;
	}
	
	private JPanel getJPanelDataless() {
		if (jPanelDataless==null) {
			jPanelDataless = new JPanel();
		}
		return jPanelDataless;
	}
	
	
	private JToolBar getJToolBarDatasetNavigation() {
		if (jToolBarDatasetNavigation == null) {
			jToolBarDatasetNavigation = new JToolBar();
			jToolBarDatasetNavigation.setFloatable(false);
			jToolBarDatasetNavigation.setRollover(true);
			jToolBarDatasetNavigation.setPreferredSize(new Dimension(120, 26));
			
			jToolBarDatasetNavigation.add(this.getJButtonDatasetFirst());
			jToolBarDatasetNavigation.add(this.getJButtonDatasetPrevious());
			jToolBarDatasetNavigation.add(this.getJTextFieldDatasetNo());
			jToolBarDatasetNavigation.add(this.getJButtonDatasetNext());
			jToolBarDatasetNavigation.add(this.getJButtonDatasetLast());
			
			jToolBarDatasetNavigation.addSeparator();
			jToolBarDatasetNavigation.add(this.getJToggleButtonEnabledPagination());
			jToolBarDatasetNavigation.add(this.getJLabelRowsPerPage());
			jToolBarDatasetNavigation.add(this.getJTextFieldRowsPerPage());
			
			jToolBarDatasetNavigation.add(this.getJLabelPagesLoaded());
			jToolBarDatasetNavigation.add(this.getJTextFieldPageLoaded());
			
			jToolBarDatasetNavigation.addSeparator();
			jToolBarDatasetNavigation.add(this.getJLabelColumnDescription());
			// --- Removed to avoid further efforts for detail properties -----
			//jToolBarDatasetNavigation.add(this.getJToggleButtonOrientationBottom());
			//jToolBarDatasetNavigation.add(this.getJToggleButtonOrientationTop());
			jToolBarDatasetNavigation.add(this.getJToggleButtonOrientationLeft());
			jToolBarDatasetNavigation.add(this.getJToggleButtonOrientationRight());
			jToolBarDatasetNavigation.addSeparator();
			jToolBarDatasetNavigation.add(this.getJToggleButtonOrientationClose());
			
			jToolBarDatasetNavigation.addSeparator();

			
			ButtonGroup bgColumnDescription = new ButtonGroup();
			bgColumnDescription.add(this.getJToggleButtonOrientationBottom());
			bgColumnDescription.add(this.getJToggleButtonOrientationTop());
			bgColumnDescription.add(this.getJToggleButtonOrientationLeft());
			bgColumnDescription.add(this.getJToggleButtonOrientationRight());
			bgColumnDescription.add(this.getJToggleButtonOrientationClose());
			
		}
		return jToolBarDatasetNavigation;
	}
	/**
	 * Depending on the current selection, sets the navigation toolbar enabled.
	 */
	private void setJToolDatasetNavigationEnabled() {
		
		boolean isEnabledToolBar = this.getJTabbedPanePropertiesContainer().getTabCount()>0;
		
		this.getJToggleButtonEnabledPagination().setSelected(isEnabledToolBar && this.getPaginationDataLoader()!=null && this.getPaginationDataLoader().isPaginationActivated()==true);
		this.setJToggleButtonEnabledPaginationIcon();
		
		this.getJButtonDatasetFirst().setEnabled(isEnabledToolBar);
		this.getJButtonDatasetPrevious().setEnabled(isEnabledToolBar);
		this.getJTextFieldDatasetNo().setEnabled(isEnabledToolBar);
		this.getJButtonDatasetNext().setEnabled(isEnabledToolBar);
		this.getJButtonDatasetLast().setEnabled(isEnabledToolBar);
		
		this.getJToggleButtonEnabledPagination().setEnabled(isEnabledToolBar);
		this.getJLabelRowsPerPage().setEnabled(isEnabledToolBar);
		this.getJTextFieldRowsPerPage().setEnabled(isEnabledToolBar);
		
		this.getJLabelPagesLoaded().setEnabled(isEnabledToolBar);
		this.getJTextFieldPageLoaded().setEnabled(isEnabledToolBar);
		
		this.getJToggleButtonOrientationBottom().setEnabled(isEnabledToolBar);
		this.getJToggleButtonOrientationTop().setEnabled(isEnabledToolBar);
		this.getJToggleButtonOrientationLeft().setEnabled(isEnabledToolBar);
		this.getJToggleButtonOrientationRight().setEnabled(isEnabledToolBar);
		this.getJToggleButtonOrientationClose().setEnabled(isEnabledToolBar);
	}

	private JButton getJButtonDatasetFirst() {
		if (jButtonDatasetFirst==null) {
			jButtonDatasetFirst = new JButton(new AwbThemeImageIcon(BundleHelper.getImageIcon("Dataset_First.png")));
			jButtonDatasetFirst.setPreferredSize(new Dimension(26, 26));
			jButtonDatasetFirst.addActionListener(this);
		}
		return jButtonDatasetFirst;
	}
		
	private JButton getJButtonDatasetPrevious() {
		if (jButtonDatasetPrevious==null) {
			jButtonDatasetPrevious = new JButton(new AwbThemeImageIcon(BundleHelper.getImageIcon("Dataset_Previous.png")));
			jButtonDatasetPrevious.setPreferredSize(new Dimension(26, 26));
			jButtonDatasetPrevious.addActionListener(this);
		}
		return jButtonDatasetPrevious;
	}
	
	private JTextField getJTextFieldDatasetNo() {
		if (jTextFieldDatasetNo==null) {
			jTextFieldDatasetNo = new JTextField();
			jTextFieldDatasetNo.setPreferredSize(this.textFieldDimension);
			jTextFieldDatasetNo.setMaximumSize(this.textFieldDimension);
			jTextFieldDatasetNo.setFont(this.baseFont);
			jTextFieldDatasetNo.setHorizontalAlignment(JTextField.CENTER);
			jTextFieldDatasetNo.addKeyListener(new KeyAdapter4Numbers(false));
			jTextFieldDatasetNo.addActionListener(this);
			
		}
		return jTextFieldDatasetNo;
	}
	private JButton getJButtonDatasetNext() {
		if (jButtonDatasetNext==null) {
			jButtonDatasetNext = new JButton(new AwbThemeImageIcon(BundleHelper.getImageIcon("Dataset_Next.png")));
			jButtonDatasetNext.setPreferredSize(new Dimension(26, 26));
			jButtonDatasetNext.addActionListener(this);
		}
		return jButtonDatasetNext;
	}

	private JButton getJButtonDatasetLast() {
		if (jButtonDatasetLast==null) {
			jButtonDatasetLast = new JButton(new AwbThemeImageIcon(BundleHelper.getImageIcon("Dataset_Last.png")));
			jButtonDatasetLast.setPreferredSize(new Dimension(26, 26));
			jButtonDatasetLast.addActionListener(this);
		}
		return jButtonDatasetLast;
	}

	
	private JToggleButton getJToggleButtonEnabledPagination() {
		if (jToggleButtonEnabledPagination==null) {
			jToggleButtonEnabledPagination = new JToggleButton();
			jToggleButtonEnabledPagination.setToolTipText("Pagination On / Off");
			jToggleButtonEnabledPagination.setPreferredSize(new Dimension(26, 26));
			jToggleButtonEnabledPagination.setMinimumSize(new Dimension(26, 26));
			jToggleButtonEnabledPagination.addActionListener(this);
			this.setJToggleButtonEnabledPaginationIcon();
		}
		return jToggleButtonEnabledPagination;
	}
	
	private void setJToggleButtonEnabledPaginationIcon() {
		if (this.getJToggleButtonEnabledPagination().isSelected()==true) {
			this.getJToggleButtonEnabledPagination().setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("Pagination-On.png")));
			this.getJToggleButtonEnabledPagination().setToolTipText("Pagination enabled");
			this.getJLabelRowsPerPage().setEnabled(true);
			this.getJTextFieldRowsPerPage().setEnabled(true);
			this.getJTextFieldRowsPerPage().setEditable(true);
		} else {
			this.getJToggleButtonEnabledPagination().setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("Pagination-Off.png")));
			this.getJToggleButtonEnabledPagination().setToolTipText("Pagination disbaled");
			this.getJLabelRowsPerPage().setEnabled(false);
			this.getJTextFieldRowsPerPage().setEnabled(false);
			this.getJTextFieldRowsPerPage().setEditable(false);
		}
	}
	
	private JLabel getJLabelRowsPerPage() {
		if (jLabelRowsPerPage==null) {
			jLabelRowsPerPage = new JLabel("Rows / Page: ");
			jLabelRowsPerPage.setFont(baseFont.deriveFont(Font.BOLD));
		}
		return jLabelRowsPerPage;
	}
	private JTextField getJTextFieldRowsPerPage() {
		if (jTextFieldRowsPerPage==null) {
			jTextFieldRowsPerPage = new JTextField();
			jTextFieldRowsPerPage.setPreferredSize(this.textFieldDimension);
			jTextFieldRowsPerPage.setMaximumSize(this.textFieldDimension);
			jTextFieldRowsPerPage.setFont(this.baseFont);
			jTextFieldRowsPerPage.setHorizontalAlignment(JTextField.CENTER);
			jTextFieldRowsPerPage.addKeyListener(new KeyAdapter4Numbers(false));
			jTextFieldRowsPerPage.addActionListener(this);
		}
		return jTextFieldRowsPerPage;
	}
	
	private JLabel getJLabelPagesLoaded() {
		if (jLabelPageLoaded==null) {
			jLabelPageLoaded = new JLabel("  Pages-Loaded: ");
			jLabelPageLoaded.setFont(this.baseFont.deriveFont(Font.BOLD));
		}
		return jLabelPageLoaded;
	}
	private JTextField getJTextFieldPageLoaded() {
		if (jTextFieldPageLoaded==null) {
			jTextFieldPageLoaded = new JTextField();
			jTextFieldPageLoaded.setPreferredSize(this.textFieldDimension);
			jTextFieldPageLoaded.setMaximumSize(this.textFieldDimension);
			jTextFieldPageLoaded.setFont(this.baseFont);
			jTextFieldPageLoaded.setHorizontalAlignment(JTextField.CENTER);
			jTextFieldPageLoaded.setEditable(false);
		}
		return jTextFieldPageLoaded;
	}
	
	
	private JLabel getJLabelColumnDescription() {
		if (jLabelColumnDescription==null) {
			jLabelColumnDescription = new JLabel(" Property View: ");
			jLabelColumnDescription.setFont(this.baseFont.deriveFont(Font.BOLD));
		}
		return jLabelColumnDescription;
	}
	
	private JToggleButton getJToggleButtonOrientationBottom() {
		if (jToggleButtonOrientationBottom== null) {
			jToggleButtonOrientationBottom = new JToggleButton();
			jToggleButtonOrientationBottom.setIcon(BundleHelper.getImageIcon("OrientationBottom.png"));
			jToggleButtonOrientationBottom.setToolTipText("Property View: Bottom");
			jToggleButtonOrientationBottom.setPreferredSize(new Dimension(26, 26));
			jToggleButtonOrientationBottom.setMargin(new Insets(0, 0, 0, 0));
			jToggleButtonOrientationBottom.addActionListener(this);
		}
		return jToggleButtonOrientationBottom;
	}
	private JToggleButton getJToggleButtonOrientationTop() {
		if (jToggleButtonOrientationTop== null) {
			jToggleButtonOrientationTop = new JToggleButton();
			jToggleButtonOrientationTop.setIcon(BundleHelper.getImageIcon("OrientationTop.png"));
			jToggleButtonOrientationTop.setToolTipText("Property View: Top");
			jToggleButtonOrientationTop.setPreferredSize(new Dimension(26, 26));
			jToggleButtonOrientationTop.setMargin(new Insets(0, 0, 0, 0));
			jToggleButtonOrientationTop.addActionListener(this);
		}
		return jToggleButtonOrientationTop;
	}
	private JToggleButton getJToggleButtonOrientationLeft() {
		if (jToggleButtonOrientationLeft == null) {
			jToggleButtonOrientationLeft = new JToggleButton();
			jToggleButtonOrientationLeft.setIcon(BundleHelper.getImageIcon("OrientationLeft.png"));
			jToggleButtonOrientationLeft.setToolTipText("Property View: Left");
			jToggleButtonOrientationLeft.setPreferredSize(new Dimension(26, 26));
			jToggleButtonOrientationLeft.setMargin(new Insets(0, 0, 0, 0));
			jToggleButtonOrientationLeft.addActionListener(this);
		}
		return jToggleButtonOrientationLeft;
	}
	private JToggleButton getJToggleButtonOrientationRight() {
		if (jToggleButtonOrientationRight == null) {
			jToggleButtonOrientationRight = new JToggleButton();
			jToggleButtonOrientationRight.setIcon(BundleHelper.getImageIcon("OrientationRight.png"));
			jToggleButtonOrientationRight.setToolTipText("Property View: Right");
			jToggleButtonOrientationRight.setPreferredSize(new Dimension(26, 26));
			jToggleButtonOrientationRight.setMargin(new Insets(0, 0, 0, 0));
			jToggleButtonOrientationRight.addActionListener(this);
		}
		return jToggleButtonOrientationRight;
	}
	
	private JToggleButton getJToggleButtonOrientationClose() {
		if (jToggleButtonOrientationClose== null) {
			jToggleButtonOrientationClose = new JToggleButton();
			jToggleButtonOrientationClose.setIcon(BundleHelper.getImageIcon("MBclose.png"));
			jToggleButtonOrientationClose.setToolTipText("Close Property View");
			jToggleButtonOrientationClose.setPreferredSize(new Dimension(26, 26));
			jToggleButtonOrientationClose.setMargin(new Insets(0, 0, 0, 0));
			jToggleButtonOrientationClose.addActionListener(this);
		}
		return jToggleButtonOrientationClose;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		DefaultMutableTreeNode treeNode = this.getSelectedDataTreeNode();
		AbstractDataSourceDTNO<?> dtnoDS = this.getSelectedDataTreeNodeDataSource();
		
		String propChanged = evt.getPropertyName();
		
		switch (propChanged) {
		case DataController.DC_DATA_LOADED:
			// --- Data could be loaded in a dedicated thread -------
			AffectedDataObjects ado = (AffectedDataObjects) evt.getNewValue();
			dtnoDS = ado.getDataTreeNodeObjectDataSource();
			
			if (SwingUtilities.isEventDispatchThread()==true) {
				// --- Just execute view adjustment -----------------
				this.setDetailView(dtnoDS);
				
			} else {
				// --- Hand over to Swing Thread --------------------
				final AbstractDataSourceDTNO<?> dtnoDSFinal = dtnoDS; 
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JPanelTreeNodeView.this.setDetailView(dtnoDSFinal);
					}
				});
			}
			break;
			
		case DataController.DC_NEW_TREE_PATH_SELECTED:
			
			// --- Current selection has no table -------------------
			if (this.currentDataViewConfiguration!=DataViewConfiguration.No_PropertiesConfiguration && this.isDescriptionPanelVisible()==false) {
				// --- Properties panel should be visible! ---------- 
				this.setDataViewConfiguration(this.currentDataViewConfiguration);
			}
			
			this.setColumnDescriptionPanel(dtnoDS);
			this.updatePropertiesConfigurationPanel(treeNode, dtnoDS);
			this.setDetailView(dtnoDS);
			
			if (this.isDescriptionPanelVisible()==true && this.getJTabbedPanePropertiesContainer().getTabCount()==0) {
				this.setDataViewConfiguration(DataViewConfiguration.No_PropertiesConfiguration);
			}
			break;
			
		case DataController.DC_PROPERTIES_CONFIGURATION_SHOW:
			// --- Show properties configuration or not -------------
			boolean isShow = (boolean) evt.getNewValue();
			if (isShow==false) {
				this.setCurrentDataViewConfiguration(DataViewConfiguration.No_PropertiesConfiguration);
			} else {
				this.setCurrentDataViewConfiguration(this.lastDataViewConfiguration!=null ? this.lastDataViewConfiguration : DataViewConfiguration.PropertiesConfiguration_Right);
			}
			break;
		}
		
	}
	
	/**
	 * Update properties configuration panel.
	 *
	 * @param treeNode the tree node
	 * @param dtnoDS the DTNO for the data source
	 */
	private void updatePropertiesConfigurationPanel(DefaultMutableTreeNode treeNode, AbstractDataSourceDTNO<?> dtnoDS) {
		
		// --- Build / Refresh the properties view? ----------------- 
		if (this.currentDataViewConfiguration==DataViewConfiguration.No_PropertiesConfiguration) return; 
		
		// --- Get the list of the current property tabs ------------
		List<String> tabsToRemove = this.getPropertiesConfigurationTabTitleList(); 
		
		// --- Check for data source properties --------------------- 
		if (dtnoDS!=null && dtnoDS.getDataSourceIntegration().providesPropertiesConfiguration(this.getDataController(), treeNode)==true) {
			String title = dtnoDS.getDataSourceIntegration().getPropertiesTitle();
			JComponent propComp = dtnoDS.getDataSourceIntegration().getPropertiesConfigurationPanel(this.getDataController(), treeNode);
			this.addUpdateOrRemovePropertiesTab(title, propComp);
			tabsToRemove.remove(title);
		}
		
		// --- Add service properties panels ------------------------
		List<PropertiesConfigurationPanel> pcpList = PropertiesConfigurationHelper.getPropertiesConfigurationPanelListForTreeNode(this.getDataController(), treeNode);
		for (PropertiesConfigurationPanel pcp : pcpList) {
			this.addUpdateOrRemovePropertiesTab(pcp.getPropertiesTitle(), pcp.getPropertiesConfigurationPanel(this.getDataController(), treeNode));
			tabsToRemove.remove(pcp.getPropertiesTitle());
		}
		
		// --- Remove tabs that are not required here ---------------
		tabsToRemove.forEach(pcpTitle -> {
			if (pcpTitle.equals(TAB_COLUMN_DESCRIPTION)==false) {
				this.addUpdateOrRemovePropertiesTab(pcpTitle, null);	
			}
		});
		
	}
	
	
	/**
	 * Sets the detail view.
	 * @param dtnoDS the data source tree node to show
	 */
	private void setDetailView(AbstractDataSourceDTNO<?> dtnoDS) {

		// --- Direct exit? -----------------------------------------
		AbstractDataSourceDTNO<?> dtnoDsSelected = this.getSelectedDataTreeNodeDataSource();
		boolean isMissingDTNO   = (dtnoDS==null || dtnoDsSelected==null);
		boolean isDifferentData = (dtnoDS!=null && dtnoDsSelected!=null && dtnoDS!=dtnoDsSelected);
		if (isDifferentData==true) return;
		
		JComponent uiDetail = this.getJPanelDataless();
		if (isMissingDTNO==false) {
			// ------------------------------------------------------
			// --- DataSource that requires sub configuration ? -----
			// ------------------------------------------------------
			if (dtnoDsSelected.getDataSource().requiresSubConfiguration()==false) {
				// --------------------------------------------------
				// --- Display the data table -----------------------
				// --------------------------------------------------
				Table tablesawDataTable = dtnoDS.getTable();
				if (tablesawDataTable!=null) {
					// --- Show table data --------------------------
					this.getJTableData().setModel(new TablesawTableModel(tablesawDataTable));
					// --- Set the column descriptions to header --- 
					if (dtnoDsSelected instanceof AbstractDataSourceDTNO<?>) {
						this.getJTableHeaderWithTooltips().setColumnDescriptionList(dtnoDsSelected.getColumnDescriptionList());
					} else {
						this.getJTableHeaderWithTooltips().setColumnDescriptionList(null);
					}

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JPanelTreeNodeView.this.setDatasetSelection(dtnoDS.getRowSelected(), 0, true);
							JPanelTreeNodeView.this.getJTextFieldRowsPerPage().setText(dtnoDS.getPaginationDataLoader().getNumberOfRecordsPerPage() + "");
							JPanelTreeNodeView.this.getJTextFieldPageLoaded().setText(dtnoDS.getPaginationDataLoader().getPageNumberLoaded() + "");
						}
					});
					
				} else {
					// --- Try loading? -----------------------------
					dtnoDS.loadNextPageAsynchronous();
					this.getJTableData().setModel(new DefaultTableModel());
				}
				uiDetail = this.getJTableData();
				
			} else {
				// --------------------------------------------------
				// --- Configuration Panel for sub configuration? ---
				// --------------------------------------------------
				JComponent detailViewPanel = dtnoDS.getDataSourceIntegration().getDetailViewPanel();
				if (detailViewPanel!=null) {
					uiDetail = detailViewPanel;
				}
				this.getColumnDescriptionPanel().setColumnDescription(null);
			}
			
		}
		this.setJToolDatasetNavigationEnabled();
		
		// --- Set UI component -------------------------------------
		this.getJScrollPaneData().setViewportView(uiDetail);
		this.getJScrollPaneData().validate();
		this.getJScrollPaneData().repaint();
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonDatasetFirst()) {
			this.setDatasetSelection(null, 0, true);
		} else if (ae.getSource()==this.getJButtonDatasetPrevious()) {
			this.setDatasetSelection(null, -1, true);
		} else if (ae.getSource()==this.getJButtonDatasetNext()) {
			this.setDatasetSelection(null, 1, true);
		} else if (ae.getSource()==this.getJButtonDatasetLast()) {
			this.setDatasetSelection(null, Integer.MAX_VALUE, true);
		} else if (ae.getSource()==this.getJTextFieldDatasetNo()) {
			// --- Jump to the selected dataset number --------------
			Integer dsNo2Select = null;
			try {
				dsNo2Select = Integer.parseInt(this.getJTextFieldDatasetNo().getText());
			} catch (Exception ex) { }
			if (dsNo2Select!=null) {
				this.setDatasetSelection(dsNo2Select, 0, true);
			}
			
		} else if (ae.getSource()==this.getJToggleButtonEnabledPagination()) {
			// --- React on pagination toggle ----------------------- 
			this.setJToggleButtonEnabledPaginationIcon();
			this.setPaginationDataLoaderActivated(this.getJToggleButtonEnabledPagination().isSelected());
			this.getSelectedDataTreeNodeDataSource().reloadDataTableAsynchronous();
			
		} else if (ae.getSource()==this.getJTextFieldRowsPerPage()) {
			// --- Change the number of rows per page ---------------
			int newRowsPerPage = -1;
			try {
				newRowsPerPage = Integer.parseInt(this.getJTextFieldRowsPerPage().getText().trim());
				if (newRowsPerPage != this.getPaginationDataLoader().getNumberOfRecordsPerPage()) {
					this.getPaginationDataLoader().setNumberOfRecordsPerPage(newRowsPerPage);
					this.getSelectedDataTreeNodeDataSource().reloadDataTableAsynchronous();
					this.getJButtonDatasetLast().requestFocus();
				}
				
			} catch (Exception ex) { }
			
		} else if (ae.getSource()==this.getJToggleButtonOrientationBottom()) {
			this.setCurrentDataViewConfiguration(DataViewConfiguration.PropertiesConfiguration_Bottom);
		} else if (ae.getSource()==this.getJToggleButtonOrientationTop()) {
			this.setCurrentDataViewConfiguration(DataViewConfiguration.PropertiesConfiguration_Top);
		} else if (ae.getSource()==this.getJToggleButtonOrientationLeft()) {
			this.setCurrentDataViewConfiguration(DataViewConfiguration.PropertiesConfiguration_Left);
		} else if (ae.getSource()==this.getJToggleButtonOrientationRight()) {
			this.setCurrentDataViewConfiguration(DataViewConfiguration.PropertiesConfiguration_Right);
		} else if (ae.getSource()==this.getJToggleButtonOrientationClose()) {
			this.setCurrentDataViewConfiguration(DataViewConfiguration.No_PropertiesConfiguration);
		}
	}
	
	/**
	 * Sets the dataset selection.
	 *
	 * @param dataRowToSelect the data row to select; may be <code>null</code> for direction!=0 and isSelectInTable==true
	 * @param direction the second index
	 * @param isSelectInTable the is select in table
	 */
	private void setDatasetSelection(Integer dataRowToSelect, int direction, boolean isSelectInTable) {
		
		AbstractDataSourceDTNO<?> dtnoDS = this.getSelectedDataTreeNodeDataSource();
		if (dtnoDS==null) return;
		
		// --- Adjust number of data row text field -----------------
		ListSelectionModel selModel = this.getJTableData().getSelectionModel();
		if (selModel.getSelectedItemsCount()==1) {
			int rowSelected = selModel.getSelectedIndices()[0] + 1;
			this.getJTextFieldDatasetNo().setText(rowSelected + "");
		    // --- Remind selected data row ------------------------- 
		    dtnoDS.setRowSelected(rowSelected);
		    
		} else {
			this.getJTextFieldDatasetNo().setText("");
		}
		
		// --- Adjust table selection? ------------------------------
		if (isSelectInTable==true) {
			
			if (dataRowToSelect!=null) {
				// --- React on text field changes ------------------ 
				if (dataRowToSelect<1) {
					dataRowToSelect = 1;
				} else if (dataRowToSelect > this.getJTableData().getRowCount()) {
					this.setDatasetSelection(null, Integer.MAX_VALUE, true);
					return;
				}
				
			} else {
				// --- React on navigation buttons ------------------
				switch (direction) {
				case 0:
					dataRowToSelect = 1;
					break;
				case -1: 
					dataRowToSelect = this.getSelectedDataRow(selModel.getSelectedIndices(), false) - 1;
					if (dataRowToSelect<1) dataRowToSelect = 1;
					break;
				case 1: 
					dataRowToSelect = this.getSelectedDataRow(selModel.getSelectedIndices(), true) + 1;
					if (dataRowToSelect>this.getJTableData().getRowCount()) {
						dataRowToSelect = this.getJTableData().getRowCount();
					}
					if (dataRowToSelect==this.getJTableData().getRowCount() && dtnoDS.getPaginationDataLoader().isPaginationActivated()==true) {
						dtnoDS.loadNextPageAsynchronous();
					}
					break;
				case Integer.MAX_VALUE:
					dataRowToSelect = this.getJTableData().getRowCount();
					if (dtnoDS.getPaginationDataLoader()!=null && dtnoDS.getPaginationDataLoader().isPaginationActivated()==true) {
						dtnoDS.loadNextPageAsynchronous();
					}
					break;
				}
			}
			
			selModel.setSelectionInterval(dataRowToSelect-1, dataRowToSelect-1);
			this.getJTextFieldDatasetNo().setText(dataRowToSelect + "");
			
		    Rectangle rect = this.getJTableData().getCellRect(dataRowToSelect-1, 0, true);
		    this.getJTableData().scrollRectToVisible(rect);
			
		    // --- Remind selected data row ------------------------- 
		    dtnoDS.setRowSelected(dataRowToSelect);
		}
	}
	
	
	/**
	 * Returns a selected index depending on the direction, a user wants to navigate.
	 *
	 * @param selectedIndices the selected indices
	 * @param getMaxValue the get max value
	 * @return the selected index
	 */
	private int getSelectedDataRow(int[] selectedIndices, boolean getMaxValue) {
		return this.getSelectedIndex(selectedIndices, getMaxValue) + 1;
	}
	/**
	 * Returns a selected index depending on the direction, a user wants to navigate.
	 *
	 * @param selectedIndices the selected indices
	 * @param getMaxValue the get max value
	 * @return the selected index
	 */
	private int getSelectedIndex(int[] selectedIndices, boolean getMaxValue) {
		
		if (selectedIndices==null || selectedIndices.length==0) return 0;
		if (selectedIndices.length==1) return selectedIndices[0];
		
		List<Integer> selectedIndicesList = new ArrayList<>();
		for (int selectedIndex : selectedIndices) {
			selectedIndicesList.add(selectedIndex);
		}
		
		if (getMaxValue==true) {
			return selectedIndicesList.stream().max(Comparator.comparingInt(Math::abs)).orElseThrow(NoSuchElementException::new);
		}
		return selectedIndicesList.stream().min(Comparator.comparingInt(Math::abs)).orElseThrow(NoSuchElementException::new);
	}
	
	/**
	 * Checks if the description panel is visible.
	 * @return true, if is description panel visible
	 */
	private boolean isDescriptionPanelVisible() {
		return this.jComponentDataViewConfiguration instanceof JSplitPane;
	}
	
}
