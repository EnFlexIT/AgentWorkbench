package de.enflexit.df.core.charts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.charts.model.AbstractChart;
import de.enflexit.df.core.model.DataController;

/**
 * The Class JPanelChartConfiguration.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelChartConfiguration extends JTabbedPane implements PropertyChangeListener {

	private static final long serialVersionUID = -8134787460803733040L;

	private DataController dataController;
	private DefaultMutableTreeNode dataTreeNode;
	
	private List<AbstractChart> chartList;
	private AbstractChart chart;
	
	private JPanelChartConfigurationBase jPanelChartConfigurationBase;
	private JPanelChartConfigurationDataSeries jPanelChartConfigurationDataSeries;
	
	
	/**
	 * Instantiates a new j panel chart configuration.
	 */
	public JPanelChartConfiguration() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setTabPlacement(JTabbedPane.TOP);
		this.addTab("Title & Label", this.getJPanelChartConfigurationBase());
		this.addTab("Data", this.getJPanelChartConfigurationDataSeries());
	}
	private JPanelChartConfigurationBase getJPanelChartConfigurationBase() {
		if (jPanelChartConfigurationBase==null) {
			jPanelChartConfigurationBase = new JPanelChartConfigurationBase(this);
		}
		return jPanelChartConfigurationBase;
	}
	private JPanelChartConfigurationDataSeries getJPanelChartConfigurationDataSeries() {
		if (jPanelChartConfigurationDataSeries==null) {
			jPanelChartConfigurationDataSeries = new JPanelChartConfigurationDataSeries(this);
		}
		return jPanelChartConfigurationDataSeries;
	}
	
	/**
	 * Sets the chart list to be configured.
	 * @param newChchart the new chart
	 */
	private void setChartList(List<AbstractChart> newChartList) {
		
		if (this.chartList!=null && newChartList!=null && (newChartList==this.chartList || newChartList.equals(this.chartList))==true) return;
		
		this.chartList = newChartList;
		if (this.chartList!=null && this.chartList.size()==1) {
			this.setChart(this.chartList.get(0));
		} else {
			this.setChart(null);
		}
	}
	/**
	 * Sets the chart that is currently to be configured.
	 * @param newChart the new chart to configure
	 */
	private void setChart(AbstractChart newChart) {
		
		if (this.chart!=null && newChart==this.chart) return;
		
		this.chart = newChart;
		this.getJPanelChartConfigurationBase().setChart(this.chart);
		this.getJPanelChartConfigurationDataSeries().setChart(this.chart);
	}
	
	/**
	 * Sets the tree node selection.
	 *
	 * @param dataController the data controller
	 * @param dataTreeNode the data tree node
	 */
	public void setTreeNodeSelection(DataController dataController, DefaultMutableTreeNode dataTreeNode) {
		
		if (dataController==null) {
			if (this.dataController!=null) {
				this.dataController.removePropertyChangeListener(this);
			}
			this.dataController = dataController; // = null !
			
		} else {
			if (this.dataController!=dataController) {
				if (this.dataController!=null) {
					this.dataController.removePropertyChangeListener(this);
				}
				this.dataController = dataController;
				this.dataController.addPropertyChangeListener(this);
			}
		}
		this.dataTreeNode = dataTreeNode;
	}
	
	/**
	 * Inform about chart settings changed.
	 *
	 * @param propertyName the property name
	 * @param oldValue the old value
	 * @param newValue the new value
	 */
	public void informAboutChartEdit(String propertyName, Object oldValue, Object newValue) {
		if (this.dataController!=null) {
			this.dataController.firePropertyChange(propertyName, oldValue, newValue);
		}
	}
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		switch (evt.getPropertyName()) {
		case DataController.DC_CHART_SELECTED: 
			this.setChartList((List<AbstractChart>) evt.getNewValue());
			break;
		}
	}
	
}
