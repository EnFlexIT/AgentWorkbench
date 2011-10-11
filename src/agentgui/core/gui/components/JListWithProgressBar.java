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
package agentgui.core.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JList.DropLocation;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ListUI;
import javax.swing.text.Position.Bias;


public class JListWithProgressBar extends JPanel {

	private static final long serialVersionUID = 1L;
	public JList jListLoading = null;
	private JProgressBar jProgressBarLoading = null;
	private JScrollPane jScrollPane = null;
	
	/**
	 * These are the default constructors, which are similar 
	 * with the jList constructors
	 */
	public JListWithProgressBar() {
		super();
		jListLoading  = new JList();
		initialize();
	}
	public JListWithProgressBar(ListModel listModel) {
		super();
		jListLoading  = new JList(listModel);
		initialize();
	}
	public JListWithProgressBar(Object[] listData) {
		super();
		jListLoading  = new JList(listData);
		initialize();
	}
	public JListWithProgressBar(Vector<?> listData) {
		super();
		jListLoading  = new JList(listData);
		initialize();
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(150, 75));
		this.add(getJScrollPane(), BorderLayout.CENTER);
		this.add(getJProgressBarLoading(), BorderLayout.SOUTH);
	}
	
	/**
	 * This method initializes jScrollPane	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getjListLoading());
		}
		return jScrollPane;
	}
	private JList getjListLoading() {
		if (jListLoading==null) {
			jListLoading = new JList();
		}
		return jListLoading;
	}
	
	/**
	 * This method initializes jProgressBar	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBarLoading() {
		if (jProgressBarLoading == null) {
			jProgressBarLoading = new JProgressBar();
			jProgressBarLoading.setVisible(false);
			jProgressBarLoading.setPreferredSize(new Dimension(100, 15));
			jProgressBarLoading.setIndeterminate(true);
		}
		return jProgressBarLoading;
	}
	
	/**
	 * shows/hides the progress bar, to indicate, that the 
	 * loading of the JList is currently running
	 * @param busy
	 */
	public void setBusy(boolean busy) {
		if (busy==true) {
			jProgressBarLoading.setVisible(true);
		} else {
			jProgressBarLoading.setVisible(false);
		}
	}
	
	// ----------------------------------------------------------------------------------
	// ---- From here we start with the copy of methods/functions comming from JList ----
	// ----------------------------------------------------------------------------------
	/**
	 * From here we start with the copy of methods/functions comming from JList 
	 */
	
	public void addListSelectionListener(ListSelectionListener listener) {
		jListLoading.addListSelectionListener(listener);		
	}
	public void addSelectionInterval(int anchor, int lead) {
		jListLoading.addSelectionInterval(anchor, lead);		
	}
	public void clearSelection() {
		jListLoading.clearSelection();		
	}
	public void ensureIndexIsVisible(int index) {
		jListLoading.ensureIndexIsVisible(index);
	}
	public int getAnchorSelectionIndex() {
		return jListLoading.getAnchorSelectionIndex();
	}
	public Rectangle getCellBounds(int index0, int index1) {
		return jListLoading.getCellBounds(index0, index1);
	}
	public ListCellRenderer getCellRenderer() {
		return jListLoading.getCellRenderer();
	}
	public boolean getDragEnabled() {
		return jListLoading.getDragEnabled();
	}
	public DropLocation getDropLocation() {
		return jListLoading.getDropLocation();
	}
	public DropMode getDropMode() {
		return jListLoading.getDropMode();
	}
	public int getFirstVisibleIndex() {
		return jListLoading.getFirstVisibleIndex();
	}
	public int getFixedCellHeight() {
		return jListLoading.getFixedCellHeight();
	}
	public int getFixedCellWidth() {
		return jListLoading.getFixedCellWidth();
	}
	public int getLastVisibleIndex() {
		return jListLoading.getLastVisibleIndex();
	}
	public int getLayoutOrientation() {
		return jListLoading.getLayoutOrientation();
	}
	public int getLeadSelectionIndex() {
		return jListLoading.getLeadSelectionIndex();
	}
	public ListSelectionListener[] getListSelectionListeners() {
		return jListLoading.getListSelectionListeners();
	}
	public int getMaxSelectionIndex() {
		return jListLoading.getMaxSelectionIndex();
	}
	public int getMinSelectionIndex() {
		return jListLoading.getMinSelectionIndex();
	}
	public ListModel getModel() {
		return jListLoading.getModel();
	}
	public int getNextMatch(String prefix, int startIndex, Bias bias) {
		return jListLoading.getNextMatch(prefix, startIndex, bias);
	}
	public Dimension getPreferredScrollableViewportSize() {
		return jListLoading.getPreferredScrollableViewportSize();
	}
	public Object getPrototypeCellValue() {
		return jListLoading.getPrototypeCellValue();
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return jListLoading.getScrollableBlockIncrement(visibleRect, orientation, direction);
	}
	public boolean getScrollableTracksViewportHeight() {
		return jListLoading.getScrollableTracksViewportHeight();
	}
	public boolean getScrollableTracksViewportWidth() {
		return jListLoading.getScrollableTracksViewportWidth();
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return jListLoading.getScrollableUnitIncrement(visibleRect, orientation, direction);
	}
	public int getSelectedIndex() {
		return jListLoading.getSelectedIndex();
	}
	public int[] getSelectedIndices() {
		return jListLoading.getSelectedIndices();
	}
	public Object getSelectedValue() {
		return jListLoading.getSelectedValue();
	}
	public Object[] getSelectedValues() {
		return jListLoading.getSelectedValues();
	}
	public Color getSelectionBackground() {
		return jListLoading.getSelectionBackground();
	}
	public Color getSelectionForeground() {
		return jListLoading.getSelectionForeground();
	}
	public int getSelectionMode() {
		return jListLoading.getSelectionMode();
	}
	public ListSelectionModel getSelectionModel() {
		return jListLoading.getSelectionModel();
	}
	public boolean getValueIsAdjusting() {
		return jListLoading.getValueIsAdjusting();
	}
	public int getVisibleRowCount() {
		return jListLoading.getVisibleRowCount();
	}
	public Point indexToLocation(int index) {
		return jListLoading.indexToLocation(index);
	}
	public boolean isSelectedIndex(int index) {
		return jListLoading.isSelectedIndex(index);
	}
	public boolean isSelectionEmpty() {
		return jListLoading.isSelectionEmpty();
	}
	public int locationToIndex(Point location) {
		return jListLoading.locationToIndex(location);
	}
	public void removeListSelectionListener(ListSelectionListener listener) {
		jListLoading.removeListSelectionListener(listener);
	}
	public void removeSelectionInterval(int index0, int index1) {
		jListLoading.removeSelectionInterval(index0, index1);
	}
	public void setCellRenderer(ListCellRenderer cellRenderer) {
		jListLoading.setCellRenderer(cellRenderer);
	}
	public void setDragEnabled(boolean b) {
		jListLoading.setDragEnabled(b);		
	}
	public void setDropMode(DropMode dropMode) {
		jListLoading.setDropMode(dropMode);
	}
	public void setFixedCellHeight(int height) {
		jListLoading.setFixedCellHeight(height);
	}
	public void setFixedCellWidth(int width) {
		jListLoading.setFixedCellWidth(width);
	}
	public void setLayoutOrientation(int layoutOrientation) {
		jListLoading.setLayoutOrientation(layoutOrientation);
	}
	public void setListData(Object[] listData) {
		jListLoading.setListData(listData);
	}
	public void setListData(Vector<?> listData) {
		jListLoading.setListData(listData);		
	}
	public void setModel(ListModel model) {
		jListLoading.setModel(model);		
	}
	public void setPrototypeCellValue(Object prototypeCellValue) {
		jListLoading.setPrototypeCellValue(prototypeCellValue);
	}
	public void setSelectedIndex(int index) {
		jListLoading.setSelectedIndex(index);		
	}
	public void setSelectedIndices(int[] indices) {
		jListLoading.setSelectedIndices(indices);		
	}
	public void setSelectedValue(Object anObject, boolean shouldScroll) {
		jListLoading.setSelectedValue(anObject, shouldScroll);
	}
	public void setSelectionBackground(Color selectionBackground) {
		jListLoading.setSelectionBackground(selectionBackground);
	}
	public void setSelectionForeground(Color selectionForeground) {
		jListLoading.setSelectionForeground(selectionForeground);
	}
	public void setSelectionInterval(int anchor, int lead) {
		jListLoading.setSelectionInterval(anchor, lead);		
	}
	public void setSelectionMode(int selectionMode) {
		jListLoading.setSelectionMode(selectionMode);		
	}
	public void setSelectionModel(ListSelectionModel selectionModel) {
		jListLoading.setSelectionModel(selectionModel);
	}
	public void setUI(ListUI ui) {
		jListLoading.setUI(ui);
	}
	public void setValueIsAdjusting(boolean b) {
		jListLoading.setValueIsAdjusting(b);
	}
	public void setVisibleRowCount(int visibleRowCount) {
		jListLoading.setVisibleRowCount(visibleRowCount);		
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
