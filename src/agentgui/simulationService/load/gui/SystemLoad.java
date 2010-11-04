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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.simulationService.agents.LoadAgent;

public class SystemLoad extends JPanel {

	private static final long serialVersionUID = 1L;
	
	final static String PathImage = Application.RunInfo.PathImageIntern();  //  @jve:decl-index=0:
	
	private LoadAgent myAgent = null;
	
	private JScrollPane jScrollPane = null;
	public JPanel jPanelLoad = null;
	
	private JToolBar jJToolBarLoad = null;
		private JButton jButtonMeasureStart = null; 
		private JButton jButtonMeasureSuspend = null;
		public JComboBox jComboBoxInterval = null;
		private DefaultComboBoxModel comboData = new DefaultComboBoxModel();
		private TimeSelection comboDataDefaultValue = null;
		private JButton jButtonMeasureRecord = null;
		private JButton jButtonMeasureRecordStop = null;
		public JLabel jLabelRecord = null;

		private JLabel jLabelAgentCount = null;
	
	/**
	 * This is the default constructor
	 */
	public SystemLoad(LoadAgent agent) {
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
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(620, 90);
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.add(getJJToolBarLoad(), BorderLayout.NORTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
	}

	public void setNumberOfAgents(Integer noAgents) {
		
		NumberFormat nf = NumberFormat.getInstance(); 
		nf.setMinimumIntegerDigits(5);  
		nf.setMaximumIntegerDigits(5); 
		nf.setGroupingUsed(false);
		String displaText = " " + nf.format(noAgents) + " " + Language.translate("Agenten") + " ";
		jLabelAgentCount.setText(displaText);
	}
	
	/**
	 * This method initializes jJToolBarLoad	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJJToolBarLoad() {
		if (jJToolBarLoad == null) {
			
			jJToolBarLoad = new JToolBar();
			jJToolBarLoad.setFloatable(false);
			jJToolBarLoad.setRollover(true);
			
			jButtonMeasureStart = new JToolBarButton( "StartMeasurement", Language.translate("Mess-Agenten starten"), null, "MBLoadPlay.png" );
			jJToolBarLoad.add(jButtonMeasureStart);
			
			jButtonMeasureSuspend = new JToolBarButton( "PauseMeasurement", Language.translate("Mess-Agenten anhalten"), null, "MBLoadPause.png" );
			jJToolBarLoad.add(jButtonMeasureSuspend);
			jJToolBarLoad.addSeparator();
			
			jJToolBarLoad.add(getJComboBoxInterval());
			jJToolBarLoad.addSeparator();
			
			jLabelAgentCount = new JLabel();
			jLabelAgentCount.setText(" 00000 " +  Language.translate("Agenten") + " ");
			jLabelAgentCount.setFont(new Font("Dialog", Font.BOLD, 12));
			jJToolBarLoad.add(jLabelAgentCount);
			jJToolBarLoad.addSeparator();
						
			jButtonMeasureRecord = new JToolBarButton( "RecordMeasurement", Language.translate("Messung aufzeichnen"), null, "MBLoadRecord.png" );
			jJToolBarLoad.add(jButtonMeasureRecord);
			
			jButtonMeasureRecordStop = new JToolBarButton( "StopRecordMeasurement", Language.translate("Messungsaufzeichnung beenden"), null, "MBLoadStopRecord.png" );			
			jJToolBarLoad.add(jButtonMeasureRecordStop);

			jLabelRecord = new JLabel();
			jLabelRecord.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelRecord.setText(" Record !");
			jLabelRecord.setForeground(Color.gray);
			jLabelRecord.setPreferredSize(new Dimension(50, 16));
			jJToolBarLoad.add(jLabelRecord);
		}
		return jJToolBarLoad;
	}

	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jScrollPane.setViewportView(getJPanelLoad());
			jScrollPane.setViewportView(getJPanelLoad());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jPanelLoad	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLoad() {
		if (jPanelLoad == null) {
			jPanelLoad = new JPanel();
			jPanelLoad.setSize(new Dimension(620, 90));
			jPanelLoad.setLayout(new BoxLayout(getJPanelLoad(), BoxLayout.Y_AXIS));
		}
		return jPanelLoad;
	}

	/**
	 * This method initializes jComboBoxInterval	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxInterval() {
		if (jComboBoxInterval == null) {
			this.setComboBoxModel();
			jComboBoxInterval = new JComboBox();
			jComboBoxInterval.setMaximumRowCount(comboData.getSize());
			jComboBoxInterval.setModel(comboData);
			jComboBoxInterval.setSelectedItem(comboDataDefaultValue);
			jComboBoxInterval.setToolTipText(Language.translate("Abtastintervall"));
			jComboBoxInterval.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Integer newTickingInterval = ((TimeSelection) jComboBoxInterval.getSelectedItem()).getTimeInMill();
					myAgent.setMonitorBehaviourTickingPeriod(newTickingInterval);
				}
			});
		}
		return jComboBoxInterval;
	}
	
	/**
	 * This method sets the default values for the ComboBoxModel
	 */
	private void setComboBoxModel() {
		
		//comboData.addElement(new TimeSelection(100));
		//comboData.addElement(new TimeSelection(200));
		comboDataDefaultValue = new TimeSelection(500);
		comboData.addElement(comboDataDefaultValue);
		
		comboData.addElement(new TimeSelection(1000));
		comboData.addElement(new TimeSelection(2000));
		comboData.addElement(new TimeSelection(3000));
		comboData.addElement(new TimeSelection(4000));
		comboData.addElement(new TimeSelection(5000));
		comboData.addElement(new TimeSelection(6000));
		comboData.addElement(new TimeSelection(7000));
		comboData.addElement(new TimeSelection(8000));
		comboData.addElement(new TimeSelection(9000));
		comboData.addElement(new TimeSelection(10000));
		
		comboData.addElement(new TimeSelection(15000));
		comboData.addElement(new TimeSelection(20000));
		comboData.addElement(new TimeSelection(30000));
		comboData.addElement(new TimeSelection(60000));
	}
	
	// ------------------------------------------------------------
	// --- Unterklasse für die Symbolleisten-Buttons --- START ----
	// ------------------------------------------------------------	
	private class JToolBarButton extends JButton implements ActionListener {

		private static final long serialVersionUID = 1L;
 
		private JToolBarButton( String actionCommand, 
								String toolTipText, 
								String altText, 
								String imgName ) {
				
			this.setText(altText);
			this.setToolTipText(toolTipText);
			this.setSize(36, 36);
			
			if ( imgName != null ) {
				this.setPreferredSize( new Dimension(26,26) );
			}
			else {
				this.setPreferredSize( null );	
			}

			if ( imgName != null ) {
				try {
					ImageIcon ButtIcon = new ImageIcon( this.getClass().getResource( PathImage + imgName ), altText);
					this.setIcon(ButtIcon);
				}
				catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}				
			}
			this.addActionListener(this);	
			this.setActionCommand(actionCommand);
		}
		
		public void actionPerformed(ActionEvent ae) {
			// --- Fallunterscheidung 'cmd' einbauen ---
			String ActCMD = ae.getActionCommand();			
			// ------------------------------------------------
			if ( ActCMD.equalsIgnoreCase("StartMeasurement") ) {
				myAgent.addBehaviour(myAgent.monitorBehaviour);
				jButtonMeasureStart.setEnabled(false);
				jButtonMeasureSuspend.setEnabled(true);

			} else if ( ActCMD.equalsIgnoreCase("PauseMeasurement") ) {
				myAgent.removeBehaviour(myAgent.monitorBehaviour);
				jButtonMeasureStart.setEnabled(true);
				jButtonMeasureSuspend.setEnabled(false);

			} else if ( ActCMD.equalsIgnoreCase("RecordMeasurement") ) {
				myAgent.setMonitorSaveLoad(true);
				jButtonMeasureRecord.setEnabled(false);
				jButtonMeasureRecordStop.setEnabled(true);
				jLabelRecord.setForeground(Color.red);
			} else if ( ActCMD.equalsIgnoreCase("StopRecordMeasurement") ) {
				myAgent.setMonitorSaveLoad(false);
				jButtonMeasureRecord.setEnabled(true);
				jButtonMeasureRecordStop.setEnabled(false);
				jLabelRecord.setForeground(Color.gray);
			} else { 
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
			};
			
		};
	};
	// ------------------------------------------------------------
	// --- Unterklasse für die Symbolleisten-Buttons --- END ------
	// ------------------------------------------------------------	

	
	// ------------------------------------------------------------
	// --- Unterklasse für das ComboBoxModel --- START ------------
	// ------------------------------------------------------------	
	public class TimeSelection {
		
		private int timeInMill = 0;
		
		public TimeSelection(int timeInMillis) {
			this.timeInMill = timeInMillis;
		}
		/**
		 * @return the timeInMill
		 */
		public int getTimeInMill() {
			return timeInMill;
		}
		/**
		 * @param timeInMill the timeInMill to set
		 */
		public void setTimeInMill(int timeInMill) {
			this.timeInMill = timeInMill;
		}
		/**
		 * @return the text to display
		 */
		public String toString() {
			int timeInTenth = Math.round(timeInMill/100);
			float timeInSecFloat = (float) timeInTenth / 10;  
			int timeInSecInt = (int) timeInSecFloat;
			
			if ((timeInSecFloat-timeInSecInt)>0) {
				return timeInSecFloat + " s";
			} else {
				return timeInSecInt + " s";
			}			
		}
		
	}
	// ------------------------------------------------------------
	// --- Unterklasse für das ComboBoxModel --- START ------------
	// ------------------------------------------------------------	

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
