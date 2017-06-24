/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.classLoadService;

import java.lang.reflect.InvocationTargetException;

import agentgui.core.ontologies.gui.OntologyClassVisualisation;
import agentgui.core.plugin.PlugIn;
import agentgui.core.project.Project;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.simulationService.balancing.DynamicLoadBalancingBase;
import agentgui.simulationService.balancing.StaticLoadBalancingBase;
import agentgui.simulationService.time.TimeModel;
import jade.content.onto.Ontology;
import jade.core.Agent;


/**
 * The Class AbstractClassLoadServiceUtility provides a super class for the {@link ClassLoadServiceUtility}. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractClassLoadServiceUtility {


	/**
	 * Returns the class load service for the specified class.
	 *
	 * @param className the class name
	 * @return the class load service
	 */
	protected abstract ClassLoadService getClassLoadService(String className);

	
	/**
	 * Has to return the class for the specified class name.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 */
	public abstract Class<?> forName(String className) throws ClassNotFoundException;


	/**
	 * Has to return a new instance of the specified class.
	 *
	 * @param className the class name
	 * @return the object
	 * @throws ClassNotFoundException the class not found exception
	 */
	public abstract Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException;
	
	/**
	 * Has to return the agent class from the specified agent class name.
	 *
	 * @param agentClassName the agent class name
	 * @return the agent class
	 * @throws NoClassDefFoundError the no class definition found error
	 * @throws ClassNotFoundException the class not found exception
	 */
	public abstract Class<? extends Agent> getAgentClass(String agentClassName) throws NoClassDefFoundError, ClassNotFoundException;

	/**
	 * Has to return the time model instance from the specified class name.
	 *
	 * @param className the class name
	 * @return the time model instance
	 * @throws ClassNotFoundException the class not found exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws SecurityException the security exception
	 */
	public abstract TimeModel getTimeModelInstance(String className) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, SecurityException;

	/**
	 * Has to return the {@link PlugIn} instance for the specified class name.
	 *
	 * @param pluginClassName the {@link PlugIn} class name
	 * @param project the project
	 * @return the plug in instance
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SecurityException the security exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public abstract PlugIn getPlugInInstance(String pluginClassName, Project project) throws ClassNotFoundException, SecurityException , NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Has to return the ontology instance from the specified ontology class name.
	 *
	 * @param ontologyClassName the ontology class name
	 * @return the ontology instance
	 * @throws ClassNotFoundException the class not found exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws SecurityException the security exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public abstract Ontology getOntologyInstance(String ontologyClassName) throws ClassNotFoundException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException;

	/**
	 * Has to return the instance of the specified {@link OntologyClassVisualisation}.
	 *
	 * @param ontologyClassVisualisationClassName the ontology class visualisation class name
	 * @return the ontology class visualisation instance
	 * @throws ClassNotFoundException the class not found exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws SecurityException the security exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public abstract OntologyClassVisualisation getOntologyClassVisualisationInstance(String ontologyClassVisualisationClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SecurityException;
	
	/**
	 * Has to returns the {@link NetworkComponentAdapter} instance from the specified class name..
	 *
	 * @param adapterClassname the adapter classname
	 * @param graphController the current {@link GraphEnvironmentController} instance
	 * @return the network component adapter instance
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SecurityException the security exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public abstract NetworkComponentAdapter getNetworkComponentAdapterInstance(String adapterClassname, GraphEnvironmentController graphController) throws ClassNotFoundException, SecurityException , NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

	/**
	 * Has to returns an instance of a static load balancing to use by the specified agent.
	 *
	 * @param balancingClassName the balancing class name
	 * @param executingAgent the executing agent
	 * @return the static load balancing
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SecurityException the security exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public abstract StaticLoadBalancingBase getStaticLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException , NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

	/**
	 * Has to returns an instance of a dynamic load balancing to use by the specified agent.
	 *
	 * @param balancingClassName the balancing class name
	 * @param executingAgent the executing agent
	 * @return the dynamic load balancing
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SecurityException the security exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public abstract DynamicLoadBalancingBase getDynamicLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException , NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;



}
