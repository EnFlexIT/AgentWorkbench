package de.enflexit.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.enflexit.common.ServiceFinder;

/**
 * The Class ExpressionServiceHelper provides some static help methods to work with the registered {@link ExpressionService}s.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionServiceHelper {

	private static HashMap<ExpressionType, ExpressionService> availableExpressionServices;
	
	/**
	 * Provides a dictionary of all available {@link ExpressionService}s, accessible by the corresponding {@link ExpressionType}.
	 * @return the available expression services
	 */
	public static HashMap<ExpressionType, ExpressionService> getAvailableExpressionServices() {
		if (availableExpressionServices==null) {
			availableExpressionServices = new HashMap<>();
			List<ExpressionService> expressionServices = ServiceFinder.findServices(ExpressionService.class);
			for (int i=0; i<expressionServices.size(); i++) {
				ExpressionService expressionService = expressionServices.get(i);
				availableExpressionServices.put(expressionService.getExpressionType(), expressionService);
			}
		}
		return availableExpressionServices;
	}
	
	/**
	 * Returns the expression service for the specified {@link ExpressionType}.
	 *
	 * @param expressionType the expression type
	 * @return the expression service
	 */
	public static ExpressionService getExpressionService(ExpressionType expressionType) {
		return getAvailableExpressionServices().get(expressionType);
	}
	
	/**
	 * Returns the expression type for the specified type prefix.
	 *
	 * @param typePrefix the type prefix
	 * @return the expression type
	 */
	public static ExpressionType getExpressionType(String typePrefix) {
		ArrayList<ExpressionType> availableTypes = new ArrayList<>(ExpressionServiceHelper.getAvailableExpressionServices().keySet());
		for (ExpressionType type : availableTypes){
			if (type.getTypePrefix().equals(typePrefix)) {
				return type;
			}
		}
		return null;
	}
	
}
