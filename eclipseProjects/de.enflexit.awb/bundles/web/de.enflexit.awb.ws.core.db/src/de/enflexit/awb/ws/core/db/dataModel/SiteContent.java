package de.enflexit.awb.ws.core.db.dataModel;

import java.util.Set;

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
public abstract class SiteContent {

	@Id
	@GeneratedValue
	@Column(name = "id_site_content")
	private Integer id;
	
	@Column(name = "is_editable")
	private boolean isEditable;
	
	@Column(name = "update_period_in_seconds")
	private int updatePeriodInSeconds;
	
	@OneToMany(mappedBy = "siteContent")
	private Set<SiteMenuContent> siteContentMenu;


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
	 * @return the siteContentMenu
	 */
	public Set<SiteMenuContent> getSiteContentMenu() {
		return siteContentMenu;
	}
	/**
	 * Sets the site content menu.
	 * @param siteContentMenu the siteContentMenu to set
	 */
	public void setSiteContentMenu(Set<SiteMenuContent> siteContentMenu) {
		this.siteContentMenu = siteContentMenu;
	}
	
}
