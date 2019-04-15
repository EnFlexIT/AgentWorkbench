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
import java.awt.Image;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
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
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphElementLayout;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.adapter.NetworkComponentToGraphNodeAdapter;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.GraphSelectionListener.GraphSelection;
import org.awb.env.networkModel.controller.ui.commands.RenamedNetworkComponent;
import org.awb.env.networkModel.helper.GraphEdgeShapeTransformer;
import org.awb.env.networkModel.settings.ComponentTypeSettings;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;
import org.awb.env.networkModel.settings.LayoutSettings;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemXDirection;
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemYDirection;

import agentgui.core.application.Application;
import de.enflexit.common.swing.imageFileSelection.ConfigurableFileFilter;
import de.enflexit.common.swing.imageFileSelection.ImageFileView;
import de.enflexit.common.swing.imageFileSelection.ImagePreview;
import de.enflexit.common.swing.imageFileSelection.ImageUtils;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
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
	private List<GraphSelectionListener> graphSelectionListenerList;
	
	private SatelliteDialog satelliteDialog;
	private SatelliteVisualizationViewer<GraphNode, GraphEdge> visViewSatellite;
	
	/** JUNG object handling zooming */
	private ScalingControl scalingControl = new CrossoverScalingControl();
	/** the margin of the graph for the visualization */
	private double graphMargin = 25;
	private Point2D defaultScaleAtPoint = new Point2D.Double(graphMargin, graphMargin);
	
	private PluggableGraphMouse pluggableGraphMouse; 
	private GraphEnvironmentMousePlugin graphEnvironmentMousePlugin;
	private GraphEnvironmentPopupPlugin<GraphNode, GraphEdge> graphEnvironmentPopupPlugin;

	private DefaultModalGraphMouse<GraphNode, GraphEdge> defaultModalGraphMouse;
	
	private CoordinateSystemXDirection xDirectionReminder;
	private CoordinateSystemYDirection yDirectionReminder;

	private Timer graphSelectionWaitTimer;
	
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
			jPanelToolBarsEast.add(this.getBasicGraphGuiTools().getJToolBarLayout(), null);
		}
		return jPanelToolBarsEast;
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
	 * Gets the scaling control.
	 * @return the scalingControl
	 */
	private ScalingControl getScalingControl() {
		return this.scalingControl;
	}


	/**
	 * Returns the PluggableGraphMouse.
	 * @return the pluggableGraphMouse
	 */
	public PluggableGraphMouse getPluggableGraphMouse() {
		if (pluggableGraphMouse==null) {
			pluggableGraphMouse = new PluggableGraphMouse();
			pluggableGraphMouse.add(this.getGraphEnvironmentMousePlugin());
			pluggableGraphMouse.add(this.getGraphEnvironmentPopupPlugin());
		}
		return pluggableGraphMouse;
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
	 * Gets the DefaultModalGraphMouse.
	 * @return the default modal graph mouse
	 */
	private DefaultModalGraphMouse<GraphNode, GraphEdge> getDefaultModalGraphMouse() {
		if (defaultModalGraphMouse == null) {
			defaultModalGraphMouse = new DefaultModalGraphMouse<GraphNode, GraphEdge>(1/1.1f, 1.1f);
		}
		return defaultModalGraphMouse;
	}

	/**
	 * Gets the default point to scale at for zooming.
	 * @return the default scale at point
	 */
	private Point2D getDefaultScaleAtPoint() {
		Rectangle2D rectVis = this.getVisualizationViewer().getVisibleRect();
		if (rectVis.isEmpty()==false) {
			this.defaultScaleAtPoint = new Point2D.Double(rectVis.getCenterX(), rectVis.getCenterY());
		}
		return defaultScaleAtPoint;
	}
	/**
	 * Sets the default point to scale at for zooming..
	 * @param scalePoint the new default scale at point
	 */
	private void setDefaultScaleAtPoint(Point2D scalePoint) {
		defaultScaleAtPoint = scalePoint;
	}

	/**
	 * This method assigns the current graph to a VisualizationViewer to display it. 
	 */
	private void reLoadGraph() {

		// --- Display the current Graph ------------------
		this.getVisualizationViewer().setGraphLayout(this.getNewGraphLayout());
		this.zoomToFitToWindow(this.getVisualizationViewer());
		this.clearPickedObjects();
		
		this.getSatelliteVisualizationViewer().getGraphLayout().setGraph(this.getGraph());
		this.zoomToFitToWindow(this.getSatelliteVisualizationViewer());
		
		// --- Remind coordinate system alignment ---------
		this.xDirectionReminder = this.getGraphEnvironmentController().getNetworkModel().getLayoutSettings().getCoordinateSystemXDirection();
		this.yDirectionReminder = this.getGraphEnvironmentController().getNetworkModel().getLayoutSettings().getCoordinateSystemYDirection();
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
		Graph<GraphNode, GraphEdge> graph = this.getGraph();
		Layout<GraphNode, GraphEdge> layout = new StaticLayout<GraphNode, GraphEdge>(graph);
		Rectangle2D graphDimension = GraphGlobals.getGraphSpreadDimension(graph);
		layout.setSize(new Dimension((int) (graphDimension.getWidth() + 2 * graphMargin), (int) (graphDimension.getHeight() + 2 * graphMargin)));
		layout.setInitializer(new TransformerForGraphNodePosition<>(this.getGraphEnvironmentController()));
		
		return layout;
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
			visView.setGraphMouse(this.getPluggableGraphMouse());
			visView.addKeyListener(this.getGraphEnvironmentMousePlugin());
			
			// --- Set the pick size of the visualisation viewer --------
			((ShapePickSupport<GraphNode, GraphEdge>) visView.getPickSupport()).setPickSize(5);
			
			
			// --- Listener for GraphNode / GraphEdge (de)selections ---------- 
			ItemListener iListener = new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent ie) {
					Object item = ie.getItem();
					if (item instanceof GraphNode || item instanceof GraphNode) {
						BasicGraphGui.this.reStartGraphSelectionWaitTimer();
					}
				}
			};
			visView.getPickedVertexState().addItemListener(iListener);
			visView.getPickedEdgeState().addItemListener(iListener);

			
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
						toolTip += "<br>(x=" + node.getPosition().getX() + " - y=" + node.getPosition().getY() + ")";	
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
					GraphElementLayout nodeLayout = node.getGraphElementLayout(graphController.getNetworkModel());
					boolean picked = visView.getPickedVertexState().isPicked(node);
					String nodeImagePath = nodeLayout.getImageReference();

					if (nodeImagePath != null) {
						if (nodeImagePath.equals("MissingIcon")==false) {
							// --- 1. Search in the local Hash ------
							LayeredIcon layeredIcon = null;
							Color currentColor = nodeLayout.getColor();
							String colorPostfix = "[" + currentColor.getRGB() + "]";
							if (picked == true) {
								layeredIcon = this.iconHash.get(nodeImagePath + colorPostfix + this.pickedPostfix);
							} else {
								layeredIcon = this.iconHash.get(nodeImagePath + colorPostfix);
							}
							// --- 2. If necessary, load the image --
							if (layeredIcon == null) {
								ImageIcon imageIcon = GraphGlobals.getImageIcon(nodeImagePath);
								
								// --- Prepare the image ---------
								BufferedImage bufferedImage;
								if (currentColor.equals(Color.WHITE) || currentColor.equals(Color.BLACK)) {
									// --- If the color is set to black or white, just use the unchanged image ----------
									bufferedImage = convertToBufferedImage(imageIcon.getImage());
								} else {
									// --- Otherwise, replace the defined basic color with the one specified in the node layout ---------
									bufferedImage = exchangeColor(convertToBufferedImage(imageIcon.getImage()), GeneralGraphSettings4MAS.IMAGE_ICON_COLORIZE_BASE_COLOR, currentColor);
								}
								
								if (bufferedImage != null) {
									// --- 3. Remind this images ----
									LayeredIcon layeredIconUnPicked = new LayeredIcon(bufferedImage);
									this.iconHash.put(nodeImagePath + colorPostfix, layeredIconUnPicked);

									LayeredIcon layeredIconPicked = new LayeredIcon(bufferedImage);
									layeredIconPicked.add(new Checkmark(nodeLayout.getColorPicked()));
									this.iconHash.put(nodeImagePath + colorPostfix + this.pickedPostfix, layeredIconPicked);
									// --- 4. Return the right one --
									if (picked == true) {
										layeredIcon = layeredIconPicked;
									} else {
										layeredIcon = layeredIconUnPicked;
									}
								}
							}
							icon = layeredIcon;
						}
					}
					return icon;
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
			
			// --- Set the EdgeShape of the Visualisation Viewer --------------
			this.setEdgeShapeTransformer(visView);
			
			// --- Set edge width ---------------------------------------------
			visView.getRenderContext().setEdgeStrokeTransformer(new Transformer<GraphEdge, Stroke>() {
				@Override
				public Stroke transform(GraphEdge edge) {
					return new BasicStroke(edge.getGraphElementLayout(graphController.getNetworkModel()).getSize());
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
						URL url = getImageURL(imageRef);
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
			visView.getRenderer().setEdgeRenderer(new GraphEnvironmentEdgeRenderer() {
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
	 * Searches and returns the image url for the specified image reference.
	 *
	 * @param imageRef the image reference
	 * @return the URL of the image 
	 */
	private URL getImageURL(String imageRef){
		
		// --- Abort URL generation ---------------------------------
		if (imageRef.equals("MissingIcon")==true) return null;

		// --- Resource by the class loader, as configured ----------
		URL url = getClass().getResource(imageRef);
		if (url!=null) return url;
		
		// --- Prepare folder for projects --------------------------
		String projectsFolder = Application.getGlobalInfo().getPathProjects();
		projectsFolder = projectsFolder.replace("\\", "/");
		
		// --- Resource by file, in projects, absolute --------------
		String extImageRef = (projectsFolder + imageRef).replace("//", "/");
		try {
			url = new URL("file", null, -1, extImageRef);
			if (url!=null) return url;
			
		} catch (MalformedURLException urlEx) {
			//urlEx.printStackTrace();
		}
		
		// --- Nothing found ----------------------------------------
		return null;
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
		this.selectObject(pickedObject);
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
	 * Same as selectObject but optionally shows component settings dialog
	 * 
	 * @param object
	 * @param showComponentSettingsDialog - shows the dialog if true
	 */
	private void selectObject(Object object) {
		this.clearPickedObjects();

		if (object instanceof GraphNode) {
			this.setPickedObject((GraphElement) object);
			// --- Is that node a distribution node? ----------------
			List<NetworkComponent> netComps = graphController.getNetworkModel().getNetworkComponents((GraphNode) object);
			NetworkComponent disNode = graphController.getNetworkModel().getDistributionNode(netComps);
			if (disNode != null) {
				this.graphController.getNetworkModelUndoManager().selectNetworkComponent(disNode);
			}
			if (netComps.size() == 1) {
				this.graphController.getNetworkModelUndoManager().selectNetworkComponent(netComps.iterator().next());
				this.clearPickedObjects();
				this.setPickedObject((GraphElement) object);
			}

		} else if (object instanceof GraphEdge) {
			NetworkComponent netComp = graphController.getNetworkModel().getNetworkComponent((GraphEdge) object);
			this.setPickedObjects(graphController.getNetworkModel().getGraphElementsFromNetworkComponent(netComp));
			this.graphController.getNetworkModelUndoManager().selectNetworkComponent(netComp);

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
	
	
	/**
	 * Zoom one to one and move the focus according to the coordinate system source.
	 * @param visViewer the VisualizationViewer to use
	 */
	private void zoomOneToOneMoveFocus(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		this.setVisualizationViewerToFitToWindow(visViewer, false);
	}
	/**
	 * Zooms that the graph fits to the window.
	 * @param visViewer the VisualizationViewer to use
	 */
	private void zoomToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		this.setVisualizationViewerToFitToWindow(visViewer, true);
	}
	/**
	 * Sets the visualization viewer to fit to window.
	 *
	 * @param visViewer the vis viewer
	 * @param scaleAtCoordinateSource the scale at coordinate source
	 */
	private void setVisualizationViewerToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer, boolean scaleAtCoordinateSource) {
		
		if (visViewer.getVisibleRect().isEmpty()) return;

		// ----------------------------------------------------------
		// --- Get coordinate systems position ----------------------
		Point2D coordinateSourcePoint = CoordinateSystemSourcePosition.getCoordinateSystemSourcePointInVisualizationViewer(visViewer, this.getGraphEnvironmentController().getNetworkModel().getLayoutSettings());
		this.setDefaultScaleAtPoint(coordinateSourcePoint);
		
		// ----------------------------------------------------------
		// --- Reset view and layout to identity -------------------- 
		visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
		visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
		
		// ----------------------------------------------------------
		// --- Calculate the movement in the view -------------------
		Rectangle2D rectGraph = GraphGlobals.getGraphSpreadDimension(visViewer.getGraphLayout().getGraph());
		double moveX = (rectGraph.getX() * (-1)) + this.graphMargin;
		double moveY = (rectGraph.getY() * (-1)) + this.graphMargin;

		// --- Transform coordinate to LayoutSettings ---------------
		TransformerForGraphNodePosition<GraphNode, GraphEdge> positionTransformer = new TransformerForGraphNodePosition<>(this.getGraphEnvironmentController());
		Point2D visualPosition = positionTransformer.transform(new Point2D.Double(moveX, moveY));
		moveX = visualPosition.getX() + coordinateSourcePoint.getX();
		moveY = visualPosition.getY() + coordinateSourcePoint.getY();
		
		// --- Set focus movement -----------------------------------
		visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).translate(moveX, moveY);
		if (scaleAtCoordinateSource==false) return;

		
		// ----------------------------------------------------------		
		// --- Calculate the scaling --------------------------------
		double graphWidth = rectGraph.getWidth() + 2 * this.graphMargin;
		double graphHeight = rectGraph.getHeight() + 2 * this.graphMargin;
		Point2D farthestCorner = positionTransformer.transform(new Point2D.Double(graphWidth, graphHeight));
		graphWidth = Math.abs(farthestCorner.getX());
		graphHeight = Math.abs(farthestCorner.getY());
		
		double visWidth = visViewer.getVisibleRect().getWidth();
		double visHeight = visViewer.getVisibleRect().getHeight();

		float scaleX = (float) (visWidth / graphWidth);
		float scaleY = (float) (visHeight / graphHeight);
		if (scaleX > 1) scaleX = 1;
		if (scaleY > 1) scaleY = 1;

		float scale = scaleX;
		if (scaleX > scaleY) {
			scale = scaleY;
		}
		
		// --- Set scaling ------------------------------------------
		if (scale != 0 && scale != 1) {
			this.scalingControl.scale(visViewer, scale, coordinateSourcePoint);
		}
	}
	
	/**
	 * Zoom to the selected component.
	 */
	private void zoomToComponent() {
		
		Set<GraphNode> nodesPicked = this.getVisualizationViewer().getPickedVertexState().getPicked();
		if (nodesPicked.size()!=0) {
			List<NetworkComponent> components = this.graphController.getNetworkModel().getNetworkComponentsFullySelected(nodesPicked);
			if (components!=null && components.size()!=0) {
				// --- Get the dimension of the selected nodes ------ 
				Rectangle2D areaSelected = GraphGlobals.getGraphSpreadDimension(nodesPicked);
				Point2D areaCenter = new Point2D.Double(areaSelected.getCenterX(), areaSelected.getCenterY());
				// --- Create temporary GraphNode -------------------
				GraphNode tmpNode = new GraphNode("tmPCenter", areaCenter);
				this.getVisualizationViewer().getGraphLayout().getGraph().addVertex(tmpNode);
				// --- Get the needed positions ---------------------
				Point2D tmpNodePos = this.getVisualizationViewer().getGraphLayout().transform(tmpNode);
				Point2D visViewCenter = this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(this.getVisualizationViewer().getCenter());
				// --- Calculate movement ---------------------------
				final double dx = (visViewCenter.getX() - tmpNodePos.getX());
				final double dy = (visViewCenter.getY() - tmpNodePos.getY());
				// --- Remove temporary GraphNode and move view -----
				this.getVisualizationViewer().getGraphLayout().getGraph().removeVertex(tmpNode);
				this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy); 
			}
		}
	}

	
	
	/**
	 * Gets the VisualizationViewer for the satellite view
	 * @return The VisualizationViewer
	 */
	public SatelliteVisualizationViewer<GraphNode, GraphEdge> getSatelliteVisualizationViewer() {
		if (visViewSatellite==null) {
			
			// --- Set dimension and create a new SatelliteVisualizationViewer ----
			visViewSatellite = new SatelliteVisualizationViewer<GraphNode, GraphEdge>(this.getVisualizationViewer(), this.getDimensionOfSatelliteVisualizationViewer());
			visViewSatellite.scaleToLayout(this.scalingControl);
			visViewSatellite.setGraphMouse(new SatelliteGraphMouse(1/1.1f, 1.1f));
			
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
	
	/**
	 * Converts an {@link Image} to a {@link BufferedImage}
	 * @param image The image
	 * @return The buffered image
	 */
	private BufferedImage convertToBufferedImage(Image image){
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D biGraphics = bufferedImage.createGraphics();
		biGraphics.drawImage(image, 0, 0, null);
		biGraphics.dispose();
		return bufferedImage;
	}
	
	/**
	 * Replaces a specified color with another one in an image.
	 * @param image The image 
	 * @param oldColor The color that will be replaced
	 * @param newColor The new color
	 * @return The image
	 */
	private BufferedImage exchangeColor(BufferedImage image, Color oldColor, Color newColor){
		for(int x=0; x<image.getWidth(); x++){
			for(int y=0; y<image.getHeight(); y++){
				Color currentColor = new Color(image.getRGB(x, y));
				if(currentColor.equals(oldColor)){
					image.setRGB(x, y, newColor.getRGB());
				}
			}
			
		}
		return image;
	}
	
	/**
	 * Checks if the coordinate system has changed.
	 * @return true, if the 
	 */
	private boolean hasChangedCoordinateSystem() {
		LayoutSettings layoutSetttings = this.getGraphEnvironmentController().getNetworkModel().getLayoutSettings();
		boolean isChangedXDirection = layoutSetttings.getCoordinateSystemXDirection()!=this.xDirectionReminder;
		boolean isChangedYDirection = layoutSetttings.getCoordinateSystemYDirection()!=this.yDirectionReminder;
		return isChangedXDirection | isChangedYDirection;
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
				this.setEdgeShapeTransformer();
				if (this.hasChangedCoordinateSystem()==true) {
					this.reLoadGraph();
				}
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Reload:
				this.reLoadGraph();
				break;

			case NetworkModelNotification.NETWORK_MODEL_Satellite_View:
				Boolean visible = (Boolean) infoObject;
				this.getSatelliteDialog().setVisible(visible);
				this.zoomToFitToWindow(this.getSatelliteVisualizationViewer());
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

			case NetworkModelNotification.NETWORK_MODEL_Component_Select:
				this.selectObject(infoObject);
				break;

			case NetworkModelNotification.NETWORK_MODEL_Zoom_Fit2Window:
				this.zoomToFitToWindow(this.getVisualizationViewer());
				break;

			case NetworkModelNotification.NETWORK_MODEL_Zoom_One2One:
				this.zoomOneToOneMoveFocus(this.getVisualizationViewer());
				break;
				
			case NetworkModelNotification.NETWORK_MODEL_Zoom_Component:
				this.zoomToComponent();
				break;

			case NetworkModelNotification.NETWORK_MODEL_Zoom_In:
				this.getScalingControl().scale(this.getVisualizationViewer(), 1.1f, this.getDefaultScaleAtPoint());
				break;

			case NetworkModelNotification.NETWORK_MODEL_Zoom_Out:
				this.getScalingControl().scale(this.getVisualizationViewer(), 1 / 1.1f, this.getDefaultScaleAtPoint());
				break;

			case NetworkModelNotification.NETWORK_MODEL_ExportGraphAsImage:
				this.exportAsImage();
				break;

			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking:
				this.getVisualizationViewer().setGraphMouse(this.getPluggableGraphMouse());
				break;

			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Transforming:
				this.getVisualizationViewer().setGraphMouse(this.getDefaultModalGraphMouse());
				break;

			default:
				break;
			}

		}

	}

}
