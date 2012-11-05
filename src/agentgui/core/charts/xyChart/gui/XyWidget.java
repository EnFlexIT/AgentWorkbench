package agentgui.core.charts.xyChart.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Language;
import agentgui.core.charts.xyChart.XyOntologyModel;
import agentgui.core.ontologies.gui.DynForm;
import agentgui.ontology.Chart;
import agentgui.ontology.XyChart;

public class XyWidget extends JPanel implements ActionListener {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -9148282428244484633L;
	
	private DynForm dynForm = null;  //  @jve:decl-index=0:
	private int startArgIndex = -1;
	
	private XyChart currChart = null;  //  @jve:decl-index=0:
	private agentgui.core.charts.xyChart.gui.XyChartDialog xycd = null;
	
	private JButton jButtonEdit = null;
	
	/**
	 * Instantiates a new xy widget.
	 *
	 * @param dynForm the {@link DynForm}
	 * @param startArgIndex the current start argument index
	 */
	public XyWidget(DynForm dynForm, int startArgIndex) {
		super();
		this.dynForm = dynForm;
		this.startArgIndex = startArgIndex;
		initialize();
	}
	
	/**
	 * This method initializes this.
	 */
	private void initialize() {
        
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        
        this.setSize(new Dimension(500, 250));
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        this.setLayout(new GridBagLayout());
        this.add(getJButtonEdit(), gridBagConstraints);
			
	}

	/**
	 * This method initializes jButtonEdit.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonEdit() {
		if (jButtonEdit == null) {
			jButtonEdit = new JButton();
			jButtonEdit.setText("Bearbeiten");
			jButtonEdit.setText(Language.translate(jButtonEdit.getText()));
			jButtonEdit.setToolTipText(Language.translate("Daten bearbeiten"));
			jButtonEdit.addActionListener(this);
		}
		return jButtonEdit;
	}
	
	/**
	 * Gets the xy chart dialog.
	 * @return the xy chart dialog
	 */
	private agentgui.core.charts.xyChart.gui.XyChartDialog getXyChartDialog() {
		if (this.xycd==null) {
			this.xycd = new agentgui.core.charts.xyChart.gui.XyChartDialog(SwingUtilities.getWindowAncestor(this), this.currChart);
		}
		return this.xycd;
	}
	
	/**
	 * Sets the xy series.
	 * @param chart the new Chart
	 */
	public void setChart(XyChart chart) {
		this.currChart = chart;
		ImageIcon icon = new ImageIcon(this.getXyChartDialog().getChartThumb());
		if(icon != null){
			// Replace text by thumbnail if available
			this.getJButtonEdit().setText("");
			this.getJButtonEdit().setIcon(icon);
		}
	}
	
	/**
	 * Gets the chart.
	 * @return the chart
	 */
	public Chart getChart() {
		return this.currChart;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source==jButtonEdit) {
			
			this.dynForm.save(true);
			Object[] startArgs = this.dynForm.getOntoArgsInstance();
			this.currChart = (XyChart) startArgs[this.startArgIndex];

			this.getXyChartDialog().setVisible(true);
			if(! this.getXyChartDialog().isCanceled()){
				startArgs[this.startArgIndex] = ((XyOntologyModel)this.getXyChartDialog().getModel().getOntologyModel()).getXyChart();
				this.dynForm.setOntoArgsInstance(startArgs);
				if(this.getXyChartDialog().getChartThumb() != null){
					getJButtonEdit().setText("");
					getJButtonEdit().setIcon(new ImageIcon(this.getXyChartDialog().getChartThumb()));
				}
			}
		}

	}

}