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

import gasmas.ontology.Calc3Parameter;
import gasmas.ontology.Calc9Parameter;
import gasmas.ontology.CompStat;
import gasmas.ontology.CompStatAdiabaticEfficiency;
import gasmas.ontology.CompStatCompressorWithSpeed;
import gasmas.ontology.CompStatConfiguration;
import gasmas.ontology.CompStatMaxP;
import gasmas.ontology.CompStatMaxPmeasurment;
import gasmas.ontology.CompStatMaxPtoAmbientTemperature;
import gasmas.ontology.CompStatSECmeasurment;
import gasmas.ontology.CompStatStage;
import gasmas.ontology.CompStatTcMeasurement;
import gasmas.ontology.ElectricMotor;
import gasmas.ontology.GasDrivenMotor;
import gasmas.ontology.GasTurbine;
import gasmas.ontology.PistonCompressor;
import gasmas.ontology.SteamTurbine;
import gasmas.ontology.TurboCompressor;
import gasmas.transfer.zib.cs.CompressorStationsType.CompressorStation;
import gasmas.transfer.zib.cs.CompressorStationsType.CompressorStation.Compressors;
import gasmas.transfer.zib.cs.CompressorStationsType.CompressorStation.Configurations;
import gasmas.transfer.zib.cs.CompressorStationsType.CompressorStation.Drives;
import gasmas.transfer.zib.cs.ConfigurationType;
import gasmas.transfer.zib.cs.ConfigurationType.Stage;
import gasmas.transfer.zib.cs.ElectricMotorType;
import gasmas.transfer.zib.cs.ElectricMotorType.MaximalPowerMeasurements;
import gasmas.transfer.zib.cs.GasDrivenMotorType;
import gasmas.transfer.zib.cs.GasTurbineType;
import gasmas.transfer.zib.cs.MpMeasurementType;
import gasmas.transfer.zib.cs.PistonCompressorType;
import gasmas.transfer.zib.cs.SECMeasurementsType.Measurement;
import gasmas.transfer.zib.cs.SteamTurbineType;
import gasmas.transfer.zib.cs.TcMeasurementType;
import gasmas.transfer.zib.cs.TurboCompressorType;
import gasmas.transfer.zib.cs.TurboCompressorType.CharacteristicDiagramMeasurements;
import gasmas.transfer.zib.cs.TurboCompressorType.CharacteristicDiagramMeasurements.AdiabaticEfficiency;
import gasmas.transfer.zib.cs.TurboCompressorType.SettlelineMeasurements;

import java.util.List;

public class CompressorStationFactory {

	
	public static CompStat newInstance(CompressorStation ogeCompressorStation) {
		
		// --- Set up the base information --------------------------
		CompStat compressorStation = new CompStat();
		compressorStation.setID(ogeCompressorStation.getId());
		compressorStation.setAlias(ogeCompressorStation.getId());
		compressorStation.setBuildingCosts(ogeCompressorStation.getBuildingCost().floatValue());
		compressorStation.setUpgradeCosts(ogeCompressorStation.getUpgradeCost().floatValue());
		
		// --- Set up the compressor --------------------------------
		Compressors comps = ogeCompressorStation.getCompressors();
		if (comps!=null) {
			setCompressors(compressorStation, comps);
		}
		
		// --- Set up the drives ------------------------------------
		Drives drives = ogeCompressorStation.getDrives();
		if (drives!=null) {
			setDrives(compressorStation, drives);
		}
		
		// --- Set up the configurations ----------------------------
		Configurations configurations = ogeCompressorStation.getConfigurations();
		if (configurations!=null) {
			setConfigurations(compressorStation, configurations);
		}
		
		return compressorStation;
	}
	
	/**
	 * Sets the configurations.
	 *
	 * @param localCompressorStation the local compressor station
	 * @param configurations the configurations
	 */
	private static void setConfigurations(CompStat localCompressorStation, Configurations ogeConfigurations) {
		
		jade.util.leap.ArrayList configurations = new jade.util.leap.ArrayList();
		for (int i = 0; i < ogeConfigurations.getConfiguration().size(); i++) {
			ConfigurationType ogeConfiguration = ogeConfigurations.getConfiguration().get(i); 

			CompStatConfiguration configuration = new CompStatConfiguration();
			configuration.setConfID(ogeConfiguration.getConfId());
			configuration.setNrOfSerialStages(ogeConfiguration.getNrOfSerialStages().intValue());

			jade.util.leap.ArrayList stages = new jade.util.leap.ArrayList();
			List<Stage> ogeStages = ogeConfiguration.getStage();
			for (int j = 0; j < ogeStages.size(); j++) {
				Stage ogeStage =  ogeStages.get(j);
				
				CompStatStage stage = new CompStatStage();
				stage.setStageNo(ogeStage.getStageNr().intValue());
				stage.setNoOfParallelUnits(ogeStage.getNrOfParallelUnits().intValue());
				
				jade.util.leap.ArrayList compressors = new jade.util.leap.ArrayList();
				List<ConfigurationType.Stage.Compressor> ogeCompressors = ogeStage.getCompressor();
				for (int k = 0; k < ogeCompressors.size(); k++) {
					ConfigurationType.Stage.Compressor ogeCompressor = ogeCompressors.get(k);
					
					CompStatCompressorWithSpeed compressor = new CompStatCompressorWithSpeed();
					compressor.setID(ogeCompressor.getId());
					compressor.setNominalSpeed(ogeCompressor.getNominalSpeed().intValue());
					
					compressors.add(compressor);
				}
				if (compressors.size()>0) {
					stage.setCompressor(compressors);	
				}
				
				stages.add(stage);
			}
			if (stages.size()>0) {
				configuration.setStages(stages);	
			}

			configurations.add(configuration);
		}
		if (configurations.size()>0) {
			localCompressorStation.setConfigurations(configurations);
		}
	}
	
	/**
	 * Sets the drives for the compressor station.
	 *
	 * @param localCompressorStation the local instance of a compressor station
	 * @param ogeDrives the oge drives
	 */
	private static void setDrives(CompStat localCompressorStation, Drives ogeDrives) {
	
		// --- Gas Turbine -------------------------------- 
		List<GasTurbineType> ogeGasTurbines = ogeDrives.getGasTurbine();
		if (ogeGasTurbines.size()>0) {
			setGasTurbines(localCompressorStation, ogeGasTurbines);
		}
		// --- Gas driven Motor ---------------------------
		List<GasDrivenMotorType> ogeGasDrivenMotors = ogeDrives.getGasDrivenMotor();
		if (ogeGasDrivenMotors.size()>0) {
			setGasDrivenMotors(localCompressorStation, ogeGasDrivenMotors);
		}
		// --- Electric Motor -----------------------------
		List<ElectricMotorType> ogeElectricMotors = ogeDrives.getElectricMotor();
		if (ogeElectricMotors.size()>0) {
			setElectricMotors(localCompressorStation, ogeElectricMotors);
		}
		// --- Steam turbine ------------------------------
		List<SteamTurbineType> ogeSteamTurbines = ogeDrives.getSteamTurbine();
		if (ogeSteamTurbines.size()>0) {
			setSteamTurbines(localCompressorStation, ogeSteamTurbines);
		}
	}
	
	private static void setGasTurbines(CompStat localCompressorStation, List<GasTurbineType> ogeGasTurbines) {
		
		jade.util.leap.ArrayList gasTurbines = new jade.util.leap.ArrayList();
		for (int i=0; i<ogeGasTurbines.size(); i++) {
			GasTurbineType ogeGasTurbine = ogeGasTurbines.get(i);
		
			// --- Set new gas turbine --------------------
			GasTurbine gasTurbine = new GasTurbine();
			gasTurbine.setID(ogeGasTurbine.getId());
			gasTurbine.setAlias(ogeGasTurbine.getId());
			
			// --- Energy rate ----------------------------
			Calc3Parameter energyRate = new Calc3Parameter();
			energyRate.setCoeff_1_3((float) ogeGasTurbine.getEnergyRateFunCoeff1().getValue());
			energyRate.setCoeff_2_3((float) ogeGasTurbine.getEnergyRateFunCoeff2().getValue());
			energyRate.setCoeff_3_3((float) ogeGasTurbine.getEnergyRateFunCoeff3().getValue());
			gasTurbine.setEnergyRateFunCoeff(energyRate);
			
			// --- Power function coefficient -------------
			Calc9Parameter powerFunCoeff = new Calc9Parameter();
			powerFunCoeff.setCoeff_1_9((float) ogeGasTurbine.getPowerFunCoeff1().getValue());
			powerFunCoeff.setCoeff_2_9((float) ogeGasTurbine.getPowerFunCoeff2().getValue());
			powerFunCoeff.setCoeff_3_9((float) ogeGasTurbine.getPowerFunCoeff3().getValue());
			powerFunCoeff.setCoeff_4_9((float) ogeGasTurbine.getPowerFunCoeff4().getValue());
			powerFunCoeff.setCoeff_5_9((float) ogeGasTurbine.getPowerFunCoeff5().getValue());
			powerFunCoeff.setCoeff_6_9((float) ogeGasTurbine.getPowerFunCoeff6().getValue());
			powerFunCoeff.setCoeff_7_9((float) ogeGasTurbine.getPowerFunCoeff7().getValue());
			powerFunCoeff.setCoeff_8_9((float) ogeGasTurbine.getPowerFunCoeff8().getValue());
			powerFunCoeff.setCoeff_9_9((float) ogeGasTurbine.getPowerFunCoeff9().getValue());
			gasTurbine.setGtPowerFunCoeff(powerFunCoeff);
			
			// --- Specific energy consumption ------------
			jade.util.leap.ArrayList secMeasurements = new jade.util.leap.ArrayList();
			List<Measurement> ogeSECMeasurements = ogeGasTurbine.getSpecificEnergyConsumptionMeasurements().getMeasurement();
			for (int j=0; j<ogeSECMeasurements.size(); j++) {
				Measurement ogeMeasurement = ogeSECMeasurements.get(j);
				
				CompStatSECmeasurment measurement = new CompStatSECmeasurment();
				measurement.setComressorPower(ValueTypeFactory.newInstance(ogeMeasurement.getCompressorPower()));
				measurement.setFuelConsumption(ValueTypeFactory.newInstance(ogeMeasurement.getFuelConsumption()));
				
				secMeasurements.add(measurement);
			}
			if (secMeasurements.size()>0) {
				gasTurbine.setGtSpecificEnergyConsumptionMeasurements(secMeasurements);	
			}
			
			// --- maximal power measurements -------------
			jade.util.leap.ArrayList maxPow2AmbientTemps = new jade.util.leap.ArrayList();
			List<GasTurbineType.MaximalPowerMeasurements.AmbientTemperature> ogeAmbientTempType = ogeGasTurbine.getMaximalPowerMeasurements().getAmbientTemperature();
			for (int j = 0; j < ogeAmbientTempType.size(); j++) {
				GasTurbineType.MaximalPowerMeasurements.AmbientTemperature ogeAmbType = ogeAmbientTempType.get(j);
				
				CompStatMaxPtoAmbientTemperature singleMaxPow2AmbientTemp = new CompStatMaxPtoAmbientTemperature();
				singleMaxPow2AmbientTemp.setTemperatureMP(ValueTypeFactory.newInstance(ogeAmbType));
				
				jade.util.leap.ArrayList maxPowMeasurments = new jade.util.leap.ArrayList();
				List<MpMeasurementType> ogeMaxPowMeasurments = ogeAmbType.getMeasurement(); 
				for (int k=0; k<ogeMaxPowMeasurments.size(); k++) {
					MpMeasurementType ogeMaxPowMeasurement = ogeMaxPowMeasurments.get(k);
					
					CompStatMaxPmeasurment maxPowMeasurement = new CompStatMaxPmeasurment();
					maxPowMeasurement.setSpeeMP(ValueTypeFactory.newInstance(ogeMaxPowMeasurement.getSpeed()));
					maxPowMeasurement.setMaximalPower(ValueTypeFactory.newInstance(ogeMaxPowMeasurement.getMaximalPower()));
					
					maxPowMeasurments.add(maxPowMeasurement);
				}
				if (maxPowMeasurments.size()>0) {
					singleMaxPow2AmbientTemp.setMeasurementsMP(maxPowMeasurments);	
				}
				
				maxPow2AmbientTemps.add(singleMaxPow2AmbientTemp);
			}
			if (maxPow2AmbientTemps.size()>0) {
				gasTurbine.setGtMaximalPowerMeasurements(maxPow2AmbientTemps);	
			}
			
			// --- finally --------------------------------
			gasTurbines.add(gasTurbine);
		}
		if (gasTurbines.size()>0) {
			localCompressorStation.setGasTurbines(gasTurbines);	
		}
	}

	private static void setGasDrivenMotors(CompStat localCompressorStation, List<GasDrivenMotorType> ogeGasDrivenMotors) {

		jade.util.leap.ArrayList gasDrivenMotors = new jade.util.leap.ArrayList();
		for (int i=0; i<ogeGasDrivenMotors.size(); i++) {
			GasDrivenMotorType ogeGasDrivenMotor = ogeGasDrivenMotors.get(i);
			
			// --- new instance of GasDrivenMotor --------- 
			GasDrivenMotor gasDrivenMotor = new GasDrivenMotor();
			gasDrivenMotor.setID(ogeGasDrivenMotor.getId());
			gasDrivenMotor.setAlias(ogeGasDrivenMotor.getId());
			
			// --- energy rate ----------------------------
			Calc3Parameter energyRate = new Calc3Parameter();
			energyRate.setCoeff_1_3((float) ogeGasDrivenMotor.getEnergyRateFunCoeff1().getValue());
			energyRate.setCoeff_2_3((float) ogeGasDrivenMotor.getEnergyRateFunCoeff2().getValue());
			energyRate.setCoeff_3_3((float) ogeGasDrivenMotor.getEnergyRateFunCoeff3().getValue());
			gasDrivenMotor.setEnergyRateFunCoeff(energyRate);

			// --- powerFunCoeff --------------------------
			Calc3Parameter powerFunCoeff = new Calc3Parameter();
			powerFunCoeff.setCoeff_1_3((float) ogeGasDrivenMotor.getPowerFunCoeff1().getValue());
			powerFunCoeff.setCoeff_2_3((float) ogeGasDrivenMotor.getPowerFunCoeff2().getValue());
			powerFunCoeff.setCoeff_3_3((float) ogeGasDrivenMotor.getPowerFunCoeff3().getValue());
			gasDrivenMotor.setGmPowerFunCoeff(powerFunCoeff);
			
			// --- specific energy consumption ------------
			jade.util.leap.ArrayList secMeasures = new jade.util.leap.ArrayList();
			List<Measurement> ogeSecMeasures = ogeGasDrivenMotor.getSpecificEnergyConsumptionMeasurements().getMeasurement();
			for (int j = 0; j < ogeSecMeasures.size(); j++) {
				Measurement ogeSecMeasurement = ogeSecMeasures.get(j);
				
				CompStatSECmeasurment secMeasurement = new CompStatSECmeasurment();
				secMeasurement.setComressorPower(ValueTypeFactory.newInstance(ogeSecMeasurement.getCompressorPower()));
				secMeasurement.setFuelConsumption(ValueTypeFactory.newInstance(ogeSecMeasurement.getFuelConsumption()));
				
				secMeasures.add(secMeasurement);
			}
			gasDrivenMotor.setGmSpecificEnergyConsumptionMeasurements(secMeasures);
			
			// --- maximal power measurements -------------
			jade.util.leap.ArrayList maxPow2Measures = new jade.util.leap.ArrayList();
			List<MpMeasurementType> ogeMaxPowMeasures = ogeGasDrivenMotor.getMaximalPowerMeasurements().getMeasurement();
			for (int j = 0; j < ogeMaxPowMeasures.size(); j++) {
				MpMeasurementType ogeMaxPowMeasure = ogeMaxPowMeasures.get(j);
				
				CompStatMaxPmeasurment maxPowMeasure = new CompStatMaxPmeasurment();
				maxPowMeasure.setSpeeMP(ValueTypeFactory.newInstance(ogeMaxPowMeasure.getSpeed()));
				maxPowMeasure.setMaximalPower(ValueTypeFactory.newInstance(ogeMaxPowMeasure.getMaximalPower()));
				
				maxPow2Measures.add(maxPowMeasure);
			}
			gasDrivenMotor.setGmMaximalPowerMeasurements(maxPow2Measures);

			// --- Add to compressor station --------------
			gasDrivenMotors.add(gasDrivenMotor);
		}
		if (gasDrivenMotors.size()>0) {
			localCompressorStation.setGasDrivenMotors(gasDrivenMotors);	
		}
	}

	private static void setElectricMotors(CompStat localCompressorStation, List<ElectricMotorType> ogeElectricMotors) {
		
		// Definition of the electric motor type. Depending on whether this
		// electric motor is modeled as a gas turbine or a gas motor, nine or three
		// dimensionless coefficients are given for the (bi)quadratic power function. For
		// details, see the corresponding documentation in the gas turbine and gas motor type
		// definitions. 
		jade.util.leap.ArrayList electricMotors = new jade.util.leap.ArrayList();
		for (int i=0; i<ogeElectricMotors.size(); i++) {
			ElectricMotorType ogeElectricMotor = ogeElectricMotors.get(i);
			
			ElectricMotor electricMotor = new ElectricMotor();
			electricMotor.setID(ogeElectricMotor.getId());
			electricMotor.setAlias(ogeElectricMotor.getId());
			
			// --- energy rate ----------------------------
			Calc3Parameter energyRate = new Calc3Parameter();
			energyRate.setCoeff_1_3((float) ogeElectricMotor.getEnergyRateFunCoeff1().getValue());
			energyRate.setCoeff_2_3((float) ogeElectricMotor.getEnergyRateFunCoeff2().getValue());
			energyRate.setCoeff_3_3((float) ogeElectricMotor.getEnergyRateFunCoeff3().getValue());
			electricMotor.setEnergyRateFunCoeff(energyRate);

			// --- Power function coefficient -------------
			Calc9Parameter powerFunCoeff = new Calc9Parameter();
			powerFunCoeff.setCoeff_1_9((float) ogeElectricMotor.getPowerFunCoeff1().getValue());
			powerFunCoeff.setCoeff_2_9((float) ogeElectricMotor.getPowerFunCoeff2().getValue());
			powerFunCoeff.setCoeff_3_9((float) ogeElectricMotor.getPowerFunCoeff3().getValue());
			if (ogeElectricMotor.getPowerFunCoeff4()!=null) {
				powerFunCoeff.setCoeff_4_9((float) ogeElectricMotor.getPowerFunCoeff4().getValue());
				powerFunCoeff.setCoeff_5_9((float) ogeElectricMotor.getPowerFunCoeff5().getValue());
				powerFunCoeff.setCoeff_6_9((float) ogeElectricMotor.getPowerFunCoeff6().getValue());
				powerFunCoeff.setCoeff_7_9((float) ogeElectricMotor.getPowerFunCoeff7().getValue());
				powerFunCoeff.setCoeff_8_9((float) ogeElectricMotor.getPowerFunCoeff8().getValue());
				powerFunCoeff.setCoeff_9_9((float) ogeElectricMotor.getPowerFunCoeff9().getValue());
			}
			electricMotor.setEmPowerFunCoeff(powerFunCoeff);

			// --- Specific energy consumption ------------
			jade.util.leap.ArrayList secMeasurements = new jade.util.leap.ArrayList();
			List<Measurement> ogeSECMeasurements = ogeElectricMotor.getSpecificEnergyConsumptionMeasurements().getMeasurement();
			for (int j=0; j<ogeSECMeasurements.size(); j++) {
				Measurement ogeMeasurement = ogeSECMeasurements.get(j);
				
				CompStatSECmeasurment measurement = new CompStatSECmeasurment();
				measurement.setComressorPower(ValueTypeFactory.newInstance(ogeMeasurement.getCompressorPower()));
				measurement.setFuelConsumption(ValueTypeFactory.newInstance(ogeMeasurement.getFuelConsumption()));
				
				secMeasurements.add(measurement);
			}
			if (secMeasurements.size()>0) {
				electricMotor.setEmSpecificEnergyConsumptionMeasurements(secMeasurements);	
			}
			
			// --- maximal power measurements -------------
			CompStatMaxP compStatMaxP = new CompStatMaxP();
			MaximalPowerMeasurements ogeElectricMotorPowerMeasurements = ogeElectricMotor.getMaximalPowerMeasurements();
			// --- 1: CompStatMaxPmeasurment --------------
			jade.util.leap.ArrayList maxPmeasurements = new jade.util.leap.ArrayList();
			List<MpMeasurementType> ogeMeasurments = ogeElectricMotorPowerMeasurements.getMeasurement();
			for (int j = 0; j < ogeMeasurments.size(); j++) {
				MpMeasurementType ogeMpMeasurement = ogeMeasurments.get(j);
				
				CompStatMaxPmeasurment maxPmeasurement = new CompStatMaxPmeasurment();
				maxPmeasurement.setMaximalPower(ValueTypeFactory.newInstance(ogeMpMeasurement.getMaximalPower()));
				maxPmeasurement.setSpeeMP(ValueTypeFactory.newInstance(ogeMpMeasurement.getSpeed()));
				
				maxPmeasurements.add(maxPmeasurement);
			}
			if (maxPmeasurements.size()>0) {
				compStatMaxP.setMeasurementsType(maxPmeasurements);
			}
			
			// --- 2: CompStatMaxPtoAmbientTemperature ----
			jade.util.leap.ArrayList ambientTemperatures = new jade.util.leap.ArrayList();	
			List<ElectricMotorType.MaximalPowerMeasurements.AmbientTemperature> ogeAmbientTemperatures = ogeElectricMotorPowerMeasurements.getAmbientTemperature();
			for (int j = 0; j < ogeAmbientTemperatures.size(); j++) {
				ElectricMotorType.MaximalPowerMeasurements.AmbientTemperature ogeAmbientTemperature = ogeAmbientTemperatures.get(j);

				CompStatMaxPtoAmbientTemperature ambientTemperature = new CompStatMaxPtoAmbientTemperature();
				ambientTemperature.setTemperatureMP(ValueTypeFactory.newInstance(ogeAmbientTemperature));
				
				// --- Work on the measurements -----------
				jade.util.leap.ArrayList ambientTTemperatureMeasurements = new jade.util.leap.ArrayList();
				List<MpMeasurementType> ogeAmbientTemperatureMeasurements = ogeAmbientTemperature.getMeasurement();
				for (int k = 0; k < ogeAmbientTemperatureMeasurements.size(); k++) {
					MpMeasurementType ogeMeasurment = ogeAmbientTemperatureMeasurements.get(k);
					
					CompStatMaxPmeasurment measurment = new CompStatMaxPmeasurment();
					measurment.setMaximalPower(ValueTypeFactory.newInstance(ogeMeasurment.getMaximalPower()));
					measurment.setSpeeMP(ValueTypeFactory.newInstance(ogeMeasurment.getSpeed()));
					
					ambientTTemperatureMeasurements.add(measurment);
				}
				if (ambientTTemperatureMeasurements.size()>0) {
					ambientTemperature.setMeasurementsMP(ambientTTemperatureMeasurements);	
				}
				// ----------------------------------------
				
				ambientTemperatures.add(ambientTemperature);
			}
			if (ambientTemperatures.size()>0) {
				compStatMaxP.setAmbientTemperature(ambientTemperatures);
			}
			electricMotor.setEmMaximalPowerMeasurements(compStatMaxP);
			
			// --- Add to the list of electric motors -----
			electricMotors.add(electricMotor);
		}
		if (electricMotors.size()>0) {
			localCompressorStation.setElectricMotors(electricMotors);	
		}
	}
	
	private static void setSteamTurbines(CompStat localCompressorStation, List<SteamTurbineType> ogeSteamTurbines) {
		
		jade.util.leap.ArrayList steamTurbines = new jade.util.leap.ArrayList();
		for (int i=0; i<ogeSteamTurbines.size(); i++) {
			SteamTurbineType ogeSteamTurbine = ogeSteamTurbines.get(i);
			
			SteamTurbine steamTurbine = new SteamTurbine();
			steamTurbine.setID(ogeSteamTurbine.getId());
			steamTurbine.setAlias(ogeSteamTurbine.getId());
			
			// --- energy rate ----------------------------
			Calc3Parameter energyRate = new Calc3Parameter();
			energyRate.setCoeff_1_3((float) ogeSteamTurbine.getEnergyRateFunCoeff1().getValue());
			energyRate.setCoeff_2_3((float) ogeSteamTurbine.getEnergyRateFunCoeff2().getValue());
			energyRate.setCoeff_3_3((float) ogeSteamTurbine.getEnergyRateFunCoeff3().getValue());
			steamTurbine.setEnergyRateFunCoeff(energyRate);

			steamTurbine.setExplicit(ogeSteamTurbine.isExplicit());
			steamTurbine.setPowerMin(ValueTypeFactory.newInstance(ogeSteamTurbine.getPowerMin()));
			steamTurbine.setPowerMax(ValueTypeFactory.newInstance(ogeSteamTurbine.getPowerMax()));
			
			steamTurbines.add(steamTurbine);
		}
		if (steamTurbines.size()>0) {
			localCompressorStation.setSteamTurbines(steamTurbines);	
		}
	}
	
	/**
	 * Set the compressor.
	 */
	private static void setCompressors(CompStat localCompressorStation, Compressors ogeCompressors) {
		
		jade.util.leap.ArrayList localTurboComps = new jade.util.leap.ArrayList();
		jade.util.leap.ArrayList localPistonComps = new jade.util.leap.ArrayList();
		
		// --- Turbo compressor ---------------------------
		List<TurboCompressorType> ogeTurboComps = ogeCompressors.getTurboCompressor();
		for (int i=0; i<ogeTurboComps.size(); i++) {
			
			TurboCompressorType ogeTurboComp = (TurboCompressorType) ogeTurboComps.get(i);
			
			// --- Initiate the compressor ----------------
			TurboCompressor localTurboComp = new TurboCompressor();
			localTurboComp.setID(ogeTurboComp.getId());
			localTurboComp.setAlias(ogeTurboComp.getId());
			localTurboComp.setDrive(ogeTurboComp.getDrive());
			
			// --- Speed settings -------------------------
			localTurboComp.setSpeedMin(ValueTypeFactory.newInstance(ogeTurboComp.getSpeedMin()));
			localTurboComp.setSpeedMax(ValueTypeFactory.newInstance(ogeTurboComp.getSpeedMax()));
			
			// --- n isolines -----------------------------
			Calc9Parameter nIsoline = new Calc9Parameter();
			nIsoline.setCoeff_1_9((float) ogeTurboComp.getNIsolineCoeff1().getValue());
			nIsoline.setCoeff_2_9((float) ogeTurboComp.getNIsolineCoeff2().getValue());
			nIsoline.setCoeff_3_9((float) ogeTurboComp.getNIsolineCoeff3().getValue());
			nIsoline.setCoeff_4_9((float) ogeTurboComp.getNIsolineCoeff4().getValue());
			nIsoline.setCoeff_5_9((float) ogeTurboComp.getNIsolineCoeff5().getValue());
			nIsoline.setCoeff_6_9((float) ogeTurboComp.getNIsolineCoeff6().getValue());
			nIsoline.setCoeff_7_9((float) ogeTurboComp.getNIsolineCoeff7().getValue());
			nIsoline.setCoeff_8_9((float) ogeTurboComp.getNIsolineCoeff8().getValue());
			nIsoline.setCoeff_9_9((float) ogeTurboComp.getNIsolineCoeff9().getValue());
			localTurboComp.setN_isoline_coeff(nIsoline);
			
			// --- eta isolines ---------------------------
			Calc9Parameter etaIsoline = new Calc9Parameter();
			etaIsoline.setCoeff_1_9((float) ogeTurboComp.getEtaAdIsolineCoeff1().getValue());
			etaIsoline.setCoeff_2_9((float) ogeTurboComp.getEtaAdIsolineCoeff2().getValue());
			etaIsoline.setCoeff_3_9((float) ogeTurboComp.getEtaAdIsolineCoeff3().getValue());
			etaIsoline.setCoeff_4_9((float) ogeTurboComp.getEtaAdIsolineCoeff4().getValue());
			etaIsoline.setCoeff_5_9((float) ogeTurboComp.getEtaAdIsolineCoeff5().getValue());
			etaIsoline.setCoeff_6_9((float) ogeTurboComp.getEtaAdIsolineCoeff6().getValue());
			etaIsoline.setCoeff_7_9((float) ogeTurboComp.getEtaAdIsolineCoeff7().getValue());
			etaIsoline.setCoeff_8_9((float) ogeTurboComp.getEtaAdIsolineCoeff8().getValue());
			etaIsoline.setCoeff_9_9((float) ogeTurboComp.getEtaAdIsolineCoeff9().getValue());
			localTurboComp.setEta_ad_isoline_coeff(etaIsoline);
			
			// --- surgeline ------------------------------
			Calc3Parameter surgeLine = new Calc3Parameter();
			surgeLine.setCoeff_1_3((float) ogeTurboComp.getSurgelineCoeff1().getValue());
			surgeLine.setCoeff_2_3((float) ogeTurboComp.getSurgelineCoeff2().getValue());
			surgeLine.setCoeff_3_3((float) ogeTurboComp.getSurgelineCoeff3().getValue());
			localTurboComp.setSurgeline_coeff(surgeLine);
			
			// --- chokeline ------------------------------
			Calc3Parameter chokeLine = new Calc3Parameter();
			chokeLine.setCoeff_1_3((float) ogeTurboComp.getChokelineCoeff1().getValue());
			chokeLine.setCoeff_2_3((float) ogeTurboComp.getChokelineCoeff2().getValue());
			chokeLine.setCoeff_3_3((float) ogeTurboComp.getChokelineCoeff3().getValue());
			localTurboComp.setChokeline_coeff(chokeLine);
			
			// --- Efficiency of chokeline ----------------
			localTurboComp.setEfficiencyOfChokeline((float) ogeTurboComp.getEfficiencyOfChokeline().getValue());

			// --- Settleline measurements ----------------
			jade.util.leap.ArrayList settlelineMeasurements = new jade.util.leap.ArrayList();
			SettlelineMeasurements ogeSetMes = ogeTurboComp.getSettlelineMeasurements();
			List<TcMeasurementType> ogeSetMeasurments = ogeSetMes.getMeasurement();
			for (int j=0; j<ogeSetMeasurments.size(); j++) {
				TcMeasurementType ogeSetMeasure = ogeSetMeasurments.get(j);
				
				CompStatTcMeasurement localSetMeasure = new CompStatTcMeasurement();
				localSetMeasure.setAdiabaticHead(ValueTypeFactory.newInstance(ogeSetMeasure.getAdiabaticHead()));
				localSetMeasure.setVolumetricFlowrate(ValueTypeFactory.newInstance(ogeSetMeasure.getVolumetricFlowrate()));
				localSetMeasure.setSpeed(ValueTypeFactory.newInstance(ogeSetMeasure.getSpeed()));
				
				settlelineMeasurements.add(localSetMeasure);
			}
			if (settlelineMeasurements.size()>0) {
				localTurboComp.setSettlelineMeasurements(settlelineMeasurements);	
			}
			
			// --- Characteristic Diagram Measurements ----
			jade.util.leap.ArrayList adiabaticEfficincies = new jade.util.leap.ArrayList();
			CharacteristicDiagramMeasurements ogeCharDiaMes = ogeTurboComp.getCharacteristicDiagramMeasurements();
			List<AdiabaticEfficiency> ogeAdiabaticEffics = ogeCharDiaMes.getAdiabaticEfficiency();
			for (int j=0; j<ogeAdiabaticEffics.size(); j++) {
				AdiabaticEfficiency ogeAdiaEff = ogeAdiabaticEffics.get(j);
				
				CompStatAdiabaticEfficiency adiaEff = new CompStatAdiabaticEfficiency();
				adiaEff.setAdiabaticEfficiency(ogeAdiaEff.getValue());
				// --- Measurements --- Start -------------
				jade.util.leap.ArrayList adiaEffMeasures = new jade.util.leap.ArrayList();
				List<TcMeasurementType> ogeMeasures = ogeAdiaEff.getMeasurement();
				for (int k=0; k < ogeMeasures.size(); k++) {
					TcMeasurementType ogeTCMeasure = ogeMeasures.get(k);
					
					CompStatTcMeasurement tcMeasure = new CompStatTcMeasurement();
					tcMeasure.setAdiabaticHead(ValueTypeFactory.newInstance(ogeTCMeasure.getAdiabaticHead()));
					tcMeasure.setSpeed(ValueTypeFactory.newInstance(ogeTCMeasure.getSpeed()));
					tcMeasure.setVolumetricFlowrate(ValueTypeFactory.newInstance(ogeTCMeasure.getVolumetricFlowrate()));
					
					adiaEffMeasures.add(tcMeasure);
				}
				if (adiaEffMeasures.size()>0) {
					adiaEff.setMeasurements(adiaEffMeasures);	
				}
				
				// --- Measurements --- End ---------------
				adiabaticEfficincies.add(adiaEff);
			}
			localTurboComp.setCharacteristicDiagramMeasurements(adiabaticEfficincies);
			
			// --- 
			
			// --- Add to the list of compressor list -----
			localTurboComps.add(localTurboComp);
		}
		
		// --- Piston compressor --------------------------		
		List<PistonCompressorType> ogePistonComps = ogeCompressors.getPistonCompressor();
		for (int i=0; i<ogePistonComps.size(); i++) {
			
			PistonCompressorType ogePistonComp = (PistonCompressorType) ogePistonComps.get(i);
			
			// --- Initiate the compressor ----------------
			PistonCompressor localPistonComp = new PistonCompressor();
			localPistonComp.setID(ogePistonComp.getId());
			localPistonComp.setAlias(ogePistonComp.getId());
			localPistonComp.setDrive(ogePistonComp.getDrive());
			
			localPistonComp.setSpeedMin(ValueTypeFactory.newInstance(ogePistonComp.getSpeedMin()));
			localPistonComp.setSpeedMax(ValueTypeFactory.newInstance(ogePistonComp.getSpeedMax()));
			
			localPistonComp.setOperatingVolume(ValueTypeFactory.newInstance(ogePistonComp.getOperatingVolume()));
			localPistonComp.setMaximalTorque(ValueTypeFactory.newInstance(ogePistonComp.getMaximalTorque()));
			localPistonComp.setMaximalCompressionRatio((float) ogePistonComp.getMaximalCompressionRatio().getValue());
			localPistonComp.setAdiabaticEfficiencyPiston((float) ogePistonComp.getAdiabaticEfficiency().getValue());
			localPistonComp.setAdditionalReductionVolFlow((float) ogePistonComp.getAdditionalReductionVolFlow().getValue());
			
			// --- Add to the list of compressor list -----
			localPistonComps.add(localPistonComp);
		}
		
		// --- Finally add the compressor -----------------
		if (localTurboComps.size()>0) {
			localCompressorStation.setTurboCompressor(localTurboComps);	
		}
		if (localPistonComps.size()>0) {
			localCompressorStation.setPistonCompressor(localPistonComps);
		}
		
	}
	
}
