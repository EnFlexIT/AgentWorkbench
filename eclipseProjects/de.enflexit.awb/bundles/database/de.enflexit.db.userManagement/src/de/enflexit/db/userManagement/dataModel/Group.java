package de.enflexit.db.userManagement.dataModel;

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
@Table(name = "um_group")
public class Group {

	@Id
	@GeneratedValue
	@Column(name="id_group", nullable=false)
	private Integer id;

	private String groupName;
	@Column(name = "date_group_created")
	private Instant dateGroupCreated;
	
	@OneToMany(mappedBy = "group")
	private Set<UserGroupRole> userGroupRoles;

	
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
	 * Returns the user group rights.
	 * @return the user group rights
	 */
	public Set<UserGroupRole> getUserGroupRights() {
		return userGroupRoles;
	}
	/**
	 * Sets the user group rights.
	 * @param userGroupRoles the new user group rights
	 */
	public void setUserGroupRights(Set<UserGroupRole> userGroupRoles) {
		this.userGroupRoles = userGroupRoles;
	}
	
}
