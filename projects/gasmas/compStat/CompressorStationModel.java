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
package gasmas.compStat;

import gasmas.ontology.CompStat;
import gasmas.ontology.CompStatCompressor;
import gasmas.ontology.CompStatDrive;
import gasmas.ontology.ElectricMotor;
import gasmas.ontology.GasDrivenMotor;
import gasmas.ontology.GasTurbine;
import gasmas.ontology.GridComponent;
import gasmas.ontology.PistonCompressor;
import gasmas.ontology.SteamTurbine;
import gasmas.ontology.TurboCompressor;

import jade.util.leap.List;

import java.util.Observable;

/**
 * The Class CompressorStationModel.
 */
public class CompressorStationModel extends Observable {

	/** The Enum Update. */
	public static enum UPDATE {
		CompressorStation, 		// --- General Notification ---
		TurboCompressorAdd,		// --- Add notification -------
		PistonCompressorAdd,	// --- Add notification -------
		GasTurbineAdd,			// --- Add notification -------
		GasDrivenMotorAdd,		// --- Add notification -------
		ElectricMotorAdd,		// --- Add notification -------
		SteamTurbineAdd,		// --- Add notification -------
		DeleteComponent			// --- Delete Notification ----
	}
	
	private CompStat myCompressorStation = null;
	
	/**
	 * Instantiates a new compressor station model with an observer pattern.
	 */
	public CompressorStationModel() {
	}

	/**
	 * Sets the state to changed and notifies with the given object.
	 * @param notification the new changed and notify
	 */
	public void setChangedAndNotify(Object notification) {
		this.setChanged();
		this.notifyObservers(notification);
	}
	/**
	 * The Class Notification can be used in order to inform Observer about specific
	 * events in the context of the compressor station.
	 */
	public class Notification {
		
		private UPDATE reason;
		private Object infoObject;
		
		/**
		 * Instantiates a new notification regarding the compressor station model.
		 *
		 * @param reason on of the reasons that are specified in the enumeration {@link UPDATE}  
		 * @param infoObject an info object
		 */
		public Notification(UPDATE reason, Object infoObject) {
			this.setReason(reason);
			this.setInfoObject(infoObject);
		}

		public void setReason(UPDATE reason) {
			this.reason = reason;
		}
		public UPDATE getReason() {
			return reason;
		}

		public void setInfoObject(Object infoObject) {
			this.infoObject = infoObject;
		}
		public Object getInfoObject() {
			return infoObject;
		}
	}
	
	/**
	 * Can be used as a kind of factory method to create and send a new notification.
	 *
	 * @param reason one reason that is specified in the enumeration {@link UPDATE}
	 * @param infoObject an info object
	 */
	public void createAndSendNotification(UPDATE reason, Object infoObject) {
		this.setChangedAndNotify(new Notification(reason, infoObject));
	}
	
	/**
	 * Sets the compressor station.
	 * @param myCompressorStation the new my compressor station
	 */
	public void setCompStat(CompStat mewCompStat) {
		this.myCompressorStation = mewCompStat;
		this.setChangedAndNotify(UPDATE.CompressorStation);
	}
	/**
	 * Gets the  compressor station instance.
	 * @return the compressor station description as instance of {@link CompStat}.
	 */
	public CompStat getCompStat() {
		if (myCompressorStation==null) {
			myCompressorStation = new CompStat();
		}
		return myCompressorStation;
	}

	/**
	 * Checks if a givven ID-String is not already in use .
	 *
	 * @param checkID the user input to check
	 * @return true, if is new id
	 */
	public boolean isNewID(String checkID) {
		
		List allCompressor = this.getCompStat().getCompressor();
		for (int i = 0; i < allCompressor.size(); i++) {
			CompStatCompressor compressor = (CompStatCompressor) allCompressor.get(i); 
			if (compressor.getID().equalsIgnoreCase(checkID)) {
				return false;
			}
		}

		List allDrives = this.getCompStat().getDrives();
		for (int i = 0; i < allDrives.size(); i++) {
			CompStatDrive compressor = (CompStatDrive) allDrives.get(i); 
			if (compressor.getID().equalsIgnoreCase(checkID)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds a new turbo compressor to the compressor station, if the specified new ID is not already in use. 
	 * @param newID the id for the turbo compressor 
	 */
	public void addTurboCompressor(String newID) {
		if (isNewID(newID)==false) return;
		TurboCompressor turboCompressor = new TurboCompressor();
		turboCompressor.setID(newID);
		this.getCompStat().getTurboCompressor().add(turboCompressor);
		this.createAndSendNotification(UPDATE.TurboCompressorAdd, turboCompressor);
	}
	/**
	 * Adds a new piston compressor to the compressor station, if the specified new ID is not already in use. 
	 * @param newID the id for the piston compressor 
	 */
	public void addPistonCompressor(String newID) {
		if (isNewID(newID)==false) return;
		PistonCompressor pistonCompressor = new PistonCompressor();
		pistonCompressor.setID(newID);
		this.getCompStat().getPistonCompressor().add(pistonCompressor);
		this.createAndSendNotification(UPDATE.PistonCompressorAdd, pistonCompressor);
	}
	/**
	 * Adds a new gas turbine to the compressor station, if the specified new ID is not already in use. 
	 * @param newID the id for the gas turbine  
	 */
	public void addGasTurbine(String newID) {
		if (isNewID(newID)==false) return;
		GasTurbine gasTurbine = new GasTurbine();
		gasTurbine.setID(newID);
		this.getCompStat().getGasTurbines().add(gasTurbine);
		this.createAndSendNotification(UPDATE.GasTurbineAdd, gasTurbine);
	}
	/**
	 * Adds a new gas driven motor to the compressor station, if the specified new ID is not already in use. 
	 * @param newID the id for the gas driven motor 
	 */
	public void addGasDrivenMotor(String newID) {
		if (isNewID(newID)==false) return;
		GasDrivenMotor gasDrivenMotor = new GasDrivenMotor();
		gasDrivenMotor.setID(newID);
		this.getCompStat().getGasDrivenMotors().add(gasDrivenMotor);
		this.createAndSendNotification(UPDATE.GasDrivenMotorAdd, gasDrivenMotor);
	}
	/**
	 * Adds a new electric motor to the compressor station, if the specified new ID is not already in use. 
	 * @param newID the id for the electric motor  
	 */
	public void addElectricMotor(String newID) {
		if (isNewID(newID)==false) return;
		ElectricMotor electricMotor = new ElectricMotor();
		electricMotor.setID(newID);
		this.getCompStat().getElectricMotors().add(electricMotor);
		this.createAndSendNotification(UPDATE.ElectricMotorAdd, electricMotor);
	}
	/**
	 * Adds a new steam turbine to the compressor station, if the specified new ID is not already in use. 
	 * @param newID the id for the steam turbine   
	 */
	public void addSteamTurbine(String newID) {
		if (isNewID(newID)==false) return;
		SteamTurbine steamTurbine = new SteamTurbine();
		steamTurbine.setID(newID);
		this.getCompStat().getSteamTurbines().add(steamTurbine);
		this.createAndSendNotification(UPDATE.SteamTurbineAdd, steamTurbine);	
	}

	/**
	 * Delete a component specified by its ID.
	 * @param idComponent2Delete the id of the component to delete
	 */
	public void deleteComponent(String idComponent2Delete) {
		
		// --- TurboCompressor ----------------------------
		List allElements = this.getCompStat().getTurboCompressor();
		for (int i = 0; i < allElements.size(); i++) {
			TurboCompressor component = (TurboCompressor) allElements.get(i); 
			if (component.getID().equalsIgnoreCase(idComponent2Delete)) {
				this.getCompStat().removeTurboCompressor(component);
				this.createAndSendNotification(UPDATE.DeleteComponent, component);
				return;
			}
		}
		// --- PistonCompressor ---------------------------
		allElements = this.getCompStat().getPistonCompressor();
		for (int i = 0; i < allElements.size(); i++) {
			PistonCompressor component = (PistonCompressor) allElements.get(i); 
			if (component.getID().equalsIgnoreCase(idComponent2Delete)) {
				this.getCompStat().removePistonCompressor(component);
				this.createAndSendNotification(UPDATE.DeleteComponent, component);
				return;
			}
		}
		// --- GasTurbine ---------------------------------
		allElements = this.getCompStat().getGasTurbines();
		for (int i = 0; i < allElements.size(); i++) {
			GasTurbine component = (GasTurbine) allElements.get(i); 
			if (component.getID().equalsIgnoreCase(idComponent2Delete)) {
				this.getCompStat().removeGasTurbines(component);
				this.createAndSendNotification(UPDATE.DeleteComponent, component);
				return;
			}
		}
		// --- GasDrivenMotor -----------------------------
		allElements = this.getCompStat().getGasDrivenMotors();
		for (int i = 0; i < allElements.size(); i++) {
			GasDrivenMotor component = (GasDrivenMotor) allElements.get(i); 
			if (component.getID().equalsIgnoreCase(idComponent2Delete)) {
				this.getCompStat().removeGasDrivenMotors(component);
				this.createAndSendNotification(UPDATE.DeleteComponent, component);
				return;
			}
		}
		// --- ElectricMotor ------------------------------
		allElements = this.getCompStat().getElectricMotors();
		for (int i = 0; i < allElements.size(); i++) {
			ElectricMotor component = (ElectricMotor) allElements.get(i); 
			if (component.getID().equalsIgnoreCase(idComponent2Delete)) {
				this.getCompStat().removeElectricMotors(component);
				this.createAndSendNotification(UPDATE.DeleteComponent, component);
				return;
			}
		}
		// --- SteamTurbine -------------------------------
		allElements = this.getCompStat().getElectricMotors();
		for (int i = 0; i < allElements.size(); i++) {
			SteamTurbine component = (SteamTurbine) allElements.get(i); 
			if (component.getID().equalsIgnoreCase(idComponent2Delete)) {
				this.getCompStat().removeSteamTurbines(component);
				this.createAndSendNotification(UPDATE.DeleteComponent, component);
				return;
			}
		}
		
	}

	/**
	 * Returns a compressor station component specified by its ID.
	 *
	 * @param componentID the compressor id
	 * @return the component
	 */
	public GridComponent getComponent(String componentID) {
		GridComponent component = null;
		List compressorStationComponents = this.getCompStat().getCompressorStationComponents();
		for (int i = 0; i < compressorStationComponents.size(); i++) {
			GridComponent listComponent = (GridComponent) compressorStationComponents.get(i);
			if (listComponent.getID().equals(componentID)) {
				component = listComponent;
				break;
			}
		}
		return component;
	}

	/**
	 * Update component.
	 * @param updatedComponent the component
	 */
	public void updateComponent(GridComponent updatedComponent) {
		
		List componentList = null;
		if (updatedComponent instanceof TurboCompressor) {
			componentList = this.getCompStat().getTurboCompressor();
		} else if (updatedComponent instanceof PistonCompressor) {
			componentList = this.getCompStat().getPistonCompressor();
		} else if (updatedComponent instanceof GasTurbine) {
			componentList = this.getCompStat().getGasTurbines();
		} else if (updatedComponent instanceof GasDrivenMotor) {
			componentList = this.getCompStat().getGasDrivenMotors();
		} else if (updatedComponent instanceof ElectricMotor) {
			componentList = this.getCompStat().getElectricMotors();
		} else if (updatedComponent instanceof SteamTurbine) {
			componentList = this.getCompStat().getSteamTurbines();
		}
		
		// --- Run through the list found -----------------
		for (int i = 0; i < componentList.size(); i++) {
			GridComponent listComponent = (GridComponent) componentList.get(i);
			if (listComponent.getID().equals(updatedComponent.getID())) {
				listComponent = updatedComponent;
				break;
			}
		}

	}
	
}
