/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.load.threading.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.Layer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.XYSeriesCollection;


import agentgui.simulationService.load.threading.storage.ThreadInfoStorageMachine;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The Class ThreadInfoStorageScrollPane.
 * 
 * Displays a JPanel with Chart of multiple Series and display/filtering options.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoStorageScrollPane extends JPanel implements ActionListener{
	
	/**
     * The Constant DEFAULT_CHART_COLORS defines
     * a set of standard colors for charts.
     */
//    public static final Color[] DEFAULT_CHART_COLORS = agentgui.core.charts.DataModel.DEFAULT_COLORS;
    public static final Color[] DEFAULT_CHART_COLORS = {
    	new Color(0, 80, 80),
    	new Color(255, 80, 80),
    	new Color(0, 0, 255),
    	new Color(153, 115, 0),
        new Color(237, 125, 49),
        new Color(255, 192, 0),
        new Color(68, 114, 196),
        new Color(112, 173, 71),
        new Color(37, 94, 145),
        new Color(158, 72, 14),
        new Color(99, 99, 99),
        new Color(91, 155, 213),
        new Color(165, 165, 165)
    };
    
	private static final long serialVersionUID = 1L;
	
	/** The plot. */
	private XYPlot plot;
	
	/** The j free chart panel. */
	private ChartPanel jFreeChartPanel;
	
	/** The delta collection. */
	private XYSeriesCollection deltaCollection;
	
	/** The total collection. */
	private XYSeriesCollection totalCollection;
	
	/** The load collection. */
	private XYSeriesCollection loadCollection;
	
	/** The extra object as additional parameter. */
	private Object extraObject;
	
	/** The J panel filter. */
	private JPanel JPanelFilter;
	
	/** The chart. */
	private JFreeChart chart;
	
	/** The file chooser. */
	private JFileChooser fileChooser;
	
	/** The j check box user time. */
	private JCheckBox jCheckBoxUserTime;
	
	/** The j check box system time. */
	private JCheckBox jCheckBoxSystemTime;
	
	/** The j check box delta. */
	private JCheckBox jCheckBoxDelta;
	
	/** The j check box total. */
	private JCheckBox jCheckBoxTotal;
	
	/** The J check box legend. */
	private JCheckBox JCheckBoxLegend;
	
	/** The renderer total. */
	private XYLineAndShapeRenderer rendererTotal;
	
	/** The renderer delta. */
	private XYLineAndShapeRenderer rendererDelta;
	
	/** The renderer load. */
	private XYLineAndShapeRenderer rendererLoad;
	
	/** The axis delta. */
	private NumberAxis axisDelta;
	
	/** The axis total. */
	private NumberAxis axisTotal;
	
	/** The axis load. */
	private NumberAxis axisLoad;
	
	/** The avg cpu load. */
	private ValueMarker avgCPULoad;

	/** Display setting.Set true to show delta. */
	private boolean showDelta;
	/** Display setting.Set true to show total. */
	private boolean showTotal;
	/** Display setting. Set to true to show the system-time */
	private boolean showSystemTime;
	/** Display setting. Set to true to show the user-time */
	private boolean showUserTime;
	/** Display charts in a new window if true */
	private boolean popup;
	/** Display legend if true */
	private boolean showLegend;
	/** The frame title. */
	private String frameTitle;

	/** The pattern. */
	private float[][] pattern;
	
	/** The stroke. */
	private BasicStroke stroke;
	
	/** The lbl curve type. */
	private JLabel lblCurveType;
	
	/** The lbl thread time. */
	private JLabel lblThreadTime;
	
	/** The btn export csv. */
	private JButton btnExportCSV;
	
	/**
	 * Instantiates a new thread info scroll pane.
	 *
	 * @param deltaCollection the delta collection
	 * @param totalCollection the total collection
	 * @param popup the popup
	 * @param frameTitle the frame title
	 */
	public ThreadInfoStorageScrollPane(XYSeriesCollection deltaCollection, XYSeriesCollection totalCollection, XYSeriesCollection loadCollection, boolean popup, String frameTitle, Object extraObject) {		
		super();
		this.showDelta = true;
		this.showTotal = true;
		this.showSystemTime = true;
		this.showUserTime = true;
		this.popup = popup;
		this.showLegend = popup;
		this.frameTitle = frameTitle;
		this.deltaCollection = deltaCollection;
		this.totalCollection = totalCollection;
		this.loadCollection = loadCollection;
		this.extraObject = extraObject;
		initialize();
	}

	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(getJFreeChartPanel());
		this.add(getJPanelFilter(), BorderLayout.SOUTH);
		
		if(popup == true){
			JFrame newWindow = new JFrame(frameTitle);
		    newWindow.setContentPane(this);
		    newWindow.pack();
		    newWindow.setVisible(true);
		}
		
		
		pattern = new float[][]{{10.0f},{10.0f,10.0f},{10.0f,10.0f,2.0f,10.0f},{3.0f,3.0f,3.0f}};
		stroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_MITER, 10.0f, pattern[3], 0.0f);
		String seriesKey;
		Color color = Color.BLACK;
		
		if(deltaCollection != null){
			
			deltaCollection.addChangeListener(new CollectionSeriesChangeListener());
			totalCollection.addChangeListener(new CollectionSeriesChangeListener());
			
			for (int x = 0; x < deltaCollection.getSeries().size(); x++) {
				seriesKey = deltaCollection.getSeries(x).getKey().toString();
				rendererDelta.setSeriesPaint(x, DEFAULT_CHART_COLORS[x]);
				// --- user time series -> dotted line style ---
				if (seriesKey.contains("USER") == true) {
					rendererDelta.setSeriesStroke(x, stroke);
				}
			}
		}

		if (totalCollection != null) {

			for (int x = 0; x < totalCollection.getSeries().size(); x++) {
				seriesKey = totalCollection.getSeries(x).getKey().toString();
				rendererTotal.setSeriesPaint(x, DEFAULT_CHART_COLORS[x]);
				// --- user time series -> dotted line style ---
				if (seriesKey.contains("USER") == true) {
					rendererTotal.setSeriesStroke(x, stroke);
				}
			}
		}

		if (loadCollection != null) {

			color = DEFAULT_CHART_COLORS[2];
			if (popup == true) {
				axisLoad.setLabelPaint(color);
			}

			for (int x = 0; x < loadCollection.getSeries().size(); x++) {
				seriesKey = loadCollection.getSeries(x).getKey().toString();

				rendererLoad.setSeriesPaint(x, color);
			}
		}
	}
	
	/**
	 * The listener interface for receiving collectionSeriesChange events.
	 * The class that is interested in processing a collectionSeriesChange
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addCollectionSeriesChangeListener<code> method. When
	 * the collectionSeriesChange event occurs, that object's appropriate
	 * method is invoked.
	 */
	class CollectionSeriesChangeListener implements DatasetChangeListener{
		private HashMap<String, Paint> localLineColorMap;
		private int seriesCount;
		private int colorIndex;
		
		/**
		 * Instantiates a new collection series change listener.
		 */
		public CollectionSeriesChangeListener(){
			this.localLineColorMap = new HashMap<String, Paint>();
			this.seriesCount = -1;
			this.colorIndex = 0;
		}
		
		/* (non-Javadoc)
		 * @see org.jfree.data.general.DatasetChangeListener#datasetChanged(org.jfree.data.general.DatasetChangeEvent)
		 */
		@Override
		public void datasetChanged(DatasetChangeEvent event) {
			if(extraObject != null){
				if(extraObject.getClass().toString().endsWith("ThreadInfoStorageMachine")){
					ThreadInfoStorageMachine tim = (ThreadInfoStorageMachine)extraObject;
					
					double avgLoadCPU = Math.round(tim.getActualAverageLoadCPU());
					avgCPULoad.setValue(avgLoadCPU);
					avgCPULoad.setLabel("Average CPU: " + avgLoadCPU);
				}
			}
			if(popup == false){
				// -- set one color for a series  ---
				if(deltaCollection != null){
					
					int newSeriesCount = deltaCollection.getSeriesCount();
			        if (newSeriesCount == seriesCount) {
			            // --- nothing added/removed ---
			        } else if (newSeriesCount < seriesCount) {
			            //--- removed ---
			            seriesCount = newSeriesCount;
			        } else {
			            //--- added ---
			            seriesCount = newSeriesCount;
			            
			            for(int x = 0; x+2 <= deltaCollection.getSeries().size(); x=x+2){
							String seriesKey = deltaCollection.getSeries(x).getKey().toString();
							Paint color = Color.BLACK;
							
							if(localLineColorMap.containsKey(seriesKey) == false){
								if(colorIndex >= DEFAULT_CHART_COLORS.length){
									colorIndex = 0; // reset 
								}
								color = DEFAULT_CHART_COLORS[colorIndex++]; 
								localLineColorMap.put(seriesKey, color);
							}else{
								color = localLineColorMap.get(seriesKey);
							}
							rendererDelta.setSeriesPaint(x, color);
							rendererTotal.setSeriesPaint(x, color);
							rendererDelta.setSeriesPaint(x+1, color);
							rendererTotal.setSeriesPaint(x+1, color);
							
							// --- user time series ->  dotted line style ---
							rendererDelta.setSeriesStroke(x+1, stroke);
							rendererTotal.setSeriesStroke(x+1, stroke);
						}
			        }								
				}
			}		
		}
	}

	/**
	 * Gets the j panel filter.
	 * @return the j panel filter
	 */
	private JPanel getJPanelFilter() {
		if (JPanelFilter == null) {
			
			JPanelFilter = new JPanel();
			JPanelFilter.setBorder(null);
			
			// --- Set default value -----------------------
			getJCheckBoxSystemTime().setSelected(showSystemTime);
			getJCheckBoxUserTime().setSelected(showUserTime);
			getJCheckBoxDelta().setSelected(showDelta);
			getJCheckBoxTotal().setSelected(showTotal);
			getJCheckBoxLegend().setSelected(showLegend);
			
			GridBagLayout gbl_JPanelFilter = new GridBagLayout();
			gbl_JPanelFilter.columnWidths = new int[] {30};
			gbl_JPanelFilter.rowHeights = new int[] {15, 15};
			gbl_JPanelFilter.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_JPanelFilter.rowWeights = new double[]{0.0, 0.0};
			JPanelFilter.setLayout(gbl_JPanelFilter);
			GridBagConstraints gbc_lblThreadTime = new GridBagConstraints();
			gbc_lblThreadTime.anchor = GridBagConstraints.EAST;
			gbc_lblThreadTime.insets = new Insets(0, 5, 0, 5);
			gbc_lblThreadTime.gridx = 0;
			gbc_lblThreadTime.gridy = 1;
			JPanelFilter.add(getLblThreadTime(), gbc_lblThreadTime);
			GridBagConstraints gbc_jCheckBoxUserTime = new GridBagConstraints();
			gbc_jCheckBoxUserTime.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxUserTime.insets = new Insets(1, 1, 1, 5);
			gbc_jCheckBoxUserTime.gridx = 1;
			gbc_jCheckBoxUserTime.gridy = 1;
			JPanelFilter.add(getJCheckBoxUserTime(), gbc_jCheckBoxUserTime);
			GridBagConstraints gbc_jCheckBoxDelta = new GridBagConstraints();
			gbc_jCheckBoxDelta.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxDelta.insets = new Insets(1, 1, 5, 5);
			gbc_jCheckBoxDelta.gridx = 1;
			gbc_jCheckBoxDelta.gridy = 0;
			JPanelFilter.add(getJCheckBoxDelta(), gbc_jCheckBoxDelta);
			GridBagConstraints gbc_jCheckBoxSystemTime = new GridBagConstraints();
			gbc_jCheckBoxSystemTime.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxSystemTime.insets = new Insets(1, 1, 1, 5);
			gbc_jCheckBoxSystemTime.gridx = 2;
			gbc_jCheckBoxSystemTime.gridy = 1;
			JPanelFilter.add(getJCheckBoxSystemTime(), gbc_jCheckBoxSystemTime);
			GridBagConstraints gbc_jCheckBoxTotal = new GridBagConstraints();
			gbc_jCheckBoxTotal.anchor = GridBagConstraints.WEST;
			gbc_jCheckBoxTotal.insets = new Insets(1, 1, 5, 5);
			gbc_jCheckBoxTotal.gridx = 2;
			gbc_jCheckBoxTotal.gridy = 0;
			JPanelFilter.add(getJCheckBoxTotal(), gbc_jCheckBoxTotal);
			GridBagConstraints gbc_JCheckBoxLegend = new GridBagConstraints();
			gbc_JCheckBoxLegend.anchor = GridBagConstraints.EAST;
			gbc_JCheckBoxLegend.insets = new Insets(0, 5, 5, 0);
			gbc_JCheckBoxLegend.gridx = 4;
			gbc_JCheckBoxLegend.gridy = 0;
			JPanelFilter.add(getJCheckBoxLegend(), gbc_JCheckBoxLegend);
			GridBagConstraints gbc_lblCurveType = new GridBagConstraints();
			gbc_lblCurveType.anchor = GridBagConstraints.EAST;
			gbc_lblCurveType.insets = new Insets(0, 5, 5, 5);
			gbc_lblCurveType.gridx = 0;
			gbc_lblCurveType.gridy = 0;
			JPanelFilter.add(getLblCurveType(), gbc_lblCurveType);
			GridBagConstraints gbc_btnExportCSV = new GridBagConstraints();
			gbc_btnExportCSV.anchor = GridBagConstraints.EAST;
			gbc_btnExportCSV.insets = new Insets(0, 0, 0, 5);
			gbc_btnExportCSV.gridx = 4;
			gbc_btnExportCSV.gridy = 1;
			JPanelFilter.add(getBtnExportCSV(), gbc_btnExportCSV);
			
		}
		return JPanelFilter;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	
		if (ae.getSource() == getJCheckBoxSystemTime()) {			
			//--- toggle ---
			if(getJCheckBoxSystemTime().isSelected() == false && getJCheckBoxUserTime().isSelected() == false){
				getJCheckBoxUserTime().setSelected(true);
			}
		}else if (ae.getSource() == getJCheckBoxUserTime()) {
			//--- toggle ---
			if(getJCheckBoxUserTime().isSelected() == false && getJCheckBoxSystemTime().isSelected() == false ){
				getJCheckBoxSystemTime().setSelected(true);
			}
		}else if (ae.getSource() == getJCheckBoxDelta()) {
			//--- toggle ---
			if(getJCheckBoxDelta().isSelected() == false && getJCheckBoxTotal().isSelected() == false){
				getJCheckBoxTotal().setSelected(true);
			}
		}else if (ae.getSource() == getJCheckBoxTotal()) {
			//--- toggle ---
			if(getJCheckBoxTotal().isSelected() == false && getJCheckBoxDelta().isSelected() == false){
				getJCheckBoxDelta().setSelected(true);
			}
		}else if (ae.getSource() == getJCheckBoxLegend()){
			//---nothing to do ---
		}else if (ae.getSource() == getBtnExportCSV()){
			
			int userSelection = getFileChooser().showSaveDialog(ThreadInfoStorageScrollPane.this);
			 
			if (userSelection == JFileChooser.APPROVE_OPTION) {
			    File fileToSave = fileChooser.getSelectedFile();
			    System.out.println("Export XYSeries as file: " + fileToSave.getAbsolutePath());
			    exportCsvFile(fileToSave.getAbsolutePath(), deltaCollection, totalCollection, loadCollection);
			    
			}
		}
		
		if(getJCheckBoxLegend().isSelected() == true){
			showLegend = true;
		}else{
			showLegend = false;
		}
		
		if(getJCheckBoxSystemTime().isSelected() == true){
			showSystemTime = true;
		}else{
			showSystemTime = false;
		}
		if(getJCheckBoxUserTime().isSelected() == true){
			showUserTime = true;
		}else{
			showUserTime = false;
		}
		if(getJCheckBoxDelta().isSelected() == true){
			showDelta = true;
		}else{
			showDelta = false;
		}
		if(getJCheckBoxTotal().isSelected() == true){
			showTotal = true;
		}else{
			showTotal = false;
		}
		
		//--- hide or show lines according to checkbox ---
		boolean condition;
		
		for(int x=0;x<totalCollection.getSeries().size(); x++){
			condition = (totalCollection.getSeries(x).getKey().toString().contains("SYSTEM") && showSystemTime  && showTotal) || (totalCollection.getSeries(x).getKey().toString().contains("USER") && showUserTime && showTotal);
			rendererTotal.setSeriesVisible(x, condition);
		}
		for(int x=0;x<deltaCollection.getSeries().size(); x++){
			condition = (deltaCollection.getSeries(x).getKey().toString().contains("SYSTEM") && showSystemTime  && showDelta) || (deltaCollection.getSeries(x).getKey().toString().contains("USER") && showUserTime && showDelta);
			rendererDelta.setSeriesVisible(x, condition);
		}
		//--- hide or show legend ---
		chart.getLegend().setVisible(showLegend);
		}

	/**
	 * Gets the j radio button delta.
	 * @return the j radio button delta
	 */
	private JCheckBox getJCheckBoxDelta() {
		if (jCheckBoxDelta == null) {
			jCheckBoxDelta = new JCheckBox("Delta");
			jCheckBoxDelta.setToolTipText("Show time delta charts");
			jCheckBoxDelta.addActionListener(this);
		}
		return jCheckBoxDelta;
	}
	
	/**
	 * Gets the j radio button total.
	 * @return the j radio button total
	 */
	private JCheckBox getJCheckBoxTotal() {
		if (jCheckBoxTotal == null) {
			jCheckBoxTotal = new JCheckBox("Total");
			jCheckBoxTotal.setToolTipText("Show total time charts");
			jCheckBoxTotal.addActionListener(this);
		}
		return jCheckBoxTotal;
	}
	
	/**
	 * Gets the j radio button for filtering user-time charts.
	 * @return the j radio button filter agents
	 */
	private JCheckBox getJCheckBoxUserTime() {
		if (jCheckBoxUserTime == null) {
			jCheckBoxUserTime = new JCheckBox("User");
			jCheckBoxUserTime.setToolTipText("Show user time charts");
			jCheckBoxUserTime.addActionListener(this);
		}
		return jCheckBoxUserTime;
	}
	
	/**
	 * Gets the j radio button for filtering user-time charts.
	 * @return the j radio button filter agents
	 */
	private JCheckBox getJCheckBoxSystemTime() {
		if (jCheckBoxSystemTime == null) {
			jCheckBoxSystemTime = new JCheckBox("System");
			jCheckBoxSystemTime.setToolTipText("Show  system time charts");
			jCheckBoxSystemTime.addActionListener(this);
		}
		return jCheckBoxSystemTime;
	}
	
	/**
	 * Gets the check box for showing chart legend.
	 * @return the j check box legend
	 */
	private JCheckBox getJCheckBoxLegend() {
		if (JCheckBoxLegend == null) {
			JCheckBoxLegend = new JCheckBox("Show legend");
			JCheckBoxLegend.setToolTipText("Show legend for charts");
			JCheckBoxLegend.addActionListener(this);
		}
		return JCheckBoxLegend;
	}
	/**
	 * Gets the jtree of threadProtocolVector.
	 * @return the j tree thread protocol vector
	 */
	private ChartPanel getJFreeChartPanel() {
		if (jFreeChartPanel == null) {
			
			jFreeChartPanel = new ChartPanel(getJFreeChart());
			
			jFreeChartPanel.setMinimumDrawWidth( 0 );
			jFreeChartPanel.setMinimumDrawHeight( 0 );
			jFreeChartPanel.setMaximumDrawWidth( 1920 );
			jFreeChartPanel.setMaximumDrawHeight( 1200 );
			jFreeChartPanel.setLayout(new BorderLayout(0, 0));
			jFreeChartPanel.setBackground(Color.WHITE);
			jFreeChartPanel.validate();
		}
		return jFreeChartPanel;
	}
	
	/**
	 * Gets the j free chart.
	 * @return the j free chart
	 */
	private JFreeChart getJFreeChart(){
		if(chart == null){
			chart = new JFreeChart(getPlot());
			chart.getLegend().setVisible(showLegend);
			chart.setBackgroundPaint(Color.WHITE);
		}	
		return chart;
	}
	
	/**
	 * Gets the plot.
	 * @return the plot
	 */
	private XYPlot getPlot(){
		if(plot == null){
			
			plot = new XYPlot();
			plot.setDomainAxis(new DateAxis("Date"));
			
			rendererTotal = new XYLineAndShapeRenderer(true, false);
			rendererDelta = new XYLineAndShapeRenderer(true, false);
			rendererLoad = new XYLineAndShapeRenderer(true, false);
			
			axisDelta = new NumberAxis("Delta CPU-Time [ms]");
			axisTotal = new NumberAxis("Total CPU-Time [ms]");;
			axisLoad = new NumberAxis("CPU-LOAD [%]");
			
			if(deltaCollection != null){
				//--- left axis scale ---
				plot.setDataset(0, deltaCollection);
				plot.setRenderer(0, rendererDelta);
			    axisDelta.setAutoRangeIncludesZero(false);
			    plot.setRangeAxis(0, axisDelta);
			}
			if(totalCollection != null){
				//--- right axis scale ---
				plot.setDataset(1, totalCollection);
				plot.setRenderer(1, rendererTotal);
			    axisTotal.setAutoRangeIncludesZero(false);
			    plot.setRangeAxis(1, axisTotal);
			}
			if(loadCollection != null){
				//--- right axis scale ---
				plot.setDataset(2, loadCollection);
				plot.setRenderer(2, rendererLoad);
				//---CPU-load  0-100%
			    axisLoad.setRange(0, 100);
			    plot.setRangeAxis(2, axisLoad);
			}
		    //--- first data set on left axis ---
			plot.mapDatasetToRangeAxis(0, 0);
		    //--- first data set on right axis ---
			plot.mapDatasetToRangeAxis(1, 1);
		    //--- third data set on right axis ---
			plot.mapDatasetToRangeAxis(2, 2);
		    // --- styling ---
			plot.setBackgroundPaint(Color.WHITE);
			plot.setDomainGridlinePaint(Color.BLACK);
			plot.setRangeGridlinePaint(Color.BLACK);
			
			
			// add Marker for average CPU utilization
			double avgLoadCPU = 0;
			if(extraObject != null){
				if(extraObject.getClass().toString().endsWith("ThreadInfoStorageMachine")){
					ThreadInfoStorageMachine tim = (ThreadInfoStorageMachine)extraObject;
					avgLoadCPU = Math.round(tim.getActualAverageLoadCPU());				
				}
			
				avgCPULoad = new ValueMarker(avgLoadCPU,Color.BLUE, new BasicStroke(3.0f), Color.BLUE,  new BasicStroke(3.0f), 1.0f);
			    avgCPULoad.setPaint(Color.BLUE);
			    avgCPULoad.setLabel("Average CPU: "+avgLoadCPU);
			    avgCPULoad.setLabelAnchor(RectangleAnchor.BOTTOM);
			    avgCPULoad.setLabelTextAnchor(TextAnchor.TOP_LEFT);
			    avgCPULoad.setLabelFont(new Font("Verdana", Font.BOLD, 12));
			    plot.addRangeMarker(2, avgCPULoad, Layer.FOREGROUND);
			}
		}
		return plot;
	}
	
	/**
	 * Gets the label curve type.
	 * @return the label curve type
	 */
	private JLabel getLblCurveType() {
		if (lblCurveType == null) {
			lblCurveType = new JLabel("Curve type:");
		}
		return lblCurveType;
	}
	
	/**
	 * Gets the label thread time.
	 * @return the label thread time
	 */
	private JLabel getLblThreadTime() {
		if (lblThreadTime == null) {
			lblThreadTime = new JLabel("Thread time:");
		}
		return lblThreadTime;
	}
	
	/**
	 * Export XYSeriesCollections as CSV file.
	 *
	 * @param sFileName the s file name
	 * @param collection1 the collection1
	 * @param collection2 the collection2
	 * @param collection3 the collection3
	 */
	private static void exportCsvFile(String sFileName, XYSeriesCollection collection1, XYSeriesCollection collection2, XYSeriesCollection collection3)
	  	{
			if(collection1 != null){
				try{
					FileWriter writer = new FileWriter(sFileName);
					/* Date(X), TOTAL_CPU_USER_TIME<>1 (Y), DELTA_CPU_USER_TIME<>1,...,TOTAL_CPU_USER_TIME<>2, .... 
					 * linuxtime, double, double,...
					 * .
					 * .
					 * .
					 */				    
				    if(collection1.getSeriesCount() != 0){
				    	// --- write header ---
					    writer.append("\"Date\"");
					    writer.append(';');
					    //--- delta ---
					    for(int x=0;x<collection1.getSeries().size(); x++){
					    	writer.append("\"" + collection1.getSeries(x).getKey().toString() + "\"");
					    	writer.append(';');						
						}
					    // --- total ---
					    if(collection2 != null){
					    	for(int x=0;x<collection2.getSeries().size(); x++){
						    	writer.append("\"" + collection2.getSeries(x).getKey().toString() + "\"");
						    	writer.append(';');						
							}
					    }
					    // --- load ---
					    if(collection3 != null){
					    	for(int x=0;x<collection3.getSeries().size(); x++){
						    	writer.append("\"" + collection3.getSeries(x).getKey().toString() + "\"");
						    	writer.append(';');						
							}
					    }
					    writer.append('\n');
					    // --- header complete ---
				    	// --- write data rows ---
					    int rowCount = collection1.getItemCount(0);
					    for(int row = 0;row < rowCount; row++){
					    	writer.append("\"" + collection1.getSeries(0).getDataItem(row).getX().toString().replace('.',',') + "\"");//Date
					    	writer.append(';');	
					    	//---delta ---
						    for(int x = 0;x < collection1.getSeries().size(); x++){		
						    	writer.append("\"" + collection1.getSeries(x).getDataItem(row).getY().toString().replace('.',',') + "\"");//value
						    	writer.append(';');
							}
						    //---total ---
						    if(collection2 != null){
						    	for(int x = 0;x < collection2.getSeries().size(); x++){
							    	writer.append("\"" + collection2.getSeries(x).getDataItem(row).getY().toString().replace('.',',') + "\"");//value
							    	writer.append(';');						
								}
						    }
						    //---load ---
						    if(collection3 != null){
						    	for(int x = 0;x < collection3.getSeries().size(); x++){
							    	writer.append("\"" + collection3.getSeries(x).getDataItem(row).getY().toString().replace('.',',') + "\"");//value
							    	writer.append(';');						
								}
						    }
						    writer.append('\n');
					    }
					    // --- data complete ---
				    }else{
				    	writer.append("no data recorded yet\n");
				    }
				    writer.flush();
				    writer.close();
				}catch(IOException e)
					{
			     		e.printStackTrace();
					} 
			}
	   }

	/**
	 * @return the fileChooser
	 */
	public JFileChooser getFileChooser() {
		if(fileChooser == null){
			fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Export data"); 
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new FileNameExtensionFilter("CSV file","csv"));
		}
		return fileChooser;
	}

	/**
	 * @param fileChooser the fileChooser to set
	 */
	public void setFileChooser(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}
	
	/**
	 * Gets the btn export csv.
	 * @return the btn export csv
	 */
	private JButton getBtnExportCSV() {
		if (btnExportCSV == null) {
			btnExportCSV = new JButton("Export CSV");
			btnExportCSV.setVerticalAlignment(SwingConstants.BOTTOM);
			btnExportCSV.addActionListener(this);
		}
		return btnExportCSV;
	}
}
