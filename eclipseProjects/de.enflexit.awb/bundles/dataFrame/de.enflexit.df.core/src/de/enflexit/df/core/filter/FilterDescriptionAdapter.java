package de.enflexit.df.core.filter;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class FilterDescriptionAdapter implements JsonSerializer<AbstractFilterDescription>, JsonDeserializer<AbstractFilterDescription>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(AbstractFilterDescription src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = context.serialize(src, src.getClass()).getAsJsonObject();
		
		// --- Add discriminator ----------------
		if (src instanceof SimpleFilterDescription) {
			json.addProperty("type", "SIMPLE");
		} else if (src instanceof GroupFilterDescription) {
			json.addProperty("type", "GROUP");
		} else {
			throw new JsonParseException("Unknown filter type: " + src.getClass().getName());
		}
		return json;
	}
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public AbstractFilterDescription deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();
		JsonElement typeElement = object.get("type");

		if (typeElement == null) {
			throw new JsonParseException("Filtertyp 'type' fehlt");
		}

		String filterType = typeElement.getAsString();

		switch (filterType) {
		case "SIMPLE":
			return context.deserialize(object, SimpleFilterDescription.class);

		case "GROUP":
			return context.deserialize(object, GroupFilterDescription.class);

		default:
			throw new JsonParseException("Unbekannter Filtertyp: " + filterType);
		}
	}


}
