package de.enflexit.df.core.workbook.ui;

import java.awt.GridBagLayout;

import javax.swing.JPanel;

import de.enflexit.df.core.workbook.DataWorkbook;

public class JPanelDataWorkbookInDB extends JPanel {

	private static final long serialVersionUID = -3104101192788727464L;
	
	public DataWorkbook dataWorkbook;
	
	/**
	 * Instantiates a new j panel data workbook as file.
	 */
	public JPanelDataWorkbookInDB() {
		this(null);
	}
	/**
	 * Instantiates a  JPanelDataWorkbookInFile.
	 * @param dataWorkbook the data workbook
	 */
	public JPanelDataWorkbookInDB(DataWorkbook dataWorkbook) {
		this.initialize();
		this.setDataWorkbook(dataWorkbook);
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
