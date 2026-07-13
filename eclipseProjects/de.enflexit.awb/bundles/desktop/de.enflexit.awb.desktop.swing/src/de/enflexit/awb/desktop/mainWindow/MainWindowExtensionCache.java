package de.enflexit.awb.desktop.mainWindow;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;

import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension;
import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension.MainWindowMenu;
import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension.MainWindowMenuItem;
import de.enflexit.awb.baseUI.mainWindow.MainWindowExtension.MainWindowToolbarComponent;

/**
 * The Class MainWindowExtensionCache.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MainWindowExtensionCache {

	private MainWindow mainWindow;
	private HashMap<String, ExtensionElements> extensionElementHashMap;
	
	/**
	 * Instantiates a new main window extension cache.
	 * @param mainWindow the main window
	 */
	public MainWindowExtensionCache(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	/**
	 * Returns the extension element hash map.
	 * @return the extension element hash map
	 */
	private HashMap<String, ExtensionElements> getExtensionElementHashMap() {
		if (extensionElementHashMap==null) {
			extensionElementHashMap = new HashMap<>();
		}
		return extensionElementHashMap;
	}
	
	/**
	 * Returns the ExtensionElements of the specified MainWindowExtension.
	 *
	 * @param mwExt theMainWindowExtension
	 * @return the extension elements
	 */
	private ExtensionElements getExtensionElements(MainWindowExtension mwExt, boolean isCreateIfNotAvailable) {
		if (mwExt==null) return null;
		return this.getExtensionElements(mwExt.getClass().getName(), isCreateIfNotAvailable);
	}
	/**
	 * Returns the ExtensionElements of the specified MainWindowExtension.
	 *
	 * @param mwExt theMainWindowExtension
	 * @return the extension elements
	 */
	private ExtensionElements getExtensionElements(String mwExtClassName, boolean isCreateIfNotAvailable) {
		if (mwExtClassName==null || mwExtClassName.isBlank()==true) return null;
		ExtensionElements eElements = this.getExtensionElementHashMap().get(mwExtClassName);
		if (eElements==null && isCreateIfNotAvailable==true) {
			eElements = new ExtensionElements();
			this.getExtensionElementHashMap().put(mwExtClassName, eElements);
		}
		return eElements;
	}
	
	/**
	 * Removes the main window extension.
	 * @param mwExt the MainWindowExtension to remove
	 */
	public void removeMainWindowExtension(MainWindowExtension mwExt) {
		this.removeMainWindowExtension(mwExt.getClass().getName());
	}
	/**
	 * Removes the main window extension.
	 * @param mwExtClassName the class name of the MainWindowExtension
	 */
	public void removeMainWindowExtension(String mwExtClassName) {
		
		if (mwExtClassName==null || mwExtClassName.isBlank()==true) return;
		
		ExtensionElements eElements = this.getExtensionElementHashMap().remove(mwExtClassName);
		if (eElements==null) return;
		
		try {
			// --- Remove JButton of the IdentityProvider -----------
			this.removeExtensionComponent(eElements.getJButtonIdentityProvider());
			
			// --- Remove individual menus --------------------------
			eElements.getMainWindowMenuList().forEach(mwMenu -> this.removeExtensionComponent(mwMenu.getJMenu()));
			
			// --- Remove individual menu items ---------------------
			for (MainWindowMenuItem mwMenuItem : eElements.getMainWindowMenuItemList()) {
				this.removeExtensionComponent(mwMenuItem.getJMenuItem());
				this.removeExtensionComponent(mwMenuItem.getSeparatorInstance());
				mwMenuItem.setSeparatorInstance(null);
			}
			
			// --- Remove toolbar components ------------------------
			for (MainWindowToolbarComponent mwToolbarComp : eElements.getMainWindowToolbarComponentList()) {
				this.removeExtensionComponent(mwToolbarComp.getJComponent());
				this.removeExtensionComponent(mwToolbarComp.getSeparatorInstance());
				mwToolbarComp.setSeparatorInstance(null);
			}
			
			this.mainWindow.validate();
			this.mainWindow.repaint();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Removes the extension component.
	 * @param compToRemove the comp to remove
	 */
	private void removeExtensionComponent(Component compToRemove) {
		if (compToRemove==null) return;
		Container cont = compToRemove.getParent();
		if (cont!=null) {
			cont.remove(compToRemove);
		}
	}
	
	
	/**
	 * Reminds the specified JButton for the identity provider.
	 *
	 * @param mwExt the source MainWindowExtension of the identity provider button
	 * @param jButtonIdentityProvider the JButton for the identity provider
	 */
	public void remindIdentityProvider(MainWindowExtension mwExt, JButton jButtonIdentityProvider) {
		if (mwExt==null || jButtonIdentityProvider==null) return;
		this.getExtensionElements(mwExt, true).setJButtonIdentityProvider(jButtonIdentityProvider);
	}
	/**
	 * Reminds the specified main window menu.
	 *
	 * @param mwExt the source MainWindowExtension of the identity provider button
	 * @param mwMenu the MainWindowMenu to remind
	 */
	public void remindMainWindowMenu(MainWindowExtension mwExt, MainWindowMenu mwMenu) {
		if (mwExt==null || mwMenu==null) return;
		this.getExtensionElements(mwExt, true).remindMainWindowMenu(mwMenu);
	}
	/**
	 * Reminds the specified MainWindowMenuItem.
	 *
	 * @param mwExt the source MainWindowExtension of the identity provider button
	 * @param mwMenuItem the MainWindowMenuItem to remind
	 */
	public void remindMainWindowMenuItem(MainWindowExtension mwExt, MainWindowMenuItem mwMenuItem) {
		if (mwExt==null || mwMenuItem==null) return;
		this.getExtensionElements(mwExt, true).remindMainWindowMenuItem(mwMenuItem);
	}
	/**
	 * Remind the specified MainWindowToolbarComponent.
	 *
	 * @param mwExt the source MainWindowExtension of the identity provider button
	 * @param mwToolbarComp the MainWindowToolbarComponent to remind
	 */
	public void remindMainWindowToolbarComponent(MainWindowExtension mwExt, MainWindowToolbarComponent mwToolbarComp) {
		if (mwExt==null || mwToolbarComp==null) return;
		this.getExtensionElements(mwExt, true).remindMainWindowToolbarComponent(mwToolbarComp);
	}
	
	
	/**
	 * The Class ExtensionElements.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class ExtensionElements {
		
		private JButton jButtonIdentityProvider;
		private List<MainWindowMenu> mainWindowMenuList;
		private List<MainWindowMenuItem> mainWindowMenuItemList;
		private List<MainWindowToolbarComponent> mainWindowToolbarComponentList;
		
		public JButton getJButtonIdentityProvider() {
			return jButtonIdentityProvider;
		}
		public void setJButtonIdentityProvider(JButton jButtonIdentityProvider) {
			this.jButtonIdentityProvider = jButtonIdentityProvider;
		}
		
		public List<MainWindowMenu> getMainWindowMenuList() {
			if (mainWindowMenuList==null) {
				mainWindowMenuList = new ArrayList<>();
			}
			return mainWindowMenuList;
		}
		public void remindMainWindowMenu(MainWindowMenu mwMenu) {
			if (this.getMainWindowMenuList().contains(mwMenu)==false) {
				this.getMainWindowMenuList().add(mwMenu);
			}
		}
		
		public List<MainWindowMenuItem> getMainWindowMenuItemList() {
			if (mainWindowMenuItemList==null) {
				mainWindowMenuItemList = new ArrayList<>();
			}
			return mainWindowMenuItemList;
		}
		public void remindMainWindowMenuItem(MainWindowMenuItem mwMenuItem) {
			if (this.getMainWindowMenuItemList().contains(mwMenuItem)==false) {
				this.getMainWindowMenuItemList().add(mwMenuItem);
			}
		}
		
		public List<MainWindowToolbarComponent> getMainWindowToolbarComponentList() {
			if (mainWindowToolbarComponentList==null) {
				mainWindowToolbarComponentList = new ArrayList<>();
			}
			return mainWindowToolbarComponentList;
		}
		public void remindMainWindowToolbarComponent(MainWindowToolbarComponent mwToolbarComp) {
			if (this.getMainWindowToolbarComponentList().contains(mwToolbarComp)==false) {
				this.getMainWindowToolbarComponentList().add(mwToolbarComp);
			}
		}
	}

}
