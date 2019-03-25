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
import java.util.Arrays;
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
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.LayoutSettings;
import agentgui.envModel.graph.networkModel.LayoutSettings.CoordinateSystemXDirection;
import agentgui.envModel.graph.networkModel.LayoutSettings.CoordinateSystemYDirection;
import agentgui.envModel.graph.networkModel.LayoutSettings.EdgeShape;

/**
 * The Class ComponentTypeDialogLayout represents the sub part of the 
 * {@link ComponentTypeDialog} that displays the layout settings.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ComponentTypeDialogLayout extends JPanel implements ActionListener {

	private static final long serialVersionUID = -6805819068032654387L;

	private final String pathImage = GraphGlobals.getPathImages();
	
	private Vector<String> columnHeaderLayouts 		= null;
	private final String COL_L_LayoutID 			= Language.translate("Layout ID", Language.EN);
	private final String COL_L_LayoutName 			= Language.translate("Layout Name", Language.EN); 
	private final String COL_L_X_Direction			= Language.translate("X - Direction", Language.EN);
	private final String COL_L_Y_Direction 			= Language.translate("Y - Direction", Language.EN);
	private final String COL_L_SnapToGrid 			= Language.translate("Use Guide Grid", Language.EN);
	private final String COL_L_SnapGridWidth		= Language.translate("Guide Grid Step", Language.EN);
	private final String COL_L_EdgeShape			= Language.translate("Edge Shape", Language.EN);

	private ComponentTypeDialog componentTypeDialog;
	
	private TreeMap<String, LayoutSettings> layoutSettings;
	
	private JButton jButtonAddLayout;
	private JButton jButtonRemoveLayoutRow;
	private JScrollPane jScrollPaneClassTableLayouts;
	private JTable jTableLayoutTypes;
	private DefaultTableModel layoutTableModel;

	
	/**
	 * Instantiates a new component type dialog layouts.
	 *
	 * @param componentTypeDialog the parent {@link ComponentTypeDialog}
	 * @param layoutSettings the layout settings
	 */
	public ComponentTypeDialogLayout(ComponentTypeDialog componentTypeDialog) {
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
		this.add(this.getJScrollPaneClassTableLayouts(), gridBagConstraints13);
		this.add(this.getJButtonAddLayout(), gridBagConstraints2);
		this.add(this.getJButtonRemoveLayoutRow(), gridBagConstraints3);
	}
	
	/**
	 * This method initializes jButtonAddLayout	.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonAddLayout() {
		if (jButtonAddLayout == null) {
			ImageIcon imageIcon = new ImageIcon(getClass().getResource(this.pathImage + "ListPlus.png"));
			jButtonAddLayout = new JButton();
			jButtonAddLayout.setIcon(imageIcon);
			jButtonAddLayout.addActionListener(this);
		}
		return jButtonAddLayout;
	}
	/**
	 * This method initializes jButtonRemoveLayoutRow	.
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonRemoveLayoutRow() {
		if (jButtonRemoveLayoutRow == null) {
			ImageIcon imageIcon1 = new ImageIcon(getClass().getResource(this.pathImage + "ListMinus.png"));
			jButtonRemoveLayoutRow = new JButton();
			jButtonRemoveLayoutRow.setIcon(imageIcon1);
			jButtonRemoveLayoutRow.addActionListener(this);
		}
		return jButtonRemoveLayoutRow;
	}
	
	/**
	 * This method initializes jScrollPaneClassTableLayouts	.
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneClassTableLayouts() {
		if (jScrollPaneClassTableLayouts == null) {
			jScrollPaneClassTableLayouts = new JScrollPane();
			jScrollPaneClassTableLayouts.setViewportView(getJTable4LayoutTypes());
		}
		return jScrollPaneClassTableLayouts;
	}
	
	/**
	 * Gets the Vector of the column header in the needed order.
	 * @return the column header
	 */
	private Vector<String> getColumnHeaderLayouts() {
		if (columnHeaderLayouts==null) {
			columnHeaderLayouts = new Vector<String>();
			columnHeaderLayouts.add(COL_L_LayoutName);
			columnHeaderLayouts.add(COL_L_X_Direction);
			columnHeaderLayouts.add(COL_L_Y_Direction);
			columnHeaderLayouts.add(COL_L_SnapToGrid);
			columnHeaderLayouts.add(COL_L_SnapGridWidth);	
			columnHeaderLayouts.add(COL_L_EdgeShape);
			columnHeaderLayouts.add(COL_L_LayoutID);
		}
		return columnHeaderLayouts;
	}
	/**
	 * Gets the header index.
	 * @param header the header
	 * @return the header index
	 */
	private int getColumnHeaderIndexLayouts(String header) {
		Vector<String> headers = this.getColumnHeaderLayouts();
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
	 * This method initializes jTableLayoutTypes	.
	 * @return javax.swing.JTable
	 */
	private JTable getJTable4LayoutTypes() {
		if (jTableLayoutTypes == null) {
			
			jTableLayoutTypes = new JTable();
			jTableLayoutTypes.setModel(this.getTableModel4Layouts());
			jTableLayoutTypes.setFillsViewportHeight(true);
			jTableLayoutTypes.setShowGrid(false);
			jTableLayoutTypes.setRowHeight(20);
			jTableLayoutTypes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableLayoutTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableLayoutTypes.setAutoCreateRowSorter(true);
			jTableLayoutTypes.getTableHeader().setReorderingAllowed(false);
			
			// --- Define the sorter ----------------------
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(getTableModel4Layouts());
			sorter.setComparator(getColumnHeaderIndexLayouts(COL_L_SnapGridWidth), new Comparator<Boolean>() {
				@Override
				public int compare(Boolean o1, Boolean o2) {
					return o1.compareTo(o2);
				}
			});
			sorter.setComparator(getColumnHeaderIndexLayouts(COL_L_Y_Direction), new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
			sorter.setComparator(getColumnHeaderIndexLayouts(COL_L_EdgeShape), new Comparator<Color>() {
				@Override
				public int compare(Color o1, Color o2) {
					Integer o1RGB = o1.getRGB();
					Integer o2RGB = o2.getRGB();
					return o1RGB.compareTo(o2RGB);
				}
			});
			sorter.setComparator(getColumnHeaderIndexLayouts(COL_L_SnapToGrid), new Comparator<Color>() {
				@Override
				public int compare(Color o1, Color o2) {
					Integer o1RGB = o1.getRGB();
					Integer o2RGB = o2.getRGB();
					return o1RGB.compareTo(o2RGB);
				}
			});
			jTableLayoutTypes.setRowSorter(sorter);

			
			// --- Define the first sort order ------------
			List<SortKey> sortKeys = new ArrayList<SortKey>();
			for (int i = 0; i < jTableLayoutTypes.getColumnCount(); i++) {
			    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
			}
			jTableLayoutTypes.getRowSorter().setSortKeys(sortKeys);

			
			// --- Configure the editor and the renderer of the cells ---------
			TableColumnModel tcm = jTableLayoutTypes.getColumnModel();

			TableColumn colXDirection = tcm.getColumn(getColumnHeaderIndexLayouts(COL_L_X_Direction));
			colXDirection.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxCoordinateSystemXDirection()));

			TableColumn colYDirection = tcm.getColumn(getColumnHeaderIndexLayouts(COL_L_Y_Direction));
			colYDirection.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxCoordinateSystemYDirection()));
			colYDirection.setPreferredWidth(10);
			
			TableColumn colSnapToGrid = tcm.getColumn(getColumnHeaderIndexLayouts(COL_L_SnapToGrid));
			colSnapToGrid.setCellEditor(new TableCellRenderEditor4CheckBox());
			colSnapToGrid.setCellRenderer(new TableCellRenderEditor4CheckBox());
			colSnapToGrid.setPreferredWidth(10);
			
			TableColumn colSnapGridWidth = tcm.getColumn(getColumnHeaderIndexLayouts(COL_L_SnapGridWidth));
			colSnapGridWidth.setCellEditor(new TableCellEditor4Spinner(0.1, 100, 0.1));
			colSnapGridWidth.setCellRenderer(new TableCellEditor4Spinner(0.1, 100, 0.1));
			colSnapGridWidth.setPreferredWidth(10);
			
			TableColumn colEdgeShape = tcm.getColumn(getColumnHeaderIndexLayouts(COL_L_EdgeShape));
			colEdgeShape.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxEdgeShape()));
			colEdgeShape.setPreferredWidth(10);

			// --- Remove the column with the layoutID ---- 
			tcm.removeColumn(tcm.getColumn(getColumnHeaderIndexLayouts(COL_L_LayoutID)));
			
		}
		return jTableLayoutTypes;
	}
	
	/**
	 * Gets the table model for layouts.
	 * @return the table model4 layouts
	 */
	private DefaultTableModel getTableModel4Layouts(){
		if (layoutTableModel==null) {
			layoutTableModel = new DefaultTableModel(null, this.getColumnHeaderLayouts()){
				private static final long serialVersionUID = 3550155601170744633L;
				/* (non-Javadoc)
				 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
				 */
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					if (columnIndex==getColumnHeaderIndexLayouts(COL_L_X_Direction)) {
						return CoordinateSystemXDirection.class;
					} else if (columnIndex==getColumnHeaderIndexLayouts(COL_L_Y_Direction)) {
						return CoordinateSystemYDirection.class;
					} else if(columnIndex==getColumnHeaderIndexLayouts(COL_L_SnapToGrid)) {
						return Boolean.class;
					} else if(columnIndex==getColumnHeaderIndexLayouts(COL_L_SnapGridWidth)) {
						return Double.class;
					} else if (columnIndex==getColumnHeaderIndexLayouts(COL_L_EdgeShape)) {
		            	return EdgeShape.class;
		            } else {
		            	return String.class;
		            }
		        }
				/* (non-Javadoc)
				 * @see javax.swing.table.isCellEditable#getColumnClass(int, int)
				 */
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column==getColumnHeaderIndexLayouts(COL_L_LayoutName)) {
						String value = (String) getTableModel4Layouts().getValueAt(row, column);
						if (value!=null && value.equals(GeneralGraphSettings4MAS.DEFAULT_LAYOUT_SETTINGS_NAME)==true) {
							return false;
						}
					}
					return true;
				}
			};
			// --- Fill the table model with data ---------
			this.fillLayoutTableModel();
		}
		return layoutTableModel;
	}
	/**
	 * Clear layout table model table.
	 */
	private void clearLayoutTableModel() {
		if (this.getTableModel4Layouts().getRowCount()>0) {
			this.getTableModel4Layouts().getDataVector().removeAllElements();
			this.getTableModel4Layouts().fireTableDataChanged();
		}
	}
	/**
	 * Fill layout table model.
	 */
	private void fillLayoutTableModel() {
		
		// --- Clear the table model if not empty -------------------
		this.clearLayoutTableModel();
		
		if (this.layoutSettings==null || this.layoutSettings.size()==0) return;
		
		// --- Set table entries for defined assignments, if any ----
		Iterator<String> layoutIterator = this.layoutSettings.keySet().iterator();
		while (layoutIterator.hasNext()){
			
			String layoutID = layoutIterator.next();
			LayoutSettings layoutSetting = this.layoutSettings.get(layoutID);
			
			String layoutName = layoutSetting.getLayoutName();
			CoordinateSystemXDirection xDircetion = layoutSetting.getCoordinateSystemXDirection();
			CoordinateSystemYDirection yDircetion = layoutSetting.getCoordinateSystemYDirection();
			boolean isSnap2Grid = layoutSetting.isSnap2Grid();
			double snapRaster = layoutSetting.getSnapRaster();
			EdgeShape edgeShape = layoutSetting.getEdgeShape();
			
			// --- Create row vector --------------
			Vector<Object> newRow = new Vector<Object>();
			for (int i = 0; i < this.getColumnHeaderLayouts().size(); i++) {
				if (i == getColumnHeaderIndexLayouts(COL_L_LayoutID)) {
					newRow.add(layoutID);
				} else if (i == getColumnHeaderIndexLayouts(COL_L_LayoutName)) {
					newRow.add(layoutName);
				} else if (i == getColumnHeaderIndexLayouts(COL_L_X_Direction)) {
					newRow.add(xDircetion);
				} else if (i == getColumnHeaderIndexLayouts(COL_L_Y_Direction)) {
					newRow.add(yDircetion);
				} else if (i == getColumnHeaderIndexLayouts(COL_L_SnapToGrid)) {
					newRow.add(isSnap2Grid);
				} else if (i == getColumnHeaderIndexLayouts(COL_L_SnapGridWidth)) {
					newRow.add(snapRaster);
				} else if (i == getColumnHeaderIndexLayouts(COL_L_EdgeShape)) {
					newRow.add(edgeShape);
				}
			}
			this.getTableModel4Layouts().addRow(newRow);
		}
	}
	
	/**
	 * This method adds a new default row to the table for layouts.
	 */
	private void addNewLayoutRow(){
		// --- Create row vector --------------
		Vector<Object> newRow = new Vector<Object>();
		for (int i = 0; i < this.getColumnHeaderLayouts().size(); i++) {
			if (i == getColumnHeaderIndexLayouts(COL_L_LayoutID)) {
				newRow.add(GeneralGraphSettings4MAS.generateLayoutID());
			} else if (i == getColumnHeaderIndexLayouts(COL_L_LayoutName)) {
				newRow.add(null);
			} else if (i == getColumnHeaderIndexLayouts(COL_L_X_Direction)) {
				newRow.add(CoordinateSystemXDirection.East);
			} else if (i == getColumnHeaderIndexLayouts(COL_L_Y_Direction)) {
				newRow.add(CoordinateSystemYDirection.ClockwiseToX);
			} else if (i == getColumnHeaderIndexLayouts(COL_L_SnapToGrid)) {
				newRow.add(true);
			} else if (i == getColumnHeaderIndexLayouts(COL_L_SnapGridWidth)) {
				newRow.add(LayoutSettings.DEFAULT_RASTER_SIZE);
			} else if (i == getColumnHeaderIndexLayouts(COL_L_EdgeShape)) {
				newRow.add(EdgeShape.Line);
			}
		}
		
		this.getTableModel4Layouts().addRow(newRow);
		int newIndex = this.getTableModel4Layouts().getRowCount() - 1;
		newIndex = this.getJTable4LayoutTypes().convertRowIndexToView(newIndex);
		
		this.getJTable4LayoutTypes().changeSelection(newIndex, 0, false, false);
		this.getJTable4LayoutTypes().editCellAt(newIndex, 0);
		this.getJTable4LayoutTypes().setSurrendersFocusOnKeystroke(true);
		this.getJTable4LayoutTypes().getEditorComponent().requestFocus();
	}
	
	/**
	 * Removes the layout row.
	 * @param rowNumTable the row num
	 */
	private void removeLayoutRow(int rowNumTable){
		
		int rowNumModel = this.getJTable4LayoutTypes().convertRowIndexToModel(rowNumTable);
		int colLayout = this.getColumnHeaderIndexLayouts(COL_L_LayoutName);
		String layoutName = (String)this.getJTable4LayoutTypes().getValueAt(rowNumTable, colLayout);
		String defaultLayout = GeneralGraphSettings4MAS.DEFAULT_LAYOUT_SETTINGS_NAME;
		
		if (layoutName!=null) {
			if (layoutName.equals(defaultLayout)) {
				String newLine = Application.getGlobalInfo().getNewLineSeparator();
				String msg = Language.translate("Dieser Eintrag ist ein notwendiger Systemparameter, der " + newLine + "nicht gelöscht oder umbenannt werden darf!");
				String title = "'" + defaultLayout + "': " +  Language.translate("Löschen nicht zulässig!");
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				return;
			} 
		} 
		((DefaultTableModel)getJTable4LayoutTypes().getModel()).removeRow(rowNumModel);
	}
	
	/**
	 * Gets the combo box for the coordinate systems X direction.
	 * @return the combo box coordinate system X direction
	 */
	private JComboBoxWide<CoordinateSystemXDirection> getJComboBoxCoordinateSystemXDirection() {
		
		DefaultComboBoxModel<CoordinateSystemXDirection> cbmXdirections = new DefaultComboBoxModel<>(); 
		cbmXdirections.addElement(CoordinateSystemXDirection.East);
		cbmXdirections.addElement(CoordinateSystemXDirection.North);
		cbmXdirections.addElement(CoordinateSystemXDirection.West);
		cbmXdirections.addElement(CoordinateSystemXDirection.South);
		
		JComboBoxWide<CoordinateSystemXDirection> jComboBoxNodeSize = new JComboBoxWide<>(cbmXdirections);
		jComboBoxNodeSize.setPreferredSize(new Dimension(50, 26));
		return jComboBoxNodeSize;
	}
	/**
	 * Gets the combo box for the coordinate systems Y direction.
	 * @return the combo box coordinate system Y direction
	 */
	private JComboBoxWide<CoordinateSystemYDirection> getJComboBoxCoordinateSystemYDirection() {
		
		DefaultComboBoxModel<CoordinateSystemYDirection> cbmXdirections = new DefaultComboBoxModel<>(); 
		cbmXdirections.addElement(CoordinateSystemYDirection.ClockwiseToX);
		cbmXdirections.addElement(CoordinateSystemYDirection.CounterclockwiseToX);
		
		JComboBoxWide<CoordinateSystemYDirection> jComboBoxNodeSize = new JComboBoxWide<>(cbmXdirections);
		jComboBoxNodeSize.setPreferredSize(new Dimension(50, 26));
		return jComboBoxNodeSize;
	}
	/**
	 * Gets the combo box for the coordinate systems Y direction.
	 * @return the combo box coordinate system Y direction
	 */
	private JComboBoxWide<EdgeShape> getJComboBoxEdgeShape() {
		
		DefaultComboBoxModel<EdgeShape> cbmXdirections = new DefaultComboBoxModel<>(); 
		List<EdgeShape> shapes = new ArrayList<EdgeShape>(Arrays.asList(EdgeShape.values()));
		for (int i = 0; i < shapes.size(); i++) {
			cbmXdirections.addElement(shapes.get(i));
		}
		
		JComboBoxWide<EdgeShape> jComboBoxEdgeShapes = new JComboBoxWide<>(cbmXdirections);
		jComboBoxEdgeShapes.setMaximumRowCount(12);
		return jComboBoxEdgeShapes;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.getJTable4LayoutTypes().setEnabled(enabled);
    	this.getJButtonAddLayout().setEnabled(enabled);
    	this.getJButtonRemoveLayoutRow().setEnabled(enabled);
		super.setEnabled(enabled);
	}
	/**
	 * Stops the cell editing, if required.
	 * @return true, if a cell editor was found and terminated
	 */
	protected boolean isStopCellEditing() {
		// --- Stop cell editing, if required ----- 
    	TableCellEditor editor = this.getJTable4LayoutTypes().getCellEditor();
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
		
		if (ae.getSource()==this.getJButtonAddLayout()) {
			// --- Add a new row to the layout table ----------------
			this.addNewLayoutRow();
			
		} else if(ae.getSource()==this.getJButtonRemoveLayoutRow()) {
			// --- Remove a row from the component types table ------
			if(getJTable4LayoutTypes().getSelectedRow() > -1){
				this.removeLayoutRow(getJTable4LayoutTypes().getSelectedRow());
			}
		}		
	}
	
	/**
	 * Sets the layout settings.
	 * @param layoutSettings the layout settings
	 */
	public void setLayoutSettings(TreeMap<String, LayoutSettings> layoutSettings) {
		this.layoutSettings = layoutSettings;
		this.fillLayoutTableModel();
	}
	/**
	 * Returns the TreeMap of LayoutSettings.
	 * @return the layout settings
	 */
	public TreeMap<String, LayoutSettings> getLayoutSettings() {
		this.hasLayoutSettingError();
		return layoutSettings;
	}
	
	/**
	 * Checks for layout setting errors.
	 * @return the component type error
	 */
	public ComponentTypeError hasLayoutSettingError() {
		
		ComponentTypeError cte = null;
		
		JTable jtLayouts = this.getJTable4LayoutTypes();
		DefaultTableModel dtmLayouts = this.getTableModel4Layouts();
		// --- Confirm, apply changes in table ------------					
		TableCellEditor tceLayouts = jtLayouts.getCellEditor();
		if (tceLayouts!=null) {
			tceLayouts.stopCellEditing();
		}

		// --- Define a new TreeMap -----------------------
		TreeMap<String, LayoutSettings> lsTreeMap = new TreeMap<String, LayoutSettings>();
		for (int row=0; row<dtmLayouts.getRowCount(); row++){
			
			String layoutName = (String) dtmLayouts.getValueAt(row, this.getColumnHeaderIndexLayouts(COL_L_LayoutName));
			if (layoutName!=null && layoutName.length()!=0){
				
				String layoutID 						= (String) dtmLayouts.getValueAt(row, this.getColumnHeaderIndexLayouts(COL_L_LayoutID));
				CoordinateSystemXDirection xDirection 	= (CoordinateSystemXDirection) dtmLayouts.getValueAt(row, this.getColumnHeaderIndexLayouts(COL_L_X_Direction));
				CoordinateSystemYDirection yDirection	= (CoordinateSystemYDirection) dtmLayouts.getValueAt(row, this.getColumnHeaderIndexLayouts(COL_L_Y_Direction));
				boolean isSnapToGrid 					= (boolean)  dtmLayouts.getValueAt(row, this.getColumnHeaderIndexLayouts(COL_L_SnapToGrid));					
				double  snapRaster 						= (double) dtmLayouts.getValueAt(row, this.getColumnHeaderIndexLayouts(COL_L_SnapGridWidth));
				EdgeShape edgeShape 					= (EdgeShape) dtmLayouts.getValueAt(row, this.getColumnHeaderIndexLayouts(COL_L_EdgeShape));
				
				LayoutSettings ls = new LayoutSettings();
				ls.setLayoutName(layoutName);
				ls.setCoordinateSystemXDirection(xDirection);
				ls.setCoordinateSystemYDirection(yDirection);
				ls.setSnap2Grid(isSnapToGrid);
				ls.setSnapRaster(snapRaster);
				ls.setEdgeShape(edgeShape);
				
				ComponentTypeError componentTypeError = this.isLayoutConfigError(layoutName, ls, lsTreeMap);
				if (componentTypeError!=null) {
					// --- Set focus to error position ---- 
					this.componentTypeDialog.getJTabbedPane().setSelectedIndex(2);
					int tableRow = jtLayouts.convertRowIndexToView(row);
					jtLayouts.setRowSelectionInterval(tableRow, tableRow);
					return componentTypeError;
				}
				lsTreeMap.put(layoutID, ls);
			}
		}
		// --- If arrived here, set to local variable ----- 
		this.layoutSettings = lsTreeMap;
		
		return cte;
	}
	/**
	 * Checks if there is layout configuration error.
	 *
	 * @param lsName the LayoutSettings name
	 * @param lsToCheck the LayoutSettings to check
	 * @param lsTreeMap the LayoutSettings hash that contains the already checked LayoutSettings
	 * @return true, if is layout configuration error
	 */
	private ComponentTypeError isLayoutConfigError(String lsID, LayoutSettings lsToCheck, TreeMap<String, LayoutSettings> lsTreeMap) {
		
		String title = "";
		String message = "";
		if (lsTreeMap.get(lsID)!=null) {
			// --- Duplicate LayoutSettings ID ----------------------
			title = Language.translate("Duplicate Layout", Language.EN) + " ID !";
			message = Language.translate("The following layout exists at least twice", Language.EN) + ": Layout-ID <b>'" + lsID + "'</b> !";
			return new ComponentTypeError(title, message);
		}
		
		String layoutNameToCheck = lsToCheck.getLayoutName();
		if (this.isAlreadyAvailableLayoutName(layoutNameToCheck, lsTreeMap)==true) {
			// --- Duplicate LayoutSettings Name --------------------
			title = Language.translate("Duplicate Layout", Language.EN) + " Name !";
			message = Language.translate("The following layout exists at least twice", Language.EN) + ": '" + layoutNameToCheck + "' !";
			return new ComponentTypeError(title, message);
		}
		return null;
	}
	/**
	 * Checks if the specified layout name is already available.
	 *
	 * @param layoutNameToCheck the layout name to check
	 * @param lsTreeMap the LayoutSettings hash that contains the already checked LayoutSettings
	 * @return true, if is already available layout name
	 */
	private boolean isAlreadyAvailableLayoutName(String layoutNameToCheck, TreeMap<String, LayoutSettings> lsTreeMap) {
		
		List<LayoutSettings> layoutSettingList = new ArrayList<>(lsTreeMap.values());
		for (int i = 0; i < layoutSettingList.size(); i++) {
			LayoutSettings lsWork = layoutSettingList.get(i);
			if (lsWork.getLayoutName().equals(layoutNameToCheck)) {
				return true;
			}
		}
		return false;

	}
	
}
