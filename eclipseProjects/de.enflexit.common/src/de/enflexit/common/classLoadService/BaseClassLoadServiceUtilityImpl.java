package de.enflexit.common.classLoadService;

/**
 * The Class ClassLoadServiceUtilityImplManager extends the {@link DefaultClassLoadServiceUtility} 
 * and prepares the access to the {@link BaseClassLoadService} depending on the OSGI bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class BaseClassLoadServiceUtilityImpl extends AbstractClassLoadServiceUtilityImpl<BaseClassLoadService> implements BaseClassLoadService {

	private BaseClassLoadService localClassLoadService;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtilityImpl#getServiceReferenceFilter()
	 */
	@Override
	public String getServiceReferenceFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtilityImpl#getLocalClassLoadService()
	 */
	@Override
	public BaseClassLoadService getLocalClassLoadService() {
		if (localClassLoadService==null) {
			localClassLoadService = new BaseClassLoadServiceImpl();
		}
		return localClassLoadService;
	}
	
	/* (non-Javadoc)
	 * @see energy.classLoadService.AbstractClassLoadServiceUtility#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException {
		return this.getClassLoadService(className).forName(className);
	}
	
	/* (non-Javadoc)
	 * @see energy.classLoadService.AbstractClassLoadServiceUtility#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return this.getClassLoadService(className).newInstance(className);
	}

	
}
