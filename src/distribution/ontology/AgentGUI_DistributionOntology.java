// file: AgentGUI_DistributionOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package distribution.ontology;

import jade.content.onto.*;
import jade.content.schema.*;
import jade.util.leap.HashMap;
import jade.content.lang.Codec;
import jade.core.CaseInsensitiveString;

/** file: AgentGUI_DistributionOntology.java
 * @author ontology bean generator
 * @version 2010/08/3, 22:33:26
 */
public class AgentGUI_DistributionOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "AgentGUI-Distribution";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new AgentGUI_DistributionOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String SLAVEUNREGISTER="SlaveUnregister";
    public static final String STARTREMOTECONTAINER="StartRemoteContainer";
    public static final String CLIENTREGISTER_CLIENTPERFORMANCE="clientPerformance";
    public static final String CLIENTREGISTER_CLIENTTIME="clientTime";
    public static final String CLIENTREGISTER_CLIENTADDRESS="clientAddress";
    public static final String CLIENTREGISTER="ClientRegister";
    public static final String CLIENTUNREGISTER="ClientUnregister";
    public static final String SLAVEREGISTER_SLAVEPERFORMANCE="slavePerformance";
    public static final String SLAVEREGISTER_SLAVEADDRESS="slaveAddress";
    public static final String SLAVEREGISTER_SLAVETIME="slaveTime";
    public static final String SLAVEREGISTER="SlaveRegister";
    public static final String SLAVETRIGGER_TRIGGERTIME="triggerTime";
    public static final String SLAVETRIGGER="SlaveTrigger";
    public static final String PLATFORMTIME_TIMESTAMPASSTRING="TimeStampAsString";
    public static final String PLATFORMTIME="PlatformTime";
    public static final String PLATFORMPERFORMANCE_CPU_VENDOR="cpu_vendor";
    public static final String PLATFORMPERFORMANCE_CPU_NUMBEROF="cpu_numberOf";
    public static final String PLATFORMPERFORMANCE_CPU_MODEL="cpu_model";
    public static final String PLATFORMPERFORMANCE_MEMORY_TOTALMB="memory_totalMB";
    public static final String PLATFORMPERFORMANCE_CPU_SPEEDMHZ="cpu_speedMhz";
    public static final String PLATFORMPERFORMANCE="PlatformPerformance";
    public static final String PLATFORMADDRESS_URL="url";
    public static final String PLATFORMADDRESS_PORT="port";
    public static final String PLATFORMADDRESS_IP="ip";
    public static final String PLATFORMADDRESS_HTTP4MTP="http4mtp";
    public static final String PLATFORMADDRESS="PlatformAddress";

  /**
   * Constructor
  */
  private AgentGUI_DistributionOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema platformAddressSchema = new ConceptSchema(PLATFORMADDRESS);
    add(platformAddressSchema, distribution.ontology.PlatformAddress.class);
    ConceptSchema platformPerformanceSchema = new ConceptSchema(PLATFORMPERFORMANCE);
    add(platformPerformanceSchema, distribution.ontology.PlatformPerformance.class);
    ConceptSchema platformTimeSchema = new ConceptSchema(PLATFORMTIME);
    add(platformTimeSchema, distribution.ontology.PlatformTime.class);

    // adding AgentAction(s)
    AgentActionSchema slaveTriggerSchema = new AgentActionSchema(SLAVETRIGGER);
    add(slaveTriggerSchema, distribution.ontology.SlaveTrigger.class);
    AgentActionSchema slaveRegisterSchema = new AgentActionSchema(SLAVEREGISTER);
    add(slaveRegisterSchema, distribution.ontology.SlaveRegister.class);
    AgentActionSchema clientUnregisterSchema = new AgentActionSchema(CLIENTUNREGISTER);
    add(clientUnregisterSchema, distribution.ontology.ClientUnregister.class);
    AgentActionSchema clientRegisterSchema = new AgentActionSchema(CLIENTREGISTER);
    add(clientRegisterSchema, distribution.ontology.ClientRegister.class);
    AgentActionSchema startRemoteContainerSchema = new AgentActionSchema(STARTREMOTECONTAINER);
    add(startRemoteContainerSchema, distribution.ontology.StartRemoteContainer.class);
    AgentActionSchema slaveUnregisterSchema = new AgentActionSchema(SLAVEUNREGISTER);
    add(slaveUnregisterSchema, distribution.ontology.SlaveUnregister.class);

    // adding AID(s)

    // adding Predicate(s)


    // adding fields
    platformAddressSchema.add(PLATFORMADDRESS_HTTP4MTP, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformAddressSchema.add(PLATFORMADDRESS_IP, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformAddressSchema.add(PLATFORMADDRESS_PORT, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformAddressSchema.add(PLATFORMADDRESS_URL, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_CPU_SPEEDMHZ, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_MEMORY_TOTALMB, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_CPU_MODEL, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_CPU_NUMBEROF, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_CPU_VENDOR, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformTimeSchema.add(PLATFORMTIME_TIMESTAMPASSTRING, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    slaveTriggerSchema.add(SLAVETRIGGER_TRIGGERTIME, platformTimeSchema, ObjectSchema.OPTIONAL);
    slaveRegisterSchema.add(SLAVEREGISTER_SLAVETIME, platformTimeSchema, ObjectSchema.OPTIONAL);
    slaveRegisterSchema.add(SLAVEREGISTER_SLAVEADDRESS, platformAddressSchema, ObjectSchema.OPTIONAL);
    slaveRegisterSchema.add(SLAVEREGISTER_SLAVEPERFORMANCE, platformPerformanceSchema, ObjectSchema.OPTIONAL);
    clientRegisterSchema.add(CLIENTREGISTER_CLIENTADDRESS, platformAddressSchema, ObjectSchema.OPTIONAL);
    clientRegisterSchema.add(CLIENTREGISTER_CLIENTTIME, platformTimeSchema, ObjectSchema.OPTIONAL);
    clientRegisterSchema.add(CLIENTREGISTER_CLIENTPERFORMANCE, platformPerformanceSchema, ObjectSchema.OPTIONAL);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
