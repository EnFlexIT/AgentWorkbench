package de.enflexit.awb.baseUI.monitor.load;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.enflexit.awb.core.config.GlobalInfo;
import de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad;
import de.enflexit.awb.simulation.agents.LoadMeasureAgent;
import de.enflexit.awb.simulation.load.LoadInformation.NodeDescription;
import de.enflexit.awb.simulation.ontology.PlatformLoad;
import de.enflexit.language.Language;

/**
 * This is the dialog window for displaying the current system load on
 * the platform and is used by the {@link LoadMeasureAgent}.
 * 
 * @see LoadMeasureAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SystemLoadDialog extends JFrame implements AwbMonitoringDialogSystemLoad {

	private static final long serialVersionUID = 3170514914879967107L;

	private LoadMeasureAgent loadMeasureAgent;
	private SystemLoadPanel systemLoadPanel;

	// --- Remember container Informations/Instances (Display) ------
	private Hashtable<String, ContainerLoadPanel> containerLoadPanelHashMap; 
	
	private int dialogTitleHeight = 38;
	private int dialogHeight = 0;

	
	/**
	 * Instantiates a new system load dialog.
	 * @param loadMeasureAgent the load measure agent
	 */
	public SystemLoadDialog(LoadMeasureAgent loadMeasureAgent) {
		super();
		this.loadMeasureAgent = loadMeasureAgent;
		this.initialize();
	}

	/**
	 * This method initialises this.
	 */
	private void initialize() {
		
		this.setSize(620, 120);
		this.setIconImage(GlobalInfo.getInternalImageAwbIcon16());
	    this.setTitle("Agent.Workbench: " + Language.translate("Load Monitor"));
		this.setContentPane(this.getSystemLoadPanel());		
		
		// --- Add a WindowsListener --------------------------------
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		this.setVisible(true);
	}

	/**
	 * Returns the system load panel.
	 * @return the system load panel
	 */
	private SystemLoadPanel getSystemLoadPanel() {
		if (systemLoadPanel==null) {
			systemLoadPanel = new SystemLoadPanel(this.loadMeasureAgent);
		}
		return systemLoadPanel;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad#setNumberOfAgents(int)
	 */
	@Override
	public void setNumberOfAgents(int noOfAgentsOnPlatform) {
		this.getSystemLoadPanel().setNumberOfAgents(noOfAgentsOnPlatform);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad#setNumberOfContainer(int)
	 */
	@Override
	public void setNumberOfContainer(int noOfContainer) {
		this.getSystemLoadPanel().setNumberOfContainer(noOfContainer);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad#setCycleTime(double)
	 */
	@Override
	public void setCycleTime(double cycleTime) {
		this.getSystemLoadPanel().setCycleTime(cycleTime);
	}

	
	/**
	 * Gets the container load panel hash map.
	 * @return the container load panel hash map
	 */
	private Hashtable<String, ContainerLoadPanel> getContainerLoadPanelHashMap() {
		if (containerLoadPanelHashMap==null) {
			containerLoadPanelHashMap = new Hashtable<String, ContainerLoadPanel>();
		}
		return containerLoadPanelHashMap;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad#updateContainerLoad(java.lang.String, de.enflexit.awb.simulation.load.LoadInformation.NodeDescription, float, de.enflexit.awb.simulation.ontology.PlatformLoad, java.lang.Integer)
	 */
	@Override
	public void updateContainerLoad(String containerName, final NodeDescription nodeDescription, final float benchmarkValue, final PlatformLoad containerLoad, final Integer noOfAgents) {
		
		ContainerLoadPanel dialogSingle = this.getContainerLoadPanelHashMap().get(containerName);
		if (dialogSingle==null) {
			dialogSingle = new ContainerLoadPanel();
			if (getSystemLoadPanel()!=null) {
				getSystemLoadPanel().getJPanelForLoadDisplays().add(dialogSingle, null);	
			}
			this.getContainerLoadPanelHashMap().put(containerName, dialogSingle);
		}
		
		if (containerLoad==null) {
			dialogSingle.setVisibleAWTsafe(false);								
		} else {
			dialogSingle.setVisibleAWTsafe(true);
			dialogSingle.updateViewAWTsafe(containerName, nodeDescription, benchmarkValue, containerLoad, noOfAgents);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad#refreshViewAfterContainerLoadUpdate()
	 */
	@Override
	public void refreshViewAfterContainerLoadUpdate() {
		
		int visibleContainerPanel = this.getVisibleContainerPanel();
		
		int newloadDialogHeightMax = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int newloadDialogHeight = (ContainerLoadPanel.loadPanelHeight * visibleContainerPanel) + (getSystemLoadPanel().getJToolBarLoad().getHeight() + this.dialogTitleHeight);
		if (newloadDialogHeight>newloadDialogHeightMax) {
			newloadDialogHeight=newloadDialogHeightMax;
		}
		if (this.dialogHeight!=newloadDialogHeight) {
			this.dialogHeight = newloadDialogHeight;
			this.setSize(this.getWidth(), newloadDialogHeight);						
		}
	}
	/**
	 * Returns the number of visible container panel.
	 * @return the visible container panel
	 */
	private int getVisibleContainerPanel() {
		
		int visibleCounter = 0;
		for (ContainerLoadPanel containerPanel : this.getContainerLoadPanelHashMap().values()) {
			if (containerPanel.isVisible()==true) visibleCounter++;
		}
		return visibleCounter;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad#alternateRecordingIndicator()
	 */
	@Override
	public void alternateRecordingIndicator() {
		if (this.getSystemLoadPanel().jLabelRecord.getForeground().equals(Color.gray) ) {
			this.getSystemLoadPanel().jLabelRecord.setForeground(Color.red);
		} else {
			this.getSystemLoadPanel().jLabelRecord.setForeground(Color.gray);
		}	
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.core.ui.AwbMonitoringDialogSystemLoad#setLoadRecordingAtSystemStart(boolean, long)
	 */
	@Override
	public void setLoadRecordingAtSystemStart(boolean doLoadRecording, long loadRecordingInterval) {
		this.getSystemLoadPanel().setRecordingInterval(loadRecordingInterval);
		this.getSystemLoadPanel().setDoLoadRecording(doLoadRecording);
	}	
}  
