package de.enflexit.awb.baseUI.options;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import de.enflexit.awb.baseUI.BundleHelper;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.common.PathHandling;
import de.enflexit.language.Language;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import java.awt.event.ActionEvent;

/**
 * The Class LoggingInformationTab extends an {@link AbstractOptionTab} and is
 * used to inform the user about the available logging options and how to configure/ start them.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoggingInformationTab extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String GITHUB_EXAMPLE_LINK = "https://github.com/EnFlexIT/AgentWorkbench/blob/master/eclipseProjects/de.enflexit.awb/bundles/core/de.enflexit.logging/properties/logbackForFileLogging.xml";
	private static final String FACTORY_ID = "de.enflexit.logging.db";
	
	private JLabel jLabelHeader;
	private JEditorPane jTextAreaFileLogging;
	private JLabel jLabelHJeaderFileLogging;
	private JTextField jTextFieldIntroText;
	private JLabel jLabelHeaderDatabaseLogging;
	private JTextArea jTextAreaDatabaseLogging;
	private JLabel jLabelLogFilePath;
	private JButton jButtonGithub;
	private JButton jButtonShowDatabaseSettings;

	/**
	 * Instantiates a new logging information tab.
	 */
	public LoggingInformationTab() {
		super();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(5, 10, 0, 0);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_jLabelHeader);
		GridBagConstraints gbc_jTextFieldIntroText = new GridBagConstraints();
		gbc_jTextFieldIntroText.gridwidth = 2;
		gbc_jTextFieldIntroText.anchor = GridBagConstraints.WEST;
		gbc_jTextFieldIntroText.fill = GridBagConstraints.VERTICAL;
		gbc_jTextFieldIntroText.insets = new Insets(5, 10, 0, 0);
		gbc_jTextFieldIntroText.gridx = 0;
		gbc_jTextFieldIntroText.gridy = 1;
		add(getJTextFieldIntroText(), gbc_jTextFieldIntroText);
		GridBagConstraints gbc_jLabelHJeaderFileLogging = new GridBagConstraints();
		gbc_jLabelHJeaderFileLogging.anchor = GridBagConstraints.WEST;
		gbc_jLabelHJeaderFileLogging.insets = new Insets(15, 10, 0, 0);
		gbc_jLabelHJeaderFileLogging.gridx = 0;
		gbc_jLabelHJeaderFileLogging.gridy = 2;
		add(getJLabelHJeaderFileLogging(), gbc_jLabelHJeaderFileLogging);
		GridBagConstraints gbc_jButtonGithub = new GridBagConstraints();
		gbc_jButtonGithub.anchor = GridBagConstraints.WEST;
		gbc_jButtonGithub.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonGithub.gridx = 1;
		gbc_jButtonGithub.gridy = 2;
		add(getJButtonGithub(), gbc_jButtonGithub);
		GridBagConstraints gbc_jTextAreaFileLogging = new GridBagConstraints();
		gbc_jTextAreaFileLogging.gridwidth = 2;
		gbc_jTextAreaFileLogging.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextAreaFileLogging.insets = new Insets(5, 10, 0, 0);
		gbc_jTextAreaFileLogging.gridx = 0;
		gbc_jTextAreaFileLogging.gridy = 3;
		add(getJTextAreaFileLogging(), gbc_jTextAreaFileLogging);
		GridBagConstraints gbc_jLabelLogFilePath = new GridBagConstraints();
		gbc_jLabelLogFilePath.insets = new Insets(0, 10, 0, 0);
		gbc_jLabelLogFilePath.anchor = GridBagConstraints.WEST;
		gbc_jLabelLogFilePath.gridx = 0;
		gbc_jLabelLogFilePath.gridy = 4;
		add(getJLabelLogFilePath(), gbc_jLabelLogFilePath);
		GridBagConstraints gbc_jLabelHeaderDatabaseLogging = new GridBagConstraints();
		gbc_jLabelHeaderDatabaseLogging.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeaderDatabaseLogging.insets = new Insets(15, 10, 0, 0);
		gbc_jLabelHeaderDatabaseLogging.gridx = 0;
		gbc_jLabelHeaderDatabaseLogging.gridy = 5;
		add(getJLabelHeaderDatabaseLogging(), gbc_jLabelHeaderDatabaseLogging);
		GridBagConstraints gbc_jButtonShowDatabaseSettings = new GridBagConstraints();
		gbc_jButtonShowDatabaseSettings.anchor = GridBagConstraints.WEST;
		gbc_jButtonShowDatabaseSettings.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonShowDatabaseSettings.gridx = 1;
		gbc_jButtonShowDatabaseSettings.gridy = 5;
		add(getJButtonShowDatabaseSettings(), gbc_jButtonShowDatabaseSettings);
		GridBagConstraints gbc_jTextAreaDatabaseLogging = new GridBagConstraints();
		gbc_jTextAreaDatabaseLogging.gridwidth = 2;
		gbc_jTextAreaDatabaseLogging.fill = GridBagConstraints.BOTH;
		gbc_jTextAreaDatabaseLogging.insets = new Insets(5, 10, 0, 0);
		gbc_jTextAreaDatabaseLogging.gridx = 0;
		gbc_jTextAreaDatabaseLogging.gridy = 6;
		add(getJTextAreaDatabaseLogging(), gbc_jTextAreaDatabaseLogging);
		this.initialize();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Logging");
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Übersicht über die verfügbaren Logging-Optionen");
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(837, 619);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	}
	
	/**
	 * Returns the path to the log files directory.
	 *
	 * @return the log location
	 */
	private String getLogLocation() {
		return PathHandling.getLoggingFilesBasePathDefault().toString();
	}

	/**
	 * Returns the j label header.
	 *
	 * @return the j label header
	 */
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel(Language.translate("Logging-Konfiguration"));
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	
	/**
	 * Returns the j text field intro text.
	 *
	 * @return the j text field intro text
	 */
	private JTextField getJTextFieldIntroText() {
		if (jTextFieldIntroText == null) {
			jTextFieldIntroText = new JTextField(Language.translate("Agent.Workbench unterstützt die Ausgabe von Logging-Meldungen in die AWB-Konsole, in Logging-Dateien und in eine Datenbank."));
			jTextFieldIntroText.setFocusable(false);
			jTextFieldIntroText.setCaretPosition(0);
			jTextFieldIntroText.setEditable(false);
			jTextFieldIntroText.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldIntroText.setBorder(null);
		}
		return jTextFieldIntroText;
	}
	
	/**
	 * Returns the j text area file logging.
	 *
	 * @return the j text area file logging
	 */
	private JEditorPane getJTextAreaFileLogging() {
		if (jTextAreaFileLogging == null) {
			jTextAreaFileLogging = new JEditorPane();
			jTextAreaFileLogging.setMargin(new Insets(0, 0, 0, 0));
			jTextAreaFileLogging.setBorder(null);
			final String FILE_LOGGING_1 = Language.translate("[Optional] Zur Aktivierung des Datei-Loggings muss die Datei ./properties/logback.xml editiert werden. Eine Vorlage hierfür finden sie auf GitHub.");
			final String FILE_LOGGING_2 = Language.translate("Die relevanten Änderungen umfassen den „FILE_APPENDER“, den „ASYNC_FILE_APPENDER“ und einen zusätzlichen Eintrag innerhalb des <root>-Tags.");
			final String FILE_LOGGING_3 = Language.translate("In der Beispielkonfiguration erstellt der FILE_APPENDER eine Logdatei pro Tag.");
			final String FILE_LOGGING_4 = Language.translate("Für weitere Konfigurationsmöglichkeiten verweisen wir auf die Logback-Dokumentation:");
			final String FILE_LOGGING_5 = " https://logback.qos.ch/documentation.html";
			final String FILE_LOGGING_6 = Language.translate("Logdateien werden in das folgende Verzeichnis geschrieben:");
			jTextAreaFileLogging.setText(
					FILE_LOGGING_1 +"\n"
					+ FILE_LOGGING_2 +"\n"
					+ FILE_LOGGING_3 +"\n"
					+ FILE_LOGGING_4 
					+ FILE_LOGGING_5 + "\n\n"
					+ FILE_LOGGING_6
					); 
			jTextAreaFileLogging.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaFileLogging.setEditable(false);
		}
		return jTextAreaFileLogging;
	}
	
	/**
	 * Returns the j label H jeader file logging.
	 *
	 * @return the j label H jeader file logging
	 */
	private JLabel getJLabelHJeaderFileLogging() {
		if (jLabelHJeaderFileLogging == null) {
			jLabelHJeaderFileLogging = new JLabel(Language.translate("Aktivierung des Datei-Loggings "));
			jLabelHJeaderFileLogging.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHJeaderFileLogging;
	}
	
	/**
	 * Returns the j label header database logging.
	 *
	 * @return the j label header database logging
	 */
	private JLabel getJLabelHeaderDatabaseLogging() {
		if (jLabelHeaderDatabaseLogging == null) {
			jLabelHeaderDatabaseLogging = new JLabel(Language.translate("Aktivierung des Datenbank-Logging "));
			jLabelHeaderDatabaseLogging.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeaderDatabaseLogging;
	}
	
	/**
	 * Returns the j text area database logging.
	 *
	 * @return the j text area database logging
	 */
	private JTextArea getJTextAreaDatabaseLogging() {
		if (jTextAreaDatabaseLogging == null) {
			jTextAreaDatabaseLogging = new JTextArea();
			jTextAreaDatabaseLogging.setMargin(new Insets(0, 0, 0, 0));
			jTextAreaDatabaseLogging.setBorder(null);
			jTextAreaDatabaseLogging.setEditable(false);
			jTextAreaDatabaseLogging.setWrapStyleWord(true);
			jTextAreaDatabaseLogging.setText(Language.translate("[Optional] Zur Aktivierung des Datenbank-Loggings muss lediglich die Datenbankverbindung mit der Factory-ID 'de.enflexit.logging.db' konfiguriert werden."));
			jTextAreaDatabaseLogging.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextAreaDatabaseLogging.setLineWrap(true);
		}
		return jTextAreaDatabaseLogging;
	}
	
	/**
	 * Returns the j label log file path.
	 *
	 * @return the j label log file path
	 */
	private JLabel getJLabelLogFilePath() {
		if (jLabelLogFilePath == null) {
			jLabelLogFilePath = new JLabel(getLogLocation());
			jLabelLogFilePath.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelLogFilePath;
	}
	
	/**
	 * Returns the j button github.
	 *
	 * @return the j button github
	 */
	private JButton getJButtonGithub() {
		if (jButtonGithub == null) {
			jButtonGithub = new JButton(Language.translate("Beispiel auf GitHub öffnen"));
			jButtonGithub.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Application.browseURI(GITHUB_EXAMPLE_LINK);
				}
			});
			jButtonGithub.setIcon(BundleHelper.getImageIcon("GitHub.png"));
			jButtonGithub.setPreferredSize(new Dimension(200, 26));
		}
		return jButtonGithub;
	}
	
	/**
	 * Returns the j button show database settings.
	 *
	 * @return the j button show database settings
	 */
	private JButton getJButtonShowDatabaseSettings() {
		if (jButtonShowDatabaseSettings == null) {
			jButtonShowDatabaseSettings = new JButton(Language.translate("Datenbank - Einstellungen"));
			jButtonShowDatabaseSettings.setIcon(GlobalInfo.getInternalImageIcon("DB_State_Blue.png"));
			jButtonShowDatabaseSettings.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Application.showDatabaseDialog(FACTORY_ID);
				}
			});
			jButtonShowDatabaseSettings.setPreferredSize(new Dimension(200, 26));
		}
		return jButtonShowDatabaseSettings;
	}
}  