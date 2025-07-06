package de.enflexit.awb.ws.core.db.dataModel;

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
@Table(name = "web_group_role")
public class WebGroupRole {

	@Id
	@GeneratedValue
	@Column(name = "id_web_group_role", nullable = false)
	private Integer id;

	private String role;


	@OneToMany(mappedBy = "webGroupRole")
	private Set<WebUserGroupRole> webUserGroupRoles;
	
	@ManyToMany(mappedBy = "webGroupRoles")
	private Set<WebGroupRight> webGroupsRights;
	

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

	/**
	 * Returns the web groups rights.
	 * @return the web groups rights
	 */
	public Set<WebGroupRight> getWebGroupsRights() {
		return webGroupsRights;
	}
	/**
	 * Sets the web groups rights.
	 * @param webGroupsRights the new web groups rights
	 */
	public void setWebGroupsRights(Set<WebGroupRight> webGroupsRights) {
		this.webGroupsRights = webGroupsRights;
	}
	
}
