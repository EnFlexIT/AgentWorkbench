package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: GasAnalysis
* @author ontology bean generator
* @version 2012/08/14, 13:58:40
*/
public class GasAnalysis implements Concept {

//////////////////////////// User code
/**
 * <code>GasComponent_CH4 = 1</code>
 */
public static final int GasComponent_CH4 = 1;
/**
 * <code>GasComponent_N2 = 2</code>
 */
public static final int GasComponent_N2 = 2;
/**
 * <code>GasComponent_CO2 = 3</code>
 */
public static final int GasComponent_CO2 = 3;
/**
 * <code>GasComponent_C2H6 = 4</code>
 */
public static final int GasComponent_C2H6 = 4;
/**
 * <code>GasComponent_C3H8 = 5</code>
 */
public static final int GasComponent_C3H8 = 5;
/**
 * <code>GasComponent_n_C4H10 = 6</code>
 */
public static final int GasComponent_n_C4H10 = 6;
/**
 * <code>GasComponent_i_C4H10 = 7</code>
 */
public static final int GasComponent_i_C4H10 = 7;
/**
 * <code>GasComponent_n_C5H12 = 8</code>
 */
public static final int GasComponent_n_C5H12 = 8;
/**
 * <code>GasComponent_i_C5H12 = 9</code>
 */
public static final int GasComponent_i_C5H12 = 9;
/**
 * <code>GasComponent_n_C6H14 = 10</code>
 */
public static final int GasComponent_n_C6H14 = 10;
/**
 * <code>GasComponent_n_C7H16 = 11</code>
 */
public static final int GasComponent_n_C7H16 = 11;
/**
 * <code>GasComponent_n_C8H18 = 12</code>
 */
public static final int GasComponent_n_C8H18 = 12;
/**
 * <code>GasComponent_n_C9H20 = 13</code>
 */
public static final int GasComponent_n_C9H20 = 13;
/**
 * <code>GasComponent_n_C10H22 = 14</code>
 */
public static final int GasComponent_n_C10H22 = 14;
/**
 * <code>GasComponent_He = 15</code>
 */
public static final int GasComponent_He = 15;
/**
 * <code>GasComponent_Ar = 16</code>
 */
public static final int GasComponent_Ar = 16;
/**
 * <code>GasComponent_H2 = 17</code>
 */
public static final int GasComponent_H2 = 17;
/**
 * <code>GasComponent_O2 = 18</code>
 */
public static final int GasComponent_O2 = 18;
/**
 * <code>GasComponent_CO = 19</code>
 */
public static final int GasComponent_CO = 19;
/**
 * <code>GasComponent_H2O = 20</code>
 */
public static final int GasComponent_H2O = 20;
/**
 * <code>GasComponent_H2S = 21</code>
 */
public static final int GasComponent_H2S = 21;
/**
 * <code>GasComponent_neo_C5H12 = 22</code>
 */
public static final int GasComponent_neo_C5H12 = 22;
/**
 * <code>GasComponent_i_C6H14 = 23</code>
 */
public static final int GasComponent_i_C6H14 = 23;
/**
 * <code>GasComponent_z3_C6H14 = 24</code>
 */
public static final int GasComponent_z3_C6H14 = 24;
/**
 * <code>GasComponent_z2_2_C6H14 = 25</code>
 */
public static final int GasComponent_z2_2_C6H14 = 25;
/**
 * <code>GasComponent_z2_3_C6H14 = 26</code>
 */
public static final int GasComponent_z2_3_C6H14 = 26;
/**
 * <code>GasComponent_C2H4 = 27</code>
 */
public static final int GasComponent_C2H4 = 27;
/**
 * <code>GasComponent_C3H6 = 28</code>
 */
public static final int GasComponent_C3H6 = 28;
/**
 * <code>GasComponent_z1_C4H8 = 29</code>
 */
public static final int GasComponent_z1_C4H8 = 29;
/**
 * <code>GasComponent_cis_2_C4H8 = 30</code>
 */
public static final int GasComponent_cis_2_C4H8 = 30;
/**
 * <code>GasComponent_tra_2_C4H8 = 31</code>
 */
public static final int GasComponent_tra_2_C4H8 = 31;
/**
 * <code>GasComponent_i_C4H8 = 32</code>
 */
public static final int GasComponent_i_C4H8 = 32;
/**
 * <code>GasComponent_z1_C5H10 = 33</code>
 */
public static final int GasComponent_z1_C5H10 = 33;
/**
 * <code>GasComponent_C3H4 = 34</code>
 */
public static final int GasComponent_C3H4 = 34;
/**
 * <code>GasComponent_z1_2_C4H6 = 35</code>
 */
public static final int GasComponent_z1_2_C4H6 = 35;
/**
 * <code>GasComponent_z1_3_C4H6 = 36</code>
 */
public static final int GasComponent_z1_3_C4H6 = 36;
/**
 * <code>GasComponent_C2H2 = 37</code>
 */
public static final int GasComponent_C2H2 = 37;
/**
 * <code>GasComponent_C5H10 = 38</code>
 */
public static final int GasComponent_C5H10 = 38;
/**
 * <code>GasComponent_z_5_C6H12 = 39</code>
 */
public static final int GasComponent_z_5_C6H12 = 39;
/**
 * <code>GasComponent_C7H14 = 40</code>
 */
public static final int GasComponent_C7H14 = 40;
/**
 * <code>GasComponent_c_C6H12 = 41</code>
 */
public static final int GasComponent_c_C6H12 = 41;
/**
 * <code>GasComponent_z6_C7H14 = 42</code>
 */
public static final int GasComponent_z6_C7H14 = 42;
/**
 * <code>GasComponent_C8H16 = 43</code>
 */
public static final int GasComponent_C8H16 = 43;
/**
 * <code>GasComponent_C6H6 = 44</code>
 */
public static final int GasComponent_C6H6 = 44;
/**
 * <code>GasComponent_C7H8 = 45</code>
 */
public static final int GasComponent_C7H8 = 45;
/**
 * <code>GasComponent_C8H10 = 46</code>
 */
public static final int GasComponent_C8H10 = 46;
/**
 * <code>GasComponent_o_C8H10 = 47</code>
 */
public static final int GasComponent_o_C8H10 = 47;
/**
 * <code>GasComponent_CH3OH = 48</code>
 */
public static final int GasComponent_CH3OH = 48;
/**
 * <code>GasComponent_CH3SH = 49</code>
 */
public static final int GasComponent_CH3SH = 49;
/**
 * <code>GasComponent_NH3 = 50</code>
 */
public static final int GasComponent_NH3 = 50;
/**
 * <code>GasComponent_HCN = 51</code>
 */
public static final int GasComponent_HCN = 51;
/**
 * <code>GasComponent_COS = 52</code>
 */
public static final int GasComponent_COS = 52;
/**
 * <code>GasComponent_CS2 = 53</code>
 */
public static final int GasComponent_CS2 = 53;
/**
 * <code>GasComponent_Ne = 54</code>
 */
public static final int GasComponent_Ne = 54;
/**
 * <code>GasComponent_SO2 = 55</code>
 */
public static final int GasComponent_SO2 = 55;
/**
 * <code>GasComponent_N2O = 56</code>
 */
public static final int GasComponent_N2O = 56;
/**
 * <code>GasComponent_Kr = 57</code>
 */
public static final int GasComponent_Kr = 57;
/**
 * <code>GasComponent_Xe = 58</code>
 */
public static final int GasComponent_Xe = 58;
   /**
* Protege name: gasComponents
   */
   private List gasComponents = new ArrayList();
   public void addGasComponents(Float elem) { 
     List oldList = this.gasComponents;
     gasComponents.add(elem);
   }
   public boolean removeGasComponents(Float elem) {
     List oldList = this.gasComponents;
     boolean result = gasComponents.remove(elem);
     return result;
   }
   public void clearAllGasComponents() {
     List oldList = this.gasComponents;
     gasComponents.clear();
   }
   public Iterator getAllGasComponents() {return gasComponents.iterator(); }
   public List getGasComponents() {return gasComponents; }
   public void setGasComponents(List l) {gasComponents = l; }

}
