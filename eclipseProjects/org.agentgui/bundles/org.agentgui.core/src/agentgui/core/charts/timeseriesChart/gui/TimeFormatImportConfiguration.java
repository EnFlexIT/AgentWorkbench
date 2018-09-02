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
package agentgui.core.charts.timeseriesChart.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.simulationService.time.TimeFormatSelection;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDateBased;
import de.enflexit.common.ExceptionHandling;

/**
 * The Dialog TimeFormatImportConfiguration is used to set the 
 * format of the time stamps for the import to a TimeSeriesChart.
 * Additionally a time shift can be applied in order to match the 
 * current simulation setup or another specifiable time. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormatImportConfiguration extends JDialog implements ActionListener, ChangeListener {

	private static final long serialVersionUID = -7271198574951719361L;
	
	public static final String PROP_CsvTimeFormat = "CSV_IMPORT_TIMEFORMAT";
	public static final String PROP_TimeOffSet = "TIME_OFFSET";
	public static final String PROP_TimeOffSetSimulationSetup = "TIME_OFFSET_SIMULATION_SETUP";
	public static final String PROP_TimeOffSetManual = "TIME_OFFSET_MANUAL";
	public static final String PROP_TimeOffSetManualStartTime = "TIME_OFFSET_MANUAL_START_TIME";
	
	private boolean canceled = false;
	private boolean error = false;
	
	private File csvFile = null;
	private String csvFileFirstTimeStamp = null;
	
	private Long timeOffset = null;  //  @jve:decl-index=0:
	private Long timeExampleFromFile = null;
	private Long timeStartSimSetup = null;  //  @jve:decl-index=0:
	private String timeFormatSimSetup = null;  //  @jve:decl-index=0:
	
	private JPanel jContentPane = null;
	private JLabel jLabelHeader = null;
	private TimeFormatSelection timeFormatSeletor = null;

	private JPanel jPanelTextExample = null;
	private JLabel jLabelFromFile = null;
	private JTextField jTextFieldFromFile = null;
	private JLabel jLabelParsed = null;
	private JTextField jTextFieldParsed = null;
	private JPanel jPanelLine1 = null;

	private JCheckBox jCheckBoxTimeShiftAdjust = null;
	private JCheckBox jCheckBoxTimeShiftUseSimulationSetup = null;
	private JLabel jLabelSimStart = null;
	private JCheckBox jCheckBoxTimeShiftAdjustToTime = null;
	
	private JPanel jPanelStartSettings = null;
	private JLabel jLabelStartDate = null;
	private JLabel jLabelStartTime = null;
	private JLabel jLabelStartMillis = null;
	private JSpinner jSpinnerDateStart = null;
	private JSpinner jSpinnerTimeStart = null;
	private JSpinner jSpinnerMillisStart = null;
	private JPanel jPanelLine2 = null;

	private JPanel jPanelButtons = null;
	private JButton jButtonOK = null;
	private JButton jButtonCancel = null;
	private JLabel jLabelDummy = null;


	/**
	 * Instantiates a new time format import configuration.
	 * @param owner the owner
	 */
	public TimeFormatImportConfiguration(Frame owner, File csvFile) {
		super(owner);
		this.csvFile = csvFile;
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {

		this.setSize(600, 400);
		this.setTitle(Language.translate("CSV-File", Language.EN) + " Import: " + Language.translate("Time Format and Offset", Language.EN));
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		
		this.setModal(true);
		this.setResizable(false);
		this.registerEscapeKeyStroke();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setCanceled(true);
				setVisible(false);
			}
		});

		// --- Set dialog -------------------------------------------
		if (Application.getProjectFocused()!=null) {
			TimeModel currTimeModel = Application.getProjectFocused().getTimeModelController().getTimeModel();
			if (currTimeModel!=null && currTimeModel instanceof TimeModelDateBased) {
				this.timeStartSimSetup = ((TimeModelDateBased)currTimeModel).getTimeStart();
				this.timeFormatSimSetup = ((TimeModelDateBased)currTimeModel).getTimeFormat();
			}
		}
		this.loadCheckBoxConfiguration();
		this.setJSpinnerTime(Long.parseLong(this.getFilePropertyManualStartTimeForOffset()));
		
		this.setContentPane(getJContentPane());
		this.getTimeFormatSelector().setTimeFormat(this.getFilePropertyCsvTimeFormat());
		this.setExampleFileData();
		this.setExampleParse();
		
		// --- Set the Look and Feel of the Dialog ------------------
		this.setLookAndFeel();

		// --- Center dialog ----------------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);			
		
	    
	}
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			setCanceled(true);
            	setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    /**
	 * This method set the Look and Feel of this Dialog.
	 * @param newLnF the new look and feel
	 */
	private void setLookAndFeel() {
 
		String lnfClassName = Application.getGlobalInfo().getAppLookAndFeelClassName();
		if (lnfClassName==null) return;
		
		String currLookAndFeelClassName = UIManager.getLookAndFeel().getClass().getName();
		if (lnfClassName.equals(currLookAndFeelClassName)==true) return;
		
		try {
			UIManager.setLookAndFeel(lnfClassName);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Cannot install " + lnfClassName + " on this platform:" + e.getMessage());
		}
		
	}
	
	/**
	 * Sets the cancelled.
	 * @param canceled the new cancelled
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	/**
	 * Checks if is cancelled.
	 * @return true, if is cancelled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * Sets that parsing results an error.
	 * @param error the new error
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	/**
	 * Checks if parsing results to an error.
	 * @return true, if is error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Gets the time offset for the import.
	 * @return the time offset
	 */
	public long getTimeOffset() {
		
		this.timeOffset = new Long(0);
		
		if (this.getJCheckBoxTimeShiftAdjust().isSelected()==true) {
			// --- An offset is wished --------------------
			if (this.getJCheckBoxTimeShiftUseSimulationSetup().isSelected()==true) {
				if (this.timeStartSimSetup!=null) {
					this.timeOffset = this.timeStartSimSetup - this.timeExampleFromFile;
				}
			} else if (this.getJCheckBoxTimeShiftAdjustToTime().isSelected()==true) {
				this.timeOffset = this.getJSpinnerTime() - this.timeExampleFromFile;
			}
		} 
		//System.out.println( "=> " + new Date(((long) this.timeExampleFromFile)+((long) this.timeOffset)).toString() );
		return timeOffset;
	}
	
	/**
	 * Gets the file property for the CSV time format.
	 * @return the file property for the CSV time format
	 */
	private String getFilePropertyCsvTimeFormat() {
		return Application.getGlobalInfo().getStringFromConfiguration(PROP_CsvTimeFormat, TimeModelDateBased.DEFAULT_TIME_FORMAT);
	}
	/**
	 * Sets the file property of the CSV time format.
	 * @param newCsvTimeFormat the new file property for the CSV time format
	 */
	private void setFilePropertyCsvTimeFormat(String newCsvTimeFormat) {
		Application.getGlobalInfo().putStringToConfiguration(PROP_CsvTimeFormat, newCsvTimeFormat);
	}
	
	
	
	/**
	 * Gets the file property for the manual start time for offset.
	 * @return the file property manual start time for offset
	 */
	private String getFilePropertyManualStartTimeForOffset() {
		String defaultValue = null;
		if (this.timeStartSimSetup==null) {
			defaultValue = ((Long) new Date().getTime()).toString();	
		} else {
			defaultValue = this.timeStartSimSetup.toString();
		}
		return Application.getGlobalInfo().getStringFromConfiguration(PROP_TimeOffSetManualStartTime, defaultValue);
	}
	/**
	 * Sets the file property for the manual start time for offset.
	 * @param newStartTimeAsLong the new file property for the manual start time for offset
	 */
	private void setFilePropertyManualStartTimeForOffset(String newStartTimeAsLong) {
		Application.getGlobalInfo().putStringToConfiguration(PROP_TimeOffSetManualStartTime, newStartTimeAsLong);
	}
	
	
	
	/**
	 * Returns the configured time format.
	 * @return the time format
	 */
	public String getTimeFormat() {
		return this.getTimeFormatSelector().getTimeFormat();
	}
	
	/**
	 * Sets the file data example.
	 */
	private void setExampleFileData() {
		
		// --- Read the data from the file --------------------------
		try {
			BufferedReader csvFileReader = new BufferedReader(new FileReader(csvFile));
			String inBuffer = null;
			while((inBuffer = csvFileReader.readLine()) != null){
				
				int timeStampCut = inBuffer.indexOf(";");
				String timeStamp = inBuffer.substring(0, timeStampCut);
				inBuffer = "1234.567;" + inBuffer.substring(timeStampCut+1);
				if(inBuffer.matches("[\\d]+\\.?[\\d]*[;[\\d]+\\.?[\\d]*]+")){
					this.csvFileFirstTimeStamp = timeStamp;
					this.getJTextFieldFromFile().setText(timeStamp);
					break;
				}
			}
			csvFileReader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Sets the example of the parsed value.
	 */
	private void setExampleParse() {

		if (csvFileFirstTimeStamp==null) {
			this.getJTextFieldParsed().setText("null");
			
		} else {
			try {
				// --- Try to parse the example String first --------
				DateFormat df = new SimpleDateFormat(this.getTimeFormat());
				Date dateParsed =  df.parse(this.csvFileFirstTimeStamp);
				this.timeExampleFromFile = dateParsed.getTime();
				
				// --- Try to display the date in a standard way ----
				DateFormat dfParsed = new SimpleDateFormat(TimeModelDateBased.DEFAULT_TIME_FORMAT);
				this.getJTextFieldParsed().setText(dfParsed.format(dateParsed).toString());
				this.setError(false);
				this.getJTextFieldParsed().setForeground(new Color(0, 0, 0));
				this.jLabelParsed.setForeground(new Color(0, 0, 0));
				
			} catch (ParseException pe) {
				// --- Error while parsing --------------------------
				String exceptionShort = ExceptionHandling.getFirstTextLineOfException(pe);
				if (exceptionShort.contains(":")==true) {
					exceptionShort = exceptionShort.substring(exceptionShort.indexOf(":")+1);
				}
				this.getJTextFieldParsed().setText(exceptionShort);
				this.setError(true);
				this.getJTextFieldParsed().setForeground(new Color(255, 0, 0));
				this.jLabelParsed.setForeground(new Color(255, 0, 0));
			}  
			
		}
		
	}
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.insets = new Insets(5, 32, 0, 10);
			gridBagConstraints12.gridy = 8;
			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.gridx = 1;
			gridBagConstraints71.insets = new Insets(15, 10, 0, 10);
			gridBagConstraints71.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints71.gridy = 11;
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.gridx = 1;
			gridBagConstraints61.anchor = GridBagConstraints.WEST;
			gridBagConstraints61.insets = new Insets(0, 27, 0, 0);
			gridBagConstraints61.gridy = 10;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.gridx = 1;
			gridBagConstraints51.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.insets = new Insets(15, 10, 0, 10);
			gridBagConstraints51.gridy = 5;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.gridy = 9;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.gridy = 7;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints11.gridy = 6;
			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.gridx = 1;
			gridBagConstraints91.fill = GridBagConstraints.BOTH;
			gridBagConstraints91.weighty = 1.0;
			gridBagConstraints91.weightx = 1.0;
			gridBagConstraints91.gridy = 13;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.insets = new Insets(10, 10, 0, 10);
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.gridy = 4;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.insets = new Insets(15, 10, 10, 10);
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.anchor = GridBagConstraints.EAST;
			gridBagConstraints7.gridy = 12;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new Insets(5, 10, 0, 10);
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(5, 10, 0, 10);
			gridBagConstraints.gridy = 1;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 1;
			
			jLabelHeader = new JLabel();
			jLabelHeader.setText("Please, configure the time format of the file data");
			jLabelHeader.setText(Language.translate(jLabelHeader.getText(), Language.EN));
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText("");
			
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getTimeFormatSelector(), gridBagConstraints);
			jContentPane.add(jLabelHeader, gridBagConstraints2);
			jContentPane.add(getJPanelButtons(), gridBagConstraints7);
			jContentPane.add(getJPanelTextExample(), gridBagConstraints8);
			jContentPane.add(jLabelDummy, gridBagConstraints91);
			jContentPane.add(getJCheckBoxTimeShiftAdjust(), gridBagConstraints11);
			jContentPane.add(getJCheckBoxTimeShiftUseSimulationSetup(), gridBagConstraints21);
			jContentPane.add(getJLabelSimulationStartTime(), gridBagConstraints12);
			jContentPane.add(getJCheckBoxTimeShiftAdjustToTime(), gridBagConstraints31);
			jContentPane.add(getJPanelLine1(), gridBagConstraints51);
			jContentPane.add(getJPanelStartSettings(), gridBagConstraints61);
			jContentPane.add(getJPanelLine2(), gridBagConstraints71);
		}
		return jContentPane;
	}

	/**
	 * Gets the time format selector.
	 * @return the time format selector
	 */
	private TimeFormatSelection getTimeFormatSelector() {
		if (timeFormatSeletor==null) {
			timeFormatSeletor = new TimeFormatSelection(false);
			timeFormatSeletor.addActionListener(this);
		}
		return timeFormatSeletor;
	}

	/**
	 * This method initializes jTextFieldFromFile	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldFromFile() {
		if (jTextFieldFromFile == null) {
			jTextFieldFromFile = new JTextField();
			jTextFieldFromFile.setEditable(false);
			jTextFieldFromFile.setPreferredSize(new Dimension(100, 26));
		}
		return jTextFieldFromFile;
	}
	/**
	 * This method initializes jTextFieldParsed	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldParsed() {
		if (jTextFieldParsed == null) {
			jTextFieldParsed = new JTextField();
			jTextFieldParsed.setEditable(false);
			jTextFieldParsed.setPreferredSize(new Dimension(100, 26));
		}
		return jTextFieldParsed;
	}
	
	/**
	 * This method initializes jPanelLine	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLine1() {
		if (jPanelLine1 == null) {
			jPanelLine1 = new JPanel();
			jPanelLine1.setLayout(new GridBagLayout());
			jPanelLine1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelLine1.setPreferredSize(new Dimension(100, 2));
		}
		return jPanelLine1;
	}
	/**
	 * This method initializes jCheckBoxTimeShiftAdjust	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxTimeShiftAdjust() {
		if (jCheckBoxTimeShiftAdjust == null) {
			jCheckBoxTimeShiftAdjust = new JCheckBox();
			jCheckBoxTimeShiftAdjust.setText("Set new start time and apply offset to the timestamps of the file data");
			jCheckBoxTimeShiftAdjust.setText(Language.translate(jCheckBoxTimeShiftAdjust.getText(), Language.EN));
			jCheckBoxTimeShiftAdjust.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxTimeShiftAdjust.addActionListener(this);
		}
		return jCheckBoxTimeShiftAdjust;
	}
	/**
	 * This method initializes jCheckBoxTimeShiftUseSimulationSetup	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxTimeShiftUseSimulationSetup() {
		if (jCheckBoxTimeShiftUseSimulationSetup == null) {
			String displayText = Language.translate("Use configured start time of the simulation setup!", Language.EN);
			jCheckBoxTimeShiftUseSimulationSetup = new JCheckBox();
			jCheckBoxTimeShiftUseSimulationSetup.setText(displayText);
			jCheckBoxTimeShiftUseSimulationSetup.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxTimeShiftUseSimulationSetup.addActionListener(this);
		}
		return jCheckBoxTimeShiftUseSimulationSetup;
	}
	/**
	 * Gets the JLabel for the simulation start time.
	 * @return the JLabel simulation start time
	 */
	private JLabel getJLabelSimulationStartTime() {
		if (jLabelSimStart==null) {
			String displayText = "<html><b>" + Language.translate("Start Time:", Language.EN) + "</b>"; 
			if (this.timeStartSimSetup==null) {
				displayText += " [" + Language.translate("Not defined!", Language.EN) + "]";
			} else {
				displayText += " " + new SimpleDateFormat(TimeModelDateBased.DEFAULT_TIME_FORMAT).format(new Date(this.timeStartSimSetup));
				displayText += " - <b>" + Language.translate("Formatted as", Language.EN) + ":</b> " + new SimpleDateFormat(timeFormatSimSetup).format(new Date(this.timeStartSimSetup)) + "";
			}
			displayText += "</html>";
			
			jLabelSimStart = new JLabel();
			jLabelSimStart.setText(displayText);
		}
		return jLabelSimStart;
	}
	/**
	 * This method initializes jCheckBoxTimeShiftAdjustToTime	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxTimeShiftAdjustToTime() {
		if (jCheckBoxTimeShiftAdjustToTime == null) {
			jCheckBoxTimeShiftAdjustToTime = new JCheckBox();
			jCheckBoxTimeShiftAdjustToTime.setText("Adjust time shift to the following start time:");
			jCheckBoxTimeShiftAdjustToTime.setText(Language.translate(jCheckBoxTimeShiftAdjustToTime.getText(), Language.EN));
			jCheckBoxTimeShiftAdjustToTime.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxTimeShiftAdjustToTime.addActionListener(this);
		}
		return jCheckBoxTimeShiftAdjustToTime;
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
	 * This method initializes jPanelLine2	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLine2() {
		if (jPanelLine2 == null) {
			jPanelLine2 = new JPanel();
			jPanelLine2.setLayout(new GridBagLayout());
			jPanelLine2.setPreferredSize(new Dimension(100, 2));
			jPanelLine2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return jPanelLine2;
	}
	/**
	 * This method initializes jButtonOK	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText("OK");
			jButtonOK.setText(Language.translate(jButtonOK.getText(), Language.EN));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setPreferredSize(new Dimension(80, 26));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}
	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Cancel");
			jButtonCancel.setText(Language.translate(jButtonCancel.getText(), Language.EN));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setPreferredSize(new Dimension(80, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	/**
	 * This method initializes jPanelButtons	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = -1;
			gridBagConstraints6.insets = new Insets(0, 80, 0, 0);
			gridBagConstraints6.fill = GridBagConstraints.NONE;
			gridBagConstraints6.gridy = -1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = -1;
			gridBagConstraints5.insets = new Insets(0, 0, 0, 80);
			gridBagConstraints5.gridy = -1;
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonOK(), gridBagConstraints5);
			jPanelButtons.add(getJButtonCancel(), gridBagConstraints6);
		}
		return jPanelButtons;
	}
	/**
	 * This method initializes jPanelTextExample	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTextExample() {
		if (jPanelTextExample == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints10.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(5, 10, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(5, 0, 0, 10);
			
			jLabelFromFile = new JLabel();
			jLabelFromFile.setText("Example String from file");
			jLabelFromFile.setText(Language.translate(jLabelFromFile.getText(), Language.EN));
			jLabelFromFile.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelParsed = new JLabel();
			jLabelParsed.setText("Parsed Date from Example");
			jLabelParsed.setText(Language.translate(jLabelParsed.getText(), Language.EN));
			jLabelParsed.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelTextExample = new JPanel();
			jPanelTextExample.setLayout(new GridBagLayout());
			jPanelTextExample.add(getJTextFieldFromFile(), gridBagConstraints3);
			jPanelTextExample.add(getJTextFieldParsed(), gridBagConstraints4);
			jPanelTextExample.add(jLabelFromFile, gridBagConstraints9);
			jPanelTextExample.add(jLabelParsed, gridBagConstraints10);
		}
		return jPanelTextExample;
	}
	
	/**
	 * Load the check box configuration from the file properties.
	 */
	private void loadCheckBoxConfiguration() {
		
		boolean timeOffset = Application.getGlobalInfo().getBooleanFromConfiguration(PROP_TimeOffSet, false);
		boolean timeOffsetSimSetup = Application.getGlobalInfo().getBooleanFromConfiguration(PROP_TimeOffSetSimulationSetup, false);
		boolean timeOffsetManual = Application.getGlobalInfo().getBooleanFromConfiguration(PROP_TimeOffSetManual, false);
		
		this.getJCheckBoxTimeShiftAdjust().setSelected(timeOffset);
		this.getJCheckBoxTimeShiftUseSimulationSetup().setSelected(timeOffsetSimSetup);
		this.getJCheckBoxTimeShiftAdjustToTime().setSelected(timeOffsetManual);
		this.doCheckBoxConfiguration(this.getJCheckBoxTimeShiftAdjust());
		
	}
	/**
	 * Sets the check box configuration.
	 */
	private void doCheckBoxConfiguration(JCheckBox initiator) {
		
		if (initiator==this.getJCheckBoxTimeShiftAdjust()) {
			if (initiator.isSelected()==true) {
				this.getJCheckBoxTimeShiftUseSimulationSetup().setEnabled(true);
				this.getJCheckBoxTimeShiftAdjustToTime().setEnabled(true);
				
			} else {
				this.getJCheckBoxTimeShiftUseSimulationSetup().setEnabled(false);
				this.getJCheckBoxTimeShiftAdjustToTime().setEnabled(false);
				this.getJSpinnerDateStart().setEnabled(false);
				this.getJSpinnerTimeStart().setEnabled(false);
				this.getJSpinnerMillisStart().setEnabled(false);
			}
			
		} else if (initiator==this.getJCheckBoxTimeShiftUseSimulationSetup()) {
			this.getJCheckBoxTimeShiftAdjustToTime().setSelected(!initiator.isSelected());
			
		} else if (initiator==this.getJCheckBoxTimeShiftAdjustToTime()) {
			this.getJCheckBoxTimeShiftUseSimulationSetup().setSelected(!initiator.isSelected());
			
		}
		
		// --- In case nothing is selected ------------------------------------
		if (this.getJCheckBoxTimeShiftUseSimulationSetup().isSelected()==false && this.getJCheckBoxTimeShiftAdjustToTime().isSelected()==false) {
			this.getJCheckBoxTimeShiftUseSimulationSetup().setSelected(true);
		}
		
		// --- Set the JSpinner for the date configuration enabled or not -----  
		if (this.getJCheckBoxTimeShiftAdjust().isSelected() & this.getJCheckBoxTimeShiftAdjustToTime().isSelected()) {
			this.getJSpinnerDateStart().setEnabled(true);
			this.getJSpinnerTimeStart().setEnabled(true);
			this.getJSpinnerMillisStart().setEnabled(true);
		} else {
			this.getJSpinnerDateStart().setEnabled(false);
			this.getJSpinnerTimeStart().setEnabled(false);
			this.getJSpinnerMillisStart().setEnabled(false);
		}
	
		// --- Save the current check box settings ----------------------------
		Application.getGlobalInfo().putBooleanToConfiguration(PROP_TimeOffSet, this.getJCheckBoxTimeShiftAdjust().isSelected());
		Application.getGlobalInfo().putBooleanToConfiguration(PROP_TimeOffSetSimulationSetup, this.getJCheckBoxTimeShiftUseSimulationSetup().isSelected());
		Application.getGlobalInfo().putBooleanToConfiguration(PROP_TimeOffSetManual, this.getJCheckBoxTimeShiftAdjustToTime().isSelected());
		
	}
	
	/**
	 * Sets the time displayed in the JSpinner.
	 * @param timeStamp the new timestamp as long value
	 */
	private void setJSpinnerTime(long timeStamp) {
		Calendar calendarWork = Calendar.getInstance();
		Date startDate = new Date(timeStamp);
		calendarWork.setTime(startDate);
		this.getJSpinnerDateStart().setValue(startDate);
		this.getJSpinnerTimeStart().setValue(startDate);
		this.getJSpinnerMillisStart().setValue(calendarWork.get(Calendar.MILLISECOND));
	}
	/**
	 * Returns the time configured in the JSpinner as long.
	 * @return the time configured in the JSpinner as long.
	 */
	private Long getJSpinnerTime() {

		Calendar calendarWork = Calendar.getInstance();
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
		return startLong;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	@Override
	public void stateChanged(ChangeEvent ce) {
		Object ceTrigger = ce.getSource();
		if (ceTrigger instanceof JSpinner) {
			this.setFilePropertyManualStartTimeForOffset(this.getJSpinnerTime().toString());
		}	
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object trigger = ae.getSource();
		
		if (trigger==this.getTimeFormatSelector()) {
			this.setExampleParse();
	
		} else if (trigger==this.getJCheckBoxTimeShiftAdjust()) {
			this.doCheckBoxConfiguration(this.getJCheckBoxTimeShiftAdjust());
		} else  if (trigger==this.getJCheckBoxTimeShiftUseSimulationSetup()) {
			this.doCheckBoxConfiguration(this.getJCheckBoxTimeShiftUseSimulationSetup());
		} else  if (trigger==this.getJCheckBoxTimeShiftAdjustToTime()) {
			this.doCheckBoxConfiguration(this.getJCheckBoxTimeShiftAdjustToTime());
			
		} else if (trigger==this.getJButtonCancel()) {
			this.setCanceled(true);
			this.setVisible(false);
			
		} else if (trigger==this.getJButtonOK()) {
			
			if (isError()==true) {
				
				String title = Language.translate("Error while parsing the time information!", Language.EN);
				String msg = "An error occurred while parsing the given time String!\n";
				msg += "Do you want to proceed anyway?";
				msg = Language.translate(msg, Language.EN);
				
				List<Object> options = new ArrayList<Object>();
				options.add(UIManager.getString("OptionPane.yesButtonText"));
				options.add(UIManager.getString("OptionPane.noButtonText"));
				Object defaultOption = UIManager.getString("OptionPane.noButtonText");
				int userAnswer = JOptionPane.showOptionDialog(null, msg, title, JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, null, options.toArray(), defaultOption);
				if (userAnswer==1) {
					return;
				}
				
			}
			this.setCanceled(false);
			this.setFilePropertyCsvTimeFormat(this.getTimeFormat());
			this.setVisible(false);
			
		}
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
