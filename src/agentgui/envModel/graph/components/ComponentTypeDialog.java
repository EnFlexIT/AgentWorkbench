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
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
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
import agentgui.envModel.graph.controller.BasicGraphGui;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;

/**
 * GUI dialog for configuring network component types
 *  
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public class ComponentTypeDialog extends JDialog implements ActionListener{
	
	/** Generated serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private Vector<String> columnHeader 	= null;  //  @jve:decl-index=0:
	public final String COL_TypeSpecifier 	= Language.translate("Type-Specifier", Language.EN);  //  @jve:decl-index=0:
	public final String COL_AgentClass 		= Language.translate("Agent class", Language.EN);
	public final String COL_GraphPrototyp 	= Language.translate("Graph-prototype", Language.EN);  //  @jve:decl-index=0:
	public final String COL_ShowLabel 		= Language.translate("Show label", Language.EN);  //  @jve:decl-index=0:
	public final String COL_Image 			= Language.translate("Image", Language.EN);  //  @jve:decl-index=0:
	public final String COL_EdgeWidth 		= Language.translate("Edge Width", Language.EN);  //  @jve:decl-index=0:
	public final String COL_EdgeColor 		= Language.translate("Edge Color", Language.EN);  //  @jve:decl-index=0:
	
	private final String pathImage = GraphGlobals.getPathImages();

	private Project currProject = null;
	private HashMap<String, ComponentTypeSettings> currCompTypSettings = null;
	private boolean canceled = false;
	
	private JPanel jContentPane = null;
	private JPanel jPanelTop = null;
	private JPanel jPanelButtonOkCancel = null;
	private JPanel jPanelComponents = null;
	private JScrollPane jScrollPaneClassTable = null;
	
	private JLabel jLabelNodeClass = null;
	private JLabel jLabelOntoClass = null;
	private JLabel jLabelComponentHeader = null;
	private JLabel jLabelGridHeader = null;
	private JLabel jLabelGuideGridWidth = null;
	private JLabel jLabelVertexSize = null;
	private JLabel jLabelNodeColor = null;
	private JLabel jLabelSeperatorHorizontal = null;
	private JLabel jLabelSeperatorVertical = null;
	private JLabel jLabelSeperatorVertical1 = null;
	
	private JTextField jTextFieldNodeClass = null;
	private JCheckBox jCheckBoxSnap2Grid = null;
	private JCheckBox jCheckBoxLableVisible = null;
	private JComboBox jComboBoxNodeSize = null;
	private JSpinner jSpnnerGridWidth = null;

	private JButton jButtonConfirm = null;
	private JButton jButtonCancel = null;
	private JButton jButtonAddRow = null;
	private JButton jButtonRemoveRow = null;
	private JButton jButtonSelectNodeClass = null;
	private JButton jButtonNodeColor = null;
	
	private JTable jTableComponentTypes = null;
	private DefaultTableModel componentTypesModel = null;
	
	private ClassSelector nodeClassSelector = null;
	
	/** Cell editor for the prototype classes column */
	private TableCellEditor4ClassSelector prototypeClassesCellEditor = null;  //  @jve:decl-index=0:
	
	
	/**
	 * This is the default constructor.
	 *
	 * @param currentCTS the current HashMap with the ComponentTypeSettings
	 * @param project the current project
	 */
	public ComponentTypeDialog(HashMap<String, ComponentTypeSettings> currentCTS, Project project) {
		this.currCompTypSettings = currentCTS;
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
	    this.setNodeConfiguration();
	    
	    // --- In case that we're in an executed MAS ------
	    if (this.currProject==null) {
	    	this.getJTable4ComponentTypes().setEnabled(false);
	    	this.getJButtonAddRow().setEnabled(false);
	    	this.getJButtonRemoveRow().setEnabled(false);
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
	private void setNodeConfiguration() {
	
		ComponentTypeSettings ctsNode = this.currCompTypSettings.get("node"); 
		
		// --- Set the ontology class for the vertices ------------------------
		if(ctsNode!= null){
			jTextFieldNodeClass.setText(ctsNode.getAgentClass());
		}
		
		// --- Set the show label property ------------------------------------
		if(ctsNode!= null){
			this.jCheckBoxLableVisible.setSelected(ctsNode.isShowLabel());
		}  else {
			this.jCheckBoxLableVisible.setSelected(true);
		}
		
		// --- Set the vertex size from the component type settings -----------
		Integer size;
		if(ctsNode!=null) {
			Integer vertexSize= ctsNode.getVertexSize();				
			if(vertexSize!=null){
				size = vertexSize;
			} else {
				size = BasicGraphGui.DEFAULT_VERTEX_SIZE;
			}
		} else {
			size = BasicGraphGui.DEFAULT_VERTEX_SIZE;
		}
		jComboBoxNodeSize.setSelectedItem(size);
		
		// --- Get the color from the component type settings -----------------
		Color color;
		if(ctsNode!=null) {
			String colorString= ctsNode.getColor();				
			if(colorString!=null){
				color = new Color(Integer.parseInt(colorString));
			} else {
				color = BasicGraphGui.DEFAULT_VERTEX_COLOR;
			}
		}
		else {
			color = BasicGraphGui.DEFAULT_VERTEX_COLOR;
		}
		this.setNodeColor(color);
		
		// --- Get the Guide grid configuration -------------------------------
		if (ctsNode!=null) {
			
			this.getJCheckBoxSnap2Grid().setSelected(ctsNode.isSnap2Grid());
			
			double snapRaster = ctsNode.getSnapRaster();
			if (snapRaster==0) {
				snapRaster = BasicGraphGui.DEFAULT_RASTER_SIZE;
			}
			this.getJSpinnerGridWidth().setValue(new Double(snapRaster));
			
		}
		
	}
	
	/**
	 * Returns all component type settings.
	 * @return the component type settings
	 */
	public HashMap<String, ComponentTypeSettings> getComponentTypeSettings() {
		return this.currCompTypSettings;
	}
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.BOTH;
			gridBagConstraints51.gridwidth = 1;
			gridBagConstraints51.gridx = 0;
			gridBagConstraints51.gridy = 2;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.weighty = 1.0;
			gridBagConstraints51.insets = new Insets(5, 10, 15, 10);
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.insets = new Insets(5, 10, 0, 10);
			gridBagConstraints41.anchor = GridBagConstraints.WEST;
			gridBagConstraints41.gridy = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridwidth = 1;
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.insets = new Insets(15, 10, 0, 10);
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridy = 0;
			jLabelNodeColor = new JLabel();
			jLabelNodeColor.setText("Vertex Color");
			jLabelNodeColor.setText(Language.translate(jLabelNodeColor.getText(),Language.EN));
			jLabelNodeClass = new JLabel();
			jLabelNodeClass.setText("Verbindungsknoten");
			jLabelNodeClass.setText(Language.translate(jLabelNodeClass.getText()));
			jLabelNodeClass.setFont(new Font("Dialog", Font.BOLD, 14));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJPanelTop(), gridBagConstraints13);
			jContentPane.add(getJPanelComponents(), gridBagConstraints41);
			jContentPane.add(getJScrollPaneClassTable(), gridBagConstraints51);
		}
		return jContentPane;
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
	 * This method initializes jScrollPaneClassTable	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneClassTable() {
		if (jScrollPaneClassTable == null) {
			jScrollPaneClassTable = new JScrollPane();
			jScrollPaneClassTable.setViewportView(getJTable4ComponentTypes());
		}
		return jScrollPaneClassTable;
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
			TableColumn agentClassColumn = tcm.getColumn(getColumnHeaderIndex(COL_AgentClass));
			agentClassColumn.setCellEditor(new TableCellEditor4AgentClass());
			agentClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			//Set up renderer and editor for Graph prototype column
			TableColumn prototypeClassColumn = tcm.getColumn(getColumnHeaderIndex(COL_GraphPrototyp));
			prototypeClassColumn.setCellEditor(getPrototypeClassesCellEditor());
			prototypeClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			//Set up renderer and editor for show label
			TableColumn showLabelClassColumn = tcm.getColumn(getColumnHeaderIndex(COL_ShowLabel));
			showLabelClassColumn.setCellEditor(new TableCellEditor4CheckBox(this.getCheckBoxEdgeWidth()));
			showLabelClassColumn.setCellRenderer(new TableCellRenderer4CheckBox());
			showLabelClassColumn.setPreferredWidth(15);
			
			//Set up Editor for the ImageIcon column
			TableColumn imageIconColumn = tcm.getColumn(getColumnHeaderIndex(COL_Image));
			imageIconColumn.setCellEditor(new TableCellEditor4Image(currProject));		
			imageIconColumn.setPreferredWidth(15);
			
			//Set up renderer and editor for the edge width.	        
			TableColumn edgeWidthColumn = tcm.getColumn(getColumnHeaderIndex(COL_EdgeWidth));
			edgeWidthColumn.setCellEditor(new TableCellEditor4Combo(this.getCombo4EdgeWidth()));
			edgeWidthColumn.setPreferredWidth(15);
			
			//Set up renderer and editor for the  Color column.	        
			TableColumn colorColumn = tcm.getColumn(getColumnHeaderIndex(COL_EdgeColor));
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
	private Vector<String> getColumnHeader() {
		if (columnHeader==null) {
			columnHeader = new Vector<String>();
			columnHeader.add(COL_TypeSpecifier);
			columnHeader.add(COL_AgentClass);
			columnHeader.add(COL_GraphPrototyp);
			columnHeader.add(COL_ShowLabel);
			columnHeader.add(COL_Image);			
			columnHeader.add(COL_EdgeWidth);
			columnHeader.add(COL_EdgeColor);
		}
		return columnHeader;
	}
	/**
	 * Gets the header index.
	 *
	 * @param header the header
	 * @return the header index
	 */
	private int getColumnHeaderIndex(String header) {
		
		Vector<String> headers = this.getColumnHeader();
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
							edgeWidth = BasicGraphGui.DEFAULT_EDGE_WIDTH;
						}
						Color edgeColor = new Color(Integer.parseInt(cts.getColor()));
						
						// --- Create row vector --------------
						Vector<Object> newRow = new Vector<Object>();
						for (int i = 0; i < this.getColumnHeader().size(); i++) {
							if (i == getColumnHeaderIndex(COL_TypeSpecifier)) {
								newRow.add(etName);
							} else if (i == getColumnHeaderIndex(COL_AgentClass)) {
								newRow.add(cts.getAgentClass());
							} else if (i == getColumnHeaderIndex(COL_GraphPrototyp)) {
								newRow.add(cts.getGraphPrototype());
							} else if (i == getColumnHeaderIndex(COL_ShowLabel)) {
								newRow.add(cts.isShowLabel());
							} else if (i == getColumnHeaderIndex(COL_Image)) {
								newRow.add(createImageIcon(imagePath, imagePath));
							} else if (i == getColumnHeaderIndex(COL_EdgeWidth)) {
								newRow.add(edgeWidth);
							} else if (i == getColumnHeaderIndex(COL_EdgeColor)) {
								newRow.add(edgeColor);
							}
						}
						dataRows.add(newRow);
					}
				}
			}
			// --------------------------------------------
			// --- define the TableModel --- Start --------
			componentTypesModel = new DefaultTableModel(dataRows, this.getColumnHeader()){
				private static final long serialVersionUID = 3550155601170744633L;
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int columnIndex) {
			            if(columnIndex==getColumnHeaderIndex(COL_Image))
			            	return ImageIcon.class;
			            else if(columnIndex==getColumnHeaderIndex(COL_EdgeColor))
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
	private void addRow(){
		// --- Create row vector --------------
		Vector<Object> newRow = new Vector<Object>();
		for (int i = 0; i < this.getColumnHeader().size(); i++) {
			if (i == getColumnHeaderIndex(COL_TypeSpecifier)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndex(COL_AgentClass)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndex(COL_GraphPrototyp)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndex(COL_ShowLabel)) {
				newRow.add(true);
			} else if (i == getColumnHeaderIndex(COL_Image)) {
				newRow.add(createImageIcon(null,"MissingIcon"));
			} else if (i == getColumnHeaderIndex(COL_EdgeWidth)) {
				newRow.add(BasicGraphGui.DEFAULT_EDGE_WIDTH);
			} else if (i == getColumnHeaderIndex(COL_EdgeColor)) {
				newRow.add(BasicGraphGui.DEFAULT_EDGE_COLOR);
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
	private void removeRow(int rowNum){
		((DefaultTableModel)getJTable4ComponentTypes().getModel()).removeRow(rowNum);
	}

	/**
	 * This method initializes jButtonAddRow	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAddRow() {
		if (jButtonAddRow == null) {
			jButtonAddRow = new JButton();
			jButtonAddRow.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListPlus.png")));
			jButtonAddRow.addActionListener(this);
		}
		return jButtonAddRow;
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
	private JComboBox getCombo4EdgeWidth() {
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
	 * This method initializes jButtonRemoveRow	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemoveRow() {
		if (jButtonRemoveRow == null) {
			jButtonRemoveRow = new JButton();
			jButtonRemoveRow.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
			jButtonRemoveRow.addActionListener(this);
		}
		return jButtonRemoveRow;
	}

	/**
	 * This method initializes jTextField	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNodeClass() {
		if (jTextFieldNodeClass == null) {
			jTextFieldNodeClass = new JTextField();
			jTextFieldNodeClass.setPreferredSize(new Dimension(250, 26));
			jTextFieldNodeClass.setEditable(false);
		}
		return jTextFieldNodeClass;
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
						getJTextFieldNodeClass().setText(getNodeClassSelector().getClassSelected());
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
	 * This method initializes jButtonNodeColor	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonNodeColor() {
		if (jButtonNodeColor == null) {
			jButtonNodeColor = new JButton();
			jButtonNodeColor.setPreferredSize(new Dimension(43, 26));
			jButtonNodeColor.setText(".");
			jButtonNodeColor.addActionListener(this);
		}
		return jButtonNodeColor;
	}
	
	/**
	 * Sets the background color of the node color button
	 * @param color
	 */
	private void setNodeColor(Color color){
		getJButtonNodeColor().setBackground(color);
		//Set the Tool tip 
		if(color!=null){
			getJButtonNodeColor().setToolTipText("RGB value: " + color.getRed() + ", "
						                                     + color.getGreen() + ", "
						                                     + color.getBlue());
        }
	}

	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = GridBagConstraints.NONE;
			gridBagConstraints23.gridy = 2;
			gridBagConstraints23.weightx = 0.0;
			gridBagConstraints23.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints23.anchor = GridBagConstraints.WEST;
			gridBagConstraints23.gridx = 9;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 8;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints22.gridy = 2;
			jLabelGuideGridWidth = new JLabel();
			jLabelGuideGridWidth.setText("Raster-Breite");
			jLabelGuideGridWidth.setText(Language.translate(jLabelGuideGridWidth.getText()));
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 11;
			gridBagConstraints21.gridheight = 3;
			gridBagConstraints21.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints21.insets = new Insets(0, 10, 0, 30);
			gridBagConstraints21.gridy = 0;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 10;
			gridBagConstraints20.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints20.gridheight = 3;
			gridBagConstraints20.insets = new Insets(0, 20, 0, 20);
			gridBagConstraints20.gridy = 0;
			jLabelSeperatorVertical1 = new JLabel();
			jLabelSeperatorVertical1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jLabelSeperatorVertical1.setText("");
			jLabelSeperatorVertical1.setFont(new Font("Dialog", Font.BOLD, 8));
			jLabelSeperatorVertical1.setPreferredSize(new Dimension(2, 20));
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.gridx = 7;
			gridBagConstraints19.gridheight = 3;
			gridBagConstraints19.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints19.insets = new Insets(0, 20, 0, 20);
			gridBagConstraints19.gridy = 0;
			jLabelSeperatorVertical = new JLabel();
			jLabelSeperatorVertical.setText("");
			jLabelSeperatorVertical.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jLabelSeperatorVertical.setFont(new Font("Dialog", Font.BOLD, 8));
			jLabelSeperatorVertical.setPreferredSize(new Dimension(2, 20 ));
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 8;
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 8;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.gridy = 0;
			jLabelGridHeader = new JLabel();
			jLabelGridHeader.setText("Hilfs-Raster");
			jLabelGridHeader.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabelGridHeader.setText(Language.translate(jLabelGridHeader.getText()));
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridwidth = 2;
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.insets = new Insets(0, 5, 5, 5);
			jLabelComponentHeader = new JLabel();
			jLabelComponentHeader.setText("Netzwerk-Komponenten");
			jLabelComponentHeader.setText(Language.translate(jLabelComponentHeader.getText()));
			jLabelComponentHeader.setFont(new Font("Dialog", Font.BOLD, 14));
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 1;
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints16.gridy = 1;
			jLabelOntoClass = new JLabel();
			jLabelOntoClass.setText("Ontologie-Klasse");
			jLabelOntoClass.setText(Language.translate(jLabelOntoClass.getText()));
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 1;
			gridBagConstraints15.gridwidth = 11;
			gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.insets = new Insets(10, 5, 0, 5);
			gridBagConstraints15.weighty = 0.0;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.gridy = 3;
			jLabelSeperatorHorizontal = new JLabel();
			jLabelSeperatorHorizontal.setText("");
			jLabelSeperatorHorizontal.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			jLabelSeperatorHorizontal.setFont(new Font("Dialog", Font.BOLD, 8));
			jLabelSeperatorHorizontal.setPreferredSize(new Dimension(200, 2));
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 5;
			gridBagConstraints14.gridwidth = 1;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.weightx = 0.0;
			gridBagConstraints14.fill = GridBagConstraints.NONE;
			gridBagConstraints14.insets = new Insets(5, 30, 0, 0);
			gridBagConstraints14.gridy = 2;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 2;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			jLabelVertexSize = new JLabel();
			jLabelVertexSize.setText("Vertex Size");
			jLabelVertexSize.setText(Language.translate(jLabelVertexSize.getText(),Language.EN));
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.NONE;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.weightx = 0.0;
			gridBagConstraints7.anchor = GridBagConstraints.CENTER;
			gridBagConstraints7.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints7.gridx = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.anchor = GridBagConstraints.CENTER;
			gridBagConstraints2.gridx = 4;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 3;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.insets = new Insets(5, 30, 0, 0);
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridx = 6;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			gridBagConstraints4.insets = new Insets(5, 5, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 0.0;
			gridBagConstraints3.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints3.gridwidth = 4;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.gridx = 2;
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.add(getJTextFieldNodeClass(), gridBagConstraints3);
			jPanelTop.add(getJButtonSelectNodeClass(), gridBagConstraints4);
			jPanelTop.add(jLabelNodeColor, gridBagConstraints5);
			jPanelTop.add(getJButtonNodeColor(), gridBagConstraints2);
			jPanelTop.add(jLabelVertexSize, gridBagConstraints12);
			jPanelTop.add(getJComboBoxNodeSize(), gridBagConstraints7);
			jPanelTop.add(getJCheckBoxLableVisible(), gridBagConstraints14);
			jPanelTop.add(jLabelSeperatorHorizontal, gridBagConstraints15);
			jPanelTop.add(jLabelOntoClass, gridBagConstraints16);
			jPanelTop.add(jLabelNodeClass, gridBagConstraints10);
			jPanelTop.add(jLabelGridHeader, gridBagConstraints11);
			jPanelTop.add(getJCheckBoxSnap2Grid(), gridBagConstraints18);
			jPanelTop.add(jLabelSeperatorVertical, gridBagConstraints19);
			jPanelTop.add(jLabelSeperatorVertical1, gridBagConstraints20);
			jPanelTop.add(getJPanelButtonOkCancel(), gridBagConstraints21);
			jPanelTop.add(jLabelGuideGridWidth, gridBagConstraints22);
			jPanelTop.add(getJSpinnerGridWidth(), gridBagConstraints23);
		}
		return jPanelTop;
	}

	/**
	 * This method initializes jComboBoxNodeSize	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxNodeSize() {
		if (jComboBoxNodeSize == null) {
			Integer[] sizeList = {0,5,6,7,8,9,10,11,12,13,14,15,20,25,30,35,40,45,50};
			DefaultComboBoxModel cbmSizes = new DefaultComboBoxModel(sizeList); 

			jComboBoxNodeSize = new JComboBox(cbmSizes);
			jComboBoxNodeSize.setPreferredSize(new Dimension(50, 26));
			
		}
		return jComboBoxNodeSize;
	}

	/**
	 * This method initializes jCheckBoxLableVisible	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxLableVisible() {
		if (jCheckBoxLableVisible == null) {
			jCheckBoxLableVisible = new JCheckBox();
			jCheckBoxLableVisible.setText("Beschriftungen anzeigen");
			jCheckBoxLableVisible.setText(Language.translate(jCheckBoxLableVisible.getText()));
		}
		return jCheckBoxLableVisible;
	}

	/**
	 * This method initializes jPanelComponents	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelComponents() {
		if (jPanelComponents == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 10, 0, 5);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.gridx = -1;
			gridBagConstraints17.gridy = -1;
			gridBagConstraints17.insets = new Insets(0, 5, 0, 0);
			jPanelComponents = new JPanel();
			jPanelComponents.setLayout(new GridBagLayout());
			jPanelComponents.add(jLabelComponentHeader, gridBagConstraints17);
			jPanelComponents.add(getJButtonAddRow(), gridBagConstraints1);
			jPanelComponents.add(getJButtonRemoveRow(), gridBagConstraints6);
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
			gridBagConstraints8.gridx = -1;
			gridBagConstraints8.gridy = -1;
			gridBagConstraints8.weightx = 0.0;
			gridBagConstraints8.insets = new Insets(0, 0, 7, 0);
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(7, 0, 0, 0);
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.anchor = GridBagConstraints.CENTER;
			gridBagConstraints9.gridx = 0;
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

		if(event.getSource().equals(getJButtonAddRow())){
			// --- Add a new row to the component types table -------
			addRow();
		} else if(event.getSource().equals(getJButtonRemoveRow())) {
			// --- Remove a row from the component types table ------
			if(getJTable4ComponentTypes().getSelectedRow() > -1){
				removeRow(getJTable4ComponentTypes().getSelectedRow());
			}
		} else if(event.getSource().equals(getJButtonConfirm())) {
			
			JTable jtc = this.getJTable4ComponentTypes();
			DefaultTableModel dtm = this.getTableModel4ComponentTypes();
			
			HashMap<String, ComponentTypeSettings> ctsHash = new HashMap<String, ComponentTypeSettings>();
			
			// --- Confirm, apply changes in table ------------------			
			TableCellEditor tce = jtc.getCellEditor();
			if (tce!=null) {
				tce.stopCellEditing();
			}
			
			// --- Get the component type definitions from table ----
			for(int row=0; row<dtm.getRowCount(); row++){
				
				String name = (String) dtm.getValueAt(row, this.getColumnHeaderIndex(COL_TypeSpecifier));
				if(name!=null && name.length()!=0){
					
					String agentClass 	 = (String)dtm.getValueAt(row, this.getColumnHeaderIndex(COL_AgentClass));
					String graphProto 	 = (String)dtm.getValueAt(row, this.getColumnHeaderIndex(COL_GraphPrototyp));
					ImageIcon imageIcon  = (ImageIcon)dtm.getValueAt(row,this.getColumnHeaderIndex(COL_Image));
					String imageIconDesc = imageIcon.getDescription();
					float edgeWidth 	 = (Float)dtm.getValueAt(row, this.getColumnHeaderIndex(COL_EdgeWidth));
					Color color 		 = (Color)dtm.getValueAt(row, this.getColumnHeaderIndex(COL_EdgeColor));
					String colorString 	 = String.valueOf(color.getRGB());
					boolean showLable 	 = (Boolean) dtm.getValueAt(row, this.getColumnHeaderIndex(COL_ShowLabel));
					
					ComponentTypeSettings cts = new ComponentTypeSettings(agentClass, graphProto, imageIconDesc, colorString );
					cts.setEdgeWidth(edgeWidth);
					cts.setShowLabel(showLable);

					ctsHash.put(name, cts);
				}
			}
			
			// --- Add the graph node definition ----------
			String nodeColor = String.valueOf(getJButtonNodeColor().getBackground().getRGB());
			
			ComponentTypeSettings cts = new ComponentTypeSettings(getJTextFieldNodeClass().getText(), null, null, nodeColor);
			cts.setVertexSize((Integer) this.jComboBoxNodeSize.getSelectedItem());
			cts.setShowLabel(this.jCheckBoxLableVisible.isSelected());
			cts.setSnap2Grid(this.jCheckBoxSnap2Grid.isSelected());
			cts.setSnapRaster((Double)jSpnnerGridWidth.getValue());
			ctsHash.put("node", cts);
			
			
			this.currCompTypSettings = ctsHash;
			this.canceled = false;
			this.setVisible(false);
			
		} else if(event.getSource().equals(getJButtonCancel())) {
			// --- Canceled, discard changes --------------
			this.canceled = true;
			this.setVisible(false);
			
		} else if(event.getSource().equals(getJButtonNodeColor())) {
			// --- Change Vertex color button clicked -----
			Color newColor = JColorChooser.showDialog(getJButtonNodeColor(), "Pick a color", getJButtonNodeColor().getBackground());
			if(newColor!=null){
				setNodeColor(newColor);
			}				
		}
	
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
