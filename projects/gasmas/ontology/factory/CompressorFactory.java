package gasmas.ontology.factory;

import gasmas.ontology.Compressor;
import gasmas.transfer.zib.net.CompressorStationType;


public class CompressorFactory {
	
	public static Compressor newInstance(CompressorStationType compressorStationType) {
	
		Compressor compressor = new Compressor();
		
		ConnectionFactory.setConnectionAttributes(compressor, compressorStationType);
		
		compressor.setFuelGasVertex(compressorStationType.getFuelGasVertex());
		
		compressor.setPressureInMin(ValueTypeFactory.newInstance(compressorStationType.getPressureInMin()));
		compressor.setPressureOutMax(ValueTypeFactory.newInstance(compressorStationType.getPressureOutMax()));
		compressor.setPressureLossOut(ValueTypeFactory.newInstance(compressorStationType.getPressureLossOut()));
		compressor.setPressureLossIn(ValueTypeFactory.newInstance(compressorStationType.getPressureLossIn()));
		compressor.setCooledOutputTemperature(ValueTypeFactory.newInstance(compressorStationType.getCooledOutputTemperature()));
		compressor.setDiameterIn(ValueTypeFactory.newInstance(compressorStationType.getDiameterIn()));
		compressor.setDiameterOut(ValueTypeFactory.newInstance(compressorStationType.getDiameterOut()));
		compressor.setDragFactorIn(getDragFactorIn(compressorStationType));
		compressor.setDragFactorOut(getDragFactorOut(compressorStationType));
		return compressor;
	}
	private static float getDragFactorOut(CompressorStationType compressorStationType) {
		if(compressorStationType.getDragFactorOut()== null) {
			return 0;
		} else {			
			return (float) compressorStationType.getDragFactorOut().getValue();
		}
	}
	
	private static float getDragFactorIn(CompressorStationType compressorStationType) {
		if (compressorStationType.getDragFactorIn() == null) {
			return 0;
		} else {
			return (float) compressorStationType.getDragFactorIn().getValue();
		}
	}
}


