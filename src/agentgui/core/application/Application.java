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
package agentgui.core.application;

import jade.debugging.components.JPanelConsole;

import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import agentgui.core.benchmark.BenchmarkMeasurement;
import agentgui.core.config.GlobalInfo;
import agentgui.core.database.DBConnection;
import agentgui.core.gui.AboutDialog;
import agentgui.core.gui.ChangeDialog;
import agentgui.core.gui.MainWindow;
import agentgui.core.gui.Translation;
import agentgui.core.gui.options.OptionDialog;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.Platform;
import agentgui.core.project.Project;
import agentgui.core.project.ProjectsLoaded;
import agentgui.core.systemtray.AgentGUITrayIcon;
import agentgui.core.update.AgentGuiUpdater;
import agentgui.core.webserver.DownloadServer;
import agentgui.simulationService.load.LoadMeasureThread;

/**
 * This is the main class of the application containing the main-method for the program execution.<br> 
 * This class is designed as singleton class in order to make it accessible from every context of the program. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Application {
	
	/** True, if Agent.GUI was updated */
	private static boolean agentGuiWasUpdated = false;
	/** True, if a remote container has to be started (see start arguments) */
	private static boolean justStartJade = false;
	/** Indicates if the benchmark is currently running */
	private static boolean benchmarkRunning = false; 
	
	/**
	 * This ClassDetector is used in order to search for agent classe's, ontology's and BaseService'.
	 * If a project was newly opened, the search process will restart in order to determine the integrated
	 * classes of the project. 
	 */
	private static ClassSearcher classSearcher = null;
	
	
	/** The instance of this singleton class */
	private static Application thisApp = new Application();
	/** This attribute holds the current state of the configurable runtime informations */
	private static GlobalInfo globalInfo = null;
	/** This will hold the instance of the main application window */
	private static MainWindow mainWindow = null;
	/** Here the tray icon of the application can be accessed */
	private static AgentGUITrayIcon trayIcon = null;
	/** This is the instance of the main application window */
	private static JPanelConsole console = null;
	/** The About dialog of the main application window. */
	private static AboutDialog about = null;
	/** The About dialog of the application.*/
	private static OptionDialog options = null;
	/** With this attribute/class the agent platform (JADE) will be controlled. */
	private static Platform jadePlatform = null;
	/** The ODBC connection to the database */
	private static DBConnection dbConnection = null;
	/** Simple web-server that can be used for larger data transfer. */
	private static DownloadServer downloadServer = null;
	

	/** The project that has to be opened after application start. Received from program parameter.*/
	private static String project2OpenAfterStart = null;
	/** Holds the list of the open projects. */
	private static ProjectsLoaded projectsLoaded = null;
	/** Holds the reference of the currently focused project */
	private static Project projectFocused = null;
	
	
	
	/**
	 * Singleton-Constructor: Instantiates a new application.
	 */
	private Application() {
	}	
	/**
	 * Will return the instance of this singleton class
	 * @return The instance of this class
	 */
	public static Application getInstance() {
		return Application.thisApp;
	}
	
	/**
	 * If the application was executed as end user application, this will be false.
	 * In case of the execution as a 'server'-tool (without application window, but 
	 * with tray icon control) this method will return true.
	 * @return true, if AgentGUI is running as server tool
	 */
	public static boolean isRunningAsServer() {
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			return true;
		}
		return false;
	}
	
	/**
	 * Sets the class searcher.
	 * @param classSearcher the new class searcher
	 */
	public static void setClassSearcher(ClassSearcher classSearcher) {
		Application.classSearcher = classSearcher;
	}
	/**
	 * Returns the class searcher.
	 * @return the class searcher
	 */
	public static ClassSearcher getClassSearcher() {
		return classSearcher;
	}
	
	/**
	 * Gets the console.
	 * @return the console
	 */
	public static JPanelConsole getConsole() {
		if (Application.console==null) {
			Application.console = new JPanelConsole(true);
		}
		return Application.console;
	}
	/**
	 * Returns the application-wide information system.
	 * @return the global info
	 */
	public static GlobalInfo getGlobalInfo() {
		if (Application.globalInfo==null) {
			Application.globalInfo = new GlobalInfo();
			Application.globalInfo.initialize();
		}
		return Application.globalInfo;
	}
	/**
	 * Gets the jade platform.
	 * @return the jade platform
	 */
	public static Platform getJadePlatform() {
		if (Application.jadePlatform==null) {
			Application.jadePlatform = new Platform();	
		}
		return jadePlatform;
	}
	/**
	 * Gets the AgentGUI tray icon.
	 * @return the tray icon
	 */
	public static AgentGUITrayIcon getTrayIcon() {
		if (Application.trayIcon==null) {
			Application.trayIcon = new AgentGUITrayIcon();
		}
		return Application.trayIcon;
	}
	/**
	 * Sets the tray icon.
	 * @param newTrayIcon the new tray icon
	 */
	public static void setTrayIcon(AgentGUITrayIcon newTrayIcon) {
		Application.trayIcon = newTrayIcon;
	}
	
	/**
	 * Gets the main window.
	 * @return the main window
	 */
	public static MainWindow getMainWindow() {
		return Application.mainWindow;
	}
	/**
	 * Sets the main window.
	 * @param mainWindow the new main window
	 */
	public static void setMainWindow(MainWindow mainWindow) {
		Application.mainWindow = mainWindow;	
	}
	
	
	/**
	 * Returns the database connection.
	 * @param renewDatabaseConnection the renew database connection
	 * @return the database connection
	 */
	public static DBConnection getDatabaseConnection(boolean renewDatabaseConnection) {
		if (renewDatabaseConnection==true) {
			Application.dbConnection = new DBConnection();
			return Application.dbConnection;
		}
		return Application.getDatabaseConnection();
	}
	/**
	 * Returns the database connection for the Application.
	 * @return the database connection
	 */
	public static DBConnection getDatabaseConnection() {
		if (Application.dbConnection==null) {
			Application.dbConnection = new DBConnection();
		}
		return Application.dbConnection;
	}
	
	/**
	 * Main method for the start of the application running as end user application or server-tool
	 * 
	 * @param args The arguments which can be configured in the command line. 
	 * -help will print all possible command line arguments 
	 */
	public static void main(String[] args) {

		// ----------------------------------------------------------
		// --- Read the start arguments and react on it?! -----------
		String[] remainingArgs = proceedStartArguments(args);
		
		if (Application.justStartJade==false) {
			// ------------------------------------------------------
			// --- Start the Agent.GUI base-instances ---------------
			getConsole();
			getGlobalInfo();
			Language.startDictionary();
			
			new LoadMeasureThread().start();  
			startAgentGUI();
			
		} else {
			// ------------------------------------------------------
			// --- Just start JADE ----------------------------------
			getGlobalInfo();
			Language.startDictionary();

			System.out.println("Just starting JADE now ...");
			jade.Boot.main(remainingArgs);
			
		}
		
	}	

	/**
	 * This method will proceed the start-arguments of the application and
	 * returns these arguments, which are not understood.
	 * @param args the start arguments
	 * @return the remaining arguments, which are not proceeded by Agent.GUI
	 */
	private static String[] proceedStartArguments(String[] args){
		
		Vector<String> remainingArgsVector = new Vector<String>();

		int i = 0;
		while (i < args.length) {

			// ----------------------------------------------------------------
			// --- Pack the single argument to the remainingArgsVector --------  
			remainingArgsVector.addElement(args[i]);
			// ----------------------------------------------------------------
			
			if (args[i].startsWith("-")) {
				
				if (args[i].equalsIgnoreCase("-project")) {
					// --------------------------------------------------------
					// --- open a specified project --------------------------- 
					remainingArgsVector.removeElement(args[i]);
					if ((args.length-1)>i) {
						i++;
						project2OpenAfterStart = args[i].toLowerCase();	
					} else {
						System.err.println("Argument -project: Could not find project specification!");
					}
					
				} else if (args[i].equalsIgnoreCase("-updated")) {
					// --------------------------------------------------------
					// --- AgentGui.jar updated, remove AgentGuiUpdate.jar ----
					remainingArgsVector.removeElement(args[i]);
					agentGuiWasUpdated = true;
					
				} else if (args[i].equalsIgnoreCase("-jade")) {
					// --------------------------------------------------------
					// --- JADE has to be started as remote container ---------	
					remainingArgsVector.removeElement(args[i]);
					justStartJade = true;
				
				} else if (args[i].equalsIgnoreCase("-help")) {
					// --------------------------------------------------------
					// --- print out the help for the start arguments ---------	
					proceedStartArgumentPrintHelp();
					
				} else if (args[i].equalsIgnoreCase("-?")) {
					// --------------------------------------------------------
					// --- print out the help for the start arguments ---------	
					remainingArgsVector.removeElement(args[i]);
					remainingArgsVector.addElement("-help");
					proceedStartArgumentPrintHelp();
					
				} else if (args[i].equalsIgnoreCase("-container") ||
						   args[i].equalsIgnoreCase("-gui")) {
					// --------------------------------------------------------
					// --- arguments without further arguments ----------------
					// --- so, there is nothing to do here --------------------
					
				} else {
					// --------------------------------------------------------
					// --- just skip and remind the next start argument -------
					if ((args.length-1)>i) {
						i++;
						remainingArgsVector.addElement(args[i]);
					}
				}
				
			} else {
				// --- unspecified start option ------------------------------- 
				System.out.println("Argument" + " " + (i+1) + " '" + args[i] + "': unspecified start argument");
			}
			
			// --- proceed next start argument ------------
			i++;
		}
		
		// ----------------------------------------------------------------
		// --- Rebuild the Array of the start arguments -------------------  
		// ----------------------------------------------------------------
		String[] remainingArgs = new String[remainingArgsVector.size()];
		remainingArgsVector.toArray(remainingArgs);
		return remainingArgs;
	}
	
	/**
	 * Prints the start-arguments to the console
	 */
	private static void proceedStartArgumentPrintHelp() {
		
		System.out.println("Agent.GUI - usage of start arguments:");
		System.out.println("");
		System.out.println("1. '-project projectFolder': opens the project located in the Agent.GUI folder 'project' (e.g. 'myProject')");
		System.out.println("2. '-jade'                 : indicates that JADE has to be started. For the JADE start arguments, see JADE administrative guide." );
		System.out.println("3. '-help' or '-?'         : provides this information to the console" );
		System.out.println("");
		System.out.println("");
	}
	/**
	 * This method can be invoked in order to start a project.
	 * It was implemented to open a project given by the start arguments
	 * after the needed instantiations are done.
	 */
	private static void proceedStartArgumentOpenProject() {
		
		if (isRunningAsServer()==false && project2OpenAfterStart!=null) {
			
			// --- wait for the end of the benchmark ------
			while(benchmarkRunning==true) {
				Application.setStatusBar(Language.translate("Warte auf das Ende des Benchmarks ..."));
				try {
					Thread.sleep(250);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			
			// --- open the specified project -------------
			Application.setStatusBar(Language.translate("Öffne Projekt") + " '" + project2OpenAfterStart + "'...");
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					getProjectsLoaded().add(project2OpenAfterStart);
					project2OpenAfterStart=null;
				}
			});
			Application.setStatusBar(Language.translate("Fertig"));
		}
	}
	
	
	/**
	 * This methods starts Agent.GUI in application or server mode
	 * depending on the configuration in 'properties/agentgui.ini'
	 */
	public static void startAgentGUI() {
		
		// ----------------------------------------------------------		
		// --- Delete the updater file ------------------------------
		if (agentGuiWasUpdated==true) {
			String updaterPath = getGlobalInfo().getFileNameUpdater(true);
			File updater = new File(updaterPath);
			if (updater.exists()==true) {
				updater.delete();
			}
		}
		
		// ----------------------------------------------------------		
		// --- Start the Application as defined by 'isServer' -------
		getJadePlatform();
		getTrayIcon();
		
		if (isRunningAsServer()==true) {
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
			getProjectsLoaded();

			startApplication();
			getMainWindow().setStatusBar(Language.translate("Fertig"));
			doBenchmark(false);
			proceedStartArgumentOpenProject();
			if (agentGuiWasUpdated==true) {
				showChangeDialog();
			} else {
				new AgentGuiUpdater().start();
			}
			
		}
	}
	
	/**
	 * Opens the main application window (JFrame)
	 */
	public static void startApplication() {
		setMainWindow(new MainWindow());
		getProjectsLoaded().setProjectView();
	}
	
	/**
	 * Starts the main procedure for the Server-Version of Agent.GUI
	 */
	public static void startServer() {
		// --- Automatically Start JADE, if configured --------------
		if ( getGlobalInfo().isServerAutoRun()==true ) {
			// --- Wait until the benchmark result is available -----
			while (LoadMeasureThread.getCompositeBenchmarkValue()==0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			
			getJadePlatform().jadeStart();
			trayIcon.popUp.refreshView();
		}
	}
	
	/**
	 * Quits the application
	 */
	public static void quit() {

		// --- JADE beenden ------------------------
		getJadePlatform().jadeStop();
		// --- Noch offene Projekte schließen ------
		if (getProjectsLoaded().closeAll()==false) {
			return;	
		}
		// --- FileProperties speichern ------------
		getGlobalInfo().getFileProperties().save();
		
		// --- Fertig ------------------------------
		System.out.println(Language.translate("Programmende... ") );
		Language.saveDictionaryFile();
		System.exit(0);		
	}

	/**
	 * Opens the translation dialog of the application
	 */
	public static void showTranslationDialog() {
		Translation trans = new Translation(Application.getMainWindow());
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
		
		if (options!=null) return;
		
		// ==================================================================
		if (isRunningAsServer()==true) {
			options = new OptionDialog(null);
		} else {
			options = new OptionDialog(getMainWindow());
		}
		if (focusOnTab!=null) {
			options.setFocusOnTab(focusOnTab);
		}
		options.setVisible(true);
		// ==================================================================
		// === Hier geht's weiter, wenn der Dialog wieder geschlossen ist ===
		// ==================================================================
		options.dispose();
		options = null;
		
	}

	/**
	 * Will show the About-Dialog of the application
	 */
	public static void showAboutDialog() {
		
		if (about!=null) return;
		if (isRunningAsServer()==true) {
			about = new AboutDialog(null);
		} else {
			about = new AboutDialog(getMainWindow());
		}
		about.setVisible(true);
		// - - - Wait for user - - - - - - - - -  
		about.dispose();
		about = null;		
	}

	/**
	 * Will show the ChangeDialog that displays the latest changes.
	 */
	public static void showChangeDialog() {
		ChangeDialog cd = null;
		if (isRunningAsServer()==true) {
			cd = new ChangeDialog(null);	
		} else {
			cd = new ChangeDialog(getMainWindow());
		}
		cd.setVisible(true);
		// - - - Wait for user - - - - - - - - -
		cd.dispose();
	}
	
	/**
	 * Adds a supplement to the application title
	 * @param add2BasicTitel
	 */
	public static void setTitelAddition( String add2BasicTitel ) {
		getMainWindow().setTitelAddition(add2BasicTitel);
	}

	/**
	 * Sets the status which shows if JADE is running (green or red in the right corner of the status bar)   
	 * @param runs  
	 */
	public static void setStatusJadeRunning(boolean runs) {
		if (getMainWindow()!=null) {
			getMainWindow().setStatusJadeRunning(runs);	
		}
		if (trayIcon!=null) {
			trayIcon.popUp.refreshView();	
		}
	}
	
	/**
	 * Enables to set the text of the status bar
	 * @param statusText
	 */
	public static void setStatusBar(String statusText) {
		getMainWindow().setStatusBar(statusText);
	}
	
	/**
	 * Set's the Look and feel of the application
	 * @param newLnF
	 */
	public static void setLookAndFeel(String newLnF) {
		getMainWindow().setLookAndFeel(newLnF);
		getProjectsLoaded().setProjectView();
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

		String newLine = getGlobalInfo().getNewLineSeparator();
		
		if (askUser==true) {
			// --- Sind die neue und die alte Anzeigesprach gleich ? ----
			Integer newLangIndex = Language.getIndexOfLanguage(newLang);
			if ( newLangIndex == Language.currLanguageIndex ) return; 
			
			// --- User fragen, ob die Sprache umgestellt werden soll ---
			String MsgHead = Language.translate("Anzeigesprache wechseln?");
			String MsgText = Language.translate(
							 "Möchten Sie die Anzeigesprache wirklich umstellen?" + newLine + 
							 "Die Anwendung muss hierzu neu gestartet und Projekte" + newLine +
							 "von Ihnen neu geöffnet werden.");
			Integer MsgAnswer = JOptionPane.showInternalConfirmDialog( Application.getMainWindow().getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if (MsgAnswer==1) return;
			
		}
		
		// --- JADE stoppen -----------------------------------------		
		getJadePlatform().jadeStop();
		// --- Projekte schliessen ----------------------------------
		if (getProjectsLoaded()!=null) {
			if (getProjectsLoaded().closeAll()==false) return;	
		}
		// --- Sprache umstellen ------------------------------------
		Language.changeApplicationLanguageTo(newLang);
		// --- Anwendungsfenster schliessen -------------------------
		getMainWindow().dispose();
		mainWindow = null;
		// --- Anwendung neu öffnen ---------------------------------
		startApplication();	

	}	
	
	/**
	 * Executes the Benchmark-Test of SciMark 2.0 to determine the ability of this system to deal with number crunching.<br> 
	 * The result will be available in Mflops (Millions of floating point operations per second)
	 */
	public static void doBenchmark(boolean forceBenchmark) {
		if (Application.isBenchmarkRunning()==false) {
			// --- Set marker -----------------------------
			benchmarkRunning = true;
			// --- Execute the Benchmark-Thread -----------
			new BenchmarkMeasurement(forceBenchmark).start();
		}
	}
	/**
	 * Sets that the benchmark is running or not.
	 * @param running the new benchmark is running
	 */
	public static void setBenchmarkRunning(boolean running) {
		benchmarkRunning = running;
	}
	/**
	 * Checks if is benchmark is running.
	 * @return true, if is benchmark is running
	 */
	public static boolean isBenchmarkRunning() {
		return benchmarkRunning;
	}

	/**
	 * Starts the Web-Server, so that a remote server.slave is able 
	 * to download the additional jar-resources of a project
	 */
	public static DownloadServer startDownloadServer() {
		
		if (downloadServer==null) {
			downloadServer = new DownloadServer();
			downloadServer.setRoot(getGlobalInfo().PathWebServer(true));
			new Thread(downloadServer).start();
		}
		return downloadServer;
	}
	/**
	 * Stops the Web-Server for the resources download of an external server.slave
	 */
	public static void stopDownloadServer() {

		if (downloadServer!=null) {
			synchronized (downloadServer) {
				downloadServer.stop();
			}
			downloadServer = null;
		}
	}
	/**
	 * Returns the current SownloadServer instance.
	 * @return the download server
	 */
	public static DownloadServer getDownloadServer() {
		if (downloadServer==null) {
			return downloadServer;
		} else {
			synchronized (downloadServer) {
				return downloadServer;
			}	
		}
	}
	
	/**
	 * This method can be used in order to change the application title during runtime
	 * @param newApplicationTitle sets a new application title/name 
	 */
	public static void setApplicationTitle(String newApplicationTitle) {
		getGlobalInfo().setApplicationTitle(newApplicationTitle);
	}
	/**
	 * This method return the current title of the application
	 * @return the Application title/name
	 */
	public static String getApplicationTitle() {
		return getGlobalInfo().getApplicationTitle();
	}
	
	/**
	 * Returns the instance/list of loaded projects .
	 * @return the projects loaded
	 */
	public static ProjectsLoaded getProjectsLoaded() {
		if (projectsLoaded==null) {
			projectsLoaded = new ProjectsLoaded();
		}
		return projectsLoaded;
	}

	/**
	 * Sets the currently focused {@link Project}.
	 * @param projectFocused the new project focused
	 */
	public static void setProjectFocused(Project projectFocused) {
		Application.projectFocused = projectFocused;
	}
	/**
	 * Returns the currently focused {@link Project}.
	 * @return the project focused
	 */
	public static Project getProjectFocused() {
		return projectFocused;
	}

	
} // --- End Class ---

