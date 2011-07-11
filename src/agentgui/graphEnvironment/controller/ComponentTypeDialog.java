/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.graphEnvironment.controller;

import jade.content.Concept;
import jade.core.Agent;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.gui.ClassSelector;
import agentgui.core.gui.ClassSelectorTableCellEditor;
import agentgui.core.gui.components.AgentClassTableCellEditor;
import agentgui.core.gui.components.ClassNameListCellRenderer;
import agentgui.core.gui.components.ClassNameTableCellRenderer;
import agentgui.core.gui.components.ColorEditor;
import agentgui.core.gui.components.ColorRenderer;
import agentgui.core.gui.components.ImageSelectorTableCellEditor;
import agentgui.core.gui.imaging.MissingIcon;
import agentgui.graphEnvironment.networkModel.ComponentTypeSettings;
import agentgui.graphEnvironment.prototypes.GraphElementPrototype;

/**
 * GUI dialog for configuring network component types 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author <br>Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 * 
 */
public class ComponentTypeDialog extends JDialog implements ActionListener{
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The content pane
	 */
	private JPanel jContentPane = null;
	/**
	 * The confirm button
	 */
	private JButton jButtonConfirm = null;
	/**
	 * The cancel button
	 */
	private JButton jButtonCancel = null;
	/**
	 * The JScrollPane containing the component type table
	 */
	private JScrollPane jScrollPaneClassTable = null;
	/**
	 * The component type table
	 */
	private JTable jTableComponentTypes = null;
	/**
	 * The add row button
	 */
	private JButton jButtonAddRow = null;
	/**
	 * The remove row button
	 */
	private JButton jButtonRemoveRow = null;
	/**
	 * The ClassSelector instance used for assigning a graph node class
	 */
	private ClassSelector nodeClassSelector = null;
	/**
	 * JComboBox used as cell editor for the agent classes column
	 */
	private JComboBox jComboBoxAgentClasses = null;
	
	/**
	 * Cell editor for the prototype classes column
	 */
	private ClassSelectorTableCellEditor prototypeClassesCellEditor = null;  //  @jve:decl-index=0:
	private ClassSelectorTableCellEditor agentClassesCellEditor = null;
	/**
	 * All available agent classes, accessible by simple class name
	 */
	private HashMap<String, Class<?>> availableAgentClasses = null;
	
	/**
	 * The GraphEnvironmentControllerGUI that started this dialog
	 */
	private GraphEnvironmentControllerGUI parent = null;
	/**
	 * The current AgentGUI project
	 */
	private Project project = null;
	/**
	 * The label for the node class text field
	 */
	private JLabel jLabelNodeClass = null;
	/**
	 * The JTextField specifying the graph node class
	 */
	private JTextField jTextFieldNodeClass = null;
	/**
	 * Button invoking the nodeClassSelector
	 */
	private JButton jButtonSelectNodeClass = null;
	
	/**
	 * Application image folder path
	 */
	private final String pathImage = Application.RunInfo.PathImageIntern();
	/**
	 * Used for assigning the vertex color
	 */
	private JButton jButtonNodeColor = null;
	/**
	 * Color chooser for selecting the vertex color
	 */
	JColorChooser colorChooser = null;
	/**
	 * Used by the colorChooser for selecting the vertex color
	 */
	JDialog colorDialog = null;
	private JLabel jLabelNodeColor = null;
	private JPanel jPanelTop = null;
	private JComboBox jComboBoxNodeSize = null;
	private JLabel jLabelVertexSize = null;
	/**
	 * This is the default constructor
	 * @param parent The parent GUI
	 */
	public ComponentTypeDialog(GraphEnvironmentControllerGUI parent) {
		super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
		this.parent = parent;
		project = parent.getController().getProject();
		initialize();
		//System.out.println(project.getProjectFolderFullPath());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(600, 400);
		this.setContentPane(getJContentPane());
		this.setTitle(Language.translate("Komponententyp-Definition"));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridwidth = 7;
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.insets = new Insets(15, 10, 10, 10);
			gridBagConstraints13.gridy = 0;
			jLabelNodeColor = new JLabel();
			jLabelNodeColor.setText("Vertex Color");
			jLabelNodeColor.setText(Language.translate(jLabelNodeColor.getText(),Language.EN));
			jLabelNodeColor.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelNodeClass = new JLabel();
			jLabelNodeClass.setText("Verbindungspunkte");
			jLabelNodeClass.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelNodeClass.setText(Language.translate(jLabelNodeClass.getText()));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new Insets(10, 5, 10, 5);
			gridBagConstraints11.gridy = 3;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(10, 10, 10, 5);
			gridBagConstraints1.gridy = 3;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.BOTH;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.weighty = 1.0;
			gridBagConstraints10.gridwidth = 7;
			gridBagConstraints10.insets = new Insets(0, 10, 0, 10);
			gridBagConstraints10.gridx = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 6;
			gridBagConstraints9.insets = new Insets(10, 5, 10, 10);
			gridBagConstraints9.gridy = 3;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 5;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.anchor = GridBagConstraints.EAST;
			gridBagConstraints8.insets = new Insets(10, 5, 10, 5);
			gridBagConstraints8.gridy = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJButtonConfirm(), gridBagConstraints8);
			jContentPane.add(getJButtonCancel(), gridBagConstraints9);
			jContentPane.add(getJScrollPaneClassTable(), gridBagConstraints10);
			jContentPane.add(getJButtonAddRow(), gridBagConstraints1);
			jContentPane.add(getJButtonRemoveRow(), gridBagConstraints11);
			jContentPane.add(getJPanelTop(), gridBagConstraints13);
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
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonConfirm() {
		if (jButtonConfirm == null) {
			jButtonConfirm = new JButton();
			jButtonConfirm.setText(Language.translate("Übernehmen"));
			jButtonConfirm.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonConfirm.setForeground(new Color(0, 153, 0));
			jButtonConfirm.addActionListener(this);
		}
		return jButtonConfirm;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText(Language.translate("Abbrechen"));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jScrollPaneClassTable	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneClassTable() {
		if (jScrollPaneClassTable == null) {
			jScrollPaneClassTable = new JScrollPane();
			jScrollPaneClassTable.setViewportView(getJTableClasses());
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
	private JTable getJTableClasses() {
		if (jTableComponentTypes == null) {
			jTableComponentTypes = new JTable();
			jTableComponentTypes.setFillsViewportHeight(true);
			jTableComponentTypes.setShowGrid(true);
			jTableComponentTypes.setRowHeight(20);
			jTableComponentTypes.setModel(getClassesTableModel());
			jTableComponentTypes.setAutoCreateRowSorter(true);
			
			//Set up renderer and editor for the agent class column
			TableColumn agentClassColumn = jTableComponentTypes.getColumnModel().getColumn(1);
			agentClassColumn.setCellEditor(new AgentClassTableCellEditor());
			agentClassColumn.setCellRenderer(new ClassNameTableCellRenderer());
			
			
			//Set up renderer and editor for Graph prototype column
			TableColumn prototypeClassColumn = jTableComponentTypes.getColumnModel().getColumn(2);
			prototypeClassColumn.setCellEditor(getPrototypeClassesCellEditor());
			prototypeClassColumn.setCellRenderer(new ClassNameTableCellRenderer());
			prototypeClassColumn.setPreferredWidth(100);
			
			//Set up Editor for the ImageIcon column
			TableColumn imageIconColumn = jTableComponentTypes.getColumnModel().getColumn(3);
			imageIconColumn.setCellEditor(new ImageSelectorTableCellEditor(project));		
			imageIconColumn.setPreferredWidth(30);
			
			//Set up renderer and editor for the  Color column.	        
			TableColumn colorColumn = jTableComponentTypes.getColumnModel().getColumn(4);
			colorColumn.setCellEditor(new ColorEditor());
			colorColumn.setCellRenderer(new ColorRenderer(true));			
			colorColumn.setPreferredWidth(30);
		}
		return jTableComponentTypes;
	}
	
	/**
	 * This method initiates the jTableClasses' TableModel
	 * @return The TableModel
	 */
	private TableModel getClassesTableModel(){
		
		// The ComboBoxModels must be initiated before adding rows 
		getJComboBoxAgentClasses();
		
		// Headlines
		Vector<String> titles = new Vector<String>();
		titles.add(Language.translate("Typ-Bezeichner"));
		titles.add(Language.translate("Agentenklasse"));
		titles.add(Language.translate("Graph-Prototyp"));
		titles.add(Language.translate("Image",Language.EN));
		titles.add(Language.translate("Edge Color",Language.EN));		
		
		Vector<Vector<Object>> dataRows = new Vector<Vector<Object>>();
		
		// Set table entries for defined assignments, if any
		HashMap<String, ComponentTypeSettings> etsHash = parent.getController().getComponentTypeSettings();
		if(etsHash != null){
			Iterator<String> etsIter = etsHash.keySet().iterator();
			while(etsIter.hasNext()){
				String etName = etsIter.next();
				if(!etName.equals("node")){	// The node class is defined in the JTextField, not in the table
					Vector<Object> newRow = new Vector<Object>();
					newRow.add(etName);
					newRow.add(etsHash.get(etName).getAgentClass());
					newRow.add(etsHash.get(etName).getGraphPrototype());

					String imagePath = etsHash.get(etName).getEdgeImage();
					//The description is used to store the path along with the ImageIcon
					newRow.add(createImageIcon(imagePath, imagePath));
					
					newRow.add(new Color(Integer.parseInt(etsHash.get(etName).getColor())));
					
					dataRows.add(newRow);
				}
			}
		}
		
		return new DefaultTableModel(dataRows, titles){
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int c) {
		            if(c==3)
		            	return ImageIcon.class;
		            else if(c==4)
		            	return Color.class;
		            else 
		            	return String.class;
		        }
		};
	}
	
	/**
	 * This method adds a new row to the jTableClasses' TableModel
	 */
	private void addRow(){
		//Creating a new dummy row
		Vector<Object> newRow = new Vector<Object>();
		newRow.add(null); //Type name
		newRow.add(null); //Agent class
		newRow.add(null); //Graph Prototype
		newRow.add(createImageIcon(null,"MissingIcon")); //Edge ImageIcon
		newRow.add(BasicGraphGUI.DEFAULT_EDGE_COLOR); //Edge Color

		((DefaultTableModel)getJTableClasses().getModel()).addRow(newRow);
		getJTableClasses().changeSelection(getJTableClasses().getRowCount()-1, 0, false, false);
		getJTableClasses().editCellAt(getJTableClasses().getRowCount()-1, 0);
	}
	
	/**
	 * This method removes a row from the jTableClasses' TableModel
	 * @param rowNum
	 */
	private void removeRow(int rowNum){
		((DefaultTableModel)getJTableClasses().getModel()).removeRow(rowNum);
	}

	/**
	 * This method initializes jButtonAddRow	
	 * 	
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
	
	/**
	 * This method initializes cellEditorAgentClass
	 * 
	 * @return @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxAgentClasses(){
		if(jComboBoxAgentClasses == null){
			jComboBoxAgentClasses = new JComboBox(getAgentComboBoxModel());
			jComboBoxAgentClasses.setRenderer(new ClassNameListCellRenderer());
		}
		return jComboBoxAgentClasses;
	}
	
	private ClassSelectorTableCellEditor getPrototypeClassesCellEditor(){
		if(prototypeClassesCellEditor == null){
			prototypeClassesCellEditor = new ClassSelectorTableCellEditor(Application.MainWindow, GraphElementPrototype.class, "", "", Language.translate("Graph-Prototypen"));
		}
		return prototypeClassesCellEditor;
	}
	
	/**
	 * This method builds a vector for initiating the cellEditorAgentClass' ComboBox model, and initiates the agentClasses HashMap.	
	 * @return Vector containing the simple names of the projects agent classes.
	 */
	private Vector<String> getAgentComboBoxModel(){
		Vector<String> agentClassNames = new Vector<String>();
		agentClassNames.add(Language.translate("Nicht definiert"));
		
		availableAgentClasses = new HashMap<String, Class<?>>();
		availableAgentClasses.put(Language.translate("Nicht definiert"), null);
		
		// Get all classes extending jade.core.Agent in the current project 
		Iterator<Class<?>> agentClassesIterator= Application.ClassDetector.csAgents.getClassesFound(true).iterator();
		// Build a HashMap containing the simple names as keys and the full class names as values
		while(agentClassesIterator.hasNext()){
			Class<?> agentClass = agentClassesIterator.next();
			availableAgentClasses.put(agentClass.getSimpleName(), agentClass);
			agentClassNames.add(agentClass.getName());
		}
		return agentClassNames;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// Add a new row to the component types table
		if(event.getSource().equals(getJButtonAddRow())){
			addRow();
		// Remove a row from the component types table
		}else if(event.getSource().equals(getJButtonRemoveRow())){
			if(getJTableClasses().getSelectedRow() > -1){
				removeRow(getJTableClasses().getSelectedRow());
			}
		// Confirmed, apply changes
		}else if(event.getSource().equals(getJButtonConfirm())){
			
			JTable jtc = getJTableClasses();
			
			int rowNum = jtc.getRowCount();
			HashMap<String, ComponentTypeSettings> etsVector = new HashMap<String, ComponentTypeSettings>();
			// Get the component type definitions from the table
			for(int row=0; row<rowNum; row++){
				String name = (String) jtc.getValueAt(row, 0);
				//Check for non empty name
				if(name!=null && name.length()!=0){
					ImageIcon imageIcon = (ImageIcon)jtc.getValueAt(row,3);
					Color color = (Color)jtc.getValueAt(row, 4);
					ComponentTypeSettings ets = new ComponentTypeSettings(
							(String)jtc.getValueAt(row, 1), 
							(String)jtc.getValueAt(row, 2),
							imageIcon.getDescription(),
							String.valueOf(color.getRGB())
							);
					// Use name as key
					etsVector.put(name, ets);
				}
			}
			// Add the graph node class definition
			String nodeColor = String.valueOf(getJButtonNodeColor().getBackground().getRGB());
			ComponentTypeSettings ets = new ComponentTypeSettings(getJTextFieldNodeClass().getText(), null, null, nodeColor);
			ets.setVertexSize(String.valueOf(jComboBoxNodeSize.getSelectedItem()));
			etsVector.put("node", ets);
			
			// Set the GraphEnvironmentController's componentTypeSettings
			parent.getController().setComponentTypeSettings(etsVector);
			
			//Refresh the Graph
			parent.getGraphGUI().getVisView().repaint();
			
			this.dispose();
		// Canceled, discard changes
		}else if(event.getSource().equals(getJButtonCancel())){
			this.dispose();
		}else if(event.getSource().equals(getJButtonNodeColor())){
		//Change Vertex color button clicked
			
			//Set up the dialog that the button brings up.
			colorChooser = new JColorChooser();
			Color newColor = JColorChooser.showDialog(
                    getJButtonNodeColor(),
                    "Pick a color",
                    getJButtonNodeColor().getBackground());
			if(newColor!=null){
				setNodeColor(newColor);
			}				
		}
		
		
	}

	/**
	 * This method initializes jButtonRemoveRow	
	 * 	
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
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNodeClass() {
		if (jTextFieldNodeClass == null) {
			jTextFieldNodeClass = new JTextField();
			
			ComponentTypeSettings nodeSettings = parent.getController().getComponentTypeSettings().get("node");
			
			if(nodeSettings != null){
				jTextFieldNodeClass.setText(nodeSettings.getAgentClass());
			}
		}
		return jTextFieldNodeClass;
	}

	/**
	 * This method initializes jButtonNodeClassSelector	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSelectNodeClass() {
		if (jButtonSelectNodeClass == null) {
			jButtonSelectNodeClass = new JButton();
			jButtonSelectNodeClass.setText("...");
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
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
		if(path!=null ){			
		    java.net.URL imgURL = getClass().getResource(path);
		    if (imgURL != null) {
		        return new ImageIcon(imgURL, description);
		    } else {
		       // System.err.println("Couldn't find file: " + path);
		        return (new MissingIcon(description));
		    }
		}
		else{
		    return (new MissingIcon(description));		    
		}
			
	}

	/**
	 * This method initializes jButtonNodeColor	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonNodeColor() {
		if (jButtonNodeColor == null) {
			jButtonNodeColor = new JButton();
			jButtonNodeColor.setPreferredSize(new Dimension(43, 26));
			//jButtonNodeColor.setBorderPainted(false);
			jButtonNodeColor.setText(".");
			jButtonNodeColor.addActionListener(this);
			
			//Get the color from the component type settings
			HashMap<String, ComponentTypeSettings> cts 
							= parent.getController().getComponentTypeSettings();
			Color color;
			if(cts.get("node")!=null)
			{
				String colorString= cts.get("node").getColor();				
				if(colorString!=null){
					color = new Color(Integer.parseInt(colorString));
				}
				else
					color = BasicGraphGUI.DEFAULT_VERTEX_COLOR;			
			}
			else 
				color = BasicGraphGUI.DEFAULT_VERTEX_COLOR;
				
			setNodeColor(color);
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
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints12.anchor = GridBagConstraints.EAST;
			jLabelVertexSize = new JLabel();
			jLabelVertexSize.setText("Vertex Size");
			jLabelVertexSize.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.NONE;
			gridBagConstraints7.gridy = -1;
			gridBagConstraints7.weightx = 0.0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridx = -1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridwidth = 2;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.gridx = 5;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.gridx = 4;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new Insets(0, 30, 0, 0);
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridx = 3;
			gridBagConstraints4.gridy = -1;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints3.gridx = 2;
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.add(jLabelNodeClass, gridBagConstraints6);
			jPanelTop.add(getJTextFieldNodeClass(), gridBagConstraints3);
			jPanelTop.add(getJButtonSelectNodeClass(), gridBagConstraints4);
			jPanelTop.add(jLabelNodeColor, gridBagConstraints5);
			jPanelTop.add(getJButtonNodeColor(), gridBagConstraints2);
			jPanelTop.add(jLabelVertexSize, gridBagConstraints12);
			jPanelTop.add(getJComboBoxNodeSize(), gridBagConstraints7);
		}
		return jPanelTop;
	}

	/**
	 * This method initializes jComboBoxNodeSize	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxNodeSize() {
		if (jComboBoxNodeSize == null) {
			Integer[] sizeList = {1,2,3,4,5};
			jComboBoxNodeSize = new JComboBox(sizeList);
			//Get the vertex size from the component type settings
			HashMap<String, ComponentTypeSettings> cts 
							= parent.getController().getComponentTypeSettings();
			
			Integer size;
			if(cts.get("node")!=null)
			{
				String vertexSize= cts.get("node").getVertexSize();				
				if(vertexSize!=null){
					size = Integer.parseInt(vertexSize);
				}
				else
					size = BasicGraphGUI.DEFAULT_VERTEX_SIZE;			
			}
			else 
				size = BasicGraphGUI.DEFAULT_VERTEX_SIZE;
			
			jComboBoxNodeSize.setSelectedItem(size);
		}
		return jComboBoxNodeSize;
	}

}
