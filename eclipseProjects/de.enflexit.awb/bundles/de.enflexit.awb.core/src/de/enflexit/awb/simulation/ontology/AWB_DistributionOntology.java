// file: AWB_DistributionOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package de.enflexit.awb.simulation.ontology;

import jade.content.onto.*;
import jade.content.schema.*;

/** file: AWB_DistributionOntology.java
 * @author ontology bean generator
 * @version 2024/02/9, 15:58:57
 */
public class AWB_DistributionOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "AWB-Distribution";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new AWB_DistributionOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String CLIENTTRIGGER_CLIENTBENCHMARKVALUE="clientBenchmarkValue";
    public static final String CLIENTTRIGGER_TRIGGERTIME="triggerTime";
    public static final String CLIENTTRIGGER_CLIENTLOAD="clientLoad";
    public static final String CLIENTTRIGGER="ClientTrigger";
    public static final String CLIENTUNREGISTER="ClientUnregister";
    public static final String CLIENTAVAILABLEMACHINESREQUEST="ClientAvailableMachinesRequest";
    public static final String SLAVETRIGGER_SLAVELOAD="slaveLoad";
    public static final String SLAVETRIGGER_TRIGGERTIME="triggerTime";
    public static final String SLAVETRIGGER_SLAVEBENCHMARKVALUE="slaveBenchmarkValue";
    public static final String SLAVETRIGGER="SlaveTrigger";
    public static final String SLAVEREGISTER_SLAVEVERSION="slaveVersion";
    public static final String SLAVEREGISTER_SLAVETIME="slaveTime";
    public static final String SLAVEREGISTER_SLAVEPERFORMANCE="slavePerformance";
    public static final String SLAVEREGISTER_SLAVEADDRESS="slaveAddress";
    public static final String SLAVEREGISTER_SLAVEOS="slaveOS";
    public static final String SLAVEREGISTER="SlaveRegister";
    public static final String CLIENTAVAILABLEMACHINESREPLY_AVAILABLEMACHINES="availableMachines";
    public static final String CLIENTAVAILABLEMACHINESREPLY="ClientAvailableMachinesReply";
    public static final String SHOWMONITORGUI="ShowMonitorGUI";
    public static final String CLIENTREMOTECONTAINERREPLY_REMOTEPERFORMANCE="remotePerformance";
    public static final String CLIENTREMOTECONTAINERREPLY_REMOTEADDRESS="remoteAddress";
    public static final String CLIENTREMOTECONTAINERREPLY_REMOTEBENCHMARKRESULT="remoteBenchmarkResult";
    public static final String CLIENTREMOTECONTAINERREPLY_REMOTEPID="remotePID";
    public static final String CLIENTREMOTECONTAINERREPLY_REMOTECONTAINERNAME="remoteContainerName";
    public static final String CLIENTREMOTECONTAINERREPLY_REMOTEOS="remoteOS";
    public static final String CLIENTREMOTECONTAINERREPLY_REMOTEVERSION="remoteVersion";
    public static final String CLIENTREMOTECONTAINERREPLY="ClientRemoteContainerReply";
    public static final String REGISTERRECEIPT="RegisterReceipt";
    public static final String SHOWTHREADGUI="ShowThreadGUI";
    public static final String CLIENTREGISTER_CLIENTADDRESS="clientAddress";
    public static final String CLIENTREGISTER_CLIENTVERSION="clientVersion";
    public static final String CLIENTREGISTER_CLIENTTIME="clientTime";
    public static final String CLIENTREGISTER_CLIENTPERFORMANCE="clientPerformance";
    public static final String CLIENTREGISTER_CLIENTOS="clientOS";
    public static final String CLIENTREGISTER="ClientRegister";
    public static final String CLIENTREMOTECONTAINERREQUEST_REMOTECONFIG="RemoteConfig";
    public static final String CLIENTREMOTECONTAINERREQUEST="ClientRemoteContainerRequest";
    public static final String MASTERUPDATENOTE="MasterUpdateNote";
    public static final String SLAVEUNREGISTER="SlaveUnregister";
    public static final String BENCHMARKRESULT_BENCHMARKVALUE="benchmarkValue";
    public static final String BENCHMARKRESULT="BenchmarkResult";
    public static final String VERSION_MAJORREVISION="majorRevision";
    public static final String VERSION_MICROREVISION="microRevision";
    public static final String VERSION_QUALIFIER="qualifier";
    public static final String VERSION_MINORREVISION="minorRevision";
    public static final String VERSION="Version";
    public static final String PLATFORMTIME_TIMESTAMPASSTRING="TimeStampAsString";
    public static final String PLATFORMTIME="PlatformTime";
    public static final String PLATFORMADDRESS_HTTP4MTP="http4mtp";
    public static final String PLATFORMADDRESS_URL="url";
    public static final String PLATFORMADDRESS_PORT="port";
    public static final String PLATFORMADDRESS_IP="ip";
    public static final String PLATFORMADDRESS="PlatformAddress";
    public static final String OSINFO_OS_NAME="os_name";
    public static final String OSINFO_OS_VERSION="os_version";
    public static final String OSINFO_OS_ARCH="os_arch";
    public static final String OSINFO="OSInfo";
    public static final String REMOTECONTAINERCONFIG_JVMMEMALLOCINITIAL="jvmMemAllocInitial";
    public static final String REMOTECONTAINERCONFIG_JADEHOST="jadeHost";
    public static final String REMOTECONTAINERCONFIG_HTTPDOWNLOADFILES="httpDownloadFiles";
    public static final String REMOTECONTAINERCONFIG_JADESERVICES="jadeServices";
    public static final String REMOTECONTAINERCONFIG_JADEPORT="jadePort";
    public static final String REMOTECONTAINERCONFIG_HOSTEXCLUDEIP="hostExcludeIP";
    public static final String REMOTECONTAINERCONFIG_JADESHOWGUI="jadeShowGUI";
    public static final String REMOTECONTAINERCONFIG_PREVENTUSAGEOFUSEDCOMPUTER="preventUsageOfUsedComputer";
    public static final String REMOTECONTAINERCONFIG_JADEISREMOTECONTAINER="jadeIsRemoteContainer";
    public static final String REMOTECONTAINERCONFIG_JVMMEMALLOCMAXIMUM="jvmMemAllocMaximum";
    public static final String REMOTECONTAINERCONFIG_JADECONTAINERNAME="jadeContainerName";
    public static final String REMOTECONTAINERCONFIG="RemoteContainerConfig";
    public static final String PLATFORMPERFORMANCE_CPU_NUMBEROFLOGICALCORES="cpu_numberOfLogicalCores";
    public static final String PLATFORMPERFORMANCE_CPU_PROCESSORNAME="cpu_processorName";
    public static final String PLATFORMPERFORMANCE_MEMORY_TOTALMB="memory_totalMB";
    public static final String PLATFORMPERFORMANCE_CPU_SPEEDMHZ="cpu_speedMhz";
    public static final String PLATFORMPERFORMANCE_CPU_NUMBEROFPHYSICALCORES="cpu_numberOfPhysicalCores";
    public static final String PLATFORMPERFORMANCE="PlatformPerformance";
    public static final String MACHINEDESCRIPTION_ISAVAILABLE="isAvailable";
    public static final String MACHINEDESCRIPTION_ISTHRESHOLDEXCEEDED="isThresholdExceeded";
    public static final String MACHINEDESCRIPTION_BENCHMARKRESULT="benchmarkResult";
    public static final String MACHINEDESCRIPTION_PLATFORMNAME="platformName";
    public static final String MACHINEDESCRIPTION_REMOTEOS="remoteOS";
    public static final String MACHINEDESCRIPTION_VERSION="version";
    public static final String MACHINEDESCRIPTION_PERFORMANCE="performance";
    public static final String MACHINEDESCRIPTION_PLATFORMADDRESS="platformAddress";
    public static final String MACHINEDESCRIPTION_CONTACTAGENT="contactAgent";
    public static final String MACHINEDESCRIPTION="MachineDescription";
    public static final String PLATFORMLOAD_LOADCPU="loadCPU";
    public static final String PLATFORMLOAD_LOADEXCEEDED="loadExceeded";
    public static final String PLATFORMLOAD_LOADNOTHREADS="loadNoThreads";
    public static final String PLATFORMLOAD_AVAILABLE="available";
    public static final String PLATFORMLOAD_LOADMEMORYSYSTEM="loadMemorySystem";
    public static final String PLATFORMLOAD_LOADMEMORYJVM="loadMemoryJVM";
    public static final String PLATFORMLOAD="PlatformLoad";

  /**
   * Constructor
  */
  private AWB_DistributionOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema platformLoadSchema = new ConceptSchema(PLATFORMLOAD);
    add(platformLoadSchema, de.enflexit.awb.simulation.ontology.PlatformLoad.class);
    ConceptSchema machineDescriptionSchema = new ConceptSchema(MACHINEDESCRIPTION);
    add(machineDescriptionSchema, de.enflexit.awb.simulation.ontology.MachineDescription.class);
    ConceptSchema platformPerformanceSchema = new ConceptSchema(PLATFORMPERFORMANCE);
    add(platformPerformanceSchema, de.enflexit.awb.simulation.ontology.PlatformPerformance.class);
    ConceptSchema remoteContainerConfigSchema = new ConceptSchema(REMOTECONTAINERCONFIG);
    add(remoteContainerConfigSchema, de.enflexit.awb.simulation.ontology.RemoteContainerConfig.class);
    ConceptSchema osInfoSchema = new ConceptSchema(OSINFO);
    add(osInfoSchema, de.enflexit.awb.simulation.ontology.OSInfo.class);
    ConceptSchema platformAddressSchema = new ConceptSchema(PLATFORMADDRESS);
    add(platformAddressSchema, de.enflexit.awb.simulation.ontology.PlatformAddress.class);
    ConceptSchema platformTimeSchema = new ConceptSchema(PLATFORMTIME);
    add(platformTimeSchema, de.enflexit.awb.simulation.ontology.PlatformTime.class);
    ConceptSchema versionSchema = new ConceptSchema(VERSION);
    add(versionSchema, de.enflexit.awb.simulation.ontology.Version.class);
    ConceptSchema benchmarkResultSchema = new ConceptSchema(BENCHMARKRESULT);
    add(benchmarkResultSchema, de.enflexit.awb.simulation.ontology.BenchmarkResult.class);

    // adding AgentAction(s)
    AgentActionSchema slaveUnregisterSchema = new AgentActionSchema(SLAVEUNREGISTER);
    add(slaveUnregisterSchema, de.enflexit.awb.simulation.ontology.SlaveUnregister.class);
    AgentActionSchema masterUpdateNoteSchema = new AgentActionSchema(MASTERUPDATENOTE);
    add(masterUpdateNoteSchema, de.enflexit.awb.simulation.ontology.MasterUpdateNote.class);
    AgentActionSchema clientRemoteContainerRequestSchema = new AgentActionSchema(CLIENTREMOTECONTAINERREQUEST);
    add(clientRemoteContainerRequestSchema, de.enflexit.awb.simulation.ontology.ClientRemoteContainerRequest.class);
    AgentActionSchema clientRegisterSchema = new AgentActionSchema(CLIENTREGISTER);
    add(clientRegisterSchema, de.enflexit.awb.simulation.ontology.ClientRegister.class);
    AgentActionSchema showThreadGUISchema = new AgentActionSchema(SHOWTHREADGUI);
    add(showThreadGUISchema, de.enflexit.awb.simulation.ontology.ShowThreadGUI.class);
    AgentActionSchema registerReceiptSchema = new AgentActionSchema(REGISTERRECEIPT);
    add(registerReceiptSchema, de.enflexit.awb.simulation.ontology.RegisterReceipt.class);
    AgentActionSchema clientRemoteContainerReplySchema = new AgentActionSchema(CLIENTREMOTECONTAINERREPLY);
    add(clientRemoteContainerReplySchema, de.enflexit.awb.simulation.ontology.ClientRemoteContainerReply.class);
    AgentActionSchema showMonitorGUISchema = new AgentActionSchema(SHOWMONITORGUI);
    add(showMonitorGUISchema, de.enflexit.awb.simulation.ontology.ShowMonitorGUI.class);
    AgentActionSchema clientAvailableMachinesReplySchema = new AgentActionSchema(CLIENTAVAILABLEMACHINESREPLY);
    add(clientAvailableMachinesReplySchema, de.enflexit.awb.simulation.ontology.ClientAvailableMachinesReply.class);
    AgentActionSchema slaveRegisterSchema = new AgentActionSchema(SLAVEREGISTER);
    add(slaveRegisterSchema, de.enflexit.awb.simulation.ontology.SlaveRegister.class);
    AgentActionSchema slaveTriggerSchema = new AgentActionSchema(SLAVETRIGGER);
    add(slaveTriggerSchema, de.enflexit.awb.simulation.ontology.SlaveTrigger.class);
    AgentActionSchema clientAvailableMachinesRequestSchema = new AgentActionSchema(CLIENTAVAILABLEMACHINESREQUEST);
    add(clientAvailableMachinesRequestSchema, de.enflexit.awb.simulation.ontology.ClientAvailableMachinesRequest.class);
    AgentActionSchema clientUnregisterSchema = new AgentActionSchema(CLIENTUNREGISTER);
    add(clientUnregisterSchema, de.enflexit.awb.simulation.ontology.ClientUnregister.class);
    AgentActionSchema clientTriggerSchema = new AgentActionSchema(CLIENTTRIGGER);
    add(clientTriggerSchema, de.enflexit.awb.simulation.ontology.ClientTrigger.class);

    // adding AID(s)

    // adding Predicate(s)


    // adding fields
    platformLoadSchema.add(PLATFORMLOAD_LOADMEMORYJVM, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    platformLoadSchema.add(PLATFORMLOAD_LOADMEMORYSYSTEM, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    platformLoadSchema.add(PLATFORMLOAD_AVAILABLE, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    platformLoadSchema.add(PLATFORMLOAD_LOADNOTHREADS, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformLoadSchema.add(PLATFORMLOAD_LOADEXCEEDED, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformLoadSchema.add(PLATFORMLOAD_LOADCPU, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_CONTACTAGENT, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_PLATFORMADDRESS, platformAddressSchema, ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_PERFORMANCE, platformPerformanceSchema, ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_VERSION, versionSchema, ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_REMOTEOS, osInfoSchema, ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_PLATFORMNAME, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_BENCHMARKRESULT, benchmarkResultSchema, ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_ISTHRESHOLDEXCEEDED, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    machineDescriptionSchema.add(MACHINEDESCRIPTION_ISAVAILABLE, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_CPU_NUMBEROFPHYSICALCORES, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_CPU_SPEEDMHZ, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_MEMORY_TOTALMB, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_CPU_PROCESSORNAME, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformPerformanceSchema.add(PLATFORMPERFORMANCE_CPU_NUMBEROFLOGICALCORES, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_JADECONTAINERNAME, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_JVMMEMALLOCMAXIMUM, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_JADEISREMOTECONTAINER, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_PREVENTUSAGEOFUSEDCOMPUTER, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_JADESHOWGUI, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_HOSTEXCLUDEIP, (TermSchema)getSchema(BasicOntology.STRING), 0, ObjectSchema.UNLIMITED);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_JADEPORT, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_JADESERVICES, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_HTTPDOWNLOADFILES, (TermSchema)getSchema(BasicOntology.STRING), 0, ObjectSchema.UNLIMITED);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_JADEHOST, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    remoteContainerConfigSchema.add(REMOTECONTAINERCONFIG_JVMMEMALLOCINITIAL, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    osInfoSchema.add(OSINFO_OS_ARCH, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    osInfoSchema.add(OSINFO_OS_VERSION, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    osInfoSchema.add(OSINFO_OS_NAME, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformAddressSchema.add(PLATFORMADDRESS_IP, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformAddressSchema.add(PLATFORMADDRESS_PORT, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    platformAddressSchema.add(PLATFORMADDRESS_URL, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformAddressSchema.add(PLATFORMADDRESS_HTTP4MTP, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    platformTimeSchema.add(PLATFORMTIME_TIMESTAMPASSTRING, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    versionSchema.add(VERSION_MINORREVISION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    versionSchema.add(VERSION_QUALIFIER, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    versionSchema.add(VERSION_MICROREVISION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    versionSchema.add(VERSION_MAJORREVISION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    benchmarkResultSchema.add(BENCHMARKRESULT_BENCHMARKVALUE, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    clientRemoteContainerRequestSchema.add(CLIENTREMOTECONTAINERREQUEST_REMOTECONFIG, remoteContainerConfigSchema, ObjectSchema.OPTIONAL);
    clientRegisterSchema.add(CLIENTREGISTER_CLIENTOS, osInfoSchema, ObjectSchema.OPTIONAL);
    clientRegisterSchema.add(CLIENTREGISTER_CLIENTPERFORMANCE, platformPerformanceSchema, ObjectSchema.OPTIONAL);
    clientRegisterSchema.add(CLIENTREGISTER_CLIENTTIME, platformTimeSchema, ObjectSchema.OPTIONAL);
    clientRegisterSchema.add(CLIENTREGISTER_CLIENTVERSION, versionSchema, ObjectSchema.OPTIONAL);
    clientRegisterSchema.add(CLIENTREGISTER_CLIENTADDRESS, platformAddressSchema, ObjectSchema.OPTIONAL);
    clientRemoteContainerReplySchema.add(CLIENTREMOTECONTAINERREPLY_REMOTEVERSION, versionSchema, ObjectSchema.OPTIONAL);
    clientRemoteContainerReplySchema.add(CLIENTREMOTECONTAINERREPLY_REMOTEOS, osInfoSchema, ObjectSchema.OPTIONAL);
    clientRemoteContainerReplySchema.add(CLIENTREMOTECONTAINERREPLY_REMOTECONTAINERNAME, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    clientRemoteContainerReplySchema.add(CLIENTREMOTECONTAINERREPLY_REMOTEPID, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    clientRemoteContainerReplySchema.add(CLIENTREMOTECONTAINERREPLY_REMOTEBENCHMARKRESULT, benchmarkResultSchema, ObjectSchema.OPTIONAL);
    clientRemoteContainerReplySchema.add(CLIENTREMOTECONTAINERREPLY_REMOTEADDRESS, platformAddressSchema, ObjectSchema.OPTIONAL);
    clientRemoteContainerReplySchema.add(CLIENTREMOTECONTAINERREPLY_REMOTEPERFORMANCE, platformPerformanceSchema, ObjectSchema.OPTIONAL);
    clientAvailableMachinesReplySchema.add(CLIENTAVAILABLEMACHINESREPLY_AVAILABLEMACHINES, machineDescriptionSchema, 0, ObjectSchema.UNLIMITED);
    slaveRegisterSchema.add(SLAVEREGISTER_SLAVEOS, osInfoSchema, ObjectSchema.OPTIONAL);
    slaveRegisterSchema.add(SLAVEREGISTER_SLAVEADDRESS, platformAddressSchema, ObjectSchema.OPTIONAL);
    slaveRegisterSchema.add(SLAVEREGISTER_SLAVEPERFORMANCE, platformPerformanceSchema, ObjectSchema.OPTIONAL);
    slaveRegisterSchema.add(SLAVEREGISTER_SLAVETIME, platformTimeSchema, ObjectSchema.OPTIONAL);
    slaveRegisterSchema.add(SLAVEREGISTER_SLAVEVERSION, versionSchema, ObjectSchema.OPTIONAL);
    slaveTriggerSchema.add(SLAVETRIGGER_SLAVEBENCHMARKVALUE, benchmarkResultSchema, ObjectSchema.OPTIONAL);
    slaveTriggerSchema.add(SLAVETRIGGER_TRIGGERTIME, platformTimeSchema, ObjectSchema.OPTIONAL);
    slaveTriggerSchema.add(SLAVETRIGGER_SLAVELOAD, platformLoadSchema, ObjectSchema.OPTIONAL);
    clientTriggerSchema.add(CLIENTTRIGGER_CLIENTLOAD, platformLoadSchema, ObjectSchema.OPTIONAL);
    clientTriggerSchema.add(CLIENTTRIGGER_TRIGGERTIME, platformTimeSchema, ObjectSchema.OPTIONAL);
    clientTriggerSchema.add(CLIENTTRIGGER_CLIENTBENCHMARKVALUE, benchmarkResultSchema, ObjectSchema.OPTIONAL);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
