package dev;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

import java.io.File;

public class SpeechToTextTest {

    private static SpeechToText service;
    private static RecognizeOptions recognizeOptions;

    public static void init(RecognizeOptions options) {
        service = new SpeechToText();
        service.setUsernameAndPassword(Messages.getString("SpeechToText.username"), Messages.getString("SpeechToText.password"));
        recognizeOptions = options;
    }

    public static SpeechResults analyzeFile(String filepath) {
        File audio = new File(filepath);
        SpeechResults transcript = service.recognize(audio, recognizeOptions).execute();
        System.out.println(transcript);
        return transcript;
    }
}
