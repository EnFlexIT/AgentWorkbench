package de.enflexit.awb.remoteControl;

/**
 * This enumeration describes possible states of the controlled AWB, that are relevant to decide if a command can currently be executed.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public enum AwbState {
	AWB_READY, PROJECT_LOADED, SETUP_READY, MAS_STARTED, SIMULATION_STEP_DONE, SIMULATION_FINISHED, MAS_STOPPED, AWB_TERMINATED
}
