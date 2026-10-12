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
import de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentPropertyEntry;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentPropertyEntry.PropertyValueType;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithReference;
import de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithValues;
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
		if (dbSiteMenuList==null || dbSiteMenuList.size() == 0) return Collections.emptyList();
		
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

		SiteMenu parentMenu = new SiteMenu();
		parentMenu.setId(menuItem.getParentId());
		siteMenu.setParentMenu(parentMenu);
		
		return siteMenu;
	}
	
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
			SiteContent dbSiteContent = TypeConverter.restToDbContent(asc);
			if (dbSiteContent!=null) {
				dbSiteContentList.add(dbSiteContent);	
			}
		});
		return dbSiteContentList;
	}	

	// --------------------------------------------------------------------------------------------
	// --- From here, static help methods to map DB to REST content -------------------------------
	// --------------------------------------------------------------------------------------------	


	/**
	 * Db to rest content.
	 *
	 * @param dbContent the site content
	 * @return the abstract site content
	 */
	public static AbstractSiteContent dbToRestContent(de.enflexit.awb.ws.core.db.dataModel.SiteContent dbContent) {
		
		if (dbContent == null) return null;
		
		AbstractSiteContent restContent = switch (dbContent) {
		case SiteContentText dbText -> mapTextDbToRest(dbText);
		case SiteContentImage dbImage -> mapImageDbToRest(dbImage);
		case SiteContentProperties dbProps -> mapPropertiesDbToRest(dbProps);
        case SiteContentTableWithValues dbTableVal -> mapTableValuesDbToRest(dbTableVal);
        case SiteContentTableWithReference dbTableRef -> mapTableRefDbToRest(dbTableRef);
        case SiteContentChart dbChart -> mapChartDbToRest(dbChart);
		default -> throw new IllegalArgumentException("Unexpected value: " + dbContent);
		};
		
		mapCommonFieldsDbToRest(dbContent, restContent);
		
		return restContent;
	}
	
	/**
	 * Map common fields db to rest.
	 *
	 * @param siteContent the site content
	 * @param restContent the rest content
	 */
	private static void mapCommonFieldsDbToRest(SiteContent siteContent, AbstractSiteContent restContent) {
		
		restContent.setUniqueContentId(siteContent.getId());
		restContent.setEditable(siteContent.isEditable());
		restContent.setUpdatePeriodInSeconds(siteContent.getUpdatePeriodInSeconds());
		
	}
	
	/**
	 * Map chart db to rest.
	 *
	 * @param dbChart the db chart
	 * @return the de.enflexit.awb.ws.dyn site api.gen.model. site content chart
	 */
	private static de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentChart mapChartDbToRest(SiteContentChart dbChart) {
	
		de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentChart restChart = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentChart();
		restChart.setChart(dbChart.getChart());
		return restChart;
	}
	
	/**
	 * Map table ref db to rest.
	 *
	 * @param dbTableRef the db table ref
	 * @return the de.enflexit.awb.ws.dyn site api.gen.model. site content table with reference
	 */
	private static de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithReference mapTableRefDbToRest(SiteContentTableWithReference dbTableRef) {
	
		de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithReference restTableWithReference = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithReference();
		TypeConverter.mapCommonTableFieldsDbToRest(dbTableRef, restTableWithReference);
		restTableWithReference.setReference(dbTableRef.getReference() == null ? "" : dbTableRef.getReference());
		return restTableWithReference;
	}
	
	/**
	 * Map table values db to rest.
	 *
	 * @param dbTableVal the db table val
	 * @return the de.enflexit.awb.ws.dyn site api.gen.model. site content table with values
	 */
	private static de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithValues mapTableValuesDbToRest(SiteContentTableWithValues dbTableVal) {
	
		de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithValues restTableWithValues = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithValues();
		TypeConverter.mapCommonTableFieldsDbToRest(dbTableVal, restTableWithValues);
		
		if (dbTableVal.getTableDataJson() != null && dbTableVal.getTableDataJson().isBlank() == false) {
			try {
				List<List<String>> data = OBJECT_MAPPER.readValue(dbTableVal.getTableDataJson(),
						new TypeReference<List<List<String>>>() {});
				restTableWithValues.setData(data);
			} catch (JsonProcessingException jpe) {
				jpe.printStackTrace();
			}
		}
		return restTableWithValues;
	}

	/**
	 * Map common table fields db to rest.
	 *
	 * @param dbTable the db table
	 * @param restTable the rest table
	 */
	private static void mapCommonTableFieldsDbToRest(de.enflexit.awb.ws.core.db.dataModel.SiteContentTable dbTable, de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTable restTable) {
	
		restTable.setHeader(dbTable.getHeader());
		
		if (dbTable.getDataType() != null) {
			List<ValueType> dataTypes = new ArrayList<ValueType>();
			dbTable.getDataType().forEach(dType -> dataTypes.add(ValueType.fromValue(dType)));
			restTable.setDataType(dataTypes);
		}		
	}
	
	/**
	 * Map image db to rest.
	 *
	 * @param dbImage the db image
	 * @return the de.enflexit.awb.ws.dyn site api.gen.model. site content image
	 */
	private static de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage mapImageDbToRest(SiteContentImage dbImage) {
		
		de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage restImage = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage();
		restImage.setDataInB64(dbImage.getTextData() == null ? "" : dbImage.getTextData());
		restImage.setMimeType(dbImage.getMimeType() == null ? "" : dbImage.getMimeType());
		return restImage;
	}
	
	/**
	 * Map text db to rest.
	 *
	 * @param dbText the db text
	 * @return the de.enflexit.awb.ws.dyn site api.gen.model. site content text
	 */
	private static de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText mapTextDbToRest(SiteContentText dbText) {
		
		de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText restText = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText();
		restText.setText(dbText.getTextData() == null ? "" : dbText.getTextData());
		restText.setMimeType(dbText.getMimeType()== null ? "" : dbText.getMimeType());
		return restText;
	}

	/**
	 * Converts de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties to de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties (db props to rest props)
	 *
	 * @param dbproperties the dbproperties
	 * @return the de.enflexit.awb.ws.dyn site api.gen.model. site content properties
	 */
	private static de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties mapPropertiesDbToRest(de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties dbproperties){
		
		de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties restProps = new de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties();
		if (dbproperties == null || dbproperties.getPropertyEntries() == null) return restProps;
		
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
	
	// --------------------------------------------------------------------------------------------
	// --- From here, static help methods to map REST to DB content -------------------------------
	// --------------------------------------------------------------------------------------------	

	/**
	 * Rest to db content.
	 *
	 * @param restContent the rest content
	 * @return the de.enflexit.awb.ws.core.db.data model. site content
	 */
	public static de.enflexit.awb.ws.core.db.dataModel.SiteContent restToDbContent(AbstractSiteContent restContent) throws IllegalArgumentException {
		
		if (restContent == null) return null;
		
		SiteContent dbContent = switch (restContent) {
		case de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText restText -> mapTextRestToDb(restText);
		case de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage restImage -> mapImageRestToDb(restImage);
		case de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties restProps -> mapPropertiesRestToDb(restProps);
        case de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithValues restTableVal -> mapTableValuesRestToDb(restTableVal);
        case de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithReference restTableRef -> mapTableRefRestToDb(restTableRef);
        case de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentChart restChart -> mapChartRestToDb(restChart);
		default -> throw new IllegalArgumentException("Unexpected value: " + restContent);
		};
		
		mapCommonFieldsRestToDb(restContent, dbContent);
		
		return dbContent;
	}
	
	/**
	 * Map common fields rest to db.
	 *
	 * @param restContent the rest content
	 * @param dbContent the db content
	 */
	private static void mapCommonFieldsRestToDb(AbstractSiteContent restContent, SiteContent dbContent) throws IllegalArgumentException {
		
		if (restContent.getEditable() == null || restContent.getUpdatePeriodInSeconds() == null) {
			throw new IllegalArgumentException("Update period and editable can't be null");	
		}
		
		dbContent.setId(restContent.getUniqueContentId());
		dbContent.setEditable(restContent.getEditable());
		dbContent.setUpdatePeriodInSeconds(restContent.getUpdatePeriodInSeconds());
		
	}
	
	/**
	 * Map chart rest to db.
	 *
	 * @param restChart the rest chart
	 * @return the de.enflexit.awb.ws.core.db.data model. site content chart
	 */
	private static de.enflexit.awb.ws.core.db.dataModel.SiteContentChart mapChartRestToDb(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentChart restChart) throws IllegalArgumentException {
		
		if (restChart.getChart() == null) {
			throw new IllegalArgumentException("Chart of site content chart can't be null.");
		}
		de.enflexit.awb.ws.core.db.dataModel.SiteContentChart dbChart = new SiteContentChart();
		dbChart.setChart(restChart.getChart());
		return dbChart;
	}
	
	/**
	 * Map table ref rest to db.
	 *
	 * @param restTableRef the rest table ref
	 * @return the de.enflexit.awb.ws.core.db.data model. site content table with reference
	 */
	private static de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithReference mapTableRefRestToDb(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithReference restTableRef) throws IllegalArgumentException {
	
		if (restTableRef.getReference() == null || restTableRef.getReference().isBlank()) {
			throw new IllegalArgumentException("The reference of a table with reference can't be null.");
		}
		
		de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithReference dbTableWithReference = new de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithReference();
		TypeConverter.mapCommonTableFieldsRestToDb(restTableRef, dbTableWithReference);
		dbTableWithReference.setReference(restTableRef.getReference());
		return dbTableWithReference;
	}
	
	/**
	 * Map table values rest to db.
	 *
	 * @param restTableVal the rest table val
	 * @return the de.enflexit.awb.ws.core.db.data model. site content table with values
	 */
	private static de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithValues mapTableValuesRestToDb(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTableWithValues restTableVal) throws IllegalArgumentException {
		
		de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithValues dbTableWithValues = new de.enflexit.awb.ws.core.db.dataModel.SiteContentTableWithValues();
		TypeConverter.mapCommonTableFieldsRestToDb(restTableVal, dbTableWithValues);
		
		if (restTableVal.getData() != null) {
			try {
				String tableDataJson = OBJECT_MAPPER.writeValueAsString(restTableVal.getData());
				dbTableWithValues.setTableDataJson(tableDataJson);
			} catch (JsonProcessingException jpe) {
				jpe.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("Data of table with data can't be null.");
		}
		return dbTableWithValues;
	}
	
	/**
	 * Map image rest to db.
	 *
	 * @param restImage the rest image
	 * @return the de.enflexit.awb.ws.core.db.data model. site content image
	 */
	private static de.enflexit.awb.ws.core.db.dataModel.SiteContentImage mapImageRestToDb(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentImage restImage) throws IllegalArgumentException {
		
		if (restImage.getMimeType() == null || restImage.getMimeType().isBlank()) {
			throw new IllegalArgumentException("Mime type of an image can't be null.");
		}
		if (restImage.getDataInB64() == null || restImage.getDataInB64().isBlank()) {
			throw new IllegalArgumentException("Data for an image can't be null");
		}
		
		SiteContentImage dbImage = new de.enflexit.awb.ws.core.db.dataModel.SiteContentImage();
		dbImage.setTextData(restImage.getDataInB64());
		dbImage.setMimeType(restImage.getMimeType());
		return dbImage;
	}
	
	/**
	 * Map text rest to db.
	 *
	 * @param restText the rest text
	 * @return the de.enflexit.awb.ws.core.db.data model. site content text
	 */
	private static de.enflexit.awb.ws.core.db.dataModel.SiteContentText mapTextRestToDb(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentText restText) throws IllegalArgumentException {

		if (restText.getMimeType() == null || restText.getMimeType().isBlank()) {
			throw new IllegalArgumentException("Mime type of text can't be null.");
		}
		
		SiteContentText dbText = new de.enflexit.awb.ws.core.db.dataModel.SiteContentText();
		dbText.setTextData(restText.getText());
		dbText.setMimeType(restText.getMimeType());
		return dbText;
	}


	/**
	 * Map common table fields rest to db.
	 *
	 * @param restTable the rest table
	 * @param dbTable the db table
	 */
	private static void mapCommonTableFieldsRestToDb(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentTable restTable, de.enflexit.awb.ws.core.db.dataModel.SiteContentTable dbTable) throws IllegalArgumentException {
		
		if (restTable.getHeader() == null || restTable.getHeader().size() == 0) {
			throw new IllegalArgumentException("At least one header must be specified for a table.");
		}
		
		if (restTable.getDataType() == null || restTable.getDataType().size() == 0) {
			throw new IllegalArgumentException("At least one data type must be specified for a table");
		}
		
		dbTable.setHeader(restTable.getHeader());

		List<String> dataTypes = new ArrayList<String>();
		restTable.getDataType().forEach(dtype -> dataTypes.add(dtype.getValue()));
		dbTable.setDataType(dataTypes);

	}


	
	/**
	 * Converts de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties to de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties.
	 *
	 * @param restProperties the rest properties
	 * @return the de.enflexit.awb.ws.core.db.data model. site content properties
	 */
	private static de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties mapPropertiesRestToDb(de.enflexit.awb.ws.dynSiteApi.gen.model.SiteContentProperties restProperties) throws IllegalArgumentException {
		
		de.enflexit.awb.ws.core.db.dataModel.SiteContentProperties dbProps = new SiteContentProperties();
		if (restProperties == null || restProperties.getPropertyEntries() == null) return dbProps;
		
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