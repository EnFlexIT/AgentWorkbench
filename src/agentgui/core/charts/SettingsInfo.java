/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.charts;

/**
 * Contains information about a change of chart settings
 * @author Nils
 *
 */
public class SettingsInfo {
	/**
	 * Indicates change of the chart title
	 */
	public static final int CHART_TITLE_CHANGED = 0;
	/**
	 * Indicates change of a series label
	 */
	public static final int SERIES_LABEL_CHANGED = 1;
	/**
	 * Indicates change of the x axis label
	 */
	public static final int X_AXIS_LABEL_CHANGED = 2;
	/**
	 * Indicates change of the y axis label
	 */
	public static final int Y_AXIS_LABEL_CHANGED = 3;
	/**
	 * Indicates change of the renderer type
	 */
	public static final int RENDERER_CHANGED = 4;
	/**
	 * Indicates change of a series color
	 */
	public static final int SERIES_COLOR_CHANGED = 5;
	/**
	 * Indicates change of a series line width
	 */
	public static final int SERIES_LINE_WIDTH_CHANGED = 6;
	/**
	 * Indicates that settings for a series have been added
	 */
	public static final int SERIES_ADDED = 7;
	/**
	 * Indicates that settings for a series have been removed
	 */
	public static final int SERIES_REMOVED = 8;
	/**
	 * Indicates that the time format hasbeen changed
	 */
	public static final int TIME_FORMAT_CHANGED = 9;
	
	/**
	 * Indicates which setting changed
	 */
	private int type;
	/**
	 * The index of the series for which the change applies (-1 == series independent)
	 */
	private int seriesIndex = -1;
	/**
	 * Further information about the change
	 */
	private Object data;
	/**
	 * @param type
	 * @param data
	 */
	public SettingsInfo(int type, Object data) {
		super();
		this.type = type;
		this.data = data;
	}
	/**
	 * @param type
	 * @param seriesIndex
	 * @param data
	 */
	public SettingsInfo(int type, int seriesIndex, Object data) {
		super();
		this.type = type;
		this.seriesIndex = seriesIndex;
		this.data = data;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the seriesIndex
	 */
	public int getSeriesIndex() {
		return seriesIndex;
	}
	/**
	 * @param seriesIndex the seriesIndex to set
	 */
	public void setSeriesIndex(int seriesIndex) {
		this.seriesIndex = seriesIndex;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
}
