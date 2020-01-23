package org.awb.env.networkModel.controller.ui.timeModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import agentgui.core.application.Language;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelContinuous;
import agentgui.simulationService.time.TimeModelDiscrete;
import agentgui.simulationService.time.TimeModelPresent;
import agentgui.simulationService.time.TimeModelStroke;

/**
 * The Class JInternalFrameTimeConfiguration is used to show and configure the time
 * configuration within the graph and network environment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JInternalFrameTimeConfiguration extends BasicGraphGuiJInternalFrame {

	private static final long serialVersionUID = -5440147820819475901L;
	private static final Color bgColor = SystemColor.info;
	
	private JLabel jLabelNoTimeModel;
	private JComponent jComponentTimeModel;
	
	
	/**
	 * Instantiates a new JInternalFrameTimeConfiguration.
	 * @param graphController the current {@link GraphEnvironmentController}
	 */
	public JInternalFrameTimeConfiguration(GraphEnvironmentController graphController) {
		super(graphController);
		this.initialize();
	}
	/**
	 * Initialize the internal frame.
	 */
	private void initialize() {
		
		this.setTitle(Language.translate("Time Configuration", Language.EN));
		
		this.setAutoscrolls(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setIconifiable(false);
		this.setClosable(true);

		
		this.setSize(900, 90);
		this.setBorder(BorderFactory.createEtchedBorder());
		((BasicInternalFrameUI)this.getUI()).setNorthPane(null);

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		this.setContent();
		this.setPosition();
		this.setSize();
	}
	
	/**
	 * Sets the position of this internal frame.
	 */
	private void setPosition() {

		BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer = this.basicGraphGui.getVisualizationViewer();
		if (this.graphDesktop!=null && visViewer!=null) {
			// --- Get the initial x-position of the property window ----------
			int posBasicGraphGui = this.basicGraphGui.getLocation().x;
			int posVisViewOnBasicGraphGui = visViewer.getParent().getLocation().x; 

			int myLocationX = posBasicGraphGui + posVisViewOnBasicGraphGui;
			int myLocationY = (int) visViewer.getLocation().getX();
			this.setLocation(myLocationX, myLocationY);
			
		} else {
			this.setLocation(0, 0);
		}
	}
	
	/**
	 * Sets the size.
	 */
	private void setSize() {
		int width  = this.basicGraphGui.getVisualizationViewer().getWidth();
		int height = (int) this.getJComponentTimeModel().getPreferredSize().getHeight(); 
		this.setSize(width, height);
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame#isRemindAsLastOpenedEditor()
	 */
	@Override
	protected boolean isRemindAsLastOpenedEditor() {
		return false;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame#registerAtDesktopAndSetVisible()
	 */
	@Override
	public void registerAtDesktopAndSetVisible() {
		if (this.isVisible()==false) {
			super.registerAtDesktopAndSetVisible();
		}
	}

	
	/**
	 * Sets the content of this internal frame.
	 */
	private void setContent() {
		JComponent jComponentTimeModel = this.getJComponentTimeModel();
		this.getContentPane().remove(jComponentTimeModel);
		this.getContentPane().add(jComponentTimeModel, BorderLayout.CENTER);
		this.getContentPane().validate();
		this.getContentPane().repaint();
	}
	private JLabel getJLabelNoTimeModel() {
		if (jLabelNoTimeModel == null) {
			jLabelNoTimeModel = new JLabel("No Time Model specified.");
			jLabelNoTimeModel.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelNoTimeModel.setPreferredSize(new Dimension(200, 90));
		}
		return jLabelNoTimeModel;
	}
	private JComponent getJComponentTimeModel() {
		if (jComponentTimeModel==null) {
			// --- Default ------------------------------------------
			jComponentTimeModel = this.getJLabelNoTimeModel(); 
			// --- Get the current time model -----------------------
			TimeModel timeModel = this.graphController.getTimeModel();
			if (timeModel == null) {
				// --- Equal to the default case (see above) --------
			} else if (timeModel instanceof TimeModelStroke) {
				jComponentTimeModel = new JPanelTimeModelStroke(this.graphController.getProject());
			} else if (timeModel instanceof TimeModelDiscrete) {
				jComponentTimeModel = new JPanelTimeModelDiscrete(this.graphController.getProject());
			} else if (timeModel instanceof TimeModelPresent) {
				jComponentTimeModel = new JPanelTimeModelPresent(this.graphController.getProject());
			} else if (timeModel instanceof TimeModelContinuous) {
				jComponentTimeModel = new JPanelTimeModelContinuous(this.graphController.getProject());
			}
			jComponentTimeModel.setBackground(JInternalFrameTimeConfiguration.bgColor);
		}
		return jComponentTimeModel;
	}
}
