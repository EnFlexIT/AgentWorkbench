package de.enflexit.df.core.processing.transformation;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;

/**
 * {@link DataTransformationService} implementation for select transformations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationServiceSelectImpl implements DataTransformationService {
	
	public static final String TRANSFORMATION_NAME = "Select Columns";
	
	private static final String TRANSFORMATION_DESCRIPTION = "This data transformation selects a subset of columns from the input table.";

	@Override
	public String getName() {
		return TRANSFORMATION_NAME;
	}

	@Override
	public String getDescription() {
		return TRANSFORMATION_DESCRIPTION;
	}

	@Override
	public InputType getInputType() {
		return InputType.SINGLE_TABLE;
	}

	@Override
	public List<String> getRequiredParameters() {
		ArrayList<String> requiredParameters = new ArrayList<String>();
		requiredParameters.add("Included columns (String list)");
		return requiredParameters;
	}

	@Override
	public AbstractDataTransformation getNewInstance() {
		return new DataTransformationSelect();
	}

}
