package de.enflexit.awb.timeSeriesDataProvider.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;
import de.enflexit.common.swing.AwbThemeImageIcon;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This panel is shown by the TimeSeriesProvider configuration UI if no data source is currently selected. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class NoDataSourceSelectedPanel extends AbstractDataSourceConfigurationPanel implements ActionListener {
	
	private static final long serialVersionUID = 5052213639263638494L;
	
	private TimeSeriesDataProviderConfigurationPanel parentPanel;
	
	private JLabel jLabelCaption;
	private JLabel jLabelExplanation;
	private JButton jButtonNewCsvDataSource;
	private JButton jButtonNewJdbcDataSource;
	
	private Dimension buttonSize = new Dimension(400, 26);
	
	/**
	 * Instantiates a new no data source selected panel.
	 * @param parentPanel the parent panel
	 */
	public NoDataSourceSelectedPanel(TimeSeriesDataProviderConfigurationPanel parentPanel) {
		this.parentPanel = parentPanel;
		initialize();
	}
	
	/**
	 * Initializes the GUI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelCaption = new GridBagConstraints();
		gbc_jLabelCaption.anchor = GridBagConstraints.SOUTH;
		gbc_jLabelCaption.insets = new Insets(0, 0, 5, 0);
		gbc_jLabelCaption.gridx = 0;
		gbc_jLabelCaption.gridy = 0;
		add(getJLabelCaption(), gbc_jLabelCaption);
		GridBagConstraints gbc_jLabelExplanation = new GridBagConstraints();
		gbc_jLabelExplanation.insets = new Insets(5, 0, 5, 0);
		gbc_jLabelExplanation.gridx = 0;
		gbc_jLabelExplanation.gridy = 1;
		add(getJLabelExplanation(), gbc_jLabelExplanation);
		GridBagConstraints gbc_jButtonNewCsvDataSource = new GridBagConstraints();
		gbc_jButtonNewCsvDataSource.insets = new Insets(5, 0, 5, 0);
		gbc_jButtonNewCsvDataSource.gridx = 0;
		gbc_jButtonNewCsvDataSource.gridy = 2;
		add(getJButtonNewCsvDataSource(), gbc_jButtonNewCsvDataSource);
		GridBagConstraints gbc_jButtonNewJdbcDataSource = new GridBagConstraints();
		gbc_jButtonNewJdbcDataSource.insets = new Insets(5, 0, 0, 0);
		gbc_jButtonNewJdbcDataSource.anchor = GridBagConstraints.NORTH;
		gbc_jButtonNewJdbcDataSource.gridx = 0;
		gbc_jButtonNewJdbcDataSource.gridy = 3;
		add(getJButtonNewJdbcDataSource(), gbc_jButtonNewJdbcDataSource);
		
	}

	private JLabel getJLabelCaption() {
		if (jLabelCaption == null) {
			jLabelCaption = new JLabel("No Data Source Selected");
			jLabelCaption.setFont(new Font("Dialog", Font.BOLD, 18));
		}
		return jLabelCaption;
	}
	private JLabel getJLabelExplanation() {
		if (jLabelExplanation == null) {
			jLabelExplanation = new JLabel("Please select a data source from the list on the left, or create a new one!");
			jLabelExplanation.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelExplanation;
	}
	private JButton getJButtonNewCsvDataSource() {
		if (jButtonNewCsvDataSource == null) {
			jButtonNewCsvDataSource = new JButton("Create a new data source based on a CSV file");
			jButtonNewCsvDataSource.setIcon(this.getThemedIcon(TimeSeriesDataProviderConfigurationPanel.ICON_PATH_NEW_CSV_SOURCE_LIGHT_MODE, TimeSeriesDataProviderConfigurationPanel.ICON_PATH_NEW_CSV_SOURCE_DARK_MODE));
			jButtonNewCsvDataSource.setSize(buttonSize);
			jButtonNewCsvDataSource.setPreferredSize(buttonSize);
			jButtonNewCsvDataSource.addActionListener(this);
		}
		return jButtonNewCsvDataSource;
	}
	private JButton getJButtonNewJdbcDataSource() {
		if (jButtonNewJdbcDataSource == null) {
			jButtonNewJdbcDataSource = new JButton("Create a new data source based on a database table");
			jButtonNewJdbcDataSource.setIcon(this.getThemedIcon(TimeSeriesDataProviderConfigurationPanel.ICON_PATH_NEW_DB_SOURCE_LIGHT_MODE, TimeSeriesDataProviderConfigurationPanel.ICON_PATH_NEW_DB_SOURCE_DARK_MODE));
			jButtonNewJdbcDataSource.setSize(buttonSize);
			jButtonNewJdbcDataSource.setPreferredSize(buttonSize);
			jButtonNewJdbcDataSource.addActionListener(this);
		}
		return jButtonNewJdbcDataSource;
	}
	/**
	 * Creates a themed icon, using the provided URLs for light and dark mode images
	 * @param lightModeImageURL the light mode image URL
	 * @param darkModeImageURL the dark mode image URL
	 * @return the themed icon
	 */
	private AwbThemeImageIcon getThemedIcon(String lightModeImageURL, String darkModeImageURL) {
		ImageIcon lightModeIcon = new ImageIcon(this.getClass().getResource(lightModeImageURL)); 
		ImageIcon darkModeIcon = new ImageIcon(this.getClass().getResource(darkModeImageURL));
		return new AwbThemeImageIcon(lightModeIcon, darkModeIcon);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// --- Delegate creating new data sources  to the parent panel
		if (ae.getSource()==this.getJButtonNewCsvDataSource()) {
			this.parentPanel.addNewCsvDataSource();
		} else if (ae.getSource()==this.getJButtonNewJdbcDataSource()) {
			this.parentPanel.addNewJdbcDataSource();
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#getDataSourceConfiguraiton()
	 */
	@Override
	public AbstractDataSourceConfiguration getDataSourceConfiguration() {
		// Not required
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#setDataSourceConfiguration(de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration)
	 */
	@Override
	public void setDataSourceConfiguration(AbstractDataSourceConfiguration sourceConfiguration) {
		// Not required
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.gui.AbstractDataSourceConfigurationPanel#setDataSeriesConfiguration(de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration)
	 */
	@Override
	public void setDataSeriesConfiguration(AbstractDataSeriesConfiguration seriesConfiguraiton) {
		// Not required
	}
}
