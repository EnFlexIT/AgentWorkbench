package de.enflexit.awb.ws.core.db.dataModel;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The Class SiteContent.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "site_content")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class SiteContent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id_site_content")
	private Integer id;
	
	@Column(name = "is_editable")
	private boolean isEditable;
	
	@Column(name = "update_period_in_seconds")
	private int updatePeriodInSeconds;
	
	@OneToMany(mappedBy = "siteContent", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SiteMenuContent> siteMenuContent;


	/**
	 * Returns the id.
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Returns if the content is editable.
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}
	/**
	 * Sets the editable.
	 * @param isEditable the isEditable to set
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * Returns the update period in seconds.
	 * @return the updatePeriodInSeconds
	 */
	public int getUpdatePeriodInSeconds() {
		return updatePeriodInSeconds;
	}
	/**
	 * Sets the update period in seconds.
	 * @param updatePeriodInSeconds the updatePeriodInSeconds to set
	 */
	public void setUpdatePeriodInSeconds(int updatePeriodInSeconds) {
		this.updatePeriodInSeconds = updatePeriodInSeconds;
	}

	/**
	 * Returns the site content menu.
	 * @return the siteMenuContent
	 */
	public Set<SiteMenuContent> getSiteMenuContent() {
		return siteMenuContent;
	}
	/**
	 * Sets the site content menu.
	 * @param siteMenuContent the siteMenuContent to set
	 */
	public void setSiteMenuContent(Set<SiteMenuContent> siteContentMenu) {
		this.siteMenuContent = siteContentMenu;
	}
	
}