package dev;

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
