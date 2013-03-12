package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: CompStatStage
* @author ontology bean generator
* @version 2013/03/10, 21:16:57
*/
public class CompStatStage implements Concept {

   /**
* Protege name: compressor
   */
   private List compressor = new ArrayList();
   public void addCompressor(CompStatCompressorWithSpeed elem) { 
     List oldList = this.compressor;
     compressor.add(elem);
   }
   public boolean removeCompressor(CompStatCompressorWithSpeed elem) {
     List oldList = this.compressor;
     boolean result = compressor.remove(elem);
     return result;
   }
   public void clearAllCompressor() {
     List oldList = this.compressor;
     compressor.clear();
   }
   public Iterator getAllCompressor() {return compressor.iterator(); }
   public List getCompressor() {return compressor; }
   public void setCompressor(List l) {compressor = l; }

   /**
* Protege name: stageNo
   */
   private int stageNo;
   public void setStageNo(int value) { 
    this.stageNo=value;
   }
   public int getStageNo() {
     return this.stageNo;
   }

   /**
* Protege name: noOfParallelUnits
   */
   private int noOfParallelUnits;
   public void setNoOfParallelUnits(int value) { 
    this.noOfParallelUnits=value;
   }
   public int getNoOfParallelUnits() {
     return this.noOfParallelUnits;
   }

}
