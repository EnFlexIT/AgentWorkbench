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
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.enflexit.language.Language;
import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;
import de.enflexit.common.swing.JSpinnerDateTime;
import de.enflexit.common.swing.TimeFormatSelection;
import de.enflexit.common.swing.TimeZoneWidget;

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
	
	private JLabel jLabelHeader1;
	private JLabel jLabelHeader2;
	
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

	private JTextField jTextFieldWidthValue;
	private JComboBox<TimeUnit> jComboBoxWidthUnit;

	private JSpinnerDateTime jSpinnerDateStart;
	private JSpinner jSpinnerMillisStart;
	private JSpinnerDateTime jSpinnerDateStop;
	private JSpinner jSpinnerMillisStop;

	private JLabel jLabelTimeZone;
	private TimeZoneWidget timeZoneWidget;

	private JLabel jLabeDateFormat;
	private TimeFormatSelection jPanelTimeFormater;
	
	private JSeparator jSeparator1;
	private JSeparator jSeparator2;

	
	private boolean enabledChangeListener = true;

	
	/**
	 * Instantiates a new time model discrete configuration.
	 *
	 * @param project the project
	 * @param timeModelController the time model controller
	 */
	public TimeModelDiscreteConfiguration(Project project, TimeModelController timeModelController) {
		super(project, timeModelController);
		this.initialize();
	}
	/**
	 * This method initializes this
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		this.setSize(600, 400);
		
		GridBagConstraints gbc_jLabelHeader1 = new GridBagConstraints();
		gbc_jLabelHeader1.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader1.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelHeader1.gridwidth = 2;
		gbc_jLabelHeader1.gridx = 0;
		gbc_jLabelHeader1.gridy = 0;

		GridBagConstraints gbc_jLabelHeader2 = new GridBagConstraints();
		gbc_jLabelHeader2.insets = new Insets(0, 10, 0, 0);
		gbc_jLabelHeader2.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader2.gridwidth = 2;
		gbc_jLabelHeader2.weightx = 0.0;
		gbc_jLabelHeader2.fill = GridBagConstraints.NONE;
		gbc_jLabelHeader2.gridx = 0;
		gbc_jLabelHeader2.gridy = 1;

		GridBagConstraints gbc_jLabelTimeZone = new GridBagConstraints();
		gbc_jLabelTimeZone.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelTimeZone.anchor = GridBagConstraints.WEST;
		gbc_jLabelTimeZone.gridx = 0;
		gbc_jLabelTimeZone.gridy = 2;
		
        GridBagConstraints gbc_timeZoneWidget = new GridBagConstraints();
        gbc_timeZoneWidget.fill = GridBagConstraints.HORIZONTAL;
        gbc_timeZoneWidget.insets = new Insets(10, 55, 5, 5);
        gbc_timeZoneWidget.gridx = 1;
        gbc_timeZoneWidget.gridy = 2;
		
        GridBagConstraints gbc_jLabeDateFormat = new GridBagConstraints();
        gbc_jLabeDateFormat.anchor = GridBagConstraints.NORTHWEST;
        gbc_jLabeDateFormat.insets = new Insets(15, 10, 0, 0);
        gbc_jLabeDateFormat.gridx = 0;
        gbc_jLabeDateFormat.gridy = 3;
        
		GridBagConstraints gbc_jPanelTimeFormater = new GridBagConstraints();
		gbc_jPanelTimeFormater.anchor = GridBagConstraints.WEST;
		gbc_jPanelTimeFormater.gridwidth = 1;
		gbc_jPanelTimeFormater.insets = new Insets(10, 5, 0, 0);
		gbc_jPanelTimeFormater.gridx = 1;
		gbc_jPanelTimeFormater.gridy = 3;
        
		GridBagConstraints gbc_jSeparator1 = new GridBagConstraints();
		gbc_jSeparator1.insets = new Insets(5, 10, 0, 5);
		gbc_jSeparator1.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparator1.gridwidth = 2;
		gbc_jSeparator1.gridx = 0;
		gbc_jSeparator1.gridy = 4;
        
        GridBagConstraints gbc_jLabelStart = new GridBagConstraints();
        gbc_jLabelStart.insets = new Insets(10, 10, 5, 0);
        gbc_jLabelStart.anchor = GridBagConstraints.WEST;
        gbc_jLabelStart.gridx = 0;
        gbc_jLabelStart.gridy = 5;

        GridBagConstraints gbc_jPanelStartSettings = new GridBagConstraints();
        gbc_jPanelStartSettings.anchor = GridBagConstraints.WEST;
        gbc_jPanelStartSettings.insets = new Insets(10, 5, 5, 0);
        gbc_jPanelStartSettings.gridx = 1;
        gbc_jPanelStartSettings.gridy = 5;
        
        GridBagConstraints gbc_jLabelStop = new GridBagConstraints();
		gbc_jLabelStop.insets = new Insets(10, 10, 0, 0);
		gbc_jLabelStop.anchor = GridBagConstraints.WEST;
		gbc_jLabelStop.gridx = 0;
		gbc_jLabelStop.gridy = 6;
		
        GridBagConstraints gbc_jPanelStopSettings = new GridBagConstraints();
        gbc_jPanelStopSettings.fill = GridBagConstraints.NONE;
        gbc_jPanelStopSettings.anchor = GridBagConstraints.WEST;
        gbc_jPanelStopSettings.insets = new Insets(10, 5, 0, 0);
        gbc_jPanelStopSettings.gridx = 1;
        gbc_jPanelStopSettings.gridy = 6;

        GridBagConstraints gbc_jSeparator2 = new GridBagConstraints();
        gbc_jSeparator2.insets = new Insets(20, 10, 0, 5);
        gbc_jSeparator2.fill = GridBagConstraints.HORIZONTAL;
        gbc_jSeparator2.gridwidth = 2;
        gbc_jSeparator2.gridx = 0;
        gbc_jSeparator2.gridy = 7;
		
		GridBagConstraints gbc_jLabelWidth = new GridBagConstraints();
		gbc_jLabelWidth.insets = new Insets(0, 10, 0, 0);
		gbc_jLabelWidth.anchor = GridBagConstraints.WEST;
		gbc_jLabelWidth.gridx = 0;
		gbc_jLabelWidth.gridy = 8;
		
		GridBagConstraints gbc_jPanelWidthSettings = new GridBagConstraints();
		gbc_jPanelWidthSettings.anchor = GridBagConstraints.WEST;
		gbc_jPanelWidthSettings.insets = new Insets(10, 5, 10, 0);
		gbc_jPanelWidthSettings.gridx = 1;
		gbc_jPanelWidthSettings.gridy = 8;

		
		jLabelHeader1 = new JLabel();
		jLabelHeader1.setText("TimeModelDiscrete");
		jLabelHeader1.setFont(new Font("Dialog", Font.BOLD, 14));
		jLabelHeader2 = new JLabel();
		jLabelHeader2.setText("Diskretes Zeitmodell mit Schrittweite sowie Start- und Endzeitpunkt.");
		jLabelHeader2.setText(Language.translate(jLabelHeader2.getText()));

		jLabelTimeZone = new JLabel();
		jLabelTimeZone.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelTimeZone.setText("Zeitzone");
		jLabelTimeZone.setText(Language.translate(jLabelTimeZone.getText()) + ":");

        jLabeDateFormat = new JLabel();
        jLabeDateFormat.setFont(new Font("Dialog", Font.BOLD, 12));
        jLabeDateFormat.setText("Ansicht");
        jLabeDateFormat.setText(Language.translate(jLabeDateFormat.getText()) + ":");

		jSeparator1 = new JSeparator();
		
        jLabelStart = new JLabel();
        jLabelStart.setText("Start bei");
        jLabelStart.setText(Language.translate(jLabelStart.getText()) + ":");
        jLabelStart.setFont(new Font("Dialog", Font.BOLD, 12));
		
		jLabelStartDate = new JLabel();
		jLabelStartDate.setText("Datum");
		jLabelStartDate.setPreferredSize(new Dimension(40, 16));
		jLabelStartDate.setText(Language.translate(jLabelStartDate.getText())+ ":");
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
		jLabelStopMillis = new JLabel();
		jLabelStopMillis.setText("Millisekunden");
		jLabelStopMillis.setText(Language.translate(jLabelStopMillis.getText())+ ":");
		
		jSeparator2 = new JSeparator();
		
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
		
		
        this.add(jLabelHeader1, gbc_jLabelHeader1);
        this.add(jLabelHeader2, gbc_jLabelHeader2);
        this.add(jLabelTimeZone, gbc_jLabelTimeZone);
        this.add(getTimeZoneWidget(), gbc_timeZoneWidget);
        this.add(jLabeDateFormat, gbc_jLabeDateFormat);
        this.add(getJPanelTimeFormater(), gbc_jPanelTimeFormater);
        this.add(jSeparator1, gbc_jSeparator1);
        this.add(jLabelStart, gbc_jLabelStart);
        this.add(getJPanelStartSettings(), gbc_jPanelStartSettings);
        this.add(jLabelStop, gbc_jLabelStop);
        this.add(getJPanelStopSettings(), gbc_jPanelStopSettings);
        this.add(jSeparator2, gbc_jSeparator2);
        this.add(jLabelWidth, gbc_jLabelWidth);
        this.add(getJPanelWidthSettings(), gbc_jPanelWidthSettings);
	}

	/**
	 * Gets the JSpinner date start.
	 * @return the JSpinner date start
	 */
	private JSpinnerDateTime getJSpinnerDateStart() {
		if (jSpinnerDateStart==null) {
			jSpinnerDateStart = new JSpinnerDateTime("dd.MM.yyyy - HH:mm:ss");
			jSpinnerDateStart.setPreferredSize(new Dimension(160, 28));
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
	private JSpinnerDateTime getJSpinnerDateStop() {
		if (jSpinnerDateStop==null) {
			jSpinnerDateStop = new JSpinnerDateTime("dd.MM.yyyy - HH:mm:ss");
			jSpinnerDateStop.setPreferredSize(new Dimension(160, 28));
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
			jSpinnerMillisStop.setEditor(new JSpinner.NumberEditor(jSpinnerMillisStop, "000"));
			jSpinnerMillisStop.setPreferredSize(new Dimension(60, 28));
			jSpinnerMillisStop.addChangeListener(this);
		}
		return jSpinnerMillisStop;
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
			jPanelStartSettings.add(jLabelStartMillis, null);
			jPanelStartSettings.add(getJSpinnerMillisStart(), null);
		}
		return jPanelStartSettings;
	}

	private TimeZoneWidget getTimeZoneWidget() {
		if (timeZoneWidget==null) {
			timeZoneWidget = new TimeZoneWidget(null, false);
			timeZoneWidget.setPreferredSize(new Dimension(60, 28));
			timeZoneWidget.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// --- TimeZone was changed ---------------------
					TimeModelDiscreteConfiguration.this.setTimeZone(null);
				}
			});
		}
		return timeZoneWidget;
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
			jPanelTimeFormater.setPreferredSize(new Dimension(360, 70));
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
		this.getJPanelTimeFormater().setTimeFormat(timeModelDiscrete.getTimeFormat());

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
		String timeFormat = this.getJPanelTimeFormater().getTimeFormat();
		
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
