package de.enflexit.df.core.processing.transformation.metrics;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.graph.TransformationGraphController;
import tech.tablesaw.api.Table;

public class DataTransformationCalculateMetrics extends AbstractDataTransformation {
	
	public static final String PARAM_NAME_SELECTED_METRICS = "selectedMetrics";
	public static final String PARAM_NAME_REFERENCE_COLUMN = "referenceColumn";
	public static final String PARAM_NAME_COMPARE_COLUMNS = "compareColumns";
	
	/**
	 * The supported metrics
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	public enum Metrics {
		R_SQUARED("R²"),
		RMSE("Root mean squared error"), 
		MAE("Mean absolute error"), 
		MSE("Mean squared error");
		
		final String label;

	    /**
    	 * Instantiates a new metrics.
    	 * @param label the label
    	 */
    	private Metrics(String label) {
	        this.label = label;
	    }
    	
    	public String label() {
			return label;
		}
	    
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#verifyParameters()
	 */
	@Override
	public boolean verifyParameters() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation(de.enflexit.df.core.processing.transformation.graph.TransformationGraphController)
	 */
	@Override
	public Table performTransformation(TransformationGraphController graphController) {
		// TODO Auto-generated method stub
		return null;
	}

}
