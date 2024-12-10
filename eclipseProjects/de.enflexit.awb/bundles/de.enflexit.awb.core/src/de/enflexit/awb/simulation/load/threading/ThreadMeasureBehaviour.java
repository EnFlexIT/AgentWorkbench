package de.enflexit.awb.simulation.load.threading;

import javax.swing.SwingUtilities;

import de.enflexit.awb.simulation.LoadService;
import de.enflexit.awb.simulation.agents.LoadMeasureAgent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

/**
 * The Class ThreadMe-asureBehaviour executes the thread measurement on the 
 * distributed platform by using the {@link LoadService}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * @author Hanno Monschan - DAWIS - ICB - University of Duisburg-Essen
 */
public class ThreadMeasureBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 7031695042584275556L;
	
	/** The load measure agent. */
	private LoadMeasureAgent loadMeasureAgent;
	
	/** The one shot behavior. */
	private boolean oneShotBehaviour;
	
	
	/**
	 * Instantiates a new thread measure behavior.
	 *
	 * @param loadMeasureAgent the load measure agent
	 * @param period the period
	 */
	public ThreadMeasureBehaviour(LoadMeasureAgent loadMeasureAgent, long period) {
		super(loadMeasureAgent, period);
		this.loadMeasureAgent = loadMeasureAgent;
	}
	
	/**
	 * Returns the instance of {@link LoadMeasureAgent}.
	 * @return the load measure agent
	 */
	private LoadMeasureAgent getLoadMeasureAgent() {
		return loadMeasureAgent;
	}
	
	/**
	 * Checks if this behavior is just used once.
	 * @return true, if is single shot behavior
	 */
	public boolean isOneShotBehaviour() {
		return oneShotBehaviour;
	}
	
	/**
	 * Sets if the measurement is just used once.
	 * @param oneShotBehaviour the new one shot behavior
	 */
	public void setOneShotBehaviour(boolean oneShotBehaviour) {
		this.oneShotBehaviour = oneShotBehaviour;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.TickerBehaviour#onTick()
	 */
	@Override
	protected void onTick() {
		// --- Do the measurement -------------------------
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() {
				doThreadMeasurement();
			}});
		
		// --- Exit, if this is a one shot behavior ------
		if (this.isOneShotBehaviour()==true) {
			this.stop();
		}
	}
	/**
	 * Executes the thread measurement.
	 */
	private void doThreadMeasurement() {
		try {
			this.getLoadMeasureAgent().getLoadServiceHelper().requestThreadMeasurements(this.getLoadMeasureAgent());
			
		} catch (ServiceException se) {
			se.printStackTrace();
		}
	}
}
