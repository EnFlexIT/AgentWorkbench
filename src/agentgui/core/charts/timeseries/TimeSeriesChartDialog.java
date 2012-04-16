package agentgui.core.charts.timeseries;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import agentgui.core.application.Language;
import agentgui.core.charts.ChartDialog;
import agentgui.ontology.TimeSeries;

public class TimeSeriesChartDialog extends ChartDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -318270376215113469L;
	
	protected JToggleButton tglbtnSum;
	
	/**
	 * Instantiates a new time series widget.
	 * @param timeSeries 
	 */
	public TimeSeriesChartDialog (Window owner, TimeSeries timeSeries) {
		super(owner);
		this.model = new TimeSeriesDataModel(timeSeries);
		initialize();
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		toolBar.add(getTglbtnSum());
		
		chartTab = new TimeSeriesChartTab((TimeSeriesDataModel) model);
		tabbedPane.addTab("Chart", chartTab);
		tableTab = new TimeSeriesTableTab((TimeSeriesDataModel) model);
		tabbedPane.addTab(Language.translate("Tabelle"), tableTab);
		settingsTab = new TimeSeriesSettingsTab((TimeSeriesDataModel) model, (TimeSeriesChartTab) chartTab);
		tabbedPane.addTab(Language.translate("Einstellungen"), settingsTab);
	}

	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if(ae.getSource()== tglbtnSum){
			((TimeSeriesDataModel)this.model).setSumSeriesEnabled(tglbtnSum.isSelected());
		}else{
			super.actionPerformed(ae);
		}
		
		
	}

	/**
	 * @return the model
	 */
	public TimeSeriesDataModel getModel() {
		return (TimeSeriesDataModel) model;
	}

	
	
	
	
	private JToggleButton getTglbtnSum() {
		if (tglbtnSum == null) {
			tglbtnSum = new JToggleButton("");
			tglbtnSum.setIcon(new ImageIcon(ChartDialog.class.getResource("/agentgui/core/gui/img/MBTransSum.png")));
			tglbtnSum.setToolTipText(Language.translate("Summe einblenden"));
			tglbtnSum.addActionListener(this);
		}
		return tglbtnSum;
	}
	
}
