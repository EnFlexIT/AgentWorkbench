package de.enflexit.df.core.processing.transformation;

import java.util.ArrayList;

import de.enflexit.common.ServiceFinder;
import de.enflexit.df.core.processing.transformation.columnOperation.DataTransformationServiceColumnOperation;
import de.enflexit.df.core.processing.transformation.filter.DataTransformationServiceFilterRows;
import de.enflexit.df.core.processing.transformation.join.DataTransformationServiceJoinTables;
import de.enflexit.df.core.processing.transformation.metrics.DataTransformationServiceCalculateMetrics;

/**
 * This class manages the known data transformation implementations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationRegistry {
	
	private static DataTransformationRegistry instance;
	
	private ArrayList<DataTransformationService> internalTransformaitons;

	/**
	 * Instantiates a new data transformation registry. Private singleton constructor.
	 */
	private DataTransformationRegistry() {
		this.registerTransformationServices();
	}
	
	/**
	 * Gets the single instance of DataTransformationRegistry.
	 * @return single instance of DataTransformationRegistry
	 */
	public static DataTransformationRegistry getInstance() {
		if (instance==null) {
			instance = new DataTransformationRegistry();
		}
		return instance;
	}
	
	/**
	 * Gets the known data transformations.
	 * @return the known data transformations
	 */
	private ArrayList<DataTransformationService> getInternalDataTransformations() {
		if (internalTransformaitons==null) {
			internalTransformaitons = new ArrayList<DataTransformationService>();
		}
		return internalTransformaitons;
	}
	/**
	 * Register transformation services.
	 */
	private void registerTransformationServices() {
		this.getInternalDataTransformations().add(new DataTransformationServiceCalculateMetrics());
		this.getInternalDataTransformations().add(new DataTransformationServiceColumnOperation());
		this.getInternalDataTransformations().add(new DataTransformationServiceFilterRows());
		this.getInternalDataTransformations().add(new DataTransformationServiceJoinTables());
		this.getInternalDataTransformations().add(new DataTransformationServiceSelectColumns());
	}
	
	/**
	 * Gets the known data transformations.
	 * @return the known data transformations
	 */
	public ArrayList<DataTransformationService> getKnownDataTransformations() {
		ArrayList<DataTransformationService> knownDataTransformations= new ArrayList<DataTransformationService>();
		knownDataTransformations.addAll(this.getInternalDataTransformations());
		knownDataTransformations.addAll(ServiceFinder.findServices(DataTransformationService.class));
		return knownDataTransformations;
	}
	
	
}
