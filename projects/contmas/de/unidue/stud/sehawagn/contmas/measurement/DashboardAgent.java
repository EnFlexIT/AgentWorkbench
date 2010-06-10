package contmas.de.unidue.stud.sehawagn.contmas.measurement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import javax.swing.JDesktopPane;

import contmas.agents.ContainerAgent;
import contmas.behaviours.listenForLogMessage;
import contmas.behaviours.subscribeToDF;
import contmas.interfaces.DFSubscriber;

public class DashboardAgent extends GuiAgent implements DFSubscriber,Measurer{

	private static final long serialVersionUID= -2081699287513185474L;


	public static final Integer SHUT_DOWN_EVENT= -1;
	public static final Integer EXAMPLEBUTTON_EVENT=0;

	private List allActors=new ArrayList();

	private JDesktopPane canvas;

	private DashboardGUI myGui;

	public DashboardAgent(JDesktopPane canvas){
		super();
		this.canvas=canvas;
	}

	@Override
	public AID getMeasureTopic(){
		AID measureTopic=Helper.getMeasureTopic(this);
		if(measureTopic!=null){
			try{
				TopicManagementHelper tmh = (TopicManagementHelper) this.getHelper(TopicManagementHelper.SERVICE_NAME);
				tmh.register(measureTopic);
			}catch(ServiceException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return measureTopic;
	}

	@Override
	protected void setup(){
		super.setup();
		this.myGui=new DashboardGUI(this,canvas);
		this.myGui.setVisible(true);
		ContainerAgent.enableForCommunication(this);
		addBehaviour(new subscribeToDF(this,"container-handling"));

		this.addBehaviour(new listenForStatusUpdate(this));

	}

	/* (non-Javadoc)
	 * @see contmas.interfaces.DFSubscriber#processSubscriptionUpdate(jade.util.leap.List, java.lang.Boolean)
	 */
	@Override
	public void processSubscriptionUpdate(List updatedAgents,Boolean remove){
		if( !remove){
			ContainerAgent.addToList(allActors,updatedAgents);
		}else{
			ContainerAgent.removeFromList(allActors,updatedAgents);
		}
		if( !updatedAgents.isEmpty()){
			echo("[DashboardAgent says] Agents ");
			if(remove){
				echo("disappeared: ");
			}else{
				echo("emerged: ");
			}
			Iterator iter=updatedAgents.iterator();
			while(iter.hasNext()){
				AID actor=(AID) iter.next();
				echo(actor.getLocalName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see jade.gui.GuiAgent#onGuiEvent(jade.gui.GuiEvent)
	 */
	@Override
	protected void onGuiEvent(GuiEvent ev){
		if(ev.getType() == EXAMPLEBUTTON_EVENT){
			String content=(String) ev.getParameter(0);
			echo("[DashboardAgent says] EXAMPLEBUTTON_EVENT (" + EXAMPLEBUTTON_EVENT + ") received from GUI. Content of exampleTextarea was: " + content);
//			echo("Refreshing view of "+subject.getLocalName());
//			this.addBehaviour(new requestOntologyRepresentation(this,subject,this.harbourMaster));
		}else if(ev.getType() == SHUT_DOWN_EVENT){
			this.doDelete();
		}
	}

	@Override
	protected void takeDown(){
		if(this.myGui != null){
			this.myGui.dispose();
		}
	}

	private void echo(String text){
		System.out.println(text);
		myGui.echoApp(text);
	}

	/* (non-Javadoc)
	 * @see contmas.de.unidue.stud.sehawagn.contmas.measurement.Measurer#processStatusUpdate(java.lang.String)
	 */
	@Override
	public void processStatusUpdate(AID sender,Long eventTime,String content, String bic){
		SimpleDateFormat smDaFo=new SimpleDateFormat();
		smDaFo.applyPattern(smDaFo.toPattern()+" s.S");
//		echo("Pattern is "+smDaFo.toLocalizedPattern());
//		DateFormat.getInstance().get
		String formattedDate=smDaFo.format(new Date(eventTime));

//		String formattedDate=DateFormat.getInstance().format(new Date(eventTime));
		echo(sender.getLocalName() + " changed status of "+bic+" at "+formattedDate+" to: " + content);
	}
}
