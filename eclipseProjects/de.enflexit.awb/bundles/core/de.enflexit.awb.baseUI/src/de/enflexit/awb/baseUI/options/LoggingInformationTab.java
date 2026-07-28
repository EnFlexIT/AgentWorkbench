package de.enflexit.awb.baseUI.options;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import de.enflexit.common.PathHandling;
import de.enflexit.language.Language;

/**
 * The Class LoggingInformationTab extends an {@link AbstractOptionTab} and is
 * used in inform the user about the available logging options and how to configure/ start them.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoggingInformationTab extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
	private JTextPane jTextPaneInformation;

	private static final String TXT_GENERAL = "Die Agent.Workbench unterstützt die Speicherung von Logmeldungen in Logdateien und in Datenbanken. Die Aktivierung der einzelnen Logging-Varianten wird nachfolgend beschrieben.";
	
	private static final String TXT_CONSOLE = "Standardmäßig aktiv. Logmeldungen werden während der Laufzeit in der AWB-Konsole angezeigt.";

	private static final String TXT_FILE = "Optional. Eine Logdatei pro Tag, automatische Rotation und Aufbewahrung für 30 Tage.";
	private static final String TXT_FILE_PROPERTIES = "Im Verzeichnis properties befinden sich zwei Logback-Konfigurationen. Die Anwendung verwendet ausschließlich die Datei mit dem Namen logback.xml.";
	private static final String TXT_FILE_ACTIVATION_ONE = "1. Benennen Sie die aktuelle logback.xml um.";		
	private static final String TXT_FILE_ACTIVATION_TWO = "2. Benennen Sie fileLogging.xml in logback.xml um.";
	private static final String TXT_FILE_ACTIVATION_THREE = "3. Starten Sie die Anwendung neu.";
	private static final String TXT_FILE_HINT = "Im Verzeichnis darf immer nur eine Datei mit dem Namen logback.xml vorhanden sein.";
	
	private static final String TXT_DATABASE = "Optional. Logmeldungen können zusätzlich in einer Datenbank gespeichert werden.";
	private static final String TXT_DATABASE_CONFIGURATION = "Konfiguration über Datenbankverbindungen > Factory Settings > Factory-ID <b>de.enflexit.logging.db</b>.";
	private static final String TXT_DATABASE_ACTIVATION = "Nach erfolgreicher Erstellung der Verbindung wird das Datenbank-Logging automatisch aktiviert.";

	/**
	 * Instantiates a new logging information tab.
	 */
	public LoggingInformationTab() {
		super();
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
		
		this.setSize(555, 307);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
		this.add(getJScrollPane(), gbc);
		SwingUtilities.invokeLater(() ->
	    getJScrollPane().getVerticalScrollBar().setValue(0)
	);
	}

	private JScrollPane getJScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getJTextPaneInformation());
		}
		return scrollPane;
	}

	/**
	 * Returns the jtextpane information.
	 *
	 * @return the jtextpane information
	 */
	private JTextPane getJTextPaneInformation() {

		if (jTextPaneInformation == null) {

			jTextPaneInformation = new JTextPane();
			jTextPaneInformation.setEditable(false);
			jTextPaneInformation.setContentType("text/html");
			jTextPaneInformation.setFont(new Font("Dialog", Font.PLAIN, 12));

			String text =
					  "<html>"
					+ "<h2>" + Language.translate("Logging in der Agent.Workbench") + "</h2>"
					+ Language.translate(TXT_GENERAL) + "<br><br>"
					+"<hr>"
					
					// --- Console logging ----------------------------------------------
					+ "<h3>" + Language.translate("Konsole") + "</h3>"
					+ Language.translate(TXT_CONSOLE)
					+ "<br><br>"
					+ "<hr>"
					
					// --- File logging -------------------------------------------------
					+ "<h3>" + Language.translate("Datei-Logging") + "</h3>"
					+ Language.translate(TXT_FILE)
					+ "<br>"
					+ Language.translate(TXT_FILE_PROPERTIES) + "<br><br>"
					
					+ "<b>"+Language.translate("Aktivierung:") +"</b> <br>"
					+ Language.translate(TXT_FILE_ACTIVATION_ONE) + "<br>"
					+ Language.translate(TXT_FILE_ACTIVATION_TWO) + "<br>"
					+ Language.translate(TXT_FILE_ACTIVATION_THREE) + "<br><br>"
					
					+ "<b>" +Language.translate("Hinweis") + ":</b> <br>"
					+ Language.translate(TXT_FILE_HINT) + "<br><br>"
					
					+ "<b>" + Language.translate("Properties Verzeichnis") + ": </b> <br>" 
					+ "&lt;" +Language.translate("Installationsverzeichnis")+"&gt;/properties"
					+ "<br><br>"
					+ getLogLocation() +"<br><br>"
					+ "<hr>"
					
					// --- Database logging ---------------------------------------------
					+ "<h3>" + Language.translate("Datenbank-Logging") + "</h3>"
					+ Language.translate(TXT_DATABASE) + "<br><br>"
					+ "<b>"+Language.translate("Aktivierung:") +"</b> <br>"
					+ Language.translate(TXT_DATABASE_CONFIGURATION)
					+ "<br>"
					+ Language.translate(TXT_DATABASE_ACTIVATION)
					+ "</html>";

			jTextPaneInformation.setText(text);
		}

		return jTextPaneInformation;
	}

	
	/**
	 * Returns the path to the log files directory.
	 *
	 * @return the log location
	 */
	private String getLogLocation() {
		return "<b>" + Language.translate("Log Verzeichnis") + ":</b><br> "+ PathHandling.getLoggingFilesBasePathDefault();
	}

}  