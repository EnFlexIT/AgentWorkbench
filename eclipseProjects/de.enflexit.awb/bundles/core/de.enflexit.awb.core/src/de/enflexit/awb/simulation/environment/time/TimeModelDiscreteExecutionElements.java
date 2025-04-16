package de.enflexit.awb.simulation.environment.time;

import java.awt.Color;

import de.enflexit.language.Language;

/**
 * The Class TimeModelDiscreteExecutionElements.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelDiscreteExecutionElements extends TimeModelBaseExecutionElements {

	private final String toolBarTitle = Language.translate("Zeit");
	private TimeModelDiscrete timeModelDiscrete = null;
	
	
	/**
	 * Instantiates a new time model discrete execution elements.
	 */
	public TimeModelDiscreteExecutionElements() {
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModelBaseExecutionElements#getToolBarTitle()
	 */
	@Override
	public String getToolBarTitle() {
		return this.toolBarTitle;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModelBaseExecutionElements#setTimeModel(de.enflexit.awb.simulation.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		this.timeModelDiscrete = (TimeModelDiscrete) timeModel;
		if (this.timeModelDiscrete==null) {
			this.getJLabelTimeDisplay().setText("");			

		} else {
			
			long time = this.timeModelDiscrete.getTime();
			long timeStop = this.timeModelDiscrete.getTimeStop();
			String timeFormat = this.timeModelDiscrete.getTimeFormat();
			
			// --- Set color of time display --------------
			if (time<=timeStop) {
				this.getJLabelTimeDisplay().setForeground(new Color(0, 153, 0));
			} else {
				this.getJLabelTimeDisplay().setForeground(new Color(255, 51, 0));	
			}
			
			switch (this.view) {
			case ViewCOUNTDOWN:
				// --- countdown view ---------------------
				long timeDiff = timeStop-time;
				if (timeDiff<=0) {
					this.view = ViewTIMER;
					this.setTimeModel(timeModel);
					return;
				}

				// --- Set display for the current time --- 
				this.getJLabelTimeDisplay().setText(this.getTimeDurationFormatted(timeDiff, timeFormat));
				this.getJButtonTimeConfig().setText(this.getJMenuItemViewCountdown().getText());
				break;
				
			default:
				// --- timer view -------------------------
				this.getJLabelTimeDisplay().setText(this.getTimeFormatted(time, this.timeModelDiscrete.getZoneId(), timeFormat));
				this.getJButtonTimeConfig().setText(this.getJMenuItemViewTimer().getText());
				break;
			}
			
		}

	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModelBaseExecutionElements#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {
		return this.timeModelDiscrete;
	}
	

}
