package de.enflexit.awb.ws.core.db.dataModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

/**
 * The Class SiteContentTableWithValues.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@Entity
public class SiteContentTableWithValues extends SiteContentTable {
    
	private static final long serialVersionUID = 1L;
	
	@Lob
    @Column(name = "table_data_json")
    private String tableDataJson;
	
	/**
	 * @return the tableDataJson
	 */
	public String getTableDataJson() {
		return tableDataJson;
	}

	/**
	 * @param tableDataJson the tableDataJson to set
	 */
	public void setTableDataJson(String tableDataJson) {
		this.tableDataJson = tableDataJson;
	}
}