package de.enflexit.df.core.processing.transformation;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation.InputType;

/**
 * {@link DataTransformationService} implementation for table row filters.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationServiceFilterRows implements DataTransformationService {
	
	public static final String TRANSFORMATION_NAME = "Filter Rows";
	
	private static final String TRANSFORMATION_DESCRIPTION = "This data transformation filters the rows of a table based on the values of one or more columns.";

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
		// TODO Auto-generated method stub
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
		return new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformationService#getNewInstance()
	 */
	@Override
	public AbstractDataTransformation getNewInstance() {
		return new DataTransformationFilterRows();
	}

}
