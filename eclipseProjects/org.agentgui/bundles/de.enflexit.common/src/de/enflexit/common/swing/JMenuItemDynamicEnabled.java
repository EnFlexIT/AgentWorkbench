package de.enflexit.common.swing;

import javax.swing.Action;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 * The Class JMenuItemDynamicEnabled represents an standard JMenueItem that enables
 * to dynamically check, if the JMenuItem should be enabled or disabled.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JMenuItemDynamicEnabled extends JMenuItem {

	private static final long serialVersionUID = 4063354931177798471L;

	private EnablementCheck enablementCheck;	
	
	private Icon icon;
	private Icon iconDisabled;
	
	
	/**
     * Creates a <code>JMenuItemDynamicEnabled</code> with no set text or icon.
     */
	public JMenuItemDynamicEnabled() {
		super(null, (Icon) null);
	}
	/**
	 * Creates a <code>JMenuItemDynamicEnabled</code> with the specified icon.
	 * @param icon the icon of the <code>JMenuItem</code>
	 */
	public JMenuItemDynamicEnabled(Icon icon) {
		super(null, icon);
	}
	/**
	 * Creates a <code>JMenuItemDynamicEnabled</code> with the specified text.
	 * @param text the text of the <code>JMenuItem</code>
	 */
	public JMenuItemDynamicEnabled(String text) {
		super(text, (Icon) null);
	}
	/**
     * Creates a menu item whose properties are taken from the
     * specified <code>Action</code>.
     * @param a the action of the <code>JMenuItem</code>
     */
    public JMenuItemDynamicEnabled(Action a) {
        super(a);
    }
    /**
     * Creates a <code>JMenuItem</code> with the specified text and icon.
     *
     * @param text the text of the <code>JMenuItem</code>
     * @param icon the icon of the <code>JMenuItem</code>
     */
    public JMenuItemDynamicEnabled(String text, Icon icon) {
        super(text, icon);
    }
    /**
     * Creates a <code>JMenuItem</code> with the specified text and
     * keyboard mnemonic.
     *
     * @param text the text of the <code>JMenuItem</code>
     * @param mnemonic the keyboard mnemonic for the <code>JMenuItem</code>
     */
    public JMenuItemDynamicEnabled(String text, int mnemonic) {
        super(text, mnemonic);
    }

    /* (non-Javadoc)
     * @see javax.swing.AbstractButton#setIcon(javax.swing.Icon)
     */
    @Override
    public void setIcon(Icon icon) {
    	this.icon = icon;
    	if (icon instanceof ImageIcon) {
    		ImageIcon ii = (ImageIcon) icon;
    		this.iconDisabled = new ImageIcon(GrayFilter.createDisabledImage(ii.getImage()));
    	}
    	super.setIcon(icon);
    }
    
    /* (non-Javadoc)
     * @see java.awt.Component#isEnabled()
     */
    @Override
    public boolean isEnabled() {
    	if (this.getEnablementCheck()==null) { 
    		// --- Without any external check ---
    		return super.isEnabled();
    	} else {
    		// --- Found an external check ------
    		boolean isEnabled = this.getEnablementCheck().isEnabled();
    		if (this.icon!=null && this.iconDisabled!=null) {
        		if (isEnabled==true) {
        			super.setIcon(this.icon);
        		} else {
        			super.setIcon(this.iconDisabled);
        		}
        	}
    		return isEnabled;
    	}
    }
    
   
    
    /**
     * Sets the enablement check.
     * @param enablementCheck the new enablement check
     */
    public void setEnablementCheck(EnablementCheck enablementCheck) {
		this.enablementCheck = enablementCheck;
	}
    public EnablementCheck getEnablementCheck() {
		return enablementCheck;
	}
    
    /**
     * The Interface to set an is enabled check into the {@link JMenuItemDynamicEnabled}.
     *
     * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
     */
    public interface EnablementCheck {
    	boolean isEnabled();
    }
    
}
