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

import jade.debugging.components.JPanelConsole;
import jade.debugging.components.JTabbedPane4Consoles;
import jade.debugging.components.SysOutBoard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.simulationService.agents.LoadExecutionAgent;

/**
 * This class represents the main user-interface of the application AgentGUI.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class CoreWindow extends JFrame implements ComponentListener {

	private static final long serialVersionUID = 1L;

	private final String pathImage = Application.RunInfo.PathImageIntern();
	
	private final ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource( pathImage + "AgentGUI.png") );
	private final Image imageAgentGUI = iconAgentGUI.getImage();
	
	private final ImageIcon iconGreen = new ImageIcon( this.getClass().getResource( pathImage + "StatGreen.png") );
	private final ImageIcon iconRed = new ImageIcon( this.getClass().getResource( pathImage + "StatRed.png") );
	private final ImageIcon iconClose = new ImageIcon( this.getClass().getResource( pathImage + "MBclose.png") );
	private final ImageIcon iconCloseDummy = new ImageIcon( this.getClass().getResource( pathImage + "MBdummy.png") );
	
	private static JLabel statusBar;	
	private JLabel statusJade;
	
	private JSplitPane jSplitPane4ProjectDesktop;
	private JDesktopPane jDesktopPane4Projects;	
	private JTabbedPane4Consoles jTabbedPane4Console;
	private JPanelConsole jPanelConsoleLocal = Application.Console;;
	private int oldDividerLocation;
	
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
	public JMenu jMenuMainWindows;
	private JMenu jMenuMainHelp;

	private JMenuItem jMenuCloseButton;
	
	private JToolBar jToolBarApplication;
		private JButton JadeTools;	
		private JPopupMenu JadeToolsPopUp;
		
		private JButton jButtonSimStart;
		private JButton jButtonSimPause;
		private JButton jButtonSimStop;

	
	// ------------------------------------------------------------		
	// --- Start -------------------------------------------------- 
	// ------------------------------------------------------------
	/**
	 * Constructor of this class.
	 */
	public CoreWindow() {
		
		// --- Set the IconImage ----------------------------------
		this.setIconImage(imageAgentGUI);
		
		// --- Set the Look and Feel of the Application -----------
		if ( Application.RunInfo.getAppLnF() != null ) {
			setLookAndFeel( Application.RunInfo.getAppLnF() );
		}
		
		// --- Create the Main-Elements of the Application --------
		this.initComponents();
		
		this.setDefaultCloseOperation(CoreWindow.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setPreferredSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.pack();		
		
		//this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setTitelAddition("");
		this.setCloseButtonPosition(false);
		
		// --- configure console ---------------------------------- 
		oldDividerLocation = jSplitPane4ProjectDesktop.getHeight() * 3 / 4; 
		jSplitPane4ProjectDesktop.setDividerLocation(oldDividerLocation);
		this.setConsoleVisible(false);
		
		// --- Set the JTabbedPan for remote console output -----------
		SysOutBoard.setJTabbedPane4Consoles(this.getJTabbedPane4Console());
		
	}
	// ------------------------------------------------------------	

	
	// ------------------------------------------------------------
	// --- Initialisierung des Fensters - START -------------------
	// ------------------------------------------------------------
	/**
	 * Inits the components.
	 */
	private void initComponents() {

		// --- Standardeinstellungen ---
		this.setJMenuBar(this.getJMenuBarBase());
		
		this.add( getJToolBarApplication(), BorderLayout.NORTH );
		this.add( getStatusBar(), BorderLayout.SOUTH );
		this.add( getMainSplitpane() );
		this.setSize(1150, 640);	
		
		// --- Listener f�r das Schlie�en der Applikation ----
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
			Application.quit();
			}
		});
		this.addComponentListener(this);
		
		// --- Schaltfl�chen f�r die Simulationskontrolle einstellen --
		this.setSimulationReady2Start();
		
	}	
	// ------------------------------------------------------------
	// --- Initialisierung des Fensters - ENDE --------------------
	// ------------------------------------------------------------

	
	// ------------------------------------------------------------
	// --- Statusanzeigen etc. definieren - START -----------------
	// ------------------------------------------------------------
	/**
	 * Gets the status bar.
	 *
	 * @return the status bar
	 */
	private JPanel getStatusBar() {
	    
		// --- Linker Teil ----------------------
		statusBar = new JLabel();			
		statusBar.setPreferredSize( new Dimension(300, 16) );
		statusBar.setFont(new Font( "Dialog", Font.PLAIN, 12) );
		
		// --- Mittlerer Teil -------------------
		statusJade = new JLabel();
		statusJade.setPreferredSize( new Dimension(200, 16) );
		statusJade.setFont(new Font( "Dialog", Font.PLAIN, 12) );
		statusJade.setHorizontalAlignment( SwingConstants.RIGHT );
		setStatusJadeRunning( false );
		
	    // --- Rechter Teil --------------------- 
	    JPanel RightPart = new JPanel( new BorderLayout() );
	    RightPart.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
	    RightPart.setOpaque( false );
	    		
	    // --- StatusBar zusammenbauen ------------------
		JPanel JPStat = new JPanel( new BorderLayout() );
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
		if ( message == null ) {
			statusBar.setText("  ");
		} else {
			statusBar.setText("  " + message);
		};	
	}
	
	/**
	 * This method is used if a project is open. Then the project name is displayed
	 * behind the applications title (e.g. 'Agent.GUI: project name'
	 *
	 * @param add2BasicTitel the new titel addition
	 */
	public void setTitelAddition(String add2BasicTitel) {
		if ( add2BasicTitel != "" ) {
			this.setTitle( Application.RunInfo.getApplicationTitle() + ": " + add2BasicTitel );	
		} else {
			this.setTitle( Application.RunInfo.getApplicationTitle() );
		}
	}
	
	/**
	 * Sets the indicator in order to visul inform that JADE is running or not
	 * (red or green button in the right corner of the status bar + text).
	 *
	 * @param isRunning the new status jade running
	 */
	public void setStatusJadeRunning(boolean isRunning) {
		if ( isRunning == false ) { 
			statusJade.setText( Language.translate("JADE wurde noch nicht gestartet.") );
			statusJade.setIcon(iconRed);			
		} else {
			statusJade.setText( Language.translate("JADE wurde lokal gestartet.") );
			statusJade.setIcon(iconGreen);
		};		
	}
	
	/**
	 * Here the 'look and feel' LnF of java Swing can be set.
	 *
	 * @param newLnF the new look and feel
	 */
	public void setLookAndFeel(String newLnF) {
		// --- Look and fell einstellen --------------- 
		if ( newLnF == null ) return;		
		Application.RunInfo.setAppLnf(newLnF);
		try {
			String lnfClassname = Application.RunInfo.getAppLnF();
			if (lnfClassname == null) {
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lnfClassname);
			SwingUtilities.updateComponentTreeUI(this);
			
		} catch (Exception e) {
				System.err.println("Cannot install " + Application.RunInfo.getAppLnF()
					+ " on this platform:" + e.getMessage());
		}
		if ( jMenuExtraLnF != null ){
			jMenuExtraLnF.removeAll();
			setjMenuExtraLnF();
		}
		if ( jMenuExtraLang != null ) {
			jMenuExtraLang.removeAll();
			setjMenuExtraLang();
		}
		if (Application.ProjectCurr != null) {
			Application.ProjectCurr.setMaximized();
		}
	}		
	
	/**
	 * Console is visible.
	 *
	 * @return true, if successful
	 */
	public boolean consoleIsVisible() {
		// --- Umschalten der Consolen-Ansicht --------------------
		if (jSplitPane4ProjectDesktop.getDividerSize()==0) {
			return false;
		} else {
			return true;
		}		
	}
	
	/**
	 * Do switch console.
	 */
	private void doSwitchConsole() {
		// --- Umschalten der Consolen-Ansicht --------------------
		if (jSplitPane4ProjectDesktop.getDividerSize()>0) {
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
		// --- Ein- und ausblenden der Console --------------------
		if (show == true) {
			// --- System.out.println("Console einblenden ...");
			jSplitPane4ProjectDesktop.setBottomComponent(this.getJTabbedPane4Console());
			jSplitPane4ProjectDesktop.setDividerLocation(oldDividerLocation);
			jSplitPane4ProjectDesktop.setDividerSize(10);
			
		} else {
			// --- System.out.println("Console ausblenden ...");			
			oldDividerLocation = jSplitPane4ProjectDesktop.getDividerLocation(); 
			jSplitPane4ProjectDesktop.setDividerSize(0);
			jSplitPane4ProjectDesktop.setDividerLocation(jSplitPane4ProjectDesktop.getHeight());			
			jSplitPane4ProjectDesktop.remove(this.getJTabbedPane4Console());
		}
		this.validate();
		if ( Application.Projects.count() != 0 ) {
			Application.ProjectCurr.setMaximized();
		}
	}
	
	/**
	 * This method sets back the focus to this JFrame.
	 */
	public void restoreFocus() {
		if ( this.getExtendedState()==Frame.ICONIFIED || this.getExtendedState()==Frame.ICONIFIED+Frame.MAXIMIZED_BOTH) {
			this.setState(Frame.NORMAL);
		} 
		this.setAlwaysOnTop(true);
		this.setAlwaysOnTop(false);		
	}
	// ------------------------------------------------------------
	// --- Statusanzeigen etc. definieren - ENDE ------------------
	// ------------------------------------------------------------
	
	
	// ------------------------------------------------------------
	// --- Desktop der Anwendung definieren - START ---------------
	// ------------------------------------------------------------
	/**
	 * Gets the main splitpane.
	 *
	 * @return the main splitpane
	 */
	private JSplitPane getMainSplitpane() {
		if (jSplitPane4ProjectDesktop == null ) {
			
			jSplitPane4ProjectDesktop = new JSplitPane();
			jSplitPane4ProjectDesktop.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane4ProjectDesktop.setDividerSize(10);
			jSplitPane4ProjectDesktop.setResizeWeight(1);
			jSplitPane4ProjectDesktop.setOneTouchExpandable(true);
			jSplitPane4ProjectDesktop.setTopComponent(getJDesktopPane4Projects());			
			//jSplitPane4ProjectDesktop.setBottomComponent(getJTabbedPane4Console());
			jSplitPane4ProjectDesktop.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent EventSource) {
					// --- Deviderpositionierung abfangen ---
					if (EventSource.getPropertyName() == "lastDividerLocation" ) {
						if ( Application.Projects.count() != 0 ) {
							Application.ProjectCurr.setMaximized();
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
		
		if (jTabbedPane4Console==null) {
			// --- Get the TabPane for Console-Tabs ---------------
			jTabbedPane4Console = new JTabbedPane4Consoles();
			jTabbedPane4Console.add(Language.translate("Lokal"), this.jPanelConsoleLocal);
		}
		return jTabbedPane4Console;
	}
	
	/**
	 * This method returns the JDesktopPane, where the
	 * JInternalFrame's of the project will be placed.
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
	// --- Men�leistendefinition - START --------------------------
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
			Insets insets = new Insets( 0, 0, 0, 0 );
			jMenuBarBase.add( this.getJMenuBarMain(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, insets, 0, 0 ));
			jMenuBarBase.add( this.getCloseButton() , new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, insets, 0, 0 ));
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
			
			jMenuBarMain.add( getJMenuMainProject() );
			jMenuBarMain.add( getJMenuMainView() );
			jMenuBarMain.add( getjMenuMainJade() );
			jMenuBarMain.add( getjMenuMainSimulation() );
			jMenuBarMain.add( getjMenuMainExtra() );
			jMenuBarMain.add( getjMenuMainWindow() );
			jMenuBarMain.add( getjMenuMainHelp() );	

		}
		return jMenuBarMain;
	}

	/**
	 * This method can be used in order to add an individual menu
	 * at a specified index position of the menu bar.
	 *
	 * @param myMenu the my menu
	 * @param indexPosition the index position
	 */
	public void addJMenu(JMenu myMenu, int indexPosition) {
		int nElements = jMenuBarMain.getSubElements().length; 
		if (indexPosition > (nElements-1)) {
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
	 * This method can be used in order to add an individual JMmenuItem
	 * at a specified index position of the given menu.
	 *
	 * @param menu2add the menu2add
	 * @param myMenuItemComponent the my menu item component
	 * @param indexPosition the index position
	 */
	public void addJMenuItemComponent(JMenu menu2add, JComponent myMenuItemComponent, int indexPosition) {
		int nElements = menu2add.getItemCount(); 
		if (indexPosition > (nElements-1)) {
			this.addJMenuItemComponent(menu2add, myMenuItemComponent);
		} else {
			menu2add.add(myMenuItemComponent, indexPosition);
			this.validate();
		}
	}
	
	/**
	 * This method can be used in order to add an
	 * individual JMmenuItem to the given menu.
	 *
	 * @param menu2add the menu2add
	 * @param myMenuItemComponent the my menu item component
	 */
	public void addJMenuItemComponent(JMenu menu2add, JComponent myMenuItemComponent) {
		menu2add.add(myMenuItemComponent);
		this.validate();
	}
	// ------------------------------------------------------------
	// --- Men� Projekte ------------------------------------------
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
			jMenuMainProject.add( new CWMenueItem( "ProjectNew", Language.translate("Neues Projekt"), "MBnew.png" )) ;
			jMenuMainProject.add( new CWMenueItem( "ProjectOpen", Language.translate("Projekt �ffnen"), "MBopen.png" )) ;
			jMenuMainProject.add( new CWMenueItem( "ProjectClose", Language.translate("Projekt schlie�en"), "MBclose.png" )) ;
			jMenuMainProject.addSeparator();
			jMenuMainProject.add( new CWMenueItem( "ProjectSave", Language.translate("Projekt speichern"), "MBsave.png" )) ;
			jMenuMainProject.addSeparator();
			jMenuMainProject.add( new CWMenueItem( "ProjectImport", Language.translate("Projekt importieren"), "MBtransImport.png" )) ;
			jMenuMainProject.add( new CWMenueItem( "ProjectExport", Language.translate("Projekt exportieren"), "MBtransExport.png" )) ;
			jMenuMainProject.addSeparator();
			jMenuMainProject.add( new CWMenueItem( "ApplicationQuit", Language.translate("Beenden"), null )) ;			
		}
		return jMenuMainProject;
	}
	// ------------------------------------------------------------
	// --- Men� "View" --------------------------------------------
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
					if (actuator==viewDeveloper) {
						if (Application.ProjectCurr.getProjectView().equalsIgnoreCase("Developer")==false) {
							Application.ProjectCurr.setProjectView(Project.VIEW_Developer);
						}
						
					} else if (actuator==viewEndUser) {
						if (Application.ProjectCurr.getProjectView().equalsIgnoreCase("User")==false) {
							Application.ProjectCurr.setProjectView(Project.VIEW_User);
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
			jMenuMainView.add( new CWMenueItem( "ViewConsole", Language.translate("Konsole ein- oder ausblenden"), "MBConsole.png" )) ;			
		}
		return jMenuMainView;
	}
	// ------------------------------------------------------------
	// --- Men� "JADE" --------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "JADE".
	 *
	 * @return the j menu main jade
	 */
	public JMenu getjMenuMainJade() {
		if (jMenuMainJade == null) {
			jMenuMainJade = new JMenu("JADE");
			jMenuMainJade.setText(Language.translate("JADE"));			
			jMenuMainJade.add( new CWMenueItem( "JadeStart", Language.translate("JADE starten"), "MBJadeOn.png" )) ;
			jMenuMainJade.add( new CWMenueItem( "JadeStop", Language.translate("JADE stoppen"), "MBJadeOff.png" )) ;
			jMenuMainJade.addSeparator();
			jMenuMainJade.add( new CWMenueItem( "PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) �ffnen"), "MBJadeRMA.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopDF", Language.translate("DF anzeigen"), "MBJadeDF.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopIntrospec", Language.translate("Introspector-Agent starten"), "MBJadeIntrospector.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopLog", Language.translate("Log-Manager starten"), "MBJadeLogger.gif" )) ;
			jMenuMainJade.addSeparator();
			jMenuMainJade.add( new CWMenueItem( "ContainerMonitoring", Language.translate("Auslastungs-Monitor �ffnen"), "MBLoadMonitor.png" ));
		}
		return jMenuMainJade;
	}
	// ------------------------------------------------------------
	// --- Men� Simulation ----------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "Project".
	 *
	 * @return the j menu main simulation
	 */
	public JMenu getjMenuMainSimulation() {
		if (jMenuMainSimulation == null) {
			jMenuMainSimulation = new JMenu("MAS");
			jMenuMainSimulation.setText(Language.translate("MAS"));			
			
			jMenuItemSimStart = new CWMenueItem( "SimulationStart", Language.translate("Start"), "MBLoadPlay.png");
			jMenuMainSimulation.add(jMenuItemSimStart);
			jMenuItemSimPause = new CWMenueItem( "SimulationPause", Language.translate("Pause"), "MBLoadPause.png");
			jMenuMainSimulation.add(jMenuItemSimPause);
			jMenuItemSimStop = new CWMenueItem( "SimulationStop", Language.translate("Stop"), "MBLoadStopRecord.png");
			jMenuMainSimulation.add(jMenuItemSimStop);
		}
		return jMenuMainSimulation;
	}
	
	
	// ------------------------------------------------------------
	// --- Men� Extras ---------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "Extra".
	 *
	 * @return the j menu main extra
	 */
	public JMenu getjMenuMainExtra() {
		if (jMenuExtra == null) {
			jMenuExtra = new JMenu("Extras");
			jMenuExtra.setText(Language.translate("Extras"));

			// --- Menue 'Sprache' ---
			jMenuExtraLang = new JMenu();
			jMenuExtraLang.setText(Language.translate("Sprache"));
			setjMenuExtraLang();			
			jMenuExtra.add( jMenuExtraLang );

			// --- Menue 'LnF' -------
			jMenuExtraLnF = new JMenu();
			jMenuExtraLnF.setText("Look and Feel");
			setjMenuExtraLnF();
			jMenuExtra.add( jMenuExtraLnF );
			
			jMenuExtra.addSeparator();
			jMenuExtra.add( new CWMenueItem( "ExtraBenchmark", "SciMark 2.0 - Benchmark", null ));

			jMenuExtra.addSeparator();
			jMenuExtra.add( new CWMenueItem( "ExtraOptions", Language.translate("Optionen"), null ));
			
			}
		return jMenuExtra;
	}
		// ------------------------------------------------------------
		// --- Sprache ------------------------------------------------
		// ------------------------------------------------------------
		/**
		 * Setj menu extra lang.
		 */
		private void setjMenuExtraLang() {
			
			// --- Anzeige der Sprachen -------------------
			String[] languageList = Language.getLanguages(true); 
			boolean setBold = false;
			for(int i=0; i<languageList.length; i++) {
				if ( Language.isCurrentLanguage(languageList[i])) 
					setBold = true;
				else 
					setBold = false;					
				jMenuExtraLang.add( new JMenuItemLang(languageList[i], setBold) );
			};

			// --- Men�punkt �bersetzung --------------
			jMenuExtraLang.addSeparator();
			jMenuExtraLang.add(new CWMenueItem("ExtraTranslation", Language.translate("�bersetzen ..."), null));
		
		}
		// --- Unterklasse f�r die verf�gbaren Sprachen  --------------
		/**
		 * The Class JMenuItemLang.
		 */
		private class JMenuItemLang extends JMenuItem implements ActionListener {
			 
			/** The Constant serialVersionUID. */
			private static final long serialVersionUID = 1L;
			
			/**
			 * Instantiates a new j menu item lang.
			 *
			 * @param LangHeader the lang header
			 * @param setBold the set bold
			 */
			private JMenuItemLang( String LangHeader, boolean setBold ) {
				this.setText( Language.getLanguageName( LangHeader.toUpperCase() ) );			
				if ( setBold ) {
					Font cfont = this.getFont();
					if ( cfont.isBold() ) {
						this.setForeground( Application.RunInfo.ColorMenuHighLight() );	
					}
					else {
						this.setFont( cfont.deriveFont(Font.BOLD) );
					}
				}
				this.addActionListener(this);
				this.setActionCommand( LangHeader );
			}
			
			/* (non-Javadoc)
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
		 * Setj menu extra ln f.
		 */
		private void setjMenuExtraLnF() {

			boolean setBold = false;
			UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();
			
			for (int i = 0, n = plaf.length; i < n; i++) {
				if ( plaf[i].getClassName() == Application.RunInfo.getAppLnF() )
					setBold = true;
				else
					setBold = false;
					jMenuExtraLnF.add( new JMenuItmenLnF(plaf[i].getName(), plaf[i].getClassName(), setBold) );
			    };			
		}
		// --- Unterklasse f�r die Look and Feel Men�-Elemente --------
		/**
		 * The Class JMenuItmenLnF.
		 */
		private class JMenuItmenLnF extends JMenuItem  {
	 
			/** The Constant serialVersionUID. */
			private static final long serialVersionUID = 1L;
			
			/** The Ln f path. */
			private String LnFPath; 
			
			/**
			 * Instantiates a new j menu itmen ln f.
			 *
			 * @param LnFName the ln f name
			 * @param LnFClass the ln f class
			 * @param setBold the set bold
			 */
			private JMenuItmenLnF( String LnFName, String LnFClass, boolean setBold  ) {
				LnFPath = LnFClass;	
				this.setText( LnFName );
				if ( setBold ) {
					Font cfont = this.getFont();
					if ( cfont.isBold() ) {
						this.setForeground( Application.RunInfo.ColorMenuHighLight() );	
					}
					else {
						this.setFont( cfont.deriveFont(Font.BOLD) );
					}
				}
				this.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						Application.setLookAndFeel( LnFPath );							
					}
				});		
			}
		}
		// ------------------------------------------------------------	
		// ------------------------------------------------------------	
	
	// ------------------------------------------------------------
	// --- Men� Fenster -------------------------------------------
	// ------------------------------------------------------------
	/**
		 * Gets the j menu main window.
		 *
		 * @return the j menu main window
		 */
		private JMenu getjMenuMainWindow() {
		if (jMenuMainWindows == null) {
			jMenuMainWindows = new JMenu("Fenster");
			jMenuMainWindows.setText(Language.translate("Fenster"));
		}
		return jMenuMainWindows;
	}
	
	// ------------------------------------------------------------
	// --- Men� Hilfe ---------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "Help".
	 *
	 * @return the j menu main help
	 */
	public JMenu getjMenuMainHelp() {
		if (jMenuMainHelp == null) {
			jMenuMainHelp = new JMenu("Hilfe");
			jMenuMainHelp.setText(Language.translate("Hilfe"));
			jMenuMainHelp.add( new CWMenueItem( "HelpAbout", Language.translate("�ber..."), null )) ;
		}
		return jMenuMainHelp;
	}

	// ------------------------------------------------------------
	// --- Men� "Close-Button" ------------------------------------
	// ------------------------------------------------------------
	/**
	 * Gets the close button.
	 *
	 * @return the close button
	 */
	private JMenuItem getCloseButton() {
		if (jMenuCloseButton == null ) {
			jMenuCloseButton = new CWMenueItem( "ProjectClose", "", "MBclose.png" );
			jMenuCloseButton.setText("");
			jMenuCloseButton.setToolTipText( Language.translate("Projekt schlie�en") );
			jMenuCloseButton.setBorder(null);
			jMenuCloseButton.setMargin( new Insets(0, 0, 0, 0) );
			jMenuCloseButton.setPreferredSize( new Dimension ( 30 , jMenuCloseButton.getHeight() ) );
			jMenuCloseButton.setIcon( iconCloseDummy );
//			jMenuCloseButton.setEnabled( false );
//			jMenuCloseButton.setVisible( false );			
		}
		return jMenuCloseButton ;
	}
	
	/**
	 * Sets the close button position.
	 *
	 * @param setVisible the new close button position
	 */
	public void setCloseButtonPosition(boolean setVisible){
		
		// --- Positionsmerker f�r das Fenster setzen ?  ----------
		// --- Wird nach der Initialisierung ausgewertet ----------
		if ( jMenuCloseButton.isVisible() == false ) {
			jMenuCloseButton.setVisible(true);
		}
		// --- Entsprechendes Icon einblenden --------------------- 
		if ( setVisible == true ){
			jMenuCloseButton.setIcon( iconClose );	
			jMenuCloseButton.setEnabled( true );	
		}
		else {
			jMenuCloseButton.setIcon( iconCloseDummy );
			jMenuCloseButton.setEnabled( false );
		}		
		
		// --- Men�leiste neu zeichnen / �nderungen anzeigen ------
		if ( jMenuBarMain != null ) {
			jMenuBarMain.revalidate();
		}						
	}	
	
	
	// ------------------------------------------------------------
	// --- Unterklasse f�r alle einfachen Men�elemente - START ----
	// ------------------------------------------------------------	
	/**
	 * The Class CWMenueItem.
	 */
	private class CWMenueItem extends JMenuItem implements ActionListener {
		
		/** Creat's a JMenueItem for PopUp- or normal Menue's and holds the ActionListener for them. */
		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new cW menue item.
		 *
		 * @param actionCommand the action command
		 * @param Text the text
		 * @param imgName the img name
		 */
		private CWMenueItem( String actionCommand, 
							 String Text, 
							 String imgName ) {

			this.setText(Text);
			if ( imgName != null ) {
				try {
					this.setIcon( new ImageIcon( this.getClass().getResource( pathImage + imgName ) ) );
				}
				catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}				
			}
			this.addActionListener(this);
			this.setActionCommand(actionCommand);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			String ActCMD = ae.getActionCommand();	
	
			// --- Men� Projekt -------------------------------
			if ( ActCMD.equalsIgnoreCase("ProjectNew") ) {
				Application.Projects.add( true );
			}
			else if ( ActCMD.equalsIgnoreCase("ProjectOpen") ) {
				Application.Projects.add( false );
			}
			else if ( ActCMD.equalsIgnoreCase("ProjectClose") ) {
				Project CurPro = Application.ProjectCurr;
				if ( CurPro != null ) CurPro.close();				
			}
			else if ( ActCMD.equalsIgnoreCase("ProjectSave") ) {
				Project CurPro = Application.ProjectCurr;
				if ( CurPro != null ) CurPro.save();
			}
			else if ( ActCMD.equalsIgnoreCase("ProjectImport") ) {
				Application.Projects.projectImport();
			}
			else if ( ActCMD.equalsIgnoreCase("ProjectExport") ) {
				Application.Projects.projectExport();
			}
			else if ( ActCMD.equalsIgnoreCase("ApplicationQuit") ) {
				Application.quit();
			}
			// --- Men� Ansicht / View ------------------------
			else if ( ActCMD.equalsIgnoreCase("ViewConsole") ) {
				Application.MainWindow.doSwitchConsole();
			}
			// --- Men� Jade ----------------------------------
			else if ( ActCMD.equalsIgnoreCase("JadeStart") ) {
				Application.JadePlatform.jadeStart();
			}
			else if ( ActCMD.equalsIgnoreCase("JadeStop") ) {
				Application.JadePlatform.jadeStop();
			}
			else if ( ActCMD.equalsIgnoreCase("PopRMAStart") ) {
				Application.JadePlatform.jadeSystemAgentOpen("rma", null);
			}
			else if ( ActCMD.equalsIgnoreCase("PopSniffer") ) {
				Application.JadePlatform.jadeSystemAgentOpen("sniffer", null);
			}
			else if ( ActCMD.equalsIgnoreCase("PopDummy") ) {
				Application.JadePlatform.jadeSystemAgentOpen("dummy", null);
			}
			else if ( ActCMD.equalsIgnoreCase("PopDF") ) {
				Application.JadePlatform.jadeSystemAgentOpen("DF", null);
			}
			else if ( ActCMD.equalsIgnoreCase("PopIntrospec") ) {
				Application.JadePlatform.jadeSystemAgentOpen("introspector", null);
			}
			else if ( ActCMD.equalsIgnoreCase("PopLog") ) {
				Application.JadePlatform.jadeSystemAgentOpen("log", null);
			}
			// --- Men� Simulation ----------------------------
			else if ( ActCMD.equalsIgnoreCase("SimulationStart") ) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
				Application.JadePlatform.jadeSystemAgentOpen("simstarter", null, startWith);
			}
			else if ( ActCMD.equalsIgnoreCase("SimulationPause") ) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Pause;
				Application.JadePlatform.jadeSystemAgentOpen("simstarter", null, startWith);
			}
			else if ( ActCMD.equalsIgnoreCase("SimulationStop") ) {
				Application.JadePlatform.jadeStop();
			}

			else if ( ActCMD.equalsIgnoreCase("ContainerMonitoring") ) { 
				Application.JadePlatform.jadeSystemAgentOpen("loadMonitor", null);
			}
			// --- Men� Extras => nicht hier !! ---------------
			else if ( ActCMD.equalsIgnoreCase("ExtraTranslation") ) {
				Application.showTranslationDialog();
			}
			else if ( ActCMD.equalsIgnoreCase("ExtraBenchmark") ) {
				Application.doBenchmark(true);
			}			
			else if ( ActCMD.equalsIgnoreCase("ExtraOptions") ) {
				Application.showOptionDialog();
			}
			// --- Men� Hilfe ---------------------------------
			else if ( ActCMD.equalsIgnoreCase("HelpAbout") ) {
				Application.showAboutDialog();
			}
			else {
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
			}			
		}		
	}		
	// ------------------------------------------------------------
	// --- Unterklasse f�r alle einfachen Men�elemente - ENDE -----
	// ------------------------------------------------------------			
	// ------------------------------------------------------------
	// --- Men�leistendefinition - ENDE ---------------------------
	// ------------------------------------------------------------
		
		
	// ------------------------------------------------------------
	// --- Symbolleiste erstellen - START -------------------------
	// ------------------------------------------------------------
	/**
	 * This method will return the current instance of the applications tool bar.
	 *
	 * @return the j tool bar application
	 */
	private JToolBar getJToolBarApplication() {

		if ( jToolBarApplication == null) {
			
			// --- PopUp-Men� zum Button 'JadeTools' definieren (s. u.) ---
			JadeToolsPopUp = new JPopupMenu("SubBar");
			JadeToolsPopUp.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) );
			JadeToolsPopUp.add( new CWMenueItem( "PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) �ffnen"), "MBJadeRMA.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopDF", Language.translate("DF anzeigen"), "MBJadeDF.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopIntrospec", Language.translate("Introspector-Agent starten"), "MBJadeIntrospector.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopLog", Language.translate("Log-Manager starten"), "MBJadeLogger.gif" )) ;
			
			// --- Symbolleisten-Definition -------------------------------
			jToolBarApplication = new JToolBar("MainBar");
			jToolBarApplication.setFloatable(false);
			jToolBarApplication.setRollover(true);
			
			jToolBarApplication.add(new JToolBarButton( "New", Language.translate("Neues Projekt"), null, "MBnew.png" ));
			jToolBarApplication.add(new JToolBarButton( "Open", Language.translate("Projekt �ffnen"), null, "MBopen.png" ));
			jToolBarApplication.add(new JToolBarButton( "Save", Language.translate("Projekt speichern"), null, "MBsave.png" ));
			jToolBarApplication.addSeparator();
			
			jToolBarApplication.add(new JToolBarButton( "ViewConsole", Language.translate("Konsole ein- oder ausblenden"), null, "MBConsole.png" ));
			jToolBarApplication.addSeparator();
			
			jToolBarApplication.add(new JToolBarButton( "JadeStart", Language.translate("JADE starten"), null, "MBJadeOn.png" ));
			jToolBarApplication.add(new JToolBarButton( "JadeStop", Language.translate("JADE stoppen"), null, "MBJadeOff.png" ));
			JadeTools = new JToolBarButton( "JadeTools", Language.translate("JADE-Tools..."), null, "MBJadeTools.png" );
			jToolBarApplication.add( JadeTools );
			jToolBarApplication.addSeparator();
			jToolBarApplication.add(new JToolBarButton( "ContainerMonitoring", Language.translate("Auslastungs-Monitor �ffnen"), null, "MBLoadMonitor.png" ));
			jToolBarApplication.addSeparator();

			// --- Simulation Buttons -----------
			jButtonSimStart = new JToolBarButton( "SimulationStart", Language.translate("MAS-Start"), null, "MBLoadPlay.png" );
			jToolBarApplication.add(jButtonSimStart);
			
			jButtonSimPause = new JToolBarButton( "SimulationPause", Language.translate("MAS-Pause"), null, "MBLoadPause.png" );
			jToolBarApplication.add(jButtonSimPause);
			
			jButtonSimStop = new JToolBarButton( "SimulationStop", Language.translate("MAS-Stop"), null, "MBLoadStopRecord.png" );
			jToolBarApplication.add(jButtonSimStop) ;
			
			jToolBarApplication.addSeparator();
			
			
		};		
		return jToolBarApplication;
	}	
	
	/**
	 * This method can be used in order to add an individual menu button
	 * a specified index position of the toolbar.
	 *
	 * @param myComponent the my component
	 * @param indexPosition the index position
	 */
	public void addJToolbarComponent(JComponent myComponent, int indexPosition) {
		int nElements = jToolBarApplication.getComponentCount(); 
		if (indexPosition > (nElements-1)) {
			this.addJToolbarComponent(myComponent);
		} else {
			jToolBarApplication.add(myComponent, indexPosition);
			this.validate();
		}
	}
	
	/**
	 * This method can be used in order to add an
	 * individual menu button to the toolbar.
	 *
	 * @param myComponent the my component
	 */
	public void addJToolbarComponent(JComponent myComponent) {
		jToolBarApplication.add(myComponent);
		this.validate();
	}
	// ------------------------------------------------------------
	// --- Symbolleiste erstellen - ENDE --------------------------
	// ------------------------------------------------------------

	
	// ------------------------------------------------------------
	// --- Unterklasse f�r die Symbolleisten-Buttons --------------
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
		private JToolBarButton( String actionCommand, 
								String toolTipText, 
								String altText, 
								String imgName ) {
				
			this.setText(altText);
			this.setToolTipText(toolTipText);
			this.setSize(36, 36);
			
			if ( imgName != null ) {
				this.setPreferredSize( new Dimension(26,26) );
			}
			else {
				this.setPreferredSize( null );	
			}

			if ( imgName != null ) {
				try {
					ImageIcon ButtIcon = new ImageIcon( this.getClass().getResource( pathImage + imgName ), altText);
					this.setIcon(ButtIcon);
				}
				catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}				
			}
			this.addActionListener(this);	
			this.setActionCommand(actionCommand);
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
			// --- Fallunterscheidung 'cmd' einbauen ---
			String ActCMD = ae.getActionCommand();			
			// ------------------------------------------------
			if ( ActCMD.equalsIgnoreCase("New") ) {
				Application.Projects.add(true);
			}
			else if ( ActCMD.equalsIgnoreCase("Open") ) {
				Application.Projects.add(false);			}
			else if ( ActCMD.equalsIgnoreCase("Save") ) {
				Project CurPro = Application.ProjectCurr;
				if ( CurPro != null ) CurPro.save();
			}
			// ------------------------------------------------
			else if ( ActCMD.equalsIgnoreCase("ViewConsole") ) { 
				Application.MainWindow.doSwitchConsole();
			}
			// ------------------------------------------------
			else if ( ActCMD.equalsIgnoreCase("JadeStart") ) { 
				Application.JadePlatform.jadeStart();				
			}
			else if ( ActCMD.equalsIgnoreCase("JadeStop") ) {
				Application.JadePlatform.jadeStop();				
			}
			else if ( ActCMD.equalsIgnoreCase("JadeTools") ) { 
				JadeToolsPopUp.show( JadeTools, 0, JadeTools.getHeight() );
			}
			else if ( ActCMD.equalsIgnoreCase("ContainerMonitoring") ) { 
				Application.JadePlatform.jadeSystemAgentOpen("loadMonitor", null);
			}
			// ------------------------------------------------
			else if ( ActCMD.equalsIgnoreCase("SimulationStart") ) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
				Application.JadePlatform.jadeSystemAgentOpen("simstarter", null, startWith);
			}
			else if ( ActCMD.equalsIgnoreCase("SimulationPause") ) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Pause;
				Application.JadePlatform.jadeSystemAgentOpen("simstarter", null, startWith);
			}
			else if ( ActCMD.equalsIgnoreCase("SimulationStop") ) {
				Application.JadePlatform.jadeStop();
//				Object[] startWith = new Object[1];
//				startWith[0] = SimStartAgent.BASE_ACTION_Stop;
//				Application.JadePlatform.jadeSystemAgentOpen("simstarter", null, startWith);
			}
			// ------------------------------------------------
			else { 
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
			};
			
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
	}	
	// -------------------------------------------------------------------
	// -------------------------------------------------------------------
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		if (consoleIsVisible() == false) {
			jSplitPane4ProjectDesktop.setDividerLocation( jSplitPane4ProjectDesktop.getHeight() );			
		}
		if ( Application.Projects.count() != 0 ) {
			Application.ProjectCurr.setMaximized();
			setCloseButtonPosition( true );
		}
		else {
			setCloseButtonPosition( false );
		}
	}
	
	// ------------------------------------------------------------
	// --- Unterklasse f�r die Schr�ge im unteren  
	// --- rechten Teil des Hauptfenmsters
	// ------------------------------------------------------------	
	/**
	 * The Class AngledLinesWindowsCornerIcon.
	 */
	private class AngledLinesWindowsCornerIcon implements Icon {
		  
		/** The WHIT e_ lin e_ color. */
		private final Color WHITE_LINE_COLOR = new Color(255, 255, 255);
		
		/** The GRA y_ lin e_ color. */
		private final Color GRAY_LINE_COLOR = new Color(172, 168, 153);
		
		/** The Constant WIDTH. */
		private static final int WIDTH = 13;
		
		/** The Constant HEIGHT. */
		private static final int HEIGHT = 13;

		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconHeight()
		 */
		public int getIconHeight() {
			return WIDTH;
		}

		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconWidth()
		 */
		public int getIconWidth() {
			return HEIGHT;
		}

		/* (non-Javadoc)
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



