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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
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
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.GraphNodePairs;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponentFactory;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.prototypes.DistributionNode;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import agentgui.envModel.graph.prototypes.Star3GraphElement;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

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
public class AddComponentDialog extends BasicGraphGuiJInternalFrame implements ActionListener {

    private static final long serialVersionUID = -7481141098749690137L;
    
    public static final String NoFilterString = Language.translate("No Filter!", Language.EN); 
    private final String pathImage = GraphGlobals.getPathImages(); 
    
    private AddComponentVisViewer<GraphNode, GraphEdge> visViewer;

    private NetworkModel localNetworkModel;
    private ComponentTypeListElement localComponentTypeListElement;
	private DomainSettings localDomainSetings;
	
    private GraphElementPrototype localGraphElementPrototype;
	private GraphNode localGraphNodeSelected;
	
	private boolean isHiddenFactory = false;
	private ComponentAdapter desktopAdapter;
	
	private JSplitPane jSplitPaneContent;
	
	private JLabel jLabelFilter;
    private JLabel jLabelInstructionMerge;
    private JLabel jLabelInstructionSelect;

    private JComboBox<String> jComboBoxFilter;
    private DefaultComboBoxModel<String> comboBoxModeFilter;
    private String filterString = AddComponentDialog.NoFilterString;
    
    private JPanel jPanelTop;
    private JPanel jPanelFilter;
	
	private JPanel jPanelSplitBottom;
	private JPanel jPanelVisView;
    private JPanel jPanelBottom;

    private JScrollPane jScrollPane;
    private DefaultListModel<ComponentTypeListElement> listModelComponentTypes;
    private JList<ComponentTypeListElement> jListComponentTypes;
    
    private JToolBar jJToolBarBarVisViewLeft;
    private JToolBar jJToolBarBarVisViewRight;
	private JButton jButtonRotate45;
	private JButton jButtonRotate90;
	private JButton jButtonRotate270;
	private JButton jButtonRotate315;
    
    private JButton jButtonAdd;
    private JButton jButtonClose;

	
	/**
     * Instantiates a new AddComponentDialog and displays it for the user.
     * @param graphController the GraphEnvironmentController
     */
    public AddComponentDialog(GraphEnvironmentController graphController) {
    	super(graphController);
		initialize();
    }

    /**
     * Instantiates a new AddComponentDialog that can be used as hidden Factory
     * for imports and other.
     *
     * @param graphController the graph controller
     * @param isHiddenFactory indicates if the dialog is used a invisible factory dialog (e. g. for a customized import function)
     */
    public AddComponentDialog(GraphEnvironmentController graphController, boolean isHiddenFactory) {
    	super(graphController);
		this.basicGraphGui = this.graphControllerGUI.getBasicGraphGuiRootJSplitPane().getBasicGraphGui();
		this.isHiddenFactory = isHiddenFactory;
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
		
		// --- Call to the super-class ----------
		if (this.isHiddenFactory==false) {
			this.registerAtDesktopAndSetVisible();	
			this.graphDesktop.addComponentListener(this.getComponentAdapter4Desktop());
		}
		
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean aFlag) {
    	if (aFlag==true) {
    		if (this.graphDesktop!=null && this.isVisible()==false) {
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
     * Returns a ComponentAdapter for the current desktop object.
     * @return the ComponentAdapter 
     */
    private ComponentAdapter getComponentAdapter4Desktop() {
    	if (this.desktopAdapter==null) {
    		desktopAdapter = new ComponentAdapter() {
    			@Override
    			public void componentResized(ComponentEvent ce) {
    				setSize(new Dimension( (int)getSize().getWidth(), (int)graphDesktop.getSize().getHeight() ));
    			}
			};
    	}
    	return desktopAdapter;
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
	private JComboBox<String> getJComboBoxFilter() {
		if (jComboBoxFilter == null) {
			jComboBoxFilter = new JComboBox<String>(this.getNewComboBoxModelFilter());
			jComboBoxFilter.setPreferredSize(new Dimension(100, 26));
			jComboBoxFilter.addActionListener(this);
		}
		return jComboBoxFilter;
	}

	/**
	 * Gets the ComboBoxModel filter.
	 * @return the ComboBoxModel filter
	 */
	private DefaultComboBoxModel<String> getNewComboBoxModelFilter() {

		Vector<String> filterStrings = new Vector<String>();
		DefaultListModel<ComponentTypeListElement> typeList = this.getListModelComponentTypes();
		for (int i=0; i < typeList.size(); i++) {
			ComponentTypeListElement ctle = typeList.getElementAt(i);
			if (filterStrings.contains(ctle.getDomain())==false) {
				filterStrings.add(ctle.getDomain());
			}
		}
		Collections.sort(filterStrings);
		
		comboBoxModeFilter = new DefaultComboBoxModel<String>();
		comboBoxModeFilter.addElement(AddComponentDialog.NoFilterString);
		for (String filterString : filterStrings) {
			comboBoxModeFilter.addElement(filterString);
		}
		return comboBoxModeFilter;
	}
	
	 /**
     * This method initializes jScrollPane
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
		    jScrollPane = new JScrollPane();
		    jScrollPane.setPreferredSize(new Dimension(20, 200));
		    jScrollPane.setViewportView(this.getJListComponentTypes());
		}
		return jScrollPane;
    }
    /**
     * This method initializes componentTypesList
     * @return javax.swing.JList
     */
    private JList<ComponentTypeListElement> getJListComponentTypes() {
		if (jListComponentTypes == null) {
		    jListComponentTypes = new JList<ComponentTypeListElement>(this.getListModelComponentTypes());
		    jListComponentTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    jListComponentTypes.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent lse) {
				    if (!lse.getValueIsAdjusting()) {
						// --- Set the current list element ---------
				    	setComponentTypeListElement();
				    }
				}
		    });
		}
		return jListComponentTypes;
    }
    
    /**
     * Gets the list of componentTypeSettings from the controller and 
     * returns it as a Vector<ComponentTypeListElement>
     * @see ComponentTypeListElement
     * @return Object[] - array of component types
     */
    private DefaultListModel<ComponentTypeListElement> getListModelComponentTypes() {
		
    	if (this.listModelComponentTypes==null) {
    		// --- Create a work Vector -----------------------------
    		Vector<ComponentTypeListElement> componentTypeVector = new Vector<ComponentTypeListElement>();
        	TreeMap<String, ComponentTypeSettings> ctsHash = this.graphController.getComponentTypeSettings();
    		if (ctsHash != null) {
    			Iterator<String> ctsIt = ctsHash.keySet().iterator();
    		    while (ctsIt.hasNext()) {
    		    	String componentName = ctsIt.next(); 
    		    	ComponentTypeSettings cts = ctsHash.get(componentName); 
    		    	componentTypeVector.add(new ComponentTypeListElement(componentName, cts));
    		    }
    		} 
    		// --- Sort the Vector ----------------------------------
    		Collections.sort(componentTypeVector, new Comparator<ComponentTypeListElement>() {
    			@Override
    			public int compare(ComponentTypeListElement cts1, ComponentTypeListElement cts2) {
    				if (cts1.getDomain().equals(cts2.getDomain())) {
    					return cts1.getComponentName().compareTo(cts2.getComponentName());
    				} else {
    					return cts1.getDomain().compareTo(cts2.getDomain());
    				}
    			}
    		});
    		// --- Add the Vector elements to the ListModel ---------
    		this.listModelComponentTypes = new DefaultListModel<ComponentTypeListElement>();
    		for (ComponentTypeListElement ctle : componentTypeVector) {
    			if (this.filterString.equals(AddComponentDialog.NoFilterString)) {
    				// --- No filter applied --------------
    				this.listModelComponentTypes.addElement(ctle);	
    			} else {
    				// --- Filter applied -----------------
    				if (ctle.getDomain().equals(this.filterString)) {
    					this.listModelComponentTypes.addElement(ctle);
    				}
    			}
    		}
    	}
		return this.listModelComponentTypes;
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
     * Initializes the VisualizationViewer
     * @return The VisualizationViewer
     */
    private AddComponentVisViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		if (visViewer == null) {
			// --- Define the graph -------------
			Graph<GraphNode, GraphEdge> graph = new SparseGraph<GraphNode, GraphEdge>();
			// --- Define the Layout ------------
		    Layout<GraphNode, GraphEdge> layout = new CircleLayout<GraphNode, GraphEdge>(graph);
		    // --- Create VisualizationViewer ---
		    visViewer = new AddComponentVisViewer<GraphNode, GraphEdge>(layout);
		    visViewer.setGraphController(this.graphController);
		}
		return visViewer;
    }
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
			jJToolBarBarVisViewLeft.add(getJButtonRotate315());
			jJToolBarBarVisViewLeft.add(getJButtonRotate270());
			
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
			jJToolBarBarVisViewRight.add(getJButtonRotate45());
			jJToolBarBarVisViewRight.add(getJButtonRotate90());
		}
		return jJToolBarBarVisViewRight;
	}
	/**
	 * This method initializes jButtonSpin45	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRotate45() {
		if (jButtonRotate45 == null) {
			jButtonRotate45 = new JButton();
			jButtonRotate45.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rotate45.png")));
			jButtonRotate45.setToolTipText("Rotate 45°");
			jButtonRotate45.addActionListener(this);
		}
		return jButtonRotate45;
	}
	/**
	 * This method initializes jButtonSpin90	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRotate90() {
		if (jButtonRotate90 == null) {
			jButtonRotate90 = new JButton();
			jButtonRotate90.setToolTipText("Rotate 90°");
			jButtonRotate90.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rotate90.png")));
			jButtonRotate90.addActionListener(this);
		}
		return jButtonRotate90;
	}
	/**
	 * This method initializes jButtonSpin315	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRotate315() {
		if (jButtonRotate315 == null) {
			jButtonRotate315 = new JButton();
			jButtonRotate315.setToolTipText("Rotate -45°");
			jButtonRotate315.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rotate315.png")));
			jButtonRotate315.addActionListener(this);
		}
		return jButtonRotate315;
	}
	/**
	 * This method initializes jButtonSpin270	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRotate270() {
		if (jButtonRotate270 == null) {
			jButtonRotate270 = new JButton();
			jButtonRotate270.setToolTipText("Rotate -90°");
			jButtonRotate270.setIcon(new ImageIcon(getClass().getResource(pathImage + "Rotate270.png")));
			jButtonRotate270.addActionListener(this);
		}
		return jButtonRotate270;
	}

	/**
     * This method initializes jBottomPanel
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
		    
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		    gridBagConstraints4.insets = new Insets(5, 0, 5, 5);
		    gridBagConstraints4.gridx = 1;
		    gridBagConstraints4.gridy = 0;
		    gridBagConstraints4.ipadx = 0;
			
		    GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		    gridBagConstraints11.gridx = 2;
		    gridBagConstraints11.gridy = 0;
		    gridBagConstraints11.ipadx = 0;
		    gridBagConstraints11.insets = new Insets(5, 5, 5, 0);
		    
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
	 * Sets the current local ComponentTypeListElement.
	 */
	private void setComponentTypeListElement() {
		
    	// --- Set the current ComponentTypeListElement ---
		this.localComponentTypeListElement = (ComponentTypeListElement) jListComponentTypes.getSelectedValue();
		if (this.localComponentTypeListElement==null) {
			this.localNetworkModel = null;
			this.localGraphElementPrototype = null;
	    	return;
		}
    	// --- Set the current domain ---------------------
    	String domain = this.localComponentTypeListElement.getDomain();
    	if (domain==null) {
    		this.localDomainSetings = this.graphController.getDomainSettings().get(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
    	} else {
    		this.localDomainSetings = this.graphController.getDomainSettings().get(domain);	
    	}
    	// --- Set the above two values to the VisualizationViewer 
    	this.getVisualizationViewer().setComponentTypeListElement(this.localComponentTypeListElement);
    	this.getVisualizationViewer().setDomainSetings(this.localDomainSetings);
    	
    	// --- Paint GraphPrototype, if available ---  
    	String graphPrototype = localComponentTypeListElement.getComponentTypeSettings().getGraphPrototype();
    	if (graphPrototype!=null) {
			this.setPrototypePreview();	
		} else {
			String msg = Language.translate("The definition of the component contains no graph prototyp definition.", Language.EN);
			String titel = Language.translate("Missing definition of the Graph prototype for '" + localComponentTypeListElement.getComponentName() + "'!", Language.EN);
			JOptionPane.showMessageDialog(getJPanelTop(), msg, titel, JOptionPane.WARNING_MESSAGE);
		}
	}
	
	 /**
     * This method takes the GraphPrototype class name as string and creates a graph of the prototype and shows a preview in the visualizationViewer
     * @param graphPrototype
     */
    private void setPrototypePreview() {

    	String componentName = this.localComponentTypeListElement.getComponentName();
    	
    	// --- Create the graph of the NetworkComponent -------------
    	this.localNetworkModel = NetworkComponentFactory.getNetworkModel4NetworkComponent(this.graphController.getNetworkModel().getGeneralGraphSettings4MAS(), componentName, this.getVisualizationViewer().getSize());
    	this.localGraphElementPrototype = NetworkComponentFactory.getGraphElementPrototypeOfLastNetworkComponent();
		if (this.localNetworkModel!=null && this.localGraphElementPrototype!=null) {

			// --- Set the graph to the layout ----------------------
	    	Layout<GraphNode, GraphEdge> layout = new StaticLayout<GraphNode, GraphEdge>(this.localNetworkModel.getGraph());
			layout.setInitializer(new Transformer<GraphNode, Point2D>() {
				public Point2D transform(GraphNode node) {
					return node.getPosition(); 
				}
			});
			this.getVisualizationViewer().setGraphLayout(layout);
			this.getVisualizationViewer().repaint();
			
			// --- Pick first GraphNode --------------------
			this.pickFirstGraphNode();
		    this.setSelectedGraphNode();
		    
		}
    }
    
    /**
     * Picks the first graph node.
     */
    private void pickFirstGraphNode() {

    	GraphNode graphNodeSelect = null;
    	Collection<GraphNode> graphNodes = this.localNetworkModel.getGraph().getVertices();
    	for (GraphNode graphNode : graphNodes) {
    		
    		if (graphNodeSelect==null) {
    			graphNodeSelect = graphNode;
    		} else {
    			
    			double selectedX = graphNodeSelect.getPosition().getX();
    			double selectedY = graphNodeSelect.getPosition().getY();
    			
    			double currentX = graphNode.getPosition().getX();
    			double currentY = graphNode.getPosition().getY();
    			
    			if (currentX<selectedX) {
    				graphNodeSelect = graphNode;
    			} else if (currentX==selectedX && currentY<selectedY) {
    				graphNodeSelect = graphNode;
    			} else if (currentX==selectedX && currentY==selectedY) {
    				
    				int comparedID = graphNode.getId().compareTo(graphNodeSelect.getId());
    				if (comparedID<0) {
    					graphNodeSelect = graphNode;
    				}
    				
    			}
    		}
    		
    	}
    	
    	// --- Clear the overall selection and set the selected GraphNode -----
    	this.getVisualizationViewer().getPickedVertexState().clear();
    	if (graphNodeSelect!=null) {
    		this.getVisualizationViewer().getPickedVertexState().pick(graphNodeSelect, true);	
    	}
    	
    }
    
    /**
     * Evaluates the current graph and sets the selected GraphNode.
     */
    private void setSelectedGraphNode() {
    	
    	// --- Is everything selected what is needed ? --------------
    	if (this.localComponentTypeListElement==null || this.localGraphElementPrototype==null) {
    		this.localNetworkModel = null;
    		this.localGraphNodeSelected = null;
    		return;
    	}
    	
    	Set<GraphNode> nodeSet = getVisualizationViewer().getPickedVertexState().getPicked();
    	if (nodeSet.size()>=1) {
    		this.localGraphNodeSelected = nodeSet.iterator().next();
    	} else {
    		this.localGraphNodeSelected = null;
    	}
    	
    }
    
    /**
     * This will select the next (possible) GraphNode in main graph.
     */
    private void selectNextNodeInMainGraph(NetworkModel networkModelThatWasAdded, GraphNode selectedGraphNode) {
    	
    	GraphNode graphNodeSelect = null;
    	VisualizationViewer<GraphNode, GraphEdge> mainVisView = this.basicGraphGui.getVisualizationViewer();
    	
    	Collection<GraphNode> possibleGraphNode = networkModelThatWasAdded.getGraph().getVertices();
    	if (possibleGraphNode.size()==1) {
    		graphNodeSelect = selectedGraphNode;
    		
    	} else if (possibleGraphNode.size()>1) {
    		
    		for (GraphNode graphNode : possibleGraphNode) {
        		
    			if (graphNode!=selectedGraphNode) {
    				if (graphNodeSelect==null) {
            			graphNodeSelect = graphNode;
            		} else {
            			
            			double selectedX = graphNodeSelect.getPosition().getX();
            			double selectedY = graphNodeSelect.getPosition().getY();
            			
            			double currentX = graphNode.getPosition().getX();
            			double currentY = graphNode.getPosition().getY();
            			
            			if (currentX>selectedX) {
            				graphNodeSelect = graphNode;
            			} else if (currentX==selectedX && currentY>selectedY) {
            				graphNodeSelect = graphNode;
            			}
            		}    			
    			}
    			
    		}
    		
    	}
    	
    	// --- Select in the main graph -------------------
    	mainVisView.getPickedVertexState().clear();
    	if (graphNodeSelect!=null) {
    		mainVisView.getPickedVertexState().pick(graphNodeSelect, true);	
    	}
    	
    	
    }
    
    /**
     * Apply a shift for the new elements, in order to match the position 
     * of the currently selected node of the current setup		
     *
     * @param graphNode1 the graph node1
     * @param graphNode2 the graph node2
     */
    private void applyNodeShift2MergeWithNetworkModel(NetworkModel networkModel, GraphNode graphNode1, GraphNode graphNode2) {
    	
    	double shiftX = 0;
		double shiftY = 0;
		if (graphNode1 != null) {
			shiftX = graphNode1.getPosition().getX() - graphNode2.getPosition().getX();
			shiftY = graphNode1.getPosition().getY() - graphNode2.getPosition().getY();
		}
		
		Collection<GraphNode> graphNodes = networkModel.getGraph().getVertices();
		for (GraphNode node : graphNodes) {
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
    	DefaultListModel<ComponentTypeListElement> listModel = this.getListModelComponentTypes();
    	for (int i = 0; i < listModel.size(); i++) {
        	ComponentTypeListElement ctsElement = listModel.get(i);
        	if (ctsElement.getComponentName().equalsIgnoreCase(componentName)) {
        		this.getJListComponentTypes().setSelectedValue(ctsElement, true);
        		break;
        	}
		}
		return this.localNetworkModel;
	}
    
    /**
     * Rotates the current graph around itself.
     * @param rotateAngle the angle on which the graph should rotate 
     */
    private void rotateGraph(double rotateAngle) {
    	
    	if (this.localNetworkModel!=null) {
    		Graph<GraphNode, GraphEdge> graph = this.localNetworkModel.getGraph();
    		if (graph!=null) {
    			Vector<GraphNode> graphNodes = new Vector<GraphNode>(graph.getVertices());
    			if (graphNodes.size()>1) {
    				
        	    	double centerX = this.getVisualizationViewer().getCenter().getX();
        	    	double centerY = this.getVisualizationViewer().getCenter().getY();
        			
        			// --- Get all GraphNodes and rotate then around the center ---
        			for (GraphNode graphNode : graphNodes) {
        				
        				double newX = 0;
        				double newY = 0;
        				double oldX = graphNode.getPosition().getX() - centerX;
        				double oldY = graphNode.getPosition().getY() - centerY;
        				
        				double hypotenuse = Math.pow((Math.pow(oldX, 2) + Math.pow(oldY, 2)), 0.5);
        				hypotenuse = Math.round(hypotenuse*10)/10;
        				double oldAngle = Math.atan(oldY / oldX);
        				if (Double.isNaN(oldAngle)==false) {
        					if (oldX < 0 && oldY >= 0) {
            					oldAngle += Math.PI;
            				} else if (oldX < 0 && oldY < 0){
            					oldAngle += Math.PI;
            				}else if (oldX >= 0 && oldY < 0){
            					oldAngle += 2*Math.PI;
            				}
            				double newAngle = oldAngle + rotateAngle;
            				newX = Math.cos(newAngle) * hypotenuse;
            				newY = Math.sin(newAngle) * hypotenuse;
            				
        				}
        				Point2D newPosition = new Point2D.Double(centerX+newX, centerY+newY);
        				graphNode.setPosition(newPosition);
        				
        			}
        			
    				Layout<GraphNode, GraphEdge> layout = new StaticLayout<GraphNode, GraphEdge>(graph);
    				layout.setInitializer(new Transformer<GraphNode, Point2D>() {
    					public Point2D transform(GraphNode node) {
    						return node.getPosition(); // The position is specified in the GraphNode instance
    					}
    				});
    				this.getVisualizationViewer().setGraphLayout(layout);
        			this.getVisualizationViewer().repaint();
        			
    			}
    		}
    	}
    }
    
    /**
     * Adds the component.
     */
    private void addComponent() {
    	
    	String msg = null;
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
	    if (this.localNetworkModel==null) {
	    	msg = "Please, select the network component that you would like to add!";
	    	JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
	    	return;
	    }
	    if (this.localGraphNodeSelected==null) {
	    	msg = "Please, select one free vertex of the network component that you want to add!";
	    	JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
	    	return;
	    }

	    
		if (this.localGraphElementPrototype instanceof Star3GraphElement) {
		    // If the picked vertex is the center of the star, cannot add
		    Graph<GraphNode, GraphEdge> graph = getVisualizationViewer().getGraphLayout().getGraph();
		    // All the edges in the graph or incident on the pickedVertex => It is a center
		    Collection<GraphEdge> incidentEdges = graph.getIncidentEdges(this.localGraphNodeSelected);
		    if (incidentEdges!=null) {
			    if (graph.getEdgeCount()==incidentEdges.size()) {
			    	msg = "Select a vertex other than the center of the star";
			    	JOptionPane.showMessageDialog(this, Language.translate(msg, Language.EN), Language.translate("Warning", Language.EN), JOptionPane.WARNING_MESSAGE);
			    	return;
			    }
		    }
		    
		} else if (this.localGraphElementPrototype instanceof DistributionNode) {
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
		// --- Get a copy of the local NetworkModel -----------------------
		NetworkModel networkModelCopy = this.localNetworkModel.getCopy();
		GraphNode graphNodeCopy = (GraphNode) networkModelCopy.getGraphElement(this.localGraphNodeSelected.getId());

		// ----------------------------------------------------------------
		// --- Apply a shift for the new elements, in order to match ------ 
		// --- the position of the currently selected node in the    ------  
		// --- main graph / current setup							 ------
		// ----------------------------------------------------------------			
		this.applyNodeShift2MergeWithNetworkModel(networkModelCopy, graphNodeSelectedInMainGraph, graphNodeCopy);
		
		// --- Create the merge description -------------------------------
		HashSet<GraphNode> nodes2Add = new HashSet<GraphNode>();
		nodes2Add.add(graphNodeCopy);
		GraphNodePairs nodeCouples = new GraphNodePairs(graphNodeSelectedInMainGraph, nodes2Add);

		// --- Add the new element to the current NetworkModel ------------
		this.graphController.getNetworkModelAdapter().mergeNetworkModel(networkModelCopy, nodeCouples);

		// --- Select the next GraphNode in the main graph ----------------
		this.selectNextNodeInMainGraph(networkModelCopy, graphNodeCopy);
		
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {

    	if (ae.getSource()==this.getJButtonAdd() ) {
    		this.addComponent();
    				   
		} else if (ae.getSource()==getJButtonClose()) {
		    this.dispose();
		 
		} else if (ae.getSource()==getJComboBoxFilter()) {
			this.filterString = (String) getJComboBoxFilter().getSelectedItem(); 
			this.listModelComponentTypes = null;
			this.getJListComponentTypes().setModel(this.getListModelComponentTypes());
			
		} else if (ae.getSource()==getJButtonRotate45()) {
			this.rotateGraph((2*Math.PI)/8);
			
		} else if (ae.getSource()==getJButtonRotate90()) {
			this.rotateGraph((2*Math.PI)/4);
			
		} else if (ae.getSource()==getJButtonRotate315()) {
			this.rotateGraph(-(2*Math.PI)/8);

		} else if (ae.getSource()==getJButtonRotate270()) {
			this.rotateGraph(-(2*Math.PI)/4);
			
		}
    	
    }

    
}  //  @jve:decl-index=0:visual-constraint="30,-18"