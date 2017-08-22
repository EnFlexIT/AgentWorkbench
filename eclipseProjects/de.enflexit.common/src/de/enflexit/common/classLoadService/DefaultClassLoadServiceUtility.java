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
package de.enflexit.common.classLoadService;

/**
 * The Class ClassLoadServiceUtility represents the default implementation for the 
 * {@link ClassLoadServiceUtility}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DefaultClassLoadServiceUtility extends AbstractClassLoadServiceUtility {

	private ClassLoadService classLoadService;
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getClassLoadService(java.lang.String)
	 */
	@Override
	protected ClassLoadService getClassLoadService(String className) {
		if (classLoadService==null) {
			classLoadService = new ClassLoadServiceImpl();
		}
		return classLoadService;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#getClass(java.lang.String)
	 */
	@Override
	public Class<?> forName(String className) throws ClassNotFoundException {
		return this.getClassLoadService(className).forName(className);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.classLoadService.AbstractClassLoadServiceUtility#newInstance(java.lang.String)
	 */
	@Override
	public Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return this.getClassLoadService(className).newInstance(className);
	}
	
}
