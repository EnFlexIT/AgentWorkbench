package de.enflexit.df.core.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.common.dataSources.ExcelDataSource;
import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.DataTreeNodeDataSource;

/**
 * The Class JToolBarData.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param ae the ae
 */
public class JToolBarData extends JToolBar implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 2584749340449450910L;

	private DataController dataController;
	
	private JButton jButtonEditDataSources;
	private JMenuItem jMenuItemCsvData; 
	private JMenuItem jMenuItemExcelFile;
	private JMenuItem jMenuItemDatabaseData;
	
	private JToggleButton jToggleButtonDataSourceConfiguration;
	private JButton jButtonDeleteDataSources;
	
	
	/**
	 * Instantiates a new j tool bar data.
	 * @param dataController the data controller
	 */
	public JToolBarData(DataController dataController) {
		this.setDataController(dataController);
		
		this.add(this.getJButtonEditDataSources());
		this.add(this.getJToggleButtonDataSourceConfiguration());
		this.add(this.getJButtonDeleteDataSources());
	}
	
	public DataController getDataController() {
		return dataController;
	}
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
		if (this.dataController!=null) {
			this.dataController.addPropertyChangeListener(this);
		}
	}
	
	/**
	 * Gets the JButton edit data sources.
	 * @return the JButton to edit data sources
	 */
	private JButton getJButtonEditDataSources() {
		if (jButtonEditDataSources==null) {
			jButtonEditDataSources = new JButton();
			jButtonEditDataSources.setToolTipText("Add Data Sources");
			jButtonEditDataSources.setIcon(BundleHelper.getImageIcon("DataSource-16.png"));
			jButtonEditDataSources.setPreferredSize(new Dimension(26, 26));
			jButtonEditDataSources.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JButton invoker = (JButton) ae.getSource();
					showJPopupForDataSources(invoker);
				}
			});
		}
		return jButtonEditDataSources;
	}
	/**
	 * Show JPop popup or invokes the default action.
	 *
	 * @param eomController the current {@link EomController}
	 * @param action the action
	 * @param invoker the invoker
	 */
	private void showJPopupForDataSources(JButton invoker) {
	
		// --- Create the popup menu to select the desired action ---------  
		JPopupMenu jPopupMenuOpen = this.getJPopupMenuDataSources();
		if (jPopupMenuOpen.getSubElements().length==1) {
			// --- Directly do the action of the single menu item ---------
			JMenuItem menuItem = (JMenuItem) jPopupMenuOpen.getSubElements()[0];
			menuItem.doClick();
		} else {
			// --- Show the pop menu --------------------------------------
			jPopupMenuOpen.show(invoker, 0, invoker.getHeight());
		}
	}
	/**
	 * Returns the JPopupMenu to open a TechnicalSystem, TechnicalSystemGroup or a ScheduleList.
	 * @return the JPopupMenu to open an EOM artifact
	 */
	private JPopupMenu getJPopupMenuDataSources() {
		JPopupMenu jPopupMenuOpen = new JPopupMenu("Data Sources");
		jPopupMenuOpen.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		jPopupMenuOpen.add(this.getJMenuItemCsvData());
		jPopupMenuOpen.add(this.getJMenuItemExcelFile());
		jPopupMenuOpen.add(this.getJMenuItemDatabaseData());
		return jPopupMenuOpen;
	}
	private JMenuItem getJMenuItemCsvData() {
		if (jMenuItemCsvData==null) {
			jMenuItemCsvData = new JMenuItem("Add CSV File");
			jMenuItemCsvData.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemCsvData.setIcon(BundleHelper.getThemedIcon("NewCsvFileBlack.png", "NewCsvFileGrey.png"));
			jMenuItemCsvData.addActionListener(this);
		}
		return jMenuItemCsvData;
	}
	private JMenuItem getJMenuItemExcelFile() {
		if (jMenuItemExcelFile==null) {
			jMenuItemExcelFile = new JMenuItem("Add MS-Excel File");
			jMenuItemExcelFile.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemExcelFile.setIcon(BundleHelper.getThemedIcon("NewExcelLight.png", "NewExcelDark.png"));
			jMenuItemExcelFile.addActionListener(this);
		}
		return jMenuItemExcelFile;
	}
	private JMenuItem getJMenuItemDatabaseData() {
		if (jMenuItemDatabaseData==null) {
			jMenuItemDatabaseData = new JMenuItem("Add Database");
			jMenuItemDatabaseData.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemDatabaseData.setIcon(BundleHelper.getThemedIcon("NewDatabaseBlack.png", "NewDatabaseGrey.png"));
			jMenuItemDatabaseData.addActionListener(this);
		}
		return jMenuItemDatabaseData;
	}
	
	/**
	 * Returns the JToggleButton edit data sources.
	 * @return the JToggleButton to edit data sources
	 */
	private JToggleButton getJToggleButtonDataSourceConfiguration() {
		if (jToggleButtonDataSourceConfiguration==null) {
			jToggleButtonDataSourceConfiguration = new JToggleButton();
			jToggleButtonDataSourceConfiguration.setToolTipText("Data Source Configuration ");
			jToggleButtonDataSourceConfiguration.setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("MBsettings.png")));
			jToggleButtonDataSourceConfiguration.setPreferredSize(new Dimension(26, 26));
			jToggleButtonDataSourceConfiguration.addActionListener(this);
		}
		return jToggleButtonDataSourceConfiguration;
	}
	/**
	 * Returns the JButton edit data sources.
	 * @return the JButton to edit data sources
	 */
	private JButton getJButtonDeleteDataSources() {
		if (jButtonDeleteDataSources==null) {
			jButtonDeleteDataSources = new JButton();
			jButtonDeleteDataSources.setToolTipText("Delete selected Data Sources");
			jButtonDeleteDataSources.setIcon(BundleHelper.getImageIcon("Delete.png"));
			jButtonDeleteDataSources.setPreferredSize(new Dimension(26, 26));
			jButtonDeleteDataSources.addActionListener(this);
		}
		return jButtonDeleteDataSources;
	}
	
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if (evt.getPropertyName().equals(DataController.DC_ADDED_DATA_SOURCE)==true) {
			this.getJToggleButtonDataSourceConfiguration().setSelected(true);
			
		} else if (evt.getPropertyName().equals(DataController.DC_DATA_SOURCE_CONFIGURATION_SHOW)==true) {
			boolean isShowConfig = (boolean) evt.getNewValue();
			if (this.getJToggleButtonDataSourceConfiguration().isSelected()!=isShowConfig) {
				this.getJToggleButtonDataSourceConfiguration().setSelected(isShowConfig);
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJMenuItemCsvData()) {
			// --- Add CsvDataSource ------------------------------------------
			this.dataController.addDataSource(new CsvDataSource());
			
		} else if (ae.getSource()==this.getJMenuItemExcelFile()) {
			// --- Add ExcelDataSource ----------------------------------------
			this.dataController.addDataSource(new ExcelDataSource());
			
		} else if (ae.getSource()==this.getJMenuItemDatabaseData()) {
			// --- Add DatabaseDataSource -------------------------------------
			this.dataController.addDataSource(new DatabaseDataSource());
		
		} else if (ae.getSource()==this.getJToggleButtonDataSourceConfiguration()) {
			// --- Show data source configuration -----------------------------
			boolean isSelected = this.getJToggleButtonDataSourceConfiguration().isSelected();
			this.dataController.firePropertyChange(DataController.DC_DATA_SOURCE_CONFIGURATION_SHOW, !isSelected, isSelected);
			
		} else if (ae.getSource()==this.getJButtonDeleteDataSources()) {
			// --- Delete currently selected data source ----------------------
			DataTreeNodeDataSource<?> dtnoDataSource = this.dataController.getSelectionModel().getSelectedDataTreeNodeDataSource();
			if (dtnoDataSource!=null) {
				// --- Ask the user to delete the data source -----------------
				this.dataController.removeDataSourceAskUser(OwnerDetection.getOwnerWindowForComponent(this), dtnoDataSource.getDataSource(), dtnoDataSource.getCaption());
			}
			
		} 
		
	}


}
