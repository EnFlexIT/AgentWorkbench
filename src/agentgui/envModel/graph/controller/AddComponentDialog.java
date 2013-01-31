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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.components.ComponentTypeListElement;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.GraphNodePairs;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.prototypes.DistributionNode;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import agentgui.envModel.graph.prototypes.Star3GraphElement;
import agentgui.envModel.graph.prototypes.StarGraphElement;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Checkmark;
import javax.swing.SwingConstants;
import java.awt.Rectangle;

/**
 * Dialog for adding a new network component to the model.<br>
 * List of component types is displayed and on selecting a component type, the preview of the graph prototype is shown .<br>
 * Adds the selected component to the graph by merging the common selected nodes.
 * 
 * @see GraphEnvironmentControllerGUI
 * @see GraphEnvironmentController
 * @see GraphElementPrototype
 * 
 * @author Satyadeep - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AddComponentDialog extends JInternalFrame implements ActionListener {

    private static final long serialVersionUID = -7481141098749690137L;
    private final String pathImage = GraphGlobals.getPathImages(); // @jve:decl-index=0:
    
    private JLabel jLabelFilter = null;
    private JLabel jLabelInstructionMerge = null;
    private JLabel jLabelInstructionSelect = null;

    private JComboBox jComboBoxFilter = null;
    
    private JPanel jPanelFilter = null;
	private JPanel jPanelVisView = null;

	private JPanel jPanelTop = null;
    private JPanel jPanelBottom = null;
    private JScrollPane jScrollPane = null;
    
    private JList jListComponentTypes = null;
    private Vector<ComponentTypeListElement> componentTypeList = null;  
    
    private JToolBar jJToolBarBarVisViewLeft = null;

	private JButton jButtonSpin45 = null;
	private JButton jButtonSpin90 = null;
	private JButton jButtonSpin270 = null;
	private JButton jButtonSpin315 = null;
    
    private JButton jButtonAdd = null;
    private JButton jButtonClose = null;

    private GraphEnvironmentController graphController = null;
    private GraphEnvironmentControllerGUI graphControllerGUI = null;
    private BasicGraphGuiJDesktopPane graphDesktop = null;
    private BasicGraphGui basicGraphGui = null;
    
    private VisualizationViewer<GraphNode, GraphEdge> visViewer = null;

    private NetworkModel currNetworkModel = null;  //  @jve:decl-index=0:
    private ComponentTypeListElement currCtsListElement;
	private DomainSettings currDomainSetings;
	
	/** The graph element prototype of the selected component type. */
    private GraphElementPrototype currGraphElementPrototype = null; // @jve:decl-index=0:
	private GraphNode currGraphNodeSelected = null;

	private JSplitPane jSplitPaneContent = null;

	private JPanel jPanelSplitBottom = null;
	private JToolBar jJToolBarBarVisViewRight = null;

	
	/**
     * Gets the parent object and initializes.
     * @param controller the GraphEnvironmentController
     */
    public AddComponentDialog(GraphEnvironmentController controller) {
		this.graphController = controller;
		this.graphControllerGUI = (GraphEnvironmentControllerGUI)controller.getEnvironmentPanel();
		this.graphDesktop = this.graphControllerGUI.getBasicGraphGuiJDesktopPane();
		this.basicGraphGui = this.graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui();
		initialize();
    }

    /**
     * This method initializes this
     * @return void
     */
    private void initialize() {
		
    	this.setContentPane(getJSplitPaneContent());
		this.setTitle("Add Network Component");
		this.setBounds(new Rectangle(0, 0, 240, 410));
		this.setTitle(Language.translate(this.getTitle(), Language.EN));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.registerEscapeKeyStroke();
		
		this.setClosable(true);
		this.setMaximizable(false);
		this.setIconifiable(false);
		
		this.setAutoscrolls(true);
		this.setResizable(true);

		BasicInternalFrameUI ui = (BasicInternalFrameUI)this.getUI();
		ui.getNorthPane().remove(0);
		
		
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean aFlag) {
    	if (aFlag==true) {
    		if (this.graphDesktop!=null && this.isVisible()==false) {
    			// --- Add to the desktop ---------------------------
    			this.graphDesktop.add(this, JDesktopPane.PALETTE_LAYER);
    			// --- remind the old inverse divider location ------
    			int oldInverseDividerLocation = this.getHeight() - this.getJSplitPaneContent().getDividerLocation();
    			// --- calculate and set the new size ---------------
    			this.setSize(new Dimension(this.getWidth(), this.graphDesktop.getHeight()));
    			// --- set the new divider position -----------------
    			int newDividerLocation = this.graphDesktop.getHeight() - oldInverseDividerLocation;
    			this.getJSplitPaneContent().setDividerLocation(newDividerLocation);
        	}	
    	} 
		super.setVisible(aFlag);
    }
    
    /**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
                setVisible(false);
                dispose();
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    /**
	 * This method initializes jSplitPaneContent	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPaneContent() {
		if (jSplitPaneContent == null) {
			jSplitPaneContent = new JSplitPane();
			jSplitPaneContent.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneContent.setDividerSize(0);
			jSplitPaneContent.setDividerLocation(160);
			jSplitPaneContent.setContinuousLayout(true);
			jSplitPaneContent.setPreferredSize(new Dimension(234, 603));
			jSplitPaneContent.setTopComponent(getJPanelTop());
			jSplitPaneContent.setBottomComponent(getJPanelSplitBottom());
			jSplitPaneContent.setResizeWeight(1.0D);
		}
		return jSplitPaneContent;
	}

    /**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 0.1;
			gridBagConstraints1.insets = new Insets(5, 10, 0, 10);
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new Insets(10, 10, 0, 10);
			gridBagConstraints9.gridy = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridx = -1;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.insets = new Insets(10, 10, 0, 10);
			
			jLabelInstructionSelect = new JLabel();
			jLabelInstructionSelect.setText("Select a network component");
			jLabelInstructionSelect.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelInstructionSelect.setText(Language.translate(jLabelInstructionSelect.getText(), Language.EN));
			
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.add(jLabelInstructionSelect, gridBagConstraints7);
			jPanelTop.add(getJPanelFilter(), gridBagConstraints9);
			jPanelTop.add(getJScrollPane(), gridBagConstraints1);
		}
		return jPanelTop;
	}
	
	/**
	 * This method initializes jPanelNorth	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelFilter() {
		if (jPanelFilter == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.BOTH;
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints10.gridx = 1;
			
			jLabelFilter = new JLabel();
			jLabelFilter.setText("Domain Filter:");
			jLabelFilter.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelFilter = new JPanel();
			jPanelFilter.setLayout(new GridBagLayout());
			jPanelFilter.add(jLabelFilter, new GridBagConstraints());
			jPanelFilter.add(getJComboBoxFilter(), gridBagConstraints10);
		}
		return jPanelFilter;
	}

	/**
	 * This method initializes jComboBoxFilter	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxFilter() {
		if (jComboBoxFilter == null) {
			jComboBoxFilter = new JComboBox();
			jComboBoxFilter.setPreferredSize(new Dimension(100, 26));
		}
		return jComboBoxFilter;
	}
	 /**
     * This method initializes jScrollPane
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
		    jScrollPane = new JScrollPane();
		    jScrollPane.setPreferredSize(new Dimension(20, 200));
		    jScrollPane.setViewportView(getJListComponentTypes());
		}
		return jScrollPane;
    }
    /**
     * This method initializes componentTypesList
     * @return javax.swing.JList
     */
    private JList getJListComponentTypes() {
		if (jListComponentTypes == null) {
		    jListComponentTypes = new JList(this.getComponentTypeList());
		    jListComponentTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    jListComponentTypes.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				    if (!e.getValueIsAdjusting()) {
						// --- Set the current list element ---------
				    	currCtsListElement = (ComponentTypeListElement) jListComponentTypes.getSelectedValue();
						if (currCtsListElement==null) {
							currNetworkModel = null;
					    	currGraphElementPrototype = null;
					    	return;
						}
				    	// --- Set the current domain ---------------
				    	String domain = currCtsListElement.getDomain();
				    	if (domain==null) {
				    		currDomainSetings = graphController.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
				    	} else {
				    		currDomainSetings = graphController.getDomainSettings().get(domain);	
				    	}
				    	// --- Paint GraphPrototype, if available ---  
				    	String graphPrototype = currCtsListElement.getComponentTypeSettings().getGraphPrototype();
				    	if (graphPrototype!=null) {
							setPrototypePreview();	
						} else {
							String msg = Language.translate("The definition of the component contains no graph prototyp definition.", Language.EN);
							String titel = Language.translate("Missing definition of the Graph prototype for '" + currCtsListElement.getComponentName() + "'!", Language.EN);
							JOptionPane.showMessageDialog(getJPanelTop(), msg, titel, JOptionPane.WARNING_MESSAGE);
						}
				    }
				}
		    });
		}
		return jListComponentTypes;
    }
    
	/**
	 * This method initializes jPanelSplitBottom	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSplitBottom() {
		if (jPanelSplitBottom == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.insets = new Insets(5, 10, 0, 10);
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 4;
			gridBagConstraints5.ipadx = 0;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(10, 10, 10, 10);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridx = -1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weighty = 0.05;
			gridBagConstraints3.insets = new Insets(5, 10, 0, 10);

			jLabelInstructionMerge = new JLabel();
		    jLabelInstructionMerge.setText("Select a vertex to merge");
		    jLabelInstructionMerge.setHorizontalTextPosition(SwingConstants.TRAILING);
		    jLabelInstructionMerge.setHorizontalAlignment(SwingConstants.CENTER);
		    jLabelInstructionMerge.setFont(new Font("Dialog", Font.BOLD, 12));
		    jLabelInstructionMerge.setText(Language.translate(jLabelInstructionMerge.getText(), Language.EN));

			jPanelSplitBottom = new JPanel();
			jPanelSplitBottom.setLayout(new GridBagLayout());
			jPanelSplitBottom.add(jLabelInstructionMerge, gridBagConstraints6);
			jPanelSplitBottom.add(getJPanelVisView(), gridBagConstraints3);
			jPanelSplitBottom.add(getJPanelBottom(), gridBagConstraints5);
			
		}
		return jPanelSplitBottom;
	}
	
	/**
	 * This method initializes jPanelVisView	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelVisView() {
		if (jPanelVisView == null) {
			
			GridBagConstraints gridBagConstraintsLeft = new GridBagConstraints();
			gridBagConstraintsLeft.fill = GridBagConstraints.VERTICAL;
			gridBagConstraintsLeft.gridx = 0;
			gridBagConstraintsLeft.gridy = 0;
			gridBagConstraintsLeft.weighty = 0.0;
			gridBagConstraintsLeft.insets = new Insets(0, 0, 0, 5);
			
			GridBagConstraints gridBagConstraintsCenter = new GridBagConstraints();
			gridBagConstraintsCenter.fill = GridBagConstraints.BOTH;
			gridBagConstraintsCenter.gridx = 1;
			gridBagConstraintsCenter.gridy = 0;
			gridBagConstraintsCenter.weightx = 1.0;
			gridBagConstraintsCenter.weighty = 1.0;

			GridBagConstraints gridBagConstraintsRight = new GridBagConstraints();
			gridBagConstraintsRight.fill = GridBagConstraints.VERTICAL;
			gridBagConstraintsRight.gridx = 2;
			gridBagConstraintsRight.gridy = 0;
			gridBagConstraintsRight.weightx = 0.0;
			gridBagConstraintsRight.insets = new Insets(0, 5, 0, 0);

			jPanelVisView = new JPanel();
			jPanelVisView.setPreferredSize(new Dimension(100, 250));
			jPanelVisView.setLayout(new GridBagLayout());
			jPanelVisView.add(getJJToolBarBarVisViewLeft(), gridBagConstraintsLeft);
			jPanelVisView.add(getVisualizationViewer(), gridBagConstraintsCenter);
			jPanelVisView.add(getJJToolBarBarVisViewRight(), gridBagConstraintsRight);
		}
		return jPanelVisView;
	}

    /**
     * This method initializes jBottomPanel
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
		    GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		    gridBagConstraints11.anchor = GridBagConstraints.NORTHEAST;
		    gridBagConstraints11.gridx = 2;
		    gridBagConstraints11.gridy = 0;
		    gridBagConstraints11.ipadx = 0;
		    gridBagConstraints11.insets = new Insets(5, 5, 5, 0);
		    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		    gridBagConstraints4.insets = new Insets(5, 0, 5, 5);
		    gridBagConstraints4.gridy = 0;
		    gridBagConstraints4.ipadx = 0;
		    gridBagConstraints4.gridx = 1;
		    
		    jPanelBottom = new JPanel();
		    jPanelBottom.setLayout(new GridBagLayout());
		    jPanelBottom.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		    jPanelBottom.add(getJButtonClose(), gridBagConstraints4);
		    jPanelBottom.add(getJButtonAdd(), gridBagConstraints11);
		}
		return jPanelBottom;
    }
    /**
     * This method initializes btnCancel
     * @return javax.swing.JButton
     */
    private JButton getJButtonClose() {
		if (jButtonClose == null) {
		    jButtonClose = new JButton();
		    jButtonClose.setText("Close");
		    jButtonClose.setText(Language.translate(jButtonClose.getText(), Language.EN));
		    jButtonClose.setPreferredSize(new Dimension(95, 26));
		    jButtonClose.setFont(new Font("Dialog", Font.BOLD, 12));
		    jButtonClose.setForeground(new Color(153, 0, 0));
		    jButtonClose.addActionListener(this);
		}
		return jButtonClose;
    }
    /**
     * This method initializes btnOK
     * @return javax.swing.JButton
     */
    private JButton getJButtonAdd() {
		if (jButtonAdd == null) {
		    jButtonAdd = new JButton();
		    jButtonAdd.setText("Add");
		    jButtonAdd.setText(Language.translate(jButtonAdd.getText(), Language.EN));
		    jButtonAdd.setPreferredSize(new Dimension(95, 26));
		    jButtonAdd.setForeground(new Color(0, 153, 0));
		    jButtonAdd.setFont(new Font("Dialog", Font.BOLD, 12));
		    jButtonAdd.addActionListener(this);
		}
		return jButtonAdd;
    }
    
    /**
     * Initializes the VisualizationViewer
     * @return The VisualizationViewer
     */
    private VisualizationViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		// create VisualizationViewer if it is not there
		if (visViewer == null) {

			// --- Define the graph -------------
			Graph<GraphNode, GraphEdge> graph = new SparseGraph<GraphNode, GraphEdge>();
			// --- Define the GraphMouse --------
			PluggableGraphMouse pgm = new PluggableGraphMouse();
		    pgm.add(new PickingGraphMousePlugin<GraphNode, GraphEdge>());
			// --- Define the Layout ------------
		    Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
		    layout.setSize(new Dimension(120, 120));
		   
		    // --- Create VisualizationViewer ---
		    visViewer = new VisualizationViewer<GraphNode, GraphEdge>(layout);

		    visViewer.setLayout(new GridBagLayout());
		    visViewer.setGraphMouse(pgm);
		    visViewer.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			visViewer.setBackground(Color.WHITE);
			visViewer.setSize(new Dimension(150, 150));
			visViewer.setPreferredSize(new Dimension(250, 250));
		    
		    // --- Define a 
//			MutableTransformer mutableLayout = visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
//			mutableLayout.translate(120, 10);
	
			// --- Configure the vertex shape and size ------------------------
			visViewer.getRenderContext().setVertexShapeTransformer(new VertexShapeSizeAspect<GraphNode, GraphEdge>());
			
			// --- Configure node icons, if configured ------------------------		
			visViewer.getRenderContext().setVertexIconTransformer(new Transformer<GraphNode, Icon>(){
				
				@Override
				public Icon transform(GraphNode node) {
					
					boolean picked = visViewer.getPickedVertexState().isPicked(node);
					Icon icon = null;
					if (isCurrentComponentDistributionNode()) {
						
						String nodeImage = currCtsListElement.getComponentTypeSettings().getEdgeImage();
						if (nodeImage!=null) {
							if (nodeImage.equals("MissingIcon")==false) {
								// --- Icon reference found --- Start ---
								LayeredIcon layeredIcon = null;
								ImageIcon imageIcon = GraphGlobals.getImageIcon(nodeImage);
								if (imageIcon!=null) {
									layeredIcon = new LayeredIcon(imageIcon.getImage());
									if (layeredIcon!=null && picked==true){
										String checkColor = currDomainSetings.getVertexColorPicked();
										Checkmark checkmark = new Checkmark(new Color(Integer.parseInt(checkColor)));
										layeredIcon.add(checkmark);
									}
								} else {
									System.err.println("Could not find node image for '" + currCtsListElement.getComponentName() + "'");
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
			visViewer.setVertexToolTipTransformer(new Transformer<GraphNode, String>() {
				@Override
				public String transform(GraphNode edge) {
					return edge.getId();
				}
			});

			// --- Configure vertex colors ------------------------------------
			visViewer.getRenderContext().setVertexFillPaintTransformer(new Transformer<GraphNode, Paint>() {
				@Override
				public Paint transform(GraphNode node) {

					String colorString = null;
					String colorStringDefault = currDomainSetings.getVertexColor();
					Color defaultColor = new Color(Integer.parseInt(colorStringDefault));

					// --- Get color from component type settings -----
					try {
						// --- Get the vertex size from the component type settings -
						if (isCurrentComponentDistributionNode()) {
							// --- Distribution node ----------------
							if (visViewer.getPickedVertexState().isPicked(node) == true) {
								colorString = currDomainSetings.getVertexColorPicked();
							} else {
								colorString = currCtsListElement.getComponentTypeSettings().getColor();	
							}
						} else {
							if (visViewer.getPickedVertexState().isPicked(node) == true) {
								colorString = currDomainSetings.getVertexColorPicked();
							} else {
								colorString = currDomainSetings.getVertexColor();
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
			visViewer.getRenderContext().setVertexLabelTransformer(new Transformer<GraphNode, String>() {
					@Override
					public String transform(GraphNode node) {
						if (isCurrentComponentDistributionNode()) {
							if (currCtsListElement.getComponentTypeSettings().isShowLabel()) {
								return node.getId();
							}
						} else {
							if (currDomainSetings.isShowLabel()) {
								return node.getId();
							}
						}
						return null;
					}
				} // end transformer
			);

			// --- Configure edge colors --------------------------------------
			visViewer.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<GraphEdge, Paint>() {
				@Override
				public Paint transform(GraphEdge edge) {
					if (visViewer.getPickedEdgeState().isPicked(edge)) {
						return GeneralGraphSettings4MAS.DEFAULT_EDGE_PICKED_COLOR;
					}

					try {
						String colorString = currCtsListElement.getComponentTypeSettings().getColor();
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
			visViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<GraphEdge, String>() {
				@Override
				public String transform(GraphEdge edge) {
					// Get the path of the Image from the component type settings
					String textDisplay = "";
					try {
						String edgeImage = currCtsListElement.getComponentTypeSettings().getEdgeImage();
						boolean showLabel = currCtsListElement.getComponentTypeSettings().isShowLabel();

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
			visViewer.getRenderContext().setLabelOffset(0);
			visViewer.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<GraphNode, GraphEdge>(.5, .5));

			// --- Use straight lines as edges --------------------------------
			visViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<GraphNode, GraphEdge>());

			// --- Set edge width ---------------------------------------------
			visViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<GraphEdge, Stroke>() {
				@Override
				public Stroke transform(GraphEdge edge) {
					float edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
					try {
						edgeWidth = currCtsListElement.getComponentTypeSettings().getEdgeWidth();
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
		return visViewer;
    }
    
    // --- Begin sub class ----------------------
    /**
	 * Controls the shape, size, and aspect ratio for each vertex.
	 * 
	 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
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
							sizeSettings = (int) currCtsListElement.getComponentTypeSettings().getEdgeWidth();
						} else {
							// --- Normal node or ClusterNode ---------------------------- Start --
							sizeSettings = currDomainSetings.getVertexSize();
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
	
	/**
	 * This method initializes jJToolBarBarVisView	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJJToolBarBarVisViewLeft() {
		if (jJToolBarBarVisViewLeft == null) {
			jJToolBarBarVisViewLeft = new JToolBar();
			jJToolBarBarVisViewLeft.setFloatable(false);
			jJToolBarBarVisViewLeft.setSize(new Dimension(26, 260));
			jJToolBarBarVisViewLeft.setPreferredSize(new Dimension(26, 260));
			jJToolBarBarVisViewLeft.setOrientation(JToolBar.VERTICAL);
			jJToolBarBarVisViewLeft.setBorder(BorderFactory.createEmptyBorder());
			jJToolBarBarVisViewLeft.add(getJButtonSpin315());
			jJToolBarBarVisViewLeft.add(getJButtonSpin270());
			
		}
		return jJToolBarBarVisViewLeft;
	}
	/**
	 * This method initializes jJToolBarBarVisViewRight	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJJToolBarBarVisViewRight() {
		if (jJToolBarBarVisViewRight == null) {
			jJToolBarBarVisViewRight = new JToolBar();
			jJToolBarBarVisViewRight.setFloatable(false);
			jJToolBarBarVisViewRight.setSize(new Dimension(26, 26));
			jJToolBarBarVisViewRight.setPreferredSize(new Dimension(26, 26));
			jJToolBarBarVisViewRight.setOrientation(JToolBar.VERTICAL);
			jJToolBarBarVisViewRight.setBorder(BorderFactory.createEmptyBorder());
			jJToolBarBarVisViewRight.add(getJButtonSpin45());
			jJToolBarBarVisViewRight.add(getJButtonSpin90());
		}
		return jJToolBarBarVisViewRight;
	}
	/**
	 * This method initializes jButtonSpin45	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSpin45() {
		if (jButtonSpin45 == null) {
			jButtonSpin45 = new JButton();
			jButtonSpin45.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rotate45.png")));
			jButtonSpin45.setToolTipText("Rotate 45°");
			jButtonSpin45.addActionListener(this);
		}
		return jButtonSpin45;
	}
	/**
	 * This method initializes jButtonSpin90	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSpin90() {
		if (jButtonSpin90 == null) {
			jButtonSpin90 = new JButton();
			jButtonSpin90.setToolTipText("Rotate 90°");
			jButtonSpin90.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rotate90.png")));
			jButtonSpin90.addActionListener(this);
		}
		return jButtonSpin90;
	}
	/**
	 * This method initializes jButtonSpin315	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSpin315() {
		if (jButtonSpin315 == null) {
			jButtonSpin315 = new JButton();
			jButtonSpin315.setToolTipText("Rotate -45°");
			jButtonSpin315.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rotate315.png")));
			jButtonSpin315.addActionListener(this);
		}
		return jButtonSpin315;
	}
	/**
	 * This method initializes jButtonSpin270	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSpin270() {
		if (jButtonSpin270 == null) {
			jButtonSpin270 = new JButton();
			jButtonSpin270.setToolTipText("Rotate -90°");
			jButtonSpin270.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rotate270.png")));
			jButtonSpin270.addActionListener(this);
		}
		return jButtonSpin270;
	}

	 /**
     * This method takes the GraphPrototype class name as string and creates a graph of the prototype and shows a preview in the visualizationViewer
     * @param graphPrototype
     */
    private void setPrototypePreview() {

    	String componentName = this.currCtsListElement.getComponentName();
    	String graphPrototype = this.currCtsListElement.getComponentTypeSettings().getGraphPrototype();
    	
    	// --- Create the graph of the NetworkComponent -------------
		this.currNetworkModel = null;
    	this.currGraphElementPrototype = null;
    	
		try {
		    Class<?> theClass = Class.forName(graphPrototype);
		    currGraphElementPrototype = (GraphElementPrototype) theClass.newInstance();
		} catch (ClassNotFoundException ex) {
		    System.err.println("GraphElementPrototype class must be in class path.\n" + ex);
		} catch (InstantiationException ex) {
		    System.err.println("GraphElementPrototype class must be concrete.\n" + ex);
		} catch (IllegalAccessException ex) {
		    System.err.println("GraphElementPrototype class must have a no-arg constructor.\n" + ex);
		}
	
		if (currGraphElementPrototype!=null) {
		    
			// --- Create a new local NetworkModel ------------------
			this.currNetworkModel = new NetworkModel();
			this.currNetworkModel.setGeneralGraphSettings4MAS(this.graphController.getNetworkModel().getGeneralGraphSettings4MAS().getCopy());

			// --- Configure and add the prototype to the graph ----- 
			this.currGraphElementPrototype.setId(this.currNetworkModel.nextNetworkComponentID());
			this.currGraphElementPrototype.setType(componentName);
			this.currGraphElementPrototype.addToGraph(this.currNetworkModel.getGraph());
		    
		    // --- Paint and Layout the graph -----------------------
		    this.paintGraph();
		    
		    // --- Create the needed NetworkComponent --------------- 
	    	ComponentTypeSettings cts = this.currCtsListElement.getComponentTypeSettings();
			HashSet<GraphElement> elements = new HashSet<GraphElement>();

	    	// --- Get graph elements -------------------------------
	    	Graph<GraphNode, GraphEdge> currGraph = this.currNetworkModel.getGraph();    	
			for (GraphNode vertex : currGraph.getVertices()) {
				elements.add(vertex);
			}
			for (GraphEdge edge : currGraph.getEdges()) {
			    elements.add(edge);
			}
	    	
			// --- Create a NetworkComponent for the local NetworkModel -
			this.currNetworkModel.addNetworkComponent(new NetworkComponent(currGraphElementPrototype.getId(), componentName, graphPrototype, cts.getAgentClass(), elements, currGraphElementPrototype.isDirected()));
		    
		    // --- Set current selected node ------------------------ 
		    this.setSelectedGraphNode();
	
		}

    }
    
    /**
     * Repaints/Refreshes the visualisation viewer, with the given graph
     * @param graph The new graph to be painted
     */
    private void paintGraph() {
		
    	Graph<GraphNode, GraphEdge> graph = this.currNetworkModel.getGraph();
    	
		// --- Define default layout ------------
    	Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
//		layout.setSize(new Dimension(100, 100));
		
		// --------------------------------------
		// --- Some special cases ---------------
		// --------------------------------------
		if (graph.getEdgeCount()==0 && graph.getVertexCount()==1) {
			// ----------------------------------
			// --- Case DistributionNodes -------
			// ----------------------------------
			GraphNode node = graph.getVertices().iterator().next();
//			node.setPosition(new Point2D.Double(50, 50));
			
			layout = new StaticLayout<GraphNode, GraphEdge>(graph);
			layout.setInitializer(new Transformer<GraphNode, Point2D>() {
				public Point2D transform(GraphNode node) {
					return node.getPosition(); // The position is specified in the GraphNode instance
				}
			});
			// --- Pick node -------------------- 
			this.getVisualizationViewer().getPickedVertexState().pick(node, true);

			
		} else if (graph.getEdgeCount()==1) {
			// ----------------------------------
			// --- Case directed Graph ----------
			// ----------------------------------
			GraphEdge edge = graph.getEdges().iterator().next();
			EdgeType edgeType = graph.getEdgeType(edge);
			if (edgeType==EdgeType.DIRECTED) {
			
				GraphNode nodeSource = graph.getSource(edge);
				GraphNode nodeDestin = graph.getDest(edge);
				nodeSource.setPosition(new Point2D.Double(0, 50));
				nodeDestin.setPosition(new Point2D.Double(100, 50));
				
				layout = new StaticLayout<GraphNode, GraphEdge>(graph);
				layout.setInitializer(new Transformer<GraphNode, Point2D>() {
					public Point2D transform(GraphNode node) {
						return node.getPosition(); // The position is specified in the GraphNode instance
					}
				});
			}
		}

		if (this.currGraphElementPrototype instanceof StarGraphElement) {
			// ----------------------------------
			// --- Case StarGraphElement --------
			// ----------------------------------
			int nNodes = graph.getVertices().size();
			double angle = 0;
			double angleStep = 0;
			if ((nNodes-1)<=4) {
				angleStep = 2 * Math.PI / 4;
			} else {
				angleStep = 2 * Math.PI / (nNodes-1);
			}
			
			for (GraphNode node : graph.getVertices()) {
				int nEdges = graph.getIncidentEdges(node).size();
				
				if (nEdges==nNodes-1) {
					node.setPosition(new Point2D.Double(50, 50));
				} else {
					// --- outer Node found -----
					double x = 50 + 50 * Math.cos(angle);
					double y = 50 + 50 * Math.sin(angle);
					node.setPosition(new Point2D.Double(x, y));
					angle += angleStep;
				}
			}
			
			layout = new StaticLayout<GraphNode, GraphEdge>(graph);
			layout.setInitializer(new Transformer<GraphNode, Point2D>() {
				public Point2D transform(GraphNode node) {
					return node.getPosition(); // The position is specified in the GraphNode instance
				}
			});
			
			
		}
		
		// --------------------------------------
		// --- Set the graph to the layout ------
		visViewer.setGraphLayout(layout);
		visViewer.repaint();
		jPanelTop.repaint();
    }

    /**
     * Evaluates the current graph and sets the selected GraphNode.
     */
    private void setSelectedGraphNode() {
    	
    	// --- Is everything selected what is needed ? --------------
    	if (this.currCtsListElement==null || this.currGraphElementPrototype==null) {
    		this.currNetworkModel = null;
    		this.currGraphNodeSelected = null;
    		return;
    	}
    	
    	// --- Update position information of current graph ---------
    	Graph<GraphNode, GraphEdge> currGraph = this.currNetworkModel.getGraph();    	
		for (GraphNode vertex : currGraph.getVertices()) {
			int x = (int) getVisualizationViewer().getGraphLayout().transform(vertex).getX();
			int y = (int) getVisualizationViewer().getGraphLayout().transform(vertex).getY();
			vertex.setPosition(new Point(x, y));
		}
		
    	Set<GraphNode> nodeSet = getVisualizationViewer().getPickedVertexState().getPicked();
    	if (nodeSet.size() == 1) {
    		this.currGraphNodeSelected = nodeSet.iterator().next();
    	} else {
    		this.currGraphNodeSelected = null;
    	}
    	
    }
    
    /**
     * Gets the list of componentTypeSettings from the controller and 
     * returns it as a Vector<ComponentTypeListElement>
     * @see ComponentTypeListElement
     * @return Object[] - array of component types
     */
    private Vector<ComponentTypeListElement> getComponentTypeList() {
		
    	if (this.componentTypeList==null) {
    		// --- Create the Vector ----------------
        	this.componentTypeList = new Vector<ComponentTypeListElement>();
        	HashMap<String, ComponentTypeSettings> ctsHash = this.graphController.getComponentTypeSettings();
    		if (ctsHash != null) {
    			Iterator<String> ctsIt = ctsHash.keySet().iterator();
    		    while (ctsIt.hasNext()) {
    		    	String componentName = ctsIt.next(); 
    		    	ComponentTypeSettings cts = ctsHash.get(componentName); 
    		    	this.componentTypeList.add(new ComponentTypeListElement(componentName, cts));
    		    }
    		} 
    		// --- Sort the Vector ------------------
    		Collections.sort(this.componentTypeList, new Comparator<ComponentTypeListElement>() {
    			@Override
    			public int compare(ComponentTypeListElement cts1, ComponentTypeListElement cts2) {
    				if (cts1.getDomain().equals(cts2.getDomain())) {
    					return cts1.getComponentName().compareTo(cts2.getComponentName());
    				} else {
    					return cts1.getDomain().compareTo(cts2.getDomain());
    				}
    			}
    		});
    	}
		return this.componentTypeList;
    }
    
    /**
     * Checks if the current selection is a DistributionNode.
     * @return true, if it is a DistributionNode
     */
    private boolean isCurrentComponentDistributionNode() {
    	if (currCtsListElement==null) {
    		return false;
    	} else {
    		return currCtsListElement.getComponentTypeSettings().getGraphPrototype().equals(DistributionNode.class.getName());	
    	}
    }
    
    /**
     * Apply a shift for the new elements, in order to match the position 
     * of the currently selected node of the current setup		
     *
     * @param graphNode1 the graph node1
     * @param graphNode2 the graph node2
     */
    private void applyNodeShift2MergeWithNetworkModel(GraphNode graphNode1, GraphNode graphNode2) {
    	
    	double shiftX = 0;
		double shiftY = 0;
		if (graphNode1 != null) {
			shiftX = graphNode1.getPosition().getX() - graphNode2.getPosition().getX();
			shiftY = graphNode1.getPosition().getY() - graphNode2.getPosition().getY();
		}
		
		for (GraphNode node : this.currNetworkModel.getGraph().getVertices()) {
			double posX = node.getPosition().getX() + shiftX;
			double posY = node.getPosition().getY() + shiftY;
			node.setPosition(new Point2D.Double(posX, posY));			
		}
		
    }
    
    /**
     * This method can be used in order to produce components, by using this 
     * dialog as factory. Just specify the component by the name given in 
     * the ComponentTypeSettings dialog. 
     *
     * @param componentName the map node2 component
     * @return the NetworkModel for the component
     */
    public NetworkModel getNetworkModel4Component(String componentName) {

    	this.getJListComponentTypes().clearSelection();
    	// --- Select the right element from the list ---------------
    	Vector<ComponentTypeListElement> listModel = this.getComponentTypeList();
    	for (int i = 0; i < listModel.size(); i++) {
        	ComponentTypeListElement ctsElement = (ComponentTypeListElement) listModel.get(i);
        	if (ctsElement.getComponentName().equalsIgnoreCase(componentName)) {
        		this.getJListComponentTypes().setSelectedValue(ctsElement, true);
        		break;
        	}
		}
		return this.currNetworkModel;
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {

    	String msg = null;

    	if (ae.getSource()==this.getJButtonAdd() ) {
    		
    		// --------------------------------------------------------------------------
    		// --- Evaluate the node to which the user want to add a component ---------- 
    		// --------------------------------------------------------------------------
    		GraphNode graphNodeSelectedInMainGraph = this.basicGraphGui.getPickedSingleNode();
    		if (this.graphController.getNetworkModel().getGraph().getVertexCount()!=0) {
    			if (graphNodeSelectedInMainGraph==null) {
    		    	msg = "Please, select one free vertex in the overall network!";
    		    	JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
    		    	return;
    		    } else {
    				if(this.graphController.getNetworkModel().isFreeGraphNode(graphNodeSelectedInMainGraph)==false) {
    					msg = "Please, select one free vertex in the overall network!";
    					JOptionPane.showMessageDialog(this.graphControllerGUI, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN),JOptionPane.WARNING_MESSAGE);
    					return;
    				};
    		    }
			}

    		// --------------------------------------------------------------------------
    		// --- Evaluate the NetworkComponent that has to be added -------------------
    		// --------------------------------------------------------------------------
    		this.setSelectedGraphNode();
		    if (this.currNetworkModel==null) {
		    	msg = "Please, select the network component that you would like to add!";
		    	JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
		    	return;
		    }
		    if (this.currGraphNodeSelected==null) {
		    	msg = "Please, select one free vertex of the network component that you want to add!";
		    	JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
		    	return;
		    }

		    
			if (this.currGraphElementPrototype instanceof Star3GraphElement) {
			    // If the picked vertex is the center of the star, cannot add
			    Graph<GraphNode, GraphEdge> graph = getVisualizationViewer().getGraphLayout().getGraph();
			    // All the edges in the graph or incident on the pickedVertex => It is a center
			    if (graph.getEdgeCount() == graph.getIncidentEdges(this.currGraphNodeSelected).size()) {
			    	msg = "Select a vertex other than the center of the star";
			    	JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
			    	return;
			    }
			    
			} else if (this.currGraphElementPrototype instanceof DistributionNode) {
				// --- If the current selection of the main graph is also a DistributionNode => disallow ---
				HashSet<NetworkComponent> components = this.graphController.getNetworkModelAdapter().getNetworkComponents(graphNodeSelectedInMainGraph);
				NetworkComponent containsDistributionNode = this.graphController.getNetworkModelAdapter().containsDistributionNode(components);
				if (containsDistributionNode!=null) {
					String newLine = Application.getGlobalInfo().getNewLineSeparator();
					msg  = "The selection in the main graph already contains a component of" + newLine;
					msg += "the type 'DistributionNode'. This is only allowed once at one node! ";
					JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			
			// ----------------------------------------------------------------
			// --- Apply a shift for the new elements, in order to match ------ 
			// --- the position of the currently selected node in the    ------  
			// --- current setup									 	 ------
			// ----------------------------------------------------------------			
			this.applyNodeShift2MergeWithNetworkModel(graphNodeSelectedInMainGraph, this.currGraphNodeSelected);
			
			// --- Create the merge description -------------------------------
			HashSet<GraphNode> nodes2Add = new HashSet<GraphNode>();
			nodes2Add.add(this.currGraphNodeSelected);
			GraphNodePairs nodeCouples = new GraphNodePairs(this.basicGraphGui.getPickedSingleNode(), nodes2Add);

			// --- Add the new element to the current NetworkModel ------------
			this.graphController.getNetworkModelAdapter().mergeNetworkModel(this.currNetworkModel, nodeCouples);
			
		   
		} else if (ae.getSource()==getJButtonClose()) {
		    // --- Cancel button ------------------------------------
		    this.dispose();
		}
    }
	

}  //  @jve:decl-index=0:visual-constraint="30,-18"
