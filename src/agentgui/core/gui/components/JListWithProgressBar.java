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

import agentgui.core.gui.ClassSelector;

/**
 * This JPanel inherits a JList and a progress bar in order to indicate
 * that the list content can be further filled by a search process.
 *
 * @see JListClassSearcher
 * @see ClassSelector
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JListWithProgressBar extends JPanel {

	private static final long serialVersionUID = 3535748006730839486L;

	public JList jListLoading = null;
	private JProgressBar jProgressBarLoading = null;
	private JScrollPane jScrollPane = null;
	
	/**
	 * These are the default constructors, which are similar
	 * with the jList constructors.
	 */
	public JListWithProgressBar() {
		super();
		jListLoading  = new JList();
		initialize();
	}
	
	/**
	 * Instantiates a new j list with progress bar.
	 *
	 * @param listModel the list model
	 */
	public JListWithProgressBar(ListModel listModel) {
		super();
		jListLoading  = new JList(listModel);
		initialize();
	}
	
	/**
	 * Instantiates a new j list with progress bar.
	 *
	 * @param listData the list data
	 */
	public JListWithProgressBar(Object[] listData) {
		super();
		jListLoading  = new JList(listData);
		initialize();
	}
	
	/**
	 * Instantiates a new j list with progress bar.
	 *
	 * @param listData the list data
	 */
	public JListWithProgressBar(Vector<?> listData) {
		super();
		jListLoading  = new JList(listData);
		initialize();
	}
	
	/**
	 * This method initializes this.
	 *
	 * @return void
	 */
	private void initialize() {
		
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(150, 75));
		this.add(getJScrollPane(), BorderLayout.CENTER);
		this.add(getJProgressBarLoading(), BorderLayout.SOUTH);
	}
	
	/**
	 * This method initializes jScrollPane.
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getjListLoading());
		}
		return jScrollPane;
	}
	
	/**
	 * Gets the j list loading.
	 *
	 * @return the j list loading
	 */
	private JList getjListLoading() {
		if (jListLoading==null) {
			jListLoading = new JList();
		}
		return jListLoading;
	}
	
	/**
	 * This method initializes jProgressBar.
	 *
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
	 * loading of the JList is currently running.
	 *
	 * @param busy the new busy
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
	 * From here we start with the copy of methods/functions comming from JList.
	 *
	 * @param listener the listener
	 */
	
	public void addListSelectionListener(ListSelectionListener listener) {
		jListLoading.addListSelectionListener(listener);		
	}
	
	/**
	 * Adds the selection interval.
	 *
	 * @param anchor the anchor
	 * @param lead the lead
	 */
	public void addSelectionInterval(int anchor, int lead) {
		jListLoading.addSelectionInterval(anchor, lead);		
	}
	
	/**
	 * Clear selection.
	 */
	public void clearSelection() {
		jListLoading.clearSelection();		
	}
	
	/**
	 * Ensure index is visible.
	 *
	 * @param index the index
	 */
	public void ensureIndexIsVisible(int index) {
		jListLoading.ensureIndexIsVisible(index);
	}
	
	/**
	 * Gets the anchor selection index.
	 *
	 * @return the anchor selection index
	 */
	public int getAnchorSelectionIndex() {
		return jListLoading.getAnchorSelectionIndex();
	}
	
	/**
	 * Gets the cell bounds.
	 *
	 * @param index0 the index0
	 * @param index1 the index1
	 * @return the cell bounds
	 */
	public Rectangle getCellBounds(int index0, int index1) {
		return jListLoading.getCellBounds(index0, index1);
	}
	
	/**
	 * Gets the cell renderer.
	 *
	 * @return the cell renderer
	 */
	public ListCellRenderer getCellRenderer() {
		return jListLoading.getCellRenderer();
	}
	
	/**
	 * Gets the drag enabled.
	 *
	 * @return the drag enabled
	 */
	public boolean getDragEnabled() {
		return jListLoading.getDragEnabled();
	}
	
	/**
	 * Gets the drop location.
	 *
	 * @return the drop location
	 */
	public DropLocation getDropLocation() {
		return jListLoading.getDropLocation();
	}
	
	/**
	 * Gets the drop mode.
	 *
	 * @return the drop mode
	 */
	public DropMode getDropMode() {
		return jListLoading.getDropMode();
	}
	
	/**
	 * Gets the first visible index.
	 *
	 * @return the first visible index
	 */
	public int getFirstVisibleIndex() {
		return jListLoading.getFirstVisibleIndex();
	}
	
	/**
	 * Gets the fixed cell height.
	 *
	 * @return the fixed cell height
	 */
	public int getFixedCellHeight() {
		return jListLoading.getFixedCellHeight();
	}
	
	/**
	 * Gets the fixed cell width.
	 *
	 * @return the fixed cell width
	 */
	public int getFixedCellWidth() {
		return jListLoading.getFixedCellWidth();
	}
	
	/**
	 * Gets the last visible index.
	 *
	 * @return the last visible index
	 */
	public int getLastVisibleIndex() {
		return jListLoading.getLastVisibleIndex();
	}
	
	/**
	 * Gets the layout orientation.
	 *
	 * @return the layout orientation
	 */
	public int getLayoutOrientation() {
		return jListLoading.getLayoutOrientation();
	}
	
	/**
	 * Gets the lead selection index.
	 *
	 * @return the lead selection index
	 */
	public int getLeadSelectionIndex() {
		return jListLoading.getLeadSelectionIndex();
	}
	
	/**
	 * Gets the list selection listeners.
	 *
	 * @return the list selection listeners
	 */
	public ListSelectionListener[] getListSelectionListeners() {
		return jListLoading.getListSelectionListeners();
	}
	
	/**
	 * Gets the max selection index.
	 *
	 * @return the max selection index
	 */
	public int getMaxSelectionIndex() {
		return jListLoading.getMaxSelectionIndex();
	}
	
	/**
	 * Gets the min selection index.
	 *
	 * @return the min selection index
	 */
	public int getMinSelectionIndex() {
		return jListLoading.getMinSelectionIndex();
	}
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public ListModel getModel() {
		return jListLoading.getModel();
	}
	
	/**
	 * Gets the next match.
	 *
	 * @param prefix the prefix
	 * @param startIndex the start index
	 * @param bias the bias
	 * @return the next match
	 */
	public int getNextMatch(String prefix, int startIndex, Bias bias) {
		return jListLoading.getNextMatch(prefix, startIndex, bias);
	}
	
	/**
	 * Gets the preferred scrollable viewport size.
	 *
	 * @return the preferred scrollable viewport size
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return jListLoading.getPreferredScrollableViewportSize();
	}
	
	/**
	 * Gets the prototype cell value.
	 *
	 * @return the prototype cell value
	 */
	public Object getPrototypeCellValue() {
		return jListLoading.getPrototypeCellValue();
	}
	
	/**
	 * Gets the scrollable block increment.
	 *
	 * @param visibleRect the visible rect
	 * @param orientation the orientation
	 * @param direction the direction
	 * @return the scrollable block increment
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return jListLoading.getScrollableBlockIncrement(visibleRect, orientation, direction);
	}
	
	/**
	 * Gets the scrollable tracks viewport height.
	 *
	 * @return the scrollable tracks viewport height
	 */
	public boolean getScrollableTracksViewportHeight() {
		return jListLoading.getScrollableTracksViewportHeight();
	}
	
	/**
	 * Gets the scrollable tracks viewport width.
	 *
	 * @return the scrollable tracks viewport width
	 */
	public boolean getScrollableTracksViewportWidth() {
		return jListLoading.getScrollableTracksViewportWidth();
	}
	
	/**
	 * Gets the scrollable unit increment.
	 *
	 * @param visibleRect the visible rect
	 * @param orientation the orientation
	 * @param direction the direction
	 * @return the scrollable unit increment
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return jListLoading.getScrollableUnitIncrement(visibleRect, orientation, direction);
	}
	
	/**
	 * Gets the selected index.
	 *
	 * @return the selected index
	 */
	public int getSelectedIndex() {
		return jListLoading.getSelectedIndex();
	}
	
	/**
	 * Gets the selected indices.
	 *
	 * @return the selected indices
	 */
	public int[] getSelectedIndices() {
		return jListLoading.getSelectedIndices();
	}
	
	/**
	 * Gets the selected value.
	 *
	 * @return the selected value
	 */
	public Object getSelectedValue() {
		return jListLoading.getSelectedValue();
	}
	
	/**
	 * Gets the selected values.
	 *
	 * @return the selected values
	 */
	public Object[] getSelectedValues() {
		return jListLoading.getSelectedValues();
	}
	
	/**
	 * Gets the selection background.
	 *
	 * @return the selection background
	 */
	public Color getSelectionBackground() {
		return jListLoading.getSelectionBackground();
	}
	
	/**
	 * Gets the selection foreground.
	 *
	 * @return the selection foreground
	 */
	public Color getSelectionForeground() {
		return jListLoading.getSelectionForeground();
	}
	
	/**
	 * Gets the selection mode.
	 *
	 * @return the selection mode
	 */
	public int getSelectionMode() {
		return jListLoading.getSelectionMode();
	}
	
	/**
	 * Gets the selection model.
	 *
	 * @return the selection model
	 */
	public ListSelectionModel getSelectionModel() {
		return jListLoading.getSelectionModel();
	}
	
	/**
	 * Gets the value is adjusting.
	 *
	 * @return the value is adjusting
	 */
	public boolean getValueIsAdjusting() {
		return jListLoading.getValueIsAdjusting();
	}
	
	/**
	 * Gets the visible row count.
	 *
	 * @return the visible row count
	 */
	public int getVisibleRowCount() {
		return jListLoading.getVisibleRowCount();
	}
	
	/**
	 * Index to location.
	 *
	 * @param index the index
	 * @return the point
	 */
	public Point indexToLocation(int index) {
		return jListLoading.indexToLocation(index);
	}
	
	/**
	 * Checks if is selected index.
	 *
	 * @param index the index
	 * @return true, if is selected index
	 */
	public boolean isSelectedIndex(int index) {
		return jListLoading.isSelectedIndex(index);
	}
	
	/**
	 * Checks if is selection empty.
	 *
	 * @return true, if is selection empty
	 */
	public boolean isSelectionEmpty() {
		return jListLoading.isSelectionEmpty();
	}
	
	/**
	 * Location to index.
	 *
	 * @param location the location
	 * @return the int
	 */
	public int locationToIndex(Point location) {
		return jListLoading.locationToIndex(location);
	}
	
	/**
	 * Removes the list selection listener.
	 *
	 * @param listener the listener
	 */
	public void removeListSelectionListener(ListSelectionListener listener) {
		jListLoading.removeListSelectionListener(listener);
	}
	
	/**
	 * Removes the selection interval.
	 *
	 * @param index0 the index0
	 * @param index1 the index1
	 */
	public void removeSelectionInterval(int index0, int index1) {
		jListLoading.removeSelectionInterval(index0, index1);
	}
	
	/**
	 * Sets the cell renderer.
	 *
	 * @param cellRenderer the new cell renderer
	 */
	public void setCellRenderer(ListCellRenderer cellRenderer) {
		jListLoading.setCellRenderer(cellRenderer);
	}
	
	/**
	 * Sets the drag enabled.
	 *
	 * @param b the new drag enabled
	 */
	public void setDragEnabled(boolean b) {
		jListLoading.setDragEnabled(b);		
	}
	
	/**
	 * Sets the drop mode.
	 *
	 * @param dropMode the new drop mode
	 */
	public void setDropMode(DropMode dropMode) {
		jListLoading.setDropMode(dropMode);
	}
	
	/**
	 * Sets the fixed cell height.
	 *
	 * @param height the new fixed cell height
	 */
	public void setFixedCellHeight(int height) {
		jListLoading.setFixedCellHeight(height);
	}
	
	/**
	 * Sets the fixed cell width.
	 *
	 * @param width the new fixed cell width
	 */
	public void setFixedCellWidth(int width) {
		jListLoading.setFixedCellWidth(width);
	}
	
	/**
	 * Sets the layout orientation.
	 *
	 * @param layoutOrientation the new layout orientation
	 */
	public void setLayoutOrientation(int layoutOrientation) {
		jListLoading.setLayoutOrientation(layoutOrientation);
	}
	
	/**
	 * Sets the list data.
	 *
	 * @param listData the new list data
	 */
	public void setListData(Object[] listData) {
		jListLoading.setListData(listData);
	}
	
	/**
	 * Sets the list data.
	 *
	 * @param listData the new list data
	 */
	public void setListData(Vector<?> listData) {
		jListLoading.setListData(listData);		
	}
	
	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(ListModel model) {
		jListLoading.setModel(model);		
	}
	
	/**
	 * Sets the prototype cell value.
	 *
	 * @param prototypeCellValue the new prototype cell value
	 */
	public void setPrototypeCellValue(Object prototypeCellValue) {
		jListLoading.setPrototypeCellValue(prototypeCellValue);
	}
	
	/**
	 * Sets the selected index.
	 *
	 * @param index the new selected index
	 */
	public void setSelectedIndex(int index) {
		jListLoading.setSelectedIndex(index);		
	}
	
	/**
	 * Sets the selected indices.
	 *
	 * @param indices the new selected indices
	 */
	public void setSelectedIndices(int[] indices) {
		jListLoading.setSelectedIndices(indices);		
	}
	
	/**
	 * Sets the selected value.
	 *
	 * @param anObject the an object
	 * @param shouldScroll the should scroll
	 */
	public void setSelectedValue(Object anObject, boolean shouldScroll) {
		jListLoading.setSelectedValue(anObject, shouldScroll);
	}
	
	/**
	 * Sets the selection background.
	 *
	 * @param selectionBackground the new selection background
	 */
	public void setSelectionBackground(Color selectionBackground) {
		jListLoading.setSelectionBackground(selectionBackground);
	}
	
	/**
	 * Sets the selection foreground.
	 *
	 * @param selectionForeground the new selection foreground
	 */
	public void setSelectionForeground(Color selectionForeground) {
		jListLoading.setSelectionForeground(selectionForeground);
	}
	
	/**
	 * Sets the selection interval.
	 *
	 * @param anchor the anchor
	 * @param lead the lead
	 */
	public void setSelectionInterval(int anchor, int lead) {
		jListLoading.setSelectionInterval(anchor, lead);		
	}
	
	/**
	 * Sets the selection mode.
	 *
	 * @param selectionMode the new selection mode
	 */
	public void setSelectionMode(int selectionMode) {
		jListLoading.setSelectionMode(selectionMode);		
	}
	
	/**
	 * Sets the selection model.
	 *
	 * @param selectionModel the new selection model
	 */
	public void setSelectionModel(ListSelectionModel selectionModel) {
		jListLoading.setSelectionModel(selectionModel);
	}
	
	/**
	 * Sets the uI.
	 *
	 * @param ui the new uI
	 */
	public void setUI(ListUI ui) {
		jListLoading.setUI(ui);
	}
	
	/**
	 * Sets the value is adjusting.
	 *
	 * @param b the new value is adjusting
	 */
	public void setValueIsAdjusting(boolean b) {
		jListLoading.setValueIsAdjusting(b);
	}
	
	/**
	 * Sets the visible row count.
	 *
	 * @param visibleRowCount the new visible row count
	 */
	public void setVisibleRowCount(int visibleRowCount) {
		jListLoading.setVisibleRowCount(visibleRowCount);		
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
