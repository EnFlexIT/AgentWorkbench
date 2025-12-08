package de.enflexit.awb.samples.db.dataModel;

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
@Table(name = "ex_web_user_group_roles")
public class WebUserGroupRole {

	@Id
	@ManyToOne
	@JoinColumn(name = "id_web_user", referencedColumnName = "id_web_user", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_WEB_USER) REFERENCES EX_WEB_USER(ID_WEB_USER) ON DELETE CASCADE"))
	private WebUser webUser;

	@Id
	@ManyToOne
	@JoinColumn(name = "id_web_group", referencedColumnName = "id_web_group", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_WEB_GROUP) REFERENCES EX_WEB_GROUP(ID_WEB_GROUP) ON DELETE CASCADE"))
	private WebGroup webGroup;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "id_web_group_role", referencedColumnName = "id_web_group_role", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_WEB_GROUP_ROLE) REFERENCES EX_WEB_GROUP_ROLE(ID_WEB_GROUP_ROLE) ON DELETE CASCADE"))
	private WebGroupRole webGroupRole;

	@Column(name = "date_granted")
	private Instant dateGranted;
	
	
	/**
	 * Gets the web user.
	 * @return the webUser
	 */
	public WebUser getWebUser() {
		return webUser;
	}
	/**
	 * Sets the web user.
	 * @param webUser the webUser to set
	 */
	public void setWebUser(WebUser webUser) {
		this.webUser = webUser;
	}
	
	/**
	 * Gets the web group.
	 * @return the webGroup
	 */
	public WebGroup getWebGroup() {
		return webGroup;
	}
	/**
	 * Sets the web group.
	 * @param webGroup the webGroup to set
	 */
	public void setWebGroup(WebGroup webGroup) {
		this.webGroup = webGroup;
	}

	/**
	 * Gets the web group role.
	 * @return the webGroupRole
	 */
	public WebGroupRole getWebGroupRole() {
		return webGroupRole;
	}
	/**
	 * Sets the web group role.
	 * @param webGroupRole the webGroupRole to set
	 */
	public void setWebGroupRole(WebGroupRole webGroupRole) {
		this.webGroupRole = webGroupRole;
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
