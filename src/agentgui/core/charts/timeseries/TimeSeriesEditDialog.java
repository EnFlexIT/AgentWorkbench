package agentgui.core.charts.timeseries;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import agentgui.core.application.Language;
import agentgui.ontology.TimeSeries;
import java.awt.Insets;

public class TimeSeriesEditDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -318270376215113469L;
	
	private JButton jButtonImport = null;
	private JTabbedPane jTabbedPane = null;
	private TimeSeriesChartTab chartTab = null; 
	private TimeSeriesTableTab tableTab = null;
	private TimeSeriesSettingsTab settingsTab = null;
	
	private TimeSeriesDataModel model = null;
	private JButton btnCancel;
	private JButton btnOk;
	
	private BufferedImage chartThumb = null;
	
	private boolean canceled = false;
	
	
	/**
	 * Instantiates a new time series widget.
	 * @param timeSeries 
	 */
	public TimeSeriesEditDialog (Window owner, TimeSeries timeSeries) {
		super(owner);
		this.model = new TimeSeriesDataModel(timeSeries);
		initialize();
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		
		setSize(600, 400);
        
        
        GridBagConstraints gbcJButtonImport = new GridBagConstraints();
        gbcJButtonImport.insets = new Insets(0, 0, 0, 5);
        gbcJButtonImport.gridx = 0;
        gbcJButtonImport.gridy = 1;
        gbcJButtonImport.anchor = GridBagConstraints.WEST;
        
        GridBagConstraints gbcTabbedPane = new GridBagConstraints();
        gbcTabbedPane.insets = new Insets(0, 0, 5, 0);
        gbcTabbedPane.weighty = 1.0;
        gbcTabbedPane.weightx = 1.0;
        gbcTabbedPane.gridx = 0;
        gbcTabbedPane.gridy = 0;
        gbcTabbedPane.gridwidth = 3;
        gbcTabbedPane.fill = GridBagConstraints.BOTH;
        
//        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(getJButtonImport(), gbcJButtonImport);
        getContentPane().add(getJTabbedPane(), gbcTabbedPane);
        GridBagConstraints gbc_btnOk = new GridBagConstraints();
        gbc_btnOk.weightx = 1.0;
        gbc_btnOk.anchor = GridBagConstraints.EAST;
        gbc_btnOk.insets = new Insets(0, 0, 0, 5);
        gbc_btnOk.gridx = 1;
        gbc_btnOk.gridy = 1;
        getContentPane().add(getBtnOk(), gbc_btnOk);
        GridBagConstraints gbc_btnCancel = new GridBagConstraints();
        gbc_btnCancel.anchor = GridBagConstraints.EAST;
        gbc_btnCancel.gridx = 2;
        gbc_btnCancel.gridy = 1;
        getContentPane().add(getBtnCancel(), gbc_btnCancel);
			
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
		}else if(ae.getSource() == btnOk){
			chartThumb = chartTab.createChartThumb();
			setVisible(false);
		}else if(ae.getSource() == btnCancel){
			canceled = true;
			setVisible(false);
		}
		
	}

	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton("OK");
			btnOk.addActionListener(this);
		}
		return btnOk;
	}

	/**
	 * @return the model
	 */
	public TimeSeriesDataModel getModel() {
		return model;
	}

	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * @return the chartThumb
	 */
	public BufferedImage getChartThumb() {
		return chartThumb;
	}
}
