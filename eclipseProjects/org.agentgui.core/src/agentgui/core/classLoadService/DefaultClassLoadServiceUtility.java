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
 * The Class ClassLoadServiceUtility represents the default implementation for the 
 * {@link ClassLoadServiceUtility}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DefaultClassLoadServiceUtility extends AbstractClassLoadServiceUtility {

	private ClassLoadService classLoadService;
	
	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getClassLoadService(java.lang.String)
	 */
	@Override
	protected ClassLoadService getClassLoadService(String className) {
		if (classLoadService==null) {
			classLoadService = new ClassLoadServiceImpl();
		}
		return classLoadService;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException {
		return this.getClassLoadService(className).forName(className);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return this.getClassLoadService(className).newInstance(className);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getAgentClass(java.lang.String)
	 */
	@Override
	public Class<? extends Agent> getAgentClass(String agentClassName) throws NoClassDefFoundError, ClassNotFoundException {
		return this.getClassLoadService(agentClassName).getAgentClass(agentClassName);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getTimeModelInstance(java.lang.String)
	 */
	@Override
	public TimeModel getTimeModelInstance(String className) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, SecurityException {
		return this.getClassLoadService(className).getTimeModelInstance(className);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getPlugInInstance(java.lang.String, agentgui.core.project.Project)
	 */
	@Override
	public PlugIn getPlugInInstance(String pluginClassName, Project project) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return this.getClassLoadService(pluginClassName).getPlugInInstance(pluginClassName, project);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getOntologyInstance(java.lang.String)
	 */
	@Override
	public Ontology getOntologyInstance(String ontologyClassName) throws ClassNotFoundException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		return this.getClassLoadService(ontologyClassName).getOntologyInstance(ontologyClassName);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getOntologyClassVisualisationInstance(java.lang.String)
	 */
	@Override
	public OntologyClassVisualisation getOntologyClassVisualisationInstance(String ontologyClassVisualisationClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SecurityException {
		return this.getClassLoadService(ontologyClassVisualisationClassName).getOntologyClassVisualisationInstance(ontologyClassVisualisationClassName);
	}
	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getNetworkComponentAdapterInstance(java.lang.String, agentgui.envModel.graph.controller.GraphEnvironmentController)
	 */
	@Override
	public NetworkComponentAdapter getNetworkComponentAdapterInstance(String adapterClassname, GraphEnvironmentController graphController) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return this.getClassLoadService(adapterClassname).getNetworkComponentAdapterInstance(adapterClassname, graphController);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getStaticLoadBalancing(java.lang.String, jade.core.Agent)
	 */
	@Override
	public StaticLoadBalancingBase getStaticLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return this.getClassLoadService(balancingClassName).getStaticLoadBalancing(balancingClassName, executingAgent);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.classLoadService.AbstractClassLoadServiceUtility#getDynamicLoadBalancing(java.lang.String, jade.core.Agent)
	 */
	@Override
	public DynamicLoadBalancingBase getDynamicLoadBalancing(String balancingClassName, Agent executingAgent) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		return this.getClassLoadService(balancingClassName).getDynamicLoadBalancing(balancingClassName, executingAgent);
	}

}
