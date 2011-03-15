package agentgui.core.sim.setup;

/**
 * Class which is used for notifications about changes and actions
 * inside the SimulationSetup, like 'addNew', 'saved' and so on.
 * Which action was used can be set by the constructor while using 
 * the static globals of 'SimulationSetups' 
 *     
 * @author Christian Derksen
 */
public class SimulationSetupsChangeNotification {
	
	private int updateReason;
	public SimulationSetupsChangeNotification(int reason) {
		updateReason = reason;
	}
	public void setUpdateReason(int updateReason) {
		this.updateReason = updateReason;
	}
	public int getUpdateReason() {
		return updateReason;
	}
	public String toString() {
		return SimulationSetups.CHANGED;
	}
	
}

