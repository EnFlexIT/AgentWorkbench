package de.enflexit.df.core.charts;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.charts.model.AbstractChart;
import de.enflexit.charts.model.BarChart;
import de.enflexit.charts.model.LineChart;
import de.enflexit.charts.model.PieChart;
import de.enflexit.charts.model.ScatterPlot;
import de.enflexit.charts.model.TimeSeriesChart;
import de.enflexit.df.core.model.DataController;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;

/**
 * The Class JPanelChartConfigurationBase.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JPanelChartConfigurationBase extends JPanel implements DocumentListener {

	private static final long serialVersionUID = -9203558305257293840L;

	private JPanelChartConfiguration jPanelChartConfiguration;
	private AbstractChart chart;
	private boolean isPauseDocumentListener;
	
	private JLabel jLabelChartID;
	private JLabel jLabelChartID_Value;
	private JLabel jLabelChartTitle;
	private JTextField jTextFieldChartTitle;

	private JCheckBox jCheckBoxShowLegend;
	
	private JLabel jLabelChart_X_Label_1;
	private JTextField jTextFieldChartXLabel_1;
	private JLabel jLabelChart_Y_Label_1;
	private JTextField jTextFieldChartYLabel_1;

	private JSeparator separator;
	
	private JLabel jLabelChart_X_Label_2;
	private JTextField jTextFieldChartXLabel_2;
	private JLabel jLabelChart_Y_Label_2;
	private JTextField jTextFieldChartYLabel_2;

	
	/**
	 * Instantiates a new j panel chart configuration base.
	 * @param jPanelChartConfiguration the parent JPanelChartConfiguration
	 */
	public JPanelChartConfigurationBase(JPanelChartConfiguration jPanelChartConfiguration) {
		this.jPanelChartConfiguration = jPanelChartConfiguration;
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelChartID = new GridBagConstraints();
		gbc_jLabelChartID.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelChartID.anchor = GridBagConstraints.WEST;
		gbc_jLabelChartID.gridx = 0;
		gbc_jLabelChartID.gridy = 0;
		add(getJLabelChartID(), gbc_jLabelChartID);
		GridBagConstraints gbc_jLabelChartID_Value = new GridBagConstraints();
		gbc_jLabelChartID_Value.insets = new Insets(5, 7, 0, 5);
		gbc_jLabelChartID_Value.anchor = GridBagConstraints.WEST;
		gbc_jLabelChartID_Value.gridx = 1;
		gbc_jLabelChartID_Value.gridy = 0;
		add(getJLabelChartID_Value(), gbc_jLabelChartID_Value);
		GridBagConstraints gbc_jLabelChartTitle = new GridBagConstraints();
		gbc_jLabelChartTitle.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelChartTitle.anchor = GridBagConstraints.WEST;
		gbc_jLabelChartTitle.gridx = 0;
		gbc_jLabelChartTitle.gridy = 1;
		add(getJLabelChartTitle(), gbc_jLabelChartTitle);
		GridBagConstraints gbc_jTextFieldChartTitle = new GridBagConstraints();
		gbc_jTextFieldChartTitle.insets = new Insets(5, 5, 0, 5);
		gbc_jTextFieldChartTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldChartTitle.gridx = 1;
		gbc_jTextFieldChartTitle.gridy = 1;
		add(getJTextFieldChartTitle(), gbc_jTextFieldChartTitle);
		GridBagConstraints gbc_jCheckBoxShowLegend = new GridBagConstraints();
		gbc_jCheckBoxShowLegend.insets = new Insets(5, 5, 0, 0);
		gbc_jCheckBoxShowLegend.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxShowLegend.gridx = 1;
		gbc_jCheckBoxShowLegend.gridy = 2;
		add(getJCheckBoxShowLegend(), gbc_jCheckBoxShowLegend);
		GridBagConstraints gbc_jLabelChart_X_Label_1 = new GridBagConstraints();
		gbc_jLabelChart_X_Label_1.anchor = GridBagConstraints.WEST;
		gbc_jLabelChart_X_Label_1.insets = new Insets(10, 5, 0, 0);
		gbc_jLabelChart_X_Label_1.gridx = 0;
		gbc_jLabelChart_X_Label_1.gridy = 3;
		add(getJLabelChart_X_Label_1(), gbc_jLabelChart_X_Label_1);
		GridBagConstraints gbc_jTextFieldChartXLabel_1 = new GridBagConstraints();
		gbc_jTextFieldChartXLabel_1.insets = new Insets(10, 5, 0, 5);
		gbc_jTextFieldChartXLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldChartXLabel_1.gridx = 1;
		gbc_jTextFieldChartXLabel_1.gridy = 3;
		add(getJTextFieldChartXLabel_1(), gbc_jTextFieldChartXLabel_1);
		GridBagConstraints gbc_jLabelChart_Y_Label_1 = new GridBagConstraints();
		gbc_jLabelChart_Y_Label_1.anchor = GridBagConstraints.WEST;
		gbc_jLabelChart_Y_Label_1.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelChart_Y_Label_1.gridx = 0;
		gbc_jLabelChart_Y_Label_1.gridy = 4;
		add(getJLabelChart_Y_Label_1(), gbc_jLabelChart_Y_Label_1);
		GridBagConstraints gbc_jTextFieldChartYLabel_1 = new GridBagConstraints();
		gbc_jTextFieldChartYLabel_1.insets = new Insets(5, 5, 0, 5);
		gbc_jTextFieldChartYLabel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldChartYLabel_1.gridx = 1;
		gbc_jTextFieldChartYLabel_1.gridy = 4;
		add(getJTextFieldChartYLabel_1(), gbc_jTextFieldChartYLabel_1);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(10, 5, 0, 5);
		gbc_separator.gridwidth = 2;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 5;
		add(getSeparator(), gbc_separator);
		GridBagConstraints gbc_jLabelChart_X_Label_2 = new GridBagConstraints();
		gbc_jLabelChart_X_Label_2.anchor = GridBagConstraints.WEST;
		gbc_jLabelChart_X_Label_2.insets = new Insets(10, 5, 0, 0);
		gbc_jLabelChart_X_Label_2.gridx = 0;
		gbc_jLabelChart_X_Label_2.gridy = 6;
		add(getJLabelChart_X_Label_2(), gbc_jLabelChart_X_Label_2);
		GridBagConstraints gbc_jTextFieldChartXLabel_2 = new GridBagConstraints();
		gbc_jTextFieldChartXLabel_2.insets = new Insets(10, 5, 0, 5);
		gbc_jTextFieldChartXLabel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldChartXLabel_2.gridx = 1;
		gbc_jTextFieldChartXLabel_2.gridy = 6;
		add(getJTextFieldChartXLabel_2(), gbc_jTextFieldChartXLabel_2);
		GridBagConstraints gbc_jLabelChart_Y_Label_2 = new GridBagConstraints();
		gbc_jLabelChart_Y_Label_2.anchor = GridBagConstraints.WEST;
		gbc_jLabelChart_Y_Label_2.insets = new Insets(5, 5, 0, 0);
		gbc_jLabelChart_Y_Label_2.gridx = 0;
		gbc_jLabelChart_Y_Label_2.gridy = 7;
		add(getJLabelChart_Y_Label_2(), gbc_jLabelChart_Y_Label_2);
		GridBagConstraints gbc_jTextFieldChartYLabel_2 = new GridBagConstraints();
		gbc_jTextFieldChartYLabel_2.insets = new Insets(5, 5, 0, 5);
		gbc_jTextFieldChartYLabel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldChartYLabel_2.gridx = 1;
		gbc_jTextFieldChartYLabel_2.gridy = 7;
		add(getJTextFieldChartYLabel_2(), gbc_jTextFieldChartYLabel_2);
	}
	private JLabel getJLabelChartID() {
		if (jLabelChartID == null) {
			jLabelChartID = new JLabel("ID:");
			jLabelChartID.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelChartID;
	}
	private JLabel getJLabelChartID_Value() {
		if (jLabelChartID_Value == null) {
			jLabelChartID_Value = new JLabel("");
			jLabelChartID_Value.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelChartID_Value;
	}
	private JLabel getJLabelChartTitle() {
		if (jLabelChartTitle == null) {
			jLabelChartTitle = new JLabel("Chart Title:");
			jLabelChartTitle.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelChartTitle;
	}
	private JTextField getJTextFieldChartTitle() {
		if (jTextFieldChartTitle == null) {
			jTextFieldChartTitle = new JTextField();
			jTextFieldChartTitle.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldChartTitle.getDocument().addDocumentListener(this);
		}
		return jTextFieldChartTitle;
	}
	
	private JCheckBox getJCheckBoxShowLegend() {
		if (jCheckBoxShowLegend == null) {
			jCheckBoxShowLegend = new JCheckBox("Show Legend");
			jCheckBoxShowLegend.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxShowLegend.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JPanelChartConfigurationBase.this.chart.setShowLegend(JPanelChartConfigurationBase.this.getJCheckBoxShowLegend().isSelected());
					JPanelChartConfigurationBase.this.informBaseConfigurationChanged();
				}
			});
		}
		return jCheckBoxShowLegend;
	}
	
	private JLabel getJLabelChart_X_Label_1() {
		if (jLabelChart_X_Label_1 == null) {
			jLabelChart_X_Label_1 = new JLabel("First x-Axis Label:");
			jLabelChart_X_Label_1.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelChart_X_Label_1;
	}
	private JTextField getJTextFieldChartXLabel_1() {
		if (jTextFieldChartXLabel_1 == null) {
			jTextFieldChartXLabel_1 = new JTextField();
			jTextFieldChartXLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldChartXLabel_1.getDocument().addDocumentListener(this);
		}
		return jTextFieldChartXLabel_1;
	}
	private JLabel getJLabelChart_Y_Label_1() {
		if (jLabelChart_Y_Label_1 == null) {
			jLabelChart_Y_Label_1 = new JLabel("First y-Axis Label:");
			jLabelChart_Y_Label_1.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelChart_Y_Label_1;
	}
	private JTextField getJTextFieldChartYLabel_1() {
		if (jTextFieldChartYLabel_1 == null) {
			jTextFieldChartYLabel_1 = new JTextField();
			jTextFieldChartYLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldChartYLabel_1.getDocument().addDocumentListener(this);
		}
		return jTextFieldChartYLabel_1;
	}
	
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}
	
	private JLabel getJLabelChart_X_Label_2() {
		if (jLabelChart_X_Label_2 == null) {
			jLabelChart_X_Label_2 = new JLabel("2nd x-Axis Label:");
			jLabelChart_X_Label_2.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelChart_X_Label_2;
	}
	private JTextField getJTextFieldChartXLabel_2() {
		if (jTextFieldChartXLabel_2 == null) {
			jTextFieldChartXLabel_2 = new JTextField();
			jTextFieldChartXLabel_2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldChartXLabel_2.getDocument().addDocumentListener(this);
		}
		return jTextFieldChartXLabel_2;
	}
	private JLabel getJLabelChart_Y_Label_2() {
		if (jLabelChart_Y_Label_2 == null) {
			jLabelChart_Y_Label_2 = new JLabel("2nd y-Axis Label:");
			jLabelChart_Y_Label_2.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelChart_Y_Label_2;
	}
	private JTextField getJTextFieldChartYLabel_2() {
		if (jTextFieldChartYLabel_2 == null) {
			jTextFieldChartYLabel_2 = new JTextField();
			jTextFieldChartYLabel_2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldChartYLabel_2.getDocument().addDocumentListener(this);
		}
		return jTextFieldChartYLabel_2;
	}
	
	/**
	 * Sets the chart to the UI.
	 * @param chart the new chart
	 */
	public void setChart(AbstractChart chart) {
		
		this.chart = chart;
		
		boolean isVisibleXAxis2 = false;
		boolean isVisibleYAxis2 = false;
		
		this.isPauseDocumentListener = true;
		if (chart==null) {
			this.getJLabelChartID_Value().setText("");
			this.getJTextFieldChartTitle().setText("");
			this.getJCheckBoxShowLegend().setSelected(false);
			this.getJTextFieldChartXLabel_1().setText("");
			this.getJTextFieldChartYLabel_1().setText("");
			
			this.getJTextFieldChartXLabel_2().setText("");
			this.getJTextFieldChartYLabel_2().setText("");
			
		} else {
			this.getJLabelChartID_Value().setText(chart.getChartId() + "");
			this.getJTextFieldChartTitle().setText(chart.getTitle());
			this.getJCheckBoxShowLegend().setSelected(chart.getShowLegend());
			this.getJTextFieldChartXLabel_1().setText(chart.getxAxisLabel());
			this.getJTextFieldChartYLabel_1().setText(chart.getyAxisLabel());
			
			if (chart instanceof BarChart barChart) {
				this.getJTextFieldChartYLabel_2().setText(barChart.getSecondaryYAxisLabel());
				isVisibleYAxis2 = true;
			} else if (chart instanceof PieChart pieChart) {
				pieChart.getChartId(); // --- just a placeholder ---
			} else if (chart instanceof ScatterPlot scatterPlot) {
				this.getJTextFieldChartYLabel_2().setText(scatterPlot.getSecondaryYAxisLabel());
				isVisibleYAxis2 = true;
			} else if (chart instanceof TimeSeriesChart tsChart) {
				this.getJTextFieldChartYLabel_2().setText(tsChart.getSecondaryYAxisLabel());
				isVisibleYAxis2 = true;
			} else if (chart instanceof LineChart lineChart) {
				this.getJTextFieldChartYLabel_2().setText(lineChart.getSecondaryYAxisLabel());
				isVisibleYAxis2 = true;
			}
		}
		this.isPauseDocumentListener = false;
		
		
		this.getJTextFieldChartTitle().setEnabled(chart!=null);;
		this.getJCheckBoxShowLegend().setEnabled(chart!=null);
		this.getJTextFieldChartXLabel_1().setEnabled(chart!=null);
		this.getJTextFieldChartYLabel_1().setEnabled(chart!=null);
		this.getJTextFieldChartXLabel_2().setEnabled(chart!=null);
		this.getJTextFieldChartYLabel_2().setEnabled(chart!=null);
		
		boolean isVisibleSeparator = isVisibleXAxis2==true || isVisibleYAxis2==true;
		this.getSeparator().setVisible(isVisibleSeparator);
		this.getJLabelChart_X_Label_2().setVisible(isVisibleXAxis2);
		this.getJTextFieldChartXLabel_2().setVisible(isVisibleXAxis2);
		this.getJLabelChart_Y_Label_2().setVisible(isVisibleYAxis2);
		this.getJTextFieldChartYLabel_2().setVisible(isVisibleYAxis2);
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent de) {
		this.onDocumentEvent(de);		
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent de) {
		this.onDocumentEvent(de);		
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent de) {
		this.onDocumentEvent(de);
	}
	/**
	 * On document event.
	 * @param de the DocumentEvent
	 */
	private void onDocumentEvent(DocumentEvent de) {
		
		if (this.isPauseDocumentListener==true) return;
		
		if (de.getDocument()==this.getJTextFieldChartTitle().getDocument()) {
			this.chart.setTitle(this.getJTextFieldChartTitle().getText());
			this.informBaseConfigurationChanged();
			this.informBaseConfigurationChanged();
		} else if (de.getDocument()==this.getJTextFieldChartXLabel_1().getDocument()) {
			this.chart.setxAxisLabel(this.getJTextFieldChartXLabel_1().getText());
			this.informBaseConfigurationChanged();
			
		} else if (de.getDocument()==this.getJTextFieldChartYLabel_1().getDocument()) {
			this.chart.setyAxisLabel(this.getJTextFieldChartYLabel_1().getText());
			this.informBaseConfigurationChanged();
			
		} else if (de.getDocument()==this.getJTextFieldChartXLabel_2().getDocument()) {
			// --- TODO: Open issue so far ----------------------
			
		} else if (de.getDocument()==this.getJTextFieldChartYLabel_2().getDocument()) {
			if (this.chart instanceof BarChart barChart) {
				barChart.setSecondaryYAxisLabel(this.getJTextFieldChartYLabel_2().getText());
			} else if (this.chart instanceof PieChart pieChart) {
				pieChart.getChartId(); // --- just a placeholder ---
			} else if (this.chart instanceof ScatterPlot scatterPlot) {
				scatterPlot.setSecondaryYAxisLabel(this.getJTextFieldChartYLabel_2().getText());
			} else if (this.chart instanceof TimeSeriesChart tsChart) {
				tsChart.setSecondaryYAxisLabel(this.getJTextFieldChartYLabel_2().getText());
			} else if (this.chart instanceof LineChart lineChart) {
				lineChart.setSecondaryYAxisLabel(this.getJTextFieldChartYLabel_2().getText());
			}
			this.informBaseConfigurationChanged();
			
		}
	}
	
	/**
	 * Inform base configuration changed.
	 */
	private void informBaseConfigurationChanged() {
		this.jPanelChartConfiguration.informAboutChartEdit(DataController.DC_CHART_EDITED_TITLE_OR_LABEL, null, this.chart);
	}
}
