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

import java.awt.Color;

public class SeriesSettings {
	private String label;
	private Color color;
	private float lineWIdth;

	/**
	 * Default constructor
	 */
	public SeriesSettings() {
	}
	/**
	 * @param label
	 * @param color
	 * @param lineWIdth
	 */
	public SeriesSettings(String label, Color color, float lineWIdth) {
		super();
		this.label = label;
		this.color = color;
		this.lineWIdth = lineWIdth;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * @return the lineWIdth
	 */
	public float getLineWIdth() {
		return lineWIdth;
	}
	/**
	 * @param lineWIdth the lineWIdth to set
	 */
	public void setLineWIdth(float lineWIdth) {
		this.lineWIdth = lineWIdth;
	}
}
