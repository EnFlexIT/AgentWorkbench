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
package gasmas.compStat.display;

import gasmas.compStat.CompressorStationModel;
import gasmas.ontology.CompStatAdiabaticEfficiency;
import gasmas.ontology.CompStatTcMeasurement;
import gasmas.ontology.TurboCompressor;
import gasmas.ontology.ValueType;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * The Class TurboCompressorDisplayChart.
 */
public class TurboCompressorDisplayChart extends ChartPanel {

	private static final long serialVersionUID = 1L;

	private CompressorStationModel compressorStationModel = null;
	private String turboCompressorID = null;
	private TurboCompressor myTurboCompressor = null;
	
	private HashMap<Double, XYSeries> speedXYSeries = null;  //  @jve:decl-index=0:
	
	/**
	 * This is the default constructor
	 */
	public TurboCompressorDisplayChart(CompressorStationModel compressorStationModel, String turboCompressorID) {
		super(null);
		this.compressorStationModel = compressorStationModel;
		this.turboCompressorID = turboCompressorID;
		this.myTurboCompressor = (TurboCompressor) this.compressorStationModel.getComponent(this.turboCompressorID);
		this.initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
        
		this.setSize(new Dimension(450, 350));
		// --- Component background -------------
		this.setBackground(Color.WHITE);								
        
        this.setChart(this.getXyChart(this.getXyDataset()));
        // --- Chart background -----------------
        this.getChart().getPlot().setBackgroundPaint(Color.WHITE);		
		// --- Background grid color ------------
		this.getChart().getXYPlot().setDomainGridlinePaint(Color.BLACK);
		this.getChart().getXYPlot().setRangeGridlinePaint(Color.BLACK);

		this.setMouseWheelEnabled(true);

	}

    /**
     * Sets the current turbo compressor.
     * @param myTurboCompressor the new my turbo compressor
     */
    public void setTurboCompressor(TurboCompressor newTurboCompressor) {
		this.myTurboCompressor = newTurboCompressor;
	}
	/**
	 * Returns the turbo compressor.
	 * @return the turbo compressor
	 */
	public TurboCompressor getTurboCompressor() {
		return myTurboCompressor;
	}

	/**
     * Gets the chart.
     *
     * @param dataset the dataset
     * @return the chart
     */
    private JFreeChart getXyChart(XYDataset dataset) {

    	JFreeChart chart = ChartFactory.createXYLineChart(
            "Compressor Characteristics",
            "Volumentric Flow Rate [m³/s]",
            "Adiabatic Head [kJ/kg]",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
    	XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        
        NumberAxis xAxis2 = new NumberAxis(null);
        plot.setDomainAxis(1, xAxis2);
        
        NumberAxis yAxis2 = new NumberAxis(null);
        plot.setRangeAxis(1, yAxis2);
        
        List<Integer> axisIndices = Arrays.asList(new Integer[] {new Integer(0), new Integer(1)});
        plot.mapDatasetToDomainAxes(0, axisIndices);
        plot.mapDatasetToRangeAxes(0, axisIndices);
        ChartUtilities.applyCurrentTheme(chart);
        return chart;
    }
    
    
    /**
     * Creates a sample dataset.
     * @return A sample dataset.
     */
    private XYDataset getXyDataset() {
        
    	XYSeriesCollection xyData = new XYSeriesCollection(); 
    	
    	jade.util.leap.List charDiaMeasurements = this.myTurboCompressor.getCharacteristicDiagramMeasurements();
		for (int i = 0; i < charDiaMeasurements.size(); i++) {
			CompStatAdiabaticEfficiency adiabaticEff = (CompStatAdiabaticEfficiency) charDiaMeasurements.get(i);
			
			String efficiency = adiabaticEff.getAdiabaticEfficiency();
			XYSeries adiabaticEfficiencySeries = new XYSeries(efficiency);
			if (efficiency!=null && efficiency.equals("")==false) {
				jade.util.leap.List measurements = adiabaticEff.getMeasurements();
				for (int j = 0; j < measurements.size(); j++) {
					CompStatTcMeasurement measurement = (CompStatTcMeasurement) measurements.get(j);
					adiabaticEfficiencySeries.add(getDouble(measurement.getVolumetricFlowrate()), getDouble(measurement.getAdiabaticHead()));
					// --- Add to speed series ------------
					this.addToSpeedSeries(measurement.getSpeed(), measurement.getVolumetricFlowrate(), measurement.getAdiabaticHead());
				}
				// --- Add to series collection -----------
				xyData.addSeries(adiabaticEfficiencySeries);
			}
		}
		
		XYSeries settlelineSeries = new XYSeries("Settleline");
		jade.util.leap.List settlelineMeasurements = this.myTurboCompressor.getSettlelineMeasurements();
		for (int i = 0; i < settlelineMeasurements.size(); i++) {
			CompStatTcMeasurement measurement = (CompStatTcMeasurement) settlelineMeasurements.get(i);
			if (!(measurement.getSpeed().getValue()==0 && measurement.getVolumetricFlowrate().getValue()==0 && measurement.getAdiabaticHead().getValue()==0)) {
				settlelineSeries.add(getDouble(measurement.getVolumetricFlowrate()), getDouble(measurement.getAdiabaticHead()));
				// --- Add to speed series ------------
				this.addToSpeedSeries(measurement.getSpeed(), measurement.getVolumetricFlowrate(), measurement.getAdiabaticHead());
			}
		}
		// --- Add to series collection -----------
		xyData.addSeries(settlelineSeries);

		// --- Add speed series to collection -----
		Vector<Double> keys = new Vector<Double>(this.getSpeedXYSeries().keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			Double key = keys.get(i);
			xyData.addSeries(this.getSpeedXYSeries().get(key));
		}
		
        return xyData;
    }

    /**
     * Adds a measurement to a speed series.
     *
     * @param speed the speed
     * @param flowRate the flow rate
     * @param adiabaticHead the adiabatic head
     */
    public void addToSpeedSeries(ValueType speedVT, ValueType flowRateVT, ValueType adiabaticHeadVT) {
    	
    	Double speed = this.getDouble(speedVT);
    	Double flowRate = this.getDouble(flowRateVT);
    	Double adiabaticHead = this.getDouble(adiabaticHeadVT);
    	
    	XYSeries series = this.getSpeedXYSeries().get(speed);
    	if (series==null) {
    		series = new XYSeries(speed);
    		this.getSpeedXYSeries().put(speed, series);
    	}
    	series.add(flowRate, adiabaticHead);
    	
    }
    
    public void setSpeedXYSeries(HashMap<Double, XYSeries> speedXYSeries) {
		this.speedXYSeries = speedXYSeries;
	}
	public HashMap<Double, XYSeries> getSpeedXYSeries() {
		if (speedXYSeries==null) {
			speedXYSeries=new HashMap<Double, XYSeries>();
		}
		return speedXYSeries;
	}


	
	
	private Double getDouble(ValueType valueType) {
    	return getDouble(valueType.getValue());
    }
    private Double getDouble(Float floatValue) {
    	double doubleValue = floatValue;
    	return doubleValue;
    }
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
