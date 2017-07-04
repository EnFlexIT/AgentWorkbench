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
package agentgui.simulationService.load.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.components.TimeSelection;
import agentgui.simulationService.agents.LoadMeasureAgent;

/**
 * This panel will display all occurring elements of the type {@link SystemLoadSingle}
 * in this panel with each other. It is used by the {@link LoadMeasureAgent}. 
 *  
 * @see SystemLoadSingle
 * @see LoadMeasureAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemLoadPanel extends JPanel {

	private static final long serialVersionUID = -6077963388508346438L;
//	private static final String pathImage = Application.getGlobalInfo().getPathImageIntern();  //  @jve:decl-index=0:
	
	private LoadMeasureAgent myAgent;
	private boolean isRecording = false;
	
	private JScrollPane jScrollPaneForLoadDisplays;
	private JPanel jPanelForLoadDisplays;
	
	private JToolBar jToolBarLoad;
		private JButton jButtonMeasureStart; 
		private JButton jButtonMeasureSuspend;
		private JComboBox<TimeSelection> jComboBoxInterval;
		private DefaultComboBoxModel<TimeSelection> comboBoxModelInterval;
		private JButton jButtonMeasureRecord;
		private JButton jButtonMeasureRecordStop;
		public JLabel jLabelRecord;
		
		public JLabel jLabelSpeed;
		
		private JLabel jLabelAgentCount;
		private JLabel jLabelContainerCount;
		
		private JTextField jTextFieldCyclesPerSecond = null;

	
	/**
	 * This is the default constructor.
	 * @param agent the agent
	 */
	public SystemLoadPanel(LoadMeasureAgent agent) {
		super();
		myAgent = agent;
		initialize();
		
		jButtonMeasureStart.setEnabled(false);
		jButtonMeasureSuspend.setEnabled(true);

		jButtonMeasureRecord.setEnabled(true);
		jButtonMeasureRecordStop.setEnabled(false);
		
		jLabelRecord.setForeground(Color.gray);
	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.setLayout(new BorderLayout());
		this.add(getJToolBarLoad(), BorderLayout.NORTH);
		this.add(getJScrollPaneForLoadDisplays(), BorderLayout.CENTER);
		this.setSize(620, SystemLoadSingle.loadPanelHeight + this.getJToolBarLoad().getHeight());
		
		this.setRecordingInterval(this.myAgent.getMonitorBehaviourTickingPeriod());
	}

	/**
	 * Sets the number of agents.
	 * @param noAgents the new number of agents
	 */
	public void setNumberOfAgents(Integer noAgents) {
		
		String displayText = null;
		
		NumberFormat nf = NumberFormat.getInstance(); 
		nf.setMinimumIntegerDigits(5);  
		nf.setMaximumIntegerDigits(5); 
		nf.setGroupingUsed(false);
		
		if (noAgents==null) {
			displayText = " " + nf.format(0) + " " + Language.translate("Agenten") + " ";
		} else {
			displayText = " " + nf.format(noAgents) + " " + Language.translate("Agenten") + " ";
		}
		jLabelAgentCount.setText(displayText);
	}
	
	/**
	 * Sets the number of container.
	 * @param noContainer the new number of container
	 */
	public void setNumberOfContainer(Integer noContainer) {
		
		NumberFormat nf = NumberFormat.getInstance(); 
		nf.setMinimumIntegerDigits(3);  
		nf.setMaximumIntegerDigits(3); 
		nf.setGroupingUsed(false);
		String displaText = " " + nf.format(noContainer) + " " + Language.translate("Container") + " ";
		jLabelContainerCount.setText(displaText);
	}

	/**
	 * Sets the cycle time.
	 * @param cycleTime the new cycle time
	 */
	public void setCycleTime(double cycleTime) {
		
		if (cycleTime==0) {
			jTextFieldCyclesPerSecond.setText("0");
			return;
		}

		// --- Calculate the frequency [1/s] ----
		double cycleTimeDbl = cycleTime / 1000; 		// to seconds
		double frequency = 1 / cycleTimeDbl;			// to frequency 
		frequency = (double) Math.round(frequency * 100) / 100;	// round
		
		String cycleTimeString = ((Double) frequency).toString();
		jTextFieldCyclesPerSecond.setText(cycleTimeString);
	}
	
	/**
	 * This method initializes jJToolBarLoad.
	 * @return javax.swing.JToolBar
	 */
	public JToolBar getJToolBarLoad() {
		if (jToolBarLoad == null) {
			
			jToolBarLoad = new JToolBar();
			jToolBarLoad.setFloatable(false);
			jToolBarLoad.setRollover(true);
			
			// --- The Pause/Start Buttons of the Load-Agent --------
			jButtonMeasureStart = new JToolBarButton( "StartMeasurement", Language.translate("Mess-Agenten starten"), null, "MBLoadPlay.png" );
			jToolBarLoad.add(jButtonMeasureStart);
			
			jButtonMeasureSuspend = new JToolBarButton( "PauseMeasurement", Language.translate("Mess-Agenten anhalten"), null, "MBLoadPause.png" );
			jToolBarLoad.add(jButtonMeasureSuspend);
			jToolBarLoad.addSeparator();
			
			// --- The measure interval -----------------------------
			jToolBarLoad.add(getJComboBoxInterval());
			jToolBarLoad.addSeparator();
			
			// --- The Record Start/Stop Buttons --------------------
			jButtonMeasureRecord = new JToolBarButton( "RecordMeasurement", Language.translate("Messung aufzeichnen"), null, "MBLoadRecord.png" );
			jToolBarLoad.add(jButtonMeasureRecord);
			
			jButtonMeasureRecordStop = new JToolBarButton( "StopRecordMeasurement", Language.translate("Messungsaufzeichnung beenden"), null, "MBLoadStopRecord.png" );			
			jToolBarLoad.add(jButtonMeasureRecordStop);

			jLabelRecord = new JLabel();
			jLabelRecord.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelRecord.setText(" Record !");
			jLabelRecord.setForeground(Color.gray);
			jLabelRecord.setPreferredSize(new Dimension(50, 16));
			jToolBarLoad.add(jLabelRecord);
			jToolBarLoad.addSeparator();
			
			// --- Counter for the number of agents -----------------
			jLabelAgentCount = new JLabel();
			jLabelAgentCount.setText(" 00000 " +  Language.translate("Agenten") + " ");
			jLabelAgentCount.setFont(new Font("Dialog", Font.BOLD, 12));
			jToolBarLoad.add(jLabelAgentCount);
			jToolBarLoad.addSeparator();
			
			// --- Counter for the number of container --------------
			jLabelContainerCount = new JLabel();
			jLabelContainerCount.setText(" 000 " +  Language.translate("Container") + " ");
			jLabelContainerCount.setFont(new Font("Dialog", Font.BOLD, 12));
			jToolBarLoad.add(jLabelContainerCount);
			jToolBarLoad.addSeparator();
			
			// --- Field for the number of cycles/second ------------
			jTextFieldCyclesPerSecond = new JTextField();
			jTextFieldCyclesPerSecond.setPreferredSize(new Dimension(70, 25));
			jTextFieldCyclesPerSecond.setEditable(false);
			jTextFieldCyclesPerSecond.setToolTipText("Cycles / second");
			jToolBarLoad.add(jTextFieldCyclesPerSecond);

			jLabelSpeed = new JLabel();
			jLabelSpeed.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelSpeed.setText(" 1 / s");
			jLabelSpeed.setPreferredSize(new Dimension(30, 16));
			jToolBarLoad.add(jLabelSpeed);
			
		}
		return jToolBarLoad;
	}

	/**
	 * Gets the JScrollPane.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneForLoadDisplays() {
		if (jScrollPaneForLoadDisplays == null) {
			jScrollPaneForLoadDisplays = new JScrollPane();
			jScrollPaneForLoadDisplays.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jScrollPaneForLoadDisplays.setViewportView(this.getJPanelForLoadDisplays());
		}
		return jScrollPaneForLoadDisplays;
	}
	/**
	 * Returns the JPanel for load displays.
	 * @return the JPanel for load displays
	 */
	public JPanel getJPanelForLoadDisplays() {
		if (jPanelForLoadDisplays == null) {
			jPanelForLoadDisplays = new JPanel();
			jPanelForLoadDisplays.setSize(new Dimension(620, SystemLoadSingle.loadPanelHeight));
			jPanelForLoadDisplays.setLayout(new BoxLayout(getJPanelForLoadDisplays(), BoxLayout.Y_AXIS));
		}
		return jPanelForLoadDisplays;
	}

	/**
	 * This method initializes jComboBoxInterval.
	 * @return javax.swing.JComboBox
	 */
	private JComboBox<TimeSelection> getJComboBoxInterval() {
		if (jComboBoxInterval == null) {
			jComboBoxInterval = new JComboBox<TimeSelection>(this.getComboBoxModelRecordingInterval());
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
		}
		return jComboBoxInterval;
	}
	
	/**
	 * This method sets the default values for the ComboBoxModel of sampling interval.
	 */
	private DefaultComboBoxModel<TimeSelection> getComboBoxModelRecordingInterval() {
		if (comboBoxModelInterval==null) {
			comboBoxModelInterval = new DefaultComboBoxModel<TimeSelection>();
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
	
	/**
	 * Sets to do the load recording now.
	 * @param doRecording the new do load recording
	 */
	public void setDoLoadRecording(boolean doRecording) {
		// --- Prevent to repeat an action if already ----- 
		if (doRecording!=isRecording) {
			if (doRecording==true) {
				this.isRecording = true;
				this.myAgent.setMonitorSaveLoad(true);
				this.jButtonMeasureRecord.setEnabled(false);
				this.jButtonMeasureRecordStop.setEnabled(true);
				this.jLabelRecord.setForeground(Color.red);
			} else {
				this.isRecording = false;
				this.myAgent.setMonitorSaveLoad(false);
				this.jButtonMeasureRecord.setEnabled(true);
				this.jButtonMeasureRecordStop.setEnabled(false);
				this.jLabelRecord.setForeground(Color.gray);
			}
		}
	}
	

	// ------------------------------------------------------------
	// --- Sub class for buttons of the toolbar --- Start ---------
	// ------------------------------------------------------------	
	/**
	 * Sub class for generating JToolBarButton's.
	 */
	private class JToolBarButton extends JButton implements ActionListener {

		private static final long serialVersionUID = 1L;
 
		/**
		 * Instantiates a new JToolBarButton.
		 *
		 * @param actionCommand the action command
		 * @param toolTipText the tool tip text
		 * @param text the text
		 * @param imgName the image name
		 */
		private JToolBarButton(String actionCommand, String toolTipText, String text, String imgName) {
				
			this.setText(text);
			this.setToolTipText(toolTipText);
			this.setSize(36, 36);
			
			if ( imgName != null ) {
				this.setPreferredSize(new Dimension(26,26));
			} else {
				this.setPreferredSize(null);	
			}

			if (imgName!=null) {
				try {
					this.setIcon(GlobalInfo.getInternalImageIcon(imgName));
				} catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}				
			}
			this.addActionListener(this);	
			this.setActionCommand(actionCommand);
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			
			String actionCMD = ae.getActionCommand();			
			// ------------------------------------------------
			if ( actionCMD.equalsIgnoreCase("StartMeasurement") ) {
				myAgent.addBehaviour(myAgent.getMonitorBehaviour());
				jButtonMeasureStart.setEnabled(false);
				jButtonMeasureSuspend.setEnabled(true);

			} else if ( actionCMD.equalsIgnoreCase("PauseMeasurement") ) {
				myAgent.removeBehaviour(myAgent.getMonitorBehaviour());
				jButtonMeasureStart.setEnabled(true);
				jButtonMeasureSuspend.setEnabled(false);

			} else if ( actionCMD.equalsIgnoreCase("RecordMeasurement") ) {
				setDoLoadRecording(true);
			} else if ( actionCMD.equalsIgnoreCase("StopRecordMeasurement") ) {
				setDoLoadRecording(false);
				
			} else { 
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actionCMD);
			};
			
		};
	};
	// ------------------------------------------------------------
	// --- sub class for buttons of the toolbar --- END -----------
	// ------------------------------------------------------------	

	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
