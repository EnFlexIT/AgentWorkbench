package de.enflexit.awb.samples.db.dataModel;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The Class WebUser.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "ex_web_user")
public class WebUser {

	@Id
	@GeneratedValue
	@Column(name="id_web_user", nullable=false)
	private Integer id;
	
	@Column(name="sso_id", unique = true, nullable=false)
	private String sso_id;
	
	@Column(name = "preferred_username")
	private String preferredUsername;

	private String name;
	private String givenName;
	private String familyName;

	private String email;
	@Column(name = "email_is_verified")
	private boolean email_verified;

	private String locale;

	@Column(name = "date_first_access")
	private Instant dateFirstAccess;
	@Column(name = "date_last_access")
	private Instant dateLastAccess;
	
	@ManyToMany
	@JoinTable(
		name = "ex_web_users_in_roles",
		joinColumns = @JoinColumn(name= "id_web_user", foreignKey = @ForeignKey(name="FK_WEB_USER", foreignKeyDefinition = "FOREIGN KEY (ID_WEB_USER) REFERENCES EX_WEB_USER(ID_WEB_USER) ON DELETE CASCADE")),
		inverseJoinColumns = @JoinColumn(name="id_web_role", foreignKey = @ForeignKey(name="FK_WEBÛSER__ROLE", foreignKeyDefinition = "FOREIGN KEY (ID_WEB_ROLE) REFERENCES EX_WEB_USER_ROLE(ID_WEB_USER_ROLE) ON DELETE CASCADE"))
	)
	private Set<WebUserRole> userRoles;
	
	@OneToMany(mappedBy = "webUser")
	private Set<WebUserGroupRole> webUserGroupRoles;
	
	
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
	 * Returns the SSO ID (e.g. from KeyCloak).
	 * @return the SSO ID
	 */
	public String getSSO_ID() {
		return sso_id;
	}
	/**
	 * Sets the SSO ID (e.g. from KeyCloak).
	 * @param sso_id the new SSO ID
	 */
	public void setSSO_ID(String sso_id) {
		this.sso_id = sso_id;
	}
	
	/**
	 * Gets the preferred user name.
	 * @return the preferredUsername
	 */
	public String getPreferredUsername() {
		return preferredUsername;
	}
	/**
	 * Sets the preferred username.
	 * @param preferredUsername the preferredUsername to set
	 */
	public void setPreferredUsername(String preferredUsername) {
		this.preferredUsername = preferredUsername;
	}
	
	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the given name.
	 * @return the givenName
	 */
	public String getGivenName() {
		return givenName;
	}
	/**
	 * Sets the given name.
	 * @param givenName the givenName to set
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/**
	 * Gets the family name.
	 * @return the familyName
	 */
	public String getFamilyName() {
		return familyName;
	}
	/**
	 * Sets the family name.
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	/**
	 * Gets the email.
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Sets the email.
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Checks if is email verified.
	 * @return the email_verified
	 */
	public boolean isEmail_verified() {
		return email_verified;
	}
	/**
	 * Sets the email verified.
	 * @param email_verified the email_verified to set
	 */
	public void setEmail_verified(boolean email_verified) {
		this.email_verified = email_verified;
	}

	/**
	 * Gets the locale.
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}
	/**
	 * Sets the locale.
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
	 * Returns the first access date.
	 * @return the first access
	 */
	public Instant getDateFirstAccess() {
		return dateFirstAccess;
	}
	/**
	 * Sets the first access date.
	 * @param dateFirstAccess the new first access
	 */
	public void setDateFirstAccess(Instant firstAccess) {
		this.dateFirstAccess = firstAccess;
	}
	
	/**
	 * Returns the last access.
	 * @return the last access
	 */
	public Instant getDateLastAccess() {
		return dateLastAccess;
	}
	/**
	 * Sets the last access.
	 * @param dateLastAccess the new last access
	 */
	public void setDateLastAccess(Instant lastAccess) {
		this.dateLastAccess = lastAccess;
	}
	
	
	/**
	 * Returns the userRoles of the current user.
	 * @return the userRoles
	 */
	public Set<WebUserRole> getUserRoles() {
		return userRoles;
	}
	/**
	 * Sets the userRoles of the current user.
	 * @param userRoles the new userRoles
	 */
	public void setUserRoles(Set<WebUserRole> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * Sets the web user group rights.
	 * @param webUserGroupRoles the new web user group rights
	 */
	public void setWebUserGroupRights(Set<WebUserGroupRole> webUserGroupRoles) {
		this.webUserGroupRoles = webUserGroupRoles;
	}
	/**
	 * Returns the web user group rights.
	 * @return the web user group rights
	 */
	public Set<WebUserGroupRole> getWebUserGroupRights() {
		return webUserGroupRoles;
	}
	
}
