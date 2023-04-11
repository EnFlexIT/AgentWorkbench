package de.enflexit.db.hibernate.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import de.enflexit.common.swing.TableCellColorHelper;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.SessionFactoryMonitor;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;

/**
 * The Class DatabaseSelectionPanel.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseSelectionPanel extends JPanel implements HibernateStateVisualizationService {

	private static final long serialVersionUID = 1345040443863386576L;

	private List<String> factoryIDsSelected;
	
	private JScrollPane scrollPane;
	private DefaultTableModel tableModelFactorySelection;
	private JTable jTableFactorySelection;
	private DefaultTableCellRenderer hibernateStateTableCellRecnderer;
	
	private boolean isInformActionListener = true;
	private List<ActionListener> actionListeners;
	private String actionCommand;
	
	/**
	 * Instantiates a new database selection panel.
	 */
	public DatabaseSelectionPanel() {
		this(null);
	}
	/**
	 * Instantiates a new database selection panel.
	 * @param selFactoryIDs the selected factory IDs
	 */
	public DatabaseSelectionPanel(List<String> selFactoryIDs) {
		this.initialize();
		this.setFactoryIDsSelected(selFactoryIDs);
	}
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		this.add(this.getScrollPane(), BorderLayout.CENTER);
		HibernateStateVisualizer.registerStateVisualizationService(this);
	}
	
	/**
	 * Dispose.
	 */
	public void dispose() {
		HibernateStateVisualizer.unregisterStateVisualizationService(this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.getHibernateStateTableCellRenderer().setEnabled(enabled);
		this.getJTableFactorySelection().setEnabled(enabled);
		this.getJTableFactorySelection().setRowSelectionAllowed(enabled);
	}
	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(this.getJTableFactorySelection());
		}
		return scrollPane;
	}
	
	private DefaultTableModel getTableModelFactorySelection() {
		if (tableModelFactorySelection == null) {
			
			Vector<String> colHeader = new Vector<>();
			colHeader.add("State");
			colHeader.add("Factory-ID");
			colHeader.add("Selected");
			
			tableModelFactorySelection = new DefaultTableModel(null, colHeader) {
				private static final long serialVersionUID = 5048176790905350653L;
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column==2) {
						return true;
					}
					return false;
				}
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					if (columnIndex==0) {
						return SessionFactoryMonitor.class;
					} else if (columnIndex==2) {
						return Boolean.class;
					}
					return String.class;
				}
			};
			
			tableModelFactorySelection.addTableModelListener(new TableModelListener() {
				@Override
				public void tableChanged(TableModelEvent tmEv) {
					if (DatabaseSelectionPanel.this.isInformActionListener==false) return;
					DatabaseSelectionPanel.this.informActionListener(new ActionEvent(DatabaseSelectionPanel.this, 1, DatabaseSelectionPanel.this.getActionCommand()));
				}
			});
		}
		return tableModelFactorySelection;
	}
	private JTable getJTableFactorySelection() {
		if (jTableFactorySelection == null) {
			jTableFactorySelection = new JTable(this.getTableModelFactorySelection());
			jTableFactorySelection.setFillsViewportHeight(true);
			jTableFactorySelection.setShowGrid(false);
			jTableFactorySelection.setRowHeight(20);
			jTableFactorySelection.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableFactorySelection.setAutoCreateRowSorter(true);
			jTableFactorySelection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableFactorySelection.getTableHeader().setReorderingAllowed(false);
			
			TableColumnModel tcm = jTableFactorySelection.getColumnModel();
			tcm.getColumn(0).setMinWidth(40);
			tcm.getColumn(0).setMaxWidth(40);
			tcm.getColumn(0).setCellRenderer(this.getHibernateStateTableCellRenderer());
			
			tcm.getColumn(1).setCellRenderer(this.getHibernateStateTableCellRenderer());
			
			tcm.getColumn(2).setMinWidth(60);
			tcm.getColumn(2).setMaxWidth(60);
			tcm.getColumn(2).setCellRenderer(this.getHibernateStateTableCellRenderer());
			
			jTableFactorySelection.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent me) {
					if (me.getClickCount()==2 && SwingUtilities.isLeftMouseButton(me)==true) {
						int rowSelected = jTableFactorySelection.rowAtPoint(me.getPoint());
						String factoryID = (String) jTableFactorySelection.getValueAt(rowSelected, 1);
						DatabaseSelectionPanel.this.showDatabaseDialog(factoryID);
					}
				}
			});
			
		}
		return jTableFactorySelection;
	}
	/**
	 * Returns the hibernate state table cell renderer.
	 * @return the hibernate state table cell renderer
	 */
	private DefaultTableCellRenderer getHibernateStateTableCellRenderer() {
		if (hibernateStateTableCellRecnderer==null) {
			hibernateStateTableCellRecnderer = new DatabaseSelectionCellRenderer();
		}
		return hibernateStateTableCellRecnderer;
	}
	
	/**
	 * Fills the table.
	 */
	private void fillTable() {
		this.isInformActionListener = false;
		this.getTableModelFactorySelection().setRowCount(0);
		for (String factotyID : HibernateUtilities.getSessionFactoryIDList()) {
			this.addRow(factotyID);
		}
		this.isInformActionListener = true;
	}
	/**
	 * Adds a row to the table.
	 * @param factoryID the factory ID
	 */
	private void addRow(String factoryID) {
		
		Vector<Object> row = new Vector<>();
		row.add(HibernateUtilities.getSessionFactoryMonitor(factoryID));
		row.add(factoryID);
		row.add(this.factoryIDsSelected.contains(factoryID));
		this.getTableModelFactorySelection().addRow(row);
	}
	
	/**
	 * Sets the selected factory ID's .
	 * @param factoryIDsSelected the new factory ID's selected
	 */
	public void setFactoryIDsSelected(List<String> factoryIDsSelected) {
		this.factoryIDsSelected = factoryIDsSelected;
		if (this.factoryIDsSelected==null) {
			this.factoryIDsSelected = new ArrayList<>();
		}
		this.fillTable();
	}
	/**
	 * Returns the currently selected Factory ID's.
	 * @return the factory ID's selected
	 */
	public List<String> getFactoryIDsSelected() {
		List<String> currSelection = new ArrayList<>();
		for (int i = 0; i < this.getTableModelFactorySelection().getRowCount(); i++) {
			if ((boolean)this.getTableModelFactorySelection().getValueAt(i, 2)==true){
				currSelection.add((String)this.getTableModelFactorySelection().getValueAt(i, 1));
			}
		}
		return currSelection;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.gui.HibernateStateVisualizationService#setSessionFactoryState(java.lang.String, de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState)
	 */
	@Override
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState) {
		this.getJTableFactorySelection().repaint();
	}

	/**
	 * Show database dialog for the specified session factory.
	 * @param factoryID the factory ID
	 */
	private void showDatabaseDialog(String factoryID) {
		
		Window parentWindow = null;
		Container parentContainer = this.getParent();
		while (parentContainer!=null) {
			if (parentContainer instanceof Window) {
				parentWindow = (Window) parentContainer;
				break;
			}
			parentContainer = parentContainer.getParent();
		}

		AwbDatabaseDialog awbDatabaseDialog = new AwbDatabaseDialog(parentWindow, factoryID);
		awbDatabaseDialog.setVisible(true);
		// - - - Wait for user - - - - - - - - -  
		awbDatabaseDialog.dispose();
		awbDatabaseDialog = null;
		
	}
	
	
	/**
	 * The Class DatabaseSelectionCellRenderer is used in the table of the above class.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private class DatabaseSelectionCellRenderer extends DefaultTableCellRenderer {
		
		private static final long serialVersionUID = 7795617116989025282L;
		
		private boolean enabled = true;
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#setEnabled(boolean)
		 */
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
			super.setEnabled(enabled);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			
			JComponent displayComponent = null;
			
			JLabel jLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			switch (column) {
			case 0:
				SessionFactoryMonitor sfm = (SessionFactoryMonitor) value;
				jLabel.setText(null);
				jLabel.setIcon(sfm.getSessionFactoryState().getIconImage());
				jLabel.setToolTipText(sfm.getSessionFactoryState().getDescription());
				displayComponent = jLabel;
				break;

			case 1:
				jLabel.setIcon(null);
				jLabel.setToolTipText(null);
				displayComponent = jLabel;
				break;
				
			case 2:
				JCheckBox jCheckBox = new JCheckBox();
				jCheckBox.setSelected((boolean)value);
				jCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
				jCheckBox.setOpaque(true);
				displayComponent = jCheckBox;
				break;
			}
			
			displayComponent.setEnabled(this.enabled);
			TableCellColorHelper.setTableCellRendererColors(displayComponent, row, isSelected);
			return displayComponent;
		}
		
	}
	
	// --------------------------------------------------------------
	// --- From here, handling of registered ActionListener ---------
	// --------------------------------------------------------------	
	private List<ActionListener> getActionListeners() {
		if (actionListeners==null) {
			actionListeners = new ArrayList<>();
		}
		return actionListeners;
	}
	private void informActionListener(ActionEvent ae) {
		ae.setSource(this);
		for (ActionListener al : this.getActionListeners()) {
			al.actionPerformed(ae);
		}
	}
	/**
	 * Adds the action listener.
	 * @param listener the listener to add
	 */
	public void addActionListener(ActionListener listener) {
		if (listener!=null && this.getActionListeners().contains(listener)==false) {
			this.getActionListeners().add(listener);
		}
	}
	/**
	 * Removes the action listener.
	 * @param listener the listener to remove
	 */
	public void removeActionListener(ActionListener listener) {
		if (listener!=null && this.getActionListeners().contains(listener)==true) {
			this.getActionListeners().remove(listener);
		}
	}
	
	/**
	 * Sets the action command for this panel.
	 * @param actionCommand the new action command
	 */
	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}
	/**
	 * Returns the action command for this panel.
	 * @return the action command
	 */
	public String getActionCommand() {
		return actionCommand;
	}
	
}
