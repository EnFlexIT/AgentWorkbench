package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import application.Application;

public class Language {

	// -------------------------------------------------------------------------
	// --- Klassenvariablen ----------------------------------------------------
	// -------------------------------------------------------------------------
	final static String Seperator = ";";
	private static Integer DictLangCount = 0;
	public static Integer DefaultLanguage = null;
	private static String DictFileLocation = Application.RunInfo.FileDictionary( false );
	private static String NewLine = Application.RunInfo.AppNewLineString();
	private static String NewLineReplacer = Application.RunInfo.AppNewLineStringReplacer();
	private static List<String> DictLineList = new ArrayList<String>();
	private static Hashtable<String, Integer> DictHash = new Hashtable<String, Integer>(); 
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	/**
	 * Changing the application language to:
	 * NewLang => "DE", "EN", "ES", "IT" etc. is equal to the 
	 * end phrase after the prefix "LANG_". E.g. "LANG_EN" needs "EN" as parameter
	 */
	// -------------------------------------------------------------------------
	public static void changeLanguageTo ( String NewLang ){
		
		// --- Sind die neue und die alte Anzeigesprach gleich ? ---------------
		Integer  NewLanguage = -1;
		Integer  DictLineNumber = DictHash.get( "LANG_DE" );
		if ( DictLineNumber == null ) {
			String MsgHead = translate("Dictionary-Error");
			String MsgText = translate("Could not find Index for Expression 'LANG_DE'! ");
			JOptionPane.showMessageDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.WARNING_MESSAGE );
			return;
		};
		String   DictLine = DictLineList.get( DictLineNumber );
		String[] DictLineValues = DictLine.split( Seperator, -1 );
		for(int i=0; i<DictLineValues.length; i++) {
			if(DictLineValues[i].equalsIgnoreCase( NewLang )) {
				NewLanguage = i;
				break;
			}			
		}
		if ( NewLanguage == DefaultLanguage ) return; 
		
		// --- User fragen, ob die Sprache umgestellt werden soll --------------
		String MsgHead = translate("Anzeigesprache wechseln?");
		String MsgText = translate(
						 "Möchten Sie die Anzeigesprache wirklich umstellen?" + NewLine + 
						 "Die Anwendung muss hierzu neu gestartet und Projekte" + NewLine +
						 "von Ihnen neu geöffnet werden.");
		Integer MsgAnswer =  JOptionPane.showInternalConfirmDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
		if ( MsgAnswer == 1 ) return;
		
		// --- Jetzt: Sprache umstellen ----------------------------------------
		DictLineList.set( DictHash.get("LANG_DEFAULT"), "LANG_DEFAULT;" + NewLanguage + StringRepeat(";", DictLangCount - 2));
		SaveDictionaryFile();
		
		// --- Lokale Klassenvariablen anpassen --------------------------------
		DictLineList.clear();
		DictHash.clear();
		DefaultLanguage = null;
		LoadDictionaryFile();		

		// --- Anwendung neu starten -------------------------------------------		
		Application.JadePlatform.jadeStop();
		Application.MainWindow.dispose();		
		Application.startApplication();		
		Application.Projects.removeAll();	
		
		return;		
	}
	// -------------------------------------------------------------------------	
		
	// -------------------------------------------------------------------------
	/**
	 * Translate one Expression, which is based on the German expression
	 */
	// -------------------------------------------------------------------------
	public static String translate( String DE_Exp )  {
		
		if ( DictHash.isEmpty() ) {
			LoadDictionaryFile();	
			if (DictLineList.size() == 0) return DE_Exp;
		};
		
		// --- Nachsehen, ob dieser Ausdruck im Dictionary existiert -----------
		String DE_Exp_Work = DE_Exp.trim();
		DE_Exp_Work = DE_Exp_Work.replace( NewLine, NewLineReplacer );
		
		String  TranslationExp = null;
		Integer TranslationLine = DictHash.get( DE_Exp_Work );		
		if ( TranslationLine == null ) {
			// --- Ausdruck NICHT vorhanden! => In das Dictionary einfügen -----
			String AddLine = DE_Exp_Work + StringRepeat( Seperator, (DictLangCount-1) );
			DictLineList.add( AddLine );
			DictHash.put( DE_Exp_Work, DictLineList.size()-1 );			
			TranslationExp = DE_Exp.trim();			
		}
		else {
			// --- Ausdruck IST vorhanden! => Übersetzung holen ----------------
			String   DictLine = DictLineList.get( TranslationLine );
			String[] DictLineValues = DictLine.split( Seperator, -1 );
			TranslationExp = DictLineValues[DefaultLanguage];
			if ( TranslationExp == null || TranslationExp.isEmpty() ) {
				TranslationExp = DE_Exp.trim();
			}
			else {
				TranslationExp = TranslationExp.replace( NewLineReplacer, NewLine );				
			};
		};

		// --- Rückgade der Übersetzung ----------------------------------------
		return TranslationExp; 
	}
	// -------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------
	/**
	 * List all available Language-Headers from the 
	 * Dictionary file as String-Array
	 */
	// -------------------------------------------------------------------------
	public static String[] getLanguages () {

		if ( DictHash.isEmpty() ) {
			LoadDictionaryFile();	
			if (DictLineList.size() == 0) return null;
		};
		
		Integer  LineDefaultDescription = DictHash.get("LANG_DE");
		String   DictLine = DictLineList.get( LineDefaultDescription );
		String[] DictLineValues = DictLine.split( Seperator );
		
		return DictLineValues;
		
	}
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	/**
	 * Translate the already known language-headers (e. g. 'LANG_EN') 
	 * and give them an proper German expression.
	 */
	// -------------------------------------------------------------------------
	public static String getLanguagesHeaderInGerman( String LangHeader ) {
		
		Hashtable<String, String> HeadHash = new Hashtable<String, String>();
		
		String LangHeaderD = null;
		String LangHeaderWork = LangHeader.toUpperCase();
		
		HeadHash.put("LANG_DE", translate("Deutsch"));
		HeadHash.put("LANG_EN", translate("Englisch"));
		HeadHash.put("LANG_IT", translate("Italienisch"));
		HeadHash.put("LANG_ES", translate("Spanisch"));
		HeadHash.put("LANG_FR", translate("Französisch"));
		
		LangHeaderD = HeadHash.get(LangHeaderWork);
		
		if ( LangHeaderD == null ) {
			LangHeaderD = LangHeaderWork;
		}		
		return LangHeaderD;		
	}
	// -------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------
	/**
	 * Reading the file 'dictionary.csv' to the memory
	 */
	// -------------------------------------------------------------------------
	private static void LoadDictionaryFile() {
		
		// --- Einlesen des Dictionary-Files -----------------------------------     
		FileReader fr = null;    
		BufferedReader br = null;   
		try {        
			String line;    
			fr = new FileReader( DictFileLocation );
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {   
				DictLineList.add(line);				
			}    
		} 
		catch (IOException e) {        
			System.err.println("Error Dict-File: " + e);
		}
		finally {
			try {            
				if ( br != null) br.close();            
				if ( fr != null) fr.close();        
			} 
			catch (IOException e) {            
				System.err.println("Error Dict-File: " + e);  
			}   
		}     

		// --- Aufteilung der Zeile in Einzelelmente um die HashMap zu füllen --
		final String[][] valuesArray = new String[DictLineList.size()][];    
		int cnt = 0;    
		for ( final String line : DictLineList ) {   
			valuesArray[cnt] = line.split( Seperator, -1 );
			if ( ! valuesArray[cnt][0].isEmpty() ) {
				DictHash.put( valuesArray[cnt][0], cnt ); 					// - Dient der Indizierung der Übersetzungsdatei -
				if ( valuesArray[cnt][0].equalsIgnoreCase( "LANG_DE" ) ) {
					DictLangCount = valuesArray[cnt].length ;				// - Anzahl der Sprachen im Dictionary ermitteln - 				
				};
			};
			cnt++;			
		}
		
		// --- Herausfinden welche Anzeigesprache gewählt ----------------------
		// --- wurde: Ist gleich dem Index der Spalte !!  ----------------------
		if ( DefaultLanguage == null ) {
			Integer  LineDefaultDescription = DictHash.get("LANG_DEFAULT");
			String   DictLine = DictLineList.get( LineDefaultDescription );
			String[] DictLineValues = DictLine.split( Seperator );
			DefaultLanguage = Integer.parseInt( DictLineValues[1] );			
		};
		
	}
	// -------------------------------------------------------------------------
		
	// -------------------------------------------------------------------------
	/**
	 *  Saving the file 'dictionary.csv' to the folder properties
	 */
	// -------------------------------------------------------------------------
	public static void SaveDictionaryFile() {
		
		FileWriter fw = null; 
		BufferedWriter bw = null;
		try { 
			fw = new FileWriter(DictFileLocation);
		    bw = new BufferedWriter(fw); 
			for ( final String line : DictLineList ) {        
				 bw.write( line.toString() );
				 bw.newLine();
			}		    
		    bw.close(); 
		} 
		catch (ArrayIndexOutOfBoundsException aioobe) { 
			System.out.println("Aufruf mit: java SchreibeDatei name"); 
		    System.out.println("erzeugt eine Datei name.html"); 
		} 
		catch (IOException e) { 
			System.out.println("Error Dict-File: "+ e); 
		} 
	}
	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
	/**
	 * Repeat one String n-times and merge them together
	 */
	// -------------------------------------------------------------------------
	private static String StringRepeat( String orig, int n ) {
		if( n <= 0 ) return "";
		int l = orig.length();
		char [] dest = new char[ n*l ];
		for( int i=0, destIndex=0; i<n; i++, destIndex+=l ) {
			orig.getChars( 0, l, dest, destIndex );
		};
		return new String( dest );
	}
	// -------------------------------------------------------------------------
	
}
 

 