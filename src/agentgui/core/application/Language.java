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

	private static String dictFileLocation64 = Application.RunInfo.FileDictionary(true, true);
	private static String dictFileLocation = Application.RunInfo.FileDictionary(false, true);
	private static String newLine = Application.RunInfo.AppNewLineString();
	private static String newLineReplacer = Application.RunInfo.AppNewLineStringReplacer();
	public final static String seperator = ";";
	
	public static Integer defaultLanguage = null;
	private static Integer dictLanguageCount = 0;
	private static String[] dictLangHeader = null;
	
	private static List<String> dictLineList64 = new ArrayList<String>();
	private static Hashtable<String, Integer> dictHash64 = new Hashtable<String, Integer>(); 


	/**
	 * Changing the application language to: newLang
	 * => "DE", "EN", "IT", "ES" or "FR"  
	 * @param newLang
	 */
	public static void changeLanguageTo(String newLang){
		String newLangShort = newLang.toLowerCase().replace("lang_", "");
		Application.RunInfo.setLanguage(newLangShort);
		defaultLanguage = getIndexOfLanguage(newLangShort);
	}
		
	/**
	 * Translate one expression, which is based on the German expression
	 */
	public static String translate(String DE_Exp)  {
		
		if ( dictHash64.isEmpty() ) {
			loadDictionaryFile();	
			if (dictLineList64.size() == 0) return DE_Exp;
		};
		
		// --- Check if the expression exists ---------------------------------
		String DE_Exp_Work = DE_Exp.trim();
		DE_Exp_Work = DE_Exp_Work.replace( newLine, newLineReplacer );
		
		String  TranslationExp = null;
		Integer TranslationLine = dictHash64.get( DE_Exp_Work );		
		if ( TranslationLine == null ) {
			// --- Expression NOT there ! => Put to the dictionary ------------
			String AddLine = DE_Exp_Work + stringRepeat( seperator, (dictLanguageCount-1) );
			dictLineList64.add( AddLine );
			dictHash64.put( DE_Exp_Work, dictLineList64.size()-1 );			
			TranslationExp = DE_Exp.trim();			
		}
		else {
			// --- Expression IS there! => get translation --------------------
			String   DictLine = dictLineList64.get( TranslationLine );
			String[] DictLineValues = DictLine.split( seperator, -1 );
			TranslationExp = DictLineValues[defaultLanguage];
			if ( TranslationExp == null || TranslationExp.isEmpty() ) {
				TranslationExp = DE_Exp.trim();
			}
			else {
				TranslationExp = TranslationExp.replace( newLineReplacer, newLine );				
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
		if ( dictHash64.isEmpty() ) {
			loadDictionaryFile();	
			if (dictLineList64.size() == 0) return null;
		};
		return dictLangHeader;
	}
	// -------------------------------------------------------------------------

	/**
	 * Translate the already known language-headers (e. g. 'LANG_EN') 
	 * and give them an proper German expression.
	 */
	public static String getLanguageName(String LangHeader) {
		
		Hashtable<String, String> headHash = new Hashtable<String, String>();
		headHash.put("LANG_DE", translate("Deutsch"));
		headHash.put("LANG_EN", translate("Englisch"));
		headHash.put("LANG_IT", translate("Italienisch"));
		headHash.put("LANG_ES", translate("Spanisch"));
		headHash.put("LANG_FR", translate("Französisch"));
		
		String langHeaderWork = LangHeader.toUpperCase();
		String langHeaderD = headHash.get(langHeaderWork);
		if ( langHeaderD == null ) {
			langHeaderD = langHeaderWork;
		}		
		return langHeaderD;		
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
		for (int i = 0; i < dictLangHeader.length; i++) {
			String lang = dictLangHeader[i].toLowerCase().replace("lang_", "");
			if (lang.equalsIgnoreCase(langWork)) {
				return i;
			}
		}
		return -1;
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
			in = new BufferedReader( new InputStreamReader( new FileInputStream(dictFileLocation) , "UTF8" ) );
			while ((line = in.readLine()) != null) {   
				//DictLineList.add(line);	
			}
			in64 = new BufferedReader( new InputStreamReader( new FileInputStream(dictFileLocation64) , "UTF8" ) );
			while ((line = in64.readLine()) != null) {
				String decodedLine = new String(Base64.decodeBase64(line.getBytes()), "UTF8");
				dictLineList64.add(decodedLine);
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
		final String[][] valuesArray = new String[dictLineList64.size()][];    
		int cnt = 0;    
		for ( final String line : dictLineList64 ) {   
			if (line != null) {
				valuesArray[cnt] = line.split( seperator, -1 );
				if ( ! valuesArray[cnt][0].isEmpty() ) {
					// --- Dient der Indizierung der Übersetzungsdatei --------
					dictHash64.put( valuesArray[cnt][0], cnt ); 					
					if ( valuesArray[cnt][0].equalsIgnoreCase( "LANG_DE" ) ) {
						// --- Remind this header -----------------------------
						dictLangHeader = valuesArray[cnt];
						// --- How many languages are in the dictionary? ------
						dictLanguageCount = dictLangHeader.length;				 		
						// --- Which Language has to be used ------------------
						defaultLanguage = getIndexOfLanguage(Application.RunInfo.getLanguage());	
					};
				};
			}
			cnt++;			
		}

		// --- Falls kein Header definiert ist, das jetzt nachholen -----------
		if (dictHash64.get("LANG_DE")==null) {
			// --- This entry is mandantory for the dictionary ----------------
			String line = "LANG_DE;LANG_EN;LANG_IT;LANG_ES;LANG_FR";
			dictLineList64.add(0, line);
			dictHash64.put("LANG_DE", 0);
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
		
		List<String> DictSorted = new Vector<String>(dictLineList64); 
		Collections.sort(DictSorted);
		
		try { 
			// --- UTF8-File for the dictionary -------------------------------- 
			OutWri = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(dictFileLocation), "UTF8" ) );
			OutWri64 = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(dictFileLocation64), "UTF8" ) );
		    
			for ( final String line : DictSorted ) {
				
				if (line.trim().equals(getEmptyLine())) {
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
	 * returns the number of languages defined in the current dictionoary
	 */
	private static int getNumberOfLanguages() {
		return getLanguages().length;
	}
	/**
	 * retuns an empty dictionary line
	 * @return
	 */
	private static String getEmptyLine() {
		return stringRepeat(seperator, getNumberOfLanguages()-1);
	}
	
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
		return dictLineList64;
	}

	/**
	 * Update this line of the dictionary
	 * @param deExp
	 * @param dictRow
	 */
	public static void update(String deExp, String dictRow) {

		Integer line = dictHash64.get(deExp);	
		if (line!=null) {
			dictLineList64.set(line, dictRow);
		}
	}

	/**
	 * Remove this line from the dictionary
	 * (put an empty line)
	 * @param deExp
	 */
	public static void delete(String deExp) {
		
		Integer lineNo = dictHash64.get(deExp);	
		if (lineNo!=null) {
			dictLineList64.set(lineNo, getEmptyLine());
			dictHash64.remove(deExp);	
		}
	}
	
}
 

 