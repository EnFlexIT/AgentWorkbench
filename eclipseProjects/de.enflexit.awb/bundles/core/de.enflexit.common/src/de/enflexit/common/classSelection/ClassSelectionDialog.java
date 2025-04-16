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
package de.enflexit.common.classSelection;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * This extended JDialog uses the {@link ClassSelectionPanel} in order to allow 
 * to select a specific class that extends a given super class. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ClassSelectionDialog extends JDialog implements ClassSelectionListener {

	private static final long serialVersionUID = 2L;
	
	private ClassSelectionPanel classSelectionPanel;

	
	/**
	 * This constructor is only used during developments and the usage of the WindowBuilder. 
	 */
	@Deprecated
	public ClassSelectionDialog() {
		this.initialize();
	}
	/**
	 * Constructor for an empty ClassSelectionDialog without a ClassSelectionPanel.<br>
	 * To integrate a ClassSelectionPanel use {@link #setClassSelectionPanel(ClassSelectionPanel)}
	 * @param owner the owner window 
	 */
	public ClassSelectionDialog(Window owner) {
		super(owner);
		this.initialize();
	}
	/**
	 * Constructor to configure the type of class, we are looking for.
	 *
	 * @param ownerWindow the owner window
	 * @param clazz2Search4 the clazz2 search4
	 * @param clazz2Search4CurrentValue the clazz2 search4 current value
	 * @param clazz2Search4DefaultValue the clazz2 search4 default value
	 * @param clazz2Search4Description the clazz2 search4 description
	 * @param allowNull the allow null
	 */
	public ClassSelectionDialog(Window ownerWindow, Class<?> clazz2Search4, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description, boolean allowNull) {
		super(ownerWindow);
		this.classSelectionPanel = new ClassSelectionPanel(clazz2Search4, clazz2Search4CurrentValue, clazz2Search4DefaultValue, clazz2Search4Description, allowNull);
		this.initialize();
	}
	/**
	 * Constructor to configure the type of class, we are looking for.
	 *
	 * @param ownerWindow the owner window (a Frame or a Dialog)
	 * @param jListClassSearcher an actual instance of a {@link JListClassSearcher}
	 * @param clazz2Search4CurrentValue the clazz2 search4 current value
	 * @param clazz2Search4DefaultValue the clazz2 search4 default value
	 * @param clazz2Search4Description the clazz2 search4 description
	 * @param allowNull the allow null
	 */
	public ClassSelectionDialog(Window ownerWindow, JListClassSearcher jListClassSearcher, String clazz2Search4CurrentValue, String clazz2Search4DefaultValue, String clazz2Search4Description, boolean allowNull) {
		super(ownerWindow);
		this.classSelectionPanel = new ClassSelectionPanel(jListClassSearcher, clazz2Search4CurrentValue, clazz2Search4DefaultValue, clazz2Search4Description, allowNull);
		this.initialize();
	}
	
	
	/**
	 * This method initializes this dialog.
	 */
	private void initialize() {
	
		this.setSize(730, 606);
		if (this.getClassSelectionPanel()!=null) {
			this.setContentPane(this.getClassSelectionPanel());
		}
		this.setTitle("Class-Selector");
		this.setModal(true);		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.registerEscapeKeyStroke();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				setCanceled(true);
				setVisible(false);
			}
		});
	    WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	}
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	setCanceled(true);
            	setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
	/**
	 * Sets a new class selector panel.
	 * @param newClassSelectionPanel the new class selector panel
	 */
	public void setClassSelectionPanel(ClassSelectionPanel newClassSelectionPanel) {
		
		if (this.classSelectionPanel==null) {
			this.classSelectionPanel = newClassSelectionPanel;
			this.classSelectionPanel.addClassSelectionListener(this);
			this.setContentPane(this.classSelectionPanel);
			this.validate();
			this.repaint();
			
		} else {
			if (newClassSelectionPanel!=this.classSelectionPanel) {
				this.classSelectionPanel.removeClassSelectionListener(this);
				this.classSelectionPanel = newClassSelectionPanel;
				this.classSelectionPanel.addClassSelectionListener(this);
				this.setContentPane(this.classSelectionPanel);
				this.validate();
				this.repaint();
			}
		}
	}
	/**
	 * Gets the class selector panel.
	 * @return the class selector panel
	 */
	public ClassSelectionPanel getClassSelectionPanel() {
		return classSelectionPanel;
	}
	
	/**
	 * Gets the class2 search4.
	 * @return the class2Search4
	 */
	public Class<?> getClass2Search4() {
		return this.getClassSelectionPanel().getClass2Search4();
	}
	/**
	 * Gets the class2 search4 current value.
	 * @return the class2Search4CurrentValue
	 */
	public String getClass2Search4CurrentValue() {
		return this.getClassSelectionPanel().getClass2Search4CurrentValue();
	}
	
	/**
	 * Sets the class2 search4 current value.
	 * @param newClassName the new class2 search4 current value
	 */
	public void setClass2Search4CurrentValue(String newClassName) {
		this.getClassSelectionPanel().setClass2Search4CurrentValue(newClassName);
	}
	/**
	 * Gets the class2 search4 default value.
	 * @return the class2Search4DefaultValue
	 */
	public String getClass2Search4DefaultValue() {
		return this.getClassSelectionPanel().getClass2Search4DefaultValue();
	}
	/**
	 * Gets the class2 search4 description.
	 * @return the class2Search4Description
	 */
	public String getClass2Search4Description() {
		return this.getClassSelectionPanel().getClass2Search4Description();
	}
	
	/**
	 * Checks if null or empty values are allowed.
	 * @return the allowNull
	 */
	public boolean isAllowNull() {
		return this.getClassSelectionPanel().isAllowNull();
	}
	/**
	 * Sets to allow null as a result or not.
	 * @param allowNull the allowNull to set
	 */
	public void setAllowNull(boolean allowNull) {
		this.getClassSelectionPanel().setAllowNull(allowNull);
	}

	/**
	 * Sets the canceled.
	 * @param canceled the canceled to set
	 */
	public void setCanceled(boolean canceled) {
		this.getClassSelectionPanel().setCanceled(canceled);
	}
	/**
	 * Checks if is canceled.
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return this.getClassSelectionPanel().isCanceled();
	}

	/**
	 * Sets the class selected.
	 * @param classSelected the classSelected to set
	 */
	public void setClassSelected(String classSelected) {
		this.getClassSelectionPanel().setClassSelected(classSelected);
	}
	/**
	 * Gets the class selected.
	 * @return the classSelected
	 */
	public String getClassSelected() {
		return this.getClassSelectionPanel().getClassSelected();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.classSelection.ClassSelectionListener#setSelectedClass(java.lang.String)
	 */
	@Override
	public void setSelectedClass(String classSelected) {
		this.setVisible(false);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.classSelection.ClassSelectionListener#setSelectionCanceled()
	 */
	@Override
	public void setSelectionCanceled() {
		this.setVisible(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible==true) {
			this.classSelectionPanel.addClassSelectionListener(this);
		} else {
			this.classSelectionPanel.removeClassSelectionListener(this);
		}
		super.setVisible(visible);
	}

	// ----------------------------------------------------
	// --- Used in the graph environment ------------------
	// ----------------------------------------------------
	/**
	 * Returns the JButton for the OK action.
	 * @return the JButton
	 */
	public JButton getJButtonOK() {
		return this.getClassSelectionPanel().getJButtonOK();
	}
	/**
	 * Returns the JButton for the cancel action.
	 * @return the JButton
	 */
	public JButton getJButtonCancel() {
		return this.getClassSelectionPanel().getJButtonCancel();
	}
	/**
	 * Handles the OK action click.
	 */
	public void handleOkClick() {
		this.getClassSelectionPanel().handleOkClick();
	}
	// ----------------------------------------------------
	
}
