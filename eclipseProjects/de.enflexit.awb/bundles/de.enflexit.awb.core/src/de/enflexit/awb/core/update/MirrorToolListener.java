package de.enflexit.awb.core.update;

import de.enflexit.awb.core.update.MirrorTool.MirrorToolsJob;

/**
 * The listener interface for receiving information that the mirroring process is finalized.
 *
 * @see MirrorTool
 */
public interface MirrorToolListener {

	/**
	 * On mirroring finalized will be invoked if a mirroring is done.
	 *
	 * @param job the job that was executed
	 * @param successful true, if the mirroring was successful
	 */
	public void onMirroringFinaliized(MirrorToolsJob job, boolean successful); 
	
	
}
