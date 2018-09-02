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
package agentgui.core.update;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;

import java.awt.Insets;

/**
 * The Class ProjectRepositoryExplorerDialog can be used to explore a specified project repository.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ProjectRepositoryExplorerDialog extends JDialog implements ProjectRepositoryExplorerPanelListener {

	private static final long serialVersionUID = -8466682987118948831L;
	
	private ProjectRepositoryExplorerPanel projectRepositoryExplorerPanel;

	/**
	 * Instantiates a new project repository explorer.
	 * @param window the window of this dialog
	 */
	public ProjectRepositoryExplorerDialog(Window window) {
		super(window);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_ExplorerPanel = new GridBagConstraints();
		gbc_ExplorerPanel.insets = new Insets(15, 15, 15, 15);
		gbc_ExplorerPanel.fill = GridBagConstraints.BOTH;
		gbc_ExplorerPanel.gridx = 0;
		gbc_ExplorerPanel.gridy = 0;
		this.getContentPane().add(this.getProjectRepositoryExplorerPanel(), gbc_ExplorerPanel);
		
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		this.setTitle(Application.getGlobalInfo().getApplicationTitle() + ": Project-Repository Explorer");
		this.setSize(1200, 750);
		this.setLocationRelativeTo(null);
		this.registerEscapeKeyStroke();
		this.setModal(true);
		this.setVisible(true);
		
	}
	
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	/**
	 * Gets the project repository explorer panel.
	 * @return the project repository explorer panel
	 */
	private ProjectRepositoryExplorerPanel getProjectRepositoryExplorerPanel() {
		if (projectRepositoryExplorerPanel == null) {
			projectRepositoryExplorerPanel = new ProjectRepositoryExplorerPanel(this);
		}
		return projectRepositoryExplorerPanel;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.update.ProjectRepositoryExplorerPanelListener#closeProjectRepositoryExplorer()
	 */
	@Override
	public void closeProjectRepositoryExplorer() {
		this.setVisible(false);
		this.dispose();
	}
}
