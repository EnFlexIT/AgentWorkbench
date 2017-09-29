package de.enflexit.common.classLoadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * The Class ObjectInputStreamForClassLoadService extends an ObjectInputStream, but
 * for resolving the class that is to be used, the class load service will be used.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ObjectInputStreamForClassLoadService extends ObjectInputStream {

	/**
	 * Instantiates a new ObjectInputStream that uses the class load service.
	 *
	 * @param in the InputStream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ObjectInputStreamForClassLoadService(InputStream in) throws IOException {
		super(in);
	}
	/* (non-Javadoc)
	 * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		return BaseClassLoadServiceUtility.forName(desc.getName());
	}
	
}
