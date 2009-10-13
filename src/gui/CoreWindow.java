package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import application.Application;
import application.Project;
import application.Language;

/**
 * Main User-Interface der Anwendung
 * 
 * @author Christin Derksen
 * @version 0.5 * 
 */
public class CoreWindow extends JFrame implements ComponentListener{

	private static final long serialVersionUID = 1L;

	final static String PathImage = Application.RunInfo.PathImageIntern();
	static ImageIcon iconGreen = new ImageIcon( CoreWindow.class.getResource( PathImage + "StatGreen.png") );
	static ImageIcon iconRed = new ImageIcon( CoreWindow.class.getResource( PathImage + "StatRed.png") );
	static ImageIcon iconClose = new ImageIcon( CoreWindow.class.getResource( PathImage + "MBclose.png") );
	static ImageIcon iconCloseDummy = new ImageIcon( CoreWindow.class.getResource( PathImage + "MBdummy.png") );
	
	static JLabel StatusBar;	
	static JLabel StatusJade;
	public JDesktopPane ProjectDesktop;
	
	private JMenuBar jMenuBarMain;
	private JMenu jMenuMainProject;
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
		
		if ( Application.RunInfo.AppLnF() != null ) 
			setLookAndFeel( Application.RunInfo.AppLnF() );

		initComponents();
		this.setDefaultCloseOperation(CoreWindow.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setPreferredSize(this.getSize());
		this.setLocationRelativeTo(null);
		this.pack();		
		//this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setVisible(true);
		setTitelAddition("");
		setCloseButtonPosition( false );
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
		this.add( getJDesktopPaneProjectDesktop() );
		this.setSize(900, 600);	
		
		// --- Listener für das Schliessen der Applikation ----
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
			StatusJade.setText( Language.translate("Jade wurde noch nicht gestartet.") );
			StatusJade.setIcon(iconRed);			
		}
		else {
			StatusJade.setText( Language.translate("Jade wurde lokal gestartet.") );
			StatusJade.setIcon(iconGreen);
		};		
	}
	public void setLookAndFeel( String NewLnF ) {
		// --- Look and fell einstellen --------------- 
		if ( NewLnF == null ) return;		
		Application.RunInfo.setAppLnf( NewLnF );
		try {
			String lnfClassname = Application.RunInfo.AppLnF();
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
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
	// ------------------------------------------------------------
	// --- Statusanzeigen etc. definieren - ENDE ------------------
	// ------------------------------------------------------------
	
	
	// ------------------------------------------------------------
	// --- Desktop der Anwendung definieren - START ---------------
	// ------------------------------------------------------------
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
			jMenuMainProject.add( new CWMenueItem( "ProjectClose", Language.translate("Projekt schliessen"), "MBclose.png" )) ;
			jMenuMainProject.addSeparator();
			jMenuMainProject.add( new CWMenueItem( "ProjectSave", Language.translate("Projekt speichern"), "MBsave.png" )) ;
			jMenuMainProject.addSeparator();
			jMenuMainProject.add( new CWMenueItem( "ApplicationQuit", Language.translate("Beenden"), null )) ;			
		}
		return jMenuMainProject;
	}
	// ------------------------------------------------------------
	// --- Menü "Jade" --------------------------------------------
	// ------------------------------------------------------------
	private JMenu getjMenuMainJade() {
		if (jMenuMainJade == null) {
			jMenuMainJade = new JMenu();
			jMenuMainJade.setText(Language.translate("Jade"));			
			jMenuMainJade.add( new CWMenueItem( "JadeStart", Language.translate("Jade starten"), "MBJadeOn.png" )) ;
			jMenuMainJade.add( new CWMenueItem( "JadeStop", Language.translate("Jade stoppen"), "MBJadeOff.png" )) ;
			jMenuMainJade.addSeparator();
			jMenuMainJade.add( new CWMenueItem( "PopRMAStart", Language.translate("RMA (Remote Monitoring Agent) öffnen"), "MBJadeRMA.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopSniffer", Language.translate("Sniffer-Agenten starten"), "MBJadeSniffer.gif" )) ;
			jMenuMainJade.add( new CWMenueItem( "PopDummy", Language.translate("Dummy-Agenten starten"), "MBJadeDummy.gif" )) ;
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
				Language.changeLanguageTo( ActCMD );			
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
			jMenuMainHelp.add( new CWMenueItem( "HelpAbout", Language.translate("Über ..."), null )) ;
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
			//jMenuSpacer.setText("Hallo");
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
			jMenuCloseButton.setToolTipText( Language.translate("Projekt schliessen") );
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
			else if ( ActCMD.equalsIgnoreCase("PopIntrospec") ) {
				Application.JadePlatform.jadeSystemAgentOpen("introspector", null);
			}
			else if ( ActCMD.equalsIgnoreCase("PopLog") ) {
				Application.JadePlatform.jadeSystemAgentOpen("log", null);
			}
			// --- Menü Extras => nicht hier !! ---------------
			// --- Menü Hilfe ---------------------------------
			else if ( ActCMD.equalsIgnoreCase("HelpAbout") ) {
				
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

			jToolBarApp.add(new JToolBarButton( "JadeStart", Language.translate("Jade starten"), null, "MBJadeOn.png" ));
			jToolBarApp.add(new JToolBarButton( "JadeStop", Language.translate("Jade stoppen"), null, "MBJadeOff.png" ));
			JadeTools = new JToolBarButton( "JadeTools", Language.translate("Jade-Tools ..."), null, "MBJadeTools.png" );
			jToolBarApp.add( JadeTools );
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
	private class JToolBarButton extends JButton implements ActionListener {

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
			else if ( ActCMD.equalsIgnoreCase("JadeStart") ) { 
				Application.JadePlatform.jadeStart();				
			}
			else if ( ActCMD.equalsIgnoreCase("JadeStop") ) {
				Application.JadePlatform.jadeStop();				
			}
			else if ( ActCMD.equalsIgnoreCase("JadeTools") ) { 
				JadeToolsPopUp.show( JadeTools, 0, JadeTools.getHeight() );
			}
			else if ( ActCMD.equalsIgnoreCase("") ) { 
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



