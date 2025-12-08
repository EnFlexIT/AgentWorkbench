package de.enflexit.awb.samples.db.dataModel;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The Class WebGroup.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "ex_web_group")
public class WebGroup {

	@Id
	@GeneratedValue
	@Column(name="id_web_group", nullable=false)
	private Integer id;

	private String groupName;
	@Column(name = "date_group_created")
	private Instant dateGroupCreated;
	
	@OneToMany(mappedBy = "webGroup")
	private Set<WebUserGroupRole> webUserGroupRoles;

	
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
	 * Returns the group name.
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * Sets the group name.
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Returns the date to which the group was created.
	 * @return the date group created
	 */
	public Instant getDateGroupCreated() {
		return dateGroupCreated;
	}
	/**
	 * Sets the date to which the group was created.
	 * @param dateGroupCreated the new date group created
	 */
	public void setDateGroupCreated(Instant dateGroupCreated) {
		this.dateGroupCreated = dateGroupCreated;
	}
	
	/**
	 * Returns the web user group rights.
	 * @return the web user group rights
	 */
	public Set<WebUserGroupRole> getWebUserGroupRights() {
		return webUserGroupRoles;
	}
	/**
	 * Sets the web user group rights.
	 * @param webUserGroupRoles the new web user group rights
	 */
	public void setWebUserGroupRights(Set<WebUserGroupRole> webUserGroupRoles) {
		this.webUserGroupRoles = webUserGroupRoles;
	}
	
}
