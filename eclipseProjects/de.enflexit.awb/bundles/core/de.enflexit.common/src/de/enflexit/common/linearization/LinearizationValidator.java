package de.enflexit.common.linearization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * The Class LinearizationValidator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LinearizationValidator {

	public static final String NO_ERRORS_FOUND = "No errors were found!";
	
	private Linearization linearization;
	private List<String> validationMessages;
	
	/**
	 * Instantiates a new linearization validity check.
	 * @param linearization the linearization
	 */
	public LinearizationValidator(Linearization linearization) {
		this.linearization = linearization;
	}
	
	/**
	 * Checks if is valid linearization.
	 * @return true, if is valid linearization
	 */
	public boolean isValidLinearization() {
		
		if (this.getValidationMessages().size()==0) this.doChecks();
		if (this.getValidationMessages().size()==1 && this.getValidationMessages().get(0).equals(NO_ERRORS_FOUND)==true) {
			return true;
		}
		return false;
	}
	/**
	 * Returns the current list of validity messages. Will be filled 
	 * with the first call of {@link #isValidLinearization()}
	 * 
	 * @return the validity messages
	 */
	public List<String> getValidationMessages() {
		if (validationMessages==null) {
			validationMessages = new ArrayList<>();
		}
		return validationMessages;
	}
	
	/**
	 * Does the validation checks in a dedicated thread.
	 */
	public void doChecksInThread() {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				LinearizationValidator.this.doChecks();
			}
		}, "Validation of Linearization").start(); 	
	}
	/**
	 * Does the validation checks.
	 */
	public void doChecks() {
		
		// --- Clear messages -----------------------------
		this.getValidationMessages().clear();

		// --- Execute checks -----------------------------
		this.checkStructure();
		this.checkFormulaKeys();
		this.checkRangeKeys();
		this.checkValueRangesOfLinearCoefficients();
		
		// --- Add 'OK' message if nothing was found ------
		if (this.getValidationMessages().size()==0) {
			this.getValidationMessages().add(NO_ERRORS_FOUND);
		}
		// --- Fire property change event -----------------
		this.linearization.firePropertyChangeEvent(Linearization.PROPERTY_VALIDATION_DONE, null, this.getValidationMessages());
	}
	
	/**
	 * Checks the structure of the linearization.
	 */
	private void checkStructure() {
		
		int noOfVariables = this.linearization.getNumberOfVariableIDs();
		int noOfFormulas  = this.linearization.getLinearFormulaList().size();
		
		if (noOfVariables==0) {
			if (noOfFormulas>1) {
				this.getValidationMessages().add("The linearization contains no variables, but defines more than one linear formula (" + noOfFormulas + " are defind instead).");
			}
		}
		
		if (noOfFormulas==0) {
			this.getValidationMessages().add("The linearization does not define a single linear formula.");
		}
		
	}
	
	/**
	 * Checks all formulas settings by using the formula key.
	 */
	private void checkFormulaKeys() {

		List<Double> formulaKeysChecked = new ArrayList<>();
		List<Double> formulaKeys = this.linearization.getFormulaKeyList();
		for (Double formulaKey : formulaKeys) {
			
			// --- Already checked formula key ----------------------
			if (formulaKeysChecked.contains(formulaKey)==true) continue;
			formulaKeysChecked.add(formulaKey);
			
			// --- Check number of equal formulas ------------------- 
			List<LinearFormula> formuList = this.linearization.getLinearFormulaByFormulaKey(formulaKey);
			if (formuList.size()==1) continue;
			
			// --- Write error message ------------------------------
			this.getValidationMessages().add(formuList.size() + " formulas have indentical parameter settings.");
		}
	}
	
	/**
	 * Checks all formulas settings by using the range key..
	 */
	private void checkRangeKeys() {

		List<Double> rangeKeysChecked = new ArrayList<>();
		List<Double> rangeKeys = this.linearization.getRangeKeyList();
		for (Double rangeKey : rangeKeys) {
			
			// --- Already checked formula key ----------------------
			if (rangeKeysChecked.contains(rangeKey)==true) continue;
			rangeKeysChecked.add(rangeKey);
			
			// --- Check number of equal formulas ------------------- 
			List<LinearFormula> formuList = this.linearization.getLinearFormulaByRangeKey(rangeKey);
			if (formuList.size()==1) continue;
			
			// --- Write error message ------------------------------
			this.getValidationMessages().add(formuList.size() + " formulas have indentical range settings.");
		}
	}
	
	/**
	 * Check for all variableID's the value ranges of the linear coefficients.
	 */
	private void checkValueRangesOfLinearCoefficients() {
		
		// --- Check all ranges of all variables -------------------- 
		TreeMap<String, List<LinearCoefficient>> rangeMap = this.linearization.getCoefficientRangeTreeMap();
		for (String variableID : rangeMap.keySet()) {
			this.checkValueRange(variableID, rangeMap.get(variableID));
		}
	}
	/**
	 * Check for all variableID's the value ranges of the linear coefficients.
	 *
	 * @param variableID the current variableID
	 * @param linearCoeffList the list of linear coefficients
	 */
	private void checkValueRange(String variableID, List<LinearCoefficient> linearCoeffList) {
		
		// --- Sort by value from ------------------------- 
		Collections.sort(linearCoeffList, LinearCoefficient.getComparatorValidFrom());
		
		// --- Check the queue of ranges ------------------ 
		double validFromPrev = Double.NaN;
		double validToPrev   = Double.NaN;
		double lowerBoundary = this.linearization.getLowerBoundary(variableID);
		double upperBoundary = this.linearization.getUpperBoundary(variableID);
		
		for (int i = 0; i < linearCoeffList.size(); i++) {
			
			LinearCoefficient linCoeff = linearCoeffList.get(i);
			double validFrom = linCoeff.getValidFrom();
			double validTo   = linCoeff.getValidTo();
			
			boolean isEqualToPrevRange = (validFrom==validFromPrev && validTo==validToPrev);
			if (isEqualToPrevRange==true) continue;
			
			if (validTo < validFrom) {
				this.getValidationMessages().add("The 'to' value of '" + variableID + "' is smaller than the corresponding 'from' value (" + validTo + " > " + validFrom + ")!");
				continue;
			} 

			if (i==0) {
				// --- Check first value ------------------
				if (validFrom != lowerBoundary) {
					this.getValidationMessages().add("The lower 'from' value of '" + variableID + "' is not equal to the lower boundary defined for the variable (" + validFrom + " <> " + lowerBoundary + ")!");
				}
				
			} else if (i==(linearCoeffList.size()-1)) {
				// --- Check last value -------------------
				if (validTo != upperBoundary) {
					this.getValidationMessages().add("The upper 'to' value of '" + variableID + "' is not equal to the upper boundary defined for the variable (" + validTo + " <> " + upperBoundary + ")!");
				}
				
			}
			
			// --- Check intermediate ranges --------------
			if (validFrom > validToPrev) {
				this.getValidationMessages().add("Definition gap: The 'from' value of '" + variableID + "' is greater than the previous 'to' value (" + validFrom + " > " + validToPrev + ")!");
			} else if (validFrom < validToPrev) {
				this.getValidationMessages().add("Definition overlap: The 'from' value of '" + variableID + "' is smaller than the previous 'valid to' value (" + validFrom + " < " + validToPrev + ")!");
			}
			
			// --- Remind for the next round --------------
			validFromPrev = validFrom;
			validToPrev   = validTo;
		}
	}
	
	
	
}
