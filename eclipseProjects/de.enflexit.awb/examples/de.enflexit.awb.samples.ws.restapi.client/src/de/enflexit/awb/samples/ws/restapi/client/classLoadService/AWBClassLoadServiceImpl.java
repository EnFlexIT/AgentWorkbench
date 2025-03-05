package de.enflexit.awb.samples.ws.restapi.client.classLoadService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.enflexit.awb.core.classLoadService.ClassLoadService;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.plugins.PlugIn;
import de.enflexit.awb.simulation.balancing.DynamicLoadBalancingBase;
import de.enflexit.awb.simulation.balancing.StaticLoadBalancingBase;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import jade.content.onto.Ontology;
import jade.core.Agent;

/**
 * The Class BaseClassLoadServiceImpl represents the .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AWBClassLoadServiceImpl implements ClassLoadService {


	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException, NoClassDefFoundError {
		return Class.forName(className);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException,InvocationTargetException, NoSuchMethodException, SecurityException, NoClassDefFoundError {
	    //https://stackoverflow.com/questions/46393863/what-to-use-instead-of-class-newinstance
	    return this.forName(className).getDeclaredConstructor().newInstance();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#getAgentClass(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Agent> getAgentClass(String agentClassName) throws NoClassDefFoundError, ClassNotFoundException {
		return (Class<? extends Agent>) Class.forName(agentClassName);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#getTimeModelInstance(java.lang.String)
	 */
	@Override
	public TimeModel getTimeModelInstance(String className) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, SecurityException, InvocationTargetException, NoSuchMethodException {
		@SuppressWarnings("unchecked")
		Class<? extends TimeModel> timeModelClass = (Class<? extends TimeModel>) Class.forName(className);
		return (TimeModel) timeModelClass.getDeclaredConstructor().newInstance();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#getPlugInInstance(java.lang.String, agentgui.core.project.Project)
	 */
	@Override
	public PlugIn getPlugInInstance(String pluginClassName, Project project) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		// --- Get the class of the class reference -----------------
		Class<?> plugInClass = (Class<?>) Class.forName(pluginClassName);
		
		// --- Look for the right constructor parameter -------------
		Class<?>[] conParameter = new Class[1];
		conParameter[0] = Project.class;
	
		// --- Get the constructor ----------------------------------
		Constructor<?> plugInClassConstructor = plugInClass.getConstructor(conParameter);
		
		// --- Define the argument for the newInstance call ---------
		Object[] args = new Object[1];
		args[0] = project;
		
		return (PlugIn) plugInClassConstructor.newInstance(args);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#getOntologyInstance(java.lang.String)
	 */
	@Override
	public Ontology getOntologyInstance(String ontologyClassName) throws ClassNotFoundException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		Class<?> currOntoClass = Class.forName(ontologyClassName);
		Method method = currOntoClass.getMethod("getInstance", new Class[0]);
		return (Ontology) method.invoke(currOntoClass, new Object[0]);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#getStaticLoadBalancing(java.lang.String, jade.core.Agent)
	 */
	@Override
	public StaticLoadBalancingBase getStaticLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		@SuppressWarnings("unchecked")
		Class<? extends StaticLoadBalancingBase> staLoBaClass = (Class<? extends StaticLoadBalancingBase>) Class.forName(balancingClassName);
		return staLoBaClass.getDeclaredConstructor( new Class[] { executingAgent.getClass() }).newInstance(new Object[] {executingAgent});
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.BaseClassLoadService#getDynamicLoadBalancing(java.lang.String, jade.core.Agent)
	 */
	@Override
	public DynamicLoadBalancingBase getDynamicLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		@SuppressWarnings("unchecked")
		Class<? extends DynamicLoadBalancingBase> dynLoBaClass = (Class<? extends DynamicLoadBalancingBase>) Class.forName(balancingClassName);
		return dynLoBaClass.getDeclaredConstructor( new Class[] { executingAgent.getClass() }).newInstance( new Object[] { executingAgent });
	}

	
}
