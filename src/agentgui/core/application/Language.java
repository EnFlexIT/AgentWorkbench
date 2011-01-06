package agentgui.core.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;

public class Language {

	private static String DictFileLocation64 = Application.RunInfo.FileDictionary(true, false);
	private static String DictFileLocation = Application.RunInfo.FileDictionary(false, false);
	private static String NewLine = Application.RunInfo.AppNewLineString();
	private static String NewLineReplacer = Application.RunInfo.AppNewLineStringReplacer();
	public final static String Seperator = ";";
	
	public static Integer DefaultLanguage = null;
	private static Integer DictLangCount = 0;
	private static String[] DictLangHeader = null;
	
	private static List<String> DictLineList64 = new ArrayList<String>();
	private static Hashtable<String, Integer> DictHash64 = new Hashtable<String, Integer>(); 
	
	public static int numberOfLanguages = getLanguages().length;
	private static String emptyLine = stringRepeat(Seperator, numberOfLanguages-1);

	// -------------------------------------------------------------------------
	
	/**
	 * Changing the application language to:
	 * NewLang => "DE", "EN", "ES", "IT" etc. is equal to the 
	 * end phrase after the prefix "LANG_". E.g. "LANG_EN" needs "EN" as parameter
	 */
	public static void changeLanguageTo(String newLang){
		
		String newLangShort = newLang.toLowerCase().replace("lang_", "");
		Application.RunInfo.setLanguage(newLangShort);
		DefaultLanguage = getIndexOfLanguage(newLangShort);
	}
	// -------------------------------------------------------------------------	
		
	/**
	 * Translate one Expression, which is based on the German expression
	 */
	public static String translate( String DE_Exp )  {
		
		if ( DictHash64.isEmpty() ) {
			loadDictionaryFile();	
			if (DictLineList64.size() == 0) return DE_Exp;
		};
		
		// --- Check if the expression exists ---------------------------------
		String DE_Exp_Work = DE_Exp.trim();
		DE_Exp_Work = DE_Exp_Work.replace( NewLine, NewLineReplacer );
		
		String  TranslationExp = null;
		Integer TranslationLine = DictHash64.get( DE_Exp_Work );		
		if ( TranslationLine == null ) {
			// --- Expression NOT there ! => Put to the dictionary ------------
			String AddLine = DE_Exp_Work + stringRepeat( Seperator, (DictLangCount-1) );
			DictLineList64.add( AddLine );
			DictHash64.put( DE_Exp_Work, DictLineList64.size()-1 );			
			TranslationExp = DE_Exp.trim();			
		}
		else {
			// --- Expression IS there! => get translation --------------------
			String   DictLine = DictLineList64.get( TranslationLine );
			String[] DictLineValues = DictLine.split( Seperator, -1 );
			TranslationExp = DictLineValues[DefaultLanguage];
			if ( TranslationExp == null || TranslationExp.isEmpty() ) {
				TranslationExp = DE_Exp.trim();
			}
			else {
				TranslationExp = TranslationExp.replace( NewLineReplacer, NewLine );				
			};
		};

		// --- Rückgabe der Übersetzung ---------------------------------------
		return TranslationExp; 
	}
	// -------------------------------------------------------------------------
	
	/**
	 * List all available Language-Headers from the 
	 * Dictionary file as String-Array
	 */
	public static String[] getLanguages () {
		if ( DictHash64.isEmpty() ) {
			loadDictionaryFile();	
			if (DictLineList64.size() == 0) return null;
		};
		return DictLangHeader;
	}
	// -------------------------------------------------------------------------

	/**
	 * Translate the already known language-headers (e. g. 'LANG_EN') 
	 * and give them an proper German expression.
	 */
	public static String getLanguageName(String LangHeader) {
		
		Hashtable<String, String> HeadHash = new Hashtable<String, String>();
		HeadHash.put("LANG_DE", translate("Deutsch"));
		HeadHash.put("LANG_EN", translate("Englisch"));
		HeadHash.put("LANG_IT", translate("Italienisch"));
		HeadHash.put("LANG_ES", translate("Spanisch"));
		HeadHash.put("LANG_FR", translate("Französisch"));
		
		String LangHeaderWork = LangHeader.toUpperCase();
		String LangHeaderD = HeadHash.get(LangHeaderWork);
		if ( LangHeaderD == null ) {
			LangHeaderD = LangHeaderWork;
		}		
		return LangHeaderD;		
	}
	// -------------------------------------------------------------------------
	
	/**
	 * Returns the index of the dictionary column which provides the current  
	 * translation. The language can be configured in the file 'agentgui.ini'
	 */
	public static int getIndexOfLanguage(String language) {
		// --- normation of the query string --------------
		String langWork = language.toLowerCase();
		if (langWork.startsWith("lang_")) {
			langWork = langWork.replace("lang_", "");
		}
		// --- look at the dictionary header --------------
		for (int i = 0; i < DictLangHeader.length; i++) {
			String lang = DictLangHeader[i].toLowerCase().replace("lang_", "");
			if (lang.equalsIgnoreCase(langWork)) {
				return i;
			}
		}
		return 0;
	}
	// -------------------------------------------------------------------------
	
	/**
	 * Reading the file 'dictionary.csv' to the memory
	 */
	private static void loadDictionaryFile() {
		
		// --- Read the Dictionary-Files --------------------------------------     
		BufferedReader in = null;
		BufferedReader in64 = null;
		try {        
			String line;
			// --- Reading UTF8-File of the dictionary ------------------------		
			in = new BufferedReader( new InputStreamReader( new FileInputStream(DictFileLocation) , "UTF8" ) );
			while ((line = in.readLine()) != null) {   
				//DictLineList.add(line);	
			}
			in64 = new BufferedReader( new InputStreamReader( new FileInputStream(DictFileLocation64) , "UTF8" ) );
			while ((line = in64.readLine()) != null) {
				String decodedLine = new String(Base64.decodeBase64(line.getBytes()), "UTF8");
				DictLineList64.add(decodedLine);
			}			
		} 
		catch (IOException ex) {        
			System.err.println("Error Dict-File: " + ex);
		}
		finally {
			try {            
				if (in!=null)   in.close();
				if (in64!=null) in64.close();
			} 
			catch (IOException ec) {            
				System.err.println("Error while closing Dict-File: " + ec);  
			}   
		}     

		// --- Aufteilung der Zeile in Einzelelmente um die HashMap zu füllen --
		final String[][] valuesArray = new String[DictLineList64.size()][];    
		int cnt = 0;    
		for ( final String line : DictLineList64 ) {   
			if (line != null) {
				valuesArray[cnt] = line.split( Seperator, -1 );
				if ( ! valuesArray[cnt][0].isEmpty() ) {
					// --- Dient der Indizierung der Übersetzungsdatei --------
					DictHash64.put( valuesArray[cnt][0], cnt ); 					
					if ( valuesArray[cnt][0].equalsIgnoreCase( "LANG_DE" ) ) {
						// --- Remind this header -----------------------------
						DictLangHeader = valuesArray[cnt];
						// --- How many languages are in the dictionary? ------
						DictLangCount = DictLangHeader.length;				 		
						// --- Which Language has to be used ------------------
						DefaultLanguage = getIndexOfLanguage(Application.RunInfo.getLanguage());	
					};
				};
			}
			cnt++;			
		}

		// --- Falls kein Header definiert ist, das jetzt nachholen -----------
		if (DictHash64.get("LANG_DE")==null) {
			// --- This entry is mandantory for the dictionary ----------------
			String line = "LANG_DE;LANG_EN;LANG_IT;LANG_ES;LANG_FR";
			DictLineList64.add(0, line);
			DictHash64.put("LANG_DE", 0);
			// --- Reload dictionary ------------------------------------------
			saveDictionaryFile();
			loadDictionaryFile();
		}
		
	}
	// ------------------------------------------------------------------------
		
	/**
	 *  Saving the file 'dictionary.csv' to the folder properties
	 */
	public static void saveDictionaryFile() {
		
		BufferedWriter OutWri = null;
		BufferedWriter OutWri64 = null;
		
		List<String> DictSorted = new Vector<String>(DictLineList64); 
		Collections.sort(DictSorted);
		
		try { 
			// --- UTF8-File for the dictionary -------------------------------- 
			OutWri = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(DictFileLocation), "UTF8" ) );
			OutWri64 = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(DictFileLocation64), "UTF8" ) );
		    
			for ( final String line : DictSorted ) {
				
				if (line.trim().equals(emptyLine)) {
					// --- just ignore this line --------------------
				} else {
					// --- TXT-Version of the dictionary ------------
					OutWri.write( line );
			    	OutWri.newLine();
			    	
			    	String encodedLine = new String(Base64.encodeBase64(line.getBytes("UTF8")));
			    	OutWri64.write(encodedLine);
			    	OutWri64.newLine();
				}
		    	
		    }
		    OutWri.close();
		    OutWri64.close();
		} 
		catch (ArrayIndexOutOfBoundsException aioobe) { 
			System.out.println( "Error Dict-File: " + aioobe ); 
		} 
		catch (IOException e) { 
			System.out.println( "Error Dict-File: "+ e ); 
		} 
	}
	// -------------------------------------------------------------------------

	/**
	 * Repeat one String n-times and merge them together
	 */
	private static String stringRepeat( String orig, int n ) {
		if( n <= 0 ) return "";
		int l = orig.length();
		char [] dest = new char[ n*l ];
		for( int i=0, destIndex=0; i<n; i++, destIndex+=l ) {
			orig.getChars( 0, l, dest, destIndex );
		};
		return new String( dest );
	}
	// -------------------------------------------------------------------------

	/**
	 * @return the dictLineList64
	 */
	public static List<String> getDictLineList() {
		return DictLineList64;
	}

	/**
	 * Update this line of the dictionary
	 * @param deExp
	 * @param dictRow
	 */
	public static void update(String deExp, String dictRow) {

		Integer line = DictHash64.get(deExp);	
		if (line!=null) {
			DictLineList64.set(line, dictRow);
		}
	}

	/**
	 * Remove this line from the dictionary
	 * (put an empty line)
	 * @param deExp
	 */
	public static void delete(String deExp) {
		
		Integer lineNo = DictHash64.get(deExp);	
		if (lineNo!=null) {
			DictLineList64.set(lineNo, emptyLine);
			DictHash64.remove(deExp);	
		}
	}
	
}
 

 