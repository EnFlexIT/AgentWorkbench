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
package org.awb.env.networkModel.controller.ui.timeModel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agentgui.core.application.Language;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;
import agentgui.simulationService.time.JPanel4TimeModelConfiguration;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelStroke;

/**
 * The Class JPanelTimeModelStroke.
 * 
 * @see TimeModelStroke
 * @see TimeModelController
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelTimeModelStroke extends JPanel4TimeModelConfiguration implements DocumentListener {

	private static final long serialVersionUID = -1170433671816358910L;
	
	private JLabel jLabelCounterStop;
	private JLabel jLabelCounterStart;
	private JTextField jTextFieldCounterStart;
	private JTextField jTextFieldCounterStop;
	
	private boolean enabledChangeListener = true;
	
	
	/**
	 * Instantiates a new time model stroke configuration.
	 *
	 * @param project the project
	 * @param timeModelController the time model controller
	 */
	public JPanelTimeModelStroke(Project project, TimeModelController timeModelController) {
		super(project, timeModelController);
		this.initialize();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(900, 65);
	}
	/**
	 * Initializes this JPanel.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[] { 0, 0, 0};
		gridBagLayout.columnWidths = new int[] { 0, 0, 0};
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		this.setSize(244, 60);
		
		GridBagConstraints gbcJLabelCounterStart = new GridBagConstraints();
		gbcJLabelCounterStart.gridx = 0;
		gbcJLabelCounterStart.gridy = 0;
		gbcJLabelCounterStart.insets = new Insets(5, 5, 0, 0);
		gbcJLabelCounterStart.anchor = GridBagConstraints.WEST;

		GridBagConstraints gbcJTextFieldCounterStart = new GridBagConstraints();
		gbcJTextFieldCounterStart.anchor = GridBagConstraints.WEST;
		gbcJTextFieldCounterStart.gridx = 1;
		gbcJTextFieldCounterStart.gridy = 0;
		gbcJTextFieldCounterStart.weightx = 1.0;
		gbcJTextFieldCounterStart.insets = new Insets(5, 5, 0, 0);
		
		GridBagConstraints gbcJLabelCounterStop = new GridBagConstraints();
		gbcJLabelCounterStop.gridx = 0;
		gbcJLabelCounterStop.gridy = 1;
		gbcJLabelCounterStop.insets = new Insets(3, 5, 0, 0);
		gbcJLabelCounterStop.anchor = GridBagConstraints.WEST;
		
		GridBagConstraints gbcJTextFiledCounterStop = new GridBagConstraints();
		gbcJTextFiledCounterStop.anchor = GridBagConstraints.WEST;
		gbcJTextFiledCounterStop.gridx = 1;		
		gbcJTextFiledCounterStop.gridy = 1;
		gbcJTextFiledCounterStop.weightx = 1.0;
		gbcJTextFiledCounterStop.insets = new Insets(3, 5, 0, 0);
		jLabelCounterStart = new JLabel();
		jLabelCounterStart.setText("Zähler Start:");
		jLabelCounterStart.setText(Language.translate(jLabelCounterStart.getText()));
		jLabelCounterStart.setFont(new Font("Dialog", Font.BOLD, 11));
		jLabelCounterStop = new JLabel();
		jLabelCounterStop.setText("Zähler Stop:");
		jLabelCounterStop.setText(Language.translate(jLabelCounterStop.getText()));
		jLabelCounterStop.setFont(new Font("Dialog", Font.BOLD, 11));
		this.add(jLabelCounterStart, gbcJLabelCounterStart);
        this.add(getJTextFieldCounterStart(), gbcJTextFieldCounterStart);
        this.add(jLabelCounterStop, gbcJLabelCounterStop);
        this.add(getJTextFieldCounterStop(), gbcJTextFiledCounterStop);
        			
	}

	/**
	 * This method initializes jTextFieldCounterStart1	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCounterStart() {
		if (jTextFieldCounterStart == null) {
			jTextFieldCounterStart = new JTextField();
			jTextFieldCounterStart.setFont(new Font("Dialog", Font.PLAIN, 11));
			jTextFieldCounterStart.setPreferredSize(new Dimension(120, 24));
			jTextFieldCounterStart.setBorder(BorderFactory.createEtchedBorder());
			jTextFieldCounterStart.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String singleChar = Character.toString(charackter);
					if (singleChar.matches("[0-9]") == false) {
						kT.consume();	
						return;
					}
				 }				 
			});
			jTextFieldCounterStart.getDocument().addDocumentListener(this);
		}
		return jTextFieldCounterStart;
	}

	/**
	 * This method initializes jTextFieldCounterStop	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCounterStop() {
		if (jTextFieldCounterStop == null) {
			jTextFieldCounterStop = new JTextField();
			jTextFieldCounterStop.setFont(new Font("Dialog", Font.PLAIN, 11));
			jTextFieldCounterStop.setPreferredSize(new Dimension(120, 24));
			jTextFieldCounterStop.setBorder(BorderFactory.createEtchedBorder());
			jTextFieldCounterStop.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String singleChar = Character.toString(charackter);
					if (singleChar.matches("[0-9]") == false) {
						kT.consume();	
						return;
					}
				 }				 
			});
			jTextFieldCounterStop.getDocument().addDocumentListener(this);
		}
		return jTextFieldCounterStop;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		TimeModelStroke tms = null;
		if (timeModel==null) {
			tms = new TimeModelStroke();
		} else {
			tms = (TimeModelStroke) timeModel;
		}
		this.enabledChangeListener = false;
		this.getJTextFieldCounterStart().setText(((Integer)tms.getCounterStart()).toString());
		this.getJTextFieldCounterStop().setText(((Integer)tms.getCounterStop()).toString());
		this.enabledChangeListener = true;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {
		
		int counterStart = 0;
		int counterStop = 0;
		
		String counterStartString = this.getJTextFieldCounterStart().getText();
		String counterStopString  = this.getJTextFieldCounterStop().getText();
		
		if (counterStartString==null || counterStartString.length()==0) {
			counterStart = 0;	
		} else {
			counterStart = Integer.parseInt(counterStartString);
		}
		if (counterStopString==null || counterStopString.length()==0) {
			counterStop = 0;	
		} else {
			counterStop = Integer.parseInt(counterStopString);
		}

		// --- Prepare return value ----------------------- 
		TimeModelStroke tms = null;
		if (this.getTimeModelController().getTimeModel() instanceof TimeModelStroke) {
			tms = (TimeModelStroke) this.getTimeModelController().getTimeModel();
		} else {
			tms = new TimeModelStroke();
		}
		tms.setCounterStart(counterStart);
		tms.setCounterStop(counterStop);
		return tms;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		this.saveTimeModelStrokeToSimulationSetup();
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		this.saveTimeModelStrokeToSimulationSetup();
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		this.saveTimeModelStrokeToSimulationSetup();
	}
	
	/**
	 * Saves the current {@link TimeModelStroke} to the simulation setup.
	 */
	private void saveTimeModelStrokeToSimulationSetup() {
		if (this.enabledChangeListener==true) {
			this.saveTimeModelToSimulationSetup();
		}
	}
	
}  
