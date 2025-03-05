package de.enflexit.awb.desktop.mainWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import de.enflexit.awb.baseUI.console.JPanelConsole;
import de.enflexit.awb.baseUI.console.JTabbedPane4Consoles;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.jade.Platform.JadeStatusColor;
import de.enflexit.awb.core.jade.Platform.SystemAgent;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.ui.AwbMainWindow;
import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.awb.core.update.ProjectRepositoryExplorerDialog;
import de.enflexit.awb.desktop.dialogs.StartAgentDialog;
import de.enflexit.awb.desktop.mainWindow.MainWindowExtension.MainWindowMenu;
import de.enflexit.awb.desktop.mainWindow.MainWindowExtension.MainWindowMenuItem;
import de.enflexit.awb.desktop.mainWindow.MainWindowExtension.MainWindowToolbarComponent;
import de.enflexit.awb.desktop.mainWindow.MainWindowExtension.SeparatorPosition;
import de.enflexit.awb.simulation.agents.LoadExecutionAgent;
import de.enflexit.awb.simulation.logging.SysOutBoard;
import de.enflexit.common.images.ImageHelper;
import de.enflexit.common.swing.AwbLookAndFeelAdjustments;
import de.enflexit.common.swing.JFrameSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.common.swing.fileSelection.DirectoryDialog;
import de.enflexit.language.Language;

/**
 * This class represents the main user-interface of the application AgentGUI.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MainWindow extends JFrame implements AwbMainWindow<JMenu, JMenuItem, JToolBar, JButton> {

	private static final long serialVersionUID = 1L;

	public static final String MAIN_WINDOW_EXTENSION_ID = "org.awb.swing.mainWindowExtension";

	public enum WorkbenchMenu {
		MenuProject,
		MenuView,
		MenuJade,
		MenuSimulation,
		MenuExtra,
		MenuWindows,
		MenuHelp
	}
	
	private final ImageIcon iconCloseDummy = GlobalInfo.getInternalImageIcon("MBdummy.png");

	private JFrameSizeAndPostionController windowController;
	
	private MainWindowStatusBar jPanelStatusBar;
	private PlatformStatusDialog platformStatusDialog;
	
	private JSplitPane jSplitPane4ProjectDesktop;
	private JDesktopPane jDesktopPane4Projects;
	private JTabbedPane4Consoles jTabbedPane4Console;
	private JPanelConsole jPanelConsoleLocal;
	private int visibleDividersLocation;

	private JMenuBar jMenuBarMain;
	private JMenu jMenuMainProject;
	
	private JMenu jMenuMainView;
	private ButtonGroup viewGroup;
	private JRadioButtonMenuItem viewDeveloper;
	private JRadioButtonMenuItem viewEndUser;
	private JMenuItem jMenuItemViewTree;
	private JMenuItem jMenuItemViewTabHeader;
	private ActionListener viewActionListener;
	
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

	private JPanel jPanelToolBar;
	private JToolBar jToolBarApplication;
	private JButton jButtonProjectTree;
	private SetupSelectorToolbar setupSelectorToolbar;
	private JButton jButtonJadeTools;
	private JPopupMenu jPopupMenuJadeTools;

	private JToolBar jToolBarCloseProject;
	private JButton jButtonCloseProject;
	private ImageIcon iconCloseProject;
	private Color textColor;
	
	private JButton jButtonSimStart;
	private JButton jButtonSimPause;
	private JButton jButtonSimStop;

	/**
	 * Constructor of this class.
	 */
	public MainWindow() {

		// --- Set the IconImage ------------------------------------
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon48());

		// --- Set the Look and Feel of the Application -------------
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// --- Configure console and divider position ---------------
		double windowHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100;
		if (Application.getGlobalInfo().isMaximzeMainWindow()==false) {
			windowHeight = this.getSizeRelatedToScreenSize().getHeight() - 100;
		}
		this.visibleDividersLocation = (int) windowHeight * 3/4; 
	
		// --- Create the Main-Elements of the Application ----------
		this.initComponents();

		// --- Set the JTabbedPan for remote console output ---------
		SysOutBoard.setConsoleFolder(this.getJTabbedPane4Console());
		this.getJTabbedPane4Console().setVisible(false);

		// --- Finalize the display of the application --------------
		this.setTitelAddition("");
		this.setCloseProjectButtonVisible(false);

		// --- Initialize the PlatformStatusDialog ------------------ 
		this.getPlatformStatusDialog();
		
		// --- Proceed the MainWindow Extensions --------------------
		this.proceedMainWindowExtensions();
		
		this.pack();
		this.setVisible(true);

		// --- Place MainWindow center on screen --------------------
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ScreenCenter);
	}

	/**
	 * Initializes the local components.
	 */
	private void initComponents() {

		this.setJMenuBar(this.getJMenuBarMain());

		getContentPane().add(this.getJPanelToolbar(), BorderLayout.NORTH);
		getContentPane().add(this.getStatusBar(), BorderLayout.SOUTH);
		getContentPane().add(this.getJSplit4ProjectDesktop());

		Dimension frameSize = this.getSizeRelatedToScreenSize();
		this.setPreferredSize(frameSize);
		this.setSize(new Dimension(1384, 739));

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
				if (isConsoleVisible() == false) {
					getJSplit4ProjectDesktop().setDividerLocation(getJSplit4ProjectDesktop().getHeight());
				}
				if (Application.getProjectsLoaded().count() != 0) {
					Application.getProjectFocused().setMaximized();
					MainWindow.this.setCloseProjectButtonVisible(true);
				} else {
					MainWindow.this.setCloseProjectButtonVisible(false);
				}
			}
		});

		// --- Start the local Window controller ----------
		this.getWindowSizeAndPostionController();
		
		// --- Set button for simulation control ----------
		this.setSimulationReady2Start();
	}
	/**
	 * Returns the JFrameSizeAndPostionController used for the MainWindow.
	 */
	private JFrameSizeAndPostionController getWindowSizeAndPostionController() {
		if (windowController==null) {
			windowController = new JFrameSizeAndPostionController(this);
		}
		return windowController;
	}
	
	/**
	 * Return the size in relation (scaled) to the screen size.
	 */
	private Dimension getSizeRelatedToScreenSize() {

		// --- Scale relative to screen ---------
		double scale = 0.9;
		
		GraphicsDevice gd = this.getGraphicsConfiguration().getDevice();
		if (gd==null) {
			gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		}
		int frameWidth  = (int) (gd.getDisplayMode().getWidth()  * scale);
		int frameHeight = (int) (gd.getDisplayMode().getHeight() * scale);

		return new Dimension(frameWidth, frameHeight);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}
	
	/**
	 * Proceeds the main window extensions that are defines 
	 * by the corresponding extension point.
	 */
	private void proceedMainWindowExtensions() {
		
		IConfigurationElement[] configElements = Platform.getExtensionRegistry().getConfigurationElementsFor(MAIN_WINDOW_EXTENSION_ID);
		try {
			for (int i = 0; i < configElements.length; i++) {
				IConfigurationElement configElement = configElements[i]; 
				final Object execExt = configElement.createExecutableExtension("class");
				if (execExt instanceof MainWindowExtension) {
					MainWindowExtension mwExtension = (MainWindowExtension) execExt;
					this.proceedMainWindowExtension(mwExtension);
				}
			} 

		} catch (CoreException ex) {
            System.err.println(ex.getMessage());
        }
	}
	/**
	 * Proceeds a single {@link MainWindowExtension} that was registered as extension.
	 * @param mwExtension the MainWindowExtension to proceed
	 */
	private void proceedMainWindowExtension(MainWindowExtension mwExtension) {
		
		if (mwExtension==null) return;

		// ----------------------------------------------------------
		// --- Menus ------------------------------------------------
		// ----------------------------------------------------------
		try {
			// --- Call the initialize method first -----------------
			mwExtension.initialize();
			
		} catch (Exception ex) {
			System.err.println(mwExtension.getClass().getName() + ": Error while initializing the MainWindowExtension.");
			ex.printStackTrace();
		}
		
		try {
			// --- Check the vector of new menus --------------------
			if (mwExtension.getMainWindowMenuVector().size()>0) {
				for (int i = 0; i < mwExtension.getMainWindowMenuVector().size(); i++) {
					MainWindowMenu mwMenu = mwExtension.getMainWindowMenuVector().get(i);
					this.addJMenu(mwMenu.getJMenu(), mwMenu.getIndexPosition());
				}
			}
			
		} catch (Exception ex) {
			System.err.println(mwExtension.getClass().getName() + ": Error while adding menu to the MainWindow.");
			ex.printStackTrace();
		}

		// ----------------------------------------------------------
		// --- Menu items -------------------------------------------
		// ----------------------------------------------------------		
		try {
			// --- Check for single MenuItems ---------------------------
			if (mwExtension.getMainWindowMenuItemVector().size()>0) {
				// --- Add the specified menu items ----------------- 
				for (int i = 0; i < mwExtension.getMainWindowMenuItemVector().size(); i++) {
					// --- Get single element ----------------------- 
					MainWindowMenuItem mwMenuItem = mwExtension.getMainWindowMenuItemVector().get(i);
					if (mwMenuItem.getWorkbenchMenu()==null) {
						System.err.println(mwExtension.getClass().getName() + ": No menu was specified for the menu item.");
						continue;
					}
					// --- Get the right menu for the menu item -----
					JMenu jMenuToAddTo = this.getJMenuOfWorkbenchMenu(mwMenuItem.getWorkbenchMenu());
					if (jMenuToAddTo==null) {						
						System.err.println(mwExtension.getClass().getName() + ": Could not find the specified menu '" + mwMenuItem.getWorkbenchMenu() + "' for the menu item.");
					} else {
						// --- Add the menu item to the menu --------
						if (mwMenuItem.getIndexPosition()!=null) {
							jMenuToAddTo.add(mwMenuItem.getJMenuItem(), (int)mwMenuItem.getIndexPosition());
						} else {
							jMenuToAddTo.add(mwMenuItem.getJMenuItem());
						}
						
						// --- Add a separator also ? --------------- 
						int indexPosition = jMenuToAddTo.getPopupMenu().getComponentIndex(mwMenuItem.getJMenuItem());
						if (mwMenuItem.getSeparatorPosition()==SeparatorPosition.SeparatorInFrontOf) {
							jMenuToAddTo.add(new JPopupMenu.Separator(), indexPosition);
						} else if (mwMenuItem.getSeparatorPosition()==SeparatorPosition.SeparatorAfter) {
							jMenuToAddTo.add(new JPopupMenu.Separator(), indexPosition+1);
						}
						
					}
				} // end for
			}
			
		} catch (Exception ex) {
			System.err.println(mwExtension.getClass().getName() + ": Error while adding a menu item to the MainWindow.");
			ex.printStackTrace();
		}
		
		// ----------------------------------------------------------
		// --- Toolbar elements -------------------------------------
		// ----------------------------------------------------------		
		try {
			// --- Check for tool bar elements ----------------------
			if (mwExtension.getMainWindowToolBarComponentVector().size()>0) {
				for (int i = 0; i < mwExtension.getMainWindowToolBarComponentVector().size(); i++) {
					
					MainWindowToolbarComponent mwToolbarComp = mwExtension.getMainWindowToolBarComponentVector().get(i);
					this.addJToolbarComponent(mwToolbarComp.getJComponent(), mwToolbarComp.getIndexPosition());
					
					// --- Add a separator also ? --------------- 
					int indexPosition = this.getJToolBarApplication().getComponentIndex(mwToolbarComp.getJComponent());
					if (mwToolbarComp.getSeparatorPosition()==SeparatorPosition.SeparatorInFrontOf) {
						this.addToolbarComponent(new JToolBar.Separator(), indexPosition);
					} else if (mwToolbarComp.getSeparatorPosition()==SeparatorPosition.SeparatorAfter) {
						this.addToolbarComponent(new JToolBar.Separator(), indexPosition+1);
					}
					
				}
			}
			
		} catch (Exception ex) {
			System.err.println(mwExtension.getClass().getName() + ": Error while adding a menu item to the MainWindow.");
			ex.printStackTrace();
		}
		
	}

	/**
	 * Returns the specified workbench menu as JMenu.
	 * @param wbMenu the {@link WorkbenchMenu}
	 * @return the JMenu of workbench menu
	 */
	private JMenu getJMenuOfWorkbenchMenu(WorkbenchMenu wbMenu) {
		
		JMenu jMenuWorkbench = null;
		switch (wbMenu) {
		case MenuProject:
			jMenuWorkbench = this.getJMenuMainProject();
			break;
		case MenuView:
			jMenuWorkbench = this.getJMenuMainView();
			break;
		case MenuJade:
			jMenuWorkbench = this.getJMenuMainJade();
			break;
		case MenuSimulation:
			jMenuWorkbench = this.getJMenuMainSimulation();
			break;
		case MenuExtra:
			jMenuWorkbench = this.getJMenuMainExtra();
			break;
		case MenuWindows:
			jMenuWorkbench = this.getJMenuMainWindow();
			break;
		case MenuHelp:
			jMenuWorkbench = this.getJMenuMainHelp();
			break;
		}
		return jMenuWorkbench;
	}
	
	/**
	 * Gets the status bar.
	 * @return the status bar
	 */
	public MainWindowStatusBar getStatusBar() {
		if (jPanelStatusBar==null) {
			jPanelStatusBar = new MainWindowStatusBar();
		}
		return jPanelStatusBar;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setTitelAddition(java.lang.String)
	 */
	@Override
	public void setTitelAddition(String add2BasicTitel) {
		if (add2BasicTitel != "") {
			this.setTitle(Application.getGlobalInfo().getApplicationTitle() + ": " + add2BasicTitel);
		} else {
			this.setTitle(Application.getGlobalInfo().getApplicationTitle());
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setStatusBarMessage(java.lang.String)
	 */
	@Override
	public void setStatusBarMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				getStatusBar().setStatusBarMessage(message);
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setJadeStatusColor(de.enflexit.awb.core.jade.Platform.JadeStatusColor)
	 */
	@Override
	public void setJadeStatusColor(JadeStatusColor jadeStatus) {
		this.getStatusBar().setJadeStatusColor(jadeStatus);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#refreshView()
	 */
	@Override
	public void refreshView() {
		this.validate();
		this.repaint();
	}
	
	/**
	 * Resets the Look and Feel and the language menu after the Lnf was updated
	 */
	public void resetAfterLookAndFeelUpdate() {

		if (jMenuExtraLnF != null) {
			jMenuExtraLnF.removeAll();
			this.setJMenuExtraLnF();
		}
		if (jMenuExtraLang != null) {
			jMenuExtraLang.removeAll();
			this.setjMenuExtraLang();
		}
		if (Application.getProjectFocused()!=null) {
			Application.getProjectFocused().setMaximized();
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#restoreFocus()
	 */
	@Override
	public void restoreFocus() {
		if (this.getExtendedState() == Frame.ICONIFIED || this.getExtendedState() == Frame.ICONIFIED + Frame.MAXIMIZED_BOTH) {
			this.setState(Frame.NORMAL);
		}
		this.setAlwaysOnTop(true);
		this.setAlwaysOnTop(false);
	}
	
	
	/**
	 * Return if the AwbConsole is visible.
	 * @return true, if successful
	 */
	public boolean isConsoleVisible() {
		if (this.getJSplit4ProjectDesktop().getDividerSize()==0) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Do switch console.
	 */
	private void doSwitchConsole() {
		if (this.getJSplit4ProjectDesktop().getDividerSize()>0) {
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
	public void setConsoleVisible(boolean show) {
		this.getJTabbedPane4Console().setVisible(show);
		if (show==true) {
			this.getJSplit4ProjectDesktop().setDividerSize(10);
			this.getJSplit4ProjectDesktop().setDividerLocation(this.visibleDividersLocation);
		} else {
			this.visibleDividersLocation = this.getJSplit4ProjectDesktop().getDividerLocation();
			this.getJSplit4ProjectDesktop().setDividerSize(0);
			this.getJSplit4ProjectDesktop().setDividerLocation(this.getJSplit4ProjectDesktop().getHeight());
		}
		if (Application.getProjectsLoaded().count()>0) {
			Application.getProjectFocused().setMaximized();
		}
	}
	/**
	 * Returns the main JSplitPane.
	 * @return the main JSplitPane
	 */
	private JSplitPane getJSplit4ProjectDesktop() {
		if (jSplitPane4ProjectDesktop == null) {
			jSplitPane4ProjectDesktop = new JSplitPane();
			jSplitPane4ProjectDesktop.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane4ProjectDesktop.setOneTouchExpandable(true);
			jSplitPane4ProjectDesktop.setTopComponent(this.getJDesktopPane4Projects());
			jSplitPane4ProjectDesktop.setBottomComponent(this.getJTabbedPane4Console());
			jSplitPane4ProjectDesktop.setDividerSize(0);
			jSplitPane4ProjectDesktop.setResizeWeight(0.8);
			jSplitPane4ProjectDesktop.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent pcEvent) {
					if (Application.getProjectsLoaded().count()>0 && pcEvent.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
						// --- Did the divider location really changed ? ----------------
						if (pcEvent.getNewValue()!=pcEvent.getOldValue()) {
							Application.getProjectFocused().setMaximized();
						}
					}
				}
			});
		}
		return jSplitPane4ProjectDesktop;
	}
	
	public JDesktopPane getJDesktopPane4Projects() {
		if (jDesktopPane4Projects == null) {
			jDesktopPane4Projects = new JDesktopPane();
		}
		return jDesktopPane4Projects;
	}
	public JTabbedPane4Consoles getJTabbedPane4Console() {
		if (jTabbedPane4Console == null) {
			jTabbedPane4Console = new JTabbedPane4Consoles();
			jTabbedPane4Console.add(Language.translate("Lokal"), this.getJPanelConsoleLocal());
			jTabbedPane4Console.setVisible(false);
		}
		return jTabbedPane4Console;
	}
	private JPanelConsole getJPanelConsoleLocal() {
		if (jPanelConsoleLocal==null) {
			jPanelConsoleLocal = (JPanelConsole) Application.getConsole();
		}
		return jPanelConsoleLocal;
	}
	
	// ------------------------------------------------------------
	// --- Desktop der Anwendung definieren - ENDE ----------------
	// ------------------------------------------------------------

	// ------------------------------------------------------------
	// --- Tollbar definition - START -----------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the applications menu bar.
	 * @return the j menu bar main
	 */
	private JMenuBar getJMenuBarMain() {
		if (jMenuBarMain == null) {
			jMenuBarMain = new JMenuBar();
			jMenuBarMain.setBorder(null);

			jMenuBarMain.add(this.getJMenuMainProject());
			jMenuBarMain.add(this.getJMenuMainView());
			jMenuBarMain.add(this.getJMenuMainJade());
			jMenuBarMain.add(this.getJMenuMainSimulation());
			jMenuBarMain.add(this.getJMenuMainExtra());
			jMenuBarMain.add(this.getJMenuMainWindow());
			jMenuBarMain.add(this.getJMenuMainHelp());
		}
		return jMenuBarMain;
	}
	
	@Override
	public void addMenu(Object myMenu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMenu(Object myMenu, int indexPosition) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method can be used in order to add an individual menu at a specified index position of the menu bar.
	 *
	 * @param myMenu the my menu
	 * @param indexPosition the index position
	 */
	private void addJMenu(JMenu myMenu, Integer indexPosition) {
		int nElements = this.getJMenuBarMain().getSubElements().length;
		if (indexPosition==null || indexPosition > (nElements - 1)) {
			this.addJMenu(myMenu);
		} else {
			this.getJMenuBarMain().add(myMenu, (int)indexPosition);
			this.validate();
		}
	}
	/**
	 * This method can be used in order to add an individual menu.
	 * @param myMenu the my menu
	 */
	private void addJMenu(JMenu myMenu) {
		this.getJMenuBarMain().add(myMenu);
		this.validate();
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#addMenuItemComponent(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void addMenuItemComponent(Object menu2add2, Object myMenuItemComponent) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#addMenuItemComponent(java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public void addMenuItemComponent(Object menu2add2, Object myMenuItemComponent, int indexPosition) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * This method can be used in order to add an individual JMmenuItem at a specified index position of the given menu.
	 *
	 * @param menu2add the menu2add
	 * @param myMenuItemComponent the my menu item component
	 * @param indexPosition the index position
	 */
	private void addJMenuItemComponent(JMenu menu2add, JComponent myMenuItemComponent, int indexPosition) {
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
	private void addJMenuItemComponent(JMenu menu2add, JComponent myMenuItemComponent) {
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
			jMenuMainProject.add(new CWMenuItem("ProjectNew", Language.translate("Neues Projekt"), "MBnew.png"));
			jMenuMainProject.add(new CWMenuItem("ProjectOpen", Language.translate("Projekt öffnen"), "MBopen.png"));
			jMenuMainProject.add(new CWMenuItem("ProjectClose", Language.translate("Projekt schließen"), "MBclose.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenuItem("ProjectSave", Language.translate("Projekt speichern"), "MBsave.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenuItem("ProjectImport", Language.translate("Projekt importieren"), "MBtransImport.png"));
			jMenuMainProject.add(new CWMenuItem("RepositoryImport", Language.translate("Repository-Import"), "MBrepositoryImport.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenuItem("ProjectExport", Language.translate("Projekt exportieren"), "MBtransExport.png"));
			jMenuMainProject.add(new CWMenuItem("RepositoryExport", Language.translate("Repository-Export"), "MBrepositoryExport.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenuItem("ProjectDelete", Language.translate("Projekt löschen"), "Delete.png"));
			jMenuMainProject.addSeparator();
			jMenuMainProject.add(new CWMenuItem("ApplicationQuit", Language.translate("Beenden"), null));
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
			jMenuMainView.add(this.getJRadioButtonMenuItemViewDeveloper());
			jMenuMainView.add(this.getJRadioButtonMenuItemViewEndUser());

			viewGroup = new ButtonGroup();
			viewGroup.add(this.getJRadioButtonMenuItemViewDeveloper());
			viewGroup.add(this.getJRadioButtonMenuItemViewEndUser());
			
			jMenuMainView.addSeparator();
			jMenuMainView.add(this.getJMenuItemViewTree());
			jMenuMainView.add(this.getJMenuItemViewTabHeader());
			// --------------------------------------------

			jMenuMainView.addSeparator();
			jMenuMainView.add(new CWMenuItem("ViewConsole", Language.translate("Konsole ein- oder ausblenden"), "MBConsole.png"));
			jMenuMainView.add(new CWMenuItem("ViewHeapMonitor", Language.translate("Heap-Monitor ein- oder ausblenden"), "MBHeapMonitor.png"));
		}
		return jMenuMainView;
	}

	private ActionListener getViewActionListener() {
		if (viewActionListener==null) {
			viewActionListener = new ActionListener() {
				/* (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				@Override
				public void actionPerformed(ActionEvent ae) {
					
					if (Application.getProjectFocused()==null) return;
					
					if (ae.getSource()==MainWindow.this.getJRadioButtonMenuItemViewDeveloper()) {
						Application.getProjectFocused().setProjectView(Project.VIEW_Developer);
					} else if (ae.getSource()==MainWindow.this.getJRadioButtonMenuItemViewEndUser()) {
						Application.getProjectFocused().setProjectView(Project.VIEW_User);

					} else if (ae.getSource()==MainWindow.this.getJMenuItemViewTree()) {
						Application.getProjectFocused().toggleViewProjectTree();
					} else if (ae.getSource()==MainWindow.this.getJMenuItemViewTabHeader()) {
						Application.getProjectFocused().toggleViewProjectTabHeader();
					}
				}
			};
		}
		return viewActionListener;
	}
	
	public JRadioButtonMenuItem getJRadioButtonMenuItemViewDeveloper() {
		if (viewDeveloper==null) {
			viewDeveloper = new JRadioButtonMenuItem(Language.translate("Entwickler-Ansicht"));
			viewDeveloper.setSelected(true);
			viewDeveloper.addActionListener(this.getViewActionListener());
		}
		return viewDeveloper;
	}
	public JRadioButtonMenuItem getJRadioButtonMenuItemViewEndUser() {
		if (viewEndUser==null) {
			viewEndUser = new JRadioButtonMenuItem(Language.translate("Endanwender-Ansicht"));
			viewEndUser.addActionListener(this.getViewActionListener());
		}
		return viewEndUser;
	}
	
	public JMenuItem getJMenuItemViewTree() {
		if (jMenuItemViewTree==null) {
			jMenuItemViewTree = new JMenuItem(Language.translate("Projektbaum ein- oder ausblenden"));
			jMenuItemViewTree.setIcon(GlobalInfo.getInternalImageIcon("ProjectTree.png"));
			jMenuItemViewTree.addActionListener(this.getViewActionListener());
		}
		return jMenuItemViewTree;
	}
	public JMenuItem getJMenuItemViewTabHeader() {
		if (jMenuItemViewTabHeader==null) {
			jMenuItemViewTabHeader = new JMenuItem(Language.translate("Tab-Header ein- oder ausblenden"));
			jMenuItemViewTabHeader.setIcon(GlobalInfo.getInternalImageIcon("ProjectTabHeader.png"));
			jMenuItemViewTabHeader.addActionListener(this.getViewActionListener());
		}
		return jMenuItemViewTabHeader;
	}
	
	// ------------------------------------------------------------
	// --- Menu "JADE" --------------------------------------------
	// ------------------------------------------------------------
	/**
	 * This method returns the current instance of the menu "JADE".
	 * @return the JMenu for JADE
	 */
	public JMenu getJMenuMainJade() {
		if (jMenuMainJade == null) {
			jMenuMainJade = new JMenu("JADE");
			jMenuMainJade.setText(Language.translate("JADE"));
			jMenuMainJade.add(new CWMenuItem("JadeStart", Language.translate("JADE starten"), "MBJadeOn.png"));
			jMenuMainJade.add(new CWMenuItem("JadeStop", Language.translate("JADE stoppen"), "MBJadeOff.png"));
			jMenuMainJade.addSeparator();
			jMenuMainJade.add(new CWMenuItem("StartAgent", Language.translate("Starte Agenten"), "MBstartAgent.png"));
			jMenuMainJade.addSeparator();
			jMenuMainJade.add(new CWMenuItem("PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) öffnen"), "MBJadeRMA.gif"));
			jMenuMainJade.add(new CWMenuItem("PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif"));
			jMenuMainJade.add(new CWMenuItem("PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif"));
			jMenuMainJade.add(new CWMenuItem("PopDF", Language.translate("DF anzeigen"), "MBJadeDF.gif"));
			jMenuMainJade.add(new CWMenuItem("PopIntrospec", Language.translate("Introspector-Agent starten"), "MBJadeIntrospector.gif"));
			jMenuMainJade.add(new CWMenuItem("PopLog", Language.translate("Log-Manager starten"), "MBJadeLogger.gif"));
			jMenuMainJade.addSeparator();
			jMenuMainJade.add(new CWMenuItem("ContainerMonitoring", Language.translate("Auslastungs-Monitor öffnen"), "MBLoadMonitor.png"));
			jMenuMainJade.add(new CWMenuItem("ThreadMonitoring", Language.translate("Thread-Monitor öffnen"), "MBclock.png"));
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

			jMenuItemSimStart = new CWMenuItem("SimulationStart", Language.translate("Start"), "MBLoadPlay.png");
			jMenuMainSimulation.add(jMenuItemSimStart);
			jMenuItemSimPause = new CWMenuItem("SimulationPause", Language.translate("Pause"), "MBLoadPause.png");
			jMenuMainSimulation.add(jMenuItemSimPause);
			jMenuItemSimStop = new CWMenuItem("SimulationStop", Language.translate("Stop"), "MBLoadStopRecord.png");
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
			jMenuExtra.add(new CWMenuItem("ExtraBenchmark", "SciMark 2.0 - Benchmark", null));

			jMenuExtra.addSeparator();
			jMenuExtra.add(new CWMenuItem("ExtraOptions", Language.translate("Optionen"), null));
			jMenuExtra.add(new CWMenuItem("DatabaseConnections", Language.translate("Datenbank Verbindungen"), "DB_State_Blue.png"));
			jMenuExtra.add(new CWMenuItem("Authentication", Language.translate("Web Service Authentifizierung"), null));

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
		jMenuExtraLang.add(new CWMenuItem("ExtraTranslation", Language.translate("Übersetzen ..."), null));

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

		for (LookAndFeelInfo lafInfo : AwbLookAndFeelAdjustments.getInstalledLookAndFeels()) {
			boolean setBold = lafInfo.getClassName().equals(Application.getGlobalInfo().getAppLookAndFeelClassName());
			jMenuExtraLnF.add(new JMenuItmenLnF(lafInfo.getName(), lafInfo.getClassName(), setBold));
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
					// TODO Change of the LookNFeel 
					//Application.setLookAndFeel(JMenuItmenLnF.this.LnFClass);
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
			jMenuMainHelp.add(new CWMenuItem("HelpAbout", Language.translate("Über..."), "awb16.png"));
			jMenuMainHelp.addSeparator();
			jMenuMainHelp.add(new CWMenuItem("HelpAWB-GitHub", Language.translate("AWB auf GitHub"), "GitHub.png"));
			jMenuMainHelp.add(new CWMenuItem("HelpAWB-GitBook", Language.translate("AWB auf GitBook"), "GitBook.png"));
			jMenuMainHelp.addSeparator();
			jMenuMainHelp.add(new CWMenuItem("HelpUpdate", Language.translate("Nach Update suchen") + " !", "Update.png"));
			jMenuMainHelp.add(new CWMenuItem("HelpAWBChanges", Language.translate("Letzte Änderungen"), null));
			jMenuMainHelp.addSeparator();
			jMenuMainHelp.add(new CWMenuItem("EclipseWindow", "Eclipse Window", "eclipse.png"));
		}
		return jMenuMainHelp;
	}

	// ------------------------------------------------------------
	// --- Sub class for simple menu items - START ----------------
	// ------------------------------------------------------------
	/** The Class CWMenueItem. */
	private class CWMenuItem extends JMenuItem implements ActionListener {

		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new cW menu item.
		 *
		 * @param actionCommand the action command
		 * @param Text the text
		 * @param imgName the img name
		 */
		private CWMenuItem(String actionCommand, String Text, String imgName) {

			this.setText(Text);
			if (imgName!=null) {
				try {
					if (imgName.equals("awb16.png")) {
						this.setIcon(GlobalInfo.getInternalImageIconAwbIcon16());
					} else {
						this.setIcon(GlobalInfo.getInternalImageIcon(imgName));
					}
					
				} catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}
			}
			this.addActionListener(this);
			this.setActionCommand(actionCommand);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			String actionCMD = ae.getActionCommand();

			// --- Menu Project -------------------------------------
			if (actionCMD.equalsIgnoreCase("ProjectNew")) {
				Application.getProjectsLoaded().add(true);

			} else if (actionCMD.equalsIgnoreCase("ProjectOpen")) {
				Application.getProjectsLoaded().add(false);

			} else if (actionCMD.equalsIgnoreCase("ProjectClose")) {
				Project currProject = Application.getProjectFocused();
				if (currProject != null) currProject.close();

			} else if (actionCMD.equalsIgnoreCase("ProjectSave")) {
				Project currProject = Application.getProjectFocused();
				if (currProject != null) currProject.save();

			} else if (actionCMD.equalsIgnoreCase("ProjectImport")) {
				Application.getProjectsLoaded().projectImport();

			} else if (actionCMD.equalsIgnoreCase("ProjectExport")) {
				Application.getProjectsLoaded().projectExport();
				
			} else if (actionCMD.equalsIgnoreCase("RepositoryImport")) {
				new ProjectRepositoryExplorerDialog(MainWindow.this);
				
			} else if (actionCMD.equalsIgnoreCase("RepositoryExport")) {
				Application.getProjectsLoaded().projectExportToRepository();

			} else if (actionCMD.equalsIgnoreCase("ProjectDelete")) {
				Application.getProjectsLoaded().projectDelete();

			} else if (actionCMD.equalsIgnoreCase("ApplicationQuit")) {
				Application.stop();

			// --- Menu View ----------------------------------------
			} else if (actionCMD.equalsIgnoreCase("ViewConsole")) {
				MainWindow.this.doSwitchConsole();

			} else if (actionCMD.equalsIgnoreCase("ViewHeapMonitor")) {
				MainWindow.this.getStatusBar().doSwitchHeapMonitorVisibility();
				
			// --- Menu Jade ----------------------------------------
			} else if (actionCMD.equalsIgnoreCase("JadeStart")) {
				Application.getJadePlatform().doStartInDedicatedThread();

			} else if (actionCMD.equalsIgnoreCase("JadeStop")) {
				Application.getJadePlatform().stop(true);

			} else if (actionCMD.equalsIgnoreCase("StartAgent")) {
				MainWindow.this.startAgent();
				
			} else if (actionCMD.equalsIgnoreCase("PopRMAStart")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.RMA, null);

			} else if (actionCMD.equalsIgnoreCase("PopSniffer")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.Sniffer, null);

			} else if (actionCMD.equalsIgnoreCase("PopDummy")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.Dummy, null);

			} else if (actionCMD.equalsIgnoreCase("PopDF")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.DF, null);

			} else if (actionCMD.equalsIgnoreCase("PopIntrospec")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.Introspector, null);

			} else if (actionCMD.equalsIgnoreCase("PopLog")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.Log, null);

			// --- Menu Simulation ----------------------------------
			} else if (actionCMD.equalsIgnoreCase("SimulationStart")) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
				Application.getJadePlatform().startSystemAgent(SystemAgent.SimStarter, null, startWith, true);
				MainWindow.this.setEnabledSimStart(false);

			} else if (actionCMD.equalsIgnoreCase("SimulationPause")) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Pause;
				Application.getJadePlatform().startSystemAgent(SystemAgent.SimStarter, null, startWith, true);
				MainWindow.this.setEnabledSimPause(false);
				
			} else if (actionCMD.equalsIgnoreCase("SimulationStop")) {
				Application.getJadePlatform().stop(true);

			} else if (actionCMD.equalsIgnoreCase("ContainerMonitoring")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.LoadMonitor, null);

			} else if (actionCMD.equalsIgnoreCase("ThreadMonitoring")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.ThreadMonitor, null);

				// --- Menu Extras => NOT here !! ---------------
			} else if (actionCMD.equalsIgnoreCase("ExtraTranslation")) {
				Application.showTranslationDialog();

			} else if (actionCMD.equalsIgnoreCase("ExtraBenchmark")) {
				Application.doBenchmark(true);

			} else if (actionCMD.equalsIgnoreCase("ExtraOptions")) {
				Application.showOptionsDialog();

			} else if (actionCMD.equalsIgnoreCase("DatabaseConnections")) {
				Application.showDatabaseDialog(null);
				
			} else if (actionCMD.equalsIgnoreCase("Authentication")) {
				Application.showAuthenticationDialog();

			// --- Help Menu ----------------------------------------
			} else if (actionCMD.equalsIgnoreCase("HelpAbout")) {
				Application.showAboutDialog();
				
			} else if (actionCMD.equalsIgnoreCase("HelpAWB-GitHub")) {
				Application.browseURI("https://github.com/EnFlexIT/AgentWorkbench");

			} else if (actionCMD.equalsIgnoreCase("HelpAWB-GitBook")) {
				Application.browseURI("https://enflexit.gitbook.io/agent-workbench/");

			} else if (actionCMD.equalsIgnoreCase("HelpAWBChanges")) {
				Application.browseURI("https://github.com/EnFlexIT/AgentWorkbench/releases");
				
			} else if (actionCMD.equalsIgnoreCase("HelpUpdate")) {
				new AWBUpdater(true).start();

			} else if (actionCMD.equalsIgnoreCase("EclipseWindow")) {
				// --- TODO Show the Eclipse Workbench to install new components and so on
				//Application.showEclipseWorkbench();

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

	private void startAgent() {
		StartAgentDialog sad = new StartAgentDialog(MainWindow.this);
		sad.setVisible(true);
		// --- Wait here ----
		if (sad.isCanceled()==false && sad.hasErrors()==false) {
			Application.getJadePlatform().startAgent(sad.getAgentName(), sad.getAgentClass(), sad.getAgentStartArguments(), sad.getAgentContainer());
		}
	}
	
	
	// ------------------------------------------------------------
	// --- Create Toolbar - START ---------------------------------
	// ------------------------------------------------------------
	/**
	 * Return the JPanel for the toolbar.
	 * @return the jPanelToolbar
	 */
	private JPanel getJPanelToolbar() {
		if (jPanelToolBar==null) {
			jPanelToolBar = new JPanel();
			jPanelToolBar.setPreferredSize(new Dimension(100, 26));
			
			GridBagLayout gbl_jPanelToolBar = new GridBagLayout();
			gbl_jPanelToolBar.columnWidths = new int[]{0,0,0};
			gbl_jPanelToolBar.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelToolBar.rowHeights = new int[]{26, 0};
			gbl_jPanelToolBar.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelToolBar.setLayout(gbl_jPanelToolBar);
			
			GridBagConstraints gbc_jToolBarApplication = new GridBagConstraints();
			gbc_jToolBarApplication.fill = GridBagConstraints.HORIZONTAL;
			gbc_jToolBarApplication.gridx = 0;
			gbc_jToolBarApplication.gridy = 0;
			jPanelToolBar.add(this.getJToolBarApplication(), gbc_jToolBarApplication);
			
			GridBagConstraints gbc_jToolBarCloseProject = new GridBagConstraints();
			gbc_jToolBarCloseProject.anchor = GridBagConstraints.WEST;
			gbc_jToolBarCloseProject.fill = GridBagConstraints.HORIZONTAL;
			gbc_jToolBarCloseProject.gridx = 1;
			gbc_jToolBarCloseProject.gridy = 0;
			gbc_jToolBarCloseProject.insets.set(0, 0, 0, 5);
			jPanelToolBar.add(this.getJToolBarCloseProject(), gbc_jToolBarCloseProject);
		}
		return jPanelToolBar;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#getApplicationToolbar()
	 */
	@Override
	public JToolBar getApplicationToolbar() {
		return this.getJToolBarApplication();
	}
	/**
	 * Returns the main JToolBar of the application window.
	 * @return the main JToolBar of the application window
	 */
	private JToolBar getJToolBarApplication() {
		if (jToolBarApplication == null) {

			// --- Symbolleisten-Definition -------------------------------
			jToolBarApplication = new JToolBar("MainBar");
			jToolBarApplication.setFloatable(false);
			jToolBarApplication.setRollover(true);
			
			jToolBarApplication.add(new JToolBarButton("New", Language.translate("Neues Projekt"), null, "MBnew.png"));
			jToolBarApplication.add(new JToolBarButton("Open", Language.translate("Projekt öffnen"), null, "MBopen.png"));
			jToolBarApplication.add(new JToolBarButton("Save", Language.translate("Projekt speichern"), null, "MBsave.png"));
			jToolBarApplication.addSeparator();

			jToolBarApplication.add(new JToolBarButton("ViewConsole", Language.translate("Konsole ein- oder ausblenden"), null, "MBConsole.png"));
			jToolBarApplication.add(this.getJButtonProjectTree());
			jToolBarApplication.addSeparator();

			jToolBarApplication.add(new JToolBarButton("DatabaseConnections", Language.translate("Datenbank Verbindungen"), null, "DB_State_Blue.png"));
			jToolBarApplication.addSeparator();
			
			jToolBarApplication.add(new JToolBarButton("JadeStart", Language.translate("JADE starten"), null, "MBJadeOn.png"));
			jToolBarApplication.add(new JToolBarButton("JadeStop", Language.translate("JADE stoppen"), null, "MBJadeOff.png"));
			jToolBarApplication.addSeparator();

			jToolBarApplication.add(new JToolBarButton("StartAgent", Language.translate("Starte Agenten"), null, "MBstartAgent.png"));
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
			// --------------------------------------------
			if (this.showTestMenuButton==true) {
				jToolBarApplication.add(new JToolBarButton("Test", Language.translate("Test"), "Funktion Testen", null));
			}
			// --------------------------------------------
			
		}
		return jToolBarApplication;
	}

	private JButton getJButtonProjectTree() {
		if (jButtonProjectTree==null) {
			jButtonProjectTree = new JButton(GlobalInfo.getInternalImageIcon("ProjectTree.png"));
			jButtonProjectTree.setToolTipText(Language.translate("Projektbaum ein- oder ausblenden"));
			jButtonProjectTree.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					if (Application.getProjectFocused()!=null) {
						Application.getProjectFocused().toggleViewProjectTree();
					}
				}
			});
		}
		return jButtonProjectTree;
	}
	/**
	 * Configures the jButtonProjectTree with respect to the current project.
	 * @param project the project
	 */
	public void configureJToggleButtonProjectTree(Project project) {
		if (project==null) {
			this.getJButtonProjectTree().setEnabled(false);
		} else {
			this.getJButtonProjectTree().setEnabled(true);
		}
	}
	
	/**
	 * Returns the JPopupMenu with the jade tools.
	 * @return JPopupMenu jade tools
	 */
	private JPopupMenu getJPopupMenuJadeTools() {
		if (jPopupMenuJadeTools==null) {
			jPopupMenuJadeTools = new JPopupMenu("JADE-Tools");
			jPopupMenuJadeTools.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPopupMenuJadeTools.add(new CWMenuItem("PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) öffnen"), "MBJadeRMA.gif"));
			jPopupMenuJadeTools.add(new CWMenuItem("PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif"));
			jPopupMenuJadeTools.add(new CWMenuItem("PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif"));
			jPopupMenuJadeTools.add(new CWMenuItem("PopDF", Language.translate("DF anzeigen"), "MBJadeDF.gif"));
			jPopupMenuJadeTools.add(new CWMenuItem("PopIntrospec", Language.translate("Introspector-Agent starten"), "MBJadeIntrospector.gif"));
			jPopupMenuJadeTools.add(new CWMenuItem("PopLog", Language.translate("Log-Manager starten"), "MBJadeLogger.gif"));
		}
		return jPopupMenuJadeTools;
	}
	/**
	 * Shows the JPopupMenu of the JADE tools.
	 */
	private void showJPopupMenuJadeTools() {
		this.getJPopupMenuJadeTools().show(jButtonJadeTools, 0, jButtonJadeTools.getHeight());
	}

	/**
	 * Gets the setup selector toolbar.
	 * 
	 * @return the setup selector toolbar
	 */
	public SetupSelectorToolbar getSetupSelectorToolbar() {
		if (this.setupSelectorToolbar == null) {
			this.setupSelectorToolbar = new SetupSelectorToolbar(this, this.getJToolBarApplication());
		}
		return this.setupSelectorToolbar;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setEnabledSetupSelector(boolean)
	 */
	@Override
	public void setEnabledSetupSelector(boolean enable) {
		this.getSetupSelectorToolbar().setEnabled(enable);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#isEnabledSetupSelector()
	 */
	@Override
	public boolean isEnabledSetupSelector() {
		return this.getSetupSelectorToolbar().isEnabled();
	}
	

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#addToolbarComponent(java.lang.Object)
	 */
	@Override
	public void addToolbarComponent(Object myComponent) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#addToolbarComponent(java.lang.Object, int)
	 */
	@Override
	public void addToolbarComponent(Object myComponent, int indexPosition) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method can be used in order to add an individual menu button to the toolbar.
	 * @param myComponent the my component
	 */
	private void addJToolbarComponent(JComponent myComponent) {
		this.addJToolbarComponent(myComponent, null);
	}
	/**
	 * This method can be used in order to add an individual menu button a specified index position of the toolbar.
	 * @param myComponent the my component
	 * @param indexPosition the index position
	 */
	private void addJToolbarComponent(JComponent myComponent, Integer indexPosition) {
		int nElements = this.getJToolBarApplication().getComponentCount();
		if (indexPosition==null || indexPosition > (nElements-1)) {
			this.getJToolBarApplication().add(myComponent);
		} else {
			this.getJToolBarApplication().add(myComponent, (int)indexPosition);
		}
		this.getJToolBarApplication().validate();
		this.getJToolBarApplication().repaint();
	}
	/**
	 * This method removes a menu button from the toolbar.
	 * @param myComponent the my component
	 */
	public void removeJToolbarComponent(JComponent myComponent) {
		this.getJToolBarApplication().remove(myComponent);
		this.validate();
	}
	// ------------------------------------------------------------
	// --- Create Toolbar - END -----------------------------------
	// ------------------------------------------------------------

	@Override
	public void removeComponent(Object component) {
		// TODO Auto-generated method stub
		
	}
	
	
	// ------------------------------------------------------------
	// --- Toolbar "Close-Button" ---------------------------------
	// ------------------------------------------------------------
	/**
	 * Gets the j tool bar close project.
	 * @return the jToolBarCloseProject
	 */
	private JToolBar getJToolBarCloseProject() {
		if (jToolBarCloseProject == null) {
			jToolBarCloseProject = new JToolBar("ProjectControl");
			jToolBarCloseProject.setFloatable(false);
			jToolBarCloseProject.setRollover(true);
			jToolBarCloseProject.add(this.getJButtonCloseProject());
		}
		return jToolBarCloseProject;
	}
	/**
	 * Gets the project close button.
	 * @return the close button
	 */
	private JButton getJButtonCloseProject() {
		if (jButtonCloseProject == null) {
			jButtonCloseProject = new JToolBarButton("ProjectClose", Language.translate("Projekt schließen"), null, "MBclose.png");
			jButtonCloseProject.setText("");
			jButtonCloseProject.setToolTipText(Language.translate("Projekt schließen"));
			jButtonCloseProject.setBorder(null);
			jButtonCloseProject.setMargin(new Insets(0, 0, 0, 0));
			jButtonCloseProject.setPreferredSize(new Dimension(26, 26));
			jButtonCloseProject.setIcon(iconCloseDummy);
		}
		return jButtonCloseProject;
	}
	/**
	 * Returns the close button ImageIcon.
	 * @return the close button image icon
	 */
	private ImageIcon getCloseButtonImageIcon() {
		if (iconCloseProject==null || this.getJMenuMainProject().getForeground().equals(this.textColor)==false) {
			
			// --- Load the base image first ------------------------
			iconCloseProject = GlobalInfo.getInternalImageIcon("MBclose.png");
			try {
				// --- Get current text color and exchange color ----
				this.textColor = this.getJMenuMainProject().getForeground();
				BufferedImage bice = ImageHelper.convertToBufferedImage(iconCloseProject.getImage());
				bice = ImageHelper.exchangeColor(bice, Color.black, this.textColor);
				// --- Remind icon for later call ------------------- 
				iconCloseProject = new ImageIcon(bice);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} 
		return iconCloseProject;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setCloseProjectButtonVisible(boolean)
	 */
	@Override
	public void setCloseProjectButtonVisible(boolean setVisible) {

		if (this.getJButtonCloseProject().isVisible() == false) {
			this.getJButtonCloseProject().setVisible(true);
		}
		if (setVisible == true) {
			this.getJButtonCloseProject().setIcon(this.getCloseButtonImageIcon());
			this.getJButtonCloseProject().setEnabled(true);
		} else {
			this.getJButtonCloseProject().setIcon(this.iconCloseDummy);
			this.getJButtonCloseProject().setEnabled(false);
		}

		if (jPanelToolBar != null) {
			jPanelToolBar.validate();
			jPanelToolBar.repaint();
		}
	}
	
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
			
			String actCMD = ae.getActionCommand();
			// ------------------------------------------------
			if (actCMD.equalsIgnoreCase("New")) {
				Application.getProjectsLoaded().add(true);

			} else if (actCMD.equalsIgnoreCase("Open")) {
				Application.getProjectsLoaded().add(false);

			} else if (actCMD.equalsIgnoreCase("Save")) {
				Project currentProject = Application.getProjectFocused();
				if (currentProject!=null) currentProject.save();

			// ------------------------------------------------
			} else if (actCMD.equalsIgnoreCase("ViewConsole")) {
				MainWindow.this.doSwitchConsole();

			// ------------------------------------------------
			} else if (actCMD.equalsIgnoreCase("DatabaseConnections")) {
				Application.showDatabaseDialog(null);
				
			// ------------------------------------------------
			} else if (actCMD.equalsIgnoreCase("JadeStart")) {
				Application.getJadePlatform().doStartInDedicatedThread();

			} else if (actCMD.equalsIgnoreCase("JadeStop")) {
				Application.getJadePlatform().stop(true);

			} else if (actCMD.equalsIgnoreCase("StartAgent")) {
				MainWindow.this.startAgent();

			} else if (actCMD.equalsIgnoreCase("JadeTools")) {
				showJPopupMenuJadeTools();

			} else if (actCMD.equalsIgnoreCase("ContainerMonitoring")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.LoadMonitor, null);
			} else if (actCMD.equalsIgnoreCase("ThreadMonitoring")) {
				Application.getJadePlatform().startSystemAgent(SystemAgent.ThreadMonitor, null);

				// ------------------------------------------------
			} else if (actCMD.equalsIgnoreCase("SimulationStart")) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Start;
				Application.getJadePlatform().startSystemAgent(SystemAgent.SimStarter, null, startWith, true);
				MainWindow.this.setEnabledSimStart(false);
				
			} else if (actCMD.equalsIgnoreCase("SimulationPause")) {
				Object[] startWith = new Object[1];
				startWith[0] = LoadExecutionAgent.BASE_ACTION_Pause;
				Application.getJadePlatform().startSystemAgent(SystemAgent.SimStarter, null, startWith, true);
				MainWindow.this.setEnabledSimPause(false);
				
			} else if (actCMD.equalsIgnoreCase("SimulationStop")) {
				Application.getJadePlatform().stop(true);

			} else if (actCMD.equalsIgnoreCase("ProjectClose")) {
				Project currProject = Application.getProjectFocused();
				if (currProject!=null) currProject.close();
				
			} else if (actCMD.equalsIgnoreCase("Test")) {
				MainWindow.this.testMethod();
				
				// ------------------------------------------------
			} else {
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
			}
			

		};
	};

	// -------------------------------------------------------------------
	// --- Methods to control the Buttons for the simulation control -----
	// -------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#isEnabledSimStart()
	 */
	@Override
	public boolean isEnabledSimStart() {
		return jButtonSimStart.isEnabled();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setEnabledSimStart(boolean)
	 */
	@Override
	public void setEnabledSimStart(boolean enable) {
		jButtonSimStart.setEnabled(enable);
		jMenuItemSimStart.setEnabled(enable);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#isEnabledSimPause()
	 */
	@Override
	public boolean isEnabledSimPause() {
		return jButtonSimPause.isEnabled();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setEnabledSimPause(boolean)
	 */
	@Override
	public void setEnabledSimPause(boolean enable) {
		jButtonSimPause.setEnabled(enable);
		jMenuItemSimPause.setEnabled(enable);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#isEnabledSimStop()
	 */
	@Override
	public boolean isEnabledSimStop() {
		return jButtonSimStop.isEnabled();
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setEnabledSimStop(boolean)
	 */
	@Override
	public void setEnabledSimStop(boolean enable) {
		jButtonSimStop.setEnabled(enable);
		jMenuItemSimStop.setEnabled(enable);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#setSimulationReady2Start()
	 */
	@Override
	public void setSimulationReady2Start() {
		this.setEnabledSimStart(true);
		this.setEnabledSimPause(false);
		this.setEnabledSimStop(false);
		if (Application.getProjectFocused()!=null) {
			this.setEnabledSetupSelector(true);
		}
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMainWindow#isSimulationReady2Start()
	 */
	@Override
	public boolean isSimulationReady2Start() {
		return this.isEnabledSimStart()==true && isEnabledSimPause()==false && isEnabledSimStop()==false;
	}
	
	
	// --------------------------------------------------------------
	// --- Start the PlatformStatusDialog to show JADE state --------
	// --------------------------------------------------------------
	/**
	 * Returns the platform status dialog.
	 * @return the platform status dialog
	 */
	public PlatformStatusDialog getPlatformStatusDialog() {
		if (platformStatusDialog==null) {
			platformStatusDialog = new PlatformStatusDialog(this);
		}
		return platformStatusDialog;
	}
	
	
	// --------------------------------------------------------------
	// --- View adjustments for projects ------------------ Start ---
	// --------------------------------------------------------------
	/**
	 * Configures the appearance of the application, depending on the current project configuration
	 */
	public void setProjectView() {
		// --- 1. Set Setup-Selector and Tools --------------------------------
		this.getSetupSelectorToolbar().setProject(Application.getProjectFocused());
		this.configureJToggleButtonProjectTree(Application.getProjectFocused());
		// --- 2. Rebuild the view to the Items in MenuBar 'Window' -----------
		this.setProjectMenuItems();
		// --- 3. Set the right value to the MenueBar 'View' ------------------
		this.setProjectView4DevOrUser();
		// --- 4. Set project tabs visible or not -----------------------------
		this.setProjectTabHeaderInVisible();
	}
	
	/**
	 * According to the current project settings, sets the project tab header visible or invisible.
	 */
	private void setProjectTabHeaderInVisible() {
		
		Project project = Application.getProjectFocused();
		if (project!=null) {
			project.getProjectEditorWindow().setProjectTabHeaderVisible(project.isProjectTabHeaderVisible());
		}
	}
	
	/**
	 * Configures the View for menue 'view' -> 'Developer' or 'End user' 
	 */
	private void setProjectView4DevOrUser() {
		
		
		JRadioButtonMenuItem viewDeveloper = this.getJRadioButtonMenuItemViewDeveloper(); 
		JRadioButtonMenuItem viewEndUser = this.getJRadioButtonMenuItemViewEndUser(); 
		JMenuItem viewProjectTree = this.getJMenuItemViewTree();
		JMenuItem viewProjectTabHeader = this.getJMenuItemViewTabHeader();
		
		boolean isEnabled = Application.getProjectsLoaded().count()>0;
		viewDeveloper.setEnabled(isEnabled);
		viewEndUser.setEnabled(isEnabled);
		viewProjectTree.setEnabled(isEnabled);
		viewProjectTabHeader.setEnabled(isEnabled); 
		
		if (isEnabled==true) {
			// --- Select the right item in relation  to the project ----------
			String viewConfigured = Application.getProjectFocused().getProjectView();
			if (viewConfigured.equalsIgnoreCase(Project.VIEW_User)) {
				viewDeveloper.setSelected(false);
				viewEndUser.setSelected(true);
			} else {
				viewEndUser.setSelected(false);
				viewDeveloper.setSelected(true);
			}
			Application.getProjectFocused().getProjectEditorWindow().setViewForDeveloperOrEndUser();
		}
	}
	/**
	 * Create's the Window=>MenuItems depending on the open projects 
	 */
	private void setProjectMenuItems() {
		
		int nProjectLoaded = Application.getProjectsLoaded().count();
		
		JMenu jMenueWindows = this.getJMenuMainWindow();
		jMenueWindows.removeAll();
		if (nProjectLoaded==0 ){
			jMenueWindows.add(new JMenuItmen_Window( Language.translate("Kein Projekt geöffnet !"), -1, true));
		} else {
			for (int i=0; i<nProjectLoaded; i++) {
				String projectName = Application.getProjectsLoaded().getProjectsOpen().get(i).getProjectName();
				boolean setFontBold = projectName.equalsIgnoreCase(Application.getProjectFocused().getProjectName());
				jMenueWindows.add(new JMenuItmen_Window( projectName, i, setFontBold));
			}		
		}
	}	
	/**
	 * Creates a single MenueItem for the Window-Menu depending on the open projects.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private class JMenuItmen_Window extends JMenuItem  {
 
		private static final long serialVersionUID = 1L;
		
		private JMenuItmen_Window( String ProjectName, int windowIndex, boolean setBold  ) {
			
			final int winIndex = windowIndex;
			int winNo = windowIndex + 1;
			
			if ( winNo <= 0 ) {
				this.setText(ProjectName);
			} else {
				this.setText( winNo + ": " + ProjectName );
			}
			
			if ( setBold ) {
				Font cfont = this.getFont();
				if ( cfont.isBold() == true ) {
					this.setForeground( Application.getGlobalInfo().ColorMenuHighLight() );	
				}
				else {
					this.setFont( cfont.deriveFont(Font.BOLD) );
				}
			}
			this.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Application.getProjectsLoaded().get( winIndex ).setFocus(true);						
				}
			});		
		}
	}
	// --------------------------------------------------------------
	// --- View adjustments for projects ------------------ End -----
	// --------------------------------------------------------------

	
	// ----------------------------------------------------
	// --- Test and debug area ------------------ Start ---
	// ----------------------------------------------------
	private boolean showTestMenuButton = false;
	
	/**
	 * This is just a test method that can be invoked if the local variable {@link #showTestMenuButton} is set true.
	 */
	private void testMethod() {
		
		DirectoryDialog dirDialog = new DirectoryDialog(new File(Application.getGlobalInfo().getPathProjects()));
		
		System.out.println("=> Files found: " + dirDialog.getDirectoryEvaluator().getFilesFound().size());
		System.out.println("Files included: " + dirDialog.getDirectoryEvaluator().getFileList(true).size()); 
		System.out.println("Files excluded: " + dirDialog.getDirectoryEvaluator().getFileList(false).size());
		System.out.println();
		
	}
	
	/**
	 * Prints the class name of the focus owning component and its parent.
	 */
	public void printFocusOwner() {
		
		String message = null;
		String messageIntro = "" + this.getClass().getSimpleName() + "#printFocusOwner() => ";

		Component focusComp = this.getFocusOwner();
		if (focusComp==null) {
			message = messageIntro + "No focussed component found!";
		} else {
		
			String parentQueue = "";
			Container parentContainer = focusComp.getParent(); 
			while (parentContainer!=null && parentContainer!=this) {
				if (parentQueue.isEmpty()==false) {
					parentQueue+= ", ";
				}
				parentQueue+= parentContainer.getParent().getClass().getSimpleName();
				parentContainer = parentContainer.getParent();
			}
			message = messageIntro + "Focus is on component " + focusComp.getClass().getName() + " - Parents: " + parentQueue;
		}
		System.err.println(message);
	}
	// ----------------------------------------------------
	// --- Test and debug area -------------------- End ---
	// ----------------------------------------------------

	
} // -- End Class ---
