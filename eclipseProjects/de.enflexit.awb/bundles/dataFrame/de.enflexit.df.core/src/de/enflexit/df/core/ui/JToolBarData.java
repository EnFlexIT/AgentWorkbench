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
import de.enflexit.df.core.workbook.DataWorkbook4JSON;
import de.enflexit.df.core.workbook.DataWorkbook4XML;

/**
 * The Class JToolBarData.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param ae the ae
 */
public class JToolBarData extends JToolBar implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 2584749340449450910L;

	private DataController dataController;
	
	private JButton jButtonDataWorkbookNew;
		private JMenuItem jMenuItemNewDataWorkbookXML; 
		private JMenuItem jMenuItemNewDataWorkbookJSON;
		private JMenuItem jMenuItemNewDataWorkbookDB;
		
	private JButton jButtonDataWorkbookOpen;
		private JMenuItem jMenuItemOpenDataWorkbookXML; 
		private JMenuItem jMenuItemOpenDataWorkbookJSON;
		private JMenuItem jMenuItemOpenDataWorkbookDB;

	private JButton jButtonDataWorkbookSave;
	private JButton jButtonDataWorkbookDelete;
	private JButton jButtonDataWorkbookClose;
	
	private JToggleButton jToggleButtonConfiguration;

	private JButton jButtonEditDataSources;
		private JMenuItem jMenuItemCsvData; 
		private JMenuItem jMenuItemExcelFile;
		private JMenuItem jMenuItemDatabaseData;
	
	private JButton jButtonDeleteDataSources;
	
	/**
	 * Instantiates a new j tool bar data.
	 * @param dataController the data controller
	 */
	public JToolBarData(DataController dataController) {
		this.setDataController(dataController);
		
		this.add(this.getJButtonDataWorkbookNew());
		this.add(this.getJButtonDataWorkbookOpen());
		this.add(this.getJButtonDataWorkbookSave());
		this.add(this.getJButtonDataWorkbookDelete());
		this.add(this.getJButtonDataWorkbookClose());
		
		this.addSeparator();
		this.add(this.getJToggleButtonConfiguration());
		
		this.addSeparator();
		this.add(this.getJButtonEditDataSources());
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
	
	private JButton getJButtonDataWorkbookNew() {
		if (jButtonDataWorkbookNew==null) {
			jButtonDataWorkbookNew = new JButton();
			jButtonDataWorkbookNew.setToolTipText("Create new Data Workbook");
			jButtonDataWorkbookNew.setIcon(BundleHelper.getImageIcon("Workbook-White-New.png"));
			jButtonDataWorkbookNew.setPreferredSize(new Dimension(26, 26));
			jButtonDataWorkbookNew.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JButton invoker = (JButton) ae.getSource();
					showJPopupForNewDataWorkbook(invoker);
				}
			});
		}
		return jButtonDataWorkbookNew;
	}
	/**
	 * Show JPop popup or invokes the default action.
	 * @param invoker the invoker
	 */
	private void showJPopupForNewDataWorkbook(JButton invoker) {
	
		// --- Create the popup menu to select the desired action ---------  
		JPopupMenu jPopupMenuOpen = this.getJPopupMenuNewDataWorkbooks();
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
	private JPopupMenu getJPopupMenuNewDataWorkbooks() {
		JPopupMenu jPopupMenuOpen = new JPopupMenu("New Data Workbooks");
		jPopupMenuOpen.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		jPopupMenuOpen.add(this.getJMenuItemNewDataWorkbookXML());
		jPopupMenuOpen.add(this.getJMenuItemNewDataWorkbookJSON());
		jPopupMenuOpen.add(this.getJMenuItemNewDataWorkbookDB());
		return jPopupMenuOpen;
	}
	private JMenuItem getJMenuItemNewDataWorkbookXML() {
		if (jMenuItemNewDataWorkbookXML==null) {
			jMenuItemNewDataWorkbookXML = new JMenuItem("Create XML Data Workbook");
			jMenuItemNewDataWorkbookXML.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemNewDataWorkbookXML.setIcon(BundleHelper.getThemedIcon("NewCsvFileBlack.png", "NewCsvFileGrey.png"));
			jMenuItemNewDataWorkbookXML.addActionListener(this);
		}
		return jMenuItemNewDataWorkbookXML;
	}
	private JMenuItem getJMenuItemNewDataWorkbookJSON() {
		if (jMenuItemNewDataWorkbookJSON==null) {
			jMenuItemNewDataWorkbookJSON = new JMenuItem("Create JSON Data Workbook");
			jMenuItemNewDataWorkbookJSON.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemNewDataWorkbookJSON.setIcon(BundleHelper.getThemedIcon("NewExcelLight.png", "NewExcelDark.png"));
			jMenuItemNewDataWorkbookJSON.addActionListener(this);
		}
		return jMenuItemNewDataWorkbookJSON;
	}
	private JMenuItem getJMenuItemNewDataWorkbookDB() {
		if (jMenuItemNewDataWorkbookDB==null) {
			jMenuItemNewDataWorkbookDB = new JMenuItem("Create Database Data Workbook");
			jMenuItemNewDataWorkbookDB.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemNewDataWorkbookDB.setIcon(BundleHelper.getThemedIcon("NewDatabaseBlack.png", "NewDatabaseGrey.png"));
			jMenuItemNewDataWorkbookDB.addActionListener(this);
		}
		return jMenuItemNewDataWorkbookDB;
	}
	
	private JButton getJButtonDataWorkbookOpen() {
		if (jButtonDataWorkbookOpen==null) {
			jButtonDataWorkbookOpen = new JButton();
			jButtonDataWorkbookOpen.setToolTipText("Open a Data Workbook");
			jButtonDataWorkbookOpen.setIcon(BundleHelper.getImageIcon("Workbook-Yellow.png"));
			jButtonDataWorkbookOpen.setPreferredSize(new Dimension(26, 26));
			jButtonDataWorkbookOpen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JButton invoker = (JButton) ae.getSource();
					showJPopupForOpenDataWorkbook(invoker);
				}
			});
		}
		return jButtonDataWorkbookOpen;
	}
	/**
	 * Show JPop popup or invokes the default action.
	 * @param invoker the invoker
	 */
	private void showJPopupForOpenDataWorkbook(JButton invoker) {
	
		// --- Create the popup menu to select the desired action ---------  
		JPopupMenu jPopupMenuOpen = this.getJPopupMenuOpenDataWorkbooks();
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
	private JPopupMenu getJPopupMenuOpenDataWorkbooks() {
		JPopupMenu jPopupMenuOpen = new JPopupMenu("Open Data Workbooks");
		jPopupMenuOpen.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		jPopupMenuOpen.add(this.getJMenuItemOpenDataWorkbookXML());
		jPopupMenuOpen.add(this.getJMenuItemOpenDataWorkbookJSON());
		jPopupMenuOpen.add(this.getJMenuItemOpenDataWorkbookDB());
		return jPopupMenuOpen;
	}

	private JMenuItem getJMenuItemOpenDataWorkbookXML() {
		if (jMenuItemOpenDataWorkbookXML==null) {
			jMenuItemOpenDataWorkbookXML = new JMenuItem("Open XML Data Workbook");
			jMenuItemOpenDataWorkbookXML.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemOpenDataWorkbookXML.setIcon(BundleHelper.getThemedIcon("NewCsvFileBlack.png", "NewCsvFileGrey.png"));
			jMenuItemOpenDataWorkbookXML.addActionListener(this);
		}
		return jMenuItemOpenDataWorkbookXML;
	}
	private JMenuItem getJMenuItemOpenDataWorkbookJSON() {
		if (jMenuItemOpenDataWorkbookJSON==null) {
			jMenuItemOpenDataWorkbookJSON = new JMenuItem("Open JSON Data Workbook");
			jMenuItemOpenDataWorkbookJSON.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemOpenDataWorkbookJSON.setIcon(BundleHelper.getThemedIcon("NewExcelLight.png", "NewExcelDark.png"));
			jMenuItemOpenDataWorkbookJSON.addActionListener(this);
		}
		return jMenuItemOpenDataWorkbookJSON;
	}
	private JMenuItem getJMenuItemOpenDataWorkbookDB() {
		if (jMenuItemOpenDataWorkbookDB==null) {
			jMenuItemOpenDataWorkbookDB = new JMenuItem("Open Database Data Workbook");
			jMenuItemOpenDataWorkbookDB.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemOpenDataWorkbookDB.setIcon(BundleHelper.getThemedIcon("NewDatabaseBlack.png", "NewDatabaseGrey.png"));
			jMenuItemOpenDataWorkbookDB.addActionListener(this);
		}
		return jMenuItemOpenDataWorkbookDB;
	}
	
	private JButton getJButtonDataWorkbookSave() {
		if (jButtonDataWorkbookSave==null) {
			jButtonDataWorkbookSave = new JButton();
			jButtonDataWorkbookSave.setToolTipText("Save the current Data Workbook");
			jButtonDataWorkbookSave.setIcon(BundleHelper.getImageIcon("MBsave.png"));
			jButtonDataWorkbookSave.setPreferredSize(new Dimension(26, 26));
			jButtonDataWorkbookSave.addActionListener(this);
		}
		return jButtonDataWorkbookSave;
	}
	private JButton getJButtonDataWorkbookDelete() {
		if (jButtonDataWorkbookDelete==null) {
			jButtonDataWorkbookDelete = new JButton();
			jButtonDataWorkbookDelete.setToolTipText("Delete the current Data Workbook");
			jButtonDataWorkbookDelete.setIcon(BundleHelper.getImageIcon("Workbook-Grey-Delete.png"));
			jButtonDataWorkbookDelete.setPreferredSize(new Dimension(26, 26));
			jButtonDataWorkbookDelete.addActionListener(this);
		}
		return jButtonDataWorkbookDelete;
	}
	private JButton getJButtonDataWorkbookClose() {
		if (jButtonDataWorkbookClose==null) {
			jButtonDataWorkbookClose = new JButton();
			jButtonDataWorkbookClose.setToolTipText("Close the current Data Workbook");
			jButtonDataWorkbookClose.setIcon(BundleHelper.getImageIcon("Workbook-Closed-Grey.png"));
			jButtonDataWorkbookClose.setPreferredSize(new Dimension(26, 26));
			jButtonDataWorkbookClose.addActionListener(this);

		}
		return jButtonDataWorkbookClose;
	}
	
	/**
	 * Returns the JToggleButton edit data sources.
	 * @return the JToggleButton to edit data sources
	 */
	private JToggleButton getJToggleButtonConfiguration() {
		if (jToggleButtonConfiguration==null) {
			jToggleButtonConfiguration = new JToggleButton();
			jToggleButtonConfiguration.setToolTipText("Configuration Options");
			jToggleButtonConfiguration.setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("MBsettings.png")));
			jToggleButtonConfiguration.setPreferredSize(new Dimension(26, 26));
			jToggleButtonConfiguration.addActionListener(this);
		}
		return jToggleButtonConfiguration;
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
			this.getJToggleButtonConfiguration().setSelected(true);
			
		} else if (evt.getPropertyName().equals(DataController.DC_DATA_SOURCE_CONFIGURATION_SHOW)==true) {
			boolean isShowConfig = (boolean) evt.getNewValue();
			if (this.getJToggleButtonConfiguration().isSelected()!=isShowConfig) {
				this.getJToggleButtonConfiguration().setSelected(isShowConfig);
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJMenuItemNewDataWorkbookXML()) {
			this.dataController.addDataWorkbook(DataWorkbook4XML.create(this));
			
		} else if (ae.getSource()==this.getJMenuItemNewDataWorkbookJSON()) {
			this.dataController.addDataWorkbook(DataWorkbook4JSON.create(this));
			
		} else if (ae.getSource()==this.getJMenuItemNewDataWorkbookDB()) {
			
			
		} else if (ae.getSource()==this.getJMenuItemOpenDataWorkbookXML()) {
			this.dataController.addDataWorkbook(DataWorkbook4XML.loadFromFile(this));
			
		} else if (ae.getSource()==this.getJMenuItemOpenDataWorkbookJSON()) {
			this.dataController.addDataWorkbook(DataWorkbook4JSON.loadFromFile(this));
			
		} else if (ae.getSource()==this.getJMenuItemOpenDataWorkbookDB()) {


		} else if (ae.getSource()==this.getJButtonDataWorkbookSave()) {
			
			
		} else if (ae.getSource()==this.getJButtonDataWorkbookDelete()) {
			
			
		} else if (ae.getSource()==this.getJButtonDataWorkbookClose()) {
			
			
		} else if (ae.getSource()==this.getJMenuItemCsvData()) {
			// --- Add CsvDataSource ------------------------------------------
			this.dataController.addDataSource(new CsvDataSource());
			
		} else if (ae.getSource()==this.getJMenuItemExcelFile()) {
			// --- Add ExcelDataSource ----------------------------------------
			this.dataController.addDataSource(new ExcelDataSource());
			
		} else if (ae.getSource()==this.getJMenuItemDatabaseData()) {
			// --- Add DatabaseDataSource -------------------------------------
			this.dataController.addDataSource(new DatabaseDataSource());
		
		} else if (ae.getSource()==this.getJToggleButtonConfiguration()) {
			// --- Show data source configuration -----------------------------
			boolean isSelected = this.getJToggleButtonConfiguration().isSelected();
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
