package de.enflexit.df.core.charts;

import java.awt.Color;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.enflexit.charts.model.DataSeriesWithValues;
import de.enflexit.charts.model.TimeSeriesChart;
import de.enflexit.charts.model.ValueRowDateTime;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.InstantColumn;
import tech.tablesaw.api.NumberColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * This class provides several helper methods to create different chart representations from TableSaw tables.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableSawChartHelper {
	
	public static ZoneId DEFAULT_TIME_ZONE = ZoneId.systemDefault();
	
	public static final Color[] DEFAULT_CHART_COLORS = {
			new Color(91, 155, 213), 
			new Color(237, 125, 49), 
			new Color(255, 192, 0), 
			new Color(165, 165, 165), 
			new Color(68, 114, 196), 
			new Color(112, 173, 71), 
			new Color(37, 94, 145), 
			new Color(158, 72, 14), 
			new Color(99, 99, 99), 
			new Color(153, 115, 0)
		};
	
	private static final float DEFAULT_CHART_LINE_WIDTH = 2f;
	
	/**
	 * Creates a time series chart from provided table, trying to determine the time column automatically.
	 * @param table the table
	 * @return the time series chart
	 */
	public static final TimeSeriesChart createTimeSeriesChartFromTable(Table table) {
		for (Column<?> column : table.columns()) {
			if (isTemporalColumn(column)) {
				return createTimeSeriesChartFromTable(table, column);
			}
		}
		throw new IllegalArgumentException("Table " + table.name() + " contains no temporal column!");
	}
	
	/**
	 * Creates a time series chart object from the provided table saw table, including all data columns.
	 * @param table the table
	 * @param xValueColumn the x value column
	 * @return the time series chart
	 */
	public static final TimeSeriesChart createTimeSeriesChartFromTable(Table table, Column<?> xValueColumn) {
		ArrayList<Column<?>> yValueColumns = new ArrayList<Column<?>>();
		for (Column<?> column : table.columns()) {
			if(column!=xValueColumn) {
				yValueColumns.add(column);
			}
		}
		return createTimeSeriesChartFromTable(table, xValueColumn, yValueColumns);
	}
	
	/**
	 * Creates a time series chart object from the provided table saw table, including the specified data columns.
	 * @param table the table
	 * @param xValueColumn the x value column
	 * @param yValueColumns the y value columns
	 * @return the time series chart
	 */
	public static final TimeSeriesChart createTimeSeriesChartFromTable(Table table, Column<?> xValueColumn, List<Column<?>> yValueColumns) {
		TimeSeriesChart timeSeriesChart = new TimeSeriesChart();
		
		DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
		
		DataSeriesWithValues dataSeries = new DataSeriesWithValues();
		for (int i=0; i<table.rowCount(); i++) {
			
			Instant xValueInstant = valueToInstant(xValueColumn.get(i));
			String isoDateTimeString = isoDateTimeFormatter.format(xValueInstant);
			
			ValueRowDateTime valueRow = new ValueRowDateTime();
			valueRow.setIsoDateTime(isoDateTimeString);
			for (Column<?> column : yValueColumns) {
				if (column instanceof NumericColumn<?> nc) {
					valueRow.addValueItem(nc.getDouble(i));
				}
			}
			
			dataSeries.addEntriesItem(valueRow);
		}
		
		timeSeriesChart.addDataSeriesItem(dataSeries);
		
		//TODO add legend entries
		
		return timeSeriesChart;
	}
	
	
	/**
	 * Creates a JFreeChart series collection from the provided TableSaw table. Trying to find the time column automatically.
	 * @param table the table
	 * @return the XY series collection
	 */
	public static XYSeriesCollection<String> createJFreeChartSeriesCollection(Table table){
		for (Column<?> column : table.columns()) {
			if (isTemporalColumn(column)) {
				return createJFreeChartSeriesCollection(table, column);
			}
		}
		throw new IllegalArgumentException("No temporal column found!");
	}
	
	
	/**
	 * Creates a JFreeChart series collection from the provided TableSaw table, using the specified time column.
	 * @param table the table
	 * @param timeColumnName the time column name
	 * @return the XY series collection
	 */
	public static XYSeriesCollection<String> createJFreeChartSeriesCollection(Table table, String timeColumnName) {
		// --- Get the time column from the table ------------------- 
		if (table.containsColumn(timeColumnName)==false) {
			throw new IllegalArgumentException("Time column " + timeColumnName + " not found!");
		}
		
		return createJFreeChartSeriesCollection(table, table.column(timeColumnName));
	}
	
	/**
	 * Creates a JFreeChart series collection from the provided TableSaw table, using the provided time column.
	 * @param table the table
	 * @param timeColumn the time column
	 * @return the XY series collection
	 */
	public static XYSeriesCollection<String> createJFreeChartSeriesCollection(Table table, Column<?> timeColumn){
		
		XYSeriesCollection<String> dataSet = new XYSeriesCollection<String>();
		
		// --- Check if the time column  is of a valid type ---------
		if (isTemporalColumn(timeColumn) == false) {
			throw new IllegalArgumentException("The specified time column " + timeColumn.name() + " is not of a temporal type");
		}

		
		// --- Iterate over all table columns -----------------------
		for (int c = 0; c < table.columnCount(); c++) {
			
			var column = table.column(c);
			
			// --- Skip the time column -----------------------------
			if (column == timeColumn) {
				continue;
			}

			// --- Skip non-numeric columns -------------------------
			if (!(column instanceof NumberColumn<?, ?> numbers)) {
				continue;
			}

			// --- Create a data series  for this column ------------
			XYSeries<String> series = new XYSeries<>(column.name());

			// --- Create one entry for each row --------------------
			for (int r = 0; r < table.rowCount(); r++) {
				
				// --- Skip rows with no value  ---------------------
				if (timeColumn.isMissing(r) || numbers.isMissing(r)) {
					continue;
				}

				// --- Create the value pair ------------------------
				Instant instant = valueToInstant(timeColumn.get(r));
				double x = instant.toEpochMilli();
				double y = numbers.getDouble(r);

				if (Double.isFinite(y)) {
					series.add(x, y);
				}
			}

			// --- Add the series to the result data set ------------
			if (!series.isEmpty()) {
				dataSet.addSeries(series);
			}
		}
		
		return dataSet;
	}
	
	/**
	 * Checks if the provided column is of a temporal type.
	 * @param column the column
	 * @return true, if is temporal column
	 */
	public static boolean isTemporalColumn(Column<?> column) {
		return (column instanceof InstantColumn || column instanceof DateTimeColumn);
	}
	
	/**
	 * Converts the provided object to an instant, using the default time zone if required.
	 * @param value the value
	 * @return the instant
	 */
	public static Instant valueToInstant(Object value) {
		return toInstant(value, DEFAULT_TIME_ZONE);
	}
	
	/**
	 * Converts the provided object to an instant, using the specified time zone if required.
	 * @param value the value
	 * @param zoneId the zone id
	 * @return the instant
	 */
	public static Instant toInstant(Object value, ZoneId zoneId) {
		if (value instanceof Instant instant) {
			return instant;
		} else if (value instanceof ZonedDateTime zdt) {
			return Instant.from(zdt);
		} else if (value instanceof LocalDateTime ldt) {
			return Instant.from(ldt.atZone(zoneId));
		} else {
			throw new IllegalArgumentException("The value is not of a supported temporal type!");
		}
	}
	
	/**
	 * Gets the chart color for the specified index of a data series.
	 * @param seriesIndex the series index
	 * @return the chart color
	 */
	public static Color getChartColor(int seriesIndex) {
		return DEFAULT_CHART_COLORS[seriesIndex % DEFAULT_CHART_COLORS.length];
	}
	
	/**
	 * Gets the default line width for charts.
	 * @return the default chart line width
	 */
	public static float getDefaultChartLineWidth() {
		return DEFAULT_CHART_LINE_WIDTH;
	}
}
