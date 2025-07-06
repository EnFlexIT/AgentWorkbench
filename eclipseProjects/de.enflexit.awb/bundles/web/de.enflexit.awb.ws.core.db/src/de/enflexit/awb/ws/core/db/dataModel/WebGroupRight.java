package de.enflexit.awb.ws.core.db.dataModel;

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
 * The Class WebGroupRight.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "web_group_right")
public class WebGroupRight {

	@Id
	@GeneratedValue
	@Column(name = "id_web_group_right", nullable=false)
	private Integer id;

	private String description;
	private int level;
	
	@ManyToMany
	@JoinTable(
			name = "web_group_rights_in_roles",
			joinColumns = @JoinColumn(name= "id_web_group_right", foreignKey = @ForeignKey(name = "FK_WEB_GROUP_RIGHT_OF_WEB_GROUP_RIGHTS_IN_ROLES", foreignKeyDefinition = "FOREIGN KEY (ID_WEB_GROUP_RIGHT) REFERENCES WEB_GROUP_RIGHT(ID_WEB_GROUP_RIGHT) ON DELETE CASCADE")),
			inverseJoinColumns = @JoinColumn(name="id_web_group_role", foreignKey = @ForeignKey(name = "FK_WEB_GROUP_ROLE_OF_WEB_GROUP_RIGHTS_IN_ROLES", foreignKeyDefinition = "FOREIGN KEY (ID_WEB_GROUP_ROLE) REFERENCES WEB_GROUP_ROLE(ID_WEB_GROUP_ROLE) ON DELETE CASCADE"))
		)
	private Set<WebGroupRole> webGroupRoles;

	
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
	public Set<WebGroupRole> getWebGroupRoles() {
		return webGroupRoles;
	}
	/**
	 * Sets the web group roles.
	 * @param webGroupRoles the new web user roles
	 */
	public void setWebGroupRoles(Set<WebGroupRole> webGroupRoles) {
		this.webGroupRoles = webGroupRoles;
	}
	
}
