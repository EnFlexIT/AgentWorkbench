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

import agentgui.core.config.GlobalInfo;

/**
 * This is the static singleton class for the use of the dictionary by the 
 * applications translation functionality.<br><br>  
 * In order to translate the String phrases of your JAVA code into the currently 
 * selected and displayed language (see App.: 'Extra' - 'Language') use:<br><br>
 *  
 * &nbsp; &nbsp; &nbsp; <i>Language.translate("Hello World", Language.EN);</i><br><br>
 * 
 * where 'Language.EN' indicates the language in which your text was written (in this case in English).<br> 
 * If you are writing your text in German you can use<br><br>
 * 
 * &nbsp; &nbsp; &nbsp; <i>Language.translate("Hallo Welt");</i><br><br>
 * 
 * This method implicitly assumes that you have written your text in German.<br><br>
 * 
 * Later on, the phrases which were used with the translate-method can be translated in<br>
 * the 'Translation' Section of the application (see App.: 'Extra' - 'Language' - 'Translate').<br>
 *  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Language {

	private static Language thisSingleton = new Language(); 
	private static GlobalInfo globalInfo;
	
	/** This is the separator used in the dictionary file */
	public static final String seperator = ";";
	
	private static String newLine;
	private static String newLineReplacer;

	private static String dictFileLocation64;
	private static String dictFileLocation;

	private static List<String> dictLineList64 = new ArrayList<String>();
	private static List<String> dictLineListCSV = new ArrayList<String>();
	private static Hashtable<String, Integer> dictHash64 = new Hashtable<String, Integer>(); 
	
	/** The header for the first column (index=0) of the dictionary */
	public static final String SOURCE_LANG = "SOURCE_LANGUAGE";	
	/** Constant for German */
	public static final String DE = "DE";
	/** Constant for English */
	public static final String EN = "EN";
	/** Constant for Italian */
	public static final String IT = "IT";
	/** Constant for Spanish */
	public static final String ES = "ES";
	/** Constant for French */
	public static final String FR = "FR";
	
	private static final String dictLangHeaderDefault = SOURCE_LANG + seperator + DE + seperator + EN + seperator + IT + seperator + ES + seperator + FR;
	private static String[] dictLangHeaderArray = null;
	
	/** The currently selected language index of the dictionary-file, which is used in the application */
	public static Integer currLanguageIndex = null;
	

	// --- Singleton-Constructor ---
	private Language() {
	}
	/**
	 * Returns the instance of this Singleton-Class
	 * @return this class instance
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
			// --- load the dictionary ----------
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
		Application.getGlobalInfo().setLanguage(newLangShort);
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
	 * @return the translated text of the expression
	 */
	public static String translate(String expression, String language)  {
		
		if (getGlobalInfo()==null) return expression;
		
		
		// --- In case that the dictionary was not loaded yet -----------------
		if (dictLineList64.size()==0) {
			loadDictionaryFile();
			if (dictLineList64.size()==0) return expression;
		}
		// --- Check if the expression exists ---------------------------------
		String translationExp = null;
		String expressionWork = null;
		expressionWork = expression.trim();
		expressionWork = expressionWork.replace(getNewLine(), getNewLineReplacer());

		Integer lineInDictionary = dictHash64.get(expressionWork);		
		if (lineInDictionary == null) {
			// --- Expression NOT there ! => Put to the dictionary ------------
			String addLine = getNewDictionaryLine(expressionWork, language);
			dictLineList64.add(addLine);
			dictHash64.put(expressionWork, dictLineList64.size()-1);			
			translationExp = expression.trim();			
		
		} else {
			// --- Expression IS there! => get translation --------------------
			String   dictLine = dictLineList64.get(lineInDictionary);
			String[] dictLineValues = dictLine.split(seperator, -1);
			
			if (dictLineValues.length < currLanguageIndex) {
				translationExp = expression.trim();
			} else {
				translationExp = dictLineValues[currLanguageIndex];
				if ( translationExp == null || translationExp.isEmpty() ) {
					translationExp = expression.trim();
				} else {
					translationExp = translationExp.replace(getNewLineReplacer(), getNewLine());				
				}
			}
			
		};
		return translationExp; 
	}
	
	/**
	 * This method will return a new dictionary line for the dictionary.
	 *
	 * @param expression The expression to translate
	 * @param language The language used in the source code
	 * @return the new dictionary line
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
	 * Returns the index of the dictionary column which provides the language 
	 * given by the parameter language. To specify this language, use the
	 * final Strings from the head of this class (e. g. Language.IT) 
	 * @param language (e. g. Language.IT)
	 * @return the index of the language in the dictionary 
	 */
	public static int getIndexOfLanguage(String language) {
		// --- normation of the query string --------------
		String langWork = language.toLowerCase();
		if (langWork.equals("")) {
			langWork = Language.EN;
			Application.getGlobalInfo().setLanguage(Language.EN);
			langWork = language.toLowerCase();
		}
		
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
	 * Returns true if the language, given by the parameter language, is the current one
	 * @param language
	 * @return true and false
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
			in = new BufferedReader( new InputStreamReader( new FileInputStream(getDictFileLocation()) ) );
			while ((line = in.readLine()) != null) {   
				dictLineListCSV.add(line);
			}
			// --- Reading the Base64 encoded UTF8-File of the dictionary -----
			in64 = new BufferedReader( new InputStreamReader( new FileInputStream(getDictFileLocation64()) , "UTF8" ) );
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
			
			dictHash64 = new Hashtable<String, Integer>(); 
    
			int cnt = 0;			
			for ( String line : dictLineList64 ) {   
				if (line != null) {
					
					String[] valuesArray = line.split(seperator, -1);
					if ( ! valuesArray[0].isEmpty() ) {
						// ------------------------------------------------------------------------
						// --- Used to identify the Header of the dictionary ----------------------
						if ( valuesArray[0].equalsIgnoreCase(Language.SOURCE_LANG) ) {
							// --- Remind this header ---------------------------------------------
							dictLangHeaderArray = valuesArray;
							// --- Which Language has to be used ----------------------------------
							currLanguageIndex = getIndexOfLanguage(Application.getGlobalInfo().getLanguage());	
							// --- index the Header -----------------------------------------------
							dictHash64.put( valuesArray[0], cnt );
							
						} else {
							// --- index the expression, that was used in the source code ---------
							int indexOfExpression = getIndexOfLanguage(valuesArray[0]);
							String indexExpression = valuesArray[indexOfExpression];
							dictHash64.put(indexExpression, cnt); 			
						}
						// ------------------------------------------------------------------------
					}
					
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
			OutWri = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(getDictFileLocation())) );
			OutWri64 = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(getDictFileLocation64()), "UTF8" ) );
		    
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
	 * returns an empty dictionary line.
	 * @return an empty dictionary line
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
	
	
	/**
	 * Gets access to the {@link GlobalInfo}.
	 * @return the global info
	 */
	private static GlobalInfo getGlobalInfo() {
		if (globalInfo==null) {
			try {
				globalInfo = Application.getGlobalInfo();	
			} catch (Exception ex) {
				//ex.printStackTrace();
			}
		}
		return globalInfo;
	}
	
	/**
	 * Returns the current new line string.
	 * @return the new line
	 */
	private static String getNewLine() {
		if (newLine==null) {
			GlobalInfo gInfo = getGlobalInfo();
			if (gInfo!=null) {
				newLine = gInfo.getNewLineSeparator();
			}
		}
		return newLine;
	}
	/**
	 * Returns the new line replacer string .
	 * @return the new line
	 */
	private static String getNewLineReplacer() {
		if (newLineReplacer==null) {
			GlobalInfo gInfo = getGlobalInfo();
			if (gInfo!=null) {
				newLineReplacer = gInfo.getNewLineSeparatorReplacer();
			}
		}
		return newLineReplacer;
	}
	/**
	 * Returns the file location for the base64 encoded dictionary file as string.
	 * @return the absolute file location as string 
	 */
	private static String getDictFileLocation64() {
		if (dictFileLocation64==null) {
			GlobalInfo gInfo = getGlobalInfo();
			if (gInfo!=null) {
				dictFileLocation64 = gInfo.getFileDictionary(true, true);
			}
		}
		return dictFileLocation64;
	}
	/**
	 * Returns the file location for the dictionary file as string.
	 * @return the absolute file location as string 
	 */
	private static String getDictFileLocation() {
		if (dictFileLocation==null) {
			GlobalInfo gInfo = getGlobalInfo();
			if (gInfo!=null) {
				dictFileLocation = gInfo.getFileDictionary(false, true);
			}
		}
		return dictFileLocation;
	}
	

	
	
}