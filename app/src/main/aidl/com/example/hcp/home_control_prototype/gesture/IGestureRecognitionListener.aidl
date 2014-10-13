package com.example.hcp.home_control_prototype.gesture;
import com.example.hcp.home_control_prototype.gesture.classifier.Distribution;

interface IGestureRecognitionListener {
	 void onGestureRecognized(in Distribution distribution);

	 void onGestureLearned(String gestureName);

	 void onTrainingSetDeleted(String trainingSet);

} 


