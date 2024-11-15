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
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import de.enflexit.language.Language;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;
import agentgui.simulationService.time.JPanel4TimeModelConfiguration;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelContinuous;
import de.enflexit.common.swing.JSpinnerDateTime;
import de.enflexit.common.swing.TimeFormatSelection;
import de.enflexit.common.swing.TimeZoneWidget;

/**
 * The Class JPanelTimeModelStroke extends the class {@link JPanel4TimeModelConfiguration}
 * and is used in order to configure the {@link TimeModelContinuous}.
 * 
 * @see TimeModelContinuous
 * @see TimeModelController
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelTimeModelContinuous extends JPanel4TimeModelConfiguration implements ChangeListener {

	private static final long serialVersionUID = -1170433671816358910L;
	
	private JPanel jPanelStartSettings;
	private JPanel jPanelStopSettings;
	private JPanel jPanelWidthSettings;
	
	private JLabel jLabelStart;
	private JLabel jLabelStartDate;
	private JLabel jLabelStartMillis;
	
	private JLabel jLabelStop;
	private JLabel jLabelStopDate;
	private JLabel jLabelStopMillis;
	
	private JLabel jLabelAcceleration;

	private JLabel jLabelTimeZone;
	private JLabel jLabelTimeFormat;
	
	private JSpinnerDateTime jSpinnerDateStart;
	private JSpinner jSpinnerMillisStart;
	private JSpinnerDateTime jSpinnerDateStop;
	private JSpinner jSpinnerMillisStop;
	private JSpinner jSpinnerAcceleration;

	private JSeparator jSeparatorVert;

	private TimeZoneWidget timeZoneWidget;
	private TimeFormatSelection jPanelTimeFormatter;

	protected boolean enabledChangeListener = true;
	
	
	/**
	 * Instantiates a new time model discrete configuration.
	 *
	 * @param project the project
	 * @param timeModelController the time model controller
	 */
	public JPanelTimeModelContinuous(Project project, TimeModelController timeModelController) {
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
	/**
	 * This method initializes this
	 */
	protected void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		this.setSize(900, 90);
		
		GridBagConstraints gbcJLabelStart = new GridBagConstraints();
		gbcJLabelStart.gridx = 0;
		gbcJLabelStart.insets = new Insets(5, 5, 0, 0);
		gbcJLabelStart.anchor = GridBagConstraints.WEST;
		gbcJLabelStart.gridy = 0;
		
		GridBagConstraints gbcJPanelStartSettings = new GridBagConstraints();
		gbcJPanelStartSettings.gridx = 1;
		gbcJPanelStartSettings.gridwidth = 1;
		gbcJPanelStartSettings.anchor = GridBagConstraints.WEST;
		gbcJPanelStartSettings.insets = new Insets(5, 5, 0, 0);
		gbcJPanelStartSettings.gridy = 0;
		
		GridBagConstraints gbcJLableStop = new GridBagConstraints();
		gbcJLableStop.gridx = 0;
		gbcJLableStop.insets = new Insets(5, 5, 0, 0);
		gbcJLableStop.anchor = GridBagConstraints.WEST;
		gbcJLableStop.gridy = 1;
		
		GridBagConstraints gbcJPanelStopSettings = new GridBagConstraints();
		gbcJPanelStopSettings.gridx = 1;
		gbcJPanelStopSettings.anchor = GridBagConstraints.WEST;
		gbcJPanelStopSettings.insets = new Insets(5, 5, 0, 0);
		gbcJPanelStopSettings.gridwidth = 1;
		gbcJPanelStopSettings.gridy = 1;
		
		GridBagConstraints gbcJPanelWidth = new GridBagConstraints();
		gbcJPanelWidth.gridx = 0;
		gbcJPanelWidth.anchor = GridBagConstraints.NORTH;
		gbcJPanelWidth.insets = new Insets(3, 5, 0, 0);
		gbcJPanelWidth.gridwidth = 2;
		gbcJPanelWidth.fill = GridBagConstraints.HORIZONTAL;
		gbcJPanelWidth.gridy = 2;
		
        GridBagConstraints gbc_jSeparatorVert = new GridBagConstraints();
        gbc_jSeparatorVert.fill = GridBagConstraints.VERTICAL;
        gbc_jSeparatorVert.insets = new Insets(5, 5, 0, 5);
        gbc_jSeparatorVert.gridheight = 3;
        gbc_jSeparatorVert.gridx = 2;
        gbc_jSeparatorVert.gridy = 0;
        
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
        gbcTimeFormatter.gridheight = 2;
        gbcTimeFormatter.anchor = GridBagConstraints.NORTHWEST;
        gbcTimeFormatter.gridx = 4;
        gbcTimeFormatter.insets = new Insets(5, 5, 0, 0);
        gbcTimeFormatter.gridy = 1;
        
        
        jLabelStart = new JLabel();
		jLabelStart.setText("Start bei");
		jLabelStart.setText(Language.translate(jLabelStart.getText()) + ":");
		jLabelStart.setFont(new Font("Dialog", Font.BOLD, 11));
		jLabelStart.setPreferredSize(JPanelTimeModelDiscrete.GB_LAYOUT_DIMENSION_FIRST_COLUMN_LABEL);

		jLabelStop = new JLabel();
		jLabelStop.setText("Stop bei");
		jLabelStop.setText(Language.translate(jLabelStop.getText()) + ":");
		jLabelStop.setFont(new Font("Dialog", Font.BOLD, 11));
		jLabelStop.setPreferredSize(JPanelTimeModelDiscrete.GB_LAYOUT_DIMENSION_FIRST_COLUMN_LABEL);
		
	    jLabelTimeZone = new JLabel();
        jLabelTimeZone.setText("Zeitzone");
        jLabelTimeZone.setText(Language.translate(jLabelTimeZone.getText())+ ":");
        jLabelTimeZone.setFont(new Font("Dialog", Font.BOLD, 11));
        
        jLabelTimeFormat = new JLabel();
        jLabelTimeFormat.setText("Zeitformat");
        jLabelTimeFormat.setText(Language.translate(jLabelTimeFormat.getText())+ ":");
        jLabelTimeFormat.setFont(new Font("Dialog", Font.BOLD, 11));
		
		
        this.add(jLabelStart, gbcJLabelStart);
        this.add(getJPanelStartSettings(), gbcJPanelStartSettings);
        this.add(jLabelStop, gbcJLableStop);
        this.add(getJPanelStopSettings(), gbcJPanelStopSettings);
        this.add(getJPanelWidthSettings(), gbcJPanelWidth);
        
        this.add(getJSeparatorVert(), gbc_jSeparatorVert);
        
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
	 * This method initializes jPanelStartSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStartSettings() {
		if (jPanelStartSettings == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.CENTER);
			flowLayout.setVgap(0);
			flowLayout.setHgap(3);
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
			
			jLabelStartDate = new JLabel();
			jLabelStartDate.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStartDate.setText("Datum");
			jLabelStartDate.setPreferredSize(new Dimension(35, 16));
			jLabelStartDate.setText(Language.translate(jLabelStartDate.getText())+ ":");

			jLabelStartMillis = new JLabel();
			jLabelStartMillis.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStartMillis.setText("Millisekunden");
			jLabelStartMillis.setText(Language.translate(jLabelStartMillis.getText())+ ":");

			jPanelStartSettings = new JPanel();
			jPanelStartSettings.setLayout(flowLayout);
			jPanelStartSettings.add(jLabelStartDate, null);
			jPanelStartSettings.add(getJSpinnerDateStart(), null);
			jPanelStartSettings.add(jLabelStartMillis, null);
			jPanelStartSettings.add(getJSpinnerMillisStart(), null);
		}
		return jPanelStartSettings;
	}
	/**
	 * Gets the JSpinner date start.
	 * @return the JSpinner date start
	 */
	private JSpinnerDateTime getJSpinnerDateStart() {
		if (jSpinnerDateStart==null) {
			jSpinnerDateStart = new JSpinnerDateTime("dd.MM.yyyy - HH:mm:ss");;
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
			// --- Just allow number to be typed --------------------
			JFormattedTextField formattedTextField = ((JSpinner.DefaultEditor) jSpinnerMillisStart.getEditor()).getTextField();
			((NumberFormatter) formattedTextField.getFormatter()).setAllowsInvalid(false);
		}
		return jSpinnerMillisStart;
	}
	
	/**
	 * This method initializes jPanelStopSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelStopSettings() {
		if (jPanelStopSettings == null) {
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setHgap(3);
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
			
			jLabelStopDate = new JLabel();
			jLabelStopDate.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStopDate.setText("Datum");
			jLabelStopDate.setPreferredSize(new Dimension(35, 16));
			jLabelStopDate.setText(Language.translate(jLabelStopDate.getText())+ ":");

			jLabelStopMillis = new JLabel();
			jLabelStopMillis.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStopMillis.setText("Millisekunden");
			jLabelStopMillis.setText(Language.translate(jLabelStopMillis.getText())+ ":");
			
			jPanelStopSettings = new JPanel();
			jPanelStopSettings.setLayout(flowLayout2);
			jPanelStopSettings.add(jLabelStopDate, null);
			jPanelStopSettings.add(getJSpinnerDateStop(), null);
			jPanelStopSettings.add(jLabelStopMillis, null);
			jPanelStopSettings.add(getJSpinnerMillisStop(), null);
		}
		return jPanelStopSettings;
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
			// --- Just allow number to be typed --------------------
			JFormattedTextField formattedTextField = ((JSpinner.DefaultEditor) jSpinnerMillisStop.getEditor()).getTextField();
			((NumberFormatter) formattedTextField.getFormatter()).setAllowsInvalid(false);
		}
		return jSpinnerMillisStop;
	}
	
	private JSeparator getJSeparatorVert() {
		if (jSeparatorVert == null) {
			jSeparatorVert = new JSeparator();
			jSeparatorVert.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparatorVert;
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
					JPanelTimeModelContinuous.this.setTimeZone(null);
				}
			});
		}
		return timeZoneWidget;
	}
	
	/**
	 * This method initializes timeFormater	
	 * @return agentgui.simulationService.time.TimeFormatSelection	
	 */
	protected TimeFormatSelection getJPanelTimeFormatter() {
		if (jPanelTimeFormatter == null) {
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
	/**
	 * This method initializes jPanelWidthSettings	
	 * @return javax.swing.JPanel	
	 */
	protected JPanel getJPanelWidthSettings() {
		if (jPanelWidthSettings == null) {
			
			jLabelAcceleration = new JLabel();
			jLabelAcceleration.setFont(new Font("Dialog", Font.BOLD, 11));
			jLabelAcceleration.setText("Beschleunigung");
			jLabelAcceleration.setText(Language.translate(jLabelAcceleration.getText())+ ":");
			jLabelAcceleration.setPreferredSize(new Dimension(109, 20));
			
			GridBagLayout gbl_jPanelWidthSettings = new GridBagLayout();
			gbl_jPanelWidthSettings.columnWidths = new int[]{0, 0, 0, 0};
			gbl_jPanelWidthSettings.rowHeights = new int[]{0, 0};
			gbl_jPanelWidthSettings.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelWidthSettings.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			
			
			GridBagConstraints gbcJLableAcceleration = new GridBagConstraints();
			gbcJLableAcceleration.anchor = GridBagConstraints.WEST;
			gbcJLableAcceleration.insets = new Insets(0, 0, 0, 2);
			gbcJLableAcceleration.gridy = 0;
			gbcJLableAcceleration.gridx = 0;
			
			GridBagConstraints gbcJSpinnerAcceleration = new GridBagConstraints();
			gbcJSpinnerAcceleration.insets = new Insets(0, 5, 0, 0);
			gbcJSpinnerAcceleration.gridy = 0;
			gbcJSpinnerAcceleration.gridx = 1;

			
			jPanelWidthSettings = new JPanel();
			jPanelWidthSettings.setLayout(gbl_jPanelWidthSettings);
			
			jPanelWidthSettings.add(jLabelAcceleration, gbcJLableAcceleration);
			jPanelWidthSettings.add(getJSpinnerAcceleration(), gbcJSpinnerAcceleration);
		}
		return jPanelWidthSettings;
	}
	/**
	 * This method initializes jTextFieldWidthValue	
	 * @return javax.swing.JTextField	
	 */
	private JSpinner getJSpinnerAcceleration() {
		if (jSpinnerAcceleration == null) {
			jSpinnerAcceleration = new JSpinner(new SpinnerNumberModel(1, 0.001, 10000, 0.001));
			jSpinnerAcceleration.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerAcceleration.setEditor(new JSpinner.NumberEditor(jSpinnerAcceleration, "0.000"));
			jSpinnerAcceleration.setPreferredSize(new Dimension(88, 24));
			jSpinnerAcceleration.addChangeListener(this);
			jSpinnerAcceleration.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					Double factor = (Double) getJSpinnerAcceleration().getValue();
					setFactorExplanationText(factor);
				}
			});
			// --- Just allow number to be typed --------------------
			JFormattedTextField formattedTextField = ((JSpinner.DefaultEditor) jSpinnerAcceleration.getEditor()).getTextField();
			((NumberFormatter) formattedTextField.getFormatter()).setAllowsInvalid(false);
		}
		return jSpinnerAcceleration;
	}
	
	/**
	 * Sets the factor explanation text.
	 * @param factor the new factor explanation text
	 */
	private void setFactorExplanationText(Double factor) {
		
		if (factor!=null) {

			String stringSimulated = Language.translate("simulierte");
			String stringrealTime = Language.translate("realer Zeit");
			
			String stringSecond = Language.translate("Sekunde");
			String stringMinute = Language.translate("Minute");
			String stringHour = Language.translate("Stunde");
			
			String stringSeconds = Language.translate("Sekunden");
			String stringMinutes = Language.translate("Minuten");
			String stringHours = Language.translate("Stunden");
			
			Double relationSeconds = 1.0 / factor;
			Double relationMinutes = 0.0;
			Double relationHours = 0.0;

			String textSeconds =  this.round(relationSeconds) + " " + stringSeconds+ " " + stringrealTime;
			String textMinutes = null;
			String textHours = null;
			
			if (relationSeconds<1.0) {
				relationMinutes = this.round(relationSeconds*60.0);
				textMinutes = relationMinutes + " " + stringSeconds + " " + stringrealTime;
				
				relationHours = this.round(relationSeconds*60.0);
				textHours = relationHours + " " + stringMinutes + " " + stringrealTime;
				
			} else {
				relationMinutes = this.round(relationSeconds);
				textMinutes = relationMinutes + " " + stringMinutes + " " + stringrealTime;
				
				relationHours = this.round(relationSeconds);
				textHours = relationHours + " " + stringHours + " " + stringrealTime;
				
			}
			
//			jLabelFactorInfoSeconds.setText("1 " + stringSimulated + " " + stringSecond + " = " + textSeconds);
//			jLabelFactorInfoMinutes.setText("1 " + stringSimulated + " " + stringMinute + " = " + textMinutes);
//			jLabelFactorInfoHour.setText("1 " + stringSimulated + " " + stringHour + " = " + textHours);

			String explanation = "1 " + stringSimulated + " " + stringSecond + " = " + textSeconds + ", ";
			explanation += "1 " + stringSimulated + " " + stringMinute + " = " + textMinutes + ", ";
			explanation += "1 " + stringSimulated + " " + stringHour + " = " + textHours;
			this.getJSpinnerAcceleration().setToolTipText(explanation);
		}
	}
	/**
	 * Rounds a double value two digits after the comma.
	 *
	 * @param doubleValue the double value
	 * @return the double
	 */
	private Double round(Double doubleValue) {
		return Math.round(doubleValue*1000.0) / 1000.0;
	}
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		TimeModelContinuous timeModelContinuous=null; 
		if (timeModel==null) {
			timeModelContinuous = new TimeModelContinuous();
		} else {
			timeModelContinuous = (TimeModelContinuous) timeModel;
		}
		
		this.enabledChangeListener = false;
		Calendar calendarWork = Calendar.getInstance();
		
		// --- Set time zone ----------------------------------------
		this.getTimeZoneWidget().setZoneId(timeModelContinuous.getZoneId());
		
		// --- Start settings ---------------------------------------
		Date startDate = new Date(timeModelContinuous.getTimeStart());
		calendarWork.setTime(startDate);
		this.getJSpinnerDateStart().setValue(startDate);
		this.getJSpinnerMillisStart().setValue(calendarWork.get(Calendar.MILLISECOND));
		
		// --- Stop settings ----------------------------------------
		Date stopDate = new Date(timeModelContinuous.getTimeStop());
		calendarWork.setTime(stopDate);
		this.getJSpinnerDateStop().setValue(stopDate);
		this.getJSpinnerMillisStop().setValue(calendarWork.get(Calendar.MILLISECOND));

		// --- Settings for the time format -------------------------
		this.getJPanelTimeFormatter().setTimeFormat(timeModelContinuous.getTimeFormat());
		
		// --- Settings for the acceleration of the time ------------
		Double factor = timeModelContinuous.getAccelerationFactor();
		this.getJSpinnerAcceleration().setValue(factor);
		this.setFactorExplanationText(factor);
		
		this.enabledChangeListener = true;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.DisplayJPanel4Configuration#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {

		Calendar calendarWork = Calendar.getInstance();
		
		// --- Get Start time as Long -------------------------------
		Date startDate = (Date) this.getJSpinnerDateStart().getValue();
		int startMillis = (Integer) this.getJSpinnerMillisStart().getValue();
		
		calendarWork.setTime(startDate);
		calendarWork.set(Calendar.MILLISECOND, startMillis);
		Long startLong = calendarWork.getTimeInMillis();
		
		
		// --- Get Stop time as Long --------------------------------
		Date stopDate = (Date) this.getJSpinnerDateStop().getValue();
		int stopMillis = (Integer) this.getJSpinnerMillisStop().getValue();
		
		calendarWork.setTime(stopDate);
		calendarWork.set(Calendar.MILLISECOND, stopMillis);
		Long stopLong = calendarWork.getTimeInMillis();
		
		
		// --- Get ZoneId and time format ---------------------------
		ZoneId zoneId = this.getTimeZoneWidget().getZoneId();
		String timeFormat = this.getJPanelTimeFormatter().getTimeFormat();
		
		// --- Getting acceleration for the time --------------------
		Double factor = (Double) this.getJSpinnerAcceleration().getValue();
		
		
		// --- Prepare return value ---------------------------------
		TimeModelContinuous  tmc = null;
		if (this.getTimeModelController().getTimeModel() instanceof TimeModelContinuous) {
			tmc = (TimeModelContinuous) this.getTimeModelController().getTimeModel(); 
		} else {
			tmc = new TimeModelContinuous();
		}
		tmc.setTimeStart(startLong);
		tmc.setTimeStop(stopLong);
		tmc.setAccelerationFactor(factor);
		tmc.setZoneId(zoneId);
		tmc.setTimeFormat(timeFormat);
		return tmc;
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
