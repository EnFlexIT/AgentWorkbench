package de.enflexit.awb.ws.core.db.dataModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The Class SiteMenu.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@Entity
@Table(name = "site_menu_content")
public class SiteMenuContent {
	
	@Id
	@ManyToOne
	@JoinColumn(name = "id_site_menu", referencedColumnName = "id_site_menu", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_SITE_MENU) REFERENCES SITE_MENU(ID_SITE_MENU) ON DELETE CASCADE"))
	private SiteMenu siteMenu;

	@Id
	@ManyToOne
	@JoinColumn(name = "id_site_content", referencedColumnName = "id_site_content", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_SITE_CONTENT) REFERENCES SITE_CONTENT(ID_SITE_CONTENT) ON DELETE CASCADE"))
	private SiteContent siteContent;
	
	@Column(name = "content_list_position")
	private int contentListPosition;

	
	/**
	 * Returns the site menu.
	 * @return the siteMenu
	 */
	public SiteMenu getSiteMenu() {
		return siteMenu;
	}
	/**
	 * Sets the site menu.
	 * @param siteMenu the siteMenu to set
	 */
	public void setSiteMenu(SiteMenu siteMenu) {
		this.siteMenu = siteMenu;
	}

	/**
	 * Returns the site content.
	 * @return the siteContent
	 */
	public SiteContent getSiteContent() {
		return siteContent;
	}
	/**
	 * Sets the site content.
	 * @param siteContent the siteContent to set
	 */
	public void setSiteContent(SiteContent siteContent) {
		this.siteContent = siteContent;
	}

	/**
	 * Returns the content list position.
	 * @return the contentListPosition
	 */
	public int getContentListPosition() {
		return contentListPosition;
	}
	/**
	 * Sets the content list position.
	 * @param contentListPosition the contentListPosition to set
	 */
	public void setContentListPosition(int contentListPosition) {
		this.contentListPosition = contentListPosition;
	}

}
