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

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import agentgui.core.benchmark.BenchmarkMeasurement;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.config.auth.OIDCAuthorization;
import agentgui.core.config.auth.OIDCPanel;
import agentgui.core.config.auth.Trust;
import agentgui.core.database.DBConnection;
import agentgui.core.gui.AboutDialog;
import agentgui.core.gui.ChangeDialog;
import agentgui.core.gui.MainWindow;
import agentgui.core.gui.Translation;
import agentgui.core.gui.options.OptionDialog;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.jade.ClassSearcher.ClassSearcherProcess;
import agentgui.core.jade.Platform;
import agentgui.core.project.Project;
import agentgui.core.project.ProjectsLoaded;
import agentgui.core.sim.setup.SimulationSetupNotification.SimNoteReason;
import agentgui.core.systemtray.AgentGUITrayIcon;
import agentgui.core.update.AgentGuiUpdater;
import agentgui.core.webserver.DownloadServer;
import agentgui.logging.components.JPanelConsole;
import agentgui.logging.logfile.LogFileWriter;
import agentgui.simulationService.agents.LoadExecutionAgent;
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
	/** Indicates that the application is running headless */
	private static Boolean headlessOperation;
	
	/** The indicator to do system exit on quit. */
	private static boolean doSystemExitOnQuit = true;
	/** The quit application. */
	private static boolean quitJVM = false;
	
	/**
	 * This ClassDetector is used in order to search for agent classe's, ontology's and BaseService'.
	 * If a project was newly opened, the search process will restart in order to determine the integrated
	 * classes of the project. 
	 */
	private static ClassSearcher classSearcher;
	
	/** The instance of this singleton class */
	private static Application thisApp = new Application();
	/** This attribute holds the current state of the configurable runtime informations */
	private static GlobalInfo globalInfo;
	/** This will hold the instance of the main application window */
	private static MainWindow mainWindow;
	/** Here the tray icon of the application can be accessed */
	private static AgentGUITrayIcon trayIcon;
	/** This is the instance of the main application window */
	private static JPanelConsole console;
	/** In case that a log file has to be written */
	private static LogFileWriter logFileWriter;
	/** In case of headless operation */
	private static ShutdownThread shutdownThread;
	/** The About dialog of the main application window. */
	private static AboutDialog about;
	/** The About dialog of the application.*/
	private static OptionDialog options;
	/** With this attribute/class the agent platform (JADE) will be controlled. */
	private static Platform jadePlatform;
	/** The ODBC connection to the database */
	private static DBConnection dbConnection;
	/** Simple web-server that can be used for larger data transfer. */
	private static DownloadServer downloadServer;
	

	/** The project that has to be opened after application start. Received from program parameter.*/
	private static String project2OpenAfterStart;
	/** Holds the list of the open projects. */
	private static ProjectsLoaded projectsLoaded;
	/** Holds the reference of the currently focused project */
	private static Project projectFocused;
	
	
	
	/**
	 * Singleton-Constructor: Instantiates a new application.
	 */
	private Application() {
	}	
	/**
	 * Will return the instance of this singleton class
	 * @return The instance of this static class
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
		
		default:
			break;
		}
		return false;
	}
	
	/**
	 * Checks if Agent.GUI is or has to be executed headless (without any GUI).
	 * @return true, if is headless operation
	 */
	public static boolean isOperatingHeadless() {
		if (headlessOperation==null) {
			// --- Do headless check ----------------------
			JDialog jDialog;
			try {
				jDialog = new JDialog();
				jDialog.validate();
				jDialog.dispose();
				jDialog = null;
				headlessOperation = false;
				
			} catch (HeadlessException he) {
				System.out.println("Headless Execution Mode ...");
				headlessOperation = true;
			}
		}
		return headlessOperation;
	}
	/**
	 * Sets that Agent.GUI has to operated headless.
	 * @param headlessOperation the new headless operation
	 */
	public static void setOperatingHeadless(boolean headlessOperation) {
		if (headlessOperation==true) {
			System.out.println("Headless Execution Mode ...");
		}
		Application.headlessOperation = headlessOperation;
	}
	
	
	/**
	 * Creates and starts the {@link ShutdownThread} that used in headless mode.
	 * @return the ShutdownThread
	 */
	public static ShutdownThread startShutdownThread() {
		if (shutdownThread==null) {
			shutdownThread = new ShutdownThread();	
			shutdownThread.start();
		}
		return shutdownThread;
	}
	/**
	 * Returns the current {@link ShutdownThread} of the application.
	 * @return the ShutdownThread
	 */
	public static ShutdownThread getShutdownThread() {
		return shutdownThread;
	}
	/**
	 * Sets the ShutdownThread for the application. Set null in order  
	 * to stop the currently running {@link ShutdownThread}.
	 * @param newShutdownThread the new ShutdownThread
	 */
	public static void setShutdownThread(ShutdownThread newShutdownThread) {
		if (shutdownThread!=null && newShutdownThread!=shutdownThread) {
			shutdownThread.stopObserving();
		}
		shutdownThread = newShutdownThread;
	}
	
	
	/**
	 * Sets the current {@link ClassSearcher}.
	 * @param newClassSearcher the new class searcher
	 */
	public static void setClassSearcher(ClassSearcher newClassSearcher) {
		if (classSearcher!=null) {
			classSearcher.stopSearch(ClassSearcherProcess.ALL_SEARCH_PROCESSES);	
		}
		classSearcher = newClassSearcher;
	}
	/**
	 * Returns the current {@link ClassSearcher}.
	 * @return the class searcher
	 */
	public static ClassSearcher getClassSearcher() {
		if (classSearcher==null) {
			classSearcher = new ClassSearcher();
		}
		return classSearcher;
	}
	
	/**
	 * Gets the console.
	 * @return the console
	 */
	public static JPanelConsole getConsole() {
		if (Application.console==null && isOperatingHeadless()==false) {
			Application.console = new JPanelConsole(true);
		}
		return Application.console;
	}
	
	
	/**
	 * Creates the log file writer.
	 * @return the log file writer
	 */
	public static LogFileWriter startLogFileWriter() {
		if (logFileWriter==null) {
			logFileWriter = new LogFileWriter();	
		}
		return logFileWriter;
	}
	/**
	 * Returns the current LogFileWriter of the application.
	 * @return the LogFileWriter
	 */
	public static LogFileWriter getLogFileWriter() {
		return logFileWriter;
	}
	/**
	 * Sets the LogFileWriter for the application. Set null in order  
	 * to stop the currently running {@link LogFileWriter}.
	 * @param newLogFileWriter the new LogFileWriter
	 */
	public static void setLogFileWriter(LogFileWriter newLogFileWriter) {
		if (logFileWriter!=null && newLogFileWriter!=logFileWriter) {
			logFileWriter.stopFileWriter();
		}
		logFileWriter = newLogFileWriter;
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
		if (Application.trayIcon==null && isOperatingHeadless()==false) {
			Application.trayIcon = new AgentGUITrayIcon();	
		}
		return Application.trayIcon;
	}
	/**
	 * Sets the tray icon.
	 * @param newTrayIcon the new tray icon
	 */
	public static void setTrayIcon(AgentGUITrayIcon newTrayIcon) {
		if (trayIcon!=null && newTrayIcon==null) {
			trayIcon.remove();	
		}
		trayIcon = newTrayIcon;
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
	 * @param newMainWindow the new main window
	 */
	public static void setMainWindow(MainWindow newMainWindow) {
		if (mainWindow!=null && newMainWindow==null) {
			mainWindow.dispose();
		}
		mainWindow = newMainWindow;	
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

		// --- Read the start arguments and react on it?! -----------
		String[] remainingArgs = proceedStartArguments(args);
		
		// --- Start log file writer, if needed ---------------------
		if (isOperatingHeadless()==true) startLogFileWriter();
		
		// --- Case separation Agent.GUI / JADE execution -----------
		if (Application.justStartJade==false) {
			// ------------------------------------------------------
			// --- Start the Agent.GUI base-instances ---------------
			// ------------------------------------------------------
			getConsole();
			getGlobalInfo();
			Language.startDictionary();
			
			new LoadMeasureThread().start();  
			startAgentGUI();
			
		} else {
			// ------------------------------------------------------
			// --- Just start JADE ----------------------------------
			// ------------------------------------------------------
			getGlobalInfo();
			Language.startDictionary();

			System.out.println("Just starting JADE now ...");
			jade.Boot.main(remainingArgs);
		
		}
		
		// --- Start ShutdownExecuter, if needed --------------------
		if (isOperatingHeadless()==true) startShutdownThread();
		
	}	

	/**
	 * This method will proceed the start-arguments of the application and
	 * returns these arguments, which are not understood.
	 * @param args the start arguments
	 * @return the remaining arguments, which are not proceeded by Agent.GUI
	 */
	private static String[] proceedStartArguments(String[] args) {
		
		if (args==null) return new String[0];
		
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
					
				} else if (args[i].equalsIgnoreCase("-headless")) {
					// --------------------------------------------------------
					// --- Agent.GUI has to be operated headless --------------
					remainingArgsVector.removeElement(args[i]);
					setOperatingHeadless(true);

				} else if (args[i].equalsIgnoreCase("-log")) {
					// --------------------------------------------------------
					// --- Start log file writing -----------------------------
					remainingArgsVector.removeElement(args[i]);
					startLogFileWriter();

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
		System.out.println("2. '-headless'             : will set Agent.GUI to operate headless (without any GUI)");
		System.out.println("3. '-jade'                 : indicates that JADE has to be started. For the JADE start arguments, see JADE administrative guide.");
		System.out.println("4. '-log'                  : starts the Agent.GUI log file writer.");
		System.out.println("5. '-help' or '-?'         : provides this information to the console");
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
		// --- Check if Agent.GUI is operated headless --------------
		// ----------------------------------------------------------
		if (isOperatingHeadless()==true) {
			// --- Check start settings for headless operation ------
			if (getGlobalInfo().getExecutionMode()==ExecutionMode.APPLICATION) {
				String configFile = getGlobalInfo().getPathConfigFile(false);
				String msg = "Agent.GUI-Execution Mode was set to 'Application', but this mode can't be executed headless.\n";
				msg+= "Please, check the argument '01_RUNAS' in file '" + configFile + "' and set this argument\n";
				msg+= "either to 'Server' or 'EmbeddedSystemAgent'.";
				System.err.println(msg);
				Application.quit();
			}
		}
		
		// ----------------------------------------------------------		
		// --- Start Agent.GUI as defined by 'ExecutionMode' --------
		// ----------------------------------------------------------
		System.out.println(Language.translate("Programmstart") + " [" + getGlobalInfo().getExecutionModeDescription() + "] ..." );
		
		switch (getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			// ------------------------------------------------------
			// --- Start Application --------------------------------
			getTrayIcon();
			getProjectsLoaded();

			startApplication();
			getMainWindow().setStatusBar(Language.translate("Fertig"));
			doBenchmark(false);
			waitForBenchmark();
			
			proceedStartArgumentOpenProject();
			
			if (agentGuiWasUpdated==true) {
				showChangeDialog();
				agentGuiWasUpdated=false;
			} else {
				new AgentGuiUpdater().start();
			}
			break;
			
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			// ------------------------------------------------------
			// --- Start Server-Version of AgentGUI -----------------
			// --- In the Server-Case, start the benchmark now ! ----
			getTrayIcon();
			doBenchmark(false);
			startServer();
			break;

		case DEVICE_SYSTEM:
			// ------------------------------------------------------
			// --- Start Service / Embedded System Agent ------------
			startServiceOrEmbeddedSystemAgent();
			
			if (agentGuiWasUpdated==true) {
				showChangeDialog();
				agentGuiWasUpdated=false;
			} else {
				new AgentGuiUpdater().start();
			}
			break;
		}
		
	}
	
	/**
	 * Opens the main application window (JFrame)
	 */
	public static void startApplication() {
		if (isOperatingHeadless()==false) {
			setMainWindow(new MainWindow());
			getProjectsLoaded().setProjectView();
		}
	}
	
	/**
	 * Starts the main procedure for the Server-Version of Agent.GUI
	 */
	public static void startServer() {
		// --- Automatically Start JADE, if configured --------------
		if (getGlobalInfo().isServerAutoRun()==true) {
			// --- Wait until the benchmark result is available -----
			waitForBenchmark();
			boolean jadeStarted = getJadePlatform().jadeStart();
			if (isOperatingHeadless()==true && jadeStarted==false) {
				Application.quit();
			}
			AgentGUITrayIcon trayIcon =  getTrayIcon();
			if (trayIcon!=null) {
				trayIcon.getAgentGUITrayPopUp().refreshView();
			}
		}
	}
	
	/**
	 * Start Agent.GUI as device system or embedded system agent.
	 */
	public static void startServiceOrEmbeddedSystemAgent() {
		
		try {
			
			final String projectFolder = getGlobalInfo().getDeviceServiceProjectFolder();
			DeviceSystemExecutionMode execMode = getGlobalInfo().getDeviceServiceExecutionMode();
			final String simulationSetup = getGlobalInfo().getDeviceServiceSetupSelected();
			
			EmbeddedSystemAgentVisualisation embSysAgentVis = getGlobalInfo().getDeviceServiceAgentVisualisation();
			
			// ---- Case separation DeviceSystemExecutionMode ---------------------------
			switch (execMode) {
			case SETUP:
				// ----------------------------------------------------------------------
				// --- Execute a setup --------------------------------------------------
				// ----------------------------------------------------------------------
				getTrayIcon();
				getProjectsLoaded();

				startApplication();
				if (getMainWindow()!=null) {
					getMainWindow().setStatusBar(Language.translate("Fertig"));
				}
				doBenchmark(false);
				waitForBenchmark();
			
				// --- Execute the simulation setup -------------------------------------
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						// --- Open the specified project -------------------------------
						Project projectOpened = getProjectsLoaded().add(projectFolder);
						if (projectOpened!=null) {
							// --- Select the specified simulation setup ----------------
							boolean setupLoaded = projectOpened.getSimulationSetups().setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, simulationSetup, false);
							if (setupLoaded==true) {
								if (getJadePlatform().jadeStart(false)==true) {
									// --- Start Setup ----------------------------------
									Object[] startWith = new Object[1];
									startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
									getJadePlatform().jadeSystemAgentOpen("simstarter", null, startWith);
								}
							}
						}
					} // end run method
				});// end runnable
				break;

			case AGENT:
				// ----------------------------------------------------------------------
				// --- Just execute an agent with limited visualisation -----------------
				// ----------------------------------------------------------------------
				switch (embSysAgentVis) {
				case TRAY_ICON:
					getTrayIcon();
					break;
					
				case NONE:
					// --- Start writing a LogFile, if not already executed -------------
					if (getLogFileWriter()==null) {
						startLogFileWriter();
						// --- Create some initial output for the log file --------------
						getGlobalInfo().getVersionInfo().printVersionInfo();
						System.out.println(Language.translate("Programmstart") + " [" + getGlobalInfo().getExecutionModeDescription() + "] ..." );
					}
					// --- Start observing shutdown file --------------------------------
					startShutdownThread();
					break;
				}
				
				// --- Load project -----------------------------------------------------
				Project projectOpened = getProjectsLoaded().add(projectFolder);
				if (projectOpened!=null) {
					// --- Switch to the specified setup --------------------------------
					if (simulationSetup!=null && simulationSetup.equals("")==false) {
						projectOpened.getSimulationSetups().setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, simulationSetup, false);
					}
					// --- Start JADE for an embedded system agent ----------------------
					getJadePlatform().jadeStart4EmbeddedSystemAgent();
				}
				break;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Sets to do <code>System.exit</code> when calling the {@link #quit()} method.
	 * @param doSystemExitOnQuit the new do system exit on quit
	 */
	public static void setDoSystemExitOnQuit(boolean doSystemExitOnQuit) {
		Application.doSystemExitOnQuit = doSystemExitOnQuit;
	}
	/**
	 * Checks if is do system exit on quit.
	 * @return true, if is do system exit on quit
	 */
	private static boolean isDoSystemExitOnQuit() {
		return doSystemExitOnQuit;
	}
	
	/**
	 * Checks if is quit JVM.
	 * @return true, if is quit JVM
	 */
	public static boolean isQuitJVM() {
		return quitJVM;
	}
	/**
	 * Sets to quit the JVM.
	 * @param quitJVM the new quit JVM
	 */
	private static void setQuitJVM(boolean quitJVM) {
		Application.quitJVM = quitJVM;
	}

	/**
	 * Quits Agent.GUI (Application | Server | Service & Embedded System Agent)
	 */
	public static void quit() {

		// --- Shutdown JADE --------------------
		getJadePlatform().jadeStop();

		// --- Close open projects --------------
		if (getProjectsLoaded().closeAll()==false) return;	
		
		// --- Save file properties -------------
		getGlobalInfo().getFileProperties().save();
		
		// --- Done -----------------------------
		System.out.println(Language.translate("Programmende... ") );
		Language.saveDictionaryFile();
		
		// --- LogFileWriter --------------------
		setLogFileWriter(null);
		
		// --- ShutdownExecuter -----------------
		setShutdownThread(null);
		
		// --- Remove TrayIcon ------------------
		setTrayIcon(null);	
		
		if (isDoSystemExitOnQuit()==true) {
			// --- Shutdown JVM -----------------
			System.exit(0);		
		} else {
			// --- Indicate to stop the JVM -----
			setQuitJVM(true);
		}
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
	 * Opens the OpenID Connect dialog 
	 */
	public static void showAuthenticationDialog() {
		try {
			OIDCAuthorization.getInstance().setTrustStore(new File(Application.getGlobalInfo().getPathProperty(true) + Trust.OIDC_TRUST_STORE));
			OIDCAuthorization.getInstance().accessResource(OIDCPanel.DEBUG_RESOURCE_URI,getGlobalInfo().getOIDCUsername(), mainWindow);
		} catch (URISyntaxException | KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {
//			e.printStackTrace();
			System.err.println("Authentication failed: "+e.getClass()+": "+e.getMessage());
		}
	}
	
	/**
	 * Opens the Option-Dialog of the application with a specified TabName
	 * @param focusOnTab Can be used to set the focus directly to a Tab specified by its name 
	 */
	public static void showOptionDialog(String focusOnTab) {
		
		if (options!=null) {
			if (options.isVisible()==true) {
				// --- Set focus again ----------
				options.requestFocus();
				return;
			} else {
				// --- dispose it first --------- 
				options.dispose();
				options = null;
			}
		}
		
		if (isRunningAsServer()==true) {
			options = new OptionDialog(null);
		} else {
			options = new OptionDialog(getMainWindow());
		}
		if (focusOnTab!=null) {
			options.setFocusOnTab(focusOnTab);
		}
		options.setVisible(true);
		// - - - - - - - - - - - - - - - - - - - -
		if (options!=null) {
			options.dispose();
		}
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
		if (getMainWindow()!=null) {
			getMainWindow().setTitelAddition(add2BasicTitel);
		}
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
			trayIcon.getAgentGUITrayPopUp().refreshView();	
		}
	}
	
	/**
	 * Enables to set the text of the status bar
	 * @param statusText
	 */
	public static void setStatusBar(String statusText) {
		if (getMainWindow()!=null) {
			getMainWindow().setStatusBar(statusText);	
		}
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
			// --- Do we have identical Languages -------------------
			Integer newLangIndex = Language.getIndexOfLanguage(newLang);
			if ( newLangIndex == Language.currLanguageIndex ) return; 
			
			// --- Ask user -----------------------------------------
			String MsgHead = Language.translate("Anzeigesprache wechseln?");
			String MsgText = Language.translate(
							 "Möchten Sie die Anzeigesprache wirklich umstellen?" + newLine + 
							 "Die Anwendung muss hierzu neu gestartet und Projekte" + newLine +
							 "von Ihnen neu geöffnet werden.");
			Integer MsgAnswer = JOptionPane.showInternalConfirmDialog( Application.getMainWindow().getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if (MsgAnswer==1) return;
			
		}
		
		// --- JADE shutdown ----------------------------------------		
		getJadePlatform().jadeStop();
		// --- Close projects ---------------------------------------
		if (getProjectsLoaded()!=null) {
			if (getProjectsLoaded().closeAll()==false) return;	
		}
		// --- Switch Language --------------------------------------
		Language.changeApplicationLanguageTo(newLang);
		// --- Close MainWindow -------------------------------------
		getMainWindow().dispose();
		mainWindow = null;
		// --- Restart application ----------------------------------
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
	 * Waits for until the end of the benchmark.
	 */
	private static void waitForBenchmark() {
		while(benchmarkRunning==true) {
			Application.setStatusBar(Language.translate("Warte auf das Ende des Benchmarks ..."));
			try {
				Thread.sleep(250);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		Application.setStatusBar(Language.translate("Fertig"));
	}
	
	/**
	 * Starts the Web-Server, so that a remote server.slave is able 
	 * to download the additional jar-resources of a project
	 */
	public static DownloadServer startDownloadServer() {
		if (downloadServer==null) {
			downloadServer = new DownloadServer();
			downloadServer.setRoot(getGlobalInfo().getPathWebServer());
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

