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
package agentgui.simulationService.load.threading;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;


/**
 * The Class ThreadMeasureDialog.
 */
public class ThreadMeasureDialog extends JFrame {

	private static final long serialVersionUID = -5535823475614083190L;
	
	private JTabbedPane tabbedPane;
	
	private ThreadProtocolVector threadProtocolVector;
	private ThreadMeasureProtocolTab jPanelMeasureProtocol;

	
	/**
	 * Instantiates a new thread measure dialog.
	 * @param threadProtocolVector the thread protocol vector
	 */
	public ThreadMeasureDialog(ThreadProtocolVector threadProtocolVector) {
		this.threadProtocolVector = threadProtocolVector;
		this.initialize();
	}
	
	private void initialize() {
		
		this.setSize(800, 600);
		
		
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		getContentPane().add(getTabbedPane());
		
		// --- Set Dialog position ----------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);			
	    
	    this.setVisible(true);
		
	}
	
	

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("Measurement", null, getJPanelMeasureProtocol(), null);
		}
		return tabbedPane;
	}
	public ThreadMeasureProtocolTab getJPanelMeasureProtocol() {
		if (jPanelMeasureProtocol == null) {
			jPanelMeasureProtocol = new ThreadMeasureProtocolTab(this.threadProtocolVector);
		}
		return jPanelMeasureProtocol;
	}
}
