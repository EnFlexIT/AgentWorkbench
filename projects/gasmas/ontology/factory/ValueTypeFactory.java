package gasmas.ontology.factory;

import gasmas.ontology.ValueType;
import gasmas.transfer.zib.net.CalorificValueType;
import gasmas.transfer.zib.net.DensityType;
import gasmas.transfer.zib.net.FlowType;
import gasmas.transfer.zib.net.HeatTransferType;
import gasmas.transfer.zib.net.LengthType;
import gasmas.transfer.zib.net.PressureType;
import gasmas.transfer.zib.net.TemperatureType;

/**
 * 
 * @author Administrator
 */
public class ValueTypeFactory {

	/**
	 * 
	 * @param lengthType
	 * @return
	 */
	public static ValueType newInstance(LengthType lengthType) {
		
		ValueType valueType = new ValueType();
		if (lengthType == null) {
			valueType.setUnit("m");
			valueType.setValue(0);
		} else {
			valueType.setUnit(lengthType.getUnit().value());
			valueType.setValue((float) lengthType.getValue());
		}
		return valueType;
	}
	
	/**
	 * @param flowType
	 * @return
	 */
	public static ValueType newInstance(FlowType flowType) {
		
		ValueType valueType = new ValueType();
		if(flowType == null) {
			valueType.setUnit("m_cube_per_s");
			valueType.setValue(0);
		} else {
			valueType.setUnit(flowType.getUnit());
			valueType.setValue((float) flowType.getValue());
		}
		return valueType;
	}
	
	public static ValueType newInstance(HeatTransferType heatTransferType) {
		
		ValueType valueType = new ValueType();
		if(heatTransferType == null){
			valueType.setUnit("W_per_m_square_per_K");
			valueType.setValue(0);
		} else {
			valueType.setUnit(heatTransferType.getUnit().value());
			valueType.setValue((float) heatTransferType.getValue());		
		}
		return valueType;
	}
	
	public static ValueType newInstance(PressureType pressureType) {
		
		ValueType valueType = new ValueType();
		if(pressureType == null){
			valueType.setUnit("barg");
			valueType.setValue(0);
		} else {
			valueType.setUnit(pressureType.getUnit().value());
			valueType.setValue((float) pressureType.getValue());			
		}
		return valueType;
	}
	
	public static ValueType newInstance(CalorificValueType calorificValueType) {
		
		ValueType valueType = new ValueType();
		
		if (calorificValueType == null) {
			valueType.setUnit("MJ_per_m_cube");
			valueType.setValue(0);
		} else {
			valueType.setUnit(calorificValueType.getUnit().value());
			valueType.setValue((float) calorificValueType.getValue());
		}
		
		return valueType;
	}
	
	public static ValueType newInstance(TemperatureType temperatureType) {
		
		ValueType valueType = new ValueType();
		
		if(temperatureType == null){
			valueType.setUnit("K");
			valueType.setValue(0);
		} else {
			valueType.setUnit(temperatureType.getUnit().value());
			valueType.setValue((float) temperatureType.getValue());			
		}
		
		return valueType;
	}
	
	
	public static ValueType newInstance(DensityType densityType) {
		
		ValueType valueType = new ValueType();
		if(densityType == null) {
			valueType.setUnit("kg_per_m_cube");
			valueType.setValue(0);
		} else {
			valueType.setUnit(densityType.getUnit().value());
			valueType.setValue((float) densityType.getValue());			
		}
		return valueType;
	}


	
}

