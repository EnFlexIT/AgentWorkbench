/**
 * 
 */
package contmas.main;

import jade.core.AID;
import jade.gui.GuiEvent;
import jade.gui.AgentTree.AgentNode;

import javax.swing.JInternalFrame;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;

import contmas.agents.MonitorAgent;
import contmas.ontology.ContainerHolder;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;

/**
 * @author hfw15
 *
 */
public class AgentView extends JInternalFrame {

	private JSplitPane controlSplit = null;
	private JPanel controlPanel = null;
	private JPanel bayMapRendering = null;
	private JButton refreshButton = null;
	private MonitorAgent myAgent;
	private AID monitoredAgent;  //  @jve:decl-index=0:
	private JLabel testLabel = null;
	private ContainerHolder ontRep;

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
			controlSplit.setTopComponent(getBayMapRendering());
			controlSplit.setBottomComponent(getControlPanel());
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
	 * This method initializes bayMapRendering	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBayMapRendering() {
		if (bayMapRendering == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			testLabel = new JLabel();
			testLabel.setText("JLabel");
			bayMapRendering = new JPanel();
			bayMapRendering.setLayout(new GridBagLayout());
			bayMapRendering.add(testLabel, gridBagConstraints1);
		}
		return bayMapRendering;
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
		testLabel.setText(ontRep.getLives_in().getId());
	}

}  //  @jve:decl-index=0:visual-constraint="18,10"
