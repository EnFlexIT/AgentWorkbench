package de.enflexit.awb.ws.dynSiteApi.content;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContentChart;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractValuePair;
import de.enflexit.awb.ws.dynSiteApi.gen.model.DataSeries;
import de.enflexit.awb.ws.dynSiteApi.gen.model.PropertyEntry;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentBarChart;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentLineChart;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentPieChart;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentScatterPlot;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText;
import de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTimeSeriesChart;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ValuePairCategory;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ValuePairDateTime;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ValuePairNumeric;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ValueType;

/**
 * A factory for creating DynamicContentExample objects.
 */
public class DynamicContentFactory {
	
	private static DateTimeFormatter dateTimeFormatter;

	/**
	 * Creates a new DynamicContentExample object.
	 *
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param mimeType the mime type
	 * @param text the text
	 * @return a new instance of the type SiteContentText
	 */
	public static SiteContentText createSiteContentText(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, String mimeType, String text) {
		
		SiteContentText scText = new SiteContentText();
		scText.setUniqueContentID(uniqueContentID);
		scText.setUpdatePeriodInSeconds(updatePeriodInSeconds);
		scText.setEditable(isEditable);

		scText.setMimeType("text/plain");
		if (mimeType!=null && mimeType.isBlank()==false) {
			scText.setMimeType(mimeType);
		}
		scText.setText(text);
		
		return scText;
	}

	/**
	 * Creates a new DynamicContentExample object.
	 *
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param mimeType the mime type
	 * @param dataInBase64 the data in base 64
	 * @param width the width of the image
	 * @param height the height of the image
	 * @return a new instance of the type SiteContentImage
	 */
	public static SiteContentImage createSiteContentImage(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, String mimeType, String dataInBase64, Integer width, Integer height) {
		
		SiteContentImage scImage = new SiteContentImage();
		scImage.setUniqueContentID(uniqueContentID);
		scImage.setUpdatePeriodInSeconds(updatePeriodInSeconds);
		scImage.setEditable(isEditable);
		
		scImage.setMimeType(mimeType);
		scImage.setDataInB64(dataInBase64);
		return scImage;
	}
	
	
	/**
	 * Creates a new DynamicContentExample object.
	 *
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param peList the list of PropertyEntry
	 * @return a new instance of the type SiteContentImage
	 */
	public static SiteContentProperties createSiteContentProperties(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, List<PropertyEntry> peList) {
		
		SiteContentProperties scProps = new SiteContentProperties();
		scProps.setUniqueContentID(uniqueContentID);
		scProps.setUpdatePeriodInSeconds(updatePeriodInSeconds);
		scProps.setEditable(isEditable);
		
		scProps.setPropertyEntries(peList);
		return scProps;
	}

	/**
	 * Creates a new DynamicContentExample object.
	 *
	 * @param key the key
	 * @param valueType the value type
	 * @param value the value
	 * @return the property entry
	 */
	public static PropertyEntry createPropertyEntry(String key, ValueType valueType, String value) {
		
		PropertyEntry pe = new PropertyEntry();
		pe.setKey(key);
		pe.setValueType(valueType);
		pe.setValue(value);
		return pe;
	}
	
	// ------------------------------------------
	// --- Chart-related factory methods --------
	// ------------------------------------------
	/**
	 * Creates a new line chart instance.
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param dataSeries the data series
	 * @param title the title
	 * @param showLegend the show legend
	 * @param xAxisLabel the x axis label
	 * @param yAxisLabel the y axis label
	 * @param secondaryYAxisLabel the secondary Y axis label
	 * @return the site content line chart
	 */
	public static SiteContentLineChart createSiteContentLineChart(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, List<DataSeries> dataSeries, String title, boolean showLegend, String xAxisLabel, String yAxisLabel, String secondaryYAxisLabel) {
		SiteContentLineChart lineChart = new SiteContentLineChart();
		fillCommonChartFields(lineChart, uniqueContentID, updatePeriodInSeconds, isEditable, dataSeries, title, showLegend, xAxisLabel, yAxisLabel);
		lineChart.setSecondaryYAxisLabel(secondaryYAxisLabel);
		
		return lineChart;
	}
	
	/**
	 * Creates a new time series chart instance.
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param dataSeries the data series
	 * @param title the title
	 * @param showLegend the show legend
	 * @param xAxisLabel the x axis label
	 * @param yAxisLabel the y axis label
	 * @param secondaryYAxisLabel the secondary Y axis label
	 * @param timeFormat the time format
	 * @return the site content time series chart
	 */
	public static SiteContentTimeSeriesChart createSiteContentTimeSeriesChart(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, List<DataSeries> dataSeries, String title,boolean showLegend,  String xAxisLabel, String yAxisLabel, String secondaryYAxisLabel, String timeFormat) {
		SiteContentTimeSeriesChart timeSeriesChart = new SiteContentTimeSeriesChart();
		fillCommonChartFields(timeSeriesChart, uniqueContentID, updatePeriodInSeconds, isEditable, dataSeries, title, showLegend, xAxisLabel, yAxisLabel);
		timeSeriesChart.setSecondaryYAxisLabel(secondaryYAxisLabel);
		timeSeriesChart.setTimeFormat(timeFormat);
		
		return timeSeriesChart;
	}
	
	/**
	 * Creates a new bar chart instance.
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param dataSeries the data series
	 * @param title the title
	 * @param showLegend the show legend
	 * @param xAxisLabel the x axis label
	 * @param yAxisLabel the y axis label
	 * @param secondaryYAxisLabel the secondary Y axis label
	 * @return the site content bar chart
	 */
	public static SiteContentBarChart createSiteContentBarChart(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, List<DataSeries> dataSeries, String title, boolean showLegend, String xAxisLabel, String yAxisLabel, String secondaryYAxisLabel) {
		SiteContentBarChart barChart = new SiteContentBarChart();
		fillCommonChartFields(barChart, uniqueContentID, updatePeriodInSeconds, isEditable, dataSeries, title, showLegend, xAxisLabel, yAxisLabel);
		return barChart;
	}
	
	/**
	 * Creates a new pie chart instance.
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param dataSeries the data series
	 * @param title the title
	 * @param showLegend the show legend
	 * @return the site content pie chart
	 */
	public static SiteContentPieChart createSiteContentPieChart(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, List<DataSeries> dataSeries, String title, boolean showLegend) {
		SiteContentPieChart pieChart = new SiteContentPieChart();
		fillCommonChartFields(pieChart, uniqueContentID, updatePeriodInSeconds, isEditable, dataSeries, title, showLegend, null, null);
		return pieChart;		
	}
	
	/**
	 * Creates a new scatter plot instance.
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param dataSeries the data series
	 * @param title the title
	 * @param showLegend the show legend
	 * @param xAxisLabel the x axis label
	 * @param yAxisLabel the y axis label
	 * @param secondaryYAxisLabel the secondary Y axis label
	 * @return the site content scatter plot
	 */
	public static SiteContentScatterPlot createSiteContentScatterPlot(int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, List<DataSeries> dataSeries, String title, boolean showLegend, String xAxisLabel, String yAxisLabel, String secondaryYAxisLabel) {
		SiteContentScatterPlot scatterPlot = new SiteContentScatterPlot();
		fillCommonChartFields(scatterPlot, uniqueContentID, updatePeriodInSeconds, isEditable, dataSeries, title, showLegend, xAxisLabel, secondaryYAxisLabel);
		return scatterPlot;
	}
	
	/**
	 * Sets values to those fields that are common for all chart types
	 * @param chart the chart instance
	 * @param uniqueContentID the unique content ID
	 * @param updatePeriodInSeconds the update period in seconds
	 * @param isEditable the is editable
	 * @param dataSeries the data series
	 * @param title the title
	 * @param showLegend the show legend
	 */
	private static void fillCommonChartFields(AbstractSiteContentChart chart, int uniqueContentID, int updatePeriodInSeconds, boolean isEditable, List<DataSeries> dataSeries, String title, boolean showLegend, String xAxisLabel, String yAxisLabel) {
		chart.setUniqueContentID(uniqueContentID);
		chart.setUpdatePeriodInSeconds(updatePeriodInSeconds);
		chart.setEditable(isEditable);
		chart.setDataSeries(dataSeries);
		chart.setTitle(title);
		chart.setShowLegend(showLegend);
		chart.setxAxisLabel(xAxisLabel);
		chart.setyAxisLabel(yAxisLabel);
	}
	
	/**
	 * Creates a new data series instance.
	 * @param label the label
	 * @param valuePairs the value pairs
	 * @return the data series
	 */
	public static DataSeries createDataSeries(String label, List<AbstractValuePair> valuePairs) {
		return createDataSeries(label, valuePairs, false);
	}
	
	/**
	 * Creates a new data series instance.
	 * @param label the label
	 * @param valuePairs the value pairs
	 * @param secondaryAxis specifies if the series should use the secondary y axis
	 * @return the data series
	 */
	public static DataSeries createDataSeries(String label, List<AbstractValuePair> valuePairs, boolean secondaryAxis) {
		DataSeries dataSeries = new DataSeries();
		dataSeries.setLabel(label);
		dataSeries.setEntries(valuePairs);
		dataSeries.setSecondaryYAxis(secondaryAxis);
		return dataSeries;
	}
	
	/**
	 * Creates a list of data series, containing a single data series with the provided value pairs.
	 * @param label the label
	 * @param valuePairs the value pairs
	 * @return the list< data series>
	 */
	public static List<DataSeries> createDataSeriesList (String label, List<AbstractValuePair> valuePairs){
		return createDataSeriesList(label, valuePairs, false);
	}
	
	/**
	 * Creates a list of data series, containing a single data series with the provided value pairs.
	 * @param label the label
	 * @param valuePairs the value pairs
	 * @param secondaryAxis the secondary axis
	 * @return the list< data series>
	 */
	public static List<DataSeries> createDataSeriesList (String label, List<AbstractValuePair> valuePairs, boolean secondaryAxis){
		DataSeries dataSeries = createDataSeries(label, valuePairs, secondaryAxis);
		List<DataSeries> seriesList = new ArrayList<DataSeries>();
		seriesList.add(dataSeries);
		return seriesList;
	}
	
	/**
	 * Creates a new numeric value pair instance.
	 * @param xValue the x value
	 * @param yValue the y value
	 * @return the value pair numeric
	 */
	public static ValuePairNumeric createValuePairNumeric(double xValue, double yValue) {
		ValuePairNumeric valuePair = new ValuePairNumeric();
		valuePair.setxValue(xValue);
		valuePair.setValue(yValue);
		return valuePair;
	}
	
	/**
	 * Creates a date time value pair instance, getting the date from a java timestamp.
	 * @param timestamp the timestamp
	 * @param value the value
	 * @return the value pair date time
	 */
	public static ValuePairDateTime createValuePairDateTime(long timestamp, double value) {
		ValuePairDateTime valuePair = new ValuePairDateTime();
		String isoDateTimeString = getIsoDateTimeFormatter().format(Instant.ofEpochMilli(timestamp));
		valuePair.setIsoDateTime(isoDateTimeString);
		valuePair.setValue(value);
		return valuePair;
	}
	
	/**
	 * Creates a new DynamicContent object.
	 * @param dateTime the date time
	 * @param dateFormat the date format
	 * @param value the value
	 * @return the value pair date time
	 */
	public static ValuePairDateTime createValuePairDateTime(String dateTime, String dateFormat, double value) {
		ValuePairDateTime valuePair = new ValuePairDateTime();
		valuePair.setIsoDateTime(translateToIsoDateTime(dateTime, dateFormat));
		valuePair.setValue(value);
		return valuePair;
	}
	
	/**
	 * Creates a new category value pair instance.
	 * @param category the category
	 * @param value the value
	 * @return the value pair category
	 */
	public static ValuePairCategory createValuePairCategory(String category, double value) {
		ValuePairCategory valuePair = new ValuePairCategory();
		valuePair.setCategory(category);
		valuePair.setValue(value);
		return valuePair;
	}
	
	/**
	 * Translates a formatted date time string to the ISO format.
	 * @param dateTime the date time
	 * @param timeFormat the time format
	 * @return the string
	 */
	private static String translateToIsoDateTime(String dateTime, String timeFormat) {
		DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern(timeFormat).withZone(ZoneId.systemDefault());
		Instant instant = Instant.from(parseFormatter.parse(dateTime));
		return getIsoDateTimeFormatter().format(instant);
	}
	
	/**
	 * Gets a {@link DateTimeFormatter} for the ISO format, with the current system timezone
	 * @return the iso date time formatter
	 */
	private static DateTimeFormatter getIsoDateTimeFormatter() {
		if (dateTimeFormatter==null) {
			dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault());
		}
		return dateTimeFormatter;
	}
	
}
