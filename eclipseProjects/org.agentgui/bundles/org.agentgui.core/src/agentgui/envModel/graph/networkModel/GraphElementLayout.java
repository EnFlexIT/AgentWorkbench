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
package agentgui.envModel.graph.networkModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * The Class GraphElementLayout.
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphElementLayout {

	private GraphElement myGraphElement = null;
	
	private NetworkModel networkModel = null;
	private TreeMap<String, DomainSettings> domainHash = null;
	private TreeMap<String, ComponentTypeSettings> ctsHash = null;

	private DomainSettings myDomain = null;
	private ComponentTypeSettings myComponentTypeSettings = null;
	
	private boolean distributionNode = false;
	private boolean clusterComponent = false;
	
	private boolean markerShow = false;
	private float markerStrokeWidth = 0;
	private Color markerColor = new Color(255,0,0, 140);
	
	private float size = 0;
	private Color color = Color.BLACK;
	private Color colorPicked = Color.YELLOW;
	private String labelText = null;
	private boolean showLabel = true;
	private String imageReference = null;
	private String shapeForm = null;
	
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
	 * Sets the network model.
	 * @param networkModel the new network model
	 */
	public void setNetworkModel(NetworkModel networkModel) {
		
		if (networkModel==null) {
			// --- There is nothing we can do -------------
			return;
		}
		
		// --- Set the Layout of the component ------------
		this.networkModel = networkModel;
		this.domainHash = this.networkModel.getGeneralGraphSettings4MAS().getDomainSettings();
		this.ctsHash = this.networkModel.getGeneralGraphSettings4MAS().getCurrentCTS();
		
		if (this.myGraphElement instanceof GraphNode) {
			this.setGraphNodeValues();
		} else if (this.myGraphElement instanceof GraphEdge) {
			this.setGraphEdgeValues();
		}
		
		this.networkModel = null;
		this.domainHash = null;
		this.ctsHash = null;
		
	}
	
	/**
	 * Sets the graph node values.
	 */
	private void setGraphNodeValues() {
		
		// --- Set default values ----------------------------------------
		this.size = domainHash.get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME).getVertexSize();
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
		HashSet<NetworkComponent> componentHashSet = this.networkModel.getNetworkComponents(graphNode);
		NetworkComponent distributionNode = networkModel.containsDistributionNode(componentHashSet);
		if (distributionNode!=null) {
			// -----------------------------------------------------------
			// --- DistributionNode --------------------------------------
			// -----------------------------------------------------------
			myComponentTypeSettings = ctsHash.get(distributionNode.getType());
			if (myComponentTypeSettings!=null) {
				
				this.setDistributionNode(true);
				
				this.myDomain = this.domainHash.get(myComponentTypeSettings.getDomain());
				
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
				this.imageReference = myComponentTypeSettings.getEdgeImage();
				if (this.imageReference != null) {
					if (this.imageReference.equals("MissingIcon")==false) {
						this.shapeForm = GeneralGraphSettings4MAS.SHAPE_IMAGE_SHAPE;
					}
				}
			}
			
		} else {
			if (componentHashSet.iterator().hasNext()) {
				// -------------------------------------------------------
				// --- Normal node or ClusterNode ------------------------
				// -------------------------------------------------------
				ArrayList<ClusterNetworkComponent> clusterHash = this.networkModel.getClusterNetworkComponents(componentHashSet);
				if (componentHashSet.size()==1 && clusterHash.size()==1 && this.networkModel.isFreeGraphNode(graphNode)==false) {
					// ---------------------------------------------------
					// --- Central GraphNode of a cluster component ------
					this.setClusterComponent(true);
					
					ClusterNetworkComponent cnc = clusterHash.get(0);
					String domain = cnc.getDomain();
					if (domain != null) {
						if (domain.equals("") == false) {
							myDomain = domainHash.get(domain);
						}
					}
					if (myDomain == null) {
						myDomain = domainHash.get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
					}
					this.size = myDomain.getVertexSize() * 3;
					this.color = new Color(Integer.parseInt(myDomain.getVertexColor()));
					this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
					this.labelText = cnc.getId();
					this.showLabel = true;
					this.imageReference = null;
					this.shapeForm = myDomain.getClusterShape();
					
				} else {
					// ---------------------------------------------------
					// --- Normal node -----------------------------------
					// ---------------------------------------------------
					NetworkComponent component = componentHashSet.iterator().next();
					if (component instanceof ClusterNetworkComponent) {
						// --- Outer GraphNode of a cluster found --------
						ClusterNetworkComponent cnc = (ClusterNetworkComponent) component;
						String domain = cnc.getDomain();
						if (domain != null) {
							if (domain.equals("") == false) {
								myDomain = domainHash.get(domain);
							}
						}
						if (myDomain == null) {
							myDomain = domainHash.get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
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
						if (myComponentTypeSettings!=null) {
							myDomain = domainHash.get(myComponentTypeSettings.getDomain());
							this.size = myDomain.getVertexSize();
							this.color = new Color(Integer.parseInt(myDomain.getVertexColor()));
							this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
							this.labelText = myGraphElement.getId();
							this.showLabel = myDomain.isShowLabel();
							this.imageReference = null;
						}	
					}// end (component instanceof ClusterNetworkComponent) 
					
				}
			}//end (componentHashSet.iterator().hasNext())
		}		
	}
	
	/**
	 * Sets the graph edge values.
	 */
	private void setGraphEdgeValues() {
		
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
		if (networkComponent == null) {
			System.out.println("Graph Element Layout: NetworkComponent for GraphEdge '" + graphEdge.getId() + "' not found!");
			return;
		}
		if (networkComponent instanceof ClusterNetworkComponent) {
			
			this.setClusterComponent(true);
			
			ClusterNetworkComponent clusterNetworkComponent = (ClusterNetworkComponent) networkComponent;
			this.myDomain = domainHash.get(clusterNetworkComponent.getDomain());
			
			this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
			this.labelText = graphEdge.getId();
			this.showLabel = false;
			this.imageReference = null;
			
		} else {
			
			this.myComponentTypeSettings = ctsHash.get(networkComponent.getType());
			this.myDomain = domainHash.get(myComponentTypeSettings.getDomain());
			
			this.size = myComponentTypeSettings.getEdgeWidth();
			this.color = new Color(Integer.parseInt(myComponentTypeSettings.getColor()));
			this.colorPicked = new Color(Integer.parseInt(myDomain.getVertexColorPicked()));
			this.labelText = graphEdge.getId();
			this.showLabel = myComponentTypeSettings.isShowLabel();
			this.imageReference = myComponentTypeSettings.getEdgeImage();
			
		}
		
	}
	
	/**
	 * Returns a copy of the current layout.
	 *
	 * @param graphElement the graph element
	 * @return the copy
	 */
	public GraphElementLayout getCopy(GraphElement graphElement) {
		
		if (isDistributionNode()==true || isClusterComponent()==true) {
			return null;
		}
		
		// --- Create a copy of the Layout --------------------------
		GraphElementLayout copy = new GraphElementLayout(graphElement);
		
		copy.setShowLabel(this.isShowLabel());
		if (this.getLabelText()!=null) {
			copy.setLabelText(new String(this.getLabelText()));	
		}
		
		copy.setSize(this.getSize());
		
		if (this.getImageReference()!=null) {
			copy.setImageReference(new String(this.getImageReference()));	
		}
		
		copy.setColor(new Color(this.getColor().getRed(), this.getColor().getGreen(), this.getColor().getBlue()));
		copy.setColorPicked(new Color(this.getColorPicked().getRed(), this.getColorPicked().getGreen(), this.getColorPicked().getBlue()));
		
		copy.setMarkerShow(this.isMarkerShow());
		copy.setMarkerColor(new Color(this.getMarkerColor().getRed(), this.getMarkerColor().getGreen(), this.getMarkerColor().getBlue()));
		copy.setMarkerStrokeWidth(this.getMarkerStrokeWidth());
		
		if (this.getShapeForm()!=null) {
			copy.setShapeForm(new String(this.getShapeForm()));	
		}
		return copy;
	}
	
	
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
	 * @param isClusterComponent 
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
