package application;

import mas.Plattform;
import gui.CoreWindow;
/**
 * Diese Klasse verwaltet alle 	 
 * @author: Christian Derksen
 * @Version 0.1  	
 */
public class Application {
	
	public static CoreWindow MainWindow;
	public static GlobalInfo RunInfo = new GlobalInfo();
	
	public static ProjectsLoaded Projects = new ProjectsLoaded();
	public static Project ProjectCurr = null;
		
	public static Plattform JadePlatform = new Plattform();
	
	/**
	 * main-method for the start of the application 
	 * @param args
	 */
	public static void main( String[] args ) {
		// --- Start Application -----------------------
		System.out.println( Language.translate("Programmstart ..." ) );
		startApplication();
		MainWindow.setStatusBar( Language.translate("Fertig") );
		Projects.setProjectMenuItems();
	}	

	/**
	 * Opens the Main-Window (JFrame)
	 */
	public static void startApplication() {
 		// --- open Main-Dialog ------------------------		
		MainWindow = new CoreWindow();		
	}
	/**
	 * Quits the application
	 */
	public static void quit() {
		// --- Anwendung beenden ---------------------- 
		JadePlatform.jadeStop();
		Language.SaveDictionaryFile();
		System.out.println( Language.translate("Programmende ... ") );
		System.exit(0);		
	}

	/**
	 * Adds a complementation to the application title
	 * @param Add2BasicTitel
	 */
	public static void setTitelAddition( String Add2BasicTitel ) {
		MainWindow.setTitelAddition(Add2BasicTitel);
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
		Language.changeLanguageTo(NewLang);		
	}	

} // --- End Class ---

