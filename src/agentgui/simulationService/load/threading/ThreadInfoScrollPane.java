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
package agentgui.simulationService.load.threading;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * The Class ThreadInfoScrollPane.
 * 
 * Displays a JPanel with Chart of multiple Series and display/filtering options.
 * 
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadInfoScrollPane extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private XYPlot plot;
	private ChartPanel jFreeChartPanel;
	private XYSeriesCollection deltaCollection;
	private XYSeriesCollection totalCollection;
	private XYSeriesCollection loadCollection;
	private JPanel JPanelFilter;
	private JFreeChart chart;
	
	private JCheckBox jCheckBoxUserTime;
	private JCheckBox jCheckBoxSystemTime;
	private JCheckBox jCheckBoxDelta;
	private JCheckBox jCheckBoxTotal;
	private JCheckBox JCheckBoxLegend;
	
	private XYLineAndShapeRenderer rendererTotal;
	private XYLineAndShapeRenderer rendererDelta;
	private XYLineAndShapeRenderer rendererLoad;
	
	NumberAxis axisDelta;
	NumberAxis axisTotal;
	NumberAxis axisLoad;
	
	private HashMap<String, Paint> lineColorMap;

	/** Display setting.Set true to show delta. */
	private boolean showDelta;
	/** Display setting.Set true to show total. */
	private boolean showTotal;
	/** Display setting. Set to true to show the system-time*/
	private boolean showSystemTime;
	/** Display setting. Set to true to show the user-time*/
	private boolean showUserTime;
	/** Display charts in a new window if true*/
	private boolean popup;
	/** Display legend if true*/
	private boolean showLegend;
	/** The frame title. */
	private String frameTitle;
	
	private float[][] pattern;
	private BasicStroke stroke;
	
	/**
	 * Instantiates a new thread info scroll pane.
	 *
	 * @param deltaCollection the delta collection
	 * @param totalCollection the total collection
	 * @param popup the popup
	 * @param frameTitle the frame title
	 * @param lineColorMap 
	 */
	public ThreadInfoScrollPane(XYSeriesCollection deltaCollection, XYSeriesCollection totalCollection, XYSeriesCollection loadCollection, boolean popup, String frameTitle, HashMap<String, Paint> lineColorMap) {		
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
		this.lineColorMap = lineColorMap;
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
		deltaCollection.addChangeListener(new CollectionSeriesChangeListener());
		totalCollection.addChangeListener(new CollectionSeriesChangeListener());
		
		pattern = new float[][]{{10.0f},{10.0f,10.0f},{10.0f,10.0f,2.0f,10.0f},{1.0f,10.0f}};
		stroke = new BasicStroke(1.5f, BasicStroke.CAP_ROUND,  BasicStroke.JOIN_MITER, 10.0f, pattern[3], 0.0f);
		String seriesKey;
		Paint color = Color.BLACK;
		
		if(deltaCollection != null){
			
			for(int x=0;x<deltaCollection.getSeries().size(); x++){
				seriesKey = deltaCollection.getSeries(x).getKey().toString();
				
				if(lineColorMap != null){
					if(lineColorMap.containsKey(seriesKey) == true){
						color = lineColorMap.get(seriesKey);
						rendererDelta.setSeriesPaint(x, color);
					}
				}	
				// --- user time series ->  dotted line style ---
				if(seriesKey.contains("USER") == true){
					rendererDelta.setSeriesStroke(x, stroke);
				}
			}
			if(popup == true){
				axisDelta.setLabelPaint(color);
			}
			
		}
		
		if( totalCollection != null){
			
			for(int x=0;x<totalCollection.getSeries().size(); x++){
				seriesKey = totalCollection.getSeries(x).getKey().toString();				
				
				if(lineColorMap != null){
					if(lineColorMap.containsKey(seriesKey) == true){
						color = lineColorMap.get(seriesKey);
						rendererTotal.setSeriesPaint(x, color);
					}
				}
				// --- user time series ->  dotted line style ---
				if(seriesKey.contains("USER") == true){
					rendererTotal.setSeriesStroke(x, stroke);
				}
			}
			if(popup == true){
				axisTotal.setLabelPaint(color);
			}
			
		}
		
		if(loadCollection != null){
			
			for(int x=0;x<loadCollection.getSeries().size(); x++){
				seriesKey = loadCollection.getSeries(x).getKey().toString();				
				
				if(lineColorMap.containsKey(seriesKey) == true){
					color = lineColorMap.get(seriesKey);
					rendererLoad.setSeriesPaint(x, color);
				}
				if(popup == true){
					axisLoad.setLabelPaint(color);
				}
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
	 *
	 * @see CollectionSeriesChangeEvent
	 */
	class CollectionSeriesChangeListener implements DatasetChangeListener{
		private HashMap<String, Paint> localLineColorMap;
		private int r,g,b;
		private Random rand;
		private int seriesCount;
		
		public CollectionSeriesChangeListener(){
			this.localLineColorMap = new HashMap<String, Paint>();
			this.rand = new Random();
			this.seriesCount = -1;
		}
		
		@Override
		public void datasetChanged(DatasetChangeEvent event) {
			
			if(popup == false){
				// -- set one color for a series  ---
				if(deltaCollection != null){
					
					int newSeriesCount = deltaCollection.getSeriesCount();
			        if (newSeriesCount == seriesCount) {
			            // no series was added/removed
			        } else if (newSeriesCount < seriesCount) {
			            //--- removed ---
			            seriesCount = newSeriesCount;
			        } else {
			            //--- added ---
			            seriesCount = newSeriesCount;
			            
			            for(int x=0;x+2<=deltaCollection.getSeries().size(); x=x+2){
							String seriesKey = deltaCollection.getSeries(x).getKey().toString();
							Paint color = Color.BLACK;
							
							if(localLineColorMap.containsKey(seriesKey) == false){
								r = rand.nextInt(256);
								g = rand.nextInt(256);
								b = rand.nextInt(256);
								color = new Color(r, g, b);
								localLineColorMap.put(seriesKey, color);
							}else{
								color = localLineColorMap.get(seriesKey);
							}
							rendererDelta.setSeriesPaint(x, color);
							rendererDelta.setSeriesPaint(x+1, color);
							rendererTotal.setSeriesPaint(x, color);
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
			
			this.JPanelFilter = new JPanel();
			this.JPanelFilter.setBorder(null);
			
			// --- Set default value -----------------------
			getJCheckBoxSystemTime().setSelected(showSystemTime);
			getJCheckBoxUserTime().setSelected(showUserTime);
			getJCheckBoxDelta().setSelected(showDelta);
			getJCheckBoxTotal().setSelected(showTotal);
			getJCheckBoxLegend().setSelected(showLegend);
			
			GridBagLayout gbl_JPanelFilter = new GridBagLayout();
			gbl_JPanelFilter.columnWidths = new int[] {30, 0, 0, 0, 30, 30, 30, 30, 30, 0};
			gbl_JPanelFilter.rowHeights = new int[] {23};
			gbl_JPanelFilter.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE, 0.0, 0.0, 0.0, 0.0, 0.0};
			gbl_JPanelFilter.rowWeights = new double[]{0.0};
			this.JPanelFilter.setLayout(gbl_JPanelFilter);
			GridBagConstraints gbc_jCheckBoxUserTime = new GridBagConstraints();
			gbc_jCheckBoxUserTime.insets = new Insets(1, 1, 0, 5);
			gbc_jCheckBoxUserTime.gridx = 2;
			gbc_jCheckBoxUserTime.gridy = 0;
			this.JPanelFilter.add(getJCheckBoxUserTime(), gbc_jCheckBoxUserTime);
			GridBagConstraints gbc_jCheckBoxDelta = new GridBagConstraints();
			gbc_jCheckBoxDelta.insets = new Insets(1, 1, 0, 5);
			gbc_jCheckBoxDelta.gridx = 0;
			gbc_jCheckBoxDelta.gridy = 0;
			this.JPanelFilter.add(getJCheckBoxDelta(), gbc_jCheckBoxDelta);
			GridBagConstraints gbc_jCheckBoxSystemTime = new GridBagConstraints();
			gbc_jCheckBoxSystemTime.insets = new Insets(1, 1, 0, 5);
			gbc_jCheckBoxSystemTime.gridx = 3;
			gbc_jCheckBoxSystemTime.gridy = 0;
			this.JPanelFilter.add(getJCheckBoxSystemTime(), gbc_jCheckBoxSystemTime);
			GridBagConstraints gbc_jCheckBoxTotal = new GridBagConstraints();
			gbc_jCheckBoxTotal.insets = new Insets(0, 0, 0, 5);
			gbc_jCheckBoxTotal.gridx = 1;
			gbc_jCheckBoxTotal.gridy = 0;
			this.JPanelFilter.add(getJCheckBoxTotal(), gbc_jCheckBoxTotal);
			GridBagConstraints gbc_JCheckBoxLegend = new GridBagConstraints();
			gbc_JCheckBoxLegend.insets = new Insets(0, 0, 0, 5);
			gbc_JCheckBoxLegend.gridx = 9;
			gbc_JCheckBoxLegend.gridy = 0;
			this.JPanelFilter.add(getJCheckBoxLegend(), gbc_JCheckBoxLegend);
			
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
			rendererTotal.setSeriesLinesVisible(x, condition);
		}
		for(int x=0;x<deltaCollection.getSeries().size(); x++){
			condition = (deltaCollection.getSeries(x).getKey().toString().contains("SYSTEM") && showSystemTime  && showDelta) || (deltaCollection.getSeries(x).getKey().toString().contains("USER") && showUserTime && showDelta);
			rendererDelta.setSeriesLinesVisible(x, condition);
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
			this.jCheckBoxDelta = new JCheckBox("Delta");
			this.jCheckBoxDelta.setToolTipText("Show time delta charts");
			this.jCheckBoxDelta.addActionListener(this);
		}
		return jCheckBoxDelta;
	}
	
	/**
	 * Gets the j radio button total.
	 * @return the j radio button total
	 */
	private JCheckBox getJCheckBoxTotal() {
		if (jCheckBoxTotal == null) {
			this.jCheckBoxTotal = new JCheckBox("Total");
			this.jCheckBoxTotal.setToolTipText("Show total time charts");
			this.jCheckBoxTotal.addActionListener(this);
		}
		return jCheckBoxTotal;
	}
	
	/**
	 * Gets the j radio button for filtering user-time charts.
	 * @return the j radio button filter agents
	 */
	private JCheckBox getJCheckBoxUserTime() {
		if (jCheckBoxUserTime == null) {
			this.jCheckBoxUserTime = new JCheckBox("User time");
			this.jCheckBoxUserTime.setToolTipText("Show user time charts");
			this.jCheckBoxUserTime.addActionListener(this);
		}
		return jCheckBoxUserTime;
	}
	
	/**
	 * Gets the j radio button for filtering user-time charts.
	 * @return the j radio button filter agents
	 */
	private JCheckBox getJCheckBoxSystemTime() {
		if (jCheckBoxSystemTime == null) {
			this.jCheckBoxSystemTime = new JCheckBox("System time");
			this.jCheckBoxSystemTime.setToolTipText("Show  system time charts");
			this.jCheckBoxSystemTime.addActionListener(this);
		}
		return jCheckBoxSystemTime;
	}
	
	/**
	 * Gets the check box for showing chart legend.
	 * @return the j check box legend
	 */
	private JCheckBox getJCheckBoxLegend() {
		if (JCheckBoxLegend == null) {
			this.JCheckBoxLegend = new JCheckBox("Show legend");
			this.JCheckBoxLegend.setToolTipText("Show legend for charts");
			this.JCheckBoxLegend.addActionListener(this);
		}
		return JCheckBoxLegend;
	}
	/**
	 * Gets the jtree of threadProtocolVector.
	 * @return the j tree thread protocol vector
	 */
	private ChartPanel getJFreeChartPanel() {
		if (jFreeChartPanel == null) {
			
			this.jFreeChartPanel = new ChartPanel(getJFreeChart());
			
			this.jFreeChartPanel.setMinimumDrawWidth( 0 );
			this.jFreeChartPanel.setMinimumDrawHeight( 0 );
			this.jFreeChartPanel.setMaximumDrawWidth( 1920 );
			this.jFreeChartPanel.setMaximumDrawHeight( 1200 );
			this.jFreeChartPanel.setLayout(new BorderLayout(0, 0));
			this.jFreeChartPanel.validate();
		}
		return jFreeChartPanel;
	}
	
	/**
	 * Gets the j free chart.
	 * @return the j free chart
	 */
	private JFreeChart getJFreeChart(){
		if(chart == null){
			this.chart = new JFreeChart(getPlot());
			this.chart.getLegend().setVisible(showLegend);
		}	
		return chart;
	}
	
	/**
	 * Gets the plot.
	 * @return the plot
	 */
	private XYPlot getPlot(){
		if(plot == null){
			
			this.plot = new XYPlot();
			//TODO: simulation-time ?
			this.plot.setDomainAxis(new DateAxis("Date"));
			
			rendererTotal = new XYLineAndShapeRenderer(true, false);
			rendererDelta = new XYLineAndShapeRenderer(true, false);
			rendererLoad = new XYLineAndShapeRenderer(true, false);
			
			axisDelta = new NumberAxis("Delta CPU-Time [ms]");
			axisTotal = new NumberAxis("Total CPU-Time [ms]");;
			axisLoad = new NumberAxis("Machine CPU-LOAD [%]");
			
			if(deltaCollection != null){
				//--- left axis scale ---
				this.plot.setDataset(0, deltaCollection);
				this.plot.setRenderer(0, rendererDelta);
			    axisDelta.setAutoRangeIncludesZero(false);
			    this.plot.setRangeAxis(0, axisDelta);
			}
			if(totalCollection != null){
				//--- right axis scale ---
				this.plot.setDataset(1, totalCollection);
				this.plot.setRenderer(1, rendererTotal);
			    axisTotal.setAutoRangeIncludesZero(false);
			    this.plot.setRangeAxis(1, axisTotal);
			}
			if(loadCollection != null){
				//--- right axis scale ---
				this.plot.setDataset(2, loadCollection);
				this.plot.setRenderer(2, rendererLoad);
				//---CPU-load  0-100%
			    axisLoad.setRange(0, 100);
			    this.plot.setRangeAxis(2, axisLoad);
			}
		    //--- first data set on left axis ---
			this.plot.mapDatasetToRangeAxis(0, 0);
		    //--- first data set on right axis ---
			this.plot.mapDatasetToRangeAxis(1, 1);
		    //--- third data set on right axis ---
			this.plot.mapDatasetToRangeAxis(2, 2);
		    // --- styling ---
			this.plot.setBackgroundPaint(Color.WHITE);
			this.plot.setDomainGridlinePaint(Color.BLACK);
			this.plot.setRangeGridlinePaint(Color.BLACK);
			
		}
		return plot;
	}
}
