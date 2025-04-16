package de.enflexit.common.crypto;

/**
 * The enumeration KeyStoreType provides a listing of supported key store types with
 * additional information such as file extension and other.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum KeyStoreType {

//	DKS    ("dks",	  "jks", 	"Domain KeyStore / Collection of KeyStores"),
	JCEKS  ("jceks",  "jceks", 	"Java Cryptography Extension KeyStore"),	
	JKS    ("jks",	  "jks", 	"Java Keystore"), 	
	PKCS12 ("pkcs12", "pfx", 	"Standard keystore type");
	
	private String type;
	private String fileExtension;
	private String description;
	
	/**
	 * Instantiates a new key store type.
	 *
	 * @param type the type
	 * @param fileExtension the file extension of the keystore
	 * @param description the description
	 */
	private KeyStoreType(String type, String fileExtension, String description) {
		this.type = type;
		this.fileExtension = fileExtension;
		this.description = description;
	}
	
	/**
	 * Returns the type of the keystore. See the KeyStore section in the Java Security Standard 
	 * Algorithm Names Specification for information about standard keystore types.
     * 
	 * @return the type of the keystore
	 * 
	 * @see java.security.KeyStore#getInstance(String)
	 */
	public String getType() {
		return type;
	}
	/**
	 * Returns the file extension for the current keystore type.
	 * @return the file extension
	 */
	public String getFileExtension() {
		return fileExtension;
	}
	/**
	 * Returns the description of the KeyStore.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Return all file extensions.
	 * @return all locally known file extensions
	 */
	public static String[] getAllFileExtensions() {
		String [] fileExtensionArray = new String[values().length];
		int i = 0;
		for (KeyStoreType keyStoreType : values()) {
			fileExtensionArray[i] = keyStoreType.getFileExtension();
			i++;
		}
		return fileExtensionArray;
	}
	/**
	 * Return the KeyStoreType by the specified file extension.
	 *
	 * @param fileExtension the file extension
	 * @return the key store type by file extension
	 */
	public static KeyStoreType getKeyStoreTypeByFileExtension(String fileExtension) {
		if (fileExtension==null) return null;
		for (KeyStoreType keyStoreType : values()) {
			if (keyStoreType.getFileExtension().equals(fileExtension)) return keyStoreType;
		}
		return null;
	}
	
}
