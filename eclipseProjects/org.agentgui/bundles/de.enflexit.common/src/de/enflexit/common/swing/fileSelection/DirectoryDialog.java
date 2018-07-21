package de.enflexit.common.swing.fileSelection;

import java.io.File;

import javax.swing.JDialog;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * The Class DirectoryDialog uses the {@link DirectoryEvaluator} and the 
 * {@link DirectoryPanel} to display the files found for visualization and selection.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DirectoryDialog extends JDialog {

	private static final long serialVersionUID = 4887714349156884868L;

	private DirectoryPanel jPanelDirectoryVisualisation;

	
	/**
	 * Instantiates a new directory dialog.
	 */
	public DirectoryDialog() {
		this(null);
	}
	/**
	 * Instantiates a new directory dialog.
	 * @param rootDirectory the root directory
	 */
	public DirectoryDialog(File rootDirectory) {
		this.setRootDirectory(rootDirectory);
		this.initialize();
	}
	
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jPanelDirectoryVisualisation = new GridBagConstraints();
		gbc_jPanelDirectoryVisualisation.fill = GridBagConstraints.BOTH;
		gbc_jPanelDirectoryVisualisation.gridx = 0;
		gbc_jPanelDirectoryVisualisation.gridy = 0;
		this.getContentPane().add(this.getJPanelDirectoryVisualisation(), gbc_jPanelDirectoryVisualisation);
		
		this.setModal(true);
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
	
	private DirectoryPanel getJPanelDirectoryVisualisation() {
		if (jPanelDirectoryVisualisation == null) {
			jPanelDirectoryVisualisation = new DirectoryPanel();
		}
		return jPanelDirectoryVisualisation;
	}
	
	/**
	 * Returns the DirectoryEvaluator used to show the information.
	 * Just use it to get a list of selected or unselected file elements.
	 * @return the directory evaluator
	 */
	public DirectoryEvaluator getDirectoryEvaluator() {
		return this.getJPanelDirectoryVisualisation().getDirectoryEvaluator();
	}
	
	/**
	 * Sets the root directory.
	 * @param rootDirectory the new root directory
	 */
	public void setRootDirectory(File rootDirectory) {
		this.getJPanelDirectoryVisualisation().setRootDirectory(rootDirectory);
	}
	/**
	 * Returns the currently used root directory.
	 * @return the root directory
	 */
	public File getRootDirectory() {
		return this.getJPanelDirectoryVisualisation().getRootDirectory();
	}
}
