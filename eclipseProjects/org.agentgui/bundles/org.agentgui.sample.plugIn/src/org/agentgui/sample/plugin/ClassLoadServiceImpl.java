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
package org.agentgui.sample.plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import agentgui.core.classLoadService.ClassLoadService;
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
 * The Class ClassLoadServiceImpl represents the .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassLoadServiceImpl implements ClassLoadService {

	
	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.ClassLoadService#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException, NoClassDefFoundError {
		return Class.forName(className);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.ClassLoadService#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return this.forName(className).newInstance();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.ClassLoadService#getAgentClass(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Agent> getAgentClass(String agentClassName) throws NoClassDefFoundError, ClassNotFoundException {
		return (Class<? extends Agent>) Class.forName(agentClassName);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.ClassLoadService#getTimeModelInstance(java.lang.String)
	 */
	@Override
	public TimeModel getTimeModelInstance(String className) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, SecurityException {
		@SuppressWarnings("unchecked")
		Class<? extends TimeModel> timeModelClass = (Class<? extends TimeModel>) Class.forName(className);
		return (TimeModel) timeModelClass.newInstance();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.ClassLoadService#getPlugInInstance(java.lang.String, agentgui.core.project.Project)
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
	 * @see agentgui.core.classLoadService.ClassLoadService#getOntologyInstance(java.lang.String)
	 */
	@Override
	public Ontology getOntologyInstance(String ontologyClassName) throws ClassNotFoundException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		Class<?> currOntoClass = Class.forName(ontologyClassName);
		Method method = currOntoClass.getMethod("getInstance", new Class[0]);
		return (Ontology) method.invoke(currOntoClass, new Object[0]);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.ClassLoadService#getNetworkComponentAdapterInstance(java.lang.String, agentgui.envModel.graph.controller.GraphEnvironmentController)
	 */
	@Override
	public NetworkComponentAdapter getNetworkComponentAdapterInstance(String adapterClassname, GraphEnvironmentController graphController) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {

		@SuppressWarnings("unchecked")
		Class<? extends NetworkComponentAdapter> nca = (Class<? extends NetworkComponentAdapter>) Class.forName(adapterClassname);
		// --- look for the right constructor parameter ---------
		Class<?>[] conParameter = new Class[1];
		conParameter[0] = GraphEnvironmentController.class;
		// --- Get the constructor ------------------------------	
		Constructor<?> ncaConstructor = nca.getConstructor(conParameter);
		// --- Define the argument for the newInstance call ----- 
		Object[] args = new Object[1];
		args[0] = graphController;
		return (NetworkComponentAdapter) ncaConstructor.newInstance(args);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.ClassLoadService#getStaticLoadBalancing(java.lang.String, jade.core.Agent)
	 */
	@Override
	public StaticLoadBalancingBase getStaticLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		@SuppressWarnings("unchecked")
		Class<? extends StaticLoadBalancingBase> staLoBaClass = (Class<? extends StaticLoadBalancingBase>) Class.forName(balancingClassName);
		return staLoBaClass.getDeclaredConstructor( new Class[] { executingAgent.getClass() }).newInstance(new Object[] {executingAgent});
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.ClassLoadService#getDynamicLoadBalancing(java.lang.String, jade.core.Agent)
	 */
	@Override
	public DynamicLoadBalancingBase getDynamicLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		@SuppressWarnings("unchecked")
		Class<? extends DynamicLoadBalancingBase> dynLoBaClass = (Class<? extends DynamicLoadBalancingBase>) Class.forName(balancingClassName);
		return dynLoBaClass.getDeclaredConstructor( new Class[] { executingAgent.getClass() }).newInstance( new Object[] { executingAgent });
	}

	
}
