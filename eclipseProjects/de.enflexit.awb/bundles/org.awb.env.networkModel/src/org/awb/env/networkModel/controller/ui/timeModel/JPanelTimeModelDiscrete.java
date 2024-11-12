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

import java.awt.Color;
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
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.language.Language;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;
import agentgui.simulationService.time.JPanel4TimeModelConfiguration;
import agentgui.simulationService.time.TimeFormatSelection;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDiscrete;
import agentgui.simulationService.time.TimeModelStroke;
import agentgui.simulationService.time.TimeUnit;
import agentgui.simulationService.time.TimeUnitVector;
import de.enflexit.common.swing.JSpinnerDateTime;
import de.enflexit.common.swing.TimeZoneWidget;

/**
 * The Class JPanelTimeModelDiscrete enables to configure the current {@link TimeModel} or {@link TimeModelDiscrete} 
 * respectively within the configuration of the current graph- or network model environment.
 * 
 * @see TimeModelStroke
 * @see TimeModelController
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelTimeModelDiscrete extends JPanel4TimeModelConfiguration implements ChangeListener {

	private static final long serialVersionUID = -1170433671816358910L;
	
	public static final Dimension GB_LAYOUT_DIMENSION_FIRST_COLUMN_LABEL = new Dimension(70, 24);
	
	private JLabel jLabelStop;
	private JLabel jLabelStart;
	private JLabel jLabelStartDate;
	private JLabel jLabelStopDate;
	private JLabel jLabelStartMillis;
	private JLabel jLabelStopMillis;
	
	private JPanel jPanelStartSettings;
	private JPanel jPanelStopSettings;
	private JPanel jPanelWidthSettings;
	
	private JLabel jLabelWidth;
	private JLabel jLabelWidthStep;
	private JLabel jLabelWidthUnit;
	private JLabel jLabelTimeZone;
	private JLabel jLabelTimeFormat;
	
	private JTextField jTextFieldWidthValue;
	private JComboBox<TimeUnit> jComboBoxWidthUnit;

	private JSpinnerDateTime jSpinnerDateStart;
	private JSpinner jSpinnerMillisStart;
	private JSpinnerDateTime jSpinnerDateStop;
	private JSpinner jSpinnerMillisStop;

	private JSeparator jSeparatorVertFirst;
	
	private TimeZoneWidget timeZoneWidget;
	private TimeFormatSelection jPanelTimeFormatter;
	
	private boolean enabledChangeListener = true;
	

	
	/**
	 * Instantiates a new time model discrete configuration.
	 *
	 * @param project the project
	 * @param timeModelController the time model controller
	 */
	public JPanelTimeModelDiscrete(Project project, TimeModelController timeModelController) {
		super(project, timeModelController);
		this.initialize();
	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(900, 95);
	}
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		this.setSize(867, 90);
		
		GridBagConstraints gbcJLabelStopTime = new GridBagConstraints();
		gbcJLabelStopTime.gridx = 0;
		gbcJLabelStopTime.insets = new Insets(3, 5, 0, 0);
		gbcJLabelStopTime.anchor = GridBagConstraints.WEST;
		gbcJLabelStopTime.gridy = 1;
		
		GridBagConstraints gbcJLabelStartTime = new GridBagConstraints();
		gbcJLabelStartTime.gridx = 0;
		gbcJLabelStartTime.insets = new Insets(5, 5, 0, 0);
		gbcJLabelStartTime.anchor = GridBagConstraints.WEST;
		gbcJLabelStartTime.gridy = 0;
		
		GridBagConstraints gbcJPanelStartTime = new GridBagConstraints();
		gbcJPanelStartTime.gridx = 1;
		gbcJPanelStartTime.anchor = GridBagConstraints.WEST;
		gbcJPanelStartTime.insets = new Insets(5, 5, 0, 0);
		gbcJPanelStartTime.gridy = 0;
		
		GridBagConstraints gbcJPanelStopTime = new GridBagConstraints();
		gbcJPanelStopTime.gridx = 1;
		gbcJPanelStopTime.fill = GridBagConstraints.NONE;
		gbcJPanelStopTime.anchor = GridBagConstraints.WEST;
		gbcJPanelStopTime.insets = new Insets(3, 5, 0, 0);
		gbcJPanelStopTime.gridy = 1;
		
        GridBagConstraints gbc_jLabelWidth = new GridBagConstraints();
        gbc_jLabelWidth.insets = new Insets(3, 5, 0, 0);
        gbc_jLabelWidth.gridx = 0;
        gbc_jLabelWidth.gridy = 2;
        
        GridBagConstraints gbcTimeIncrement = new GridBagConstraints();
        gbcTimeIncrement.anchor = GridBagConstraints.WEST;
        gbcTimeIncrement.gridx = 1;
        gbcTimeIncrement.insets = new Insets(3, 5, 0, 0);
        gbcTimeIncrement.gridy = 2;

        GridBagConstraints gbc_SeparatorVertFirst = new GridBagConstraints();
        gbc_SeparatorVertFirst.insets = new Insets(5, 5, 0, 5);
        gbc_SeparatorVertFirst.fill = GridBagConstraints.VERTICAL;
        gbc_SeparatorVertFirst.gridheight = 3;
        gbc_SeparatorVertFirst.gridx = 2;
        gbc_SeparatorVertFirst.gridy = 0;
        
        GridBagConstraints gbc_jLabelTimeZone = new GridBagConstraints();
        gbc_jLabelTimeZone.insets = new Insets(5, 0, 0, 0);
        gbc_jLabelTimeZone.anchor = GridBagConstraints.WEST;
        gbc_jLabelTimeZone.gridx = 3;
        gbc_jLabelTimeZone.gridy = 0;

        GridBagConstraints gbc_timeZoneWidget = new GridBagConstraints();
        gbc_timeZoneWidget.insets = new Insets(5, 5, 0, 0);
        gbc_timeZoneWidget.fill = GridBagConstraints.BOTH;
        gbc_timeZoneWidget.gridx = 4;
        gbc_timeZoneWidget.gridy = 0;
        
        GridBagConstraints gbc_jLabelTimeFormat = new GridBagConstraints();
        gbc_jLabelTimeFormat.insets = new Insets(5, 0, 0, 0);
        gbc_jLabelTimeFormat.anchor = GridBagConstraints.WEST;
        gbc_jLabelTimeFormat.gridx = 3;
        gbc_jLabelTimeFormat.gridy = 1;
        
        GridBagConstraints gbcTimeFormatter = new GridBagConstraints();
        gbcTimeFormatter.insets = new Insets(5, 5, 0, 0);
        gbcTimeFormatter.anchor = GridBagConstraints.NORTHWEST;
        gbcTimeFormatter.gridwidth = 1;
        gbcTimeFormatter.gridheight = 2;
        gbcTimeFormatter.gridx = 4;
        gbcTimeFormatter.gridy = 1;
        
        
    	jLabelStart = new JLabel();
		jLabelStart.setText("Start bei");
		jLabelStart.setText(Language.translate(jLabelStart.getText()) + ":");
		jLabelStart.setFont(new Font("Dialog", Font.BOLD, 11));
		jLabelStart.setPreferredSize(GB_LAYOUT_DIMENSION_FIRST_COLUMN_LABEL);
		
		jLabelStop = new JLabel();
		jLabelStop.setText("Stop bei");
		jLabelStop.setText(Language.translate(jLabelStop.getText())+ ":");
		jLabelStop.setFont(new Font("Dialog", Font.BOLD, 11));
		jLabelStop.setPreferredSize(GB_LAYOUT_DIMENSION_FIRST_COLUMN_LABEL);
		
        jLabelWidth = new JLabel();
        jLabelWidth.setFont(new Font("Dialog", Font.BOLD, 11));
        jLabelWidth.setText("Schrittweite");
        jLabelWidth.setText(Language.translate(jLabelWidth.getText())+ ":");
        jLabelWidth.setPreferredSize(GB_LAYOUT_DIMENSION_FIRST_COLUMN_LABEL);
    
        jLabelTimeZone = new JLabel();
        jLabelTimeZone.setText("Zeitzone");
        jLabelTimeZone.setText(Language.translate(jLabelTimeZone.getText())+ ":");
        jLabelTimeZone.setFont(new Font("Dialog", Font.BOLD, 11));
        
        jLabelTimeFormat = new JLabel();
        jLabelTimeFormat.setText("Zeitformat");
        jLabelTimeFormat.setText(Language.translate(jLabelTimeFormat.getText())+ ":");
        jLabelTimeFormat.setFont(new Font("Dialog", Font.BOLD, 11));

        
		this.add(jLabelStart, gbcJLabelStartTime);
		this.add(getJPanelStartSettings(), gbcJPanelStartTime);
		this.add(jLabelStop, gbcJLabelStopTime);
		this.add(getJPanelStopSettings(), gbcJPanelStopTime);
		this.add(jLabelWidth, gbc_jLabelWidth);
		this.add(getJPanelWidthSettings(), gbcTimeIncrement);
		
		this.add(getJSeparatorVertFirst(), gbc_SeparatorVertFirst);
		
		this.add(jLabelTimeZone, gbc_jLabelTimeZone);
		this.add(getTimeZoneWidget(), gbc_timeZoneWidget);
		this.add(jLabelTimeFormat, gbc_jLabelTimeFormat);
		this.add(getJPanelTimeFormatter(), gbcTimeFormatter);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setBackground(java.awt.Color)
	 */
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		this.getJPanelStartSettings().setBackground(bg);
		this.getJPanelStopSettings().setBackground(bg);
		this.getJPanelWidthSettings().setBackground(bg);
		this.getTimeZoneWidget().setBackground(bg);
		this.getJPanelTimeFormatter().setBackground(bg);
	}
	
	/**
	 * Gets the JSpinner date start.
	 * @return the JSpinner date start
	 */
	private JSpinnerDateTime getJSpinnerDateStart() {
		if (jSpinnerDateStart==null) {
			jSpinnerDateStart = new JSpinnerDateTime("dd.MM.yyyy - HH:mm:ss");
			jSpinnerDateStart.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerDateStart.setPreferredSize(new Dimension(150, 24));
			jSpinnerDateStart.addChangeListener(this);
		}
		return jSpinnerDateStart;
	}
	/**
	 * Gets the JSpinner milliseconds start.
	 * @return the JSpinner milliseconds start
	 */
	private JSpinner getJSpinnerMillisStart() {
		if (jSpinnerMillisStart==null) {
			jSpinnerMillisStart = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
			jSpinnerMillisStart.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerMillisStart.setEditor(new JSpinner.NumberEditor(jSpinnerMillisStart, "000"));
			jSpinnerMillisStart.setPreferredSize(new Dimension(55, 24));
			jSpinnerMillisStart.addChangeListener(this);
		}
		return jSpinnerMillisStart;
	}
	/**
	 * Gets the JSpinner date stop.
	 * @return the JSpinner date stop
	 */
	private JSpinnerDateTime getJSpinnerDateStop() {
		if (jSpinnerDateStop==null) {
			jSpinnerDateStop = new JSpinnerDateTime("dd.MM.yyyy - HH:mm:ss");
			jSpinnerDateStop.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerDateStop.setPreferredSize(new Dimension(150, 24));
			jSpinnerDateStop.addChangeListener(this);
		}
		return jSpinnerDateStop;
	}
	/**
	 * Gets the JSpinner milliseconds stop.
	 * @return the JSpinner milliseconds stop
	 */
	private JSpinner getJSpinnerMillisStop() {
		if (jSpinnerMillisStop==null) {
			jSpinnerMillisStop = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
			jSpinnerMillisStop.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerMillisStop.setEditor(new JSpinner.NumberEditor(jSpinnerMillisStop, "000"));
			jSpinnerMillisStop.setPreferredSize(new Dimension(55, 24));
			jSpinnerMillisStop.addChangeListener(this);
		}
		return jSpinnerMillisStop;
	}
	
	/**
	 * This method initializes jPanelStartSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStartSettings() {
		if (jPanelStartSettings == null) {

			jLabelStartDate = new JLabel();
			jLabelStartDate.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStartDate.setText("Datum");
			jLabelStartDate.setText(Language.translate(jLabelStartDate.getText())+ ":");
			jLabelStartDate.setPreferredSize(new Dimension(35, 16));
			
			jLabelStartMillis = new JLabel();
			jLabelStartMillis.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStartMillis.setText("Millisekunden");
			jLabelStartMillis.setText(Language.translate(jLabelStartMillis.getText())+ ":");
			
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			flowLayout.setVgap(0);
			flowLayout.setHgap(3);
			
			jPanelStartSettings = new JPanel();
			jPanelStartSettings.setLayout(flowLayout);
			jPanelStartSettings.add(jLabelStartDate);
			jPanelStartSettings.add(getJSpinnerDateStart());
			jPanelStartSettings.add(jLabelStartMillis);
			jPanelStartSettings.add(getJSpinnerMillisStart());
		}
		return jPanelStartSettings;
	}
	/**
	 * This method initializes jPanelStopSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStopSettings() {
		if (jPanelStopSettings == null) {
			
			jLabelStopDate = new JLabel();
			jLabelStopDate.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStopDate.setText("Datum");
			jLabelStopDate.setText(Language.translate(jLabelStopDate.getText())+ ":");
			jLabelStopDate.setPreferredSize(new Dimension(35, 16));
			
			jLabelStopMillis = new JLabel();
			jLabelStopMillis.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStopMillis.setText("Millisekunden");
			jLabelStopMillis.setText(Language.translate(jLabelStopMillis.getText())+ ":");
			
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			flowLayout.setVgap(0);
			flowLayout.setHgap(3);
			
			jPanelStopSettings = new JPanel();
			jPanelStopSettings.setLayout(flowLayout);
			jPanelStopSettings.add(jLabelStopDate);
			jPanelStopSettings.add(getJSpinnerDateStop());
			jPanelStopSettings.add(jLabelStopMillis);
			jPanelStopSettings.add(getJSpinnerMillisStop());
		}
		return jPanelStopSettings;
	}

	/**
	 * This method initializes jPanelWidthSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelWidthSettings() {
		if (jPanelWidthSettings == null) {
			
			jLabelWidthStep = new JLabel();
			jLabelWidthStep.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelWidthStep.setText("Wert");
			jLabelWidthStep.setText(Language.translate(jLabelWidthStep.getText())+ ":");
			jLabelWidthStep.setPreferredSize(new Dimension(35, 16));
			
			jLabelWidthUnit = new JLabel();
			jLabelWidthUnit.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelWidthUnit.setText("Einheit");
			jLabelWidthUnit.setText(Language.translate(jLabelWidthUnit.getText())+ ":");
			
			
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.CENTER);
			flowLayout.setVgap(0);
			flowLayout.setHgap(3);
			
			jPanelWidthSettings = new JPanel();
			jPanelWidthSettings.setLayout(flowLayout);
			jPanelWidthSettings.add(jLabelWidthStep);
			jPanelWidthSettings.add(getJTextFieldWidthValue());
			jPanelWidthSettings.add(jLabelWidthUnit);
			jPanelWidthSettings.add(getJComboBoxWidthUnit());
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
			jTextFieldWidthValue.setFont(new Font("Dialog. Font", Font.PLAIN, 11));
			jTextFieldWidthValue.setBorder(BorderFactory.createEtchedBorder());
			jTextFieldWidthValue.setPreferredSize(new Dimension(88, 24));
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
			jComboBoxWidthUnit.setPreferredSize(new Dimension(100, 24));
			jComboBoxWidthUnit.setFont(new Font("Dialog", Font.PLAIN, 11));
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
	
	private JSeparator getJSeparatorVertFirst() {
		if (jSeparatorVertFirst == null) {
			jSeparatorVertFirst = new JSeparator();
			jSeparatorVertFirst.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparatorVertFirst;
	}
	
	
	private TimeZoneWidget getTimeZoneWidget() {
		if (timeZoneWidget==null) {
			timeZoneWidget = new TimeZoneWidget(null, false);
			timeZoneWidget.setFont(new Font("Dialog", Font.BOLD, 11));
			timeZoneWidget.setPreferredSize(new Dimension(60, 24));
			timeZoneWidget.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// --- TimeZone was changed ---------------------
					JPanelTimeModelDiscrete.this.setTimeZone(null);
				}
			});
		}
		return timeZoneWidget;
	}
	
	/**
	 * Gets the time formatter.
	 * @return the time formatter
	 */
	private TimeFormatSelection getJPanelTimeFormatter() {
		if (jPanelTimeFormatter==null) {
			jPanelTimeFormatter = new TimeFormatSelection(false, 3, new Font("Dialog", Font.PLAIN, 11));
			jPanelTimeFormatter.setPreferredSize(new Dimension(370, 51));
			jPanelTimeFormatter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					if (enabledChangeListener==true) {
						saveTimeModelToSimulationSetup();					
					}
				}
			});
		}
		return jPanelTimeFormatter;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		TimeModelDiscrete timeModelDiscrete=null; 
		if (timeModel==null) {
			timeModelDiscrete = new TimeModelDiscrete();
		} else {
			timeModelDiscrete = (TimeModelDiscrete) timeModel;
		}
		
		this.enabledChangeListener = false;
		Calendar calendarWork = Calendar.getInstance();

		// --- Time zone settings -----------------------------------
		this.getTimeZoneWidget().setZoneId(timeModelDiscrete.getZoneId());
		
		// --- Start settings ---------------------------------------
		Date startDate = new Date(timeModelDiscrete.getTimeStart());
		calendarWork.setTime(startDate);
		this.getJSpinnerDateStart().setValue(startDate);
		this.getJSpinnerMillisStart().setValue(calendarWork.get(Calendar.MILLISECOND));
		
		// --- Stop settings ----------------------------------------
		Date stopDate = new Date(timeModelDiscrete.getTimeStop());
		calendarWork.setTime(stopDate);
		this.getJSpinnerDateStop().setValue(stopDate);
		this.getJSpinnerMillisStop().setValue(calendarWork.get(Calendar.MILLISECOND));
		
		// --- Settings for the time format -------------------------
		this.getJPanelTimeFormatter().setTimeFormat(timeModelDiscrete.getTimeFormat());

		// --- Settings for the step width --------------------------
		long step = timeModelDiscrete.getStep();
		int unitSelection = timeModelDiscrete.getStepDisplayUnitAsIndexOfTimeUnitVector();
		TimeUnit timeUnit = (TimeUnit) this.getJComboBoxWidthUnit().getModel().getElementAt(unitSelection);
		Long stepInUnit = step / timeUnit.getFactorToMilliseconds();
		
		this.getJTextFieldWidthValue().setText(stepInUnit.toString());
		this.getJComboBoxWidthUnit().setSelectedIndex(unitSelection);
		
		this.enabledChangeListener = true;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {

		Calendar calendarWork = Calendar.getInstance();
		
		// --- Getting Start time as Long ---------------------------
		Date startDate = (Date) this.getJSpinnerDateStart().getValue();
		int startMillis = (Integer) this.getJSpinnerMillisStart().getValue();
		
		calendarWork.setTime(startDate);
		calendarWork.set(Calendar.MILLISECOND, startMillis);
		Long startLong = calendarWork.getTimeInMillis();
		
		
		// --- Getting Stop time as Long ----------------------------
		Date stopDate = (Date) this.getJSpinnerDateStop().getValue();
		int stopMillis = (Integer) this.getJSpinnerMillisStop().getValue();

		calendarWork.setTime(stopDate);
		calendarWork.set(Calendar.MILLISECOND, stopMillis);
		Long stopLong = calendarWork.getTimeInMillis();
		
		
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
			step =  Long.valueOf(0);
		} else if (stepString.equals("")) {
			step = Long.valueOf(0);
		} else {
			stepInUnit = Long.parseLong(this.getJTextFieldWidthValue().getText());
			step = stepInUnit * timeUnit.getFactorToMilliseconds();
		}
		
		// --- Get ZoneId and time format ---------------------------
		ZoneId zoneId = this.getTimeZoneWidget().getZoneId();
		String timeFormat = this.getJPanelTimeFormatter().getTimeFormat();
		
		// --- Prepare return value ---------------------------------
		TimeModelDiscrete  timeModelDiscrete = null;
		if (this.getTimeModelController().getTimeModel() instanceof TimeModelDiscrete) {
			timeModelDiscrete = (TimeModelDiscrete) this.getTimeModelController().getTimeModel(); 
		} else {
			timeModelDiscrete = new TimeModelDiscrete();
		}
		timeModelDiscrete.setTimeStart(startLong);
		timeModelDiscrete.setTimeStop(stopLong);
		timeModelDiscrete.setStep(step);
		timeModelDiscrete.setStepDisplayUnitAsIndexOfTimeUnitVector(indexSelected);
		timeModelDiscrete.setZoneId(zoneId);
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

	
	/**
	 * Returns the current time zone.
	 * @return the time zone
	 */
	private TimeZone getTimeZone() {
		return TimeZone.getTimeZone(this.getTimeZoneWidget().getZoneId());
	}
	/**
	 * Sets the time zone for the current configuration.
	 * @param newTimeZone the new time zone. May be null. For this, the ZoneId/TimeZone will be taken from the local {@link TimeZoneWidget}. 
	 * {@link #getTimeZoneWidget()}
	 */
	private void setTimeZone(TimeZone newTimeZone) {
	
		if (newTimeZone==null) {
			newTimeZone = this.getTimeZone();
			if (newTimeZone==null) return;
		}
		
		// --- Set TimeZone to local date spinner -------------------
		this.getJSpinnerDateStart().setTimeZone(newTimeZone);
		this.getJSpinnerDateStop().setTimeZone(newTimeZone);
		
		// --- Set the new TimeZone to the time zone widget? --------
		TimeZone oldTimeZone = this.getTimeZoneWidget().getTimeZone();
		if (newTimeZone.equals(oldTimeZone)==false) {
			this.getTimeZoneWidget().setTimeZone(newTimeZone);
			// --- Something to do? --------------
		}
	}
	
} 
