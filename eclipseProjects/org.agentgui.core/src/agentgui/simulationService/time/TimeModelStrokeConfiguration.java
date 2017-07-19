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
package agentgui.simulationService.time;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agentgui.core.application.Language;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;

/**
 * The Class TimeModelStrokeConfiguration.
 * 
 * @see TimeModelStroke
 * @see TimeModelController
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStrokeConfiguration extends JPanel4TimeModelConfiguration implements DocumentListener {

	private static final long serialVersionUID = -1170433671816358910L;
	
	private JLabel jLabelCounterStop;
	private JLabel jLabelCounterStart;
	private JTextField jTextFieldCounterStart;
	private JTextField jTextFieldCounterStop;
	private JLabel jLabelHeader1;
	private JLabel jLabelHeader2;
	private JPanel jPanelDummy;
	
	private boolean enabledChangeListener = true;
	
	/**
	 * Instantiates a new time model stroke configuration.
	 * @param project the project
	 */
	public TimeModelStrokeConfiguration(Project project) {
		super(project);
		this.initialize();
	}
	
	/**
	 * Initializes this JPanel.
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 2;
		gridBagConstraints8.gridy = 0;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 1;
		gridBagConstraints7.fill = GridBagConstraints.BOTH;
		gridBagConstraints7.weightx = 1.0;
		gridBagConstraints7.weighty = 1.0;
		gridBagConstraints7.gridy = 4;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.gridwidth = 2;
		gridBagConstraints6.gridy = 1;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.insets = new Insets(10, 10, 5, 0);
		gridBagConstraints5.gridwidth = 2;
		gridBagConstraints5.gridy = 0;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints4.gridy = 3;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints4.gridx = 1;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.NONE;
		gridBagConstraints3.gridy = 2;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridy = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(10, 10, 0, 0);
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridy = 3;
		
		jLabelHeader1 = new JLabel();
		jLabelHeader1.setText("TimeModelStroke");
		jLabelHeader1.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabelHeader2 = new JLabel();
		jLabelHeader2.setText("Einfach zählendes Zeitmodell (z. B. 1 ... 9999)");
		jLabelHeader2.setText(Language.translate(jLabelHeader2.getText()));
		jLabelCounterStart = new JLabel();
		jLabelCounterStart.setText("Start bei:");
		jLabelCounterStart.setText(Language.translate(jLabelCounterStart.getText()));
		jLabelCounterStart.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelCounterStop = new JLabel();
		jLabelCounterStop.setText("Stop bei:");
		jLabelCounterStop.setText(Language.translate(jLabelCounterStop.getText()));
		jLabelCounterStop.setFont(new Font("Dialog", Font.BOLD, 12));
		
		this.setSize(new Dimension(400, 180));
        this.setLayout(new GridBagLayout());
        this.add(jLabelCounterStop, gridBagConstraints);
        this.add(jLabelCounterStart, gridBagConstraints2);
        this.add(getJTextFieldCounterStart(), gridBagConstraints3);
        this.add(getJTextFieldCounterStop(), gridBagConstraints4);
        this.add(jLabelHeader1, gridBagConstraints5);
        this.add(jLabelHeader2, gridBagConstraints6);
        this.add(getJPanelDummy(), gridBagConstraints7);
        			
	}

	/**
	 * This method initializes jTextFieldCounterStart1	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCounterStart() {
		if (jTextFieldCounterStart == null) {
			jTextFieldCounterStart = new JTextField();
			jTextFieldCounterStart.setPreferredSize(new Dimension(120, 26));
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
			jTextFieldCounterStop.setPreferredSize(new Dimension(120, 26));
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

	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
		}
		return jPanelDummy;
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
		
		TimeModelStroke tms = new TimeModelStroke(counterStart, counterStop);
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
