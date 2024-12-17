package de.enflexit.awb.core.classLoadService;

import java.lang.reflect.InvocationTargetException;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.plugins.PlugIn;
import de.enflexit.awb.simulation.balancing.DynamicLoadBalancingBase;
import de.enflexit.awb.simulation.balancing.StaticLoadBalancingBase;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.common.classLoadService.BaseClassLoadService;
import jade.core.Agent;

/**
 * The Interface for the BaseClassLoadService within an OSGI-bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ClassLoadService extends BaseClassLoadService {

    /**
     * Has to returns the agent class for the specified agent class name.
     *
     * @param agentClassName the agent class name
     * @return the agent class
     * @throws NoClassDefFoundError   the no class def found error
     * @throws ClassNotFoundException the class not found exception
     */
    public Class<? extends Agent> getAgentClass(String agentClassName) throws NoClassDefFoundError, ClassNotFoundException;

    /**
     * Has to return a {@link TimeModel} instance from the specified class name.
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
    public TimeModel getTimeModelInstance(String className) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, SecurityException, InvocationTargetException, NoSuchMethodException;

    /**
     * Has to return the {@link PlugIn} instance for the specified class name.
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
    public PlugIn getPlugInInstance(String pluginClassName, Project project) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * Has to returns an instance of a static load balancing to use by the specified
     * agent.
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
    public StaticLoadBalancingBase getStaticLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * Has to returns an instance of a static load balancing to use by the specified
     * agent.
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
    public DynamicLoadBalancingBase getDynamicLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

}
