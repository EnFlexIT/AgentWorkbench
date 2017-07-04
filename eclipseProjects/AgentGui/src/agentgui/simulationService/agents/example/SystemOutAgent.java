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
package agentgui.simulationService.agents.example;

import java.text.SimpleDateFormat;
import java.util.Date;

import agentgui.ontology.Simple_Integer;
import agentgui.ontology.Simple_String;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * If this agent is started its prints out a test message every 5 seconds, by
 * using <code>System.out.println(String)</code> and <code>System.err.println(String)</code>.<br>  
 * The agent can be migrated to a remote container, if the background system is set up
 * and will demonstrate the usage of the remote debugging ability of <b>Agent.GUI</b>.<br> 
 * Therefore the Service 'agentgui.logging.DebugService' has to be set up within a project!
 *    
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemOutAgent extends Agent {

	private static final long serialVersionUID = 471703602130677272L;

	private Integer speakingInterval = 5;
	private String speakingText = "This is a test system output!";
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
	
		Object[] startArgs = this.getArguments();
		if (startArgs!=null) {
			if (startArgs.length>0) {
				// --- Work on the start argument ---------
				if (startArgs[0]!= null) {
					try {
						speakingInterval = ((Simple_Integer) startArgs[0]).getIntegerValue();	
					} catch (Exception e) {}
				}
				if (startArgs[1]!= null) {
					try {
						speakingText = ((Simple_String) startArgs[1]).getStringValue();
					} catch (Exception e) {}
				}
				
			}
		}
		
		if (speakingInterval==0) {
			speakingInterval = 5;
		}
		if (speakingText==null) {
			speakingText = "Error, while computing start arguments !";
		}
		this.addBehaviour(new SpeakTick(this, speakingInterval*1000));
	}
	
	
	/**
	 * The Class SpeakTick.
	 */
	private class SpeakTick extends TickerBehaviour {

		private static final long serialVersionUID = 757478665038190137L;

		/**
		 * Instantiates a new speak tick.
		 *
		 * @param a the a
		 * @param period the period
		 */
		public SpeakTick(Agent a, long period) {
			super(a, period);
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			String zeitString = sdf.format(new Date());

			System.out.println("=> " + zeitString + " (out) " + speakingText);
			System.err.println("=> " + zeitString + " (err) " + speakingText);
		}
		
	}
	
	
}
