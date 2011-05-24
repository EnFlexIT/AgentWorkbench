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

	private static Language thisSingleton = new Language(); 
	
	public static final String seperator = ";";
	private static String newLine = Application.RunInfo.AppNewLineString();
	private static String newLineReplacer = Application.RunInfo.AppNewLineStringReplacer();

	private static String dictFileLocation64 = Application.RunInfo.FileDictionary(true, true);
	private static String dictFileLocation = Application.RunInfo.FileDictionary(false, true);

	private static List<String> dictLineList64 = new ArrayList<String>();
	private static List<String> dictLineListCSV = new ArrayList<String>();
	private static Hashtable<String, Integer> dictHash64 = new Hashtable<String, Integer>(); 
	
	public static final String SOURCE_LANG = "SOURCE_LANGUAGE";	
	public static final String DE = "DE";
	public static final String EN = "EN";
	public static final String IT = "IT";
	public static final String ES = "ES";
	public static final String FR = "FR";
	
	private static final String dictLangHeaderDefault = SOURCE_LANG + seperator + DE + seperator + EN + seperator + IT + seperator + ES + seperator + FR;
	private static String[] dictLangHeaderArray = null;
	public static Integer currLanguageIndex = null;
	

	// --- Singleton-Constructor ---
	private Language() {
	}
	/**
	 * Returns the instance of this Singleton-Class
	 * @return 
	 */
	public static Language getInstance() {
		return thisSingleton;
	}
	
	/**
	 * This method has to be invoked only once in order to prepare the translation
	 * functionalities. This will be done by the Application class at the program 
	 * execution and can not be done a second time.
	 */
	public static void startDictionary() {
		if (dictHash64.size()==0) {
			// --- load the dictionary ------------------------------
			loadDictionaryFile();
		}
	}
	
	/**
	 * Changing the application language to: newLang
	 * => "DE", "EN", "IT", "ES" or "FR"  
	 * @param newLang
	 */
	public static void changeApplicationLanguageTo(String newLang){
		String newLangShort = newLang.toLowerCase().replace("lang_", "");
		Application.RunInfo.setLanguage(newLangShort);
		currLanguageIndex = getIndexOfLanguage(newLangShort);
	}
		
	/**
	 * This method can be used in order to change the source dictionary file of the application 
	 * to the CSV-dictionary file located at '/properties/dictionary.csv'
	 * The idea is to make the translation also in other applications, as for example in MS Excel
	 */
	public static void useCSVDictionaryFile() {
		dictLineList64 = dictLineListCSV;
		proceedLoadedDictionaryLines();
		Application.setLanguage(Application.RunInfo.getLanguage());		
	}
	
	/**
	 * Translate one expression, which is based on a German expression
	 */
	public static String translate(String deExpression) {
		return translate(deExpression, Language.DE);
	}
	/**
	 * Translate one expression, which is based on the language specified through 
	 * the second parameter language (use one of the languages specified as static 
	 * attribute in this class e.g. 'Language.EN')
	 * 
	 * @param expression
	 * @param language => take one of these parameters: Language.EN, Language.DE and so on
	 * @return
	 */
	public static String translate(String expression, String language)  {
		
		// --- In case that the dictionary was not loaded yet -----------------
		if (dictLineList64.size() == 0) {
			return expression;
		}
		// --- Check if the expression exists ---------------------------------
		String translationExp = null;
		String expressionWork = null;
		expressionWork = expression.trim();
		expressionWork = expressionWork.replace(newLine, newLineReplacer);

		Integer lineInDictionary = dictHash64.get(expressionWork);		
		if (lineInDictionary == null) {
			// --- Expression NOT there ! => Put to the dictionary ------------
			String addLine = getNewDictionaryLine(expressionWork, language);
			dictLineList64.add(addLine);
			dictHash64.put(expressionWork, dictLineList64.size()-1);			
			translationExp = expression.trim();			
		
		} else {
			// --- Expression IS there! => get translation --------------------
			String   dictLine = dictLineList64.get( lineInDictionary );
			String[] dictLineValues = dictLine.split(seperator, -1);
			
			if (dictLineValues.length < currLanguageIndex) {
				translationExp = expression.trim();
			} else {
				translationExp = dictLineValues[currLanguageIndex];
				if ( translationExp == null || translationExp.isEmpty() ) {
					translationExp = expression.trim();
				} else {
					translationExp = translationExp.replace( newLineReplacer, newLine );				
				};
			}
			
		};
		return translationExp; 
	}
	
	/**
	 * This method will return a new dictionary line for the dictionary
	 * @param expression The expression to translate
	 * @param language The language used in the source code
	 * @return
	 */
	private static String getNewDictionaryLine(String expression, String language) {

		int numberOfLanguages = getNumberOfLanguages();
		int indexOfLanguage   = getIndexOfLanguage(language);
		String newDictLine = "";
		String[] newLineVector = new String[numberOfLanguages+1];
		
		newLineVector[0] = language.toLowerCase();
		newLineVector[indexOfLanguage] = expression;
		
		for (int i = 0; i < newLineVector.length; i++) {
			if (newDictLine.equals("")==false) {
				newDictLine += seperator;	
			}
			if (newLineVector[i]==null) {
				newDictLine += "";
			} else {
				newDictLine += newLineVector[i];	
			}
		}
		return newDictLine;
	}
	
	/**
	 * List all available Language-Headers from the 
	 * Dictionary file as String-Array
	 */
	public static String[] getLanguages () {
		return getLanguages(false);	
	}
	/**
	 * List all available Language-Headers from the 
	 * Dictionary file as String-Array
	 */
	public static String[] getLanguages (boolean remove_LANG_SOURCE) {
		if (remove_LANG_SOURCE==true) {
			String[] languageArray = new String[dictLangHeaderArray.length-1];
			for (int i = 0; i < languageArray.length; i++) {
				languageArray[i] = dictLangHeaderArray[i+1];
			}
			return languageArray;
		} else {
			return dictLangHeaderArray;	
		}
	}
	
	/**
	 * Translate the already known language-headers (e. g. 'LANG_EN') 
	 * and give them an proper German expression.
	 */
	public static String getLanguageName(String langHeader) {
		
		Hashtable<String, String> headDescriptions = new Hashtable<String, String>();
		// --- prepare internal language descriptions -----------
		headDescriptions.put(Language.DE, translate("Deutsch"));
		headDescriptions.put(Language.EN, translate("Englisch"));
		headDescriptions.put(Language.IT, translate("Italienisch"));
		headDescriptions.put(Language.ES, translate("Spanisch"));
		headDescriptions.put(Language.FR, translate("Französisch"));

		String langHeaderWork = langHeader.toUpperCase();
		String langHeaderD = headDescriptions.get(langHeaderWork);
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
		// --- search the dictionary header ---------------
		for (int i = 0; i < dictLangHeaderArray.length; i++) {
			String lang = dictLangHeaderArray[i].toLowerCase();
			if (lang.equalsIgnoreCase(langWork)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns true if the language asked for is the current one
	 * @param language
	 * @return
	 */
	public static boolean isCurrentLanguage(String language) {
		
		int indexOfLanguage = getIndexOfLanguage(language);
		if (indexOfLanguage==currLanguageIndex) {
			return true;
		} else {
			return false;
		}
	}
	
	// -------------------------------------------------------------------------
	
	
	/**
	 * Reading the dictionary files to the memory 
	 */
	private static void loadDictionaryFile() {
		
		// --- Read the Dictionary-Files --------------------------------------     
		BufferedReader in = null;
		BufferedReader in64 = null;
		try {        
			String line;
			// --- Reading the CSV-Version of the dictionary ------------------		
			in = new BufferedReader( new InputStreamReader( new FileInputStream(dictFileLocation) ) );
			while ((line = in.readLine()) != null) {   
				dictLineListCSV.add(line);
			}
			// --- Reading the Base64 encoded UTF8-File of the dictionary -----
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
		// ----------------------------------------------------------
		// --- proceed the loaded data so that they are usable ------
		proceedLoadedDictionaryLines();
		
	}

	/**
	 * This method will work on the just been loaded dictionary lines in order to
	 * make them usable and quickly accessible for the application
	 */
	private static void proceedLoadedDictionaryLines() {
		
		// --------------------------------------------------------------------
		// --- Index the dictionary by using a Hashtable<String, Integer> -----
		// --- where String is the SourceText coming from the source code -----
		// --- and the Integer value holds the position in the Array 	  -----
		// --- 'dictLineList64'											  -----
		// --------------------------------------------------------------------		
		if (dictLineList64.size()!=0) {
			
			final String[][] valuesArray = new String[dictLineList64.size()][];    
			int cnt = 0;    
			
			for ( final String line : dictLineList64 ) {   
				if (line != null) {
					valuesArray[cnt] = line.split(seperator, -1);
					if ( ! valuesArray[cnt][0].isEmpty() ) {
						// ------------------------------------------------------------------------
						// --- Used to identify the Header of the dictionary ----------------------
						if ( valuesArray[cnt][0].equalsIgnoreCase(Language.SOURCE_LANG) ) {
							// --- Remind this header ---------------------------------------------
							dictLangHeaderArray = valuesArray[cnt];
							// --- Which Language has to be used ----------------------------------
							currLanguageIndex = getIndexOfLanguage(Application.RunInfo.getLanguage());	
							// --- index the Header -----------------------------------------------
							dictHash64.put( valuesArray[cnt][0], cnt );
							
						} else {
							// --- index the expression, that was used in the source code ---------
							int indexOfExpression = getIndexOfLanguage(valuesArray[cnt][0]);
							String indexExpression = valuesArray[cnt][indexOfExpression];
							dictHash64.put( indexExpression, cnt ); 			
						}
						// ------------------------------------------------------------------------
					};
				}
				cnt++;			
			}
		}

		// --- If there was no proper header, define it now -------------------
		if (dictHash64.get(Language.SOURCE_LANG)==null) {
			// --- This entry is mandatory for the dictionary -----------------
			dictLineList64.add(0, dictLangHeaderDefault);
			dictHash64.put(Language.SOURCE_LANG, 0);
			dictLangHeaderArray = dictLangHeaderDefault.split(seperator, -1);
			// --- Reload dictionary ------------------------------------------
			saveDictionaryFile();
			dictLineList64 = new ArrayList<String>();
			dictLineListCSV = new ArrayList<String>();
			dictHash64 = new Hashtable<String, Integer>(); 
			loadDictionaryFile();
		}
	}
		
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
			OutWri = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(dictFileLocation)) );
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
	
	/**
	 * returns the number of languages defined in the current dictionary
	 */
	private static int getNumberOfLanguages() {
		return getLanguages().length-1;
	}
	/**
	 * returns an empty dictionary line
	 * @return
	 */
	private static String getEmptyLine() {
		return stringRepeat(seperator, getNumberOfLanguages()-1);
	}
	
	/**
	 * Repeat one String n-times and merge them together
	 */
	private static String stringRepeat(String orig, int n) {
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
	 * @param expression
	 * @param dictRow
	 */
	public static void update(String expression, String dictRow) {

		Integer line = dictHash64.get(expression);	
		if (line!=null) {
			dictLineList64.set(line, dictRow);
		}
	}

	/**
	 * Remove this line from the dictionary
	 * (put an empty line)
	 * @param expression
	 */
	public static void delete(String expression) {
		
		Integer lineNo = dictHash64.get(expression);	
		if (lineNo!=null) {
			dictLineList64.set(lineNo, getEmptyLine());
			dictHash64.remove(expression);	
		}
	}
	
}
 

 