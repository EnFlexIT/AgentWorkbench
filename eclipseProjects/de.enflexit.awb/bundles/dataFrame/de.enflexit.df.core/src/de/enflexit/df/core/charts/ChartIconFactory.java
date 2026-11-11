package de.enflexit.df.core.charts;

import javax.swing.Icon;

import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.df.core.BundleHelper;

/**
 * A factory for creating ChartIcon objects.
 */
public class ChartIconFactory {

	/**
	 * Creates a new Bar Chart Icon object.
	 * @return the icon
	 */
	public static Icon createBarChartIcon() {
		return new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartBars16-Light.png"), BundleHelper.getImageIcon("chart/ChartBars16-Dark.png"));
	}
	/**
	 * Creates a new Pie Chart Icon object.
	 * @return the icon
	 */
	public static Icon createPieChartIcon() {
		return new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartPie16-Light.png"), BundleHelper.getImageIcon("chart/ChartPie16-Dark.png"));
	}
	/**
	 * Creates a new Scatter Plot Icon object.
	 * @return the icon
	 */
	public static Icon createScatterPlotIcon() {
		return new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartScatter16-Light.png"), BundleHelper.getImageIcon("chart/ChartScatter16-Dark.png"));
	}
	/**
	 * Creates a new XY Chart Icon object.
	 * @return the icon
	 */
	public static Icon createXYChartIcon() {
		return new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartXY16-Light.png"), BundleHelper.getImageIcon("chart/ChartXY16-Dark.png"));
	}
	/**
	 * Creates a new Time Series Chart Icon object.
	 * @return the icon
	 */
	public static Icon createTimeSeriesChartIcon() {
		return new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartTimeSeries16-Light.png"), BundleHelper.getImageIcon("chart/ChartTimeSeries16-Dark.png"));
	}
}
