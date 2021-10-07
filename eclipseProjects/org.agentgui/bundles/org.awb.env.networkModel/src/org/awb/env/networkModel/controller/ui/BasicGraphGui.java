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
package org.awb.env.networkModel.controller.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;

import org.apache.commons.collections15.Transformer;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphElementLayout;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.adapter.NetworkComponentToGraphNodeAdapter;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.GraphSelectionListener.GraphSelection;
import org.awb.env.networkModel.controller.ui.commands.RenamedNetworkComponent;
import org.awb.env.networkModel.controller.ui.configLines.ConfiguredLineMousePlugin;
import org.awb.env.networkModel.controller.ui.configLines.ConfiguredLinePopupPlugin;
import org.awb.env.networkModel.controller.ui.configLines.IntermediatePointTransformer;
import org.awb.env.networkModel.helper.GraphEdgeShapeTransformer;
import org.awb.env.networkModel.maps.MapService;
import org.awb.env.networkModel.maps.MapSettings;
import org.awb.env.networkModel.positioning.GraphNodePositionDialog;
import org.awb.env.networkModel.settings.ComponentTypeSettings;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;
import org.awb.env.networkModel.settings.LayoutSettings;

import agentgui.core.application.Application;
import de.enflexit.common.swing.imageFileSelection.ConfigurableFileFilter;
import de.enflexit.common.swing.imageFileSelection.ImageFileView;
import de.enflexit.common.swing.imageFileSelection.ImagePreview;
import de.enflexit.common.swing.imageFileSelection.ImageUtils;
import de.enflexit.geography.coordinates.AbstractCoordinate;
import de.enflexit.geography.coordinates.AbstractGeoCoordinate;
import de.enflexit.geography.coordinates.UTMCoordinate;
import de.enflexit.geography.coordinates.WGS84LatLngCoordinate;
import de.enflexit.geography.coordinates.ui.GeoCoordinateDialog;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Checkmark;

/**
 * This class implements a GUI component for displaying visualizations for JUNG graphs. <br>
 * This class also has a toolbar component which provides various features for editing, importing and interacting with the graph.
 * 
 * @see GraphEnvironmentControllerGUI
 * @see GraphEnvironmentMousePlugin
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BasicGraphGui extends JPanel implements Observer {

	private static final long serialVersionUID = 5764679914667183305L;
	
	public static double graphMargin = 25; // TODO not exact if WGS84 coordinates are used
	
	/**
	 * The enumeration ToolBarType describes the toolbar type available in the {@link BasicGraphGui}.
	 */
	public enum ToolBarType {
		EditControl,
		LayoutControl,
		ViewControl
	}
	/**
	 * The enumeration ToolBarSurrounding describes, if a customised toolbar button is to be shown during configuration or during runtime.
	 */
	public enum ToolBarSurrounding {
		Both,
		ConfigurationOnly,
		RuntimeOnly
	}
	/**
	 * The enumeration that describes a possible GraphMouseMode.
	 */
	public enum GraphMouseMode {
		Picking,
		Transforming,
		EdgeEditing
	}
	
	/** Environment model controller, to be passed by the parent GUI. */
	private GraphEnvironmentController graphController;
	
	/** The GUI's main component, either the graph visualization, or an empty JPanel if no graph is loaded */
	private GraphZoomScrollPane graphZoomScrollPane;
	
	/** The ToolBar for this component */
	private BasicGraphGuiTools graphGuiTools;
	private JPanel jPanelToolBarsWest;
	private JPanel jPanelToolBarsEast;
	
	/** Graph visualization component */
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visView;
	private boolean isCreatedVisualizationViewer;
	private ItemListener pickedStateItemListener;
	private List<GraphSelectionListener> graphSelectionListenerList;
	
	private SatelliteDialog satelliteDialog;
	private SatelliteVisualizationViewer<GraphNode, GraphEdge> visViewSatellite;
	
	/** The current GraphMouseMode */
	private GraphMouseMode graphMouseMode;
	
	private PluggableGraphMouse graphMouse4Picking; 
	private GraphEnvironmentMousePlugin graphEnvironmentMousePlugin;
	private GraphEnvironmentPopupPlugin<GraphNode, GraphEdge> graphEnvironmentPopupPlugin;

	private PluggableGraphMouse graphMouse4EdgeEditing;
	private DefaultModalGraphMouse<GraphNode, GraphEdge> graphMouse4Transforming;
	
	private Timer graphSelectionWaitTimer;

	/** The current ZoomController */
	private ZoomController zoomController;
	
	
	/**
	 * This is the default constructor
	 * @param controller The Graph Environment controller
	 */
	public BasicGraphGui(GraphEnvironmentController controller) {
		this.graphController = controller;
		this.graphController.addObserver(this);
		this.initialize();
		this.reLoadGraph();
	}
	/**
	 * This method initializes this
	 */
	private void initialize() {

		// --- Set appearance -----------------------------
		this.setVisible(true);
		this.setSize(300, 300);
		this.setLayout(new BorderLayout());
		this.setDoubleBuffered(true);
		
		// --- Add components -----------------------------
		this.add(this.getJPanelToolBarsWest(), BorderLayout.WEST);
		this.add(this.getGraphZoomScrollPane(), BorderLayout.CENTER);
		this.add(this.getJPanelToolBarsEast(), BorderLayout.EAST);
		
		// --- Initialize further elements ----------------
		this.getGraphMouse4EdgeEditing();
	}
	/**
	 * Dispose this panel.
	 */
	public void dispose() {
		this.disposeSatelliteView();
		this.visView = null;
	}
	
	
	/**
	 * Gets the graph environment controller.
	 * @return the controller
	 */
	public GraphEnvironmentController getGraphEnvironmentController() {
		return this.graphController;
	}
	/**
	 * Returns the instance of the BasicGraphGuiTools.
	 * @return the BasicGraphGuiTools
	 */
	private BasicGraphGuiTools getBasicGraphGuiTools() {
		if (graphGuiTools==null) {
			graphGuiTools = new BasicGraphGuiTools(this.graphController);
		}
		return graphGuiTools;
	}
	/**
	 * Returns the specified JToolBar of the {@link BasicGraphGui}.
	 *
	 * @param toolBarType the tool bar type
	 * @return the JToolBar
	 */
	public JToolBar getJToolBar(ToolBarType toolBarType) {
		JToolBar toolBar = null;
		switch (toolBarType) {
		case EditControl:
			toolBar = this.getBasicGraphGuiTools().getJToolBarEdit();
			break;
		case ViewControl:
			toolBar = this.getBasicGraphGuiTools().getJToolBarView();
			break;
		case LayoutControl:
			toolBar = this.getBasicGraphGuiTools().getJToolBarLayout();
			break;
		}
		return toolBar;
	}
	
	/**
	 * Returns the JPanel with the tool bars for the west-side.
	 * @return the jPanel tool bars west-side
	 */
	private JPanel getJPanelToolBarsWest() {
		if (jPanelToolBarsWest==null) {
			jPanelToolBarsWest = new JPanel();
			jPanelToolBarsWest.setLayout(new BoxLayout(jPanelToolBarsWest, BoxLayout.X_AXIS));
			jPanelToolBarsWest.add(this.getBasicGraphGuiTools().getJToolBarView(), null);
			// --- In case of editing the simulation setup ----------
		    if (this.graphController.getProject()!=null) {
		    	jPanelToolBarsWest.add(this.getBasicGraphGuiTools().getJToolBarEdit(), null);
		    }
		}
		return jPanelToolBarsWest;
	}
	/**
	 * Returns the JPanel with the tool bars for the east-side.
	 * @return the jPanel tool bars east-side
	 */
	private JPanel getJPanelToolBarsEast() {
		if (jPanelToolBarsEast==null) {
			jPanelToolBarsEast = new JPanel();
			jPanelToolBarsEast.setLayout(new BoxLayout(jPanelToolBarsEast, BoxLayout.X_AXIS));
			jPanelToolBarsEast.add(this.getBasicGraphGuiTools().getJToolBarLayout());
		}
		return jPanelToolBarsEast;
	}
	

	/**
	 * Returns the current graph mouse mode.
	 * @return the graph mouse mode
	 */
	public GraphMouseMode getGraphMouseMode() {
		if (graphMouseMode==null) {
			graphMouseMode = GraphMouseMode.Picking;
		}
		return graphMouseMode;
	}
	/**
	 * Sets the graph mouse mode.
	 * @param newGraphMouseMode the new graph mouse mode
	 */
	private void setGraphMouseMode(GraphMouseMode newGraphMouseMode) {
		
		if (newGraphMouseMode==null || newGraphMouseMode==this.graphMouseMode) return;
		
		this.graphMouseMode = newGraphMouseMode;

		PluggableGraphMouse graphMouse = null;
		switch (newGraphMouseMode) {
		case Picking:
			graphMouse = this.getGraphMouse4Picking();
			break;
		case Transforming:
			graphMouse = this.getGraphMouse4Transforming();
			break;
		case EdgeEditing:
			graphMouse = this.getGraphMouse4EdgeEditing();
			break;
		}
		this.getVisualizationViewer().setGraphMouse(graphMouse);
	}
	
	
	/**
	 * Returns the PluggableGraphMouse that is used in the 'Picking' mode of the graph..
	 * @return the graphMouse4Picking
	 */
	private PluggableGraphMouse getGraphMouse4Picking() {
		if (graphMouse4Picking==null) {
			graphMouse4Picking = new PluggableGraphMouse();
			graphMouse4Picking.add(this.getGraphEnvironmentMousePlugin());
			graphMouse4Picking.add(this.getGraphEnvironmentPopupPlugin());
		}
		return graphMouse4Picking;
	}
	/**
	 * Returns the {@link GraphEnvironmentMousePlugin}
	 * @return the graph environment mouse plugin
	 */
	private GraphEnvironmentMousePlugin getGraphEnvironmentMousePlugin() {
		if (graphEnvironmentMousePlugin==null) {
			graphEnvironmentMousePlugin = new GraphEnvironmentMousePlugin(this);
		}	
		return graphEnvironmentMousePlugin;
	}
	/**
	 * Returns the GraphEnvironmentPopupPlugin.
	 * @return the graph environment popup plugin
	 */
	private GraphEnvironmentPopupPlugin<GraphNode, GraphEdge> getGraphEnvironmentPopupPlugin() {
		if (graphEnvironmentPopupPlugin==null) {
			graphEnvironmentPopupPlugin = new GraphEnvironmentPopupPlugin<GraphNode, GraphEdge>(this);
			graphEnvironmentPopupPlugin.setEdgePopup(this.getBasicGraphGuiTools().getEdgePopup());
			graphEnvironmentPopupPlugin.setVertexPopup(this.getBasicGraphGuiTools().getVertexPopup());
		}
		return graphEnvironmentPopupPlugin;
	}
	
	/**
	 * Return the DefaultModalGraphMouse that is used in the 'Transforming' mode of the graph.
	 * @return the default modal graph mouse
	 */
	private DefaultModalGraphMouse<GraphNode, GraphEdge> getGraphMouse4Transforming() {
		if (graphMouse4Transforming == null) {
			graphMouse4Transforming = new DefaultModalGraphMouse<GraphNode, GraphEdge>();
		}
		return graphMouse4Transforming;
	}

	
	/**
	 * Returns the PluggableGraphMouse that is used in the 'Edge editing' mode of the graph..
	 * @return the graphMouse4Picking
	 */
	private PluggableGraphMouse getGraphMouse4EdgeEditing() {
		if (graphMouse4EdgeEditing==null) {
			graphMouse4EdgeEditing = new PluggableGraphMouse();
			graphMouse4EdgeEditing.add(new ConfiguredLineMousePlugin(this));
			graphMouse4EdgeEditing.add(new ConfiguredLinePopupPlugin(this));
		}
		return graphMouse4EdgeEditing;
	}

	/**
	 * This method assigns the current graph to a VisualizationViewer to display it. 
	 */
	private void reLoadGraph() {

		// --- Display the current Graph ------------------
		this.getVisualizationViewer().setGraphLayout(this.getNewGraphLayout());
		this.clearPickedObjects();
		this.setEdgeShapeTransformer();
		this.setMapPreRendering();
		this.getZoomController().zoomToFitToWindow();

		// --- Re-attach item listener for vis-viewer -----
		this.getVisualizationViewer().getPickedVertexState().removeItemListener(this.getPickedStateItemListener());
		this.getVisualizationViewer().getPickedVertexState().addItemListener(this.getPickedStateItemListener());
		this.getVisualizationViewer().getPickedEdgeState().removeItemListener(this.getPickedStateItemListener());
		this.getVisualizationViewer().getPickedEdgeState().addItemListener(this.getPickedStateItemListener());
		
		// --- Adjust the satellite view ------------------
		this.getSatelliteVisualizationViewer().getGraphLayout().setGraph(this.getGraph());
		this.zoomSatelliteVisualizationViewerToFitToWindow();
		
	}
	/**
	 * Gets the picked state item listener, a listener 
	 * for GraphNode / GraphEdge (de)selections.  
	 *
	 * @return the picked state item listener
	 */
	public ItemListener getPickedStateItemListener() {
		if (pickedStateItemListener==null) {
			pickedStateItemListener = new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent ie) {
					Object item = ie.getItem();
					if (item instanceof GraphNode || item instanceof GraphNode) {
						BasicGraphGui.this.reStartGraphSelectionWaitTimer();
					}
				}
			};
		}
		return pickedStateItemListener;
	}
	
	
	/**
	 * Gets the current Graph and repaints the visualization viewer.
	 */
	private void repaintGraph() {
		Graph<GraphNode, GraphEdge> graph = this.getGraph();
		if (graph!=null && this.getVisualizationViewer().getGraphLayout().getGraph()!=graph) {
			this.getVisualizationViewer().getGraphLayout().setGraph(graph);
		}
		this.getVisualizationViewer().repaint();
	}
	
	/**
	 * Returns the current graph.
	 * @return the graph
	 */
	private Graph<GraphNode, GraphEdge> getGraph() {
		return this.graphController.getNetworkModel().getGraph();
	}
	
	/**
	 * Returns the graph layout for the visualization viewer.
	 * @return the graph layout
	 */
	private Layout<GraphNode, GraphEdge> getNewGraphLayout() {

		Layout<GraphNode, GraphEdge> layout = null;
		if (this.graphController.getNetworkModel().getLayoutSettings().isGeographicalLayout()==false) {
			// --- Uses the standard layout, where the GraphNode coordinates are used as xy-values for the positioning ----------
			layout = new BasicGraphGuiStaticLayout(this.graphController, this.getGraph());
		} else {
			// --- Use the layout that considers the geographical coordinates of GraphNodes in an UTM system --------------------
			layout = new BasicGraphGuiStaticGeoLayout(this.graphController, this.getGraph());
		}
		return layout;
	}
	
	/**
	 * Returns the current {@link BasicGraphGuiStaticLayout} that is used in the current visualization viewer.
	 * @return the BasicGraphGuiStaticLayout or <code>null</code>
	 */
	public BasicGraphGuiStaticLayout getBasicGraphGuiStaticLayout() {
		return this.getVisualizationViewer().getBasicGraphGuiStaticLayout();
	}
	/**
	 * Returns the position transformer that considers the directions of the defined coordinate system.
	 * @return the TransformerForGraphNodePosition
	 */
	public TransformerForGraphNodePosition getCoordinateSystemPositionTransformer() {
		return this.getVisualizationViewer().getCoordinateSystemPositionTransformer();
	}
	
	
	/**
	 * Gets the graph zoom scroll pane.
	 * @return the graph zoom scroll pane
	 */
	public GraphZoomScrollPane getGraphZoomScrollPane() {
		if (graphZoomScrollPane==null) {
			graphZoomScrollPane = new GraphZoomScrollPane(this.getVisualizationViewer());
		}
		return graphZoomScrollPane;
	}
	/**
	 * Gets the VisualizationViewer
	 * @return The VisualizationViewer
	 */
	public BasicGraphGuiVisViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		
		if (visView==null) {
			// ----------------------------------------------------------------
			// --- Define graph layout ----------------------------------------
			// ----------------------------------------------------------------
			Layout<GraphNode, GraphEdge> layout = this.getNewGraphLayout();
			
			// ----------------------------------------------------------------
			// --- Create a new VisualizationViewer instance ------------------
			// ----------------------------------------------------------------
			visView = new BasicGraphGuiVisViewer<GraphNode, GraphEdge>(layout);
			visView.setBackground(Color.WHITE);
			visView.setDoubleBuffered(true);
			
			// --- Configure mouse and key interaction ------------------------
			visView.setGraphMouse(this.getGraphMouse4Picking());
			visView.addKeyListener(this.getGraphEnvironmentMousePlugin());
			
			// --- Set the pick size of the visualization viewer --------------
			visView.setPickSupport(new GraphEnvironmentShapePickSupport(visView, 5));
			
			// --- Add an item listener for pickings --------------------------
			visView.getPickedVertexState().addItemListener(this.getPickedStateItemListener());
			visView.getPickedEdgeState().addItemListener(this.getPickedStateItemListener());

			
			// ----------------------------------------------------------------
			// --- Define edge and node ToolTip --------------------- Start ---
			// ----------------------------------------------------------------
			
			// --- Edge -------------------------------------------------------
			visView.setVertexToolTipTransformer(new Transformer<GraphNode, String>() {
				@Override
				public String transform(GraphNode node) {
					
					List<NetworkComponent> netCompsAtNode = graphController.getNetworkModel().getNetworkComponents(node);
					NetworkComponent netCompDisNode =  graphController.getNetworkModel().getDistributionNode(netCompsAtNode);
					
					// --- Generally define the toolTip -----------------------
					String toolTip = "<html><b>GraphNode: " + node.getId() + "</b>";
					if (netCompDisNode!=null) {
						// --- In case of a distribution node -----------------
						String type = netCompDisNode.getType();
						String domain = "unknown";
						String isAgent = "?";
						ComponentTypeSettings cts = graphController.getGeneralGraphSettings4MAS().getCurrentCTS().get(type);
						if (cts!=null) {
							domain = cts.getDomain();
							if (cts.getAgentClass()==null) {
								isAgent = "No";
							} else {
								isAgent = "Yes";
							}
						}
						// --- Define the ToolTip -----------------------------
						toolTip += "<br><b>NetworkComponent: " + netCompDisNode.getId() + "</b><br>(Domain: " + domain + ", Type: " + type + ", isAgent: " + isAgent + ")";  
					}
					
					// --- Show position in edit mode only --------------------
					if (graphController.getProject()!=null) {
						toolTip += "<br>(" + node.getCoordinate().toString() + ")";
					}
					toolTip += "</html>";
					return toolTip;
				}
			});
			
			// --- Edge -------------------------------------------------------
			visView.setEdgeToolTipTransformer(new Transformer<GraphEdge, String>() {
				@Override
				public String transform(GraphEdge edge) {
					String toolTip = null;
					NetworkComponent netComp = graphController.getNetworkModel().getNetworkComponent(edge);
					if (netComp!=null) {
						String type = netComp.getType();
						String domain = "unknown";
						String isAgent = "?";
						ComponentTypeSettings cts = graphController.getGeneralGraphSettings4MAS().getCurrentCTS().get(type);
						if (cts!=null) {
							domain = cts.getDomain();
							if (cts.getAgentClass()==null) {
								isAgent = "No";
							} else {
								isAgent = "Yes";
							}
						}
						// --- Define the ToolTip -----------------------------
						toolTip = "<html>";
						toolTip += "<b>NetworkComponent: " + netComp.getId() + "</b><br>(Domain: " + domain + ", Type: " + type + ", isAgent: " + isAgent + ")";  
						toolTip += "</html>";
					}
					return toolTip;
				}
			});
			// ----------------------------------------------------------------
			// --- Define edge and node ToolTip ------------------------ End---
			// ----------------------------------------------------------------

			// ----------------------------------------------------------------
			// --- Node configurations ------------------------------ Start ---
			// ----------------------------------------------------------------
			
			// --- Configure the node shape and size --------------------------
			visView.getRenderContext().setVertexShapeTransformer(new TransformerForVertexShape<GraphNode, GraphEdge>(this.getGraphEnvironmentController()));
			
			// --- Configure node icons, if configured ------------------------
			visView.getRenderContext().setVertexIconTransformer(new Transformer<GraphNode, Icon>() {

				private final String pickedPostfix = "[picked]";
				private HashMap<String, LayeredIcon> iconHash = new HashMap<String, LayeredIcon>();

				@Override
				public Icon transform(GraphNode node) {

					Icon icon = null;
					boolean isPicked = visView.getPickedVertexState().isPicked(node);
					
					GraphElementLayout nodeLayout = node.getGraphElementLayout(graphController.getNetworkModel());
					String nodeImagePath = nodeLayout.getImageReference();
					if (nodeImagePath!=null && nodeImagePath.equals("MissingIcon")==false) {

						// --- 1. Search in the local Hash ----------
						Color currentColor = nodeLayout.getColor();
						String colorPostfix = "[" + currentColor.getRGB() + "]";
						int scaleMultiplier = this.getScaleMultiplier();
						
						// --- 2. If necessary, load the image ------
						String iconHashKey = scaleMultiplier + "|" + nodeImagePath + colorPostfix;
						String iconHashKeyPicked = iconHashKey + this.pickedPostfix;
						LayeredIcon layeredIcon = this.iconHash.get(isPicked==false ? iconHashKey : iconHashKeyPicked);
						if (layeredIcon == null) {
							// --- Load the image icon --------------
							ImageIcon imageIcon = GraphGlobals.getImageIcon(nodeImagePath);
							// --- Prepare the image -------------
							BufferedImage bufferedImage;
							if (currentColor.equals(Color.WHITE) || currentColor.equals(Color.BLACK)) {
								// --- If the color is set to black or white, just use the unchanged image ----------
								bufferedImage = GraphGlobals.convertToBufferedImage(imageIcon.getImage());
							} else {
								// --- Otherwise, replace the defined basic color with the one specified in the node layout ---------
								bufferedImage = GraphGlobals.exchangeColor(GraphGlobals.convertToBufferedImage(imageIcon.getImage()), GeneralGraphSettings4MAS.IMAGE_ICON_COLORIZE_BASE_COLOR, currentColor);
							}
							
							if (bufferedImage != null) {
								// --- Scale the image icon? --------
								if (scaleMultiplier>1) {
									bufferedImage = GraphGlobals.scaleBufferedImage(bufferedImage, scaleMultiplier);
								}
								// --- 3. Remind the images ---------
								LayeredIcon layeredIconUnPicked = new LayeredIcon(bufferedImage);
								this.iconHash.put(iconHashKey, layeredIconUnPicked);

								LayeredIcon layeredIconPicked = new LayeredIcon(bufferedImage);
								layeredIconPicked.add(new Checkmark(nodeLayout.getColorPicked()));
								this.iconHash.put(iconHashKeyPicked, layeredIconPicked);

								// --- 4. Return the right one --
								if (isPicked == true) {
									layeredIcon = layeredIconPicked;
								} else {
									layeredIcon = layeredIconUnPicked;
								}
							}
						}
						icon = layeredIcon;
					}
					return icon;
				}
				/**
				 * Returns the scale multiplier that is used during a map visualization.
				 * @return the scale multiplier
				 */
				private int getScaleMultiplier() {
					int scaleMultiplier = 1;
					MapSettings ms = BasicGraphGui.this.graphController.getNetworkModel().getMapSettings();
					if (ms!=null) {
						scaleMultiplier = ms.getMapScale().getScaleMultiplier();
					}
					return scaleMultiplier;
				}
			});

			// --- Configure node colors --------------------------------------
			visView.getRenderContext().setVertexFillPaintTransformer(new Transformer<GraphNode, Paint>() {
				@Override
				public Paint transform(GraphNode node) {
					if (visView.getPickedVertexState().isPicked(node)) {
						return node.getGraphElementLayout(graphController.getNetworkModel()).getColorPicked();
					} 
					return node.getGraphElementLayout(graphController.getNetworkModel()).getColor();
				}
			});

			// --- Configure to show node labels ------------------------------
			visView.getRenderContext().setVertexLabelTransformer(new Transformer<GraphNode, String>() {
				@Override
				public String transform(GraphNode node) {
					if (node.getGraphElementLayout(graphController.getNetworkModel()).isShowLabel()==true) {
						return node.getGraphElementLayout(graphController.getNetworkModel()).getLabelText();
					}
					return null;
				}
			});
			
			// ----------------------------------------------------------------
			// --- Edge configurations ------------------------------ Start ---
			// ----------------------------------------------------------------
			
			// --- Configure edge label position ------------------------------
			visView.getRenderContext().setLabelOffset(6);
			visView.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<GraphNode, GraphEdge>(.5, .5));
			
			// --- Set the EdgeShape of the Visualization Viewer --------------
			this.setEdgeShapeTransformer(visView);
			
			// --- Set edge width ---------------------------------------------
			visView.getRenderContext().setEdgeStrokeTransformer(new Transformer<GraphEdge, Stroke>() {
				@Override
				public Stroke transform(GraphEdge edge) {
					int scaleMultiplier = 1;
					MapSettings ms = graphController.getNetworkModel().getMapSettings();
					if (ms!=null) {
						scaleMultiplier = ms.getMapScale().getScaleMultiplier();
					}
					return new BasicStroke(scaleMultiplier * edge.getGraphElementLayout(graphController.getNetworkModel()).getSize());
				}
			});
			
			// --- Configure edge color ---------------------------------------
			Transformer<GraphEdge, Paint> edgeColorTransformer = new Transformer<GraphEdge, Paint>() {
				@Override
				public Paint transform(GraphEdge edge) {
					Color initColor = edge.getGraphElementLayout(graphController.getNetworkModel()).getColor();
					if (visView.getPickedEdgeState().isPicked(edge)) {
						initColor = edge.getGraphElementLayout(graphController.getNetworkModel()).getColorPicked();
					}
					return initColor;
				}}; 
			visView.getRenderContext().setEdgeDrawPaintTransformer(edgeColorTransformer);
			visView.getRenderContext().setArrowFillPaintTransformer(edgeColorTransformer);
			visView.getRenderContext().setArrowDrawPaintTransformer(edgeColorTransformer);
			
			// --- Configure Edge Image Labels --------------------------------
			visView.getRenderContext().setEdgeLabelTransformer(new Transformer<GraphEdge, String>() {
				@Override
				public String transform(GraphEdge edge) {
					// --- Get the needed info --------------------------------
					String imageRef = edge.getGraphElementLayout(graphController.getNetworkModel()).getImageReference();
					boolean showLabel = edge.getGraphElementLayout(graphController.getNetworkModel()).isShowLabel();
					// --- Configure color ------------------------------------
					Color color = Color.BLACK;
					if (visView.getPickedEdgeState().isPicked(edge)) {
						color = edge.getGraphElementLayout(graphController.getNetworkModel()).getColorPicked();
					}
					String htmlColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
					
					// --- Get the text / image content -----------------------
					String content = "";
					if (showLabel) {
						content = edge.getId();
					}
					if (imageRef!= null) {
						URL url = GraphGlobals.getImageURL(imageRef);
						if (url != null) {
							if (showLabel) {
								content = content + "<br><img src='" + url + "'>";
							} else {
								content = "<img src='" + url + "'>";
							}
						}
					}
					// --- Set the return value -------------------------------
					String textDisplay = "<html><center><font color='[COLOR]'>[CONTENT]</font></center></html>";
					textDisplay = textDisplay.replace("[COLOR]", htmlColor);
					textDisplay = textDisplay.replace("[CONTENT]", content);
					return textDisplay;
				}
			});

			// --- Set edge renderer for a background color of an edge --------
			visView.getRenderer().setEdgeRenderer(new GraphEnvironmentEdgeRenderer(visView) {
				@Override
				public boolean isShowMarker(GraphEdge edge) {
					return edge.getGraphElementLayout(graphController.getNetworkModel()).isMarkerShow();
				}
				@Override
				public float getMarkerStrokeWidth(GraphEdge edge) {
					return edge.getGraphElementLayout(graphController.getNetworkModel()).getMarkerStrokeWidth();
				}
				@Override
				public Color getMarkerColor(GraphEdge edge) {
					return edge.getGraphElementLayout(graphController.getNetworkModel()).getMarkerColor();
				}
			});
			
			this.isCreatedVisualizationViewer=true;
		}
		return visView;
	}
	/**
	 * Returns if the visualization viewer was fully created.
	 * @return true, if is created visualization viewer
	 */
	public boolean isCreatedVisualizationViewer() {
		return isCreatedVisualizationViewer;
	}

	/**
	 * Sets the edge shape transformer according to the {@link GeneralGraphSettings4MAS}.
	 * @see LayoutSettings#getEdgeShape()
	 */
	public void setEdgeShapeTransformer() {
		this.setEdgeShapeTransformer(this.getVisualizationViewer());
	}
	/**
	 * Sets the edge shape transformer according to the {@link GeneralGraphSettings4MAS}.
	 * @see LayoutSettings#getEdgeShape()
	 * @param visViewer the vis viewer
	 */
	public void setEdgeShapeTransformer(BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer) {

		// --- Use straight lines as edges ? ------------------------------
		AbstractEdgeShapeTransformer<GraphNode, GraphEdge> edgeShapeTransformer = null;
		switch (this.getGraphEnvironmentController().getNetworkModel().getLayoutSettings().getEdgeShape()) {
		case BentLine:
			edgeShapeTransformer = new EdgeShape.BentLine<GraphNode, GraphEdge>();
			break;
		case Box:
			edgeShapeTransformer = new EdgeShape.Box<GraphNode, GraphEdge>();
			break;
		case ConfigurableLine:
			edgeShapeTransformer = new GraphEdgeShapeTransformer<GraphNode, GraphEdge>();
			break;
		case CubicCurve:
			edgeShapeTransformer = new EdgeShape.CubicCurve<GraphNode, GraphEdge>();
			break;
		case Line:
			edgeShapeTransformer = new EdgeShape.Line<GraphNode, GraphEdge>();
			break;
		case Loop:
			edgeShapeTransformer = new EdgeShape.Loop<GraphNode, GraphEdge>();
			break;
		case Orthogonal:
			edgeShapeTransformer = new EdgeShape.Orthogonal<GraphNode, GraphEdge>();
			break;
		case QuadCurve:
			edgeShapeTransformer = new EdgeShape.QuadCurve<GraphNode, GraphEdge>();
			break;
		case SimpleLoop:
			edgeShapeTransformer = new EdgeShape.SimpleLoop<GraphNode, GraphEdge>();
			break;
		case Wedge:
			edgeShapeTransformer = new EdgeShape.Wedge<GraphNode, GraphEdge>(5);
			break;
		}
		visViewer.getRenderContext().setEdgeShapeTransformer(edgeShapeTransformer);
		visViewer.repaint();
	}

	
	/**
	 * This method notifies the observers about a graph object selection
	 * @param pickedObject The selected object
	 */
	public void handleObjectLeftClick(Object pickedObject) {
		this.handleObjectLeftClick(pickedObject, false);
	}
	
	/**
	 * Handle object left click.
	 *
	 * @param pickedObject the picked object
	 * @param shiftPressed the shift pressed
	 */
	public void handleObjectLeftClick(Object pickedObject, boolean shiftPressed) {
		this.selectObject(pickedObject, shiftPressed);
	}
	/**
	 * Notifies the observers that this object is right clicked
	 * @param pickedObject the selected object
	 */
	public void handleObjectRightClick(Object pickedObject) {
		this.selectObject(pickedObject);
	}
	/**
	 * Invoked when a graph node or edge is double clicked (left or right).
	 * @param pickedObject the picked object
	 */
	public void handleObjectDoubleClick(Object pickedObject) {
		
		this.selectObject(pickedObject);
		
		if (pickedObject instanceof GraphNode) {
			// --- Set the local variable -------------------------------------
			GraphNode graphNode = (GraphNode) pickedObject;
			// --- Is the GraphNode a DistributionNode ? ----------------------
			NetworkComponent networkComponent = this.graphController.getNetworkModel().getDistributionNode(graphNode);
			if (networkComponent!=null) {
				ComponentTypeSettings cts = this.graphController.getGeneralGraphSettings4MAS().getCurrentCTS().get(networkComponent.getType());
				if (cts.getAdapterClass()==null || cts.getAdapterClass().equals(NetworkComponentToGraphNodeAdapter.class.getName())==false) {
					NetworkModelNotification nmn = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_ShowPopUpMenue);
					nmn.setInfoObject(pickedObject);
					this.graphController.notifyObservers(nmn);
					return;
				}
			}
		}
		
		// --- Notify about the editing request for a component ---------------
		NetworkModelNotification nmNote = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_EditComponentSettings);
		nmNote.setInfoObject(pickedObject);
		this.graphController.notifyObservers(nmNote);
	}

	/**
	 * Clears the picked nodes and edges
	 */
	private void clearPickedObjects() {
		this.getVisualizationViewer().getPickedVertexState().clear();
		this.getVisualizationViewer().getPickedEdgeState().clear();	
	}

	/**
	 * Sets a node or edge as picked
	 * @param object The GraphNode or GraphEdge to pick
	 */
	private void setPickedObject(GraphElement object) {
		if (object instanceof GraphEdge) {
			this.getVisualizationViewer().getPickedEdgeState().pick((GraphEdge) object, true);
		} else if (object instanceof GraphNode) {
			this.getVisualizationViewer().getPickedVertexState().pick((GraphNode) object, true);
		}
	}

	/**
	 * Marks a group of objects as picked
	 * @param objects The objects
	 */
	private void setPickedObjects(Vector<GraphElement> objects) {
		Iterator<GraphElement> objIter = objects.iterator();
		while (objIter.hasNext()) {
			this.setPickedObject(objIter.next());
		}
	}

	/**
	 * Returns the node which is picked. If multiple nodes are picked, returns null.
	 * @return GraphNode - the GraphNode which is picked.
	 */
	public GraphNode getPickedSingleNode() {
		Set<GraphNode> nodeSet = this.getVisualizationViewer().getPickedVertexState().getPicked();
		if (nodeSet.size() == 1) {
			return nodeSet.iterator().next();
		}
		return null;
	}

	/**
	 * Return a set of GraphNodes that are currently picked.
	 * @return the picked nodes or null
	 */
	public Set<GraphNode> getPickedNodes() {
		PickedState<GraphNode> nodesPicked = this.getVisualizationViewer().getPickedVertexState();
		if (nodesPicked!=null) {
			return nodesPicked.getPicked();
		}
		return null;
	}

	/**
	 * Returns the edge which is picked. If multiple nodes are picked, returns null.
	 * @return GraphEdge - the GraphNode which is picked.
	 */
	public GraphEdge getPickedSingleEdge() {
		Set<GraphEdge> edgeSet = this.getVisualizationViewer().getPickedEdgeState().getPicked();
		if (edgeSet.size() == 1) {
			return edgeSet.iterator().next();
		}
		return null;
	}

	/**
	 * Return a set of GraphEdge that are currently picked.
	 * @return the picked edges
	 */
	public Set<GraphEdge> getPickedEdges() {
		PickedState<GraphEdge> edgesPicked = this.getVisualizationViewer().getPickedEdgeState();
		if (edgesPicked!=null) {
			return edgesPicked.getPicked();
		}
		return null;
	}
	
	/**
	 * Selects an object. Previous selections are cleared:
	 * @param object the object to select
	 */
	private void selectObject(Object object) {
		this.selectObject(object, false);
	}

	/**
	 * Selects an object, optionally keeping or clearing the previous selection
	 * @param object the object to select
	 * @param keepPrevoiusSelection if true, the previous selection is kept, otherwise cleared
	 */
	private void selectObject(Object object, boolean keepPrevoiusSelection) {
		
		if (keepPrevoiusSelection==false) {
			this.clearPickedObjects();
		}

		if (object instanceof GraphNode) {
			this.setPickedObject((GraphElement) object);
			// --- Is that node a distribution node? ----------------
			List<NetworkComponent> netComps = graphController.getNetworkModel().getNetworkComponents((GraphNode) object);
			NetworkComponent disNode = graphController.getNetworkModel().getDistributionNode(netComps);
			if (disNode != null) {
				if (keepPrevoiusSelection==true) {
					this.graphController.getNetworkModelUndoManager().addNetworkComponentToSelection(disNode);
				} else {
					this.graphController.getNetworkModelUndoManager().selectNetworkComponent(disNode);
				}
			} 
			if (netComps.size() == 1) {
				NetworkComponent netComp = netComps.get(0);
				if (keepPrevoiusSelection==true) {
					this.graphController.getNetworkModelUndoManager().addNetworkComponentToSelection(netComp);
				} else {
					this.graphController.getNetworkModelUndoManager().selectNetworkComponent(netComp);
					this.clearPickedObjects();
				}
				this.setPickedObject((GraphElement) object);
			}

		} else if (object instanceof GraphEdge) {
			NetworkComponent netComp = graphController.getNetworkModel().getNetworkComponent((GraphEdge) object);
			this.setPickedObjects(graphController.getNetworkModel().getGraphElementsFromNetworkComponent(netComp));
			if (keepPrevoiusSelection==true) {
				this.graphController.getNetworkModelUndoManager().addNetworkComponentToSelection(netComp);
			} else {
				this.graphController.getNetworkModelUndoManager().selectNetworkComponent(netComp);
			}

		} else if (object instanceof NetworkComponent) {
			this.setPickedObjects(graphController.getNetworkModel().getGraphElementsFromNetworkComponent((NetworkComponent) object));
		}
	}

	
	/**
	 * Returns the selected graph objects. These are either all selected {@link NetworkComponent}'s
	 * or, in case of the selection of a single {@link GraphNode}   
	 * @return the selected graph object
	 */
	public Set<Object> getSelectedGraphObject() {
		
		HashSet<Object> selectedGraphObject = new HashSet<Object>();

		Set<GraphNode> nodesSelected = this.getPickedNodes();
		Set<GraphEdge> edgesSelected = this.getPickedEdges();
		
		if (edgesSelected.size()==0 && nodesSelected.size()==0) {
			// --- Nothing selected -----------------------
			return null;
		
		} else {
			// --- Something selected ---------------------
			List<NetworkComponent> fsNetComps = this.graphController.getNetworkModel().getNetworkComponentsFullySelected(nodesSelected);
			if (fsNetComps==null || fsNetComps.size()==0) {
				if (nodesSelected.size()==1) {
					selectedGraphObject.add(nodesSelected.iterator().next());
				}	
			} else {
				selectedGraphObject.addAll(fsNetComps);
			}
		}
		return selectedGraphObject;
	}
	
	/**
	 * Edits the position of the specified or of the currently picked GraphNode.
	 * @param graphNodeToEdit the GraphNode where the position should be edited
	 */
	public void editGraphNodePosition(GraphNode graphNodeToEdit) {
	
		if (graphNodeToEdit==null) graphNodeToEdit = this.getPickedSingleNode();
		if (graphNodeToEdit==null) return;

		AbstractCoordinate oldCoordinate = graphNodeToEdit.getCoordinate();
		AbstractCoordinate newCoordinate = null;
		
		// --- Found a GraphNode ------------------------------------
		if (this.getPickedNodes().contains(graphNodeToEdit)==false) {
			// --- Pick the actual GraphNode ------------------------ 
			this.getVisualizationViewer().getPickedVertexState().clear();
			this.getVisualizationViewer().getPickedVertexState().pick(graphNodeToEdit, true);
		}
		
		// --- What type of coordinates we're dealing with? ---------
		if (this.graphController.getNetworkModel().getLayoutSettings().isGeographicalLayout()==true) {
			// ------------------------------------------------------
			// --- Edit geographical coordinates --------------------			
			// ------------------------------------------------------
			AbstractGeoCoordinate olgGeoCoordinate = (AbstractGeoCoordinate) oldCoordinate;
			Frame dialogOwner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
			GeoCoordinateDialog geoDialog = new GeoCoordinateDialog(dialogOwner, olgGeoCoordinate);
			// - - - - Wait for user - - - - -
			if (geoDialog.isCanceled()==true) return;
			
			// --- Get UTM coordinates to set the new position ------
			AbstractCoordinate geoCoordinate = geoDialog.getGeoCoordinate();
			if (geoCoordinate==null) {
				return;
			} else if (geoCoordinate instanceof WGS84LatLngCoordinate) {
				newCoordinate = (WGS84LatLngCoordinate) geoCoordinate;
			} else if (geoCoordinate instanceof UTMCoordinate) {
				newCoordinate = (UTMCoordinate) geoCoordinate;
			}

		} else {
			// ------------------------------------------------------
			// --- Edit regular coordinates -------------------------
			// ------------------------------------------------------
			Frame dialogOwner = Application.getGlobalInfo().getOwnerFrameForComponent(this);
			GraphNodePositionDialog posDialog = new GraphNodePositionDialog(dialogOwner, oldCoordinate);
			// - - - - Wait for user - - - - -
			if (posDialog.isCanceled()==true) return;

			// --- Set new GraphNode position -----------------------
			newCoordinate = posDialog.getCoordinate();
			
		}

		// --- Do movement and define undoable action ---------------
		if (newCoordinate!=null && newCoordinate.equals(oldCoordinate)==false) {
			// --- Set position to graph node ----------------------- 
			graphNodeToEdit.setCoordinate(newCoordinate);
			this.updateGraphNodePositionInLayout(graphNodeToEdit);

			// --- Do we work on an intermediate GraphNode? ---------
			String graphNodeID = graphNodeToEdit.getId();
			if (this.getGraphMouseMode()!=GraphMouseMode.EdgeEditing) {
				// --- Create undoable action ----------------------- 
				this.graphController.getNetworkModelUndoManager().setGraphNodesMoved(this, graphNodeToEdit, oldCoordinate);
				
			} else {
				// --- Reconfigure edge -----------------------------
				GraphEdge editGraphEdge = this.getVisualizationViewer().getPickedEdgeState().getPicked().iterator().next();
				if (editGraphEdge!=null) {
					// --- Work on intermediate nodes? --------------
					if (graphNodeID.startsWith(ConfiguredLineMousePlugin.INTERMEDIATE_GRAPH_NODE_ID_PREFIX)==true) {
						
						GraphEdgeShapeConfiguration<?> shapeConfig = editGraphEdge.getEdgeShapeConfiguration();
						// --- Work on the intermediate points ------
						int posIdx = Integer.parseInt(graphNodeID.substring(graphNodeID.indexOf("_") + 1, graphNodeID.length()));
						List<Point2D> intPointList = shapeConfig.getIntermediatePoints();
						// --- Define new intermediate position -----
						Point2D intPointPosNew = newCoordinate;
						if (shapeConfig.isUseAbsoluteCoordinates()==false) {
							// --- To relative coordinates ---------- 
							Pair<GraphNode> graphNodes = this.getGraph().getEndpoints(editGraphEdge);
							intPointPosNew = new IntermediatePointTransformer().transformToIntermediateCoordinate(intPointPosNew, graphNodes.getFirst(), graphNodes.getSecond());
						}
						intPointList.set(posIdx, intPointPosNew);					
					}
				}
				// --- Redraw edge ------------------------------
				this.getVisualizationViewer().getPickedEdgeState().pick(editGraphEdge, false);
				this.getVisualizationViewer().getPickedEdgeState().pick(editGraphEdge, true);
			}
		}
		
	}
	
	/**
	 * Updates the GraphNode position in the current layout. May be called, if the position was renewed in the GrapHNode.
	 * @param graphNode the GraphNode
	 */
	public void updateGraphNodePositionInLayout(GraphNode graphNode) {
		if (graphNode!=null && graphNode.getPosition()!=null) {
			this.getVisualizationViewer().getGraphLayout().setLocation(graphNode, this.getCoordinateSystemPositionTransformer().transform(graphNode.getPosition()));
		}
	}
	
	/**
	 * Returns the list of registered {@link GraphSelectionListener}.
	 * @return the graph selection listener list
	 */
	private List<GraphSelectionListener> getGraphSelectionListenerList() {
		if (graphSelectionListenerList==null) {
			graphSelectionListenerList = new ArrayList<>();
		}
		return graphSelectionListenerList;
	}
	/**
	 * Adds the graph selection listener.
	 * @param listener the listener to add
	 */
	public void addGraphSelectionListener(GraphSelectionListener listener) {
		if (listener!=null && this.getGraphSelectionListenerList().contains(listener)==false) {
			this.getGraphSelectionListenerList().add(listener);
		}
	}
	/**
	 * Removes the graph selection listener.
	 * @param listener the listener to remove
	 */
	public void removeGraphSelectionListener(GraphSelectionListener listener) {
		this.getGraphSelectionListenerList().remove(listener);
	}

	/**
	 * Returns the resize wait timer.
	 * @return the resize wait timer
	 */
	private Timer getGraphSelectionWaitTimer() {
		if (graphSelectionWaitTimer==null) {
			graphSelectionWaitTimer = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BasicGraphGui.this.informGraphSelectionListener();
				}
			});
			graphSelectionWaitTimer.setRepeats(false);
		}
		return graphSelectionWaitTimer;
	}
	/**
	 * Start or restarts the graph selection wait timer.
	 */
	private void reStartGraphSelectionWaitTimer() {
		if (getGraphSelectionWaitTimer().isRunning()==true) {
			this.getGraphSelectionWaitTimer().restart();
		} else {
			this.getGraphSelectionWaitTimer().start();
		}
	}
	/**
	 * Informs the graph selection listener.
	 */
	private void informGraphSelectionListener() {
		
		if (this.getGraphSelectionListenerList().size()==0) return;
		
		// --- Get what is available locally --------------
		Set<GraphNode> nodesSelected = this.getPickedNodes();
		Set<GraphEdge> edgesSelected = this.getPickedEdges();
		List<NetworkComponent> netCompsSelected = this.graphController.getNetworkModel().getNetworkComponentsFullySelected(nodesSelected);
		
		// --- Define current selection instance ----------
		GraphSelection graphSelection = new GraphSelection();
		graphSelection.getNodeList().addAll(nodesSelected);
		graphSelection.getEdgeList().addAll(edgesSelected);
		if (netCompsSelected!=null) graphSelection.getNetworkComponentList().addAll(netCompsSelected);

		// --- Invoke listener ----------------------------
		for (int i = 0; i < this.getGraphSelectionListenerList().size(); i++) {
			this.getGraphSelectionListenerList().get(i).onGraphSelectionChanged(graphSelection);
		}
	}
	
	
	/**
	 * Export the current graph as image by using a file selection dialog.
	 */
	private void exportAsImage() {

		String currentFolder = null;
		if (Application.getGlobalInfo() != null) {
			// --- Get the last selected folder of Agent.GUI ---
			currentFolder = Application.getGlobalInfo().getLastSelectedFolderAsString();
		}

		// --- Create instance of JFileChooser -----------------
		JFileChooser jfc = new JFileChooser();
		jfc.setMultiSelectionEnabled(false);
		jfc.setAcceptAllFileFilterUsed(false);

		// --- Add custom icons for file types. ----------------
		jfc.setFileView(new ImageFileView());
		// --- Add the preview pane. ---------------------------
		jfc.setAccessory(new ImagePreview(jfc));

		// --- Set the file filter -----------------------------
		String[] extensionsJPEG = { ImageUtils.jpg, ImageUtils.jpeg };

		ConfigurableFileFilter filterJPG = new ConfigurableFileFilter(extensionsJPEG, "JPEG - Image");
		ConfigurableFileFilter filterPNG = new ConfigurableFileFilter(ImageUtils.png, "PNG - File");
		ConfigurableFileFilter filterGIF = new ConfigurableFileFilter(ImageUtils.gif, "GIF - Image");

		jfc.addChoosableFileFilter(filterGIF);
		jfc.addChoosableFileFilter(filterJPG);
		jfc.addChoosableFileFilter(filterPNG);

		jfc.setFileFilter(filterPNG);

		// --- Maybe set the current directory -----------------
		if (currentFolder != null) {
			jfc.setCurrentDirectory(new File(currentFolder));
		}

		// === Show dialog and wait on user action =============
		int state = jfc.showSaveDialog(this);
		if (state == JFileChooser.APPROVE_OPTION) {

			ConfigurableFileFilter cff = (ConfigurableFileFilter) jfc.getFileFilter();
			String selectedExtension = cff.getFileExtension()[0];
			String mustExtension = "." + selectedExtension;

			File selectedFile = jfc.getSelectedFile();
			if (selectedFile != null) {
				String selectedPath = selectedFile.getAbsolutePath();
				if (selectedPath.endsWith(mustExtension) == false) {
					selectedPath = selectedPath + mustExtension;
				}
				// ---------------------------------------------
				// --- Export current display to image ---------
				// ---------------------------------------------
				this.exportAsImage(this.getVisualizationViewer(), selectedPath, selectedExtension);
				// ---------------------------------------------

				if (Application.getGlobalInfo() != null) {
					Application.getGlobalInfo().setLastSelectedFolder(jfc.getCurrentDirectory());
				}
			}
		} // end APPROVE_OPTION

	}
	/**
	 * Export the current graph as image by using specified parameters. 
	 *
	 * @param vv the VisualizationViewer
	 * @param file the current file to export to
	 */
	private void exportAsImage(BasicGraphGuiVisViewer<GraphNode, GraphEdge> vv, String path2File, String extension) {

		// --- If the VisualizationViewer is null ---------
		if (vv == null) {
			return;
		}
		// --- Overwrite existing file ? ------------------
		File writeFile = new File(path2File);
		if (writeFile.exists()) {
			String msgHead = "Overwrite?";
			String msgText = "Overwrite existing file?";
			int msgAnswer = JOptionPane.showConfirmDialog(this, msgText, msgHead, JOptionPane.YES_NO_OPTION);
			if (msgAnswer == JOptionPane.NO_OPTION) {
				return;
			}
		}

		// --- Lets go ! ----------------------------------
		int width = vv.getSize().width;
		int height = vv.getSize().height;

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics2D graphics = bi.createGraphics();
		graphics.fillRect(0, 0, width, height);

		boolean wasDoubleBuffered=vv.isDoubleBuffered();
		vv.setDoubleBuffered(false);
		vv.paint(graphics);
		vv.paintComponents(graphics);
		vv.setDoubleBuffered(wasDoubleBuffered);

		try {
			ImageIO.write(bi, extension, writeFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// --------------------------------------------------------------------------------------------
	// --- From here, handling of MapServices ----------------------------------------------------- 
	// --------------------------------------------------------------------------------------------
	/**
	 * Depending on the current MapSettings, sets the map pre-rendering ON or OFF.
	 */
	private void setMapPreRendering() {
		
		// --- Get old / current instances ------------------------------------
		MapService mapServiceOld = this.getVisualizationViewer().getMapService();
		BasicGraphGuiStaticLayout staticLayoutOld = this.getBasicGraphGuiStaticLayout();
		
		// --- Check how to configure -----------------------------------------
		boolean isDoMapPreRendering = this.isDoMapPreRendering();
		
		if (this.graphController.getNetworkModel().getLayoutSettings().isGeographicalLayout()==true) {
			// --- For geographical layouts ----------------------------------- 
			if (isDoMapPreRendering==true) {
				this.getVisualizationViewer().setMapService(this.getMapService());
				// -- Ensure that a ZoomController is initialized -------------
				this.getZoomController();
			} else {
				this.getVisualizationViewer().setMapService(null);
				if (mapServiceOld!=null) mapServiceOld.destroyMapServiceInstances();
			}
			
			// --- Prepare visualization viewer ------------------------------- 
			this.getVisualizationViewer().setDoMapPreRendering(isDoMapPreRendering);
			
			// --- Renew static graph layout ? --------------------------------
			if (staticLayoutOld.getClass().getName().equals(BasicGraphGuiStaticGeoLayout.class.getName())==false) {
				this.getVisualizationViewer().setGraphLayout(this.getNewGraphLayout());
			} else {
				staticLayoutOld.refreshGraphNodePosition();
			}
			
		} else {
			// --- For normal / non-geographical layouts ----------------------
			this.getVisualizationViewer().setMapService(null);
			if (mapServiceOld!=null) mapServiceOld.destroyMapServiceInstances();
			
			// --- Prepare visualization viewer -------------------------------
			this.getVisualizationViewer().setDoMapPreRendering(false);
			
			// --- Renew static graph layout ? --------------------------------
			if (staticLayoutOld.getClass().getName().equals(BasicGraphGuiStaticLayout.class.getName())==false) {
				this.getVisualizationViewer().setGraphLayout(this.getNewGraphLayout());
			}
			
		}
		
	}
	/**
	 * Checks if is usable map service.
	 * @return true, if is usable map service
	 */
	public boolean isDoMapPreRendering() {
		
		// --- Get the current layout and check if is layout with geographical coordinates --------
		LayoutSettings lSettings = this.getGraphEnvironmentController().getNetworkModel().getLayoutSettings();
		if (lSettings.isGeographicalLayout()==false) return false;
		
		// --- Get current MapSettings and check if map painting is activated ---------------------
		MapSettings mSettings = this.getGraphEnvironmentController().getNetworkModel().getMapSettings();
		if (mSettings==null) return false;
		if (mSettings.getMapServiceName()==null || mSettings.getMapServiceName().isEmpty()==true) return false;
		//if (mSettings.getMapTileTransparency()==100) return false;
		
		// --- Ensure that the MapService is available --------------------------------------------
		if (this.getMapService()==null) return false;
		
		return true;
	}
	/**
	 * Returns the currently selected {@link MapService} from the current {@link MapSettings} of the {@link NetworkModel}.
	 * @return the MapService selected
	 */
	public MapService getMapService() {
		MapSettings mapSettings = this.getGraphEnvironmentController().getNetworkModel().getMapSettings();
		if (mapSettings!=null) {
			return mapSettings.getMapService();
		}
		return null;
	}
	
	
	// --------------------------------------------------------------------------------------------
	// --- From here, handling of zooming and the usage of the individual scaling control --------- 
	// --------------------------------------------------------------------------------------------	
	/**
	 * Return the current zoom controller.
	 * @return the zoom controller
	 */
	public ZoomController getZoomController() {

		// --- Check if we have an individual ZoomController ------------------ 
		if (this.isDoMapPreRendering()==true) {
			ZoomController zc = this.getMapService().getZoomController();
			if (zc!=null) {
				zc.setGraphEnvironmentController(this.getGraphEnvironmentController());
				zc.setBasicGraphGui(this);
				zc.setVisualizationViewer(this.getVisualizationViewer());
				return zc;
			}
		}
		// --- Use the default ZommController --------------------------------- 
		if (zoomController==null) {
			zoomController = new BasicGraphGuiZoomController(this.getGraphEnvironmentController(), this);
		}
		return zoomController;
	}
	
	// --------------------------------------------------------------------------------------------
	// --- From here, handling of satellite visualization ----------------------------------------- 
	// --------------------------------------------------------------------------------------------	
	/**
	 * Gets the VisualizationViewer for the satellite view
	 * @return The VisualizationViewer
	 */
	public SatelliteVisualizationViewer<GraphNode, GraphEdge> getSatelliteVisualizationViewer() {
		if (visViewSatellite==null) {
			
			// --- Set dimension and create a new SatelliteVisualizationViewer ----
			visViewSatellite = new SatelliteVisualizationViewer<GraphNode, GraphEdge>(this.getVisualizationViewer(), this.getDimensionOfSatelliteVisualizationViewer());
			visViewSatellite.scaleToLayout(this.getZoomController().getScalingControl());
			visViewSatellite.setGraphMouse(new SatelliteGraphMouse(this));
			
			// --- Configure the node shape and size ------------------------------
			visViewSatellite.getRenderContext().setVertexShapeTransformer(this.getVisualizationViewer().getRenderContext().getVertexShapeTransformer());
			// --- Configure node icons, if configured ----------------------------
			visViewSatellite.getRenderContext().setVertexIconTransformer(this.getVisualizationViewer().getRenderContext().getVertexIconTransformer());
			// --- Configure node colors ------------------------------------------
			visViewSatellite.getRenderContext().setVertexFillPaintTransformer(this.getVisualizationViewer().getRenderContext().getVertexFillPaintTransformer());
			// --- Configure node label transformer -------------------------------
			visViewSatellite.getRenderContext().setVertexLabelTransformer(this.getVisualizationViewer().getRenderContext().getVertexLabelTransformer());
			
			// --- Use straight lines as edges ------------------------------------
			visViewSatellite.getRenderContext().setEdgeShapeTransformer(this.getVisualizationViewer().getRenderContext().getEdgeShapeTransformer());
			// --- Set edge width -------------------------------------------------
			visViewSatellite.getRenderContext().setEdgeStrokeTransformer(this.getVisualizationViewer().getRenderContext().getEdgeStrokeTransformer());
			// --- Configure edge color -------------------------------------------
			visViewSatellite.getRenderContext().setEdgeDrawPaintTransformer(this.getVisualizationViewer().getRenderContext().getEdgeDrawPaintTransformer());
			visViewSatellite.getRenderContext().setArrowFillPaintTransformer(this.getVisualizationViewer().getRenderContext().getEdgeDrawPaintTransformer());
			visViewSatellite.getRenderContext().setArrowDrawPaintTransformer(this.getVisualizationViewer().getRenderContext().getEdgeDrawPaintTransformer());

			// --- Configure Edge Image Labels ------------------------------------
			visViewSatellite.getRenderContext().setEdgeLabelTransformer(this.getVisualizationViewer().getRenderContext().getEdgeLabelTransformer());
			// --- Set edge renderer for a background color of an edge ------------
			visViewSatellite.getRenderer().setEdgeRenderer(this.getVisualizationViewer().getRenderer().getEdgeRenderer());
		}
		return visViewSatellite;
	}
	/**
	 * Zooms the satellite visualization viewer to fit to window.
	 */
	private void zoomSatelliteVisualizationViewerToFitToWindow() {
		if (satelliteDialog!=null && satelliteDialog.isVisible()==true) {
			this.getZoomController().zoomToFitToWindow(this.getSatelliteVisualizationViewer());
		}
	}
	/**
	 * Returns the dimension of the SatelliteVisualizationViewer.
	 * @return the vis view satellite dimension
	 */
	private Dimension getDimensionOfSatelliteVisualizationViewer() {
		double ratio = 0.3;
		Dimension vvSize = this.getVisualizationViewer().getSize();
		Dimension visViewSateliteSize = new Dimension((int)(vvSize.getWidth()*ratio), (int) (vvSize.getHeight()*ratio)); 
		return visViewSateliteSize;
	}
	/**
	 * Returns the satellite view.
	 * @return the satellite view
	 */
	private SatelliteDialog getSatelliteDialog() {
		if (satelliteDialog==null){
			Frame ownerFrame = Application.getGlobalInfo().getOwnerFrameForComponent(this);
			if (ownerFrame!=null) {
				satelliteDialog = new SatelliteDialog(ownerFrame, this.graphController, this);
			} else {
				Dialog ownerDialog = Application.getGlobalInfo().getOwnerDialogForComponent(this);
				satelliteDialog = new SatelliteDialog(ownerDialog, this.graphController, this);
			}
			satelliteDialog.setSize(this.getDimensionOfSatelliteVisualizationViewer());
		}
		return satelliteDialog;
	}
	/**
	 * Sets the satellite view.
	 * @param satelliteDialog the new satellite view
	 */
	private void setSatelliteDialog(SatelliteDialog satelliteDialog) {
		this.satelliteDialog = satelliteDialog;
	}
	/**
	 * Disposes the satellite view.
	 */
	private void disposeSatelliteView() {
		if (this.satelliteDialog!=null) {
			this.satelliteDialog.setVisible(false);
			this.satelliteDialog.dispose();
			this.setSatelliteDialog(null);	
		}
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
			case NetworkModelNotification.NETWORK_MODEL_ComponentTypeSettingsChanged:
			case NetworkModelNotification.NETWORK_MODEL_LayoutChanged:
				this.reLoadGraph();
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Reload:
				this.reLoadGraph();
				break;

			case NetworkModelNotification.NETWORK_MODEL_Satellite_View:
				Boolean visible = (Boolean) infoObject;
				this.getSatelliteDialog().setVisible(visible);
				if (visible==true) {
					this.zoomSatelliteVisualizationViewerToFitToWindow();
				}
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Repaint:
				this.repaintGraph();
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Component_Added:
			case NetworkModelNotification.NETWORK_MODEL_Component_Removed:
			case NetworkModelNotification.NETWORK_MODEL_Nodes_Merged:
			case NetworkModelNotification.NETWORK_MODEL_Nodes_Splited:
			case NetworkModelNotification.NETWORK_MODEL_Merged_With_Supplement_NetworkModel:
				this.clearPickedObjects();
				this.repaintGraph();
				break;

			case NetworkModelNotification.NETWORK_MODEL_Component_Renamed:
				this.clearPickedObjects();
				RenamedNetworkComponent renamed = (RenamedNetworkComponent) infoObject;
				this.selectObject(renamed.getNetworkComponent()); 
				break;

			case NetworkModelNotification.NETWORK_MODEL_Component_Selected:
				this.selectObject(infoObject);
				break;

				
			case NetworkModelNotification.NETWORK_MODEL_Zoom_Fit2Window:
				this.getZoomController().zoomToFitToWindow();
				break;
			case NetworkModelNotification.NETWORK_MODEL_Zoom_One2One:
				this.getZoomController().zoomOneToOne();
				break;
			case NetworkModelNotification.NETWORK_MODEL_Zoom_Component:
				this.getZoomController().zoomToComponent();
				break;
			case NetworkModelNotification.NETWORK_MODEL_Zoom_In:
				this.getZoomController().zoomIn();
				break;
			case NetworkModelNotification.NETWORK_MODEL_Zoom_Out:
				this.getZoomController().zoomOut();
				break;

				
			case NetworkModelNotification.NETWORK_MODEL_ExportGraphAsImage:
				this.exportAsImage();
				break;

				
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking:
				this.setGraphMouseMode(GraphMouseMode.Picking);
				break;
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Transforming:
				this.setGraphMouseMode(GraphMouseMode.Transforming);
				break;
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_EdgeEditing:
				this.setGraphMouseMode(GraphMouseMode.EdgeEditing);
				break;
				
				
			case NetworkModelNotification.NETWORK_MODEL_MapServiceChanged:
			case NetworkModelNotification.NETWORK_MODEL_MapScaleChanged:
			case NetworkModelNotification.NETWORK_MODEL_MapTransparencyChanged:
				this.setMapPreRendering();
				break;

				
			default:
				break;
			}

		}

	}

}
