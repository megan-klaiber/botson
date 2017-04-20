package dev;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;


public class App {
    private static Logger log = LogManager.getLogger(App.class);

    public static void main( String[] args ) {
        // Initialize Api Context
        ApiContextInitializer.init();

        RecognizeOptions.Builder recognizeOptionsBuilder = new RecognizeOptions.Builder();
        recognizeOptionsBuilder.contentType(HttpMediaType.AUDIO_OGG);
        RecognizeOptions recognizeOptions =recognizeOptionsBuilder.build();
        SpeechToTextTest.init(recognizeOptions);

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register our bot
        try {
            botsApi.registerBot(new Botson());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    	
//        log.debug("With respect to logging you may want to configure file ");
//        log.debug("'src/main/resources/log4j2.xml' to suit your needs.");
//        log.debug("This config file 'log4j2.xml' will result in 'A1.log'");
//        log.debug("file containing logging output as well.");
    }
}
