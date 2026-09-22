package de.enflexit.df.charts;

/**
 * The Class AbstractChart.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AbstractChart implements Chart {

	private Integer id;
	private Integer idDataSource;
	
	private String name;
	private String description;
	
	private String title;
	private String xAxisLabel;
	private String yAxisLabel;
	private Boolean showLegend;

	private Object data;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#getId()
	 */
	@Override
	public int getId() {
		return this.id==null ? -1 : this.id;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#setId(int)
	 */
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#getIdDataSource()
	 */
	@Override
	public int getIdDataSource() {
		return this.idDataSource==null ? -1 : this.idDataSource;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#setDataSource(int)
	 */
	@Override
	public void setDataSource(int idDataSource) {
		this.idDataSource = idDataSource;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#getxAxisLabel()
	 */
	@Override
	public String getXAxisLabel() {
		return xAxisLabel;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#setXAxisLabel(java.lang.String)
	 */
	@Override
	public void setXAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#getYAxisLabel()
	 */
	@Override
	public String getYAxisLabel() {
		return yAxisLabel;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#setYAxisLabel(java.lang.String)
	 */
	@Override
	public void setYAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#isShowLegend()
	 */
	@Override
	public boolean isShowLegend() {
		return showLegend;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.df.charts.Chart#setShowLegend(java.lang.Boolean)
	 */
	@Override
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}
	
}
