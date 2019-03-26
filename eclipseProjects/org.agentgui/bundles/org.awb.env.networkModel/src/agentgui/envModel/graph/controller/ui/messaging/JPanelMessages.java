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
package agentgui.envModel.graph.controller.ui.messaging;

import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import agentgui.core.config.GlobalInfo;
import agentgui.envModel.graph.controller.ui.messaging.GraphUIMessage.GraphUIMessageType;
import de.enflexit.common.swing.TableCellColorHelper;

/**
 * The Class JPanelMessages represents the visualization for state indications.
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class JPanelMessages extends JScrollPane {

	private static final long serialVersionUID = 1110726364983505331L;

	private final ImageIcon iconInfo 	= GlobalInfo.getInternalImageIcon("StateInformation.png");
	private final ImageIcon iconWarning = GlobalInfo.getInternalImageIcon("StateWarning.png");
	private final ImageIcon iconError 	= GlobalInfo.getInternalImageIcon("StateError.png");

	private final int maxRowCount = 150;
	
	private DefaultTableModel tableModel;
	private JTable jTableMessages;
	
	
	/**
	 * Instantiates a new messaging state panel.
	 */
	public JPanelMessages() {
		this.initialize();
	}
	/**
	 * Initializes the panel.
	 */
	private void initialize() {
		this.setBackground(MessagingJInternalFrame.bgColor);
		this.setViewportView(this.getJTableMessages());
	}
	
	/**
	 * Returns the JTable for messages.
	 * @return the JTable messages
	 */
	private JTable getJTableMessages() {
		if (jTableMessages == null) {
			jTableMessages = new JTable(this.getTableModel());
			jTableMessages.setFillsViewportHeight(true);
			jTableMessages.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableMessages.setBackground(MessagingJInternalFrame.bgColor);
			jTableMessages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			jTableMessages.getTableHeader().setReorderingAllowed(false);
			
			TableColumn column = null;
			
			// ----------------------------------------------------------------
			// --- Column index 0 ---------------------------------------------
			column = jTableMessages.getColumnModel().getColumn(0); 
			column.setMinWidth(30);
			column.setMaxWidth(30);
			column.setCellRenderer(new TableCellRenderer() {
				/* (non-Javadoc)
				 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
				 */
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					
					JLabel display = new JLabel();
					if (value!=null && value instanceof GraphUIMessageType) {
						GraphUIMessageType msgType = (GraphUIMessageType) value;
						switch (msgType) {
						case Information:
							display.setIcon(iconInfo);
							break;
						case Warning:
							display.setIcon(iconWarning);
							break;
						case Error:
							display.setIcon(iconError);
							break;
						}
						display.setHorizontalAlignment(SwingConstants.CENTER);
						
					} else {
						display.setText("?" + value.toString());
					}
					TableCellColorHelper.setTableCellRendererColors(display, row, isSelected, jTableMessages.getBackground());
					return display;
				}
			});
			
			// ----------------------------------------------------------------
			// --- Column index 1 ---------------------------------------------
			column = jTableMessages.getColumnModel().getColumn(1);
			column.setMinWidth(120);
			column.setMaxWidth(120);
			column.setCellRenderer(new TableCellRenderer() {
				/* (non-Javadoc)
				 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
				 */
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					
					JLabel display = new JLabel();
					if (value!=null && value instanceof Long) {
						Date date = new Date((long) value);
						String dateString = new SimpleDateFormat("dd.MM.YY - HH:mm:ss").format(date);
						display.setText(dateString);
					} else {
						display.setText("?" + value.toString());
					}
					TableCellColorHelper.setTableCellRendererColors(display, row, isSelected, jTableMessages.getBackground());
					return display;
				}
			});
			
			// ----------------------------------------------------------------
			// --- Column index 2 ---------------------------------------------
			column = jTableMessages.getColumnModel().getColumn(2);
			column.setCellRenderer(new TableCellRenderer() {
				/* (non-Javadoc)
				 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
				 */
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					JLabel display = new JLabel(value.toString());
					TableCellColorHelper.setTableCellRendererColors(display, row, isSelected, jTableMessages.getBackground());
					return display;
				}
			});
			
			// --- Define the row sorter --------------------------------------
			jTableMessages.setAutoCreateRowSorter(true);
			// --- Toggle twice to have a descending time order ---------------
			jTableMessages.getRowSorter().toggleSortOrder(1);
			jTableMessages.getRowSorter().toggleSortOrder(1);
			
		}
		return jTableMessages;
	}
	
	/**
	 * Gets the table model.
	 * @return the table model
	 */
	private DefaultTableModel getTableModel() {
		if (this.tableModel==null) {
			Vector<String> header = new Vector<>();
			header.add("");
			header.add("Time");
			header.add("Message");
			tableModel = new DefaultTableModel(null, header) {
				private static final long serialVersionUID = -7758604994401313895L;
				/* (non-Javadoc)
				 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
				 */
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column<=1) return false;
					return super.isCellEditable(row, column);
				}
			};
		}
		return tableModel;
	}
	
	/**
	 * Adds the specified GraphUIMessage to the table of messages .
	 * @param graphUiMessage the GraphUIMessage to add
	 */
	public void addMessage(GraphUIMessage graphUiMessage) {
		this.addMessage(graphUiMessage.getTimeStamp(), graphUiMessage.getMessageType(), graphUiMessage.getMessage());
	}
	/**
	 * Adds the specified message to the table of messages.
	 *
	 * @param timeStamp the time stamp of the message
	 * @param messageType the message type
	 * @param message the message itself
	 */
	public void addMessage(long timeStamp, GraphUIMessageType messageType, String message) {
		
		Vector<Object> row = new Vector<>();
		row.add(messageType);
		row.add(timeStamp);
		row.add(message);

		this.getTableModel().addRow(row);
		if (this.getTableModel().getRowCount()>this.maxRowCount) {
			this.getTableModel().removeRow(0);
		}
	}
	
}
