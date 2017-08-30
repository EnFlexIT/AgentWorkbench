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
package agentgui.envModel.graph.components;

import jade.core.Agent;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.JComboBoxWide;
import agentgui.core.gui.imaging.MissingIcon;
import agentgui.core.project.Project;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS.EdgeShape;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;

import javax.swing.JComboBox;

/**
 * GUI dialog for configuring network component types
 *  
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ComponentTypeDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private final String pathImage = GraphGlobals.getPathImages();
	
	private Vector<String> columnHeaderDomains 		= null;
	public final String COL_D_DomainName 			= Language.translate("Name", Language.EN);  			//  @jve:decl-index=0:
	public final String COL_D_AdapterClass			= Language.translate("Adapter class", Language.EN); 	//  @jve:decl-index=0:
	public final String COL_D_VertexSize 			= Language.translate("Vertex size", Language.EN);  		//  @jve:decl-index=0:
	public final String COL_D_VertexColor			= Language.translate("Color", Language.EN);  			//  @jve:decl-index=0:
	public final String COL_D_VertexColorPicked 	= Language.translate("Color picked", Language.EN);  	//  @jve:decl-index=0:
	public final String COL_D_ShowLable				= Language.translate("Show label", Language.EN);  		//  @jve:decl-index=0:
	public final String COL_D_ClusterShape			= Language.translate("Cluster shape", Language.EN);  	//  @jve:decl-index=0:
	public final String COL_D_ClusterAgent			= Language.translate("Cluster agent", Language.EN);  	//  @jve:decl-index=0:
	
	private Vector<String> columnHeaderComponents 	= null;
	public final String COL_TypeSpecifier 			= Language.translate("Type-Specifier", Language.EN);  	//  @jve:decl-index=0:
	public final String COL_Domain 					= Language.translate("Subnetwork", Language.EN);  		//  @jve:decl-index=0:
	public final String COL_AgentClass 				= Language.translate("Agent Class", Language.EN); 		//  @jve:decl-index=0:
	public final String COL_GraphPrototyp 			= Language.translate("Graph-Prototype", Language.EN);  	//  @jve:decl-index=0:
	public final String COL_AdapterClass 			= Language.translate("Adapter class", Language.EN);  	//  @jve:decl-index=0:
	public final String COL_ShowLabel 				= Language.translate("Show Label", Language.EN);  		//  @jve:decl-index=0:
	public final String COL_Image 					= Language.translate("Image", Language.EN);  			//  @jve:decl-index=0:
	public final String COL_EdgeWidth 				= Language.translate("Width", Language.EN);  			//  @jve:decl-index=0:
	public final String COL_EdgeColor 				= Language.translate("Color", Language.EN);  			//  @jve:decl-index=0:

	
	private TreeMap<String, ComponentTypeSettings> currCompTypSettings;
	private TreeMap<String, DomainSettings> currDomainSettings;
	
	private boolean currSnap2Grid = true;
	private double currSnapRaster = 5; 
	
	private EdgeShape currEdgeShape; 
	
	private Project currProject;
	private boolean canceled = false;

	private JTabbedPane jTabbedPane;

	private JPanel jContentPane;
	private JPanel jPanelDomains;
	private JPanel jPanelComponents;
	private JPanel jPanelRaster;
	private JPanel jPanelGeneralSettings;
	private JPanel jPanelButtonOkCancel;

	private JLabel jLabelGridHeader;
	private JLabel jLabelGuideGridWidth;
	private JLabel jLabelBottomDummy;
	private JCheckBox jCheckBoxSnap2Grid;
	private JSpinner jSpnnerGridWidth;

	private JButton jButtonConfirm;
	private JButton jButtonCancel;

	private JButton jButtonAddDomain;
	private JButton jButtonRemoveDomainntRow;
	private JScrollPane jScrollPaneClassTableDomains;
	private JTable jTableDomainTypes;
	private DefaultTableModel domainTableModel;

	private JButton jButtonAddComponentRow;
	private JButton jButtonRemoveComponentRow;
	private JScrollPane jScrollPaneClassTableComponents;
	private JTable jTableComponentTypes;
	private DefaultTableModel componentTypesModel;
	
	private JLabel jLabelEdgeShape;
	private JComboBox<EdgeShape> jComboBoxEdgeShapes;
	private DefaultComboBoxModel<EdgeShape> comboBoxModelEdgeShapes;

	private TableCellEditor4ClassSelector agentClassesCellEditor;  		
	private TableCellEditor4ClassSelector prototypeClassesCellEditor;  	
	private TableCellEditor4ClassSelector adapterClassesCellEditor;  

	
	/**
	 * This is the default constructor.
	 *
	 * @param graphSettings the graph settings
	 * @param project the current project
	 */
	public ComponentTypeDialog(GeneralGraphSettings4MAS graphSettings, Project project) {
		this.setStartArguments(graphSettings, project);
		this.initialize();
	}
	/**
	 * Instantiates a new component type dialog.
	 *
	 * @param ownerFrame the owner frame
	 * @param graphSettings the graph settings
	 * @param project the project
	 */
	public ComponentTypeDialog(Frame ownerFrame, GeneralGraphSettings4MAS graphSettings, Project project) {
		super(ownerFrame);
		this.setStartArguments(graphSettings, project);
		this.initialize();
	}
	/**
	 * Instantiates a new component type dialog.
	 *
	 * @param ownerDialog the owner dialog
	 * @param graphSettings the graph settings
	 * @param project the project
	 */
	public ComponentTypeDialog(Dialog ownerDialog, GeneralGraphSettings4MAS graphSettings, Project project) {
		super(ownerDialog);
		this.setStartArguments(graphSettings, project);
		this.initialize();
	}

	/**
	 * Sets the start arguments to the local variables.
	 *
	 * @param graphSettings the graph settings
	 * @param project the project
	 */
	private void setStartArguments(GeneralGraphSettings4MAS graphSettings, Project project) {
		this.currCompTypSettings = graphSettings.getCurrentCTS();
		this.currDomainSettings = graphSettings.getDomainSettings();
		this.currSnap2Grid = graphSettings.isSnap2Grid();
		this.currSnapRaster = graphSettings.getSnapRaster();
		this.currEdgeShape = graphSettings.getEdgeShape();
		this.currProject = project;
	}
	/**
	 * This method initialises this.
	 */
	private void initialize() {
		
		this.setSize(980, 700);
		this.setTitle("Komponententyp-Definition");
		this.setTitle(Language.translate(this.getTitle()));
		this.setModal(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		this.registerEscapeKeyStroke();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);		
	    
	    this.setContentPane(getJContentPane());
	    this.setGeneralConfiguration();
	    
	    // --- In case that we're in an executed MAS ------
	    if (this.currProject==null) {
	    	
	    	this.getJTable4DomainTypes().setEnabled(false);
	    	this.getJButtonAddDomain().setEnabled(false);
	    	this.getJButtonRemoveDomainntRow().setEnabled(false);
	    	
	    	this.getJTable4ComponentTypes().setEnabled(false);
	    	this.getJButtonAddComponentRow().setEnabled(false);
	    	this.getJButtonRemoveComponentRow().setEnabled(false);

	    	this.getJButtonConfirm().setEnabled(false);
	    }
	    
	}
	
    /**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ComponentTypeDialog compTypeDialog = this;
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent ae) {
            	// --- Stop cell editing, if required ----- 
            	TableCellEditor editor = getJTable4DomainTypes().getCellEditor();
				if (editor!=null)  {
					editor.stopCellEditing();
					return;
				}
				editor = getJTable4ComponentTypes().getCellEditor();
				if (editor!=null)  {
					editor.stopCellEditing();
					return;
				}
            	
            	// --- Close dialog -----------------------
				String title = Language.translate("Schließen") + " ?";
				String message = Language.translate("Dialog schließen") + " ?";
				if (JOptionPane.showConfirmDialog(compTypeDialog, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
					canceled = true;
	    			setVisible(false);	
				}
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * This will set the current configuration for the nodes (propagation points) to the current view.
	 */
	private void setGeneralConfiguration() {
		// --- Get the Guide grid configuration -------------------------------
		this.getJCheckBoxSnap2Grid().setSelected(this.currSnap2Grid);
		this.getJSpinnerGridWidth().setValue(this.currSnapRaster);
		this.getJComboBoxEdgeShapes().setSelectedItem(this.currEdgeShape);
	}
	
	/**
	 * Returns all component type settings.
	 * @return the ComponentTypeSettings
	 */
	public TreeMap<String, ComponentTypeSettings> getComponentTypeSettings() {
		return this.currCompTypSettings;
	}
	/**
	 * Returns the domain settings.
	 * @return the DomainSettings
	 */
	public TreeMap<String, DomainSettings> getDomainSettings() {
		return this.currDomainSettings;
	}
	/**
	 * Checks if is snap2 grid.
	 * @return true, if is snap2 grid
	 */
	public boolean isSnap2Grid() {
		return this.currSnap2Grid;
	}
	/**
	 * Gets the snap raster.
	 * @return the snap raster
	 */
	public double getSnapRaster(){
		return this.currSnapRaster;
	}
	/**
	 * Gets the edge shape.
	 * @return the edge shape
	 */
	public EdgeShape getEdgeShape() {
		return this.currEdgeShape;
	}
	/**
	 * Returns the GeneralGraphSettings4MAS.
	 * @return the GeneralGraphSettings4MAS for the graph environment
	 */
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		GeneralGraphSettings4MAS genSettings = new GeneralGraphSettings4MAS();
		genSettings.setCurrentCTS(this.getComponentTypeSettings());
		genSettings.setDomainSettings(this.getDomainSettings());
		genSettings.setSnap2Grid(this.isSnap2Grid());
		genSettings.setSnapRaster(this.getSnapRaster());
		genSettings.setEdgeShape(this.getEdgeShape());
		return genSettings;
	}
	
	/**
	 * Gets the Vector of the column header in the needed order.
	 * @return the column header
	 */
	private Vector<String> getColumnHeaderDomains() {
		if (columnHeaderDomains==null) {
			columnHeaderDomains = new Vector<String>();
			columnHeaderDomains.add(COL_D_DomainName);
			columnHeaderDomains.add(COL_D_AdapterClass);
			columnHeaderDomains.add(COL_D_ShowLable);	
			columnHeaderDomains.add(COL_D_VertexSize);
			columnHeaderDomains.add(COL_D_VertexColor);
			columnHeaderDomains.add(COL_D_VertexColorPicked);
			columnHeaderDomains.add(COL_D_ClusterAgent);
			columnHeaderDomains.add(COL_D_ClusterShape);
		}
		return columnHeaderDomains;
	}
	/**
	 * Gets the header index.
	 * @param header the header
	 * @return the header index
	 */
	private int getColumnHeaderIndexDomains(String header) {
		Vector<String> headers = this.getColumnHeaderDomains();
		int headerIndex = -1;
		for (int i=0; i < headers.size(); i++) {
			if (headers.get(i).equals(header)) {
				headerIndex = i;
				break;
			}
		}
		return headerIndex;
	}
	
	/**
	 * Gets the Vector of the column header in the needed order.
	 * @return the column header
	 */
	private Vector<String> getColumnHeaderComponents() {
		if (columnHeaderComponents==null) {
			columnHeaderComponents = new Vector<String>();
			columnHeaderComponents.add(COL_Domain);
			columnHeaderComponents.add(COL_TypeSpecifier);
			columnHeaderComponents.add(COL_AgentClass);
			columnHeaderComponents.add(COL_GraphPrototyp);
			columnHeaderComponents.add(COL_AdapterClass);
			columnHeaderComponents.add(COL_ShowLabel);
			columnHeaderComponents.add(COL_Image);			
			columnHeaderComponents.add(COL_EdgeWidth);
			columnHeaderComponents.add(COL_EdgeColor);
		}
		return columnHeaderComponents;
	}
	/**
	 * Gets the header index.
	 * @param header the header
	 * @return the header index
	 */
	private int getColumnHeaderIndexComponents(String header) {
		Vector<String> headers = this.getColumnHeaderComponents();
		int headerIndex = -1;
		for (int i=0; i < headers.size(); i++) {
			if (headers.get(i).equals(header)) {
				headerIndex = i;
				break;
			}
		}
		return headerIndex;
	}
	
	/**
	 * This method initializes jContentPane.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridheight = 3;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 3;
			gridBagConstraints21.insets = new Insets(15, 15, 20, 15);
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			gridBagConstraints110.fill = GridBagConstraints.BOTH;
			gridBagConstraints110.weighty = 1.0;
			gridBagConstraints110.gridx = 0;
			gridBagConstraints110.gridy = 0;
			gridBagConstraints110.insets = new Insets(15, 15, 0, 15);
			gridBagConstraints110.weightx = 1.0;
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJTabbedPane(), gridBagConstraints110);
			jContentPane.add(getJPanelButtonOkCancel(), gridBagConstraints21);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jTabbedPane	.
	 *
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setFont(new Font("Dialog", Font.BOLD, 12));
			jTabbedPane.addTab("Teilnetze", null, getJPanelDomains(), null);
			jTabbedPane.addTab("Netzwerk-Komponenten", null, getJPanelComponents(), null);
			jTabbedPane.addTab("Allgemein", null, getJPanelGeneralSettings(), null);
			
			jTabbedPane.setTitleAt(0, Language.translate("Teilnetze"));
			jTabbedPane.setTitleAt(1, Language.translate("Netzwerk-Komponenten"));
			jTabbedPane.setTitleAt(2, Language.translate("Allgemein"));
			jTabbedPane.setSelectedIndex(1);

			jTabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					TableCellEditor editor = getJTable4DomainTypes().getCellEditor();
					if (editor!=null)  editor.stopCellEditing();
					editor = getJTable4ComponentTypes().getCellEditor();
					if (editor!=null)  editor.stopCellEditing();
					setTableCellEditor4DomainsInComponents(null);
				}
			});
			
		}
		return jTabbedPane;
	}
	
	/**
	 * This method initializes jPanelGeneralSettings	.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelGeneralSettings() {
		if (jPanelGeneralSettings == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.weighty = 1.0;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.insets = new Insets(0, 15, 15, 15);
			gridBagConstraints12.gridy = 1;
			
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(15, 15, 0, 15);
			gridBagConstraints4.gridy = 0;
			
			jLabelBottomDummy = new JLabel();
			jLabelBottomDummy.setText("");
			
			jPanelGeneralSettings = new JPanel();
			jPanelGeneralSettings.setLayout(new GridBagLayout());
			jPanelGeneralSettings.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelGeneralSettings.add(getJPanelRaster(), gridBagConstraints4);
			jPanelGeneralSettings.add(jLabelBottomDummy, gridBagConstraints12);
		}
		return jPanelGeneralSettings;
	}
	
	/**
	 * This method initializes jPanelRaster	.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelRaster() {
		if (jPanelRaster == null) {
			
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.BOTH;
			gridBagConstraints15.gridy = 2;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.gridwidth = 2;
			gridBagConstraints15.insets = new Insets(20, 5, 0, 0);
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(20, 5, 0, 0);
			gridBagConstraints14.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(5, 5, 5, 0);
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 0.0;
			gridBagConstraints11.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new Insets(5, 10, 5, 0);
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.gridx = -1;
			gridBagConstraints7.gridy = -1;
			gridBagConstraints7.insets = new Insets(5, 10, 5, 0);
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridwidth = 1;
			gridBagConstraints5.gridx = -1;
			gridBagConstraints5.gridy = -1;
			gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
			
			jLabelGridHeader = new JLabel();
			jLabelGridHeader.setText("Hilfs-Raster");
			jLabelGridHeader.setText(Language.translate(jLabelGridHeader.getText()) + ":");
			jLabelGridHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelGuideGridWidth = new JLabel();
			jLabelGuideGridWidth.setText("Raster-Breite");
			jLabelGuideGridWidth.setText(Language.translate(jLabelGuideGridWidth.getText()));
			
			jLabelEdgeShape = new JLabel();
			jLabelEdgeShape.setText("Kanten-Typ");
			jLabelEdgeShape.setText(Language.translate(jLabelEdgeShape.getText()) + ":");
			jLabelEdgeShape.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelRaster = new JPanel();
			jPanelRaster.setLayout(new GridBagLayout());
			jPanelRaster.add(jLabelGridHeader, gridBagConstraints5);
			jPanelRaster.add(getJCheckBoxSnap2Grid(), gridBagConstraints7);
			jPanelRaster.add(jLabelGuideGridWidth, gridBagConstraints10);
			jPanelRaster.add(getJSpinnerGridWidth(), gridBagConstraints11);
			jPanelRaster.add(jLabelEdgeShape, gridBagConstraints14);
			jPanelRaster.add(getJComboBoxEdgeShapes(), gridBagConstraints15);
		}
		return jPanelRaster;
	}
	
	/**
	 * This method initializes jButtonConfirm	.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonConfirm() {
		if (jButtonConfirm == null) {
			jButtonConfirm = new JButton();
			jButtonConfirm.setText("Übernehmen");
			jButtonConfirm.setText(Language.translate(jButtonConfirm.getText()));
			jButtonConfirm.setPreferredSize(new Dimension(120, 28));
			jButtonConfirm.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonConfirm.setForeground(new Color(0, 153, 0));
			jButtonConfirm.addActionListener(this);
		}
		return jButtonConfirm;
	}
	
	/**
	 * This method initializes jButtonCancel	.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Abbrechen");
			jButtonCancel.setText(Language.translate(jButtonCancel.getText()));
			jButtonCancel.setPreferredSize(new Dimension(120, 28));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jPanelDomains	.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelDomains() {
		if (jPanelDomains == null) {
			
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(5, 5, 3, 5);
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(5, 5, 3, 5);
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 3;
			gridBagConstraints13.gridwidth = 2;
			gridBagConstraints13.weightx = 1.0;
			
			jPanelDomains = new JPanel();
			jPanelDomains.setLayout(new GridBagLayout());
			jPanelDomains.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelDomains.add(getJScrollPaneClassTableDomains(), gridBagConstraints13);
			jPanelDomains.add(getJButtonAddDomain(), gridBagConstraints2);
			jPanelDomains.add(getJButtonRemoveDomainntRow(), gridBagConstraints3);
		}
		return jPanelDomains;
	}
	
	/**
	 * This method initializes jButtonAddDomain	.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonAddDomain() {
		if (jButtonAddDomain == null) {
			ImageIcon imageIcon = new ImageIcon(getClass().getResource(this.pathImage + "ListPlus.png"));
			jButtonAddDomain = new JButton();
			jButtonAddDomain.setIcon(imageIcon);
			jButtonAddDomain.addActionListener(this);
		}
		return jButtonAddDomain;
	}
	
	/**
	 * This method initializes jButtonRemoveDomainntRow	.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonRemoveDomainntRow() {
		if (jButtonRemoveDomainntRow == null) {
			ImageIcon imageIcon1 = new ImageIcon(getClass().getResource(this.pathImage + "ListMinus.png"));
			jButtonRemoveDomainntRow = new JButton();
			jButtonRemoveDomainntRow.setIcon(imageIcon1);
			jButtonRemoveDomainntRow.addActionListener(this);
		}
		return jButtonRemoveDomainntRow;
	}
	
	/**
	 * This method initializes jScrollPaneClassTableDomains	.
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneClassTableDomains() {
		if (jScrollPaneClassTableDomains == null) {
			jScrollPaneClassTableDomains = new JScrollPane();
			jScrollPaneClassTableDomains.setViewportView(getJTable4DomainTypes());
		}
		return jScrollPaneClassTableDomains;
	}
	
	/**
	 * This method initializes jTableDomainTypes	.
	 *
	 * @return javax.swing.JTable
	 */
	private JTable getJTable4DomainTypes() {
		if (jTableDomainTypes == null) {
			
			jTableDomainTypes = new JTable();
			jTableDomainTypes.setModel(this.getTableModel4Domains());
			jTableDomainTypes.setFillsViewportHeight(true);
			jTableDomainTypes.setShowGrid(false);
			jTableDomainTypes.setRowHeight(20);
			jTableDomainTypes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableDomainTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableDomainTypes.setAutoCreateRowSorter(true);
			jTableDomainTypes.getTableHeader().setReorderingAllowed(false);
			
			// --- Define the sorter ----------------------
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(getTableModel4Domains());
			sorter.setComparator(getColumnHeaderIndexDomains(COL_D_ShowLable), new Comparator<Boolean>() {
				@Override
				public int compare(Boolean o1, Boolean o2) {
					return o1.compareTo(o2);
				}
			});
			sorter.setComparator(getColumnHeaderIndexDomains(COL_D_VertexSize), new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
			sorter.setComparator(getColumnHeaderIndexDomains(COL_D_VertexColor), new Comparator<Color>() {
				@Override
				public int compare(Color o1, Color o2) {
					Integer o1RGB = o1.getRGB();
					Integer o2RGB = o2.getRGB();
					return o1RGB.compareTo(o2RGB);
				}
			});
			sorter.setComparator(getColumnHeaderIndexDomains(COL_D_VertexColorPicked), new Comparator<Color>() {
				@Override
				public int compare(Color o1, Color o2) {
					Integer o1RGB = o1.getRGB();
					Integer o2RGB = o2.getRGB();
					return o1RGB.compareTo(o2RGB);
				}
			});
			jTableDomainTypes.setRowSorter(sorter);

			
			// --- Define the first sort order ------------
			List<SortKey> sortKeys = new ArrayList<SortKey>();
			for (int i = 0; i < jTableDomainTypes.getColumnCount(); i++) {
			    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
			}
			jTableDomainTypes.getRowSorter().setSortKeys(sortKeys);

			
			// --- Configure the editor and the renderer of the cells ---------
			TableColumnModel tcm = jTableDomainTypes.getColumnModel();

			//Set up renderer and editor for the domain name column
			TableColumn domainColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_DomainName));
			domainColumn.setCellEditor(new TableCellEditor4Domains(this));
			
			//Set up renderer and editor for the agent class column
			TableColumn agentClassColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_AdapterClass));
			agentClassColumn.setCellEditor(this.getAdapterClassesCellEditor());
			agentClassColumn.setCellRenderer(new TableCellRenderer4Label());

			//Set up renderer and editor for Graph prototype column
			TableColumn vertexSize = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_VertexSize));
			vertexSize.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxNodeSize()));
			vertexSize.setPreferredWidth(10);
			
			//Set up renderer and editor for the  Color column.	        
			TableColumn vertexColor = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_VertexColor));
			vertexColor.setCellEditor(new TableCellEditor4Color());
			vertexColor.setCellRenderer(new TableCellRenderer4Color(true));			
			vertexColor.setPreferredWidth(10);
			
			//Set up renderer and editor for the  Color column.
			TableColumn vertexColorPicked = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_VertexColorPicked));
			vertexColorPicked.setCellEditor(new TableCellEditor4Color());
			vertexColorPicked.setCellRenderer(new TableCellRenderer4Color(true));			
			vertexColorPicked.setPreferredWidth(10);
			
			//Set up renderer and editor for show label
			TableColumn showLabelClassColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_ShowLable));
			showLabelClassColumn.setCellEditor(new TableCellEditor4CheckBox(this.getCheckBoxEdgeWidth()));
			showLabelClassColumn.setCellRenderer(new TableCellRenderer4CheckBox());
			showLabelClassColumn.setPreferredWidth(10);
			
			TableColumn clusterShapeColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_ClusterShape));
			clusterShapeColumn.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxClusterShape()));
			clusterShapeColumn.setPreferredWidth(10);
			
			TableColumn clusterAgentColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_ClusterAgent));
			clusterAgentColumn.setCellEditor(this.getAgentClassesCellEditor());
			clusterAgentColumn.setCellRenderer(new TableCellRenderer4Label());
			
		}
		return jTableDomainTypes;
	}
	
	/**
	 * Gets the table model for domains.
	 * @return the table model4 domains
	 */
	private DefaultTableModel getTableModel4Domains(){
		
		if (domainTableModel==null) {
			
			Vector<Vector<Object>> dataRows = new Vector<Vector<Object>>();
			
			// Set table entries for defined assignments, if any
			if(this.currDomainSettings!=null){
				
				Iterator<String> domainIterator = this.currDomainSettings.keySet().iterator();
				while(domainIterator.hasNext()){
					
					String domainName = domainIterator.next();
					
					DomainSettings domSetting = this.currDomainSettings.get(domainName);
					String ontologyClass = domSetting.getAdapterClass();
					Integer vertexSize = domSetting.getVertexSize();
					if (vertexSize==0) {
						vertexSize = GeneralGraphSettings4MAS.DEFAULT_VERTEX_SIZE;
					}
					Color vertexColor = new Color(Integer.parseInt(domSetting.getVertexColor()));
					Color vertexColorPicked = new Color(Integer.parseInt(domSetting.getVertexColorPicked()));
					boolean showLabel = domSetting.isShowLabel();
					String clusterShape = domSetting.getClusterShape();
					String clusterAgent = domSetting.getClusterAgent();
					
					// --- Create row vector --------------
					Vector<Object> newRow = new Vector<Object>();
					for (int i = 0; i < this.getColumnHeaderDomains().size(); i++) {
						if (i == getColumnHeaderIndexDomains(COL_D_DomainName)) {
							newRow.add(domainName);
						} else if (i == getColumnHeaderIndexDomains(COL_D_AdapterClass)) {
							newRow.add(ontologyClass);
						} else if (i == getColumnHeaderIndexDomains(COL_D_VertexSize)) {
							newRow.add(vertexSize);
						} else if (i == getColumnHeaderIndexDomains(COL_D_VertexColor)) {
							newRow.add(vertexColor);
						} else if (i == getColumnHeaderIndexDomains(COL_D_VertexColorPicked)) {
							newRow.add(vertexColorPicked);
						} else if (i == getColumnHeaderIndexDomains(COL_D_ShowLable)) {
							newRow.add(showLabel);
						} else if (i == getColumnHeaderIndexDomains(COL_D_ClusterShape)) {
							newRow.add(clusterShape);
						} else if (i == getColumnHeaderIndexDomains(COL_D_ClusterAgent)) {
							newRow.add(clusterAgent);
						}
					}
					dataRows.add(newRow);
				}
			}
			
			// --------------------------------------------
			// --- define the TableModel --- Start --------
			domainTableModel = new DefaultTableModel(dataRows, this.getColumnHeaderDomains()){
				private static final long serialVersionUID = 3550155601170744633L;
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int columnIndex) {
			            if(columnIndex==getColumnHeaderIndexComponents(COL_Image))
			            	return ImageIcon.class;
			            else if(columnIndex==getColumnHeaderIndexComponents(COL_D_VertexColor))
			            	return Color.class;
			            else if(columnIndex==getColumnHeaderIndexComponents(COL_D_VertexColorPicked))
			            	return Color.class;
			            else 
			            	return String.class;
			        }
				};
				
			// --- define the TableModel --- End ----------
			// --------------------------------------------
			// ----------------------------------------------------------------
		}
		return domainTableModel;
	}
	
	/**
	 * This method adds a new row to the jTableClasses' TableModel4Domains.
	 */
	private void addDomainRow(){
		// --- Create row vector --------------
		Vector<Object> newRow = new Vector<Object>();
		for (int i = 0; i < this.getColumnHeaderDomains().size(); i++) {
			if (i == getColumnHeaderIndexDomains(COL_D_DomainName)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexDomains(COL_D_AdapterClass)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexDomains(COL_D_ShowLable)) {
				newRow.add(true);
			} else if (i == getColumnHeaderIndexDomains(COL_D_VertexSize)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_VERTEX_SIZE);
			} else if (i == getColumnHeaderIndexDomains(COL_D_VertexColor)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_VERTEX_COLOR);
			} else if (i == getColumnHeaderIndexDomains(COL_D_VertexColorPicked)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_VERTEX_PICKED_COLOR);
			} else if (i == getColumnHeaderIndexDomains(COL_D_ClusterShape)) {
				newRow.add(GeneralGraphSettings4MAS.SHAPE_DEFAULT_4_CLUSTER);
			} else if (i == getColumnHeaderIndexDomains(COL_D_ClusterAgent)) {
				newRow.add(null);
			}
		}
		
		this.getTableModel4Domains().addRow(newRow);
		int newIndex = this.getTableModel4Domains().getRowCount() - 1;
		newIndex = this.getJTable4DomainTypes().convertRowIndexToView(newIndex);
		
		this.getJTable4DomainTypes().changeSelection(newIndex, 0, false, false);
		this.getJTable4DomainTypes().editCellAt(newIndex, 0);
		this.setTableCellEditor4DomainsInComponents(null);
	}
	
	/**
	 * Removes the domain row.
	 * @param rowNumTable the row num
	 */
	private void removeDomainRow(int rowNumTable){
		
		int rowNumModel = this.getJTable4DomainTypes().convertRowIndexToModel(rowNumTable);
		int colDamain = this.getColumnHeaderIndexDomains(COL_D_DomainName);
		String domainName = (String)this.getJTable4DomainTypes().getValueAt(rowNumTable, colDamain);
		String defaultDomain = GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME;
		
		if (domainName!=null) {
			if (domainName.equals(defaultDomain)) {
				String newLine = Application.getGlobalInfo().getNewLineSeparator();
				String msg = Language.translate("Dieser Eintrag ist ein notwendiger Systemparameter, der " + newLine + "nicht gelöscht oder umbenannt werden darf!");
				String title = "'" + defaultDomain + "': " +  Language.translate("Löschen nicht zulässig!");
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				return;
			} 
		} 
		((DefaultTableModel)getJTable4DomainTypes().getModel()).removeRow(rowNumModel);
		this.renameDomainInComponents(domainName, defaultDomain);
		this.setTableCellEditor4DomainsInComponents(null);	
		
	}
	
	/**
	 * Rename domain in components.
	 *
	 * @param oldDomainName the old domain name
	 * @param newDomainName the new domain name
	 */
	public void renameDomainInComponents(String oldDomainName, String newDomainName) {
		
		DefaultTableModel dtmComponents = this.getTableModel4ComponentTypes();
		int column = getColumnHeaderIndexComponents(COL_Domain);
		
		// --- Get the component type definitions from table ----
		JTable jtComponents = this.getJTable4ComponentTypes();
		// --- Confirm, apply changes in table ------------------						
		TableCellEditor tceComponents = jtComponents.getCellEditor();
		if (tceComponents!=null) {
			tceComponents.stopCellEditing();
		}
		for(int row=0; row<dtmComponents.getRowCount(); row++){
			String currValue = (String) dtmComponents.getValueAt(row, column);
			if (currValue.equals(oldDomainName)) {
				dtmComponents.setValueAt(newDomainName, row, column);	
			}
		}
		this.setTableCellEditor4DomainsInComponents(null);
	}
	
	/**
	 * Sets the table CellEditor for domains in components.
	 * @param domainVector the string vector of the current domains
	 */
	public void setTableCellEditor4DomainsInComponents(Vector<String> domainVector){
		TableColumnModel tcm = this.getJTable4ComponentTypes().getColumnModel();
		TableColumn domainColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_Domain));
		domainColumn.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxDomains(domainVector)));
	}
	
	/**
	 * This method initializes jScrollPaneClassTableComponents	.
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneClassTableComponents() {
		if (jScrollPaneClassTableComponents == null) {
			jScrollPaneClassTableComponents = new JScrollPane();
			jScrollPaneClassTableComponents.setViewportView(getJTable4ComponentTypes());
		}
		return jScrollPaneClassTableComponents;
	}

	/**
	 * This method initializes jTableClasses.<br>	
	 * 
	 * The Images of the component types should be added into the current project folder or subfolders.
	 * Preferable icon size is 16x16 px.	
	 * 
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable4ComponentTypes() {
		if (jTableComponentTypes == null) {
			jTableComponentTypes = new JTable();
			jTableComponentTypes.setFillsViewportHeight(true);
			jTableComponentTypes.setShowGrid(false);
			jTableComponentTypes.setRowHeight(20);
			jTableComponentTypes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableComponentTypes.setModel(getTableModel4ComponentTypes());
			jTableComponentTypes.setAutoCreateRowSorter(true);
			jTableComponentTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableComponentTypes.getTableHeader().setReorderingAllowed(false);
			
			// --- Define the sorter ------------------------------------------
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(getTableModel4ComponentTypes());
			sorter.setComparator(getColumnHeaderIndexComponents(COL_ShowLabel), new Comparator<Boolean>() {
				@Override
				public int compare(Boolean o1, Boolean o2) {
					return o1.compareTo(o2);
				}
			});
			sorter.setComparator(getColumnHeaderIndexComponents(COL_EdgeWidth), new Comparator<Float>() {
				@Override
				public int compare(Float o1, Float o2) {
					return o1.compareTo(o2);
				}
			});
			sorter.setComparator(getColumnHeaderIndexComponents(COL_EdgeColor), new Comparator<Color>() {
				@Override
				public int compare(Color o1, Color o2) {
					Integer o1RGB = o1.getRGB();
					Integer o2RGB = o2.getRGB();
					return o1RGB.compareTo(o2RGB);
				}
			});
			jTableComponentTypes.setRowSorter(sorter);

			
			// --- Define the first sort order --------------------------------
			List<SortKey> sortKeys = new ArrayList<SortKey>();
			for (int i = 0; i < jTableComponentTypes.getColumnCount(); i++) {
			    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
			}
			jTableComponentTypes.getRowSorter().setSortKeys(sortKeys);

			
			// --- Configure the editor and the renderer of the cells ---------
			TableColumnModel tcm = jTableComponentTypes.getColumnModel();
			
			//Set up renderer and editor for domain column
			TableColumn domainColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_Domain));
			domainColumn.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxDomains(null)));
			domainColumn.setPreferredWidth(20);

			
			//Set up renderer and editor for the agent class column
			TableColumn agentClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_AgentClass));
			agentClassColumn.setCellEditor(this.getAgentClassesCellEditor());
			agentClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			//Set up renderer and editor for Graph prototype column
			TableColumn prototypeClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_GraphPrototyp));
			prototypeClassColumn.setCellEditor(this.getPrototypeClassesCellEditor());
			prototypeClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			//Set up renderer and editor for the adapter class column
			TableColumn adapterClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_AdapterClass));
			adapterClassColumn.setCellEditor(this.getAdapterClassesCellEditor());
			adapterClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			
			//Set up renderer and editor for show label
			TableColumn showLabelClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_ShowLabel));
			showLabelClassColumn.setCellEditor(new TableCellEditor4CheckBox(this.getCheckBoxEdgeWidth()));
			showLabelClassColumn.setCellRenderer(new TableCellRenderer4CheckBox());
			showLabelClassColumn.setMinWidth(80);
			showLabelClassColumn.setMaxWidth(80);

			
			//Set up Editor for the ImageIcon column
			TableColumn imageIconColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_Image));
			imageIconColumn.setCellEditor(new TableCellEditor4Image(this, currProject));		
			imageIconColumn.setMinWidth(80);
			imageIconColumn.setMaxWidth(80);
			
			//Set up renderer and editor for the edge width.	        
			TableColumn edgeWidthColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_EdgeWidth));
			edgeWidthColumn.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxEdgeWidth()));
			edgeWidthColumn.setMinWidth(60);
			edgeWidthColumn.setMaxWidth(60);
			
			//Set up renderer and editor for the  Color column.	        
			TableColumn colorColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_EdgeColor));
			colorColumn.setCellEditor(new TableCellEditor4Color());
			colorColumn.setCellRenderer(new TableCellRenderer4Color(true));			
			colorColumn.setMinWidth(50);
			colorColumn.setMaxWidth(50);
			
		}
		return jTableComponentTypes;
	}
	
	/**
	 * This method initiates the jTableClasses' TableModel.
	 *
	 * @return The TableModel
	 */
	private DefaultTableModel getTableModel4ComponentTypes(){
		
		if (componentTypesModel== null) {
			
			Vector<Vector<Object>> dataRows = new Vector<Vector<Object>>();
			
			// Set table entries for defined assignments, if any
			if(this.currCompTypSettings!=null){
				Iterator<String> etsIter = this.currCompTypSettings.keySet().iterator();
				while(etsIter.hasNext()){
					
					String etName = etsIter.next();
					ComponentTypeSettings cts = this.currCompTypSettings.get(etName);
					
					String imagePath = cts.getEdgeImage();
					Float edgeWidth = cts.getEdgeWidth();
					if (edgeWidth==0) {
						edgeWidth = GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH;
					}
					Color edgeColor = new Color(Integer.parseInt(cts.getColor()));
					
					// --- Create row vector --------------
					Vector<Object> newRow = new Vector<Object>();
					for (int i = 0; i < this.getColumnHeaderComponents().size(); i++) {
						if (i == getColumnHeaderIndexComponents(COL_TypeSpecifier)) {
							newRow.add(etName);
						} else if (i == getColumnHeaderIndexComponents(COL_Domain)) {
							newRow.add(cts.getDomain());
						} else if (i == getColumnHeaderIndexComponents(COL_AgentClass)) {
							newRow.add(cts.getAgentClass());
						} else if (i == getColumnHeaderIndexComponents(COL_GraphPrototyp)) {
							newRow.add(cts.getGraphPrototype());
						} else if (i == getColumnHeaderIndexComponents(COL_AdapterClass)) {
							newRow.add(cts.getAdapterClass());
						} else if (i == getColumnHeaderIndexComponents(COL_ShowLabel)) {
							newRow.add(cts.isShowLabel());
						} else if (i == getColumnHeaderIndexComponents(COL_Image)) {
							newRow.add(this.createImageIcon(imagePath, imagePath));
						} else if (i == getColumnHeaderIndexComponents(COL_EdgeWidth)) {
							newRow.add(edgeWidth);
						} else if (i == getColumnHeaderIndexComponents(COL_EdgeColor)) {
							newRow.add(edgeColor);
						}
					}
					dataRows.add(newRow);
				
				}
			}
			// --------------------------------------------
			// --- define the TableModel --- Start --------
			componentTypesModel = new DefaultTableModel(dataRows, this.getColumnHeaderComponents()){
				private static final long serialVersionUID = 3550155601170744633L;
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int columnIndex) {
			            if(columnIndex==getColumnHeaderIndexComponents(COL_Image))
			            	return ImageIcon.class;
			            else if(columnIndex==getColumnHeaderIndexComponents(COL_EdgeColor))
			            	return Color.class;
			            else 
			            	return String.class;
			        }
				};
			// --- define the TableModel --- End ----------
			// --------------------------------------------
			// ----------------------------------------------------------------
		}
		return componentTypesModel;
	}
	
	/**
	 * This method adds a new row to the jTableClasses' TableModel.
	 */
	private void addComponentRow(){
		// --- Create row vector --------------
		Vector<Object> newRow = new Vector<Object>();
		for (int i = 0; i < this.getColumnHeaderComponents().size(); i++) {
			if (i == getColumnHeaderIndexComponents(COL_TypeSpecifier)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexComponents(COL_AgentClass)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexComponents(COL_Domain)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME);
			} else if (i == getColumnHeaderIndexComponents(COL_GraphPrototyp)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexComponents(COL_AdapterClass)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexComponents(COL_ShowLabel)) {
				newRow.add(true);
			} else if (i == getColumnHeaderIndexComponents(COL_Image)) {
				newRow.add(createImageIcon(null,"MissingIcon"));
			} else if (i == getColumnHeaderIndexComponents(COL_EdgeWidth)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_EDGE_WIDTH);
			} else if (i == getColumnHeaderIndexComponents(COL_EdgeColor)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_EDGE_COLOR);
			}
		}
		
		this.getTableModel4ComponentTypes().addRow(newRow);
		int newIndex = this.getTableModel4ComponentTypes().getRowCount() - 1;
		newIndex = this.getJTable4ComponentTypes().convertRowIndexToView(newIndex);
		
		int editCell = this.getColumnHeaderIndexComponents(COL_TypeSpecifier);
		this.getJTable4ComponentTypes().changeSelection(newIndex, editCell, false, false);
		this.getJTable4ComponentTypes().editCellAt(newIndex, editCell);
		
	}
	
	/**
	 * This method removes a row from the jTableClasses' TableModel.
	 *
	 * @param rowNumTable the row num table
	 */
	private void removeComponentRow(int rowNumTable){
		int rowNumModel = this.getJTable4ComponentTypes().convertRowIndexToModel(rowNumTable);
		((DefaultTableModel)getJTable4ComponentTypes().getModel()).removeRow(rowNumModel);
	}

	/**
	 * This method initializes jButtonAddComponentRow	.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonAddComponentRow() {
		if (jButtonAddComponentRow == null) {
			jButtonAddComponentRow = new JButton();
			jButtonAddComponentRow.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListPlus.png")));
			jButtonAddComponentRow.addActionListener(this);
		}
		return jButtonAddComponentRow;
	}
	
	/**
	 * Returns the ClassSelectionDialog cell editor for Agent classes.
	 * @return cell editor for the class selection
	 */
	private TableCellEditor4ClassSelector getAgentClassesCellEditor(){
		if(agentClassesCellEditor == null){
			agentClassesCellEditor = new TableCellEditor4ClassSelector(Application.getMainWindow(), Agent.class, "", "", Language.translate("Agenten"), true);
		}
		return agentClassesCellEditor;
	}
	/**
	 * Returns the ClassSelectionDialog cell editor for NetworkComponentAdapter classes.
	 * @return cell editor for the class selection
	 */
	private TableCellEditor4ClassSelector getAdapterClassesCellEditor(){
		if(adapterClassesCellEditor == null){
			adapterClassesCellEditor = new TableCellEditor4ClassSelector(Application.getMainWindow(), NetworkComponentAdapter.class, "", "", Language.translate("Erweiterungsadapter für Netzwerkkomponenten"), true);
		}
		return adapterClassesCellEditor;
	}
	/**
	 * Returns the ClassSelectionDialog cell editor for classes of GraphElementPrototype.
	 * @return cell editor for the class selection
	 */
	private TableCellEditor4ClassSelector getPrototypeClassesCellEditor(){
		if(prototypeClassesCellEditor == null){
			prototypeClassesCellEditor = new TableCellEditor4ClassSelector(Application.getMainWindow(), GraphElementPrototype.class, "", "", Language.translate("Graph-Prototypen"), false);
		}
		return prototypeClassesCellEditor;
	}
	
	/**
	 * Gets the combo4 edge width.
	 * @return the combo4 edge width
	 */
	private JComboBoxWide<Float> getJComboBoxEdgeWidth() {
		Float[] sizeList = {(float) 1.0, (float) 1.5, (float) 2.0, (float) 2.5, (float) 3.0, (float) 3.5, (float) 4.0, (float) 4.5, (float) 5.0, (float) 6.0, (float) 7.0, (float) 8.0, (float) 9.0, (float) 10.0, (float) 12.5, (float) 15.0, (float) 17.5, (float) 20.0};
		DefaultComboBoxModel<Float> cbmSizes = new DefaultComboBoxModel<Float>(sizeList); 
		return new JComboBoxWide<Float>(cbmSizes);
	}

	/**
	 * Gets the check box edge width.
	 * @return the check box edge width
	 */
	private JCheckBox getCheckBoxEdgeWidth() {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setHorizontalAlignment(SwingConstants.CENTER);
		return checkBox;
	}
	
	/**
	 * This method initializes jComboBoxNodeSize	.
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBoxWide<Integer> getJComboBoxNodeSize() {
		Integer[] sizeList = {0,5,6,7,8,9,10,11,12,13,14,15,20,25,30,35,40,45,50};
		DefaultComboBoxModel<Integer> cbmSizes = new DefaultComboBoxModel<Integer>(sizeList); 

		JComboBoxWide<Integer> jComboBoxNodeSize = new JComboBoxWide<Integer>(cbmSizes);
		jComboBoxNodeSize.setPreferredSize(new Dimension(50, 26));
		return jComboBoxNodeSize;
	}
	
	/**
	 * Gets the domain vector.
	 * @return the domain vector
	 */
	public Vector<String> getDomainVector() {
		Vector<String> domainVector =  new Vector<String>();
		for (int i = 0; i < this.getTableModel4Domains().getRowCount(); i++) {
			String domain = (String) this.getTableModel4Domains().getValueAt(i, 0);
			if (domain!=null) {
				domainVector.addElement(domain);	
			}
		}
		Collections.sort(domainVector);
		return domainVector;
	}
	
	/**
	 * Returns a JComboBox with the list of Domains.
	 * @return the JComboBox with the available domains
	 */
	private JComboBoxWide<String> getJComboBoxDomains(Vector<String> domainVector) {
		DefaultComboBoxModel<String> comboBoxModel4Domains = null;
		if (domainVector==null) {
			comboBoxModel4Domains = new DefaultComboBoxModel<String>(this.getDomainVector());
		} else {
			comboBoxModel4Domains = new DefaultComboBoxModel<String>(domainVector);
		}
		return new JComboBoxWide<String>(comboBoxModel4Domains);
	}
	
	/**
	 * Returns the JComboBox for the possible cluster shapes.
	 * @return the JComboBox for the possible cluster shapes
	 */
	private JComboBoxWide<String> getJComboBoxClusterShape() {
		
		DefaultComboBoxModel<String> cbmShape = new DefaultComboBoxModel<String>(); 
		cbmShape.addElement(GeneralGraphSettings4MAS.SHAPE_ELLIPSE);
		cbmShape.addElement(GeneralGraphSettings4MAS.SHAPE_RECTANGLE);
		cbmShape.addElement(GeneralGraphSettings4MAS.SHAPE_ROUND_RECTANGLE);
		cbmShape.addElement(GeneralGraphSettings4MAS.SHAPE_REGULAR_POLYGON);
		cbmShape.addElement(GeneralGraphSettings4MAS.SHAPE_REGULAR_STAR);
		
		JComboBoxWide<String> jComboBoxClusterShape = new JComboBoxWide<String>(cbmShape);
		jComboBoxClusterShape.setPreferredSize(new Dimension(50, 26));
		return jComboBoxClusterShape;
	}
	
	/**
	 * This method initializes jButtonRemoveComponentRow	.
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonRemoveComponentRow() {
		if (jButtonRemoveComponentRow == null) {
			jButtonRemoveComponentRow = new JButton();
			jButtonRemoveComponentRow.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
			jButtonRemoveComponentRow.addActionListener(this);
		}
		return jButtonRemoveComponentRow;
	}

	/**
	 * Returns an ImageIcon, or a default MissingIcon(a red X) if image not found.
	 *
	 * @param path the path
	 * @param description the description
	 * @return ImageIcon
	 */
	public ImageIcon createImageIcon(String path, String description) {
		if(path!=null ){		
			if (path.equals("MissingIcon")) {
				return new MissingIcon(description);
			} else {
			    ImageIcon imageIcon = GraphGlobals.getImageIcon(path);
				if (imageIcon!=null) {
					return imageIcon;
				} else {
					System.err.println("Couldn't find file: " + path);
			        return new MissingIcon(description);
				}
			}
		} else{
		    return (new MissingIcon(description));		    
		}
	}

	/**
	 * This method initializes jPanelComponents	.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelComponents() {
		if (jPanelComponents == null) {
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.fill = GridBagConstraints.BOTH;
			gridBagConstraints24.gridwidth = 2;
			gridBagConstraints24.gridx = -1;
			gridBagConstraints24.gridy = 1;
			gridBagConstraints24.weightx = 1.0;
			gridBagConstraints24.weighty = 1.0;
			gridBagConstraints24.ipadx = 0;
			gridBagConstraints24.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new Insets(5, 5, 3, 5);
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(5, 5, 3, 5);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 0;
			
			jPanelComponents = new JPanel();
			jPanelComponents.setLayout(new GridBagLayout());
			jPanelComponents.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelComponents.add(getJButtonAddComponentRow(), gridBagConstraints1);
			jPanelComponents.add(getJButtonRemoveComponentRow(), gridBagConstraints6);
			jPanelComponents.add(getJScrollPaneClassTableComponents(), gridBagConstraints24);
		}
		return jPanelComponents;
	}

	/**
	 * This method initializes jCheckBoxSnap2Grid	.
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxSnap2Grid() {
		if (jCheckBoxSnap2Grid == null) {
			jCheckBoxSnap2Grid = new JCheckBox();
			jCheckBoxSnap2Grid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			jCheckBoxSnap2Grid.setText("Raster verwenden");
			jCheckBoxSnap2Grid.setText(Language.translate(jCheckBoxSnap2Grid.getText()));
		}
		return jCheckBoxSnap2Grid;
	}

	/**
	 * This method initializes jPanelButtonOkCancel	.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelButtonOkCancel() {
		if (jPanelButtonOkCancel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.EAST;
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 0.0;
			gridBagConstraints8.insets = new Insets(0, 0, 0, 30);
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(0, 30, 0, 0);
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.anchor = GridBagConstraints.CENTER;
			gridBagConstraints9.gridx = 1;
			jPanelButtonOkCancel = new JPanel();
			jPanelButtonOkCancel.setLayout(new GridBagLayout());
			jPanelButtonOkCancel.add(getJButtonCancel(), gridBagConstraints9);
			jPanelButtonOkCancel.add(getJButtonConfirm(), gridBagConstraints8);
		}
		return jPanelButtonOkCancel;
	}

	/**
	 * This method initializes jComboBoxGridWidth	.
	 *
	 * @return javax.swing.JComboBox
	 */
	private JSpinner getJSpinnerGridWidth() {
		if (jSpnnerGridWidth == null) {
			jSpnnerGridWidth = new JSpinner(new SpinnerNumberModel(5.0, 0.1, 100.0, 0.1));
			jSpnnerGridWidth.setPreferredSize(new Dimension(60, 26));
		}
		return jSpnnerGridWidth;
	}
	
	/**
	 * This method initializes jComboBoxEdgeShapes	.
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox<EdgeShape> getJComboBoxEdgeShapes() {
		if (jComboBoxEdgeShapes == null) {
			jComboBoxEdgeShapes = new JComboBox<EdgeShape>(this.getComboBoxModel4EdgeShapes());
		}
		return jComboBoxEdgeShapes;
	}
	/**
	 * Gets the combo box model4 edge shapes.
	 * @return the combo box model4 edge shapes
	 */
	private DefaultComboBoxModel<EdgeShape> getComboBoxModel4EdgeShapes() {
		if (comboBoxModelEdgeShapes==null) {
			comboBoxModelEdgeShapes = new DefaultComboBoxModel<EdgeShape>();
			List<EdgeShape> shapes = new ArrayList<EdgeShape>(Arrays.asList(EdgeShape.values()));
			for (int i = 0; i < shapes.size(); i++) {
				comboBoxModelEdgeShapes.addElement(shapes.get(i));
			}
		}
		return comboBoxModelEdgeShapes;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if(ae.getSource()==this.getJButtonAddComponentRow()){
			// --- Add a new row to the component types table -------
			this.addComponentRow();
			
		} else if(ae.getSource()==this.getJButtonRemoveComponentRow()) {
			// --- Remove a row from the component types table ------
			if(getJTable4ComponentTypes().getSelectedRow() > -1){
				this.removeComponentRow(getJTable4ComponentTypes().getSelectedRow());
			}
		
		} else if(ae.getSource()==this.getJButtonAddDomain()) {
			// --- Add a new row to the domain table ----------------
			this.addDomainRow();
			
		} else if(ae.getSource()==this.getJButtonRemoveDomainntRow()) {
			// --- Remove a row from the component types table ------
			if(getJTable4DomainTypes().getSelectedRow() > -1){
				this.removeDomainRow(getJTable4DomainTypes().getSelectedRow());
			}
		
		} else if(ae.getSource()==this.getJButtonConfirm()) {
			// --- Check and prepare new settings -------------------
			TreeMap<String, ComponentTypeSettings> ctsHash = new TreeMap<String, ComponentTypeSettings>();
			TreeMap<String, DomainSettings> dsHash = new TreeMap<String, DomainSettings>();
			
			// --- Get the domain settings from table ---------------
			JTable jtDomains = this.getJTable4DomainTypes();
			DefaultTableModel dtmDomains = this.getTableModel4Domains();
			// --- Confirm, apply changes in table ------------------						
			TableCellEditor tceDomains = jtDomains.getCellEditor();
			if (tceDomains!=null) {
				tceDomains.stopCellEditing();
			}
			for(int row=0; row<dtmDomains.getRowCount(); row++){
				
				String name = (String) dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_DomainName));
				if(name!=null && name.length()!=0){
					
					String adapterClass  = (String) dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_AdapterClass));
					boolean showLabel 	 = (Boolean)dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_ShowLable));
					Integer vertexSize 	 = (Integer)dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_VertexSize));
					Color color 		 = (Color)  dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_VertexColor));
					String colorStr 	 = String.valueOf(color.getRGB());
					Color colorPicked	 = (Color)  dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_VertexColorPicked));					
					String colorPickStr	 = String.valueOf(colorPicked.getRGB());
					String clusterShape  = (String) dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_ClusterShape));
					String clusterAgent  = (String) dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_ClusterAgent));
					
					DomainSettings ds = new DomainSettings();
					ds.setAdapterClass(adapterClass);
					ds.setShowLabel(showLabel);
					ds.setVertexSize(vertexSize);
					ds.setVertexColor(colorStr);
					ds.setVertexColorPicked(colorPickStr);
					ds.setClusterShape(clusterShape);
					ds.setClusterAgent(clusterAgent);
					
					Error error = this.isDomainConfigError(name, ds, dsHash);
					if (error!=null) {
						// --- Set focus to error position ---------- 
						this.getJTabbedPane().setSelectedIndex(0);
						int tableRow = jtDomains.convertRowIndexToView(row);
						jtDomains.setRowSelectionInterval(tableRow, tableRow);
						JOptionPane.showMessageDialog(this, error.getMessage(), error.getTitle(), JOptionPane.WARNING_MESSAGE);
						return;
					}
					dsHash.put(name, ds);
				}
			}
			this.currDomainSettings = dsHash;
			
			// --- Get the component type definitions from table ----
			JTable jtComponents = this.getJTable4ComponentTypes();
			DefaultTableModel dtmComponents = this.getTableModel4ComponentTypes();
			// --- Confirm, apply changes in table ------------------						
			TableCellEditor tceComponents = jtComponents.getCellEditor();
			if (tceComponents!=null) {
				tceComponents.stopCellEditing();
			}
			for(int row=0; row<dtmComponents.getRowCount(); row++){
				
				String name = (String) dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_TypeSpecifier));
				if(name!=null && name.length()!=0){
					
					String agentClass 	 = (String)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_AgentClass));
					String domain 	 	 = (String)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_Domain));
					String graphProto 	 = (String)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_GraphPrototyp));
					String adapterClass	 = (String)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_AdapterClass));
					ImageIcon imageIcon  = (ImageIcon)dtmComponents.getValueAt(row,this.getColumnHeaderIndexComponents(COL_Image));
					String imageIconDesc = imageIcon.getDescription();
					float edgeWidth 	 = (Float)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_EdgeWidth));
					Color color 		 = (Color)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_EdgeColor));
					String colorString 	 = String.valueOf(color.getRGB());
					boolean showLable 	 = (Boolean) dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_ShowLabel));
					
					ComponentTypeSettings cts = new ComponentTypeSettings();
					cts.setDomain(domain);
					cts.setAgentClass(agentClass);
					cts.setGraphPrototype(graphProto);
					cts.setAdapterClass(adapterClass);
					cts.setEdgeImage(imageIconDesc);
					cts.setColor(colorString);
					cts.setEdgeWidth(edgeWidth);
					cts.setShowLabel(showLable);

					Error error = this.isComponentTypeError(name, cts, ctsHash);
					if (error!=null) {
						// --- Set focus to error position ---------- 
						this.getJTabbedPane().setSelectedIndex(1);
						int tableRow = jtComponents.convertRowIndexToView(row);
						jtComponents.setRowSelectionInterval(tableRow, tableRow);
						JOptionPane.showMessageDialog(this, error.getMessage(), error.getTitle(), JOptionPane.WARNING_MESSAGE);
						return;
					}
					ctsHash.put(name, cts);
				}
			}
			this.currCompTypSettings = ctsHash;
			
			this.currSnap2Grid = this.jCheckBoxSnap2Grid.isSelected();
			this.currSnapRaster = (Double)this.jSpnnerGridWidth.getValue(); 
			this.currEdgeShape = (EdgeShape) this.jComboBoxEdgeShapes.getSelectedItem();
			
			this.canceled = false;
			this.setVisible(false);
			
		} else if(ae.getSource()==this.getJButtonCancel()) {
			// --- Canceled, discard changes --------------
			this.canceled = true;
			this.setVisible(false);
			
		}
	
	}

	/**
	 * Checks if there is domain configuration error.
	 *
	 * @param dsName the DomainSettings name
	 * @param ds the DomainSettings to check
	 * @param dsHash the DomainSettings hash that contains the already checked DomainSettings
	 * @return true, if is domain configuration error
	 */
	private Error isDomainConfigError(String dsName, DomainSettings ds, TreeMap<String, DomainSettings> dsHash) {
		
		String title = "";
		String message = "";
		if (dsHash.get(dsName)!=null) {
			// --- Duplicate DomainSettings -------------------------
			title = Language.translate("Duplicate Domain", Language.EN) + "!";
			message = Language.translate("The following domain exists at least twice", Language.EN) + ": '" + dsName + "' !";
			return new Error(title, message);
		}
		return null;
	}
	
	/**
	 * Checks if there is component type error.
	 *
	 * @param ctsName the ComponentTypeSettings name
	 * @param cts the ComponentTypeSettings to check
	 * @param ctsHash the ComponentTypeSettings hash that contains the already checked ComponentTypeSettings
	 * @return true, if is component type error
	 */
	private Error isComponentTypeError(String ctsName, ComponentTypeSettings cts, TreeMap<String, ComponentTypeSettings> ctsHash) {
		
		String title = "";
		String message = "";
		if (ctsHash.get(ctsName)!=null) {
			// --- Duplicate ComponentTypeSettings ------------------
			title = Language.translate("Duplicate Component Type", Language.EN) + "!";
			message = Language.translate("The following component type exists at least twice", Language.EN) + ": '" + ctsName + "' !";
			return new Error(title, message);
		}
		
		if (cts.getGraphPrototype()==null || cts.getGraphPrototype().equals("")) {
			// --- Duplicate ComponentTypeSettings ------------------
			title = Language.translate("Component Type Error", Language.EN) + "!";
			message = Language.translate("No Graph-Prototype defined for", Language.EN) + " '" + ctsName + "' !";
			return new Error(title, message);
		}
		return null;
	}
	
	/**
	 * The Class Error is used to describe an error.
	 */
	private class Error {
		
		private String message;
		private String title;
		
		/**
		 * Instantiates a new error.
		 *
		 * @param title the title
		 * @param message the message
		 */
		public Error(String title, String message) {
			this.setTitle(title);
			this.setMessage(message);
		}
		
		/**
		 * Gets the title.
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}
		/**
		 * Sets the title.
		 * @param title the new title
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		
		/**
		 * Gets the message.
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
		/**
		 * Sets the message.
		 * @param messag the new message
		 */
		public void setMessage(String messag) {
			this.message = messag;
		}
	}
	
	
} 
