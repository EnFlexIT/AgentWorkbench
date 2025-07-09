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
@Table(name = "site_menu_translation")
public class SiteMenuTranslation {

	@Id
	@ManyToOne
	@JoinColumn(name = "id_site_menu", referencedColumnName = "id_site_menu", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (ID_SITE_MENU) REFERENCES SITE_MENU(ID_SITE_MENU) ON DELETE CASCADE"))
	private SiteMenu siteMenu;
	
	@Id
	private String locale;
	
	@Column(name = "caption_translated")
	private String captionTranslated;
	

	/**
	 * Returns the locale.
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}
	/**
	 * Sets the locale.
	 * @param locale the new locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
	 * Returns the translation.
	 * @return the translation
	 */
	public String getCaptionTranslated() {
		return captionTranslated;
	}
	/**
	 * Sets the translation.
	 * @param translation the new translation
	 */
	public void setCaptionTranslated(String captionTranslated) {
		this.captionTranslated = captionTranslated;
	}
	
}
