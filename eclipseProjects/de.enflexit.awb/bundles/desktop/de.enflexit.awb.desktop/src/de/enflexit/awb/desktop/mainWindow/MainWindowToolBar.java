package de.enflexit.awb.desktop.mainWindow;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JToolBar;

import de.enflexit.awb.baseUI.ToolBarGroup;

/**
 * The Class MainWindowToolBar organizes the AWB toolbar elements into groups.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MainWindowToolBar extends JToolBar {

	private static final long serialVersionUID = 6033364534244260033L;

	private HashMap<ToolBarGroup, JToolBar> toolbarButtonsListHashMap;
	
	/**
	 * Instantiates a new main window tool bar.
	 * @param name the name of the toolbar
	 */
	public MainWindowToolBar(String name) {
		super(name);
	}
	/**
	 * Returns the toolbar buttons list hash map.
	 * @return the toolbar buttons list hash map
	 */
	private HashMap<ToolBarGroup, JToolBar> getToolbarButtonsListHashMap() {
		if (toolbarButtonsListHashMap==null) {
			toolbarButtonsListHashMap = new HashMap<>();
			for (ToolBarGroup tbGroup : ToolBarGroup.values()) {
				this.getGroupToolBar(tbGroup);
			}
		}
		return toolbarButtonsListHashMap;
	}
	/**
	 * Returns the tool bar button list for the specified ToolBarGroup.
	 *
	 * @param tbGroup the ToolBarGroup
	 * @return the tool bar button list
	 */
	private JToolBar getGroupToolBar(ToolBarGroup tbGroup) {
		JToolBar groupToolBar = this.getToolbarButtonsListHashMap().get(tbGroup);
		if (groupToolBar==null) {
			// --- Create a new group toolbar -------------
			groupToolBar = new JToolBar(tbGroup.name());
			groupToolBar.setFloatable(false);
			groupToolBar.setRollover(true);
			
			super.add(groupToolBar);
			// --- Remind toolbar for later usage --------- 
			this.getToolbarButtonsListHashMap().put(tbGroup, groupToolBar);
		}
		return groupToolBar;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	@Override
	public Component add(Component comp) {
		return this.add(comp, (ToolBarGroup)null);
	}
	/**
	 * Adds the specified component to the toolbar.
	 *
	 * @param comp the Component to add
	 * @param tbGroup the ToolBarGroup
	 * @return the component
	 */
	public Component add(Component comp, ToolBarGroup tbGroup) {
		return this.add(comp, -1, tbGroup);
	}
	/* (non-Javadoc)
	 * @see java.awt.Container#add(java.awt.Component, int)
	 */
	@Override
	public Component add(Component comp, int groupIndex) {
		return this.add(comp, groupIndex, (ToolBarGroup)null);
	}
	/**
	 * Adds the specified component to the toolbar.
	 *
	 * @param comp the Component to add
	 * @param groupIndex the index position
	 * @param tbGroup the the ToolBarGroup
	 * @return the component
	 */
	public Component add(Component comp, int groupIndex, ToolBarGroup tbGroup) {
		
		// --- Get the relevant ToolBarGroup --------------
		if (comp instanceof MainWindowToolBarButton) {
			MainWindowToolBarButton tbButton = (MainWindowToolBarButton) comp;
			tbGroup = tbButton.getToolBarGroup();
		}
		if (tbGroup==null) tbGroup = ToolBarGroup.NoGroup;

		// --- Add to the corresponding list --------------
		Component compAdded = null;
		JToolBar groupToolBar = this.getGroupToolBar(tbGroup);

		// --- Remove separator first (last element!) -----
		if (groupToolBar.getComponentCount()>0 && this.isSeparatorElement(groupToolBar, groupToolBar.getComponentCount()-1)==true) {
			groupToolBar.remove(groupToolBar.getComponentCount()-1);
		}
		// --- Add component to add -----------------------
		if (groupIndex < 0 || groupIndex >= groupToolBar.getComponentCount() ) {
			compAdded = groupToolBar.add(comp);
		} else {
			compAdded = groupToolBar.add(comp, groupIndex);
		}
		// --- Add separator as last element again --------
		if (groupToolBar.getComponentCount()>0 && this.isSeparatorElement(groupToolBar, groupToolBar.getComponentCount()-1)==false) {
			groupToolBar.addSeparator();
		}

		return compAdded;
	}
	/**
	 * Checks if the component at the specified index of the specified toolbar is a {@link Separator}.
	 *
	 * @param toolBar the tool bar to check in
	 * @param index the index
	 * @return true, if is separator element
	 */
	private boolean isSeparatorElement(JToolBar toolBar, int index) {
		
		if (toolBar==null || toolBar.getComponentCount()==0 || index<0 || index> toolBar.getComponentCount()-1) return false;
		
		boolean isSeparator = false;
		try {
			isSeparator = toolBar.getComponent(index) instanceof JToolBar.Separator;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isSeparator;
	}
	
	/**
	 * Returns the ToolBarGroup for the specified component.
	 *
	 * @param comp the Component to be found
	 * @return the hosting ToolBarGroup, if found
	 */
	public ToolBarGroup getToolBarGroup(Component comp) {
		
		for (ToolBarGroup tbGroupWork : ToolBarGroup.values()) {
			JToolBar tBar =  this.getGroupToolBar(tbGroupWork);
			int compIndex = tBar.getComponentIndex(comp);
			if (compIndex!=-1) {
				return tbGroupWork;
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#getComponentIndex(java.awt.Component)
	 */
	@Override
	public int getComponentIndex(Component comp) {
		
		for (ToolBarGroup tbGroupWork : ToolBarGroup.values()) {
			JToolBar tBar =  this.getGroupToolBar(tbGroupWork);
			int compIndex = tBar.getComponentIndex(comp);
			if (compIndex!=-1) {
				return compIndex;
			}
		}
		return super.getComponentIndex(comp);
	}
	
}
