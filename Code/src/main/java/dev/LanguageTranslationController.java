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
		service.setUsernameAndPassword(Messages.getString("LanguageTranslator.username"),
				Messages.getString("LanguageTranslator.password"));
		service.setEndPoint("https://gateway.watsonplatform.net/language-translator/api");
	}

	/**
	 * Translate a string with IBM Watson service.
	 * 
	 * @param stringToTranslate
	 *            String which will be translated
	 * @param sourceLanguage
	 *            Language of the given stringToTranslate
	 * @param targetLanguage
	 *            Target Language for translation
	 * @return stringToTranslate translated to targetLanguage
	 */
	public static TranslationResult translate(String stringToTranslate, Language sourceLanguage,
			Language targetLanguage) {
		return service.translate(stringToTranslate, sourceLanguage, targetLanguage).execute();
	}

	/**
	 * Translate a string with IBM Watson service, utilizing identifyLanguage().
	 * 
	 * @param stringToTranslate
	 *            String which will be translated
	 * @param targetLanguage
	 *            Target Language for translation
	 * @return stringToTranslate translated to targetLanguage
	 */
	public static TranslationResult translate(String stringToTranslate, Language targetLanguage) {
		// identify the source language
		List<IdentifiedLanguage> possibleLanguages = identifyLanguage(stringToTranslate);
		// get the language with the highest score 
		Language sourceLanguage = Language.valueOf(getEnumByString(possibleLanguages.get(0).getLanguage()));
		// translate the given String
		return service.translate(stringToTranslate, sourceLanguage, targetLanguage).execute();
	}

	/**
	 * Get the name of the language based on the country code
	 * @param code
	 * 			country code
	 * @return name of the language
	 */
	public static String getEnumByString(String code) {
		for (Language l : Language.values()) {
			if (code.equals(l.toString())){
				return l.name();
			}
		}
		return null;
	}

	/**
	 * Identify the language of a given string with IBM Watson service.
	 * 
	 * @param stringToIdentifyLanguage
	 *            The language of this string will be identified
	 * @return List of possible languages with their respective confidence value
	 */
	private static List<IdentifiedLanguage> identifyLanguage(String stringToIdentifyLanguage) {
		return service.identify(stringToIdentifyLanguage).execute();
	}

}
