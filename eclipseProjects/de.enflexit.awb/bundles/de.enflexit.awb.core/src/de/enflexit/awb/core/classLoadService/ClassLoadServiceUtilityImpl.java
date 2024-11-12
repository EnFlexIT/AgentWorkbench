package de.enflexit.awb.core.classLoadService;

import java.lang.reflect.InvocationTargetException;

import de.enflexit.common.classLoadService.BaseClassLoadService;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.plugins.PlugIn;
import de.enflexit.common.classLoadService.AbstractClassLoadServiceUtilityImpl;
import jade.core.Agent;

/**
 * The Class BaseClassLoadServiceUtility extends the {@link AbstractClassLoadServiceUtilityImpl} 
 * and prepares the access to the {@link BaseClassLoadService} depending on the OSGI bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class ClassLoadServiceUtilityImpl extends AbstractClassLoadServiceUtilityImpl<ClassLoadService> implements ClassLoadService {

	private ClassLoadService localClassLoadService;
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtilityImpl#getLocalClassLoadService()
	 */
	@Override
	public ClassLoadService getLocalClassLoadService() {
		if (localClassLoadService==null) {
			localClassLoadService = new ClassLoadServiceImpl();
		}
		return localClassLoadService;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getAgentClass(java.lang.String)
	 */
	@Override
	public Class<? extends Agent> getAgentClass(String agentClassName) throws NoClassDefFoundError, ClassNotFoundException {
		return this.getClassLoadService(agentClassName, ClassLoadService.class).getAgentClass(agentClassName);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getTimeModelInstance(java.lang.String)
	 */
	@Override
	public TimeModel getTimeModelInstance(String className) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, SecurityException, InvocationTargetException, NoSuchMethodException {
		return this.getClassLoadService(className, ClassLoadService.class).getTimeModelInstance(className);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getPlugInInstance(java.lang.String, agentgui.core.project.Project)
	 */
	@Override
	public PlugIn getPlugInInstance(String pluginClassName, Project project) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return this.getClassLoadService(pluginClassName, ClassLoadService.class).getPlugInInstance(pluginClassName, project);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getStaticLoadBalancing(java.lang.String, jade.core.Agent)
	 */
	@Override
	public StaticLoadBalancingBase getStaticLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return this.getClassLoadService(balancingClassName, ClassLoadService.class).getStaticLoadBalancing(balancingClassName, executingAgent);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getDynamicLoadBalancing(java.lang.String, jade.core.Agent)
	 */
	@Override
	public DynamicLoadBalancingBase getDynamicLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return this.getClassLoadService(balancingClassName, ClassLoadService.class).getDynamicLoadBalancing(balancingClassName, executingAgent);
	}
	
}
