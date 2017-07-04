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
package agentgui.simulationService.time;

import java.awt.Color;

import agentgui.core.application.Language;

/**
 * The Class TimeModelContinuousExecutionElements.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelContinuousExecutionElements extends TimeModelBaseExecutionElements {

	private final String toolBarTitle = Language.translate("Zeit");
	private TimeModelContinuous timeModelContinuous = null;
	
	private TimeSettingThread timeSettingThread = null;
	private long timeSettingThreadUpdateTime = 0;
	
	/**
	 * Instantiates a new time model discrete execution elements.
	 */
	public TimeModelContinuousExecutionElements() {
		this.getTimeSettingThread();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModelBaseExecutionElements#getToolBarTitle()
	 */
	@Override
	public String getToolBarTitle() {
		return this.toolBarTitle;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModelBaseExecutionElements#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		
		if (timeModel==null) {
			this.getJLabelTimeDisplay().setText("");			

		} else {
			this.timeModelContinuous = (TimeModelContinuous) timeModel;
			
			// --- Configure how often the time display refreshes --- 
			if (timeSettingThreadUpdateTime==0) {
				this.setTimeSettingThreadUpdateTime(this.timeModelContinuous.getAccelerationFactor());
			}
			
			long time = this.timeModelContinuous.getTime();
			long timeStop = this.timeModelContinuous.getTimeStop();
			String timeFormat = this.timeModelContinuous.getTimeFormat();
			
			// --- Set color of time display ------------------------
			if (time<=timeStop) {
				this.getJLabelTimeDisplay().setForeground(new Color(0, 153, 0));
			} else {
				this.getJLabelTimeDisplay().setForeground(new Color(255, 51, 0));	
			}
			
			switch (this.view) {
			case ViewCOUNTDOWN:
				// --- countdown view -------------------------------
				time = timeStop-time;
				if (time<=0) {
					this.view = ViewTIMER;
					this.setTimeModel(timeModel);
					return;
				}
				this.getJLabelTimeDisplay().setText(this.getTimeDurationFormatted(time, timeFormat));
				this.getJButtonTimeConfig().setText(this.getJMenuItemViewCountdown().getText());
				break;
				
			default:
				// --- timer view -----------------------------------
				this.getJLabelTimeDisplay().setText(this.getTimeFormatted(time, timeFormat));
				this.getJButtonTimeConfig().setText(this.getJMenuItemViewTimer().getText());
				break;
			}
			
		}

	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModelBaseExecutionElements#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {
		return this.timeModelContinuous;
	}

	/**
	 * Gets the time setting thread.
	 * @return the timeSettingThread
	 */
	public TimeSettingThread getTimeSettingThread() {
		if (this.timeSettingThread==null) {
			this.timeSettingThread = new TimeSettingThread();
			this.timeSettingThread.start();
		}
		return this.timeSettingThread;
	}

	/**
	 * Sets the update (or sleeping) time for the TimeSettingThread.
	 * @param accelerationFactor the factor as configured in the current time model
	 */
	private void setTimeSettingThreadUpdateTime(double accelerationFactor) {
		
		if (accelerationFactor<=0.1) {
			this.timeSettingThreadUpdateTime = 1000;
		} else if (accelerationFactor>0.1 && accelerationFactor<=0.3) {
			this.timeSettingThreadUpdateTime = 600;
		} else if (accelerationFactor>0.3 && accelerationFactor<=1.0) {
			this.timeSettingThreadUpdateTime = 250;
		} else if (accelerationFactor>1 && accelerationFactor<=5.0) {
			this.timeSettingThreadUpdateTime = 125;
		} else {
			this.timeSettingThreadUpdateTime = 100;
		}
		this.getTimeSettingThread().setSleepTime(this.timeSettingThreadUpdateTime);
	}
	
	/**
	 * The Class TimeSettingThread.
	 */
	public class TimeSettingThread extends Thread {
		
		private boolean isClockRunning = true;
		private long sleepTime= 100;
		
		/**
		 * Instantiates a new time setting thread.
		 */
		public TimeSettingThread() {
			
		}
		/**
		 * Instantiates a new time setting thread.
		 * @param sleepTime the sleep time
		 */
		public TimeSettingThread(long sleepTime) {
			this.sleepTime= sleepTime;
		}

		/**
		 * Sets the clock running.
		 * @param isClockRunning the new clock running
		 */
		public synchronized void setClockRunning(boolean isClockRunning) {
			this.isClockRunning = isClockRunning;
		}
		/**
		 * Checks if is clock running.
		 * @return true, if is clock running
		 */
		public synchronized boolean isClockRunning() {
			return isClockRunning;
		}

		/**
		 * Gets the sleep time.
		 * @return the sleepTime
		 */
		public long getSleepTime() {
			return sleepTime;
		}
		/**
		 * Sets the sleep time.
		 * @param sleepTime the sleepTime to set
		 */
		public void setSleepTime(long sleepTime) {
			this.sleepTime = sleepTime;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			
			this.setName("TimeModelContinuous-DisplayThread");
			
			// --- Run the clock as long as set ---------------------
			while (isClockRunning()) {
				try {
					if (timeModelContinuous!=null) {
						synchronized (timeModelContinuous) {
							setTimeModel(timeModelContinuous);	
						}	
					}
					sleep(sleepTime);

				} catch (InterruptedException iex) {
					iex.printStackTrace();
				}
			}
		}

	}// -- end sub class

}
