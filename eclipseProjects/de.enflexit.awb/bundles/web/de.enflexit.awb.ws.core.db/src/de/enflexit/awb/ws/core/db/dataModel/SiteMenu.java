package de.enflexit.awb.ws.core.db.dataModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.enflexit.db.userManagement.dataModel.GroupRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * The Class SiteMenu.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "site_menu")
public class SiteMenu implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
		
	@OneToMany(mappedBy = "siteMenu")
	private Set<SiteMenuTranslation> translations;
	@Transient
	private HashMap<String, String> translationHashMap;
	
	@OneToMany(mappedBy = "siteMenu")
	private Set<SiteMenuContent> siteMenuContent;

	@ManyToMany
    @JoinTable(
        name = "site_menu_group_roles", 
        joinColumns = @JoinColumn(name = "id_site_menu"), 
        inverseJoinColumns = @JoinColumn(name = "id_group_role")
    )
    private Set<GroupRole> allowedGroupRoles = new HashSet<>();
	
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
	 * Returns the caption in the designate language.
	 *
	 * @param language the language
	 * @return the caption
	 */
	public String getCaption(String language) {
		
		if (language.toLowerCase().equals("en")==true) return this.getCaption();
		
		String translation = this.getTranslationHashMap().get(language.toLowerCase());
		return translation==null ? this.getCaption() : translation;
	}
	/**
	 * Returns a hash map that enable faster translations.
	 * @return the translation hash map
	 */
	private HashMap<String, String> getTranslationHashMap() {
		if (translationHashMap==null) {
			translationHashMap = new HashMap<>();
			this.getTranslations().forEach(smt -> translationHashMap.put(smt.getLocale().toLowerCase(), smt.getCaptionTranslated()));
		}
		return translationHashMap;
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
	public Set<SiteMenuContent> getSiteMenuContent() {
		return siteMenuContent;
	}
	/**
	 * Sets the site content menu.
	 * @param siteMenuContent the new site content menu
	 */
	public void setSiteMenuContent(Set<SiteMenuContent> siteContentMenu) {
		this.siteMenuContent = siteContentMenu;
	}
	
	public Set<GroupRole> getAllowedGroupRoles() {
        return allowedGroupRoles;
    }

    public void setAllowedGroupRoles(Set<GroupRole> allowedGroupRoles) {
        this.allowedGroupRoles = allowedGroupRoles;
    }
	
}
