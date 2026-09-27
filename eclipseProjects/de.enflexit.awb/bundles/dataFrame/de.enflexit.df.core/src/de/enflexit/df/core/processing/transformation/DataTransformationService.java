package de.enflexit.df.core.processing.transformation;

import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;

/**
 * The interface every data transformation has to implement. The {@link AbstractDataTransformation} 
 * superclass provides some default implementations for table and parameter handling. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface DataTransformationService {
	
	/**
	 * Gets the name of this data transformation.
	 * @return the name
	 */
	public abstract String getName();
	
	/**
	 * Gets a description for this data transformation.
	 * @return the description
	 */
	public abstract String getDescription();
	
	/**
	 *  Gets the input type (single or multi table) for this data transformation.
	 * @return the input type
	 */
	public abstract InputType getInputType();
	
	/**
	 * Gets a list of required parameters for this transformation.
	 * @return the required parameters
	 */
	public abstract List<String> getRequiredParameters();
	
	/**
	 * Returns a new instance of the {@link AbstractDataTransformation} subclass implementing this transformation.
	 * @return the new instance
	 */
	public abstract AbstractDataTransformation getNewInstance();
	
}
