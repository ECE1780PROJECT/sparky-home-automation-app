package com.example.hcp.home_control_prototype;
import com.example.hcp.home_control_prototype.Distribution;

interface IGestureRecognitionListener {
	 void onGestureRecognized(in Distribution distribution);

	 void onGestureLearned(String gestureName);

	 void onTrainingSetDeleted(String trainingSet);
} 


