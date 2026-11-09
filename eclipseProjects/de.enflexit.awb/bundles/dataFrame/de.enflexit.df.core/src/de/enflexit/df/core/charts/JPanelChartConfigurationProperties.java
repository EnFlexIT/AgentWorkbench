package de.enflexit.df.core.charts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.charts.model.AbstractChart;
import de.enflexit.charts.model.BarChart;
import de.enflexit.charts.model.LineChart;
import de.enflexit.charts.model.PieChart;
import de.enflexit.charts.model.ScatterPlot;
import de.enflexit.charts.model.TimeSeriesChart;
import de.enflexit.common.swing.AwbThemeImageIcon;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.PropertiesConfigurationPanel;

/**
 * The Class JPanelChartConfigurationProperties.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelChartConfigurationProperties extends JPanel implements PropertiesConfigurationPanel, ActionListener {

	private static final long serialVersionUID = -890032683256656426L;
	
	private DataController dataController;
	private DefaultMutableTreeNode dataTreeNode;
	
	private JToolBar jToolBarChart;
	private JButton jButtonBarChart;
	private JButton jButtonPieChart;
	private JButton jButtonScatterChart;
	private JButton jButtonXYChart;
	private JButton jButtonTimeSeriesChart;
	private JSplitPane jSplitPaneConfiguration;
	private JPanelChartList jPanelChartList;
	
	
	/**
	 * Instantiates a new j panel chart configuration properties.
	 */
	public JPanelChartConfigurationProperties() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJToolBarChart(), BorderLayout.NORTH);
		add(getJSplitPaneConfiguration(), BorderLayout.CENTER);
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.PropertiesConfigurationPanel#getPropertiesTitle()
	 */
	@Override
	public String getPropertiesTitle() {
		return "Chart Configuration";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.PropertiesConfigurationPanel#providesPropertiesConfiguratoin(de.enflexit.df.core.model.DataController, javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	public boolean providesPropertiesConfiguration(DataController dataController, DefaultMutableTreeNode treeNode) {
		
		//System.out.println("Test, Test ... :-) ");
		
		return true;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.PropertiesConfigurationPanel#getPropertiesConfigurationPanel(de.enflexit.df.core.model.DataController, javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	public JComponent getPropertiesConfigurationPanel(DataController dataController, DefaultMutableTreeNode treeNode) {
		this.dataController = dataController;
		this.dataTreeNode = treeNode;
		return this;
	}
	
	private JToolBar getJToolBarChart() {
		if (jToolBarChart == null) {
			jToolBarChart = new JToolBar();
			jToolBarChart.setFloatable(false);
			jToolBarChart.add(getJButtonBarChart());
			jToolBarChart.add(getJButtonPieChart());
			jToolBarChart.add(getJButtonScatterChart());
			jToolBarChart.add(getJButtonXYChart());
			jToolBarChart.add(getJButtonTimeSeriesChart());
		}
		return jToolBarChart;
	}
	private JButton getJButtonBarChart() {
		if (jButtonBarChart == null) {
			jButtonBarChart = new JButton();
			jButtonBarChart.setToolTipText("Create new Bar Chart");
			jButtonBarChart.setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartBars16-Light.png"), BundleHelper.getImageIcon("chart/ChartBars16-Dark.png")));
			jButtonBarChart.setPreferredSize(new Dimension(26, 26));
			jButtonBarChart.addActionListener(this);
		}
		return jButtonBarChart;
	}
	private JButton getJButtonPieChart() {
		if (jButtonPieChart == null) {
			jButtonPieChart = new JButton();
			jButtonPieChart.setToolTipText("Create new Pie Chart");
			jButtonPieChart.setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartPie16-Light.png"), BundleHelper.getImageIcon("chart/ChartPie16-Dark.png")));
			jButtonPieChart.setPreferredSize(new Dimension(26, 26));
			jButtonPieChart.addActionListener(this);
		}
		return jButtonPieChart;
	}
	private JButton getJButtonScatterChart() {
		if (jButtonScatterChart == null) {
			jButtonScatterChart = new JButton();
			jButtonScatterChart.setToolTipText("Create new Scatter Plot");
			jButtonScatterChart.setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartScatter16-Light.png"), BundleHelper.getImageIcon("chart/ChartScatter16-Dark.png")));
			jButtonScatterChart.setPreferredSize(new Dimension(26, 26));
			jButtonScatterChart.addActionListener(this);
		}
		return jButtonScatterChart;
	}
	private JButton getJButtonXYChart() {
		if (jButtonXYChart == null) {
			jButtonXYChart = new JButton();
			jButtonXYChart.setToolTipText("Create new XY Chart");
			jButtonXYChart.setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartXY16-Light.png"), BundleHelper.getImageIcon("chart/ChartXY16-Dark.png")));
			jButtonXYChart.setPreferredSize(new Dimension(26, 26));
			jButtonXYChart.addActionListener(this);
		}
		return jButtonXYChart;
	}
	private JButton getJButtonTimeSeriesChart() {
		if (jButtonTimeSeriesChart == null) {
			jButtonTimeSeriesChart = new JButton();
			jButtonTimeSeriesChart.setToolTipText("Create new TimeSeries Chart");
			jButtonTimeSeriesChart.setIcon(new AwbThemeImageIcon(BundleHelper.getImageIcon("chart/ChartTimeSeries16-Light.png"), BundleHelper.getImageIcon("chart/ChartTimeSeries16-Dark.png")));
			jButtonTimeSeriesChart.setPreferredSize(new Dimension(26, 26));
			jButtonTimeSeriesChart.addActionListener(this);
		}
		return jButtonTimeSeriesChart;
	}
	
	
	private JSplitPane getJSplitPaneConfiguration() {
		if (jSplitPaneConfiguration == null) {
			jSplitPaneConfiguration = new JSplitPane();
			jSplitPaneConfiguration.setResizeWeight(0.5);
			
			jSplitPaneConfiguration.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneConfiguration.setTopComponent(this.getJPanelChartList());
		}
		return jSplitPaneConfiguration;
	}
	private JPanelChartList getJPanelChartList() {
		if (jPanelChartList == null) {
			jPanelChartList = new JPanelChartList();
		}
		return jPanelChartList;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonBarChart()) {
			this.addNewChart(new BarChart());
		} else if (ae.getSource()==this.getJButtonPieChart()) {
			this.addNewChart(new PieChart());
		} else if (ae.getSource()==this.getJButtonScatterChart()) {
			this.addNewChart(new ScatterPlot());
		} else if (ae.getSource()==this.getJButtonXYChart()) {
			this.addNewChart(new LineChart());			
		} else if (ae.getSource()==this.getJButtonTimeSeriesChart()) {
			this.addNewChart(new TimeSeriesChart());
		}
	}
	
	private void addNewChart(AbstractChart absChart) {
		
		if (absChart==null) return;
		
		absChart.setChartId(null);
		absChart.setTitle("New " + absChart.getClass().getSimpleName());
		absChart.setShowLegend(true);

		// --- Add to the list of charts for the workbook --------
		
	}
	
	
}
