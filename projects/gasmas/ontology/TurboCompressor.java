package gasmas.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TurboCompressor
* @author ontology bean generator
* @version 2013/03/7, 00:03:01
*/
public class TurboCompressor extends CompStatCompressor{ 

   /**
* Protege name: surgeline_coeff
   */
   private Calc3Parameter surgeline_coeff;
   public void setSurgeline_coeff(Calc3Parameter value) { 
    this.surgeline_coeff=value;
   }
   public Calc3Parameter getSurgeline_coeff() {
     return this.surgeline_coeff;
   }

   /**
* Protege name: chokeline_coeff
   */
   private Calc3Parameter chokeline_coeff;
   public void setChokeline_coeff(Calc3Parameter value) { 
    this.chokeline_coeff=value;
   }
   public Calc3Parameter getChokeline_coeff() {
     return this.chokeline_coeff;
   }

   /**
* Protege name: characteristicDiagramMeasurements
   */
   private List characteristicDiagramMeasurements = new ArrayList();
   public void addCharacteristicDiagramMeasurements(CompStatAdiabaticEfficiency elem) { 
     List oldList = this.characteristicDiagramMeasurements;
     characteristicDiagramMeasurements.add(elem);
   }
   public boolean removeCharacteristicDiagramMeasurements(CompStatAdiabaticEfficiency elem) {
     List oldList = this.characteristicDiagramMeasurements;
     boolean result = characteristicDiagramMeasurements.remove(elem);
     return result;
   }
   public void clearAllCharacteristicDiagramMeasurements() {
     List oldList = this.characteristicDiagramMeasurements;
     characteristicDiagramMeasurements.clear();
   }
   public Iterator getAllCharacteristicDiagramMeasurements() {return characteristicDiagramMeasurements.iterator(); }
   public List getCharacteristicDiagramMeasurements() {return characteristicDiagramMeasurements; }
   public void setCharacteristicDiagramMeasurements(List l) {characteristicDiagramMeasurements = l; }

   /**
* Protege name: settlelineMeasurements
   */
   private List settlelineMeasurements = new ArrayList();
   public void addSettlelineMeasurements(CompStatTcMeasurement elem) { 
     List oldList = this.settlelineMeasurements;
     settlelineMeasurements.add(elem);
   }
   public boolean removeSettlelineMeasurements(CompStatTcMeasurement elem) {
     List oldList = this.settlelineMeasurements;
     boolean result = settlelineMeasurements.remove(elem);
     return result;
   }
   public void clearAllSettlelineMeasurements() {
     List oldList = this.settlelineMeasurements;
     settlelineMeasurements.clear();
   }
   public Iterator getAllSettlelineMeasurements() {return settlelineMeasurements.iterator(); }
   public List getSettlelineMeasurements() {return settlelineMeasurements; }
   public void setSettlelineMeasurements(List l) {settlelineMeasurements = l; }

   /**
* Protege name: efficiencyOfChokeline
   */
   private float efficiencyOfChokeline;
   public void setEfficiencyOfChokeline(float value) { 
    this.efficiencyOfChokeline=value;
   }
   public float getEfficiencyOfChokeline() {
     return this.efficiencyOfChokeline;
   }

   /**
* Protege name: n_isoline_coeff
   */
   private Calc9Parameter n_isoline_coeff;
   public void setN_isoline_coeff(Calc9Parameter value) { 
    this.n_isoline_coeff=value;
   }
   public Calc9Parameter getN_isoline_coeff() {
     return this.n_isoline_coeff;
   }

   /**
* Protege name: eta_ad_isoline_coeff
   */
   private Calc9Parameter eta_ad_isoline_coeff;
   public void setEta_ad_isoline_coeff(Calc9Parameter value) { 
    this.eta_ad_isoline_coeff=value;
   }
   public Calc9Parameter getEta_ad_isoline_coeff() {
     return this.eta_ad_isoline_coeff;
   }

}
