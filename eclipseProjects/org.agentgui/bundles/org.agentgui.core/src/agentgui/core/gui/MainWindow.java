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
package agentgui.core.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.gui.projectwindow.simsetup.SetupSelectorToolbar;
import agentgui.core.project.Project;
import agentgui.core.update.AgentGuiUpdater;
import agentgui.logging.components.JPanelConsole;
import agentgui.logging.components.JTabbedPane4Consoles;
import agentgui.logging.components.SysOutBoard;
import agentgui.simulationService.agents.LoadExecutionAgent;

/**
 * This class represents the main user-interface of the application AgentGUI.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private final ImageIcon iconGreen = GlobalInfo.getInternalImageIcon("StatGreen.png");
	private final ImageIcon iconRed = GlobalInfo.getInternalImageIcon("StatRed.png");
	private final ImageIcon iconClose = GlobalInfo.getInternalImageIcon("MBclose.png");
	private final ImageIcon iconCloseDummy = GlobalInfo.getInternalImageIcon("MBdummy.png");

	private static JLabel statusBar;
	private JLabel statusJade;

	private JSplitPane jSplitPane4ProjectDesktop;
	private JDesktopPane jDesktopPane4Projects;
	private JTabbedPane4Consoles jTabbedPane4Console;
	private JPanelConsole jPanelConsoleLocal = Application.getConsole();;
	private int oldDividerLocation;
	private boolean allowProjectMaximization = true;

	private JMenuBar jMenuBarBase;
	private JMenuBar jMenuBarMain;
	private JMenu jMenuMainProject;
	private JMenu jMenuMainView;
	private ButtonGroup viewGroup;
	public JRadioButtonMenuItem viewDeveloper;
	public JRadioButtonMenuItem viewEndUser;
	private JMenu jMenuMainJade;
	private JMenu jMenuMainSimulation;
	private JMenuItem jMenuItemSimStart;
	private JMenuItem jMenuItemSimPause;
	private JMenuItem jMenuItemSimStop;
	private JMenu jMenuExtra;
	private JMenu jMenuExtraLang;
	private JMenu jMenuExtraLnF;
	private JMenu jMenuMainWindows;
	private JMenu jMenuMainHelp;

	private JMenuItem jMenuCloseButton;

	private JToolBar jToolBarApplication;
	private SetupSelectorToolbar setupSelectorToolbar;
	private JButton jButtonJadeTools;
	private JPopupMenu jPopupMenuJadeTools;

	private JButton jButtonSimStart;
	private JButton jButtonSimPause;
	private JButton jButtonSimStop;

	/**
	 * Constructor of this class.
	 */
	public MainWindow() {

		// --- Set the IconImage ----------------------------------
		this.setIconImage(GlobalInfo.getInternalImage("AgentGUI.png"));

		// --- Set the Look and Feel of the Application -----------
		this.setLookAndFeel();

		// --- Create the Main-Elements of the Application --------
		this.initComponents();
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);

		// --- configure console ----------------------------------
		this.oldDividerLocation = this.jSplitPane4ProjectDesktop.getHeight() * 3 / 4;
		this.jSplitPane4ProjectDesktop.setDividerLocation(this.oldDividerLocation);
		this.setConsoleVisible(false);

		// --- Set the JTabbedPan for remote console output -------
		SysOutBoard.setJTabbedPane4Consoles(this.getJTabbedPane4Console());

		// --- Finalize the display of the application ------------
		this.setTitelAddition("");
		this.setCloseButtonPosition(false);

		this.pack();
		this.validate();
		this.repaint();
		this.setVisible(true);

	}

	/**
	 * Inits the components.
	 */
	private void initComponents() {

		this.setJMenuBar(this.getJMenuBarBase());

		this.add(getJToolBarApplication(), BorderLayout.NORTH);
		this.add(getStatusBar(), BorderLayout.SOUTH);
		this.add(getMainSplitpane());

		Dimension frameSize = this.getSizeRelatedToScreenSize();
		this.setPreferredSize(frameSize);
		this.setSize(frameSize);

		// --- Maximze the JFrame, if configured in the GlobalInfo --
		if (Application.getGlobalInfo().isMaximzeMainWindow() == true) {
			this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		}

		// --- Listener for closing the application -----------------
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				Application.stop();
			}
		});

		// --- Listener for state max / Normal events ---------------
		this.addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent evt) {
				if (evt.getNewState() == JFrame.NORMAL) {
					Application.getGlobalInfo().setMaximzeMainWindow(false);
				} else if (evt.getNewState() == JFrame.MAXIMIZED_BOTH) {
					Application.getGlobalInfo().setMaximzeMainWindow(true);
				}
			}
		});

		// --- Listener for resizing events of the window -----------
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent ce) {
				if (consoleIsVisible() == false) {
					jSplitPane4ProjectDesktop.setDividerLocation(jSplitPane4ProjectDesktop.getHeight());
				}
				if (Application.getProjectsLoaded().count() != 0) {
					Application.getProjectFocused().setMaximized();
					setCloseButtonPosition(true);
				} else {
					setCloseButtonPosition(false);
				}
			}
		});

		// --- Set button for simulation control ----------
		this.setSimulationReady2Start();

	}

	/**
	 * Return the size in relation (scaled) to the screen size.
	 */
	private Dimension getSizeRelatedToScreenSize() {
		// --- Default size ---------------------
		Dimension frameSize = new Dimension(1150, 640);

		// --- Scale relative to screen ---------
		double scale = 0.9;
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
		int frameWidth = (int) (screenDimension.getWidth() * scale);
		int frameHeight = (int) (screenDimension.getHeight() * scale);
		frameSize.setSize(frameWidth, frameHeight);

		return frameSize;
	}

	/**
	 * Gets the status bar.
	 * 
	 * @return the status bar
	 */
	private JPanel getStatusBar() {

		// --- Linker Teil ----------------------
		statusBar = new JLabel();
		statusBar.setPreferredSize(new Dimension(300, 16));
		statusBar.setFont(new Font("Dialog", Font.PLAIN, 12));

		// --- Mittlerer Teil -------------------
		statusJade = new JLabel();
		statusJade.setPreferredSize(new Dimension(200, 16));
		statusJade.setFont(new Font("Dialog", Font.PLAIN, 12));
		statusJade.setHorizontalAlignment(SwingConstants.RIGHT);
		setStatusJadeRunning(false);

		// --- Rechter Teil ---------------------
		JPanel RightPart = new JPanel(new BorderLayout());
		RightPart.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
		RightPart.setOpaque(false);

		// --- StatusBar zusammenbauen ------------------
		JPanel JPStat = new JPanel(new BorderLayout());
		JPStat.setPreferredSize(new Dimension(10, 23));
		JPStat.add(statusBar, BorderLayout.WEST);
		JPStat.add(statusJade, BorderLayout.CENTER);
		JPStat.add(RightPart, BorderLayout.EAST);
		return JPStat;
	}

	/**
	 * Sets a text in the applications status bar.
	 * 
	 * @param message the new status bar
	 */
	public void setStatusBar(String message) {
		if (message == null) {
			statusBar.setText("  ");
		} else {
			statusBar.setText("  " + message);
		}
		;
		statusBar.validate();
		statusBar.repaint();
	}

	/**
	 * This method is used if a project is open. Then the project name is displayed behind the applications title (e.g. 'Agent.GUI: project name'
	 *
	 * @param add2BasicTitel the new titel addition
	 */
	public void setTitelAddition(String add2BasicTitel) {
		if (add2BasicTitel != "") {
			this.setTitle(Application.getGlobalInfo().getApplicationTitle() + ": " + add2BasicTitel);
		} else {
			this.setTitle(Application.getGlobalInfo().getApplicationTitle());
		}
	}

	/**
	 * Sets the indicator in order to visul inform that JADE is running or not (red or green button in the right corner of the status bar + text).
	 *
	 * @param isRunning the new status jade running
	 */
	public void setStatusJadeRunning(boolean isRunning) {
		if (isRunning == false) {
			statusJade.setText(Language.translate("JADE wurde noch nicht gestartet."));
			statusJade.setIcon(iconRed);
		} else {
			statusJade.setText(Language.translate("JADE wurde lokal gestartet."));
			statusJade.setIcon(iconGreen);
		}
		;
	}

	/**
	 * Here the 'look and feel' LnF of java Swing can be set.
	 * 
	 * @param newLnF the new look and feel
	 */
	public void setLookAndFeel() {

		String lnfClassName = Application.getGlobalInfo().getAppLookAndFeelClassName();
		if (lnfClassName == null)
			return;

		String currLookAndFeelClassName = UIManager.getLookAndFeel().getClass().getName();
		if (lnfClassName.equals(currLookAndFeelClassName) == true)
			return;

		try {
			UIManager.setLookAndFeel(lnfClassName);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Cannot install " + lnfClassName + " on this platform:" + e.getMessage());
		}

		if (jMenuExtraLnF != null) {
			jMenuExtraLnF.removeAll();
			this.setJMenuExtraLnF();
		}
		if (jMenuExtraLang != null) {
			jMenuExtraLang.removeAll();
			this.setjMenuExtraLang();
		}
		if (Application.getProjectFocused() != null) {
			Application.getProjectFocused().setMaximized();
		}
	}

	/**
	 * Console is visible.
	 *
	 * @return true, if successful
	 */
	public boolean consoleIsVisible() {
		if (jSplitPane4ProjectDesktop.getDividerSize() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Do switch console.
	 */
	private void doSwitchConsole() {
		if (jSplitPane4ProjectDesktop.getDividerSize() > 0) {
			this.setConsoleVisible(false);
		} else {
			this.setConsoleVisible(true);
		}
	}

	/**
	 * Sets the console visible.
	 *
	 * @param show the new console visible
	 */
	private void setConsoleVisible(boolean show) {

		if (show == true) {
			// --- System.out.println("Console einblenden ...");
			jSplitPane4ProjectDesktop.setDividerLocation(oldDividerLocation);
			jSplitPane4ProjectDesktop.setDividerSize(10);

		} else {
			// --- System.out.println("Console ausblenden ...");
			oldDividerLocation = jSplitPane4ProjectDesktop.getDividerLocation();
			jSplitPane4ProjectDesktop.setDividerSize(0);
			jSplitPane4ProjectDesktop.setDividerLocation(jSplitPane4ProjectDesktop.getHeight());
		}
		this.validate();
		if (Application.getProjectsLoaded().count() != 0) {
			Application.getProjectFocused().setMaximized();
		}
	}

	/**
	 * This method sets back the focus to this JFrame.
	 */
	public void restoreFocus() {
		if (this.getExtendedState() == Frame.ICONIFIED || this.getExtendedState() == Frame.ICONIFIED + Frame.MAXIMIZED_BOTH) {
			this.setState(Frame.NORMAL);
		}
		this.setAlwaysOnTop(true);
		this.setAlwaysOnTop(false);
	}

	/**
	 * Gets the main splitpane.
	 * 
	 * @return the main splitpane
	 */
	private JSplitPane getMainSplitpane() {
		if (jSplitPane4ProjectDesktop == null) {

			jSplitPane4ProjectDesktop = new JSplitPane();
			jSplitPane4ProjectDesktop.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane4ProjectDesktop.setDividerSize(10);
			jSplitPane4ProjectDesktop.setResizeWeight(1);
			jSplitPane4ProjectDesktop.setOneTouchExpandable(true);
			jSplitPane4ProjectDesktop.setTopComponent(getJDesktopPane4Projects());
			jSplitPane4ProjectDesktop.setBottomComponent(getJTabbedPane4Console());
			jSplitPane4ProjectDesktop.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent EventSource) {
					// --- Deviderpositionierung abfangen ---
					if (EventSource.getPropertyName() == "lastDividerLocation") {
						if (Application.getProjectsLoaded().count() != 0) {
							if (allowProjectMaximization == true) {
								allowProjectMaximization = false;
								Application.getProjectFocused().setMaximized();
								allowProjectMaximization = true;
							}
						}
					}
				}
			});
		}
		return jSplitPane4ProjectDesktop;
	}

	/**
	 * This method returns the JTabbedPane for console windows.
	 *
	 * @return the j tabbed pane4 console
	 */
	public JTabbedPane4Consoles getJTabbedPane4Console() {

		if (jTabbedPane4Console == null) {
			// --- Get the TabPane for Console-Tabs ---------------
			jTabbedPane4Console = new JTabbedPane4Consoles();
			jTabbedPane4Console.add(Language.translate("Lokal"), this.jPanelConsoleLocal);
		}
		return jTabbedPane4Console;
	}

	/**
	 * This method returns the JDesktopPane, where the JInternalFrame's of the project will be placed.
	 *
	 * @return the j desktop pane4 projects
	 */
	public JDesktopPane getJDesktopPane4Projects() {
		if (jDesktopPane4Projects == null) {
			jDesktopPane4Projects = new JDesktopPane();
			jDesktopPane4Projects.setDoubleBuffered(false);
		}
		return jDesktopPane4Projects;
	}
	// ------------------------------------------------------------
	// --- Desktop der Anwendung definieren - ENDE ----------------
	// ------------------------------------------------------------

	// ------------------------------------------------------------
	// --- Tollbar definition - START -----------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the applications menu bar.
	 *
	 * @return the j menu bar base
	 */
	private JMenuBar getJMenuBarBase() {
		if (jMenuBarBase == null) {
			jMenuBarBase = new JMenuBar();
			jMenuBarBase.setLayout(new GridBagLayout());
			Insets insets = new Insets(0, 0, 0, 0);
			jMenuBarBase.add(this.getJMenuBarMain(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, insets, 0, 0));
			jMenuBarBase.add(this.getCloseButton(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, insets, 0, 0));
		}
		return jMenuBarBase;
	}

	/**
	 * This method returns the current instance of the applications menu bar.
	 *
	 * @return the j menu bar main
	 */
	private JMenuBar getJMenuBarMain() {
		if (jMenuBarMain == null) {
			jMenuBarMain = new JMenuBar();
			jMenuBarMain.setBorder(null);
			jMenuBarMain.setLayout(new GridBagLayout());

			jMenuBarMain.add(getJMenuMainProject());
			jMenuBarMain.add(getJMenuMainView());
			jMenuBarMain.add(getJMenuMainJade());
			jMenuBarMain.add(getJMenuMainSimulation());
			jMenuBarMain.add(getJMenuMainExtra());
			jMenuBarMain.add(getJMenuMainWindow());
			jMenuBarMain.add(getJMenuMainHelp());

		}
		return jMenuBarMain;
	}

	/**
	 * This method can be used in order to add an individual menu at a specified index position of the menu bar.
	 *
	 * @param myMenu the my menu
	 * @param indexPosition the index position
	 */
	public void addJMenu(JMenu myMenu, int indexPosition) {
		int nElements = jMenuBarMain.getSubElements().length;
		if (indexPosition > (nElements - 1)) {
			this.addJMenu(myMenu);
		} else {
			jMenuBarMain.add(myMenu, indexPosition);
			this.validate();
		}
	}

	/**
	 * This method can be used in order to add an individual menu.
	 *
	 * @param myMenu the my menu
	 */
	public void addJMenu(JMenu myMenu) {
		jMenuBarMain.add(myMenu);
		this.validate();
	}

	/**
	 * This method can be used in order to add an individual JMmenuItem at a specified index position of the given menu.
	 *
	 * @param menu2add the menu2add
	 * @param myMenuItemComponent the my menu item component
	 * @param indexPosition the index position
	 */
	public void addJMenuItemComponent(JMenu menu2add, JComponent myMenuItemComponent, int indexPosition) {
		int nElements = menu2add.getItemCount();
		if (indexPosition > (nElements - 1)) {
			this.addJMenuItemComponent(menu2add, myMenuItemComponent);
		} else {
			menu2add.add(myMenuItemComponent, indexPosition);
			this.validate();
		}
	}

	/**
	 * This method can be used in order to add an individual JMmenuItem to the given menu.
	 *
	 * @param menu2add the menu2add
	 * @param myMenuItemComponent the my menu item component
	 */
	public void addJMenuItemComponent(JMenu menu2add, JComponent myMenuItemComponent) {
		menu2add.add(myMenuItemComponent);
		this.validate();
	}

	// ------------------------------------------------------------
	// --- Menu Projects ------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "Project".
	 *
	 * @return the j menu main project
	 */
	public JMenu getJMenuMainProject() {
		if (jMenuMainProject == null) {
			jMenuMainProject = new JMenu("Projekte");
			jMenuMainProject.setText(Language.translate("Projekte"));
			jMenuMainProject.add(new CWMenueItem("ProjectNew", Language.translate("Neues Projekt"), "MBnew.png"));
			jMenuMainProject.add(new CWMenueItem("ProjectOpen", Language.translate("Projekt öffnen"), "MBopen.png"));
			jMenuMainProject.add(new CWMenueItem("ProjectClose", Language.translate("Projekt schließen"), "MBclose.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenueItem("ProjectSave", Language.translate("Projekt speichern"), "MBsave.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenueItem("ProjectImport", Language.translate("Projekt importieren"), "MBtransImport.png"));
			jMenuMainProject.add(new CWMenueItem("ProjectExport", Language.translate("Projekt exportieren"), "MBtransExport.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenueItem("ProjectDelete", Language.translate("Projekt löschen"), "Delete.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenueItem("ApplicationQuit", Language.translate("Beenden"), null));
		}
		return jMenuMainProject;
	}

	// ------------------------------------------------------------
	// --- Menu "View" --------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "View".
	 *
	 * @return the j menu main view
	 */
	public JMenu getJMenuMainView() {
		if (jMenuMainView == null) {
			jMenuMainView = new JMenu("Ansicht");
			jMenuMainView.setText(Language.translate("Ansicht"));

			// --------------------------------------------
			// --- View for Developer or End user ---------
			// --------------------------------------------
			ActionListener viewListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					JRadioButtonMenuItem actuator = (JRadioButtonMenuItem) evt.getSource();
					if (actuator == viewDeveloper) {
						if (Application.getProjectFocused().getProjectView().equals(Project.VIEW_Developer) == false) {
							Application.getProjectFocused().setProjectView(Project.VIEW_Developer);
						}

					} else if (actuator == viewEndUser) {
						if (Application.getProjectFocused().getProjectView().equals(Project.VIEW_User) == false) {
							Application.getProjectFocused().setProjectView(Project.VIEW_User);
						}

					}
				}
			};

			viewDeveloper = new JRadioButtonMenuItem(Language.translate("Entwickler-Ansicht"));
			viewDeveloper.setSelected(true);
			viewDeveloper.addActionListener(viewListener);
			jMenuMainView.add(viewDeveloper);

			viewEndUser = new JRadioButtonMenuItem(Language.translate("Endanwender-Ansicht"));
			viewEndUser.addActionListener(viewListener);
			jMenuMainView.add(viewEndUser);

			viewGroup = new ButtonGroup();
			viewGroup.add(viewDeveloper);
			viewGroup.add(viewEndUser);
			// --------------------------------------------

			jMenuMainView.addSeparator();
			jMenuMainView.add(new CWMenueItem("ViewConsole", Language.translate("Konsole ein- oder ausblenden"), "MBConsole.png"));
		}
		return jMenuMainView;
	}

	// ------------------------------------------------------------
	// --- Menu "JADE" --------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "JADE".
	 *
	 * @return the j menu main jade
	 */
	public JMenu getJMenuMainJade() {
		if (jMenuMainJade == null) {
			jMenuMainJade = new JMenu("JADE");
			jMenuMainJade.setText(Language.translate("JADE"));
			jMenuMainJade.add(new CWMenueItem("JadeStart", Language.translate("JADE starten"), "MBJadeOn.png"));
			jMenuMainJade.add(new CWMenueItem("JadeStop", Language.translate("JADE stoppen"), "MBJadeOff.png"));
			jMenuMainJade.addSeparator();
			jMenuMainJade.add(new CWMenueItem("PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) öffnen"), "MBJadeRMA.gif"));
			jMenuMainJade.add(new CWMenueItem("PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif"));
			jMenuMainJade.add(new CWMenueItem("PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif"));
			jMenuMainJade.add(new CWMenueItem("PopDF", Language.translate("DF anzeigen"), "MBJadeDF.gif"));
			jMenuMainJade.add(new CWMenueItem("PopIntrospec", Language.translate("Introspector-Agent starten"), "MBJadeIntrospector.gif"));
			jMenuMainJade.add(new CWMenueItem("PopLog", Language.translate("Log-Manager starten"), "MBJadeLogger.gif"));
			jMenuMainJade.addSeparator();
			jMenuMainJade.add(new CWMenueItem("ContainerMonitoring", Language.translate("Auslastungs-Monitor öffnen"), "MBLoadMonitor.png"));
			jMenuMainJade.add(new CWMenueItem("ThreadMonitoring", Language.translate("Thread-Monitor öffnen"), "MBclock.png"));
		}
		return jMenuMainJade;
	}

	// ------------------------------------------------------------
	// --- Menu Simulation ----------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "MAS".
	 *
	 * @return the j menu main simulation
	 */
	public JMenu getJMenuMainSimulation() {
		if (jMenuMainSimulation == null) {
			jMenuMainSimulation = new JMenu("MAS");
			jMenuMainSimulation.setText(Language.translate("MAS"));

			jMenuItemSimStart = new CWMenueItem("SimulationStart", Language.translate("Start"), "MBLoadPlay.png");
			jMenuMainSimulation.add(jMenuItemSimStart);
			jMenuItemSimPause = new CWMenueItem("SimulationPause", Language.translate("Pause"), "MBLoadPause.png");
			jMenuMainSimulation.add(jMenuItemSimPause);
			jMenuItemSimStop = new CWMenueItem("SimulationStop", Language.translate("Stop"), "MBLoadStopRecord.png");
			jMenuMainSimulation.add(jMenuItemSimStop);
		}
		return jMenuMainSimulation;
	}

	// ------------------------------------------------------------
	// --- Menu Extras ---------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "Extra".
	 *
	 * @return the j menu main extra
	 */
	public JMenu getJMenuMainExtra() {
		if (jMenuExtra == null) {
			jMenuExtra = new JMenu("Extras");
			jMenuExtra.setText(Language.translate("Extras"));

			// --- Menue 'Sprache' ---
			jMenuExtraLang = new JMenu();
			jMenuExtraLang.setText(Language.translate("Sprache"));
			this.setjMenuExtraLang();
			jMenuExtra.add(jMenuExtraLang);

			// --- Menue 'LnF' -------
			jMenuExtraLnF = new JMenu();
			jMenuExtraLnF.setText("Look and Feel");
			this.setJMenuExtraLnF();
			jMenuExtra.add(jMenuExtraLnF);

			jMenuExtra.addSeparator();
			jMenuExtra.add(new CWMenueItem("ExtraBenchmark", "SciMark 2.0 - Benchmark", null));

			jMenuExtra.addSeparator();
			jMenuExtra.add(new CWMenueItem("ExtraOptions", Language.translate("Optionen"), null));
			jMenuExtra.add(new CWMenueItem("Authentication", Language.translate("Web Service Authentifizierung"), null));

		}
		return jMenuExtra;
	}

	// ----------------------------------------------------------
	// --- Sprache ----------------------------------------------
	// ----------------------------------------------------------
	/**
	 * Setj menu extra lang.
	 */
	private void setjMenuExtraLang() {

		// --- Display languages --------------------------------
		String[] languageList = Language.getLanguages(true);
		boolean setBold = false;
		for (int i = 0; i < languageList.length; i++) {
			if (Language.isCurrentLanguage(languageList[i])) {
				setBold = true;
			} else {
				setBold = false;
			}
			jMenuExtraLang.add(new JMenuItemLang(languageList[i], setBold));
		}

		jMenuExtraLang.addSeparator();
		jMenuExtraLang.add(new CWMenueItem("ExtraTranslation", Language.translate("Übersetzen ..."), null));

	}

	// ------------------------------------------------
	// --- Sub class for available languages ----------
	// ------------------------------------------------
	/**
	 * The Class JMenuItemLang.
	 */
	private class JMenuItemLang extends JMenuItem implements ActionListener {

		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new j menu item lang.
		 *
		 * @param LangHeader the lang header
		 * @param setBold the set bold
		 */
		private JMenuItemLang(String LangHeader, boolean setBold) {
			this.setText(Language.getLanguageName(LangHeader.toUpperCase()));
			if (setBold) {
				Font cfont = this.getFont();
				if (cfont.isBold()) {
					this.setForeground(Application.getGlobalInfo().ColorMenuHighLight());
				} else {
					this.setFont(cfont.deriveFont(Font.BOLD));
				}
			}
			this.addActionListener(this);
			this.setActionCommand(LangHeader);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent evt) {
			String ActCMD = evt.getActionCommand();
			Application.setLanguage(ActCMD);
		}
	}
	// ------------------------------------------------------------

	// ------------------------------------------------------------
	// --- Look and Feel ------------------------------------------
	// ------------------------------------------------------------
	/**
	 * Set the JMenue for the look and feel.
	 */
	private void setJMenuExtraLnF() {

		UIManager.LookAndFeelInfo installedLnF[] = UIManager.getInstalledLookAndFeels();
		for (int i = 0, n = installedLnF.length; i < n; i++) {
			boolean setBold = installedLnF[i].getClassName().equals(Application.getGlobalInfo().getAppLookAndFeelClassName());
			jMenuExtraLnF.add(new JMenuItmenLnF(installedLnF[i].getName(), installedLnF[i].getClassName(), setBold));
		}
	}

	// ------------------------------------------------
	// --- Sub class for the Look and Feel Menu -------
	// ------------------------------------------------
	/**
	 * The Class JMenuItmenLnF provides a container for single Look and Feel descriptions.
	 */
	private class JMenuItmenLnF extends JMenuItem {

		private static final long serialVersionUID = 1L;
		private String LnFClass;

		/**
		 * Instantiates a new JMenuItmen for a single Look and Feel entry.
		 *
		 * @param LnFName the name of the look and feel
		 * @param LnFClass the class of the look and feel
		 * @param setBold the set bold
		 */
		private JMenuItmenLnF(String LnFName, String LnFClass, boolean setBold) {

			this.LnFClass = LnFClass;
			this.setText(LnFName);
			if (setBold == true) {
				Font cfont = this.getFont();
				if (cfont.isBold()) {
					this.setForeground(Application.getGlobalInfo().ColorMenuHighLight());
				} else {
					this.setFont(cfont.deriveFont(Font.BOLD));
				}
			}
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Application.setLookAndFeel(JMenuItmenLnF.this.LnFClass);
				}
			});
		}
	}
	// ------------------------------------------------------------
	// ------------------------------------------------------------

	// ------------------------------------------------------------
	// --- Menu Window---------------------------------------------
	// ------------------------------------------------------------
	/**
	 * Gets the JMenu main window.
	 *
	 * @return the JMenu main window
	 */
	public JMenu getJMenuMainWindow() {
		if (jMenuMainWindows == null) {
			jMenuMainWindows = new JMenu("Fenster");
			jMenuMainWindows.setText(Language.translate("Fenster"));
		}
		return jMenuMainWindows;
	}

	// ------------------------------------------------------------
	// --- Menu Help ----------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "Help".
	 * 
	 * @return the j menu main help
	 */
	public JMenu getJMenuMainHelp() {
		if (jMenuMainHelp == null) {
			jMenuMainHelp = new JMenu("Hilfe");
			jMenuMainHelp.setText(Language.translate("Hilfe"));
			jMenuMainHelp.add(new CWMenueItem("HelpAbout", Language.translate("Über..."), null));
			jMenuMainHelp.add(new CWMenueItem("HelpChanges", Language.translate("Letzte Änderungen"), null));
			jMenuMainHelp.addSeparator();
			jMenuMainHelp.add(new CWMenueItem("HelpUpdate", Language.translate("Nach Update suchen") + " !", null));
			jMenuMainHelp.addSeparator();
			jMenuMainHelp.add(new CWMenueItem("EclipsePreferences", "Eclipse Preferences", null));
		}
		return jMenuMainHelp;
	}

	// ------------------------------------------------------------
	// --- Menu "Close-Button" ------------------------------------
	// ------------------------------------------------------------
	/**
	 * Gets the close button.
	 *
	 * @return the close button
	 */
	private JMenuItem getCloseButton() {
		if (jMenuCloseButton == null) {
			jMenuCloseButton = new CWMenueItem("ProjectClose", "", "MBclose.png");
			jMenuCloseButton.setText("");
			jMenuCloseButton.setToolTipText(Language.translate("Projekt schließen"));
			jMenuCloseButton.setBorder(null);
			jMenuCloseButton.setMargin(new Insets(0, 0, 0, 0));
			jMenuCloseButton.setPreferredSize(new Dimension(30, jMenuCloseButton.getHeight()));
			jMenuCloseButton.setIcon(iconCloseDummy);
		}
		return jMenuCloseButton;
	}

	/**
	 * Sets the close button position.
	 *
	 * @param setVisible the new close button position
	 */
	public void setCloseButtonPosition(boolean setVisible) {

		if (jMenuCloseButton.isVisible() == false) {
			jMenuCloseButton.setVisible(true);
		}
		if (setVisible == true) {
			jMenuCloseButton.setIcon(iconClose);
			jMenuCloseButton.setEnabled(true);
		} else {
			jMenuCloseButton.setIcon(iconCloseDummy);
			jMenuCloseButton.setEnabled(false);
		}

		if (jMenuBarMain != null) {
			jMenuBarMain.revalidate();
		}
	}

	// ------------------------------------------------------------
	// --- Sub class for simple menu items - START ----------------
	// ------------------------------------------------------------
	/** The Class CWMenueItem. */
	private class CWMenueItem extends JMenuItem implements ActionListener {

		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new cW menue item.
		 *
		 * @param actionCommand the action command
		 * @param Text the text
		 * @param imgName the img name
		 */
		private CWMenueItem(String actionCommand, String Text, String imgName) {

			this.setText(Text);
			if (imgName != null) {
				try {
					this.setIcon(GlobalInfo.getInternalImageIcon(imgName));
				} catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}
			}
			this.addActionListener(this);
			this.setActionCommand(actionCommand);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			String actionCMD = ae.getActionCommand();

			// --- Menu Projekt -------------------------------
			if (actionCMD.equalsIgnoreCase("ProjectNew")) {
				Application.getProjectsLoaded().add(true);

			} else if (actionCMD.equalsIgnoreCase("ProjectOpen")) {
				Application.getProjectsLoaded().add(false);

			} else if (actionCMD.equalsIgnoreCase("ProjectClose")) {
				Project currProject = Application.getProjectFocused();
				if (currProject != null)
					currProject.close();

			} else if (actionCMD.equalsIgnoreCase("ProjectSave")) {
				Project currProject = Application.getProjectFocused();
				if (currProject != null)
					currProject.save();

			} else if (actionCMD.equalsIgnoreCase("ProjectImport")) {
				Application.getProjectsLoaded().projectImport();

			} else if (actionCMD.equalsIgnoreCase("ProjectExport")) {
				Application.getProjectsLoaded().projectExport();

			} else if (actionCMD.equalsIgnoreCase("ProjectDelete")) {
				Application.getProjectsLoaded().projectDelete();

			} else if (actionCMD.equalsIgnoreCase("ApplicationQuit")) {
				Application.stop();

				// --- Menu Ansicht / View ------------------------
			} else if (actionCMD.equalsIgnoreCase("ViewConsole")) {
				Application.getMainWindow().doSwitchConsole();

				// --- Menu Jade ----------------------------------
			} else if (actionCMD.equalsIgnoreCase("JadeStart")) {
				Application.getJadePlatform().start();

			} else if (actionCMD.equalsIgnoreCase("JadeStop")) {
				Application.getJadePlatform().stop();

			} else if (actionCMD.equalsIgnoreCase("PopRMAStart")) {
				Application.getJadePlatform().startSystemAgent("rma", null);

			} else if (actionCMD.equalsIgnoreCase("PopSniffer")) {
				Application.getJadePlatform().startSystemAgent("sniffer", null);

			} else if (actionCMD.equalsIgnoreCase("PopDummy")) {
				Application.getJadePlatform().startSystemAgent("dummy", null);

			} else if (actionCMD.equalsIgnoreCase("PopDF")) {
				Application.getJadePlatform().startSystemAgent("DF", null);

			} else if (actionCMD.equalsIgnoreCase("PopIntrospec")) {
				Application.getJadePlatform().startSystemAgent("introspector", null);

			} else if (actionCMD.equalsIgnoreCase("PopLog")) {
				Application.getJadePlatform().startSystemAgent("log", null);

				// --- Menu Simulation ----------------------------
			} else if (actionCMD.equalsIgnoreCase("SimulationStart")) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
				Application.getJadePlatform().startSystemAgent("simstarter", null, startWith);

			} else if (actionCMD.equalsIgnoreCase("SimulationPause")) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Pause;
				Application.getJadePlatform().startSystemAgent("simstarter", null, startWith);

			} else if (actionCMD.equalsIgnoreCase("SimulationStop")) {
				Application.getJadePlatform().stop();

			} else if (actionCMD.equalsIgnoreCase("ContainerMonitoring")) {
				Application.getJadePlatform().startSystemAgent("loadMonitor", null);

			} else if (actionCMD.equalsIgnoreCase("ThreadMonitoring")) {
				Application.getJadePlatform().startSystemAgent("threadMonitor", null);

				// --- Menu Extras => NOT here !! ---------------
			} else if (actionCMD.equalsIgnoreCase("ExtraTranslation")) {
				Application.showTranslationDialog();

			} else if (actionCMD.equalsIgnoreCase("ExtraBenchmark")) {
				Application.doBenchmark(true);

			} else if (actionCMD.equalsIgnoreCase("ExtraOptions")) {
				Application.showOptionDialog();

			} else if (actionCMD.equalsIgnoreCase("Authentication")) {
				Application.showAuthenticationDialog();

				// --- Menu Hilfe ---------------------------------
			} else if (actionCMD.equalsIgnoreCase("HelpUpdate")) {
				new AgentGuiUpdater(true).start();

			} else if (actionCMD.equalsIgnoreCase("HelpChanges")) {
				Application.showChangeDialog();

			} else if (actionCMD.equalsIgnoreCase("HelpAbout")) {
				Application.showAboutDialog();

			} else if (actionCMD.equalsIgnoreCase("EclipsePreferences")) {
				Application.showEclipsePreferences();

			} else {
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actionCMD);
			}
		}

	}
	// ------------------------------------------------------------
	// --- Sub class for simple menu items - End ------------------
	// ------------------------------------------------------------
	// ------------------------------------------------------------
	// --- Menu definition - END ----------------------------------
	// ------------------------------------------------------------

	// ------------------------------------------------------------
	// --- Create Toolbar - START ---------------------------------
	// ------------------------------------------------------------
	/**
	 * Returns the main JToolBar of the application window.
	 * 
	 * @return the main JToolBar of the application window
	 */
	public JToolBar getJToolBarApplication() {

		if (jToolBarApplication == null) {

			// --- PopUp-Menu zum Button 'JadeTools' definieren (s. u.) ---
			jPopupMenuJadeTools = new JPopupMenu("SubBar");
			jPopupMenuJadeTools.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPopupMenuJadeTools.add(new CWMenueItem("PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) öffnen"), "MBJadeRMA.gif"));
			jPopupMenuJadeTools.add(new CWMenueItem("PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif"));
			jPopupMenuJadeTools.add(new CWMenueItem("PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif"));
			jPopupMenuJadeTools.add(new CWMenueItem("PopDF", Language.translate("DF anzeigen"), "MBJadeDF.gif"));
			jPopupMenuJadeTools.add(new CWMenueItem("PopIntrospec", Language.translate("Introspector-Agent starten"), "MBJadeIntrospector.gif"));
			jPopupMenuJadeTools.add(new CWMenueItem("PopLog", Language.translate("Log-Manager starten"), "MBJadeLogger.gif"));

			// --- Symbolleisten-Definition -------------------------------
			jToolBarApplication = new JToolBar("MainBar");
			jToolBarApplication.setFloatable(false);
			jToolBarApplication.setRollover(true);

			jToolBarApplication.add(new JToolBarButton("New", Language.translate("Neues Projekt"), null, "MBnew.png"));
			jToolBarApplication.add(new JToolBarButton("Open", Language.translate("Projekt öffnen"), null, "MBopen.png"));
			jToolBarApplication.add(new JToolBarButton("Save", Language.translate("Projekt speichern"), null, "MBsave.png"));
			jToolBarApplication.addSeparator();

			jToolBarApplication.add(new JToolBarButton("ViewConsole", Language.translate("Konsole ein- oder ausblenden"), null, "MBConsole.png"));
			jToolBarApplication.addSeparator();

			jToolBarApplication.add(new JToolBarButton("JadeStart", Language.translate("JADE starten"), null, "MBJadeOn.png"));
			jToolBarApplication.add(new JToolBarButton("JadeStop", Language.translate("JADE stoppen"), null, "MBJadeOff.png"));
			jToolBarApplication.addSeparator();

			jButtonJadeTools = new JToolBarButton("JadeTools", Language.translate("JADE-Tools..."), null, "MBJadeTools.png");
			jButtonJadeTools.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showJPopupMenuJadeTools();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					showJPopupMenuJadeTools();
				}
			});
			jToolBarApplication.add(jButtonJadeTools);
			jToolBarApplication.addSeparator();

			// --- Add Simulation Setup -----------------------------------
			this.getSetupSelectorToolbar();
			jToolBarApplication.addSeparator();

			// --- Simulation Buttons -----------
			jButtonSimStart = new JToolBarButton("SimulationStart", Language.translate("MAS-Start"), null, "MBLoadPlay.png");
			jToolBarApplication.add(jButtonSimStart);
			jButtonSimPause = new JToolBarButton("SimulationPause", Language.translate("MAS-Pause"), null, "MBLoadPause.png");
			jToolBarApplication.add(jButtonSimPause);
			jButtonSimStop = new JToolBarButton("SimulationStop", Language.translate("MAS-Stop"), null, "MBLoadStopRecord.png");
			jToolBarApplication.add(jButtonSimStop);
			jToolBarApplication.addSeparator();

			jToolBarApplication.add(new JToolBarButton("ContainerMonitoring", Language.translate("Auslastungs-Monitor öffnen"), null, "MBLoadMonitor.png"));
			jToolBarApplication.add(new JToolBarButton("ThreadMonitoring", Language.translate("Thread-Monitor öffnen"), null, "MBclock.png"));
			jToolBarApplication.addSeparator();

		}
		;
		return jToolBarApplication;
	}

	/**
	 * Shows the popup menu of the JADE tools.
	 */
	private void showJPopupMenuJadeTools() {
		this.jPopupMenuJadeTools.show(jButtonJadeTools, 0, jButtonJadeTools.getHeight());
	}

	/**
	 * Gets the setup selector toolbar.
	 * 
	 * @return the setup selector toolbar
	 */
	public SetupSelectorToolbar getSetupSelectorToolbar() {
		if (this.setupSelectorToolbar == null) {
			this.setupSelectorToolbar = new SetupSelectorToolbar(jToolBarApplication);
		}
		return this.setupSelectorToolbar;
	}

	/**
	 * Enable/Disables the SetupSelector in the toolbar.
	 * 
	 * @param enable the enable
	 */
	public void enableSetupSelector(boolean enable) {
		this.getSetupSelectorToolbar().setEnabled(enable);
	}

	/**
	 * This method can be used in order to add an individual menu button a specified index position of the toolbar.
	 *
	 * @param myComponent the my component
	 * @param indexPosition the index position
	 */
	public void addJToolbarComponent(JComponent myComponent, int indexPosition) {
		int nElements = jToolBarApplication.getComponentCount();
		if (indexPosition > (nElements - 1)) {
			this.addJToolbarComponent(myComponent);
		} else {
			jToolBarApplication.add(myComponent, indexPosition);
			this.validate();
		}
	}

	/**
	 * This method can be used in order to add an individual menu button to the toolbar.
	 *
	 * @param myComponent the my component
	 */
	public void addJToolbarComponent(JComponent myComponent) {
		this.getJToolBarApplication().add(myComponent);
		this.validate();
	}

	/**
	 * This methode removes a menu button from the toolbar.
	 * 
	 * @param myComponent the my component
	 */
	public void removeJToolbarComponent(JComponent myComponent) {
		this.getJToolBarApplication().remove(myComponent);
		this.validate();
	}
	// ------------------------------------------------------------
	// --- Create Toolbar - END -----------------------------------
	// ------------------------------------------------------------

	// ------------------------------------------------------------
	// --- Sub class for toolbar buttons --------------------------
	// ------------------------------------------------------------
	/**
	 * The Class JToolBarButton.
	 */
	public class JToolBarButton extends JButton implements ActionListener {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new j tool bar button.
		 *
		 * @param actionCommand the action command
		 * @param toolTipText the tool tip text
		 * @param altText the alt text
		 * @param imgName the img name
		 */
		private JToolBarButton(String actionCommand, String toolTipText, String altText, String imgName) {

			this.setText(altText);
			this.setToolTipText(toolTipText);
			this.setSize(36, 36);

			if (imgName != null) {
				this.setPreferredSize(new Dimension(26, 26));
			} else {
				this.setPreferredSize(null);
			}

			if (imgName != null) {
				try {
					this.setIcon(GlobalInfo.getInternalImageIcon(imgName));
				} catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}
			}
			this.addActionListener(this);
			this.setActionCommand(actionCommand);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			// --- Fallunterscheidung 'cmd' einbauen ---
			String ActCMD = ae.getActionCommand();
			// ------------------------------------------------
			if (ActCMD.equalsIgnoreCase("New")) {
				Application.getProjectsLoaded().add(true);

			} else if (ActCMD.equalsIgnoreCase("Open")) {
				Application.getProjectsLoaded().add(false);

			} else if (ActCMD.equalsIgnoreCase("Save")) {
				Project CurPro = Application.getProjectFocused();
				if (CurPro != null)
					CurPro.save();

				// ------------------------------------------------
			} else if (ActCMD.equalsIgnoreCase("ViewConsole")) {
				Application.getMainWindow().doSwitchConsole();

				// ------------------------------------------------
			} else if (ActCMD.equalsIgnoreCase("JadeStart")) {
				Application.getJadePlatform().start();

			} else if (ActCMD.equalsIgnoreCase("JadeStop")) {
				Application.getJadePlatform().stop();

			} else if (ActCMD.equalsIgnoreCase("JadeTools")) {
				showJPopupMenuJadeTools();

			} else if (ActCMD.equalsIgnoreCase("ContainerMonitoring")) {
				Application.getJadePlatform().startSystemAgent("loadMonitor", null);
			} else if (ActCMD.equalsIgnoreCase("ThreadMonitoring")) {
				Application.getJadePlatform().startSystemAgent("threadMonitor", null);

				// ------------------------------------------------
			} else if (ActCMD.equalsIgnoreCase("SimulationStart")) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
				Application.getJadePlatform().startSystemAgent("simstarter", null, startWith);

			} else if (ActCMD.equalsIgnoreCase("SimulationPause")) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Pause;
				Application.getJadePlatform().startSystemAgent("simstarter", null, startWith);

			} else if (ActCMD.equalsIgnoreCase("SimulationStop")) {
				Application.getJadePlatform().stop();

				// ------------------------------------------------
			} else {
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
			}
			;

		};
	};

	// -------------------------------------------------------------------
	// --- Methods to control the Buttons for the simulation control -----
	// -------------------------------------------------------------------
	/**
	 * Sets the enable sim start.
	 * 
	 * @param enable the new enable sim start
	 */
	public void setEnableSimStart(boolean enable) {
		jButtonSimStart.setEnabled(enable);
		jMenuItemSimStart.setEnabled(enable);
	}

	/**
	 * Sets the enable sim pause.
	 * 
	 * @param enable the new enable sim pause
	 */
	public void setEnableSimPause(boolean enable) {
		jButtonSimPause.setEnabled(enable);
		jMenuItemSimPause.setEnabled(enable);
	}

	/**
	 * Sets the enable sim stop.
	 * 
	 * @param enable the new enable sim stop
	 */
	public void setEnableSimStop(boolean enable) {
		jButtonSimStop.setEnabled(enable);
		jMenuItemSimStop.setEnabled(enable);
	}

	/**
	 * Checks if is enabled sim start.
	 * 
	 * @return true, if is enabled sim start
	 */
	public boolean isEnabledSimStart() {
		return jButtonSimStart.isEnabled();
	}

	/**
	 * Checks if is enabled sim pause.
	 * 
	 * @return true, if is enabled sim pause
	 */
	public boolean isEnabledSimPause() {
		return jButtonSimPause.isEnabled();
	}

	/**
	 * Checks if is enabled sim stop.
	 * 
	 * @return true, if is enabled sim stop
	 */
	public boolean isEnabledSimStop() {
		return jButtonSimStop.isEnabled();
	}

	/**
	 * Sets the simulation ready2 start.
	 */
	public void setSimulationReady2Start() {
		this.setEnableSimStart(true);
		this.setEnableSimPause(false);
		this.setEnableSimStop(false);
		if (Application.getProjectFocused() != null) {
			this.enableSetupSelector(true);
		}
	}
	// -------------------------------------------------------------------
	// -------------------------------------------------------------------

	/**
	 * The Class AngledLinesWindowsCornerIcon.
	 */
	private class AngledLinesWindowsCornerIcon implements Icon {

		private final Color WHITE_LINE_COLOR = new Color(255, 255, 255);
		private final Color GRAY_LINE_COLOR = new Color(172, 168, 153);

		private static final int WIDTH = 13;
		private static final int HEIGHT = 13;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#getIconHeight()
		 */
		public int getIconHeight() {
			return WIDTH;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#getIconWidth()
		 */
		public int getIconWidth() {
			return HEIGHT;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
		 */
		public void paintIcon(Component c, Graphics g, int x, int y) {

			g.setColor(WHITE_LINE_COLOR);
			g.drawLine(0, 12, 12, 0);
			g.drawLine(5, 12, 12, 5);
			g.drawLine(10, 12, 12, 10);

			g.setColor(GRAY_LINE_COLOR);
			g.drawLine(1, 12, 12, 1);
			g.drawLine(2, 12, 12, 2);
			g.drawLine(3, 12, 12, 3);

			g.drawLine(6, 12, 12, 6);
			g.drawLine(7, 12, 12, 7);
			g.drawLine(8, 12, 12, 8);

			g.drawLine(11, 12, 12, 11);
			g.drawLine(12, 12, 12, 12);

		}
	}

} // -- End Class ---
