package de.enflexit.df.core.processing.transformation;

import java.util.List;

import de.enflexit.common.properties.PropertiesPanel;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;
import de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel;

/**
 * The interface every data transformation has to implement. The {@link AbstractDataTransformation} 
 * superclass provides some default implementations for table and parameter handling. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface DataTransformationService {
	
	public static final String CATEGORY_BASIC_FUNCTIONS = "Basic Functions"; 
	public static final String CATEGORY_STATISTIC_FUNCTIONS = "Statistic Functions";
	public static final String CATEGORY_SERVICE_FUNCTIONS = "Service Functions";
	
	/**
	 * Gets the name of this data transformation.
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets a description for this data transformation.
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * Gets the category.
	 * @return the category
	 */
	public String getCategory();
	
	/**
	 *  Gets the input type (single or multi table) for this data transformation.
	 * @return the input type
	 */
	public InputType getInputType();
	
	/**
	 * Gets a list of required parameters for this transformation.
	 * @return the required parameters
	 */
	public List<String> getRequiredParameters();
	
	/**
	 * Returns a new instance of the {@link AbstractDataTransformation} subclass implementing this transformation.
	 * @return the new instance
	 */
	public AbstractDataTransformation getNewInstance();
	
	/**
	 * Returns an instance of a transformaiton-specific panel for parameter configuration. May return null, then a default implementation with a {@link PropertiesPanel} will be used.
	 * @return the parameter configuration panel
	 */
	public AbstractTransformationParameterConfigurationPanel getParameterConfigurationPanel();
	
}
