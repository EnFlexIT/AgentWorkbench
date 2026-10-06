package de.enflexit.awb.ws.dynSiteApi.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.enflexit.awb.ws.core.db.dataModel.SiteContent;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentChart;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentImage;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentMedia;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentPropertyEntry;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentPropertyEntry.PropertyValueType;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentText;
import de.enflexit.awb.ws.core.db.dataModel.SiteMenu;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;
import de.enflexit.awb.ws.dynSiteApi.gen.model.MenuItem;
import de.enflexit.awb.ws.dynSiteApi.gen.model.PropertyEntry;
import de.enflexit.awb.ws.dynSiteApi.gen.model.ValueType;

/**
 * The Class TypeConverter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TypeConverter {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	// --------------------------------------------------------------------------------------------
	// --- From here, static help method to handle menu items and lists ---------------------------
	// --------------------------------------------------------------------------------------------	
	/**
	 * Returns the list of menu items derived from the specified list of DB SiteMenu's.
	 *
	 * @param dbSiteMenuList the database site menu list
	 * @return the menu item
	 */
	public static List<MenuItem> toRestMenuItemList(List<SiteMenu> dbSiteMenuList, String language) {
		
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
		menuItem.setMenuId(siteMenu.getId());
		if (siteMenu.getParentMenu()!=null) {
			menuItem.setParentId(siteMenu.getParentMenu().getId());
		}
		
		menuItem.setPosition(siteMenu.getPosition());
		menuItem.setIsHeadMenu(siteMenu.isHeadMenu());
		menuItem.setCaption(siteMenu.getCaption(language));
		menuItem.setPathId(path);
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
	public static SiteMenu toDBSiteMenu(MenuItem menuItem) {
		
		SiteMenu siteMenu = new SiteMenu();
		siteMenu.setCaption(menuItem.getCaption());
		siteMenu.setPosition(menuItem.getPosition());
		siteMenu.setHeadMenu(menuItem.getIsHeadMenu());
		
		// TODO find and set the values which are actually needed.
//		siteMenu.setAccessRightLevel(...); 


		SiteMenu parentMenu = new SiteMenu();
		parentMenu.setId(menuItem.getParentId());
		siteMenu.setParentMenu(parentMenu);
		
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
	public static List<SiteContent> toDBSiteContentList(List<de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent> siteContentList) {
		
		if (siteContentList==null || siteContentList.size()==0) return null;
		
		List<SiteContent> dbSiteContentList =  new ArrayList<>();
		siteContentList.forEach(asc -> {
			SiteContent dbSiteContent = TypeConverter.toDBSiteContent(asc);
			if (dbSiteContent!=null) {
				dbSiteContentList.add(dbSiteContent);	
			}
		});
		return dbSiteContentList;
	}

	/**
	 * To rest content.
	 *
	 * @param siteContent the site content
	 * @return the abstract site content
	 */
	public static AbstractSiteContent toRestContent(de.enflexit.awb.ws.core.db.dataModel.SiteContent siteContent) {
		
		AbstractSiteContent restContent = new AbstractSiteContent();
		
		if (siteContent instanceof de.enflexit.awb.ws.core.db.dataModel.SiteContentText dbText) {
			
			de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText restText = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText();
			restText.setText(dbText.getTextData());
			restContent = restText;
		
		} else if (siteContent instanceof de.enflexit.awb.ws.core.db.dataModel.SiteContentImage dbImage) {
		
			de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage restImage = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage();
			restImage.setDataInB64(dbImage.getTextData());
			restContent = restImage;
			
		} else if (siteContent instanceof de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties dbProperties) {
			
			de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties restProps = TypeConverter.dbToRestPorperties(dbProperties);
			restContent = restProps;
			
		} else if (siteContent instanceof de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithValues dbTableWithValues) {
			
			de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithValues restTableWithValues = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithValues();
			TypeConverter.mapCommonTableFieldsDbToRest(dbTableWithValues, restTableWithValues);
			
			if (dbTableWithValues.getTableDataJson() != null && dbTableWithValues.getTableDataJson().isBlank() == false) {
				try {
					List<List<String>> data = OBJECT_MAPPER.readValue(dbTableWithValues.getTableDataJson(),
							new TypeReference<List<List<String>>>() {
							});
					restTableWithValues.setData(data);
				} catch (JsonProcessingException jpe) {
					jpe.printStackTrace();
				}
			}
			restContent = restTableWithValues;
			
		} else if (siteContent instanceof de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithReference dbTableWithReference) {
		
			de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithReference restTableWithReference = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithReference();
			TypeConverter.mapCommonTableFieldsDbToRest(dbTableWithReference, restTableWithReference);
			restTableWithReference.setReference(dbTableWithReference.getReference());
			restContent = restTableWithReference;
			
		} else if(siteContent instanceof de.enflexit.awb.ws.core.db.dataModel.SiteContentChart dbChart) {
		
			de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentChart restChart = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentChart();
			restChart.setChart(dbChart.getChart());
			restContent = restChart;
		}
		
		if (restContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentMedia scm && siteContent instanceof SiteContentMedia dbScm) {
			scm.setMimeType(dbScm.getMimeType());
		}
		
		restContent.setUniqueContentId(siteContent.getId());
		restContent.setEditable(siteContent.isEditable());
		restContent.setUpdatePeriodInSeconds(siteContent.getUpdatePeriodInSeconds());
		
		return restContent;
		
	}

	/**
	 * Converts the passed rest content to de.enflexit.awb.ws.core.db.dataModel.SiteContent (db SiteContent)
	 *
	 * @param siteContent the site content
	 * @return the site content
	 */
	public static SiteContent toDBSiteContent(de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent siteContent) {
		
		if (siteContent == null) {
			return null;
		}
		
		SiteContent dbSiteContent = null;
		
		if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText scText) {
			SiteContentText dbText = new de.enflexit.awb.ws.core.db.dataModel.SiteContentText();
			dbText.setTextData(scText.getText());
			dbSiteContent = dbText;
		
		} else if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage scImage) {
			SiteContentImage dbImage = new de.enflexit.awb.ws.core.db.dataModel.SiteContentImage();
			dbImage.setTextData(scImage.getDataInB64());
			dbSiteContent = dbImage;
			
		} else if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties scProperties) {
			SiteContentProperties dbProps = TypeConverter.restToDbProperties(scProperties);
			dbSiteContent = dbProps;
			
		} else if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithValues scTableWithValues) {

			de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithValues dbTableWithValues = new de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithValues();
			TypeConverter.mapCommonTableFieldsRestToDb(scTableWithValues, dbTableWithValues);
			
			if (scTableWithValues.getData() != null) {
				try {
					String tableDataJson = OBJECT_MAPPER.writeValueAsString(scTableWithValues.getData());
					dbTableWithValues.setTableDataJson(tableDataJson);
				} catch (JsonProcessingException jpe) {
					jpe.printStackTrace();
				}
			}
			
			dbSiteContent = dbTableWithValues;
				
		} else if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithReference scTableWithReference) {
		
			de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithReference dbTableWithReference = new de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithReference();	
			
			TypeConverter.mapCommonTableFieldsRestToDb(scTableWithReference, dbTableWithReference);
			dbTableWithReference.setReference(scTableWithReference.getReference());
			dbSiteContent = dbTableWithReference;
			
		} else if (siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentChart scChart) {
			de.enflexit.awb.ws.core.db.dataModel.SiteContentChart dbChart = new SiteContentChart();
			dbChart.setChart(scChart.getChart());
			dbSiteContent = dbChart;
		}
		
		
		if (dbSiteContent instanceof SiteContentMedia scm && siteContent instanceof de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentMedia restScm ) {
			scm.setMimeType(restScm.getMimeType());
		}
		
		// --- Set common fields ------------------------------------------------------------------
		dbSiteContent.setId(siteContent.getUniqueContentId());
		dbSiteContent.setEditable(siteContent.getEditable());
		dbSiteContent.setUpdatePeriodInSeconds(siteContent.getUpdatePeriodInSeconds());
		
		return dbSiteContent;
	}

	private static void mapCommonTableFieldsRestToDb(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTable restTable, de.enflexit.awb.ws.core.db.dataModel.SiteContentTable dbTable) {
		
		dbTable.setHeader(restTable.getHeader());
		
		if (restTable.getDataType() != null) {
			List<String> dataTypes = new ArrayList<String>();
			restTable.getDataType().forEach(dtype -> dataTypes.add(dtype.getValue()));
			dbTable.setDataType(dataTypes);
		}
		
	}

	private static void mapCommonTableFieldsDbToRest(de.enflexit.awb.ws.core.db.dataModel.SiteContentTable dbTable, de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTable restTable) {
	
		restTable.setHeader(dbTable.getHeader());
		
		if (dbTable.getDataType() != null) {
			List<ValueType> dataTypes = new ArrayList<ValueType>();
			dbTable.getDataType().forEach(dType -> dataTypes.add(ValueType.fromValue(dType)));
			restTable.setDataType(dataTypes);
		}		
	}
	
	/**
	 * Converts de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties to de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties (db props to rest props)
	 *
	 * @param dbproperties the dbproperties
	 * @return the de.enflexit.awb.ws.dyn site api.gen.model. site content properties
	 */
	private static de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties dbToRestPorperties(de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties dbproperties){
		
		de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties restProps = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties();
		if (dbproperties == null) return restProps;
		
		for (SiteContentPropertyEntry dbEntry : dbproperties.getPropertyEntries()) {
			
			PropertyEntry restEntry = new PropertyEntry();
			restEntry.setKey(dbEntry.getPropertyKey());
			restEntry.setValue(dbEntry.getPropertyValue());
			
			switch (dbEntry.getValueType()) {
			
			case BOOLEAN:
				restEntry.setValueType(ValueType.BOOLEAN);
				break;
			case DOUBLE:
				restEntry.setValueType(ValueType.DOUBLE);
				break;
			case INTEGER:
				restEntry.setValueType(ValueType.INTEGER);
				break;
			case LONG:
				restEntry.setValueType(ValueType.LONG);
				break;
			case STRING:
				restEntry.setValueType(ValueType.STRING);
				break;
			default:
				break;
			}
			restProps.addPropertyEntriesItem(restEntry);
		}
		return restProps;
	}
	
	/**
	 * Converts de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties to de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties.
	 *
	 * @param restProperties the rest properties
	 * @return the de.enflexit.awb.ws.core.db.data model. site content properties
	 */
	private static de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties restToDbProperties(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties restProperties){
		
		de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties dbProps = new SiteContentProperties();
		if (restProperties == null) return dbProps;
		
		for (PropertyEntry restEntry : restProperties.getPropertyEntries()) {
			
			SiteContentPropertyEntry dbEntry = new SiteContentPropertyEntry();
			dbEntry.setPropertyKey(restEntry.getKey());
			dbEntry.setPropertyValue(restEntry.getValue());
			switch (restEntry.getValueType()) {
			case BOOLEAN:
				dbEntry.setValueType(PropertyValueType.BOOLEAN);
				break;
			case DOUBLE:
				dbEntry.setValueType(PropertyValueType.DOUBLE);
				break;
			case INTEGER:
				dbEntry.setValueType(PropertyValueType.INTEGER);
				break;
			case LONG:
				dbEntry.setValueType(PropertyValueType.LONG);
				break;
			case STRING:
				dbEntry.setValueType(PropertyValueType.STRING);
				break;
			default:
				break;
			
			}
			dbProps.getPropertyEntries().add(dbEntry);
		}
		return dbProps;
	}
	
}