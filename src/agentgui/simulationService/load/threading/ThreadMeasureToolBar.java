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
package agentgui.simulationService.load.threading;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.TimeSelection;
import agentgui.simulationService.agents.LoadMeasureAgent;


/**
 * The Class ThreadMeasureToolBar.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ThreadMeasureToolBar extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 7052789869732559092L;
	private static final String pathImage = Application.getGlobalInfo().getPathImageIntern();  //  @jve:decl-index=0:
	
	private LoadMeasureAgent myAgent;
	
	private JButton jButtonMeasureStart;
	private JButton jButtonMeasurePause;
	
	private JComboBox jComboBoxInterval;
	private DefaultComboBoxModel comboBoxModelInterval;
	
	/**
	 * Instantiates a new thread measure tool bar.
	 */
	public ThreadMeasureToolBar(LoadMeasureAgent agent) {
		this.myAgent = agent;
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setFloatable(false);
		this.setRollover(true);
		
		this.add(this.getJButtonMeasureStart());
		this.add(this.getJButtonMeasurePause());
		this.addSeparator();

		this.add(getJComboBoxInterval());
		this.addSeparator();

	}
	
	private JButton getJButtonMeasureStart() {
		if (jButtonMeasureStart==null) {
			jButtonMeasureStart = new JButton();
			jButtonMeasureStart.setToolTipText(Language.translate("Start Thread Measurement", Language.EN));
			jButtonMeasureStart.setSize(36, 36);
			jButtonMeasureStart.setPreferredSize(new Dimension(26,26));
			jButtonMeasureStart.setIcon(new ImageIcon( this.getClass().getResource( pathImage + "MBLoadPlay.png" )));
			jButtonMeasureStart.addActionListener(this);	
		}
		return jButtonMeasureStart;
	}
	private JButton getJButtonMeasurePause() {
		if (jButtonMeasurePause==null) {
			jButtonMeasurePause = new JButton();
			jButtonMeasurePause.setToolTipText(Language.translate("Pause Thread Measurement", Language.EN));
			jButtonMeasurePause.setSize(36, 36);
			jButtonMeasurePause.setPreferredSize(new Dimension(26,26));
			jButtonMeasurePause.setIcon(new ImageIcon( this.getClass().getResource( pathImage + "MBLoadPause.png" )));
			jButtonMeasurePause.addActionListener(this);	
		}
		return jButtonMeasurePause;
	}
	
	/**
	 * This method initializes jComboBoxInterval.
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getJComboBoxInterval() {
		if (jComboBoxInterval == null) {
			jComboBoxInterval = new JComboBox(this.getComboBoxModelRecordingInterval());
			jComboBoxInterval.setMaximumRowCount(comboBoxModelInterval.getSize());
			jComboBoxInterval.setModel(comboBoxModelInterval);
			jComboBoxInterval.setToolTipText(Language.translate("Abtastintervall"));
			jComboBoxInterval.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					long newTickingInterval = ((TimeSelection) jComboBoxInterval.getSelectedItem()).getTimeInMill();
					myAgent.setMonitorBehaviourTickingPeriod(newTickingInterval);
				}
			});
			this.setRecordingInterval(500);
		}
		return jComboBoxInterval;
	}
	/**
	 * This method sets the default values for the ComboBoxModel of sampling interval.
	 */
	private DefaultComboBoxModel getComboBoxModelRecordingInterval() {
		if (comboBoxModelInterval==null) {
			comboBoxModelInterval = new DefaultComboBoxModel();
			comboBoxModelInterval.addElement(new TimeSelection(500));
			comboBoxModelInterval.addElement(new TimeSelection(1000));
			comboBoxModelInterval.addElement(new TimeSelection(2000));
			comboBoxModelInterval.addElement(new TimeSelection(3000));
			comboBoxModelInterval.addElement(new TimeSelection(4000));
			comboBoxModelInterval.addElement(new TimeSelection(5000));
			comboBoxModelInterval.addElement(new TimeSelection(6000));
			comboBoxModelInterval.addElement(new TimeSelection(7000));
			comboBoxModelInterval.addElement(new TimeSelection(8000));
			comboBoxModelInterval.addElement(new TimeSelection(9000));
			comboBoxModelInterval.addElement(new TimeSelection(10000));
			comboBoxModelInterval.addElement(new TimeSelection(15000));
			comboBoxModelInterval.addElement(new TimeSelection(20000));
			comboBoxModelInterval.addElement(new TimeSelection(30000));
			comboBoxModelInterval.addElement(new TimeSelection(60000));
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
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
	}
		
}
