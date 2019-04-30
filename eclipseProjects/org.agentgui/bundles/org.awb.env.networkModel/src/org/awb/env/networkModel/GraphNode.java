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
package org.awb.env.networkModel;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import de.enflexit.common.SerialClone;

/**
 * This class represents a graph node in an environment model of the type graph / network
 * 
 * @see GraphEdge
 * @see GraphElement
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphNode extends GraphElement {

	private static final long serialVersionUID = 7676853104978228247L;
	
	public final static String GRAPH_NODE_PREFIX = "PP";
	
	/** The GraphNode's position in a visualization */
	private Point2D position;

	/**	The data model for this GraphNode.*/
	private Object dataModel;
	/** The data model for this GraphNode encoded as Base64 String*/
	private Vector<String> dataModelBase64;

	private TreeMap<String, Point2D> positionTreeMap;
	
	/**
	 * Default constructor with a default position of (Point2D.Double(50.0, 50.0)).
	 */
	public GraphNode() {
		this.position = new Point2D.Double(50.0, 50.0);
	}

	/**
	 * Instantiates a new graph node.
	 *
	 * @param id the ID of this GraphNode
	 * @param position the position of the GraphNode
	 */
	public GraphNode(String id, Point2D position) {
		this.id = id;
		this.position = position;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphElement#getCopy()
	 */
	@Override
	public GraphNode getCopy() {
		GraphNode copy = null;	
		try {
			copy = SerialClone.clone(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return copy;
	}

	/**
	 * Sets the position of this GraphNode.
	 * @param point2d the position to set
	 */
	public void setPosition(Point2D point2d) {
		this.position = point2d;
	}
	/**
	 * Returns the position.
	 * @return the position
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * Returns the position tree map that distinguishes positions for different layouts.
	 * @return the position tree map
	 */
	public TreeMap<String, Point2D> getPositionTreeMap() {
		if (positionTreeMap==null) {
			positionTreeMap = new TreeMap<>();
		}
		return positionTreeMap;
	}
	/**
	 * Sets the position tree map.
	 * @param positionTreeMap the position tree map
	 */
	public void setPositionTreeMap(TreeMap<String, Point2D> positionTreeMap) {
		this.positionTreeMap = positionTreeMap;
	}
	
	/**
	 * Returns the position tree map as string.
	 * @return the position tree map as string
	 */
	public String getPositionTreeMapAsString() {
		
		String config = null;
		if (this.getPositionTreeMap().size()==0) {
			// --- Set to null to reduce RAM load ---------
			this.setPositionTreeMap(null);
			
		} else {
			// --- Convert to String ----------------------
			List<String> keys = new ArrayList<>(this.getPositionTreeMap().keySet()); 
			Collections.sort(keys);
			for (int i = 0; i < keys.size(); i++) {
				String layoutID = keys.get(i);
				Point2D position = this.getPositionTreeMap().get(layoutID);
	
				String singleConfig = layoutID + ":=" + getPositionAsString(position);
				if (config==null) {
					config = singleConfig;
				} else {
					config = config + "|" + singleConfig;
				}
			}
			
		}
		return config;
	}
	/**
	 * Sets the position tree map from the specified string.
	 * @param treeMapAsString the new position tree map from string
	 */
	public void setPositionTreeMapFromString(String treeMapAsString) {
		
		if (treeMapAsString==null || treeMapAsString.isEmpty()) return;
		
		String[] layoutPositions = treeMapAsString.split("\\|");
		for (int i = 0; i < layoutPositions.length; i++) {
			
			String[] layoutPositionPair = layoutPositions[i].split(":=");
			
			String layoutID = layoutPositionPair[0];
			Point2D postion = getPositionFromString(layoutPositionPair[1]);
			if (layoutID!=null && layoutID.isEmpty()==false && position!=null) {
				this.getPositionTreeMap().put(layoutID, postion);
			}
		}
	}
	
	/**
	 * Sets the current data model.
	 * @param dataModel the new Object instance of the data model
	 */
	public void setDataModel(Object dataModel) {
		this.dataModel = dataModel;
	}
	/**
	 * Returns the current data model as Object instance.
	 * @return the data model
	 */
	public Object getDataModel() {
		return dataModel;
	}

	/**
	 * Sets the data model as Vector of Base64 encoded Strings.
	 * @param dataModelBase64 the new data model base64 
	 */
	public void setDataModelBase64(Vector<String> dataModelBase64) {
		this.dataModelBase64 = dataModelBase64;
	}
	/**
	 * Returns the data model as Vector of Base64 encoded Strings.
	 * @return the data model base64
	 */
	public Vector<String> getDataModelBase64() {
		return dataModelBase64;
	}

	
	// ------------------------------------------------------------------------
	// --- From here, some static help methods can be found -------------------
	// ------------------------------------------------------------------------	
	/**
	 * Return a specified position as string.
	 *
	 * @param position the position
	 * @return the position as string
	 */
	public static String getPositionAsString(Point2D position) {
		return position.getX() + ":" + position.getY();
	}
	/**
	 * Returns the position point from string.
	 *
	 * @param positionString the position string
	 * @return the position from string
	 */
	public static Point2D getPositionFromString(String positionString) {
		
		if (positionString==null || positionString.isEmpty()) return null;
		
		Point2D pos = null;
		String[] coords = positionString.split(":");
		if (coords.length == 2) {
			double xPos = Double.parseDouble(coords[0]);
			double yPos = Double.parseDouble(coords[1]);
			pos = new Point2D.Double(xPos, yPos);
			
		}
		return pos;
	}
	
}
