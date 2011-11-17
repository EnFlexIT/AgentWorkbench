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

package agentgui.envModel.graph.controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.SwingUtilities;

import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * Handling mouse interaction with graph visualizations in a BasicGraphGUI.
 * 
 * @see BasicGraphGUI  
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class GraphEnvironmentMousePlugin extends PickingGraphMousePlugin<GraphNode, GraphEdge> implements MouseWheelListener {
	
	/** The parent BasicGraphGUI */
	private BasicGraphGUI myGUI = null;
	/** The current VisualizationViewer	*/
	private VisualizationViewer<GraphNode,GraphEdge> vv = null; 	
	
	/** Move panel with right currently ? */
	private boolean movePanelWithRightAction = false;
	/** Move node with left currently ? */
	private boolean moveNodeWithLeftAction = false;

	private Vector<GraphNode> nodesTemp = new Vector<GraphNode>();
	private Vector<GraphNode> nodesMoved = new Vector<GraphNode>();
	
	/** Whether to center the zoom at the current mouse position */
	private boolean zoomAtMouse = true;
	/** controls scaling operations */
    private ScalingControl scaler = new CrossoverScalingControl();
    /** the amount to zoom in by */
	protected float in = 1.1f;
	/** the amount to zoom out by */
	protected float out = 1/1.1f;
	
	
	/**
	 * Constructor
	 * @param parentGUI The parent BasicGraphGUI
	 */
	public GraphEnvironmentMousePlugin(BasicGraphGUI parentGUI) {
		super();
		this.myGUI = parentGUI;
	}
	/**
	 * Sets the visualization viewer.
	 * @param me the new visualization viewer
	 */
	@SuppressWarnings("unchecked")
	private void setVisualizationViewer(MouseEvent me) {
		this.vv = (VisualizationViewer<GraphNode, GraphEdge>)me.getSource();
	}
	/**
	 * Rounds a position value to the closest position using the grid raster.
	 * @param position the position
	 * @param snapRaster the snap raster size
	 * @return the new position coordinate
	 */
	private double roundGridSnap(double position, double snapRaster) {
		
		double dblDivisor = position/snapRaster;  
		double dblDivisorLB = Math.floor(dblDivisor);
		double dblResidual = Math.round(dblDivisor - dblDivisorLB); 
		double rounded = (dblDivisorLB + dblResidual) * snapRaster;
		return rounded;
	}
	
	/**
	 * Sets the nodes moved2 end position.
	 */
	private void setNodesMoved2EndPosition() {
		this.removeAllTemporaryNodes(myGUI.getGraphEnvironmentController().getNetworkModel().getGraph());
		for (int i = 0; i < this.nodesMoved.size(); i++) {
			GraphNode node = this.nodesMoved.get(i);
			vv.getGraphLayout().setLocation(node, node.getPosition());
		}
		this.nodesMoved.removeAllElements();
	}
	
	/**
	 * Sets a temporary node.
	 * @param graph the graph
	 * @param point2d the point2d
	 */
	private void addTemporaryNode(Graph<GraphNode, GraphEdge> graph, Point2D point2d) {
		GraphNode tmpNode = new GraphNode();
		tmpNode.setPosition(point2d);
		graph.addVertex(tmpNode);
		this.nodesTemp.add(tmpNode);
	}
	/**
	 * Removes all temporary nodes.
	 * @param graph the graph
	 */
	private void removeAllTemporaryNodes(Graph<GraphNode, GraphEdge> graph) {
		for (int i = 0; i < this.nodesTemp.size(); i++) {
			GraphNode node = this.nodesTemp.get(i);
			graph.removeVertex(node);
		}
		this.nodesTemp.removeAllElements();
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		super.mousePressed(me);

		this.setVisualizationViewer(me);
		GraphElementAccessor<GraphNode, GraphEdge> ps = vv.getPickSupport();

		Point position = me.getPoint();
		GraphNode pickedNode = ps.getVertex(vv.getGraphLayout(), position.getX(), position.getY());
		GraphEdge pickedEdge = ps.getEdge(vv.getGraphLayout(), position.getX(), position.getY());
		
		if (SwingUtilities.isRightMouseButton(me)) {
			if(pickedNode == null && pickedEdge==null){		
				this.movePanelWithRightAction = true;
				vv.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			} 
		} else if (SwingUtilities.isLeftMouseButton(me)) {
			if (pickedNode!=null) {
				this.moveNodeWithLeftAction = true;	
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		super.mouseReleased(me);
		
		if (SwingUtilities.isRightMouseButton(me)) {
			if (movePanelWithRightAction==true) {
				this.movePanelWithRightAction = false;
				this.setVisualizationViewer(me);
				vv.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else if (SwingUtilities.isLeftMouseButton(me)) {
			if (moveNodeWithLeftAction=true) {
				this.moveNodeWithLeftAction = true;	
				this.setNodesMoved2EndPosition();
			}
		}
	
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me){

		if(me.getButton()==MouseEvent.BUTTON1 || me.getButton()==MouseEvent.BUTTON3){			
			
			// --- Left click or Right click --------------
			Point point = me.getPoint();
			
			this.setVisualizationViewer(me);
			GraphElementAccessor<GraphNode, GraphEdge> ps = vv.getPickSupport();

			// --- Check if an object was selected --------
			Object pickedObject = null;
			GraphNode pickedNode = ps.getVertex(vv.getGraphLayout(), point.getX(), point.getY());
			if(pickedNode != null) {  
				pickedObject = pickedNode;
			} else {
				GraphEdge pickedEdge = ps.getEdge(vv.getGraphLayout(), point.getX(), point.getY());
				if(pickedEdge != null) { 
					pickedObject = pickedEdge;
				}
			}

			// --- Only when node or edge is clicked -----------
			if(pickedObject != null) {
				if (me.getClickCount()==2){
					// --- Double click ---------
					myGUI.handleObjectDoubleClick(pickedObject);
				} else if(me.getButton()==MouseEvent.BUTTON1 && me.isShiftDown()) {
					// --- Shift + Left click ---
					
				} else if(me.getButton()==MouseEvent.BUTTON1) {
					// --- Left click -----------
					myGUI.handleObjectLeftClick(pickedObject);
				}
			}
			
		}
		
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me){
		super.mouseDragged(me);
		
		this.setVisualizationViewer(me);
		MutableTransformer modelTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		
		// ----------------------------------------------------------------------------------------
		// --- Action if the right mouse button is pressed and no graph element is selected -------
		// ----------------------------------------------------------------------------------------
		if (movePanelWithRightAction==true) {
			
            try {
                Point2D q = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(down);
                Point2D p = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(me.getPoint());
                float dx = (float) (p.getX()-q.getX());
                float dy = (float) (p.getY()-q.getY());
                
                modelTransformer.translate(dx, dy);
                down.x = me.getX();
                down.y = me.getY();
            } catch(RuntimeException ex) {
                System.err.println("down = "+down+", e = "+me);
                throw ex;
            }
        
            me.consume();
            vv.repaint();
		}
		
		// ----------------------------------------------------------------------------------------
		// --- Update the GraphNode's position attribute ------------------------------------------ 
		// ----------------------------------------------------------------------------------------
		if (moveNodeWithLeftAction==true) {
			
			Graph<GraphNode, GraphEdge> graph = null;
			ComponentTypeSettings cts = null;
			boolean snapToGrid = false;
			double snapRaster = 0;
			
			Iterator<GraphNode> pickedNodes = vv.getPickedVertexState().getPicked().iterator();
			while(pickedNodes.hasNext()){

				// --- Get the Graph, if not already there --------------------
				if (graph==null) {
					graph = myGUI.getGraphEnvironmentController().getNetworkModel().getGraph();
					this.nodesMoved.removeAllElements();
					this.removeAllTemporaryNodes(graph);
				}
				// --- Get the ComponentTypeSettings, if not already there ----
				if (cts==null) {
					cts = myGUI.getGraphEnvironmentController().getComponentTypeSettings().get("node");	
					snapToGrid = cts.isSnap2Grid();
					snapRaster = cts.getSnapRaster();
				}
				
				// --- Get the position of the node ---------------------------
				GraphNode pickedNode = pickedNodes.next();
				Point2D newPos = myGUI.getVisView().getGraphLayout().transform(pickedNode);
				if (snapToGrid==true && snapRaster>0) {
					double xPos = roundGridSnap(newPos.getX(), snapRaster); 
					double yPos = roundGridSnap(newPos.getY(), snapRaster);
					newPos.setLocation(xPos, yPos);
					
					this.nodesMoved.add(pickedNode);
					this.addTemporaryNode(graph, newPos);
				}
				pickedNode.setPosition(newPos);
				
			}
			me.consume();
	        vv.repaint();
		}
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent me) {

		this.setVisualizationViewer(me);
		Point2D mouse = me.getPoint();
		Point2D center = vv.getCenter();
		int amount = me.getWheelRotation();
         
		if(zoomAtMouse) {
			if(amount > 0) {
				scaler.scale(vv, in, mouse);
			} else if(amount < 0) {
				scaler.scale(vv, out, mouse);
			}
			
		} else {
			if(amount > 0) {
				scaler.scale(vv, in, center);
			} else if(amount < 0) {
				scaler.scale(vv, out, center);
			}
         }
         me.consume();
         vv.repaint();
		
	}
	
}
