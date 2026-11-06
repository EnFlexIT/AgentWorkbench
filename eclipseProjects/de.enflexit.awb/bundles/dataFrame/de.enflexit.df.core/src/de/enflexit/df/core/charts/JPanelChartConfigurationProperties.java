package de.enflexit.df.core.charts;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.ui.PropertiesConfigurationPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;

/**
 * The Class JPanelChartConfigurationProperties.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelChartConfigurationProperties extends JPanel implements PropertiesConfigurationPanel {

	private static final long serialVersionUID = -890032683256656426L;
	
	private DataController dataController;
	private DefaultMutableTreeNode dataTreeNode;
	
	private JButton jButtonNewXYChart;
	private JButton jButtonNewTimeSeriesChart;
	
	
	/**
	 * Instantiates a new j panel chart configuration properties.
	 */
	public JPanelChartConfigurationProperties() {
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jButtonNewXYChart = new GridBagConstraints();
		gbc_jButtonNewXYChart.insets = new Insets(10, 10, 0, 0);
		gbc_jButtonNewXYChart.fill = GridBagConstraints.HORIZONTAL;
		gbc_jButtonNewXYChart.gridx = 0;
		gbc_jButtonNewXYChart.gridy = 0;
		add(getJButtonNewXYChart(), gbc_jButtonNewXYChart);
		GridBagConstraints gbc_jButtonNewTimeSeriesChart = new GridBagConstraints();
		gbc_jButtonNewTimeSeriesChart.insets = new Insets(10, 10, 0, 0);
		gbc_jButtonNewTimeSeriesChart.gridx = 0;
		gbc_jButtonNewTimeSeriesChart.gridy = 1;
		add(getJButtonNewTimeSeriesChart(), gbc_jButtonNewTimeSeriesChart);
	}
	
	private JButton getJButtonNewXYChart() {
		if (jButtonNewXYChart == null) {
			jButtonNewXYChart = new JButton("XY - Chart");
			jButtonNewXYChart.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jButtonNewXYChart;
	}
	private JButton getJButtonNewTimeSeriesChart() {
		if (jButtonNewTimeSeriesChart == null) {
			jButtonNewTimeSeriesChart = new JButton("Time Series - Chart");
			jButtonNewTimeSeriesChart.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jButtonNewTimeSeriesChart;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.PropertiesConfigurationPanel#getPropertiesTitle()
	 */
	@Override
	public String getPropertiesTitle() {
		return "Chart - Configuration";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.PropertiesConfigurationPanel#providesPropertiesConfiguratoin(de.enflexit.df.core.model.DataController, javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	public boolean providesPropertiesConfiguration(DataController dataController, DefaultMutableTreeNode treeNode) {
		
		//System.out.println("Test, Test ... :-) ");
		
		return true;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.ui.PropertiesConfigurationPanel#getPropertiesConfigurationPanel(de.enflexit.df.core.model.DataController, javax.swing.tree.DefaultMutableTreeNode)
	 */
	@Override
	public JComponent getPropertiesConfigurationPanel(DataController dataController, DefaultMutableTreeNode treeNode) {
		
		this.dataController = dataController;
		this.dataTreeNode = treeNode;
		
		
		return this;
	}
	
}
