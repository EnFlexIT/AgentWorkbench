package de.enflexit.awb.timeSeriesDataProvider.gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

/**
 * A configuration dialog for the weather data provider.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesDataProviderConfigurationDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 364118646586044313L;
	private TimeSeriesDataProviderConfigurationPanel configurationPanel;
	
	private JPanel buttonsPanel;
	private JButton btnOk;
	private JButton btnCancel;
	
	/**
	 * Instantiates a new weather data provider configuration dialog.
	 */
	public TimeSeriesDataProviderConfigurationDialog(Window owner) {
		super(owner);
		this.initialize();
	}
	
	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		this.setTitle("Time Series Data Provider Configuration");
		this.setSize(1200, 800);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getConfigurationPanel());
		getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
		
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	}

	/**
	 * Gets the configuration panel.
	 * @return the configuration panel
	 */
	private TimeSeriesDataProviderConfigurationPanel getConfigurationPanel() {
		if (configurationPanel == null) {
			configurationPanel = new TimeSeriesDataProviderConfigurationPanel();
		}
		return configurationPanel;
	}
	
	/**
	 * Gets the buttons panel.
	 * @return the buttons panel
	 */
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			GridBagLayout gbl_buttonsPanel = new GridBagLayout();
			gbl_buttonsPanel.columnWidths = new int[]{0, 0, 0};
			gbl_buttonsPanel.rowHeights = new int[]{0, 0};
			gbl_buttonsPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_buttonsPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			buttonsPanel.setLayout(gbl_buttonsPanel);
			GridBagConstraints gbc_btnOk = new GridBagConstraints();
			gbc_btnOk.anchor = GridBagConstraints.EAST;
			gbc_btnOk.insets = new Insets(10, 0, 10, 15);
			gbc_btnOk.gridx = 0;
			gbc_btnOk.gridy = 0;
			buttonsPanel.add(getBtnOk(), gbc_btnOk);
			GridBagConstraints gbc_btnCancel = new GridBagConstraints();
			gbc_btnCancel.anchor = GridBagConstraints.WEST;
			gbc_btnCancel.insets = new Insets(10, 15, 10, 0);
			gbc_btnCancel.gridx = 1;
			gbc_btnCancel.gridy = 0;
			buttonsPanel.add(getBtnCancel(), gbc_btnCancel);
		}
		return buttonsPanel;
	}
	
	/**
	 * Gets the ok button.
	 *
	 * @return the ok button
	 */
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton("OK");
			btnOk.setFont(new Font("Dialog", Font.BOLD, 12));
			btnOk.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			btnOk.setPreferredSize(new Dimension(85, 26));
			btnOk.addActionListener(this);

		}
		return btnOk;
	}
	
	/**
	 * Gets the cancel button
	 * @return the cancel button
	 */
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			btnCancel.setForeground(AwbThemeColor.ButtonTextRed.getColor());
			btnCancel.setPreferredSize(new Dimension(85, 26));
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getBtnOk()) {
			TimeSeriesDataProvider.getInstance().storeConfigurationsList();
			TimeSeriesDataProvider.getInstance().resetDataSources();
			this.dispose();
		} else if(ae.getSource()==this.getBtnCancel()) {
			this.dispose();
		}
		
	}
}
