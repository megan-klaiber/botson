package dev;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.Face;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier.VisualClass;

public class Botson extends TelegramLongPollingBot {

	@Override
	public void onUpdateReceived(Update update) {
		// check if message is a text message
		if (update.getMessage().hasText()) {
			if (update.getMessage().getText().startsWith("/translate")) {
				LanguageTranslationController.translate("Das Haus ist blau", Language.AFRIKAANS);
				
//				String translation = LanguageTranslationController.translate("The sea is blue. The rabbit is black.", Language.ENGLISH, Language.GERMAN).getFirstTranslation();
//				prepareAndSendMessage(update, "English to German:\nThe sea is blue. The rabbit is black.\n" + translation);
				
//				translation = LanguageTranslationController.translate("Hast du Tim gesehen? Ja, er ist bei Karl.", Language.GERMAN, Language.ENGLISH).getFirstTranslation();
//				prepareAndSendMessage(update, "German to English:\nHast du Tim gesehen? Ja, er ist bei Karl.\n" + translation);
				
//				KeyboardRow kbrow = new KeyboardRow();
//				kbrow.add(new KeyboardButton("English"));
//				kbrow.add(new KeyboardButton("German"));
				
//				List<KeyboardRow> kbrowlist = new ArrayList<>();
				
//				kbrowlist.add(kbrow);
				
//				SendMessage msg = new SendMessage()
//						.setReplyMarkup(new ReplyKeyboardMarkup().setKeyboard(kbrowlist))
//						.setChatId(update.getMessage().getChatId())
//						.setText("Dies ist ein Textitext :)");
				
//				try {
//					sendMessage(msg);
//				} catch (TelegramApiException e) {
//					e.printStackTrace();
//				}
			}
			
		}
		
		// check if message is a voice message
		else if(update.getMessage().getVoice() != null) {
			speechToText(update);
		} else if (update.getMessage().hasPhoto()){
			visualRecognition(update);
		}
	}
	
	/**
	 * Download a photo, upload it to Watson and send the results as chat message
	 * make a classification and a face detection
	 * @param update
	 * 			Telegram update object
	 */
	private void visualRecognition(Update update) {
		GetFile getFile = new GetFile();
		getFile.setFileId(update.getMessage().getPhoto().get(1).getFileId());
		
		try {
			String filepath = getFile(getFile).getFileUrl(Messages.getString("Botson.token"));
			File fetchedFile = fetchFile(filepath, Messages.getString("Botson.VisualFileDestination"));
			
			if(fetchedFile != null){
				// get the results of the classification 
				VisualClassification resultClassification = VisualRecognitionController.analyzeImage(fetchedFile);
				// get the classifiers
				List<VisualClassifier> classifiers = resultClassification.getImages().get(0).getClassifiers();
				// build the chat message with the results of the classification
				String classification = "Classification:";
				
				DecimalFormat f = new DecimalFormat("#0.0"); 
				
				for(VisualClassifier vc: classifiers){
					// get the classifier
					String classifierId = vc.getId();
					classification += "\n\n" + classifierId + " classifier:\n";
					
					// get the classes with the names and scores
					List<VisualClass> classes = vc.getClasses();	
					for(VisualClass vcl: classes){
						String c = vcl.getName();
						String s = f.format(vcl.getScore() * 100);
						
						classification += "\n" + s + "% " + c;
					}
				}
				prepareAndSendMessage(update, classification);
				
				// get the result of the face detection
				DetectedFaces resultDetection = VisualRecognitionController.faceDetection(fetchedFile);
				// get the faces
				List<Face> faces = resultDetection.getImages().get(0).getFaces();
				
				// check if there are detected faces
				if(!faces.isEmpty()){
					// build the chat message with the results of the face detection
					String detection = "Face Detection:";
					
					// get the information about the faces
					for(Face face: faces){
						Integer maxAge = face.getAge().getMax();
						Integer minAge = face.getAge().getMin();
						String scoreAge = f.format(face.getAge().getScore() * 100);
						
						String gender = face.getGender().getGender().toLowerCase();
						String scoreGender = f.format(face.getGender().getScore() * 100);
						
						detection += "\n\n" + scoreGender + "% " + gender
								+ "\nAge (" + scoreAge + "%):\n   max: " + maxAge
								+ "\n   min: " + minAge;
					}
					prepareAndSendMessage(update, detection);
				}
			} else {
				System.err.println("Something went wrong!");
			}
			
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Download a voice message, upload it to Watson and send the results as chat message
	 * @param update
	 * 			Telegram update object
	 */
	private void speechToText(Update update){
		GetFile getFile = new GetFile();
		getFile.setFileId(update.getMessage().getVoice().getFileId());
		try {
			String filepath = getFile(getFile).getFileUrl(Messages.getString("Botson.token"));
			File fetchedFile = fetchFile(filepath, Messages.getString("Botson.SpeechFileDestination"));
			if(fetchedFile != null){
				// get the results of the analysis
				SpeechResults result = SpeechToTextController.analyzeFile(fetchedFile);
				// get the username
				String username = update.getMessage().getFrom().getFirstName();
				// check if the result is empty
				// if not get the text of the voice message
				if(!result.getResults().isEmpty()){
					String text = result.getResults().get(0).getAlternatives().get(0).getTranscript();
					prepareAndSendMessage(update, username + ":\n" + text);
				} else {
					String text = "Sorry " + username + ", I couldn't understand your voice message!";
					prepareAndSendMessage(update, text);
				}
			} else {
				System.err.println("Something went wrong!");
			}
			
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get message information and send message.
	 * @param update
	 * 			update event from telegram api
	 * @param text 
	 * 			text which should be printed
	 */
	private void prepareAndSendMessage(Update update, String text){
		
		try{	
			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(update.getMessage().getChatId())
					.setText(text);
			
			sendMessage(message); // Call method to send the message
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Saves file from filepath to disk. 
	 * Fetches file from disk and returns it as a file object.
	 * 
	 * @param filepath 
	 * 			url to file
	 * @param destination
	 * 			destination path
	 * @return file object
	 */
	private File fetchFile(String filepath, String destination){
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet httpget = new HttpGet(filepath);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
			    InputStream inputStream = entity.getContent();
			    // path where the file will be saved
			    String fileDestination = destination;
			    // save the file on disk
			    File file = new File(fileDestination);
			    // fetch file
			    FileUtils.copyInputStreamToFile(inputStream, file);
			    return file;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;	
	}

	@Override
	public String getBotUsername() {
		return Messages.getString("Botson.username"); 
	}

	@Override
	public String getBotToken() {
		return Messages.getString("Botson.token");
	}

}
