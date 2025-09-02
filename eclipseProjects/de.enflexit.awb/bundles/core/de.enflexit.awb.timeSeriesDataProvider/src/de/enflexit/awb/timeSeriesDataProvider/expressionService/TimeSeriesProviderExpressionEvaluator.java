package de.enflexit.awb.timeSeriesDataProvider.expressionService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.awb.timeSeriesDataProvider.TimeValuePair;
import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionData;
import de.enflexit.expression.ExpressionData.DataType;
import de.enflexit.expression.ExpressionResult;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.TimeSeriesDescription;
import de.enflexit.expression.TimeSeriesException;
import de.enflexit.expression.UnknownExpressionException;

/**
 * {@link ExpressionServiceEvaluator} implementation for the {@link TimeSeriesProviderExpressionService}.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesProviderExpressionEvaluator implements ExpressionServiceEvaluator {
	
	private static final String MATCHING_AND_SPLITTING_REGEX = ExpressionTypeTimeSeriesProvider.TYPE_PREFIX + "!([A-Za-z_\\s]+)\\.([A-Za-z_\\s]+)_([A-Z]+)";
	private Pattern matchAndSplitPattern;

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#evaluate(de.enflexit.expression.Expression, de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionResult evaluate(Expression expression, ExpressionContext context) throws UnknownExpressionException {
		ExpressionResult result = null;

		// --- Use a RegEx to verify and split the expression -------
		Matcher matcher = this.getMatchAndSplitPattern().matcher(expression.getExpressionString());
		if (matcher.find()==true) {
			String dataSourceName = matcher.group(1);
			String dataSeriesName = matcher.group(2);
			String requestType = matcher.group(3);
			
			// --- Get the requested data source --------------------
			AbstractDataSource dataSource = TimeSeriesDataProvider.getInstance().getDataSource(dataSourceName);
			if (dataSource==null) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Unable to evaluate expression " + expression.getExpressionString() + ", data source " + dataSourceName + " not found!");
				return null;
			}
			
			// --- Get the requested data series --------------------
			AbstractDataSeries dataSeries = dataSource.getDataSeries(dataSeriesName);
			if (dataSeries==null) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Unable to evaluate expression " + expression.getExpressionString() + ", data series " + dataSeriesName + " not found!");
				return null;
			}
			
			// --- Get parameter values from sub-expressions --------
			ArrayList<ExpressionData> parameters = this.evaluateParameterExpressions(expression, context);
			
			// --- Obtain the requested entries ---------------------
			if (requestType.equals("ALL")) {
				result = this.getAllValues(dataSeries);
			} else if(requestType.equals("RANGE")) {
				result = this.getRangeValues(dataSeries, parameters.get(0), parameters.get(1));
			} else if(requestType.equals("SINGLE")) {
				result = this.getSingleValue(dataSeries, parameters.get(0));
			}
			
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] " + expression.getExpressionString() + " is no valid time series provider expression, unable to evaluate!");
		}
		
		return result;
	}

	/**
	 * Gets a RegEx pattern that verifies the form of the expression and splits it into parts for further evaluation. 
	 * @return the match and split pattern
	 */
	private Pattern getMatchAndSplitPattern() {
		if (matchAndSplitPattern==null) {
			matchAndSplitPattern = Pattern.compile(MATCHING_AND_SPLITTING_REGEX);
		}
		return matchAndSplitPattern;
	}
	
	/**
	 * Evaluates all sub expressions providing parameter values.
	 * @param expression the parent expression
	 * @param context the expression context
	 * @return the sub-expression results
	 */
	private ArrayList<ExpressionData> evaluateParameterExpressions(Expression expression, ExpressionContext context){
		ArrayList<ExpressionData> subExpressionData = new ArrayList<>();
		for (Expression subExpression : expression.getSubExpressions()) {
			ExpressionResult subExpResult = subExpression.getExpressionResult(context);
			if (subExpResult!=null) {
				subExpressionData.add(subExpResult.getExpressionData());
			}
		}
		return subExpressionData;
	}
	
	/**
	 * Gets all values from the provided data series from the {@link TimeSeriesDataProvider}.
	 * @param dataSeries the data series
	 * @return an {@link ExpressionResult} of time series type containing the values.
	 */
	private ExpressionResult getAllValues(AbstractDataSeries dataSeries) {
		
		// --- Request the whole time range covered by the data series --------
		long timeFrom = dataSeries.getFirstTimeStamp();
		long timeTo = dataSeries.getLastTimeStamp();
		
		return this.getValuesForTimeRange(dataSeries, timeFrom, timeTo);
	}
	
	/**
	 * Gets the values for a specific time range from the {@link TimeSeriesDataProvider}.
	 * @param dataSeries the data series
	 * @param parameterFrom the parameter from
	 * @param parameterTo the parameter to
	 * @return the range values
	 */
	private ExpressionResult getRangeValues(AbstractDataSeries dataSeries, ExpressionData parameterFrom, ExpressionData parameterTo) {
		
		// --- Convert the parameters to Long objects --------------- 
		Long timeFrom = this.getTimestampValue(parameterFrom);
		Long timeTo = this.getTimestampValue(parameterTo);
		
		// --- Request the data from the time series provider --------
		if (timeFrom!=null && timeTo!=null) {
			return this.getValuesForTimeRange(dataSeries, timeFrom, timeTo);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Required time parameter is missing!");
			return null;
		}
		
	}
	
	/**
	 * Gets the values for time range.
	 * @param dataSeries the data series
	 * @param timeFrom the time from
	 * @param timeTo the time to
	 * @return the values for time range
	 */
	private ExpressionResult getValuesForTimeRange(AbstractDataSeries dataSeries, long timeFrom, long timeTo) {
		
		ExpressionResult result = null;
		
		List<TimeValuePair> values = dataSeries.getValuesForTimeRange(timeFrom, timeTo);
		if (values!=null && values.size()>0) {
			try {
				result = this.convertToTimeSeriesResult(dataSeries.getName(), values);
			} catch (TimeSeriesException e) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Error converting the time series provider response to an expression result!");
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	/**
	 * Gets a single value from the {@link TimeSeriesDataProvider}.
	 * @param dataSeries the data series
	 * @param timeParameter the time
	 * @return the value
	 */
	private ExpressionResult getSingleValue(AbstractDataSeries dataSeries, ExpressionData timeParameter) {
		ExpressionResult expResult = null;
		
		Long timestamp = this.getTimestampValue(timeParameter); 
		
		// --- Check if the required parameter was provided 
		if (timeParameter!=null) {
			TimeValuePair tvp = dataSeries.getValueForTime(timestamp);
			if (tvp!=null){
				expResult = new ExpressionResult(tvp.getValue());
			}
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Time parameter is missing!");
		}
		return expResult;
	}

	/**
	 * Converts the result as provided from the {@link TimeSeriesDataProvider} to an {@link ExpressionResult} of time series type.
	 * @param seriesName the series name
	 * @param timeSeriesProviderResult the time series provider result
	 * @return the expression result
	 * @throws TimeSeriesException if the conversion failed
	 */
	private ExpressionResult convertToTimeSeriesResult(String seriesName, List<TimeValuePair> timeSeriesProviderResult) throws TimeSeriesException {
		
		// --- Prepare the time series expression result ------------
		TimeSeriesDescription tsd = new TimeSeriesDescription("time", Application.getGlobalInfo().getZoneId());
		tsd.addDataColumn(seriesName, DataType.Double);
		tsd.setInitialListLength(timeSeriesProviderResult.size());
		ExpressionResult timeSeriesResult = new ExpressionResult(tsd);
		
		// --- Add the entries --------------------------------------
		for (TimeValuePair tvp : timeSeriesProviderResult) {
			timeSeriesResult.addDataRow(tvp.getTimestamp(), tvp.getValue());
		}
		
		return timeSeriesResult;
	}
	
	private Long getTimestampValue(ExpressionData expData) {
		Long timestamp = null;
		if (expData!=null) {
			if (expData.getDataType()==DataType.Double) {
				timestamp = expData.getDoubleValue().longValue();
			} else if (expData.getDataType()==DataType.Long) {
				timestamp = expData.getLongValue();
			} else {
				System.err.println("[" + this.getClass().getSimpleName() + "] Time parameter is of invalid type, must be double or long!");
			}
		}
		return timestamp;
	}
}
