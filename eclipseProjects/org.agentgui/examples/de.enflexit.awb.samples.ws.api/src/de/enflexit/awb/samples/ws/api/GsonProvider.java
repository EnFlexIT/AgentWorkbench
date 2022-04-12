package de.enflexit.awb.samples.ws.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * The Class GsonProvider can be used to serialize and deserialize a JSON 
 * String to and from a Java Object.
 * To use it, register this class in your {@link Application}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * @param <T> the generic type
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {

	private static final String PRETTY_PRINT = "pretty-print";

	private GsonBuilder gsonBuilder;
	private Gson gson;
	private Gson gsonPretty;

	@Context
	private UriInfo ui;

	
	/**
	 * Instantiates a new GsonProvider.
	 */
	public GsonProvider() { }

	/**
	 * Returns the local GsonBuilder.
	 * @return the gson builder
	 */
	private GsonBuilder getGsonBuilder() {
		if (gsonBuilder==null) {
			gsonBuilder = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization();
		}
		return gsonBuilder;
	}
	/**
	 * Return the local Gson.
	 * @return the gson
	 */
	private Gson getGson() {
		if (gson==null) {
			gson = this.getGsonBuilder().create();
		}
		return gson;
	}
	/**
	 * Gets the local pretty {@link Gson}.
	 * @return the pretty Gson
	 */
	private Gson getGsonPretty() {
		if (gsonPretty==null) {
			gsonPretty = this.getGsonBuilder().setPrettyPrinting().create();
		}
		return gsonPretty;
	}
	
	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
	 */
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.InputStream)
	 */
	@Override
	public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		
		InputStreamReader reader = new InputStreamReader(entityStream, "UTF-8");
		try {
			return this.getGson().fromJson(reader, type);
		} finally {
			reader.close();
		}
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
	 */
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
	 */
	@Override
	public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

		PrintWriter printWriter = new PrintWriter(entityStream);
		try {
			String json;
			if (ui.getQueryParameters().containsKey(PRETTY_PRINT)) {
				json = this.getGsonPretty().toJson(t);
			} else {
				json = this.getGson().toJson(t);
			}
			printWriter.write(json);
			printWriter.flush();
			
		} finally {
			printWriter.close();
		}
	}
	
}