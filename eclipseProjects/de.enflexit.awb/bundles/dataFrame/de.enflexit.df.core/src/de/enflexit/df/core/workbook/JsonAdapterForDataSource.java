package de.enflexit.df.core.workbook;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.enflexit.common.NumberHelper;
import de.enflexit.df.core.dataSources.DefaultDataSource;

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

		out.name("dataSourceSubConfigurations");
		out.beginArray();
		for (String subConfig : absDS.getDataSourceSubConfigurations()) {
			out.value(subConfig);
		}
		out.endArray();

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

			if (name.equalsIgnoreCase("id") == true) {
				absDS.setId(NumberHelper.parseInteger(value));
			} else if (name.equalsIgnoreCase("name") == true) {
				absDS.setName(value);
			} else if (name.equalsIgnoreCase("description") == true) {
				absDS.setDescription(value);
			} else if (name.equalsIgnoreCase("rowsPerPage") == true) {
				absDS.setRowsPerPage(NumberHelper.parseInteger(value));
			} else if (name.equalsIgnoreCase("storageConfiguration") == true) {
				absDS.setStorageConfiguration(value);
			} else if (name.equalsIgnoreCase("dataSourceSubConfigurations") == true) {
				if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
					in.nextNull();
				} else {
					in.beginArray();
					while (in.hasNext()) {
						absDS.getDataSourceSubConfigurations().add(in.nextString());
					}
					in.endArray();
				}
			} else {
				in.skipValue();
			}
		}

		in.endObject();
		return absDS;
	}

}
