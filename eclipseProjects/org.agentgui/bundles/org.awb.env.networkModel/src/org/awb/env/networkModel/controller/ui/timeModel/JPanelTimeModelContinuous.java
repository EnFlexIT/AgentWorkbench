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
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;

import agentgui.core.application.Language;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;
import agentgui.simulationService.time.JPanel4TimeModelConfiguration;
import agentgui.simulationService.time.TimeFormatSelection;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelContinuous;

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
	private JLabel jLabelStartTime;
	private JLabel jLabelStartMillis;
	
	private JLabel jLabelStop;
	private JLabel jLabelStopDate;
	private JLabel jLabelStopTime;
	private JLabel jLabelStopMillis;
	
	private JLabel jLabelAcceleration;
	private JLabel jLabelFactorInfoSeconds;

	private JSpinner jSpinnerDateStart;
	private JSpinner jSpinnerTimeStart;
	private JSpinner jSpinnerMillisStart;
	private JSpinner jSpinnerDateStop;
	private JSpinner jSpinnerTimeStop;
	private JSpinner jSpinnerMillisStop;
	private JSpinner jSpinnerAcceleration;

	protected boolean enabledChangeListener = true;

	private TimeFormatSelection jPanelTimeFormatter;
	private JSeparator jSeparatorVert;
	
	
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
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		gbcJPanelWidth.gridwidth = 4;
		gbcJPanelWidth.fill = GridBagConstraints.HORIZONTAL;
		gbcJPanelWidth.gridy = 2;
		
        GridBagConstraints gbc_jSeparatorVert = new GridBagConstraints();
        gbc_jSeparatorVert.fill = GridBagConstraints.VERTICAL;
        gbc_jSeparatorVert.insets = new Insets(5, 5, 0, 5);
        gbc_jSeparatorVert.gridheight = 2;
        gbc_jSeparatorVert.gridx = 2;
        gbc_jSeparatorVert.gridy = 0;
        
        GridBagConstraints gbcTimeFormatter = new GridBagConstraints();
        gbcTimeFormatter.gridheight = 2;
        gbcTimeFormatter.anchor = GridBagConstraints.NORTHWEST;
        gbcTimeFormatter.gridx = 3;
        gbcTimeFormatter.insets = new Insets(5, 5, 0, 0);
        gbcTimeFormatter.gridy = 0;
        
        
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
		
        this.add(jLabelStart, gbcJLabelStart);
        this.add(getJPanelStartSettings(), gbcJPanelStartSettings);
        this.add(jLabelStop, gbcJLableStop);
        this.add(getJPanelStopSettings(), gbcJPanelStopSettings);
        this.add(getJPanelWidthSettings(), gbcJPanelWidth);
        this.add(getJSeparatorVert(), gbc_jSeparatorVert);
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
			jLabelStartTime = new JLabel();
			jLabelStartTime.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStartTime.setText("Uhrzeit");
			jLabelStartTime.setText(Language.translate(jLabelStartTime.getText())+ ":");
			jLabelStartMillis = new JLabel();
			jLabelStartMillis.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStartMillis.setText("Millisekunden");
			jLabelStartMillis.setText(Language.translate(jLabelStartMillis.getText())+ ":");

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
	 * Gets the JSpinner date start.
	 * @return the JSpinner date start
	 */
	private JSpinner getJSpinnerDateStart() {
		if (jSpinnerDateStart==null) {
			jSpinnerDateStart = new JSpinner(new SpinnerDateModel());
			jSpinnerDateStart.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerDateStart.setEditor(new JSpinner.DateEditor(jSpinnerDateStart, "dd.MM.yyyy"));
			jSpinnerDateStart.setPreferredSize(new Dimension(85, 24));
			jSpinnerDateStart.addChangeListener(this);
			// --- Just allow number to be typed --------------------
			JFormattedTextField formattedTextField = ((JSpinner.DateEditor) jSpinnerDateStart.getEditor()).getTextField();
			((DateFormatter) formattedTextField.getFormatter()).setAllowsInvalid(false);
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
			jSpinnerTimeStart.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerTimeStart.setEditor(new JSpinner.DateEditor(jSpinnerTimeStart, "HH:mm:ss"));
			jSpinnerTimeStart.setPreferredSize(new Dimension(75, 24));
			jSpinnerTimeStart.addChangeListener(this);
			// --- Just allow number to be typed --------------------
			JFormattedTextField formattedTextField = ((JSpinner.DateEditor) jSpinnerTimeStart.getEditor()).getTextField();
			((DateFormatter) formattedTextField.getFormatter()).setAllowsInvalid(false);
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
			jLabelStopTime = new JLabel();
			jLabelStopTime.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStopTime.setText("Uhrzeit");
			jLabelStopTime.setText(Language.translate(jLabelStopTime.getText())+ ":");
			jLabelStopMillis = new JLabel();
			jLabelStopMillis.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelStopMillis.setText("Millisekunden");
			jLabelStopMillis.setText(Language.translate(jLabelStopMillis.getText())+ ":");
			
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
	 * Gets the JSpinner date stop.
	 * @return the JSpinner date stop
	 */
	private JSpinner getJSpinnerDateStop() {
		if (jSpinnerDateStop==null) {
			jSpinnerDateStop = new JSpinner(new SpinnerDateModel());
			jSpinnerDateStop.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerDateStop.setEditor(new JSpinner.DateEditor(jSpinnerDateStop, "dd.MM.yyyy"));
			jSpinnerDateStop.setPreferredSize(new Dimension(85, 24));
			jSpinnerDateStop.addChangeListener(this);
			// --- Just allow number to be typed --------------------
			JFormattedTextField formattedTextField = ((JSpinner.DateEditor) jSpinnerDateStop.getEditor()).getTextField();
			((DateFormatter) formattedTextField.getFormatter()).setAllowsInvalid(false);
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
			jSpinnerTimeStop.setFont(new Font("Dialog", Font.PLAIN, 11));
			jSpinnerTimeStop.setEditor(new JSpinner.DateEditor(jSpinnerTimeStop, "HH:mm:ss"));
			jSpinnerTimeStop.setPreferredSize(new Dimension(75, 24));
			jSpinnerTimeStop.addChangeListener(this);
			// --- Just allow number to be typed --------------------
			JFormattedTextField formattedTextField = ((JSpinner.DateEditor) jSpinnerTimeStop.getEditor()).getTextField();
			((DateFormatter) formattedTextField.getFormatter()).setAllowsInvalid(false);
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
	/**
	 * This method initializes timeFormater	
	 * @return agentgui.simulationService.time.TimeFormatSelection	
	 */
	protected TimeFormatSelection getJPanelTimeFormatter() {
		if (jPanelTimeFormatter == null) {
			jPanelTimeFormatter = new TimeFormatSelection(true, 3, new Font("Dialog", Font.PLAIN, 11));
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
			
			jLabelFactorInfoSeconds = new JLabel();
			jLabelFactorInfoSeconds.setFont(new Font("Dialog", Font.PLAIN, 11));
			jLabelFactorInfoSeconds.setText("Explanation");
			
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
			
			GridBagConstraints gbcInfoSeconds = new GridBagConstraints();
			gbcInfoSeconds.insets = new Insets(0, 3, 0, 0);
			gbcInfoSeconds.gridy = 0;
			gbcInfoSeconds.anchor = GridBagConstraints.WEST;
			gbcInfoSeconds.fill = GridBagConstraints.HORIZONTAL;
			gbcInfoSeconds.weightx = 0.0;
			gbcInfoSeconds.gridx = 2;

			
			jPanelWidthSettings = new JPanel();
			jPanelWidthSettings.setLayout(gbl_jPanelWidthSettings);
			
			jPanelWidthSettings.add(jLabelAcceleration, gbcJLableAcceleration);
			jPanelWidthSettings.add(getJSpinnerAcceleration(), gbcJSpinnerAcceleration);
			jPanelWidthSettings.add(jLabelFactorInfoSeconds, gbcInfoSeconds);
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
			jSpinnerAcceleration.setPreferredSize(new Dimension(85, 24));
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
			jLabelFactorInfoSeconds.setText(explanation);
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
		
		Calendar calendarWork = Calendar.getInstance();
		
		TimeModelContinuous timeModelContinuous=null; 
		if (timeModel==null) {
			timeModelContinuous = new TimeModelContinuous();
		} else {
			timeModelContinuous = (TimeModelContinuous) timeModel;
		}
		
		this.enabledChangeListener = false;
		
		// --- Start settings ---------------------------------------
		Date startDate = new Date(timeModelContinuous.getTimeStart());
		calendarWork.setTime(startDate);
		this.getJSpinnerDateStart().setValue(startDate);
		this.getJSpinnerTimeStart().setValue(startDate);
		this.getJSpinnerMillisStart().setValue(calendarWork.get(Calendar.MILLISECOND));
		
		// --- Stop settings ----------------------------------------
		Date stopDate = new Date(timeModelContinuous.getTimeStop());
		calendarWork.setTime(stopDate);
		this.getJSpinnerDateStop().setValue(stopDate);
		this.getJSpinnerTimeStop().setValue(stopDate);
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
		
		// --- Getting the time format ------------------------------
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
	
	private JSeparator getJSeparatorVert() {
		if (jSeparatorVert == null) {
			jSeparatorVert = new JSeparator();
			jSeparatorVert.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparatorVert;
	}
}  
