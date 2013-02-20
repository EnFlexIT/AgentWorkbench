/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package gasmas.transfer.zib.factory;

import gasmas.ontology.ValueType;
import gasmas.transfer.zib.cs.CompressorSpeedType;
import gasmas.transfer.zib.cs.ElectricMotorType;
import gasmas.transfer.zib.cs.GasTurbineType;
import gasmas.transfer.zib.cs.MpMeasurementType.MaximalPower;
import gasmas.transfer.zib.cs.PowerType;
import gasmas.transfer.zib.cs.SECMeasurementsType.Measurement.CompressorPower;
import gasmas.transfer.zib.cs.SECMeasurementsType.Measurement.FuelConsumption;
import gasmas.transfer.zib.cs.TcMeasurementType.AdiabaticHead;
import gasmas.transfer.zib.cs.TcMeasurementType.VolumetricFlowrate;
import gasmas.transfer.zib.cs.TorqueType;
import gasmas.transfer.zib.cs.VolumeType;
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
	
	public static ValueType newInstance(VolumeType operatingVolume) {
		ValueType valueType = new ValueType();
		if(operatingVolume == null) {
			valueType.setUnit("m_cube");
			valueType.setValue(0);
		} else {
			valueType.setUnit(operatingVolume.getUnit().value());
			valueType.setValue((float) operatingVolume.getValue());
		}
		return valueType;
	}
	
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
	
	public static ValueType newInstance(VolumetricFlowrate flowRate) {
		ValueType valueType = new ValueType();
		if(flowRate == null) {
			valueType.setUnit("m_cube_per_s");
			valueType.setValue(0);
		} else {
			valueType.setUnit(flowRate.getUnit());
			valueType.setValue((float) flowRate.getValue());
		}
		return valueType;
	}
	
	public static ValueType newInstance(AdiabaticHead adiabaticHead) {
		ValueType valueType = new ValueType();
		if(adiabaticHead == null){
			valueType.setUnit("kJ_per_kg");
			valueType.setValue(0);
		} else {
			valueType.setUnit(adiabaticHead.getUnit());
			valueType.setValue((float) adiabaticHead.getValue());		
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
	
	public static ValueType newInstance(CompressorSpeedType compressorSpeedType) {
		ValueType valueType = new ValueType();
		if(compressorSpeedType == null) {
			valueType.setUnit("per_min");
			valueType.setValue(0);
		} else {
			valueType.setUnit(compressorSpeedType.getUnit().toString());
			valueType.setValue((float) compressorSpeedType.getValue());			
		}
		return valueType;
	}

	public static ValueType newInstance(TorqueType maximalTorque) {
		ValueType valueType = new ValueType();
		if(maximalTorque == null) {
			valueType.setUnit("kNm");
			valueType.setValue(0);
		} else {
			valueType.setUnit(maximalTorque.getUnit().value());
			valueType.setValue((float) maximalTorque.getValue());			
		}
		return valueType;
	}

	public static ValueType newInstance(CompressorPower compressorPower) {
		ValueType valueType = new ValueType();
		if(compressorPower == null) {
			valueType.setUnit("kW");
			valueType.setValue(0);
		} else {
			valueType.setUnit(compressorPower.getUnit());
			valueType.setValue((float) compressorPower.getValue());			
		}
		return valueType;
	}

	public static ValueType newInstance(FuelConsumption fuelConsumption) {
		ValueType valueType = new ValueType();
		if(fuelConsumption == null) {
			valueType.setUnit("kW");
			valueType.setValue(0);
		} else {
			valueType.setUnit(fuelConsumption.getUnit());
			valueType.setValue((float) fuelConsumption.getValue());			
		}
		return valueType;
	}

	public static ValueType newInstance(ElectricMotorType.MaximalPowerMeasurements.AmbientTemperature ogeAmbientTemperature) {
		ValueType valueType = new ValueType();
		if(ogeAmbientTemperature == null) {
			valueType.setUnit("K");
			valueType.setValue(0);
		} else {
			valueType.setUnit(ogeAmbientTemperature.getUnit());
			valueType.setValue((float) ogeAmbientTemperature.getValue());			
		}
		return valueType;
	}
	
	public static ValueType newInstance(GasTurbineType.MaximalPowerMeasurements.AmbientTemperature ogeAmbientTemperature) {
		ValueType valueType = new ValueType();
		if(ogeAmbientTemperature == null) {
			valueType.setUnit("K");
			valueType.setValue(0);
		} else {
			valueType.setUnit(ogeAmbientTemperature.getUnit());
			valueType.setValue((float) ogeAmbientTemperature.getValue());			
		}
		return valueType;
	}

	public static ValueType newInstance(MaximalPower maximalPower) {
		ValueType valueType = new ValueType();
		if(maximalPower == null) {
			valueType.setUnit("kW");
			valueType.setValue(0);
		} else {
			valueType.setUnit(maximalPower.getUnit());
			valueType.setValue((float) maximalPower.getValue());			
		}
		return valueType;
	}

	public static ValueType newInstance(PowerType powerType) {
		ValueType valueType = new ValueType();
		if(powerType == null) {
			valueType.setUnit("kW");
			valueType.setValue(0);
		} else {
			valueType.setUnit(powerType.getUnit().value());
			valueType.setValue((float) powerType.getValue());			
		}
		return valueType;
	}

}