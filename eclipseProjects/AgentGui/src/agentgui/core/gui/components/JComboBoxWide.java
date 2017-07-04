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

import javax.swing.*; 
import java.awt.*; 
import java.util.Vector; 
 
/**
 * The Class WideComboBox.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JComboBoxWide<E> extends JComboBox<E> { 
 
	private static final long serialVersionUID = 3185430839619749814L;

    private boolean layingOut = false;
	private boolean wide = true;

    
	/**
	 * Instantiates a new wide JComboBox.
	 */
	public JComboBoxWide() { 
    } 
    /**
     * Instantiates a new wide JComboBox.
     * @param items the items
     */
    public JComboBoxWide(E[] items){ 
        super(items); 
    } 
    /**
     * Instantiates a new wide JComboBox.
     * @param items the items
     */
    public JComboBoxWide(Vector<E> items) { 
        super(items); 
    } 
    /**
     * Instantiates a new wide JComboBox.
     * @param aModel the a model
     */
    public JComboBoxWide(ComboBoxModel<E> aModel) { 
        super(aModel); 
    } 
 
	/**
	 * Checks if is wide.
	 * @return true, if is wide
	 */
	public boolean isWide() {
		return wide;
	}
	/**
	 * Sets the wide.
	 * @param wide the new wide
	 */
	public void setWide(boolean wide) {
		this.wide = wide;
	}

	

    /* (non-Javadoc)
     * @see java.awt.Container#doLayout()
     */
    public void doLayout(){ 
        try{ 
            layingOut = true; 
            super.doLayout(); 
        }finally{ 
            layingOut = false; 
        } 
    } 
    
    /* (non-Javadoc)
     * @see java.awt.Component#getSize()
     */
    public Dimension getSize() {
		Dimension dim = super.getSize();
		if (!layingOut && isWide()) {
			dim.width = Math.max(dim.width, getPreferredSize().width);
		}
		return dim;
	}
    
} 
