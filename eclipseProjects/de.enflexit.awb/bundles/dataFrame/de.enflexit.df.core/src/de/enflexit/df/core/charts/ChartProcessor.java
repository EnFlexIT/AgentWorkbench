package de.enflexit.df.core.charts;

import java.awt.BasicStroke;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.enflexit.charts.model.AbstractChart;
import de.enflexit.charts.model.BarChart;
import de.enflexit.charts.model.LineChart;
import de.enflexit.charts.model.PieChart;
import de.enflexit.charts.model.ScatterPlot;
import de.enflexit.charts.model.TimeSeriesChart;
import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.df.core.extension.DetailViewPanelService.DetailViewTabComponent;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class ChartProcessor.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ChartProcessor {

	private HashMap<DataController, HashMap<DataWorkbook, List<ChartDefinition>>> dataControllerToWorkbookToChartDefinitionHashMap;
	
	private boolean fillWithTestData = true;

	
	/**
	 * Returns the data controller to workbook to chart panel hash map.
	 * @return the data controller to workbook to chart panel hash map
	 */
	private HashMap<DataController, HashMap<DataWorkbook, List<ChartDefinition>>> getDataControllerToWorkbookToChartDefinitionHashMap() {
		if (dataControllerToWorkbookToChartDefinitionHashMap==null) {
			dataControllerToWorkbookToChartDefinitionHashMap = new HashMap<>();
		}
		return dataControllerToWorkbookToChartDefinitionHashMap;
	}
	/**
	 * Returns the chart in workbook hash map.
	 *
	 * @param dataController the data controller
	 * @return the chart in workbook hash map
	 */
	private HashMap<DataWorkbook, List<ChartDefinition>> getChartDefinitionInWorkbookHashMap(DataController dataController) {
		HashMap<DataWorkbook, List<ChartDefinition>> chartsInWorkbookHashMap = this.getDataControllerToWorkbookToChartDefinitionHashMap().get(dataController);
		if (chartsInWorkbookHashMap==null) {
			chartsInWorkbookHashMap = new HashMap<>();
			this.getDataControllerToWorkbookToChartDefinitionHashMap().put(dataController, chartsInWorkbookHashMap);
		}
		return chartsInWorkbookHashMap;
	}
	/**
	 * Returns the chart definition list.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @return the chart panel list
	 */
	public List<ChartDefinition> getChartDefinitionList(DataController dataController, DataWorkbook dataWorkbook) {
		
		HashMap<DataWorkbook, List<ChartDefinition>> chartInWorkbookHashMap = this.getChartDefinitionInWorkbookHashMap(dataController);
		List<ChartDefinition> chartDefinitionList = chartInWorkbookHashMap.get(dataWorkbook);
		if (chartDefinitionList==null) {
			chartDefinitionList = new ArrayList<>();
			chartInWorkbookHashMap.put(dataWorkbook, chartDefinitionList);
		}
		return chartDefinitionList;
	}

	/**
	 * Returns the detail view panels.
	 *
	 * @param dataController the data controller
	 * @param dataWorkbook the data workbook
	 * @return the detail view panels
	 */
	public List<DetailViewTabComponent> getDetailViewPanels(DataController dataController, DataWorkbook dataWorkbook) {
		
		// --- First some maintenance ---------------------
		List<AbstractChart> chartsInWorkbook = dataWorkbook.getChartList();
		List<ChartDefinition> cdList = this.getChartDefinitionList(dataController, dataWorkbook);
		this.synchronizeChartDefinitionList(chartsInWorkbook, cdList);
		
		// --- Prepare return value -----------------------
		List<DetailViewTabComponent> dvCompList = new ArrayList<>();
		for (ChartDefinition cd : cdList) {
			dvCompList.add(new DetailViewTabComponent(cd.getChart().getTitle(), cd.getChartPanel()));
		}
		return dvCompList;
	}
	
	/**
	 * Synchronize chart definition list.
	 *
	 * @param chartsInWorkbook the charts in workbook
	 * @param chartDefinitionList the chart definition list
	 */
	private void synchronizeChartDefinitionList(List<AbstractChart> chartsInWorkbook, List<ChartDefinition> chartDefinitionList) {

		List<ChartDefinition> cdList = new ArrayList<>(chartDefinitionList);
		chartDefinitionList.clear();
		
		for (AbstractChart chart : chartsInWorkbook) {
			ChartDefinition cd = this.findChartDefinition(cdList, chart);
			if (cd==null) {
				cd = new ChartDefinition(chart, new ChartPanel(this.createChart(chart)));
			}
			chartDefinitionList.add(cd);
		}
	}
	/**
	 * Tries to find the ChartDefinition for the specified chart instance.
	 *
	 * @param chartDefinitionList the chart definition list
	 * @param chart the chart
	 * @return the chart definition
	 */
	private ChartDefinition findChartDefinition(List<ChartDefinition> chartDefinitionList, AbstractChart chart) {
		for (ChartDefinition cd : chartDefinitionList) {
			if (cd.getChart()==chart) return cd;
		}
		return null;
	}

	/**
	 * Factory to create a chart.
	 * @return the chart
	 */
	private JFreeChart createChart(AbstractChart chart) {
		
		JFreeChart jfChart = null;
		if (chart instanceof BarChart barChart) {
			jfChart = ChartFactory.createBarChart(chart.getTitle(), chart.getxAxisLabel(), chart.getyAxisLabel(), this.createCategoryDataset(barChart), PlotOrientation.VERTICAL, chart.getShowLegend(), true, false);
			this.configureCategoryPlot(jfChart);
		} else if (chart instanceof PieChart pieChart) {
			jfChart = ChartFactory.createPieChart(chart.getTitle(), this.createPieDataset(pieChart), chart.getShowLegend(), true, false);
			this.configurePiePlot(jfChart);
		} else if (chart instanceof ScatterPlot scatterPlot) {
			jfChart = ChartFactory.createScatterPlot(chart.getTitle(), chart.getxAxisLabel(), chart.getyAxisLabel(), this.createScatterDataset(scatterPlot), PlotOrientation.HORIZONTAL, chart.getShowLegend(), true, false);
			this.configureXYPlot(jfChart);
		} else if (chart instanceof TimeSeriesChart tsChart) {
			jfChart = ChartFactory.createTimeSeriesChart(chart.getTitle(), chart.getxAxisLabel(), chart.getyAxisLabel(), this.createTimeSeriesDataset(tsChart), chart.getShowLegend(), true, false);
			this.configureXYPlot(jfChart);
		} else if (chart instanceof LineChart lineChart) {
			jfChart = ChartFactory.createXYLineChart(chart.getTitle(), chart.getxAxisLabel(), chart.getyAxisLabel(), this.createXYLineDataset(lineChart), PlotOrientation.VERTICAL, chart.getShowLegend(), true, false);
			this.configureXYPlot(jfChart);
		}

		// --- Some base configuration --------------------
		if (jfChart!=null) {
			jfChart.setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
			jfChart.setBorderPaint(AwbThemeColor.Chart_GridLine.getColor());
			
			jfChart.getLegend().setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
			jfChart.getLegend().setItemPaint(AwbThemeColor.Chart_GridLine.getColor());
		}
		return jfChart;
	}
	
	/**
	 * Creates the category dataset.
	 * @return the default category dataset
	 */
	private DefaultCategoryDataset<?, ?> createCategoryDataset(BarChart barChart) {
		
		DefaultCategoryDataset<String, String> catData = new DefaultCategoryDataset<>();
		// --- Fill with test data ? ----------------------
		if (this.fillWithTestData==true) {
			catData.addValue(10, "Limosine", "VW");
			catData.addValue(50, "Kombi", "VW");
			catData.addValue(15, "Limosine", "BMW");
			catData.addValue(35, "Kombi", "BMW");
			catData.addValue(43, "Limosine", "Mercedes");
			catData.addValue(28, "Kombi", "Mercedes");
		}
		return catData;
	}
	/**
	 * Configure category plot.
	 * @param chart the chart
	 */
	private void configureCategoryPlot(JFreeChart chart) {
		
        CategoryPlot<?, ?> catPlot = (CategoryPlot<?, ?>) chart.getPlot();

        catPlot.setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());  
        catPlot.setDomainGridlinePaint(AwbThemeColor.Chart_GridLine.getColor());
        catPlot.setRangeGridlinePaint(AwbThemeColor.Chart_GridLine.getColor());
        catPlot.setDrawingSupplier(ChartHelper.createDrawingSupplier());

        if (catPlot.getRenderer() instanceof BarRenderer barRenderer) {
            barRenderer.setBarPainter(new StandardBarPainter());
            barRenderer.setShadowVisible(false);
        }
        
        ValueAxis rangeAxis = catPlot.getRangeAxis();
		rangeAxis.setLabelPaint(AwbThemeColor.Chart_Text.getColor());
		rangeAxis.setAxisLinePaint(AwbThemeColor.Chart_GridLine.getColor());
		rangeAxis.setTickMarkPaint(AwbThemeColor.Chart_GridLine.getColor());
		rangeAxis.setTickLabelPaint(AwbThemeColor.Chart_Text.getColor());
		
		CategoryAxis domainAxis = catPlot.getDomainAxis();
		domainAxis.setLabelPaint(AwbThemeColor.Chart_Text.getColor());
		domainAxis.setAxisLinePaint(AwbThemeColor.Chart_GridLine.getColor());
		domainAxis.setTickMarkPaint(AwbThemeColor.Chart_GridLine.getColor());
		domainAxis.setTickLabelPaint(AwbThemeColor.Chart_Text.getColor());
	}
	
	
	/**
	 * Creates the pie dataset.
	 *
	 * @param pieChart the pie chart
	 * @return the pie dataset
	 */
	private PieDataset<?> createPieDataset(PieChart pieChart) {
		
		DefaultPieDataset<String> pieData = new DefaultPieDataset<>();
		if (this.fillWithTestData==true) {
			pieData.setValue("VW", 38);
			pieData.setValue("BMW", 22);
			pieData.setValue("Mercedes", 25);
			pieData.setValue("Audi", 15);
		}
		return pieData;
	}
	/**
	 * Configure pie plot.
	 * @param chart the chart
	 */
	private void configurePiePlot(JFreeChart chart) {

	    PiePlot<?> piePlot = (PiePlot<?>) chart.getPlot();

	    // --- Background / Outline -----------------------
	    piePlot.setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
	    piePlot.setOutlineVisible(false);
	    piePlot.setShadowPaint(null);              // no drop shadow under the pie

	    // --- Section colors (same palette as the bar charts) ---
	    piePlot.setDrawingSupplier(ChartHelper.createDrawingSupplier());

	    // --- Separator lines between the sections ---------------
	    piePlot.setSectionOutlinesVisible(true);
	    piePlot.setDefaultSectionOutlinePaint(AwbThemeColor.Chart_Background.getColor());
	    piePlot.setDefaultSectionOutlineStroke(new BasicStroke(1.5f));

	    // --- Labels -------------------------------------
	    piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));  // Name (Prozent)
	    piePlot.setLabelPaint(AwbThemeColor.Chart_Text.getColor());
	    piePlot.setLabelBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
	    piePlot.setLabelOutlinePaint(AwbThemeColor.Chart_GridLine.getColor());
	    piePlot.setLabelShadowPaint(null);
	    piePlot.setLabelBackgroundPaint(null);
	    piePlot.setLabelLinkPaint(AwbThemeColor.Chart_GridLine.getColor());
	    piePlot.setLabelOutlinePaint(null);
	    
	    // --- Legend (located in the chart, not in the plot) ---
	    if (chart.getLegend()!=null) {
	        chart.getLegend().setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
	        chart.getLegend().setItemPaint(AwbThemeColor.Chart_Text.getColor());
	    }
	}
	
	
	/**
	 * Creates the scatter dataset.
	 * @return the XY series collection
	 */
	private XYSeriesCollection<?> createScatterDataset(ScatterPlot scatterPlot) {

		XYSeriesCollection<String> xyData = new XYSeriesCollection<>();
		// --- Fill with test data ? ----------------------
		if (this.fillWithTestData==true) {
			XYSeries<String> vw = new XYSeries<>("VW");     // x = PS, y = Preis (Tsd. €)
			vw.add(110, 28);
			vw.add(150, 35);
			vw.add(190, 44);
			XYSeries<String> bmw = new XYSeries<>("BMW");
			bmw.add(140, 39);
			bmw.add(184, 48);
			bmw.add(250, 62);
			xyData.addSeries(vw);
			xyData.addSeries(bmw);
		}
		return xyData;
	}

	/**
	 * Creates the XY line dataset.
	 * @return the XY series collection
	 */
	private XYSeriesCollection<?> createXYLineDataset(LineChart xyLineChart) {

		XYSeriesCollection<String> xyData = new XYSeriesCollection<>();
		// --- Fill with test data ? ----------------------
		if (this.fillWithTestData==true) {
			XYSeries<String> limo = new XYSeries<>("Limosine");   // x = km/h, y = l/100km
			XYSeries<String> kombi = new XYSeries<>("Kombi");
			for (int speed = 50; speed <= 200; speed += 25) {
				limo.add(speed, 3.5 + speed * speed / 6000.0);
				kombi.add(speed, 4.0 + speed * speed / 5500.0);
			}
			xyData.addSeries(limo);
			xyData.addSeries(kombi);
		}
		return xyData;
	}

	/**
	 * Creates the time series dataset.
	 * @return the time series collection
	 */
	private TimeSeriesCollection<?> createTimeSeriesDataset(TimeSeriesChart timeSeriesChart) {

		TimeSeriesCollection<String> tsData = new TimeSeriesCollection<>();
		// --- Fill with test data ? ----------------------
		if (this.fillWithTestData==true) {
			TimeSeries<String> vw = new TimeSeries<>("VW");       // Zulassungen pro Monat
			TimeSeries<String> bmw = new TimeSeries<>("BMW");
			int[] vwValues  = { 120, 135, 128, 150, 162, 158 };
			int[] bmwValues = {  80,  85,  95,  90, 102, 110 };
			for (int month = 1; month <= 6; month++) {
				vw.add(new Month(month, 2026), vwValues[month-1]);
				bmw.add(new Month(month, 2026), bmwValues[month-1]);
			}
			tsData.addSeries(vw);
			tsData.addSeries(bmw);
		}
		return tsData;
	}
	
	/**
	 * Configure XY plot (scatter, time series and XY line chart).
	 * @param chart the chart
	 */
	private void configureXYPlot(JFreeChart chart) {

		XYPlot<?> xyPlot = (XYPlot<?>) chart.getPlot();

		xyPlot.setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
		xyPlot.setDomainGridlinePaint(AwbThemeColor.Chart_GridLine.getColor());
		xyPlot.setRangeGridlinePaint(AwbThemeColor.Chart_GridLine.getColor());
		xyPlot.setOutlineVisible(false);

		xyPlot.setDrawingSupplier(ChartHelper.createDrawingSupplier());

		// --- Renderer: all three factory methods use an XYLineAndShapeRenderer ---
		if (xyPlot.getRenderer() instanceof XYLineAndShapeRenderer xyRenderer) {
			xyRenderer.setDefaultShapesFilled(true);
			// xyRenderer.setDefaultShapesVisible(true);   // e.g. additional points on the lines
		}

		// --- Axes (domain = x, range = y) ---------------
		ValueAxis domainAxis = xyPlot.getDomainAxis();   // NumberAxis or DateAxis
		domainAxis.setLabelPaint(AwbThemeColor.Chart_Text.getColor());
		domainAxis.setAxisLinePaint(AwbThemeColor.Chart_GridLine.getColor());
		domainAxis.setTickMarkPaint(AwbThemeColor.Chart_GridLine.getColor());
		domainAxis.setTickLabelPaint(AwbThemeColor.Chart_Text.getColor());

		if (domainAxis instanceof DateAxis dateAxis) {
			dateAxis.setDateFormatOverride(new SimpleDateFormat("MM.yyyy"));
		}

		ValueAxis rangeAxis = xyPlot.getRangeAxis();
		rangeAxis.setLabelPaint(AwbThemeColor.Chart_Text.getColor());
		rangeAxis.setAxisLinePaint(AwbThemeColor.Chart_GridLine.getColor());
		rangeAxis.setTickMarkPaint(AwbThemeColor.Chart_GridLine.getColor());
		rangeAxis.setTickLabelPaint(AwbThemeColor.Chart_Text.getColor());

		// --- Legend -------------------------------------
		if (chart.getLegend()!=null) {
			chart.getLegend().setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
			chart.getLegend().setItemPaint(AwbThemeColor.Chart_Text.getColor());
		}
	}
	
	
	/**
	 * The Class ChartDefinition.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class ChartDefinition {
		
		private AbstractChart chart;
		private ChartPanel chartPanel;
		
		/**
		 * Instantiates a new chart definition.
		 *
		 * @param chart the chart
		 * @param chartPanel the chart panel
		 */
		public ChartDefinition(AbstractChart chart, ChartPanel chartPanel) {
			this.setChart(chart);
			this.setChartPanel(chartPanel);
		}

		public AbstractChart getChart() {
			return chart;
		}
		public void setChart(AbstractChart chart) {
			this.chart = chart;
		}

		public ChartPanel getChartPanel() {
			return chartPanel;
		}
		public void setChartPanel(ChartPanel chartPanel) {
			this.chartPanel = chartPanel;
		}
	}



	
}
