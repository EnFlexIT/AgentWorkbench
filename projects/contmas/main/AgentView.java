/**
 * 
 */
package contmas.main;

import jade.core.AID;
import jade.gui.GuiEvent;
import jade.gui.AgentTree.AgentNode;

import javax.media.j3d.Canvas3D;
import javax.swing.JInternalFrame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JApplet;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;

import contmas.agents.MonitorAgent;
import contmas.de.unidue.stud.sehawagn.BayMapRenderer;
import contmas.ontology.ContainerHolder;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JDesktopPane;

/**
 * @author hfw15
 *
 */
public class AgentView extends JInternalFrame {

	private JSplitPane controlSplit = null;
	private JPanel controlPanel = null;
	private JButton refreshButton = null;
	private MonitorAgent myAgent;
	private AID monitoredAgent;  //  @jve:decl-index=0:
	private ContainerHolder ontRep;
	private JPanel jPanel = null;
	/**
	 * This method initializes 
	 * 
	 */
	public AgentView(MonitorAgent a,AID monitoredAgent) {
		super();
		initialize();
		myAgent=a;
		setMonitoredAgent(monitoredAgent);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(239, 222));
        this.setTitle("[AgentName]");
        this.setContentPane(getControlSplit());
        this.setVisible(true);
			
	}

	/**
	 * This method initializes controlSplit	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getControlSplit() {
		if (controlSplit == null) {
			controlSplit = new JSplitPane();
			controlSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
			controlSplit.setContinuousLayout(true);
			controlSplit.setDividerSize(1);
			controlSplit.setDividerLocation(150);

			controlSplit.setBottomComponent(getControlPanel());
			controlSplit.setTopComponent(getJPanel());
			controlSplit.setVisible(true);
		}
		return controlSplit;
	}

	/**
	 * This method initializes controlPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getControlPanel() {
		if (controlPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			controlPanel = new JPanel();
			controlPanel.setLayout(new GridBagLayout());
			controlPanel.add(getRefreshButton(), gridBagConstraints);
		}
		return controlPanel;
	}

	/**
	 * This method initializes refreshButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRefreshButton() {
		if (refreshButton == null) {
			refreshButton = new JButton();
			refreshButton.setText("refresh");
			refreshButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					initiateRefresh();
				}
			});
		}
		return refreshButton;
	}
	
	public void setMonitoredAgent(AID agent){
		this.monitoredAgent=agent;
        this.setTitle(agent.getLocalName());
	}
	
	public void initiateRefresh(){
			GuiEvent ge=new GuiEvent(this,MonitorAgent.REFRESH);
			ge.addParameter(monitoredAgent);
			this.myAgent.postGuiEvent(ge);
	}
	
	public void updateOntRep(ContainerHolder ontRep){
		this.ontRep=ontRep;
//		testLabel.setText(ontRep.getLives_in().getId());
		BayMapRenderer applet=new BayMapRenderer();
		applet.init();
		Integer divLoc=getControlSplit().getDividerLocation();
		getControlSplit().setTopComponent(applet);
		getControlSplit().setDividerLocation(divLoc);
		applet.createContainerAt(1,0,0);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel(){
		if(jPanel == null){
			jPanel=new JPanel();
			jPanel.setLayout(new GridBagLayout());
		}
		return jPanel;
	}


}  //  @jve:decl-index=0:visual-constraint="18,10"
