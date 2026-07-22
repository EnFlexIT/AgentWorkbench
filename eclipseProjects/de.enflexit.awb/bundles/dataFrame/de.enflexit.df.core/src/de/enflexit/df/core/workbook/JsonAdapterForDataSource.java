package de.enflexit.df.core.workbook;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.enflexit.common.NumberHelper;
import de.enflexit.db.dataSources.DefaultDataSource;

/**
 * The Class JsonAdapterForDataSource.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JsonAdapterForDataSource extends TypeAdapter<DefaultDataSource>{

	/* (non-Javadoc)
	 * @see com.google.gson.TypeAdapter#write(com.google.gson.stream.JsonWriter, java.lang.Object)
	 */
	@Override
	public void write(JsonWriter out, DefaultDataSource absDS) throws IOException {
		
		if (absDS == null) {
            out.nullValue();
            return;
        }

		out.beginObject();
		out.name("id").value(absDS.getId());
		out.name("name").value(absDS.getName());
		out.name("description").value(absDS.getDescription());
		
		out.name("rowsPerPage").value(absDS.getRowsPerPage());
		out.name("storageConfiguration").value(absDS.getStorageConfiguration());
		
		out.endObject();
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.TypeAdapter#read(com.google.gson.stream.JsonReader)
	 */
	@Override
	public DefaultDataSource read(JsonReader in) throws IOException {

		DefaultDataSource absDS = new DefaultDataSource();
		in.beginObject();
		
		while (in.hasNext()) {
			
			String name = in.nextName().toLowerCase();
			String value = in.nextString();
			
			if (name.equalsIgnoreCase("id")==true) {
				absDS.setId(NumberHelper.parseInteger(value));
			} else if (name.equalsIgnoreCase("name")==true) {
				absDS.setName(value);
			} else if (name.equalsIgnoreCase("description")==true) {
				absDS.setDescription(value);
			} else if (name.equalsIgnoreCase("rowsPerPage")==true) {
				absDS.setRowsPerPage(NumberHelper.parseInteger(value));
			} else if (name.equalsIgnoreCase("storageConfiguration")==true) {
				absDS.setStorageConfiguration(value);
			} else {
				in.skipValue();
			}
		}
		
		in.endObject();
		return absDS;
	}

	
//	private static final String TYPE_KEY = "type";
//	
//	/* (non-Javadoc)
//	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
//	 */
//	@Override
//	public DefaultDataSource deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//
//		JsonObject jObject = json.getAsJsonObject();
//		String type = jObject.get(TYPE_KEY).getAsString();
//		
//		DefaultDataSource ds = null;
//		if (type.toLowerCase().equals(CsvDataSource.class.getSimpleName().toLowerCase())==true) {
//			ds = context.deserialize(json, CsvDataSource.class);
//		} else if (type.toLowerCase().equals(ExcelDataSource.class.getSimpleName().toLowerCase())==true) {
//			ds = context.deserialize(json, ExcelDataSource.class);
//		} else if (type.toLowerCase().equals(DatabaseDataSource.class.getSimpleName().toLowerCase())==true) {
//			ds = context.deserialize(json, DatabaseDataSource.class);
//		}
//		return ds;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
//	 */
//	@Override
//	public JsonElement serialize(DefaultDataSource src, Type typeOfSrc, JsonSerializationContext context) {
//		JsonObject jObject = (JsonObject) context.serialize(src, src.getClass());
//		jObject.addProperty(TYPE_KEY, src.getClass().getSimpleName());
//		return jObject;
//	}

}
