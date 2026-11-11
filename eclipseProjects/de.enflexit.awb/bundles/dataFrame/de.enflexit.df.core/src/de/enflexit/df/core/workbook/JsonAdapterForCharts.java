package de.enflexit.df.core.workbook;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.enflexit.charts.model.AbstractChart;
import de.enflexit.charts.model.BarChart;
import de.enflexit.charts.model.LineChart;
import de.enflexit.charts.model.PieChart;
import de.enflexit.charts.model.ScatterPlot;
import de.enflexit.charts.model.TimeSeriesChart;

/**
 * The Class JsonAdapterForCharts.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JsonAdapterForCharts implements JsonDeserializer<AbstractChart>, JsonSerializer<AbstractChart> {

	private static final String TYPE_KEY = "type";

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(AbstractChart src, Type typeOfSrc, JsonSerializationContext context) {

		JsonObject obj = (JsonObject) context.serialize(src, src.getClass());
		obj.addProperty(TYPE_KEY, src.getClass().getSimpleName().toLowerCase());
		return obj;
	}

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public AbstractChart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		JsonObject obj = json.getAsJsonObject();
        String type = obj.get(TYPE_KEY).getAsString();

        return switch (type) {
            case "barchart" -> context.deserialize(obj, BarChart.class);
            case "piechart" -> context.deserialize(obj, PieChart.class);
            case "scatterplot" -> context.deserialize(obj, ScatterPlot.class);
            case "linechart" -> context.deserialize(obj, LineChart.class);
            case "timeserieschart" -> context.deserialize(obj, TimeSeriesChart.class);
            default    -> throw new JsonParseException("Unbekannter Typ: " + type);
        };
		
	}

}
