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

import javax.swing.JPopupMenu;

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
	private BasicGraphGui myGUI = null;

    private JPopupMenu edgePopup, vertexPopup;
    
    
    /** Creates a new instance of GraphPopupMenuMousePlugin */
    public GraphEnvironmentPopupPlugin(BasicGraphGui parentGUI) {
        super(MouseEvent.BUTTON3_MASK);
        this.myGUI = parentGUI;
    }
    
    /**
     * Implementation of the AbstractPopupGraphMousePlugin method. This is where the 
     * work gets done. You shouldn't have to modify unless you really want to...
     * @param e 
     */
    protected void handlePopup(MouseEvent e) {
        
    	@SuppressWarnings("unchecked")
		final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
        Point2D p = e.getPoint();
        
        GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
        if(pickSupport != null) {
            final V v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            if(v != null) {
                //System.out.println("Vertex " + v + " was right clicked");
                updateVertexMenu(v, vv, p);
                vertexPopup.show(vv, e.getX(), e.getY());
                myGUI.handleObjectRightClick(v);
                
            } else {
                final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
                if(edge != null) {
                    //System.out.println("Edge " + edge + " was right clicked");
                    updateEdgeMenu(edge, vv, p);
                    edgePopup.show(vv, e.getX(), e.getY());
                    myGUI.handleObjectRightClick(edge);  
                }
            }
        }
    }
    
    /**
     * Update vertex menu with context sensitive menu items.
     *
     * @param v the v
     * @param vv the vv
     * @param point the point
     */
    private void updateVertexMenu(V v, VisualizationViewer<V, E> vv, Point2D point) {
        
    	if (vertexPopup == null) return;
    	
//        Component[] menuComps = vertexPopup.getComponents();
//        for (Component comp: menuComps) {
//            if (comp instanceof VertexMenuListener) {
//                ((VertexMenuListener)comp).setVertexAndView(v, vv);
//            }
//            if (comp instanceof MenuPointListener) {
//                ((MenuPointListener)comp).setPoint(point);
//            }
//        }
        
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
    
    /**
     * Update edge menu with context sensitive menu items.
     *
     * @param edge the edge
     * @param vv the vv
     * @param point the point
     */
    private void updateEdgeMenu(E edge, VisualizationViewer<V, E> vv, Point2D point) {
        
    	if (edgePopup == null) return;
    	
//        Component[] menuComps = edgePopup.getComponents();
//        for (Component comp: menuComps) {
//            if (comp instanceof EdgeMenuListener) {
//                ((EdgeMenuListener)comp).setEdgeAndView(edge, vv);
//            }
//            if (comp instanceof MenuPointListener) {
//                ((MenuPointListener)comp).setPoint(point);
//            }
//        }
        
    }
    
}
