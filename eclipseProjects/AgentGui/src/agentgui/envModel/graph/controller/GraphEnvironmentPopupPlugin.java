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

import java.awt.Component;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
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
public class GraphEnvironmentPopupPlugin<V, E> extends AbstractPopupGraphMousePlugin implements Observer {
	
	private final String MENU_ITEM_NAME_PREFIX = "~TmpItem~";
	private final String IMAGE_PATH = GraphGlobals.getPathImages(); // @jve:decl-index=0:
	
	private BasicGraphGui basicGraphGui = null;
	private GraphEnvironmentController graphController = null;
	
    private JPopupMenu edgePopup;
    private JPopupMenu vertexPopup;

    
    /** Creates a new instance of GraphPopupMenuMousePlugin */
    public GraphEnvironmentPopupPlugin(BasicGraphGui parentGUI) {
        super(MouseEvent.BUTTON3_MASK);
        this.basicGraphGui = parentGUI;
        this.graphController = this.basicGraphGui.getGraphEnvironmentController();
        this.graphController.addObserver(this);
    }
    
    /**
     * Shows the pop up menu for a given component.
     * @param graphObject the graph object
     */
    public void showPopUp(Object graphObject) {
    	
    	BasicGraphGuiVisViewer<GraphNode, GraphEdge> visView = this.basicGraphGui.getVisualizationViewer();
    	Point locationMouse = MouseInfo.getPointerInfo().getLocation();
    	Point locationPanel = visView.getLocationOnScreen();
    	Point locationOnPanel = new Point(locationMouse.x - locationPanel.x, locationMouse.y - locationPanel.y);
    	
    	if (graphObject instanceof GraphNode) {
    	
    		GraphNode graphNode = (GraphNode) graphObject;
    		this.updateVertexMenu(graphNode);
    		vertexPopup.validate();
    		vertexPopup.show(visView, locationOnPanel.x, locationOnPanel.y);
            basicGraphGui.handleObjectRightClick(graphNode);
            
    	} else if (graphObject instanceof GraphEdge) {
    		
    		GraphEdge graphEdge = (GraphEdge) graphObject;
    		GraphNode graphNode = null;
    		
    		NetworkModel networkModel = this.graphController.getNetworkModel();
    		NetworkComponent netComp = networkModel.getNetworkComponent(graphEdge);
    		Vector<GraphElement> graphElements = networkModel.getGraphElementsFromNetworkComponent(netComp);
    		for(GraphElement graphElement : graphElements){
    			if (graphElement instanceof GraphNode) {
    				graphNode = (GraphNode) graphElement;
    			}
    		}
    		
    		if (graphNode!=null) {
    			this.updateEdgeMenu(graphEdge);
    			edgePopup.validate();
    			edgePopup.show(visView, locationOnPanel.x, locationOnPanel.y);
                basicGraphGui.handleObjectRightClick(graphEdge);	
    		}
            
    	}
    	
    }
    
    /**
     * Implementation of the AbstractPopupGraphMousePlugin method. This is where the 
     * work gets done. You shouldn't have to modify unless you really want to...
     * @param me the MousEvent
     */
    protected void handlePopup(MouseEvent me) {
        
    	Point2D p = me.getPoint();
    	
		VisualizationViewer<GraphNode,GraphEdge> visView = this.basicGraphGui.getVisualizationViewer();
        GraphElementAccessor<GraphNode,GraphEdge> pickSupport = visView.getPickSupport();
        if(pickSupport != null) {
            GraphNode vertex = (GraphNode) pickSupport.getVertex(visView.getGraphLayout(), p.getX(), p.getY());
            if(vertex != null) {
                //System.out.println("Vertex " + v + " was right clicked");
                this.updateVertexMenu(vertex);
                vertexPopup.show(visView, me.getX(), me.getY());
                basicGraphGui.handleObjectRightClick(vertex);
                
            } else {
                GraphEdge edge = (GraphEdge) pickSupport.getEdge(visView.getGraphLayout(), p.getX(), p.getY());
                if(edge != null) {
                    //System.out.println("Edge " + edge + " was right clicked");
                	this.updateEdgeMenu(edge);
                    edgePopup.show(visView, me.getX(), me.getY());
                    basicGraphGui.handleObjectRightClick(edge);  
                }
            }
        }
    }
    
    /**
     * Update edge menu with context sensitive menu items.
     * @param graphEdge the current GraphEdge
     */
    private void updateEdgeMenu(GraphEdge graphEdge) {
        
    	if (this.edgePopup == null) return;
    	
    	// --- Remove the old customized entries ---------------
    	Component[] comps = this.edgePopup.getComponents();
    	for (int i = 0; i < comps.length; i++) {
    		Component comp = comps[i];
    		if (comp!=null && comp.getName()!=null && comp.getName().startsWith(MENU_ITEM_NAME_PREFIX)) {
    			this.edgePopup.remove(comp);
    		}
		}
    	
    	// --- Evaluate for a NetworkComponent -----------------
    	Vector<NetworkComponent> netComps = this.getNetworkComponents(graphEdge);
    	if (netComps!=null) {
    		// --- Add the context sensitive JMenueItems -------
    		NetworkComponent netComp = netComps.iterator().next();
    		NetworkModel netModel = this.basicGraphGui.getGraphEnvironmentController().getNetworkModel();
    		NetworkComponentAdapter adapter = netModel.getNetworkComponentAdapter(this.graphController, netComp);
    		if (adapter!=null) {
    			// --- Get the context menu items -------------- 
    			Vector<JComponent> adapterJComponents2Add = adapter.invokeGetJPopupMenuElements(netComp);
        		if (adapterJComponents2Add!=null) {
        			if (adapterJComponents2Add.size()>0) {
        				// --- Add Separator -------------------
        				this.edgePopup.add(this.getNewSeparator(netComp.getId()+ "Adapter"));
        				for (int i = 0; i < adapterJComponents2Add.size(); i++) {
							JComponent item = adapterJComponents2Add.get(i);
							item.setName(MENU_ITEM_NAME_PREFIX + netComp.getId()+ i);
							this.edgePopup.add(item);
						}
        			}
        		}
    		}
    	}
    }
    
    /**
     * Update vertex menu with context sensitive menu items.
     * @param graphNode the GraphNode
     */
    private void updateVertexMenu(GraphNode graphNode) {
        
    	if (this.vertexPopup == null) return;
    	
    	// --- Remove the old customized entries ---------------
    	Component[] comps = this.vertexPopup.getComponents();
    	for (int i = 0; i < comps.length; i++) {
    		Component comp = comps[i];
    		if (comp!=null && comp.getName()!=null && comp.getName().startsWith(MENU_ITEM_NAME_PREFIX)) {
    			this.vertexPopup.remove(comp);
    		}
		}
    	
    	// --- Get the current NetworkModel --------------------
    	NetworkModel netModel = this.basicGraphGui.getGraphEnvironmentController().getNetworkModel();
    	NetworkComponentAdapter adapter = null;
    	Vector<JComponent> adapterJComponents2Add = null;
    		
    	// --- Evaluate for a GraphNode context menu -----------
    	adapter = netModel.getNetworkComponentAdapter(this.graphController, graphNode);
    	if (adapter!=null) {
    		adapterJComponents2Add = adapter.invokeGetJPopupMenuElements(graphNode);
        	if (adapterJComponents2Add!=null) {
    			if (adapterJComponents2Add.size()>0) {
    				this.vertexPopup.add(this.getNewSeparator(graphNode.getId()+ "Adapter"));
    				for (int i = 0; i < adapterJComponents2Add.size(); i++) {
    					JComponent item = adapterJComponents2Add.get(i);
    					item.setName(MENU_ITEM_NAME_PREFIX + graphNode.getId()+ i);
    					this.vertexPopup.add(item);
    				}
    			}
    		}	
    	}
    	
    	// --- Evaluate for a NetworkComponent -----------------
    	Vector<NetworkComponent> netComps = this.getNetworkComponents(graphNode);
    	if (netComps!=null) {
    		
    		for (NetworkComponent netComp : netComps) {
        		// --- Add the Properties Item for this NetworkComponentn -----------------
    			this.vertexPopup.add(this.getNewSeparator(netComp.getId()));
    			this.vertexPopup.add(this.getJLabel4NetworkComponent(netComp));
    			this.vertexPopup.add(this.getJMenuItem4NetworkComponentProperties(netComp));
    			
    			// --- Add the context sensitive JMenueItems for the single element -------
        		adapter = netModel.getNetworkComponentAdapter(this.graphController, netComp);
        		if (adapter!=null) {
        			// --- Get the context menu items --------------
        			adapterJComponents2Add = null;
        			adapterJComponents2Add = adapter.invokeGetJPopupMenuElements(netComp);
        			if (adapterJComponents2Add!=null) {
        				if (adapterJComponents2Add.size()>0) {
        					this.vertexPopup.add(this.getNewSeparator(netComp.getId()+ "Adapter"));
            				for (int i = 0; i < adapterJComponents2Add.size(); i++) {
    							JComponent item = adapterJComponents2Add.get(i);
    							item.setName(MENU_ITEM_NAME_PREFIX + netComp.getId()+ i);
    							this.vertexPopup.add(item);
    						}
        				}
        			}
        		}
    		}
    	}
        
    }
    
    
    /**
     * Returns the network components for a specified graphElement.
     *
     * @param graphElement the graph element
     * @return the network component
     */
    private Vector<NetworkComponent> getNetworkComponents(Object graphElement) {
    	Vector<NetworkComponent> netCompSet = new Vector<NetworkComponent>();
    	NetworkModel netModel = this.basicGraphGui.getGraphEnvironmentController().getNetworkModel();
    	if (graphElement instanceof GraphNode) {
    		netCompSet.addAll(netModel.getNetworkComponents((GraphNode)graphElement));
    	} else if (graphElement instanceof GraphEdge) {
    		netCompSet.add(netModel.getNetworkComponent((GraphEdge)graphElement));
    	}
    	
    	if (netCompSet.size()>0) {
    		Collections.sort(netCompSet, new Comparator<NetworkComponent>() {
				@Override
				public int compare(NetworkComponent comp1, NetworkComponent comp2) {
					if (comp1.getType().equals(comp2.getType())) {
						return comp1.getId().compareTo(comp2.getId());
					} 
					return comp1.getType().compareTo(comp2.getType());
				}
    		});
    		return netCompSet;
    	}
    	return null;
    }

    /**
     * Returns a JMenueItem for the properties of a NetworkComponent.
     *
     * @param networkComponent the NetworkComponent
     * @return the JMenueItem for the specified NetworkComponent
     */
    private JLabel getJLabel4NetworkComponent(NetworkComponent networkComponent) {
    	JLabel jlabel = new JLabel();
    	jlabel.setName(MENU_ITEM_NAME_PREFIX + "_JLabel_" + networkComponent.getId());
    	jlabel.setFont(new Font("Dialog", Font.BOLD, 12));
    	jlabel.setText("       " + networkComponent.getType() + ": " + networkComponent.getId());
    	return jlabel;
    }
    /**
     * Returns a JMenueItem for the properties of a NetworkComponent.
     *
     * @param networkComponent the NetworkComponent
     * @return the JMenueItem for the specified NetworkComponent
     */
    private JMenuItem getJMenuItem4NetworkComponentProperties(NetworkComponent networkComponent) {
    	
    	final NetworkComponent networkComponent2Edit = networkComponent;
    	
    	JMenuItem jMenuItem = new JMenuItem();
    	jMenuItem.setName(MENU_ITEM_NAME_PREFIX + "_JMenuItem_" + networkComponent.getId());
    	jMenuItem.setText(Language.translate("Edit Properties", Language.EN));
    	jMenuItem.setIcon(new ImageIcon(getClass().getResource(IMAGE_PATH + "Properties.png")));
    	jMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
    			NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
    			nmn.setInfoObject(networkComponent2Edit);
    			basicGraphGui.getGraphEnvironmentController().notifyObservers(nmn);
			}
		});
    	return jMenuItem;
    }
    
    /**
     * Returns a new JSeparator for a popup.
     * @return the new JSeparator for the popup
     */
    private JSeparator getNewSeparator(String identifier) {
    	JSeparator sep = new JSeparator();
    	sep.setName(MENU_ITEM_NAME_PREFIX + "_Separator_" + identifier);
    	return sep;
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

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
		
		if (object instanceof NetworkModelNotification) {
    		
    		NetworkModelNotification nmNotification = (NetworkModelNotification) object;
    		int reason = nmNotification.getReason();
    		Object infoObject = nmNotification.getInfoObject();

			switch (reason) {
			case NetworkModelNotification.NETWORK_MODEL_ShowPopUpMenue:
				this.showPopUp(infoObject);
				break;
			}
		}
	}

}
