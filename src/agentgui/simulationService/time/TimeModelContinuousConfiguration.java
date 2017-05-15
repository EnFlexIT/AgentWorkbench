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
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;

import agentgui.core.application.Language;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;

/**
 * The Class TimeModelStrokeConfiguration extends the class {@link JPanel4TimeModelConfiguration}
 * and is used in order to configure the {@link TimeModelContinuous}.
 * 
 * @see TimeModelContinuous
 * @see TimeModelController
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelContinuousConfiguration extends JPanel4TimeModelConfiguration implements ChangeListener {

	private static final long serialVersionUID = -1170433671816358910L;
	
	private JLabel jLabelHeader1 = null;
	private JLabel jLabelHeader2 = null;
	
	private JPanel jPanelStartSettings = null;
	private JPanel jPanelStopSettings = null;
	private JPanel jPanelWidthSettings = null;
	private JPanel jPanelDivider = null;
	private JPanel jPanelDummy = null;
	
	private JLabel jLabelStart = null;
	private JLabel jLabelStartDate = null;
	private JLabel jLabelStartTime = null;
	private JLabel jLabelStartMillis = null;
	
	private JLabel jLabelStop = null;
	private JLabel jLabelStopDate = null;
	private JLabel jLabelStopTime = null;
	private JLabel jLabelStopMillis = null;
	
	private JLabel jLabelAcceleration = null;
	private JLabel jLabelDummy = null;
	private JLabel jLabelFactorInfoSeconds = null;
	private JLabel jLabelFactorInfoMinutes = null;
	private JLabel jLabelFactorInfoHour = null;

	private JSpinner jSpinnerDateStart = null;
	private JSpinner jSpinnerTimeStart = null;
	private JSpinner jSpinnerMillisStart = null;
	private JSpinner jSpinnerDateStop = null;
	private JSpinner jSpinnerTimeStop = null;
	private JSpinner jSpinnerMillisStop = null;
	private JSpinner jSpinnerAcceleration = null;

	protected boolean enabledChangeListener = true;

	private JLabel jLabelDateFormat = null;
	private TimeFormatSelection jPanelTimeFormater = null;
	
	
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
	protected void initialize() {
		
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 1;
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.insets = new Insets(10, 5, 0, 0);
		gridBagConstraints21.gridy = 6;
		GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
		gridBagConstraints20.gridx = 0;
		gridBagConstraints20.insets = new Insets(15, 10, 0, 0);
		gridBagConstraints20.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints20.gridy = 6;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.insets = new Insets(5, 7, 5, 20);
		gridBagConstraints4.gridwidth = 2;
		gridBagConstraints4.gridy = 7;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 1;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.insets = new Insets(10, 5, 10, 0);
		gridBagConstraints11.gridwidth = 1;
		gridBagConstraints11.gridy = 5;
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 0;
		gridBagConstraints10.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints10.anchor = GridBagConstraints.WEST;
		gridBagConstraints10.gridy = 5;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 0;
		gridBagConstraints9.anchor = GridBagConstraints.WEST;
		gridBagConstraints9.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints9.gridwidth = 2;
		gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints9.gridy = 9;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints15.anchor = GridBagConstraints.WEST;
		gridBagConstraints15.gridy = 4;
		GridBagConstraints gridBagConstraints141 = new GridBagConstraints();
		gridBagConstraints141.gridx = 1;
		gridBagConstraints141.gridwidth = 1;
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
		gridBagConstraints7.gridy = 11;
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

		jLabelStop = new JLabel();
		jLabelStop.setText("Stop bei");
		jLabelStop.setText(Language.translate(jLabelStop.getText()) + ":");
		jLabelStop.setFont(new Font("Dialog", Font.BOLD, 12));

		jLabelDateFormat = new JLabel();
		jLabelDateFormat.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelDateFormat.setText("Ansicht");
		jLabelDateFormat.setText(Language.translate(jLabelDateFormat.getText()) + ":");

		jLabelAcceleration = new JLabel();
		jLabelAcceleration.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelAcceleration.setText("Beschleunigungsfaktor");
		jLabelAcceleration.setText(Language.translate(jLabelAcceleration.getText())+ ":");
		
		this.setSize(new Dimension(615, 367));
        this.setLayout(new GridBagLayout());
        this.add(jLabelHeader1, gridBagConstraints5);
        this.add(jLabelHeader2, gridBagConstraints6);
        this.add(jLabelStart, gridBagConstraints15);
        this.add(getJPanelStartSettings(), gridBagConstraints141);
        this.add(getJPanelWidthSettings(), gridBagConstraints9);
        this.add(getJPanelDummy(), gridBagConstraints7);
        this.add(jLabelStop, gridBagConstraints10);
        this.add(getJPanelStopSettings(), gridBagConstraints11);
        this.add(getJPanelDivider(), gridBagConstraints4);
        this.add(jLabelDateFormat, gridBagConstraints20);
        this.add(getJPanelTimeFormater(), gridBagConstraints21);
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
			jSpinnerDateStart.setEditor(new JSpinner.DateEditor(jSpinnerDateStart, "dd.MM.yyyy"));
			jSpinnerDateStart.setPreferredSize(new Dimension(100, 28));
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
			jSpinnerTimeStart.setEditor(new JSpinner.DateEditor(jSpinnerTimeStart, "HH:mm:ss"));
			jSpinnerTimeStart.setPreferredSize(new Dimension(80, 28));
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
			jSpinnerMillisStart.setEditor(new JSpinner.NumberEditor(jSpinnerMillisStart, "000"));
			jSpinnerMillisStart.setPreferredSize(new Dimension(60, 28));
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
			jLabelStopDate.setText("Datum");
			jLabelStopDate.setPreferredSize(new Dimension(40, 16));
			jLabelStopDate.setText(Language.translate(jLabelStopDate.getText())+ ":");
			jLabelStopTime = new JLabel();
			jLabelStopTime.setText("Uhrzeit");
			jLabelStopTime.setText(Language.translate(jLabelStopTime.getText())+ ":");
			jLabelStopMillis = new JLabel();
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
			jSpinnerDateStop.setEditor(new JSpinner.DateEditor(jSpinnerDateStop, "dd.MM.yyyy"));
			jSpinnerDateStop.setPreferredSize(new Dimension(100, 28));
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
			jSpinnerTimeStop.setEditor(new JSpinner.DateEditor(jSpinnerTimeStop, "HH:mm:ss"));
			jSpinnerTimeStop.setPreferredSize(new Dimension(80, 28));
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
			jSpinnerMillisStop.setEditor(new JSpinner.NumberEditor(jSpinnerMillisStop, "000"));
			jSpinnerMillisStop.setPreferredSize(new Dimension(60, 28));
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
	protected TimeFormatSelection getJPanelTimeFormater() {
		if (jPanelTimeFormater == null) {
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
	/**
	 * This method initializes jPanelWidthSettings	
	 * @return javax.swing.JPanel	
	 */
	protected JPanel getJPanelWidthSettings() {
		if (jPanelWidthSettings == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints12.gridy = 0;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints16.gridy = 3;
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridwidth = 3;
			gridBagConstraints16.gridx = 0;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints13.gridy = 2;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridwidth = 3;
			gridBagConstraints13.gridx = 0;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.insets = new Insets(10, 0, 5, 0);
			gridBagConstraints17.gridy = 1;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridwidth = 3;
			gridBagConstraints17.weightx = 0.0;
			gridBagConstraints17.gridx = 0;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints19.gridy = 0;
			gridBagConstraints19.gridx = 1;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.insets = new Insets(0, 0, 0, 2);
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.gridx = 0;

			jLabelDummy = new JLabel();
			jLabelDummy.setText(" ");
			
			jLabelFactorInfoSeconds = new JLabel();
			jLabelFactorInfoSeconds.setText("Sekunden");
			jLabelFactorInfoMinutes = new JLabel();
			jLabelFactorInfoMinutes.setText("Minuten");
			jLabelFactorInfoHour = new JLabel();
			jLabelFactorInfoHour.setText("Stunden");
			
			jPanelWidthSettings = new JPanel();
			jPanelWidthSettings.setLayout(new GridBagLayout());
			jPanelWidthSettings.add(jLabelAcceleration, gridBagConstraints18);
			jPanelWidthSettings.add(getJSpinnerAcceleration(), gridBagConstraints19);
			jPanelWidthSettings.add(jLabelFactorInfoSeconds, gridBagConstraints17);
			jPanelWidthSettings.add(jLabelFactorInfoMinutes, gridBagConstraints13);
			jPanelWidthSettings.add(jLabelFactorInfoHour, gridBagConstraints16);
			jPanelWidthSettings.add(jLabelDummy, gridBagConstraints12);
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
			jSpinnerAcceleration.setEditor(new JSpinner.NumberEditor(jSpinnerAcceleration, "0.000"));
			jSpinnerAcceleration.setPreferredSize(new Dimension(100, 28));
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
	 * This method initializes jPanelDivider	
	 * @return javax.swing.JPanel	
	 */
	protected JPanel getJPanelDivider() {
		if (jPanelDivider == null) {
			jPanelDivider = new JPanel();
			jPanelDivider.setLayout(new GridBagLayout());
			jPanelDivider.setPreferredSize(new Dimension(200, 2));
			jPanelDivider.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return jPanelDivider;
	}
	/**
	 * This method initializes jPanelDummy	
	 * @return javax.swing.JPanel	
	 */
	protected JPanel getJPanelDummy() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
		}
		return jPanelDummy;
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
			
			jLabelFactorInfoSeconds.setText("1 " + stringSimulated + " " + stringSecond + " = " + textSeconds);
			jLabelFactorInfoMinutes.setText("1 " + stringSimulated + " " + stringMinute + " = " + textMinutes);
			jLabelFactorInfoHour.setText("1 " + stringSimulated + " " + stringHour + " = " + textHours);
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
		this.getJPanelTimeFormater().setTimeFormat(timeModelContinuous.getTimeFormat());
		
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
		String timeFormat = this.getJPanelTimeFormater().getTimeFormat();
		
		// --- Getting acceleration for the time --------------------
		Double factor = (Double) this.getJSpinnerAcceleration().getValue();
		
		// --- Set TimeModel ----------------------------------------
		TimeModelContinuous tmc = new TimeModelContinuous(startLong, stopLong);
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

	
}  
