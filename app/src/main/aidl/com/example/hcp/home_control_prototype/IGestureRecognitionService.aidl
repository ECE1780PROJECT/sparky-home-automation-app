package com.example.hcp.home_control_prototype;
import com.example.hcp.home_control_prototype.IGestureRecognitionListener;

interface IGestureRecognitionService {
    void startClassificationMode(String trainingSetName);
    
    void stopClassificationMode();
			
	void registerListener(IGestureRecognitionListener listener);
	
	void unregisterListener(IGestureRecognitionListener listener);
    
	void startLearnMode(String trainingSetName, String gestureName);
					
	void stopLearnMode();	
	
	void onPushToGesture(boolean pushed);
						
	void deleteTrainingSet(String trainingSetName);
	
	void deleteGesture(String trainingSetName, String gestureName);
	
	List<String> getGestureList(String trainingSet);
	
	boolean isLearning();
} 


