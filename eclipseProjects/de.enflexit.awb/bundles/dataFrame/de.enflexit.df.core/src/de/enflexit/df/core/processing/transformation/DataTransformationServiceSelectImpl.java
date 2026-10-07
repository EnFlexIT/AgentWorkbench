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

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getName()
	 */
	@Override
	public String getName() {
		return TRANSFORMATION_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getDescription()
	 */
	@Override
	public String getDescription() {
		return TRANSFORMATION_DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getInputType()
	 */
	@Override
	public InputType getInputType() {
		return InputType.SINGLE_TABLE;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getRequiredParameters()
	 */
	@Override
	public List<String> getRequiredParameters() {
		ArrayList<String> requiredParameters = new ArrayList<String>();
		requiredParameters.add(DataTransformationSelect.TRANSFORMATION_PARAMETER_COLUMNS_TO_SELECT);
		return requiredParameters;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getNewInstance()
	 */
	@Override
	public AbstractDataTransformation getNewInstance() {
		return new DataTransformationSelect();
	}

}
