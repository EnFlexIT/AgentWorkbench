package de.enflexit.awb.core;

import de.enflexit.awb.core.classLoadService.ClassLoadServiceUtility;

/**
 * The Class JadeClassLoaderService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JadeClassLoaderService implements jade.JadeClassLoaderService {

	/* (non-Javadoc)
	 * @see jade.JadeClassLoaderService#forName(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException, NoClassDefFoundError {
		
		Class<?> clazz = ClassLoadServiceUtility.forName(className);
		if (clazz==null) {
			clazz = Class.forName(className);
		}
		return clazz;
	}

	/* (non-Javadoc)
	 * @see jade.JadeClassLoaderService#forName(java.lang.String, boolean, java.lang.ClassLoader)
	 */
	@Override
	public Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException, NoClassDefFoundError {
		
		Class<?> clazz = ClassLoadServiceUtility.forName(className);
		if (clazz==null) {
			clazz = Class.forName(className, initialize, loader);
		}
		return clazz;
	}

}
