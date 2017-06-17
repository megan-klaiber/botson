package dev;

import java.io.File;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualRecognitionOptions;

public class VisualRecognitionController {
	
	private static VisualRecognition service;
	
	/**
     * Initialize the visual recognition service.
     */
	public static void init() {
		service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey(Messages.getString("VisualRecognition.key"));
    }

	/**
	 * Analyze an image with IBM Watson service.
	 * @param photo
	 * 			image which should be analyzed
	 * @return the results of the analysis
	 */	
	public static VisualClassification analyzeImage(File photo){
		ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
			    .images(photo)
			    .build();
		
		VisualClassification result = service.classify(options).execute();
		System.out.println(result);
		return result;
	}
	
	/**
	 * Detect Faces on an image with IBM Watson service.
	 * @param photo
	 * 			image where faces should be detect
	 * @return the results of the face detection
	 */
	public static DetectedFaces faceDetection(File photo){	
		VisualRecognitionOptions options = new VisualRecognitionOptions.Builder()
		        .images(photo)
		        .build();
		
		DetectedFaces result = service.detectFaces(options).execute();
		System.out.println(result);
		return result;
	}
}
