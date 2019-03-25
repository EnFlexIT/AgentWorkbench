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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.JComboBoxWide;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.networkModel.DomainSettings;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;

/**
 * The Class ComponentTypeDialogDomains represents the sub part of the 
 * {@link ComponentTypeDialog} that displays the domain settings.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ComponentTypeDialogDomains extends JPanel implements ActionListener {

	private static final long serialVersionUID = -6805819068032654387L;

	private final String pathImage = GraphGlobals.getPathImages();
	
	private Vector<String> columnHeaderDomains 		= null;
	private final String COL_D_DomainName 			= Language.translate("Name", Language.EN); 
	private final String COL_D_AdapterClass			= Language.translate("Adapter class", Language.EN);
	private final String COL_D_VertexSize 			= Language.translate("Vertex size", Language.EN);
	private final String COL_D_VertexColor			= Language.translate("Color", Language.EN);
	private final String COL_D_VertexColorPicked 	= Language.translate("Color picked", Language.EN);
	private final String COL_D_ShowLable			= Language.translate("Show label", Language.EN);
	private final String COL_D_ClusterShape			= Language.translate("Cluster shape", Language.EN);
	private final String COL_D_ClusterAgent			= Language.translate("Cluster agent", Language.EN);

	private ComponentTypeDialog componentTypeDialog;
	
	private TreeMap<String, DomainSettings> domainSettings;
	
	private JButton jButtonAddDomain;
	private JButton jButtonRemoveDomainntRow;
	private JScrollPane jScrollPaneClassTableDomains;
	private JTable jTableDomainTypes;
	private DefaultTableModel domainTableModel;

	
	/**
	 * Instantiates a new component type dialog domains.
	 *
	 * @param componentTypeDialog the parent {@link ComponentTypeDialog}
	 * @param domainSettings the domain settings
	 */
	public ComponentTypeDialogDomains(ComponentTypeDialog componentTypeDialog) {
		this.componentTypeDialog = componentTypeDialog;
		this.initialize();
	}
	/**
	 * Initializes this panel.
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.insets = new Insets(5, 5, 3, 5);
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.gridy = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.insets = new Insets(5, 5, 3, 5);
		gridBagConstraints2.gridy = 0;
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.fill = GridBagConstraints.BOTH;
		gridBagConstraints13.weighty = 1.0;
		gridBagConstraints13.gridx = 0;
		gridBagConstraints13.gridy = 1;
		gridBagConstraints13.gridwidth = 2;
		gridBagConstraints13.weightx = 1.0;
		
		this.setLayout(new GridBagLayout());
		//this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(this.getJScrollPaneClassTableDomains(), gridBagConstraints13);
		this.add(this.getJButtonAddDomain(), gridBagConstraints2);
		this.add(this.getJButtonRemoveDomainntRow(), gridBagConstraints3);
	}
	
	/**
	 * This method initializes jButtonAddDomain	.
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
	 * This method initializes jTableDomainTypes	.
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
			domainColumn.setCellEditor(new TableCellEditor4Domains(this.componentTypeDialog));
			
			//Set up renderer and editor for the agent class column
			TableColumn agentClassColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_AdapterClass));
			agentClassColumn.setCellEditor(this.componentTypeDialog.getAdapterClassesCellEditor());
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
			showLabelClassColumn.setCellEditor(new TableCellRenderEditor4CheckBox());
			showLabelClassColumn.setCellRenderer(new TableCellRenderEditor4CheckBox());
			showLabelClassColumn.setPreferredWidth(10);
			
			TableColumn clusterShapeColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_ClusterShape));
			clusterShapeColumn.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxClusterShape()));
			clusterShapeColumn.setPreferredWidth(10);
			
			TableColumn clusterAgentColumn = tcm.getColumn(getColumnHeaderIndexDomains(COL_D_ClusterAgent));
			clusterAgentColumn.setCellEditor(this.componentTypeDialog.getAgentClassesCellEditor());
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
			domainTableModel = new DefaultTableModel(null, this.getColumnHeaderDomains()){
				private static final long serialVersionUID = 3550155601170744633L;
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				public Class<?> getColumnClass(int columnIndex) {
		            if(columnIndex==getColumnHeaderIndexDomains(COL_D_VertexColor)) {
		            	return Color.class;
		            } else if(columnIndex==getColumnHeaderIndexDomains(COL_D_VertexColorPicked)) {
		            	return Color.class;
		            } else if(columnIndex==getColumnHeaderIndexDomains(COL_D_ShowLable)) {
		            	return Boolean.class;
		            } else {
		            	return String.class;
		            }
		        }
			
				/* (non-Javadoc)
				 * @see javax.swing.table.isCellEditable#getColumnClass(int, int)
				 */
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column==0) {
						String value = (String) getTableModel4Domains().getValueAt(row, column);
						if (value!=null && value.equals(GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME)==true) {
							return false;
						}
					}
					return true;
				}
			};
			
			
			// --- Fill the table model with data ---------
			this.fillDomainTableModel();
		}
		return domainTableModel;
	}
	/**
	 * Clear domain table model table.
	 */
	private void clearDomainTableModel() {
		if (this.getTableModel4Domains().getRowCount()>0) {
			this.getTableModel4Domains().getDataVector().removeAllElements();
			this.getTableModel4Domains().fireTableDataChanged();
		}
	}
	/**
	 * Fill domain table model.
	 */
	private void fillDomainTableModel() {
		
		// --- Clear the table model if not empty -------------------
		this.clearDomainTableModel();
		
		if (this.domainSettings==null || this.domainSettings.size()==0)  return;
		
		// --- Set table entries for defined assignments, if any ----
		Iterator<String> domainIterator = this.domainSettings.keySet().iterator();
		while (domainIterator.hasNext()){
			
			String domainName = domainIterator.next();
			DomainSettings domSetting = this.domainSettings.get(domainName);

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
			this.getTableModel4Domains().addRow(newRow);
		}
	}
	
	/**
	 * This method adds a new row to the jTableClasses' TableModel4Domains.
	 */
	private void addNewDomainRow(){
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
		this.getJTable4DomainTypes().setSurrendersFocusOnKeystroke(true);
		this.getJTable4DomainTypes().getEditorComponent().requestFocus();
		this.componentTypeDialog.setTableCellEditor4DomainsInComponents(null);
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
		this.componentTypeDialog.renameDomainInComponents(domainName, defaultDomain);
		this.componentTypeDialog.setTableCellEditor4DomainsInComponents(null);	
		
	}
	
	/**
	 * This method initializes jComboBoxNodeSize	.
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
	 * Return the current domain vector.
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

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.getJTable4DomainTypes().setEnabled(enabled);
    	this.getJButtonAddDomain().setEnabled(enabled);
    	this.getJButtonRemoveDomainntRow().setEnabled(enabled);
		super.setEnabled(enabled);
	}
	/**
	 * Stops the cell editing, if required.
	 * @return true, if a cell editor was found and terminated
	 */
	protected boolean isStopCellEditing() {
		// --- Stop cell editing, if required ----- 
    	TableCellEditor editor = this.getJTable4DomainTypes().getCellEditor();
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
		
		if (ae.getSource()==this.getJButtonAddDomain()) {
			// --- Add a new row to the domain table ----------------
			this.addNewDomainRow();
			
		} else if(ae.getSource()==this.getJButtonRemoveDomainntRow()) {
			// --- Remove a row from the component types table ------
			if(getJTable4DomainTypes().getSelectedRow() > -1){
				this.removeDomainRow(getJTable4DomainTypes().getSelectedRow());
			}
		}		
	}
	
	/**
	 * Sets the domain settings.
	 * @param domainSettings the domain settings
	 */
	public void setDomainSettings(TreeMap<String, DomainSettings> domainSettings) {
		this.domainSettings = domainSettings;
		this.fillDomainTableModel();
	}
	/**
	 * Returns the TreeMap of DomainSettings.
	 * @return the domain settings
	 */
	public TreeMap<String, DomainSettings> getDomainSettings() {
		this.hasDomainSettingError();
		return domainSettings;
	}
	
	/**
	 * Checks for domain setting errors.
	 * @return the component type error
	 */
	public ComponentTypeError hasDomainSettingError() {
		
		ComponentTypeError cte = null;
		
		JTable jtDomains = this.getJTable4DomainTypes();
		DefaultTableModel dtmDomains = this.getTableModel4Domains();
		// --- Confirm, apply changes in table ------------					
		TableCellEditor tceDomains = jtDomains.getCellEditor();
		if (tceDomains!=null) {
			tceDomains.stopCellEditing();
		}

		// --- Define a new TreeMap -----------------------
		TreeMap<String, DomainSettings> dsTreeMap = new TreeMap<String, DomainSettings>();
		for (int row=0; row<dtmDomains.getRowCount(); row++){
			
			String name = (String) dtmDomains.getValueAt(row, this.getColumnHeaderIndexDomains(COL_D_DomainName));
			if (name!=null && name.length()!=0){
				
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
				
				ComponentTypeError componentTypeError = this.isDomainConfigError(name, ds, dsTreeMap);
				if (componentTypeError!=null) {
					// --- Set focus to error position ---- 
					this.componentTypeDialog.getJTabbedPane().setSelectedIndex(0);
					int tableRow = jtDomains.convertRowIndexToView(row);
					jtDomains.setRowSelectionInterval(tableRow, tableRow);
					return componentTypeError;
				}
				dsTreeMap.put(name, ds);
			}
		}
		// --- If arrived here, set to local variable ----- 
		this.domainSettings = dsTreeMap;
		
		return cte;
	}
	/**
	 * Checks if there is domain configuration error.
	 *
	 * @param dsName the DomainSettings name
	 * @param ds the DomainSettings to check
	 * @param dsHash the DomainSettings hash that contains the already checked DomainSettings
	 * @return true, if is domain configuration error
	 */
	private ComponentTypeError isDomainConfigError(String dsName, DomainSettings ds, TreeMap<String, DomainSettings> dsHash) {
		
		String title = "";
		String message = "";
		if (dsHash.get(dsName)!=null) {
			// --- Duplicate DomainSettings -------------------------
			title = Language.translate("Duplicate Domain", Language.EN) + "!";
			message = Language.translate("The following domain exists at least twice", Language.EN) + ": '" + dsName + "' !";
			return new ComponentTypeError(title, message);
		}
		return null;
	}
	
}
