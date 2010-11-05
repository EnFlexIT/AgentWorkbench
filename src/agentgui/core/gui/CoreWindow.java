package agentgui.core.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;

/**
 * Main User-Interface der Anwendung
 * 
 * @author Christin Derksen
 */
public class CoreWindow extends JFrame implements ComponentListener{

	private static final long serialVersionUID = 1L;

	final static String PathImage = Application.RunInfo.PathImageIntern();
	
	private final ImageIcon iconAgentGUI = new ImageIcon( this.getClass().getResource( PathImage + "AgentGUI.png") );
	private final Image imageAgentGUI = iconAgentGUI.getImage();
	
	private final ImageIcon iconGreen = new ImageIcon( this.getClass().getResource( PathImage + "StatGreen.png") );
	private final ImageIcon iconRed = new ImageIcon( this.getClass().getResource( PathImage + "StatRed.png") );
	private final ImageIcon iconClose = new ImageIcon( this.getClass().getResource( PathImage + "MBclose.png") );
	private final ImageIcon iconCloseDummy = new ImageIcon( this.getClass().getResource( PathImage + "MBdummy.png") );
	
	private static JLabel StatusBar;	
	private JLabel StatusJade;
	
	public JSplitPane SplitProjectDesktop;
	public JDesktopPane ProjectDesktop;	
	public JEditorPane ConsoleText;
	private int ConsoleHeight;
	
	private JMenuBar jMenuBarMain;
	private JMenu jMenuMainProject;
	private JMenu jMenuMainView;
	private JMenu jMenuMainJade;
	private JMenu jMenuExtra;
		private JMenu jMenuExtraLang;
		private JMenu jMenuExtraLnF;
	public JMenu jMenuMainWindow;
	private JMenu jMenuMainHelp;

	private static int jMenuMainHelpPositionLeft;
	private JLabel jMenuSpacer ;
	private JMenuItem jMenuCloseButton;
	
	private JToolBar jToolBarApp;
		private JButton JadeTools;	
		private JPopupMenu JadeToolsPopUp;

	// ------------------------------------------------------------		
	// --- Start -------------------------------------------------- 
	// ------------------------------------------------------------
	public CoreWindow() {
		
		// --- Set the IconImage ----------------------------------
		this.setIconImage(imageAgentGUI);
		
		// --- Set the Look and Feel of the Application -----------
		if ( Application.RunInfo.AppLnF() != null ) {
			setLookAndFeel( Application.RunInfo.AppLnF() );
		}
		
		// --- Create the Main-Elements of the Application --------
		initComponents();
		
		this.setDefaultCloseOperation(CoreWindow.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setPreferredSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.pack();		
		
		//this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setVisible(true);
		setTitelAddition("");
		setCloseButtonPosition( false );
		
		// --- Console einstellen --------------------------------- 
		ConsoleHeight = SplitProjectDesktop.getHeight() / 4; 
		SplitProjectDesktop.setDividerLocation( SplitProjectDesktop.getHeight() - ConsoleHeight );
		if ( Application.RunInfo.isAppUseInternalConsole() == false ) { 
			ConsoleSetVisible(false);
		}
	}
	// ------------------------------------------------------------	

	
	// ------------------------------------------------------------
	// --- Initialisierung des Fensters - START -------------------
	// ------------------------------------------------------------
	private void initComponents() {

		// --- Standardeinstellungen ---
		this.setJMenuBar(getJMenuBarProject());
		this.add( getJToolBarApp(), BorderLayout.NORTH );
		this.add( getStatusBar(), BorderLayout.SOUTH );
		this.add( getMainSplitpane() );
		this.setSize(1100, 640);	
		
		// --- Listener für das Schließen der Applikation ----
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
			Application.quit();
			}
		});
		this.addComponentListener(this);
	}	
	// ------------------------------------------------------------
	// --- Initialisierung des Fensters - ENDE --------------------
	// ------------------------------------------------------------

	
	// ------------------------------------------------------------
	// --- Statusanzeigen etc. definieren - START -----------------
	// ------------------------------------------------------------
	private JPanel getStatusBar() {
	    
		// --- Linker Teil ----------------------
		StatusBar = new JLabel();			
		StatusBar.setPreferredSize( new Dimension(300, 16) );
		StatusBar.setFont(new Font( "Dialog", Font.PLAIN, 12) );
		
		// --- Mittlerer Teil -------------------
		StatusJade = new JLabel();
		StatusJade.setPreferredSize( new Dimension(200, 16) );
		StatusJade.setFont(new Font( "Dialog", Font.PLAIN, 12) );
		StatusJade.setHorizontalAlignment( SwingConstants.RIGHT );
		setStatusJadeRunning( false );
		
	    // --- Rechter Teil --------------------- 
	    JPanel RightPart = new JPanel( new BorderLayout() );
	    RightPart.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
	    RightPart.setOpaque( false );
	    		
	    // --- StatusBar zusammenbauen ------------------
		JPanel JPStat = new JPanel( new BorderLayout() );
		JPStat.setPreferredSize(new Dimension(10, 23));
		JPStat.add(StatusBar, BorderLayout.WEST); 
		JPStat.add(StatusJade, BorderLayout.CENTER);
		JPStat.add(RightPart, BorderLayout.EAST);
		return JPStat;
	}
	public void setStatusBar(String Message) {
		if ( Message == null ) {
			StatusBar.setText("  ");
		}
		else {
			StatusBar.setText("  " + Message);
		};	
	}
	public void setTitelAddition( String Add2BasicTitel ) {
		if ( Add2BasicTitel != "" ) {
			this.setTitle( Application.RunInfo.AppTitel() + ": " + Add2BasicTitel );	
		}
		else {
			this.setTitle( Application.RunInfo.AppTitel() );
		}
	}
	public void setStatusJadeRunning(boolean runs) {
		if ( runs == false ) { 
			StatusJade.setText( Language.translate("JADE wurde noch nicht gestartet.") );
			StatusJade.setIcon(iconRed);			
		}
		else {
			StatusJade.setText( Language.translate("JADE wurde lokal gestartet.") );
			StatusJade.setIcon(iconGreen);
		};		
	}
	public void setLookAndFeel( String NewLnF ) {
		// --- Look and fell einstellen --------------- 
		if ( NewLnF == null ) return;		
		Application.RunInfo.setAppLnf( NewLnF );
		try {
			String lnfClassname = Application.RunInfo.AppLnF();
			if (lnfClassname == null) {
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			UIManager.setLookAndFeel(lnfClassname);
			SwingUtilities.updateComponentTreeUI(this);				
		} 
		catch (Exception e) {
				System.err.println("Cannot install " + Application.RunInfo.AppLnF()
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
	public boolean ConsoleIsVisible() {
		// --- Umschalten der Consolen-Ansicht --------------------
		if ( ConsoleText.isVisible() == true ) {
			return true;
		} else {
			return false;
		}		
	}
	public void ConsoleSwitch() {
		// --- Umschalten der Consolen-Ansicht --------------------
		if ( ConsoleText.isVisible() == true ) {
			this.ConsoleSetVisible(false);
		} else {
			this.ConsoleSetVisible(true);
		}
	}
	private void ConsoleSetVisible(boolean show) {
		// --- Ein- und ausblenden der Console --------------------
		if (show == true) {
			// --- System.out.println("Console einblenden ...");
			SplitProjectDesktop.setDividerLocation( SplitProjectDesktop.getHeight() - ConsoleHeight );
			SplitProjectDesktop.setDividerSize(5);
			ConsoleText.setVisible(true);			
		} else {
			// --- System.out.println("Console ausblenden ...");			
			ConsoleHeight = SplitProjectDesktop.getHeight() - SplitProjectDesktop.getDividerLocation(); 
			SplitProjectDesktop.setDividerLocation( SplitProjectDesktop.getHeight() );			
			SplitProjectDesktop.setDividerSize( 0 );
			ConsoleText.setVisible(false);	
		}
		this.validate();
		if ( Application.Projects.count() != 0 ) {
			Application.ProjectCurr.setMaximized();
		}
	}
	/**
	 * This method sets back the focus to this JFrame 
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
	private JSplitPane getMainSplitpane() {
		if (SplitProjectDesktop == null ) {
			// --- JEditorPane aus Application-Objekt übernehmen --
			ConsoleText = Application.Console;
			
			// --- Panel für Text und Button ----------------------
			JPanel ConsolePanel = new JPanel();
			ConsolePanel.setLayout(new BorderLayout());
			ConsolePanel.add(new JScrollPane(ConsoleText),BorderLayout.CENTER);
			
			SplitProjectDesktop = new JSplitPane();
			SplitProjectDesktop.setOrientation(JSplitPane.VERTICAL_SPLIT);
			SplitProjectDesktop.setDividerSize(5);
			SplitProjectDesktop.setResizeWeight(1);
			SplitProjectDesktop.setOneTouchExpandable(false);
			SplitProjectDesktop.setTopComponent(getJDesktopPaneProjectDesktop());			
			SplitProjectDesktop.setBottomComponent(ConsolePanel);
			SplitProjectDesktop.addPropertyChangeListener(new PropertyChangeListener() {
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
		return SplitProjectDesktop;		
	}
	private JDesktopPane getJDesktopPaneProjectDesktop() {
		if (ProjectDesktop == null) {
			ProjectDesktop = new JDesktopPane();
			ProjectDesktop.setDoubleBuffered(false);
		}
		return ProjectDesktop;
	}
	// ------------------------------------------------------------
	// --- Desktop der Anwendung definieren - ENDE ----------------
	// ------------------------------------------------------------

	
	// ------------------------------------------------------------
	// --- Menüleistendefinition - START --------------------------
	// ------------------------------------------------------------
	private JMenuBar getJMenuBarProject() {
		if (jMenuBarMain == null) {
			jMenuBarMain = new JMenuBar();
			jMenuBarMain.add( getjMenuMainProject() );
			jMenuBarMain.add( getjMenuMainView() );
			jMenuBarMain.add( getjMenuMainJade() );
			jMenuBarMain.add( getjMenuMainExtra() );
			jMenuBarMain.add( getjMenuMainWindow() );
			jMenuBarMain.add( getjMenuMainHelp() );	
			jMenuBarMain.add( getMenuSpacer() );
			jMenuBarMain.add( getCloseButton() );
		}
		return jMenuBarMain;
	}

	// ------------------------------------------------------------
	// --- Menü Projekte ------------------------------------------
	// ------------------------------------------------------------
	private JMenu getjMenuMainProject() {
		if (jMenuMainProject == null) {
			jMenuMainProject = new JMenu();
			jMenuMainProject.setText(Language.translate("Projekte"));			
			jMenuMainProject.add( new CWMenueItem( "ProjectNew", Language.translate("Neues Projekt"), "MBnew.png" )) ;
			jMenuMainProject.add( new CWMenueItem( "ProjectOpen", Language.translate("Projekt öffnen"), "MBopen.png" )) ;
			jMenuMainProject.add( new CWMenueItem( "ProjectClose", Language.translate("Projekt schließen"), "MBclose.png" )) ;
			jMenuMainProject.addSeparator();
			jMenuMainProject.add( new CWMenueItem( "ProjectSave", Language.translate("Projekt speichern"), "MBsave.png" )) ;
			jMenuMainProject.addSeparator();
			jMenuMainProject.add( new CWMenueItem( "ApplicationQuit", Language.translate("Beenden"), null )) ;			
		}
		return jMenuMainProject;
	}
	// ------------------------------------------------------------
	// --- Menü "JADE" --------------------------------------------
	// ------------------------------------------------------------
	private JMenu getjMenuMainView() {
		if (jMenuMainView == null) {
			jMenuMainView = new JMenu();
			jMenuMainView.setText(Language.translate("Ansicht"));
			jMenuMainView.add( new CWMenueItem( "ViewConsole", Language.translate("Konsole ein- oder ausblenden"), "MBConsole.png" )) ;			
		}
		return jMenuMainView;
	}
	// ------------------------------------------------------------
	// --- Menü "JADE" --------------------------------------------
	// ------------------------------------------------------------
	private JMenu getjMenuMainJade() {
		if (jMenuMainJade == null) {
			jMenuMainJade = new JMenu();
			jMenuMainJade.setText(Language.translate("JADE"));			
			jMenuMainJade.add( new CWMenueItem( "JadeStart", Language.translate("JADE starten"), "MBJadeOn.png" )) ;
			jMenuMainJade.add( new CWMenueItem( "JadeStop", Language.translate("JADE stoppen"), "MBJadeOff.png" )) ;
			jMenuMainJade.addSeparator();
			jMenuMainJade.add( new CWMenueItem( "PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) öffnen"), "MBJadeRMA.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopDF", Language.translate("DF anzeigen"), "MBJadeDF.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopIntrospec", Language.translate("Introspector-Agent starten"), "MBJadeIntrospector.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopLog", Language.translate("Log-Manager starten"), "MBJadeLogger.gif" )) ;
		}
		return jMenuMainJade;
	}
	
	// ------------------------------------------------------------
	// --- Menü Extras ---------------------------------------------
	// ------------------------------------------------------------
	private JMenu getjMenuMainExtra() {
		if (jMenuExtra == null) {
			jMenuExtra = new JMenu();
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
		private void setjMenuExtraLang() {
			
			String[] DictLineValues = Language.getLanguages(); 
			boolean setBold = false;
			for(int i=0; i<DictLineValues.length; i++) {
				if ( i == Language.DefaultLanguage ) 
					setBold = true;
				else 
					setBold = false;					
				jMenuExtraLang.add( new JMenuItemLang(DictLineValues[i], setBold) );
			};
		}
		// --- Unterklasse für die verfügbaren Sprachen  --------------
		private class JMenuItemLang extends JMenuItem implements ActionListener {
			 
			private static final long serialVersionUID = 1L;
			
			private JMenuItemLang( String LangHeader, boolean setBold ) {
				this.setText( Language.getLanguagesHeaderInGerman( LangHeader.toUpperCase() ) );			
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
			public void actionPerformed(ActionEvent evt) {
				String ActCMD = evt.getActionCommand();	
				Application.setLanguage(ActCMD);							
			}
		}
		// ------------------------------------------------------------		
		
		// ------------------------------------------------------------
		// --- Look and Feel ------------------------------------------
		// ------------------------------------------------------------
		private void setjMenuExtraLnF() {

			boolean setBold = false;
			UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();
			
			for (int i = 0, n = plaf.length; i < n; i++) {
				if ( plaf[i].getClassName() == Application.RunInfo.AppLnF() )
					setBold = true;
				else
					setBold = false;
					jMenuExtraLnF.add( new JMenuItmenLnF(plaf[i].getName(), plaf[i].getClassName(), setBold) );
			    };			
		}
		// --- Unterklasse für die Look and Feel Menü-Elemente --------
		private class JMenuItmenLnF extends JMenuItem  {
	 
			private static final long serialVersionUID = 1L;
			private String LnFPath; 
			
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
	// --- Menü Fenster -------------------------------------------
	// ------------------------------------------------------------
	private JMenu getjMenuMainWindow() {
		if (jMenuMainWindow == null) {
			jMenuMainWindow = new JMenu();
			jMenuMainWindow.setText(Language.translate("Fenster"));
		}
		return jMenuMainWindow;
	}
	
	// ------------------------------------------------------------
	// --- Menü Hilfe ---------------------------------------------
	// ------------------------------------------------------------
	private JMenu getjMenuMainHelp() {
		if (jMenuMainHelp == null) {
			jMenuMainHelp = new JMenu();
			jMenuMainHelp.setText(Language.translate("Hilfe"));
			jMenuMainHelp.add( new CWMenueItem( "HelpAbout", Language.translate("Über..."), null )) ;
		}
		return jMenuMainHelp;
	}

	// ------------------------------------------------------------
	// --- Menü-Spacer für den folgenden "Close-Button" -----------
	// ------------------------------------------------------------
	private JLabel getMenuSpacer() {
		if (jMenuSpacer == null ) {
			jMenuSpacer = new JLabel();
			jMenuSpacer.setIcon(iconCloseDummy);
			jMenuSpacer.setVisible(false);			
		}
		return jMenuSpacer;
	}
	// ------------------------------------------------------------
	// --- Menü "Close-Button" ------------------------------------
	// ------------------------------------------------------------
	private JMenuItem getCloseButton() {
		if (jMenuCloseButton == null ) {
			jMenuCloseButton = new CWMenueItem( "ProjectClose", "", "MBclose.png" );
			jMenuCloseButton.setText("");
			jMenuCloseButton.setToolTipText( Language.translate("Projekt schließen") );
			jMenuCloseButton.setBorder( null );
			jMenuCloseButton.setMargin( new Insets(0, 0, 0, 0) );
			jMenuCloseButton.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
			jMenuCloseButton.setPreferredSize( new Dimension ( 30 , jMenuCloseButton.getHeight() ) );
			jMenuCloseButton.setIcon( iconCloseDummy );
			jMenuCloseButton.setEnabled( false );
			jMenuCloseButton.setVisible( false );			
		}
		return jMenuCloseButton ;
	}
	public void setCloseButtonPosition( boolean setVisible ){
		
		// --- Positionsmerker für das Fenster setzen ?  ----------
		// --- Wird nach der Initialisierung ausgewertet ----------
		if ( jMenuCloseButton.isVisible() == false ) {
			jMenuMainHelpPositionLeft = jMenuMainHelp.getLocation().x +  jMenuMainHelp.getWidth();
			jMenuCloseButton.setVisible(true);
			jMenuSpacer.setVisible(true);
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
		
		// --- Breite des jMenuSpacer's anpassen ------------------		 
		int CBoWidth = 30;
		int NewWidth = (this.getWidth() - jMenuMainHelpPositionLeft - CBoWidth ) ;
		if ( NewWidth <= 0 ) {
			jMenuSpacer.setPreferredSize( new Dimension( 0, jMenuMainProject.getHeight() ) );			
		}
		else {
			jMenuSpacer.setPreferredSize( new Dimension( NewWidth , jMenuMainProject.getHeight() ) );			
		}
		// --- Menüleiste neu zeichnen / Änderungen anzeigen ------
		if ( jMenuBarMain != null ) {
			jMenuBarMain.revalidate();
			//jMenuBarMain.updateUI();
		}						
	}	
	
	
	// ------------------------------------------------------------
	// --- Unterklasse für alle einfachen Menüelemente - START ----
	// ------------------------------------------------------------	
	private class CWMenueItem extends JMenuItem implements ActionListener {
		/**
		 * Creat's a JMenueItem for PopUp- or normal Menue's and 
		 * holds the ActionListener for them     
		 */
		private static final long serialVersionUID = 1L;

		private CWMenueItem( String actionCommand, 
							 String Text, 
							 String imgName ) {

			this.setText(Text);
			if ( imgName != null ) {
				try {
					this.setIcon( new ImageIcon( this.getClass().getResource( PathImage + imgName ) ) );
				}
				catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}				
			}
			this.addActionListener(this);
			this.setActionCommand(actionCommand);
		}

		public void actionPerformed(ActionEvent ae) {
			String ActCMD = ae.getActionCommand();	
	
			// --- Menü Projekt -------------------------------
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
			else if ( ActCMD.equalsIgnoreCase("ApplicationQuit") ) {
				Application.quit();
			}
			// --- Menü Ansicht / View ------------------------
			else if ( ActCMD.equalsIgnoreCase("ViewConsole") ) {
				Application.MainWindow.ConsoleSwitch();
			}
			// --- Menü Jade ----------------------------------
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
			// --- Menü Extras => nicht hier !! ---------------
			else if ( ActCMD.equalsIgnoreCase("ExtraBenchmark") ) {
				Application.doBenchmark(true);
			}			
			else if ( ActCMD.equalsIgnoreCase("ExtraOptions") ) {
				Application.showOptionDialog();
			}
			// --- Menü Hilfe ---------------------------------
			else if ( ActCMD.equalsIgnoreCase("HelpAbout") ) {
				Application.showAboutDialog();
			}
			else {
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
			}			
		}		
	}		
	// ------------------------------------------------------------
	// --- Unterklasse für alle einfachen Menüelemente - ENDE -----
	// ------------------------------------------------------------			
	// ------------------------------------------------------------
	// --- Menüleistendefinition - ENDE ---------------------------
	// ------------------------------------------------------------
		
		
	// ------------------------------------------------------------
	// --- Symbolleiste erstellen - START -------------------------
	// ------------------------------------------------------------
	private JToolBar getJToolBarApp() {
		/**
		 * 
		 */
		if ( jToolBarApp == null) {
			
			// --- PopUp-Menü zum Button 'JadeTools' definieren (s. u.) ---
			JadeToolsPopUp = new JPopupMenu("SubBar");
			JadeToolsPopUp.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) );
			JadeToolsPopUp.add( new CWMenueItem( "PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) öffnen"), "MBJadeRMA.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopDF", Language.translate("DF anzeigen"), "MBJadeDF.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopIntrospec", Language.translate("Introspector-Agent starten"), "MBJadeIntrospector.gif" )) ;
			JadeToolsPopUp.add( new CWMenueItem( "PopLog", Language.translate("Log-Manager starten"), "MBJadeLogger.gif" )) ;
			
			// --- Symbolleisten-Definition -------------------------------
			jToolBarApp = new JToolBar("MainBar");
			jToolBarApp.setFloatable(false);
			jToolBarApp.setRollover(true);
			
			jToolBarApp.add(new JToolBarButton( "New", Language.translate("Neues Projekt"), null, "MBnew.png" ));
			jToolBarApp.add(new JToolBarButton( "Open", Language.translate("Projekt öffnen"), null, "MBopen.png" ));
			jToolBarApp.add(new JToolBarButton( "Save", Language.translate("Projekt speichern"), null, "MBsave.png" ));
			jToolBarApp.addSeparator();
			
			jToolBarApp.add(new JToolBarButton( "ViewConsole", Language.translate("Konsole ein- oder ausblenden"), null, "MBConsole.png" ));
			jToolBarApp.addSeparator();
			
			jToolBarApp.add(new JToolBarButton( "JadeStart", Language.translate("JADE starten"), null, "MBJadeOn.png" ));
			jToolBarApp.add(new JToolBarButton( "JadeStop", Language.translate("JADE stoppen"), null, "MBJadeOff.png" ));
			JadeTools = new JToolBarButton( "JadeTools", Language.translate("JADE-Tools..."), null, "MBJadeTools.png" );
			jToolBarApp.add( JadeTools );
			jToolBarApp.addSeparator();
			
			jToolBarApp.add(new JToolBarButton( "ContainerMonitoring", Language.translate("Load Monitor öffnen"), null, "MBLoadMonitor.png" ));
			jToolBarApp.addSeparator();		
			
		};		
		return jToolBarApp;
	}	
	// ------------------------------------------------------------
	// --- Symbolleiste erstellen - ENDE --------------------------
	// ------------------------------------------------------------

	
	// ------------------------------------------------------------
	// --- Unterklasse für die Symbolleisten-Buttons --------------
	// ------------------------------------------------------------	
	public class JToolBarButton extends JButton implements ActionListener {

		private static final long serialVersionUID = 1L;
 
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
					ImageIcon ButtIcon = new ImageIcon( this.getClass().getResource( PathImage + imgName ), altText);
					this.setIcon(ButtIcon);
				}
				catch (Exception err) {
					System.err.println(Language.translate("Fehler beim Laden des Bildes: ") + err.getMessage());
				}				
			}
			this.addActionListener(this);	
			this.setActionCommand(actionCommand);
		}
		
		public void actionPerformed(ActionEvent ae) {
			// --- Fallunterscheidung 'cmd' einbauen ---
			String ActCMD = ae.getActionCommand();			
			// ------------------------------------------------
			if ( ActCMD.equalsIgnoreCase("New") ) {
				Application.Projects.add( true );
			}
			else if ( ActCMD.equalsIgnoreCase("Open") ) {
				Application.Projects.add( false );			}
			else if ( ActCMD.equalsIgnoreCase("Save") ) {
				Project CurPro = Application.ProjectCurr;
				if ( CurPro != null ) CurPro.save();
			}
			// ------------------------------------------------
			else if ( ActCMD.equalsIgnoreCase("ViewConsole") ) { 
				Application.MainWindow.ConsoleSwitch();
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
			else { 
				System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + ActCMD);
			};
			
		};
	};
		
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub				
	}
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub		
	}
	@Override
	public void componentResized(ComponentEvent e) {
		if (ConsoleIsVisible() == false) {
			SplitProjectDesktop.setDividerLocation( SplitProjectDesktop.getHeight() );			
		}
		if ( Application.Projects.count() != 0 ) {
			Application.ProjectCurr.setMaximized();
			setCloseButtonPosition( true );
		}
		else {
			setCloseButtonPosition( false );
		}
		
	}
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
	}
	
	// ------------------------------------------------------------
	// --- Unterklasse für die Schräge im unteren  
	// --- rechten Teil des Hauptfenmsters
	// ------------------------------------------------------------	
	private class AngledLinesWindowsCornerIcon implements Icon {
		  
		private final Color WHITE_LINE_COLOR = new Color(255, 255, 255);
		private final Color GRAY_LINE_COLOR = new Color(172, 168, 153);
		private static final int WIDTH = 13;
		private static final int HEIGHT = 13;

		public int getIconHeight() {
			return WIDTH;
		}

		public int getIconWidth() {
			return HEIGHT;
		}

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



