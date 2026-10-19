package de.enflexit.df.core.expression;

import de.enflexit.df.core.model.DataController;
import tech.tablesaw.api.Table;

/**
 * The Class DataFrameExpressionParser.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameExpressionParser {

	private DataController dataController;
	private String resourceLink;
	
	private Table table;
	
	/**
	 * Instantiates a new data frame resource parser.
	 * @param dataController the data controller
	 */
	public DataFrameExpressionParser(DataController dataController) { 
		this(dataController, null);
	}
	/**
	 * Instantiates a new data frame resource parser.
	 * @param dataController the data controller
	 * @param resourceLink the resource link
	 */
	public DataFrameExpressionParser(DataController dataController, String resourceLink) {
		this.setResourceLink(resourceLink);
	}
	
	/**
	 * Sets the resource link.
	 * @param resourceLink the new resource link
	 */
	public void setResourceLink(String resourceLink) {
		this.resourceLink = resourceLink;
	}
	/**
	 * Returns the resource link.
	 * @return the resource link
	 */
	public String getResourceLink() {
		return resourceLink;
	}

	/**
	 * Parses the.
	 */
	private void parse() {
		
		if (resourceLink==null || resourceLink.trim().isBlank()==true) return;
		
		
		
		
	}
	
	
	/**
	 * Returns the data.
	 * @return the data
	 */
	public Table getData() {
		if (table==null) {
			this.parse();
		}
		return table;
	}
	
}
