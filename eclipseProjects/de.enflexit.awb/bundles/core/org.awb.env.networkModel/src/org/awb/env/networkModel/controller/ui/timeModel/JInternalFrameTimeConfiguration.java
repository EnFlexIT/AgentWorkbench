package org.awb.env.networkModel.controller.ui.timeModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;

import de.enflexit.language.Language;
import de.enflexit.awb.simulation.environment.time.JPanel4TimeModelConfiguration;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.awb.simulation.environment.time.TimeModelContinuous;
import de.enflexit.awb.simulation.environment.time.TimeModelDiscrete;
import de.enflexit.awb.simulation.environment.time.TimeModelPresent;
import de.enflexit.awb.simulation.environment.time.TimeModelStroke;

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
	
	private JScrollPane jScrollPaneContent;
	private JComponent jComponentTimeModel;
	
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> graphVisualizationPanel;
	private ComponentListener graphVisualizationPanelListener;

	
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
		
		this.setContentPane(this.getJScrollPaneContent());
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
		int height = (int) this.getJComponentTimeModel().getPreferredSize().getHeight() + 5;
		
		// --- Keep height value if higher ------
		if (this.getHeight()>height) height = this.getHeight();

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
			// --- Listen to component changes of the graph visualization viewer --------
			this.getGraphVisualizationPanel();
		}
	}

	
	/**
	 * Sets the content of this internal frame.
	 */
	private void setContent() {
		this.getJScrollPaneContent().remove(this.getJComponentTimeModel());
		this.getJScrollPaneContent().setViewportView(this.getJComponentTimeModel());
		this.getContentPane().validate();
		this.getContentPane().repaint();
	}
	
	public JScrollPane getJScrollPaneContent() {
		if (jScrollPaneContent==null) {
			jScrollPaneContent = new JScrollPane();
			jScrollPaneContent.setBorder(BorderFactory.createEmptyBorder());
		}
		return jScrollPaneContent;
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
				jComponentTimeModel = new JPanelTimeModelStroke(this.graphController.getProject(), null);
			} else if (timeModel instanceof TimeModelDiscrete) {
				jComponentTimeModel = new JPanelTimeModelDiscrete(this.graphController.getProject(), null);
			} else if (timeModel instanceof TimeModelPresent) {
				jComponentTimeModel = new JPanelTimeModelPresent(this.graphController.getProject(), null);
			} else if (timeModel instanceof TimeModelContinuous) {
				jComponentTimeModel = new JPanelTimeModelContinuous(this.graphController.getProject(), null);
			}
			jComponentTimeModel.setBackground(JInternalFrameTimeConfiguration.bgColor);
		}
		return jComponentTimeModel;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiJInternalFrame#dispose()
	 */
	@Override
	public void dispose() {
		if (jComponentTimeModel!=null && jComponentTimeModel instanceof JPanel4TimeModelConfiguration) {
			((JPanel4TimeModelConfiguration)jComponentTimeModel).deleteAsProjectObserver();
		}
		if (graphVisualizationPanel!=null) {
			graphVisualizationPanel.removeComponentListener(this.getGraphVisualizationPanelListener());
		}
		super.dispose();
	}
	
	
	/**
	 * Returns the graph visualization panel.
	 * @return the graph visualization panel
	 */
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> getGraphVisualizationPanel() {
		if (graphVisualizationPanel==null) {
			graphVisualizationPanel = this.basicGraphGui.getVisualizationViewer();
			graphVisualizationPanel.addComponentListener(this.getGraphVisualizationPanelListener());
		}
		return graphVisualizationPanel;
	}
	/**
	 * Returns the graph visualization panel listener.
	 * @return the graph visualization panel listener
	 */
	private ComponentListener getGraphVisualizationPanelListener() {
		if (graphVisualizationPanelListener==null) {
			graphVisualizationPanelListener = new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					JInternalFrameTimeConfiguration.this.setPosition();
					JInternalFrameTimeConfiguration.this.setSize();
				}
			};
		}
		return graphVisualizationPanelListener;
	}
	
}
