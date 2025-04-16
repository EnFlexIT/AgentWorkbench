package de.enflexit.awb.core.project.plugins;

import de.enflexit.awb.core.project.Project;

/**
 * The Class PlugInError serves as an error instance in case that a PlugIn class can not be found or loaded.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PlugInError extends PlugIn {

	private Throwable throwable;
	
	/**
	 * Instantiates a new plug in error.
	 * @param currProject the current project instance
	 */
	public PlugInError(Project currProject) {
		super(currProject);
	}
	/* (non-Javadoc)
	 * @see agentgui.core.plugin.PlugIn#getName()
	 */
	@Override
	public String getName() {
		return "ERROR-PlugIn";
	}

	/**
	 * Sets the throwable.
	 * @param ex the new throwable
	 */
	public void setThrowable(Throwable ex) {
		throwable = ex;
	}
	/**
	 * Returns the throwable that was thrown during load operation.
	 * @return the throwable
	 */
	public Throwable getThrowable() {
		return throwable;
	}
	
}
