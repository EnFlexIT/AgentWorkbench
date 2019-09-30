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
package agentgui.core.application;

/**
 * The listener interface for receiving application events.
 * The class that is interested in processing a application
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addApplicationListener<code> method. When
 * the application event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ApplicationEvent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ApplicationListener {

	
	/**
	 * Will be invoked if an application event occurs.
	 * @param event the actual {@link ApplicationEvent}
	 */
	public void onApplicationEvent(ApplicationEvent event);
	
	
	
	/**
	 * The Class ApplicationEvent is used as an information object to 
	 * inform {@link ApplicationListener} about events.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	public class ApplicationEvent {
		
		public static final String PROJECT_LOADED  = "ProjectLoaded";
		public static final String PROJECT_CLOSED  = "ProjectClosed";
		public static final String PROJECT_FOCUSED = "ProjectFocused";
		
		private String applicationEvent;
		private Object eventObject;
		
		/**
		 * Instantiates a new application event.
		 * @param applicationEvent the application event
		 */
		public ApplicationEvent(String applicationEvent) {
			this(applicationEvent, null);
		}
		/**
		 * Instantiates a new application event.
		 *
		 * @param applicationEvent the application event
		 * @param eventObject the event object
		 */
		public ApplicationEvent(String applicationEvent, Object eventObject) {
			this.setApplicationEvent(applicationEvent);
			this.setEventObject(eventObject);
		}
		
		/**
		 * Returns the application event.
		 * @return the application event
		 */
		public String getApplicationEvent() {
			return applicationEvent;
		}
		/**
		 * Sets the application event.
		 * @param applicationEvent the new application event
		 */
		private void setApplicationEvent(String applicationEvent) {
			this.applicationEvent = applicationEvent;
		}

		/**
		 * Returns the event object that may transfer additional instances for the {@link ApplicationEvent}.
		 * @return the event object
		 */
		public Object getEventObject() {
			return eventObject;
		}
		/**
		 * Sets the event object.
		 * @param eventObject the new event object
		 */
		private void setEventObject(Object eventObject) {
			this.eventObject = eventObject;
		}
	}
	
}
