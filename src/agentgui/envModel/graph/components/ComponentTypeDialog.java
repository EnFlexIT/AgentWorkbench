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

import jade.content.Concept;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.ClassSelector;
import agentgui.core.gui.imaging.MissingIcon;
import agentgui.core.project.Project;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import java.lang.String;

/**
 * GUI dialog for configuring network component types
 *  
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public class ComponentTypeDialog extends JDialog implements ActionListener{
	
	/** Generated serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private final String pathImage = GraphGlobals.getPathImages();
	
	private Vector<String> columnHeaderComponents 	= null;  //  @jve:decl-index=0:
	public final String COL_TypeSpecifier 			= Language.translate("Type-Specifier", Language.EN);  	//  @jve:decl-index=0:
	public final String COL_Domain 					= Language.translate("Subnetwork", Language.EN);  		//  @jve:decl-index=0:
	public final String COL_AgentClass 				= Language.translate("Agent class", Language.EN); 		//  @jve:decl-index=0:
	public final String COL_GraphPrototyp 			= Language.translate("Graph-prototype", Language.EN);  	//  @jve:decl-index=0:
	public final String COL_ShowLabel 				= Language.translate("Show label", Language.EN);  		//  @jve:decl-index=0:
	public final String COL_Image 					= Language.translate("Image", Language.EN);  			//  @jve:decl-index=0:
	public final String COL_EdgeWidth 				= Language.translate("Width", Language.EN);  			//  @jve:decl-index=0:
	public final String COL_EdgeColor 				= Language.translate("Color", Language.EN);  			//  @jve:decl-index=0:

	
	private Vector<String> columnHeaderDomains 		= null;  //  @jve:decl-index=0:
	public final String COL_D_DomainName 			= Language.translate("Name", Language.EN);  			//  @jve:decl-index=0:
	public final String COL_D_OntologyClass			= Language.translate("Ontology class", Language.EN); 	//  @jve:decl-index=0:
	public final String COL_D_VertexSize 			= Language.translate("Vertex size", Language.EN);  		//  @jve:decl-index=0:
	public final String COL_D_VertexColor			= Language.translate("Color", Language.EN);  			//  @jve:decl-index=0:
	public final String COL_D_VertexColorPicked 	= Language.translate("Color picked", Language.EN);  	//  @jve:decl-index=0:
	public final String COL_D_ShowLable				= Language.translate("Show label", Language.EN);  		//  @jve:decl-index=0:
	
	private HashMap<String, ComponentTypeSettings> currCompTypSettings = null;
	private HashMap<String, DomainSettings> currDomainSettings = null;
	private boolean currSnap2Grid = true;
	private double currSnapRaster = 5; 
	
	private Project currProject = null;
	private boolean canceled = false;

	private JTabbedPane jTabbedPane = null;
	
	private JPanel jContentPane = null;
	private JPanel jPanelDomains = null;
	private JPanel jPanelComponents = null;
	private JPanel jPanelGeneralSettings = null;
	private JPanel jPanelButtonOkCancel = null;
	private JScrollPane jScrollPaneClassTableComponents = null;
	private JScrollPane jScrollPaneClassTableDomains = null;
	
	private JLabel jLabelGridHeader = null;
	private JLabel jLabelGuideGridWidth = null;
	private JCheckBox jCheckBoxSnap2Grid = null;
	private JSpinner jSpnnerGridWidth = null;

	private JButton jButtonConfirm = null;
	private JButton jButtonCancel = null;
	private JButton jButtonAddComponentRow = null;
	private JButton jButtonRemoveComponentRow = null;
	private JButton jButtonAddDomain = null;
	private JButton jButtonRemoveDomainntRow = null;
	private JButton jButtonSelectNodeClass = null;
	
	private JTable jTableComponentTypes = null;
	private DefaultTableModel componentTypesModel = null;
	private JTable jTableDomainTypes = null;
	private DefaultTableModel domainTableModel = null;
	
	private TableCellEditor4ClassSelector prototypeClassesCellEditor = null;  //  @jve:decl-index=0:
	private ClassSelector nodeClassSelector = null;
	
	/**
	 * This is the default constructor.
	 *
	 * @param currentCTS the current HashMap with the ComponentTypeSettings
	 * @param project the current project
	 */
	public ComponentTypeDialog(GeneralGraphSettings4MAS graphSettings, Project project) {
		this.currCompTypSettings = graphSettings.getCurrentCTS();
		this.currDomainSettings = graphSettings.getDomainSettings();
		this.currSnap2Grid = graphSettings.isSnap2Grid();
		this.currSnapRaster = graphSettings.getSnapRaster();
		this.currProject = project;
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(980, 589);
		this.setTitle("Komponententyp-Definition");
		this.setTitle(Language.translate(this.getTitle()));
		this.setModal(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				canceled = true;
				setVisible(false);
			}
		});
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);		
	    
	    this.setContentPane(getJContentPane());
	    this.setGeneralConfiguration();
	    
	    // --- In case that we're in an executed MAS ------
	    if (this.currProject==null) {
	    	this.getJTable4ComponentTypes().setEnabled(false);
	    	this.getJButtonAddComponentRow().setEnabled(false);
	    	this.getJButtonRemoveComponentRow().setEnabled(false);
	    	this.getJButtonConfirm().setEnabled(false);
	    }
	    
	}
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * This will set the current configuration for the nodes (propagation points) to the
	 * current view
	 */
	private void setGeneralConfiguration() {
		// --- Get the Guide grid configuration -------------------------------
		this.getJCheckBoxSnap2Grid().setSelected(this.currSnap2Grid);
		this.getJSpinnerGridWidth().setValue(this.currSnapRaster);
	}
	
	/**
	 * Returns all component type settings.
	 * @return the ComponentTypeSettings
	 */
	public HashMap<String, ComponentTypeSettings> getComponentTypeSettings() {
		return this.currCompTypSettings;
	}
	/**
	 * Returns the domain settings.
	 * @return the DomainSettings
	 */
	public HashMap<String, DomainSettings> getDomainSettings() {
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
	 * Returns the GeneralGraphSettings4MAS.
	 * @return the GeneralGraphSettings4MAS for the graph environment
	 */
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		GeneralGraphSettings4MAS genSettings = new GeneralGraphSettings4MAS();
		genSettings.setCurrentCTS(getComponentTypeSettings());
		genSettings.setDomainSettings(getDomainSettings());
		genSettings.setSnap2Grid(isSnap2Grid());
		genSettings.setSnapRaster(getSnapRaster());
		return genSettings;
	}
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridheight = 3;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 3;
			gridBagConstraints21.insets = new Insets(15, 10, 20, 10);
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
	 * This method initializes jTabbedPane	
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
			
		}
		return jTabbedPane;
	}
	
	/**
	 * This method initializes jPanelGeneralSettings	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGeneralSettings() {
		if (jPanelGeneralSettings == null) {
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.gridy = 2;
			gridBagConstraints22.insets = new Insets(5, 0, 0, 0);
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.anchor = GridBagConstraints.WEST;
			gridBagConstraints23.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints23.gridx = 1;
			gridBagConstraints23.gridy = 2;
			gridBagConstraints23.weightx = 0.0;
			gridBagConstraints23.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.gridy = 1;
			gridBagConstraints18.insets = new Insets(5, 0, 0, 0);
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.gridx = -1;
			gridBagConstraints11.gridy = -1;
			gridBagConstraints11.insets = new Insets(0, 0, 5, 0);
			
			jLabelGridHeader = new JLabel();
			jLabelGridHeader.setText("Hilfs-Raster");
			jLabelGridHeader.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabelGridHeader.setText(Language.translate(jLabelGridHeader.getText()));
			
			jLabelGuideGridWidth = new JLabel();
			jLabelGuideGridWidth.setText("Raster-Breite");
			jLabelGuideGridWidth.setText(Language.translate(jLabelGuideGridWidth.getText()));
			
			jPanelGeneralSettings = new JPanel();
			jPanelGeneralSettings.setLayout(new GridBagLayout());
			jPanelGeneralSettings.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelGeneralSettings.add(jLabelGridHeader, gridBagConstraints11);
			jPanelGeneralSettings.add(getJCheckBoxSnap2Grid(), gridBagConstraints18);
			jPanelGeneralSettings.add(getJSpinnerGridWidth(), gridBagConstraints23);
			jPanelGeneralSettings.add(jLabelGuideGridWidth, gridBagConstraints22);
		}
		return jPanelGeneralSettings;
	}
	/**
	 * This method initializes the nodeClassSelector
	 * @return The nodeClassSelector
	 */
	private ClassSelector getNodeClassSelector(){
		if(nodeClassSelector == null){
			Class<?> superClass = Concept.class;
			String currValue = null;
			String defaultValue = null;
			String description = Language.translate("Ontologie-Klasse für Übergabepunkte");
			nodeClassSelector = new ClassSelector(Application.MainWindow, superClass, currValue, defaultValue, description);
		}
		return nodeClassSelector;
	}

	/**
	 * This method initializes jButtonConfirm	
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
	 * This method initializes jButtonCancel	
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
	 * This method initializes jPanelDomains	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDomains() {
		if (jPanelDomains == null) {
			
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 3;
			gridBagConstraints13.gridwidth = 2;
			gridBagConstraints13.weightx = 1.0;
			
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridx = 6;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			gridBagConstraints4.insets = new Insets(5, 5, 0, 0);
			
			jPanelDomains = new JPanel();
			jPanelDomains.setLayout(new GridBagLayout());
			jPanelDomains.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jPanelDomains.add(getJButtonSelectNodeClass(), gridBagConstraints4);
			jPanelDomains.add(getJScrollPaneClassTableDomains(), gridBagConstraints13);
			jPanelDomains.add(getJButtonAddDomain(), gridBagConstraints2);
			jPanelDomains.add(getJButtonRemoveDomainntRow(), gridBagConstraints3);
		}
		return jPanelDomains;
	}
	/**
	 * This method initializes jButtonAddDomain	
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
	 * This method initializes jButtonRemoveDomainntRow	
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
	 * This method initializes jScrollPaneClassTableDomains	
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
	 * This method initializes jTableDomainTypes	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTable4DomainTypes() {
		if (jTableDomainTypes == null) {
			jTableDomainTypes = new JTable();
			jTableDomainTypes.setFillsViewportHeight(true);
			jTableDomainTypes.setShowGrid(true);
			jTableDomainTypes.setRowHeight(20);
			jTableDomainTypes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableDomainTypes.setModel(getTableModel4Domains());
			jTableDomainTypes.setAutoCreateRowSorter(true);
			
			TableColumnModel tcm = jTableDomainTypes.getColumnModel();
			
			//Set up renderer and editor for the agent class column
			TableColumn agentClassColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_OntologyClass));
			agentClassColumn.setCellEditor(new TableCellEditor4AgentClass());
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
			
		}
		return jTableDomainTypes;
	}
	
	/**
	 * Gets the table model for domains.
	 * @return the table model4 domains
	 */
	private DefaultTableModel getTableModel4Domains(){
		
		if (domainTableModel== null) {
			// ----------------------------------------------------------------
			// The ComboBoxModels must be initiated before adding rows 
			//getJComboBoxAgentClasses();
			
			Vector<Vector<Object>> dataRows = new Vector<Vector<Object>>();
			
			// Set table entries for defined assignments, if any
			if(this.currDomainSettings!=null){
				Iterator<String> domainIterator = this.currDomainSettings.keySet().iterator();
				while(domainIterator.hasNext()){
					
					String domainName = domainIterator.next();
					
					DomainSettings domSetting = this.currDomainSettings.get(domainName);
					String ontologyClass = domSetting.getOntologyClass();
					Integer vertexSize = domSetting.getVertexSize();
					if (vertexSize==0) {
						vertexSize = GeneralGraphSettings4MAS.DEFAULT_VERTEX_SIZE;
					}
					Color vertexColor = new Color(Integer.parseInt(domSetting.getVertexColor()));
					Color vertexColorPicked = new Color(Integer.parseInt(domSetting.getVertexColorPicked()));
					boolean showLabel = domSetting.isShowLabel();
					
					// --- Create row vector --------------
					Vector<Object> newRow = new Vector<Object>();
					for (int i = 0; i < this.getColumnHeaderDomains().size(); i++) {
						if (i == getColumnHeaderIndexDomains(COL_D_DomainName)) {
							newRow.add(domainName);
						} else if (i == getColumnHeaderIndexDomains(COL_D_OntologyClass)) {
							newRow.add(ontologyClass);
						} else if (i == getColumnHeaderIndexDomains(COL_D_VertexSize)) {
							newRow.add(vertexSize);
						} else if (i == getColumnHeaderIndexDomains(COL_D_VertexColor)) {
							newRow.add(vertexColor);
						} else if (i == getColumnHeaderIndexDomains(COL_D_VertexColorPicked)) {
							newRow.add(vertexColorPicked);
						} else if (i == getColumnHeaderIndexDomains(COL_D_ShowLable)) {
							newRow.add(showLabel);
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
	 * This method adds a new row to the jTableClasses' TableModel4Domains
	 */
	private void addDomainRow(){
		// --- Create row vector --------------
		Vector<Object> newRow = new Vector<Object>();
		for (int i = 0; i < this.getColumnHeaderDomains().size(); i++) {
			if (i == getColumnHeaderIndexDomains(COL_D_DomainName)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexDomains(COL_D_OntologyClass)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexDomains(COL_D_ShowLable)) {
				newRow.add(true);
			} else if (i == getColumnHeaderIndexDomains(COL_D_VertexSize)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_VERTEX_SIZE);
			} else if (i == getColumnHeaderIndexDomains(COL_D_VertexColor)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_VERTEX_COLOR);
			} else if (i == getColumnHeaderIndexDomains(COL_D_VertexColorPicked)) {
				newRow.add(GeneralGraphSettings4MAS.DEFAULT_VERTEX_PICKED_COLOR);
			}
		}
		this.getTableModel4Domains().addRow(newRow);
		this.getJTable4DomainTypes().changeSelection(getJTable4DomainTypes().getRowCount()-1, 0, false, false);
		this.getJTable4DomainTypes().editCellAt(getJTable4DomainTypes().getRowCount()-1, 0);
	}
	
	/**
	 * Removes the domain row.
	 * @param rowNum the row num
	 */
	private void removeDomainRow(int rowNum){
		((DefaultTableModel)getJTable4DomainTypes().getModel()).removeRow(rowNum);
	}
	/**
	 * Gets the Vector of the column header in the needed order.
	 * @return the column header
	 */
	private Vector<String> getColumnHeaderDomains() {
		if (columnHeaderDomains==null) {
			columnHeaderDomains = new Vector<String>();
			columnHeaderDomains.add(COL_D_DomainName);
			columnHeaderDomains.add(COL_D_OntologyClass);
			columnHeaderDomains.add(COL_D_ShowLable);	
			columnHeaderDomains.add(COL_D_VertexSize);
			columnHeaderDomains.add(COL_D_VertexColor);
			columnHeaderDomains.add(COL_D_VertexColorPicked);
		}
		return columnHeaderDomains;
	}
	/**
	 * Gets the header index.
	 *
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
	 * This method initializes jScrollPaneClassTableComponents	
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
			jTableComponentTypes.setShowGrid(true);
			jTableComponentTypes.setRowHeight(20);
			jTableComponentTypes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableComponentTypes.setModel(getTableModel4ComponentTypes());
			jTableComponentTypes.setAutoCreateRowSorter(true);
			
			TableColumnModel tcm = jTableComponentTypes.getColumnModel();
			
			//Set up renderer and editor for the agent class column
			TableColumn agentClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_AgentClass));
			agentClassColumn.setCellEditor(new TableCellEditor4AgentClass());
			agentClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			//Set up renderer and editor for Graph prototype column
			TableColumn prototypeClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_GraphPrototyp));
			prototypeClassColumn.setCellEditor(getPrototypeClassesCellEditor());
			prototypeClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			//Set up renderer and editor for show label
			TableColumn showLabelClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_ShowLabel));
			showLabelClassColumn.setCellEditor(new TableCellEditor4CheckBox(this.getCheckBoxEdgeWidth()));
			showLabelClassColumn.setCellRenderer(new TableCellRenderer4CheckBox());
			showLabelClassColumn.setPreferredWidth(15);
			
			//Set up Editor for the ImageIcon column
			TableColumn imageIconColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_Image));
			imageIconColumn.setCellEditor(new TableCellEditor4Image(currProject));		
			imageIconColumn.setPreferredWidth(15);
			
			//Set up renderer and editor for the edge width.	        
			TableColumn edgeWidthColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_EdgeWidth));
			edgeWidthColumn.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxEdgeWidth()));
			edgeWidthColumn.setPreferredWidth(15);
			
			//Set up renderer and editor for the  Color column.	        
			TableColumn colorColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_EdgeColor));
			colorColumn.setCellEditor(new TableCellEditor4Color());
			colorColumn.setCellRenderer(new TableCellRenderer4Color(true));			
			colorColumn.setPreferredWidth(15);
		}
		return jTableComponentTypes;
	}
	
	
	/**
	 * Gets the Vector of the column header in the needed order.
	 * @return the column header
	 */
	private Vector<String> getColumnHeaderComponents() {
		if (columnHeaderComponents==null) {
			columnHeaderComponents = new Vector<String>();
			columnHeaderComponents.add(COL_TypeSpecifier);
			columnHeaderComponents.add(COL_Domain);
			columnHeaderComponents.add(COL_AgentClass);
			columnHeaderComponents.add(COL_GraphPrototyp);
			columnHeaderComponents.add(COL_ShowLabel);
			columnHeaderComponents.add(COL_Image);			
			columnHeaderComponents.add(COL_EdgeWidth);
			columnHeaderComponents.add(COL_EdgeColor);
		}
		return columnHeaderComponents;
	}
	/**
	 * Gets the header index.
	 *
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
	 * This method initiates the jTableClasses' TableModel
	 * @return The TableModel
	 */
	private DefaultTableModel getTableModel4ComponentTypes(){
		if (componentTypesModel== null) {
			// ----------------------------------------------------------------
			// The ComboBoxModels must be initiated before adding rows 
			//getJComboBoxAgentClasses();
			
			Vector<Vector<Object>> dataRows = new Vector<Vector<Object>>();
			
			// Set table entries for defined assignments, if any
			if(this.currCompTypSettings!=null){
				Iterator<String> etsIter = this.currCompTypSettings.keySet().iterator();
				while(etsIter.hasNext()){
					String etName = etsIter.next();
					if(etName.equals("node")==false){	// The node class is defined in the JTextField, not in the table
						
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
							} else if (i == getColumnHeaderIndexComponents(COL_ShowLabel)) {
								newRow.add(cts.isShowLabel());
							} else if (i == getColumnHeaderIndexComponents(COL_Image)) {
								newRow.add(createImageIcon(imagePath, imagePath));
							} else if (i == getColumnHeaderIndexComponents(COL_EdgeWidth)) {
								newRow.add(edgeWidth);
							} else if (i == getColumnHeaderIndexComponents(COL_EdgeColor)) {
								newRow.add(edgeColor);
							}
						}
						dataRows.add(newRow);
					}
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
	 * This method adds a new row to the jTableClasses' TableModel
	 */
	private void addComponentRow(){
		// --- Create row vector --------------
		Vector<Object> newRow = new Vector<Object>();
		for (int i = 0; i < this.getColumnHeaderComponents().size(); i++) {
			if (i == getColumnHeaderIndexComponents(COL_TypeSpecifier)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexComponents(COL_AgentClass)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexComponents(COL_GraphPrototyp)) {
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
		this.getJTable4ComponentTypes().changeSelection(getJTable4ComponentTypes().getRowCount()-1, 0, false, false);
		this.getJTable4ComponentTypes().editCellAt(getJTable4ComponentTypes().getRowCount()-1, 0);
	}
	
	/**
	 * This method removes a row from the jTableClasses' TableModel
	 * @param rowNum
	 */
	private void removeComponentRow(int rowNum){
		((DefaultTableModel)getJTable4ComponentTypes().getModel()).removeRow(rowNum);
	}

	/**
	 * This method initializes jButtonAddComponentRow	
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
	
	private TableCellEditor4ClassSelector getPrototypeClassesCellEditor(){
		if(prototypeClassesCellEditor == null){
			prototypeClassesCellEditor = new TableCellEditor4ClassSelector(Application.MainWindow, GraphElementPrototype.class, "", "", Language.translate("Graph-Prototypen"));
		}
		return prototypeClassesCellEditor;
	}
	
	/**
	 * Gets the combo4 edge width.
	 * @return the combo4 edge width
	 */
	private JComboBox getJComboBoxEdgeWidth() {
		Float[] sizeList = {(float) 1.0, (float) 1.5, (float) 2.0, (float) 2.5, (float) 3.0, (float) 3.5, (float) 4.0, (float) 4.5, (float) 5.0, (float) 6.0, (float) 7.0, (float) 8.0, (float) 9.0, (float) 10.0, (float) 12.5, (float) 15.0, (float) 17.5, (float) 20.0};
		DefaultComboBoxModel cbmSizes = new DefaultComboBoxModel(sizeList); 
		JComboBox combo = new JComboBox(cbmSizes);
		return combo;
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
	 * This method initializes jComboBoxNodeSize	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxNodeSize() {
		Integer[] sizeList = {0,5,6,7,8,9,10,11,12,13,14,15,20,25,30,35,40,45,50};
		DefaultComboBoxModel cbmSizes = new DefaultComboBoxModel(sizeList); 

		JComboBox jComboBoxNodeSize = new JComboBox(cbmSizes);
		jComboBoxNodeSize.setPreferredSize(new Dimension(50, 26));
		return jComboBoxNodeSize;
	}
	
	/**
	 * This method initializes jButtonRemoveComponentRow	
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
	 * This method initializes jButtonNodeClassSelector	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSelectNodeClass() {
		if (jButtonSelectNodeClass == null) {
			jButtonSelectNodeClass = new JButton();
			jButtonSelectNodeClass.setText("...");
			jButtonSelectNodeClass.setPreferredSize(new Dimension(43, 26));
			jButtonSelectNodeClass.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					getNodeClassSelector().setVisible(true);
					if(! getNodeClassSelector().isCanceled()){
						//getJTextFieldNodeClass().setText(getNodeClassSelector().getClassSelected());
					}
				}
			});
		}
		return jButtonSelectNodeClass;
	}
	
	/** 
	 * Returns an ImageIcon, or a default MissingIcon(a red X) if image not found.
	 * @param path
	 * @param description
	 * @return ImageIcon 
	 */
	protected ImageIcon createImageIcon(String path, String description) {
		
		if(path!=null ){			
		    java.net.URL imgURL = getClass().getResource(path);
		    if (imgURL != null) {
		        return new ImageIcon(imgURL, description);
		    } else {
		        return (new MissingIcon(description));
		    }
		
		} else{
		    return (new MissingIcon(description));		    
		}
			
	}

	/**
	 * This method initializes jPanelComponents	
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
			gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
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
	 * This method initializes jCheckBoxSnap2Grid	
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
	 * This method initializes jPanelButtonOkCancel	
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
	 * This method initializes jComboBoxGridWidth	
	 * @return javax.swing.JComboBox	
	 */
	private JSpinner getJSpinnerGridWidth() {
		if (jSpnnerGridWidth == null) {
			jSpnnerGridWidth = new JSpinner(new SpinnerNumberModel(5.0, 5.0, 100.0, 0.1));
			jSpnnerGridWidth.setPreferredSize(new Dimension(80, 26));
		}
		return jSpnnerGridWidth;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {

		if(event.getSource().equals(getJButtonAddComponentRow())){
			// --- Add a new row to the component types table -------
			this.addComponentRow();
		} else if(event.getSource().equals(getJButtonRemoveComponentRow())) {
			// --- Remove a row from the component types table ------
			if(getJTable4ComponentTypes().getSelectedRow() > -1){
				this.removeComponentRow(getJTable4ComponentTypes().getSelectedRow());
			}
		
		} else if(event.getSource().equals(getJButtonAddDomain())) {
			// --- Add a new row to the domain table ----------------
			this.addDomainRow();
		} else if(event.getSource().equals(getJButtonRemoveDomainntRow())) {
			// --- Remove a row from the component types table ------
			if(getJTable4DomainTypes().getSelectedRow() > -1){
				this.removeDomainRow(getJTable4DomainTypes().getSelectedRow());
			}
		
		} else if(event.getSource().equals(getJButtonConfirm())) {
			
			HashMap<String, ComponentTypeSettings> ctsHash = new HashMap<String, ComponentTypeSettings>();
			HashMap<String, DomainSettings> dsHash = new HashMap<String, DomainSettings>();

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
					String graphProto 	 = (String)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_GraphPrototyp));
					ImageIcon imageIcon  = (ImageIcon)dtmComponents.getValueAt(row,this.getColumnHeaderIndexComponents(COL_Image));
					String imageIconDesc = imageIcon.getDescription();
					float edgeWidth 	 = (Float)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_EdgeWidth));
					Color color 		 = (Color)dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_EdgeColor));
					String colorString 	 = String.valueOf(color.getRGB());
					boolean showLable 	 = (Boolean) dtmComponents.getValueAt(row, this.getColumnHeaderIndexComponents(COL_ShowLabel));
					
					ComponentTypeSettings cts = new ComponentTypeSettings(agentClass, graphProto, imageIconDesc, colorString );
					cts.setEdgeWidth(edgeWidth);
					cts.setShowLabel(showLable);

					ctsHash.put(name, cts);
				}
			}
			this.currCompTypSettings = ctsHash;
			
			// --- Get the domain settings from table ---------------
			JTable jtDomains = this.getJTable4DomainTypes();
			DefaultTableModel dtmDomains = this.getTableModel4Domains();
			// --- Confirm, apply changes in table ------------------						
			TableCellEditor tceDomains = jtDomains.getCellEditor();
			if (tceDomains!=null) {
				tceDomains.stopCellEditing();
			}
			for(int row=0; row<dtmDomains.getRowCount(); row++){
				
				String name = (String) dtmDomains.getValueAt(row, this.getColumnHeaderIndexComponents(COL_D_DomainName));
				if(name!=null && name.length()!=0){
					
					String ontoClass 	 = (String) dtmDomains.getValueAt(row, this.getColumnHeaderIndexComponents(COL_D_OntologyClass));
					boolean showLabel 	 = (Boolean)dtmDomains.getValueAt(row, this.getColumnHeaderIndexComponents(COL_D_ShowLable));
					Integer vertexSize 	 = (Integer)dtmDomains.getValueAt(row, this.getColumnHeaderIndexComponents(COL_D_VertexSize));
					Color color 		 = (Color)  dtmDomains.getValueAt(row, this.getColumnHeaderIndexComponents(COL_D_VertexColor));
					String colorStr 	 = String.valueOf(color.getRGB());
					Color colorPicked	 = (Color)  dtmDomains.getValueAt(row, this.getColumnHeaderIndexComponents(COL_D_VertexColorPicked));					
					String colorPickStr	 = String.valueOf(colorPicked.getRGB());
					
					DomainSettings ds = new DomainSettings();
					ds.setOntologyClass(ontoClass);
					ds.setShowLabel(showLabel);
					ds.setVertexSize(vertexSize);
					ds.setVertexColor(colorStr);
					ds.setVertexColorPicked(colorPickStr);

					dsHash.put(name, ds);
				}
			}
			this.currDomainSettings = dsHash;
			
			
			this.currSnap2Grid = this.jCheckBoxSnap2Grid.isSelected();
			this.currSnapRaster = (Double)jSpnnerGridWidth.getValue(); 
			
			this.canceled = false;
			this.setVisible(false);
			
		} else if(event.getSource().equals(getJButtonCancel())) {
			// --- Canceled, discard changes --------------
			this.canceled = true;
			this.setVisible(false);
			
		}
	
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
