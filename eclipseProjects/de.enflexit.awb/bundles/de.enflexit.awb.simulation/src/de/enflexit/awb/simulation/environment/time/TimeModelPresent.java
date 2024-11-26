package de.enflexit.awb.simulation.environment.time;


/**
 * TimeModel which uses the current system time of the system/platform, especially for monitoring use cases.
 */
public class TimeModelPresent extends TimeModelContinuous {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7524432551373560286L;

    /**
     * Instantiates a new time model present.
     */
    public TimeModelPresent() {
        setAccelerationFactor(1.0);
        setTimeStart(getTime());
        setTimeStop(Long.MAX_VALUE);
    }

    /* (non-Javadoc)
     * @see de.enflexit.awb.simulation.time.TimeModelContinuous#getJPanel4Configuration(agentgui.core.project.Project, agentgui.core.gui.projectwindow.simsetup.TimeModelController)
     */
    @Override
    public JPanel4TimeModelConfiguration getJPanel4Configuration(Project project, TimeModelController timeModelController) {
        return new TimeModelPresentConfiguration(project, timeModelController);
    }

    /* (non-Javadoc)
     * @see de.enflexit.awb.simulation.time.TimeModelContinuous#getTime()
     */
    @Override
    public long getTime() {
        return getSystemTimeSynchronized();
    }

    /* (non-Javadoc)
     * @see de.enflexit.awb.simulation.time.TimeModelDateBased#getTimeStart()
     */
    @Override
    public long getTimeStart() {
        return getTime();
    }

    /* (non-Javadoc)
     * @see de.enflexit.awb.simulation.time.TimeModelContinuous#logTookLocalTime()
     */
    @Override
    protected void logTookLocalTime() {
        // don't output message
    }
    
    /* (non-Javadoc)
	 * @see de.enflexit.awb.simulation.time.TimeModel#getCopy()
	 */
	@Override
	public TimeModel getCopy() {
		TimeModelPresent tmc = new TimeModelPresent();
		// ------------------------------------------------
		// --- Do this first to avoid side effects --------
		tmc.setExecuted(this.isExecuted());
		// --- Do this first to avoid side effects --------
		// ------------------------------------------------		
		tmc.setTimeStart(this.timeStart);
		tmc.setTimeStop(this.timeStop);
		tmc.setTimeFormat(this.timeFormat);
		tmc.setZoneId(this.zoneId);
		return tmc;
	}

}
