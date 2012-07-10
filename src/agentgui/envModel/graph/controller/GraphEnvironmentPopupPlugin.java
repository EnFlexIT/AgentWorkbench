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

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkModel;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * A GraphMousePlugin that brings up distinct pop up menus when an edge or vertex is
 * appropriately clicked in a graph.  If these menus contain components that implement
 * either the EdgeMenuListener or VertexMenuListener then the corresponding interface
 * methods will be called prior to the display of the menus (so that they can display
 * context sensitive information for the edge or vertex).
 * 
 * @author <br>Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public class GraphEnvironmentPopupPlugin<V, E> extends AbstractPopupGraphMousePlugin {
	
	/** The parent BasicGraphGUI */
	private BasicGraphGui basicGraphGui = null;

    private JPopupMenu edgePopup;
    private JPopupMenu vertexPopup;

    private Vector<JMenuItem> jMenuItemsAdditionsVertex = new Vector<JMenuItem>();
    private Vector<JMenuItem> jMenuItemsAdditionsEdge = new Vector<JMenuItem>();
    
    private JSeparator separatorVertex = new JPopupMenu.Separator();
    private JSeparator separatorEdge = new JPopupMenu.Separator();
    
    
    
    /** Creates a new instance of GraphPopupMenuMousePlugin */
    public GraphEnvironmentPopupPlugin(BasicGraphGui parentGUI) {
        super(MouseEvent.BUTTON3_MASK);
        this.basicGraphGui = parentGUI;
    }
    
    /**
     * Implementation of the AbstractPopupGraphMousePlugin method. This is where the 
     * work gets done. You shouldn't have to modify unless you really want to...
     * @param me 
     */
    protected void handlePopup(MouseEvent me) {
        
    	@SuppressWarnings("unchecked")
		final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)me.getSource();
        Point2D p = me.getPoint();
        
        GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
        if(pickSupport != null) {
            final V vertex = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            if(vertex != null) {
                //System.out.println("Vertex " + v + " was right clicked");
                this.updateVertexMenu(vertex, vv, p);
                vertexPopup.show(vv, me.getX(), me.getY());
                basicGraphGui.handleObjectRightClick(vertex);
                
            } else {
                final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
                if(edge != null) {
                    //System.out.println("Edge " + edge + " was right clicked");
                	this.updateEdgeMenu(edge, vv, p);
                    edgePopup.show(vv, me.getX(), me.getY());
                    basicGraphGui.handleObjectRightClick(edge);  
                }
            }
        }
    }
    
    /**
     * Update vertex menu with context sensitive menu items.
     *
     * @param vertex the vertex
     * @param vv the VisualizationViewer
     * @param point the point
     */
    private void updateVertexMenu(V vertex, VisualizationViewer<V, E> vv, Point2D point) {
        
    	if (this.vertexPopup == null) return;
    	
    	// --- Remove the old customized entries ---------------
    	if (this.jMenuItemsAdditionsVertex!=null) {
    		this.vertexPopup.remove(this.separatorVertex);
    		for (JMenuItem item : this.jMenuItemsAdditionsVertex) {
        		this.vertexPopup.remove(item);
    		}
    	}

    	// --- Evaluate for a NetworkComponent -----------------
    	NetworkComponent netComp = this.getNetworkComponent(vertex);
    	if (netComp!=null) {
    		// --- Add the context sensitive JMenueItems -------
    		NetworkModel netModel = this.basicGraphGui.getGraphEnvironmentController().getNetworkModel();
    		NetworkComponentAdapter adapter = netModel.getNetworkComponentAdapter(netComp);
    		if (adapter!=null) {
    			// --- Get the context menu items --------------
    			this.jMenuItemsAdditionsVertex = adapter.invokeGetJPopupMenuElements(this.basicGraphGui.getGraphEnvironmentController(), netComp);
    			if (this.jMenuItemsAdditionsVertex!=null) {
    				if (this.jMenuItemsAdditionsVertex.size()>0) {
    					this.vertexPopup.add(this.separatorVertex);
    					for (JMenuItem item : this.jMenuItemsAdditionsVertex) {
    	        			this.vertexPopup.add(item);
    	    			}
    				}
    			}
    		}
    		
    	}
        
    }
    /**
     * Update edge menu with context sensitive menu items.
     *
     * @param edge the edge
     * @param vv the VisualizationViewer
     * @param point the point
     */
    private void updateEdgeMenu(E edge, VisualizationViewer<V, E> vv, Point2D point) {
        
    	if (this.edgePopup == null) return;
    	
    	// --- Remove the old customized entries ---------------
    	if (this.jMenuItemsAdditionsEdge!=null) {
    		this.edgePopup.remove(this.separatorEdge);
    		for (JMenuItem item : this.jMenuItemsAdditionsEdge) {
        		this.edgePopup.remove(item);
    		}	
    	}
    	
    	// --- Evaluate for a NetworkComponent -----------------
    	NetworkComponent netComp = this.getNetworkComponent(edge);
    	if (netComp!=null) {
    		// --- Add the context sensitive JMenueItems -------
    		NetworkModel netModel = this.basicGraphGui.getGraphEnvironmentController().getNetworkModel();
    		NetworkComponentAdapter adapter = netModel.getNetworkComponentAdapter(netComp);
    		if (adapter!=null) {
    			// --- Get the context menu items -------------- 
    			this.jMenuItemsAdditionsEdge = adapter.invokeGetJPopupMenuElements(this.basicGraphGui.getGraphEnvironmentController(), netComp);
        		if (this.jMenuItemsAdditionsEdge!=null) {
        			if (this.jMenuItemsAdditionsEdge.size()>0) {
        				// --- Add Separator
        				this.edgePopup.add(this.separatorEdge);
        				for (JMenuItem item : this.jMenuItemsAdditionsEdge) {
                			this.edgePopup.add(item);
            			}		
        			}
        		}
    		}
    		
    	}
    	
    }
    
    /**
     * Gets the network component for a specified graphElement.
     *
     * @param graphElement the graph element
     * @return the network component
     */
    private NetworkComponent getNetworkComponent(Object graphElement) {
    	
    	NetworkModel netModel = this.basicGraphGui.getGraphEnvironmentController().getNetworkModel();
    	NetworkComponent netComp = null;
    	if (graphElement instanceof GraphNode) {
    		
    		HashSet<NetworkComponent> netComps = netModel.getNetworkComponents((GraphNode)graphElement);
    		if (netComps.size()==1) {
    			netComp = netComps.iterator().next();
    		} else if (netComps.size()>1) {
    			netComp = netModel.containsDistributionNode(netComps);	
    		}
    		
    	} else if (graphElement instanceof GraphEdge) {
    		
    		netComp = netModel.getNetworkComponent((GraphEdge)graphElement);
    		
    	}
    	return netComp;
    }
    
    /**
     * Getter for the edge popup.
     * @return the edge popup menu
     */
    public JPopupMenu getEdgePopup() {
        return edgePopup;
    }
    /**
     * Setter for the Edge popup.
     * @param edgePopup 
     */
    public void setEdgePopup(JPopupMenu edgePopup) {
        this.edgePopup = edgePopup;
    }
    
    /**
     * Getter for the vertex popup.
     * @return  the vertex popup menu
     */
    public JPopupMenu getVertexPopup() {
        return vertexPopup;
    }
    /**
     * Setter for the vertex popup.
     * @param vertexPopup 
     */
    public void setVertexPopup(JPopupMenu vertexPopup) {
        this.vertexPopup = vertexPopup;
    }
    
}
