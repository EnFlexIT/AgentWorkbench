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
package org.agentgui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.common.bundleEvaluation.BundleEvaluator;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class PlugInActivator extends AbstractUIPlugin implements BundleListener {

	public static final String PLUGIN_ID = "org.agentgui.core";
	private boolean debug = false;

	private static PlugInActivator thisActivator;
	
	
	/**
	 * The constructor
	 */
	public PlugInActivator() {
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		thisActivator = this;
		context.addBundleListener(this);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		context.removeBundleListener(this);
		thisActivator = null;
		super.stop(context);
	}
	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static PlugInActivator getInstance() {
		return thisActivator;
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework.BundleEvent)
	 */
	@Override
	public void bundleChanged(BundleEvent event) {
		
		final Bundle bundle = event.getBundle();
		if (this.debug==true) {
			String symbolicName = bundle.getSymbolicName();
			String type = this.getBundleEventAsString(event);
			System.out.println(this.getClass().getSimpleName() + "#bundleChanged(event): " + symbolicName + ", event.type: " + type);
		}
		
		// ------------------------------------------------------------------------------
		// --- Make sure that only external bundles will be considered ------------------
		// ------------------------------------------------------------------------------
		if (bundle.getSymbolicName().equals(PLUGIN_ID)==false) {
			
			switch (event.getType()) {
			case BundleEvent.STARTED:
				// ----------------------------------------------------------------------
				// --- Start search for classes with the BundleEvaluator? ---------------
				// ----------------------------------------------------------------------
				new Thread(new Runnable() {
					@Override
					public void run() {
						// --- First, wait for the Hibernate start ----------------------
						HibernateUtilities.waitForSessionFactoryCreation();
						// --- If Application mode ... ----------------------------------
						boolean isApplicationMode = Application.getGlobalInfo().getExecutionMode()==ExecutionMode.APPLICATION;
						if (isApplicationMode==true) {
							// --- ... search in the new bundle -------------------------
							BundleEvaluator.getInstance().setBundleAdded(bundle);
						}
					}
				}, "BundleEvaluator Starter-Thread for " + bundle.getSymbolicName());
				break;
				
			case BundleEvent.STOPPED:
				// ----------------------------------------------------------------------
				// --- Remove search results from the BundleEvaluator -------------------
				// ----------------------------------------------------------------------
				BundleEvaluator.getInstance().setBundleRemoved(bundle);
				break;

			default:
				break;
			}
		}
				
	}
	
	/**
	 * Returns the specified bundle event as string.
	 * @param event the bundle event
	 * @return the bundle event as string
	 */
	private String getBundleEventAsString(BundleEvent event) {
		
		if (event == null) return "null";
		
		int type = event.getType();
		switch (type) {
		case BundleEvent.INSTALLED:
			return "INSTALLED";
		case BundleEvent.LAZY_ACTIVATION:
			return "LAZY_ACTIVATION";
		case BundleEvent.RESOLVED:
			return "RESOLVED";
		case BundleEvent.STARTED:
			return "STARTED";
		case BundleEvent.STARTING:
			return "STARTING";
		case BundleEvent.STOPPED:
			return "STOPPED";
		case BundleEvent.UNINSTALLED:
			return "UNINSTALLED";
		case BundleEvent.UNRESOLVED:
			return "UNRESOLVED";
		case BundleEvent.UPDATED:
			return "UPDATED";
		default:
			return "unknown event type: " + type;
		}
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
