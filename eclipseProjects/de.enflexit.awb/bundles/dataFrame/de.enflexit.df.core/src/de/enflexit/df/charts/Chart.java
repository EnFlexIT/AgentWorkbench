package de.enflexit.df.charts;

public interface Chart {

	/**
	 * Has to return the id.
	 * @return the id
	 */
	public int getId();
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(int id);
	
	
	/**
	 * Has to return the id of the data source.
	 * @return the id of the data source 
	 */
	public int getIdDataSource();
	/**
	 * Sets the id of the data source.
	 * @param id the id of the data source
	 */
	public void setDataSource(int idDataSource);
	
	
	/**
	 * Has to return the name of the data source.
	 * @return the name
	 */
	public String getName();
	/**
	 * Sets the name of the data source.
	 * @param name the new name
	 */
	public void setName(String name);
	
	
	/**
	 * Has to return the description for the data source.
	 * @return the description
	 */
	public String getDescription();
	/**
	 * Sets the description of the data source.
	 * @param description the new description
	 */
	public void setDescription(String description);
	
	
	public String getTitle();
	
	public void setTitle(String title);

	

	public String getXAxisLabel();

	public void setXAxisLabel(String xAxisLabel);

	
	public String getYAxisLabel();

	public void setYAxisLabel(String yAxisLabel);

	
	public boolean isShowLegend();

	public void setShowLegend(boolean showLegend);
	
	
}
