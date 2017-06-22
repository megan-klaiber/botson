package dev;

import java.util.Comparator;

import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier.VisualClass;

public class SortByClassScore implements Comparator<VisualClass> {

	@Override
	public int compare(final VisualClass vc0, final VisualClass vc1) {
		return Double.compare(vc1.getScore(), vc0.getScore());
	}

}
