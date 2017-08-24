package de.enflexit.common.classLoadService;


/**
 * The Class BaseClassLoadServiceImpl represents the .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BaseClassLoadServiceImpl implements BaseClassLoadService {

	
	/* (non-Javadoc)
	 * @see energy.classLoadService.ClassLoadService#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}
	
	/* (non-Javadoc)
	 * @see energy.classLoadService.ClassLoadService#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return this.forName(className).newInstance();
	}


}
