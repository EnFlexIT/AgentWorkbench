package de.enflexit.df.core.processing.transformation.graph.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.common.properties.Properties;
import de.enflexit.df.core.processing.transformation.graph.DataTransformationEdge;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * This class holds all information that is required for saving and loading {@link DataTransformationEdge}s.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */

@Entity
@Table(name="data_transformation_edge")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersistentTransformation implements Serializable {
	
	private static final long serialVersionUID = -1924241629656187251L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_transformation_edge", nullable=false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = PersistentTransformationGraph.class)
	@JoinColumn(name = "id_transformation_graph", nullable = false)
	@XmlTransient
	private PersistentTransformationGraph transformationGraph;
	
	@Column(name="transformation_name", nullable=false)
	private String transformationName;
	
	@Convert(converter = PropertiesConverter.class)
	@Column(name = "transformation_parameters", columnDefinition = "TEXT")
	private Properties transformationParameters;
	
	@Convert(converter = StringsListConverter.class)
	@Column(name = "input_node_uuids", columnDefinition = "TEXT")
	private List<String> inputNodeUUIDs;
	
	@Column(name="output_node_uuid", nullable=false)
	private String outputNodeUUID;
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * Gets the transformation graph.
	 * @return the transformation graph
	 */
	public PersistentTransformationGraph getTransformationGraph() {
		return transformationGraph;
	}
	/**
	 * Sets the transformation graph.
	 * @param transformationGraph the new transformation graph
	 */
	public void setTransformationGraph(PersistentTransformationGraph transformationGraph) {
		this.transformationGraph = transformationGraph;
	}

	/**
	 * Gets the transformation name.
	 * @return the transformation name
	 */
	public String getTransformationName() {
		return transformationName;
	}
	
	/**
	 * Sets the transformation name.
	 * @param transformationName the new transformation name
	 */
	public void setTransformationName(String transformationName) {
		this.transformationName = transformationName;
	}
	
	/**
	 * Gets the transformation parameters.
	 * @return the transformation parameters
	 */
	public Properties getTransformationParameters() {
		return transformationParameters;
	}
	/**
	 * Sets the transformation parameters.
	 * @param transformationParameters the new transformation parameters
	 */
	public void setTransformationParameters(Properties transformationParameters) {
		this.transformationParameters = transformationParameters;
	}
	
	/**
	 * Gets the input node UUIDs.
	 * @return the input node UUIDs
	 */
	public List<String> getInputNodeUUIDs() {
		if (inputNodeUUIDs==null) {
			inputNodeUUIDs = new ArrayList<String>();
		}
		return inputNodeUUIDs;
	}
	
	/**
	 * Sets the input node UUIDs.
	 * @param inputNodeUUIDs the new input node UUIDs
	 */
	public void setInputNodeUUIDs(ArrayList<String> inputNodeUUIDs) {
		this.inputNodeUUIDs = inputNodeUUIDs;
	}
	
	/**
	 * Gets the output node UUID.
	 * @return the output node UUID
	 */
	public String getOutputNodeUUID() {
		return outputNodeUUID;
	}
	/**
	 * Sets the output node UUID.
	 * @param outputNodeUUID the new output node UUID
	 */
	public void setOutputNodeUUID(String outputNodeUUID) {
		this.outputNodeUUID = outputNodeUUID;
	}
}
