// file: GasgridOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package agentgui.gasgridEnvironment.ontology;

import jade.content.onto.*;
import jade.content.schema.*;
import jade.util.leap.HashMap;
import jade.content.lang.Codec;
import jade.core.CaseInsensitiveString;

/** file: GasgridOntology.java
 * @author ontology bean generator
 * @version 2010/12/30, 20:54:11
 */
public class GasgridOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "Gasgrid";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new GasgridOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String COMPRESSOR="Compressor";
    public static final String PIPE="Pipe";
    public static final String BRANCH="Branch";
    public static final String GRIDCOMPONENT_ID="id";
    public static final String GRIDCOMPONENT="GridComponent";
    public static final String GRIDLINK_TARGETID="targetID";
    public static final String GRIDLINK_SOURCEID="sourceID";
    public static final String GRIDLINK="GridLink";
    public static final String SINK="Sink";
    public static final String SOURCE="Source";
    public static final String VALVE="Valve";
    public static final String STORAGE="Storage";

  /**
   * Constructor
  */
  private GasgridOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema storageSchema = new ConceptSchema(STORAGE);
    add(storageSchema, agentgui.gasgridEnvironment.ontology.Storage.class);
    ConceptSchema valveSchema = new ConceptSchema(VALVE);
    add(valveSchema, agentgui.gasgridEnvironment.ontology.Valve.class);
    ConceptSchema sourceSchema = new ConceptSchema(SOURCE);
    add(sourceSchema, agentgui.gasgridEnvironment.ontology.Source.class);
    ConceptSchema sinkSchema = new ConceptSchema(SINK);
    add(sinkSchema, agentgui.gasgridEnvironment.ontology.Sink.class);
    ConceptSchema gridLinkSchema = new ConceptSchema(GRIDLINK);
    add(gridLinkSchema, agentgui.gasgridEnvironment.ontology.GridLink.class);
    ConceptSchema gridComponentSchema = new ConceptSchema(GRIDCOMPONENT);
    add(gridComponentSchema, agentgui.gasgridEnvironment.ontology.GridComponent.class);
    ConceptSchema branchSchema = new ConceptSchema(BRANCH);
    add(branchSchema, agentgui.gasgridEnvironment.ontology.Branch.class);
    ConceptSchema pipeSchema = new ConceptSchema(PIPE);
    add(pipeSchema, agentgui.gasgridEnvironment.ontology.Pipe.class);
    ConceptSchema compressorSchema = new ConceptSchema(COMPRESSOR);
    add(compressorSchema, agentgui.gasgridEnvironment.ontology.Compressor.class);

    // adding AgentAction(s)

    // adding AID(s)

    // adding Predicate(s)


    // adding fields
    gridLinkSchema.add(GRIDLINK_SOURCEID, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    gridLinkSchema.add(GRIDLINK_TARGETID, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    gridComponentSchema.add(GRIDCOMPONENT_ID, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);

    // adding name mappings

    // adding inheritance
    storageSchema.addSuperSchema(gridComponentSchema);
    valveSchema.addSuperSchema(gridComponentSchema);
    sourceSchema.addSuperSchema(gridComponentSchema);
    sinkSchema.addSuperSchema(gridComponentSchema);
    branchSchema.addSuperSchema(gridComponentSchema);
    pipeSchema.addSuperSchema(gridComponentSchema);
    compressorSchema.addSuperSchema(gridComponentSchema);

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
