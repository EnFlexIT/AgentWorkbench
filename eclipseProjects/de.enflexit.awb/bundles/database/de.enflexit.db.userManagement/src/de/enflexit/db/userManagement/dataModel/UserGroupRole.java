package de.enflexit.db.userManagement.dataModel;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The Class WebUserGroupRole.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "um_user_group_roles")
public class UserGroupRole {

	@Id
	@ManyToOne
	@JoinColumn(name = "id_user", referencedColumnName = "id_user", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_USER) REFERENCES UM_USER(ID_USER) ON DELETE CASCADE"))
	private User user;

	@Id
	@ManyToOne
	@JoinColumn(name = "id_group", referencedColumnName = "id_group", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_GROUP) REFERENCES UM_GROUP(ID_GROUP) ON DELETE CASCADE"))
	private Group group;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "id_group_role", referencedColumnName = "id_group_role", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_GROUP_ROLE) REFERENCES UM_GROUP_ROLE(ID_GROUP_ROLE) ON DELETE CASCADE"))
	private GroupRole groupRole;

	@Column(name = "date_granted")
	private Instant dateGranted;
	
	/**
	 * Gets the user.
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * Sets the user.
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the group.
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}
	/**
	 * Sets the group.
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * Gets the group role.
	 * @return the groupRole
	 */
	public GroupRole getGroupRole() {
		return groupRole;
	}
	/**
	 * Sets the group role.
	 * @param groupRole the groupRole to set
	 */
	public void setGroupRole(GroupRole groupRole) {
		this.groupRole = groupRole;
	}
	
	/**
	 * Returns the date granted.
	 * @return the date granted
	 */
	public Instant getDateGranted() {
		return dateGranted;
	}
	/**
	 * Sets the date granted.
	 * @param dateGranted the new date granted
	 */
	public void setDateGranted(Instant dateGranted) {
		this.dateGranted = dateGranted;
	}
	
}
