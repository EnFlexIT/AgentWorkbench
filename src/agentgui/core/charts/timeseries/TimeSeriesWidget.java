/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.charts.timeseries;

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
import agentgui.core.ontologies.gui.DynForm;
import agentgui.ontology.TimeSeries;

/**
 * The Class TimeSeriesWidget.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeSeriesWidget extends JPanel implements ActionListener {

	private static final long serialVersionUID = -6165412456864146609L;
	
	private DynForm dynForm = null;  //  @jve:decl-index=0:
	private int startArgIndex = -1;
	
	private TimeSeries currTimeSeries = null;  //  @jve:decl-index=0:
	private TimeSeriesChartDialog tscd = null;
	
	private JButton jButtonEdit = null;

	
	/**
	 * Instantiates a new time series widget.
	 *
	 * @param dynForm the {@link DynForm}
	 * @param startArgIndex the current start argument index
	 */
	public TimeSeriesWidget(DynForm dynForm, int startArgIndex) {
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
	 * Gets the time series chart dialog.
	 * @return the time series chart dialog
	 */
	private TimeSeriesChartDialog getTimeSeriesChartDialog() {
		if (this.tscd==null) {
			this.tscd = new TimeSeriesChartDialog(SwingUtilities.getWindowAncestor(this), this.currTimeSeries);
		}
		return this.tscd;
	}
	
	/**
	 * Sets the time series.
	 * @param timeSeries the new time series
	 */
	public void setTimeSeries(TimeSeries timeSeries) {
		this.currTimeSeries = timeSeries;
		ImageIcon icon = new ImageIcon(this.getTimeSeriesChartDialog().getChartThumb());
		if(icon != null){
			// Replace text by thumbnail if available
			this.getJButtonEdit().setText("");
			this.getJButtonEdit().setIcon(icon);
		}
	}
	
	/**
	 * Gets the time series.
	 * @return the time series
	 */
	public TimeSeries getTimeSeries() {
		return currTimeSeries;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object source = ae.getSource();
		if (source==jButtonEdit) {
			
			this.dynForm.save(true);
			Object[] startArgs = this.dynForm.getOntoArgsInstance();
			this.currTimeSeries = (TimeSeries) startArgs[this.startArgIndex];

			this.getTimeSeriesChartDialog().setVisible(true);
			if(! this.getTimeSeriesChartDialog().isCanceled()){
				startArgs[this.startArgIndex] = this.getTimeSeriesChartDialog().getModel().getOntologyModel();
				this.dynForm.setOntoArgsInstance(startArgs);
				if(this.getTimeSeriesChartDialog().getChartThumb() != null){
					getJButtonEdit().setText("");
					getJButtonEdit().setIcon(new ImageIcon(this.getTimeSeriesChartDialog().getChartThumb()));
				}
			}
		}
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
