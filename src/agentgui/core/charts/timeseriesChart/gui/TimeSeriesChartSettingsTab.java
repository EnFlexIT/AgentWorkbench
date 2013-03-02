package agentgui.core.charts.timeseriesChart.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;

import agentgui.core.application.Language;
import agentgui.core.charts.DataModel;
import agentgui.core.charts.gui.ChartSettingsTab;
import agentgui.core.charts.timeseriesChart.TimeSeriesChartSettings;

public class TimeSeriesChartSettingsTab extends ChartSettingsTab {
	
	TimeSeriesChartEditorJPanel parent; 

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 675579393370531354L;
	
	private JLabel lblTimeFormatSelector = null;
	private TimeFormatSelector timeFormatSelector = null;

	public TimeSeriesChartSettingsTab(DataModel model, TimeSeriesChartEditorJPanel parent) {
		this.model = model;
		this.parent = parent;
		
		this.settings = model.getChartSettings();
		this.settings.addObserver(this);
		
		initialize();
	}
	
	

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartSettingsTab#initialize()
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		// --- Insert time format setter above series settings table 
		
		// --- Move table
		GridBagLayout layoutManager = (GridBagLayout) getLayout();
		GridBagConstraints gbcTable = layoutManager.getConstraints(getSpTblSeriesSettings());
		int oldY = gbcTable.gridy;
		gbcTable.gridy = oldY + 1;
		layoutManager.setConstraints(getSpTblSeriesSettings(), gbcTable);
		
		// --- Add label
		GridBagConstraints gbcLblTimeFormatSelector = new GridBagConstraints();
		gbcLblTimeFormatSelector.gridx = 0;
		gbcLblTimeFormatSelector.gridy = oldY;
		gbcLblTimeFormatSelector.anchor = GridBagConstraints.NORTHWEST;
		gbcLblTimeFormatSelector.insets = new Insets(8, 5, 5, 5);
		this.add(getLblTimeFormatSelector(), gbcLblTimeFormatSelector);
		
		// --- Add format selector component
		GridBagConstraints gbcTimeFormatSelector = new GridBagConstraints();
		gbcTimeFormatSelector.gridx = 1;
		gbcTimeFormatSelector.gridy = oldY;
		gbcTimeFormatSelector.anchor = GridBagConstraints.WEST;
		gbcTimeFormatSelector.fill = GridBagConstraints.HORIZONTAL;
		gbcTimeFormatSelector.insets = new Insets(3, 0, 0, 0);
		this.add(getTimeFormatSelector(), gbcTimeFormatSelector);
		
	}



	/**
	 * @return the jPanelTimeFormater
	 */
	public TimeFormatSelector getTimeFormatSelector() {
		if (timeFormatSelector==null) {
			timeFormatSelector = new TimeFormatSelector(this);
			timeFormatSelector.setPreferredSize(new Dimension(360, 80));
			timeFormatSelector.setTimeFormat(((TimeSeriesChartSettings)settings).getTimeFormat());
			timeFormatSelector.addActionListener(this);
		}
		return timeFormatSelector;
	}



	/**
	 * @return the lblTimeFormatSelector
	 */
	public JLabel getLblTimeFormatSelector() {
		if(lblTimeFormatSelector == null){
			lblTimeFormatSelector = new JLabel(Language.translate("Zeit-Format"));
		}
		return lblTimeFormatSelector;
	}



	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartSettingsTab#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getTimeFormatSelector()){
			((TimeSeriesChartSettings)settings).setTimeFormat(getTimeFormatSelector().getTimeFormat());
		}else{
			super.actionPerformed(e);
		}
	}



	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartSettingsTab#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource() == getTimeFormatSelector()){
			((TimeSeriesChartSettings)settings).setTimeFormat(getTimeFormatSelector().getTimeFormat());
		}else{
			super.focusLost(e);
		}
		
	}
	
	public String getDefaultTimeFormat(){
		return parent.getDefaultTimeFormat();
	}


}
