package de.enflexit.db.userManagement.dataModel;

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
@Table(name = "um_user_role")
public class UserRole {

	@Id
	@GeneratedValue
	@Column(name = "id_user_role", nullable=false)
	private Integer id;
	
	private String role;

	@ManyToMany(mappedBy = "userRoles")
	private Set<User> usersInRole;
	
	@ManyToMany(mappedBy = "userRoles")
	private Set<UserRight> userRights;
	
	
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
	public Set<User> getUsersInRole() {
		return usersInRole;
	}
	/**
	 * Sets the users in role.
	 * @param usersInRole the new users in role
	 */
	public void setUsersInRole(Set<User> usersInRole) {
		this.usersInRole = usersInRole;
	}
	
}
