package de.enflexit.api;

/**
 * The Interface for Translator.
 */
public interface Translator {
	
	/**
	 * The enumeration that describes possible source languages.
	 */
	public enum SourceLangugae {
		DE("DE"),
		EN("EN"),
		IT("IT"),
		ES("ES"),
		FR("FR");
		
		private final String languageIndicator;
		
		private SourceLangugae(final String languageIndicator) {
			this.languageIndicator = languageIndicator;
		}
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return languageIndicator;
		}
	}
	
	/**
	 * Has to translate the specified input.
	 *
	 * @param expression the expression to translate
	 * @return the string
	 */
	String dynamicTranslate(String expression);
	
	/**
	 * Has to translate the specified input, where the second parameter 
	 * describes the source language in which the input is given.
	 *
	 * @param expression the expression to translate
	 * @param sourceLanguage the source language
	 * @return the string
	 */
	String dynamicTranslate(String expression, SourceLangugae sourceLanguage);
	
}
