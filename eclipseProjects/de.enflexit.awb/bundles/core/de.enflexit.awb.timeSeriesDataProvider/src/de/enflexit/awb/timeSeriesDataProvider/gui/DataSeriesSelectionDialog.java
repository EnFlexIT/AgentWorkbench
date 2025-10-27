package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.common.swing.AwbThemeColor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;

/**
 * A simple dialog to select a data series from the time series provider.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataSeriesSelectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1733677067996570288L;
	private DataSeriesSelectionPanel dataSeriesSelectionPanel;
	private JPanel buttonsPanel;
	private JButton jButtonOk;
	private JButton jButtonCancel;
	
	private boolean canceled;
	
	/**
	 * Instantiates a new data series selection dialog.
	 */
	public DataSeriesSelectionDialog() {
		initialize();
	}
	
	/**
	 * Instantiates a new data series selection dialog.
	 * @param owner the owner
	 */
	public DataSeriesSelectionDialog(Window owner) {
		super(owner);
		this.initialize();
	}

	private void initialize() {
		getContentPane().add(getDataSeriesSelectionPanel(), BorderLayout.CENTER);
		getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
		
		this.setTitle("Select a Data Series");
		this.setSize(450, 175);
		this.setModal(true);
	}

	/**
	 * Gets the data series selection panel.
	 * @return the data series selection panel
	 */
	private DataSeriesSelectionPanel getDataSeriesSelectionPanel() {
		if (dataSeriesSelectionPanel == null) {
			dataSeriesSelectionPanel = new DataSeriesSelectionPanel();
		}
		return dataSeriesSelectionPanel;
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
			GridBagConstraints gbc_jButtonOk = new GridBagConstraints();
			gbc_jButtonOk.anchor = GridBagConstraints.EAST;
			gbc_jButtonOk.insets = new Insets(10, 0, 10, 15);
			gbc_jButtonOk.gridx = 0;
			gbc_jButtonOk.gridy = 0;
			buttonsPanel.add(getJButtonOk(), gbc_jButtonOk);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.insets = new Insets(10, 15, 10, 0);
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			buttonsPanel.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return buttonsPanel;
	}
	
	/**
	 * Gets the OK button.
	 * @return the OK button
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton("OK");
			jButtonOk.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOk.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonOk.setPreferredSize(new Dimension(85, 26));
			jButtonOk.addActionListener(this);

		}
		return jButtonOk;
	}
	
	/**
	 * Gets the cancel button
	 * @return the cancel button
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(AwbThemeColor.ButtonTextRed.getColor());
			jButtonCancel.setPreferredSize(new Dimension(85, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/**
	 * Gets the selected data source.
	 * @return the selected data source
	 */
	public AbstractDataSource getSelectedDataSource() {
		return this.getDataSeriesSelectionPanel().getSelectedDataSource();
	}
	/**
	 * Sets the selected data source.
	 * @param dataSource the new selected data source
	 */
	public void setSelectedDataSource(AbstractDataSource dataSource) {
		this.getDataSeriesSelectionPanel().setSelectedDataSource(dataSource);
	}
	/**
	 * Sets the selected data source.
	 * @param dataSourceName the new selected data source
	 */
	public void setSelectedDataSource(String dataSourceName) {
		AbstractDataSource dataSource = TimeSeriesDataProvider.getInstance().getDataSource(dataSourceName);
		if (dataSource!=null) {
			this.getDataSeriesSelectionPanel().setSelectedDataSource(dataSource);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Unable to select data source " + dataSourceName + ", not configured in the time series provider!!");
		}
	}
	/**
	 * Gets the selected data series.
	 * @return the selected data series
	 */
	public AbstractDataSeries getSelectedDataSeries() {
		return this.getDataSeriesSelectionPanel().getSelectedDataSeries();
	}
	/**
	 * Sets the selected data series.
	 * @param dataSeries the new selected data series
	 */
	public void setSelectedDataSeries(AbstractDataSeries dataSeries) {
		this.getDataSeriesSelectionPanel().setSelectedDataSeries(dataSeries);
	}
	
	/**
	 * Sets the selected data series.
	 * @param dataSeriesName the data series name
	 */
	public void setSelectedDataSeries(String dataSeriesName) {
		if (this.getSelectedDataSource()!=null) {
			AbstractDataSeries dataSeries = this.getSelectedDataSource().getDataSeries(dataSeriesName);
			if (dataSeries!=null) {
				this.setSelectedDataSeries(dataSeries);
			} else {
				System.err.println("[" + this.getClass().getSimpleName() + "] Unable to select data series " + dataSeriesName + ", not available in the current data source " + this.getSelectedDataSource().getName());
			}
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Unable to select data series, no data source selected!");
		}
	}
	
	/**
	 * Checks if the dialog was canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonOk()) {
			this.canceled = false;
			this.setVisible(false);
		} else if (ae.getSource()==this.getJButtonCancel()) {
			this.canceled = true;
			this.setVisible(false);
		}
	}
}
