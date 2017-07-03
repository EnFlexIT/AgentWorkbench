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
import java.util.List;
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

/**
 * This JPanel contains a JList and a progress bar in order to indicate
 * that the list content can further be  filled by a search process.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JListWithProgressBar<E> extends JPanel {

	private static final long serialVersionUID = 3535748006730839486L;

	public JList<E> jListLoading;
	private JProgressBar jProgressBarLoading;
	private JScrollPane jScrollPane;
	
	
	/**
	 * These are the default constructors, which are similar
	 * with the jList constructors.
	 */
	public JListWithProgressBar() {
		super();
		initialize();
	}
	/**
	 * Instantiates a new j list with progress bar.
	 * @param listModel the list model
	 */
	public JListWithProgressBar(ListModel<E> listModel) {
		super();
		this.setJListLoading(new JList<E>(listModel));
		this.initialize();
	}
	/**
	 * Instantiates a new j list with progress bar.
	 * @param listData the list data
	 */
	public JListWithProgressBar(E[] listData) {
		super();
		this.setJListLoading(new JList<E>(listData));
		this.initialize();
	}
	/**
	 * Instantiates a new j list with progress bar.
	 * @param listData the list data
	 */
	public JListWithProgressBar(Vector<E> listData) {
		super();
		this.setJListLoading(new JList<E>(listData));
		this.initialize();
	}
	/**
	 * Initializes this panel.
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(150, 75));
		this.add(this.getJScrollPane(), BorderLayout.CENTER);
		this.add(this.getJProgressBarLoading(), BorderLayout.SOUTH);
	}
	
	/**
	 * This method initializes jScrollPane.
	 * @return the j scroll pane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(this.getJListLoading());
		}
		return jScrollPane;
	}
	/**
	 * Gets the j list loading.
	 * @return the j list loading
	 */
	private JList<E> getJListLoading() {
		if (jListLoading==null) {
			jListLoading = new JList<E>();
		}
		return jListLoading;
	}
	/**
	 * Sets the j list loading.
	 * @param newJList the new j list loading
	 */
	private void setJListLoading(JList<E> newJList) {
		jListLoading = newJList;
	}
	
	/**
	 * This method initializes jProgressBar.
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
		if (busy!=this.getJProgressBarLoading().isVisible()) {
			this.getJProgressBarLoading().setVisible(busy);
			this.validate();
			this.repaint();
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
		this.getJListLoading().addListSelectionListener(listener);		
	}
	
	/**
	 * Adds the selection interval.
	 *
	 * @param anchor the anchor
	 * @param lead the lead
	 */
	public void addSelectionInterval(int anchor, int lead) {
		this.getJListLoading().addSelectionInterval(anchor, lead);		
	}
	
	/**
	 * Clear selection.
	 */
	public void clearSelection() {
		this.getJListLoading().clearSelection();		
	}
	
	/**
	 * Ensure index is visible.
	 *
	 * @param index the index
	 */
	public void ensureIndexIsVisible(int index) {
		this.getJListLoading().ensureIndexIsVisible(index);
	}
	
	/**
	 * Gets the anchor selection index.
	 *
	 * @return the anchor selection index
	 */
	public int getAnchorSelectionIndex() {
		return this.getJListLoading().getAnchorSelectionIndex();
	}
	
	/**
	 * Gets the cell bounds.
	 *
	 * @param index0 the index0
	 * @param index1 the index1
	 * @return the cell bounds
	 */
	public Rectangle getCellBounds(int index0, int index1) {
		return this.getJListLoading().getCellBounds(index0, index1);
	}
	
	/**
	 * Gets the cell renderer.
	 * @return the cell renderer
	 */
	public ListCellRenderer<? super E> getCellRenderer() {
		return this.getJListLoading().getCellRenderer();
	}
	
	/**
	 * Gets the drag enabled.
	 * @return the drag enabled
	 */
	public boolean getDragEnabled() {
		return this.getJListLoading().getDragEnabled();
	}
	
	/**
	 * Gets the drop location.
	 * @return the drop location
	 */
	public DropLocation getDropLocation() {
		return this.getJListLoading().getDropLocation();
	}
	
	/**
	 * Gets the drop mode.
	 * @return the drop mode
	 */
	public DropMode getDropMode() {
		return this.getJListLoading().getDropMode();
	}
	
	/**
	 * Gets the first visible index.
	 * @return the first visible index
	 */
	public int getFirstVisibleIndex() {
		return this.getJListLoading().getFirstVisibleIndex();
	}
	
	/**
	 * Gets the fixed cell height.
	 * @return the fixed cell height
	 */
	public int getFixedCellHeight() {
		return this.getJListLoading().getFixedCellHeight();
	}
	
	/**
	 * Gets the fixed cell width.
	 * @return the fixed cell width
	 */
	public int getFixedCellWidth() {
		return this.getJListLoading().getFixedCellWidth();
	}
	
	/**
	 * Gets the last visible index.
	 * @return the last visible index
	 */
	public int getLastVisibleIndex() {
		return this.getJListLoading().getLastVisibleIndex();
	}
	
	/**
	 * Gets the layout orientation.
	 * @return the layout orientation
	 */
	public int getLayoutOrientation() {
		return this.getJListLoading().getLayoutOrientation();
	}
	
	/**
	 * Gets the lead selection index.
	 * @return the lead selection index
	 */
	public int getLeadSelectionIndex() {
		return this.getJListLoading().getLeadSelectionIndex();
	}
	
	/**
	 * Gets the list selection listeners.
	 * @return the list selection listeners
	 */
	public ListSelectionListener[] getListSelectionListeners() {
		return this.getJListLoading().getListSelectionListeners();
	}
	
	/**
	 * Gets the max selection index.
	 * @return the max selection index
	 */
	public int getMaxSelectionIndex() {
		return this.getJListLoading().getMaxSelectionIndex();
	}
	
	/**
	 * Gets the min selection index.
	 * @return the min selection index
	 */
	public int getMinSelectionIndex() {
		return this.getJListLoading().getMinSelectionIndex();
	}
	
	/**
	 * Gets the model.
	 * @return the model
	 */
	public ListModel<E> getModel() {
		return this.getJListLoading().getModel();
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
		return this.getJListLoading().getNextMatch(prefix, startIndex, bias);
	}
	
	/**
	 * Gets the preferred scrollable viewport size.
	 * @return the preferred scrollable viewport size
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return this.getJListLoading().getPreferredScrollableViewportSize();
	}
	
	/**
	 * Gets the prototype cell value.
	 * @return the prototype cell value
	 */
	public Object getPrototypeCellValue() {
		return this.getJListLoading().getPrototypeCellValue();
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
		return this.getJListLoading().getScrollableBlockIncrement(visibleRect, orientation, direction);
	}
	
	/**
	 * Gets the scrollable tracks viewport height.
	 *
	 * @return the scrollable tracks viewport height
	 */
	public boolean getScrollableTracksViewportHeight() {
		return this.getJListLoading().getScrollableTracksViewportHeight();
	}
	
	/**
	 * Gets the scrollable tracks viewport width.
	 * @return the scrollable tracks viewport width
	 */
	public boolean getScrollableTracksViewportWidth() {
		return this.getJListLoading().getScrollableTracksViewportWidth();
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
		return this.getJListLoading().getScrollableUnitIncrement(visibleRect, orientation, direction);
	}
	
	/**
	 * Gets the selected index.
	 * @return the selected index
	 */
	public int getSelectedIndex() {
		return this.getJListLoading().getSelectedIndex();
	}
	
	/**
	 * Gets the selected indices.
	 * @return the selected indices
	 */
	public int[] getSelectedIndices() {
		return this.getJListLoading().getSelectedIndices();
	}
	
	/**
	 * Gets the selected value.
	 * @return the selected value
	 */
	public Object getSelectedValue() {
		return this.getJListLoading().getSelectedValue();
	}
	
	/**
	 * Gets the selected values.
	 * @return the selected values
	 */
	public List<E> getSelectedValuesList() {
		return this.getJListLoading().getSelectedValuesList();
	}
	
	/**
	 * Gets the selection background.
	 * @return the selection background
	 */
	public Color getSelectionBackground() {
		return this.getJListLoading().getSelectionBackground();
	}
	
	/**
	 * Gets the selection foreground.
	 * @return the selection foreground
	 */
	public Color getSelectionForeground() {
		return this.getJListLoading().getSelectionForeground();
	}
	
	/**
	 * Gets the selection mode.
	 * @return the selection mode
	 */
	public int getSelectionMode() {
		return this.getJListLoading().getSelectionMode();
	}
	
	/**
	 * Gets the selection model.
	 * @return the selection model
	 */
	public ListSelectionModel getSelectionModel() {
		return this.getJListLoading().getSelectionModel();
	}
	
	/**
	 * Gets the value is adjusting.
	 * @return the value is adjusting
	 */
	public boolean getValueIsAdjusting() {
		return this.getJListLoading().getValueIsAdjusting();
	}
	
	/**
	 * Gets the visible row count.
	 * @return the visible row count
	 */
	public int getVisibleRowCount() {
		return this.getJListLoading().getVisibleRowCount();
	}
	
	/**
	 * Index to location.
	 *
	 * @param index the index
	 * @return the point
	 */
	public Point indexToLocation(int index) {
		return this.getJListLoading().indexToLocation(index);
	}
	
	/**
	 * Checks if is selected index.
	 *
	 * @param index the index
	 * @return true, if is selected index
	 */
	public boolean isSelectedIndex(int index) {
		return this.getJListLoading().isSelectedIndex(index);
	}
	
	/**
	 * Checks if is selection empty.
	 *
	 * @return true, if is selection empty
	 */
	public boolean isSelectionEmpty() {
		return this.getJListLoading().isSelectionEmpty();
	}
	
	/**
	 * Location to index.
	 *
	 * @param location the location
	 * @return the int
	 */
	public int locationToIndex(Point location) {
		return this.getJListLoading().locationToIndex(location);
	}
	
	/**
	 * Removes the list selection listener.
	 * @param listener the listener
	 */
	public void removeListSelectionListener(ListSelectionListener listener) {
		this.getJListLoading().removeListSelectionListener(listener);
	}
	
	/**
	 * Removes the selection interval.
	 *
	 * @param index0 the index0
	 * @param index1 the index1
	 */
	public void removeSelectionInterval(int index0, int index1) {
		this.getJListLoading().removeSelectionInterval(index0, index1);
	}
	
	/**
	 * Sets the cell renderer.
	 * @param cellRenderer the new cell renderer
	 */
	public void setCellRenderer(ListCellRenderer<E> cellRenderer) {
		this.getJListLoading().setCellRenderer(cellRenderer);
	}
	
	/**
	 * Sets the drag enabled.
	 * @param b the new drag enabled
	 */
	public void setDragEnabled(boolean b) {
		this.getJListLoading().setDragEnabled(b);		
	}
	
	/**
	 * Sets the drop mode.
	 * @param dropMode the new drop mode
	 */
	public void setDropMode(DropMode dropMode) {
		this.getJListLoading().setDropMode(dropMode);
	}
	
	/**
	 * Sets the fixed cell height.
	 * @param height the new fixed cell height
	 */
	public void setFixedCellHeight(int height) {
		this.getJListLoading().setFixedCellHeight(height);
	}
	
	/**
	 * Sets the fixed cell width.
	 * @param width the new fixed cell width
	 */
	public void setFixedCellWidth(int width) {
		this.getJListLoading().setFixedCellWidth(width);
	}
	
	/**
	 * Sets the layout orientation.
	 * @param layoutOrientation the new layout orientation
	 */
	public void setLayoutOrientation(int layoutOrientation) {
		this.getJListLoading().setLayoutOrientation(layoutOrientation);
	}
	
	/**
	 * Sets the list data.
	 * @param listData the new list data
	 */
	public void setListData(E[] listData) {
		this.getJListLoading().setListData(listData);
	}
	
	/**
	 * Sets the list data.
	 * @param listData the new list data
	 */
	public void setListData(Vector<E> listData) {
		this.getJListLoading().setListData(listData);		
	}
	
	/**
	 * Sets the model.
	 * @param model the new model
	 */
	public void setModel(ListModel<E> model) {
		this.getJListLoading().setModel(model);		
	}
	
	/**
	 * Sets the prototype cell value.
	 * @param prototypeCellValue the new prototype cell value
	 */
	public void setPrototypeCellValue(E prototypeCellValue) {
		this.getJListLoading().setPrototypeCellValue(prototypeCellValue);
	}
	
	/**
	 * Sets the selected index.
	 * @param index the new selected index
	 */
	public void setSelectedIndex(int index) {
		this.getJListLoading().setSelectedIndex(index);		
	}
	
	/**
	 * Sets the selected indices.
	 * @param indices the new selected indices
	 */
	public void setSelectedIndices(int[] indices) {
		this.getJListLoading().setSelectedIndices(indices);		
	}
	
	/**
	 * Sets the selected value.
	 *
	 * @param anObject the an object
	 * @param shouldScroll the should scroll
	 */
	public void setSelectedValue(Object anObject, boolean shouldScroll) {
		this.getJListLoading().setSelectedValue(anObject, shouldScroll);
	}
	
	/**
	 * Sets the selection background.
	 * @param selectionBackground the new selection background
	 */
	public void setSelectionBackground(Color selectionBackground) {
		this.getJListLoading().setSelectionBackground(selectionBackground);
	}
	
	/**
	 * Sets the selection foreground.
	 * @param selectionForeground the new selection foreground
	 */
	public void setSelectionForeground(Color selectionForeground) {
		this.getJListLoading().setSelectionForeground(selectionForeground);
	}
	
	/**
	 * Sets the selection interval.
	 *
	 * @param anchor the anchor
	 * @param lead the lead
	 */
	public void setSelectionInterval(int anchor, int lead) {
		this.getJListLoading().setSelectionInterval(anchor, lead);		
	}
	
	/**
	 * Sets the selection mode.
	 * @param selectionMode the new selection mode
	 */
	public void setSelectionMode(int selectionMode) {
		this.getJListLoading().setSelectionMode(selectionMode);		
	}
	
	/**
	 * Sets the selection model.
	 * @param selectionModel the new selection model
	 */
	public void setSelectionModel(ListSelectionModel selectionModel) {
		this.getJListLoading().setSelectionModel(selectionModel);
	}
	
	/**
	 * Sets the uI.
	 * @param ui the new uI
	 */
	public void setUI(ListUI ui) {
		this.getJListLoading().setUI(ui);
	}
	
	/**
	 * Sets the value is adjusting.
	 * @param b the new value is adjusting
	 */
	public void setValueIsAdjusting(boolean b) {
		this.getJListLoading().setValueIsAdjusting(b);
	}
	
	/**
	 * Sets the visible row count.
	 * @param visibleRowCount the new visible row count
	 */
	public void setVisibleRowCount(int visibleRowCount) {
		this.getJListLoading().setVisibleRowCount(visibleRowCount);		
	}

} 
