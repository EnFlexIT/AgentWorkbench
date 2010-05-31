/**
 * 
 */
package contmas.main;

import jade.core.AID;
import jade.gui.GuiEvent;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import contmas.agents.MonitorAgent;
import contmas.de.unidue.stud.sehawagn.contmas.monitor.BayMapRenderer;
import contmas.ontology.BayMap;
import contmas.ontology.ContainerHolder;

/**
 * @author hfw15
 *
 */
public class AgentView extends JInternalFrame{

	private JSplitPane controlSplit=null;
	private JPanel controlPanel=null;
	private JButton refreshButton=null;
	private MonitorAgent myAgent;
	private AID monitoredAgent; //  @jve:decl-index=0:
	private ContainerHolder ontRep; //  @jve:decl-index=0:
	private BayMapRenderer bayMapRenderer;

	public AgentView(MonitorAgent a,AID monitoredAgent,ContainerHolder ontRep){
		super();
		initialize();
		myAgent=a;
		setMonitoredAgent(monitoredAgent);
		if(ontRep!=null){
			updateOntRep(ontRep);
		}
		initiateRefresh();

	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize(){
		this.setSize(new Dimension(239,222));
		this.setResizable(true);
		this.setIconifiable(true);
		this.setClosable(true);
		this.setTitle("[AgentName]");
		this.setContentPane(getControlSplit());
		this.setVisible(true);


	}

	/**
	 * This method initializes controlSplit	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getControlSplit(){
		if(controlSplit == null){
			controlSplit=new JSplitPane();
			controlSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
			controlSplit.setContinuousLayout(true);
			controlSplit.setDividerSize(1);
			controlSplit.setDividerLocation(150);

			controlSplit.setResizeWeight(1.0D);
			controlSplit.setBottomComponent(getControlPanel());

			controlSplit.setTopComponent(getBayMapRenderer());
			controlSplit.setDividerLocation(controlSplit.getDividerLocation() + 1);

		}
		return controlSplit;
	}

	private BayMapRenderer getBayMapRenderer(){
		if(bayMapRenderer == null){
			bayMapRenderer=new BayMapRenderer(false);
			bayMapRenderer.init();
		}
		return bayMapRenderer;
	}

	/**
	 * This method initializes controlPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getControlPanel(){
		if(controlPanel == null){
			GridBagConstraints gridBagConstraints=new GridBagConstraints();
			gridBagConstraints.gridx=0;
			gridBagConstraints.gridy=0;
			controlPanel=new JPanel();
			controlPanel.setLayout(new GridBagLayout());
			controlPanel.add(getRefreshButton(),gridBagConstraints);
		}
		return controlPanel;
	}

	/**
	 * This method initializes refreshButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRefreshButton(){
		if(refreshButton == null){
			refreshButton=new JButton();
			refreshButton.setText("refresh");
			refreshButton.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e){
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
	
	public AID getMonitoredAgent(){
		return this.monitoredAgent;
	}

	public void initiateRefresh(){
		GuiEvent ge=new GuiEvent(this,MonitorAgent.REFRESH);
		ge.addParameter(monitoredAgent);
		this.myAgent.postGuiEvent(ge);
	}

	public void updateOntRep(ContainerHolder ontRep){
		BayMapRenderer bayMapRenderer=getBayMapRenderer();
		BayMap map=ontRep.getContains();

		if(this.ontRep == null){
			bayMapRenderer.setBayMapDimensions(map.getX_dimension(),map.getY_dimension(),map.getZ_dimension());
			bayMapRenderer.renderBayMapBounds();
		} else {
			bayMapRenderer.flushLoading();
		}
		HashMap<Vector<Integer>, String> loading=Const.convertLoading(map,ontRep.getContainer_states());
		bayMapRenderer.renderLoading(loading);
		this.ontRep=ontRep;

	//	bayMapRenderer.createContainerAt(1,0,0);
	}

} //  @jve:decl-index=0:visual-constraint="18,10"
