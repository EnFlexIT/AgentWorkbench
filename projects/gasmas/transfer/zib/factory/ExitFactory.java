package gasmas.transfer.zib.factory;

import gasmas.ontology.Exit;
import gasmas.transfer.zib.net.GasNodeType;
import gasmas.transfer.zib.net.SinkType;

public class ExitFactory extends InnodeFactory {

	public static Exit newInstance(GasNodeType gasNodeType) {
		Exit exit = new Exit();
		setInnodeAttributes(exit, gasNodeType);
		setExitAttributes(exit, gasNodeType);
		return exit;
	}

	protected static void setExitAttributes(Exit exit, GasNodeType gasNodeType) {
		SinkType sinkType = (SinkType) gasNodeType;
		exit.setFlowMin(ValueTypeFactory.newInstance(sinkType.getFlowMin()));
		exit.setFlowMax(ValueTypeFactory.newInstance(sinkType.getFlowMax()));
	}

}
