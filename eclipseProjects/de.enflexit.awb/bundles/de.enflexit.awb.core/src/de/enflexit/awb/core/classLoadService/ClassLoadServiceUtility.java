package de.enflexit.awb.core.classLoadService;

import java.lang.reflect.InvocationTargetException;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.plugins.PlugIn;
import de.enflexit.awb.core.simulation.balancing.DynamicLoadBalancingBase;
import de.enflexit.awb.core.simulation.balancing.StaticLoadBalancingBase;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.common.classLoadService.AbstractClassLoadServiceUtilityImpl;
import de.enflexit.common.classLoadService.BaseClassLoadServiceUtility;
import de.enflexit.common.classLoadService.ClassLoadServiceUtilityImplManager;
import jade.core.Agent;

/**
 * The Class BaseClassLoadServiceUtility provides static access to load classes
 * or to initialize them.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassLoadServiceUtility extends BaseClassLoadServiceUtility {

	/**
	 * Return the current BaseClassLoadServiceUtility.
	 * 
	 * @return the class load service utility
	 */
	private static ClassLoadServiceUtilityImpl getClassLoadServiceUtilityImpl() {
		ClassLoadServiceUtilityImplManager clsu = ClassLoadServiceUtilityImplManager.getInstance();
		AbstractClassLoadServiceUtilityImpl<?> cls = clsu.getClassLoadServiceUtilityImpl(ClassLoadService.class);
		if (cls == null || !(cls instanceof ClassLoadService)) {
			cls = new ClassLoadServiceUtilityImpl();
			clsu.registerClassLoadServiceUtilityImpl(ClassLoadService.class, cls);
		}
		return (ClassLoadServiceUtilityImpl) cls;
	}

	/**
	 * Returns the class load service that provides the actual implementations.
	 *
	 * @param className the class name
	 * @return the class load service
	 */
	public static ClassLoadService getClassLoadService(String className) {
		return getClassLoadServiceUtilityImpl().getClassLoadService(className, ClassLoadService.class);
	}

	/**
	 * Returns the agent class from the specified agent class name.
	 *
	 * @param agentClassName the agent class name
	 * @return the agent class
	 * @throws NoClassDefFoundError   the no class definition found error
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Class<? extends Agent> getAgentClass(String agentClassName) throws NoClassDefFoundError, ClassNotFoundException {
		return getClassLoadServiceUtilityImpl().getAgentClass(agentClassName);
	}

	/**
	 * Returns a time model instance from the specified class name.
	 *
	 * @param className the class name
	 * @return the time model instance
	 * @throws ClassNotFoundException    the class not found exception
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws InstantiationException    the instantiation exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws SecurityException         the security exception
	 * @throws NoSuchMethodException     if a matching method is not found
	 * @throws InvocationTargetException if the underlying constructor throws an exception
	 */
	public static TimeModel getTimeModelInstance(String className) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, SecurityException, InvocationTargetException, NoSuchMethodException {
		return getClassLoadServiceUtilityImpl().getTimeModelInstance(className);
	}

	/**
	 * Returns the {@link PlugIn} instance for the specified class name.
	 *
	 * @param pluginClassName the {@link PlugIn} class name
	 * @param project         the project
	 * @return the plug in instance
	 * @throws ClassNotFoundException    the class not found exception
	 * @throws SecurityException         the security exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws InstantiationException    the instantiation exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public static PlugIn getPlugInInstance(String pluginClassName, Project project) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return getClassLoadServiceUtilityImpl().getPlugInInstance(pluginClassName, project);
	}

	/**
	 * Returns an instance of a static load balancing to use by the specified agent.
	 *
	 * @param balancingClassName the balancing class name
	 * @param executingAgent     the executing agent
	 * @return the static load balancing
	 * @throws ClassNotFoundException    the class not found exception
	 * @throws SecurityException         the security exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws InstantiationException    the instantiation exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public static StaticLoadBalancingBase getStaticLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return getClassLoadServiceUtilityImpl().getStaticLoadBalancing(balancingClassName, executingAgent);
	}

	/**
	 * Returns an instance of a dynamic load balancing to use by the specified
	 * agent.
	 *
	 * @param balancingClassName the balancing class name
	 * @param executingAgent     the executing agent
	 * @return the dynamic load balancing
	 * @throws ClassNotFoundException    the class not found exception
	 * @throws SecurityException         the security exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws InstantiationException    the instantiation exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public static DynamicLoadBalancingBase getDynamicLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return getClassLoadServiceUtilityImpl().getDynamicLoadBalancing(balancingClassName, executingAgent);
	}

}
