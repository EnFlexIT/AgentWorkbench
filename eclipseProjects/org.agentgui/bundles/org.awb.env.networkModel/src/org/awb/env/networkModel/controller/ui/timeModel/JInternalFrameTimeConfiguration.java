package org.awb.env.networkModel.controller.ui.timeModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import agentgui.core.application.Language;

/**
 * The Class JInternalFrameTimeConfiguration is used to show and configure the time
 * configuration within the graph and network environment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JInternalFrameTimeConfiguration extends BasicGraphGuiJInternalFrame {

	private static final long serialVersionUID = -5440147820819475901L;
	private static final Color bgColor = SystemColor.info;
	
	
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
		this.getContentPane().setBackground(JInternalFrameTimeConfiguration.bgColor);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setIconifiable(false);

		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosing(InternalFrameEvent ife) {
				JInternalFrameTimeConfiguration.this.setVisible(false);
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
//				onResizedInternalFrame();
			}
		});
		
		// --- Remove the complete header ------- 
		((BasicInternalFrameUI)this.getUI()).setNorthPane(null);
		this.setBorder(BorderFactory.createEtchedBorder());
		
		this.setSize(new Dimension(300, 60));
		this.setPosition();
		this.setContent();
	}
	/**
	 * Sets the content of this internal frame.
	 */
	private void setContent() {
		// TODO
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
	
	
}
