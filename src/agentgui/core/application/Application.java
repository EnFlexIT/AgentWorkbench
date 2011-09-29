/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.core.application;

import jade.debugging.components.JPanelConsole;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import agentgui.core.benchmark.BenchmarkMeasurement;
import agentgui.core.config.FileProperties;
import agentgui.core.config.GlobalInfo;
import agentgui.core.database.DBConnection;
import agentgui.core.gui.AboutDialog;
import agentgui.core.gui.CoreWindow;
import agentgui.core.gui.Translation;
import agentgui.core.gui.options.OptionDialog;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.Platform;
import agentgui.core.systemtray.AgentGUITrayIcon;
import agentgui.core.webserver.DownloadServer;
import agentgui.simulationService.load.LoadMeasureThread;

/**
 * This is the main class of the application containing the main-method for the program execution.<br> 
 * This class is designed as singleton class in order to make it accessible from every context of the program. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Application {
	
	/**
	 * The instance of this singleton class
	 */
	private static Application thisApp = new Application(); 
	/**
	 * This attribute holds the current state of the configurable runtime informations  
	 */
	public static GlobalInfo RunInfo = null;
	/**
	 * Holds the instance of the file properties which are defined in '/properties/agentgui.ini' 
	 */
	public static FileProperties Properties = null;
	/**
	 * This is the instance of the main application window
	 */
	public static JPanelConsole Console = null;

	/**
	 * With this attribute/class the agent platform (JADE) will be controlled 
	 */
	public static Platform JadePlatform = null;
	/**
	 * This will hold the instance of the download-server, which will be started concurrently
	 * to the JADE platform. With this simple web-server the external resources (.jar - files), 
	 * configured in the project can be transfered to remote container
	 */
	public static DownloadServer WebServer = null;
	/**
	 * This ClassDetector is used in order to search for agent classes, ontology's and base service.
	 * If a project was newly opened, the search process will start again. 
	 */
	public static ClassSearcher ClassDetector = null;
	
	/**
	 * Here the tray icon of the application can be accessed
	 */
	public static AgentGUITrayIcon TrayIcon = null;
	/**
	 * If the application was executed as end user application, this attribute will be false.
	 * In case of the execution as a 'server'-tool (without GUI but with tray icon control), this will be true. 
	 */
	public static boolean isServer = false;
	
	/**
	 * Holds the list of the open projects. Can be also seen in the window menu of the applications main window
	 */
	public static ProjectsLoaded Projects = null;
	/**
	 * Holds the reference of the currently focused project
	 */
	public static Project ProjectCurr = null;
	/**
	 * This will hold the instance of the main application window 
	 */
	public static CoreWindow MainWindow = null;
	/**
	 * In case that this program instance was executed as 'server.master', this 
	 * will hold the connection to the database 
	 */
	public static DBConnection DBconnection = null;
	
	private static AboutDialog about = null;
	private static OptionDialog options = null;
	/**
	 * Indicates if the benchmark is currently running
	 */
	public static boolean benchmarkIsRunning = false; 
	
	private static String project2OpenAfterStart = null;
	
	// --- Singleton-Constructor ---
	private Application() {
	}	
	/**
	 * Will return the instance of this singleton class
	 * @return The instance of this class
	 */
	public static Application getInstance() {
		return thisApp;
	}
	
	/**
	 * main-method for the start of the application running as end user application or server-tool
	 * 
	 * @param args The arguments which can be configured in the command line. 
	 * -help will print all possible command line arguments 
	 */
	public static void main(String[] args) {

		// ----------------------------------------------------------
		// --- Just starts the base-instances -----------------------
		Console = new JPanelConsole(true);
		RunInfo = new GlobalInfo();
		Properties = new FileProperties();
		Language.startDictionary();
		proceedStartArguments(args);
		new LoadMeasureThread().start();  
		startAgentGUI();
		proceedStartArgumentOpenProject();
		
	}	

	/**
	 * This method will proceed the start-arguments of the application  
	 * @param args
	 */
	private static void proceedStartArguments(String[] args){
		
		int i = 0;
		while (i < args.length) {

			if (args[i].startsWith("-")) {
				
				// ------------------------------------------------------------
				// --- open a specified project ------------------------------- 
				if (args[i].equalsIgnoreCase("-project")) {
					i++;
					project2OpenAfterStart = args[i];
					
				// ------------------------------------------------------------
				// --- print out the help for the start arguments --------------					
				} else if (args[i].equalsIgnoreCase("-help")) {
					proceedStartArgumentPrintHelp();
				} else if (args[i].equalsIgnoreCase("-?")) {
					proceedStartArgumentPrintHelp();
				}
				
			} else {
				// --- unspecified start option ------------------------------- 
				System.out.println(Language.translate("Argument") + " " + (i+1) + " '" + args[i] + "': " + Language.translate("Bitte spezifizieren Sie den Typ des Startarguments!"));
			}
			
			// --- proceed next start argument ------------
			i++;
		}
		
	}
	
	/**
	 * Prints the start-arguments to the console
	 */
	private static void proceedStartArgumentPrintHelp() {
		
		System.out.println("Agent.GUI - usage of start arguments:");
		System.out.println("");
		System.out.println("1. '-project projectFolder': opens the project located in the Agent.GUI folder 'project' (e.g. 'myProject')");
		System.out.println("2. '-help' or '-?'         : provides this information to the console" );
		System.out.println("");
		System.out.println("");
	}
	/**
	 * This method can be invoked in order to start a project.
	 * It was implemented to open a project given by the start arguments
	 * after the needed instantiations are done.
	 */
	private static void proceedStartArgumentOpenProject() {
		
		if (isServer==false && project2OpenAfterStart!=null) {
			// --- open teh specified project -----------
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					Projects.add(project2OpenAfterStart);
					project2OpenAfterStart=null;
				}
			});
		}
	}
	
	
	/**
	 * This methods starts Agent.GUI in application or server mode
	 * depending on the configuration in 'properties/agentgui.ini'
	 */
	private static void startAgentGUI() {
		
		// ----------------------------------------------------------		
		// --- Start the Application as defined by 'isServer' -------
		JadePlatform = new Platform();
		TrayIcon = new AgentGUITrayIcon();
		if ( isServer == true ) {
			// ------------------------------------------------------
			// --- Start Server-Version of AgentGUI -----------------
			System.out.println( Language.translate("Programmstart [Server] ..." ) );
			// --- In the Server-Case, start the benchmark now ! ----
			doBenchmark(false);
			startServer();
			
		} else {
			// ------------------------------------------------------
			// --- Start Application --------------------------------
			System.out.println( Language.translate("Programmstart [Anwendung] ..." ) );
			Projects = new ProjectsLoaded();

			startApplication();
			MainWindow.setStatusBar( Language.translate("Fertig") );
			doBenchmark(false);
		}
	}
	
	/**
	 * Opens the main application window (JFrame)
	 */
	public static void startApplication() {
 		// --- open Main-Dialog -------------------------------------		
		MainWindow = null;
		MainWindow = new CoreWindow();		
		Projects.setProjectView();
	}
	
	/**
	 * Starts the main procedure for the Server-Version of Agent.GUI
	 */
	public static void startServer() {
		// --- Automatically Start JADE, if configured --------------
		if ( RunInfo.isServerAutoRun()==true ) {
			// --- Wait until the benchmark result is available -----
			while (LoadMeasureThread.getCompositeBenchmarkValue()==0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			
			JadePlatform.jadeStart();
			TrayIcon.popUp.refreshView();
		}
	}
	
	/**
	 * Quits the application
	 */
	public static void quit() {
		// --------------------------------------------
		// --- Anwendung beenden ---------------------- 
		// --------------------------------------------
		// --- JADE beenden ------------------------
		JadePlatform.jadeStop();
		// --- Noch offene Projekte schließen ------
		if ( Projects != null ) {
			if ( Projects.closeAll() == false ) return;	
		}		
		// --- FileProperties speichern ------------
		Properties.save();
		
		// --- Fertig ------------------------------
		System.out.println( Language.translate("Programmende... ") );
		Language.saveDictionaryFile();
		System.exit(0);		
	}

	/**
	 * Opens the translation dialog of the application
	 */
	public static void showTranslationDialog() {
		Translation trans = new Translation(Application.MainWindow);
		trans.setVisible(true);
	}
	
	/**
	 * Opens the Configuration-Dialog for the 
	 * AgentGUI-Application without a specific
	 * Tab to show
	 */
	public static void showOptionDialog() {
		showOptionDialog(null);
	}
	/**
	 * Opens the Option-Dialog of the application with a specified TabName
	 * @param focusOnTab Can be used to set the focus directly to a Tab specified by its name 
	 */
	public static void showOptionDialog(String focusOnTab) {
		
		boolean okAction = false;
		boolean enforceRestart = false;		
		boolean isServerOld = isServer;
		boolean isServerNew = isServer;
		
		if (options!=null) return;
		
		// ==================================================================
		if (isServer==true) {
			options = new OptionDialog(null);
		} else {
			options = new OptionDialog(MainWindow);
		}
		if (focusOnTab!=null) {
			options.setFocusOnTab(focusOnTab);
		}
		options.setVisible(true);
		// ==================================================================
		// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
		// ==================================================================
		okAction = !options.isCanceled();
		enforceRestart = options.isForceRestart();
		isServerNew = isServer;
		options.dispose();
		options = null;		
		// ==================================================================
		
		if ( (isServerOld==true && isServerNew==true && okAction==true) || enforceRestart==true) { 
			// --- JADE beenden -------------------------------------
			JadePlatform.jadeStop();			
			// --- Anwendung neu starten ----------------------------
			if (isServerOld == true) {
				if (isServerNew==true) {
					// --- Neustart 'Server'-------------------------
					System.out.println(Language.translate("Neustart des Server-Dienstes"));
				} else {
					// --- Umschalten von 'Server' auf 'Application' ----
					System.out.println(Language.translate("Umschalten von 'Server' auf 'Anwendung'"));
				}				
				// --- Tray-Icon entfernen / schliessen -------------
				TrayIcon.remove();
				TrayIcon = null;

			} else {
				// --- Umschalten von 'Application' auf 'Server' ----
				System.out.println(Language.translate("Umschalten von 'Anwendung' auf 'Server'"));
				// --- Noch offene Projekte schließen ---------------
				if ( Projects != null ) {
					if ( Projects.closeAll() == false ) return;	
				}		
				// --- Anwendungsfenster schliessen -----------------
				MainWindow.dispose();
				MainWindow = null;
				// --- Tray-Icon schliessen -------------------------
				TrayIcon.remove();
				TrayIcon = null;
			}
			// --- Restart ------------------------------------------
			startAgentGUI();
		}
		// ==================================================================
	}

	/**
	 * Will show the About-Dialog of the application
	 */
	public static void showAboutDialog() {
		
		if (about!=null) return;
		// ==================================================================
		if (isServer==true) {
			about = new AboutDialog(null);
		} else {
			about = new AboutDialog(MainWindow);
		}
		about.setVisible(true);
		// ==================================================================
		// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
		// ==================================================================
		about.dispose();
		about = null;		
	}

	/**
	 * Adds a supplement to the application title
	 * @param add2BasicTitel
	 */
	public static void setTitelAddition( String add2BasicTitel ) {
		MainWindow.setTitelAddition(add2BasicTitel);
	}

	/**
	 * Sets the status which shows if JADE is running (green or red in the right corner of the status bar)   
	 * @param runs  
	 */
	public static void setStatusJadeRunning(boolean runs) {
		if (MainWindow!=null) {
			MainWindow.setStatusJadeRunning(runs);	
		}
		TrayIcon.popUp.refreshView();
	}
	
	/**
	 * Enables to set the text of the status bar
	 * @param statusText
	 */
	public static void setStatusBar(String statusText) {
		MainWindow.setStatusBar(statusText);
	}
	
	/**
	 * Set's the Look and feel of the application
	 * @param newLnF
	 */
	public static void setLookAndFeel(String newLnF) {
		MainWindow.setLookAndFeel(newLnF);
		Projects.setProjectView();
	}	
	
	/**
	 * Enables to change the user language of the application   
	 * @param newLang => 'de', 'en', 'it', 'es' or 'fr' etc. is equal to the header line of the dictionary
	 */
	public static void setLanguage(String newLang) {
		setLanguage(newLang, true);
	}
	/**
	 * Enables to change the user language of the application  
	 * If 'askUser' is set to false the changes will be done without user interaction. 
	 * 
	 * @param newLang => 'de', 'en', 'it', 'es' or 'fr' etc. is equal to the header line of the dictionary
	 * @param askUser 
	 */
	public static void setLanguage(String newLang, boolean askUser) {

		String newLine = RunInfo.AppNewLineString();
		
		if (askUser) {
			// --- Sind die neue und die alte Anzeigesprach gleich ? ----
			Integer newLangIndex = Language.getIndexOfLanguage(newLang);
			if ( newLangIndex == Language.currLanguageIndex ) return; 
			
			// --- User fragen, ob die Sprache umgestellt werden soll ---
			String MsgHead = Language.translate("Anzeigesprache wechseln?");
			String MsgText = Language.translate(
							 "Möchten Sie die Anzeigesprache wirklich umstellen?" + newLine + 
							 "Die Anwendung muss hierzu neu gestartet und Projekte" + newLine +
							 "von Ihnen neu geöffnet werden.");
			Integer MsgAnswer = JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if (MsgAnswer==1) return;
			
		}
		
		// --- JADE stoppen -----------------------------------------		
		JadePlatform.jadeStop();
		// --- Projekte schliessen ----------------------------------
		if ( Projects != null ) {
			if ( Projects.closeAll() == false ) return;	
		}
		// --- Sprache umstellen ------------------------------------
		Language.changeApplicationLanguageTo(newLang);
		// --- Anwendungsfenster schliessen -------------------------
		MainWindow.dispose();
		MainWindow = null;
		// --- Anwendung neu öffnen ---------------------------------
		startApplication();	

	}	
	
	/**
	 * Executes the Benchmark-Test of SciMark 2.0 to determine the ability of this system to deal with number crunching.<br> 
	 * The result will be available in Mflops (Millions of floating point operations per second)
	 */
	public static void doBenchmark(boolean forceBenchmark) {
		// ------------------------------------------------
		// --- Merker beachten, damit die Messung immer --- 
		// --- nur einmal ausgeführt werden kann        ---
		if (Application.benchmarkIsRunning==false) {
			// --- Merker setzen --------------------------
			Application.benchmarkIsRunning = true;
			// --- Execute the Benchmark-Thread -----------
			new BenchmarkMeasurement(forceBenchmark).start();
		}
		// ------------------------------------------------
	}
	
	/**
	 * Starts the Web-Server, so that a remote server.slave is able 
	 * to download the additional jar-resources of a project
	 */
	public static DownloadServer startDownloadServer() {
		
		if (WebServer==null) {
			WebServer = new DownloadServer();
			WebServer.setRoot(RunInfo.PathServer(false));
			new Thread(WebServer).start();
		}
		return WebServer;
	}
	/**
	 * Stops the Web-Server for the resources download of an external server.slave
	 */
	public static void stopDownloadServer() {

		if (WebServer!=null) {
			synchronized (WebServer) {
				WebServer.stop();	
			}			
			WebServer = null;
		}
	}
	
	/**
	 * This method can be used in order to change the application title during runtime
	 * @param newApplicationTitle sets a new application title/name 
	 */
	public static void setApplicationTitle(String newApplicationTitle) {
		RunInfo.setApplicationTitle(newApplicationTitle);
	}
	/**
	 * This method return the current title of the application
	 * @return the Application title/name
	 */
	public String getApplicationTitle() {
		return RunInfo.getApplicationTitle();
	}
	
} // --- End Class ---

