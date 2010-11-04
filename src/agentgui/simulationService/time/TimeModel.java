package agentgui.simulationService.time;

import jade.util.leap.Serializable;



public class TimeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4597561080133786915L;
	
	public static final int STROKE = 1;
	public static final int DISCRETE_TIME = 2;
	
	private Integer typeOfTimeModel = 0; 
	
	// --- Constructor for Stroke-TimeModel ---------------
	public TimeModel(){
		
	}
		
	/**
	 * This Method steps the concrete discrete TimeModel
	 * @param timeModel
	 */
	public void step(TimeModel timeModel) {
		if ( timeModel instanceof TimeModelStroke ) {
			TimeModelStroke tms =(TimeModelStroke) timeModel;
			tms.step();
		} else if ( timeModel instanceof TimeModelDiscrete ) {
			TimeModelDiscrete tmd =(TimeModelDiscrete) timeModel;
			tmd.step();
		}
	}
	
	/**
	 * @param typeOfTimeModel the typeOfTimeModel to set
	 */
	public void setTypeOfTimeModel(Integer typeOfTimeModel) {
		this.typeOfTimeModel = typeOfTimeModel;
	}
	/**
	 * @return the typeOfTimeModel
	 */
	public Integer getTypeOfTimeModel() {
		return typeOfTimeModel;
	}
		
}
