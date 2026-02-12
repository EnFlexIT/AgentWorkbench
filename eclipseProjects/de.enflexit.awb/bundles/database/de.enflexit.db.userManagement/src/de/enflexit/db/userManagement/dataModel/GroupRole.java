package de.enflexit.db.userManagement.dataModel;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The Class WebGroupRole.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "um_group_role")
public class GroupRole {

	@Id
	@GeneratedValue
	@Column(name = "id_group_role", nullable = false)
	private Integer id;

	private String role;


	@OneToMany(mappedBy = "groupRole")
	private Set<UserGroupRole> userGroupRoles;
	
	@ManyToMany(mappedBy = "groupRoles")
	private Set<GroupRight> groupsRights;
	

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
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * Sets the role name.
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
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

	/**
	 * Returns the groups rights.
	 * @return the groups rights
	 */
	public Set<GroupRight> getGroupsRights() {
		return groupsRights;
	}
	/**
	 * Sets the web groups rights.
	 * @param groupsRights the new web groups rights
	 */
	public void setGroupsRights(Set<GroupRight> groupsRights) {
		this.groupsRights = groupsRights;
	}
	
}
