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
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
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
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.common.ExceptionHandling;
import agentgui.simulationService.time.TimeFormatSelection;

/**
 * The Dialog TimeFormatImportConfiguration is used to set the 
 * format of timestamps for the import to a TimeSeriesChart.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormatImportConfiguration extends JDialog implements ActionListener {

	private static final long serialVersionUID = -7271198574951719361L;
	
	private final String PathImage = Application.getGlobalInfo().PathImageIntern();
	private final ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private final Image imageAgentGUI = iconAgentGUI.getImage();

	public static final String CsvTimeFormatProperty = "CSV_IMPORT_TIMEFORMAT";
	
	private boolean canceled = false;
	private boolean error = false;
	
	private File csvFile = null;
	private String dataExampleFromFile = null;
	
	private JPanel jContentPane = null;
	private JLabel jLabelHeader = null;
	private TimeFormatSelection timeFormatSeletor = null;

	private JPanel jPanelTextExample = null;
	private JLabel jLabelFromFile = null;
	private JTextField jTextFieldFromFile = null;
	private JLabel jLabelParsed = null;
	private JTextField jTextFieldParsed = null;

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

		this.setSize(540, 245);
		this.setTitle(Language.translate("CSV-File", Language.EN) + " Import: " + Language.translate("Time Format", Language.EN));
		this.setIconImage(imageAgentGUI);
		
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
		this.setContentPane(getJContentPane());
		this.getTimeFormatSelector().setTimeFormat(this.getFilePropertyCsvTimeFormat());
		this.setExampleFileData();
		this.setExampleParse();
		
		// --- Set the Look and Feel of the Dialog ------------------
		if (Application.isRunningAsServer()==true) {
			if (Application.getGlobalInfo().getAppLnF()!=null) {
				setLookAndFeel(Application.getGlobalInfo().getAppLnF());
			}
		}

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
	private void setLookAndFeel(String newLnF) {
 
		if (newLnF==null) return;		
		Application.getGlobalInfo().setAppLnf(newLnF);
		try {
			String lnfClassname = Application.getGlobalInfo().getAppLnF();
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
				UIManager.setLookAndFeel(lnfClassname);
				SwingUtilities.updateComponentTreeUI(this);
				
		} catch (Exception e) {
			System.err.println("Cannot install " + Application.getGlobalInfo().getAppLnF() + " on this platform:" + e.getMessage());
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
	 * Gets the file property for the CSV time format.
	 * @return the file property for the CSV time format
	 */
	private String getFilePropertyCsvTimeFormat() {
		return Application.getGlobalInfo().getFileProperties().getProperty(CsvTimeFormatProperty, this.getTimeFormatSelector().getTimeFormatDefault());
	}
	/**
	 * Sets the file property of the CSV time format.
	 * @param newCsvTimeFormat the new file property for the CSV time format
	 */
	private void setFilePropertyCsvTimeFormat(String newCsvTimeFormat) {
		Application.getGlobalInfo().getFileProperties().setProperty(CsvTimeFormatProperty, newCsvTimeFormat);
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
					this.dataExampleFromFile = timeStamp;
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

		if (dataExampleFromFile==null) {
			this.getJTextFieldParsed().setText("null");
			
		} else {
			try {
				// --- Try to parse the example String first --------
				DateFormat df = new SimpleDateFormat(this.getTimeFormat());
				Date dateParsed =  df.parse(this.dataExampleFromFile);
				
				// --- Try to display the date in a standard way ----
				DateFormat dfParsed = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss,SSS");
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
			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.gridx = 1;
			gridBagConstraints91.fill = GridBagConstraints.BOTH;
			gridBagConstraints91.weighty = 1.0;
			gridBagConstraints91.weightx = 1.0;
			gridBagConstraints91.gridy = 7;
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
			gridBagConstraints7.gridy = 6;
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
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object trigger = ae.getSource();
		if (trigger==this.getTimeFormatSelector()) {
			this.setExampleParse();
			
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
