package gasmas.ontology.factory;

import gasmas.ontology.Pipe;
import gasmas.transfer.zib.net.GasConnectionType;
import gasmas.transfer.zib.net.PipeType;

public class PipeFactory {
	
	public static Pipe newInstance(GasConnectionType gasConnectionType) {
		
		Pipe pipe = new Pipe();
		ConnectionFactory.setConnectionAttributes(pipe, gasConnectionType);
		PipeType pipeType = (PipeType)gasConnectionType;
		
		pipe.setDiameter(ValueTypeFactory.newInstance(pipeType.getDiameter()));
		pipe.setHeatTransferCoefficient(ValueTypeFactory.newInstance(pipeType.getHeatTransferCoefficient()));
		pipe.setLength(ValueTypeFactory.newInstance(pipeType.getLength()));
		pipe.setLineOfSight(ValueTypeFactory.newInstance(pipeType.getLineOfSight()));
		pipe.setPressureMax(ValueTypeFactory.newInstance(pipeType.getPressureMax()));
		pipe.setRoughness(ValueTypeFactory.newInstance(pipeType.getRoughness()));
		
		return pipe;
	}
}
