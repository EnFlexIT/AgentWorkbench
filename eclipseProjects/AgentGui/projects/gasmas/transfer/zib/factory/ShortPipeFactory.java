package gasmas.transfer.zib.factory;

import gasmas.ontology.ShortPipe;
import gasmas.transfer.zib.net.GasConnectionType;
import gasmas.transfer.zib.net.ShortPipeType;

public class ShortPipeFactory {

	public static ShortPipe newInstance(GasConnectionType gasConnectionType) {
		
		ShortPipe shortPipe = new ShortPipe();
		ConnectionFactory.setConnectionAttributes(shortPipe, gasConnectionType);
		//---For the case of new attributes---------- 
		ShortPipeType shortPipeType = (ShortPipeType) gasConnectionType;
		return shortPipe;
	}
}
