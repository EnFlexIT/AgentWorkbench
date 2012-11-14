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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
public class TimeModelContinuousConfiguration extends DisplayJPanel4Configuration implements ChangeListener {

	private static final long serialVersionUID = -1170433671816358910L;
	
	private JLabel jLabelHeader1 = null;
	private JLabel jLabelHeader2 = null;
	private JPanel jPanelDummy = null;
	
	private JLabel jLabelStart = null;
	private JLabel jLabelStopDate = null;
	private JLabel jLabelStopTime = null;
	private JLabel jLabelStopMillis = null;
	
	private JPanel jPanelStartSettings = null;
	private JPanel jPanelWidthSettings = null;
	
	private JLabel jLabelWidth = null;
	private JLabel jLabelWidthStep = null;
	private JTextField jTextFieldWidthValue = null;
	private JSpinner jSpinnerDateStart = null;
	private JSpinner jSpinnerTimeStart = null;
	private JSpinner jSpinnerMillisStart = null;
	/**
	 * Instantiates a new time model discrete configuration.
	 * @param project the project
	 */
	public TimeModelContinuousConfiguration(Project project) {
		super(project);
		this.initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 1;
		gridBagConstraints9.anchor = GridBagConstraints.WEST;
		gridBagConstraints9.insets = new Insets(10, 5, 10, 0);
		gridBagConstraints9.gridy = 8;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints4.gridy = 8;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints15.anchor = GridBagConstraints.WEST;
		gridBagConstraints15.gridy = 4;
		GridBagConstraints gridBagConstraints141 = new GridBagConstraints();
		gridBagConstraints141.gridx = 1;
		gridBagConstraints141.gridwidth = 5;
		gridBagConstraints141.anchor = GridBagConstraints.WEST;
		gridBagConstraints141.insets = new Insets(10, 5, 10, 0);
		gridBagConstraints141.gridy = 4;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 2;
		gridBagConstraints8.gridy = 0;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.fill = GridBagConstraints.BOTH;
		gridBagConstraints7.weightx = 1.0;
		gridBagConstraints7.weighty = 1.0;
		gridBagConstraints7.gridwidth = 2;
		gridBagConstraints7.gridy = 10;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.gridwidth = 2;
		gridBagConstraints6.weightx = 0.0;
		gridBagConstraints6.fill = GridBagConstraints.NONE;
		gridBagConstraints6.gridy = 1;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.insets = new Insets(10, 10, 5, 0);
		gridBagConstraints5.gridwidth = 2;
		gridBagConstraints5.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;
		
		jLabelHeader1 = new JLabel();
		jLabelHeader1.setText("TimeModelContinuous");
		jLabelHeader1.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabelHeader2 = new JLabel();
		jLabelHeader2.setText("Kontinuierlich fortschreitende Zeit.");
		jLabelHeader2.setText(Language.translate(jLabelHeader2.getText()));
		jLabelStart = new JLabel();
		jLabelStart.setText("Start bei");
		jLabelStart.setText(Language.translate(jLabelStart.getText()) + ":");
		jLabelStart.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelStopDate = new JLabel();
		jLabelStopDate.setText("Datum");
		jLabelStopDate.setPreferredSize(new Dimension(40, 16));
		jLabelStopDate.setText(Language.translate(jLabelStopDate.getText())+ ":");
		jLabelStopTime = new JLabel();
		jLabelStopTime.setText("Uhrzeit");
		jLabelStopTime.setText(Language.translate(jLabelStopTime.getText())+ ":");
		jLabelStopMillis = new JLabel();
		jLabelStopMillis.setText("Millisekunden");
		jLabelStopMillis.setText(Language.translate(jLabelStopMillis.getText())+ ":");
		jLabelWidth = new JLabel();
		jLabelWidth.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelWidth.setText("Faktor");
		jLabelWidth.setText(Language.translate(jLabelWidth.getText())+ ":");
		jLabelWidthStep = new JLabel();
		jLabelWidthStep.setText("Wert");
		jLabelWidthStep.setPreferredSize(new Dimension(40, 16));
		jLabelWidthStep.setText(Language.translate(jLabelWidthStep.getText())+ ":");
		
		this.setSize(new Dimension(609, 252));
        this.setLayout(new GridBagLayout());
        this.add(jLabelHeader1, gridBagConstraints5);
        this.add(jLabelHeader2, gridBagConstraints6);
        this.add(getJPanelDummy(), gridBagConstraints7);
        this.add(getJPanelStartSettings(), gridBagConstraints141);
        this.add(jLabelStart, gridBagConstraints15);
        this.add(jLabelWidth, gridBagConstraints4);
        this.add(getJPanelWidthSettings(), gridBagConstraints9);
        			
	}

	/**
	 * Gets the JSpinner date start.
	 * @return the JSpinner date start
	 */
	private JSpinner getJSpinnerDateStart() {
		if (jSpinnerDateStart==null) {
			jSpinnerDateStart = new JSpinner(new SpinnerDateModel());
			jSpinnerDateStart.setEditor(new JSpinner.DateEditor(jSpinnerDateStart, "dd.MM.yyyy"));
			jSpinnerDateStart.setPreferredSize(new Dimension(100, 28));
			jSpinnerDateStart.addChangeListener(this);
		}
		return jSpinnerDateStart;
	}
	/**
	 * Gets the JSpinner time start.
	 * @return the JSpinner time start
	 */
	private JSpinner getJSpinnerTimeStart() {
		if (jSpinnerTimeStart==null) {
			jSpinnerTimeStart = new JSpinner(new SpinnerDateModel());
			jSpinnerTimeStart.setEditor(new JSpinner.DateEditor(jSpinnerTimeStart, "HH:mm:ss"));
			jSpinnerTimeStart.setPreferredSize(new Dimension(80, 28));
			jSpinnerTimeStart.addChangeListener(this);
		}
		return jSpinnerTimeStart;
	}
	/**
	 * Gets the JSpinner milliseconds start.
	 * @return the JSpinner milliseconds start
	 */
	private JSpinner getJSpinnerMillisStart() {
		if (jSpinnerMillisStart==null) {
			jSpinnerMillisStart = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
			jSpinnerMillisStart.setEditor(new JSpinner.NumberEditor(jSpinnerMillisStart, "000"));
			jSpinnerMillisStart.setPreferredSize(new Dimension(60, 28));
			jSpinnerMillisStart.addChangeListener(this);
		}
		return jSpinnerMillisStart;
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
	
	/**
	 * This method initializes jPanelStartSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStartSettings() {
		if (jPanelStartSettings == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.CENTER);
			flowLayout.setVgap(0);
			flowLayout.setHgap(5);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new Insets(10, 10, 0, 0);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			gridBagConstraints.insets = new Insets(10, 10, 0, 0);
			
			jPanelStartSettings = new JPanel();
			jPanelStartSettings.setLayout(flowLayout);
			jPanelStartSettings.add(jLabelStopDate, null);
			jPanelStartSettings.add(getJSpinnerDateStart(), null);
			jPanelStartSettings.add(jLabelStopTime, null);
			jPanelStartSettings.add(getJSpinnerTimeStart(), null);
			jPanelStartSettings.add(jLabelStopMillis, null);
			jPanelStartSettings.add(getJSpinnerMillisStart(), null);
		}
		return jPanelStartSettings;
	}

	/**
	 * This method initializes jPanelWidthSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelWidthSettings() {
		if (jPanelWidthSettings == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.CENTER);
			flowLayout1.setVgap(0);
			jPanelWidthSettings = new JPanel();
			jPanelWidthSettings.setLayout(flowLayout1);
			jPanelWidthSettings.add(jLabelWidthStep, null);
			jPanelWidthSettings.add(getJTextFieldWidthValue(), null);
		}
		return jPanelWidthSettings;
	}
	/**
	 * This method initializes jTextFieldWidthValue	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldWidthValue() {
		if (jTextFieldWidthValue == null) {
			jTextFieldWidthValue = new JTextField();
			jTextFieldWidthValue.setPreferredSize(new Dimension(100, 26));
			jTextFieldWidthValue.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String singleChar = Character.toString(charackter);
					if (singleChar.matches("[0-9]") == false) {
						kT.consume();	
						return;
					}
				 }				 
			});
			jTextFieldWidthValue.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent de) {
					this.saveValue();
				}
				@Override
				public void insertUpdate(DocumentEvent de) {
					this.saveValue();
				}
				@Override
				public void changedUpdate(DocumentEvent de) {
					this.saveValue();
				}
				private void saveValue() {
					saveTimeModelInSimulationSetup(getTimeModel());
				}
			});
			
		}
		return jTextFieldWidthValue;
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		Calendar calendarWork = Calendar.getInstance();
		
		TimeModelContinuous timeModelDiscrete=null; 
		if (timeModel==null) {
			timeModelDiscrete = new TimeModelContinuous();
		} else {
			timeModelDiscrete = (TimeModelContinuous) timeModel;
		}
		
		// --- Start settings ---------------------------------------
		Date startDate = new Date(timeModelDiscrete.getStartTime());
		calendarWork.setTime(startDate);
		this.getJSpinnerDateStart().setValue(startDate);
		this.getJSpinnerTimeStart().setValue(startDate);
		this.getJSpinnerMillisStart().setValue(calendarWork.get(Calendar.MILLISECOND));
		
		// --- Settings for the step width --------------------------
		long step = 1;
		
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {

		Calendar calendarWork = Calendar.getInstance();
		
		// --- Getting Start time as Long ---------------------------
		Calendar startCalenderMerged = Calendar.getInstance();
		Date startDate = (Date) this.getJSpinnerDateStart().getValue();
		Date startTime = (Date) this.getJSpinnerTimeStart().getValue();
		int startMillis = (Integer) this.getJSpinnerMillisStart().getValue();
		
		calendarWork.setTime(startDate);
		startCalenderMerged.set(Calendar.DAY_OF_MONTH, calendarWork.get(Calendar.DAY_OF_MONTH));
		startCalenderMerged.set(Calendar.MONTH, calendarWork.get(Calendar.MONTH));
		startCalenderMerged.set(Calendar.YEAR, calendarWork.get(Calendar.YEAR));
		calendarWork.setTime(startTime);
		startCalenderMerged.set(Calendar.HOUR_OF_DAY, calendarWork.get(Calendar.HOUR_OF_DAY));
		startCalenderMerged.set(Calendar.MINUTE, calendarWork.get(Calendar.MINUTE));
		startCalenderMerged.set(Calendar.SECOND, calendarWork.get(Calendar.SECOND));
		startCalenderMerged.set(Calendar.MILLISECOND, startMillis);
		Date start = startCalenderMerged.getTime();
		Long startLong = start.getTime();
		
		// --- Getting step width -----------------------------------
		String stepString = this.getJTextFieldWidthValue().getText();
		long step;
		if (stepString==null) {
			step = new Long(0);
		} else if (stepString.equals("")) {
			step = new Long(0);
		} else {
			
		}
		
		// --- Set TimeModel ----------------------------------------
		TimeModelContinuous tmc = new TimeModelContinuous(startLong);
		return tmc;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent ce) {
		Object ceTrigger = ce.getSource();
		if (ceTrigger instanceof JSpinner) {
			this.saveTimeModelInSimulationSetup(getTimeModel());	
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="3,10"
