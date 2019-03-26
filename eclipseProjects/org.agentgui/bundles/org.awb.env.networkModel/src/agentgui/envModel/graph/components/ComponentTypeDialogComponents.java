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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import agentgui.core.application.Language;
import agentgui.core.gui.components.JComboBoxWide;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import de.enflexit.common.swing.TableCellEditor4Color;
import de.enflexit.common.swing.TableCellRenderer4Color;

/**
 * The Class ComponentTypeDialogComponents represents the sub part of the 
 * {@link ComponentTypeDialog} that displays the settings for {@link NetworkComponent}s.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ComponentTypeDialogComponents extends JPanel implements ActionListener {

	private static final long serialVersionUID = 4983964694623728927L;

	private final String pathImage = GraphGlobals.getPathImages();
	
	private Vector<String> columnHeaderComponents 	= null;
	private final String COL_TypeSpecifier 			= Language.translate("Type-Specifier", Language.EN);
	private final String COL_Domain 				= Language.translate("Subnetwork", Language.EN);
	private final String COL_AgentClass 			= Language.translate("Agent Class", Language.EN);
	private final String COL_GraphPrototyp 			= Language.translate("Graph-Prototype", Language.EN);
	private final String COL_AdapterClass 			= Language.translate("Adapter class", Language.EN);
	private final String COL_ShowLabel 				= Language.translate("Show Label", Language.EN);
	private final String COL_Image 					= Language.translate("Image", Language.EN);
	private final String COL_EdgeWidth 				= Language.translate("Width", Language.EN);
	private final String COL_EdgeColor 				= Language.translate("Color", Language.EN);

	private ComponentTypeDialog componentTypeDialog;

	private TreeMap<String, ComponentTypeSettings> componentTypeSettings;
	
	private JButton jButtonAddComponentRow;
	private JButton jButtonRemoveComponentRow;
	private JScrollPane jScrollPaneClassTableComponents;
	private JTable jTableComponentTypes;
	private DefaultTableModel componentTypesModel;

	
	/**
	 * Instantiates the sub part for {@link NetworkComponent}s.
	 *
	 * @param componentTypeDialog the parent {@link ComponentTypeDialog}
	 * @param domainSettings the domain settings
	 */
	public ComponentTypeDialogComponents(ComponentTypeDialog componentTypeDialog) {
		this.componentTypeDialog = componentTypeDialog;
		this.initialize();
	}
	/**
	 * Initializes this panel.
	 */
	private void initialize() {
		
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
		
		this.setLayout(new GridBagLayout());
		//this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(this.getJButtonAddComponentRow(), gridBagConstraints1);
		this.add(this.getJButtonRemoveComponentRow(), gridBagConstraints6);
		this.add(this.getJScrollPaneClassTableComponents(), gridBagConstraints24);
	}
	
	/**
	 * This method initializes jButtonRemoveComponentRow	.
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
	 * This method initializes jButtonAddComponentRow	.
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
	 * This method initializes jScrollPaneClassTableComponents	.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneClassTableComponents() {
		if (jScrollPaneClassTableComponents == null) {
			jScrollPaneClassTableComponents = new JScrollPane();
			jScrollPaneClassTableComponents.setViewportView(this.getJTable4ComponentTypes());
		}
		return jScrollPaneClassTableComponents;
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
			agentClassColumn.setCellEditor(this.componentTypeDialog.getAgentClassesCellEditor());
			agentClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			//Set up renderer and editor for Graph prototype column
			TableColumn prototypeClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_GraphPrototyp));
			prototypeClassColumn.setCellEditor(this.componentTypeDialog.getPrototypeClassesCellEditor());
			prototypeClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			//Set up renderer and editor for the adapter class column
			TableColumn adapterClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_AdapterClass));
			adapterClassColumn.setCellEditor(this.componentTypeDialog.getAdapterClassesCellEditor());
			adapterClassColumn.setCellRenderer(new TableCellRenderer4Label());
			
			
			//Set up renderer and editor for show label
			TableColumn showLabelClassColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_ShowLabel));
			showLabelClassColumn.setCellEditor(new TableCellRenderEditor4CheckBox());
			showLabelClassColumn.setCellRenderer(new TableCellRenderEditor4CheckBox());
			showLabelClassColumn.setMinWidth(80);
			showLabelClassColumn.setMaxWidth(80);

			
			//Set up Editor for the ImageIcon column
			TableColumn imageIconColumn = tcm.getColumn(getColumnHeaderIndexComponents(COL_Image));
			imageIconColumn.setCellEditor(new TableCellEditor4Image(this.componentTypeDialog));		
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
			componentTypesModel = new DefaultTableModel(null, this.getColumnHeaderComponents()){
				private static final long serialVersionUID = 3550155601170744633L;
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int columnIndex) {
		            if (columnIndex==getColumnHeaderIndexComponents(COL_Image)) {
		            	return ImageIcon.class;
		            } else if(columnIndex==getColumnHeaderIndexComponents(COL_EdgeColor)) {
		            	return Color.class;
		            } else if(columnIndex==getColumnHeaderIndexComponents(COL_ShowLabel)) {
		            	return Boolean.class;
		            } else {
		            	return String.class;
		            }
		        }
			};
			// --- Fill the table model with data ---------
			this.fillComponentTypeTableModel();
		}
		return componentTypesModel;
	}
	/**
	 * Clear component type table model.
	 */
	private void clearComponentTypeTableModel() {
		if (this.getTableModel4ComponentTypes().getRowCount()>0) {
			this.getTableModel4ComponentTypes().getDataVector().removeAllElements();
			this.getTableModel4ComponentTypes().fireTableDataChanged();
		}

	}
	/**
	 * Fill component type table model.
	 */
	private void fillComponentTypeTableModel() {

		// --- Clear the table model if not empty -------------------
		this.clearComponentTypeTableModel();
		
		if (this.componentTypeSettings==null || this.componentTypeSettings.size()==0)  return;

		Iterator<String> etsIter = this.componentTypeSettings.keySet().iterator();
		while(etsIter.hasNext()){
			
			String etName = etsIter.next();
			ComponentTypeSettings cts = this.componentTypeSettings.get(etName);
			
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
					newRow.add(ComponentTypeDialog.createImageIcon(imagePath, imagePath));
				} else if (i == getColumnHeaderIndexComponents(COL_EdgeWidth)) {
					newRow.add(edgeWidth);
				} else if (i == getColumnHeaderIndexComponents(COL_EdgeColor)) {
					newRow.add(edgeColor);
				}
			}
			this.getTableModel4ComponentTypes().addRow(newRow);
		}
	}
	
	/**
	 * This method adds a new row to the jTableClasses' TableModel.
	 */
	private void addNewComponentRow(){
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
				newRow.add(ComponentTypeDialog.createImageIcon(null, "MissingIcon"));
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
		this.getJTable4ComponentTypes().setSurrendersFocusOnKeystroke(true);
		this.getJTable4ComponentTypes().getEditorComponent().requestFocus();

		
	}
	/**
	 * This method removes a row from the jTableClasses' TableModel.
	 * @param rowNumTable the row num table
	 */
	private void removeComponentRow(int rowNumTable){
		int rowNumModel = this.getJTable4ComponentTypes().convertRowIndexToModel(rowNumTable);
		((DefaultTableModel)getJTable4ComponentTypes().getModel()).removeRow(rowNumModel);
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
	 * Returns a JComboBox with the list of Domains.
	 * @return the JComboBox with the available domains
	 */
	private JComboBoxWide<String> getJComboBoxDomains(Vector<String> domainVector) {
		DefaultComboBoxModel<String> comboBoxModel4Domains = null;
		if (domainVector==null) {
			comboBoxModel4Domains = new DefaultComboBoxModel<String>(this.componentTypeDialog.getDomainVector());
		} else {
			comboBoxModel4Domains = new DefaultComboBoxModel<String>(domainVector);
		}
		return new JComboBoxWide<String>(comboBoxModel4Domains);
	}
	/**
	 * Sets the table CellEditor for domains in components.
	 * @param domainVector the string vector of the current domains
	 */
	public void setTableCellEditor4DomainsInComponents(Vector<String> domainVector){
		TableColumnModel tcm = this.getJTable4ComponentTypes().getColumnModel();
		TableColumn domainColumn = tcm.getColumn(this.getColumnHeaderIndexComponents(COL_Domain));
		domainColumn.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxDomains(domainVector)));
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
		for (int row=0; row<dtmComponents.getRowCount(); row++){
			String currValue = (String) dtmComponents.getValueAt(row, column);
			if (currValue.equals(oldDomainName)) {
				dtmComponents.setValueAt(newDomainName, row, column);	
			}
		}
		this.componentTypeDialog.setTableCellEditor4DomainsInComponents(null);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.getJTable4ComponentTypes().setEnabled(enabled);
    	this.getJButtonAddComponentRow().setEnabled(enabled);
    	this.getJButtonRemoveComponentRow().setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
	/**
	 * Stops the cell editing, if required.
	 * @return true, if a cell editor was found and terminated
	 */
	protected boolean isStopCellEditing() {
		// --- Stop cell editing, if required ----- 
		TableCellEditor editor = getJTable4ComponentTypes().getCellEditor();
		if (editor!=null)  {
			editor.stopCellEditing();
			return true;
		}
		return false;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if(ae.getSource()==this.getJButtonAddComponentRow()){
			// --- Add a new row to the component types table -------
			this.addNewComponentRow();
			
		} else if(ae.getSource()==this.getJButtonRemoveComponentRow()) {
			// --- Remove a row from the component types table ------
			if(getJTable4ComponentTypes().getSelectedRow() > -1){
				this.removeComponentRow(getJTable4ComponentTypes().getSelectedRow());
			}
		} 
		
	}

	/**
	 * Sets the component type settings.
	 * @param componentTypeSettings the component type settings
	 */
	public void setComponentTypeSettings(TreeMap<String, ComponentTypeSettings> componentTypeSettings) {
		this.componentTypeSettings = componentTypeSettings;
		this.fillComponentTypeTableModel();
	}
	/**
	 * Gets the component type settings.
	 * @return the component type settings
	 */
	public TreeMap<String, ComponentTypeSettings> getComponentTypeSettings() {
		this.hasComponentTypeSettingError();
		return componentTypeSettings;
	}
	
	/**
	 * Checks for component type setting errors.
	 */
	public ComponentTypeError hasComponentTypeSettingError() {

		ComponentTypeError cte = null;
		
		// --- Get the component type definitions from table ----
		JTable jtComponents = this.getJTable4ComponentTypes();
		DefaultTableModel dtmComponents = this.getTableModel4ComponentTypes();
		// --- Confirm, apply changes in table ------------------						
		TableCellEditor tceComponents = jtComponents.getCellEditor();
		if (tceComponents!=null) {
			tceComponents.stopCellEditing();
		}
		
		// --- Define a new TreeMap -----------------------
		TreeMap<String, ComponentTypeSettings> ctsHash = new TreeMap<String, ComponentTypeSettings>();
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

				ComponentTypeError componentTypeError = this.isComponentTypeError(name, cts, ctsHash);
				if (componentTypeError!=null) {
					// --- Set focus to error position ---------- 
					this.componentTypeDialog.getJTabbedPane().setSelectedIndex(1);
					int tableRow = jtComponents.convertRowIndexToView(row);
					jtComponents.setRowSelectionInterval(tableRow, tableRow);
					return componentTypeError;
				}
				ctsHash.put(name, cts);
			}
		}
		// --- If arrived here, set to local variable ----- 
		this.componentTypeSettings = ctsHash;
		
		return cte;

		
	}
	/**
	 * Checks if there is component type error.
	 *
	 * @param ctsName the ComponentTypeSettings name
	 * @param cts the ComponentTypeSettings to check
	 * @param ctsHash the ComponentTypeSettings hash that contains the already checked ComponentTypeSettings
	 * @return true, if is component type error
	 */
	private ComponentTypeError isComponentTypeError(String ctsName, ComponentTypeSettings cts, TreeMap<String, ComponentTypeSettings> ctsHash) {
		
		String title = "";
		String message = "";
		if (ctsHash.get(ctsName)!=null) {
			// --- Duplicate ComponentTypeSettings ------------------
			title = Language.translate("Duplicate Component Type", Language.EN) + "!";
			message = Language.translate("The following component type exists at least twice", Language.EN) + ": '" + ctsName + "' !";
			return new ComponentTypeError(title, message);
		}
		
		if (cts.getGraphPrototype()==null || cts.getGraphPrototype().equals("")) {
			// --- Duplicate ComponentTypeSettings ------------------
			title = Language.translate("Component Type ComponentTypeError", Language.EN) + "!";
			message = Language.translate("No Graph-Prototype defined for", Language.EN) + " '" + ctsName + "' !";
			return new ComponentTypeError(title, message);
		}
		return null;
	}
	
	
}
