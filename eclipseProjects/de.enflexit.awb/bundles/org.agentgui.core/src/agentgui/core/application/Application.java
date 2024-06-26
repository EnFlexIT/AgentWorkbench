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

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.agentgui.PlugInApplication;
import org.agentgui.bundle.evaluation.BundleClassFilterCollector;
import org.agentgui.gui.AwbConsole;
import org.agentgui.gui.AwbTrayIcon;
import org.agentgui.gui.UiBridge;
import org.eclipse.equinox.app.IApplication;

import agentgui.core.application.ApplicationListener.ApplicationEvent;
import agentgui.core.charts.timeseriesChart.TimeSeriesVisualisation;
import agentgui.core.charts.xyChart.XyChartVisualisation;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.gui.AboutDialog;
import agentgui.core.gui.MainWindow;
import agentgui.core.gui.MainWindowStatusBar.JadeStatusColor;
import agentgui.core.gui.Translation;
import agentgui.core.gui.options.OptionDialog;
import agentgui.core.jade.Platform;
import agentgui.core.jade.Platform.SystemAgent;
import agentgui.core.network.NetworkAddresses;
import agentgui.core.network.NetworkAddresses.NetworkAddress;
import agentgui.core.project.Project;
import agentgui.core.project.ProjectsLoaded;
import agentgui.core.project.setup.SimulationSetupNotification.SimNoteReason;
import agentgui.core.update.AWBUpdater;
import agentgui.logging.logfile.LogFileWriter;
import agentgui.logging.logfile.LogbackConfigurationReader;
import agentgui.simulationService.agents.LoadExecutionAgent;
import agentgui.simulationService.load.LoadMeasureThread;
import de.enflexit.common.SystemEnvironmentHelper;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;
import de.enflexit.common.featureEvaluation.FeatureEvaluator;
import de.enflexit.common.ontology.OntologyVisualisationConfiguration;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.gui.AwbDatabaseDialog;
import de.enflexit.oidc.OIDCAuthorization;
import de.enflexit.oidc.OIDCAuthorization.URLProcessor;
import de.enflexit.oidc.OIDCPanel;
import de.enflexit.oidc.OIDCResourceAvailabilityHandler;
import de.enflexit.oidc.Trust;


/**
 * This is the main class of the application containing the main-method for the program execution.<br> 
 * This class is designed as singleton class in order to make it accessible from every context of the program. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Application {
	
	/** True, if a remote container has to be started (see start arguments) */
	private static boolean justStartJade = false;
	/** Indicates if the benchmark is currently running */
	private static boolean benchmarkRunning = false; 
	/** Indicates that the application is running headless */
	private static Boolean headlessOperation;
	
	/** The eclipse IApplication */
	private static PlugInApplication plugInApplication;
	
	/** The quit application. */
	private static boolean quitJVM = false;
	
	/** This attribute holds the current state of the configurable runtime informations */
	private static GlobalInfo globalInfo;
	private static Object globalInfoSynchronizerForInitialization = new Object();
	/** Here the tray icon of the application can be accessed */
	private static AwbTrayIcon trayIcon;
	/** This is the instance of the main application window */
	private static AwbConsole console;
	/** In case that a log file has to be written */
	private static LogFileWriter logFileWriter;
	/** In case of headless operation */
	private static ShutdownThread shutdownThread;
	/** The About dialog of the main application window. */
	private static AboutDialog aboutDialog;
	/** The Database dialog for the main application window */
	private static AwbDatabaseDialog awbDatabaseDialog;
	/** The About dialog of the application.*/
	private static OptionDialog optionDialog;
	/** With this attribute/class the agent platform (JADE) will be controlled. */
	private static Platform jadePlatform;
		

	/** The project that has to be opened after application start. Received from program parameter.*/
	private static String project2OpenAfterStart;
	/** Holds the list of the open projects. */
	private static ProjectsLoaded projectsLoaded;
	/** Holds the reference of the currently focused project */
	private static Project projectFocused;
	
	/** The list of listener that waiting for ApplicationEvents  */
	private static List<ApplicationListener> appListenerList;
	
	
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
	 * Checks if Agent.Workbench is or has to be executed headless (without any GUI).
	 * @return true, if is headless operation
	 */
	public static boolean isOperatingHeadless() {
		if (headlessOperation==null) {
			headlessOperation = SystemEnvironmentHelper.isHeadlessOperation();
		}
		return headlessOperation;
	}
	/**
	 * Sets that Agent.Workbench has to operated headless.
	 * @param headlessOperation the new headless operation
	 */
	public static void setOperatingHeadless(boolean headlessOperation) {
		if (headlessOperation==true) {
			System.out.println("Headless Execution Mode ...");
		}
		Application.headlessOperation = headlessOperation;
	}
	
	/**
	 * Checks if the application is connected to a network.
	 * @return true, if so. 
	 */
	public static boolean isNetworkConnected() {
		NetworkAddresses na = new NetworkAddresses();
		Vector<NetworkAddress> addressVector =  na.getNetworkAddressVector();
		if (addressVector.size()==0) {
			return false;
		}
		return true;
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
		if (shutdownThread!=null) {
			shutdownThread.stopObserving();
		}
		shutdownThread = newShutdownThread;
	}
	
	/**
	 * Returns the current console panel/element.
	 * @return the console
	 */
	public static AwbConsole getConsole() {
		if (console==null && isOperatingHeadless()==false) {
			console = UiBridge.getInstance().getConsole(true);
		}
		return Application.console;
	}
	
	/**
	 * Creates the log file writer.
	 * @return the log file writer
	 */
	public static LogFileWriter startLogFileWriter() {
		if (Application.getGlobalInfo().isLoggingEnabled() && logFileWriter==null) {
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
		if (newLogFileWriter==null && logFileWriter!=null) {
			logFileWriter.stopFileWriter();
		}
		if (newLogFileWriter!=null && newLogFileWriter!=logFileWriter) {
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
			// --- Avoid double initialization ----------------------
			synchronized (globalInfoSynchronizerForInitialization) {
				if (Application.globalInfo==null) {
					Application.globalInfo = new GlobalInfo();
					Application.globalInfo.initialize();
					if (Application.globalInfo.isLoggingEnabled()==true) {
						startLogFileWriter();
					}
				}
			}
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
	public static AwbTrayIcon getTrayIcon() {
		if (trayIcon==null && isOperatingHeadless()==false) {
			trayIcon = UiBridge.getInstance().getTrayIcon();
		}
		return trayIcon;
	}
	/**
	 * Removes the tray icon. 
	 */
	public static void removeTrayIcon() {
		if (trayIcon!=null) {
			trayIcon.remove();
			trayIcon=null;
		}
	}
	
	/**
	 * Gets the main window.
	 * @return the main window
	 */
	public static MainWindow getMainWindow() {
		return getIApplication()==null ? null : getIApplication().getSwingMainWindow();
	}
	/**
	 * Starts the main application window (JFrame).
	 * @param postWindowOpenRunnable the post window open runnable
	 */
	public static void startMainWindow(Runnable postWindowOpenRunnable) {
		getIApplication().startEndUserApplication(postWindowOpenRunnable);
	}
	/**
	 * Sets the main window.
	 * @param newMainWindow the new main window
	 */
	public static void setMainWindow(MainWindow newMainWindow) {
		if (getIApplication().getSwingMainWindow()!=null && newMainWindow==null) {
			getIApplication().getSwingMainWindow().dispose();
		}
		getIApplication().setSwingMainWindow(newMainWindow);	
	}
	
		
	/**
	 * Returns the lApplication.
	 * @return the l application
	 */
	public static PlugInApplication getIApplication() {
		return plugInApplication;
	}
	
	/**
	 * Main method for the start of the application running as end user application or server-tool.
	 * @param piApp the current {@link PlugInApplication} instance 
	 */
	public static void start(PlugInApplication piApp) {

		// --- Remind the IApplication of the eclipse framework -----
		plugInApplication = piApp;
		
		// --- Read the start arguments and react on it?! -----------
		String[] remainingArgs = proceedStartArguments(org.eclipse.core.runtime.Platform.getApplicationArgs());
		
		// --- Configure slf4j --------------------------------------
		LogbackConfigurationReader.readConfiguration();
		
		// --- Start log file writer, if needed ---------------------
		if (isOperatingHeadless()==true) startLogFileWriter();
		
		// --- Start the HibernateUtilities -------------------------
		HibernateUtilities.start();
		
		
		// --- Case separation Agent.Workbench / JADE execution -----
		if (Application.justStartJade==false) {
			// ------------------------------------------------------
			// --- Start required Agent.Workbench instances ---------
			// ------------------------------------------------------
			getConsole();
			getGlobalInfo();
			startBundleEvaluation();
			
			new LoadMeasureThread().start();  
			startAgentWorkbench();
			
		} else {
			// ------------------------------------------------------
			// --- Just start JADE ----------------------------------
			// ------------------------------------------------------
			getGlobalInfo();

			// --- Load project resources ? -------------------------
			if (project2OpenAfterStart!=null) {
				try {
					System.out.println("Load project '" + project2OpenAfterStart + "' ...");
					Application.getProjectsLoaded().add(project2OpenAfterStart);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			System.out.println("Starting JADE ...");
			getIApplication().startJadeStandalone(remainingArgs);
		
		}
		
		// --- Start ShutdownExecuter, if needed --------------------
		if (isOperatingHeadless()==true) startShutdownThread();
		
	}	

	/**
	 * This method will proceed the start-arguments of the application and
	 * returns these arguments, which are not understood.
	 * @param args the start arguments
	 * @return the remaining arguments, which are not proceeded by Agent.Workbench
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
					
				} else if (args[i].equalsIgnoreCase("-jade")) {
					// --------------------------------------------------------
					// --- JADE has to be started as remote container ---------	
					remainingArgsVector.removeElement(args[i]);
					justStartJade = true;
					
				} else if (args[i].equalsIgnoreCase("-headless")) {
					// --------------------------------------------------------
					// --- Agent.Workbench has to be operated headless --------
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
				// --- Exclude known cases, e.g. for further JADE arguments ---  
				if (args[i].startsWith("rma.remote")==true) {
					// System.out.println(args[i] + "excluded!");
				} else {
					// --- Print unspecified start option ---------------------
					System.out.println("Argument" + " " + (i+1) + " '" + args[i] + "': unknown start argument");
				}
				
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
		
		System.out.println("Agent.Workbench - usage of start arguments:");
		System.out.println("");
		System.out.println("1. '-project projectFolder': opens the project located in the configured Agent.Workbench directory for projects (e.g. 'myProject')");
		System.out.println("2. '-headless'             : will set Agent.Workbench to operate headless (without any visualization)");
		System.out.println("3. '-jade'                 : indicates that JADE has to be started. For the JADE start arguments, see JADE administrative guide.");
		System.out.println("4. '-log'                  : starts the Agent.Workbench log file writer.");
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

			// --- Open the project -----------------------
			Application.setStatusBarMessage(Language.translate("Öffne Projekt") + " '" + project2OpenAfterStart + "'...");

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					getProjectsLoaded().add(project2OpenAfterStart);
					Application.setStatusBarMessageReady();
				}
			});
		}
	}
	
	
	/**
	 * This methods starts Agent.Workbench into application or server mode, which
	 * depends on the configuration in 'properties/agentgui.ini'.<br>
	 * Inverse method to {@link #stopAgentWorkbench()}
	 */
	public static void startAgentWorkbench() {
		
		// ----------------------------------------------------------
		// --- Check if Agent.Workbench is operated headless --------
		// ----------------------------------------------------------
		if (isOperatingHeadless()==true) {
			// --- Check start settings for headless operation ------
			if (getGlobalInfo().getExecutionMode()==ExecutionMode.APPLICATION) {
				String configFile = getGlobalInfo().getPathConfigFile(false);
				String msg = "Agent.Workbench-Execution Mode was set to 'Application', but this mode can't be executed headless.\n";
				msg+= "Please, check the argument '01_RUNAS' in file '" + configFile + "' and set this argument\n";
				msg+= "either to 'Server' or 'EmbeddedSystemAgent'.";
				System.err.println(msg);
				Application.stop();
				return;
			}
		}
		
		// ----------------------------------------------------------		
		// --- Start Agent.Workbench as defined by 'ExecutionMode' --
		// ----------------------------------------------------------
		System.out.println(Language.translate("Programmstart") + " [" + getGlobalInfo().getExecutionModeDescription() + "] ..." );
		// --- Fire ApplicationEvent AWB_START ----------------------
		Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.AWB_START));
		
		switch (getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			
			// ------------------------------------------------------
			// --- Start Application --------------------------------
			getTrayIcon(); 
			getProjectsLoaded();

			startMainWindow(new Runnable() {
				@Override
				public void run() {
					
					setStatusBarMessageReady();
					
					setOntologyVisualisationConfigurationToCommonBundle();
					
					// --- Do the benchmark -------------------------
					doBenchmark(false);
					waitForBenchmark();

					// --- Check for updates ------------------------
					AWBUpdater updater = new AWBUpdater();
					updater.start();
					updater.waitForUpdate();
					
					// --- Open project? ----------------------------
					proceedStartArgumentOpenProject();
					
				}
			});
			break;
			
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			// ------------------------------------------------------
			// --- Start Server-Version of AgentGUI -----------------
			// --- In the Server-Case, start the benchmark now ! ----
			getIApplication().setApplicationIsRunning();
			getTrayIcon();
			doBenchmark(false);
			startServer();
			break;

		case DEVICE_SYSTEM:

			// --- Check for updates --------------------------------
			AWBUpdater updater = new AWBUpdater();
			updater.start();
			updater.waitForUpdate();

			// --- Set application as executed ----------------------
			getIApplication().setApplicationIsRunning();
			
			// --- Start Service / Embedded System Agent ------------
			System.out.println("Starting the service/agent");
			startServiceOrEmbeddedSystemAgent();
			break;
		}
		
	}
	
	
	/**
	 * Starts the main procedure for the Server-Version of Agent.Workbench
	 */
	public static void startServer() {
		// --- Automatically Start JADE, if configured --------------
		if (getGlobalInfo().isServerAutoRun()==true) {
			// --- Wait until the benchmark result is available -----
			waitForBenchmark();
			boolean jadeStarted = getJadePlatform().start();
			if (isOperatingHeadless()==true && jadeStarted==false) {
				Application.stop();
			}
			AwbTrayIcon trayIcon = getTrayIcon();
			if (trayIcon!=null) {
				trayIcon.refreshView();
			}
		}
	}
	
	/**
	 * Start Agent.Workbench as device system or embedded system agent.
	 */
	public static void startServiceOrEmbeddedSystemAgent() {
		
		try {
			
			DeviceSystemExecutionMode execMode = getGlobalInfo().getDeviceServiceExecutionMode();
			EmbeddedSystemAgentVisualisation embSysAgentVis = getGlobalInfo().getDeviceServiceAgentVisualisation();

			final String projectFolder = getGlobalInfo().getDeviceServiceProjectFolder();
			final String simulationSetup = getGlobalInfo().getDeviceServiceSetupSelected();
			
			// ---- Case separation DeviceSystemExecutionMode ---------------------------
			switch (execMode) {
			case SETUP:
				// ----------------------------------------------------------------------
				// --- Execute a setup --------------------------------------------------
				// ----------------------------------------------------------------------
				getTrayIcon();
				getProjectsLoaded();
				
				startMainWindow(new Runnable() {
					@Override
					public void run() {
						
						if (getMainWindow()!=null) {
							setStatusBarMessageReady();
						}
						doBenchmark(false);
						waitForBenchmark();
					
						// --- Open the specified project -------------------------------
						Application.setStatusBarMessage(Language.translate("Öffne Projekt") + " '" + projectFolder + "'...");
						final Project projectOpened = getProjectsLoaded().add(projectFolder);
						if (projectOpened!=null) {
							// --- Execute the simulation setup -------------------------
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									// --- Select the specified simulation setup --------
									boolean setupLoaded = projectOpened.getSimulationSetups().setupLoadAndFocus(SimNoteReason.SIMULATION_SETUP_LOAD, simulationSetup, false);
									if (setupLoaded==true) {
										if (getJadePlatform().start(false)==true) {
											// --- Start Setup --------------------------
											Object[] startWith = new Object[1];
											startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
											getJadePlatform().startSystemAgent(SystemAgent.SimStarter, null, startWith);
										}
									}
								} // end run method
							});// end Runnable 2
						}// end project opened
					}// end run 
				});// end Runnable 1
				break;

			case AGENT:
				// ----------------------------------------------------------------------
				// --- Just execute an agent with limited visualization -----------------
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
					getJadePlatform().start4EmbeddedSystemAgent();
				}
				break;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This methods stops Agent.Workbench, running in application or server mode, which 
	 * depends on the configuration in 'properties/agentgui.ini'.<br>
	 * Inverse method to {@link #startAgentWorkbench()}
	 *
	 * @return true, if everything was stopped successfully
	 */
	public static boolean stopAgentWorkbench() {
		
		// --- Shutdown JADE --------------------
		getJadePlatform().stop(false);

		// --- Close open projects --------------
		if (getGlobalInfo().getExecutionMode()==ExecutionMode.APPLICATION) {
			if (getProjectsLoaded().closeAll()==false) return false;	
		} else {
			getProjectsLoaded().closeAll(null, true);
		}

		// --- Close visualization --------------
		setMainWindow(null);
		
		// --- Fire ApplicationEvent AWB_Stop ---
		Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.AWB_STOP));
		
		// --- Save file properties -------------
		getGlobalInfo().doSaveConfiguration();
		
		// --- Save dictionary ------------------
		Language.saveDictionaryFile();
		
		// --- Remove TrayIcon ------------------
		removeTrayIcon();	
		
		
		
		return true;
	}
	/**
	 * Stops Agent.Workbench in each execution mode ('Application', 'Server' (Master or Slave), 'Service' or 'Embedded System Agent')
	 */
	public static void stop() {
		getIApplication().stop();
	}
	/**
	 * Restarts Agent.Workbench as configured in each execution mode ('Application', 'Server' (Master or Slave), 'Service' or 'Embedded System Agent')
	 */
	public static void restart() {
		getIApplication().stop(IApplication.EXIT_RESTART);
	}
	
	/**
	 * Relaunches the application (as 'Application', as 'Server' (Master or Slave), as 'Service' or as 'Embedded System Agent').
	 * @param additionalArguments the additional arguments for the relaunch
	 */
	public static void relaunch(String additionalArguments) {
		
		String exitData = buildExitDataString(additionalArguments);

		// --- Relaunch the application with the additional arguments ---- 
		System.setProperty("eclipse.exitdata", exitData);
		getIApplication().stop(IApplication.EXIT_RELAUNCH);
	}
	
	/**
	 * Builds the eclipse.exitdata property for the application relaunch
	 * @param additionalArguments additional arguments for the relaunch
	 * @return
	 */
	private static String buildExitDataString(String additionalArguments) {

		String NEW_LINE = "\n";
		String CMD_VMARGS = "-vmargs";
		String PROP_VM = "eclipse.vm";
		String PROP_VMARGS = "eclipse.vmargs";
		String PROP_COMMANDS = "eclipse.commands";

		// ---  Extract the single arguments from the string
		String[] addArgs = null;
		if (additionalArguments!=null) {
			addArgs = additionalArguments.split(" ");
		}
		
		StringBuilder result = new StringBuilder(512);

		// --- Append the eclipse.vm property -----------
		String property = System.getProperty(PROP_VM);
		result.append(property);
		result.append(NEW_LINE);

		// --- Append the eclipse.vmargs property --------
		String vmargs = System.getProperty(PROP_VMARGS);
		if (vmargs!=null) {
			result.append(vmargs);
			if (vmargs.endsWith(NEW_LINE)==false) {
				result.append(NEW_LINE);
			}
		}

		// --- Append the regular arguments ----------------
		String propCommands = System.getProperty(PROP_COMMANDS);
		
		// --- If no arguments were specified before, just append the additional arguments
		if (propCommands==null) {
			for(String arg : addArgs) {
				result.append(arg);
				result.append(NEW_LINE);
			}
			
		} else {

			// --- Append the arguments from the last start --------
			result.append(propCommands);
			result.append(NEW_LINE);
			
			// --- Append the additional arguments if not already specified ---
			int i=0;
			while (i<addArgs.length) {
				
				// --- Determine the next argument and, if existing, its value -----
				String key = null;
				String val = null;
				key = addArgs[i];
				i++;
				if (i<addArgs.length && addArgs[i].startsWith("-")==false) {
					val = addArgs[i];
					i++;
				}
				
				// --- Check if the argument is already specified ----------
				if (propCommands.indexOf(key)==-1) {

					//TODO implement replacement of existing argument values
					
					// --- If not, append it -------------------------------
					result.append(key);
					result.append(NEW_LINE);
					if(val != null) {
						result.append(val);
						result.append(NEW_LINE);
					}
				}
				
			} // end while
			
		}

		// --- Append the vmargs at the end of the string ------
		if (vmargs!=null) {
			if (result.charAt(result.length()-1)!='\n') {
				result.append('\n');
			}
			result.append(CMD_VMARGS);
			result.append(NEW_LINE);
			result.append(vmargs);
		}

		return result.toString();
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
	public static void setQuitJVM(boolean quitJVM) {
		Application.quitJVM = quitJVM;
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
		
			OIDCAuthorization.getInstance().setIssuerURI(Application.getGlobalInfo().getOIDCIssuerURI());
			OIDCAuthorization.getInstance().setTranslator(Language.getInstance());
			OIDCAuthorization.getInstance().setTrustStore(new File(Application.getGlobalInfo().getPathProperty(true) + Trust.OIDC_TRUST_STORE));
			OIDCAuthorization.getInstance().setAvailabilityHandler( new OIDCResourceAvailabilityHandler() {
				
				/* (non-Javadoc)
				 * @see de.enflexit.oidc.OIDCResourceAvailabilityHandler#onResourceAvailable(de.enflexit.oidc.OIDCAuthorization.URLProcessor)
				 */
				@Override
				public void onResourceAvailable(URLProcessor urlProcessor) {
					Application.getGlobalInfo().setOIDCUsername(OIDCAuthorization.getInstance().getLastSuccessfulUser());
				}
				/* (non-Javadoc)
				 * @see de.enflexit.oidc.OIDCResourceAvailabilityHandler#onAuthorizationNecessary(de.enflexit.oidc.OIDCAuthorization)
				 */
				@Override
				public boolean onAuthorizationNecessary(OIDCAuthorization oidcAuthorization) {
					return true; // show the login panel
				}
			});
			OIDCAuthorization.getInstance().accessResource(OIDCPanel.DEBUG_RESOURCE_URI, getGlobalInfo().getOIDCUsername(), getMainWindow());
			
		} catch (URISyntaxException | KeyManagementException | NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException ex) {
			//ex.printStackTrace();
			System.err.println("Authentication failed: "+ex.getClass()+": "+ex.getMessage());
		}
	}
	
	/**
	 * Opens the Option-Dialog of the application with a specified TabName
	 * @param focusOnTab Can be used to set the focus directly to a Tab specified by its name 
	 */
	public static void showOptionDialog(String focusOnTab) {
		
		if (optionDialog!=null) {
			if (optionDialog.isVisible()==true) {
				// --- Set focus again ----------
				optionDialog.requestFocus();
				return;
			} else {
				// --- dispose it first --------- 
				optionDialog.dispose();
				optionDialog = null;
			}
		}
		
		if (isRunningAsServer()==true) {
			optionDialog = new OptionDialog(null);
		} else {
			optionDialog = new OptionDialog(getMainWindow());
		}
		if (focusOnTab!=null) {
			optionDialog.setFocusOnTab(focusOnTab);
		}
		optionDialog.setVisible(true);
		// - - - - - - - - - - - - - - - - - - - -
		if (optionDialog!=null) {
			optionDialog.dispose();
		}
		optionDialog = null;
		
	}

	/**
	 * Will show the About-Dialog of the application
	 */
	public static void showAboutDialog() {
		
		if (aboutDialog!=null) return;
		if (isRunningAsServer()==true) {
			aboutDialog = new AboutDialog(null);
		} else {
			aboutDialog = new AboutDialog(getMainWindow());
		}
		aboutDialog.setVisible(true);
		// - - - Wait for user - - - - - - - - -  
		aboutDialog.dispose();
		aboutDialog = null;		
	}
	
	/**
	 * Opens the central {@link AwbDatabaseDialog} of the application with a specified factoryID.
	 *
	 * @param ownerWindow the owner window
	 * @param factoryID the factory ID to configure or <code>null</code>
	 */
	public static void showDatabaseDialog(Window ownerWindow, String factoryID) {
		
		if (awbDatabaseDialog!=null) return;
		
		if (ownerWindow!=null) {
			awbDatabaseDialog = new AwbDatabaseDialog(ownerWindow, factoryID);
		} else {
			awbDatabaseDialog = new AwbDatabaseDialog(getMainWindow(), factoryID);
		}
		awbDatabaseDialog.setVisible(true);
		// - - - Wait for user - - - - - - - - -  
		awbDatabaseDialog.dispose();
		awbDatabaseDialog = null;
	}
	
	/**
	 * Will try to browse to the URI specified by the string (e.g. https://www.enflex.it).
	 * @param uri the URI as String
	 */
	public static void browseURI(String uri) {
		try {
			URI linkURI = new URI(uri);
			Desktop.getDesktop().browse(linkURI);
		} catch (URISyntaxException | IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Shows the eclipse workbench.
	 */
	public static void showEclipseWorkbench() {
		getIApplication().startEclipseUiThroughThread(null);
	}
	
	/**
	 * Adds a supplement to the application title.
	 * @param add2BasicTitle the addition for the basic title 
	 */
	public static void setTitleAddition(String add2BasicTitle) {
		if (getMainWindow()!=null) {
			getMainWindow().setTitelAddition(add2BasicTitle);
		}
	}

	/**
	 * Sets the jade status color.
	 * @param jadeStatus the new jade status color
	 */
	public static void setJadeStatusColor(JadeStatusColor jadeStatus) {
		if (getMainWindow()!=null) {
			getMainWindow().setJadeStatusColor(jadeStatus);
		}
		if (trayIcon!=null) {
			trayIcon.refreshView();	
		}
	}
	
	/**
	 * Sets the status bar message text.
	 * @param statusText the new status bar message
	 */
	public static void setStatusBarMessage(String statusText) {
		if (getMainWindow()!=null) {
			getMainWindow().setStatusBarMessage(statusText);	
		}
	}
	/**
	 * Sets the status bar text message to 'Ready'.
	 */
	public static void setStatusBarMessageReady() {
		if (getMainWindow()!=null) {
			getMainWindow().setStatusBarMessage(Language.translate("Fertig"));
		}
	}
	/**
	 * Sets the cursor for the application.
	 * @param cursor the new application cursor
	 */
	public static void setCursor(Cursor cursor) {
		if (getMainWindow()!=null) {
			getMainWindow().setCursor(cursor);
		}
	}
	
	/**
	 * Set's the Look and feel of the application.
	 * @param newLnF the new look and feel
	 */
	public static void setLookAndFeel(String newLnF) {
		if (getMainWindow()!=null) {
			getGlobalInfo().setAppLookAndFeelClassName(newLnF);
			AwbLookAndFeelAdjustments.setLookAndFeel(newLnF, getMainWindow());
			getMainWindow().resetAfterLookAndFeelUpdate();
			getProjectsLoaded().setProjectView();
		}
	}	
	
	/**
	 * Enables to change the user language of the application   .
	 * @param newLang the new language indicator. Should be: 'de', 'en', 'it', 'es' or 'fr' etc. and is equal to the header line of the dictionary
	 */
	public static void setLanguage(String newLang) {
		setLanguage(newLang, true);
	}
	/**
	 * Enables to change the user language of the application  
	 * If 'askUser' is set to false the changes will be done without user interaction. 
	 *
	 * @param newLang the new language indicator. Should be: 'de', 'en', 'it', 'es' or 'fr' etc. and is equal to the header line of the dictionary
	 * @param askUser the indicator to ask the user swithcing the language
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
			Integer MsgAnswer = JOptionPane.showConfirmDialog( Application.getMainWindow().getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if (MsgAnswer==1) return;
			
		}
		
		// --- Stop Agent.Workbench ---------------------------------
		if (stopAgentWorkbench()==false) return;
		// --- Switch Language --------------------------------------
		System.out.println("=> " + Language.translate("Sprachumstellung zu") + " '" + newLang + "'.");
		Language.changeApplicationLanguageTo(newLang);
		// --- Restart application ----------------------------------
		startAgentWorkbench();
	}	
	
	/**
	 * Executes the Benchmark-Test of SciMark 2.0 to determine the ability of this system to deal with number crunching.<br> 
	 * The result will be available in Mflops (Millions of floating point operations per second)
	 *
	 * @param forceBenchmark the indicator to force benchmark, even it was done earlier
	 */
	public static void doBenchmark(boolean forceBenchmark) {
		
		boolean execute = forceBenchmark || BenchmarkMeasurement.isBenchmarkRequired();
		if (execute==true && Application.isBenchmarkRunning()==false) {
			// --- Execute the Benchmark-Thread -----------
			BenchmarkMeasurement bm = new BenchmarkMeasurement(forceBenchmark);
			Object synchronizationObject = bm.getSynchronizationObject();
			bm.start();
			synchronized (synchronizationObject) {
				try {
					synchronizationObject.wait();
				} catch (InterruptedException intEx) {
					System.err.println("[Application]: Waiting for the benchmark to properly start was interrupted");
					intEx.printStackTrace();
				}
			}
			
		}
	}
	/**
	 * Sets that the benchmark is running or not.
	 * @param isRunning the indicator if the benchmark is running
	 */
	public static void setBenchmarkRunning(boolean isRunning) {
		benchmarkRunning = isRunning;
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
		while (Application.isBenchmarkRunning()==true) {
			Application.setStatusBarMessage(Language.translate("Warte auf das Ende des Benchmarks ..."));
			try {
				Thread.sleep(250);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		Application.setStatusBarMessageReady();
	}
	
	/**
	 * Starts the bundle evaluation for specific classes if required.
	 */
	private static void startBundleEvaluation() {
		
		// --- Check if to start the BundleEvaluator ---------------- 
		if (Application.getGlobalInfo().isStartBundleEvaluator()==false) return;
		
		// -- Create a new thread that starts the evaluations -------
		Thread evaluatorThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// --- Wait for the SessionFactory creation --------- 
				HibernateUtilities.waitForSessionFactoryCreation();
				// --- Define filter for bundle class search --------
				BundleClassFilterCollector.collectAndDefineSetOfBundleClassFilter();
				// --- Evaluate the already loaded bundles ----------
				BundleEvaluator.getInstance().evaluateAllBundles();
				// --- Evaluate the features ------------------------
				FeatureEvaluator.getInstance().evaluateFeatureInformationInThread();
			}
		}, "Bundle and Feature Evaluation Starter");
		evaluatorThread.start();
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
		Application.informApplicationListener(new ApplicationEvent(ApplicationEvent.PROJECT_FOCUSED, projectFocused));
	}
	/**
	 * Returns the currently focused {@link Project}.
	 * @return the project focused
	 */
	public static Project getProjectFocused() {
		return projectFocused;
	}
	
	/**
	 * Sets the actual configuration for the ontology visualization.
	 */
	public static void setOntologyVisualisationConfigurationToCommonBundle() {
		
		// --- Add the known OntologyClassVisualisation's of Agent.Workbench --
		OntologyVisualisationConfiguration.registerOntologyClassVisualisation(new TimeSeriesVisualisation());
		OntologyVisualisationConfiguration.registerOntologyClassVisualisation(new XyChartVisualisation());
		
		// --- Set the current main window ------------------------------------
		OntologyVisualisationConfiguration.setApplicationTitle(Application.getGlobalInfo().getApplicationTitle());
		OntologyVisualisationConfiguration.setOwnerWindow(Application.getMainWindow());
		OntologyVisualisationConfiguration.setApplicationIconImage(GlobalInfo.getInternalImageAwbIcon16());
	}

	
	/**
	 * Starts the java systems garbage collection in an individual Thread.
	 * @see System#gc()
	 */
	public static void startGarbageCollection() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.gc();
				} catch (Exception ex) {
					// Hide exception
				}
			}
		}, "GarbageCollectorThread").start();
	}
	
	
	// --------------------------------------------------------------
	// --- From here, handling of application listeners -------------
	// --------------------------------------------------------------
	/**
	 * Returns the list of {@link ApplicationListener}.
	 * @return the application listener
	 */
	private static List<ApplicationListener> getApplicationListenerList() {
		if (appListenerList==null) {
			appListenerList = new ArrayList<>();
		}
		return appListenerList;
	}
	/**
	 * Adds the specified listener to the list of {@link ApplicationListener} .
	 * @param listener the listener to add
	 */
	public static void addApplicationListener(ApplicationListener listener) {
		if (listener!=null && getApplicationListenerList().contains(listener)==false) {
			getApplicationListenerList().add(listener);
		}
	}
	/**
	 * Removes the specified listener to the list of {@link ApplicationListener} .
	 * @param listener the listener to remove
	 */
	public static void removeApplicationListener(ApplicationListener listener) {
		if (listener!=null && getApplicationListenerList().contains(listener)==true) {
			getApplicationListenerList().remove(listener);
		}
	}
	/**
	 * Informs all registered listener aboutDialog the specified event.
	 * @param event the actual {@link ApplicationEvent} to inform aboutDialog
	 */
	public static void informApplicationListener(ApplicationEvent event) {
		if (event==null) return;
		for (int i = 0; i < getApplicationListenerList().size(); i++) {
			try {
				getApplicationListenerList().get(i).onApplicationEvent(event);;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
} 

