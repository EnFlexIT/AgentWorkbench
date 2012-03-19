package agentgui.core.charts.timeseries;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import agentgui.core.application.Language;
import agentgui.ontology.TimeSeries;

public class TimeSeriesEditDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -318270376215113469L;
	
	private TimeSeries timeSeries = null;
	
	private JButton jButtonImport = null;
	private JTabbedPane jTabbedPane = null;
	private TimeSeriesChartTab chartTab = null; 
	private TimeSeriesTableTab tableTab = null;
	private TimeSeriesSettingsTab settingsTab = null;
	
	private TimeSeriesDataModel model = null;
	
	
	/**
	 * Instantiates a new time series widget.
	 * @param timeSeries 
	 */
	public TimeSeriesEditDialog (Window owner, TimeSeries timeSeries) {
		super(owner);
		this.timeSeries = timeSeries;
		this.model = new TimeSeriesDataModel(null);
		initialize();
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		
		setSize(600, 400);
        
        
        GridBagConstraints gbcJButtonImport = new GridBagConstraints();
        gbcJButtonImport.gridx = 0;
        gbcJButtonImport.gridy = 1;
        gbcJButtonImport.anchor = GridBagConstraints.WEST;
        
        GridBagConstraints gbcTabbedPane = new GridBagConstraints();
        gbcTabbedPane.weighty = 1.0;
        gbcTabbedPane.weightx = 1.0;
        gbcTabbedPane.gridx = 0;
        gbcTabbedPane.gridy = 0;
        gbcTabbedPane.gridwidth = 1;
        gbcTabbedPane.fill = GridBagConstraints.BOTH;
        
//        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        this.setLayout(new GridBagLayout());
        this.add(getJButtonImport(), gbcJButtonImport);
        this.add(getJTabbedPane(), gbcTabbedPane);
			
	}

	
	/**
	 * This method initializes jButtonImport.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonImport(){
		if(jButtonImport == null){
			jButtonImport = new JButton();
			jButtonImport.setText(Language.translate("Daten Importieren..."));
			jButtonImport.addActionListener(this);
		}
		return jButtonImport;
	}
	
	/**
	 * This method initializes jTabbedPane.
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane(){
		if(jTabbedPane == null){
			jTabbedPane = new JTabbedPane();
			chartTab = new TimeSeriesChartTab(model);
			tableTab = new TimeSeriesTableTab(model);
			settingsTab = new TimeSeriesSettingsTab(model, chartTab);
			jTabbedPane.addTab("Chart", chartTab);
			jTabbedPane.addTab(Language.translate("Tabelle"), tableTab);
			jTabbedPane.addTab(Language.translate("Einstellungen"), settingsTab);
		}
		return jTabbedPane;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object source = ae.getSource();
		if (source == jButtonImport){
			// Import CSF data

			// Choose file
			JFileChooser jFileChooserImportCSV = new JFileChooser();
			if(jFileChooserImportCSV.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				File csvFile = jFileChooserImportCSV.getSelectedFile();
				
				// Read data and init models
				model.importTimeSeriesFromCSV(csvFile);
				repaint();
			}
		}
		
	}

}
