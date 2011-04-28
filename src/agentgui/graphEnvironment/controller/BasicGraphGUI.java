package agentgui.graphEnvironment.controller;

import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.graphEnvironment.environmentModel.GraphEdge;
import agentgui.graphEnvironment.environmentModel.GraphElement;
import agentgui.graphEnvironment.environmentModel.GraphNode;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

public class BasicGraphGUI extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel pnlControlls = null;
	private JButton btnZoomIn = null;
	private JButton btnZoomOut = null;
	private JButton btnZoomReset = null;
	private VisualizationViewer<GraphNode, GraphEdge> visView = null;
	private ScalingControl scalingControl = null;
	private Component rightComponent = null;
	
	private GraphEnvironmentControllerGUI parentGUI = null;

	/**
	 * This is the default constructor
	 */
	public BasicGraphGUI(GraphEnvironmentControllerGUI parentGUI) {
		super();
		this.parentGUI = parentGUI;
		scalingControl = new CrossoverScalingControl();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getPnlControlls(), BorderLayout.WEST);
	}

	/**
	 * This method initializes pnlControlls	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlControlls() {
		if (pnlControlls == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.anchor = GridBagConstraints.NORTH;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints1.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 1;
			pnlControlls = new JPanel();
			pnlControlls.setLayout(new GridBagLayout());
			pnlControlls.add(getBtnZoomIn(), gridBagConstraints2);
			pnlControlls.add(getBtnZoomOut(), gridBagConstraints);
			pnlControlls.add(getBtnZoomReset(), gridBagConstraints1);
		}
		return pnlControlls;
	}

	/**
	 * This method initializes btnZoomIn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnZoomIn() {
		if (btnZoomIn == null) {
			btnZoomIn = new JButton();
			btnZoomIn.setPreferredSize(new Dimension(45, 26));
			btnZoomIn.setIcon(new ImageIcon(getClass().getResource(Application.RunInfo.PathImageIntern() + "ListPlus.png")));
			btnZoomIn.setToolTipText(Language.translate("Vergrößern"));
			btnZoomIn.addActionListener(this);
			btnZoomIn.setEnabled(false);
		}
		return btnZoomIn;
	}

	/**
	 * This method initializes btnZoomOut	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnZoomOut() {
		if (btnZoomOut == null) {
			btnZoomOut = new JButton();
			btnZoomOut.setPreferredSize(new Dimension(45, 26));
			btnZoomOut.setIcon(new ImageIcon(getClass().getResource(Application.RunInfo.PathImageIntern() + "ListMinus.png")));
			btnZoomOut.setToolTipText(Language.translate("Verkleinern"));
			btnZoomOut.addActionListener(this);
			btnZoomOut.setEnabled(false);
		}
		return btnZoomOut;
	}

	/**
	 * This method initializes btnReset	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnZoomReset() {
		if (btnZoomReset == null) {
			btnZoomReset = new JButton();
			btnZoomReset.setPreferredSize(new Dimension(45, 26));
			btnZoomReset.setIcon(new ImageIcon(getClass().getResource(Application.RunInfo.PathImageIntern() + "Refresh.png")));
			btnZoomReset.setToolTipText(Language.translate("Zurücksetzen"));
			btnZoomReset.addActionListener(this);
			btnZoomReset.setEnabled(false);
		}
		return btnZoomReset;
	}
	
	VisualizationViewer<GraphNode, GraphEdge> getVisView(){
		return visView;
	}
	
	public void setGraph(Graph<GraphNode, GraphEdge> graph){
		if(graph != null){
			Layout<GraphNode, GraphEdge> layout = new StaticLayout<GraphNode, GraphEdge>(graph);
			layout.setSize(new Dimension(400, 400));
			layout.setInitializer(new Transformer<GraphNode, Point2D>() {

				@Override
				public Point2D transform(GraphNode node) {
					return node.getPosition();
				}
			});
			visView = new VisualizationViewer<GraphNode, GraphEdge>(layout);
			// Node labels
			visView.getRenderContext().setVertexLabelTransformer(new Transformer<GraphNode, String>() {
				
				@Override
				public String transform(GraphNode arg0) {
					return arg0.getId();
				}
			});
//			visView.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
			
			// Edge labels
			visView.getRenderContext().setEdgeLabelTransformer(new Transformer<GraphEdge, String>() {
	
				@Override
				public String transform(GraphEdge arg0) {
					return arg0.getType()+" "+arg0.getId();
				}
			});
			
			visView.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<GraphNode, GraphEdge>());
			
			PluggableGraphMouse pgm = new PluggableGraphMouse();
			pgm.add(new GraphEnvironmentMousePlugin(this));
			visView.setGraphMouse(pgm);
			visView.setBackground(Color.WHITE);
			
			rightComponent = new GraphZoomScrollPane(visView);;
			
			getBtnZoomIn().setEnabled(true);
			getBtnZoomOut().setEnabled(true);
			getBtnZoomReset().setEnabled(true);
		}else{
			rightComponent = new JPanel();
			
			getBtnZoomIn().setEnabled(false);
			getBtnZoomOut().setEnabled(false);
			getBtnZoomReset().setEnabled(false);
		}
		
		this.add(rightComponent, BorderLayout.CENTER);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getBtnZoomIn() && visView != null){
			scalingControl.scale(visView, 1.1f, visView.getCenter());
		}else if(e.getSource() == getBtnZoomOut() && visView != null){
			scalingControl.scale(visView, 1/1.1f, visView.getCenter());
		}else if(e.getSource() == getBtnZoomReset() && visView != null){
			visView.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
			visView.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
		}
		
	}
	
	void handleObjectSelection(Object pickedObject){
		parentGUI.selectObject(pickedObject);
	}
	
	/**
	 * Clears the picked nodes and edges
	 */
	void clearPickedObjects(){
		visView.getPickedVertexState().clear();
		visView.getPickedEdgeState().clear();
	}
	
	/**
	 * Sets a node or edge as picked
	 * @param object The GraphNode or GraphEdge to pick
	 */
	void setPickedObject(GraphElement object){
		if(object instanceof GraphEdge){
			visView.getPickedEdgeState().pick((GraphEdge) object, true);
		}else if(object instanceof GraphNode){
			visView.getPickedVertexState().pick((GraphNode) object, true);
		}
	}
	
	/**
	 * Marks a group of objects as picked
	 * @param objects The objects
	 */
	void setPickedObjects(Vector<GraphElement> objects){
		Iterator<GraphElement> objIter = objects.iterator();
		while(objIter.hasNext()){
			setPickedObject(objIter.next());
		}
	}
	
}
