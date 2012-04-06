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
package gasmas.transfer.zib;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import agentgui.core.application.Language;
import agentgui.envModel.graph.components.TableCellEditor4Combo;
import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.ComponentTypeSettings;

public class UserMapping extends JDialog implements ActionListener {

	private static final long serialVersionUID = -128684181921347260L;
	
	private JPanel jContentPane = null;
	private JScrollPane jScrollPaneTable = null;
	private JTable jTableMapping = null;
	private DefaultTableModel tableModelMappingModel = null;
	private JPanel jPanelBottom = null;
	private JButton jButtonOK = null;
	private JButton jButtonCancel = null;
	private JLabel jLabelTitle = null;

	private HashMap<String, TypeDescription> externalTypes = null; 
	private GeneralGraphSettings4MAS generalGraphSettings4MAS = null;
	private boolean cancelled = false;
	
	
	/**
	 * @param owner
	 */
	public UserMapping(Frame owner, HashMap<String, TypeDescription> externalTypes, GeneralGraphSettings4MAS generalGraphSettings4MAS) {
		super(owner);
		this.externalTypes = externalTypes;
		this.generalGraphSettings4MAS = generalGraphSettings4MAS;
		this.initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setSize(600, 430);
		this.setTitle("Types to Components mapping");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setContentPane(getJContentPane());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				cancelled = true;
				setVisible(false);
			}
		});
		
		// --- Center Dialog --------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	
		
		// --- Translations ---------------------
		jLabelTitle.setText(Language.translate(jLabelTitle.getText(),Language.EN));
		jButtonCancel.setText(Language.translate(jButtonCancel.getText(),Language.EN));
		
	}

	/**
	 * Checks if is cancelled.
	 * @return true, if is cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Gets the edited external types.
	 * @return the external types
	 */
	public HashMap<String, TypeDescription> getExternalTypes() {
		return externalTypes;
	}

	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(10, 10, 0, 0);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.insets = new Insets(0, 10, 15, 10);
			gridBagConstraints1.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(10, 10, 10, 10);
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJScrollPaneTable(), gridBagConstraints);
			jContentPane.add(getJPanelBottom(), gridBagConstraints1);
			jContentPane.add(getJLabelTitle(), gridBagConstraints2);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPaneTable	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneTable() {
		if (jScrollPaneTable == null) {
			jScrollPaneTable = new JScrollPane();
			jScrollPaneTable.setViewportView(getJTableMapping());
		}
		return jScrollPaneTable;
	}

	/**
	 * Gets the table model4 mapping.
	 * @return the table model4 mapping
	 */
	private DefaultTableModel getTableModel4Mapping(){
		
		if (this.tableModelMappingModel== null) {
			
			// --- Define header of table -----------------
			Vector<String> columnHeader = new Vector<String>();
			columnHeader.add(Language.translate("External Type", Language.EN));
			columnHeader.add(Language.translate("Component Type", Language.EN));
			
			// --- Define data rows ----------------------- 
			Vector<Vector<Object>> dataRows = new Vector<Vector<Object>>();
			Iterator<String> typeIterator = this.externalTypes.keySet().iterator();
			while(typeIterator.hasNext()){
				String typeName = typeIterator.next();
				Vector<Object> newRow = new Vector<Object>();
				newRow.add(typeName);
				newRow.add(null);
				dataRows.add(newRow);
			}
			
			// --- Finally, create TableModel -------------
			this.tableModelMappingModel =  new DefaultTableModel(dataRows, columnHeader) {
				private static final long serialVersionUID = -955036182324685554L;
				public boolean isCellEditable(int row, int column) {
					if (column==0) {
						return false;
					} else {
						return true;
					}
				};
			};
			
		}
		return this.tableModelMappingModel;
	}
	
	/**
	 * This method initializes jTableMapping	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableMapping() {
		if (jTableMapping == null) {
			
			jTableMapping = new JTable(this.getTableModel4Mapping());
			jTableMapping.setFillsViewportHeight(true);
			jTableMapping.setShowGrid(false);
			jTableMapping.setRowHeight(20);
			jTableMapping.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableMapping.setAutoCreateRowSorter(true);
			jTableMapping.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			// --- Define the first sort order --------------------------------
			List<SortKey> sortKeys = new ArrayList<SortKey>();
			for (int i = 0; i < jTableMapping.getColumnCount(); i++) {
			    sortKeys.add(new SortKey(i, SortOrder.ASCENDING));
			}
			jTableMapping.getRowSorter().setSortKeys(sortKeys);
			
			// --- Configure the editor and the renderer of the cells ---------
			TableColumnModel tcm = jTableMapping.getColumnModel();
			
			TableColumn domainColumn = tcm.getColumn(1);
			domainColumn.setCellEditor(new TableCellEditor4Combo(this.getJComboBoxComponents()));
		}
		return jTableMapping;
	}

	/**
	 * Gets the JComboBox for the known components.
	 * @return the JComboBox for the components
	 */
	private JComboBox getJComboBoxComponents() {
		
		Vector<String> ctsVector =  new Vector<String>();
		HashMap<String, ComponentTypeSettings> cts = this.generalGraphSettings4MAS.getCurrentCTS();
		for (String ctsName : cts.keySet()) {
			ctsVector.addElement(ctsName);	
		}
		Collections.sort(ctsVector);
		DefaultComboBoxModel comboBoxModel4Domains = new DefaultComboBoxModel(ctsVector);
		
		JComboBox jComboBoxNodeSize = new JComboBox(comboBoxModel4Domains);
		return jComboBoxNodeSize;
	}
	
	/**
	 * This method initializes jPanelBottom	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(0, 0, 0, 50);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(0, 50, 0, 0);
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.add(getJButtonOK(), gridBagConstraints4);
			jPanelBottom.add(getJButtonCancel(), gridBagConstraints3);
		}
		return jPanelBottom;
	}

	/**
	 * This method initializes jButtonOK	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText("OK");
			jButtonOK.setForeground(new Color(0, 153, 0));
			jButtonOK.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonOK.setPreferredSize(new Dimension(80, 26));
			jButtonOK.addActionListener(this);
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Cancel");
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setPreferredSize(new Dimension(80, 26));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jLabelTitle	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabelTitle() {
		if (jLabelTitle == null) {
			jLabelTitle = new JLabel();
			jLabelTitle.setText("Set mapping of external types to local components");
			jLabelTitle.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTitle;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource() == this.getJButtonOK()) {
			// --- Stop editing -----------------
			JTable jtMapping = this.getJTableMapping();
			TableCellEditor tceMapping = jtMapping.getCellEditor();
			if (tceMapping!=null) {
				tceMapping.stopCellEditing();
			}
			// --- Configure external types -----
			DefaultTableModel dtmMapping = this.getTableModel4Mapping();
			for(int row=0; row<dtmMapping.getRowCount(); row++){
				String extType = (String) dtmMapping.getValueAt(row, 0);
				String compo   = (String) dtmMapping.getValueAt(row, 1);
				
				TypeDescription typeDesc = this.externalTypes.get(extType);
				typeDesc.setMapToComponent(compo);
			}
			this.cancelled = false;
			
		} else if (ae.getSource() == this.getJButtonCancel()) {
			this.cancelled = true;
		}
		this.setVisible(false);
	}

	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
