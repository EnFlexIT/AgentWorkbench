package de.enflexit.df.core.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import de.enflexit.common.dataSources.CsvDataSource;
import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.io.AddCellToColumnException;
import tech.tablesaw.io.ColumnIndexOutOfBoundsException;
import tech.tablesaw.io.csv.CsvReadOptions;

/**
 * The Class PaginationDataLoader4CSV.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PaginationDataLoader4CSV extends PaginationDataLoader<CsvDataSource> {

	private BufferedReader butBufferedReader;
	
	private Vector<Vector<String>> pageOne; 
	private Vector<String> headLine;
	private List<ColumnType> columnTypes;

	private Vector<Vector<String>> pageDataVector;
	private List<Column<?>> pageDataColumns;
	
	private DateTimeFormatter dtFormatter;
	
	
	/**
	 * Instantiates a new pagination data loader 4 CSV.
	 * @param dataSource the data source
	 */
	public PaginationDataLoader4CSV(CsvDataSource dataSource) {
		super(dataSource);
	}

	/**
	 * Load the complete file, means the complete table at once.
	 * @return the table
	 */
	private Table loadCompleteFile() {
		
		Table table = null;
		try {
			
			CsvDataSource csvDS = this.getDataSource();
			File csvFile = csvDS.getCsvFilePath()!=null ? new File(csvDS.getCsvFilePath()) : null; 
			if (csvFile!=null && csvFile.exists()==true) {
				CsvReadOptions csvOptions = CsvReadOptions.builder(csvFile)
						.separator(csvDS.getColumnSeparator().charAt(0))
						.header(csvDS.isHeadline())
						.dateTimeFormat(this.getDateTimeFormatter())
						.missingValueIndicator("-")
						.build();
				
				table = Table.read().usingOptions(csvOptions);
			}
			this.setErrorMessage(null);			
			return table;
			
		} catch (AddCellToColumnException | ColumnIndexOutOfBoundsException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
			this.setErrorMessage(ex.getLocalizedMessage());
		}
		return table;
	}
	
	
	/**
	 * Returns the buffered file reader.
	 * @return the but buffered reader
	 */
	private BufferedReader getButBufferedReader() {
		if (butBufferedReader==null) {
			File csvFile = new File(this.getDataSource().getCsvFilePath());
			if (csvFile.exists()==true) {
				try {
					InputStream is = new FileInputStream(csvFile);
					butBufferedReader = new BufferedReader(new InputStreamReader(is));
					
				} catch (FileNotFoundException fnfEx) {
					fnfEx.printStackTrace();
				}
			}
		}
		return butBufferedReader;
	}
	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		if (this.butBufferedReader!=null) {
			try {
				this.butBufferedReader.close();
				this.butBufferedReader = null;
				this.pageOne = null;
				this.columnTypes = null;
				this.headLine = null;
				this.dtFormatter = null;
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the table columns.
	 * @return the columns
	 */
	public List<ColumnType> getColumnTypes() {
		if (columnTypes==null) {
			columnTypes = new ArrayList<>();
		}
		return columnTypes;
	}
	/**
	 * Returns the next data rows from the file.
	 *
	 * @param noOfRows the no of rows to read
	 * @return the next file data rows
	 */
	private Vector<Vector<String>> readNextDataRows(int noOfRows) {
		
		try {
			// --- Create new page instances ------------------------
			this.pageDataVector = new Vector<>();
			this.pageDataColumns = this.createColumnList();
			
			// --- Read the lines -----------------------------------
			String line;
			while ((line = this.getButBufferedReader().readLine())!=null) {
				String[] tokens = line.split(this.getDataSource().getColumnSeparator());
				Vector<String> row = new Vector<String>(Arrays.asList(tokens));
				this.pageDataVector.addElement(row);

				// --- Fill columns in parallel? --------------------
				if (this.pageDataColumns!=null) {
					this.addDataRow(this.pageDataColumns, row);
				}
				
				if (this.pageDataVector.size()>=noOfRows) {
					break;
				}
			} // --- end while ---
			
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return this.pageDataVector;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.data.PaginationDataLoader#loadNextPage()
	 */
	@Override
	public Table loadNextPage() {
		
		boolean isDebug = true;
		
		try {
			
			Table newPage = null;
			Vector<Vector<String>> pageData = null;
			
			if (this.isPaginationActivated()==false) {
				// --------------------------------------------------
				// --- Read complete file ---------------------------
				// --------------------------------------------------
				newPage = this.loadCompleteFile();
				this.setPageNumberLoaded(1);
				
			} else {
				// --------------------------------------------------
				// --- Reap data page-wise --------------------------
				// --------------------------------------------------
				// --- Initial read required? -----------------------
				if (this.getColumnTypes().size()==0) {
					// --- Read first page --------------------------
					boolean isHeadLine = this.getDataSource().isHeadline();
					int noOfRows = isHeadLine==true ? (this.getNumberOfRecordsPerPage()+1) : this.getNumberOfRecordsPerPage(); 
					
					pageData = this.readNextDataRows(noOfRows);
					this.setPageNumberLoaded(1);
					
					// --- Proceed first chunk ----------------------
					if (isHeadLine==true) {
						this.headLine = pageData.remove(0);
					}
					this.pageOne = pageData;
					// --- Detect column model ----------------------
					this.detectColumnTypes();
					
					// --- Create tablesaw table --------------------
					newPage = Table.create(this.createAndFillDataColumns(pageData));
					
				} else {
					// --- Read next page ---------------------------
					pageData = this.readNextDataRows(this.getNumberOfRecordsPerPage());
					if (pageData.size()>0) {
						this.setPageNumberLoaded(this.getPageNumberLoaded() + 1);
					}
					
					// --- Create tablesaw table --------------------
					newPage = Table.create(this.pageDataColumns);
				}
				this.setErrorMessage(null);
			}
			return newPage;
			
		} catch (AddCellToColumnException | ColumnIndexOutOfBoundsException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
			this.setErrorMessage(ex.getLocalizedMessage());
			if (isDebug==true) {
				ex.printStackTrace();
			}
		} finally {
			// --- Reset local page data ----------------------------
			this.pageDataVector = null;
			this.pageDataColumns = null;
		}
		return null;
	}
	
	/**
	 * Creates and fills new data columns with the specified page data.
	 *
	 * @param pageData the page data
	 * @return the list with filled data columns
	 */
	private List<Column<?>> createAndFillDataColumns(Vector<Vector<String>> pageData) {
		
		List<Column<?>> dataColums = this.createColumnList();
		if (dataColums==null) return null;
		
		pageData.forEach(row -> this.addDataRow(dataColums, row));
		
		return dataColums;
	}
	
	/**
	 * Adds the specified single data row to the data columns.
	 *
	 * @param dataColumns the data columns
	 * @param row the row
	 */
	private void addDataRow(List<Column<?>> dataColumns, Vector<String> row) {
		
		if (dataColumns==null || row==null) return;
		
		boolean isDebug = false;
		for (int colIdx = 0; colIdx < this.getColumnTypes().size(); colIdx++) {
			
			// --- Define column ------------------------------------
			Column<?> column = dataColumns.get(colIdx);
			ColumnType cType = column.type();
			
			// --- Read 'cell' value ----------------------------
			String valueString = null;
			Object value = null;
			if (colIdx < row.size()) {
				valueString = row.get(colIdx);
			}
			
			if (valueString!=null && valueString.isBlank()==false) {
				// --- Convert type -----------------------------
				if (cType == ColumnType.INTEGER) {
					value = Integer.parseInt(valueString);
				} else if (cType == ColumnType.DOUBLE) {
					value = Double.parseDouble(valueString.replace(",", "."));
				} else if (cType == ColumnType.BOOLEAN) {
					value = Boolean.parseBoolean(valueString);
				} else if (cType == ColumnType.LOCAL_DATE) {
					for (DateTimeFormatter fmt : DATE_FORMATS) {
						try {
							value = LocalDate.parse(valueString, fmt);
							break;
						} catch (DateTimeParseException ignored) { }
					}
				} else if (cType == ColumnType.LOCAL_DATE_TIME) {
					try {
						value = LocalDateTime.parse(valueString, this.getDateTimeFormatter());
					} catch (Exception e) { }
				} else {
					value = valueString;
				}
			}
			
			// --- Append value to Column -----------------------
			try {
				column.appendObj(value);
			} catch (Exception ex) {
				if (isDebug==true) {
					ex.printStackTrace();
				}
			}
			
		} // end for columns
	}
	
	/**
	 * Creates a new column list.
	 * @return the list
	 */
	private List<Column<?>> createColumnList() {
		
		if (this.getColumnTypes().size()==0) return null;
		
		List<Column<?>> dataColums = new ArrayList<>();
		for (int colIdx = 0; colIdx < this.getColumnTypes().size(); colIdx++) {
			
			// --- Define column ------------------------------------
			ColumnType cType = this.getColumnTypes().get(colIdx);
			String columnName = (this.headLine!=null && colIdx < this.headLine.size()  && this.headLine.get(colIdx)!=null) ? this.headLine.get(colIdx) : "Column " + (colIdx+1);
			Column<?> column = this.createColumn(columnName, cType);
			dataColums.add(column);
		}
		return dataColums;
	}
	/**
	 * Creates the specified column.
	 *
	 * @param name the name
	 * @param type the type
	 * @return the column
	 */
	private Column<?> createColumn(String name, ColumnType type) {
		
		if (type == ColumnType.INTEGER) {
			return IntColumn.create(name);
		} else if (type == ColumnType.DOUBLE) {
			return DoubleColumn.create(name);
		} else if (type == ColumnType.BOOLEAN) {
			return BooleanColumn.create(name);
		} else if (type == ColumnType.LOCAL_DATE) {
			return DateColumn.create(name);
		} else if (type == ColumnType.LOCAL_DATE_TIME) {
			return DateTimeColumn.create(name);
		}
		return StringColumn.create(name);
    }
	
	
	/**
	 * Detect column types.
	 */
	private void detectColumnTypes() {
		
		// --- Set start values -------------------------------------
		this.columnTypes = null;
		int colIndex = 0;
		int rowStart = this.getDataSource().isHeadline()==true ? 1 : 0;
		
		while (true) {
			
			Vector<String> sampleColumnData = this.getSampleData(rowStart, colIndex);
			if (sampleColumnData.size()==0) break;
			
			// --- Count number of data types -----------------------
			Map<ColumnType, Integer> votes = new HashMap<>();
			for (String val : sampleColumnData) {
                votes.merge(inferType(val), 1, Integer::sum);
            }
			
			// --- Get the winning column type ----------------------
			ColumnType cType = null;
			if (votes.size()==1) {
				cType = votes.keySet().iterator().next();
			} else {
				cType = ColumnType.STRING;
			}
			//cType = votes.entrySet().stream().filter(e -> e.getKey() != ColumnType.STRING).max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(ColumnType.STRING);
			
			// --- Define the actual column ------------------------- 
			this.getColumnTypes().add(cType);
			
			colIndex++;
		}
	}

	/**
	 * Returns sample data out of page one for the type inference.
	 *
	 * @param rowIdxStart the row start index 
	 * @param colIdx the column index to get sample date from
	 * @return the sample data
	 */
	private Vector<String> getSampleData(int rowIdxStart, int colIdx) {
		
		Vector<String> sampleColumnData = new Vector<>();
		for (int row = rowIdxStart; row < this.pageOne.size(); row++) {

			if (colIdx>=this.pageOne.get(row).size()) continue;

			String cellValue = this.pageOne.get(row).get(colIdx);
			if (cellValue!=null && cellValue.isBlank()==false) {
				sampleColumnData.addElement(cellValue);
			}
		}
		return sampleColumnData;
	}


	/** The Constant DATE_FORMATS. */
 	private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
		        DateTimeFormatter.ofPattern("dd.MM.yyyy"),
		        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
		        DateTimeFormatter.ofPattern("MM/dd/yyyy")
		    );
 	
 	/**
	  * Returns a date time formatter.
	  * @return the date time formatter
	  */
	 private DateTimeFormatter getDateTimeFormatter() {
 		if (this.dtFormatter==null && this.getDataSource().getDateTimeFormat()!=null) {
 			this.dtFormatter = DateTimeFormatter.ofPattern(this.getDataSource().getDateTimeFormat());
 		}
 		return this.dtFormatter;
 	}
	/**
	 * Infer type.
	 *
	 * @param value the value
	 * @return the column type
	 */
	private ColumnType inferType(String value) {
        
		if (value == null || value.isBlank()) return ColumnType.STRING;

        try { Integer.parseInt(value); return ColumnType.INTEGER; }
        catch (NumberFormatException ignored) {}

        try { Double.parseDouble(value.replace(",", ".")); return ColumnType.DOUBLE; }
        catch (NumberFormatException ignored) {}

		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			return ColumnType.BOOLEAN;
		}
        
        try { LocalDateTime.parse(value, this.getDateTimeFormatter()); return ColumnType.LOCAL_DATE_TIME; }
        catch (DateTimeParseException ignored) {}
        
        for (DateTimeFormatter fmt : DATE_FORMATS) {
            try { LocalDate.parse(value, fmt); return ColumnType.LOCAL_DATE; }
            catch (DateTimeParseException ignored) {}
        }

        return ColumnType.STRING;
    }

	
}
