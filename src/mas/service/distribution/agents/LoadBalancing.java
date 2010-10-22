package mas.service.distribution.agents;

import java.util.Iterator;

public class LoadBalancing extends LoadBalancingBase implements LoadBalancingInterface {

	private static final long serialVersionUID = -4721675611537786965L;

	public LoadBalancing(LoadAgent loadAgent) {
		super(loadAgent);
	}

	@Override
	public void doBalancing() {
		
		if (loadThresholdExceededOverAll!=0) {
			// --------------------------------------------------------------------------
			// --- If the threshold level is exceeded, start a new remote container -----
			// --------------------------------------------------------------------------
			String newRemoteContainer = this.startRemoteContainer();
			if (newRemoteContainer!=null) {
				
				Iterator<String> it = loadMachines4Balancing.keySet().iterator();
				while (it.hasNext()) {
					
					String jvmName = it.next(); 
					LoadMachine lm = loadMachines4Balancing.get(jvmName);
					System.out.println(lm.getMachineLoad().getLoadCPU());
					
				}
				
				
			} else {
				
				
			}
			
		} else {
			// --------------------------------------------------------------------------
			// --- Mal sehen .........
			// --------------------------------------------------------------------------
			
		}
	}	
	
}
