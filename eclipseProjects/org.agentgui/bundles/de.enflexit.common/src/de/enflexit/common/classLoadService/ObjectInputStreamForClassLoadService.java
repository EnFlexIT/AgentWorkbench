package de.enflexit.common.classLoadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The Class ObjectInputStreamForClassLoadService extends an ObjectInputStream, but
 * for resolving the class that is to be used, the class load service will be used.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ObjectInputStreamForClassLoadService extends ObjectInputStream {

	private Class<?> utilityClass;

	
	/**
	 * Instantiates a new ObjectInputStream that uses the class load service.
	 *
	 * @param in the InputStream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ObjectInputStreamForClassLoadService(InputStream in) throws IOException {
		this(in, null);
	}
	/**
	 * Instantiates a new ObjectInputStream that uses the class load service.
	 *
	 * @param in the InputStream
	 * @param utilityClass the utility class that provides the static methods
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ObjectInputStreamForClassLoadService(InputStream in, Class<?> utilityClass) throws IOException {
		super(in);
		this.setLocalClassLoadService(utilityClass);
	}
	
	/**
	 * Sets the local class load service to be used to resolve classes.
	 * @param utilityClass the new local class load service
	 */
	public void setLocalClassLoadService(Class<?> utilityClass) {
		this.utilityClass = utilityClass;
	}
	
	/* (non-Javadoc)
	 * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
	 */
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {

		if (this.utilityClass!=null) {
			try {
				Method method = this.utilityClass.getMethod("forName", String.class);
				Object returnObject = method.invoke(null, desc.getName());
				Class<?> returnClass = (Class<?>) returnObject;
				return returnClass;

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				//ex.printStackTrace();
			}
		}
		return BaseClassLoadServiceUtility.forName(desc.getName());
	}
	
}
