package de.enflexit.awb.ws.credential;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class JwtTokenParser {

	public JwtToken parseJwtToken(String token) {
		String[] splitted = token.split(".");
		String header = null;
		String body = null;
		String cryptoSig = null;

		JsonElement headerJson = null;
		JsonElement bodyJson = null;

		if (splitted.length >= 3) {
			header = splitted[0];
			body = splitted[1];
			cryptoSig = splitted[2];

			header = Base64.getUrlDecoder().decode(header).toString();
			body = Base64.getUrlDecoder().decode(body).toString();

			headerJson = null;
			bodyJson = null;

			Map<String, String> headerMap = null;
			Map<String, String> bodyMap = null;
			try {
				headerJson = JsonParser.parseString(header);
				bodyJson = JsonParser.parseString(body);
				Gson gson = new Gson();
				Type type = new TypeToken<Map<String, String>>() {
				}.getType();

				headerMap = gson.fromJson(headerJson, type);
				bodyMap = gson.fromJson(bodyJson, type);
			} catch (JsonParseException e) {
				e.printStackTrace();
			}
			return new JwtToken(token, headerMap, bodyMap, cryptoSig);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
}
