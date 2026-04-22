package de.enflexit.df.core.workbook;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.enflexit.common.dataSources.AbstractDataSource;
import de.enflexit.common.dataSources.CsvDataSource;
import de.enflexit.common.dataSources.DatabaseDataSource;
import de.enflexit.common.dataSources.ExcelDataSource;

/**
 * The Class JsonAdapterForDataSource.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JsonAdapterForDataSource implements JsonDeserializer<AbstractDataSource>, JsonSerializer<AbstractDataSource> {

	private static final String TYPE_KEY = "type";

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public AbstractDataSource deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		JsonObject jObject = json.getAsJsonObject();
		String type = jObject.get(TYPE_KEY).getAsString();
		
		AbstractDataSource ds = null;
		if (type.toLowerCase().equals(CsvDataSource.class.getSimpleName().toLowerCase())==true) {
			ds = context.deserialize(json, CsvDataSource.class);
		} else if (type.toLowerCase().equals(ExcelDataSource.class.getSimpleName().toLowerCase())==true) {
			ds = context.deserialize(json, ExcelDataSource.class);
		} else if (type.toLowerCase().equals(DatabaseDataSource.class.getSimpleName().toLowerCase())==true) {
			ds = context.deserialize(json, DatabaseDataSource.class);
		}
		return ds;
	}

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(AbstractDataSource src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jObject = (JsonObject) context.serialize(src, src.getClass());
		jObject.addProperty(TYPE_KEY, src.getClass().getSimpleName());
		return jObject;
	}

}
