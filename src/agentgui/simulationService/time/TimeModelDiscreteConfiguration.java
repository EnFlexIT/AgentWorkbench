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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
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
public class TimeModelDiscreteConfiguration extends JPanel4TimeModelConfiguration implements ChangeListener {

	private static final long serialVersionUID = -1170433671816358910L;
	
	private JLabel jLabelHeader1 = null;
	private JLabel jLabelHeader2 = null;
	private JPanel jPanelDummy = null;
	
	private JLabel jLabelStop = null;
	private JLabel jLabelStart = null;
	private JLabel jLabelStartDate = null;
	private JLabel jLabelStopDate = null;
	private JLabel jLabelStartTime = null;
	private JLabel jLabelStopTime = null;
	private JLabel jLabelStartMillis = null;
	private JLabel jLabelStopMillis = null;
	
	private JPanel jPanelStartSettings = null;
	private JPanel jPanelStopSettings = null;
	private JPanel jPanelWidthSettings = null;
	
	private JLabel jLabelWidth = null;
	private JLabel jLabelWidthStep = null;
	private JLabel jLabelWidthUnit = null;

	private JTextField jTextFieldWidthValue = null;
	private JComboBox<TimeUnit> jComboBoxWidthUnit = null;

	private JSpinner jSpinnerDateStart = null;
	private JSpinner jSpinnerTimeStart = null;
	private JSpinner jSpinnerMillisStart = null;
	private JSpinner jSpinnerDateStop = null;
	private JSpinner jSpinnerTimeStop = null;
	private JSpinner jSpinnerMillisStop = null;

	private TimeFormatSelection jPanelTimeFormater = null;
	private boolean enabledChangeListener = true;

	private JLabel jLabeDateFormat = null;

	/**
	 * Instantiates a new time model discrete configuration.
	 * @param project the project
	 */
	public TimeModelDiscreteConfiguration(Project project) {
		super(project);
		this.initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 0;
		gridBagConstraints10.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints10.insets = new Insets(15, 10, 0, 0);
		gridBagConstraints10.gridy = 5;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridy = 5;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.gridwidth = 1;
		gridBagConstraints11.insets = new Insets(10, 5, 0, 0);
		gridBagConstraints11.gridx = 1;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 1;
		gridBagConstraints9.anchor = GridBagConstraints.WEST;
		gridBagConstraints9.insets = new Insets(10, 5, 10, 0);
		gridBagConstraints9.gridy = 4;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.gridy = 4;
		GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		gridBagConstraints16.gridx = 0;
		gridBagConstraints16.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints16.anchor = GridBagConstraints.WEST;
		gridBagConstraints16.gridy = 3;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints15.anchor = GridBagConstraints.WEST;
		gridBagConstraints15.gridy = 2;
		GridBagConstraints gridBagConstraints141 = new GridBagConstraints();
		gridBagConstraints141.gridx = 1;
		gridBagConstraints141.gridwidth = 5;
		gridBagConstraints141.anchor = GridBagConstraints.WEST;
		gridBagConstraints141.insets = new Insets(10, 5, 10, 0);
		gridBagConstraints141.gridy = 2;
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 1;
		gridBagConstraints13.fill = GridBagConstraints.NONE;
		gridBagConstraints13.gridwidth = 8;
		gridBagConstraints13.anchor = GridBagConstraints.WEST;
		gridBagConstraints13.insets = new Insets(10, 5, 10, 0);
		gridBagConstraints13.gridy = 3;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 2;
		gridBagConstraints8.gridy = 0;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.fill = GridBagConstraints.BOTH;
		gridBagConstraints7.weightx = 1.0;
		gridBagConstraints7.weighty = 1.0;
		gridBagConstraints7.gridwidth = 2;
		gridBagConstraints7.gridy = 6;
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
		jLabelHeader1.setText("TimeModelDiscrete");
		jLabelHeader1.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabelHeader2 = new JLabel();
		jLabelHeader2.setText("Diskretes Zeitmodell mit Schrittweite sowie Start- und Endzeitpunkt.");
		jLabelHeader2.setText(Language.translate(jLabelHeader2.getText()));
		
		jLabelStart = new JLabel();
		jLabelStart.setText("Start bei");
		jLabelStart.setText(Language.translate(jLabelStart.getText()) + ":");
		jLabelStart.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelStartDate = new JLabel();
		jLabelStartDate.setText("Datum");
		jLabelStartDate.setPreferredSize(new Dimension(40, 16));
		jLabelStartDate.setText(Language.translate(jLabelStartDate.getText())+ ":");
		jLabelStartTime = new JLabel();
		jLabelStartTime.setText("Uhrzeit");
		jLabelStartTime.setText(Language.translate(jLabelStartTime.getText())+ ":");
		jLabelStartMillis = new JLabel();
		jLabelStartMillis.setText("Millisekunden");
		jLabelStartMillis.setText(Language.translate(jLabelStartMillis.getText())+ ":");
		
		jLabelStop = new JLabel();
		jLabelStop.setText("Stop bei");
		jLabelStop.setText(Language.translate(jLabelStop.getText())+ ":");
		jLabelStop.setFont(new Font("Dialog", Font.BOLD, 12));
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
		jLabelWidth.setText("Schrittweite");
		jLabelWidth.setText(Language.translate(jLabelWidth.getText())+ ":");
		jLabelWidthStep = new JLabel();
		jLabelWidthStep.setText("Wert");
		jLabelWidthStep.setPreferredSize(new Dimension(40, 16));
		jLabelWidthStep.setText(Language.translate(jLabelWidthStep.getText())+ ":");
		jLabelWidthUnit = new JLabel();
		jLabelWidthUnit.setText("Einheit");
		jLabelWidthUnit.setText(Language.translate(jLabelWidthUnit.getText())+ ":");

		jLabeDateFormat = new JLabel();
		jLabeDateFormat.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabeDateFormat.setText("Ansicht");
		jLabeDateFormat.setText(Language.translate(jLabeDateFormat.getText()) + ":");
		
		this.setSize(new Dimension(559, 350));
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(600, 322));
        this.setPreferredSize(new Dimension(810, 322));
        this.add(jLabelHeader1, gridBagConstraints5);
        this.add(jLabelHeader2, gridBagConstraints6);
        this.add(getJPanelDummy(), gridBagConstraints7);
        this.add(getJPanelStopSettings(), gridBagConstraints13);
        this.add(getJPanelStartSettings(), gridBagConstraints141);
        this.add(jLabelStart, gridBagConstraints15);
        this.add(jLabelStop, gridBagConstraints16);
        this.add(jLabelWidth, gridBagConstraints4);
        this.add(getJPanelWidthSettings(), gridBagConstraints9);
        this.add(getJPanelTimeFormater(), gridBagConstraints11);
        this.add(jLabeDateFormat, gridBagConstraints10);
        			
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
	 * Gets the JSpinner date stop.
	 * @return the JSpinner date stop
	 */
	private JSpinner getJSpinnerDateStop() {
		if (jSpinnerDateStop==null) {
			jSpinnerDateStop = new JSpinner(new SpinnerDateModel());
			jSpinnerDateStop.setEditor(new JSpinner.DateEditor(jSpinnerDateStop, "dd.MM.yyyy"));
			jSpinnerDateStop.setPreferredSize(new Dimension(100, 28));
			jSpinnerDateStop.addChangeListener(this);
		}
		return jSpinnerDateStop;
	}
	/**
	 * Gets the JSpinner time stop.
	 * @return the JSpinner time stop
	 */
	private JSpinner getJSpinnerTimeStop() {
		if (jSpinnerTimeStop==null) {
			jSpinnerTimeStop = new JSpinner(new SpinnerDateModel());
			jSpinnerTimeStop.setEditor(new JSpinner.DateEditor(jSpinnerTimeStop, "HH:mm:ss"));
			jSpinnerTimeStop.setPreferredSize(new Dimension(80, 28));
			jSpinnerTimeStop.addChangeListener(this);
		}
		return jSpinnerTimeStop;
	}
	/**
	 * Gets the JSpinner milliseconds stop.
	 * @return the JSpinner milliseconds stop
	 */
	private JSpinner getJSpinnerMillisStop() {
		if (jSpinnerMillisStop==null) {
			jSpinnerMillisStop = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
			jSpinnerMillisStop.setEditor(new JSpinner.NumberEditor(jSpinnerMillisStop, "000"));
			jSpinnerMillisStop.setPreferredSize(new Dimension(60, 28));
			jSpinnerMillisStop.addChangeListener(this);
		}
		return jSpinnerMillisStop;
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
	 * This method initializes jPanelStopSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStopSettings() {
		if (jPanelStopSettings == null) {
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setVgap(0);
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.gridx = -1;
			gridBagConstraints14.gridy = -1;
			gridBagConstraints14.insets = new Insets(10, 10, 0, 0);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.insets = new Insets(10, 10, 0, 0);
			
			jPanelStopSettings = new JPanel();
			jPanelStopSettings.setLayout(flowLayout2);
			jPanelStopSettings.add(jLabelStopDate, null);
			jPanelStopSettings.add(getJSpinnerDateStop(), null);
			jPanelStopSettings.add(jLabelStopTime, null);
			jPanelStopSettings.add(getJSpinnerTimeStop(), null);
			jPanelStopSettings.add(jLabelStopMillis, null);
			jPanelStopSettings.add(getJSpinnerMillisStop(), null);
		}
		return jPanelStopSettings;
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
			jPanelStartSettings.add(jLabelStartDate, null);
			jPanelStartSettings.add(getJSpinnerDateStart(), null);
			jPanelStartSettings.add(jLabelStartTime, null);
			jPanelStartSettings.add(getJSpinnerTimeStart(), null);
			jPanelStartSettings.add(jLabelStartMillis, null);
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
			jPanelWidthSettings.add(jLabelWidthUnit, null);
			jPanelWidthSettings.add(getJComboBoxWidthUnit(), null);
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
					if (enabledChangeListener==true) {
						saveTimeModelToSimulationSetup();
					}
				}
			});
			
		}
		return jTextFieldWidthValue;
	}

	/**
	 * This method initializes jComboBoxWidthUnit	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<TimeUnit> getJComboBoxWidthUnit() {
		if (jComboBoxWidthUnit == null) {
			jComboBoxWidthUnit = new JComboBox<TimeUnit>(new DefaultComboBoxModel<TimeUnit>(new TimeUnitVector()));
			jComboBoxWidthUnit.setPreferredSize(new Dimension(120, 26));
			jComboBoxWidthUnit.setFont(new Font("Dialog", Font.PLAIN, 12));
			jComboBoxWidthUnit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (enabledChangeListener==true) {
						saveTimeModelToSimulationSetup();
					}
				}
			}); 
		}
		return jComboBoxWidthUnit;
	}
	
	/**
	 * Gets the time formater.
	 * @return the time formater
	 */
	private TimeFormatSelection getJPanelTimeFormater() {
		if (jPanelTimeFormater==null) {
			jPanelTimeFormater = new TimeFormatSelection();
			jPanelTimeFormater.setPreferredSize(new Dimension(360, 80));
			jPanelTimeFormater.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					if (enabledChangeListener==true) {
						saveTimeModelToSimulationSetup();					
					}
				}
			});
		}
		return jPanelTimeFormater;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		Calendar calendarWork = Calendar.getInstance();
		
		TimeModelDiscrete timeModelDiscrete=null; 
		if (timeModel==null) {
			timeModelDiscrete = new TimeModelDiscrete();
		} else {
			timeModelDiscrete = (TimeModelDiscrete) timeModel;
		}
		
		this.enabledChangeListener = false;
		// --- Start settings ---------------------------------------
		Date startDate = new Date(timeModelDiscrete.getTimeStart());
		calendarWork.setTime(startDate);
		this.getJSpinnerDateStart().setValue(startDate);
		this.getJSpinnerTimeStart().setValue(startDate);
		this.getJSpinnerMillisStart().setValue(calendarWork.get(Calendar.MILLISECOND));
		
		// --- Stop settings ----------------------------------------
		Date stopDate = new Date(timeModelDiscrete.getTimeStop());
		calendarWork.setTime(stopDate);
		this.getJSpinnerDateStop().setValue(stopDate);
		this.getJSpinnerTimeStop().setValue(stopDate);
		this.getJSpinnerMillisStop().setValue(calendarWork.get(Calendar.MILLISECOND));
		
		// --- Settings for the step width --------------------------
		long step = timeModelDiscrete.getStep();
		int unitSelection = timeModelDiscrete.getStepDisplayUnitAsIndexOfTimeUnitVector();
		TimeUnit timeUnit = (TimeUnit) this.getJComboBoxWidthUnit().getModel().getElementAt(unitSelection);
		Long stepInUnit = step / timeUnit.getFactorToMilliseconds();
		
		this.getJTextFieldWidthValue().setText(stepInUnit.toString());
		this.getJComboBoxWidthUnit().setSelectedIndex(unitSelection);
		
		// --- Settings for the time format -------------------------
		this.getJPanelTimeFormater().setTimeFormat(timeModelDiscrete.getTimeFormat());
		
		this.enabledChangeListener = true;
		
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
		
		// --- Getting Stop time as Long ----------------------------
		Calendar stopCalenderMerged = Calendar.getInstance();
		Date stopDate = (Date) this.getJSpinnerDateStop().getValue();
		Date stopTime = (Date) this.getJSpinnerTimeStop().getValue();
		int stopMillis = (Integer) this.getJSpinnerMillisStop().getValue();

		calendarWork.setTime(stopDate);
		stopCalenderMerged.set(Calendar.DAY_OF_MONTH, calendarWork.get(Calendar.DAY_OF_MONTH));
		stopCalenderMerged.set(Calendar.MONTH, calendarWork.get(Calendar.MONTH));
		stopCalenderMerged.set(Calendar.YEAR, calendarWork.get(Calendar.YEAR));
		calendarWork.setTime(stopTime);
		stopCalenderMerged.set(Calendar.HOUR_OF_DAY, calendarWork.get(Calendar.HOUR_OF_DAY));
		stopCalenderMerged.set(Calendar.MINUTE, calendarWork.get(Calendar.MINUTE));
		stopCalenderMerged.set(Calendar.SECOND, calendarWork.get(Calendar.SECOND));
		stopCalenderMerged.set(Calendar.MILLISECOND, stopMillis);
		Date stop = stopCalenderMerged.getTime();
		Long stopLong = stop.getTime();
		
		// --- Get the current index of the unit list ---------------
		ComboBoxModel<TimeUnit> cbm = this.getJComboBoxWidthUnit().getModel();
		TimeUnit timeUnit = (TimeUnit) cbm.getSelectedItem();
		int indexSelected = 0;
		for (int i = 0; i < cbm.getSize(); i++) {
			if (cbm.getElementAt(i)==timeUnit) {
				indexSelected = i;
				break;
			}
		}
		
		// --- Getting step width -----------------------------------
		String stepString = this.getJTextFieldWidthValue().getText();
		long step;
		long stepInUnit;
		if (stepString==null) {
			step = new Long(0);
		} else if (stepString.equals("")) {
			step = new Long(0);
		} else {
			stepInUnit = Long.parseLong(this.getJTextFieldWidthValue().getText());
			step = stepInUnit * timeUnit.getFactorToMilliseconds();
		}
		
		// --- Getting the time format ------------------------------
		String timeFormat = this.getJPanelTimeFormater().getTimeFormat();
		
		// --- Set TimeModel ----------------------------------------
		TimeModelDiscrete  timeModelDiscrete = new TimeModelDiscrete(startLong, stopLong, step);
		timeModelDiscrete.setStepDisplayUnitAsIndexOfTimeUnitVector(indexSelected);
		timeModelDiscrete.setTimeFormat(timeFormat);
		return timeModelDiscrete;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent ce) {
		if (this.enabledChangeListener==true) {
			Object ceTrigger = ce.getSource();
			if (ceTrigger instanceof JSpinner) {
				this.saveTimeModelToSimulationSetup();	
			}	
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="3,10"
