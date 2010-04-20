package contmas.main;

/**
 * @author Hanno - Felix Wagner, 08.04.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class OWLImportMapper{

	protected static final String AID="AID";
	protected static final String AGENTACTION="AgentAction";
	protected static final String CONCEPT="Concept";
	protected static final String PREDICATE="Predicate";

	protected static final String beanGeneratorNS="http://jade.cselt.it/beangenerator#"; //NS prefix j.0 
	protected static final String w3cRDFURI="http://www.w3.org/2000/01/rdf-schema#"; //NS prefix rdfs

	protected String structureNS;
	protected String individualNS;

	protected String ontologyJavaPackage;
	protected OntModel model;

	private HashMap<String, Object> allMappedConcepts=new HashMap<String, Object>();
	private HashMap<Individual, HashMap<OntProperty, List<RDFNode>>> allKnownProperties=new HashMap<Individual, HashMap<OntProperty, List<RDFNode>>>();

	public OWLImportMapper(String inputFileName,String ontologyJavaPackage){
		this.setIndividualsFile(inputFileName);

		this.ontologyJavaPackage=ontologyJavaPackage;
	}
	
	public void resetMappings(){
		allMappedConcepts=new HashMap<String, Object>();
		allKnownProperties=new HashMap<Individual, HashMap<OntProperty, List<RDFNode>>>();
	}

	protected OntModel readModel(String inputFileName){
		OntDocumentManager mgr=new OntDocumentManager();
		mgr.setProcessImports(false);
		OntModelSpec s=new OntModelSpec(OntModelSpec.OWL_MEM);
		s.setDocumentManager(mgr);
		OntModel curModel=ModelFactory.createOntologyModel(s);
		InputStream in=FileManager.get().open(inputFileName);
		if(in == null){
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		curModel.read(in,"");
//		curModel.get
		return curModel;
	}

	public void setStructureFile(String inputFileName){
		OntModel structureModel=this.readModel(inputFileName);
		this.structureNS=structureModel.getNsPrefixMap().get(""); //set URI of current ontology as namespace URI with blank prefix
//		System.out.println("structureNS: "+structureNS);
		this.model.addSubModel(structureModel,true);
	}

	protected void setIndividualsFile(String inputFileName){
		OntModel individualModel=this.readModel(inputFileName);
		this.individualNS=individualModel.getNsPrefixMap().get(""); //set URI of current ontology as namespace URI with blank prefix
//		System.out.println("individualNS: "+individualNS);
		this.model=individualModel;
	}

	public void setBeanGeneratorFile(String inputFileName){
		OntModel beanGenModel=this.readModel(inputFileName);
		this.model.addSubModel(beanGenModel,true);
		this.model.addLoadedImport(beanGenModel.getNsPrefixMap().get(""));
	}
	
	public void addSubOntology(String inputFileName){
		OntModel subModel=this.readModel(inputFileName);
		this.model.addSubModel(subModel,true);
		this.model.addLoadedImport(subModel.getNsPrefixMap().get(""));
	}

	public Model getModel(){
		return this.model;
	}

	public void printAllStatements(){
		StmtIterator iter=this.model.listStatements();
		while(iter.hasNext()){
			System.out.println("Statement: " + iter.nextStatement().toString());
		}
	}

	public List<Object> getMappedIndividualsOf(String concept){
		return this.getMappedIndividualsOf(this.model.createResource(this.structureNS + concept));
	}

	public List<Object> getMappedIndividualsOf(Resource concept){
		List<Object> mappedIndividuals=new ArrayList<Object>();
//		System.out.println("All Individuals of " + concept + ":");

		ExtendedIterator<Individual> iter=this.model.listIndividuals(concept);
		while(iter.hasNext()){
			Individual res=iter.next();
//			System.out.println(res);
			Object mappedIndividual=this.getMappedIndividual(res);

			mappedIndividuals.add(mappedIndividual);
		}
		return mappedIndividuals;
	}

	public HashMap<String, Object> getNamedMappedIndividualsOf(String concept){
		return this.getNamedMappedIndividualsOf(this.model.createResource(this.structureNS + concept));
	}

	public HashMap<String, Object> getNamedMappedIndividualsOf(Resource concept){
		HashMap<String, Object> mappedIndividuals=new HashMap<String, Object>();
		ExtendedIterator<Individual> iter=this.model.listIndividuals(concept);
		while(iter.hasNext()){
			Individual res=iter.next();
//			System.out.println(res);
			Object mappedIndividual=this.getMappedIndividual(res);

			mappedIndividuals.put(this.getResourceName(res),mappedIndividual);
		}
		return mappedIndividuals;
	}

	public String getResourceName(Resource res){
		if(res.isAnon()){
			return res.getId().getLabelString();
		}else{
			return res.getLocalName();
		}
	}

	public List<Object> getMappedIndividualsOf(List<Resource> allConcepts){
		List<Object> allIndividuals=new ArrayList<Object>();
		Iterator<Resource> iter=allConcepts.iterator();
		while(iter.hasNext()){
			Resource curConcept=iter.next();
			allIndividuals.addAll(this.getMappedIndividualsOf(curConcept));
		}
		return allIndividuals;
	}

	protected HashMap<OntProperty, List<RDFNode>> getPropertiesOf(Individual indiv){
//		System.out.println("getPropertiesOf "+indiv);
		HashMap<OntProperty, List<RDFNode>> properties=new HashMap<OntProperty, List<RDFNode>>();
		StmtIterator iter=this.model.listStatements(new SimpleSelector(indiv,null,(RDFNode) null));
//		StmtIterator iter=indiv.getOntClass().listProperties();

		List<RDFNode> valueList;
		while(iter.hasNext()){
			Statement stmt=iter.next();
			Property predicate=stmt.getPredicate();
//			System.out.print("	"+predicate+"=");
			/*
			if( predicate.getLocalName().equals("type")){ //internal ../22-rdf-syntax-ns#type property
				continue;
			}
			*/
			
			if( !predicate.canAs(OntProperty.class)){ //internal ../22-rdf-syntax-ns#type property
				continue;
			}
			
			OntProperty prop=stmt.getPredicate().as(OntProperty.class);
			RDFNode value=stmt.getObject();
//			System.out.println(value);

			if(properties.containsKey(prop)){ //one value of this property has already been added, thus creating a list
				valueList=properties.get(prop);
			}else{ //property not yet known, create new list
				valueList=new ArrayList<RDFNode>();
			}
			valueList.add(value);
			properties.put(prop,valueList);
		}
		return properties;
	}

	public void printProperties(HashMap<OntProperty, List<RDFNode>> properties){
		Iterator<Entry<OntProperty, List<RDFNode>>> iter=properties.entrySet().iterator();
		while(iter.hasNext()){
			Entry<OntProperty, List<RDFNode>> entry=iter.next();
			System.out.println("OntProperty: " + entry.getKey().getLocalName());
			Iterator<RDFNode> valueIter=entry.getValue().iterator();
			while(valueIter.hasNext()){
				RDFNode node=valueIter.next();
				System.out.println("	Value:    " + node.toString());
				System.out.println("		isLiteral: " + node.isLiteral());
			}
		}
	}

	protected void mapAllProperties(HashMap<OntProperty, List<RDFNode>> properties,Object on){
		final String SET="set";
		final String ADD="add";

		Iterator<Entry<OntProperty, List<RDFNode>>> iter=properties.entrySet().iterator();
		while(iter.hasNext()){
			Entry<OntProperty, List<RDFNode>> entry=iter.next();
			String action=SET;

			if(entry.getKey().canAs(FunctionalProperty.class)){
				action=SET;
			}else{
				action=ADD;
			}
			Iterator<RDFNode> valIter=entry.getValue().iterator();
			try{
				Class<?> claas=OWLImportMapper.convertClass(this.getClassOfProperty(entry));
				String propertyName=getJavaName(entry.getKey());
//				System.out.println("set property "+propertyName+" as:");
				Method setter=on.getClass().getMethod(action + propertyName,claas);
				while(valIter.hasNext()){
					RDFNode node=valIter.next();
//					System.out.println("	"+node);

					if(node.isLiteral()){
						setter.invoke(on,node.as(Literal.class).getValue());
					}else{
						setter.invoke(on,this.getMappedIndividual(node.as(Individual.class)));
					}
				}

			}catch(SecurityException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(NoSuchMethodException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IllegalArgumentException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IllegalAccessException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(InvocationTargetException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected Class<?> getClassOfProperty(Entry<OntProperty, List<RDFNode>> entry){
		OntProperty key=entry.getKey();

		Iterator<RDFNode> iter=entry.getValue().iterator();
		while(iter.hasNext()){ //TODO does only the first element in list, in case there are more than one, assume generic type like Object or String(Datatype)
			RDFNode value=iter.next();
			if( !value.isLiteral()){
				Resource range=key.getRange();
				return this.mapResourceAsClass(range);
			}else if(value.isLiteral()){
				Literal literal=value.as(Literal.class);
				return literal.getDatatype().getJavaClass();
			}
		}
		return null;
	}

	protected Object getMappedIndividual(Individual lookFor){
		Object found;
		if(this.allMappedConcepts.containsKey(lookFor.toString())){
			found=this.allMappedConcepts.get(lookFor.toString());
//			System.out.print("Individual "+lookFor+" was already mapped as ");
		}else{
			found=this.mapIndividualAsInstance(lookFor);
//			System.out.print("Individual "+lookFor+" newly mapped as ");
		}
//		System.out.println(found);

		return found;
	}

	protected static Class<?> convertClass(Class<?> ObjectClass){
		if(ObjectClass == Integer.class){
			return int.class;
		}else if(ObjectClass == Float.class){
			return float.class;
		}else{
			return ObjectClass;
		}
	}

	public List<Resource> getSubConceptsOf(String superConcept,Boolean transitive){
		Resource superClass=this.model.createResource(this.structureNS + superConcept); //old: OWLImportMapper.beanGeneratorNS
		return this.getSubConceptsOf(superClass,transitive);
	}

	public List<Resource> getSubConceptsOf(Resource superClass,Boolean transitive){
		StmtIterator iter=this.model.listStatements(new SimpleSelector(null,this.model.getProperty(OWLImportMapper.w3cRDFURI + "subClassOf"),superClass));
		List<Resource> subConcepts=new ArrayList<Resource>();
		while(iter.hasNext()){
			Statement stmt=iter.nextStatement();
			Resource test=stmt.getSubject();
//			if( test.getNameSpace().equals(ontologyURI)){
			if( !test.getNameSpace().equals(OWLImportMapper.beanGeneratorNS)){ //TODO work on the namespaces
				subConcepts.add(test);
				if(transitive){
					subConcepts.addAll(this.getSubConceptsOf(test,true));
				}
			}
		}
		return subConcepts;
	}

	protected String getJavaName(Resource toBeMapped){
		String resourceName=toBeMapped.getLocalName();
		String ns=toBeMapped.getNameSpace();
//		System.out.println("getJavaClassName "+toBeMapped+" ns: "+ns);
		if(!ns.equals(structureNS)){
			resourceName= model.getNsURIPrefix(ns)+"_"+resourceName;
		}
		return OWLImportMapper.capitalStart(resourceName);
	}
	
	protected Class<?> mapResourceAsClass(Resource toBeMapped){
		try{
			Class<?> conceptClass=Class.forName(this.ontologyJavaPackage + "." + getJavaName(toBeMapped));
			return conceptClass;
		}catch(ClassNotFoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(SecurityException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected Object mapIndividualAsInstance(Individual individual){
		try{
			Class<?> of=this.mapResourceAsClass(individual.getRDFType());
			
//			System.out.println("map Individual "+individual+" as Instance of "+of);
			
			String hash=individual.toString();
			Constructor<?> conceptConstructor=of.getConstructor();
			Object conceptObject=conceptConstructor.newInstance();

			HashMap<OntProperty, List<RDFNode>> properties=this.filterProperties(this.getPropertiesOf(individual),individual);
			this.allMappedConcepts.put(hash,conceptObject);

			this.mapAllProperties(properties,conceptObject);

			return conceptObject;
		}catch(IllegalArgumentException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(InstantiationException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IllegalAccessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(InvocationTargetException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(SecurityException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(NoSuchMethodException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param properties
	 * @param individual 
	 * @return
	 */
	protected HashMap<OntProperty, List<RDFNode>> filterProperties(HashMap<OntProperty, List<RDFNode>> properties,Individual individual){
		HashMap<OntProperty, List<RDFNode>> curProperties;
		if(this.allKnownProperties.containsKey(individual)){
			curProperties=this.allKnownProperties.get(individual);
		}else{ //no properties for this individual known yet
			curProperties=new HashMap<OntProperty, List<RDFNode>>();
			this.allKnownProperties.put(individual,curProperties);
		}

		HashMap<OntProperty, List<RDFNode>> filtered=new HashMap<OntProperty, List<RDFNode>>();

		Iterator<OntProperty> iter=properties.keySet().iterator();
		while(iter.hasNext()){
			OntProperty key=iter.next();
			if( !curProperties.containsKey(key)){ //not yet known
				curProperties.put(key,properties.get(key)); //add to known props
				filtered.put(key,properties.get(key));
			}else{

			}
		}
		return filtered;
	}

	/**
	 * 
	 */
	public void printModel(){
		this.model.write(System.out,"RDF/XML-ABBREV",this.structureNS);
	}

	public void printNSPrefixMap(){
		Map<String, String> karte=this.model.getNsPrefixMap();

		Set<String> schluessel=karte.keySet();
		for(Iterator<String> iterator=schluessel.iterator();iterator.hasNext();){
			String key=iterator.next();
			System.out.print("Key: " + key + " \t\t\t");
			String value=karte.get(key);
			System.out.println("Value: " + value);
		}
	}

	protected static String capitalStart(String input){
		String output=input;
		output=input.substring(0,1).toUpperCase();
		output=output + input.substring(1);
		return output;
	}

	/*
	 * not usable due to 
	 * com.hp.hpl.jena.ontology.ConversionException:
	 * Cannot convert node http://jade.cselt.it/beangenerator#AID to OntClass: it does not have rdf:type owl:Class or equivalent
	 * at listDeclaredProperties(true);
	 */
	protected HashMap<OntProperty, List<RDFNode>> getPropertiesOfALT(Individual indiv){
		HashMap<OntProperty, List<RDFNode>> properties=new HashMap<OntProperty, List<RDFNode>>();
		ExtendedIterator<OntProperty> iter=indiv.getOntClass().listDeclaredProperties(true);

		List<RDFNode> valueList;
		while(iter.hasNext()){
			OntProperty prop=iter.next();
			NodeIterator values=prop.listPropertyValues(prop);
			while(values.hasNext()){
				RDFNode object=values.next();
				if(object.isLiteral()){ //only development simplification, remove
					if(properties.containsKey(prop)){ //one value of this property has already been added, thus creating a list
						valueList=properties.get(prop);
					}else{ //property not yet known, create new list
						valueList=new ArrayList<RDFNode>();
					}
					valueList.add(object);
					properties.put(prop,valueList);
				}else{
//					properties.put(predicate,object);
				}
			}
		}
		return properties;
	}
}
