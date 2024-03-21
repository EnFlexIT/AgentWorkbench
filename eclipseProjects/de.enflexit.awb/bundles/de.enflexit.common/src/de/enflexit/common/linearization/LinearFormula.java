//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.12.05 at 09:28:32 AM CET 
//


package de.enflexit.common.linearization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 			Describes all parameters for a single linear formula.
 * 			
 * 
 * <p>Java class for LinearFormula complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LinearFormula"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CoefficientList" type="{http://www.dawis.wiwi.uni-due.de/EnergyOptionModel}LinearCoefficient" maxOccurs="unbounded"/&gt;
 *         &lt;element name="AxisIntercept" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinearFormula", propOrder = {
    "coefficientList",
    "axisIntercept"
})
public class LinearFormula
    implements Serializable
{

    private final static long serialVersionUID = 201404191434L;
    @XmlElement(name = "CoefficientList", required = true)
    protected List<LinearCoefficient> coefficientList;
    @XmlElement(name = "AxisIntercept")
    protected double axisIntercept;

    /**
     * Gets the value of the coefficientList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coefficientList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoefficientList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinearCoefficient }
     * 
     * 
     */
    public List<LinearCoefficient> getCoefficientList() {
        if (coefficientList == null) {
            coefficientList = new ArrayList<LinearCoefficient>();
        }
        return this.coefficientList;
    }

    /**
     * Gets the value of the axisIntercept property.
     * 
     */
    public double getAxisIntercept() {
        return axisIntercept;
    }

    /**
     * Sets the value of the axisIntercept property.
     * 
     */
    public void setAxisIntercept(double value) {
        this.axisIntercept = value;
    }

    
    // ------------------------------------------------------------------------
    // --- From here customized code ------------------------------------------ 
    // ------------------------------------------------------------------------  
    /**
     * Returns a unique formula key to be used for a quick comparison.
     * @return the formula key
     */
    public double getFormulaKey() {
    	
    	double formularKey = this.getAxisIntercept();
    	
    	// --- Get coefficient list sorted by variableID ------------  
    	List<LinearCoefficient> coeffList = this.getCoefficientList();
    	Collections.sort(coeffList, LinearCoefficient.getComparatorVariableID());
    	// --- Add-up coefficient values ----------------------------
    	for (LinearCoefficient lineCoeff : coeffList) {
    		for (char character : lineCoeff.getVariableID().toCharArray()) {
    			formularKey += (int)character;
    		}
    		formularKey += lineCoeff.getValue();
    		formularKey += lineCoeff.getValidFrom();
    		formularKey += lineCoeff.getValidTo();
    	}
    	return formularKey;
    }
    /**
     * Returns a single key value for the ranges defined.
     * @return the range key
     */
    public double getRangeKey() {
    	
    	double rangeKey = 0;
    	
    	// --- Get coefficient list sorted by variableID ------------  
    	List<LinearCoefficient> coeffList = this.getCoefficientList();
    	Collections.sort(coeffList, LinearCoefficient.getComparatorVariableID());
    	// --- Add-up coefficient values ----------------------------
    	for (LinearCoefficient lineCoeff : coeffList) {
    		for (char character : lineCoeff.getVariableID().toCharArray()) {
    			rangeKey += (int)character;
    		}
    		rangeKey += lineCoeff.getValidFrom();
    		rangeKey += lineCoeff.getValidTo();
    	}
    	return rangeKey;
    }
    
	/**
	 * Returns the coefficient that matches the specified variable ID.
	 *
	 * @param variableID the variable ID
	 * @return the coefficient or <code>null</code>
	 */
	public LinearCoefficient getCoefficient(String variableID) {
		
		for (LinearCoefficient linCoeff : this.getCoefficientList()) {
			if (linCoeff.getVariableID().equals(variableID)==true) {
				return linCoeff;
			}
		}
		return null;
	}

	/**
	 * Checks if the current formula provides the required variable range for the specified variable.
	 *
	 * @param variableID the variable ID
	 * @param value the value
	 * @param isEnabledSmallerEqualComparison the indicator to enable an smaller equal comparison
	 * @return true, if the formula is applicable for the specified variable value
	 */
	public boolean providesRequiredVariableRange(String variableID, Double value, boolean isEnabledSmallerEqualComparison) {
		
		for (LinearCoefficient linCoeff : this.getCoefficientList()) {
			if (linCoeff.getVariableID().equals(variableID)==true) {
				return linCoeff.providesRequiredVariableRange(value, isEnabledSmallerEqualComparison);
			}
		}
		return false;
	}

	/**
	 * Returns a {@link LinearFormulaMatch} with a match weight, derived from the current formula.
	 *
	 * @param variableValueTreeMap the variable value tree map
	 * @param validToMaxTreeMap the valid to max TreeMap
	 * @return the linear formula match
	 */
	public LinearFormulaMatch getLinearFormulaMatch(TreeMap<String, Double> variableValueTreeMap, TreeMap<String, Double> validToMaxTreeMap) {
		
		// --- Define return type -----------------------------------
		LinearFormulaMatch formulaMatch = new LinearFormulaMatch(this);
		
		// --- Any variables defined? -------------------------------
		if (this.getCoefficientList().size()==0) {
			formulaMatch.setMatchWeight(LinearFormulaMatch.MATCH);
			return formulaMatch;
		}
		
		// --- Apply variable values to determine the match weight --
		if (variableValueTreeMap!=null) {
			// --- Check each variable value ------------------------ 
    		for (String variableID : variableValueTreeMap.keySet()) {
    			
    			// --- Get value
    			Double value = variableValueTreeMap.get(variableID);
    			Double valueMax = validToMaxTreeMap.get(variableID);
    			boolean isEnabledSmallerEqualComparison = (value>=valueMax);
    			
    			LinearCoefficient linCoeff = this.getCoefficient(variableID);
    			int matchWeight = linCoeff.getMatchWeight(value, isEnabledSmallerEqualComparison);
    			if (matchWeight==LinearFormulaMatch.NO_MATCH) {
    				return null;
    			} else {
    				formulaMatch.addMatchWeight(matchWeight);
    			}
    		}
		}
		return formulaMatch;
	}
	
	
	/**
     * Returns the calculated linearization result value for the specified variable values.
     *
     * @param variableValueTreeMap the variable values as TreeMap
     * @return the calculated result value
     */
	public Double getResult(TreeMap<String, Double> variableValueTreeMap) {
		
		double sum = 0;
		
		int noOfExpectedVariableValues = this.getCoefficientList().size();
		int noOfProvidedVariableValues = variableValueTreeMap==null ? 0 : variableValueTreeMap.size();
		if (noOfProvidedVariableValues < noOfExpectedVariableValues) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The number of provided parameter does not match the number of expected parameter (" + noOfProvidedVariableValues + " < " + noOfExpectedVariableValues + ")");
		}
		
		if (variableValueTreeMap!=null) {
			for (String variableID : variableValueTreeMap.keySet()) {
				Double value = variableValueTreeMap.get(variableID);
				LinearCoefficient lc = this.getCoefficient(variableID);
				Double termResult = lc.getTermResult(value);
				if (termResult!=null) {
					sum += termResult;
				}
			}
		}
		sum += this.getAxisIntercept();
		return sum;
	}

}