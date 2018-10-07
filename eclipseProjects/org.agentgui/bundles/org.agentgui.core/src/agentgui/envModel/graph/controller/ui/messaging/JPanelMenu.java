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
package agentgui.envModel.graph.controller.ui.messaging;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.controller.ui.messaging.MessagingJInternalFrame.WidgetOrientation;

/**
 * The Class JPanleStates represents the visualization for state indications.
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class JPanelMenu extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 1110726364983505331L;

	private final Dimension buttonSize = new Dimension(20, 20);
	
	private MessagingJInternalFrame messagingFrame;
	
	private JButton jButtonClose;
	
	private JToggleButton jButtonOrientationBottom;
	private JToggleButton jButtonOrientationRight;
	private JToggleButton jButtonOrientationTop;
	private JToggleButton jButtonOrientationLeft;
	
	/**
	 * Instantiates a new messaging state panel.
	 *
	 * @param messagingFrame the messaging frame
	 */
	public JPanelMenu(MessagingJInternalFrame messagingFrame) {
		this.messagingFrame = messagingFrame;
		this.initialize();
	}
	/**
	 * Initializes the panel.
	 */
	private void initialize() {
		
		this.setFloatable(false);
		this.setBackground(MessagingJInternalFrame.bgColor);
		this.setWidgetOrientation(this.messagingFrame.getWidgetOrientation());
	}
	
	/**
	 * Adds the tool bar buttons.
	 */
	private void addToolBarButtons() {
		
		if (this.getComponentCount()>0) this.removeAll();
		
		switch (this.messagingFrame.getWidgetOrientation()) {
		case Bottom:
		case Top:
			
			this.setOrientation(JToolBar.VERTICAL);
			
			this.add(this.getJButtonClose());
			this.addSeparator();
			this.add(this.getJButtonOrientationTop());
			this.add(this.getJButtonOrientationBottom());
			this.add(this.getJButtonOrientationLeft());
			this.add(this.getJButtonOrientationRight());
			
			break;
			
		case Left:
		case Right:
			
			this.setOrientation(JToolBar.HORIZONTAL);
			
			this.add(this.getJButtonOrientationTop());
			this.add(this.getJButtonOrientationBottom());
			this.add(this.getJButtonOrientationLeft());
			this.add(this.getJButtonOrientationRight());

			this.add(Box.createHorizontalGlue());
			this.addSeparator();
			this.add(this.getJButtonClose());
			
			break;
		}
		this.validate();
		this.repaint();
	}
	
	/**
	 * Returns the close button.
	 * @return the close button
	 */
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setIcon(GlobalInfo.getInternalImageIcon("MBclose.png"));
			jButtonClose.setToolTipText(Language.translate("Close Messaging Widget", Language.EN));
			jButtonClose.setPreferredSize(this.buttonSize);
			jButtonClose.setBorder(null);
			jButtonClose.setMargin(new Insets(0, 0, 0, 0));
			jButtonClose.addActionListener(this);
		}
		return jButtonClose;
	}
	
	private JToggleButton getJButtonOrientationBottom() {
		if (jButtonOrientationBottom== null) {
			jButtonOrientationBottom = new JToggleButton();
			jButtonOrientationBottom.setIcon(new ImageIcon(getClass().getResource(GraphGlobals.getPathImages() + "OrientationBottom.png")));
			jButtonOrientationBottom.setToolTipText(Language.translate("Widget Orientation: Bottom", Language.EN));
			jButtonOrientationBottom.setPreferredSize(this.buttonSize);
			jButtonOrientationBottom.setMargin(new Insets(0, 0, 0, 0));
			jButtonOrientationBottom.addActionListener(this);
		}
		return jButtonOrientationBottom;
	}
	private JToggleButton getJButtonOrientationTop() {
		if (jButtonOrientationTop== null) {
			jButtonOrientationTop = new JToggleButton();
			jButtonOrientationTop.setIcon(new ImageIcon(getClass().getResource(GraphGlobals.getPathImages() + "OrientationTop.png")));
			jButtonOrientationTop.setToolTipText(Language.translate("Widget Orientation: Top", Language.EN));
			jButtonOrientationTop.setPreferredSize(this.buttonSize);
			jButtonOrientationTop.setMargin(new Insets(0, 0, 0, 0));
			jButtonOrientationTop.addActionListener(this);
		}
		return jButtonOrientationTop;
	}
	private JToggleButton getJButtonOrientationLeft() {
		if (jButtonOrientationLeft == null) {
			jButtonOrientationLeft = new JToggleButton();
			jButtonOrientationLeft.setIcon(new ImageIcon(getClass().getResource(GraphGlobals.getPathImages() + "OrientationLeft.png")));
			jButtonOrientationLeft.setToolTipText(Language.translate("Widget Orientation: Left", Language.EN));
			jButtonOrientationLeft.setPreferredSize(this.buttonSize);
			jButtonOrientationLeft.setMargin(new Insets(0, 0, 0, 0));
			jButtonOrientationLeft.addActionListener(this);
		}
		return jButtonOrientationLeft;
	}
	private JToggleButton getJButtonOrientationRight() {
		if (jButtonOrientationRight == null) {
			jButtonOrientationRight = new JToggleButton();
			jButtonOrientationRight.setIcon(new ImageIcon(getClass().getResource(GraphGlobals.getPathImages() + "OrientationRight.png")));
			jButtonOrientationRight.setToolTipText(Language.translate("Widget Orientation: Right", Language.EN));
			jButtonOrientationRight.setPreferredSize(this.buttonSize);
			jButtonOrientationRight.setMargin(new Insets(0, 0, 0, 0));
			jButtonOrientationRight.addActionListener(this);
		}
		return jButtonOrientationRight;
	}

	/**
	 * Sets the widget orientation.
	 * @param widgetOrientation the new widget orientation
	 */
	protected void setWidgetOrientation(WidgetOrientation widgetOrientation) {

		this.getJButtonOrientationBottom().setSelected(false);
		this.getJButtonOrientationRight().setSelected(false);
		this.getJButtonOrientationTop().setSelected(false);
		this.getJButtonOrientationLeft().setSelected(false);
		
		switch (widgetOrientation) {
		case Bottom:
			this.getJButtonOrientationBottom().setSelected(true);
			break;
		case Top:
			this.getJButtonOrientationTop().setSelected(true);
			break;
		case Left:
			this.getJButtonOrientationLeft().setSelected(true);
			break;
		case Right:
			this.getJButtonOrientationRight().setSelected(true);
			break;
		}
		this.messagingFrame.setWidgetOrientation(widgetOrientation);
		this.addToolBarButtons();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJButtonClose()) {
			this.messagingFrame.setVisible(false);
		} else if (ae.getSource()==this.getJButtonOrientationBottom()) {
			this.setWidgetOrientation(WidgetOrientation.Bottom);
		} else if (ae.getSource()==this.getJButtonOrientationTop()) {
			this.setWidgetOrientation(WidgetOrientation.Top);
		} else if (ae.getSource()==this.getJButtonOrientationLeft()) {
			this.setWidgetOrientation(WidgetOrientation.Left);
		} else if (ae.getSource()==this.getJButtonOrientationRight()) {
			this.setWidgetOrientation(WidgetOrientation.Right);
		}
		
	}
}
