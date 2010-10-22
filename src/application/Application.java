package application;

import benchmark.BenchmarkMeasurement;
import gui.AboutDialog;
import gui.CoreWindow;
import gui.CoreWindowConsole;
import gui.options.OptionDialog;
import mas.Platform;
import mas.service.load.LoadMeasureThread;
import systemtray.AgentGUITrayIcon;
import config.FileProperties;
import config.GlobalInfo;
import database.DBConnection;

/**
 * @author: Christian Derksen  	
 */
public class Application {
		
	private static Application thisApp = new Application(); 
	
	public static GlobalInfo RunInfo = null;
	public static CoreWindowConsole Console = null;
	public static FileProperties properties = null;
	public static Platform JadePlatform = null;	
	
	public static AgentGUITrayIcon trayIconInstance = null;
	public static boolean isServer = false;
	
	public static ProjectsLoaded Projects = null;
	public static Project ProjectCurr = null;
	public static CoreWindow MainWindow = null;
	public static DBConnection DBconnection = null;
	
	private static AboutDialog about = null;
	private static OptionDialog options = null;

	public static boolean benchmarkIsRunning = false; 
	
	
	// --- Singelton-Construct ---
	private Application() {
	}	
	public static Application getInstance() {
		return thisApp;
	}
	
	/**
	 * main-method for the start of the application 
	 * @param args
	 */
	public static void main( String[] args ) {
		
		// ----------------------------------------------------------
		// --- Just starts the base-instances -----------------------
		RunInfo = new GlobalInfo();
		Console = new CoreWindowConsole();
		properties = new FileProperties();
		new LoadMeasureThread().start();  
		startAgentGUI();		
	}	

	
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
			Projects.setProjectMenuItems();
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
		Language.SaveDictionaryFile();
		System.exit(0);		
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
		Projects.setProjectMenuItems();
	}	
	
	/**
	 * Changing the application language to:
	 * NewLang => "DE", "EN", "IT", "ES" or "FR" etc. is equal to the 
	 * end phrase after the prefix "LANG_". E.g. "LANG_EN" needs "EN" as parameter
	 */
	public static void setLanguage( String NewLang ) {

		// --- jade stoppen ---------------------		
		JadePlatform.jadeStop();
		// --- Projekte schliessen --------------
		if ( Projects != null ) {
			if ( Projects.closeAll() == false ) return;	
		}
		// --- Sprache umstellen ----------------
		Language.changeLanguageTo(NewLang);
		// --- Anwendungsfenster schliessen -----
		MainWindow.dispose();
		MainWindow = null;
		// --- Anwendung neu öffnen -------------
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
	
	
} // --- End Class ---

