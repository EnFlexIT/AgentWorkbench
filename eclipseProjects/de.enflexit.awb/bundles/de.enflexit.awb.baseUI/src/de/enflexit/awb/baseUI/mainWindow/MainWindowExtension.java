package de.enflexit.awb.baseUI.mainWindow;

import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import de.enflexit.awb.baseUI.SeparatorPosition;
import de.enflexit.awb.baseUI.systemtray.TrayIconMenuExtension;
import de.enflexit.awb.core.ui.AwbMainWindowMenu;

/**
 * The Class MainWindowExtension can be extended to define individual menus, menu items
 * and toolbar components to the Swing main window of Agent.Workbench.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class MainWindowExtension extends TrayIconMenuExtension {

	public static final String MAIN_WINDOW_EXTENSION_ID = "de.enflexit.awb.desktop.mainWindowExtension";

	private Vector<MainWindowMenu> mainWindowMenuVector;
	private Vector<MainWindowMenuItem> mainWindowMenuItemVector;
	private Vector<MainWindowToolbarComponent> mainWindowToolBarComponentVector;

	/**
	 * Initializes the extension. Use this method to add your individual 
	 * elements to the main window of the workbench.
	 */
	public abstract void initialize();
	
	
	/**
	 * Returns the vector of JMenus to be added to the MainWindow.
	 * @return the main window menu vector
	 */
	public Vector<MainWindowMenu> getMainWindowMenuVector() {
		if (mainWindowMenuVector==null) {
			mainWindowMenuVector = new Vector<>();	
		}
		return mainWindowMenuVector;
 	}
	/**
	 * Adds the specified JMenu to the {@link MainWindow}.
	 *
	 * @param jMenu the JMmenu to be added
	 * @param indexPosition the index position for the menu (may be null also)
	 */
	protected void addJMenu(JMenu jMenu, Integer indexPosition) {
		if (jMenu==null) {
			throw new NullPointerException("The specified menu to add is null!");
		}
		this.getMainWindowMenuVector().addElement(new MainWindowMenu(jMenu, indexPosition));
	}
	
	
	/**
	 * Returns the vector of menu items that are to be added to the MainWindow.
	 * @return the main window menu item vector
	 */
	public Vector<MainWindowMenuItem> getMainWindowMenuItemVector() {
		if (mainWindowMenuItemVector==null) {
			mainWindowMenuItemVector = new Vector<>();
		}
		return mainWindowMenuItemVector;
	}
	/**
	 * Adds the specified JMenuItem to the specified workbench window at the given index position.
	 *
	 * @param workbenchMenuToAddTo the workbench menu to add to
	 * @param menuItemToAdd the menu item to add
	 * @param indexPosition the index position for the menu item (may be <code>null</code> also)
	 * @param separatorPosition the separator position (may be <code>null</code> also)
	 */
	protected void addJMenuItem(AwbMainWindowMenu workbenchMenuToAddTo, JMenuItem menuItemToAdd, Integer indexPosition, SeparatorPosition separatorPosition) {
		if (workbenchMenuToAddTo==null) {
			throw new NullPointerException("The menu for the menu item to add was not specified.");
		}
		if (menuItemToAdd==null) {
			throw new NullPointerException("The specified menu item to add is null.");
		}
		this.getMainWindowMenuItemVector().addElement(new MainWindowMenuItem(workbenchMenuToAddTo, menuItemToAdd, indexPosition, separatorPosition));
	}
	
	/**
	 * Returns the main window tool bar component vector.
	 * @return the main window tool bar component vector
	 */
	public Vector<MainWindowToolbarComponent> getMainWindowToolBarComponentVector() {
		if (mainWindowToolBarComponentVector==null) {
			mainWindowToolBarComponentVector = new Vector<>();
		}
		return mainWindowToolBarComponentVector;
	}
	/**
	 * Adds the specified toolbar component to the toolbar.
	 *
	 * @param toolbarComponentToAdd the toolbar component to add
	 * @param indexPosition the index position of the component (may be <code>null</code> also)
	 * @param separatorPosition the separator position (may be <code>null</code> also)
	 */
	protected void addToolbarComponent(JComponent toolbarComponentToAdd, Integer indexPosition, SeparatorPosition separatorPosition) {
		if (toolbarComponentToAdd==null) {
			throw new NullPointerException("The specified toolbar component to add is null.");
		}
		this.getMainWindowToolBarComponentVector().addElement(new MainWindowToolbarComponent(toolbarComponentToAdd, indexPosition, separatorPosition));
	}
	
	
	
	
	// ------------------------------------------------------------------------
	// --- From here, sub classes are defined ---------------------------------
	// ------------------------------------------------------------------------
	/**
	 * The Class MainWindowMenu describes the JMenu and it index position that is
	 * to be added to the {@link MainWindow} of Agent.Workbench.
	 */
	public class MainWindowMenu {

		private JMenu jMenu;
		private Integer indexPosition;
		
		public MainWindowMenu(JMenu jMenu, Integer indexPosition) {
			this.setJMenu(jMenu);
			this.setIndexPosition(indexPosition);
		}
		
		public JMenu getJMenu() {
			return jMenu;
		}
		public void setJMenu(JMenu jMenu) {
			this.jMenu = jMenu;
		}
		
		public Integer getIndexPosition() {
			return indexPosition;
		}
		public void setIndexPosition(Integer indexPosition) {
			this.indexPosition = indexPosition;
		}
	}
	
	/**
	 * The Class MainWindowMenuItem describes single menu items that are to 
	 * be added to MainWindow of Agent.Workbench.
	 */
	public class MainWindowMenuItem {
		
		private AwbMainWindowMenu awbMainWindowMenu;
		private JMenuItem jMenuItem;
		private Integer indexPosition;
		private SeparatorPosition separatorPosition;
		
		public MainWindowMenuItem(AwbMainWindowMenu awbMainWindowMenu, JMenuItem menuItem, Integer indexPosition, SeparatorPosition separatorPosition) {
			this.setWorkbenchMenu(awbMainWindowMenu);
			this.setJMenuItem(menuItem);
			this.setIndexPosition(indexPosition);
			this.setSeparatorPosition(separatorPosition);
		}
		
		public AwbMainWindowMenu getWorkbenchMenu() {
			return awbMainWindowMenu;
		}
		public void setWorkbenchMenu(AwbMainWindowMenu awbMainWindowMenu) {
			this.awbMainWindowMenu = awbMainWindowMenu;
		}

		public JMenuItem getJMenuItem() {
			return jMenuItem;
		}
		public void setJMenuItem(JMenuItem jMenuItem) {
			this.jMenuItem = jMenuItem;
		}

		public Integer getIndexPosition() {
			return indexPosition;
		}
		public void setIndexPosition(Integer indexPosition) {
			this.indexPosition = indexPosition;
		}
		
		public SeparatorPosition getSeparatorPosition() {
			return separatorPosition;
		}
		public void setSeparatorPosition(SeparatorPosition separatorPosition) {
			this.separatorPosition = separatorPosition;
		}
	}

	/**
	 * The Class MainWindowToolbarComponent describes single components that 
	 * are to be added to the main tool bar of Agent.Workbench.
	 */
	public class MainWindowToolbarComponent {
		
		private JComponent jComponent;
		private Integer indexPosition;
		private SeparatorPosition separatorPosition;
		
		public MainWindowToolbarComponent(JComponent jComponent, Integer indexPosition, SeparatorPosition separatorPosition) {
			this.setJComponent(jComponent);
			this.setIndexPosition(indexPosition);
			this.setSeparatorPosition(separatorPosition);
		}

		public JComponent getJComponent() {
			return jComponent;
		}
		public void setJComponent(JComponent jComponent) {
			this.jComponent = jComponent;
		}

		public Integer getIndexPosition() {
			return indexPosition;
		}
		public void setIndexPosition(Integer indexPosition) {
			this.indexPosition = indexPosition;
		}
		
		public SeparatorPosition getSeparatorPosition() {
			return separatorPosition;
		}
		public void setSeparatorPosition(SeparatorPosition separatorPosition) {
			this.separatorPosition = separatorPosition;
		}
	}
	
	
}
