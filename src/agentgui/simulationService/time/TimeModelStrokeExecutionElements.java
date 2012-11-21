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
package agentgui.simulationService.time;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Language;

/**
 * The Class TimeModelStrokeExecutionElements.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStrokeExecutionElements extends JToolBarElements4TimeModelExecution {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 385807623783469748L;

	public final static int viewTimer = 0;
	public final static int viewCountdown = 1;
	private int view = 0;
	
	private TimeModelStroke timeModelStroke = null;
	
	private JLabel jLabelIntro = null;
	private JLabel jLabelTimeDisplay = null;
	
	private JButton jButtonTimeConfig=null;
	private JPopupMenu jPopupMenuViewConfiguration=null;
	private JMenuItem jMenuItemViewTimer=null;	
	private JMenuItem jMenuItemViewCountdown=null;
		
		
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.JToolBarElements4TimeModelExecution#addToolbarElements()
	 */
	@Override
	public void addToolbarElements() {
		
		this.jToolBar4Elements.add(this.getJLabelIntro());
		this.jToolBar4Elements.add(this.getJTextFieldTimeDisplay());
		this.jToolBar4Elements.add(this.getSeparator(1));
		this.jToolBar4Elements.add(this.getJButtonTimeConfig());
		
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.JToolBarElements4TimeModelExecution#removeToolbarElements()
	 */
	@Override
	public void removeToolbarElements() {
		
		this.jToolBar4Elements.remove(this.getJLabelIntro());
		this.jToolBar4Elements.remove(this.getJTextFieldTimeDisplay());
		this.jToolBar4Elements.remove(this.getJButtonTimeConfig());
		this.removeAllSeparator();
		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.JToolBarElements4TimeModelExecution#setTimeModel(agentgui.simulationService.time.TimeModel)
	 */
	@Override
	public void setTimeModel(TimeModel timeModel) {
		
		this.timeModelStroke = (TimeModelStroke) timeModel;
		if (this.timeModelStroke==null) {
			this.getJTextFieldTimeDisplay().setText("");			

		} else {
			
			Integer counter = this.timeModelStroke.getCounter();
			Integer counterStop = this.timeModelStroke.getCounterStop();
			switch (this.view) {
			case viewCountdown:
				// --- countdown view -----------
				counter = counterStop-counter;
				this.getJTextFieldTimeDisplay().setText((counter.toString()));
				break;
				
			default:
				// --- timer view ---------------
				this.getJTextFieldTimeDisplay().setText(counter.toString());
				break;
			}
			
		}

	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.JToolBarElements4TimeModelExecution#getTimeModel()
	 */
	@Override
	public TimeModel getTimeModel() {
		return this.timeModelStroke;
	}
	
	/**
	 * Gets the intro.
	 * @return the time intro
	 */
	private JLabel getJLabelIntro() {
		if (this.jLabelIntro==null) {
			jLabelIntro = new JLabel("Zähler");
			jLabelIntro.setText(" " + Language.translate(jLabelIntro.getText()) + ": ");
			jLabelIntro.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return this.jLabelIntro;
	}
	/**
	 * Returns the time display.
	 * @return the time display
	 */
	private JLabel getJTextFieldTimeDisplay() {
		if (this.jLabelTimeDisplay==null) {
			jLabelTimeDisplay = new JLabel();
			jLabelTimeDisplay.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelTimeDisplay.setPreferredSize(new Dimension(60, 26));
		}
		return this.jLabelTimeDisplay;
	}
	/**
	 * Gets the button for the time configuration.
	 * @return the button for the time configuration
	 */
	private JButton getJButtonTimeConfig() {
		if (jButtonTimeConfig==null) {
			jButtonTimeConfig = new JButton();
			jButtonTimeConfig.setText(Language.translate("Ansicht"));
			jButtonTimeConfig.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonTimeConfig.setSize(36, 36);
			jButtonTimeConfig.addActionListener(this);	
		}
		return jButtonTimeConfig;
	}
	/**
	 * Gets the j popup menu view configuration.
	 * @return the j popup menu view configuration
	 */
	private JPopupMenu getJPopupMenuViewConfiguration(){
		if (jPopupMenuViewConfiguration==null) {
			jPopupMenuViewConfiguration = new JPopupMenu("Time-Configuration");
			jPopupMenuViewConfiguration.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) );
			jPopupMenuViewConfiguration.add(this.getJMenuItemViewTimer());
			jPopupMenuViewConfiguration.add(this.getJMenuItemViewCountdown());
		}
		return jPopupMenuViewConfiguration;
	}
	/**
	 * Gets the j menu item view timer.
	 * @return the j menu item view timer
	 */
	private JMenuItem getJMenuItemViewTimer() {
		if (jMenuItemViewTimer==null) {
			jMenuItemViewTimer=new JMenuItem();
			jMenuItemViewTimer.setText(Language.translate("Timer-Ansicht"));
			jMenuItemViewTimer.addActionListener(this);
		}
		return jMenuItemViewTimer;
	}
	/**
	 * Gets the j menu item view Countdown.
	 * @return the j menu item view Countdown
	 */
	private JMenuItem getJMenuItemViewCountdown() {
		if (jMenuItemViewCountdown==null) {
			jMenuItemViewCountdown=new JMenuItem();
			jMenuItemViewCountdown.setText(Language.translate("Countdown-Ansicht"));
			jMenuItemViewCountdown.addActionListener(this);
		}
		return jMenuItemViewCountdown;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object trigger = ae.getSource();
		if (trigger==this.getJButtonTimeConfig()) {
			this.getJPopupMenuViewConfiguration().show(this.getJButtonTimeConfig(), 0, this.getJButtonTimeConfig().getHeight());
			
		} else if (trigger==this.getJMenuItemViewTimer()) {
			this.view = viewTimer;
			this.setTimeModel(this.getTimeModel());
			
		} else if (trigger==this.getJMenuItemViewCountdown()) {
			this.view = viewCountdown;
			this.setTimeModel(this.getTimeModel());
		}
		
	}

}
