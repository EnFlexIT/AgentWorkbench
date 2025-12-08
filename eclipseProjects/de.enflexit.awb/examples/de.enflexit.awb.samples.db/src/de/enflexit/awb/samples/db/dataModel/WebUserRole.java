package de.enflexit.awb.samples.db.dataModel;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * The Class WebUserRole.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "ex_web_user_role")
public class WebUserRole {

	@Id
	@GeneratedValue
	@Column(name = "id_web_user_role", nullable=false)
	private Integer id;
	
	private String role;

	@ManyToMany(mappedBy = "userRoles")
	private Set<WebUser> usersInRole;
	
	@ManyToMany(mappedBy = "webUserRoles")
	private Set<WebUserRight> webUserRights;
	
	
	/**
	 * Gets the id.
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
	 * Gets the role.
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * Sets the role.
	 * @param locale the role to set
	 */
	public void setLocale(String role) {
		this.role = role;
	}
	
	/**
	 * Returns the users in the current role.
	 * @return the users in role
	 */
	public Set<WebUser> getUsersInRole() {
		return usersInRole;
	}
	/**
	 * Sets the users in role.
	 * @param usersInRole the new users in role
	 */
	public void setUsersInRole(Set<WebUser> usersInRole) {
		this.usersInRole = usersInRole;
	}
	
}
