package de.enflexit.common.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;

/**
 * The Class OwnerDetection provides static help methods to detect an owner frame, dialog or component.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OwnerDetection {

	/**
	 * Returns the top parent component of the specified component.
	 * @param component the component
	 * @return the top parent component
	 */
	public static Component getTopParentComponent(Component component) {
		Component compFound = component;
		if (compFound!=null) {
			while (compFound.getParent()!=null) {
				compFound = compFound.getParent();
			}	
		}
		return compFound;
	}

	/**
	 * Returns the owner window for the specified component. Therefore, the methods 
	 * searches for i) Dialogs and ii) Frames 
	 * @param component the Component
	 * @return the owner window for the component or <code>null</code>, if no component was specified
	 */
	public static Window getOwnerWindowForComponent(Component component) {
		
		if (component==null) return null;
		
		// --- Check for a Dialog ---------------
		Window ownerWindow = OwnerDetection.getOwnerDialogForComponent(component);
		if (ownerWindow==null) {
			// --- Check for a Frame ------------
			ownerWindow = OwnerDetection.getOwnerFrameForComponent(component);
			if (ownerWindow==null) {
				// --- Check for a JPopupMenu ---
				Component comp = getTopParentComponent(component);
				if (comp instanceof JPopupMenu ) {
					JPopupMenu jPopupMenu = (JPopupMenu) comp;
					ownerWindow = getOwnerWindowForComponent(jPopupMenu.getInvoker());
				}
			}
		}
		return ownerWindow;
	}
	
	/**
	 * Returns the owner frame for the specified JComponent.
	 * @param component the JComponent
	 * @return the owner frame for component or null, if no component was specified
	 */
	public static Frame getOwnerFrameForComponent(Component component) {
		if (component== null) return null;
		return OwnerDetection.getOwnerFrameForContainer(component.getParent());
	}
	/**
	 * Returns the owner frame for the specified Container.
	 * @param container the container
	 * @return the owner frame for component or null, if no container was specified
	 */
	public static Frame getOwnerFrameForContainer(Container container) {
		if (container==null) return null;
		Frame frameFound = null;
		Container currComp = container;
		while (currComp!=null) {
			if (currComp instanceof Frame) {
				frameFound = (Frame) currComp;
				break;
			}
			currComp = currComp.getParent();
		}
		return frameFound;
	}
	/**
	 * Returns the owner dialog for a JComponent.
	 * @param component the Component
	 * @return the owner dialog for component
	 */
	public static Dialog getOwnerDialogForComponent(Component component) {
		Dialog dialogFound = null;
		Container currComp = component.getParent();
		while (currComp!=null) {
			if (currComp instanceof Dialog) {
				dialogFound = (Dialog) currComp;
				break;
			}
			currComp = currComp.getParent();
		}
		return dialogFound;
	}
	
	/**
	 * Returns the owner JInternalFrame for a JComponent if possible.
	 * @param component the Component
	 * @return the owner JInternalFrame for component
	 */
	public static JInternalFrame getOwnerJInternalFrameForComponent(Component component) {
		
		JInternalFrame iFrameFound = null;
		Container currComp = component.getParent();
		while (currComp!=null) {
			if (currComp instanceof JInternalFrame) {
				iFrameFound = (JInternalFrame) currComp;
				break;
			}
			currComp = currComp.getParent();
		}
		return iFrameFound;
	}
	
}
