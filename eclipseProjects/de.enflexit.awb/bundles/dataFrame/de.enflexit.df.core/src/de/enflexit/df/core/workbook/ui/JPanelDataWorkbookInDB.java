package de.enflexit.df.core.workbook.ui;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

public class JPanelDataWorkbookInDB extends JPanel {

	private static final long serialVersionUID = -3104101192788727464L;
	
	private DataController dataController;
	public DataWorkbook dataWorkbook;
	
	/**
	 * Instantiates a new JPanelDataWorkbookInDB.
	 */
	public JPanelDataWorkbookInDB() {
		this(null, null);
	}
	
	/**
	 * Instantiates a new JPanelDataWorkbookInDB
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 */
	public JPanelDataWorkbookInDB(DataController dataController, DataWorkbook dataWorkbook) {
		this.initialize();
		this.setDataWorkbook(dataWorkbook);
		this.setDataController(dataController);
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
	}
	
	/**
	 * Returns the controller.
	 * @return the controller
	 */
	private DataController getDataController() {
		return this.dataController;
	}
	/**
	 * Sets the data controller.
	 * @param dataController the new data controller
	 */
	public void setDataController(DataController dataController) {
		this.dataController = dataController;
	}
	/**
	 * Informs the data controller about settings changes.
	 */
	protected void informDataWorkbookSettingChanged() {
		if (this.getDataController()!=null) {
			this.getDataController().firePropertyChange(DataController.DC_DATA_WORKBOOK_CONFIGURATION_CHANGED, null, null);	
		}
	}
	
	/**
	 * Returns the data workbook.
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook() {
		return dataWorkbook;
	}
	/**
	 * Sets the data workbook.
	 * @param dataWorkbook the new data workbook
	 */
	public void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
	}
	
}
