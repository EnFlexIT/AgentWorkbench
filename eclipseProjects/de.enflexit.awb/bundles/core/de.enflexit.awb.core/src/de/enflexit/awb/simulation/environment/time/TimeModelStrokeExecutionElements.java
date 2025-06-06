package de.enflexit.awb.simulation.environment.time;

import java.awt.Color;
import java.awt.Dimension;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.language.Language;

/**
 * The Class TimeModelStrokeExecutionElements.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStrokeExecutionElements extends TimeModelBaseExecutionElements {

	private final String toolBarTitle = Language.translate("Zähler");
	private TimeModelStroke timeModelStroke = null;
	
		
	/**
	 * Instantiates a new time model stroke execution elements.
	 */
	public TimeModelStrokeExecutionElements() {
		this.getJLabelTimeDisplay().setPreferredSize(new Dimension(60, 26));
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
		
		this.timeModelStroke = (TimeModelStroke) timeModel;
		if (this.timeModelStroke==null) {
			this.getJLabelTimeDisplay().setText("");			

		} else {
			
			Integer counter = this.timeModelStroke.getCounter();
			Integer counterStop = this.timeModelStroke.getCounterStop();
			// --- Set color of time display --------------
			if (counter<=counterStop) {
				this.getJLabelTimeDisplay().setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			} else {
				this.getJLabelTimeDisplay().setForeground(new Color(255, 51, 0));	
			}
			
			switch (this.view) {
			case ViewCOUNTDOWN:
				// --- countdown view ---------------------
				counter = counterStop-counter;
				if (counter<=0) {
					this.view = ViewTIMER;
					this.setTimeModel(timeModel);
					return;
				}
				this.getJLabelTimeDisplay().setText((counter.toString()));
				this.getJButtonTimeConfig().setText(this.getJMenuItemViewCountdown().getText());
				break;
				
			default:
				// --- timer view -------------------------
				this.getJLabelTimeDisplay().setText(counter.toString());
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
		return this.timeModelStroke;
	}

}
