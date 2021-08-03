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
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import org.awb.env.networkModel.positioning.GraphNodePositionFactory;
import org.awb.env.networkModel.positioning.GraphNodePositionTreeMap;

import de.enflexit.common.SerialClone;
import de.enflexit.geography.coordinates.AbstractCoordinate;

/**
 * This class represents a graph node in an environment model of the type graph / network
 * 
 * @see GraphEdge
 * @see GraphElement
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphNode extends GraphElement implements DataModelNetworkElement {

	private static final long serialVersionUID = 7676853104978228247L;
	
	public final static String GRAPH_NODE_PREFIX = "PP";
	
	/** The GraphNode's position in a visualization */
	private AbstractCoordinate coordinate;
	private GraphNodePositionTreeMap positionTreeMap;

	private Object dataModel;
	protected TreeMap<String, String> dataModelStorageSettings;
	private Vector<String> dataModelBase64;

	
	/**
	 * Default constructor with a default position of (Point2D.Double(50.0, 50.0)).
	 */
	public GraphNode() { }
	/**
	 * Instantiates a new graph node.
	 *
	 * @param id the ID of this GraphNode
	 * @param position the position of the GraphNode
	 */
	public GraphNode(String id, Point2D position) {
		this.setId(id);
		this.setPosition(position);
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
	 * @param point2D the position to set
	 */
	public void setPosition(Point2D point2D) {
		this.coordinate = GraphNodePositionFactory.convertToCoordinate(point2D);
	}
	/**
	 * Returns the position.
	 * @return the position
	 */
	public Point2D getPosition() {
		return this.getCoordinate();
	}
	
	/**
	 * Sets the new coordinate of the GraphNode.
	 * @param coordinate the new coordinate
	 */
	public void setCoordinate(AbstractCoordinate coordinate) {
		this.coordinate = coordinate;
	}
	/**
	 * Return the coordinate of the current GraphNode.
	 * @return the coordinate
	 */
	public AbstractCoordinate getCoordinate() {
		if (coordinate==null) {
			coordinate = GraphNodePositionFactory.convertToCoordinate(new Point2D.Double(50.0, 50.0));
		}
		return coordinate;
	}
	

	/**
	 * Returns the position tree map that distinguishes positions for different layouts.
	 * @return the position tree map
	 */
	public GraphNodePositionTreeMap getPositionTreeMap() {
		if (positionTreeMap==null) {
			positionTreeMap = new GraphNodePositionTreeMap();
		}
		return positionTreeMap;
	}
	/**
	 * Sets the position tree map.
	 * @param positionTreeMap the position tree map
	 */
	private void setPositionTreeMap(GraphNodePositionTreeMap positionTreeMap) {
		this.positionTreeMap = positionTreeMap;
	}
	
	/**
	 * Returns the position tree map as string.
	 *
	 * @param allowedLayoutIDs the allowed layout I ds
	 * @return the position tree map as string
	 */
	public String getPositionTreeMapAsString(HashSet<String> allowedLayoutIDs) {
		
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
				if (allowedLayoutIDs.contains(layoutID)==false) continue;
				
				AbstractCoordinate coordinate = this.getPositionTreeMap().get(layoutID);
				String singleConfig = layoutID + ":=" + coordinate.serialize();
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
			AbstractCoordinate coordinate = GraphNodePositionFactory.getCoordinateFromString(layoutID, layoutPositionPair[1]);
			if (layoutID!=null && layoutID.isEmpty()==false && coordinate!=null) {
				this.getPositionTreeMap().put(layoutID, coordinate);
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#setDataModel(java.lang.Object)
	 */
	@Override
	public void setDataModel(Object dataModel) {
		this.dataModel = dataModel;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#getDataModel()
	 */
	@Override
	public Object getDataModel() {
		return dataModel;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#getDataModelStorageSettings()
	 */
	@Override
	public TreeMap<String, String> getDataModelStorageSettings() {
		return dataModelStorageSettings;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#setDataModelStorageSettings(java.util.TreeMap)
	 */
	@Override
	public void setDataModelStorageSettings(TreeMap<String, String> dataModelStorageSettings) {
		this.dataModelStorageSettings = dataModelStorageSettings;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#setDataModelBase64(java.util.Vector)
	 */
	@Override
	public void setDataModelBase64(Vector<String> dataModelBase64) {
		this.dataModelBase64 = dataModelBase64;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#getDataModelBase64()
	 */
	@Override
	public Vector<String> getDataModelBase64() {
		return dataModelBase64;
	}
	
}
