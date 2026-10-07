package de.enflexit.df.core.processing.transformation.ui;

import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.JPanel;

import de.enflexit.df.core.processing.transformation.DataTransformationService;

/**
 * Abstract superclass for transformation-specific configuration components.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractTransformationParameterConfigurationPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -2488414551927207932L;
	
	private DataTransformationService transformation;
	
	/**
	 * Instantiates a new abstract transformation parameter configuration component.
	 * @param transformation the transformation
	 */
	public AbstractTransformationParameterConfigurationPanel(DataTransformationService transformation) {
		this.transformation = transformation;
	}

	/**
	 * Returns the parameters as configured in the component UI.  
	 * @return the configured parameters
	 */
	public abstract Map<String, Object> getConfiguredParameters();
	
	/**
	 * Sets the configured parameters to be displayed by the panel.
	 * @param parameters the parameters
	 */
	public abstract void setConfiguredParameters(Map<String, Object> parameters);

	/**
	 * Gets the transformation.
	 * @return the transformation
	 */
	public DataTransformationService getTransformation() {
		return transformation;
	}

	/**
	 * Sets the transformation.
	 * @param transformation the new transformation
	 */
	public void setTransformation(DataTransformationService transformation) {
		this.transformation = transformation;
	}
	
	

}
