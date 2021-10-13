package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.Component;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGui;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodeGeoPosition;

import agentgui.core.application.Language;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * The Class ConfiguredLinePopupPlugin.
 */
public class ConfiguredLinePopupPlugin extends AbstractPopupGraphMousePlugin implements ActionListener {

	private BasicGraphGui basicGraphGui;

	private JMenuItem jMenuItemNodePositioning;
	private JMenuItem jMenuItemFinalizeEdit;
	
	private GraphEdge currentGraphEdge;
	private GraphEdgeShapeConfiguration<? extends Shape> currentShapeConfig;
	private GraphNode currentGraphNode;
	private Point2D currentMousePositionInGraph;
	
	
	/**
     * Creates a new instance of ConfiguredLinePopupPlugin.
     * @param basicGraphGui the instance of the parent {@link BasicGraphGui}
     */
    public ConfiguredLinePopupPlugin(BasicGraphGui basicGraphGui) {
        super(MouseEvent.BUTTON3_MASK);
        this.basicGraphGui = basicGraphGui;
    }

    /**
     * Returns the current GraphEnvironmentController.
     * @return the graph environment controller
     */
    public GraphEnvironmentController getGraphEnvironmentController() {
    	return this.basicGraphGui.getGraphEnvironmentController();
    }
    /**
     * Return the BasicGraphGui.
     * @return the basic graph gui
     */
    public BasicGraphGui getBasicGraphGui() {
		return this.basicGraphGui;
	}
    /**
     * Gets the visualization viewer.
     * @return the visualization viewer
     */
    public BasicGraphGuiVisViewer<GraphNode, GraphEdge> getVisualizationViewer() {
    	return this.basicGraphGui.getVisualizationViewer();
    }
    
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin#handlePopup(java.awt.event.MouseEvent)
	 */
	@Override
	protected void handlePopup(MouseEvent me) {

		Point2D mp = me.getPoint();
    	
    	// -- Check if an edge is selected --------------------------
		VisualizationViewer<GraphNode,GraphEdge> visView = this.getVisualizationViewer();
    	Set<GraphEdge> edgesPicked = visView.getPickedEdgeState().getPicked();
    	if (edgesPicked.size()==1) {
    		GraphEdge edgePicked = edgesPicked.iterator().next();
    		if (edgePicked!=null) {
    			// --- Get graph elements captured by mouse --------- 
    			GraphEdge edgeCaptured = visView.getPickSupport().getEdge(visView.getGraphLayout(), mp.getX(), mp.getY());
    			GraphNode nodeCaptured = visView.getPickSupport().getVertex(visView.getGraphLayout(), mp.getX(), mp.getY());
    			
    			JPopupMenu popupMenu = null;
    			if (nodeCaptured!=null) {
    				popupMenu = this.getJPopupMenu(edgePicked, nodeCaptured, mp);
    			} else {
    				popupMenu = this.getJPopupMenu(edgeCaptured, nodeCaptured, mp);
    			}

    			if (popupMenu!=null) {
    				popupMenu.show(visView, me.getX(), me.getY());
    			}
    		}
    	}
	}

	/**
	 * Returns the JPopupMenu for the specified edge and graph node.
	 *
	 * @param graphEdge the graph edge captured by the mouse position
	 * @param graphNode the graph node captured by the mouse position
	 * @param mousePosition the mouse position
	 * @return the JPopupMenu
	 */
	public JPopupMenu getJPopupMenu(GraphEdge graphEdge, GraphNode graphNode, Point2D mousePosition) {
		
		if (graphEdge==null) return null;
		
		JPopupMenu popupMenu = new JPopupMenu();
		// --- Add the vertex positioning menu item? ----------------
		if (graphNode!=null) {
			popupMenu.add(this.getJMenuItemNodePositioning());
			popupMenu.addSeparator();
		}
		
		// --- Check if the configuration returns menu items --------
		GraphEdgeShapeConfiguration<? extends Shape> shapeConfig = graphEdge.getEdgeShapeConfiguration();
		if (shapeConfig!=null) {
			// --- Set local reminder variables ---------------------
			this.currentShapeConfig = shapeConfig;
			this.currentGraphEdge = graphEdge;
			this.currentGraphNode = graphNode;

			// --- Get JUNG coordinate ------------------------------
			this.currentMousePositionInGraph = this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(mousePosition);
			if (this.getVisualizationViewer().getCoordinateSystemPositionTransformer() instanceof TransformerForGraphNodeGeoPosition) {
				// --- Use geographical coordinates -----------------
				this.currentMousePositionInGraph = this.getVisualizationViewer().getCoordinateSystemPositionTransformer().inverseTransform(this.currentMousePositionInGraph);
			} else {
				// --- The default case -----------------------------
				this.currentMousePositionInGraph = this.getVisualizationViewer().getCoordinateSystemPositionTransformer().apply(this.currentMousePositionInGraph);
			}
			
			// --- Get the type specific menu items -----------------
			shapeConfig.addPopupMenuItems(popupMenu, graphEdge, graphNode);
			// --- Add local ActionListener -------------------------
			Component[] comps = popupMenu.getComponents();
			for (int i = 0; i < comps.length; i++) {
				Component comp = comps[i];
				if (comp instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) comp;
					button.addActionListener(this);
				}
			} 
		}
		
		// --- Add the finalize edit item ---------------------------
		if (popupMenu.getComponentCount()>0) {
			Component lastComponent = popupMenu.getComponent(popupMenu.getComponentCount()-1);
			if (!(lastComponent instanceof JPopupMenu.Separator)) {
				popupMenu.addSeparator();
			}
		}
		popupMenu.add(this.getJMenuItemFinalizeEdit());
		
		return popupMenu;
	}
	
	  /**
     * This method initializes jMenuItemNodePositioning
     * @return javax.swing.JMenuItem
     */
    protected JMenuItem getJMenuItemNodePositioning() {
    	if (jMenuItemNodePositioning==null) {
    		jMenuItemNodePositioning = new JMenuItem(Language.translate("Node Positioning", Language.EN));
    		jMenuItemNodePositioning.setIcon(GraphGlobals.getImageIcon("Positioning.png"));
    		jMenuItemNodePositioning.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					ConfiguredLinePopupPlugin.this.basicGraphGui.editGraphNodePosition(ConfiguredLinePopupPlugin.this.currentGraphNode);
				}
			});
    	}
    	return jMenuItemNodePositioning;
    }
	
	/**
	 * Returns the JMenuItem  to finalize edit.
	 * @return the j menu item finalize edit
	 */
	private JMenuItem getJMenuItemFinalizeEdit() {
		if (jMenuItemFinalizeEdit==null) {
			jMenuItemFinalizeEdit = new JMenuItem(Language.translate("Finalize editing", Language.EN));
			jMenuItemFinalizeEdit.setIcon(GraphGlobals.getImageIcon("EdgeEditFinalize.png"));
			jMenuItemFinalizeEdit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					ConfiguredLinePopupPlugin.this.getGraphEnvironmentController().getNetworkModelUndoManager().setGraphMousePicking();
				}
			});
		}
		return jMenuItemFinalizeEdit;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actionCommand = ae.getActionCommand();
		if (actionCommand==null || actionCommand.isEmpty()) {
			System.err.println("[" + this.getClass().getSimpleName() + "] No action command was provided for the button " + ae.getSource());
		} else {
			this.currentShapeConfig.actionPerformed(ae.getActionCommand(), this, this.currentGraphEdge, this.currentGraphNode, this.currentMousePositionInGraph);
		}
	}
	
}
