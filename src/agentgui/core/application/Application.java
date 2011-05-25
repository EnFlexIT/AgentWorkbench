package agentgui.core.application;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import agentgui.core.benchmark.BenchmarkMeasurement;
import agentgui.core.config.FileProperties;
import agentgui.core.config.GlobalInfo;
import agentgui.core.database.DBConnection;
import agentgui.core.gui.AboutDialog;
import agentgui.core.gui.CoreWindow;
import agentgui.core.gui.CoreWindowConsole;
import agentgui.core.gui.Translation;
import agentgui.core.gui.options.OptionDialog;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.Platform;
import agentgui.core.systemtray.AgentGUITrayIcon;
import agentgui.core.webserver.DownloadServer;
import agentgui.simulationService.load.LoadMeasureThread;

/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Application {
		
	private static Application thisApp = new Application(); 
	
	public static GlobalInfo RunInfo = null;
	public static CoreWindowConsole Console = null;
	public static FileProperties properties = null;
	public static Platform JadePlatform = null;	
	public static DownloadServer webServer = null;
	public static ClassSearcher ClassDetector = null;
	
	public static AgentGUITrayIcon trayIconInstance = null;
	public static boolean isServer = false;
	
	public static ProjectsLoaded Projects = null;
	public static Project ProjectCurr = null;
	public static CoreWindow MainWindow = null;
	public static DBConnection DBconnection = null;
	
	private static AboutDialog about = null;
	private static OptionDialog options = null;

	public static boolean benchmarkIsRunning = false; 
	
	private static String project2OpenAfterStart = null;
	
	// --- Singleton-Constructor ---
	private Application() {
	}	
	public static Application getInstance() {
		return thisApp;
	}
	
	/**
	 * main-method for the start of the application 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// ----------------------------------------------------------
		// --- Just starts the base-instances -----------------------
		RunInfo = new GlobalInfo();
		properties = new FileProperties();
		Language.startDictionary();
		proceedStartArguments(args);
		new LoadMeasureThread().start();  
		Console = new CoreWindowConsole();
		startAgentGUI();
		proceedStartArgumentOpenProject();
		
	}	

	/**
	 * This method will proceed the arguments which are given
	 * from the  
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
				// --- specify if the console should be used or not -----------
				} else if (args[i].equalsIgnoreCase("-console")) {
					i++;
					if (args[i].equalsIgnoreCase("off")) {
						RunInfo.setAppUseInternalConsole(false);
					}
					
				// ------------------------------------------------------------
				// --- print out the hel for the start argumetns --------------					
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
		System.out.println("2. '-console off'          : switches off the applications console output i. o. to keep the output at the IDE" );
		System.out.println("3. '-help' or '-?'         : provides this information to the console" );
		System.out.println("");
		System.out.println("");
	}
	/**
	 * This methode can be invoked in order to start a project.
	 * It was implemented to open a project given by the start arguments
	 * @param projectFolder
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
	 * depending on the configuration in 'properies\agentgui.ini'
	 */
	private static void startAgentGUI() {
		
		// ----------------------------------------------------------		
		// --- Start the Application as defined by 'isServer' -------
		JadePlatform = new Platform();
		trayIconInstance = new AgentGUITrayIcon();
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
	 * Opens the Main-Window (JFrame)
	 */
	public static void startApplication() {
 		// --- open Main-Dialog -------------------------------------		
		MainWindow = null;
		MainWindow = new CoreWindow();		
		Projects.setProjectView();
	}
	
	/**
	 * Starts the Main-Procedure for the Server-Version of Agent.GUI
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
			trayIconInstance.popUp.refreshView();
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
		properties.save();
		
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
	 * Opens the Configuration-Dialog for the 
	 * AgentGUI-Application with a specified TabName
	 * @param showTab
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
				trayIconInstance.remove();
				trayIconInstance = null;

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
				trayIconInstance.remove();
				trayIconInstance = null;
			}
			// --- Restart ------------------------------------------
			startAgentGUI();
		}
		// ==================================================================
	}

	/**
	 * Opens the Configuration-Dialog for the 
	 * AgentGUI-Application with a specified TabName
	 * @param showTab
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
	 * Adds a complementation to the application title
	 * @param Add2BasicTitel
	 */
	public static void setTitelAddition( String Add2BasicTitel ) {
		MainWindow.setTitelAddition(Add2BasicTitel);
	}

	/**
	 * Sets the JADE Status- Visulazitation for the User 
	 * @param runs  
	 */
	public static void setStatusJadeRunning(boolean runs) {
		if (MainWindow!=null) {
			MainWindow.setStatusJadeRunning(runs);	
		}
		trayIconInstance.popUp.refreshView();
	}
	
	/**
	 * Sets the text of the status bar
	 * @param Message
	 */
	public static void setStatusBar( String Message ) {
		MainWindow.setStatusBar(Message);
	}
	
	/**
	 * Set's the Look and feel of the application
	 * @param NewLnF
	 */
	public static void setLookAndFeel( String NewLnF ) {
		MainWindow.setLookAndFeel(NewLnF);
		Projects.setProjectView();
	}	
	
	/**
	 * Changing the application language to:
	 * NewLang => "DE", "EN", "IT", "ES" or "FR" etc. is equal to the 
	 * end phrase after the prefix "LANG_". E.g. "LANG_EN" needs "EN" as parameter
	 * 
	 * @param newLang
	 */
	public static void setLanguage(String newLang) {
		setLanguage(newLang, true);
	}
	/**
	 * Changing the application language to:
	 * NewLang => "DE", "EN", "IT", "ES" or "FR" etc. is equal to the 
	 * end phrase after the prefix "LANG_". E.g. "LANG_EN" needs "EN" as parameter.
	 * 
	 * If 'askUser' is set to false the changes will be done without user interaction. 
	 * 
	 * @param newLang
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
	 * Executes the Benchmark-Test of SciMark 2.0 to determine the abbility 
	 * of this system to deal with numbers. The result will be available in 
	 * Mflops (Millions of floating point operations per second)
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
	 * Starts the Web-Server, so that remote server.slaves are
	 * able to download additional jar-resources
	 */
	public static DownloadServer startDownloadServer() {
		
		if (webServer==null) {
			webServer = new DownloadServer();
			webServer.setRoot(RunInfo.PathServer(false));
			new Thread(webServer).start();
		}
		return webServer;
	}

	/**
	 * Stops the Web-Server for the resources download of 
	 * external server.clients
	 */
	public static void stopDownloadServer() {

		if (webServer!=null) {
			synchronized (webServer) {
				webServer.stop();	
			}			
			webServer = null;
		}
	}
	
	/**
	 * @param newApplicationTitle sets a new Application Title/Name 
	 */
	public static void setApplicationTitle(String newApplicationTitle) {
		RunInfo.setApplicationTitle(newApplicationTitle);
	}
	/**
	 * @return the Application Title/Name
	 */
	public String getApplicationTitle() {
		return RunInfo.getApplicationTitle();
	}
	
} // --- End Class ---

