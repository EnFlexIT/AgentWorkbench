package de.enflexit.common;

import de.enflexit.api.Translator;
import de.enflexit.api.Translator.SourceLanguage;

/**
 * The Class Language is used within this bundle to allow translations.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Language {

	private static Translator translator;
	
	/**
	 * Returns translator.
	 * @return the translator
	 */
	public static Translator getTranslator() {
		return translator;
	}
	
	/**
	 * Sets the translator.
	 * @param newTranslator the new translator
	 */
	public static void setTranslator(Translator newTranslator) {
		translator = newTranslator;
	}
	
	/**
	 * Translate one expression, which is based on a German expression.
	 *
	 * @param deExpression the de expression
	 * @return the string
	 */
	public static String translate(String deExpression) {
		return translate(deExpression, SourceLanguage.DE);
	}
	
	/**
	 * Translate one expression, which is based on the language specified through 
	 * the second parameter language (use one of the languages specified as static 
	 * attribute in this class e.g. 'Language.EN')
	 *
	 * @param expression the expression
	 * @param sourceLanguage the source language
	 * @return the translation of the expression
	 */
	public static String translate(String expression, SourceLanguage sourceLanguage) {
		String translation = expression;
		if (getTranslator()!=null) {
			translation = getTranslator().dynamicTranslate(expression, sourceLanguage);
		}
		return translation;
	}

}
