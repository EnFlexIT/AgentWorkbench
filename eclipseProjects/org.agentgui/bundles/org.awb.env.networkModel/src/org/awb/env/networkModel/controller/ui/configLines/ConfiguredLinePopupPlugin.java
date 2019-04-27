package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGui;

import agentgui.core.application.Language;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * The Class ConfiguredLinePopupPlugin.
 */
public class ConfiguredLinePopupPlugin extends AbstractPopupGraphMousePlugin {

	private BasicGraphGui basicGraphGui = null;
	private GraphEnvironmentController graphController = null;

	private JMenuItem jMenuItemFinalizeEdit;
	
	
	/**
     * Creates a new instance of ConfiguredLinePopupPlugin.
     * @param basicGraphGui the instance of the parent {@link BasicGraphGui}
     */
    public ConfiguredLinePopupPlugin(BasicGraphGui basicGraphGui) {
        super(MouseEvent.BUTTON3_MASK);
        this.basicGraphGui = basicGraphGui;
        this.graphController = this.basicGraphGui.getGraphEnvironmentController();
    }
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin#handlePopup(java.awt.event.MouseEvent)
	 */
	@Override
	protected void handlePopup(MouseEvent me) {

		Point2D p = me.getPoint();
    	
    	// -- Check if an edge is selected ----------------------
		VisualizationViewer<GraphNode,GraphEdge> visView = this.basicGraphGui.getVisualizationViewer();
    	Set<GraphEdge> edgesPicked = visView.getPickedEdgeState().getPicked();
    	if (edgesPicked.size()==1) {
    		GraphEdge edge = edgesPicked.iterator().next();
    		if (edge!=null) {
    			// --- Additionally, check for a selected node -- 
    			GraphNode node = (GraphNode) visView.getPickSupport().getVertex(visView.getGraphLayout(), p.getX(), p.getY());
    			this.getJPopupMenu(edge, node).show(visView, me.getX(), me.getY());
    		}
    	}
    	
		
	}

	/**
	 * Return the JPopUp menu.
	 *
	 * @param graphEdge the graph edge
	 * @param graphNode the graph node
	 * @return the j pop up menu
	 */
	public JPopupMenu getJPopupMenu(GraphEdge graphEdge, GraphNode graphNode) {
		
		JPopupMenu popupMenu = new JPopupMenu();
		
		// --- Check if the configuration returns menu items --------
		GraphEdgeShapeConfiguration<? extends Shape> shapeConfig = graphEdge.getEdgeShapeConfiguration();
		if (shapeConfig!=null) {
			
			
		}
		
		// --- Add the finalize edit item ---------------------------
		if (popupMenu.getComponentCount()>0) {
			popupMenu.addSeparator();
		}
		popupMenu.add(this.getJMenuItemFinalizeEdit());
		
		return popupMenu;
	}
	
	/**
	 * Returns the JMenuItem  to finalize edit.
	 * @return the j menu item finalize edit
	 */
	private JMenuItem getJMenuItemFinalizeEdit() {
		if (jMenuItemFinalizeEdit==null) {
			jMenuItemFinalizeEdit = new JMenuItem(Language.translate("Finalize edit", Language.EN));
			jMenuItemFinalizeEdit.setIcon(GraphGlobals.getImageIcon("EdgeEditFinalize.png"));
			jMenuItemFinalizeEdit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					ConfiguredLinePopupPlugin.this.graphController.getNetworkModelUndoManager().setGraphMousePicking();
				}
			});
		}
		return jMenuItemFinalizeEdit;
	}
	
	
}
