package de.enflexit.awb.simulation.environment.time;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import de.enflexit.language.Language;
import de.enflexit.common.swing.JSpinnerDateTime;
import de.enflexit.common.swing.TimeFormatSelection;
import de.enflexit.common.swing.TimeZoneWidget;

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
	
	private JLabel jLabelHeader1;
	private JLabel jLabelHeader2;
	
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
	private JLabel jLabelDummy;
	private JLabel jLabelFactorInfoSeconds;
	private JLabel jLabelFactorInfoMinutes;
	private JLabel jLabelFactorInfoHour;

	private JSpinnerDateTime jSpinnerDateStart;
	private JSpinner jSpinnerMillisStart;
	private JSpinnerDateTime jSpinnerDateStop;
	private JSpinner jSpinnerMillisStop;
	private JSpinner jSpinnerAcceleration;

	private JLabel jLabelTimeZone;
	private TimeZoneWidget timeZoneWidget;
	private JLabel jLabelDateFormat;
	private TimeFormatSelection jPanelTimeFormater;
	
	private JSeparator jSeparator1;
	private JSeparator jSeparator2;
	

	protected boolean enabledChangeListener = true;
	
	
	/**
	 * Instantiates a new time model discrete configuration.
	 *
	 * @param project the project
	 * @param timeModelController the time model controller
	 */
	public TimeModelContinuousConfiguration(Project project, TimeModelController timeModelController) {
		super(project, timeModelController);
		this.initialize();
	}

	/**
	 * This method initializes this
	 */
	protected void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		this.setSize(600, 486);
		
		GridBagConstraints gbc_jLabelHeader1 = new GridBagConstraints();
		gbc_jLabelHeader1.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader1.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelHeader1.gridwidth = 2;
		gbc_jLabelHeader1.gridx = 0;
		gbc_jLabelHeader1.gridy = 0;
		
		GridBagConstraints gbcjLabelHeader2 = new GridBagConstraints();
		gbcjLabelHeader2.insets = new Insets(0, 10, 0, 0);
		gbcjLabelHeader2.anchor = GridBagConstraints.WEST;
		gbcjLabelHeader2.gridwidth = 2;
		gbcjLabelHeader2.weightx = 0.0;
		gbcjLabelHeader2.fill = GridBagConstraints.NONE;
		gbcjLabelHeader2.gridx = 0;
		gbcjLabelHeader2.gridy = 1;
		
		GridBagConstraints gbc_jLabelTimeZone = new GridBagConstraints();
		gbc_jLabelTimeZone.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelTimeZone.anchor = GridBagConstraints.WEST;
		gbc_jLabelTimeZone.gridx = 0;
		gbc_jLabelTimeZone.gridy = 2;

		GridBagConstraints gbc_jSeparator1 = new GridBagConstraints();
		gbc_jSeparator1.insets = new Insets(5, 10, 0, 5);
		gbc_jSeparator1.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparator1.gridwidth = 2;
		gbc_jSeparator1.gridx = 0;
		gbc_jSeparator1.gridy = 4;

		GridBagConstraints gbc_timeZoneWidget = new GridBagConstraints();
        gbc_timeZoneWidget.fill = GridBagConstraints.HORIZONTAL;
        gbc_timeZoneWidget.insets = new Insets(10, 55, 5, 5);
        gbc_timeZoneWidget.gridx = 1;
        gbc_timeZoneWidget.gridy = 2;
		
        GridBagConstraints gbc_jLabelDateFormat = new GridBagConstraints();
		gbc_jLabelDateFormat.insets = new Insets(15, 10, 0, 0);
		gbc_jLabelDateFormat.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelDateFormat.gridx = 0;
		gbc_jLabelDateFormat.gridy = 3;
		
		GridBagConstraints gbc_jPanelTimeFormater = new GridBagConstraints();
		gbc_jPanelTimeFormater.anchor = GridBagConstraints.WEST;
		gbc_jPanelTimeFormater.insets = new Insets(10, 5, 0, 0);
		gbc_jPanelTimeFormater.gridx = 1;
		gbc_jPanelTimeFormater.gridy = 3;
		
		GridBagConstraints gbc_jLabelStart = new GridBagConstraints();
		gbc_jLabelStart.insets = new Insets(10, 10, 5, 0);
		gbc_jLabelStart.anchor = GridBagConstraints.WEST;
		gbc_jLabelStart.gridx = 0;
		gbc_jLabelStart.gridy = 5;
		
		GridBagConstraints gbc_jPanelStartSettings = new GridBagConstraints();
        gbc_jPanelStartSettings.gridwidth = 1;
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
        gbc_jPanelStopSettings.anchor = GridBagConstraints.WEST;
        gbc_jPanelStopSettings.insets = new Insets(10, 5, 0, 0);
        gbc_jPanelStopSettings.gridwidth = 1;
        gbc_jPanelStopSettings.gridx = 1;
        gbc_jPanelStopSettings.gridy = 6;
		
		GridBagConstraints gbc_jSeparator2 = new GridBagConstraints();
        gbc_jSeparator2.insets = new Insets(20, 10, 0, 5);
        gbc_jSeparator2.fill = GridBagConstraints.HORIZONTAL;
        gbc_jSeparator2.gridwidth = 2;
        gbc_jSeparator2.gridx = 0;
        gbc_jSeparator2.gridy = 7;
		
		GridBagConstraints gbc_jPanelWidthSettings = new GridBagConstraints();
		gbc_jPanelWidthSettings.anchor = GridBagConstraints.NORTH;
		gbc_jPanelWidthSettings.insets = new Insets(10, 10, 10, 10);
		gbc_jPanelWidthSettings.gridwidth = 2;
		gbc_jPanelWidthSettings.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPanelWidthSettings.gridx = 0;
		gbc_jPanelWidthSettings.gridy = 8;
		
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 2;
		gridBagConstraints8.gridy = 0;
		
		
		jLabelHeader1 = new JLabel();
		jLabelHeader1.setText("TimeModelContinuous");
		jLabelHeader1.setFont(new Font("Dialog", Font.BOLD, 14));
		
		jLabelHeader2 = new JLabel();
		jLabelHeader2.setText("Kontinuierlich fortschreitende Zeit.");
		jLabelHeader2.setText(Language.translate(jLabelHeader2.getText()));
		
		jLabelTimeZone = new JLabel();
		jLabelTimeZone.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelTimeZone.setText("Zeitzone");
		jLabelTimeZone.setText(Language.translate(jLabelTimeZone.getText()) + ":");

		jLabelDateFormat = new JLabel();
		jLabelDateFormat.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelDateFormat.setText("Ansicht");
		jLabelDateFormat.setText(Language.translate(jLabelDateFormat.getText()) + ":");
		
		jSeparator1 = new JSeparator();
		
		jLabelStart = new JLabel();
		jLabelStart.setText("Start bei");
		jLabelStart.setText(Language.translate(jLabelStart.getText()) + ":");
		jLabelStart.setFont(new Font("Dialog", Font.BOLD, 12));

		jLabelStop = new JLabel();
		jLabelStop.setText("Stop bei");
		jLabelStop.setText(Language.translate(jLabelStop.getText()) + ":");
		jLabelStop.setFont(new Font("Dialog", Font.BOLD, 12));
		
		jSeparator2 = new JSeparator();
		
		jLabelAcceleration = new JLabel();
		jLabelAcceleration.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelAcceleration.setText("Beschleunigungsfaktor");
		jLabelAcceleration.setText(Language.translate(jLabelAcceleration.getText())+ ":");
		
		this.add(jLabelHeader1, gbc_jLabelHeader1);
        this.add(jLabelHeader2, gbcjLabelHeader2);
        this.add(jLabelTimeZone, gbc_jLabelTimeZone);
        this.add(getTimeZoneWidget(), gbc_timeZoneWidget);
        this.add(jLabelDateFormat, gbc_jLabelDateFormat);
        this.add(getJPanelTimeFormater(), gbc_jPanelTimeFormater);
        this.add(jLabelStart, gbc_jLabelStart);
        this.add(getJPanelStartSettings(), gbc_jPanelStartSettings);
        this.add(jLabelStop, gbc_jLabelStop);
        this.add(getJPanelStopSettings(), gbc_jPanelStopSettings);
        this.add(getJPanelWidthSettings(), gbc_jPanelWidthSettings);
        this.add(jSeparator1, gbc_jSeparator1);
        this.add(jSeparator2, gbc_jSeparator2);
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
			jLabelStartMillis = new JLabel();
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
			jLabelStopMillis = new JLabel();
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
	private TimeZoneWidget getTimeZoneWidget() {
		if (timeZoneWidget==null) {
			timeZoneWidget = new TimeZoneWidget(null, false);
			timeZoneWidget.setPreferredSize(new Dimension(60, 28));
			timeZoneWidget.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					// --- TimeZone was changed ---------------------
					TimeModelContinuousConfiguration.this.setTimeZone(null);
				}
			});
		}
		return timeZoneWidget;
	}
	/**
	 * This method initializes timeFormater	
	 * @return de.enflexit.awb.simulation.time.TimeFormatSelection	
	 */
	protected TimeFormatSelection getJPanelTimeFormater() {
		if (jPanelTimeFormater == null) {
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
			
			jPanelWidthSettings = new JPanel();
			jPanelWidthSettings.setLayout(new GridBagLayout());
			jPanelWidthSettings.add(jLabelAcceleration, gridBagConstraints18);
			jPanelWidthSettings.add(getJSpinnerAcceleration(), gridBagConstraints19);
			jPanelWidthSettings.add(getJLabelFactorInfoSeconds(), gridBagConstraints17);
			jPanelWidthSettings.add(getJLabelFactorInfoMinutes(), gridBagConstraints13);
			jPanelWidthSettings.add(getJLabelFactorInfoHour(), gridBagConstraints16);
			jPanelWidthSettings.add(jLabelDummy, gridBagConstraints12);
		}
		return jPanelWidthSettings;
	}
	
	private JLabel getJLabelFactorInfoSeconds() {
		if (jLabelFactorInfoSeconds==null) {
			jLabelFactorInfoSeconds = new JLabel();
			jLabelFactorInfoSeconds.setText("Sekunden");
		}
		return jLabelFactorInfoSeconds;
	}
	private JLabel getJLabelFactorInfoMinutes() {
		if (jLabelFactorInfoMinutes==null) {
			jLabelFactorInfoMinutes = new JLabel();
			jLabelFactorInfoMinutes.setText("Minuten");
		}
		return jLabelFactorInfoMinutes;
	}
	private JLabel getJLabelFactorInfoHour() {
		if (jLabelFactorInfoHour==null) {
			jLabelFactorInfoHour = new JLabel();
			jLabelFactorInfoHour.setText("Stunden");
		}
		return jLabelFactorInfoHour;
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
			
			this.getJLabelFactorInfoSeconds().setText("1 " + stringSimulated + " " + stringSecond + " = " + textSeconds);
			this.getJLabelFactorInfoMinutes().setText("1 " + stringSimulated + " " + stringMinute + " = " + textMinutes);
			this.getJLabelFactorInfoHour().setText("1 " + stringSimulated + " " + stringHour + " = " + textHours);
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
	 * @see de.enflexit.awb.simulation.time.DisplayJPanel4Configuration#setTimeModel(de.enflexit.awb.simulation.time.TimeModel)
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
		this.getJPanelTimeFormater().setTimeFormat(timeModelContinuous.getTimeFormat());
		
		// --- Settings for the acceleration of the time ------------
		Double factor = timeModelContinuous.getAccelerationFactor();
		this.getJSpinnerAcceleration().setValue(factor);
		this.setFactorExplanationText(factor);
		
		this.enabledChangeListener = true;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.DisplayJPanel4Configuration#getTimeModel()
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
		String timeFormat = this.getJPanelTimeFormater().getTimeFormat();
		
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
