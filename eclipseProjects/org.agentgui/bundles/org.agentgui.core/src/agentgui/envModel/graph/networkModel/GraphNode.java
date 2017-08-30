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
package agentgui.envModel.graph.networkModel;

import java.awt.geom.Point2D;
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
	private Point2D position = null;

	/**	The data model for this GraphNode.*/
	private Object dataModel;
	/** The data model for this GraphNode encoded as Base64 String*/
	private Vector<String> dataModelBase64;

	
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
	 * @see agentgui.envModel.graph.networkModel.GraphElement#getCopy(agentgui.envModel.graph.networkModel.NetworkModel)
	 */
	@Override
	public GraphNode getCopy(NetworkModel networkModel) {
		
		GraphNode copy = null;	
		boolean cloneInstance = true;
		if (cloneInstance==true) {
			// -------------------------------------------------------
			// --- Make a serialisation copy the NetworkModel -------- 
			// -------------------------------------------------------
			try {
				copy = SerialClone.clone(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		} else {
			// -------------------------------------------------------
			// ---- Alternative approach to copy a GraphNode ---------
			// -------------------------------------------------------
			copy = new GraphNode(new String(this.id), new Point2D.Double(this.position.getX(), this.position.getY()));
			if (this.graphElementLayout!=null) {
				copy.setGraphElementLayout(this.graphElementLayout.getCopy(copy));	
			}
			// --- Copy the data model ------------------------
			if (this.dataModel!=null) {
				NetworkComponentAdapter adapter = networkModel.getNetworkComponentAdapter(null, this);
				NetworkComponentAdapter4DataModel adapter4DataModel = adapter.getStoredDataModelAdapter();
				if (this.dataModelBase64==null) {
					this.dataModelBase64 = adapter4DataModel.getDataModelBase64Encoded(this.dataModel);
				} 
				Object dataModelCopy = adapter4DataModel.getDataModelBase64Decoded(this.dataModelBase64);
				copy.setDataModel(dataModelCopy);
			}
			// --- Copy the Base64 data model -----------------
			if (this.dataModelBase64!=null) {
				Vector<String> dataModelCopy64 = new Vector<String>();
				for (String single64 : this.dataModelBase64) {
					if (single64!=null & single64.equals("")==false) {
						dataModelCopy64.add(new String(single64));
					} else {
						dataModelCopy64.add(null);
					}
				}
				copy.setDataModelBase64(dataModelCopy64);
			}
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

}
