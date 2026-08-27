package de.enflexit.df.descriptionService.ui;

import java.awt.Window;
import java.util.HashMap;

import javax.swing.JDialog;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;
import de.enflexit.df.descriptionService.DescriptionsController;
import de.enflexit.df.descriptionService.db.DataColumnDescription;

/**
 * This dialog shows the UI components to edit data column descriptions.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataColumnDescriptionEditorDialog extends JDialog {

	private static final long serialVersionUID = 3831291234749481374L;
	
	private DataColumnDescriptionEditorMainPanel mainPanel;
	
	private DescriptionsController descriptionsControler;
	
	public DataColumnDescriptionEditorDialog(Window owner, DescriptionsController descriptionsController) {
		super(owner);
		this.descriptionsControler = descriptionsController;
		this.initialize();
	}
	
	private void initialize() {
		this.setContentPane(this.getMainPanel());
		this.setTitle(Application.getApplicationTitle() + " - Data Column Description Editor");
		this.setSize(1024, 512);
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon48());
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
	}
	
	public DataColumnDescriptionEditorMainPanel getMainPanel() {
		if (mainPanel==null) {
			mainPanel = new DataColumnDescriptionEditorMainPanel(this.descriptionsControler);
		}
		return mainPanel;
	}
	
	public HashMap<String, DataColumnDescription> getDataColumnDescriptions() {
		return this.getMainPanel().getDataColumnDescriptions();
	}

	public void setDataColumnDescriptions(HashMap<String, DataColumnDescription> dataColumnDescriptions) {
		this.getMainPanel().setDataColumnDescriptions(dataColumnDescriptions);
	}

}
