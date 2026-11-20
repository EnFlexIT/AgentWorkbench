package de.enflexit.df.core.processing.transformation;

import java.util.ArrayList;

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
	
	private ArrayList<DataTransformationService> knownDataTransformaitons;

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
	 * Register transformation services.
	 */
	private void registerTransformationServices() {
		this.getKnownDataTransformations().add(new DataTransformationServiceCalculateMetrics());
		this.getKnownDataTransformations().add(new DataTransformationServiceColumnOperation());
		this.getKnownDataTransformations().add(new DataTransformationServiceFilterRows());
		this.getKnownDataTransformations().add(new DataTransformationServiceJoinTables());
		this.getKnownDataTransformations().add(new DataTransformationServiceSelectColumns());
	}
	
	/**
	 * Gets the known data transformations.
	 * @return the known data transformations
	 */
	public ArrayList<DataTransformationService> getKnownDataTransformations() {
		if (knownDataTransformaitons==null) {
			knownDataTransformaitons = new ArrayList<DataTransformationService>();
		}
		return knownDataTransformaitons;
	}
	
	
}
