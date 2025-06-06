package de.enflexit.language;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;
import org.osgi.service.prefs.BackingStoreException;

import de.enflexit.language.PropertyContentProvider.FileToProvide;

import org.apache.commons.codec.binary.Base64;

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
	
	public enum DictionarySourceFile {
		DefaultFile64,
		CsvFile,
		BundleFile
	}

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

	/** This is the separator used in the dictionary file */
	public static final String VALUE_SEPERATOR = ";";

	/** The header for the first column (index=0) of the dictionary */
	public static final String SOURCE_LANG = "SOURCE_LANGUAGE";	
	private static final String dictLangHeaderDefault = SOURCE_LANG + VALUE_SEPERATOR + DE + VALUE_SEPERATOR + EN + VALUE_SEPERATOR + IT + VALUE_SEPERATOR + ES + VALUE_SEPERATOR + FR;

	private static String[] dictLangHeaderArray;

	
	private static String newLine;
	private static String newLineReplacer;

	private static String dictFileNameDefault64;
	private static String dictFileNameCSV;
	private static String dictFileNameBundle;
	
	/** The dictionary lines as string list */
	private static List<String> dictionaryLines;
	/** The HashMap that connects a search phrase with the dictionary lines */
	private static Hashtable<String, Integer> dictionaryHashtabel; 
	
	/** The currently selected language index of the dictionary-file, which is used in the application */
	private static String currLanguage;
	private static Integer currLanguageIndex;
	
	/** The local bundle properties */
	private static IEclipsePreferences eclipsePreferences;
	private static Boolean isUpdatedDictionaryBundle;
	
	
	/**
	 * Returns the dictionary Hashtable.
	 * @return the dictionary hashtabel
	 */
	private static Hashtable<String, Integer> getDictionaryHashtabel() {
		if (dictionaryHashtabel==null) {
			dictionaryHashtabel = new Hashtable<>();
		}
		return dictionaryHashtabel;
	}
	
	/**
	 * Restarts the dictionary.
	 */
	public static void reStartDictionary() {
		Language.loadDictionaryFromDefaultFile();
	}
	
	/**
	 * Changes the translation and the application language to the specified new language.
	 * @param newLang the new language to use. Use 'DE', 'EN', 'IT', 'ES' or 'FR' (corresponds to the first line in the dictionary file)  
	 */
	public static void changeApplicationLanguageTo(String newLang){
		String newLangShort = newLang.toLowerCase().replace("lang_", "");
		currLanguageIndex = getIndexOfLanguage(newLangShort);
		Language.setLanguage(newLangShort);
	}
		
	/**
	 * Translate one expression, which is based on a German expression.
	 *
	 * @param deExpression the German (de) expression
	 * @return the translated expression
	 */
	public static String translate(String deExpression) {
		return translate(deExpression, Language.DE);
	}
	/**
	 * Translate one expression, which is based on the language specified through 
	 * the second parameter language (use one of the languages specified as static 
	 * attribute in this class e.g. 'Language.EN')
	 * 
	 * @param expression the expression to translate
	 * @param language the source language. Use one of these parameters: Language.EN, Language.DE and so on
	 * @return the translated text of the expression
	 */
	public static String translate(String expression, String language)  {
		
		// --- In case that the dictionary was not loaded yet -----------------
		if (Language.getDictionaryLines().size()==0) {
			Language.loadDictionaryFromDefaultFile();
			if (Language.getDictionaryLines().size()==0) return expression;
		}
		
		// --- Check if the expression exists ---------------------------------
		String translationExp = null;
		String expressionWork = null;
		expressionWork = expression.trim();
		expressionWork = expressionWork.replace(getNewLine(), getNewLineReplacer());

		Integer lineInDictionary = getDictionaryHashtabel().get(expressionWork);		
		if (lineInDictionary==null) {
			// --- Expression NOT available => Put into dictionary ------------
			String addLine = getNewDictionaryLine(expressionWork, language);
			getDictionaryLines().add(addLine);
			getDictionaryHashtabel().put(expressionWork, getDictionaryLines().size()-1);			
			translationExp = expression.trim();			
		
		} else {
			// --- Expression IS there! => get translation --------------------
			String   dictLine = getDictionaryLines().get(lineInDictionary);
			String[] dictLineValues = dictLine.split(VALUE_SEPERATOR, -1);
			
			if (currLanguageIndex==null || dictLineValues==null || dictLineValues.length<currLanguageIndex) {
				translationExp = expression.trim();
			} else {
				translationExp = dictLineValues[currLanguageIndex];
				if (translationExp==null || translationExp.isEmpty()==true ) {
					translationExp = expression.trim();
				} else {
					translationExp = translationExp.replace(getNewLineReplacer(), getNewLine());				
				}
			}
		}
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
				newDictLine += VALUE_SEPERATOR;	
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
	 * List of all available Language-Headers in the dictionary file as String-Array.
	 * @return the languages header from the dictionary file 
	 */
	public static String[] getLanguages() {
		return getLanguages(false);	
	}
	
	/**
	 * List all available Language-Headers from the dictionary file as String-Array.
	 *
	 * @param remove_LANG_SOURCE the remove LAN G SOURCE
	 * @return the languages
	 */
	public static String[] getLanguages(boolean remove_LANG_SOURCE) {
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
	 * Translate the already known language-headers (e. g. 'LANG_EN') and give them an proper German expression.
	 *
	 * @param langHeader the language header
	 * @return the language name in German
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
		if (langHeaderD==null) {
			langHeaderD = langHeaderWork;
		}		
		return langHeaderD;		
	}
	// -------------------------------------------------------------------------
	
	/**
	 * Returns the index of the dictionary column which provides the language 
	 * given by the parameter language. To specify this language, use the
	 * final Strings from the head of this class (e. g. Language.IT) 
	 * 
	 * @param language (e. g. Language.IT)
	 * @return the index of the language in the dictionary 
	 */
	public static int getIndexOfLanguage(String language) {

		// --- Normation of the query string --------------
		String langWork = language;
		if (langWork==null || langWork.isEmpty()==true) {
			langWork = Language.EN;
			Language.setLanguage(language);
		}
		langWork = langWork.toLowerCase();
		
		// --- Search the dictionary header ---------------
		for (int i = 0; i < dictLangHeaderArray.length; i++) {
			String lang = dictLangHeaderArray[i].toLowerCase();
			if (lang.equalsIgnoreCase(langWork)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns true if the language, given by the parameter language, is the current one.
	 * @param language the language header from the dictionary to check (e.g. 'en')
	 * @return true if the specified language is the current one 
	 */
	public static boolean isCurrentLanguage(String language) {
		if (currLanguageIndex==null) return false;
		int indexOfLanguage = getIndexOfLanguage(language);
		if (indexOfLanguage==currLanguageIndex) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the number of languages defined in the current dictionary.
	 * @return the number of languages
	 */
	private static int getNumberOfLanguages() {
		return getLanguages().length-1;
	}
	
	/**
	 * returns an empty dictionary line.
	 * @return an empty dictionary line
	 */
	private static String getEmptyLine() {
		return stringRepeat(VALUE_SEPERATOR, getNumberOfLanguages()-1);
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
	 * Updates the specified dictionary line.
	 *
	 * @param keyExpression the key expression that was defined with the source language
	 * @param newDictRow the new dictionary line to set
	 */
	public static void update(String keyExpression, String newDictRow) {
		Integer dictLineIndex = getDictionaryHashtabel().get(keyExpression);	
		if (dictLineIndex!=null) {
			getDictionaryLines().set(dictLineIndex, newDictRow);
		}
	}
	
	/**
	 * Remove this line from the dictionary (or better: places an empty line in the dictionary).
	 * @param keyExpression the key expression
	 */
	public static void delete(String keyExpression) {
		Integer lineNo = getDictionaryHashtabel().get(keyExpression);	
		if (lineNo!=null) {
			getDictionaryLines().set(lineNo, getEmptyLine());
			getDictionaryHashtabel().remove(keyExpression);	
		}
	}
	
	
	// -------------------------------------------------------------------------
	// --- From here, method for the file handling can be found ----------------
	// -------------------------------------------------------------------------
	/**
	 * Returns the dictionary lines that were loaded from file.
	 * @return the dictionary lines
	 */
	public static List<String> getDictionaryLines() {
		if (dictionaryLines==null) {
			dictionaryLines = new ArrayList<>();
		}
		return dictionaryLines;
	}
	
	/**
	 * Reading the dictionary files to the memory 
	 */
	private static void loadDictionaryFromDefaultFile() {
		Language.getDictionaryLines().clear();
		Language.checkAndProvideDictionaryFiles(false);
		Language.getDictionaryLines().addAll(Language.loadDictionaryFile(DictionarySourceFile.DefaultFile64));
		Language.proceedLoadedDictionaryLines();
		// --- Update dictionary using the dictionary within the bundle? ------  
		if (Language.isUpdatedDictionaryBundle()==true) {
			Language.updateDictionaryFromBundleDictionary();
		}
	}
	/**
	 * This method can be used in order to change the source dictionary file of the application 
	 * to the CSV-dictionary file located at '/properties/dictionary.csv'.
	 * The idea is to translate also in different applications, as for example in MS Excel.
	 */
	public static void loadDictionaryFromCSVFile() {
		Language.getDictionaryLines().clear();
		Language.getDictionaryLines().addAll(loadDictionaryFile(DictionarySourceFile.CsvFile));
		Language.proceedLoadedDictionaryLines();
	}
	
	/**
	 * Load dictionary file from the specified source file.
	 *
	 * @param sourceFile the source file
	 * @return the list of dictionary lines
	 */
	public static List<String> loadDictionaryFile(DictionarySourceFile sourceFile) {
		
		List<String> dictLines = new ArrayList<String>();
		BufferedReader bReader = null;
		try {  
			// --- Create buffered reader to load the dictionary ----
			switch (sourceFile) {
			case DefaultFile64:
				File fileDefault = new File(Language.getDictionaryFileNameDefault64());
				if (fileDefault.exists()==true) {
					bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileDefault)));
				}
				break;

			case CsvFile:
				File fileCSV = new File(Language.getDictionaryFileNameCSV());
				if (fileCSV.exists()==true) {
					bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileCSV)));
				}
				break;
				
			case BundleFile:
				String bundleFileName = Language.getDictionaryFileNameBundle();
				if (bundleFileName!=null) {
					InputStream inStream = Language.class.getResourceAsStream(bundleFileName);
					if (inStream!=null) {
						bReader = new BufferedReader(new InputStreamReader(inStream));
					} else {
						System.err.println("[" +  Language.class.getSimpleName() + "] Could not read dictionary from bunlde file '" + bundleFileName + "'!");
					}
				}
				break;
			}
							
			if (bReader!=null) {
				// --- Read the lines from file ---------------------
				String line;
				while ((line = bReader.readLine()) != null) {
					// --- Do some conversions ----------------------
					switch (sourceFile) {
					case DefaultFile64:
					case BundleFile:
						line = new String(Base64.decodeBase64(line.getBytes()), "UTF8");
						break;
					case CsvFile:
						line = line.replace(getNewLineReplacer(), getNewLine());
						break;
					}
					dictLines.add(line);
				}
			}
			
		} catch (IOException ioEx) {        
			System.err.println("=> Error in dictionary file:");
			ioEx.printStackTrace();
			
		} finally {
			try {            
				if (bReader!=null) bReader.close();
			} catch (IOException ioEx) {            
				System.err.println("=> Error while closing dictionary file:");
				ioEx.printStackTrace();
			} 
		}     
		return dictLines;
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
		if (getDictionaryLines().size()!=0) {
			
			Language.getDictionaryHashtabel().clear(); 

			for (int i=0; i < getDictionaryLines().size(); i++) {
				
				String line = getDictionaryLines().get(i);
				if (line!=null && line.isEmpty()==false) {
					// --- Split the dictionary line --------------------------
					String[] valueArray = line.split(VALUE_SEPERATOR, -1);
					if (valueArray[0].isEmpty()==false) {
						// ----------------------------------------------------
						// --- Used to identify the Header of the dictionary --
						if (valueArray[0].equalsIgnoreCase(Language.SOURCE_LANG)) {
							// --- Remind this header -------------------------
							dictLangHeaderArray = valueArray;
							// --- Which Language has to be used --------------
							currLanguageIndex = getIndexOfLanguage(Language.getLanguage());	
							// --- index the Header ---------------------------
							getDictionaryHashtabel().put( valueArray[0], i);
							
						} else {
							// ------------------------------------------------
							// --- index the expression -----------------------
							int indexOfExpression = getIndexOfLanguage(valueArray[0]);
							if ( !(indexOfExpression==-1 || indexOfExpression-1 > valueArray.length)) {
								String indexExpression = valueArray[indexOfExpression];
								getDictionaryHashtabel().put(indexExpression, i); 			
							}
						}
						// ----------------------------------------------------
					}
				}
			}

		}

		// --- If there was no proper header, define it now -------------------
		if (getDictionaryHashtabel().get(Language.SOURCE_LANG)==null) {
			// --- This entry is mandatory for the dictionary -----------------
			getDictionaryLines().add(0, dictLangHeaderDefault);
			getDictionaryHashtabel().put(Language.SOURCE_LANG, 0);
			dictLangHeaderArray = dictLangHeaderDefault.split(VALUE_SEPERATOR, -1);
			// --- Reload dictionary ------------------------------------------
			if (Language.saveDictionaryFile()==true) {
				Language.getDictionaryLines().clear();
				Language.getDictionaryHashtabel().clear(); 
				Language.loadDictionaryFromDefaultFile();
			}
		}
		
	}
		
	/**
	 *  Saving the file 'dictionary.csv' to the folder properties
	 * @return true, if successful
	 */
	public static boolean saveDictionaryFile() {
		
		boolean saved = false;
		
		BufferedWriter bw = null;
		BufferedWriter bw64 = null;
		
		List<String> dictSorted = new ArrayList<String>(getDictionaryLines()); 
		Collections.sort(dictSorted);
		
		try { 
			// --- UTF8-File for the dictionary --------------------------------
			FileOutputStream fos = new FileOutputStream(getDictionaryFileNameCSV());
			OutputStreamWriter osw = new OutputStreamWriter(fos); 
			bw = new BufferedWriter(osw);
	    	// --- Base64 encoded file of the dictionary ----------------------
			FileOutputStream fos64 = new FileOutputStream(getDictionaryFileNameDefault64());
			OutputStreamWriter osw64 = new OutputStreamWriter(fos64); 
			bw64 = new BufferedWriter(osw64);
		    
			// --- Save dictionary lines --------------------------------------
			for (int i = 0; i < dictSorted.size(); i++) {
				
				String line = dictSorted.get(i);
				if (line.trim().equals(getEmptyLine())==false) {
					// --- TXT-Version of the dictionary ----------------------
					String txtLine = line.replace(getNewLine(), getNewLineReplacer());
					txtLine = txtLine.replace("\n", getNewLineReplacer());
					txtLine = txtLine.replace("\r", getNewLineReplacer());
					bw.write(txtLine);
			    	bw.newLine();
			    	// --- Base64 encoded version of the dictionary -----------
			    	String encodedLine = new String(Base64.encodeBase64(line.getBytes("UTF8")));
			    	bw64.write(encodedLine);
			    	bw64.newLine();
				}
			}
			saved = true;
		    
		} catch (ArrayIndexOutOfBoundsException aiEx) { 
			System.out.println( "=> ArrayIndexOutOfBoundsException while saving dictionary file:");
			aiEx.printStackTrace();
			
		} catch (IOException ioEx) { 
			System.out.println( "=> IO-Error while saving dictionary file: ");
			ioEx.printStackTrace();
			
		} finally {
			try {
				if (bw!=null) bw.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} 
			try {
				if (bw64!=null) bw64.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} 
		}
		return saved;
	}
	
	
	/**
	 * Returns the current new line string.
	 * @return the new line
	 */
	private static String getNewLine() {
		if (newLine==null) {
			newLine = System.getProperty("line.separator");
		}
		return newLine;
	}
	/**
	 * Returns the new line replacer string .
	 * @return the new line
	 */
	private static String getNewLineReplacer() {
		if (newLineReplacer==null) {
			newLineReplacer = "<br>";
		}
		return newLineReplacer;
	}
	/**
	 * Returns the file location for the base64 encoded dictionary file as string.
	 * @return the absolute file location as string 
	 */
	private static String getDictionaryFileNameDefault64() {
		if (dictFileNameDefault64==null) {
			dictFileNameDefault64 = PathHandling.getFileDictionary(true, true);
		}
		return dictFileNameDefault64;
	}
	/**
	 * Returns the file location for the dictionary file as string.
	 * @return the absolute file location as string 
	 */
	private static String getDictionaryFileNameCSV() {
		if (dictFileNameCSV==null) {
			dictFileNameCSV = PathHandling.getFileDictionary(false, true);
		}
		return dictFileNameCSV;
	}
	/**
	 * Returns the file location for the dictionary file as string.
	 * @return the absolute file location as string 
	 */
	private static String getDictionaryFileNameBundle() {
		if (dictFileNameBundle==null) {
			dictFileNameBundle = PathHandling.getFileDictionary(true, false);
			dictFileNameBundle = dictFileNameBundle.replace("\\", "/");
			if (dictFileNameBundle.startsWith("/")==false) {
				dictFileNameBundle = "/" + dictFileNameBundle;
			}
		}
		return dictFileNameBundle;
	}
	
	/**
	 * Check and provide dictionary files.
	 * @param overwriteExistingFile the indicator to overwrite existing file
	 */
	public static void checkAndProvideDictionaryFiles(boolean overwriteExistingFile) {
		File propFile = PathHandling.getPropertiesPath(true).toFile();
		PropertyContentProvider pcProvider = new PropertyContentProvider(propFile);
		pcProvider.checkAndProvidePropertyContent(FileToProvide.DICTIONARY_CSV, overwriteExistingFile);
		pcProvider.checkAndProvidePropertyContent(FileToProvide.DICTIONARY_BIN, overwriteExistingFile);
	}
	
	// -------------------------------------------------------------------------
	// --- From here, the dictionary update from bundle is implemented ---------
	// -------------------------------------------------------------------------
	/**
	 * Returns the local bundle.
	 * @return the bundle
	 */
	private static Bundle getBundle() {
		return FrameworkUtil.getBundle(Language.class);
	}
	/**
	 * Returns the current eclipse preferences.
	 * @return the eclipse preferences
	 */
	public static IEclipsePreferences getEclipsePreferences() {
		if (eclipsePreferences==null) {
			IScopeContext iScopeContext = ConfigurationScope.INSTANCE;
			eclipsePreferences = iScopeContext.getNode(Language.getBundle().getSymbolicName());
		}
		return eclipsePreferences;
	}
	
	/**
	 * Sets the language.
	 * @param newLanguage the new language
	 */
	public static void setLanguage(String newLanguage) {
		if (newLanguage==null || newLanguage.isBlank()==true || newLanguage.equals(getLanguage())==true) return;
		try {
			currLanguage = newLanguage;
			Language.getEclipsePreferences().put("ApplicationLanguage", newLanguage);
			Language.getEclipsePreferences().flush();
		} catch (BackingStoreException bsEx) {
			bsEx.printStackTrace();
		}
	}
	/**
	 * Returns the current language.
	 * @return the language
	 */
	public  static String getLanguage() {
		if (currLanguage==null) {
			currLanguage = Language.getEclipsePreferences().get("ApplicationLanguage", Language.EN);
		}
		return currLanguage;
	}
	
	/**
	 * Checks if the currently running bundle is an updated version in comparison 
	 * to the previously executed version.
	 * 
	 * @return true, if the current core bundle version is different to the previous execution
	 */
	private static boolean isUpdatedDictionaryBundle() {
		if (isUpdatedDictionaryBundle==null) {
			// --- Get the current bundle version -------------------
			Bundle bundle = Language.getBundle();
			if (bundle==null) return false;
			
			String bundleVersionProperty = bundle.getSymbolicName() + "." + "version";
			Version version = bundle.getVersion();
			String bundleVersionString = version.toString();
			
			String propsVersionString = Language.getEclipsePreferences().get(bundleVersionProperty, null);
			if (propsVersionString==null || propsVersionString.isEmpty()==true || propsVersionString.equals(bundleVersionString)==false) {
				// --- A different language bundle was executed -----
				try {
					Language.getEclipsePreferences().put(bundleVersionProperty, bundleVersionString);
					Language.getEclipsePreferences().flush();
					isUpdatedDictionaryBundle = true;
				} catch (BackingStoreException bsEx) {
					bsEx.printStackTrace();
					isUpdatedDictionaryBundle = false;
				}
				
			} else {
				isUpdatedDictionaryBundle = false;
			}
		}
		return isUpdatedDictionaryBundle;
	}
	
	/**
	 * Updates the current dictionary from the dictionary located in the bundle. This 
	 * is especially used after an AWB update, were a dictionary is already located
	 * in the properties directory (which will not be exchanged with an update).
	 */
	public static void updateDictionaryFromBundleDictionary() {
		
		try {
			
			// --- Ensure that the regular dictionary is loaded -----
			if (Language.getDictionaryLines().size()==0) Language.loadDictionaryFromDefaultFile();

			// --- Load dictionary from within bundle ---------------
			List<String> bundleDictLines = loadDictionaryFile(DictionarySourceFile.BundleFile);
			if (bundleDictLines==null) return;
			
			// --- Check each dictionary line -----------------------
			for (int i = 0; i < bundleDictLines.size(); i++) {
				
				String dictLine = bundleDictLines.get(i);
				if (dictLine!=null && dictLine.isEmpty()==false) {
					// --- Split the dictionary line ----------------
					String[] valueArray = dictLine.split(VALUE_SEPERATOR, -1);
					if (valueArray[0].isEmpty()==false && valueArray[0].equalsIgnoreCase(Language.SOURCE_LANG)==false) {
						// --- Get source language and expression ---
						int indexOfSourceExpression = getIndexOfLanguage(valueArray[0]);
						if ( !(indexOfSourceExpression==-1 || indexOfSourceExpression-1 > valueArray.length)) {
							// --- Update single line ---------------
							String sourceExpression = valueArray[indexOfSourceExpression];
							try {
								updateDictionaryRow(sourceExpression, valueArray);
							} catch (Exception ex) {
								System.err.println("[" + Language.class.getClassLoader() + "] Unable to update dictionary row for expression '" + sourceExpression + "'!");
								ex.printStackTrace();
							}
						}
					} 
				}
			} // end for
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Updates the specified dictionary row (see sourceExpression) with the specified valueArray.
	 *
	 * @param expression the expression that is the source expression for translations
	 * @param newValueArray the value array
	 */
	public static void updateDictionaryRow(String expression, String[] newValueArray) {
		
		Integer dictLineIndex = getDictionaryHashtabel().get(expression);	
		if (dictLineIndex==null) {
			// --- New expression for the property dictionary -------
			String dictLineNew = joinString(VALUE_SEPERATOR, newValueArray);
			getDictionaryLines().add(dictLineNew);
			getDictionaryHashtabel().put(expression, getDictionaryLines().size()-1);			
			
		} else {
			// --- Expression is not new ----------------------------
			String dictLineLocal = getDictionaryLines().get(dictLineIndex);
			String[] dictLineLocalArray = dictLineLocal.split(VALUE_SEPERATOR, -1);

			String[] dictLineUpdateArray = new String[Math.max(dictLineLocalArray.length, newValueArray.length)];
			for (int i = 0; i < dictLineUpdateArray.length; i++) {
				
				String localValue  = i<=dictLineLocalArray.length-1 ? dictLineLocalArray[i] : null;
				String newValue    = i<=newValueArray.length-1 ? newValueArray[i] : null;
				
				String updateValue = null;
				if (isNullOrEmpty(localValue)==true && isNullOrEmpty(newValue)==true) {
					// --- Stay null ----------------------
				} else if (isNullOrEmpty(localValue)==true && isNullOrEmpty(newValue)==false) {
					updateValue = newValue;
				} else if (isNullOrEmpty(localValue)==false && isNullOrEmpty(newValue)==true) {
					updateValue = localValue;
				} else {
					// --- External or local value ? ------
					// --- ... and the winner is ... ------
					updateValue = newValue;
				}
				dictLineUpdateArray[i] = updateValue;
			}
			
			// --- Update the dictionary line? ------------
			String dictLineUpdate = joinString(VALUE_SEPERATOR, dictLineUpdateArray);
			if (dictLineUpdate.equals(dictLineLocal)==false) {
				getDictionaryLines().set(dictLineIndex, dictLineUpdate);
			}
		}
	}
	/**
	 * Checks if is null or empty.
	 *
	 * @param checkValue the check value
	 * @return true, if is null or empty
	 */
	private static boolean isNullOrEmpty(String checkValue) {
		return ! (checkValue!=null && checkValue.isEmpty()==false);
	} 
	/**
	 * Joins the specified array to one string using the specified delimiter.
	 *
	 * @param delimiter the delimiter
	 * @param stringArray the string array
	 * @return the string
	 */
	public static String joinString(String delimiter, String[] stringArray) {
		
		if (stringArray==null || stringArray.length==0) return null;
		
		String joined = null;
		for (int i = 0; i < stringArray.length; i++) {
			String value = stringArray[i]==null || stringArray[i].equalsIgnoreCase("null")==true ? "" : stringArray[i];
			if (joined==null || joined.isEmpty()==true) {
				joined = value;
			} else {
				joined += delimiter + value;
			}
		}
		return joined;
	}
	
}