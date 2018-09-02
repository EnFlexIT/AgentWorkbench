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
package agentgui.core.charts.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import de.enflexit.common.ontology.gui.DynForm;
/**
 * Abstract super class for dialogs for chart viewing and editing. 
 * ChartEditorDialogs are containers for ChartEditorJPanel implementations for 
 * the corresponding type of chart, all functionality should be implemented in
 * the panel.
 * 
 * @author Nils
 *
 */
public abstract class ChartEditorJDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1820851101239120387L;
	
	private Image imageAgentGUI = GlobalInfo.getInternalImageAwbIcon16();
	
	/** The Dialog is just a container for a ChartEditorJPanel implementation that "does the work" */
	protected ChartEditorJPanel contentPane;
	
	// Swing components
	protected JPanel buttonPane;
	protected JButton btnClose;
	
	protected DynForm dynForm = null;  //  @jve:decl-index=0:
	protected int startArgIndex = -1;
	
	
	/**
	 * Instantiates a new JDialog as chart editor .
	 *
	 * @param dynForm the {@link DynForm}
	 * @param startArgIndex the start argument index
	 */
	public ChartEditorJDialog(DynForm dynForm, int startArgIndex) {
		
		this.dynForm = dynForm;
		this.startArgIndex = startArgIndex;
		
		this.setModal(true);
		this.setSize(600, 450);
		this.setIconImage(imageAgentGUI);
		this.setTitle(Language.translate("Edit Chart", Language.EN));
		
		// --- center dialog --------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);	


	    this.setContentPane(this.getContentPane());
		this.getContentPane().add(getButtonPane(), BorderLayout.SOUTH);

	}
	
	protected JPanel getButtonPane(){
		if(buttonPane == null){
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.add(getBtnClose());
		}
		return buttonPane;
	}
	
	protected JButton getBtnClose(){
		if(btnClose == null){
			btnClose = new JButton(Language.translate("Close", Language.EN));
			btnClose.setFont(new Font("Dialog", Font.BOLD, 12));
			btnClose.setForeground(new Color(0, 0 ,153));
			btnClose.addActionListener(this);
		}
		return btnClose;
	}
	

	/**
	 * Gets the dialogs content pane, which must be a ChartEditorJPanel implementation. 
	 */
	public abstract ChartEditorJPanel getContentPane();

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == getBtnClose()){
			this.setVisible(false);
		}
	}
	
}
