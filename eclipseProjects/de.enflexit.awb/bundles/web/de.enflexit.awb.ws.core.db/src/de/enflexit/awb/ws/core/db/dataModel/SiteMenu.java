package de.enflexit.awb.ws.core.db.dataModel;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The Class SiteMenu.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "site_menu")
public class SiteMenu {

	@Id
	@GeneratedValue
	@Column(name = "id_site_menu")
	private Integer id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "id_site_menu_parent")
	private SiteMenu parentMenu;
	
	@OneToMany
	@JoinColumn(name = "id_site_menu_parent", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_SITE_MENU_PARENT) REFERENCES SITE_MENU(ID_SITE_MENU) ON DELETE CASCADE"))
	private List<SiteMenu> childMenus;
	
	private int position;
	private String caption;

	@Column(name = "is_head_menu")
	private boolean isHeadMenu;

	@Column(name = "access_right_level")
	private int accessRightLevel;
	
	
	@OneToMany(mappedBy = "siteMenu")
	private Set<SiteMenuTranslation> translations;

	@OneToMany(mappedBy = "siteMenu")
	private Set<SiteMenuContent> siteContentMenu;
	
	
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
	 * Returns the parent menu.
	 * @return the parent menu
	 */
	public SiteMenu getParentMenu() {
		return parentMenu;
	}
	/**
	 * Sets the parent menu.
	 * @param parentMenu the new parent menu
	 */
	public void setParentMenu(SiteMenu parentMenu) {
		this.parentMenu = parentMenu;
	}
	
	/**
	 * Returns the child menu.
	 * @return the childMenus
	 */
	public List<SiteMenu> getChildMenus() {
		return childMenus;
	}
	/**
	 * Sets the child menu.
	 * @param childMenus the childMenus to set
	 */
	public void setChildMenu(List<SiteMenu> childMenus) {
		this.childMenus = childMenus;
	}

	/**
	 * Returns the position.
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * Sets the position.
	 * @param position the new position
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Returns the caption.
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * Sets the caption.
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Checks if is head menu.
	 * @return true, if is head menu
	 */
	public boolean isHeadMenu() {
		return isHeadMenu;
	}
	/**
	 * Sets the head menu.
	 * @param isHeadMenu the new head menu
	 */
	public void setHeadMenu(boolean isHeadMenu) {
		this.isHeadMenu = isHeadMenu;
	}
	
	/**
	 * Returns the access right level.
	 * @return the access right level
	 */
	public int getAccessRightLevel() {
		return accessRightLevel;
	}
	/**
	 * Sets the access right level.
	 * @param accessRightLevel the new access right level
	 */
	public void setAccessRightLevel(int accessRightLevel) {
		this.accessRightLevel = accessRightLevel;
	}
	
	/**
	 * Returns the translations.
	 * @return the translations
	 */
	public Set<SiteMenuTranslation> getTranslations() {
		return translations;
	}
	/**
	 * Sets the translations.
	 * @param translations the new translations
	 */
	public void setTranslations(Set<SiteMenuTranslation> translations) {
		this.translations = translations;
	}
	
	/**
	 * Return the site content menu.
	 * @return the site content menu
	 */
	public Set<SiteMenuContent> getSiteContentMenu() {
		return siteContentMenu;
	}
	/**
	 * Sets the site content menu.
	 * @param siteContentMenu the new site content menu
	 */
	public void setSiteContentMenu(Set<SiteMenuContent> siteContentMenu) {
		this.siteContentMenu = siteContentMenu;
	}
	
}
