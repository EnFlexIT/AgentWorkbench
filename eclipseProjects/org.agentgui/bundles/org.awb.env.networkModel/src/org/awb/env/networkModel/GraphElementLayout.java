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
package org.awb.env.networkModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.settings.ComponentTypeSettings;
import org.awb.env.networkModel.settings.DomainSettings;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;

/**
 * The Class GraphElementLayout.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphElementLayout {

	private GraphElement myGraphElement;
	
	// --- Information temporary available ----------------------------------------------
	private NetworkModel networkModel;
	private TreeMap<String, DomainSettings> domainHash;
	private TreeMap<String, ComponentTypeSettings> ctsHash;

	// --- Information available after initialization and by using getter methods ------- 
	private DomainSettings myDomain;
	private ComponentTypeSettings myComponentTypeSettings;
	
	private boolean distributionNode = false;
	private boolean clusterComponent = false;
	
	private float size = 0;
	private Color color = Color.BLACK;
	private Color colorPicked = Color.YELLOW;
	private String labelText;
	private boolean showLabel = true;
	private String imageReference;
	private String shapeForm;
	
	private boolean markerShow = false;
	private float markerStrokeWidth = 0;
	private Color markerColor = new Color(255,0,0, 140);
	
	
	/**
	 * Instantiates a new graph element layout.
	 * @param graphElement the graph element
	 */
	public GraphElementLayout(GraphElement graphElement) {
		this.myGraphElement = graphElement;
	}
	
	/**
	 * Returns the graph element.
	 * @return the myGraphElement
	 */
	public GraphElement getGraphElement() {
		return myGraphElement;
	}
	/**
	 * Sets the graph element.
	 * @param myGraphElement the myGraphElement to set
	 */
	public void setGraphElement(GraphElement myGraphElement) {
		this.myGraphElement = myGraphElement;
	}
	
	/**
	 * Sets the current {@link GraphEnvironmentController} to the layout that will set the 
	 * individual layout parameters for {@link GraphNode}s or {@link GraphEdge}s.
	 *
	 * @param graphController the current graph controller
	 */
	public void setGraphElementLayout(GraphEnvironmentController graphController) {
		// --- Early return? ------------------------------
		if (graphController==null) return;
		this.setGraphElementLayout(graphController.getNetworkModel());
	}
	/**
	 * Sets the current {@link NetworkModel} to the layout that will set the 
	 * individual layout parameters for {@link GraphNode}s or {@link GraphEdge}s.
	 *
	 * @param networkModel the new graph element layout
	 */
	public void setGraphElementLayout(NetworkModel networkModel) {
		
		this.networkModel = networkModel;
		this.domainHash = this.networkModel.getGeneralGraphSettings4MAS().getDomainSettings();
		this.ctsHash = this.networkModel.getGeneralGraphSettings4MAS().getCurrentCTS();
		
		// --- Set the Layout of the component ------------
		if (this.myGraphElement instanceof GraphNode) {
			this.setGraphNodeLayout();
		} else if (this.myGraphElement instanceof GraphEdge) {
			this.setGraphEdgeLayout();
		}
		
		// --- Forget information sources -----------------
		this.networkModel = null;
		this.domainHash = null;
		this.ctsHash = null;
	}
	
	/**
	 * Sets the layout parameter for {@link GraphNode}s.
	 */
	private void setGraphNodeLayout() {
		
		// --- Set default values ----------------------------------------
		this.size = this.domainHash.get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME).getVertexSize();
		this.color = GeneralGraphSettings4MAS.DEFAULT_VERTEX_COLOR;
		this.colorPicked = GeneralGraphSettings4MAS.DEFAULT_VERTEX_PICKED_COLOR;
		this.labelText = this.myGraphElement.getId();
		this.showLabel = true;
		this.imageReference = null;
		this.shapeForm = GeneralGraphSettings4MAS.SHAPE_ELLIPSE;
		
		this.setDistributionNode(false);
		this.setClusterComponent(false);
		
		// --- Evaluate the GraphNode ------------------------------------
		GraphNode graphNode = (GraphNode) this.myGraphElement;
		List<NetworkComponent> componentList = this.networkModel.getNetworkComponents(graphNode);
		if (componentList.size()==0) return;
		
		NetworkComponent distributionNode = this.networkModel.getDistributionNode(componentList);
		if (distributionNode!=null) {
			// -----------------------------------------------------------
			// --- DistributionNode --------------------------------------
			// -----------------------------------------------------------
			this.myComponentTypeSettings = ctsHash.get(distributionNode.getType());
			if (this.myComponentTypeSettings!=null) {
				
				this.setDistributionNode(true);
				
				this.myDomain = this.domainHash.get(this.myComponentTypeSettings.getDomain());
				
				String labelText = null;
				boolean showNodeLabel = this.myDomain.isShowLabel();
				boolean showComponentlabel = myComponentTypeSettings.isShowLabel();
				if (showComponentlabel==true) {
					labelText = distributionNode.getId();
				}
				if (showNodeLabel==true) {
					if (labelText==null) {
						labelText = this.myGraphElement.getId();
					} else {
						labelText += " (" + this.myGraphElement.getId() + ")";
					}
				}
				
				this.showLabel = showNodeLabel | showComponentlabel;
				this.labelText = labelText;
				this.size = (int) myComponentTypeSettings.getEdgeWidth();
				this.color = new Color(Integer.parseInt(myComponentTypeSettings.getColor()));
				this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
				this.imageReference = this.myComponentTypeSettings.getEdgeImage();
				if (this.imageReference!=null) {
					if (this.imageReference.equals("MissingIcon")==false) {
						this.shapeForm = GeneralGraphSettings4MAS.SHAPE_IMAGE_SHAPE;
					}
				}
			}
			
		} else {
			// -----------------------------------------------------------
			// --- Normal node OR ClusterNode ----------------------------
			// -----------------------------------------------------------
			List<ClusterNetworkComponent> clusterNetCompList = this.networkModel.getClusterNetworkComponents(new ArrayList<>(componentList));
			if (componentList.size()==1 && clusterNetCompList.size()==1 && this.networkModel.isFreeGraphNode(graphNode)==false) {
				// -------------------------------------------------------
				// --- Central GraphNode of a cluster component ----------
				this.setClusterComponent(true);
				
				ClusterNetworkComponent cnc = clusterNetCompList.get(0);
				String domain = cnc.getDomain();
				if (domain!=null && domain.isEmpty()==false) {
					this.myDomain = this.domainHash.get(domain);
				}
				if (this.myDomain == null) {
					this.myDomain = this.domainHash.get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
				}
				this.size = myDomain.getVertexSize() * 3;
				this.color = new Color(Integer.parseInt(myDomain.getVertexColor()));
				this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
				this.labelText = cnc.getId();
				this.showLabel = true;
				this.imageReference = null;
				this.shapeForm = myDomain.getClusterShape();
				
			} else {
				// -------------------------------------------------------
				// --- Normal node ---------------------------------------
				// -------------------------------------------------------
				NetworkComponent component = componentList.iterator().next();
				if (component instanceof ClusterNetworkComponent) {
					// --- Outer GraphNode of a cluster found ------------
					ClusterNetworkComponent cnc = (ClusterNetworkComponent) component;
					String domain = cnc.getDomain();
					if (domain!=null && domain.isEmpty()==false) {
						this.myDomain = this.domainHash.get(domain);
					}
					if (this.myDomain==null) {
						this.myDomain = this.domainHash.get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
					}
					this.size = myDomain.getVertexSize();
					this.color = new Color(Integer.parseInt(myDomain.getVertexColor()));
					this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
					this.labelText = cnc.getId();
					this.showLabel = myDomain.isShowLabel();;
					this.imageReference = null;
					
				} else {
					// --- 
					this.myComponentTypeSettings = ctsHash.get(component.getType());
					if (this.myComponentTypeSettings!=null) {
						this.myDomain = this.domainHash.get(this.myComponentTypeSettings.getDomain());
						this.size = myDomain.getVertexSize();
						this.color = new Color(Integer.parseInt(myDomain.getVertexColor()));
						this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
						this.labelText = myGraphElement.getId();
						this.showLabel = myDomain.isShowLabel();
						this.imageReference = null;
					}	
				}// end (component instanceof ClusterNetworkComponent) 
				
			}
		}		
	}
	
	/**
	 * Sets the layout parameters for {@link GraphEdge}s.
	 */
	private void setGraphEdgeLayout() {
		
		// --- Set default values ----------------------------------------
		this.size = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
		this.color = GeneralGraphSettings4MAS.DEFAULT_EDGE_COLOR;
		this.colorPicked = GeneralGraphSettings4MAS.DEFAULT_EDGE_PICKED_COLOR;
		this.labelText = this.myGraphElement.getId();
		this.showLabel = true;
		this.imageReference = null;
		
		this.setDistributionNode(false);
		this.setClusterComponent(false);

		// --- Evaluate the GraphEdge ------------------------------------
		GraphEdge graphEdge = (GraphEdge) this.myGraphElement;
		NetworkComponent networkComponent = this.networkModel.getNetworkComponent(graphEdge);
		if (networkComponent==null) {
			System.out.println("Graph Element Layout: NetworkComponent for GraphEdge '" + graphEdge.getId() + "' not found!");
			return;
		}
		
		// --- Set values according to Domain and component type settings - 
		this.labelText = graphEdge.getId();

		if (networkComponent instanceof ClusterNetworkComponent) {
			
			this.setClusterComponent(true);
			
			ClusterNetworkComponent clusterNetworkComponent = (ClusterNetworkComponent) networkComponent;

			this.showLabel = false;
			this.imageReference = null;

			this.myDomain = this.domainHash.get(clusterNetworkComponent.getDomain());
			if (this.myDomain!=null) {
				this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
			}
			
		} else {
			
			this.myComponentTypeSettings = this.ctsHash.get(networkComponent.getType());
			if (this.myComponentTypeSettings!=null) {
				// --- Set values derived from ComponentTypeSettings ----------
				this.size = this.myComponentTypeSettings.getEdgeWidth();
				this.color = new Color(Integer.parseInt(this.myComponentTypeSettings.getColor()));
				this.showLabel = this.myComponentTypeSettings.isShowLabel();
				this.imageReference = this.myComponentTypeSettings.getEdgeImage();

				this.myDomain = domainHash.get(this.myComponentTypeSettings.getDomain());
				if (this.myDomain!=null) {
					// --- Set values derived from DomainSettings -------------
					this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
				}
			}
		}
		
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here, regular getter and setter methods for the actual settings ---------
	// ----------------------------------------------------------------------------------	
	/**
	 * Sets the DistributionNode.
	 * @param isDistributionNode the new distribution node
	 */
	public void setDistributionNode(boolean isDistributionNode) {
		this.distributionNode = isDistributionNode;
	}
	/**
	 * Checks if the current GraphElement is a DistributionNode.
	 * @return true, if this is a DistributionNode
	 */
	public boolean isDistributionNode() {
		return distributionNode;
	}

	/**
	 * Sets, if this component belongs to a cluster component.
	 * @param isClusterComponent the new indicator for a cluster component
	 */
	public void setClusterComponent(boolean isClusterComponent) {
		this.clusterComponent = isClusterComponent;
	}
	/**
	 * Checks if the current GraphElement belongs to a cluster component.
	 * @return true, if this GraphElement belongs to a cluster component
	 */
	public boolean isClusterComponent() {
		return clusterComponent;
	}

	
	/**
	 * Gets the label text.
	 * @return the labelText
	 */
	public String getLabelText() {
		return labelText;
	}
	/**
	 * Sets the label text.
	 * @param labelText the new label text
	 */
	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	/**
	 * Checks if is show label.
	 * @return the showLable
	 */
	public boolean isShowLabel() {
		return showLabel;
	}
	/**
	 * Sets the show label.
	 * @param showLabel the new show label
	 */
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}
	
	
	/**
	 * Gets the size.
	 * @return the size
	 */
	public float getSize() {
		return size;
	}
	/**
	 * Sets the size.
	 * @param size the size to set
	 */
	public void setSize(float size) {
		this.size = size;
	}

	/**
	 * Gets the color.
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * Sets the color.
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the color picked.
	 * @return the colorPicked
	 */
	public Color getColorPicked() {
		return colorPicked;
	}
	/**
	 * Sets the color picked.
	 * @param colorPicked the colorPicked to set
	 */
	public void setColorPicked(Color colorPicked) {
		this.colorPicked = colorPicked;
	}

	/**
	 * Gets the image reference.
	 * @return the imageReference
	 */
	public String getImageReference() {
		return imageReference;
	}
	/**
	 * Sets the image reference.
	 * @param imageReference the imageReference to set
	 */
	public void setImageReference(String imageReference) {
		this.imageReference = imageReference;
	}
	
	/**
	 * Gets the shape form.
	 * @return the shape form
	 */
	public String getShapeForm() {
		return shapeForm;
	}
	/**
	 * Sets the shape form.
	 * @param shapeForm the new shape form
	 */
	public void setShapeForm(String shapeForm) {
		this.shapeForm = shapeForm;
	}

	/**
	 * Checks if is marker show.
	 * @return the markerShow
	 */
	public boolean isMarkerShow() {
		return markerShow;
	}
	/**
	 * Sets the marker show.
	 * @param markerShow the markerShow to set
	 */
	public void setMarkerShow(boolean markerShow) {
		this.markerShow = markerShow;
	}
	
	/**
	 * Gets the marker stroke width.
	 * @return the markerStrokeWidth
	 */
	public float getMarkerStrokeWidth() {
		return markerStrokeWidth;
	}
	/**
	 * Sets the marker stroke width.
	 * @param markerStrokeWidth the markerStrokeWidth to set
	 */
	public void setMarkerStrokeWidth(float markerStrokeWidth) {
		this.markerStrokeWidth = markerStrokeWidth;
	}

	/**
	 * Returns the marker color.
	 * @return the markerColor
	 */
	public Color getMarkerColor() {
		return markerColor;
	}
	/**
	 * Sets the marker color.
	 * @param markerColor the markerColor to set
	 */
	public void setMarkerColor(Color markerColor) {
		this.markerColor = markerColor;
	}

}
