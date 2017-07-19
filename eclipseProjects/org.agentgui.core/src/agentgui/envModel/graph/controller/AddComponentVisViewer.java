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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.border.EtchedBorder;

import org.apache.commons.collections15.Transformer;

import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.components.ComponentTypeListElement;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.prototypes.DistributionNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Checkmark;

/**
 * The Class AddComponentVisViewer defines the extended JUNG VisualizationViewer for
 * the {@link AddComponentDialog}.
 *
 * @param <V> the value type
 * @param <E> the element type
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AddComponentVisViewer<V, E> extends VisualizationViewer<GraphNode, GraphEdge> {
	
	private static final long serialVersionUID = 471389789905002474L;

	private GraphEnvironmentController graphController = null;
	private ComponentTypeListElement localComponentTypeListElement=null;
	private DomainSettings localDomainSetings = null;
	
	/**
	 * Instantiates a new VisualizationViewer for the AddComponentDialog.
	 * @param layout the layout
	 */
	public AddComponentVisViewer(Layout<GraphNode, GraphEdge> layout) {
		super(layout);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the AddComponentDialog.
	 * @param layout the layout
	 * @param preferredSize the preferred size
	 */
	public AddComponentVisViewer(Layout<GraphNode, GraphEdge> layout, Dimension preferredSize) {
		super(layout, preferredSize);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the AddComponentDialog.
	 * @param model the model
	 */
	public AddComponentVisViewer(VisualizationModel<GraphNode, GraphEdge> model) {
		super(model);
		this.initialize();
	}
	/**
	 * Instantiates a new VisualizationViewer for the AddComponentDialog.
	 * @param model the model
	 * @param preferredSize the preferred size
	 */
	public AddComponentVisViewer(VisualizationModel<GraphNode, GraphEdge>  model, Dimension preferredSize) {
		super(model, preferredSize);
		this.initialize();
	}

	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		// --- Define the GraphMouse --------
		PluggableGraphMouse pgm = new PluggableGraphMouse();
	    pgm.add(new PickingGraphMousePlugin<GraphNode, GraphEdge>());
		
	    this.setGraphMouse(pgm);
	    this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	    this.setBackground(Color.WHITE);
	    this.setSize(new Dimension(150, 150));
	    this.setPreferredSize(new Dimension(250, 250));
	    
	    // --- Define a movement for the nodes ---------------------------- 
//		MutableTransformer mutableLayout = visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
//		mutableLayout.translate(120, 10);

		// --- Configure the vertex shape and size ------------------------
	    this.getRenderContext().setVertexShapeTransformer(new VertexShapeSizeAspect<GraphNode, GraphEdge>());
		
		// --- Configure node icons, if configured ------------------------		
	    this.getRenderContext().setVertexIconTransformer(new Transformer<GraphNode, Icon>(){
			
			@Override
			public Icon transform(GraphNode node) {
				
				boolean picked = getPickedVertexState().isPicked(node);
				Icon icon = null;
				if (isCurrentComponentDistributionNode()) {
					
					String nodeImage = localComponentTypeListElement.getComponentTypeSettings().getEdgeImage();
					if (nodeImage!=null) {
						if (nodeImage.equals("MissingIcon")==false) {
							// --- Icon reference found --- Start ---
							LayeredIcon layeredIcon = null;
							ImageIcon imageIcon = GraphGlobals.getImageIcon(nodeImage);
							if (imageIcon!=null) {
								layeredIcon = new LayeredIcon(imageIcon.getImage());
								if (layeredIcon!=null && picked==true){
									String checkColor = localDomainSetings.getVertexColorPicked();
									Checkmark checkmark = new Checkmark(new Color(Integer.parseInt(checkColor)));
									layeredIcon.add(checkmark);
								}
							} else {
								System.err.println("Could not find node image for '" + localComponentTypeListElement.getComponentName() + "'");
							}
							icon = layeredIcon;	
							// --- Icon reference found --- End -----	
						}
					}
				}
				return icon;
			}
			
		});
		
		// --- Set tool tip for nodes -------------------------------------
	    this.setVertexToolTipTransformer(new Transformer<GraphNode, String>() {
			@Override
			public String transform(GraphNode edge) {
				return edge.getId();
			}
		});

		// --- Configure vertex colors ------------------------------------
	    this.getRenderContext().setVertexFillPaintTransformer(new Transformer<GraphNode, Paint>() {
			@Override
			public Paint transform(GraphNode node) {

				String colorString = null;
				String colorStringDefault = localDomainSetings.getVertexColor();
				Color defaultColor = new Color(Integer.parseInt(colorStringDefault));

				// --- Get color from component type settings -----
				try {
					// --- Get the vertex size from the component type settings -
					if (isCurrentComponentDistributionNode()) {
						// --- Distribution node ----------------
						if (getPickedVertexState().isPicked(node) == true) {
							colorString = localDomainSetings.getVertexColorPicked();
						} else {
							colorString = localComponentTypeListElement.getComponentTypeSettings().getColor();	
						}
					} else {
						if (getPickedVertexState().isPicked(node) == true) {
							colorString = localDomainSetings.getVertexColorPicked();
						} else {
							colorString = localDomainSetings.getVertexColor();
						}
					}
					if (colorString != null) {
						return new Color(Integer.parseInt(colorString));
					}
					return defaultColor;

				} catch (NullPointerException ex) {
					ex.printStackTrace();
					return defaultColor;
				}
			}
		}); // end transformer
				
		// --- Configure to show node labels ------------------------------
	    this.getRenderContext().setVertexLabelTransformer(new Transformer<GraphNode, String>() {
				@Override
				public String transform(GraphNode node) {
					if (isCurrentComponentDistributionNode()) {
						if (localComponentTypeListElement.getComponentTypeSettings().isShowLabel()) {
							return node.getId();
						}
					} else {
						if (localDomainSetings.isShowLabel()) {
							return node.getId();
						}
					}
					return null;
				}
			} // end transformer
		);

		// --- Configure edge colors --------------------------------------
	    this.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<GraphEdge, Paint>() {
			@Override
			public Paint transform(GraphEdge edge) {
				if (getPickedEdgeState().isPicked(edge)) {
					return GeneralGraphSettings4MAS.DEFAULT_EDGE_PICKED_COLOR;
				}

				try {
					String colorString = localComponentTypeListElement.getComponentTypeSettings().getColor();
					if (colorString != null) {
						return new Color(Integer.parseInt(colorString));
					}

				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
				return GeneralGraphSettings4MAS.DEFAULT_EDGE_COLOR;
			}
		});
		// --- Configure Edge Image Labels --------------------------------
	    this.getRenderContext().setEdgeLabelTransformer(new Transformer<GraphEdge, String>() {
			@Override
			public String transform(GraphEdge edge) {
				// Get the path of the Image from the component type settings
				String textDisplay = "";
				try {
					String edgeImage = localComponentTypeListElement.getComponentTypeSettings().getEdgeImage();
					boolean showLabel = localComponentTypeListElement.getComponentTypeSettings().isShowLabel();

					if (showLabel) {
						textDisplay = edge.getId();
					}

					if (edgeImage != null) {
						URL url = getClass().getResource(edgeImage);
						if (url != null) {
							if (showLabel) {
								textDisplay = "<html><center>" + textDisplay + "<br><img src='" + url + "'></center></html>";
							} else {
								textDisplay = "<html><center><img src='" + url + "'></center></html>";
							}
						}
					}
					return textDisplay;

				} catch (NullPointerException ex) {
					ex.printStackTrace();
					return edge.getId();
				}
			}
		});
		// --- Configure edge label position ------------------------------
	    this.getRenderContext().setLabelOffset(0);
	    this.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<GraphNode, GraphEdge>(.5, .5));

		// --- Use straight lines as edges --------------------------------
	    this.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<GraphNode, GraphEdge>());

		// --- Set edge width ---------------------------------------------
	    this.getRenderContext().setEdgeStrokeTransformer(new Transformer<GraphEdge, Stroke>() {
			@Override
			public Stroke transform(GraphEdge edge) {
				float edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
				try {
					edgeWidth = localComponentTypeListElement.getComponentTypeSettings().getEdgeWidth();
					if (edgeWidth == 0) {
						edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
					}

				} catch (Exception e) {
					e.printStackTrace();
					edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
				}
				return new BasicStroke(edgeWidth);
			}
		});
		
	}
	
	/**
     * Checks if the current selection is a DistributionNode.
     * @return true, if it is a DistributionNode
     */
    private boolean isCurrentComponentDistributionNode() {
    	if (localComponentTypeListElement==null) {
    		return false;
    	} else {
    		return localComponentTypeListElement.getComponentTypeSettings().getGraphPrototype().equals(DistributionNode.class.getName());	
    	}
    }
    
	/**
	 * Sets the GraphEnvironmentController.
	 * @param graphController the graphController to set
	 */
	public void setGraphController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	/**
	 * Sets the current ComponentTypeListElement.
	 * @param newComponentTypeListElement the localComponentTypeListElement to set
	 */
	public void setComponentTypeListElement(ComponentTypeListElement newComponentTypeListElement) {
		this.localComponentTypeListElement = newComponentTypeListElement;
	}
	/**
	 * Sets the DomainSettings.
	 * @param newDomainSetings the new DomainSettings
	 */
	public void setDomainSetings(DomainSettings newDomainSetings) {
		this.localDomainSetings = newDomainSetings;
	}

	
	// --- Begin sub class ----------------------
    /**
	 * Controls the shape, size, and aspect ratio for each vertex.
	 * 
	 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	@SuppressWarnings("hiding")
	private final class VertexShapeSizeAspect<V, E> extends AbstractVertexShapeTransformer<GraphNode> implements Transformer<GraphNode, Shape> {

		/** Instantiates a new vertex shape size aspect. */
		public VertexShapeSizeAspect() {

			this.setSizeTransformer(new Transformer<GraphNode, Integer>() {
				@Override
				public Integer transform(GraphNode node) {

					Integer size = graphController.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME).getVertexSize();
					Integer sizeSettings = null;
					try {
						if (isCurrentComponentDistributionNode()) {
							// --- DistributionNode: get size from ComponentTypeSettings - Start --
							sizeSettings = (int) localComponentTypeListElement.getComponentTypeSettings().getEdgeWidth();
						} else {
							// --- Normal node or ClusterNode ---------------------------- Start --
							sizeSettings = localDomainSetings.getVertexSize();
						}
						if (sizeSettings != null) {
							size = sizeSettings;
						}

					} catch (NullPointerException ex) {
						System.err.println("Invalid vertex size");
						ex.printStackTrace();
					}
					return size;
				}
			});

		}// end constructor

		/*
		 * (non-Javadoc)
		 * @see org.apache.commons.collections15.Transformer#transform(java.lang.Object)
		 */
		@Override
		public Shape transform(GraphNode node) {
			return factory.getEllipse(node); // DEFAULT;
		}
	}
    // --- End sub class ------------------------

}
