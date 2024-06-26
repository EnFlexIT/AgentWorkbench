package de.enflexit.expression;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.stream.Collectors;

import de.enflexit.expression.ExpressionData.DataColumn;


/**
 * This class contains static helper methods for handling expression data objects.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionDataHelper {
	
	/**
	 * Writes the content of an {@link ExpressionData} instance to the specified CSV file.
	 * @param expressionData the expression data
	 * @param csvFile the csv file
	 */
	public static void writeExpressionDataToCSV(ExpressionData expressionData, File csvFile) {
		
		int numRows = getMaxColumnLength(expressionData);
		
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter(csvFile));
			
			// --- Create and write the header row ------------------
			Vector<String> headers = new Vector<>();
			for(DataColumn dataColumn : expressionData.getDataColumnList()) {
				headers.add(dataColumn.getColName());
			}
			String headerString = headers.stream().collect(Collectors.joining(";"));
			printWriter.println(headerString);

			// --- Create and write the data rows -------------------
			for (int i=0; i<numRows; i++) {
				Vector<String> rowData = new Vector<>();
				for(DataColumn dataColumn : expressionData.getDataColumnList()) {
					rowData.add(getValueStringFromDataColumn(dataColumn, i));
				}
				String rowDataString = rowData.stream().collect(Collectors.joining(";"));
				printWriter.println(rowDataString);
			}
			
		} catch (IOException e) {
			System.err.println("[" + ExpressionDataHelper.class.getSimpleName() + "] Error writing CSV data to file " + csvFile.getName());
			e.printStackTrace();
		} finally {
			if(printWriter!=null) {
				printWriter.close();
			}
		}
		
	}
	
	// --------------------------------------------------------------
	// --- From here, internal helper methods -----------------------
	
	/**
	 * Gets the length of the longest {@link DataColumn} in an {@link ExpressionData} instance
	 * @param expressionData the expression data
	 * @return the max column length
	 */
	private static int getMaxColumnLength(ExpressionData expressionData) {
		int maxLength = 0;
		for(DataColumn dataColumn : expressionData.getDataColumnList()) {
			
			int numRows = getDataColumnLength(dataColumn);
			
			if (numRows>maxLength) {
				maxLength = numRows;
			}
		}
		return maxLength;
	}
	
	/**
	 * Gets the length of the provided {@link DataColumn}.
	 * @param dataColumn the data column
	 * @return the data column length
	 */
	private static int getDataColumnLength(DataColumn dataColumn) {
		if (dataColumn.isList()==true) {
			return getListDataLength(dataColumn);
		} else if (dataColumn.isArray()==true) {
			return getArrayDataLength(dataColumn);
		} else {
			// --- Single data value ---------------------- 
			return 1;
		}
	}
	
	/**
	 * Gets length of a {@link DataColumn} containing list data.
	 * @param dataColumn the data column
	 * @return the list data length
	 */
	private static int getListDataLength(DataColumn dataColumn) {
		if (dataColumn.isList()==false) {
			throw new IllegalArgumentException("The provided data column does not contain a list!");
		} else {
			int numRows = 0;
			switch (dataColumn.getDataType()) {
			case Boolean:
				numRows = dataColumn.getBooleanList().size();
				break;
			case Double:
				numRows = dataColumn.getDoubleList().size();
				break;
			case Integer:
				numRows = dataColumn.getIntegerList().size();
				break;
			case Long:
				numRows = dataColumn.getLongList().size();
				break;
			}
			return numRows;
		}
	}
	
	/**
	 * Gets length of a {@link DataColumn} containing array data.
	 * @param dataColumn the data column
	 * @return the array data length
	 */
	private static int getArrayDataLength(DataColumn dataColumn) {
		if (dataColumn.isArray()==false) {
			throw new IllegalArgumentException("The provided data column does not contain an array!");
		} else {
			int numRows = 0;
			switch (dataColumn.getDataType()) {
			case Boolean:
				numRows = dataColumn.getBooleanArray().length;
				break;
			case Double:
				numRows = dataColumn.getDoubleArray().length;
				break;
			case Integer:
				numRows = dataColumn.getIntegerArray().length;
				break;
			case Long:
				numRows = dataColumn.getLongArray().length;
				break;
			}
			return numRows;
		}
	}
	
	private static String getValueStringFromDataColumn(DataColumn dataColumn, int valueIndex) {
		if (dataColumn.isList()==true) {
			return getValueStringFromListDataColumn(dataColumn, valueIndex);
		} else if (dataColumn.isArray()==true) {
			return getValueStringFromArrayDataColumn(dataColumn, valueIndex);
		} else {
			return getValueStringFromSingleValueDataColumn(dataColumn, valueIndex);
		}
	}
	
	private static String getValueStringFromListDataColumn(DataColumn dataColumn, int valueIndex) {
		if (dataColumn.isList()==false) {
			throw new IllegalArgumentException("The provided data column does not contain a list!");
		} else {
			String valueString = null;
			if (valueIndex<getDataColumnLength(dataColumn)) {
				switch (dataColumn.getDataType()) {
				case Boolean:
					boolean booleanValue = dataColumn.getBooleanList().get(valueIndex);
					valueString = String.valueOf(booleanValue);
					break;
				case Double:
					double doubleValue = dataColumn.getDoubleList().get(valueIndex);
					valueString = String.valueOf(doubleValue);
					break;
				case Integer:
					int intValue = dataColumn.getIntegerList().get(valueIndex);
					valueString = String.valueOf(intValue);
					break;
				case Long:
					long longValue = dataColumn.getLongList().get(valueIndex);
					valueString = String.valueOf(longValue);
					break;
				}
			}
			return valueString;
		}
	}
	
	private static String getValueStringFromArrayDataColumn(DataColumn dataColumn, int valueIndex) {
		if (dataColumn.isArray()==false) {
			throw new IllegalArgumentException("The provided data column does not contain an array!");
		} else {
			String valueString = null;
			if (valueIndex<getDataColumnLength(dataColumn)) {
				switch (dataColumn.getDataType()) {
				case Boolean:
					boolean booleanValue = dataColumn.getBooleanArray()[valueIndex];
					valueString = String.valueOf(booleanValue);
					break;
				case Double:
					double doubleValue = dataColumn.getDoubleArray()[valueIndex];
					valueString = String.valueOf(doubleValue);
					break;
				case Integer:
					int intValue = dataColumn.getIntegerArray()[valueIndex];
					valueString = String.valueOf(intValue);
					break;
				case Long:
					long longValue = dataColumn.getLongArray()[valueIndex];
					valueString = String.valueOf(longValue);
					break;
				}
			}
			return valueString;
		}
	}
	
	private static String getValueStringFromSingleValueDataColumn(DataColumn dataColumn, int valueIndex) {
		if (dataColumn.isList()==true || dataColumn.isArray()==true) {
			throw new IllegalArgumentException("The provided data column does not contain a aingle value!");
		} else {
			String valueString = null;
			if (valueIndex==0) {
				switch (dataColumn.getDataType()) {
				case Boolean:
					boolean booleanValue = dataColumn.getBooleanValue();
					valueString = String.valueOf(booleanValue);
					break;
				case Double:
					double doubleValue = dataColumn.getDoubleValue();
					valueString = String.valueOf(doubleValue);
					break;
				case Integer:
					int intValue = dataColumn.getIntegerValue();
					valueString = String.valueOf(intValue);
					break;
				case Long:
					long longValue = dataColumn.getLongValue();
					valueString = String.valueOf(longValue);
					break;
				}
			}
			return valueString;
		}
	}
	
}
