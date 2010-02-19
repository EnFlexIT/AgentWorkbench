package monitormas.agents;

import monitormas.ontology.*;
import jade.content.lang.Codec;
import jade.content.lang.leap.LEAPCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class MeasureAgentExtern extends Agent{

	private static final long serialVersionUID = 1L;

	protected Codec codec = new LEAPCodec();
	protected Ontology onto = MonitorMASOntology.getInstance();
	protected TimeDetector TimeLocal;
	
	protected void setup() {

		// ----------------------------------------------------------
		// --- Start auf der initialisierenden Plattform ------------
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(onto);
		
		// --- Namen des ControlAgenten und Plattform merken -------- 
		
		
		// --- Zielplattform ansteuern und beamen -------------------
		
		
		// ----------------------------------------------------------
		// --- An Zielplattform angekommen --------------------------
		
		// --- Bei ControlerAgent melden und auf Zeitmessung warten -
		// --- (ggf. 3x oder häufiger und ggf. regelmäßig)
		
		// --- Auf Experimentierzeit warten (set Time) --------------
		
		
		// --- Auf die Arbeitsaufträge warten -----------------------
		
		// --- Arbeitsaufträge in die Sceduleliste eintragen --------

		// --- Arbeitsaufträge abarbeiten und messen ----------------
		

		// --- Ergebnisnachricht an Controler schicken --------------
		
		// --- Zurück-Beamen ----------------------------------------
		
		// --- Suizid begehen ---------------------------------------

		// ----------------------------------------------------------
		
		
		TimeLocal = new TimeDetector();
		TimeLocal.setTime_Transmit_Start( System.currentTimeMillis() ) ;
		
		
		addBehaviour( new BhvRunTest() );
	}
	protected void takeDown(){
		// TODO
	}
	
	public class BhvRunTest extends OneShotBehaviour {

		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			
			Object[] args = getArguments();
			if((args.length>0)){
		
			}
			
			
			System.out.println("Ich bin eine laufender Prozess in einem Behaiviour ...");
			
			System.out.println( System.currentTimeMillis() );
		}

		/*@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}*/
		
		
		
		
	}
	
	
}
