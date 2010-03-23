// file: ContainerTerminalOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package contmas.ontology;

import jade.content.onto.*;
import jade.content.schema.*;
import jade.util.leap.HashMap;
import jade.content.lang.Codec;
import jade.core.CaseInsensitiveString;

/** file: ContainerTerminalOntology.java
 * @author ontology bean generator
 * @version 2010/03/21, 12:51:18
 */
public class ContainerTerminalOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "ContainerTerminal";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new ContainerTerminalOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String STATICCONTAINERHOLDER="StaticContainerHolder";
    public static final String AGV="AGV";
    public static final String TRAIN="Train";
    public static final String PASSIVECONTAINERHOLDER="PassiveContainerHolder";
    public static final String APRON="Apron";
    public static final String ACTIVECONTAINERHOLDER_CAPABLE_OF="capable_of";
    public static final String ACTIVECONTAINERHOLDER="ActiveContainerHolder";
    public static final String SHIP_LENGTH="length";
    public static final String SHIP="Ship";
    public static final String CONTAINERHOLDER_CONTAINER_STATES="container_states";
    public static final String CONTAINERHOLDER_SERVICE_TYPE="service_type";
    public static final String CONTAINERHOLDER_CONTRACTORS="contractors";
    public static final String CONTAINERHOLDER_LIVES_IN="lives_in";
    public static final String CONTAINERHOLDER_CONTAINS="contains";
    public static final String CONTAINERHOLDER="ContainerHolder";
    public static final String CRANE="Crane";
    public static final String STRADDLECARRIER="StraddleCarrier";
    public static final String RMG="RMG";
    public static final String YARD="Yard";
    public static final String CALLFORPROPOSALSONLOADSTAGE_REQUIRED_TURNOVER_CAPACITY="required_turnover_capacity";
    public static final String CALLFORPROPOSALSONLOADSTAGE="CallForProposalsOnLoadStage";
    public static final String REQUESTPOPULATEDBAYMAP_POPULATE_ON="populate_on";
    public static final String REQUESTPOPULATEDBAYMAP="RequestPopulatedBayMap";
    public static final String GETCRANELIST_REQUIRED_TURNOVER_CAPACITY="required_turnover_capacity";
    public static final String GETCRANELIST_ASSIGNED_QUAY="assigned_quay";
    public static final String GETCRANELIST="GetCraneList";
    public static final String PROVIDECRANELIST_AVAILABLE_CRANES="available_cranes";
    public static final String PROVIDECRANELIST="ProvideCraneList";
    public static final String ASSIGNHARBORQUAY_ASSIGNED_QUAY="assigned_quay";
    public static final String ASSIGNHARBORQUAY="AssignHarborQuay";
    public static final String PROVIDEPOPULATEDBAYMAP_PROVIDES="provides";
    public static final String PROVIDEPOPULATEDBAYMAP="ProvidePopulatedBayMap";
    public static final String ANNOUNCELOADSTATUS_LOAD_STATUS="load_status";
    public static final String ANNOUNCELOADSTATUS_LOAD_OFFER="load_offer";
    public static final String ANNOUNCELOADSTATUS="AnnounceLoadStatus";
    public static final String PROVIDEBAYMAP_PROVIDES="provides";
    public static final String PROVIDEBAYMAP="ProvideBayMap";
    public static final String REQUESTRANDOMBAYMAP_Y_DIMENSION="y_dimension";
    public static final String REQUESTRANDOMBAYMAP_X_DIMENSION="x_dimension";
    public static final String REQUESTRANDOMBAYMAP_Z_DIMENSION="z_dimension";
    public static final String REQUESTRANDOMBAYMAP="RequestRandomBayMap";
    public static final String PROPOSELOADOFFER_LOAD_OFFER="load_offer";
    public static final String PROPOSELOADOFFER="ProposeLoadOffer";
    public static final String ENROLLATHARBOR_SHIP_LENGTH="ship_length";
    public static final String ENROLLATHARBOR="EnrollAtHarbor";
    public static final String REJECTLOADOFFER_LOAD_OFFER="load_offer";
    public static final String REJECTLOADOFFER="RejectLoadOffer";
    public static final String REQUESTONTOLOGYREPRESENTATION_AGENT_IN_QUESTION="agent_in_question";
    public static final String REQUESTONTOLOGYREPRESENTATION="RequestOntologyRepresentation";
    public static final String ACCEPTLOADOFFER_LOAD_OFFER="load_offer";
    public static final String ACCEPTLOADOFFER="AcceptLoadOffer";
    public static final String PROVIDEONTOLOGYREPRESENTATION_ACCORDING_ONTREP="according_ontrep";
    public static final String PROVIDEONTOLOGYREPRESENTATION="ProvideOntologyRepresentation";
    public static final String TWENTYFOOTCONTAINER="TwentyFootContainer";
    public static final String LAND="Land";
    public static final String TRANSPORTORDERCHAINSTATE="TransportOrderChainState";
    public static final String SEA="Sea";
    public static final String APRONAREA="ApronArea";
    public static final String PROPOSEDFOR="ProposedFor";
    public static final String BERTH="Berth";
    public static final String PENDINGFORSUBCFP="PendingForSubCFP";
    public static final String ADMINISTERED="Administered";
    public static final String FAILED="Failed";
    public static final String QUAY="Quay";
    public static final String RAIL="Rail";
    public static final String ANNOUNCED="Announced";
    public static final String STREET="Street";
    public static final String CONTAINER_WIDTH="width";
    public static final String CONTAINER_WEIGHT="weight";
    public static final String CONTAINER_LENGTH="length";
    public static final String CONTAINER_HEIGHT="height";
    public static final String CONTAINER_BIC_CODE="bic_code";
    public static final String CONTAINER="Container";
    public static final String YARDAREA="YardArea";
    public static final String HARBOUR="Harbour";
    public static final String DESIGNATOR_ABSTRACT_DESIGNATION="abstract_designation";
    public static final String DESIGNATOR_TYPE="type";
    public static final String DESIGNATOR_CONCRETE_DESIGNATION="concrete_designation";
    public static final String DESIGNATOR="Designator";
    public static final String LOADLIST_CONSISTS_OF="consists_of";
    public static final String LOADLIST="LoadList";
    public static final String TOCHASSTATE_STATE="state";
    public static final String TOCHASSTATE_SUBJECTED_TOC="subjected_toc";
    public static final String TOCHASSTATE="TOCHasState";
    public static final String TRANSPORTORDERCHAIN_TRANSPORTS="transports";
    public static final String TRANSPORTORDERCHAIN_IS_LINKED_BY="is_linked_by";
    public static final String TRANSPORTORDERCHAIN_TERMINATES_AT="terminates_at";
    public static final String TRANSPORTORDERCHAIN="TransportOrderChain";
    public static final String TRANSPORTORDER_ENDS_AT="ends_at";
    public static final String TRANSPORTORDER_STARTS_AT="starts_at";
    public static final String TRANSPORTORDER_TAKES="takes";
    public static final String TRANSPORTORDER="TransportOrder";
    public static final String DOMAIN_ID="id";
    public static final String DOMAIN_HAS_SUBDOMAINS="has_subdomains";
    public static final String DOMAIN_LIES_IN="lies_in";
    public static final String DOMAIN="Domain";
    public static final String BAYMAP_Y_DIMENSION="y_dimension";
    public static final String BAYMAP_IS_FILLED_WITH="is_filled_with";
    public static final String BAYMAP_X_DIMENSION="x_dimension";
    public static final String BAYMAP_Z_DIMENSION="z_dimension";
    public static final String BAYMAP="BayMap";
    public static final String BLOCKADDRESS_Y_DIMENSION="y_dimension";
    public static final String BLOCKADDRESS_LOCATES="locates";
    public static final String BLOCKADDRESS_X_DIMENSION="x_dimension";
    public static final String BLOCKADDRESS_Z_DIMENSION="z_dimension";
    public static final String BLOCKADDRESS="BlockAddress";

  /**
   * Constructor
  */
  private ContainerTerminalOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema blockAddressSchema = new ConceptSchema(BLOCKADDRESS);
    add(blockAddressSchema, contmas.ontology.BlockAddress.class);
    ConceptSchema bayMapSchema = new ConceptSchema(BAYMAP);
    add(bayMapSchema, contmas.ontology.BayMap.class);
    ConceptSchema domainSchema = new ConceptSchema(DOMAIN);
    add(domainSchema, contmas.ontology.Domain.class);
    ConceptSchema transportOrderSchema = new ConceptSchema(TRANSPORTORDER);
    add(transportOrderSchema, contmas.ontology.TransportOrder.class);
    ConceptSchema transportOrderChainSchema = new ConceptSchema(TRANSPORTORDERCHAIN);
    add(transportOrderChainSchema, contmas.ontology.TransportOrderChain.class);
    ConceptSchema tocHasStateSchema = new ConceptSchema(TOCHASSTATE);
    add(tocHasStateSchema, contmas.ontology.TOCHasState.class);
    ConceptSchema loadListSchema = new ConceptSchema(LOADLIST);
    add(loadListSchema, contmas.ontology.LoadList.class);
    ConceptSchema designatorSchema = new ConceptSchema(DESIGNATOR);
    add(designatorSchema, contmas.ontology.Designator.class);
    ConceptSchema harbourSchema = new ConceptSchema(HARBOUR);
    add(harbourSchema, contmas.ontology.Harbour.class);
    ConceptSchema yardAreaSchema = new ConceptSchema(YARDAREA);
    add(yardAreaSchema, contmas.ontology.YardArea.class);
    ConceptSchema containerSchema = new ConceptSchema(CONTAINER);
    add(containerSchema, contmas.ontology.Container.class);
    ConceptSchema streetSchema = new ConceptSchema(STREET);
    add(streetSchema, contmas.ontology.Street.class);
    ConceptSchema announcedSchema = new ConceptSchema(ANNOUNCED);
    add(announcedSchema, contmas.ontology.Announced.class);
    ConceptSchema railSchema = new ConceptSchema(RAIL);
    add(railSchema, contmas.ontology.Rail.class);
    ConceptSchema quaySchema = new ConceptSchema(QUAY);
    add(quaySchema, contmas.ontology.Quay.class);
    ConceptSchema failedSchema = new ConceptSchema(FAILED);
    add(failedSchema, contmas.ontology.Failed.class);
    ConceptSchema administeredSchema = new ConceptSchema(ADMINISTERED);
    add(administeredSchema, contmas.ontology.Administered.class);
    ConceptSchema pendingForSubCFPSchema = new ConceptSchema(PENDINGFORSUBCFP);
    add(pendingForSubCFPSchema, contmas.ontology.PendingForSubCFP.class);
    ConceptSchema berthSchema = new ConceptSchema(BERTH);
    add(berthSchema, contmas.ontology.Berth.class);
    ConceptSchema proposedForSchema = new ConceptSchema(PROPOSEDFOR);
    add(proposedForSchema, contmas.ontology.ProposedFor.class);
    ConceptSchema apronAreaSchema = new ConceptSchema(APRONAREA);
    add(apronAreaSchema, contmas.ontology.ApronArea.class);
    ConceptSchema seaSchema = new ConceptSchema(SEA);
    add(seaSchema, contmas.ontology.Sea.class);
    ConceptSchema transportOrderChainStateSchema = new ConceptSchema(TRANSPORTORDERCHAINSTATE);
    add(transportOrderChainStateSchema, contmas.ontology.TransportOrderChainState.class);
    ConceptSchema landSchema = new ConceptSchema(LAND);
    add(landSchema, contmas.ontology.Land.class);
    ConceptSchema twentyFootContainerSchema = new ConceptSchema(TWENTYFOOTCONTAINER);
    add(twentyFootContainerSchema, contmas.ontology.TwentyFootContainer.class);

    // adding AgentAction(s)
    AgentActionSchema provideOntologyRepresentationSchema = new AgentActionSchema(PROVIDEONTOLOGYREPRESENTATION);
    add(provideOntologyRepresentationSchema, contmas.ontology.ProvideOntologyRepresentation.class);
    AgentActionSchema acceptLoadOfferSchema = new AgentActionSchema(ACCEPTLOADOFFER);
    add(acceptLoadOfferSchema, contmas.ontology.AcceptLoadOffer.class);
    AgentActionSchema requestOntologyRepresentationSchema = new AgentActionSchema(REQUESTONTOLOGYREPRESENTATION);
    add(requestOntologyRepresentationSchema, contmas.ontology.RequestOntologyRepresentation.class);
    AgentActionSchema rejectLoadOfferSchema = new AgentActionSchema(REJECTLOADOFFER);
    add(rejectLoadOfferSchema, contmas.ontology.RejectLoadOffer.class);
    AgentActionSchema enrollAtHarborSchema = new AgentActionSchema(ENROLLATHARBOR);
    add(enrollAtHarborSchema, contmas.ontology.EnrollAtHarbor.class);
    AgentActionSchema proposeLoadOfferSchema = new AgentActionSchema(PROPOSELOADOFFER);
    add(proposeLoadOfferSchema, contmas.ontology.ProposeLoadOffer.class);
    AgentActionSchema requestRandomBayMapSchema = new AgentActionSchema(REQUESTRANDOMBAYMAP);
    add(requestRandomBayMapSchema, contmas.ontology.RequestRandomBayMap.class);
    AgentActionSchema provideBayMapSchema = new AgentActionSchema(PROVIDEBAYMAP);
    add(provideBayMapSchema, contmas.ontology.ProvideBayMap.class);
    AgentActionSchema announceLoadStatusSchema = new AgentActionSchema(ANNOUNCELOADSTATUS);
    add(announceLoadStatusSchema, contmas.ontology.AnnounceLoadStatus.class);
    AgentActionSchema providePopulatedBayMapSchema = new AgentActionSchema(PROVIDEPOPULATEDBAYMAP);
    add(providePopulatedBayMapSchema, contmas.ontology.ProvidePopulatedBayMap.class);
    AgentActionSchema assignHarborQuaySchema = new AgentActionSchema(ASSIGNHARBORQUAY);
    add(assignHarborQuaySchema, contmas.ontology.AssignHarborQuay.class);
    AgentActionSchema provideCraneListSchema = new AgentActionSchema(PROVIDECRANELIST);
    add(provideCraneListSchema, contmas.ontology.ProvideCraneList.class);
    AgentActionSchema getCraneListSchema = new AgentActionSchema(GETCRANELIST);
    add(getCraneListSchema, contmas.ontology.GetCraneList.class);
    AgentActionSchema requestPopulatedBayMapSchema = new AgentActionSchema(REQUESTPOPULATEDBAYMAP);
    add(requestPopulatedBayMapSchema, contmas.ontology.RequestPopulatedBayMap.class);
    AgentActionSchema callForProposalsOnLoadStageSchema = new AgentActionSchema(CALLFORPROPOSALSONLOADSTAGE);
    add(callForProposalsOnLoadStageSchema, contmas.ontology.CallForProposalsOnLoadStage.class);

    // adding AID(s)
    ConceptSchema yardSchema = new ConceptSchema(YARD);
    add(yardSchema, contmas.ontology.Yard.class);
    ConceptSchema rmgSchema = new ConceptSchema(RMG);
    add(rmgSchema, contmas.ontology.RMG.class);
    ConceptSchema straddleCarrierSchema = new ConceptSchema(STRADDLECARRIER);
    add(straddleCarrierSchema, contmas.ontology.StraddleCarrier.class);
    ConceptSchema craneSchema = new ConceptSchema(CRANE);
    add(craneSchema, contmas.ontology.Crane.class);
    ConceptSchema containerHolderSchema = new ConceptSchema(CONTAINERHOLDER);
    add(containerHolderSchema, contmas.ontology.ContainerHolder.class);
    ConceptSchema shipSchema = new ConceptSchema(SHIP);
    add(shipSchema, contmas.ontology.Ship.class);
    ConceptSchema activeContainerHolderSchema = new ConceptSchema(ACTIVECONTAINERHOLDER);
    add(activeContainerHolderSchema, contmas.ontology.ActiveContainerHolder.class);
    ConceptSchema apronSchema = new ConceptSchema(APRON);
    add(apronSchema, contmas.ontology.Apron.class);
    ConceptSchema passiveContainerHolderSchema = new ConceptSchema(PASSIVECONTAINERHOLDER);
    add(passiveContainerHolderSchema, contmas.ontology.PassiveContainerHolder.class);
    ConceptSchema trainSchema = new ConceptSchema(TRAIN);
    add(trainSchema, contmas.ontology.Train.class);
    ConceptSchema agvSchema = new ConceptSchema(AGV);
    add(agvSchema, contmas.ontology.AGV.class);
    ConceptSchema staticContainerHolderSchema = new ConceptSchema(STATICCONTAINERHOLDER);
    add(staticContainerHolderSchema, contmas.ontology.StaticContainerHolder.class);

    // adding Predicate(s)


    // adding fields
    blockAddressSchema.add(BLOCKADDRESS_Z_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    blockAddressSchema.add(BLOCKADDRESS_X_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    blockAddressSchema.add(BLOCKADDRESS_LOCATES, containerSchema, ObjectSchema.OPTIONAL);
    blockAddressSchema.add(BLOCKADDRESS_Y_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    bayMapSchema.add(BAYMAP_Z_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    bayMapSchema.add(BAYMAP_X_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    bayMapSchema.add(BAYMAP_IS_FILLED_WITH, blockAddressSchema, 0, ObjectSchema.UNLIMITED);
    bayMapSchema.add(BAYMAP_Y_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    domainSchema.add(DOMAIN_LIES_IN, domainSchema, ObjectSchema.OPTIONAL);
    domainSchema.add(DOMAIN_HAS_SUBDOMAINS, domainSchema, 0, ObjectSchema.UNLIMITED);
    domainSchema.add(DOMAIN_ID, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    transportOrderSchema.add(TRANSPORTORDER_TAKES, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    transportOrderSchema.add(TRANSPORTORDER_STARTS_AT, designatorSchema, ObjectSchema.OPTIONAL);
    transportOrderSchema.add(TRANSPORTORDER_ENDS_AT, designatorSchema, ObjectSchema.OPTIONAL);
    transportOrderChainSchema.add(TRANSPORTORDERCHAIN_TERMINATES_AT, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    transportOrderChainSchema.add(TRANSPORTORDERCHAIN_IS_LINKED_BY, transportOrderSchema, 0, ObjectSchema.UNLIMITED);
    transportOrderChainSchema.add(TRANSPORTORDERCHAIN_TRANSPORTS, containerSchema, ObjectSchema.OPTIONAL);
    tocHasStateSchema.add(TOCHASSTATE_SUBJECTED_TOC, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    tocHasStateSchema.add(TOCHASSTATE_STATE, transportOrderChainStateSchema, ObjectSchema.OPTIONAL);
    loadListSchema.add(LOADLIST_CONSISTS_OF, transportOrderChainSchema, 0, ObjectSchema.UNLIMITED);
    designatorSchema.add(DESIGNATOR_CONCRETE_DESIGNATION, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
    designatorSchema.add(DESIGNATOR_TYPE, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    designatorSchema.add(DESIGNATOR_ABSTRACT_DESIGNATION, domainSchema, ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_BIC_CODE, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_HEIGHT, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_LENGTH, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_WEIGHT, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_WIDTH, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    provideOntologyRepresentationSchema.add(PROVIDEONTOLOGYREPRESENTATION_ACCORDING_ONTREP, containerHolderSchema, ObjectSchema.OPTIONAL);
    acceptLoadOfferSchema.add(ACCEPTLOADOFFER_LOAD_OFFER, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    requestOntologyRepresentationSchema.add(REQUESTONTOLOGYREPRESENTATION_AGENT_IN_QUESTION, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
    rejectLoadOfferSchema.add(REJECTLOADOFFER_LOAD_OFFER, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    enrollAtHarborSchema.add(ENROLLATHARBOR_SHIP_LENGTH, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    proposeLoadOfferSchema.add(PROPOSELOADOFFER_LOAD_OFFER, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    requestRandomBayMapSchema.add(REQUESTRANDOMBAYMAP_Z_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    requestRandomBayMapSchema.add(REQUESTRANDOMBAYMAP_X_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    requestRandomBayMapSchema.add(REQUESTRANDOMBAYMAP_Y_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    provideBayMapSchema.add(PROVIDEBAYMAP_PROVIDES, bayMapSchema, ObjectSchema.OPTIONAL);
    announceLoadStatusSchema.add(ANNOUNCELOADSTATUS_LOAD_OFFER, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    announceLoadStatusSchema.add(ANNOUNCELOADSTATUS_LOAD_STATUS, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    providePopulatedBayMapSchema.add(PROVIDEPOPULATEDBAYMAP_PROVIDES, bayMapSchema, ObjectSchema.OPTIONAL);
    assignHarborQuaySchema.add(ASSIGNHARBORQUAY_ASSIGNED_QUAY, quaySchema, ObjectSchema.OPTIONAL);
    provideCraneListSchema.add(PROVIDECRANELIST_AVAILABLE_CRANES, (ConceptSchema)getSchema(BasicOntology.AID), 0, ObjectSchema.UNLIMITED);
    getCraneListSchema.add(GETCRANELIST_ASSIGNED_QUAY, quaySchema, ObjectSchema.OPTIONAL);
    getCraneListSchema.add(GETCRANELIST_REQUIRED_TURNOVER_CAPACITY, loadListSchema, ObjectSchema.OPTIONAL);
    requestPopulatedBayMapSchema.add(REQUESTPOPULATEDBAYMAP_POPULATE_ON, bayMapSchema, ObjectSchema.OPTIONAL);
    callForProposalsOnLoadStageSchema.add(CALLFORPROPOSALSONLOADSTAGE_REQUIRED_TURNOVER_CAPACITY, loadListSchema, ObjectSchema.OPTIONAL);
    containerHolderSchema.add(CONTAINERHOLDER_CONTAINS, bayMapSchema, ObjectSchema.OPTIONAL);
    containerHolderSchema.add(CONTAINERHOLDER_LIVES_IN, domainSchema, ObjectSchema.OPTIONAL);
    containerHolderSchema.add(CONTAINERHOLDER_CONTRACTORS, (ConceptSchema)getSchema(BasicOntology.AID), 0, ObjectSchema.UNLIMITED);
    containerHolderSchema.add(CONTAINERHOLDER_SERVICE_TYPE, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    containerHolderSchema.add(CONTAINERHOLDER_CONTAINER_STATES, tocHasStateSchema, 0, ObjectSchema.UNLIMITED);
    shipSchema.add(SHIP_LENGTH, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    activeContainerHolderSchema.add(ACTIVECONTAINERHOLDER_CAPABLE_OF, domainSchema, 0, ObjectSchema.UNLIMITED);

    // adding name mappings

    // adding inheritance
    harbourSchema.addSuperSchema(domainSchema);
    yardAreaSchema.addSuperSchema(domainSchema);
    streetSchema.addSuperSchema(domainSchema);
    announcedSchema.addSuperSchema(transportOrderChainStateSchema);
    railSchema.addSuperSchema(domainSchema);
    quaySchema.addSuperSchema(domainSchema);
    failedSchema.addSuperSchema(transportOrderChainStateSchema);
    administeredSchema.addSuperSchema(transportOrderChainStateSchema);
    pendingForSubCFPSchema.addSuperSchema(transportOrderChainStateSchema);
    berthSchema.addSuperSchema(domainSchema);
    proposedForSchema.addSuperSchema(transportOrderChainStateSchema);
    apronAreaSchema.addSuperSchema(domainSchema);
    seaSchema.addSuperSchema(domainSchema);
    landSchema.addSuperSchema(domainSchema);
    twentyFootContainerSchema.addSuperSchema(containerSchema);
    yardSchema.addSuperSchema(staticContainerHolderSchema);
    rmgSchema.addSuperSchema(activeContainerHolderSchema);
    straddleCarrierSchema.addSuperSchema(activeContainerHolderSchema);
    craneSchema.addSuperSchema(activeContainerHolderSchema);
    shipSchema.addSuperSchema(staticContainerHolderSchema);
    activeContainerHolderSchema.addSuperSchema(containerHolderSchema);
    apronSchema.addSuperSchema(staticContainerHolderSchema);
    passiveContainerHolderSchema.addSuperSchema(containerHolderSchema);
    trainSchema.addSuperSchema(staticContainerHolderSchema);
    agvSchema.addSuperSchema(passiveContainerHolderSchema);
    staticContainerHolderSchema.addSuperSchema(containerHolderSchema);

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
