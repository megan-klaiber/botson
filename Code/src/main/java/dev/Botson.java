package dev;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class Botson extends TelegramLongPollingBot {

	@Override
	public void onUpdateReceived(Update update) {
		// check if message is a text message
		if (update.getMessage().hasText()) {
			
		}
		
		// check if message is a voice message
		else if(update.getMessage().getVoice() != null) {
			speechToText(update);
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
			//System.out.println(filepath);
			
			File fetchedFile = fetchFile(filepath);
			if(fetchedFile != null){
				SpeechResults result = SpeechToTextController.analyzeFile(fetchedFile);
				String username = update.getMessage().getFrom().getFirstName();
				String text = result.getResults().get(0).getAlternatives().get(0).getTranscript();
				
				prepareAndSendMessage(update, username + ":\n" + text);

				//System.out.println(username + ": " + text);
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
	 * @return file object
	 */
	private File fetchFile(String filepath){
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet httpget = new HttpGet(filepath);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
			    InputStream inputStream = entity.getContent();
			    // path where the file will be saved
			    String fileDestination = Messages.getString("Botson.fileDestination");
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
