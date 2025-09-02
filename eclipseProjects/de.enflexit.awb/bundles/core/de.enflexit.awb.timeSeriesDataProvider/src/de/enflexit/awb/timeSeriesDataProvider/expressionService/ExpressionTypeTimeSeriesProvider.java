package de.enflexit.awb.timeSeriesDataProvider.expressionService;

import de.enflexit.expression.ExpressionType;

/**
 * The Class ExpressionTypeTimeSeriesProvider.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ExpressionTypeTimeSeriesProvider extends ExpressionType {
	
	public static final String TYPE_PREFIX = "TSP";
	
	private static ExpressionTypeTimeSeriesProvider instance;

	/**
	 * Instantiates a new expression type time series provider.
	 */
	private ExpressionTypeTimeSeriesProvider() {};
	
	/**
	 * Gets the single instance of ExpressionTypeTimeSeriesProvider.
	 * @return single instance of ExpressionTypeTimeSeriesProvider
	 */
	public static ExpressionTypeTimeSeriesProvider getInstance() {
		if (instance==null) {
			instance = new ExpressionTypeTimeSeriesProvider();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionType#getTypePrefix()
	 */
	@Override
	public String getTypePrefix() {
		return TYPE_PREFIX;
	}

}
