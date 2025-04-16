package agentgui.core.plugin;

import de.enflexit.common.Observer;
import jade.core.ProfileImpl;

public interface AwbPlugIn extends Observer {

	public static final String CHANGED = "PlugIns";
	public static final int ADDED = 1;
	public static final int REMOVED = 2;
	
	/**
	 * Sets the class reference of this PugIn.
	 * @param classReference the classReference to set
	 */
	public void setClassReference(String classReference);

	/**
	 * Returns the class reference of the current {@link PlugIn}.
	 * @return the classReference as {@link String}
	 */
	public String getClassReference();

	/**
	 * Returns the name of the current {@link PlugIn}.
	 * @return the pluginName
	 */
	public String getName();

	/**
	 * This method be called if the plug-in is loaded into the project.
	 * This happens immediately after the project was opened. 
	 */
	public void onPlugIn();

	/**
	 * This method will be called if the plug-in will be removed from the 
	 * project, means immediately before the project will be closed.
	 */
	public void onPlugOut();

	/**
	 * This method will be invoked just after the onPlugOut() method 
	 * was executed.
	 * DO NOT OVERRIDE !!!
	 */
	public void afterPlugOut();

	

	/**
	 * Overriding his method allows to extend/change the currently 
	 * used Profile JADE container.
	 * 
	 * @param jadeContainerProfile The profile to CHANGE
	 * @return the SetupChangeEvent (!) configuration of the JADE Profile
	 */
	ProfileImpl getJadeProfile(ProfileImpl jadeContainerProfile);

	/**
	 * Checks for valid precondition before the MAS execution.
	 * @return true, if the preconditions are valid
	 */
	boolean hasValidPreconditionForMasExecution();

	/**
	 * Will be executed, if the Multi-Agent System is about to start.
	 */
	void onMasWillBeExecuted();

	/**
	 * Will be executed, if the Multi-Agent System was terminated.
	 */
	void onMasWasTerminated();

}