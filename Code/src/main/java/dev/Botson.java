package dev;

import org.telegram.telegrambots.api.methods.GetFile;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class Botson extends TelegramLongPollingBot {

	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(update.getMessage().getChatId())
					.setText(update.getMessage().getText());
			try {
				sendMessage(message); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

		if(update.getMessage().getVoice() != null) {
			GetFile getFile = new GetFile();
			getFile.setFileId(update.getMessage().getVoice().getFileId());
			try {
				String filepath = getFile(getFile).getFileUrl(Messages.getString("Botson.token"));
				System.out.println(filepath);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
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
