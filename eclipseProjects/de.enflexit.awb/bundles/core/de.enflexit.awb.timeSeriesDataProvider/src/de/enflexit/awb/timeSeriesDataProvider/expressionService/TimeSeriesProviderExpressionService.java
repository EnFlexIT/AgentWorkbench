package de.enflexit.awb.timeSeriesDataProvider.expressionService;

import java.util.ArrayList;
import java.util.TreeMap;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionEditorTreeNode;
import de.enflexit.expression.ExpressionService;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.ExpressionType;

/**
 * {@link ExpressionService} implementation for the {@link TimeSeriesDataProvider}.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesProviderExpressionService implements ExpressionService {
	
	private static final String EXPRESSION_LIBRARY_ROOT_NODE = "Time Series Provider";
	
	private static final String EXPRESSION_SUFFIX_ALL_ENTRIES = "_ALL";
	private static final String EXPRESSION_SUFFIX_ENTRY_RANGE = "_RANGE(<timeFrom>,<timeTo>)";
	private static final String EXPRESSION_SUFFIX_SINGLE_ENTRY = "_SINGLE(<time>)";

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionType()
	 */
	@Override
	public ExpressionType getExpressionType() {
		return ExpressionTypeTimeSeriesProvider.getInstance();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionServiceEvaluator()
	 */
	@Override
	public ExpressionServiceEvaluator getExpressionServiceEvaluator() {
		return new TimeSeriesProviderExpressionEvaluator();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionEditorNode(de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionEditorTreeNode getExpressionEditorNode(ExpressionContext context) {
		ExpressionEditorTreeNode rootNode = new ExpressionEditorTreeNode(EXPRESSION_LIBRARY_ROOT_NODE);
		
		// --- Add one child category for every data source --------------------
		for (String dataSourceName : TimeSeriesDataProvider.getInstance().getAvailableDataSourceNames()) {
			AbstractDataSource dataSource = TimeSeriesDataProvider.getInstance().getDataSource(dataSourceName);
			if (dataSource!=null && dataSource.isAvailable()==true) {
				ExpressionEditorTreeNode dataSourceNode = new ExpressionEditorTreeNode(dataSource.getName());
				dataSourceNode.setExpressionTemplates(this.buildExpressionTemplatesTreeMap(dataSource));
				rootNode.add(dataSourceNode);
				
			} else {
				System.err.println("[" + this.getClass().getSimpleName() + "] Data source " + dataSourceName + " not available, skipping editor entries.");
			}
		}
		return rootNode;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getInsertString(java.lang.String)
	 */
	@Override
	public String getInsertString(String libraryExpression) {
		return null;	// No custom insert strings needed
	}
	
	/**
	 * Builds the expression templates tree map for the provided data source.
	 * @param dataSource the data source
	 * @return the tree map
	 */
	private TreeMap<String, ArrayList<String>> buildExpressionTemplatesTreeMap(AbstractDataSource dataSource){
		TreeMap<String, ArrayList<String>> dataSourceExpressionsMap = new TreeMap<String, ArrayList<String>>();
		ArrayList<String> allDataSeriesExpressions = new ArrayList<String>();
		dataSourceExpressionsMap.put("All", allDataSeriesExpressions);
		
		for (String dataSeriesName : dataSource.getSourceConfiguration().getDataSeriesNames()) {
			
			// --- Add expressions to access all values, a range of values or a single value
			ArrayList<String> dataSeriesExpressions = new ArrayList<String>();
			dataSeriesExpressions.add(dataSource.getName() + "." + dataSeriesName + EXPRESSION_SUFFIX_ALL_ENTRIES);
			dataSeriesExpressions.add(dataSource.getName() + "." + dataSeriesName + EXPRESSION_SUFFIX_ENTRY_RANGE);
			dataSeriesExpressions.add(dataSource.getName() + "." + dataSeriesName + EXPRESSION_SUFFIX_SINGLE_ENTRY);
			
			// --- Add a filter category for the data series ------------------
			dataSourceExpressionsMap.put(dataSeriesName, dataSeriesExpressions);
			// --- Also add the  expressions to the unfiltered list -----------
			allDataSeriesExpressions.addAll(dataSeriesExpressions);
		}
		return dataSourceExpressionsMap;
	}

}
