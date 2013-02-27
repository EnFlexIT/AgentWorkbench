package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStat
* @author ontology bean generator
* @version 2013/02/26, 16:41:10
*/
public class CompStat extends GridComponent{ 

//////////////////////////// User code
/**
 * Returns the list of all compressor (turbo compressor as well as piston compressor).
 * @return the list of all compressor
 */
public List getCompressor() {
	ArrayList compressor = new ArrayList();
	ArrayList turboCompressor = (ArrayList) this.getTurboCompressor();
	for (int i=0; i<turboCompressor.size(); i++) {
		compressor.add(turboCompressor.get(i));
	}
	ArrayList pistonCompressor = (ArrayList) this.getPistonCompressor();
	for (int i=0; i<pistonCompressor.size(); i++) {
		compressor.add(pistonCompressor.get(i));
	}
	return compressor;
}

/**
 * Returns the list of all drives for this compressor station 
 * (gas turbines, gas driven motors, electric motors and steam turbines).
 * @return the list of all drives for this compressor station
 */
public List getDrives() {
	ArrayList drives = new ArrayList();
	ArrayList gasTurbines = (ArrayList) this.getGasTurbines();
	for (int i=0; i<gasTurbines.size(); i++) {
		drives.add(gasTurbines.get(i));
	}
	ArrayList gasDrivenMotors = (ArrayList) this.getGasDrivenMotors();
	for (int i=0; i<gasDrivenMotors.size(); i++) {
		drives.add(gasDrivenMotors.get(i));
	}
	ArrayList electricMotors = (ArrayList) this.getElectricMotors();
	for (int i=0; i<electricMotors.size(); i++) {
		drives.add(electricMotors.get(i));
	}
	ArrayList steamTurbines = (ArrayList) this.getSteamTurbines();
	for (int i=0; i<steamTurbines.size(); i++) {
		drives.add(steamTurbines.get(i));
	}
	return drives;
}
   /**
* Protege name: upgradeCosts
   */
   private float upgradeCosts;
   public void setUpgradeCosts(float value) { 
    this.upgradeCosts=value;
   }
   public float getUpgradeCosts() {
     return this.upgradeCosts;
   }

   /**
* Protege name: pistonCompressor
   */
   private List pistonCompressor = new ArrayList();
   public void addPistonCompressor(PistonCompressor elem) { 
     List oldList = this.pistonCompressor;
     pistonCompressor.add(elem);
   }
   public boolean removePistonCompressor(PistonCompressor elem) {
     List oldList = this.pistonCompressor;
     boolean result = pistonCompressor.remove(elem);
     return result;
   }
   public void clearAllPistonCompressor() {
     List oldList = this.pistonCompressor;
     pistonCompressor.clear();
   }
   public Iterator getAllPistonCompressor() {return pistonCompressor.iterator(); }
   public List getPistonCompressor() {return pistonCompressor; }
   public void setPistonCompressor(List l) {pistonCompressor = l; }

   /**
* Protege name: buildingCosts
   */
   private float buildingCosts;
   public void setBuildingCosts(float value) { 
    this.buildingCosts=value;
   }
   public float getBuildingCosts() {
     return this.buildingCosts;
   }

   /**
* Protege name: gasTurbines
   */
   private List gasTurbines = new ArrayList();
   public void addGasTurbines(GasTurbine elem) { 
     List oldList = this.gasTurbines;
     gasTurbines.add(elem);
   }
   public boolean removeGasTurbines(GasTurbine elem) {
     List oldList = this.gasTurbines;
     boolean result = gasTurbines.remove(elem);
     return result;
   }
   public void clearAllGasTurbines() {
     List oldList = this.gasTurbines;
     gasTurbines.clear();
   }
   public Iterator getAllGasTurbines() {return gasTurbines.iterator(); }
   public List getGasTurbines() {return gasTurbines; }
   public void setGasTurbines(List l) {gasTurbines = l; }

   /**
* Protege name: configurations
   */
   private List configurations = new ArrayList();
   public void addConfigurations(CompStatConfiguration elem) { 
     List oldList = this.configurations;
     configurations.add(elem);
   }
   public boolean removeConfigurations(CompStatConfiguration elem) {
     List oldList = this.configurations;
     boolean result = configurations.remove(elem);
     return result;
   }
   public void clearAllConfigurations() {
     List oldList = this.configurations;
     configurations.clear();
   }
   public Iterator getAllConfigurations() {return configurations.iterator(); }
   public List getConfigurations() {return configurations; }
   public void setConfigurations(List l) {configurations = l; }

   /**
* Protege name: steamTurbines
   */
   private List steamTurbines = new ArrayList();
   public void addSteamTurbines(SteamTurbine elem) { 
     List oldList = this.steamTurbines;
     steamTurbines.add(elem);
   }
   public boolean removeSteamTurbines(SteamTurbine elem) {
     List oldList = this.steamTurbines;
     boolean result = steamTurbines.remove(elem);
     return result;
   }
   public void clearAllSteamTurbines() {
     List oldList = this.steamTurbines;
     steamTurbines.clear();
   }
   public Iterator getAllSteamTurbines() {return steamTurbines.iterator(); }
   public List getSteamTurbines() {return steamTurbines; }
   public void setSteamTurbines(List l) {steamTurbines = l; }

   /**
* Protege name: turboCompressor
   */
   private List turboCompressor = new ArrayList();
   public void addTurboCompressor(TurboCompressor elem) { 
     List oldList = this.turboCompressor;
     turboCompressor.add(elem);
   }
   public boolean removeTurboCompressor(TurboCompressor elem) {
     List oldList = this.turboCompressor;
     boolean result = turboCompressor.remove(elem);
     return result;
   }
   public void clearAllTurboCompressor() {
     List oldList = this.turboCompressor;
     turboCompressor.clear();
   }
   public Iterator getAllTurboCompressor() {return turboCompressor.iterator(); }
   public List getTurboCompressor() {return turboCompressor; }
   public void setTurboCompressor(List l) {turboCompressor = l; }

   /**
* Protege name: gasDrivenMotors
   */
   private List gasDrivenMotors = new ArrayList();
   public void addGasDrivenMotors(GasDrivenMotor elem) { 
     List oldList = this.gasDrivenMotors;
     gasDrivenMotors.add(elem);
   }
   public boolean removeGasDrivenMotors(GasDrivenMotor elem) {
     List oldList = this.gasDrivenMotors;
     boolean result = gasDrivenMotors.remove(elem);
     return result;
   }
   public void clearAllGasDrivenMotors() {
     List oldList = this.gasDrivenMotors;
     gasDrivenMotors.clear();
   }
   public Iterator getAllGasDrivenMotors() {return gasDrivenMotors.iterator(); }
   public List getGasDrivenMotors() {return gasDrivenMotors; }
   public void setGasDrivenMotors(List l) {gasDrivenMotors = l; }

   /**
* Protege name: electricMotors
   */
   private List electricMotors = new ArrayList();
   public void addElectricMotors(ElectricMotor elem) { 
     List oldList = this.electricMotors;
     electricMotors.add(elem);
   }
   public boolean removeElectricMotors(ElectricMotor elem) {
     List oldList = this.electricMotors;
     boolean result = electricMotors.remove(elem);
     return result;
   }
   public void clearAllElectricMotors() {
     List oldList = this.electricMotors;
     electricMotors.clear();
   }
   public Iterator getAllElectricMotors() {return electricMotors.iterator(); }
   public List getElectricMotors() {return electricMotors; }
   public void setElectricMotors(List l) {electricMotors = l; }

}
