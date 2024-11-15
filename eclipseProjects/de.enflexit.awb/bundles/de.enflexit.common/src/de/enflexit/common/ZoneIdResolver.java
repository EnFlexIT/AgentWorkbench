package de.enflexit.common;

import java.time.ZoneId;

/**
 * The Interface ZoneIdResolver can be used to implement different approaches  to resolve the ZoneId currently to use.
 * For this, implement your approach and register your implementation in the static class {@link GlobalRuntimeValues}.
 * 
 * @see GlobalRuntimeValues
 * @see GlobalRuntimeValues#setZoneIdResolver(ZoneIdResolver)
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface ZoneIdResolver {

	/**
	 * Has to return the current ZoneId, which is usually {@link ZoneId#systemDefault()}, but 
	 * in the context of agent projects & simulations it might be something configured.
	 *
	 * @return the zone id
	 */
	public ZoneId getZoneId();
	
}
