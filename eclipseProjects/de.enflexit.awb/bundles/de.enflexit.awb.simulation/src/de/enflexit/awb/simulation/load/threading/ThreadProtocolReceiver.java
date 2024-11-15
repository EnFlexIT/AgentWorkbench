package de.enflexit.awb.simulation.load.threading;

import de.enflexit.awb.simulation.load.LoadMeasureThread;

/**
 * The Interface ThreadProtocolReceiver can be used in order to register
 * as a receiver for the thread measurements of the {@link LoadMeasureThread}.
 * 
 * @see LoadMeasureThread
 * @see LoadMeasureThread#doThreadMeasurement(long, ThreadProtocolReceiver)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ThreadProtocolReceiver {

	/**
	 * Receives a thread measurement by a {@link ThreadProtocol}.
	 * @param threadProtocol the protocol the was determined
	 */
	public void receiveThreadProtocol(ThreadProtocol threadProtocol);
}
