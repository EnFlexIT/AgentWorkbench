package de.enflexit.df.core.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.common.swing.KeyAdapter4Numbers;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import de.enflexit.df.core.dataSources.integration.AbstractPaginationDataLoader;
import de.enflexit.df.core.model.AffectedDataObjects;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.TablesawTableModel;
import tech.tablesaw.api.Table;

/**
 * The Class JPanelDataTableView.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelDataTableView extends JPanel implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = -2503356793902058897L;

	public enum DataViewConfiguration {
		No_ColumnDescription,
		ColumnDescription_Top,
		ColumnDescription_Right,
		ColumnDescription_Left,
		ColumnDescription_Bottom
	}
	
	
	private DataController dataController;
	
	private Font baseFont = new Font("Dialog", Font.PLAIN, 12);
	private Dimension textFieldDimension = new Dimension(60, 24);
	
	
	private GridBagConstraints gbc_dataViewConfiguration;
	private JComponent jComponentDataViewConfiguration;
	
	private JScrollPane jScrollPaneData;
	private JTable jTableData;
	private JTableHeaderWithToolTips jTableHeaderWithToolTips;
	private JPanel jPanelColumDescription;
	
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
		
		
	/**
	 * Instantiates a new JPanelDataTableView.
	 * @param dataController the data controller
	 */
	public JPanelDataTableView(DataController dataController) {
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
		
		this.setDataViewConfiguration(DataViewConfiguration.No_ColumnDescription);
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
	 * Sets the data view configuration.
	 * @param dataViewConfig the new data view configuration
	 */
	public void setDataViewConfiguration(DataViewConfiguration dataViewConfig) {
		
		if (dataViewConfig==null) return;
		
		if (this.jComponentDataViewConfiguration!=null) {
			this.remove(this.jComponentDataViewConfiguration);
		}
		
		int dividerLocationAbs = 0;
		double dividerLocation = 0.25;
		double resizeWeight = 0.95;
		
		switch (dataViewConfig) {
		case No_ColumnDescription:
			this.jComponentDataViewConfiguration = this.getJScrollPaneData();
			break;
		case ColumnDescription_Top:
			resizeWeight = (1-resizeWeight);
			dividerLocationAbs = (int) (this.getSize().getHeight() * dividerLocation);
			this.jComponentDataViewConfiguration = this.createJSplitPaneData(JSplitPane.VERTICAL_SPLIT, dividerLocation, resizeWeight, this.getJPanelColumnDescription(), this.getJScrollPaneData());
			break;
		case ColumnDescription_Bottom:
			dividerLocation = (1 - dividerLocation);
			dividerLocationAbs = (int) (this.getSize().getHeight() * dividerLocation);
			this.jComponentDataViewConfiguration = this.createJSplitPaneData(JSplitPane.VERTICAL_SPLIT, dividerLocation, resizeWeight, this.getJScrollPaneData(), this.getJPanelColumnDescription());
			break;
		case ColumnDescription_Left:
			resizeWeight = (1-resizeWeight);
			dividerLocationAbs = (int) (this.getSize().getWidth() * dividerLocation);
			this.jComponentDataViewConfiguration = this.createJSplitPaneData(JSplitPane.HORIZONTAL_SPLIT, dividerLocation, resizeWeight, this.getJPanelColumnDescription(), this.getJScrollPaneData());
			break;
		case ColumnDescription_Right:
			dividerLocation = (1 - dividerLocation);
			dividerLocationAbs = (int) (this.getSize().getWidth() * dividerLocation);
			this.jComponentDataViewConfiguration = this.createJSplitPaneData(JSplitPane.HORIZONTAL_SPLIT, dividerLocation, resizeWeight, this.getJScrollPaneData(), this.getJPanelColumnDescription());
			break;
		}
		
		this.add(this.jComponentDataViewConfiguration, this.getGridBagConstraintsDataViewConfiguration());
		this.validate();
		this.repaint();
		
		if (this.jComponentDataViewConfiguration instanceof JSplitPane jspDV) {
			jspDV.setDividerLocation(dividerLocationAbs);
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
	 * @return the j split pane
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
					JPanelDataTableView.this.setDatasetSelection(lse.getFirstIndex(), lse.getLastIndex(), false);
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
		}
		return jTableHeaderWithToolTips;
	}
	
	private JPanel getJPanelDataless() {
		if (jPanelDataless==null) {
			jPanelDataless = new JPanel();
		}
		return jPanelDataless;
	}
	
	private JPanel getJPanelColumnDescription() {
		if (jPanelColumDescription==null) {
			jPanelColumDescription = new JPanel();
			
		}
		return jPanelColumDescription;
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
			jToolBarDatasetNavigation.add(this.getJToggleButtonOrientationBottom());
			jToolBarDatasetNavigation.add(this.getJToggleButtonOrientationTop());
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
		
		AbstractDataSourceDTNO<?> dtnoDS = this.getSelectedDataTreeNodeDataSource();
		boolean isEnabledToolBar = (dtnoDS!=null && this.getPaginationDataLoader()!=null);
		
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
			jLabelColumnDescription = new JLabel(" Column Description: ");
			jLabelColumnDescription.setFont(this.baseFont.deriveFont(Font.BOLD));
		}
		return jLabelColumnDescription;
	}
	
	private JToggleButton getJToggleButtonOrientationBottom() {
		if (jToggleButtonOrientationBottom== null) {
			jToggleButtonOrientationBottom = new JToggleButton();
			jToggleButtonOrientationBottom.setIcon(BundleHelper.getImageIcon("OrientationBottom.png"));
			jToggleButtonOrientationBottom.setToolTipText("Column Description: Bottom");
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
			jToggleButtonOrientationTop.setToolTipText("Column Description: Top");
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
			jToggleButtonOrientationLeft.setToolTipText("Column Description: Left");
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
			jToggleButtonOrientationRight.setToolTipText("Column Description: Right");
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
			jToggleButtonOrientationClose.setToolTipText("Close Column Description");
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
		
		AbstractDataSourceDTNO<?> dtnoDS = null;
		
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
						JPanelDataTableView.this.setDetailView(dtnoDSFinal);
					}
				});
			}
			break;
			
		case DataController.DC_NEW_TREE_PATH_SELECTED:
			dtnoDS = this.getSelectedDataTreeNodeDataSource();
			this.setDetailView(dtnoDS);
			break;
			
		}
		
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
							JPanelDataTableView.this.setDatasetSelection(dtnoDS.getRowSelected(), 0, true);
							JPanelDataTableView.this.getJTextFieldRowsPerPage().setText(dtnoDS.getPaginationDataLoader().getNumberOfRecordsPerPage() + "");
							JPanelDataTableView.this.getJTextFieldPageLoaded().setText(dtnoDS.getPaginationDataLoader().getPageNumberLoaded() + "");
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
			this.setDataViewConfiguration(DataViewConfiguration.ColumnDescription_Bottom);
		} else if (ae.getSource()==this.getJToggleButtonOrientationTop()) {
			this.setDataViewConfiguration(DataViewConfiguration.ColumnDescription_Top);
		} else if (ae.getSource()==this.getJToggleButtonOrientationLeft()) {
			this.setDataViewConfiguration(DataViewConfiguration.ColumnDescription_Left);
		} else if (ae.getSource()==this.getJToggleButtonOrientationRight()) {
			this.setDataViewConfiguration(DataViewConfiguration.ColumnDescription_Right);
		} else if (ae.getSource()==this.getJToggleButtonOrientationClose()) {
			this.setDataViewConfiguration(DataViewConfiguration.No_ColumnDescription);
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
					if (dtnoDS.getPaginationDataLoader().isPaginationActivated()==true) {
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
	
}
