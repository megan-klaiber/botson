package dev;

import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.json.JSONObject;

public class Messages {
	private static final String BUNDLE_NAME = "messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private static boolean propFile = false;
	private static Map<String, String> properties = new TreeMap<String, String>();
	
	
	private Messages() {
	}
	
	public static void init() {
		String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "messages.properties";
		File prop = new File(path);
		
		if (prop.exists() && !prop.isDirectory()) {
			propFile = true;
		}
		else {
			properties.put("Botson.token", System.getenv("Botson.token"));
			properties.put("Botson.username", System.getenv("Botson.username"));
			properties.put("Botson.fileDestination", System.getenv("Botson.fileDestination"));
			
			String envVariables = System.getenv("VCAP_SERVICES");
			
			JSONObject envJson = new JSONObject(envVariables);
			
			JSONObject speechToTextCreds = envJson.getJSONArray("speech_to_text").getJSONObject(0).getJSONObject("credentials");
			properties.put("SpeechToText.username", speechToTextCreds.getString("username"));
			properties.put("SpeechToText.password", speechToTextCreds.getString("password"));
			
			JSONObject LangTransCreds = envJson.getJSONArray("language_translator").getJSONObject(0).getJSONObject("credentials");
			properties.put("LanguageTranslator.username", LangTransCreds.getString("username"));
			properties.put("LanguageTranslator.password", LangTransCreds.getString("password"));
			
			JSONObject VisRecCreds = envJson.getJSONArray("watson_vision_combined").getJSONObject(0).getJSONObject("credentials");
			properties.put("VisualRecognition.key", VisRecCreds.getString("api_key"));
						
		}
	}
	
	public static String getString(String key) {
			if (propFile) {
				return RESOURCE_BUNDLE.getString(key);
			} else {
				return properties.get(key);
			}
	}	
}
