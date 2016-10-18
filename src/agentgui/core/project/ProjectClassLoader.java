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
package agentgui.core.project;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;


public class ProjectClassLoader {

	private ArrayList<URL> urlResources;
	private PrivateClassLoader privateClassLoader;
	
	
	/**
	 * Instantiates a new project class loader.
	 */
	public ProjectClassLoader() {

	}

	/**
	 * Gets the url resources.
	 * @return the url resources
	 */
	public ArrayList<URL> getUrlResources() {
		if (urlResources==null) {
			urlResources = new ArrayList<>();
		}
		return urlResources;
	}
	private URL[] getURLArray() {
		return this.getUrlResources().toArray(new URL[this.getUrlResources().size()]);
	}
	
	
	/**
	 * Adds the specified URL to the class loader.
	 * @param url the URL
	 */
	public void addURL(URL url) {
		if (url!=null && this.getUrlResources().contains(url)==false) {
			// --- Add to resources ---------------------------------------------------------------
			this.getUrlResources().add(url);
			if (this.getUrlResources().size()==1) {
				// --- Create the private class loader (URL will be taken from the ArrayList) ----- 
				this.getPrivateClassLoader();
			} else {
				// --- Dynamically add to the private class loader --------------------------------
				this.getPrivateClassLoader().addSinlgeURL(url);
			}
		}
	}
	
	private PrivateClassLoader getPrivateClassLoader() {
		if (privateClassLoader==null) {
			privateClassLoader = new PrivateClassLoader(this.getURLArray());
		}
		return privateClassLoader;
	}
	
	
	private class PrivateClassLoader extends URLClassLoader {

		public PrivateClassLoader(URL[] urls) {
			super(urls, ClassLoader.getSystemClassLoader());
		}
		private void addSinlgeURL(URL url) {
			this.addURL(url);
		}
		
		
		
		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			System.out.println("PrivateClassLoader: find class " + name);
			return super.findClass(name);
		}
		
		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			System.out.println("PrivateClassLoader: load class " + name);
			return super.loadClass(name);
		}
		
	}
	
	
}
