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
package agentgui.simulationService.load.threading.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.components.TimeSelection;
import agentgui.simulationService.agents.LoadMeasureAgent;

/**
 * The Class ThreadMonitorToolBar.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ThreadMonitorToolBar extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 7052789869732559092L;
	
	private LoadMeasureAgent myAgent;
	
	private JButton jButtonMeasureRefresh;
	private JButton jButtonMeasureStart;
	private JButton jButtonMeasurePause;

	private DefaultComboBoxModel<TimeSelection> comboBoxModelInterval;
	private JComboBox<TimeSelection> jComboBoxInterval;

	
	/**
	 * Instantiates a new thread measure tool bar.
	 */
	public ThreadMonitorToolBar(LoadMeasureAgent agent) {
		this.myAgent = agent;
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setFloatable(false);
		this.setRollover(true);
		
		this.add(this.getJButtonMeasureRefresh());
		this.addSeparator();
		this.add(this.getJButtonMeasureStart());
		this.add(this.getJButtonMeasurePause());
		this.addSeparator();

		this.add(getJComboBoxInterval());
		this.addSeparator();

		// --- Start recording immediately ------
		this.setContinuousMeasurement(true);
		this.myAgent.reStartThreadMeasurement(false);
		
	}
	
	/**
	 * Gets the j button measure refresh.
	 *
	 * @return the j button measure refresh
	 */
	private JButton getJButtonMeasureRefresh() {
		if (jButtonMeasureRefresh==null) {
			jButtonMeasureRefresh = new JButton();
			jButtonMeasureRefresh.setToolTipText(Language.translate("Refresh Thread Measurement", Language.EN));
			jButtonMeasureRefresh.setPreferredSize(new Dimension(26,26));
			jButtonMeasureRefresh.setIcon(GlobalInfo.getInternalImageIcon("Refresh.png" ));
			jButtonMeasureRefresh.addActionListener(this);	
		}
		return jButtonMeasureRefresh;
	}
	
	/**
	 * Gets the j button measure start.
	 *
	 * @return the j button measure start
	 */
	private JButton getJButtonMeasureStart() {
		if (jButtonMeasureStart==null) {
			jButtonMeasureStart = new JButton();
			jButtonMeasureStart.setToolTipText(Language.translate("Start Thread Measurement", Language.EN));
			jButtonMeasureStart.setPreferredSize(new Dimension(26,26));
			jButtonMeasureStart.setIcon(GlobalInfo.getInternalImageIcon("MBLoadPlay.png" ));
			jButtonMeasureStart.addActionListener(this);	
		}
		return jButtonMeasureStart;
	}
	
	/**
	 * Gets the j button measure pause.
	 *
	 * @return the j button measure pause
	 */
	private JButton getJButtonMeasurePause() {
		if (jButtonMeasurePause==null) {
			jButtonMeasurePause = new JButton();
			jButtonMeasurePause.setToolTipText(Language.translate("Pause Thread Measurement", Language.EN));
			jButtonMeasurePause.setPreferredSize(new Dimension(26,26));
			jButtonMeasurePause.setIcon(GlobalInfo.getInternalImageIcon("MBLoadPause.png"));
			jButtonMeasurePause.addActionListener(this);	
		}
		return jButtonMeasurePause;
	}
	
	/**
	 * This method initializes jComboBoxInterval.
	 * @return javax.swing.JComboBox
	 */
	public JComboBox<TimeSelection> getJComboBoxInterval() {
		if (jComboBoxInterval == null) {
			jComboBoxInterval = new JComboBox<TimeSelection>(this.getComboBoxModelRecordingInterval());
			jComboBoxInterval.setMaximumRowCount(comboBoxModelInterval.getSize());
			jComboBoxInterval.setModel(comboBoxModelInterval);
			jComboBoxInterval.setToolTipText(Language.translate("Abtastintervall"));
			jComboBoxInterval.addActionListener(this);
		}
		return jComboBoxInterval;
	}
	/**
	 * This method sets the default values for the ComboBoxModel of sampling interval.
	 * @return the combo box model recording interval
	 */
	private DefaultComboBoxModel<TimeSelection> getComboBoxModelRecordingInterval() {
		if (comboBoxModelInterval==null) {
			TimeSelection defaultTimeSelection = new TimeSelection(5000);
			
			comboBoxModelInterval = new DefaultComboBoxModel<TimeSelection>();	
			comboBoxModelInterval.addElement(new TimeSelection(1000));
			comboBoxModelInterval.addElement(new TimeSelection(2000));
			comboBoxModelInterval.addElement(defaultTimeSelection);
			comboBoxModelInterval.addElement(new TimeSelection(10000));
			comboBoxModelInterval.addElement(new TimeSelection(15000));
			comboBoxModelInterval.addElement(new TimeSelection(20000));
			comboBoxModelInterval.addElement(new TimeSelection(30000));
			comboBoxModelInterval.addElement(new TimeSelection(60000));
			
			comboBoxModelInterval.setSelectedItem(defaultTimeSelection);
		}
		return comboBoxModelInterval;
	}
	/**
	 * Sets the recording interval.
	 * @param timeInMillis the new recording interval
	 */
	public void setRecordingInterval(long timeInMillis) {
		for (int i = 0; i < this.getComboBoxModelRecordingInterval().getSize(); i++) {
			TimeSelection timeSelection = (TimeSelection) this.getComboBoxModelRecordingInterval().getElementAt(i); 
			if (timeSelection.getTimeInMill()==timeInMillis) {
				this.getComboBoxModelRecordingInterval().setSelectedItem(timeSelection);
				break;
			}
		}
	}
	
	/**
	 * Sets the appearance of the toolbar depending on a continuous measurement.
	 * @param isContinuousMeasurement the new continuous measurement
	 */
	private void setContinuousMeasurement(boolean isContinuousMeasurement) {
		this.getJButtonMeasureRefresh().setEnabled(!isContinuousMeasurement);
		this.getJButtonMeasurePause().setEnabled(isContinuousMeasurement);
		this.getJButtonMeasureStart().setEnabled(!isContinuousMeasurement);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==getJButtonMeasureRefresh()) {
			this.myAgent.reStartThreadMeasurement(true);
			
		} else if (ae.getSource()==getJButtonMeasureStart()) {
			this.myAgent.reStartThreadMeasurement(false);
			this.setContinuousMeasurement(true);
			
		} else if (ae.getSource()==getJButtonMeasurePause()) {
			this.myAgent.reStartThreadMeasurement(true);
			this.setContinuousMeasurement(false);
			
		} else if (ae.getSource()==this.getJComboBoxInterval()) {
			long newTickingInterval = ((TimeSelection) jComboBoxInterval.getSelectedItem()).getTimeInMill();
			myAgent.setThreadMeasurementTickingPeriod(newTickingInterval);
			
		}
		
	}
		
}
