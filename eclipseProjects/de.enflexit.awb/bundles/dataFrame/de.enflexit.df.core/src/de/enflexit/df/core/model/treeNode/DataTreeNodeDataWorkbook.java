package de.enflexit.df.core.model.treeNode;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.ConfigurationPanel;
import de.enflexit.df.core.workbook.DataWorkbook;
import de.enflexit.df.core.workbook.DataWorkbook4DB;
import de.enflexit.df.core.workbook.DataWorkbook4JSON;
import de.enflexit.df.core.workbook.DataWorkbook4XML;
import de.enflexit.df.core.workbook.ui.JPanelDataWorkbookInDB;
import de.enflexit.df.core.workbook.ui.JPanelDataWorkbookInFile;

/**
 * The Class DataTreeNodeDataWorkbook.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataWorkbook extends DataTreeNodeObjectBase implements ConfigurationPanel {

	private DataController dataController;
	private DataWorkbook dataWorkbook;

	private JPanel jPanelConfiguration;
	
	/**
	 * Instantiates a new data tree node data source.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 */
	public DataTreeNodeDataWorkbook(DataController dataController, DataWorkbook dataWorkbook) {
		this.setDataController(dataController);
		this.setDataWorkbook(dataWorkbook);
	}
	
	/**
	 * Gets the data controller.
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
	}
	
	/**
	 * Returns the DataWorkbook.
	 * @return the DataWorkbook
	 */
	public DataWorkbook getDataWorkbook() {
		return dataWorkbook;
	}
	/**
	 * Sets the DataWorkbook.
	 * @param dataWorkbook the new DataWorkbook
	 */
	public void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.DataTreeNodeObjectBase#getCaption()
	 */
	@Override
	public String getCaption() {
		return this.getDataWorkbook().getName();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.treeNode.DataTreeNodeObjectBase#getImageIcon()
	 */
	@Override
	public ImageIcon getImageIcon() {
		if (this.imageIcon==null) {
			if (this.getDataWorkbook() instanceof DataWorkbook4XML) {
				this.imageIcon = BundleHelper.getThemedIcon("wb/Workbook-XML-light.png", "wb/Workbook-XML-dark.png");
			} else if (this.getDataWorkbook() instanceof DataWorkbook4JSON) {
				this.imageIcon = BundleHelper.getThemedIcon("wb/Workbook-JSON-light.png", "wb/Workbook-JSON-dark.png");
			} else if (this.getDataWorkbook() instanceof DataWorkbook4DB) {
				this.imageIcon = BundleHelper.getThemedIcon("wb/Workbook-DB-light.png", "wb/Workbook-DB-dark.png");
			}
		}
		return super.getImageIcon();
	}
	
	/**
	 * Return the a configuration panel for the current DataWorkbook.
	 * @return the JPanel for the configuration
	 */
	public JPanel getJPanelConfiguration() {
		if (jPanelConfiguration==null) {
			if (this.getDataWorkbook() instanceof DataWorkbook4XML || this.getDataWorkbook() instanceof DataWorkbook4JSON) {
				jPanelConfiguration = new JPanelDataWorkbookInFile(this.getDataWorkbook());
			} else if (this.getDataWorkbook() instanceof DataWorkbook4DB) {
				jPanelConfiguration = new JPanelDataWorkbookInDB(this.getDataWorkbook());
			}
		}
		return jPanelConfiguration;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationToolbarComponents()
	 */
	@Override
	public List<JComponent> getConfigurationToolbarComponents() {
		return null;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.ConfigurationPanel#getConfigurationPanel()
	 */
	@Override
	public JComponent getConfigurationPanel() {
		return this.getJPanelConfiguration();
	}
	
}
