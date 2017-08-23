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
 * The Class ClassLoadServiceUtility provides static access to load classes or to initialize them.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassLoadServiceUtility {
	
	private static AbstractClassLoadServiceUtility classLoadServiceUtility; 
	
	
	/**
	 * Returns the current class load service utility.
	 * @param newClassLoadServiceUtility the new ClassLoadServiceUtility
	 */
	public static void setClassLoadServiceUtility(AbstractClassLoadServiceUtility newClassLoadServiceUtility) {
		classLoadServiceUtility = newClassLoadServiceUtility;
	}
	/**
	 * Return the current ClassLoadServiceUtility.
	 * @return the class load service utility
	 */
	public static AbstractClassLoadServiceUtility getClassLoadServiceUtility() {
		if (classLoadServiceUtility==null) {
			classLoadServiceUtility = new ClassLoadServiceUtilityImpl();
		}
		return classLoadServiceUtility;
	}
	
	
	/**
	 * Returns the class load service that provides the actual implementations.
	 *
	 * @param className the class name
	 * @return the class load service
	 */
	public static ClassLoadService getClassLoadService(String className) {
		return getClassLoadServiceUtility().getClassLoadService(className);
	}
	
	/**
	 * Returns the class for the specified class name or reference.
	 *
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Class<?> forName(String className) throws ClassNotFoundException {
		return getClassLoadServiceUtility().forName(className);
	}
	
	/**
	 * Returns a new instance of the specified class.
	 *
	 * @param className the class name
	 * @return the object
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return getClassLoadServiceUtility().newInstance(className);
	}
	
}
