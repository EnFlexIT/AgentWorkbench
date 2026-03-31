package de.enflexit.df.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.common.swing.WindowSizeAndPostionController;
import de.enflexit.common.swing.WindowSizeAndPostionController.JDialogPosition;

/**
 * The Class JFrameDataView.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JFrameDataView extends JFrame {

	private static final long serialVersionUID = 4308063704167480118L;

	private Dimension ownerSize;
	private JPanelData jPanelData;
	
	/**
	 * Instantiates a new JFrameDataView.
	 */
	public JFrameDataView() {
		this(null);
	}
	/**
	 * Instantiates a new JFrameDataView.
	 * @param owner the owner window
	 */
	public JFrameDataView(Window owner) {
		if (owner!=null) this.ownerSize = owner.getSize();
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setTitle(Application.getApplicationTitle() + " - Data Viewer");
		this.setSize(this.getDialogDimension());
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon48());
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.getContentPane().add(this.getJPanelData(), BorderLayout.CENTER);
		
		WindowSizeAndPostionController.setJDialogPositionOnScreen(this, JDialogPosition.ParentCenter);
		
	}
	/**
	 * Returns the dialog dimension.
	 * @return the dialog dimension
	 */
	private Dimension getDialogDimension() {
		Dimension dialogDim = new Dimension();
		if (this.ownerSize!=null) {
			double scale = 3. / 4.;
			dialogDim.setSize(this.ownerSize.getWidth() * scale, this.ownerSize.getHeight() * scale);
		} else {
			dialogDim.setSize(960, 620);
		}
		return dialogDim;
	}
	
	/**
	 * Returns the JPanelData.
	 * @return the j panel data
	 */
	private JPanelData getJPanelData() {
		if (jPanelData==null) {
			jPanelData = new JPanelData();
		}
		return jPanelData;
	}
	
}
