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
package agentgui.core.charts.timeseriesChart.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import de.enflexit.common.ontology.gui.DynForm;
import de.enflexit.common.ontology.gui.OntologyClassWidget;
import de.enflexit.language.Language;

/**
 * The Class TimeSeriesWidget.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeSeriesWidget extends OntologyClassWidget implements ActionListener {

	private static final long serialVersionUID = -6165412456864146609L;
	
	// --- The dimension of the thumbnail that is displayed in the widget
	private static final int THUMBNAIL_WIDTH = 260;
	private static final int THUMBNAIL_HEIGHT = 175;
	
	private TimeSeriesChartEditorJDialog tscejd = null;
	
	private JButton jButtonEdit = null;

	
	/**
	 * Instantiates a new time series widget.
	 *
	 * @param dynForm the {@link DynForm}
	 * @param startArgIndex the current start argument index
	 */
	public TimeSeriesWidget(DynForm dynForm, int startArgIndex) {
		super(dynForm, startArgIndex);
		initialize();
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
        
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        
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
			getJButtonEdit().setText("");
			getJButtonEdit().setIcon(new ImageIcon(this.getChartThumb()));
		}
		return jButtonEdit;
	}

	/**
	 * @return the tscejd
	 */
	public TimeSeriesChartEditorJDialog getTimeSeriesChartEditorJDialog() {
		if(tscejd == null){
			tscejd = new TimeSeriesChartEditorJDialog(this.getDynForm(), this.getArgumentIndex());
		}
		return tscejd;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.timeseriesChart.gui.OntologyClassWidget#setOntologyClassInstance(java.lang.Object)
	 */
	@Override
	public void setOntologyClassInstance(Object objectInstance) {
//		this.currChart = (TimeSeriesChart) objectInstance;
		this.getTimeSeriesChartEditorJDialog().setOntologyClassInstance(objectInstance);
		ImageIcon icon = new ImageIcon(this.getChartThumb());
		if(icon != null){
			// Replace text by thumbnail if available
			this.getJButtonEdit().setText("");
			this.getJButtonEdit().setIcon(icon);
		}
	}
	/* (non-Javadoc)
	 * @see agentgui.core.charts.timeseriesChart.gui.OntologyClassWidget#getOntologyClassInstance()
	 */
	@Override
	public Object getOntologyClassInstance() {
//		return this.currChart;
		return this.getTimeSeriesChartEditorJDialog().getOntologyClassInstance();
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object source = ae.getSource();
		if (source==jButtonEdit) {
			
			this.getTimeSeriesChartEditorJDialog().setVisible(true);
			// --- wait until the modal dialog is closed --
			this.setOntologyClassInstance(this.getTimeSeriesChartEditorJDialog().getOntologyClassInstance());
			
		}
	}
	
	private BufferedImage getChartThumb(){
		return this.getTimeSeriesChartEditorJDialog().getContentPane().exportChartAsImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
