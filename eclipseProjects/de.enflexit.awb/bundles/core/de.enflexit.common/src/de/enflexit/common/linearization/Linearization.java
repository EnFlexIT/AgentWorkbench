//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.12.05 at 09:28:32 AM CET 
//


package de.enflexit.common.linearization;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 			Describes all linear formulas that belong to a mathematical, stepwise linearization.
 * 			
 * 
 * <p>Java class for Linearization complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Linearization"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LinearFormulaList" type="{http://www.dawis.wiwi.uni-due.de/EnergyOptionModel}LinearFormula" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Linearization", propOrder = {
    "linearFormulaList"
})
public class Linearization
    implements Serializable
{

    private final static long serialVersionUID = 201404191434L;
    @XmlElement(name = "LinearFormulaList", required = true)
    protected List<LinearFormula> linearFormulaList;

    
    // --- Constants and variables for linearization boundaries ---------------
    public static final double DEFAULT_DOUBLE_VALUE_MAX = 999999;
    public static final double DEFAULT_DOUBLE_VALUE_MIN = -DEFAULT_DOUBLE_VALUE_MAX;

    private transient LinearizationBoundaries linearizationBoundaries;
    private transient LinearizationValidator validator;

    
    // --- Constants and variables to accelerate result evaluation ------------
    private transient TreeMap<String, Double> validFromMinTreeMap;
    private transient TreeMap<String, Double> validToMaxTreeMap;
    private transient long timeToRenewAfterLastRequest = 1000;	// ms
    private transient long lastRequestTime;
    
    // --- Constants and variables for property changes -----------------------
    public static final String PROPERTY_LINEAR_COEFFICIENT_ADDED = "LinearCoefficientAdded";
    public static final String PROPERTY_LINEAR_COEFFICIENT_REMOVED = "LinearCoefficientRemoved";
    public static final String PROPERTY_LINEAR_COEFFICIENT_RENAMED = "LinearCoefficientRenamed";
    
    public static final String PROPERTY_LINEAR_FORMULA_ADDED = "LinearFormulaAdded";
    public static final String PROPERTY_LINEAR_FORMULA_REMOVED = "LinearFormulaRemoved";
    
    public static final String PROPERTY_VALIDATION_DONE = "LinearizationValidationDone";
    
    private transient List<PropertyChangeListener> propertyChangeListener;
    
    
    /**
     * Gets the value of the linearFormulaList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linearFormulaList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinearFormulaList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinearFormula }
     * 
     * 
     */
    public List<LinearFormula> getLinearFormulaList() {
        if (linearFormulaList == null) {
            linearFormulaList = new ArrayList<LinearFormula>();
        }
        return this.linearFormulaList;
    }

    
    // ------------------------------------------------------------------------
    // --- From here customized code ------------------------------------------ 
    // ------------------------------------------------------------------------    
    /**
     * Gets the linearization validator.
     * @return the LinearizationValidator currently in use
     */
    public LinearizationValidator getValidator() {
    	if (validator==null) {
    		validator = new LinearizationValidator(this);
    	}
		return validator;
	}
    /**
     * Sets the linearization validator.
     * @param validator the LinearizationValidator to use
     */
    public void setValidator(LinearizationValidator linearizationValidator) {
		this.validator = linearizationValidator;
	}
    /**
     * Checks if is valid.
     * @return true, if is valid
     */
    public boolean isValid() {
    	return this.getValidator().isValidLinearization();
    }
    
    
    /**
     * Returns the linearization boundaries.
     * @return the linearization boundaries
     */
    public LinearizationBoundaries getLinearizationBoundaries() {
		if (linearizationBoundaries==null) {
			linearizationBoundaries = new LinearizationBoundaries();
		}
    	return linearizationBoundaries;
	}
    /**
     * Sets the current linearization boundaries.
     * @param linearizationBoundaries the new linearization boundaries
     */
    public void setLinearizationBoundaries(LinearizationBoundaries linearizationBoundaries) {
		this.linearizationBoundaries = linearizationBoundaries;
	}
	/**
	 * Returns the lower boundary value for the specified variable.
	 *
	 * @param variableID the variable ID
	 * @return the lower boundary value
	 */
	public double getLowerBoundary(String variableID) {
		return this.getLinearizationBoundaries().getLowerBoundaryValue(variableID);
	}
	/**
	 * Returns the upper boundary value for the specified variable.
	 *
	 * @param variableID the variable ID
	 * @return the upper boundary value
	 */
	public double getUpperBoundary(String variableID) {
		return this.getLinearizationBoundaries().getUpperBoundaryValue(variableID);
	}
    
    
    
    /**
     * Creates a LinearCoefficient.
     *
     * @param variableID the variable ID
     * @param value the value
     * @param validFrom the valid from
     * @param validTo the valid to
     * @return the linear coefficient
     */
    public LinearCoefficient createLinearCoefficient(String variableID, Double value, Double validFrom, Double validTo) {
    	return LinearCoefficient.createLinearCoefficient(variableID, value, validFrom, validTo);
    }
    /**
     * Adds the specified LinearCoefficient to all linear functions within the current Linearization.
     *
     * @param linearCoefficient the linear coefficient
     * @return true, if the LinearCoefficient could be added successfully
     */
    public boolean addLinearCoefficient(LinearCoefficient linearCoefficient) {
    	
    	if (this.isUsedVariableID(linearCoefficient.getVariableID())==true) return false;
    	
    	// --- If no formula is yet created, create one -------------
    	if (this.getLinearFormulaList().size()==0) {
    		this.getLinearFormulaList().add(this.createLinearFormula());
    	}
    	// --- Add coefficient to each formula ----------------------
    	for (LinearFormula linearFormula : this.getLinearFormulaList()) {
    		linearFormula.getCoefficientList().add(linearCoefficient.getCopy());
    	}
    	// --- Fire property change event ---------------------------
    	this.firePropertyChangeEvent(PROPERTY_LINEAR_COEFFICIENT_ADDED, null, linearCoefficient);
    	return true;
    }
    /**
     * Removes the linear coefficient with the specified variable ID from all linear functions.
     *
     * @param variableID the variable ID
     * @return true, if successful
     */
    public boolean removeLinearCoefficient(String variableID) {
		
    	boolean success = false;
    	for (LinearFormula formula : this.getLinearFormulaList()) {
    		LinearCoefficient linCoeffToDelete = formula.getCoefficient(variableID);
    		if (linCoeffToDelete!=null) {
    			success = formula.getCoefficientList().remove(linCoeffToDelete);
    		}
    	}
    	// --- Fire property change event ---------------------------
    	if (success==true) {
        	this.firePropertyChangeEvent(PROPERTY_LINEAR_COEFFICIENT_REMOVED, variableID, null);
    	}
    	return success;
	}
    /**
     * Renames all linear coefficients.
     *
     * @param oldVariableID the old variable ID
     * @param newVariableID the new variable ID
     * @return true, if successful
     */
    public void renameLinearCoefficient(String oldVariableID, String newVariableID) {
		
    	for (LinearFormula formula : this.getLinearFormulaList()) {
    		LinearCoefficient linCoeffToEdit = formula.getCoefficient(oldVariableID);
    		if (linCoeffToEdit!=null) {
    			linCoeffToEdit.setVariableID(newVariableID);
    		}
    	}
    	// --- Fire property change event ---------------------------
        this.firePropertyChangeEvent(PROPERTY_LINEAR_COEFFICIENT_RENAMED, oldVariableID, newVariableID);
	}
    
    
    
    /**
     * Creates a linear formula, considering the currently defined variableIDs.
     * @return the linear formula
     */
    public LinearFormula createLinearFormula() {
    	
    	LinearFormula formula = new LinearFormula();
    	formula.setAxisIntercept(0);
    	
    	// --- Create the require LinearCoefficients ---------------- 
    	List<String> variableIDs = this.getVariableIDs();
    	for (String variableID : variableIDs) {
    		formula.getCoefficientList().add(this.createLinearCoefficient(variableID, 0.0, this.getLowerBoundary(variableID), this.getUpperBoundary(variableID)));
    	}
    	return formula;
    }
    /**
     * Adds the specified linear formula to the current Linearization.
     *
     * @param linearFormula the linear formula to add
     * @return true, if successful
     */
    public boolean addLinearFormula(LinearFormula linearFormula) {
    	if (linearFormula==null) return false;
    	boolean success = this.getLinearFormulaList().add(linearFormula); 
    	if (success==true) {
    		this.firePropertyChangeEvent(PROPERTY_LINEAR_FORMULA_ADDED, null, linearFormula);
    	}
    	return success;
    }
    /**
     * Removes the specified linear formula from the current Linearization.
     *
     * @param linearFormula the linear formula to remove
     * @return true, if successful
     */
    public boolean removeLinearFormula(LinearFormula linearFormula) {
    	
    	if (linearFormula==null) return false;
    	
    	boolean success = false;
    	int indexPos = this.getLinearFormulaList().indexOf(linearFormula);
    	if (indexPos!=-1) {
    		if (this.getLinearFormulaList().size()==1) {
    			// --- In case that we have the last formula --------
    			LinearFormula formula = this.getLinearFormulaList().get(indexPos);
    			formula.getCoefficientList().clear();
    			this.firePropertyChangeEvent(PROPERTY_LINEAR_COEFFICIENT_REMOVED, null, null);
    			
    		} else {
    			if (this.getLinearFormulaList().remove(indexPos)!=null) {
    				success = true;
    				this.firePropertyChangeEvent(PROPERTY_LINEAR_FORMULA_REMOVED, linearFormula, null);
    			}
    		}
    		
    	}
    	return success;
    }
    /**
     * Returns the linear formulas that can be identified by the specified formula key.
     *
     * @param formulaKey the formula key
     * @return the list of formulas found for the key
     */
    public List<LinearFormula> getLinearFormulaByFormulaKey(double formulaKey) {
    	
    	List<LinearFormula> formulaList = new ArrayList<>();
    	for (LinearFormula formula : this.getLinearFormulaList()) {
    		if (formula.getFormulaKey()==formulaKey) {
    			formulaList.add(formula);
    		}
    	}
    	return formulaList;
    }
    /**
     * Returns the list of formula keys derived from the currently define formulas.
     * @return the formula key list
     */
    public List<Double> getFormulaKeyList() {
    	
    	List<Double> formulaKeyList = new ArrayList<>();
    	for (LinearFormula formula : this.getLinearFormulaList()) {
    		formulaKeyList.add(formula.getFormulaKey());
    	}
    	return formulaKeyList;
    }
    
    /**
     * Returns the linear formulas that can be identified by the specified formula key.
     *
     * @param rangeKey the formula key
     * @return the list of formulas found for the key
     */
    public List<LinearFormula> getLinearFormulaByRangeKey(double rangeKey) {
    	
    	List<LinearFormula> formulaList = new ArrayList<>();
    	for (LinearFormula formula : this.getLinearFormulaList()) {
    		if (formula.getRangeKey()==rangeKey) {
    			formulaList.add(formula);
    		}
    	}
    	return formulaList;
    }
    /**
     * Returns the list of range keys derived from the currently define formulas.
     * @return the formula key list
     */
    public List<Double> getRangeKeyList() {
    	
    	List<Double> formulaKeyList = new ArrayList<>();
    	for (LinearFormula formula : this.getLinearFormulaList()) {
    		formulaKeyList.add(formula.getRangeKey());
    	}
    	return formulaKeyList;
    }

    
    
    /**
     * Check if the specified variableID is used within the Linearization.
     *
     * @param variableID the variableID to check
     * @return true, if successful
     */
    public boolean isUsedVariableID(String variableID) {
    	return this.getVariableIDs().contains(variableID);
    }
    /**
     * Returns a sorted list of all variableID's used in the linearization.
     * @return the variableIDs
     */
    public List<String> getVariableIDs() {
    	
    	List<String> variableIDList = new ArrayList<>();
    	for (LinearFormula linearFormula : this.getLinearFormulaList()) {
    		for (LinearCoefficient linearCoefficient : linearFormula.getCoefficientList()) {
    			if (variableIDList.contains(linearCoefficient.getVariableID())==false) {
    				variableIDList.add(linearCoefficient.getVariableID());
    			}
    		}
    	}
    	Collections.sort(variableIDList);
    	return variableIDList;
    }
    /**
     * Gets the number of currently defined variables.
     * @return the number of variableID's
     */
    public int getNumberOfVariableIDs() {
		return this.getVariableIDs().size();
	}
    
    
    /**
     * Returns a TreeMap, where the key represents a variableID and the value the list of 
     * coefficients (with it ranges) used in the current linearization.
     * 
     * @return the coefficient range tree map
     */
    public TreeMap<String, List<LinearCoefficient>> getCoefficientRangeTreeMap() {

    	TreeMap<String, List<LinearCoefficient>> rangeMap = new TreeMap<>();
    	for (LinearFormula linFormula : this.getLinearFormulaList()) {
    		// --- Check coefficients of Formula --------------------
    		for (LinearCoefficient lcFormula : linFormula.getCoefficientList()) {
    			String varibaleID = lcFormula.getVariableID();
    			// --- Try to find the current list ----------------- 
    			List<LinearCoefficient> linearCoeffList = rangeMap.get(varibaleID);
    			if (linearCoeffList==null) {
    				// --- Create the list --------------------------
    				linearCoeffList = new ArrayList<>();
    				rangeMap.put(varibaleID, linearCoeffList);
    			}
    			// --- Add the coefficient --------------------------
    			linearCoeffList.add(lcFormula);
    		}
    	}
		return rangeMap;
	}
    
    /**
     * Returns the TreeMap filled with the minimum values for 'valid from' of all 
     * {@link LinearCoefficient}s defined with the current linearization.
     * @return the validFrom min TreeMap
     */
    public TreeMap<String, Double> getValidFromMinTreeMap() {
    	if (validFromMinTreeMap==null || this.isRenewValidFromMinAndValidToMaxTreeMap()) {
    		this.fillValidFromMinAndValidToMaxTreeMap();
    	}
    	this.lastRequestTime = System.currentTimeMillis();
		return validFromMinTreeMap;
	}
    /**
     * Returns the TreeMap filled with the maximum values for 'valid to' of all  
     * {@link LinearCoefficient}s defined with the current linearization.
     * @return the validFrom max TreeMap
     */
    public TreeMap<String, Double> getValidToMaxTreeMap() {
		if (validToMaxTreeMap==null || this.isRenewValidFromMinAndValidToMaxTreeMap()) {
			this.fillValidFromMinAndValidToMaxTreeMap();
		}
		this.lastRequestTime = System.currentTimeMillis();
    	return validToMaxTreeMap;
	}
    /**
     * Checks if  the 'valid from min' and the 'valid to max' TreeMaps needs to be renewed.
     * @return true, if the TreeMaps needs to be renewed
     */
    private boolean isRenewValidFromMinAndValidToMaxTreeMap() {
    	return System.currentTimeMillis()-this.timeToRenewAfterLastRequest>=this.lastRequestTime;
    }
    /**
     * Fills the 'valid from min' and the 'valid to max' TreeMaps.
     */
    private void fillValidFromMinAndValidToMaxTreeMap() {
    	
    	this.validFromMinTreeMap = new TreeMap<>();
    	this.validToMaxTreeMap = new TreeMap<>();
    	
    	TreeMap<String, List<LinearCoefficient>> rangeMap = this.getCoefficientRangeTreeMap();
    	for (String variabelID : rangeMap.keySet()) {
    		
    		List<LinearCoefficient> linearCoeffList = rangeMap.get(variabelID);
    		if (linearCoeffList!=null && linearCoeffList.size()!=0) {
    			// --- Get minimum 'valid from' value ---------------   
    			Collections.sort(linearCoeffList, LinearCoefficient.getComparatorValidFrom());
    			this.validFromMinTreeMap.put(variabelID, linearCoeffList.get(0).getValidFrom());
    			// --- Get maximum 'valid to' value -----------------
    			Collections.sort(linearCoeffList, LinearCoefficient.getComparatorValidTo());
    			this.validToMaxTreeMap.put(variabelID, linearCoeffList.get(linearCoeffList.size()-1).getValidTo());
    		}
    	}
    	this.lastRequestTime = System.currentTimeMillis();
    }
    
    
    /**
     * Returns a blueprint that can be filled with values for a later result calculation.
     * @return a variable value tree map without values
     * @see #getResult(TreeMap)
     */
    public TreeMap<String, Double> getBlueprintVariableValueTreeMap() {
    	
    	TreeMap<String, Double> varValueTreeMap = new TreeMap<>();
    	List<String> variableIDList = this.getVariableIDs();
    	for (String variableID : variableIDList) {
    		varValueTreeMap.put(variableID, null);
    	}
    	return varValueTreeMap;
    }
    /**
     * Returns the calculated linearization result value for the specified variable values.
     *
     * @param variableValueTreeMap the variable values as TreeMap
     * @return the calculated result value
     */
    public Double getResult(TreeMap<String, Double> variableValueTreeMap) {

    	// --- Get TreeMap with the maximum 'valid to' values -------
    	TreeMap<String, Double> validToMaxTreeMap = this.getValidToMaxTreeMap();
    	
    	// --- Get a LinearFormulaMatch list ------------------------
    	List<LinearFormulaMatch> formulaMatchList = new ArrayList<>();
    	for (LinearFormula formula : this.getLinearFormulaList()) {
    		LinearFormulaMatch match = formula.getLinearFormulaMatch(variableValueTreeMap, validToMaxTreeMap);
    		if (match!=null && match.getMatchWeight()>0) {
    			formulaMatchList.add(match);
    		}
    	}
    	
    	// --- Calculate the linearization result -------------------
    	if (formulaMatchList.size()==0) {
    		System.err.println("[" + this.getClass().getSimpleName() + "] No formula definition could be found for the variable values " + variableValueTreeMap.toString());
    		return null;
    		
    	} else if (formulaMatchList.size()>1) {
    		// --- Sort descending by match weight ------------------
    		Collections.sort(formulaMatchList, new Comparator<LinearFormulaMatch>() {
				@Override
				public int compare(LinearFormulaMatch lfm1, LinearFormulaMatch lfm2) {
					return lfm2.getMatchWeight().compareTo(lfm1.getMatchWeight());
				}
			});
    	} 
    	// --- Return calculation result ----------------------------
    	return formulaMatchList.get(0).getResult(variableValueTreeMap);
    }
    
    
    /**
     * Filters the specified formula list for the specified value of the variableID.
     *
     * @param formulaList the formula list
     * @param variableID the variable ID
     * @param value the value
     * @return the list
     */
    public static List<LinearFormula> filterFormulaList(final List<LinearFormula> formulaList, final String variableID, final Double value) {
    	
    	// --- Sort ascending by variableID and validTo ------------- 
    	Collections.sort(formulaList, new Comparator<LinearFormula>() {
			@Override
			public int compare(LinearFormula lf1, LinearFormula lf2) {
				LinearCoefficient lc1 = lf1.getCoefficient(variableID);
				LinearCoefficient lc2 = lf2.getCoefficient(variableID);
				Double validTo1 = lc1.getValidTo();
				Double validTo2 = lc2.getValidTo();
				return validTo1.compareTo(validTo2);
			}
    	});
    	
    	// --- Filter list of formulas ------------------------------
    	List<LinearFormula> formulaListFiltered = new ArrayList<>();
    	for (int i = 0; i < formulaList.size(); i++) {
    		LinearFormula formula = formulaList.get(i);
    		boolean isLastListEntry = (i==(formulaList.size()-1));
    		if (formula.providesRequiredVariableRange(variableID, value, isLastListEntry)==true) {
    			formulaListFiltered.add(formula);
    		}
    	}
    	return formulaListFiltered;
    }
    
    
    // ------------------------------------------------------------------------
    // --- From here handling of property change listener --------------------- 
    // ------------------------------------------------------------------------ 
    /**
     * Fires a property change event.
     *
     * @param propertyName the property name
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void firePropertyChangeEvent(String propertyName, Object oldValue, Object newValue) {
    	
    	PropertyChangeEvent pcEvent = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
    	for (PropertyChangeListener pcl : this.getPropertyChangeListener()) {
    		try {
    			pcl.propertyChange(pcEvent);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
    	}
    }
    /**
     * Returns the property change listener.
     * @return the property change listener
     */
	private List<PropertyChangeListener> getPropertyChangeListener() {
		if (propertyChangeListener==null) {
			propertyChangeListener = new ArrayList<>();
		}
		return propertyChangeListener;
	}
    /**
     * Adds the property change listener.
     * @param pcl the PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
    	if (this.getPropertyChangeListener().contains(pcl)==false) {
    		this.getPropertyChangeListener().add(pcl);
    	}
    }
    /**
     * Removes the property change listener.
     * @param pcl the PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
    	this.getPropertyChangeListener().remove(pcl);
    }

}
