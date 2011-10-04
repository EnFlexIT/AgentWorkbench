package remotestart.agents;

import java.text.SimpleDateFormat;
import java.util.Date;

import agentgui.ontology.Simple_Integer;
import agentgui.ontology.Simple_String;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class SystemOutAgent extends Agent {

	private static final long serialVersionUID = 471703602130677272L;

	private Integer speakingInterval = 5;
	private String speakingText = "This is a system output!";
	
	@Override
	protected void setup() {
	
		Object[] startArgs = this.getArguments();
		if (startArgs!=null) {
			if (startArgs.length>0) {
				// --- Work on the start argument ---------
				if (startArgs[0]!= null) {
					try {
						speakingInterval = ((Simple_Integer) startArgs[0]).getIntegerValue();	
					} catch (Exception e) {}
				}
				if (startArgs[1]!= null) {
					try {
						speakingText = ((Simple_String) startArgs[1]).getStringValue();
					} catch (Exception e) {}
				}
				
			}
		}
		
		if (speakingInterval==0) {
			speakingInterval = 5;
		}
		if (speakingText==null) {
			speakingText = "Error, while computing start arguments !";
		}
		this.addBehaviour(new SpeakTick(this, speakingInterval*1000));
	}
	
	
	private class SpeakTick extends TickerBehaviour {

		private static final long serialVersionUID = 757478665038190137L;

		public SpeakTick(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick() {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			String zeitString = sdf.format(new Date());

			System.out.println("=> " + zeitString + " (out) " + speakingText);
			System.err.println("=> " + zeitString + " (err) " + speakingText);
		}
		
	}
	
	
}
