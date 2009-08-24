package application;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import mas.Plattform;
import gui.CoreWindow;
/**
 * Diese Klasse verwaltet alle 	 
 * @author: Christian Derksen
 * @Version 0.1  	
 */
public class Application {
	
	public static GlobalInfo RunInfo = new GlobalInfo();
	public static CoreWindow MainWindow;
	public static Plattform JadePlatform = new Plattform();
	
	public static void main( String[] args ) {
		// --- Start Application -----------------------
		System.out.println( Language.translate("Programmstart ..." ) );
		startApplication();
		System.out.println( Language.translate("Fertig") );
		MainWindow.setStatusBar( Language.translate("Fertig") );		
	}	
	
	public static void startApplication() {
 		// --- open Main-Dialog ------------------------		
		MainWindow = new CoreWindow();		
		MainWindow.setDefaultCloseOperation(CoreWindow.DO_NOTHING_ON_CLOSE);
		MainWindow.setTitle( RunInfo.AppTitel() );
		MainWindow.getContentPane().setPreferredSize(MainWindow.getSize());
		MainWindow.pack();
		MainWindow.setLocationRelativeTo(null);
		if ( RunInfo.AppLnF() != null ) setLookAndFeel( RunInfo.AppLnF() );
		//MainWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
		MainWindow.setVisible(true);	
	}
	
	public static void quit() {
		// --- Anwendung beenden ---------------------- 
		JadePlatform.jadeStop();
		Language.SaveDictionaryFile();
		System.out.println( Language.translate("Programmende ... ") );
		System.exit(0);		
	}

	public static void setLookAndFeel( String NewLnF ) {
		// --- Look and fell einstellen --------------- 
		if (NewLnF == null) return;		
		RunInfo.setAppLnf( NewLnF );	
		System.out.println( "Setting LnF to '" + NewLnF + "'");
		try {
			String lnfClassname = RunInfo.AppLnF();
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
				UIManager.setLookAndFeel(lnfClassname);
				SwingUtilities.updateComponentTreeUI(MainWindow);
		} 
		catch (Exception e) {
				System.err.println("Cannot install " + RunInfo.AppLnF()
					+ " on this platform:" + e.getMessage());
		}
		  
	}

	
}

