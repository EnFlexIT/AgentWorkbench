package agentgui.core.plugin;


public class PlugInNotification {

	private PlugIn plugIn = null;
	private int updateReason = 0; 
	
	public PlugInNotification(int reason, PlugIn ppi) {
		this.setUpdateReason(reason);
		this.setPlugIn(ppi);
	}
	public void setUpdateReason(int updateReason) {
		this.updateReason = updateReason;
	}
	public int getUpdateReason() {
		return updateReason;
	}
	public void setPlugIn(PlugIn plugIn) {
		this.plugIn = plugIn;
	}
	public PlugIn getPlugIn() {
		return plugIn;
	}
	@Override
	public String toString() {
		return PlugIn.CHANGED;
	}
	
}
