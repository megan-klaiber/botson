package dev;

import java.util.List;

import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.IdentifiedLanguage;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;

public class LanguageTranslationController {
	private static LanguageTranslator service;
	
	/**
	 * Initialize the language translator service.
	 */
	public static void init() {
		service = new LanguageTranslator();
		service.setUsernameAndPassword(Messages.getString("LanguageTranslator.username"), Messages.getString("LanguageTranslator.password"));
		service.setEndPoint("https://gateway.watsonplatform.net/language-translator/api");
	}
	
	/**
	 * Translate a string with IBM Watson service.
	 * @param stringToTranslate
	 * 			String which will be translated
	 * @param sourceLanguage
	 * 			Language of the given stringToTranslate
	 * @param targetLanguage
	 * 			Target Language for translation
	 * @return
	 * 			stringToTranslate translated to targetLanguage
	 */
	public static TranslationResult translate(String stringToTranslate, Language sourceLanguage, Language targetLanguage) {
		return service.translate(stringToTranslate, sourceLanguage, targetLanguage).execute();
	}
	
	/**
	 * Translate a string with IBM Watson service, utilizing identifyLanguage().
	 * @param stringToTranslate
	 * 			String which will be translated
	 * @param targetLanguage
	 * 			Target Language for translation
	 * @return
	 * 			stringToTranslate translated to targetLanguage
	 */
	public static TranslationResult translate(String stringToTranslate, Language targetLanguage) {
		List<IdentifiedLanguage> possibleLanguages = identifyLanguage(stringToTranslate);
		
		System.out.println(possibleLanguages);
		System.out.println(possibleLanguages.get(0).getLanguage());
		System.out.println(Language.valueOf(possibleLanguages.get(0).getLanguage()));
		
		
//		return service.translate(stringToTranslate, possibleLanguages.get(0).getLanguage(), targetLanguage);
		return null;
	}
	
	/**
	 * Identify the language of a given string with IBM Watson service.
	 * @param stringToIdentifyLanguage
	 * 			The language of this string will be identified
	 * @return
	 * 			List of possible languages with their respective confidence value
	 */
	private static List<IdentifiedLanguage> identifyLanguage (String stringToIdentifyLanguage) {
		return service.identify(stringToIdentifyLanguage).execute();
	}
	
}
