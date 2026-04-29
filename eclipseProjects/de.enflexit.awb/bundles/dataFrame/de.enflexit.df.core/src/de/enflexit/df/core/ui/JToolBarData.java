package de.enflexit.df.core.ui;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import de.enflexit.awb.core.ui.AwbMessageDialog;
import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.common.dataSources.ExcelDataSource;
import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.model.DataControllerSelectionModel;
import de.enflexit.df.core.model.treeNode.AbstractDataTreeNodeDataSource;
import de.enflexit.df.core.model.treeNode.DataTreeNodeDataWorkbook;
import de.enflexit.df.core.workbook.DataWorkbook;
import de.enflexit.df.core.workbook.DataWorkbook4DB;
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
	private HashSet<String> propertyListForJButtonEnablement;
	
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
	
	private JButton jButtonSelectedDataWorkbookOpen;
	private JButton jButtonSelectedDataWorkbookClose;
	
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
		this.add(this.getJButtonDataWorkbookDelete());
		
		this.addSeparator();
		this.add(this.getJToggleButtonConfiguration());

		this.addSeparator();
		this.add(this.getJButtonSelectedDataWorkbookOpen());
		this.add(this.getJButtonDataWorkbookSave());
		this.add(this.getJButtonSelectedDataWorkbookClose());
		
		this.addSeparator();
		this.add(this.getJButtonEditDataSources());
		this.add(this.getJButtonDeleteDataSources());
		
		this.enableJButtonEnabledToSelection(null);
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
			jButtonDataWorkbookNew.setIcon(BundleHelper.getImageIcon("wb/Workbook-White-New.png"));
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
			jMenuItemNewDataWorkbookXML.setIcon(BundleHelper.getThemedIcon("wb/Workbook-XML-light.png", "wb/Workbook-XML-dark.png"));
			jMenuItemNewDataWorkbookXML.addActionListener(this);
		}
		return jMenuItemNewDataWorkbookXML;
	}
	private JMenuItem getJMenuItemNewDataWorkbookJSON() {
		if (jMenuItemNewDataWorkbookJSON==null) {
			jMenuItemNewDataWorkbookJSON = new JMenuItem("Create JSON Data Workbook");
			jMenuItemNewDataWorkbookJSON.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemNewDataWorkbookJSON.setIcon(BundleHelper.getThemedIcon("wb/Workbook-JSON-light.png", "wb/Workbook-JSON-dark.png"));
			jMenuItemNewDataWorkbookJSON.addActionListener(this);
		}
		return jMenuItemNewDataWorkbookJSON;
	}
	private JMenuItem getJMenuItemNewDataWorkbookDB() {
		if (jMenuItemNewDataWorkbookDB==null) {
			jMenuItemNewDataWorkbookDB = new JMenuItem("Create Database Data Workbook");
			jMenuItemNewDataWorkbookDB.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemNewDataWorkbookDB.setIcon(BundleHelper.getThemedIcon("wb/Workbook-DB-light.png", "wb/Workbook-DB-dark.png"));
			jMenuItemNewDataWorkbookDB.addActionListener(this);
		}
		return jMenuItemNewDataWorkbookDB;
	}
	
	private JButton getJButtonDataWorkbookOpen() {
		if (jButtonDataWorkbookOpen==null) {
			jButtonDataWorkbookOpen = new JButton();
			jButtonDataWorkbookOpen.setToolTipText("Open a Data Workbook");
			jButtonDataWorkbookOpen.setIcon(BundleHelper.getImageIcon("wb/Workbook-Yellow.png"));
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
			jMenuItemOpenDataWorkbookXML.setIcon(BundleHelper.getThemedIcon("wb/Workbook-XML-light.png", "wb/Workbook-XML-dark.png"));
			jMenuItemOpenDataWorkbookXML.addActionListener(this);
		}
		return jMenuItemOpenDataWorkbookXML;
	}
	private JMenuItem getJMenuItemOpenDataWorkbookJSON() {
		if (jMenuItemOpenDataWorkbookJSON==null) {
			jMenuItemOpenDataWorkbookJSON = new JMenuItem("Open JSON Data Workbook");
			jMenuItemOpenDataWorkbookJSON.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemOpenDataWorkbookJSON.setIcon(BundleHelper.getThemedIcon("wb/Workbook-JSON-light.png", "wb/Workbook-JSON-dark.png"));
			jMenuItemOpenDataWorkbookJSON.addActionListener(this);
		}
		return jMenuItemOpenDataWorkbookJSON;
	}
	private JMenuItem getJMenuItemOpenDataWorkbookDB() {
		if (jMenuItemOpenDataWorkbookDB==null) {
			jMenuItemOpenDataWorkbookDB = new JMenuItem("Open Database Data Workbook");
			jMenuItemOpenDataWorkbookDB.setForeground(AwbThemeColor.RegularText.getColor());
			jMenuItemOpenDataWorkbookDB.setIcon(BundleHelper.getThemedIcon("wb/Workbook-DB-light.png", "wb/Workbook-DB-dark.png"));
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
			jButtonDataWorkbookDelete.setIcon(BundleHelper.getImageIcon("wb/Workbook-Grey-Delete.png"));
			jButtonDataWorkbookDelete.setPreferredSize(new Dimension(26, 26));
			jButtonDataWorkbookDelete.addActionListener(this);
		}
		return jButtonDataWorkbookDelete;
	}
	
	private JButton getJButtonSelectedDataWorkbookOpen() {
		if (jButtonSelectedDataWorkbookOpen==null) {
			jButtonSelectedDataWorkbookOpen = new JButton();
			jButtonSelectedDataWorkbookOpen.setToolTipText("Open the selected Data Workbook");
			jButtonSelectedDataWorkbookOpen.setIcon(BundleHelper.getImageIcon("wb/Workbook-Grey.png"));
			jButtonSelectedDataWorkbookOpen.setPreferredSize(new Dimension(26, 26));
			jButtonSelectedDataWorkbookOpen.addActionListener(this);
		}
		return jButtonSelectedDataWorkbookOpen;
	}
	
	private JButton getJButtonSelectedDataWorkbookClose() {
		if (jButtonSelectedDataWorkbookClose==null) {
			jButtonSelectedDataWorkbookClose = new JButton();
			jButtonSelectedDataWorkbookClose.setToolTipText("Close the currently selected Data Workbook");
			jButtonSelectedDataWorkbookClose.setIcon(BundleHelper.getImageIcon("wb/Workbook-Closed-Grey.png"));
			jButtonSelectedDataWorkbookClose.setPreferredSize(new Dimension(26, 26));
			jButtonSelectedDataWorkbookClose.addActionListener(this);

		}
		return jButtonSelectedDataWorkbookClose;
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
		
		this.enableJButtonEnabledToSelection(evt.getPropertyName());
		
		if (evt.getPropertyName().equals(DataController.DC_ADDED_DATA_SOURCE)==true) {
			this.getJToggleButtonConfiguration().setSelected(true);
			
		} else if (evt.getPropertyName().equals(DataController.DC_DATA_SOURCE_CONFIGURATION_SHOW)==true) {
			boolean isShowConfig = (boolean) evt.getNewValue();
			if (this.getJToggleButtonConfiguration().isSelected()!=isShowConfig) {
				this.getJToggleButtonConfiguration().setSelected(isShowConfig);
			}
		}
	}
	
	/**
	 * Gets the list of properties to which an enablement for the JButtons is to be done.
	 * @return the property list button enablement
	 */
	private HashSet<String> getPropertyListForJButtonEnablement() {
		if (propertyListForJButtonEnablement==null) {
			propertyListForJButtonEnablement = new HashSet<>();
			propertyListForJButtonEnablement.add(DataController.DC_NEW_TREE_PATH_SELECTED);
			propertyListForJButtonEnablement.add(DataController.DC_ADDED_DATA_WORKBOOK);
			propertyListForJButtonEnablement.add(DataController.DC_OPENED_DATA_WORKBOOK);
			propertyListForJButtonEnablement.add(DataController.DC_CLOSED_DATA_WORKBOOK);
			propertyListForJButtonEnablement.add(DataController.DC_REMOVED_DATA_WORKBOOK);
		}
		return propertyListForJButtonEnablement;
	}
	/**
	 * Sets the JButton enabled to the current selection.
	 * @param propertyChanged the actual property that changed
	 */
	private void enableJButtonEnabledToSelection(String propertyChanged) {
		
		if (propertyChanged!=null && this.getPropertyListForJButtonEnablement().contains(propertyChanged)==false) return;
		 
		DataControllerSelectionModel selModel = this.getDataController().getSelectionModel();

		// --- DataWorkbook open / save / close buttons -------------
		DataTreeNodeDataWorkbook dtnoDW = selModel.getSelectedDataTreeNodeDataWorkbook();
		if (dtnoDW==null) {
			this.getJButtonSelectedDataWorkbookOpen().setEnabled(false);
			this.getJButtonDataWorkbookSave().setEnabled(false);
			this.getJButtonSelectedDataWorkbookClose().setEnabled(false);
		} else {
			this.getJButtonSelectedDataWorkbookOpen().setEnabled(dtnoDW.isDataSourcesLoaded()==false);
			this.getJButtonDataWorkbookSave().setEnabled(dtnoDW.isDataSourcesLoaded()==true);
			this.getJButtonSelectedDataWorkbookClose().setEnabled(dtnoDW.isDataSourcesLoaded()==true);
		}
		
		// --- Edit or delete data sources --------------------------
		this.getJButtonEditDataSources().setEnabled(dtnoDW!=null && dtnoDW.isDataSourcesLoaded());
		this.getJButtonDeleteDataSources().setEnabled(dtnoDW!=null && dtnoDW.isDataSourcesLoaded());
		
		
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJMenuItemNewDataWorkbookXML()) {
			// --- Create XML DataWorkbook ------------------------------------
			this.addNewDataWorkbook(DataWorkbook4XML.create(this.dataController, this));
		} else if (ae.getSource()==this.getJMenuItemNewDataWorkbookJSON()) {
			// --- Create JSON DataWorkbook -----------------------------------
			this.addNewDataWorkbook(DataWorkbook4JSON.create(this.dataController, this));
		} else if (ae.getSource()==this.getJMenuItemNewDataWorkbookDB()) {
			// --- Create database DataWorkbook -------------------------------
			this.addNewDataWorkbook(DataWorkbook4DB.create(this.dataController, this));
			
			
		} else if (ae.getSource()==this.getJMenuItemOpenDataWorkbookXML()) {
			// --- Open XML DataWorkbook --------------------------------------
			this.dataController.openDataWorkbook(DataWorkbook4XML.loadFromFile(this));
		} else if (ae.getSource()==this.getJMenuItemOpenDataWorkbookJSON()) {
			// --- Open JSON DataWorkbook -------------------------------------
			this.dataController.openDataWorkbook(DataWorkbook4JSON.loadFromFile(this));
		} else if (ae.getSource()==this.getJMenuItemOpenDataWorkbookDB()) {
			// --- Open database DataWorkbook ---------------------------------
			this.dataController.openDataWorkbook(DataWorkbook4DB.loadFromDatabase());

			
		} else if (ae.getSource()==this.getJButtonDataWorkbookSave()) {
			// --- Save current DataWorkbook ----------------------------------
			DataWorkbook dw = this.getDataController().getSelectionModel().getSelectedDataWorkbook();
			if (dw!=null) dw.save();
			
		} else if (ae.getSource()==this.getJButtonDataWorkbookDelete()) {
			// --- Delete selected DataWorkbook: ------------------------------
			DataWorkbook dwSelected = this.getDataController().getSelectionModel().getSelectedDataWorkbook();
			if (dwSelected==null) return;
			
			// --- Prepare user request ---------------------------------------
			Window owner = OwnerDetection.getOwnerWindowForComponent(this);
			String requestMsg = "Delete the current Data Workbook '" + dwSelected.getName() + "' from the view?\n";
			JCheckBox jCheckBoxDeletePermanently = new JCheckBox("Also delete data workbook from disk!");
			Object[] requestMsgCheck = { requestMsg, jCheckBoxDeletePermanently };
			boolean isFileBasedDataWorkbook = dwSelected.getDataWorkbookFile()!=null;
			Object message = isFileBasedDataWorkbook==true ? requestMsgCheck : requestMsg;
			
			// --- Ask the user -----------------------------------------------
			int userAnswer = AwbMessageDialog.showOptionDialog(owner, message, "Delete DataWorkbook?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (userAnswer==JOptionPane.OK_OPTION) {
				boolean isDeletePermanently = isFileBasedDataWorkbook==true && jCheckBoxDeletePermanently.isSelected();
				// --- Remove the DataWorkbook --------------------------------
				this.getDataController().removeDataWorkbook(dwSelected);
				if (isDeletePermanently==true) {
					// --- Delete the DataWorkbook file -----------------------
					dwSelected.getDataWorkbookFile().delete();
				}
			}
			
		} else if (ae.getSource()==this.getJButtonSelectedDataWorkbookOpen()) {
			// --- Open the selected DataWorkbook -----------------------------
			DataWorkbook dwSelected = this.getDataController().getSelectionModel().getSelectedDataWorkbook();
			this.getDataController().openDataWorkbook(dwSelected);
			
		} else if (ae.getSource()==this.getJButtonSelectedDataWorkbookClose()) {
			// --- Close the currently selected DataWorkbook ------------------
			DataWorkbook dwSelected = this.getDataController().getSelectionModel().getSelectedDataWorkbook();
			this.getDataController().closeDataWorkbook(dwSelected);
			
		} else if (ae.getSource()==this.getJMenuItemCsvData()) {
			// --- Add CsvDataSource ------------------------------------------
			this.dataController.addDataSource(this.getDataController().getSelectionModel().getSelectedDataWorkbook(), new CsvDataSource());
			
		} else if (ae.getSource()==this.getJMenuItemExcelFile()) {
			// --- Add ExcelDataSource ----------------------------------------
			this.dataController.addDataSource(this.getDataController().getSelectionModel().getSelectedDataWorkbook(), new ExcelDataSource());
			
		} else if (ae.getSource()==this.getJMenuItemDatabaseData()) {
			// --- Add DatabaseDataSource -------------------------------------
			this.dataController.addDataSource(this.getDataController().getSelectionModel().getSelectedDataWorkbook(), new DatabaseDataSource());
		
		} else if (ae.getSource()==this.getJToggleButtonConfiguration()) {
			// --- Show data source configuration -----------------------------
			boolean isSelected = this.getJToggleButtonConfiguration().isSelected();
			this.dataController.firePropertyChange(DataController.DC_DATA_SOURCE_CONFIGURATION_SHOW, !isSelected, isSelected);
			
		} else if (ae.getSource()==this.getJButtonDeleteDataSources()) {
			// --- Delete currently selected data source ----------------------
			DataWorkbook dw = this.getDataController().getSelectionModel().getSelectedDataWorkbook();
			AbstractDataTreeNodeDataSource<?> dtnoDataSource = this.getDataController().getSelectionModel().getSelectedDataTreeNodeDataSource();
			if (dw!=null && dtnoDataSource!=null) {
				// --- Ask the user to delete the data source -----------------
				this.dataController.removeDataSourceAskUser(OwnerDetection.getOwnerWindowForComponent(this), dw, dtnoDataSource.getDataSource(), dtnoDataSource.getCaption());
			}
			
		} 
		
	}

	/**
	 * Adds the specified new data work book.
	 * @param newDataWorkbook the new data workbook
	 */
	private void addNewDataWorkbook(DataWorkbook newDataWorkbook) {
		this.dataController.addDataWorkbook(newDataWorkbook);
		this.dataController.openDataWorkbook(newDataWorkbook);
	}
	
}
