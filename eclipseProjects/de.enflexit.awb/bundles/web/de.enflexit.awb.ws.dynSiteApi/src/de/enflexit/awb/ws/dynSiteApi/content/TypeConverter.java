package de.enflexit.awb.ws.dynSiteApi.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.enflexit.awb.ws.core.db.dataModel.SiteContent;
import de.enflexit.awb.ws.core.db.dataModel.SiteMenu;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;

/**
 * The Class TypeConverter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TypeConverter {

	// --------------------------------------------------------------------------------------------
	// --- From here, static help method to handle menu items and lists ---------------------------
	// --------------------------------------------------------------------------------------------	
	/**
	 * Returns the list of menu items derived from the specified list of DB SiteMenu's.
	 *
	 * @param dbSiteMenuList the database site menu list
	 * @return the menu item
	 */
	public static List<MenuItem> getMenuItemList(List<SiteMenu> dbSiteMenuList, String language) {
		
		// --- Fast exit ? --------------------------------
		if (dbSiteMenuList==null) return null;
		if (dbSiteMenuList.size()==0) return new ArrayList<>();
		
		// --- Place everything into a HashMap ------------
		HashMap<Integer, SiteMenu> siteMenuHashMap = new HashMap<>();
		dbSiteMenuList.forEach(sm -> siteMenuHashMap.put(sm.getId(), sm));
		
		// --- Run through the list of SiteMenu -----------
		int maxDepth = 0;
		List<MenuItem> restMenuItemList = new ArrayList<>();
		for (SiteMenu dbSiteMenu : dbSiteMenuList) {

			MenuPathDescriptor mpd = TypeConverter.getPathDescription(dbSiteMenu, siteMenuHashMap, language);
			maxDepth = Math.max(maxDepth, mpd.getDepth());

			MenuItem restMenuItem = TypeConverter.getMenuItem(dbSiteMenu, language, mpd.getPathID(), mpd.getPathCaption(), mpd.getPathPosition());
			restMenuItemList.add(restMenuItem);
		}
		
		// --- Check path position ------------------------ 
		for (MenuItem mi : restMenuItemList) {
			String pathPos = mi.getPathPosition();
			String[] pathPosParts = pathPos.split("/");
			int noParts = pathPosParts.length;
			if (noParts < maxDepth) {
				for (int i = noParts; i < maxDepth; i++) {
					pathPos += "/00";
				}
				mi.setPathPosition(pathPos);
			}
		}
		
		// --- Sort menu as they should appear ------------
		Collections.sort(restMenuItemList, new Comparator<MenuItem>() {
			@Override
			public int compare(MenuItem mi1, MenuItem mi2) {
				return mi1.getPathPosition().compareTo(mi2.getPathPosition());
			}
		});
		
		return restMenuItemList;
	}
	/**
	 * Returns the path for the specified SiteMenu, derived from the HashMap .
	 *
	 * @param siteMenu the site menu
	 * @param siteMenuHashMap the site menu hash map
	 * @param language the language
	 * @return the MenuPathDescriptor
	 */
	private static MenuPathDescriptor getPathDescription(SiteMenu siteMenu, HashMap<Integer, SiteMenu> siteMenuHashMap, String language) {
		
		if (siteMenu==null) return null;
		
		SiteMenu siteMenuWork = siteMenu; 

		ArrayList<String> pathIDList = new ArrayList<>();
		ArrayList<String> pathCaptionList = new ArrayList<>();
		ArrayList<String> pathPostionList = new ArrayList<>();
		int depth = 1;
		
		pathIDList.add(String.valueOf(siteMenuWork.getId()));
		pathCaptionList.add(siteMenuWork.getCaption(language));
		pathPostionList.add(String.format("%02d", siteMenuWork.getPosition()));
		
		while (siteMenuWork.getParentMenu()!=null) {
			siteMenuWork = siteMenuWork.getParentMenu();
			pathIDList.add(String.valueOf(siteMenuWork.getId()));	
			pathCaptionList.add(siteMenuWork.getCaption(language));
			pathPostionList.add(String.format("%02d", siteMenuWork.getPosition()));
			depth++;
		}

		Collections.reverse(pathIDList);
		Collections.reverse(pathCaptionList);
		Collections.reverse(pathPostionList);
		
		String pathID = String.join("/", pathIDList);
		String pathCaption = String.join(" / ", pathCaptionList);  
		String pathPosition = String.join("/", pathPostionList);
		
		MenuPathDescriptor mpd = new MenuPathDescriptor();
		mpd.setPathID(pathID);
		mpd.setPathCaption(pathCaption);
		mpd.setPathPosition(pathPosition);
		mpd.setDepth(depth);
		
		return mpd;
	}
	
	/**
	 * Returns the REST MenuItem based on the specified DB SiteMenu
	 * without setting the path or the parentID.
	 *
	 * @param siteMenu the site menu
	 * @param language the language
	 * @param path the path
	 * @param pathCaption the path caption
	 * @param pathPosition the path position
	 * @return the menu item
	 */
	private static MenuItem getMenuItem(SiteMenu siteMenu, String language, String path, String pathCaption, String pathPosition) {
		
		MenuItem menuItem = new MenuItem();
		menuItem.setMenuID(siteMenu.getId());
		if (siteMenu.getParentMenu()!=null) {
			menuItem.setParentID(siteMenu.getParentMenu().getId());
		}
		
		menuItem.setPosition(siteMenu.getPosition());
		menuItem.setIsHeadMenu(siteMenu.isHeadMenu());
		menuItem.setCaption(siteMenu.getCaption(language));
		menuItem.setPathID(path);
		menuItem.setPathCaption(pathCaption);
		menuItem.setPathPosition(pathPosition);
		return menuItem;
	}
	
	/**
	 * Returns the DB SiteMenu based on the specified REST MenuItem
	 *
	 * @param menuItem the menu item
	 * @return the site menu
	 */
	public static SiteMenu getDBSiteMenu(MenuItem menuItem) {
		
		SiteMenu siteMenu = new SiteMenu();
		
		
		return siteMenu;
	}
	

	// --------------------------------------------------------------------------------------------
	// --- From here, static help method to handle site content -----------------------------------
	// --------------------------------------------------------------------------------------------	
	/**
	 * Returns the specified list to a database site content list.
	 *
	 * @param siteContentList the site content list
	 * @return the DB site content list
	 */
	public static List<SiteContent> getDBSiteContentList(List<de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent> siteContentList) {
		
		if (siteContentList==null || siteContentList.size()==0) return null;
		
		List<SiteContent> dbSiteContentList =  new ArrayList<>();
		siteContentList.forEach(asc -> {
			SiteContent dbSiteContent = TypeConverter.getDBSiteContent(asc);
			if (dbSiteContent!=null) {
				dbSiteContentList.add(dbSiteContent);	
			}
		});
		return dbSiteContentList;
	}
	
	/**
	 * Returns the specified list to a database site content list.
	 *
	 * @param siteContent the site content
	 * @return the DB site content list
	 */
	public static SiteContent getDBSiteContent(de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent siteContent) {
		
		SiteContent dbSiteContent = null;
		
		if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText scText) {
			dbSiteContent = new de.enflexit.awb.ws.core.db.dataModel.SiteContentText();
			
		} else if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage scImage) {

		} else if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties scProperties) {

		} else if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTable scTable) {
			
		}
		
		return dbSiteContent;
	}
	
}
