package de.enflexit.awb.simulation.environment;

import java.io.Serializable;

import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.common.properties.Properties;

/**
 * This is the generalized environment model to use with the SimulationService
 * and in order to extend individual environment models.
 * 
 * @see SimulationService
 * 
 * @see SimulationServiceHelper#setEnvironmentModel(EnvironmentModel)
 * @see SimulationServiceHelper#stepSimulation(EnvironmentModel, int)
 * @see SimulationServiceHelper#stepSimulation(EnvironmentModel, int, boolean)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentModel implements Serializable {

	private static final long serialVersionUID = -2845036237763599630L;
	
	private TimeModel timeModel;
	private AbstractEnvironmentModel abstractEnvironment;
	private DisplaytEnvironmentModel displayEnvironment;	

	private Properties projectProperties;
	private Properties setupProperties;
	
	
	/**
	 * Returns true if nothing is set yet (e.g. timeModel, abstractEnvironment or displayEnvironment)
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		if (timeModel==null && abstractEnvironment==null && displayEnvironment==null) {
			return true;
		} else {
			return false;	
		}	
	}
	
	/**
	 * Gets the time model.
	 * @return the timeModel
	 */
	public TimeModel getTimeModel() {
		return timeModel;
	}
	/**
	 * Sets the time model.
	 * @param timeModel the timeModel to set
	 */
	public void setTimeModel(TimeModel timeModel) {
		this.timeModel = timeModel;
	}
	
	/**
	 * Gets the abstract environment.
	 * @return the abstract environment
	 */
	public AbstractEnvironmentModel getAbstractEnvironment() {
		return abstractEnvironment;
	}
	/**
	 * Sets the abstract environment.
	 * @param newAbstractEnvironment the new abstract environment
	 */
	public void setAbstractEnvironment(AbstractEnvironmentModel newAbstractEnvironment) {
		this.abstractEnvironment = newAbstractEnvironment;
	}
	
	/**
	 * Gets the display environment.
	 * @return the display environment
	 */
	public DisplaytEnvironmentModel getDisplayEnvironment() {
		return displayEnvironment;
	}
	/**
	 * Sets the display environment.
	 * @param displayEnvironment the new display environment
	 */
	public void setDisplayEnvironment(DisplaytEnvironmentModel displayEnvironment) {
		this.displayEnvironment = displayEnvironment;
	}

	/**
	 * Returns the available project {@link Properties}.
	 * @return the project properties
	 * @see Project#getProperties()
	 */
	public Properties getProjectProperties() {
		return projectProperties;
	}
	/**
	 * Sets project {@link Properties} to the environment model.
	 * @param projectProperties the new project properties
	 */
	public void setProjectProperties(Properties projectProperties) {
		this.projectProperties = projectProperties;
	}
	
	/**
	 * Returns the available setup Properties.
	 * @return the setup properties
	 * @see SimulationSetup#getProperties()
	 */
	public Properties getSetupProperties() {
		return setupProperties;
	}
	/**
	 * Sets setup {@link Properties} to the environment model.
	 * @param projectProperties the new project properties
	 */
	public void setSetupProperties(Properties setupProperties) {
		this.setupProperties = setupProperties;
	}
	
	
	/**
	 * Returns a copy of the current environment model.
	 * @return the copied instance of the current object
	 */
	public EnvironmentModel getCopy() {
		
		if (this.isEmpty()==true) {
			return null;
		}
				
		EnvironmentModel copy = new EnvironmentModel();
		if (this.timeModel!=null) {
			copy.setTimeModel(this.timeModel.getCopy());
		}
		if (this.displayEnvironment!=null) {
			copy.setDisplayEnvironment(this.displayEnvironment.getCopy());
		}
		if (this.abstractEnvironment!=null) {
			copy.setAbstractEnvironment(this.abstractEnvironment.getCopy());
		}
		if (this.projectProperties!=null) {
			copy.setProjectProperties(this.projectProperties.getCopy());
		}
		if (this.setupProperties!=null) {
			copy.setSetupProperties(this.setupProperties.getCopy());
		}
		return copy;
	}

}
