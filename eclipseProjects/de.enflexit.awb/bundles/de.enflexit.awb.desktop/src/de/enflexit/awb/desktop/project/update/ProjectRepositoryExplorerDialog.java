package de.enflexit.awb.desktop.project.update;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.common.http.WebResourcesAuthorization;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

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
		
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
		this.setTitle(Application.getGlobalInfo().getApplicationTitle() + ": Project-Repository Explorer");
		this.registerEscapeKeyStroke();
		this.setModal(true);

		this.setContentElements();
		
		this.setSize(1200, 750);
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		
		this.setVisible(true);
	}
	
	private void setContentElements() {
		
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
	/**
	 * Returns the last used {@link WebResourcesAuthorization}.
	 * @return the last used authorization
	 */
	public WebResourcesAuthorization getLastUsedAuthorization() {
		return this.getProjectRepositoryExplorerPanel().getLastUsedAuthorization();
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
