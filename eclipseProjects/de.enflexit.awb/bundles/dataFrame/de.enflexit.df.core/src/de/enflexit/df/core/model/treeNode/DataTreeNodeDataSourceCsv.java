package de.enflexit.df.core.model.treeNode;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JComponent;

import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.dataSource.JPanelDataSourceConfigurationCsv;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.AddCellToColumnException;
import tech.tablesaw.io.ColumnIndexOutOfBoundsException;
import tech.tablesaw.io.csv.CsvReadOptions;

/**
 * The Class DataTreeNodeDataSourceCsv.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataTreeNodeDataSourceCsv extends AbstractDataTreeNodeDataSource<CsvDataSource> {

	private JPanelDataSourceConfigurationCsv jPanelDataSourceConfigurationCsv;
	
	/**
	 * Instantiates a new data tree node data source csv.
	 *
	 * @param dataController the data controller
	 * @param dataSource the data source
	 */
	public DataTreeNodeDataSourceCsv(DataController dataController, CsvDataSource dataSource) {
		super(dataController, dataSource);
		if (dataSource.getName()==null) {
			dataSource.setName("New CSV data source");
		}
		this.setImageIcon(BundleHelper.getThemedIcon("CsvFileBlack.png", "CsvFileGrey.png"));
		this.setTooltipText("Please, configure the CSV File settings ...");
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
		if (jPanelDataSourceConfigurationCsv==null) {
			jPanelDataSourceConfigurationCsv = new JPanelDataSourceConfigurationCsv(this.getDataController(), this);
		}
		return jPanelDataSourceConfigurationCsv;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeObjectBase#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return this.getDataSource().getCsvFilePath();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.model.DataTreeNodeDataSource#loadData()
	 */
	@Override
	public boolean loadData() {
	
		Table oldTable = this.getTable();
		Table newTable = null;
		
		try {
			CsvDataSource csvDS = this.getDataSource();

			File csvFile = csvDS.getCsvFilePath()!=null ? new File(csvDS.getCsvFilePath()) : null; 
			DateTimeFormatter dtFomatter = DateTimeFormatter.ofPattern(csvDS.getDateTimeFormat());
			
			
			if (csvFile!=null) {
				CsvReadOptions csvOptions = CsvReadOptions.builder(csvFile)
						.separator(csvDS.getColumnSeparator().charAt(0))
						.header(csvDS.isHeadline())
						.dateTimeFormat(dtFomatter)
						.missingValueIndicator(" - ")
						.build();
				
				newTable = Table.read().usingOptions(csvOptions).inRange(0, 50);
				
			}
			this.setTable(newTable);
			this.setErrorMessage(null);			
			return true;
			
		} catch (AddCellToColumnException | ColumnIndexOutOfBoundsException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
			LOGGER.error(ex.getLocalizedMessage());
			this.setErrorMessage(ex.getLocalizedMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			this.setErrorMessage(ex.getLocalizedMessage());
		} finally {
			this.getDataController().firePropertyChange(DataController.DC_DATA_LOADED, oldTable, newTable);
		}
		return false;
	}

}
