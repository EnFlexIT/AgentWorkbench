package de.enflexit.db.userManagement.dataModel;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * The Class WebUserRight.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "um_user_right")
public class UserRight {

	@Id
	@GeneratedValue
	@Column(name = "id_user_right", nullable=false)
	private Integer id;

	private String description;
	private int level;
	
	@ManyToMany
	@JoinTable(
			name = "um_user_rights_in_roles",
			joinColumns = @JoinColumn(name= "id_user_right", foreignKey = @ForeignKey(name = "FK_RIGHT", foreignKeyDefinition = "FOREIGN KEY (ID_USER_RIGHT) REFERENCES UM_USER_RIGHT(ID_USER_RIGHT) ON DELETE CASCADE")),
			inverseJoinColumns = @JoinColumn(name="id_role", foreignKey = @ForeignKey(name = "FK_ROLE", foreignKeyDefinition = "FOREIGN KEY (ID_ROLE) REFERENCES UM_USER_Role(ID_USER_ROLE) ON DELETE CASCADE"))
		)
	private Set<UserRole> userRoles;

	
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
	 * Gets the description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Sets the description.
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the level.
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * Sets the level.
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	
	/**
	 * Gets the web user roles.
	 * @return the webUserRoles
	 */
	public Set<UserRole> getUserRoles() {
		return userRoles;
	}
	/**
	 * Sets the web user roles.
	 * @param userRoles the webUserRoles to set
	 */
	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
}
