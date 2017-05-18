package dev;

import java.io.File;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class SpeechToTextController {

    private static SpeechToText service;
    private static RecognizeOptions recognizeOptions;

    /**
     * Initialize the speech to text service.
     * @param options
     * 			settings for speech to text request
     */
    public static void init(RecognizeOptions options) {
        service = new SpeechToText();
        service.setUsernameAndPassword(Messages.getString("SpeechToText.username"), Messages.getString("SpeechToText.password"));
        service.setEndPoint("https://stream.watsonplatform.net/speech-to-text/api");
        recognizeOptions = options;
    }

    /**
     * Analyze an audio file with IBM Watson service.
     * @param audio
     * 			audio file which should be analyzed
     * @return the results of the analysis
     */
    public static SpeechResults analyzeFile(File audio) {      
        SpeechResults transcript = service.recognize(audio, recognizeOptions).execute();
        System.out.println(transcript);
        return transcript;
    }
}
