package de.enflexit.awb.ws.core.db.dataModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OrderColumn;

/**
 * The Class SiteContentTable.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "table_type")
public abstract class SiteContentTable extends SiteContent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "site_content_table_header", joinColumns = @JoinColumn(name = "id_site_content"))
    @OrderColumn(name = "header_order")
	@Column(name = "header_title")
    private List<String> header = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "site_content_table_data_type", joinColumns = @JoinColumn(name = "id_site_content"))
    @OrderColumn(name = "data_type_order")
    private List<String> dataType = new ArrayList<>();

    /**
	 * @return the header
	 */
	public List<String> getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(List<String> header) {
		this.header = header;
	}

	/**
	 * @return the dataType
	 */
	public List<String> getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(List<String> dataType) {
		this.dataType = dataType;
	}


}