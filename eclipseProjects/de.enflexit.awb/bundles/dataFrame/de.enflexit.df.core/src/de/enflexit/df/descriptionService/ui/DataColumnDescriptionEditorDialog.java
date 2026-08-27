package de.enflexit.df.descriptionService.ui;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.df.descriptionService.DescriptionsController;

/**
 * This dialog shows the UI components to edit data column descriptions.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataColumnDescriptionEditorDialog extends JDialog {

	private static final long serialVersionUID = 3831291234749481374L;
	
	private DescriptionEditorMainPanel editorMainPanel;
	
	private DescriptionsController descriptionsControler;
	
	/**
	 * Instantiates a new data column description editor dialog.
	 * @param owner the owner
	 * @param descriptionsController the descriptions controller
	 */
	public DataColumnDescriptionEditorDialog(Window owner, DescriptionsController descriptionsController) {
		super(owner);
		this.descriptionsControler = descriptionsController;
		this.initialize();
	}
	
	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		this.setContentPane(this.getEditorMainPanel());
		this.setTitle(Application.getApplicationTitle() + " - Data Column Description Editor");
		this.setSize(1024, 512);
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon48());
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		// --- Check for unsaved changes before closing the dialog
		this.addWindowListener(new WindowAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent we) {
				if (DataColumnDescriptionEditorDialog.this.getEditorMainPanel().allowLeaveSelection()==true) {
					DataColumnDescriptionEditorDialog.this.dispose();
				}
			}
		});
		
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	}
	
	/**
	 * Gets the editor main panel.
	 * @return the editor main panel
	 */
	public DescriptionEditorMainPanel getEditorMainPanel() {
		if (editorMainPanel==null) {
			editorMainPanel = new DescriptionEditorMainPanel(this.descriptionsControler);
		}
		return editorMainPanel;
	}
	
}
